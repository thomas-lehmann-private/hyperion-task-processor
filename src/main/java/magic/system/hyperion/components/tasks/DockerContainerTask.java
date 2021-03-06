/*
 * The MIT License
 *
 * Copyright 2021 Thomas Lehmann.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package magic.system.hyperion.components.tasks;

import magic.system.hyperion.exceptions.HyperionException;
import magic.system.hyperion.tools.Capabilities;
import magic.system.hyperion.tools.FileExtensions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Running a docker container.
 *
 * @author Thomas Lehmann
 */
public class DockerContainerTask extends AbstractShellTask {
    /**
     * Platform Windows.
     */
    public static final String PLATFORM_WINDOWS = "windows";

    /**
     * Platform Unix.
     */
    public static final String PLATFORM_UNIX = "unix";

    /**
     * Readonly file extensions to use depending on target environment in Docker container.
     */
    public static final Map<String, String> FILE_EXTENSIONS = Map.of(
            PLATFORM_WINDOWS, FileExtensions.CMD.getValue(),
            PLATFORM_UNIX, FileExtensions.SH.getValue());

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DockerContainerTask.class);

    /**
     * Name of the Docker image.
     */
    private String strImageName;

    /**
     * Version of the Docker image.
     */
    private String strImageVersion;

    /**
     * When true the cmd shell is used, otherwise the sh shell (default: sh shell).
     * Can also be used later on together with --platform (not yet implemented).
     */
    private String strPlatform;

    /**
     * When true then using -d to run Container in background (default: false).
     */
    private boolean bDetached;

    /**
     * Initialize task.
     *
     * @param strInitTitle - title of the task.
     * @param strInitCode  - Path and name of file of script or inline script.
     * @since 1.0.0
     */
    public DockerContainerTask(final String strInitTitle, final String strInitCode) {
        super(strInitTitle, strInitCode);
        this.strImageName = "";
        this.strImageVersion = "latest";
        this.strPlatform = PLATFORM_UNIX;
        this.bDetached = false;
    }

    /**
     * Change image name.
     *
     * @param strInitImageName new image name.
     * @since 1.0.0
     */
    public void setImageName(final String strInitImageName) {
        this.strImageName = strInitImageName;
    }

    /**
     * Get image name.
     *
     * @return image name.
     * @since 1.0.0
     */
    public String getImageName() {
        return this.strImageName;
    }

    /**
     * Change version of image.
     *
     * @param strInitImageVersion new version of image.
     * @since 1.0.0
     */
    public void setImageVersion(final String strInitImageVersion) {
        this.strImageVersion = strInitImageVersion;
    }

    /**
     * Get image version.
     *
     * @return image version.
     * @since 1.0.0
     */
    public String getImageVersion() {
        return this.strImageVersion;
    }

    /**
     * Change target environment (platform) inside of the Docker container.
     *
     * @param strInitPlatform new target environment (platform).
     * @since 1.0.0
     */
    public void setPlatform(final String strInitPlatform) {
        if (List.of(PLATFORM_WINDOWS, PLATFORM_UNIX).contains(strInitPlatform)) {
            this.strPlatform = strInitPlatform;
        }
    }

    /**
     * Check target environment (platform) inside of the Docker container.
     *
     * @return true when Docker container runs Windows platform (Windows container).
     * @since 1.0.0
     */
    public String getPlatform() {
        return this.strPlatform;
    }

    /**
     * Change detached mode.
     * @param bInitDetached new value.
     * @since 2.0.0
     */
    public void setDetached(final boolean bInitDetached) {
        this.bDetached = bInitDetached;
    }

    /**
     * Get run mode.
     *
     * @return when true then docker container will run detached (in background).
     * @since 2.0.0
     */
    public boolean isDetached() {
        return this.bDetached;
    }

    @Override
    protected String getTempFilePrefix() {
        return "hyperion-docker-container-task-";
    }

    @Override
    protected List<String> getFileExtensions() {
        return List.of(FILE_EXTENSIONS.get(this.strPlatform));
    }

    @Override
    protected boolean isTempFileRelativePath() {
        return false;
    }

    @SuppressWarnings("checkstyle:multiplestringliterals")
    @Override
    protected Process runFile(final Path path) throws IOException, HyperionException {
        final Path parentPath = path.getParent();
        final Path fileName = path.getFileName();

        if (parentPath == null || fileName == null) {
            throw new HyperionException("Path or file name of Docker container script invalid!");
        }

        // specifying how to call docker on current environment
        // --rm      automatic remove the container when the process has finished.
        // -v a:b    mount host path <a> onto Docker container path <b>
        // -i        keep STDIN open even if not attached
        // -d        run detached (in background)
        final var baseCommand = List.of("docker", "run", "--rm", "-v",
                System.getProperty("user.dir") + ":/work",
                "-v", parentPath.toString() + ":/hosttmp",
                this.bDetached ? "-d": "-i",
                this.strImageName + ":" + this.strImageVersion);

        final var strCommand = String.join(" ", Stream.of(baseCommand,
                this.strPlatform.equals(PLATFORM_WINDOWS)
                        ? List.of("cmd", "/C") : List.of("sh", "-c"),
                List.of("/hosttmp/" + fileName.toString()))
                .flatMap(Collection::stream).collect(Collectors.toList()));

        // inject the shell to be used inside of the container and append the script too
        final var finalCommand = Capabilities.createCommand(strCommand);

        LOGGER.info("Running command: {}", String.join(" ", finalCommand));
        return new ProcessBuilder(finalCommand).start();
    }

    @Override
    public AbstractTask copy() {
        final var task = new DockerContainerTask(getTitle(), getCode());
        task.setImageName(getImageName());
        task.setImageVersion(getImageVersion());
        task.setPlatform(getPlatform());
        return task;
    }
}

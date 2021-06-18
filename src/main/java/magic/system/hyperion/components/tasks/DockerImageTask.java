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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Task for creating a Docker image.
 *
 * @author Thomas Lehmann
 */
public class DockerImageTask extends AbstractShellTask {
    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DockerImageTask.class);

    /**
     * Delimiter for joining parts of a command.
     */
    private static final String DELIMITER = " ";

    /**
     * Repository and version for image. The form a:b means "a" is the repository and "b" is the
     * tag (version); you see with "docker images".
     */
    private String strRepositoryTag;

    /**
     * Initialize task.
     *
     * @param strInitTitle - title of the task.
     * @param strInitCode  - Path and name of file of script or inline script.
     */
    public DockerImageTask(String strInitTitle, String strInitCode) {
        super(strInitTitle, strInitCode);
        this.strRepositoryTag = "";
    }

    /**
     * Change repository:tag.
     *
     * @param strInitRepositoryTag new value for repository:tag.
     * @since 1.0.0
     */
    public void setRepositoryTag(final String strInitRepositoryTag) {
        this.strRepositoryTag = strInitRepositoryTag;
    }

    /**
     * Get repository and tag.
     *
     * @return repository and tag.
     * @since 1.0.0
     */
    public String getRepositoryTag() {
        return this.strRepositoryTag;
    }

    @Override
    protected String getTempFilePrefix() {
        return "hyperion-docker-image-task-";
    }

    @Override
    protected List<String> getFileExtensions() {
        return List.of();
    }

    @Override
    protected boolean isTempFileRelativePath() {
        // Required by "docker build"
        return true;
    }

    @Override
    protected Process runFile(Path path) throws IOException, HyperionException {
        if (this.strRepositoryTag.trim().isEmpty()) {
            throw new HyperionException("Image tag not specified!");
        }

        // how to call docker on current environment
        final var baseCommand = List.of(
                "docker", "build", "-t", this.strRepositoryTag, "-f", path.toString(), ".");
        final var strCommand = String.join(DELIMITER, baseCommand);
        // inject the shell to be used inside of the container and append the script too
        final var finalCommand = Capabilities.createCommand(strCommand);
        LOGGER.info("Running command: {}", String.join(DELIMITER, finalCommand));
        return new ProcessBuilder(finalCommand).start();
    }

    @Override
    public AbstractTask copy() {
        final var task = new DockerImageTask(getTitle(), getCode());
        task.setRepositoryTag(getRepositoryTag());
        return task;
    }
}

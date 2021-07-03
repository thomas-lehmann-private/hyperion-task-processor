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

import magic.system.hyperion.components.TaskParameters;
import magic.system.hyperion.components.TaskResult;
import magic.system.hyperion.exceptions.HyperionException;
import magic.system.hyperion.interfaces.ISimpleRunnable;
import magic.system.hyperion.tools.FileUtils;
import magic.system.hyperion.tools.ProcessResults;
import magic.system.hyperion.tools.TemplateEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Base class for shell tasks.
 *
 * @author Thomas Lehmann
 */
public abstract class AbstractShellTask extends AbstractCodableTask {
    /**
     * Logger of this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractShellTask.class);

    /**
     * Newline character.
     */
    private static final String NEWLINE = "\n";

    /**
     * Initialize task.
     *
     * @param strInitTitle - title of the task.
     * @param strInitCode  - Path and name of file of script or inline script.
     * @since 1.0.0
     */
    public AbstractShellTask(String strInitTitle, String strInitCode) {
        super(strInitTitle, strInitCode);
    }

    @Override
    public boolean isRegularFile() {
        final String[] tokens = getCode().split("\\.");
        return super.isRegularFile() && (getFileExtensions().isEmpty()
                || tokens.length > 0 && getFileExtensions().contains(tokens[tokens.length - 1]));
    }

    @Override
    public TaskResult run(final TaskParameters parameters) {
        TaskResult taskResult;
        ISimpleRunnable cleanup = () -> {
        };

        logTitle(parameters);

        final var engine = new TemplateEngine();
        var strContent = getCode();

        try {
            if (isRegularFile()) {
                LOGGER.info("Processing file {}", getCode());
                strContent = Files.readString(Paths.get(getCode()));
            }

            final var temporaryScriptPath = createTemporaryFile();
            cleanup = () -> FileUtils.deletePath(temporaryScriptPath);

            final var renderedText = engine.render(
                    strContent, parameters.getTemplatingContext());

            Files.write(temporaryScriptPath, renderedText.getBytes(
                    Charset.defaultCharset()));

            LOGGER.info("Running script {}", temporaryScriptPath);
            final var process = runFile(temporaryScriptPath);
            final var processResults = ProcessResults.of(process);
            this.getVariable().setValue(String.join(NEWLINE, processResults.getStdout()));
            taskResult = new TaskResult(processResults.getExitCode() == 0,
                    getVariable());
        } catch (IOException | InterruptedException | HyperionException e) {
            taskResult = new TaskResult(false, this.getVariable());
        } finally {
            cleanup.run();
        }

        return taskResult;
    }

    /**
     * Providing temporary file.
     *
     * @return temporary script path.
     * @throws IOException       when creation of temporary file has failed.
     * @throws HyperionException when filename is null (should never happen)
     * @since 1.0.0
     */
    private Path createTemporaryFile() throws IOException, HyperionException {
        final var strPostFix
                = getFileExtensions().isEmpty() ? "" : "." + getFileExtensions().get(0);
        final var temporaryScriptPath = FileUtils.createTemporaryFile(
                getTempFilePrefix(), strPostFix);

        var finalTemporaryScriptPath = temporaryScriptPath;

        if (isTempFileRelativePath()) {
            final var path = temporaryScriptPath.getFileName();
            if (path == null) {
                throw new HyperionException(
                        "Failed to get file name for " + temporaryScriptPath.toString());
            }

            final var currentPath = Paths.get(System.getProperty("user.dir"));
            final var temporaryRelativeScriptPath = Paths.get(
                    currentPath.toString(), path.toString());
            Files.move(temporaryScriptPath, temporaryRelativeScriptPath);
            finalTemporaryScriptPath = temporaryRelativeScriptPath;
        }

        return finalTemporaryScriptPath;
    }

    /**
     * Provide prefix for tempfile for shell script.
     *
     * @return Provide prefix for tempfile for shell script.
     * @since 1.0.0
     */
    protected abstract String getTempFilePrefix();

    /**
     * Defines valid file extensions. When the list is empty it is assumed that
     * no extension is required.
     *
     * @return list of valid file extensions or an empty list.
     * @since 1.0.0
     */
    protected abstract List<String> getFileExtensions();

    /**
     * Provide whether path of temporary file is relative to current path.
     *
     * @return when true then path of temporary file is relative to current path.
     * @since 1.0.0
     */
    protected abstract boolean isTempFileRelativePath();

    /**
     * Does execute the concrete shell script.
     *
     * @param path path to the shell script.
     * @return the process for the execution.
     * @throws IOException       when file execution failed.
     * @throws HyperionException when an application error occurs.
     * @since 1.0.0
     */
    protected abstract Process runFile(Path path) throws IOException, HyperionException;
}

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
import java.util.Map;
import java.util.UUID;

/**
 * Providing Unix shell task.
 *
 * @author Thomas Lehmann
 */
public class UnixShellTask extends AbstractTask {
    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UnixShellTask.class);

    /**
     * Newline character.
     */
    private static final String NEWLINE = "\n";

    /**
     * Valid Shell file extensions.
     */
    private static final List<String> FILE_EXTENSIONS = List.of("sh");

    /**
     * Initialize task.
     *
     * @param strInitTitle - title of the task.
     * @param strInitCode  - Path and name of file of script or inline script.
     */
    public UnixShellTask(String strInitTitle, String strInitCode) {
        super(strInitTitle, strInitCode);
    }

    @Override
    public boolean isRegularFile() {
        final String[] tokens = getCode().split("\\.");
        return super.isRegularFile() && tokens.length > 0
                && FILE_EXTENSIONS.contains(tokens[tokens.length-1]);
    }

    @Override
    public TaskResult run(final TaskParameters parameters) {
        TaskResult taskResult;

        try {
            if (isRegularFile()) {
                LOGGER.info("Executing script from path in code {}", getCode());
                final var process = runFile(Paths.get(getCode()));
                process.waitFor();
                final var processResults = ProcessResults.of(process);
                this.getVariable().setValue(String.join(NEWLINE,
                        processResults.getStdout()));
                taskResult = new TaskResult(processResults.getExitCode() == 0,
                        getVariable());
            } else {
                final var temporaryScriptPath = Files.createTempFile(
                        "hyperion-unix-shell-task-",
                        UUID.randomUUID() + FILE_EXTENSIONS.get(0));

                final var engine = new TemplateEngine();
                final var renderedText = engine.render(getCode(),
                        Map.of("model", parameters.getModel().getData(),
                                "matrix", parameters.getMatrixParameters(),
                                "variables", parameters.getVariables()));

                Files.write(temporaryScriptPath, renderedText.getBytes(
                        Charset.defaultCharset()));

                LOGGER.info("Executing embedded script {}", temporaryScriptPath.toString());
                final var process = runFile(temporaryScriptPath);
                process.waitFor();
                final var processResults = ProcessResults.of(process);
                Files.delete(temporaryScriptPath);
                this.getVariable().setValue(String.join(NEWLINE,
                        processResults.getStdout()));
                processResults.getStdout().forEach(LOGGER::info);
                taskResult = new TaskResult(processResults.getExitCode() == 0,
                        getVariable());
            }
        } catch (IOException | InterruptedException e) {
            taskResult = new TaskResult(false, this.getVariable());
        }

        return taskResult;
    }

    /**
     * Running Unix shell and providing process.
     *
     * @param path the path and name of the Batch file
     * @return process.
     * @throws IOException when starting of the process has failed.
     */
    private static Process runFile(final Path path) throws IOException {
        return new ProcessBuilder(List.of("/bin/sh", path.toString())
                .toArray(String[]::new)).start();
    }
}

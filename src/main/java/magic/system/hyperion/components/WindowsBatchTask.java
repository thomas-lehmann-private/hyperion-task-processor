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
package magic.system.hyperion.components;

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
 * Providing Windows Batch task.
 *
 * @author Thomas Lehmann
 */
public class WindowsBatchTask extends AbstractTask {
    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowsBatchTask.class);

    /**
     * Newline character.
     */
    private static final String NEWLINE = "\n";

    /**
     * Valid Batch file extensions.
     */
    private static final List<String> FILE_EXTENSIONS = List.of(".cmd", ".bat");

    /**
     * Initialize task.
     *
     * @param strInitTitle - title of the task.
     * @param strInitCode  - Path and name of file of script or inline script.
     */
    public WindowsBatchTask(String strInitTitle, String strInitCode) {
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
                final var process = runFile(Paths.get(getCode()));
                final var processResults = ProcessResults.of(process);
                this.getVariable().setValue(String.join(NEWLINE,
                        processResults.getStdout()));
                taskResult = new TaskResult(processResults.getExitCode() == 0,
                        getVariable());
            } else {
                final var temporaryScriptPath = Files.createTempFile(
                        "spline-windows-batch-task-",
                        UUID.randomUUID() + FILE_EXTENSIONS.get(0));

                final var engine = new TemplateEngine();
                final var renderedText = engine.render(getCode(),
                        Map.of("model", parameters.getModel().getData(),
                                "variables", parameters.getVariables()));

                Files.write(temporaryScriptPath, renderedText.getBytes(
                        Charset.defaultCharset()));

                final var process = runFile(temporaryScriptPath);
                final var processResults = ProcessResults.of(process);
                Files.delete(temporaryScriptPath);
                this.getVariable().setValue(String.join(NEWLINE,
                        processResults.getStdout()));
                processResults.getStdout().forEach(LOGGER::info);
                taskResult = new TaskResult(processResults.getExitCode() == 0,
                        getVariable());
            }
        } catch (IOException e) {
            taskResult = new TaskResult(false, this.getVariable());
        }

        return taskResult;
    }

    /**
     * Running Batch and providing process.
     *
     * @param path the path and name of the Batch file
     * @return process.
     * @throws IOException when starting of the process has failed.
     */
    private static Process runFile(final Path path) throws IOException {
        return new ProcessBuilder(List.of("cmd", "/q", "/c",
                path.toString()).toArray(String[]::new)).start();
    }
}

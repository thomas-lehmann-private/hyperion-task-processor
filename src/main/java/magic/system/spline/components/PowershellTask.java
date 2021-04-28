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
package magic.system.spline.components;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Map;
import java.util.UUID;
import magic.system.spline.interfaces.IVariable;
import magic.system.spline.tools.ProcessResults;
import magic.system.spline.tools.TemplateEngine;

/**
 * Task for running a powershell script.
 *
 * @author Thomas Lehmann
 */
public class PowershellTask extends AbstractTask {

    /**
     * Newline character.
     */
    private static final String NEWLINE = "\n";

    /**
     * Powershell command for executing a script file.
     */
    private static final String POWERSHELL_COMMAND = "powershell -File ";

    /**
     * Powershell file extension.
     */
    private static final String FILE_EXTENSION = ".ps1";

    /**
     * Initialize Powershell task.
     */
    public PowershellTask() {
        super("", "");
    }

    /**
     * Initialize Powershell task.
     *
     * @param strInitTitle - title of the task.
     * @param strInitCode - Path and name of file of script or inline script.
     */
    public PowershellTask(final String strInitTitle, final String strInitCode) {
        super(strInitTitle, strInitCode);
    }

    @Override
    public boolean isRegularFile() {
        return super.isRegularFile() && getCode().endsWith(FILE_EXTENSION);
    }

    @Override
    public TaskResult run(final Map<String, IVariable> variables) {
        TaskResult taskResult;

        try {
            if (isRegularFile()) {
                final var strCommand = POWERSHELL_COMMAND + getCode();
                final var process = Runtime.getRuntime().exec(strCommand);
                final var processResults = ProcessResults.of(process);
                this.getVariable().setValue(String.join(NEWLINE,
                        processResults.getStdout()));
                taskResult = new TaskResult(processResults.getExitCode() == 0,
                        getVariable());
            } else {
                final var temporaryScriptPath = Files.createTempFile(
                        "spline-powershell-task-",
                        UUID.randomUUID().toString() + FILE_EXTENSION);

                final var engine = new TemplateEngine();
                final var renderedText = engine.render(getCode(),
                        Map.of("variables", variables));

                Files.write(temporaryScriptPath, renderedText.getBytes(
                        Charset.defaultCharset()));

                final var strCommand = POWERSHELL_COMMAND + temporaryScriptPath.toString();
                final var process = Runtime.getRuntime().exec(strCommand);
                final var processResults = ProcessResults.of(process);
                Files.delete(temporaryScriptPath);
                this.getVariable().setValue(String.join(NEWLINE,
                        processResults.getStdout()));
                taskResult = new TaskResult(processResults.getExitCode() == 0,
                        getVariable());
            }
        } catch (IOException e) {
            taskResult = new TaskResult(false, this.getVariable());
        }

        return taskResult;
    }
}

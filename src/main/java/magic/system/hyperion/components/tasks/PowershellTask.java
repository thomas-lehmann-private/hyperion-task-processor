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

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Task for running a powershell script.
 *
 * @author Thomas Lehmann
 */
public class PowershellTask extends AbstractShellTask {
    /**
     * Initialize Powershell task.
     * @since 1.0.0
     */
    public PowershellTask() {
        super("", "");
    }

    /**
     * Initialize Powershell task.
     *
     * @param strInitTitle - title of the task.
     * @param strInitCode  - Path and name of file of script or inline script.
     * @since 1.0.0
     */
    public PowershellTask(final String strInitTitle, final String strInitCode) {
        super(strInitTitle, strInitCode);
    }

    @Override
    protected String getTempFilePrefix() {
        return "hyperion-powershell-task-";
    }

    @Override
    protected List<String> getFileExtensions() {
        return List.of("ps1");
    }

    @Override
    protected boolean isTempFileRelativePath() {
        return false;
    }

    @Override
    protected Process runFile(Path path) throws IOException {
        return new ProcessBuilder(List.of("powershell", "-File",
                path.toString()).toArray(String[]::new)).start();
    }

    @Override
    public AbstractTask copy() {
        return new PowershellTask(getTitle(), getCode());
    }
}

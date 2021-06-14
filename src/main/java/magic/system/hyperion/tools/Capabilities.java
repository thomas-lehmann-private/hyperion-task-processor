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
package magic.system.hyperion.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Capabilities of the system.
 *
 * @author Thomas Lehmann
 */
public class Capabilities {
    /**
     * Command for printing Docker version.
     */
    private static final String DOCKER_VERSION_COMMAND = "docker -v";

    /**
     * Command for printing Powershell version.
     */
    private static final String POWERSHELL_VERSION_COMMAND
            = "powershell -Command \"($PSVersionTable.PSVersion).toString()\"";

    /**
     * Evaluating whether underlying system is Windows.
     *
     * @return true when system is Windows.
     */
    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase(Locale.getDefault()).startsWith("windows");
    }

    /**
     * Checking that Docker command is available on the current system.
     *
     * @return true when Docker command is available.
     */
    public static boolean hasDocker() {
        boolean bSuccess;
        try {
            final var process = new ProcessBuilder(
                    createCommand(DOCKER_VERSION_COMMAND)).start();
            process.waitFor();
            bSuccess = ProcessResults.of(process, false).getExitCode() == 0;
        } catch (IOException | InterruptedException e) {
            bSuccess = false;
        }
        return bSuccess;
    }

    /**
     * Checking that Powershell command is available on the current system.
     *
     * @return true when Powershell command is available.
     */
    public static boolean hasPowershell() {
        boolean bSuccess;
        try {
            final var process = new ProcessBuilder(
                    createCommand(POWERSHELL_VERSION_COMMAND)).start();
            process.waitFor();
            bSuccess = ProcessResults.of(process, false).getExitCode() == 0;
        } catch (IOException | InterruptedException e) {
            bSuccess = false;
        }
        return bSuccess;
    }

    /**
     * Provide Docker version.
     *
     * @return version of Docker.
     */
    public static String getDockerVersion() {
        String strResult = "";
        try {
            final var process = new ProcessBuilder(
                    createCommand(DOCKER_VERSION_COMMAND)).start();
            process.waitFor();
            final var processResults = ProcessResults.of(process, false);
            if (!processResults.getStdout().isEmpty() && processResults.getExitCode() == 0) {
                strResult = processResults.getStdout().get(0).replace("Docker version ", "");
            }
        } catch (IOException | InterruptedException e) {
            strResult = "";
        }
        return strResult;
    }

    /**
     * Provide Powershell version.
     *
     * @return version of Powershell.
     */
    public static String getPowershellVersion() {
        String strResult = "";
        try {
            final var process = new ProcessBuilder(
                    createCommand(POWERSHELL_VERSION_COMMAND)).start();
            process.waitFor();
            final var processResults = ProcessResults.of(process, false);
            if (!processResults.getStdout().isEmpty() && processResults.getExitCode() == 0) {
                strResult = processResults.getStdout().get(0);
            }
        } catch (IOException | InterruptedException e) {
            strResult = "";
        }
        return strResult;
    }

    /**
     * Define shell command on environment.
     *
     * @param arguments the command to execute.
     * @return command as String array.
     */
    public static String[] createCommand(String... arguments) {
        final var command = new ArrayList<String>();

        if (isWindows()) {
            command.addAll(List.of("cmd", "/c"));
        } else {
            command.addAll(List.of("sh", "-c"));
        }

        command.addAll(Arrays.asList(arguments));
        return command.toArray(String[]::new);
    }
}

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

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Testing of class {@link PowershellTask}.
 *
 * @author Thomas Lehmann
 */
@Tag("windows")
public class PowershellTaskTest {

    /**
     * Test task title.
     */
    private static final String PRINT_HELLO_WORLD_TITLE = "print hello world";
    
    /**
     * Test output.
     */
    private static final String HELLO_WORD_TEXT = "hello world!";

    /**
     * Test process exit code.
     */
    private static final int PROCESS_EXIT_CODE = 12345;

    /**
     * Testing (finally) of {@link AbstractTask#getTitle()} and
     * {@link PowershellTask#run()} in context of inline code.
     */
    @Test
    public void testHelloWorldInline() {
        final var task = new PowershellTask(PRINT_HELLO_WORLD_TITLE,
                "Write-Host \"" + HELLO_WORD_TEXT + "\"");
        assertEquals(PRINT_HELLO_WORLD_TITLE, task.getTitle());
        final var results = task.run();

        //CHECKSTYLE.OFF: MultipleStringLiterals - we don't need to worry here
        assertEquals(0, results.getStderr().size(),
                "There should not be any error output!");
        assertEquals(1, results.getStdout().size(),
                "There should be exactly one line!");
        //CHECKSTYLE.ON: MultipleStringLiterals

        assertEquals(HELLO_WORD_TEXT, results.getStdout().get(0));
        assertEquals(0, results.getExitCode());
    }

    /**
     * Testing (finally) of {@link AbstractTask#getTitle()} and
     * {@link PowershellTask#run()} in context of code representing a path and
     * filename to an existing powershell script.
     */
    @Test
    public void testHelloWorldFile() {
        final var scriptPath = getClass().getResource("/scripts/say-hello-world.ps1")
                .toString().replaceFirst("file:/", "");
        final var task = new PowershellTask(PRINT_HELLO_WORLD_TITLE, scriptPath);
        final var results = task.run();

        // CHECKSTYLE.OFF: MultipleStringLiterals - we don't need to worry here
        assertEquals(0, results.getStderr().size(),
                "There should not be any error output!");
        assertEquals(1, results.getStdout().size(),
                "There should be exactly one line!");
        //CHECKSTYLE.ON: MultipleStringLiterals
        assertEquals(HELLO_WORD_TEXT, results.getStdout().get(0));
        assertEquals(0, results.getExitCode());
    }

    /**
     * Testing (finally) of {@link AbstractTask#getTitle()} and
     * {@link PowershellTask#run()} with errors from the script.
     *
     * @throws java.net.URISyntaxException when the syntax of the URI is wrong.
     */
    @Test
    public void testScriptWithStderr() throws URISyntaxException {
        final var scriptPath = Paths.get(getClass().getResource(
                "/scripts/say-error.ps1").toURI()).normalize().toString();
        final var task = new PowershellTask("print error", scriptPath);
        final var results = task.run();

        // FIXME: This is not very comfortable and the format is not reliable
        final var expectedLines = List.of(scriptPath + " : Something went wrong!",
            "    + CategoryInfo          : NotSpecified: (:) [Write-Error], WriteErrorException",
            "    + FullyQualifiedErrorId : UnknownProblem,say-error.ps1",
            " ");

        assertArrayEquals(expectedLines.toArray(), results.getStderr().toArray());
        // CHECKSTYLE.OFF: MultipleStringLiterals - we don't need to worry here
        assertEquals(0, results.getStdout().size(),
                "There should not be any stdout output!");
        //CHECKSTYLE.ON: MultipleStringLiterals
        assertEquals(0, results.getExitCode());
    }

    /**
     * Testing (finally) of {@link AbstractTask#getTitle()} and
     * {@link PowershellTask#run()} with an exit code not 0.
     */
    @Test
    public void testExitCode() {
        final var task = new PowershellTask("testing exit code",
                "exit " + PROCESS_EXIT_CODE);
        final var results = task.run();
        assertEquals(0, results.getStdout().size(),
                "There should not be any stdout output!");
        assertEquals(0, results.getStderr().size(),
                "There should not be any stderr output!");
        assertEquals(PROCESS_EXIT_CODE, results.getExitCode());
    }
}

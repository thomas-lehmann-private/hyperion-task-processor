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

import magic.system.hyperion.components.Model;
import magic.system.hyperion.components.TaskParameters;
import magic.system.hyperion.components.Variable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testing of class {@link PowershellTask}.
 *
 * @author Thomas Lehmann
 */
@Tag("windows")
@DisplayName("Testing PowershellTask class")
@TestMethodOrder(value = MethodOrderer.Random.class)
@SuppressWarnings("checkstyle:multiplestringliterals")
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
     * run method in context of inline code.
     */
    @Test
    @EnabledOnOs(OS.WINDOWS)
    public void testHelloWorldInline() {
        final var task = new PowershellTask(PRINT_HELLO_WORLD_TITLE,
                "Write-Host \"" + HELLO_WORD_TEXT + "\"");
        assertEquals(PRINT_HELLO_WORLD_TITLE, task.getTitle());
        final var result = task.run(TaskTestsTools.getDefaultTaskParameters());

        assertEquals(HELLO_WORD_TEXT, result.getVariable().getValue());
        assertTrue(result.isSuccess());
    }

    /**
     * Testing (finally) of {@link AbstractTask#getTitle()} and
     * {@link PowershellTask#run(TaskParameters)}} in context of code
     * representing a path and filename to an existing powershell script.
     *
     * @throws URISyntaxException when URL is bad
     */
    @Test
    @EnabledOnOs(OS.WINDOWS)
    public void testHelloWorldFile() throws URISyntaxException {
        final var scriptUrl = getClass().getResource("/scripts/say-hello-world.ps1");
        final var file = new File(scriptUrl.toURI());

        final var task = new PowershellTask(PRINT_HELLO_WORLD_TITLE, file.getAbsolutePath());
        final var result = task.run(TaskTestsTools.getDefaultTaskParameters());

        assertEquals(HELLO_WORD_TEXT, result.getVariable().getValue());
        assertTrue(result.isSuccess());
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    public void testFileWithTemplating() throws URISyntaxException {
        final var scriptUrl = getClass().getResource("/scripts/say-something.ps1");
        final var file = new File(scriptUrl.toURI());

        final var task = new PowershellTask(PRINT_HELLO_WORLD_TITLE, file.getAbsolutePath());
        final var result = task.run(TaskTestsTools.getSimpleTaskParameters());

        final var strExpected = "hello world 1!\nhello world 2!\n"
                + "hello world 3!\n2\nhello world 4!";
        assertEquals(strExpected, result.getVariable().getValue());
        assertTrue(result.isSuccess());
    }

    /**
     * Testing (finally) of {@link AbstractTask#getTitle()} and
     * {@link PowershellTask#run(TaskParameters)}} with errors from the script.
     *
     * @throws java.net.URISyntaxException when the syntax of the URI is wrong.
     */
    @Test
    @EnabledOnOs(OS.WINDOWS)
    public void testScriptWithStderr() throws URISyntaxException {
        final var scriptPath = Paths.get(getClass().getResource(
                "/scripts/say-error.ps1").toURI()).normalize().toString();
        final var task = new PowershellTask("print error", scriptPath);
        final var result = task.run(TaskTestsTools.getDefaultTaskParameters());

        assertTrue(result.getVariable().getValue().isEmpty());
        assertTrue(result.isSuccess());
    }

    /**
     * Testing (finally) of {@link AbstractTask#getTitle()} and
     * {@link PowershellTask#run(TaskParameters)} with an exit code not 0.
     */
    @Test
    @EnabledOnOs(OS.WINDOWS)
    public void testExitCode() {
        final var task = new PowershellTask("testing exit code",
                "exit " + PROCESS_EXIT_CODE);
        final var result = task.run(TaskTestsTools.getDefaultTaskParameters());
        assertFalse(result.isSuccess());
    }

    /**
     * Testing (finally) of {@link AbstractTask#getTitle()} and
     * {@link PowershellTask#run(TaskParameters)}} in context of inline code with
     * rendering variables.
     */
    @Test
    @EnabledOnOs(OS.WINDOWS)
    public void testHelloWorldInlineWithVariables() {
        final var variable = new Variable();
        variable.setValue(HELLO_WORD_TEXT);
        
        final var task = new PowershellTask(PRINT_HELLO_WORLD_TITLE,
                "Write-Host \"{{ variables.text.value }}\"");
        assertEquals(PRINT_HELLO_WORLD_TITLE, task.getTitle());

        final var parameters = TaskParameters.of(
                new Model(), Map.of(), Map.of("text", variable), null);
        final var result = task.run(parameters);

        assertEquals(HELLO_WORD_TEXT, result.getVariable().getValue());
        assertTrue(result.isSuccess());
    }

    /**
     * Testing copying of task.
     */
    @Test
    public void testCopy() {
        final var task = new PowershellTask(PRINT_HELLO_WORLD_TITLE,
                "Write-Host \"" + HELLO_WORD_TEXT + "\"");
        assertEquals(task, task.copy());
    }
}

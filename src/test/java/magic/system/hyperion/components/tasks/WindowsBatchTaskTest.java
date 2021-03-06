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
import magic.system.hyperion.tools.FileUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.net.URISyntaxException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testing of class {@link WindowsBatchTask}.
 *
 * @author Thomas Lehmann
 */
@DisplayName("Testing WindowsBatchTask class")
@TestMethodOrder(value = MethodOrderer.Random.class)
@SuppressWarnings("checkstyle:multiplestringliterals")
public class WindowsBatchTaskTest {
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
     * Testing Batch code inline in YAML.
     */
    @Test
    @EnabledOnOs(OS.WINDOWS)
    public void testHelloWorldInline() {
        final var task = new WindowsBatchTask(PRINT_HELLO_WORLD_TITLE,
                "echo " + HELLO_WORD_TEXT);
        assertEquals(PRINT_HELLO_WORLD_TITLE, task.getTitle());
        final var result = task.run(TaskTestsTools.getDefaultTaskParameters());

        assertEquals(HELLO_WORD_TEXT, result.getVariable().getValue());
        assertTrue(task.getTags().isEmpty());
        assertTrue(result.isSuccess());
    }

    /**
     * Testing (finally) of {@link AbstractTask#getTitle()} and
     * {@link WindowsBatchTask#run(TaskParameters)}} in context of code
     * representing a path and filename to an existing powershell script.
     *
     * @throws URISyntaxException when URL is bad
     */
    @Test
    @EnabledOnOs(OS.WINDOWS)
    public void testHelloWorldFile() throws URISyntaxException {
        final var scriptPath = FileUtils.getResourcePath("/scripts/say-hello-world.cmd");
        final var task = new WindowsBatchTask(PRINT_HELLO_WORLD_TITLE, scriptPath.toString());
        final var result = task.run(TaskTestsTools.getDefaultTaskParameters());

        assertEquals(HELLO_WORD_TEXT, result.getVariable().getValue());
        assertTrue(result.isSuccess());
    }

    /**
     * Testing (finally) of {@link AbstractTask#getTitle()} and
     * {@link WindowsBatchTask#run(TaskParameters)}} with errors from the script.
     *
     * @throws java.net.URISyntaxException when the syntax of the URI is wrong.
     */
    @Test
    @EnabledOnOs(OS.WINDOWS)
    public void testScriptWithStderr() throws URISyntaxException {
        final var scriptPath = FileUtils.getResourcePath("/scripts/say-error.cmd");
        final var task = new WindowsBatchTask("print error", scriptPath.toString());
        final var result = task.run(TaskTestsTools.getDefaultTaskParameters());

        assertTrue(result.getVariable().getValue().isEmpty());
        assertTrue(result.isSuccess());
    }

    /**
     * Testing (finally) of {@link AbstractTask#getTitle()} and
     * {@link WindowsBatchTask#run(TaskParameters)} with an exit code not 0.
     */
    @Test
    @EnabledOnOs(OS.WINDOWS)
    public void testExitCode() {
        final var task = new WindowsBatchTask("testing exit code",
                "exit " + PROCESS_EXIT_CODE);
        final var result = task.run(TaskTestsTools.getDefaultTaskParameters());
        assertFalse(result.isSuccess());
    }

    /**
     * Testing (finally) of {@link AbstractTask#getTitle()} and
     * {@link WindowsBatchTask#run(TaskParameters)}} in context of inline code with
     * rendering variables.
     */
    @Test
    @EnabledOnOs(OS.WINDOWS)
    public void testHelloWorldInlineWithVariables() {
        final var variable = new Variable();
        variable.setValue(HELLO_WORD_TEXT);

        final var task = new WindowsBatchTask(PRINT_HELLO_WORLD_TITLE,
                "echo {{ variables.text.value }}");
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
        final var task = new WindowsBatchTask(PRINT_HELLO_WORLD_TITLE,
                "echo " + HELLO_WORD_TEXT);
        assertEquals(task, task.copy());
    }
}

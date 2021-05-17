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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Testing class GroovyTask")
public class GroovyTaskTest {
    /**
     * Test output.
     */
    private static final String HELLO_WORD_TEXT = "hello world!";

    /**
     * Test task title.
     */
    private static final String TASK_TITLE = "test";

    /**
     * Testing embedded without using templating.
     */
    @Test
    public void testHelloWorldInline() {
        final var task = new GroovyTask(TASK_TITLE, "println 'hello world!'");
        final var result = task.run(Collections.emptyMap());

        assertEquals(TASK_TITLE, task.getTitle());
        assertEquals(HELLO_WORD_TEXT, result.getVariable().getValue());
        assertTrue(result.isSuccess());
    }

    /**
     * Testing file execution.
     */
    @Test
    public void testHelloWorldFile() {
        final var scriptPath = getClass().getResource("/scripts/say-hello-world.groovy")
                .toString().replaceFirst("file:/", "");

        final var task = new GroovyTask(TASK_TITLE, scriptPath);
        final var result = task.run(Collections.emptyMap());

        assertEquals(TASK_TITLE, task.getTitle());
        assertEquals(HELLO_WORD_TEXT, result.getVariable().getValue());
        assertTrue(result.isSuccess());
    }

    /**
     * Testing templating evaluating a variable.
     */
    @Test
    public void testHelloWorldInlineWithVariables() {
        final var variable = new Variable();
        variable.setValue(HELLO_WORD_TEXT);

        final var task = new GroovyTask(TASK_TITLE,
                "println '{{ variables.text.value }}'");
        assertEquals(TASK_TITLE, task.getTitle());
        final var result = task.run(Map.of("text", variable));

        assertEquals(HELLO_WORD_TEXT, result.getVariable().getValue());
        assertTrue(result.isSuccess());
    }
}

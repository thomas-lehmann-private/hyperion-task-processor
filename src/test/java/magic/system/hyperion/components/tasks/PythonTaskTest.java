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

import magic.system.hyperion.tools.Capabilities;
import magic.system.hyperion.tools.MessagesCollector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.File;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Testing of class {@link PythonTask}.
 *
 * @author Thomas Lehmann
 */
@DisplayName("Testing of class PythonTask")
@TestMethodOrder(value = MethodOrderer.Random.class)
@SuppressWarnings("checkstyle:multiplestringliterals")
public class PythonTaskTest {
    /**
     * Testing embedded code.
     */
    @Test
    public void testEmbeddedCode() {
        assumeTrue(Capabilities.hasPython());

        final var task = new PythonTask("test1", "print(\"hello world!\")");
        final var result = task.run(TaskTestsTools.getDefaultTaskParameters());

        assertTrue(result.isSuccess());
        assertEquals("hello world!", result.getVariable().getValue());
    }

    /**
     * Testing executing referenced file.
     */
    @Test
    public void testFile() throws URISyntaxException {
        assumeTrue(Capabilities.hasPython());

        final var scriptUrl = getClass().getResource("/scripts/say-hello-world.py");
        final var file = new File(scriptUrl.toURI());

        final var task = new PythonTask("test2", file.getAbsolutePath());
        final var result = task.run(TaskTestsTools.getDefaultTaskParameters());

        assertTrue(result.isSuccess());
        assertEquals("hello world!", result.getVariable().getValue());
    }

    /**
     * Testing file execution with templating.
     *
     * @throws URISyntaxException when URL is bad
     */
    @Test
    public void testFileWithTemplating() throws URISyntaxException {
        assumeTrue(Capabilities.hasPython());

        final var scriptUrl = getClass().getResource("/scripts/say-something.py");
        final var file = new File(scriptUrl.toURI());

        final var task = new PythonTask("{{model.attributes.test}}",
                file.getAbsolutePath());

        MessagesCollector.clear();
        final var result = task.run(TaskTestsTools.getSimpleTaskParameters());
        final var strExpected = "hello world 1!|hello world 2!|hello world 3!|2|hello world 4!"
                .replaceAll("\\|", "\n");

        assertEquals("Running task '" + TaskTestsTools.MODEL_TEST_VALUE + "'",
                MessagesCollector.getMessages().get(0));
        assertEquals(strExpected, result.getVariable().getValue().strip());
        assertTrue(result.isSuccess());
    }
}

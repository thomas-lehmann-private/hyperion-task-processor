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

import magic.system.hyperion.reader.DocumentReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testing of class {@link GroovyTask}.
 *
 * @author Thomas Lehmann
 */
@DisplayName("Testing class GroovyTask")
@SuppressWarnings({"checkstyle:multiplestringliterals", "checkstyle:classdataabstractioncoupling"})
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
        final var result = task.run(new TaskParameters());

        assertEquals(TASK_TITLE, task.getTitle());
        assertEquals(HELLO_WORD_TEXT, result.getVariable().getValue().strip());
        assertTrue(result.isSuccess());
    }

    /**
     * Testing file execution.
     *
     * @throws URISyntaxException when URL is bad
     */
    @Test
    public void testHelloWorldFile() throws URISyntaxException {
        final var scriptUrl = getClass().getResource("/scripts/say-hello-world.groovy");
        final var file = new File(scriptUrl.toURI());

        final var task = new GroovyTask(TASK_TITLE, file.getAbsolutePath());
        final var result = task.run(new TaskParameters());

        assertEquals(TASK_TITLE, task.getTitle());
        assertEquals(HELLO_WORD_TEXT, result.getVariable().getValue().strip());
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

        final var parameters = new TaskParameters(new Model(), Map.of("text", variable));
        final var result = task.run(parameters);

        assertEquals(HELLO_WORD_TEXT, result.getVariable().getValue().strip());
        assertTrue(result.isSuccess());
    }

    /**
     * Testing templating evaluating the model.
     *
     * @param strModelPath the path inside the model to access a value
     * @param strExpectedValue the expected value
     */
    @ParameterizedTest(name = "#{index} - path: {0} - expected value: {1}")
    @MethodSource("provideModelPaths")
    public void testWithModel(final String strModelPath, final String strExpectedValue)
            throws URISyntaxException {
        final var path = Paths.get(getClass().getResource(
                "/documents/valid-document-with-just-a-model.yml").toURI());

        final var task = new GroovyTask(TASK_TITLE,
                String.format("println '{{ %s }}'", strModelPath));

        final var reader = new DocumentReader(path);
        final var document = reader.read();

        final var parameters = new TaskParameters(document.getModel(), Map.of());
        final var result = task.run(parameters);

        assertEquals(strExpectedValue, result.getVariable().getValue().strip());
        assertTrue(result.isSuccess());
    }

    /**
     * Test data for the model test.
     *
     * @return test data.
     */
    private static Stream<Arguments> provideModelPaths() {
        return Stream.of(
                Arguments.of("model.attributes.description",
                        "this is a test model (main model)"),
                Arguments.of("model.attributes.subModel2.attributes.description",
                        "this is a sub model (map of depth 2)"),
                Arguments.of("model.attributes.listOfMessages.values[2]",
                        "hello world 3"),
                Arguments.of("model.attributes.listOfAnything.values[1]"
                                + ".attributes.subModel1.attributes.description",
                        "this is a sub model inside a list of the main model")
        );
    }
}

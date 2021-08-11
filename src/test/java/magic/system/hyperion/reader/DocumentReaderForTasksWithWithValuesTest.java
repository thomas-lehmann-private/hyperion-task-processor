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
package magic.system.hyperion.reader;

import magic.system.hyperion.components.DocumentParameters;
import magic.system.hyperion.data.AttributeMap;
import magic.system.hyperion.data.ListOfValues;
import magic.system.hyperion.data.StringValue;
import magic.system.hyperion.generics.Pair;
import magic.system.hyperion.tools.MessagesCollector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Testing {@link DocumentReader} in context of tasks that have
 * the attribute "with".
 *
 * @author Thomas Lehmann
 */
@DisplayName("Testing class DocumentReader for tasks with 'with' values")
@SuppressWarnings({"checkstyle:multiplestringliterals", "checkstyle:magicnumber"})
public class DocumentReaderForTasksWithWithValuesTest {
    /**
     * If something takes longer than a minute here this would be a problem.
     */
    private static final int DEFAULT_TIMEOUT_TASKGROUP = 60 * 1000;

    /**
     * Testing read of "with" values.
     *
     * @throws URISyntaxException when document url is wrong.
     */
    @Test
    public void testRead() throws URISyntaxException {
        final var path = Paths.get(getClass().getResource(
                "/documents/document-with-with-values.yml").toURI());
        final var reader = new DocumentReader();
        final var document = reader.read(path);
        assertNotNull(document, "Document shouldn't be null");

        final var firstTask = document.getListOfTaskGroups().get(0).getListOfTasks().get(0);
        final var expectedWithValues = ListOfValues.of(StringValue.of("hello world 1!"),
                StringValue.of("hello world 2!"), StringValue.of("hello world 3!"));
        assertEquals(expectedWithValues, firstTask.getWithValues());

        final var secondTask = document.getListOfTaskGroups().get(0).getListOfTasks().get(1);
        final var expectedWithValues2 = ListOfValues.of(StringValue.of("hello world 1!"),
                AttributeMap.of(
                        Pair.of("test1",
                                ListOfValues.of(StringValue.of("{{ model.attributes.test1 }}"))),
                        Pair.of("test2", StringValue.of("{{ model.attributes.test2 }}")),
                        Pair.of("test3", AttributeMap.of(
                                Pair.of("test4",
                                        StringValue.of("{{ model.attributes.test4 }}"))))));
        assertEquals(expectedWithValues2, secondTask.getWithValues());
    }

    /**
     * Testing read of "with" values.
     *
     * @throws URISyntaxException when document url is wrong.
     */
    @Test
    public void testRun() throws URISyntaxException {
        final var path = Paths.get(getClass().getResource(
                "/documents/document-with-with-values.yml").toURI());
        final var reader = new DocumentReader();
        final var document = reader.read(path);

        MessagesCollector.clear();
        document.run(getDefaultDocumentParameters());

        final var output = MessagesCollector.getMessages().stream().filter(
                entry ->  entry.contains("set variable")).collect(Collectors.toList());
        assertEquals("set variable default=hello world 1!", output.get(0));
        assertEquals("set variable default=hello world 2!", output.get(1));
        assertEquals("set variable default=hello world 3!", output.get(2));
        assertEquals("set variable default=hello world 1!", output.get(3));
        assertEquals("set variable default=hello world 4!", output.get(4));
    }

    /**
     * Provide default document parameters.
     *
     * @return default document parameters.
     */
    private static DocumentParameters getDefaultDocumentParameters() {
        return DocumentParameters.of(List.of(), DEFAULT_TIMEOUT_TASKGROUP);
    }
}

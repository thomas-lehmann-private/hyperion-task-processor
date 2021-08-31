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

import magic.system.hyperion.tools.MessagesCollector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testing reader for Python tasks.
 *
 * @author Thomas Lehmann
 */
@DisplayName("Testing reader for Python tasks")
@SuppressWarnings("checkstyle:magicnumber")
public class DocumentReaderForPythonTasksTest extends DocumentReaderBaseTest {
    /**
     * Testing a document with Python.
     *
     * @throws URISyntaxException when loading of the document has failed.
     */
    @Test
    public void testPythonEmbedded() throws URISyntaxException {
        final var path = Paths.get(getClass().getResource(
                "/documents/document-with-python.yml").toURI());
        final var reader = new DocumentReader();
        final var document = reader.read(path);
        assertNotNull(document, "Document shouldn't be null");
        assertEquals(1, document.getListOfTaskGroups().size());
        assertEquals(3, document.getListOfTaskGroups().get(0).getListOfTasks().size());
        final var tags = document.getListOfTaskGroups().get(0).getListOfTasks().get(2).getTags();
        assertEquals("tag support", tags.get(0));
        assertEquals("third example", tags.get(1));

        MessagesCollector.clear();
        document.run(getDefaultDocumentParameters());

        final var lines = MessagesCollector.getMessages().stream().filter(
                line -> line.contains("set variable")).collect(Collectors.toList());

        assertTrue(lines.contains("set variable default=hello world 1!"));
        assertTrue(lines.contains("set variable test2=this is a demo"));
        assertTrue(lines.contains("set variable default=hello world 2!"));
    }
}

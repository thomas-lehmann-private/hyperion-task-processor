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

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Testing of {@link WriteFileTaskReader}.
 *
 * @author Thomas Lehmann
 */
@DisplayName("Testing of WriteFileTaskReader")
public class DocumentReaderForWriteFileTasksTest extends DocumentReaderBaseTest {
    /**
     * Testing write file task.
     *
     * @throws URISyntaxException when url for document is wrong.
     */
    @Test
    public void testWriteFileTask() throws URISyntaxException, IOException {
        final var testPath = new File(getClass().getResource("/").toURI()).getAbsolutePath();
        final var path = Paths.get(getClass().getResource(
                "/documents/document-with-write-file-tasks.yml").toURI());

        final var strContent = Files.readString(path).replace("PATH", testPath);
        final var reader = new DocumentReader();
        final var document = reader.read(strContent.getBytes(StandardCharsets.UTF_8));
        assertNotNull(document, "Document shouldn't be null");

        MessagesCollector.clear();
        assertEquals(0, document.run(getDefaultDocumentParameters()));

        final var lines = MessagesCollector.getMessages().stream()
                .filter(line -> line.contains("Writing file to")).collect(Collectors.toList());
        assertEquals(2, lines.size());
    }
}

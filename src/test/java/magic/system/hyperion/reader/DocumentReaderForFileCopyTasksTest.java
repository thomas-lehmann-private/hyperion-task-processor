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
import magic.system.hyperion.tools.FileUtils;
import magic.system.hyperion.tools.MessagesCollector;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testing of {@link FileCopyTaskReader}.
 *
 * @author Thomas Lehmann
 */
@DisplayName("Testing of FileCopyTaskReader class")
@SuppressWarnings("checkstyle:multiplestringliterals")
public class DocumentReaderForFileCopyTasksTest {
    /**
     * If something takes longer than a minute here this would be a problem.
     */
    private static final int DEFAULT_TIMEOUT_TASKGROUP = 1;

    /**
     * Number of test tasks.
     */
    private static final int NUMBER_OF_TASKS = 4;

    @BeforeEach
    public void setup() throws URISyntaxException, IOException {
        final URL baseUrl = getClass().getResource("/");
        final var path = Paths.get(Path.of(baseUrl.toURI()).toString(), "tasks");
        Files.createDirectories(path);
    }

    @AfterEach
    public void cleanUp() throws IOException, URISyntaxException {
        final URL baseUrl = getClass().getResource("/");
        final var path = Paths.get(Path.of(baseUrl.toURI()).toString(), "tasks");
        assertTrue(FileUtils.removeDirectoryRecursive(path));
    }

    /**
     * Testing copy file task.
     *
     * @throws URISyntaxException when url for document is wrong.
     */
    @Test
    public void testFileCopyTask() throws URISyntaxException, IOException {
        final var testPath = new File(getClass().getResource("/").toURI()).getAbsolutePath();
        final var path = Paths.get(getClass().getResource(
                "/documents/document-with-copy-file-tasks.yml").toURI());

        final var strContent = Files.readString(path).replace("PATH", testPath);
        final var reader = new DocumentReader();
        final var document = reader.read(strContent.getBytes(StandardCharsets.UTF_8));
        assertNotNull(document, "Document shouldn't be null");

        MessagesCollector.clear();
        assertEquals(0, document.run(getDefaultDocumentParameters()));

        final var lines = MessagesCollector.getMessages().stream()
                .filter(line -> line.contains("Copying file from")).collect(Collectors.toList());
        assertEquals(NUMBER_OF_TASKS, lines.size());
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

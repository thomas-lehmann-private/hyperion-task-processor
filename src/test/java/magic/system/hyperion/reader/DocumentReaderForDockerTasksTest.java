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
import magic.system.hyperion.tools.Capabilities;
import magic.system.hyperion.tools.MessagesCollector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Testing of {@link DocumentReader} for Docker related tasks.
 *
 * @author Thomas Lehmann
 */
@DisplayName("Testing DocumentReader class for Docker tasks")
@SuppressWarnings({"checkstyle:multiplestringliterals", "checkstyle:magicnumber"})
public class DocumentReaderForDockerTasksTest {
    /**
     * If something takes longer than a minute here this would be a problem.
     */
    private static final int DEFAULT_TIMEOUT_TASKGROUP = 1;

    /**
     * Testing running a Docker container task.
     *
     * @throws URISyntaxException when url for document is wrong.
     */
    @Test
    public void testDockerContainerTask() throws URISyntaxException {
        assumeTrue(Capabilities.hasDocker());

        final var path = Paths.get(getClass().getResource(
                "/documents/document-with-docker-container.yml").toURI());
        final var reader = new DocumentReader(path);
        final var document = reader.read();
        assertNotNull(document, "Document shouldn't be null");
        assertEquals(1, document.getListOfTaskGroups().size());
        assertEquals(3, document.getListOfTaskGroups().get(0).getListOfTasks().size());
        final var tags = document.getListOfTaskGroups().get(0).getListOfTasks().get(2).getTags();
        assertEquals("tag support", tags.get(0));
        assertEquals("third example", tags.get(1));

        MessagesCollector.clear();
        document.run(getDefaultDocumentParameters());
        assertTrue(MessagesCollector.getMessages().contains("set variable default=hello world!"));
        assertTrue(MessagesCollector.getMessages().contains("set variable test2=this is a demo"));
    }

    /**
     * Testing running a Docker image + container task.
     *
     * @throws URISyntaxException when url for document is wrong.
     */
    @Test
    public void testDockerImageTask() throws URISyntaxException {
        assumeTrue(Capabilities.hasDocker());

        final var path = Paths.get(getClass().getResource(
                "/documents/document-with-docker-image.yml").toURI());
        final var reader = new DocumentReader(path);
        final var document = reader.read();
        assertNotNull(document, "Document shouldn't be null");
        assertEquals(1, document.getListOfTaskGroups().size());
        assertEquals(2, document.getListOfTaskGroups().get(0).getListOfTasks().size());

        MessagesCollector.clear();
        document.run(getDefaultDocumentParameters());
        assertTrue(MessagesCollector.getMessages().contains("set variable default=hello world!"));
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

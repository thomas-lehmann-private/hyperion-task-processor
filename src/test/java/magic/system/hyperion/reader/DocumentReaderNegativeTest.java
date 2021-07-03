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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Negative tests.
 *
 * @author Thomas Lehmann
 */
@DisplayName("Negative tests for document reader classes")
@SuppressWarnings("checkstyle:multiplestringliterals")
public class DocumentReaderNegativeTest {
    /**
     * Testing unknown main field. Usually there should be model,
     * matrix of taskgroups. Testing reading from byte content.
     *
     * @throws URISyntaxException when url is invalid.
     */
    @Test
    public void testUnknownMainFieldContent() throws URISyntaxException, IOException {
        final var path = Paths.get(Objects.requireNonNull(
                getClass().getResource("/documents/invalid/unknown-main-field.yml")).toURI());
        final var reader = new DocumentReader();
        final var document = reader.read(Files.readAllBytes(path));
        assertNull(document, "Document should be null");
    }

    /**
     * Testing unknown field in different locations (depends on document).
     *
     * @param strDocument the path and filename of the YAML document.
     * @throws URISyntaxException when url is invalid.
     */
    @ParameterizedTest(name = "#{index}  document={0}")
    @MethodSource("getUnknownOrMissingFieldTestData")
    public void testUnknownOrMissingField(final String strDocument) throws URISyntaxException {
        final var path = Paths.get(Objects.requireNonNull(
                getClass().getResource(strDocument)).toURI());
        final var reader = new DocumentReader();
        final var document = reader.read(path);
        assertNull(document, "Document should be null");
    }

    /**
     * Provide paths to documents with unknown fields.
     *
     * @return test data.
     */
    private static Stream<Arguments> getUnknownOrMissingFieldTestData() {
        return Stream.of(
                Arguments.of("/documents/invalid/unknown-main-field.yml"),
                Arguments.of("/documents/invalid/unknown-field-file-copy-task.yml"),
                Arguments.of("/documents/invalid/unknown-field-docker-image-task.yml"),
                Arguments.of("/documents/invalid/unknown-field-docker-container-task.yml"),
                Arguments.of("/documents/invalid/unknown-field-matrix.yml"),
                Arguments.of("/documents/invalid/unknown-field-variable.yml"),
                Arguments.of("/documents/invalid/unknown-field-taskgroup.yml"),
                Arguments.of("/documents/invalid/missing-field-type.yml"),
                Arguments.of("/documents/invalid/missing-field-variable.yml"),
                Arguments.of("/documents/invalid/missing-field-taskgroup.yml"),
                Arguments.of("/documents/invalid/missing-field-code.yml")
        );
    }
}

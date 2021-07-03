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

import com.fasterxml.jackson.databind.JsonNode;
import magic.system.hyperion.components.Document;
import magic.system.hyperion.components.TaskGroupsReader;
import magic.system.hyperion.exceptions.HyperionException;
import magic.system.hyperion.tools.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Reading a YAML file representing the spline document.
 *
 * @author Thomas Lehmann
 */
public class DocumentReader {

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentReader.class);

    /**
     * Final document.
     */
    private final Document document;

    /**
     * Initialize document reader with path of the document.
     * @since 1.0.0
     */
    public DocumentReader() {
        this.document = new Document();
    }

    /**
     * Reading the YAML document.
     *
     * @param path reading YAML from a path and filename.
     * @return document when successfully read otherwise null.
     * @since 1.0.0
     */
    public Document read(final Path path) {
        Document finalDocument = null;
        try {
            readDocument(FileUtils.readYamlTree(path));
            finalDocument = this.document;
        } catch (IOException | HyperionException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return finalDocument;
    }

    /**
     * Reading the YAML document.
     *
     * @param content reading YAML from a byte content.
     * @return document when successfully read otherwise null.
     * @since 1.0.0
     */
    public Document read(final byte[] content) {
        Document finalDocument = null;
        try {
            readDocument(FileUtils.readYamlTree(content));
            finalDocument = this.document;
        } catch (IOException | HyperionException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return finalDocument;
    }

    /**
     * Reading the main parts of the document.
     *
     * @param node where to read the parts from.
     * @throws HyperionException when reading of document has failed.
     */
    private void readDocument(final JsonNode node) throws HyperionException {
        final var iter = node.fields();
        while (iter.hasNext()) {
            final var entry = iter.next();
            final var field = DocumentReaderFields.fromValue(entry.getKey());

            switch (field) {
                case MODEL: {
                    new ModelReader(this.document.getModel()).read(entry.getValue());
                    break;
                }

                case MATRIX: {
                    new MatrixReader(this.document).read(entry.getValue());
                    break;
                }

                case TASKGROUPS: {
                    new TaskGroupsReader(this.document).read(entry.getValue());
                    break;
                }

                default: {
                    throw new HyperionException(String.format(
                            "Known field '%s' is not handled!", entry.getKey()));

                }
            }
        }
    }
}

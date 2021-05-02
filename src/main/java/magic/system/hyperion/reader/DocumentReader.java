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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.IOException;
import java.nio.file.Path;
import magic.system.hyperion.components.Document;
import magic.system.hyperion.components.PowershellTask;
import magic.system.hyperion.components.TaskGroup;
import magic.system.hyperion.generics.Converters;
import magic.system.hyperion.matcher.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reading a YAML file representing the spline document.
 *
 * @author Thomas Lehmann
 */
@SuppressWarnings("checkstyle:classdataabstractioncoupling")
public class DocumentReader {

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(
            DocumentReader.class);

    /**
     * Final document.
     */
    private final Document document;

    /**
     * Path where to find the document.
     */
    private final Path path;

    /**
     * Initialize document reader with path of the document.
     *
     * @param initPath path of the document.
     */
    public DocumentReader(final Path initPath) {
        this.document = new Document();
        this.path = initPath;
    }

    /**
     * Reading the YAML document.
     *
     * @return document when successfully read otherwise null.
     */
    public Document read() {
        Document finalDocument = null;
        try {
            final var mapper = new ObjectMapper(new YAMLFactory());
            readDocument(mapper.readTree(this.path.toUri().toURL()));
            finalDocument = this.document;
        } catch (IOException | DocumentReaderException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return finalDocument;
    }

    private void readDocument(final JsonNode node) throws DocumentReaderException {
        final var iter = node.fields();
        while (iter.hasNext()) {
            final var entry = iter.next();
            switch (entry.getKey()) {
                case "model": {
                    readModel(entry.getValue());
                    break;
                }

                case "taskgroups": {
                    readTaskGroups(entry.getValue());
                    break;
                }

                default: {
                    throw new DocumentReaderException(String.format(
                            "Unknown field %s!", entry.getKey()));

                }
            }
        }
    }

    private void readModel(final JsonNode node) {

    }

    private void readTaskGroups(final JsonNode node) throws DocumentReaderException {
        final var iter = node.elements();
        while (iter.hasNext()) {
            // there should be nothing else but individual task group.
            readTaskGroup(iter.next());
        }
    }

    private void readTaskGroup(final JsonNode node) throws DocumentReaderException {
        final var names = Converters.convertToSortedList(node.fieldNames());
        
        final var matcher = Matcher.of(names);
        matcher.requireExactlyOnce(DocumentReaderFields.TITLE.getFieldName());
        matcher.requireExactlyOnce(DocumentReaderFields.TASKS.getFieldName());
        matcher.allow(DocumentReaderFields.PARALLEL.getFieldName());
        
        if (!matcher.matches(names)) {
            throw new DocumentReaderException(
                    "Task group fields are missing or unknown!");
        }
        
        final var taskGroup = new TaskGroup(node.get(
                DocumentReaderFields.TITLE.getFieldName()).asText(),
                node.get(
                        DocumentReaderFields.PARALLEL.getFieldName()).asBoolean());

        final var iterTask = node.get(DocumentReaderFields.TASKS.getFieldName()).elements();
        while (iterTask.hasNext()) {
            readTask(taskGroup, iterTask.next());
        }

        // adding task group with its tasks
        this.document.add(taskGroup);
    }

    private void readTask(final TaskGroup taskGroup, final JsonNode node)
            throws DocumentReaderException {
        if (!node.has(DocumentReaderFields.TYPE.getFieldName())) {
            throw new DocumentReaderException(
                    "A task is expected to have a type field!");
        }

        final var strType = node.get(DocumentReaderFields.TYPE.getFieldName()).asText();
        switch (strType) {
            case "powershell": {
                readPowershellTask(taskGroup, node);
                break;
            }
            default: {
                throw new DocumentReaderException(
                        String.format("Task of type '%s' not known!", strType));
            }
        }

    }

    private void readPowershellTask(final TaskGroup taskGroup, final JsonNode node) {
        final var task = new PowershellTask();

        taskGroup.add(task);
    }
}

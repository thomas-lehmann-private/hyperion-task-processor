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
import magic.system.hyperion.components.AbstractTask;
import magic.system.hyperion.components.Document;
import magic.system.hyperion.components.GroovyTask;
import magic.system.hyperion.components.JShellTask;
import magic.system.hyperion.components.PowershellTask;
import magic.system.hyperion.components.TaskGroup;
import magic.system.hyperion.components.Variable;
import magic.system.hyperion.components.WindowsBatchTask;
import magic.system.hyperion.data.AttributeMap;
import magic.system.hyperion.data.ListOfValues;
import magic.system.hyperion.generics.Converters;
import magic.system.hyperion.interfaces.ICodeTaskCreator;
import magic.system.hyperion.matcher.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Reading a YAML file representing the spline document.
 *
 * @author Thomas Lehmann
 */
@SuppressWarnings({
        "checkstyle:classfanoutcomplexity",
        "checkstyle:classdataabstractioncoupling"})
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
     * @version 1.0.0
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
            final var field = DocumentReaderFields.fromValue(entry.getKey());

            switch (field) {
                case TASKGROUPS: {
                    readTaskGroups(entry.getValue());
                    break;
                }

                case MODEL: {
                    readAttributeMap(this.document.getModel().getData(), entry.getValue());
                    break;
                }

                default: {
                    throw new DocumentReaderException(String.format(
                            "Known field '%s' is not handled!", entry.getKey()));

                }
            }
        }
    }

    private void readAttributeMap(final AttributeMap attributeList, final JsonNode node) {
        final var iter = node.fields();
        while (iter.hasNext()) {
            final var currentField = iter.next();
            switch(currentField.getValue().getNodeType()) {
                case STRING:
                case NUMBER:
                case BOOLEAN: {
                    attributeList.set(currentField.getKey(), currentField.getValue().asText());
                    break;
                }
                case OBJECT: {
                    final var newAttributeList = new AttributeMap();
                    readAttributeMap(newAttributeList, currentField.getValue());
                    attributeList.set(currentField.getKey(), newAttributeList);
                    break;
                }
                case ARRAY: {
                    final var newList = new ListOfValues();
                    readListOfValues(newList, currentField.getValue());
                    attributeList.set(currentField.getKey(), newList);
                    break;
                }
                default: {
                    LOGGER.warn(DocumentReaderMessage.NODE_TYPE_NOT_SUPPORTED.getMessage(),
                            currentField.getValue().getNodeType());
                }
            }
        }
    }

    private void readListOfValues(final ListOfValues list, final JsonNode node) {
        final var iter = node.elements();
        while (iter.hasNext()) {
            final var currentElement = iter.next();
            switch(currentElement.getNodeType()) {
                case STRING:
                case NUMBER:
                case BOOLEAN: {
                    list.add(currentElement.asText());
                    break;
                }
                case OBJECT: {
                    final var newAttributeList = new AttributeMap();
                    readAttributeMap(newAttributeList, currentElement);
                    list.add(newAttributeList);
                    break;
                }
                default: {
                    LOGGER.warn(DocumentReaderMessage.NODE_TYPE_NOT_SUPPORTED.getMessage(),
                            currentElement.getNodeType());
                }
            }
        }
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
                    DocumentReaderMessage.MISSING_OR_UNKNOWN_TASK_GROUP_FIELDS.getMessage());
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
        final var names = Converters.convertToSortedList(node.fieldNames());
        final var matcher = Matcher.of(names);
        matcher.requireExactlyOnce(DocumentReaderFields.TYPE.getFieldName());

        if (!matcher.matches(names)) {
            throw new DocumentReaderException(
                    "A task is expected to have a type field!");
        }

        final var strType = node.get(DocumentReaderFields.TYPE.getFieldName()).asText();
        switch (strType) {
            case "powershell": {
                readCodeTask(taskGroup, node,
                        (strTitle, strCode) -> new PowershellTask(strTitle, strCode));
                break;
            }
            case "batch": {
                readCodeTask(taskGroup, node,
                        (strTitle, strCode) -> new WindowsBatchTask(strTitle, strCode));
                break;
            }
            case "jshell": {
                readCodeTask(taskGroup, node,
                        (strTitle, strCode) -> new JShellTask(strTitle, strCode));
                break;
            }
            case "groovy": {
                readCodeTask(taskGroup, node,
                        (strTitle, strCode) -> new GroovyTask(strTitle, strCode));
                break;
            }
            default: {
                throw new DocumentReaderException(
                        String.format("Task of type '%s' is not known!", strType));
            }
        }

    }

    /**
     * Reading a code task.
     *
     * @param taskGroup   the task group where to add the code task.
     * @param node        the node to process containing the code task.
     * @param taskCreator function that does create the task.
     */
    private void readCodeTask(final TaskGroup taskGroup, final JsonNode node,
                              final ICodeTaskCreator taskCreator)
            throws DocumentReaderException {
        final var names = Converters.convertToSortedList(node.fieldNames());
        final var matcher = Matcher.of(names);

        matcher.requireExactlyOnce(DocumentReaderFields.TYPE.getFieldName());
        matcher.requireExactlyOnce(DocumentReaderFields.CODE.getFieldName());
        matcher.allow(DocumentReaderFields.TITLE.getFieldName());
        matcher.allow(DocumentReaderFields.VARIABLE.getFieldName());
        matcher.allow(DocumentReaderFields.TAGS.getFieldName());

        if (!matcher.matches(names)) {
            throw new DocumentReaderException("The task fields are not correct!");
        }

        final var strTitle = node.has(DocumentReaderFields.TITLE.getFieldName())
                ? node.get(DocumentReaderFields.TITLE.getFieldName()).asText() : "";

        final var task = taskCreator.createTask(strTitle,
                node.get(DocumentReaderFields.CODE.getFieldName()).asText());

        if (node.has(DocumentReaderFields.VARIABLE.getFieldName())) {
            readVariable(task.getVariable(),
                    node.get(DocumentReaderFields.VARIABLE.getFieldName()));
        }

        if (node.has(DocumentReaderFields.TAGS.getFieldName())) {
            readTags(task, node.get(DocumentReaderFields.TAGS.getFieldName()));
        }

        taskGroup.add(task);
    }

    private void readVariable(final Variable variable, final JsonNode node) {
        final var names = Converters.convertToSortedList(node.fieldNames());
        final var matcher = Matcher.of(names);

        matcher.requireExactlyOnce(DocumentReaderFields.NAME.getFieldName());
        matcher.requireExactlyOnce(DocumentReaderFields.REGEX.getFieldName());
        matcher.allow(DocumentReaderFields.GROUP.getFieldName());

        if (matcher.matches(names)) {
            variable.setName(node.get(DocumentReaderFields.NAME.getFieldName()).asText());
            variable.setRegex(node.get(DocumentReaderFields.REGEX.getFieldName()).asText());
            if (node.has(DocumentReaderFields.GROUP.getFieldName())) {
                variable.setRegexGroup(
                        node.get(DocumentReaderFields.GROUP.getFieldName()).asInt());
            }
        }
    }

    /**
     * Reading tags into the task.
     *
     * @param task the concrete task where to add the tags.
     * @param node the node where to read the tags from.
     */
    private void readTags(final AbstractTask task, final JsonNode node) {
        final var iter = node.elements();
        while (iter.hasNext()) {
            task.addTag(iter.next().asText());
        }
    }
}

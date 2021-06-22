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
import magic.system.hyperion.components.TaskGroup;
import magic.system.hyperion.exceptions.HyperionException;
import magic.system.hyperion.generics.Converters;
import magic.system.hyperion.interfaces.ICodeTaskCreator;
import magic.system.hyperion.matcher.Matcher;

/**
 * Reading coded tasks.
 *
 * @author Thomas Lehmann
 */
public class CodedTaskReader implements INodeReader {
    /**
     * The task group where to add the Docker container task when all is fine.
     */
    private final TaskGroup taskGroup;

    /**
     * The creator that does create the concrete task.
     */
    private final ICodeTaskCreator taskCreator;

    /**
     * Initialize with task group where to add the coded task.
     *
     * @param initTaskGroup keeper of the list of tasks.
     * @param initTaskCreator the function that provides the creator for a task.
     * @since 1.0.0
     */
    public CodedTaskReader(final TaskGroup initTaskGroup, final ICodeTaskCreator initTaskCreator) {
        this.taskGroup = initTaskGroup;
        this.taskCreator = initTaskCreator;
    }

    @Override
    public void read(JsonNode node) throws HyperionException {
        final var names = Converters.convertToSortedList(node.fieldNames());
        final var matcher = Matcher.of(names);

        matcher.requireExactlyOnce(DocumentReaderFields.TYPE.getFieldName());
        matcher.requireExactlyOnce(DocumentReaderFields.CODE.getFieldName());
        matcher.allow(DocumentReaderFields.TITLE.getFieldName());
        matcher.allow(DocumentReaderFields.VARIABLE.getFieldName());
        matcher.allow(DocumentReaderFields.TAGS.getFieldName());
        matcher.allow(DocumentReaderFields.WITH.getFieldName());

        if (!matcher.matches(names)) {
            throw new HyperionException("The task fields are not correct!");
        }

        final var strTitle = node.has(DocumentReaderFields.TITLE.getFieldName())
                ? node.get(DocumentReaderFields.TITLE.getFieldName()).asText() : "";

        final var task = taskCreator.createTask(strTitle,
                node.get(DocumentReaderFields.CODE.getFieldName()).asText());

        if (node.has(DocumentReaderFields.VARIABLE.getFieldName())) {
            new VariableReader(task.getVariable())
                    .read(node.get(DocumentReaderFields.VARIABLE.getFieldName()));
        }

        if (node.has(DocumentReaderFields.TAGS.getFieldName())) {
            new TagsReader(task).read(node.get(DocumentReaderFields.TAGS.getFieldName()));
        }

        if (node.has(DocumentReaderFields.WITH.getFieldName())) {
            new ListOfValuesReader(task.getWithValues()).read(
                    node.get(DocumentReaderFields.WITH.getFieldName()));
        }

        this.taskGroup.add(task);
    }
}

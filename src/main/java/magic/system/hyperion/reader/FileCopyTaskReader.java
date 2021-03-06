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
import magic.system.hyperion.components.tasks.FileCopyTask;
import magic.system.hyperion.exceptions.HyperionException;
import magic.system.hyperion.generics.Converters;
import magic.system.hyperion.interfaces.ITaskCreator;

/**
 * Reader for a copy file task.
 *
 * @author Thomas Lehmann
 */
public class FileCopyTaskReader extends AbstractFileTaskReader {
    /**
     * Initialize with task group where to add the task.
     *
     * @param initTaskGroup   keeper of the list of tasks.
     * @param initTaskCreator the function that provides the creator for a task.
     * @since 1.0.0
     */
    public FileCopyTaskReader(TaskGroup initTaskGroup, ITaskCreator initTaskCreator) {
        super(initTaskGroup, initTaskCreator);
    }

    @Override
    public void read(JsonNode node) throws HyperionException {
        final var matcher = getMatcher(node);

        matcher.requireExactlyOnce(DocumentReaderFields.SOURCE.getFieldName());
        matcher.allow(DocumentReaderFields.DESTINATION_IS_DIRECTORY.getFieldName());

        final var names = Converters.convertToSortedList(node.fieldNames());
        if (!matcher.matches(names)) {
            throw new HyperionException("The copy file task fields are not correct!");
        }

        final var task = (FileCopyTask) taskCreator.createTask();
        readBasic(task, node);
        readFile(task, node);

        task.setSourcePath(node.get(
                DocumentReaderFields.SOURCE.getFieldName()).asText());

        if (node.has(DocumentReaderFields.DESTINATION_IS_DIRECTORY.getFieldName())) {
            task.setDestinationIsDirectory(node.get(
                    DocumentReaderFields.DESTINATION_IS_DIRECTORY.getFieldName()).asBoolean());
        }

        this.taskGroup.add(task);
    }
}

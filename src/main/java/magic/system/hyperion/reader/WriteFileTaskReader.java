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
import magic.system.hyperion.annotations.Named;
import magic.system.hyperion.components.TaskGroup;
import magic.system.hyperion.components.tasks.TaskType;
import magic.system.hyperion.components.tasks.WriteFileTask;
import magic.system.hyperion.exceptions.HyperionException;
import magic.system.hyperion.generics.Converters;
import magic.system.hyperion.interfaces.ITaskCreator;

/**
 * Reader for a write file task.
 *
 * @author Thomas Lehmann
 */
@Named(TaskType.Constants.WRITE_FILE)
public class WriteFileTaskReader extends AbstractFileTaskReader {
    /**
     * Initialize with task group where to add the coded task.
     *
     * @param initTaskGroup   keeper of the list of tasks.
     * @param initTaskCreator the function that provides the creator for a task.
     * @since 1.0.0
     */
    public WriteFileTaskReader(TaskGroup initTaskGroup, ITaskCreator initTaskCreator) {
        super(initTaskGroup, initTaskCreator);
    }

    @Override
    public void read(JsonNode node) throws HyperionException {
        final var matcher = getMatcher(node);

        matcher.requireExactlyOnce(DocumentReaderFields.CONTENT.getFieldName());
        matcher.requireExactlyOnce(DocumentReaderFields.DESTINATION.getFieldName());
        matcher.allow(DocumentReaderFields.ENSURE_PATH.getFieldName());
        matcher.allow(DocumentReaderFields.OVERWRITE.getFieldName());

        final var names = Converters.convertToSortedList(node.fieldNames());
        if (!matcher.matches(names)) {
            throw new HyperionException("The copy file task fields are not correct!");
        }

        final var task = (WriteFileTask) taskCreator.createTask();
        readBasic(task, node);

        task.setContent(node.get(
                DocumentReaderFields.CONTENT.getFieldName()).asText());
        task.setDestinationPath(node.get(
                DocumentReaderFields.DESTINATION.getFieldName()).asText());

        readFile(task, node);

        this.taskGroup.add(task);
    }
}

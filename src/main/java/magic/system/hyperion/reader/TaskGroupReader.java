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
import magic.system.hyperion.components.interfaces.IChangeableDocument;
import magic.system.hyperion.exceptions.HyperionException;
import magic.system.hyperion.generics.Converters;
import magic.system.hyperion.matcher.Matcher;

/**
 * Reading one task group from document.
 *
 * @author Thomas Lehmann
 */
public class TaskGroupReader implements INodeReader{
    /**
     * The document access reduced to operations for adding things only.
     */
    private final IChangeableDocument document;

    /**
     * Initialize reader with document.
     *
     * @param initDocument the document where to add the task group.
     * @since 1.0.0
     */
    public TaskGroupReader(final IChangeableDocument initDocument) {
        this.document = initDocument;
    }

    @Override
    public void read(JsonNode node) throws HyperionException {
        final var names = Converters.convertToSortedList(node.fieldNames());
        final var matcher = Matcher.of(names);
        matcher.requireExactlyOnce(DocumentReaderFields.TITLE.getFieldName());
        matcher.requireExactlyOnce(DocumentReaderFields.TASKS.getFieldName());
        matcher.allow(DocumentReaderFields.PARALLEL.getFieldName());

        if (!matcher.matches(names)) {
            throw new HyperionException(
                    DocumentReaderMessage.MISSING_OR_UNKNOWN_TASK_GROUP_FIELDS.getMessage());
        }

        final var bRunTaskAsParallel =
                node.has(DocumentReaderFields.PARALLEL.getFieldName())
                        && node.get(DocumentReaderFields.PARALLEL.getFieldName()).asBoolean();

        final var taskGroup = new TaskGroup(node.get(
                DocumentReaderFields.TITLE.getFieldName()).asText(), bRunTaskAsParallel);

        final var iterTask = node.get(DocumentReaderFields.TASKS.getFieldName()).elements();
        while (iterTask.hasNext()) {
            new TaskReader(taskGroup).read(iterTask.next());
        }

        // adding task group with its tasks
        this.document.add(taskGroup);
    }
}

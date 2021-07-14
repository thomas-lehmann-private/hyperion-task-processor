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
import magic.system.hyperion.components.tasks.AbstractTask;
import magic.system.hyperion.exceptions.HyperionException;
import magic.system.hyperion.interfaces.ITaskCreator;
import magic.system.hyperion.matcher.ListMatcher;

/**
 * Reader for the basic task attributes.
 *
 * @author Thomas Lehmann
 */
public abstract class AbstractBasicTaskReader implements INodeReader {
    /**
     * The task group where to add the Docker container task when all is fine.
     */
    protected TaskGroup taskGroup;

    /**
     * The creator that does create the concrete task.
     */
    protected final ITaskCreator taskCreator;

    /**
     * Initialize with task group where to add the coded task.
     *
     * @param initTaskGroup   keeper of the list of tasks.
     * @param initTaskCreator the function that provides the creator for a task.
     * @since 1.0.0
     */
    public AbstractBasicTaskReader(final TaskGroup initTaskGroup,
                                   final ITaskCreator initTaskCreator) {
        this.taskGroup = initTaskGroup;
        this.taskCreator = initTaskCreator;
    }

    /**
     * Change task group.
     *
     * @param initTaskGroup new task group where to add the tasks.
     * @since 2.0.0
     */
    public void setTaskGroup(final TaskGroup initTaskGroup) {
        this.taskGroup = initTaskGroup;
    }

    /**
     * Provide match being suitable for all situations.
     *
     * @param node current node.
     * @return matcher.
     */
    protected ListMatcher<String> getMatcher(final JsonNode node) {
        final var matcher = new ListMatcher<String>();

        matcher.requireExactlyOnce(DocumentReaderFields.TYPE.getFieldName());
        matcher.allow(DocumentReaderFields.TITLE.getFieldName());
        matcher.allow(DocumentReaderFields.VARIABLE.getFieldName());
        matcher.allow(DocumentReaderFields.TAGS.getFieldName());
        matcher.allow(DocumentReaderFields.WITH.getFieldName());

        return matcher;
    }

    /**
     * Reading for the task all basic information.
     *
     * @param task the created task.
     * @param node the node of the task.
     * @throws HyperionException when something went wrong.
     */
    protected void readBasic(final AbstractTask task, final JsonNode node)
            throws HyperionException {
        final var strTitle = node.has(DocumentReaderFields.TITLE.getFieldName())
                ? node.get(DocumentReaderFields.TITLE.getFieldName()).asText() : "";

        task.setTitle(strTitle);

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
    }
}

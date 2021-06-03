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
import magic.system.hyperion.components.tasks.DockerContainerTask;
import magic.system.hyperion.exceptions.HyperionException;
import magic.system.hyperion.generics.Converters;
import magic.system.hyperion.interfaces.ICodeTaskCreator;
import magic.system.hyperion.matcher.Matcher;
import magic.system.hyperion.tools.Capabilities;

/**
 * Reading a docker container task in a document.
 *
 * @author Thomas Lehmann
 */
public class DockerContainerTaskReader implements INodeReader{
    /**
     * The task group where to add the Docker container task when all is fine.
     */
    private final TaskGroup taskGroup;

    /**
     * The creator that does create the concrete task.
     */
    private final ICodeTaskCreator taskCreator;

    public DockerContainerTaskReader(final TaskGroup initTaskGroup,
                                     final ICodeTaskCreator initTaskCreator) {
        this.taskGroup = initTaskGroup;
        this.taskCreator = initTaskCreator;
    }

    @Override
    public void read(JsonNode node) throws HyperionException {
        if (!Capabilities.hasDocker()) {
            throw new HyperionException("Docker seems to be missing; cannot process document!");
        }

        final var names = Converters.convertToSortedList(node.fieldNames());
        final var matcher = Matcher.of(names);

        matcher.requireExactlyOnce(DocumentReaderFields.TYPE.getFieldName());
        matcher.requireExactlyOnce(DocumentReaderFields.CODE.getFieldName());
        matcher.requireExactlyOnce(DocumentReaderFields.IMAGE_NAME.getFieldName());
        matcher.allow(DocumentReaderFields.TITLE.getFieldName());
        matcher.allow(DocumentReaderFields.IMAGE_VERSION.getFieldName());
        matcher.allow(DocumentReaderFields.PLATFORM.getFieldName());
        matcher.allow(DocumentReaderFields.VARIABLE.getFieldName());
        matcher.allow(DocumentReaderFields.TAGS.getFieldName());

        if (!matcher.matches(names)) {
            throw new HyperionException("The Docker container task fields are not correct!");
        }

        final var strTitle = node.has(DocumentReaderFields.TITLE.getFieldName())
                ? node.get(DocumentReaderFields.TITLE.getFieldName()).asText() : "";

        final var task = (DockerContainerTask) taskCreator.createTask(strTitle,
                node.get(DocumentReaderFields.CODE.getFieldName()).asText());

        task.setImageName(node.get(DocumentReaderFields.IMAGE_NAME.getFieldName()).asText());

        if (node.has(DocumentReaderFields.IMAGE_VERSION.getFieldName())) {
            task.setImageVersion(
                    node.get(DocumentReaderFields.IMAGE_VERSION.getFieldName()).asText());
        }

        if (node.has(DocumentReaderFields.PLATFORM.getFieldName())) {
            task.setPlatform(
                    node.get(DocumentReaderFields.PLATFORM.getFieldName()).asText());
        }

        if (node.has(DocumentReaderFields.VARIABLE.getFieldName())) {
            new VariableReader(task.getVariable())
                    .read(node.get(DocumentReaderFields.VARIABLE.getFieldName()));
        }

        if (node.has(DocumentReaderFields.TAGS.getFieldName())) {
            new TagsReader(task).read(node.get(DocumentReaderFields.TAGS.getFieldName()));
        }

        taskGroup.add(task);
    }
}

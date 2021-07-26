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
import magic.system.hyperion.interfaces.ITaskCreator;
import magic.system.hyperion.tools.Capabilities;

/**
 * Reading a docker container task in a document.
 *
 * @author Thomas Lehmann
 */
public class DockerContainerTaskReader extends AbstractDockerTaskReader {
    /**
     * Initialize with task group where to add the coded task.
     *
     * @param initTaskGroup   keeper of the list of tasks.
     * @param initTaskCreator the function that provides the creator for a task.
     * @since 1.0.0
     */
    public DockerContainerTaskReader(final TaskGroup initTaskGroup,
                                     final ITaskCreator initTaskCreator) {
        super(initTaskGroup, initTaskCreator);
    }

    @Override
    public void read(final JsonNode node) throws HyperionException {
        if (!Capabilities.hasDocker()) {
            throw new HyperionException("Docker seems to be missing; cannot process document!");
        }

        final var matcher = getMatcher(node);
        matcher.requireExactlyOnce(DocumentReaderFields.IMAGE_NAME.getFieldName());
        matcher.allow(DocumentReaderFields.IMAGE_VERSION.getFieldName());
        matcher.allow(DocumentReaderFields.PLATFORM.getFieldName());
        matcher.allow(DocumentReaderFields.DETACHED.getFieldName());

        final var names = Converters.convertToSortedList(node.fieldNames());
        if (!matcher.matches(names)) {
            throw new HyperionException("The Docker container task fields are not correct!");
        }

        final var task = (DockerContainerTask) taskCreator.createTask();
        readBasic(task, node);
        task.setCode(node.get(DocumentReaderFields.CODE.getFieldName()).asText());

        task.setImageName(node.get(DocumentReaderFields.IMAGE_NAME.getFieldName()).asText());

        if (node.has(DocumentReaderFields.IMAGE_VERSION.getFieldName())) {
            task.setImageVersion(
                    node.get(DocumentReaderFields.IMAGE_VERSION.getFieldName()).asText());
        }

        if (node.has(DocumentReaderFields.PLATFORM.getFieldName())) {
            task.setPlatform(
                    node.get(DocumentReaderFields.PLATFORM.getFieldName()).asText());
        }

        if (node.has(DocumentReaderFields.DETACHED.getFieldName())) {
            task.setDetached(
                    node.get(DocumentReaderFields.DETACHED.getFieldName()).asBoolean());
        }

        taskGroup.add(task);
    }
}

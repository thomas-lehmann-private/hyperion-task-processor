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
import magic.system.hyperion.components.tasks.TaskType;
import magic.system.hyperion.components.tasks.creator.ITaskCreator;
import magic.system.hyperion.exceptions.HyperionException;
import magic.system.hyperion.generics.Converters;
import magic.system.hyperion.matcher.Matcher;
import magic.system.hyperion.tools.Factory;

/**
 * Reader for tasks (main class).
 *
 * @author Thomas
 */
@SuppressWarnings("checkstyle:classdataabstractioncoupling") // will be fixed later on
public class TaskReader implements INodeReader {
    /**
     * The task group where to add the Docker container task when all is fine.
     */
    private final TaskGroup taskGroup;

    /**
     * Factory for coded tasks.
     */
    private final Factory<AbstractTask> tasksFactory;

    /**
     * Initialize with task group where to add the coded task.
     *
     * @param initTaskGroup keeper of the list of tasks.
     */
    public TaskReader(final TaskGroup initTaskGroup) {
        this.taskGroup = initTaskGroup;
        this.tasksFactory = new Factory<AbstractTask>(ITaskCreator.class);
    }

    @Override
    public void read(JsonNode node) throws HyperionException {
        final var names = Converters.convertToSortedList(node.fieldNames());
        final var matcher = Matcher.of(names);
        matcher.requireExactlyOnce(DocumentReaderFields.TYPE.getFieldName());

        if (!matcher.matches(names)) {
            throw new HyperionException(
                    "A task is expected to have a type field!");
        }

        final var strType = node.get(DocumentReaderFields.TYPE.getFieldName()).asText();
        final var type = TaskType.fromValue(strType);

        switch (type) {
            case DOCKER_CONTAINER: {
                new DockerContainerTaskReader(taskGroup, () -> {
                    return tasksFactory.create(type.getTypeName());
                }).read(node);
                break;
            }

            case DOCKER_IMAGE: {
                new DockerImageTaskReader(taskGroup, () -> {
                    return tasksFactory.create(type.getTypeName());
                }).read(node);
                break;
            }

            case COPY_FILE: {
                new FileCopyTaskReader(taskGroup, () -> {
                    return tasksFactory.create(type.getTypeName());
                }).read(node);
                break;
            }

            default: {
                new CodedTaskReader(taskGroup, () -> {
                    return tasksFactory.create(type.getTypeName());
                }).read(node);
            }
        }
    }
}

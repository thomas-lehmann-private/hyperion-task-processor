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
package magic.system.hyperion.reader.creator;

import magic.system.hyperion.annotations.Named;
import magic.system.hyperion.components.tasks.AbstractTask;
import magic.system.hyperion.components.tasks.TaskType;
import magic.system.hyperion.components.tasks.creator.ITaskCreator;
import magic.system.hyperion.reader.AbstractBasicTaskReader;
import magic.system.hyperion.reader.DockerContainerTaskReader;
import magic.system.hyperion.tools.Factory;

/**
 * Creator for instance of {@link magic.system.hyperion.reader.DockerContainerTaskReader}.
 *
 * @author Thomas Lehmann
 */
@Named(TaskType.Constants.DOCKER_CONTAINER)
public class DockerContainerTaskReaderCreator implements ITaskReaderCreator {
    @Override
    public AbstractBasicTaskReader create() {
        final var tasksFactory = new Factory<AbstractTask>(ITaskCreator.class);
        return new DockerContainerTaskReader(null, () -> {
            return tasksFactory.create(TaskType.DOCKER_CONTAINER.getTypeName());
        });
    }
}

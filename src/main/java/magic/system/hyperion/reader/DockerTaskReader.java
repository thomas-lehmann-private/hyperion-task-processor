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
import magic.system.hyperion.interfaces.ICodeTaskCreator;
import magic.system.hyperion.matcher.ListMatcher;

/**
 * Base class for docker image task reader and docker container task reader.
 *
 * @author Thomas Lehmann
 */
public class DockerTaskReader {
    /**
     * The task group where to add the Docker container task when all is fine.
     */
    protected final TaskGroup taskGroup;

    /**
     * The creator that does create the concrete task.
     */
    protected final ICodeTaskCreator taskCreator;

    /**
     * Initialize with task group where to add the coded task.
     *
     * @param initTaskGroup   keeper of the list of tasks.
     * @param initTaskCreator the function that provides the creator for a task.
     */
    public DockerTaskReader(final TaskGroup initTaskGroup,
                                     final ICodeTaskCreator initTaskCreator) {
        this.taskGroup = initTaskGroup;
        this.taskCreator = initTaskCreator;
    }

    /**
     * Provide match being suitable for both situations.
     *
     * @param node current node.
     * @return matcher.
     */
    protected ListMatcher<String> getMatcher(final JsonNode node) {
        final var matcher = new ListMatcher<String>();

        matcher.requireExactlyOnce(DocumentReaderFields.TYPE.getFieldName());
        matcher.requireExactlyOnce(DocumentReaderFields.CODE.getFieldName());
        matcher.allow(DocumentReaderFields.TITLE.getFieldName());
        matcher.allow(DocumentReaderFields.VARIABLE.getFieldName());
        matcher.allow(DocumentReaderFields.TAGS.getFieldName());

        return matcher;
    }
}

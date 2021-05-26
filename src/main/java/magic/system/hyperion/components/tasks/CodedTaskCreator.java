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
package magic.system.hyperion.components.tasks;

import magic.system.hyperion.exceptions.HyperionException;

/**
 * Task creator for coded tasks.
 *
 * @author Thomas Lehmann
 */
@SuppressWarnings("checkstyle:classdataabstractioncoupling")
public final class CodedTaskCreator {
    /**
     * Not intended for instantiation.
     */
    private CodedTaskCreator() {
        // Nothing to do here
    }

    /**
     * Creating task by type.
     *
     * @param type type of the task.
     * @param strTitle title of the task.
     * @param strCode code of the task.
     * @return the task if known otherwise null.
     * @throws HyperionException when task type is unknown.
     */
    public static AbstractTask createTask(final TaskType type,
                                          final String strTitle, final String strCode)
            throws HyperionException {
        final AbstractTask task;

        switch (type) {
            case GROOVY: {
                task = new GroovyTask(strTitle, strCode);
                break;
            }
            case SHELL: {
                task = new UnixShellTask(strTitle, strCode);
                break;
            }
            case JSHELL: {
                task = new JShellTask(strTitle, strCode);
                break;
            }
            case BATCH: {
                task = new WindowsBatchTask(strTitle, strCode);
                break;
            }
            case POWERSHELL: {
                task = new PowershellTask(strTitle, strCode);
                break;
            }
            default: {
                throw new HyperionException(
                        String.format("Task of type '%s' is not known!", type.getTypeName()));
            }
        }

        return task;
    }
}

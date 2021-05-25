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
package magic.system.hyperion.interfaces;

import magic.system.hyperion.components.AbstractTask;
import magic.system.hyperion.reader.DocumentReaderException;

/**
 * Interface for creating a code task (Powershell, Batch, Groovy,
 * JShell and so on).
 */
@FunctionalInterface
public interface ICodeTaskCreator {
    /**
     * Create a task with initial title and code.
     *
     * @param strTitle title of the task.
     * @param strCode code of the task (or path and filename of the script).
     * @return instance of a concrete task.
     * @throws DocumentReaderException when creating of the task has failed.
     */
    AbstractTask createTask(String strTitle, String strCode) throws DocumentReaderException;
}

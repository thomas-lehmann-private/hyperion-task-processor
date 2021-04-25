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
package magic.system.spline.components;

/**
 * Result of a task.
 *
 * @author Thomas Lehmann
 */
public class TaskResult {

    /**
     * When true the task was successful.
     */
    private final boolean bSuccess;

    /**
     * The variable with extracted content from last task (when defined).
     */
    private final Variable variable;

    /**
     * Init task result.
     *
     * @param bInitSuccess when true the task has been successful.
     * @param initVariable the extracted content from related task (when
     * defined).
     */
    public TaskResult(final boolean bInitSuccess, final Variable initVariable) {
        this.bSuccess = bInitSuccess;
        this.variable = initVariable;
    }

    /**
     * Provide success of related task.
     *
     * @return true when task has been successful.
     */
    public boolean isSuccess() {
        return this.bSuccess;
    }

    /**
     * Provide variable of task.
     *
     * @return provide extracted value from related task (when defined).
     */
    public Variable getVariable() {
        return this.variable;
    }
}

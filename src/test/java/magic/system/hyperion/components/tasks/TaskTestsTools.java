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

import magic.system.hyperion.components.Model;
import magic.system.hyperion.components.TaskParameters;
import magic.system.hyperion.components.Variable;
import magic.system.hyperion.components.WithParameters;
import magic.system.hyperion.data.StringValue;
import magic.system.hyperion.generics.Pair;

import java.util.Map;

/**
 * Test tools for tasks.
 *
 * @author Thomas Lehmann
 */
@SuppressWarnings("checkstyle:multiplestringliterals")
public final class TaskTestsTools {
    /**
     * Model test value.
     */
    public static final String MODEL_TEST_VALUE = "hello world 1!";

    /**
     * Instantiation not wanted.
     */
    private TaskTestsTools() {
        // Nothing to do.
    }

    /**
     * Provide default task parameters (no templating content).
     *
     * @return default task parameters.
     */
    public static TaskParameters getDefaultTaskParameters() {
        return TaskParameters.of(new Model(), Map.of(), Map.of(), null);
    }

    /**
     * Provide a simple setup for some concrete task parameters.
     *
     * @return simple task parameters.
     */
    public static TaskParameters getSimpleTaskParameters() {
        final var variable = new Variable();
        variable.setValue("hello world 3!");

        return TaskParameters.of(
                Model.of(Pair.of("test", StringValue.of(MODEL_TEST_VALUE))),
                Map.of("test", "hello world 2!"),
                Map.of("test", variable),
                WithParameters.of(2, StringValue.of("hello world 4!")));
    }

}

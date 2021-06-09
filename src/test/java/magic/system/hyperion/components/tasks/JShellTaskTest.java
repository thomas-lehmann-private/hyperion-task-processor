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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testing class {@link JShellTask}.
 *
 * @author Thomas Lehmann
 */
@DisplayName("Testing JShellTask class")
@TestMethodOrder(value = MethodOrderer.Random.class)
public class JShellTaskTest {
    /**
     * Testing simple example.
     */
    @Test
    public void testSimple() {
        //CHECKSTYLE.OFF: MultipleStringLiterals - ok here
        final var task = new JShellTask("test", "System.out.println(\"hello world\")");
        final var result = task.run(new TaskParameters());
        assertTrue(result.isSuccess());
        assertEquals("hello world", result.getVariable().getValue());
        //CHECKSTYLE.ON: MultipleStringLiterals
    }

    /**
     * Testing syntax error in the JShell script.
     */
    @Test
    public void testFailure() {
        //CHECKSTYLE.OFF: MultipleStringLiterals - ok here
        final var task = new JShellTask("test", "System.out.println(");
        final var result = task.run(new TaskParameters());
        assertFalse(result.isSuccess());
        //CHECKSTYLE.ON: MultipleStringLiterals
    }

    /**
     * Testing template mechanism evaluating a variable.
     */
    @Test
    public void testWithVariable() {
        //CHECKSTYLE.OFF: MultipleStringLiterals - ok here
        final var variable = new Variable();
        variable.setValue("hello world");

        final var task = new JShellTask(
                "test", "System.out.println(\"{{ variables.text.value }}\")");

        final var parameters = new TaskParameters(new Model(), Map.of(), Map.of("text", variable));
        final var result = task.run(parameters);

        assertTrue(result.isSuccess());
        assertEquals("hello world", result.getVariable().getValue());
        //CHECKSTYLE.ON: MultipleStringLiterals
    }
}

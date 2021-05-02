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
package magic.system.hyperion.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Testing class {@link TaskGroup}.
 *
 * @author Thomas Lehmann
 */
@DisplayName("Testing TaskGroup class")
public class TaskGroupTest {

    /**
     * Testing the ordered execution.
     */
    @Test
    public void testRunTaskOneAfterTheOther() {
        final var taskGroup = createTestTaskGroup(false);
        // FIXME: Document the moment but should be model.
        // FIXME: Should return TaskGroupResult (same idea as TaskResult)
        //   - isSuccess the same
        //   - what else? -> variables?
        taskGroup.run(null);
        assertEquals(taskGroup.getListOfTasks().size(),
                taskGroup.getVariables().size());
        //CHECKSTYLE.OFF: MultipleStringLiterals - not a problem here
        assertEquals("Gandalf", taskGroup.getVariables().get("name1").getValue());
        assertEquals("Frodo", taskGroup.getVariables().get("name2").getValue());
        //CHECKSTYLE.ON: MultipleStringLiterals
    }

    /**
     * Creating a test task group.
     *
     * @param bRunTasksInParallel when true then run tasks in parallel.
     * @return instance of {@link TaskGroup}.
     */
    private TaskGroup createTestTaskGroup(final boolean bRunTasksInParallel) {
        //CHECKSTYLE.OFF: MultipleStringLiterals - not a problem here
        final var taskGroup = new TaskGroup("test", bRunTasksInParallel);
        var task = new PowershellTask("test1", "Write-Host \"Gandalf\"");
        task.getVariable().setName("name1");
        taskGroup.add(task);
        task = new PowershellTask("test2", "Write-Host \"Frodo\"");
        task.getVariable().setName("name2");
        taskGroup.add(task);
        return taskGroup;
        //CHECKSTYLE.ON: MultipleStringLiterals
    }
}

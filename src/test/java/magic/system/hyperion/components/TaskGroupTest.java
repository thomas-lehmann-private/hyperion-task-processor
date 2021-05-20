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

import java.util.List;

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
        taskGroup.run(List.of());
        assertEquals(taskGroup.getListOfTasks().size(), taskGroup.getVariables().size());
        //CHECKSTYLE.OFF: MultipleStringLiterals - not a problem here
        assertEquals("Gandalf", taskGroup.getVariables().get("name1").getValue());
        assertEquals("Frodo", taskGroup.getVariables().get("name2").getValue());
        //CHECKSTYLE.ON: MultipleStringLiterals
    }

    /**
     * Testing the ordered and filtered execution.
     */
    @Test
    public void testRunTaskOneAfterTheOtherFiltered() {
        final var taskGroup = createTestTaskGroup(false);
        //CHECKSTYLE.OFF: MultipleStringLiterals - not a problem here
        taskGroup.run(List.of("groovy"));
        assertEquals(1, taskGroup.getVariables().size());
        assertEquals("Gandalf", taskGroup.getVariables().get("name1").getValue());
        //CHECKSTYLE.ON: MultipleStringLiterals
    }

    /**
     * Creating a test task group with tasks working on all platforms.
     *
     * @param bRunTasksInParallel when true then run tasks in parallel.
     * @return instance of {@link TaskGroup}.
     */
    private TaskGroup createTestTaskGroup(final boolean bRunTasksInParallel) {
        //CHECKSTYLE.OFF: MultipleStringLiterals - not a problem here
        final var taskGroup = new TaskGroup("test", bRunTasksInParallel);

        //CHECKSTYLE.OFF: MultipleStringLiterals - not a problem here
        final var task1 = new GroovyTask("test1", "println 'Gandalf'");
        task1.getVariable().setName("name1");
        task1.addTag("groovy");
        task1.addTag("test1");
        taskGroup.add(task1);

        final var task2 = new JShellTask("test2", "System.out.println(\"Frodo\");");
        task2.getVariable().setName("name2");
        task2.addTag("jshell");
        task2.addTag("test2");
        taskGroup.add(task2);
        //CHECKSTYLE.ON: MultipleStringLiterals

        return taskGroup;
        //CHECKSTYLE.ON: MultipleStringLiterals
    }
}

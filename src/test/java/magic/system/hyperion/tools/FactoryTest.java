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
package magic.system.hyperion.tools;

import magic.system.hyperion.components.tasks.AbstractTask;
import magic.system.hyperion.components.tasks.GroovyTask;
import magic.system.hyperion.components.tasks.TaskType;
import magic.system.hyperion.components.tasks.creator.ITaskCreator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testing of class {@link Factory}.
 *
 * @author Thomas Lehmann
 */
@SuppressWarnings("checkstyle:multiplestringliterals")
public class FactoryTest {
    /**
     * Testing default factory mechanism when everything should be fine.
     */
    @Test
    public void testDefault() {
        final var factory = new Factory<AbstractTask>(ITaskCreator.class);
        final var task = factory.create(TaskType.GROOVY.getTypeName());
        assertNotNull(task);
        assertTrue(task instanceof GroovyTask);
    }

    /**
     * Testing default factory mechanism when everything should be fine.
     */
    @Test
    public void testNotFound() {
        final var factory = new Factory<AbstractTask>(ITaskCreator.class);
        final var task = factory.create("should-not-exist");
        assertNull(task);
    }
}

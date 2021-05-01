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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Testing class {@link Variable}.
 *
 * @author Thomas Lehmann
 */
public class VariableTest {
    /**
     * Testing regex applied to whole string.
     */
    @Test
    public void testRegexCompleteString() {
        //CHECKSTYLE.OFF: MultipleStringLiterals - Here no constants are necessary
        final var variable = new Variable("test", "[A-Za-z]+", false);
        assertTrue(variable.setValue("!!! Gandalf !!!"));
        assertEquals("test", variable.getName());
        assertEquals("Gandalf", variable.getValue());

        assertFalse(variable.setValue("!!! 12345 !!!"));
        assertEquals("Gandalf", variable.getValue());
        //CHECKSTYLE.ON: MultipleStringLiterals
    }
    
    /**
     * Testing regex applied line by line for a string.
     */
    @Test
    public void testRegexLineByLine() {
        //CHECKSTYLE.OFF: MultipleStringLiterals - Here no constants are necessary
        final var variable = new Variable("test", "[A-Za-z]+", true);
        assertTrue(variable.setValue("!!! Gandalf !!!\n!!! Frodo !!!"));
        assertEquals("test", variable.getName());
        assertEquals("Gandalf\nFrodo", variable.getValue());
        //CHECKSTYLE.ON: MultipleStringLiterals
    }
}

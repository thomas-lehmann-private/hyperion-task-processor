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
package magic.system.hyperion.data;

import magic.system.hyperion.generics.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testing of class {@link ListOfValues}.
 */
@DisplayName("Testing of ListOfValues")
public class ListOfValuesTest {
    /**
     * Testing without adding values.
     */
    @Test
    public void testDefault() {
        final var list = new ListOfValues();
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
        assertTrue(list.getValues().isEmpty());
        assertEquals(0, list.getValues().size());
    }

    /**
     * Testing the static "of" function.
     */
    @Test
    public void testOf() {
        //CHECKSTYLE.OFF: MultipleStringLiterals - ok here.
        final var list = ListOfValues.of(StringValue.of("hello"), StringValue.of("world"));
        assertFalse(list.isEmpty());
        assertEquals(2, list.size());
        assertEquals(List.of("hello", "world").toString(), list.getValues().toString());
        //CHECKSTYLE.ON: MultipleStringLiterals
    }

    /**
     * Testing the add functionality.
     */
    @Test
    public void testAdd() {
        final var list = new ListOfValues();
        //CHECKSTYLE.OFF: MultipleStringLiterals - ok here.
        list.add(StringValue.of("hello"));
        list.add("world");

        assertFalse(list.isEmpty());
        assertEquals(2, list.size());
        assertEquals(List.of("hello", "world").toString(), list.getValues().toString());
        //CHECKSTYLE.ON: MultipleStringLiterals
    }

    /**
     * Testing of {@link ListOfValues#getString(int)}.
     */
    @Test
    public void testGetString() {
        final var list = new ListOfValues();
        //CHECKSTYLE.OFF: MultipleStringLiterals - ok here.
        list.add(StringValue.of("hello"));
        list.add("world");
        list.add(AttributeMap.of(Pair.of("description", StringValue.of("just a test"))));

        assertEquals("hello", list.getString(0));
        assertEquals("world", list.getString(1));
        assertEquals(null, list.getString(2));
        //CHECKSTYLE.ON: MultipleStringLiterals
    }
}

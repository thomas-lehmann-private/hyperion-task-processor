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
package magic.system.spline.data;

import magic.system.spline.generics.Pair;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Testing of class {@link AttributeList}.
 *
 * @author Thomas Lehmann
 */
public class AttributsListTest {

    /**
     * Testing default without adding data.
     */
    @Test
    public void testDefault() {
        final var attributList = new AttributeList();
        assertTrue(attributList.getAttributes().isEmpty(),
                "No attributes expected");
    }

    /**
     * Testing adding a string key and a string value.
     */
    @Test
    public void testStringString() {
        final var attributList = new AttributeList();
        //CHECKSTYLE.OFF: MultipleStringLiterals - not to worry here
        // adding key/value
        attributList.set("key1", "value1");
        assertEquals(1, attributList.getAttributes().size());
        assertTrue(attributList.contains("key1"));
        assertEquals("key1", attributList.getAttributes().get(0).getFirst());
        assertEquals("value1",
                attributList.getAttributes().get(0).getSecond().toString());
        // overwriting key/value
        attributList.set("key1", "value1.1");
        assertEquals(1, attributList.getAttributes().size());
        assertEquals("key1", attributList.getAttributes().get(0).getFirst());
        assertEquals("value1.1",
                attributList.getAttributes().get(0).getSecond().toString());
        // adding (sorted) key/value
        attributList.set("key0", "value0");
        assertEquals(2, attributList.getAttributes().size());
        assertEquals("key0", attributList.getAttributes().get(0).getFirst());
        assertEquals("value0",
                attributList.getAttributes().get(0).getSecond().toString());
        assertEquals("key1", attributList.getAttributes().get(1).getFirst());
        assertEquals("value1.1",
                attributList.getAttributes().get(1).getSecond().toString());
        //CHECKSTYLE.ON: MultipleStringLiterals
    }

    /**
     * Testing adding a string key and attribute list.
     */
    @Test
    public void testStringAttributList() {
        final var attributList = new AttributeList();
        //CHECKSTYLE.OFF: MultipleStringLiterals - not to worry here
        attributList.set("key1", AttributeList.of(Pair.of("key1.1",
                StringValue.of("value1.1"))));
        assertEquals(1, attributList.getAttributes().size());
        assertTrue(attributList.contains("key1"));
        assertEquals("value1.1",
                attributList.getAttributList("key1").getString("key1.1"));
        //CHECKSTYLE.ON: MultipleStringLiterals
    }
}

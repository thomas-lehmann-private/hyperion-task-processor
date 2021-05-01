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
package magic.system.hyperion.generics;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Testing class {@link Converters}.
 *
 * @author Thomas Lehmann
 */
public class ConvertersTest {

    /**
     * Test text.
     */
    private static final String WORLD = "world";

    /**
     * Test text.
     */
    private static final String HELLO = "hello";

    /**
     * Testing {@link Converters#convertToList(java.util.Iterator)}.
     */
    @Test
    public void testConvertIteratorToList() {
        final var list1 = List.of(HELLO, WORLD);
        final var iterator = list1.iterator();
        final var list2 = Converters.convertToList(iterator);
        Assertions.assertArrayEquals(list1.toArray(), list2.toArray());
    }

    /**
     * Testing {@link Converters#convertToSortedList(java.util.Iterator)}.
     */
    @Test
    public void testConvertIteratorToSortedList() {
        final var list1 = List.of(WORLD, HELLO);
        final var list2 = List.of(HELLO, WORLD);
        final var iterator = list1.iterator();
        final var list3 = Converters.convertToSortedList(iterator);
        Assertions.assertArrayEquals(list2.toArray(), list3.toArray());
    }
}

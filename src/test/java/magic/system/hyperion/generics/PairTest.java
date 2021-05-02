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

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Testing of {@link Pair}.
 *
 * @author Thomas Lehmann
 */
@DisplayName("Testing generic Pair class")
public class PairTest {

    /**
     * Test text.
     */
    public static final String SOME_TEXT = "hello world!";
    /**
     * Test number.
     */
    public static final Integer SOME_NUMBER = 1234567;

    /**
     * Instantiation via c'tor and asking for values.
     */
    @Test
    public void testConstructorAndGetters() {
        final var pair = new Pair<String, Integer>(SOME_TEXT, SOME_NUMBER);
        assertEquals(SOME_TEXT, pair.getFirst());
        assertEquals(SOME_NUMBER, pair.getSecond());
    }

    /**
     * Instantiation via "of" function and asking for values.
     */
    @Test
    public void testOf() {
        final var pair = Pair.of(SOME_TEXT, SOME_NUMBER);
        assertEquals(SOME_TEXT, pair.getFirst());
        assertEquals(SOME_NUMBER, pair.getSecond());
    }
}

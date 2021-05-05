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

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Pair of two values.
 *
 * @param <F> Type for first value
 * @param <S> Type for second value
 * @author Thomas Lehmann
 */
public class Pair<F, S> {

    /**
     * first value.
     */
    private final F first;
    /**
     * second value.
     */
    private final S second;

    /**
     * Default c'tor.
     */
    public Pair() {
        this.first = null;
        this.second = null;
    }

    /**
     * Initialize pair with two values.
     *
     * @param initFirst first value of pair.
     * @param initSecond second value of pair.
     */
    public Pair(final F initFirst, final S initSecond) {
        this.first = initFirst;
        this.second = initSecond;
    }

    /**
     * Get first value.
     *
     * @return first value.
     */
    public F getFirst() {
        return this.first;
    }

    /**
     * Get second value.
     *
     * @return second value.
     */
    public S getSecond() {
        return this.second;
    }

    /**
     * Provde pair with two values.
     *
     * @param <U> Type for first value.
     * @param <V> Type for second value.
     * @param initFirst first value of pair.
     * @param initSecond second value of pair.
     * @return pair with two values.
     */
    public static <U, V> Pair<U, V> of(final U initFirst, final V initSecond) {
        return new Pair<>(initFirst, initSecond);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(this.first)
                .append(this.second)
                .build();
    }
}

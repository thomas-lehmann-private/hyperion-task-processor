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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Different converter utilities.
 *
 * @author Thomas Lehmann
 */
public final class Converters {

    /**
     * Not intended to be called.
     */
    private Converters() {
        // Nothing to do.
    }

    /**
     * Converts an interator into a list.
     *
     * @param <E> type for iterator and list
     * @param iterator iterator over a collection.
     * @return list.
     */
    public static <E> List<E> convertToList(final Iterator<E> iterator) {
        final List<E> newList = new ArrayList<>();
        iterator.forEachRemaining(newList::add);
        return newList;
    }

    /**
     * Converts an interator into a sorted list (ascending).
     *
     * @param <E> type for iterator and list
     * @param iterator iterator over a collection.
     * @return sorted list.
     */
    public static <E extends Comparable<E>> List<E> convertToSortedList(
            final Iterator<E> iterator) {
        final List<E> newList = convertToList(iterator);
        newList.sort(Comparator.naturalOrder());
        return newList;
    }
}

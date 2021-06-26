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

import magic.system.hyperion.data.interfaces.IValueVisitor;
import magic.system.hyperion.data.interfaces.IValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Wrapper for a list.
 *
 * @author Thomas Lehmann
 */
public class ListOfValues implements IValue {
    /**
     * List of IValue elements.
     */
    private List<IValue> values;

    /**
     * Initialize list.
     */
    public ListOfValues() {
        this.values = new ArrayList<>();
    }

    /**
     * Get list of values (readonly).
     *
     * @return list of values.
     */
    public List<IValue> getValues() {
        return Collections.unmodifiableList(this.values);
    }

    /**
     * Checking for values in the list.
     *
     * @return true when there is not value in the list.
     */
    public boolean isEmpty() {
        return this.values.isEmpty();
    }

    /**
     * Get number of values in the list.
     *
     * @return number of values in the list.
     */
    public int size() {
        return this.values.size();
    }

    /**
     * Adding value to list.
     *
     * @param value new value to add.
     */
    public void add(final IValue value) {
        this.values.add(value);
    }

    /**
     * Adding a string value.
     *
     * @param value a new string value to add.
     * @version 1.0.0
     */
    public void add(final String value) {
        this.values.add(StringValue.of(value));
    }

    /**
     * Get value at index.
     *
     * @param iIndex index of value.
     * @return value if bounds are valid.
     * @version 1.0.0
     */
    public IValue get(final int iIndex) {
        return this.values.get(iIndex);
    }

    /**
     * Get the entry as string if the entry represents a string.
     *
     * @param iIndex index of entry.
     * @return string value or null when entry is not a string.
     * @version 1.0.0
     */
    public String getString(final int iIndex) {
        String strValue = null;

        final var entry = this.values.get(iIndex);
        if (entry instanceof StringValue) {
            strValue = ((StringValue) entry).getValue();
        }

        return strValue;
    }

    /**
     * Get the entry as attribute list if the entry represents an attribute list.
     *
     * @param iIndex index of entry.
     * @return attribute list or null when entry is not an attribute list.
     * @version 1.0.0
     */
    public AttributeMap getAttributeList(final int iIndex) {
        AttributeMap list = null;

        final var entry = this.values.get(iIndex);
        if (entry instanceof AttributeMap) {
            list = (AttributeMap) entry;
        }

        return list;
    }

    /**
     * Create a list from an array of values (variable arguments).
     *
     * @param arrayOfValues array of values to add.
     * @return list wrapper instance with added values.
     * @version 1.0.0
     */
    @SafeVarargs
    public static ListOfValues of(final IValue... arrayOfValues) {
        final ListOfValues newList = new ListOfValues();

        for (final var arrayOfValue : arrayOfValues) {
            newList.add(arrayOfValue);
        }

        return newList;
    }

    @Override
    public String toString() {
        return this.values.toString();
    }

    @Override
    public int hashCode() {
        return this.values.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        return this.values.equals(((ListOfValues)obj).getValues());
    }

    @Override
    public void accept(final IValueVisitor visitor) {
        this.values.forEach(visitor::visit);
    }
}

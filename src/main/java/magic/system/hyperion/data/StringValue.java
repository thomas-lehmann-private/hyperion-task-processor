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

import magic.system.hyperion.data.interfaces.IDataVisitable;
import magic.system.hyperion.data.interfaces.IDataVisitor;
import magic.system.hyperion.data.interfaces.IValue;

/**
 * Wrapper for string value.
 *
 * @author Thomas Lehmann
 */
public class StringValue implements IValue, IDataVisitable {

    /**
     * String value.
     */
    private String strValue;

    /**
     * Initialize string value.
     *
     * @param strInitValue some string value.
     */
    public StringValue(final String strInitValue) {
        this.strValue = strInitValue;
    }

    /**
     * Get string value.
     *
     * @return string value.
     */
    public String getValue() {
        return this.strValue;
    }

    /**
     * Change value.
     *
     * @param strInitValue new value.
     */
    public void setValue(final String strInitValue) {
        this.strValue = strInitValue;
    }

    /**
     * Create instance of {@link StringValue}.
     *
     * @param strInitValue string value to store.
     * @return instance of {@link StringValue}.
     */
    public static StringValue of(final String strInitValue) {
        return new StringValue(strInitValue);
    }

    @Override
    public String toString() {
        return this.strValue;
    }

    @Override
    public int hashCode() {
        return this.strValue.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        return this.strValue.equals(((StringValue) obj).getValue());
    }

    @Override
    public void accept(final IDataVisitor visitor) {
        visitor.visit(this);
    }
}

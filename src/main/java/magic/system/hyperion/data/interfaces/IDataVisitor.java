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
package magic.system.hyperion.data.interfaces;

import magic.system.hyperion.data.AttributeMap;
import magic.system.hyperion.data.ListOfValues;
import magic.system.hyperion.data.StringValue;

/**
 * Visitor for data classes.
 *
 * @author Thomas Lehmann
 */
public interface IDataVisitor {
    /**
     * Visit of an attribute map.
     *
     * @param attributeMap attribute map to visit.
     */
    void visit(AttributeMap attributeMap);

    /**
     * Visit of a list of values.
     *
     * @param listOfValues list of values to visit.
     */
    void visit(ListOfValues listOfValues);

    /**
     * Visit of a string value.
     *
     * @param stringValue string value to visit.
     */
    void visit(StringValue stringValue);

    /**
     * Visit of the generic value.
     * The interface intends to enforce that this method delegates
     * to the other visitor methods depending on concrete value.
     *
     * @param value generic value to visit.
     */
    void visit(IValue value);
}

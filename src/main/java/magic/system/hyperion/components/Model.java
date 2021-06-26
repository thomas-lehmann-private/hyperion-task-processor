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

import magic.system.hyperion.data.AttributeMap;
import magic.system.hyperion.generics.Pair;
import magic.system.hyperion.data.interfaces.IValue;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;

/**
 * Model with data required for the processing.
 *
 * @author Thomas Lehmann
 */
public class Model {

    /**
     * List of string based attributes with a value being a list of strings or
     * just a string.
     */
    final AttributeMap data;

    /**
     * Initialize model.
     */
    public Model() {
        this.data = new AttributeMap();
    }

    /**
     * Get model data.
     *
     * @return model data.
     */
    public AttributeMap getData() {
        return this.data;
    }

    /**
     * Checking whether model is empty.
     *
     * @return true when model is empty otherwise false.
     */
    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.data)
                .build();
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

        final Model other = (Model) obj;
        return new EqualsBuilder()
                .append(this.data, other.getData()).build();
    }

    /**
     * Creating an model from an array of pairs. Each pair is
     * a combination of a string key and a value implementing {@link IValue}.
     *
     * @param arrayOfPairs array of pairs.
     * @return created model.
     */
    @SafeVarargs
    public static Model of(final Pair<String, IValue>... arrayOfPairs) {
        final var model = new Model();
        List.of(arrayOfPairs).forEach(pair ->
                model.getData().set(pair.getFirst(), pair.getSecond()));
        return model;
    }
}

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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import magic.system.hyperion.generics.Pair;
import magic.system.hyperion.interfaces.IValue;

/**
 * List of sorted Attributes (by key). This class is not intended for huge
 * amount of data.
 *
 * @author Thomas Lehmann
 */
public class AttributeList implements IValue {

    /**
     * List of attributes.
     */
    private final List<Pair<String, IValue>> attributes;

    /**
     * Initialize attribute list.
     */
    public AttributeList() {
        this.attributes = new ArrayList<>();
    }

    /**
     * Get list of attributes.
     *
     * @return list of attributes.
     */
    public List<Pair<String, IValue>> getAttributes() {
        return this.attributes;
    }

    /**
     * Verifying whether attribute list does have key.
     *
     * @param strKey key to find
     * @return true when key does exist.
     */
    public boolean contains(final String strKey) {
        return this.attributes.stream().anyMatch(
                entry -> entry.getFirst().equals(strKey));
    }

    /**
     * Removes the key and its value.
     *
     * @param strKey key to search.
     */
    public void remove(final String strKey) {
        this.attributes.removeIf(entry -> entry.getFirst().equals(strKey));
    }

    /**
     * Adding a string/String attribute. An existing key/value will be removed
     * first. The list is kept sorted.
     *
     * @param strInitKey the key to add.
     * @param strInitValue the value to store.
     */
    public void set(final String strInitKey, final String strInitValue) {
        remove(strInitKey);
        this.attributes.add(Pair.of(strInitKey, StringValue.of(strInitValue)));
        this.attributes.sort(Comparator.comparing(Pair::getFirst));
    }

    /**
     * Adding an attribute with a list of attributes as value.An existing
     * key/value will be removed first. The list is kept sorted.
     *
     * @param strInitKey the key to add.
     * @param initAttributes the value to store.
     */
    public void set(final String strInitKey, final AttributeList initAttributes) {
        remove(strInitKey);
        this.attributes.add(Pair.of(strInitKey, initAttributes));
        this.attributes.sort(Comparator.comparing(Pair::getFirst));
    }

    /**
     * Adding a string/String attribute. An existing key/value will be removed
     * first. The list is kept sorted.
     *
     * @param strInitKey the key to add.
     * @param initValue the value to store.
     */
    private void set(final String strInitKey, final IValue initValue) {
        remove(strInitKey);
        this.attributes.add(Pair.of(strInitKey, initValue));
        this.attributes.sort(Comparator.comparing(Pair::getFirst));
    }

    /**
     * Provide attribut list for given key if value is one.
     *
     * @param strKey key to search.
     * @return attribut list instance if key has been found and value is an
     * attribute list.
     */
    public AttributeList getAttributList(final String strKey) {
        AttributeList attributeList = null;
        for (var entry : this.attributes) {
            if (entry.getFirst().equals(strKey)) {
                if (entry.getSecond() instanceof AttributeList) {
                    attributeList = (AttributeList) entry.getSecond();
                }
                break;
            }
        }
        return attributeList;
    }

    /**
     * Provide string value for given key when value is a string.
     *
     * @param strKey key to search.
     * @return string value when key has been found and key is a string.
     */
    public String getString(final String strKey) {
        String strValue = null;
        for (var entry : this.attributes) {
            if (entry.getFirst().equals(strKey)) {
                if (entry.getSecond() instanceof StringValue) {
                    strValue = ((StringValue) entry.getSecond()).getValue();
                }
                break;
            }
        }
        return strValue;
    }

    /**
     * Creating an attribut list from an array of pairs.
     *
     * @param arrayOfPairs array of pairs.
     * @return created attribut list.
     */
    @SafeVarargs
    public static AttributeList of(final Pair<String, IValue>... arrayOfPairs) {
        final var attributeList = new AttributeList();
        List.of(arrayOfPairs).forEach(pair ->
                attributeList.set(pair.getFirst(), pair.getSecond()));
        return attributeList;
    }
}

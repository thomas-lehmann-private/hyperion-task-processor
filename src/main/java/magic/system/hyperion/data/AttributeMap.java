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

import magic.system.hyperion.generics.Pair;
import magic.system.hyperion.interfaces.IValue;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Sorted map of key/value.
 *
 * @author Thomas Lehmann
 */
public class AttributeMap implements IValue {

    /**
     * List of attributes.
     */
    private final Map<String, IValue> attributes;

    /**
     * Initialize attribute list.
     */
    public AttributeMap() {
        this.attributes = new TreeMap<>();
    }

    /**
     * Get attributes.
     *
     * @return attributes.
     */
    public Map<String, IValue> getAttributes() {
        return this.attributes;
    }

    /**
     * Checking whether map is empty.
     *
     * @return true when map is empty otherwise false.
     */
    public boolean isEmpty() {
        return this.attributes.isEmpty();
    }

    /**
     * Get number of entries in the map.
     *
     * @return number of entries in the map.
     */
    public int size() {
        return this.attributes.size();
    }

    /**
     * Removes all attributes.
     */
    public void clear() {
        this.attributes.clear();
    }

    /**
     * Adding all attributes to the map. Existing keys might be overwritten.
     *
     * @param initAttributeMap the attributes to add.
     */
    public void addAll(final AttributeMap initAttributeMap) {
        initAttributeMap.getAttributes().forEach(this.attributes::put);
    }

    /**
     * Verifying whether map does have key.
     *
     * @param strKey key to find
     * @return true when key does exist.
     */
    public boolean contains(final String strKey) {
        return this.attributes.containsKey(strKey);
    }

    /**
     * Removes the key and its value.
     *
     * @param strKey key to search.
     */
    public void remove(final String strKey) {
        this.attributes.remove(strKey);
    }

    /**
     * Adding a string/String attribute. An existing key/value will be removed
     * first. The list is kept sorted.
     *
     * @param strInitKey the key to add.
     * @param strInitValue the value to store.
     */
    public void set(final String strInitKey, final String strInitValue) {
        this.attributes.put(strInitKey, StringValue.of(strInitValue));
    }

    /**
     * Adding an attribute with a list of attributes as value.An existing
     * key/value will be removed first. The list is kept sorted.
     *
     * @param strInitKey the key to add.
     * @param initAttributes the value to store.
     */
    public void set(final String strInitKey, final AttributeMap initAttributes) {
        this.attributes.put(strInitKey, initAttributes);
    }

    /**
     * Adding a string/String attribute. An existing key/value will be removed
     * first. The list is kept sorted.
     *
     * @param strInitKey the key to add.
     * @param initValue the value to store.
     */
    public void set(final String strInitKey, final IValue initValue) {
        this.attributes.put(strInitKey, initValue);
    }

    /**
     * Provide attribute map for given key if value is one.
     *
     * @param strKey key to search.
     * @return attribute list instance if key has been found and value is an
     * attribute list.
     */
    public AttributeMap getAttributeList(final String strKey) {
        AttributeMap attributeList = null;

        final var value = this.attributes.get(strKey);
        if (value instanceof AttributeMap) {
            attributeList = (AttributeMap) value;
        }

        return attributeList;
    }

    /**
     * Provide string value for given key when value is a string.
     *
     * @param strKey key to search.
     * @return string value when key has been found and value is a string.
     */
    public String getString(final String strKey) {
        String strValue = null;

        final var value = this.attributes.get(strKey);
        if (value instanceof StringValue) {
            strValue = ((StringValue) value).getValue();
        }

        return strValue;
    }

    /**
     * Provide list value for given key when value is a list.
     *
     * @param strKey key to search.
     * @return list when key been found and value is a list.
     */
    public ListOfValues getList(final String strKey) {
        ListOfValues list = null;

        final var value = this.attributes.get(strKey);
        if (value instanceof ListOfValues) {
            list = (ListOfValues) value;
        }

        return list;
    }

    /**
     * Get value for key.
     *
     * @param strKey key to search
     * @return found value.
     */
    public IValue get(final String strKey) {
        return this.attributes.get(strKey);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(ToStringStyle.SHORT_PREFIX_STYLE)
                .append(this.attributes).build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.attributes)
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

        final AttributeMap other = (AttributeMap) obj;
        return new EqualsBuilder()
                .append(this.attributes, other.getAttributes()).build();
    }

    /**
     * Creating an attribute list from an array of pairs.
     *
     * @param arrayOfPairs array of pairs.
     * @return created attribute list.
     */
    @SafeVarargs
    public static AttributeMap of(final Pair<String, IValue>... arrayOfPairs) {
        final var attributeList = new AttributeMap();
        List.of(arrayOfPairs).forEach(pair ->
                attributeList.set(pair.getFirst(), pair.getSecond()));
        return attributeList;
    }
}

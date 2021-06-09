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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testing of class {@link AttributeMap}.
 *
 * @author Thomas Lehmann
 */
@DisplayName("Testing AttributeMap class")
@TestMethodOrder(value = MethodOrderer.Random.class)
@SuppressWarnings("checkstyle:multiplestringliterals")
public class AttributeMapTest {

    /**
     * Testing default without adding data.
     */
    @Test
    public void testDefault() {
        final var attributeList = new AttributeMap();
        assertTrue(attributeList.getAttributes().isEmpty(),
                "No attributes expected");

        assertNull(attributeList.getString("Unknown"));
        assertNull(attributeList.getAttributeList("Unknown"));
        assertNull(attributeList.getList("Unknown"));
    }

    /**
     * Testing adding a string key and a string value.
     *
     * @param attributes list of attributes to add.
     * @param expectedValues expected values in order
     */
    @ParameterizedTest(name = "#{index} - attributes to add: {0} - expected values: {1}")
    @MethodSource("provideWithStringsData")
    public void testWithStrings(final List<Attribute> attributes,
                                final List<String> expectedValues) {
        final var attributeMap = new AttributeMap();

        for (final var attribute : attributes) {
            attributeMap.set(attribute.getFirst(), attribute.getSecond());
        }

        for (final var attribute : attributeMap.getAttributes().entrySet()) {
            assertEquals(((StringValue)attribute.getValue()).getValue(),
                    attributeMap.getString(attribute.getKey()));
        }
    }

    /**
     * Testing adding a string key and attribute list.
     */
    @Test
    public void testStringAttributeList() {
        final var attributeList = new AttributeMap();
        attributeList.set("key1", AttributeMap.of(Pair.of("key1.1",
                StringValue.of("value1.1"))));
        assertEquals(1, attributeList.getAttributes().size());
        assertTrue(attributeList.contains("key1"));
        assertEquals("value1.1",
                attributeList.getAttributeList("key1").getString("key1.1"));
    }

    /**
     * Provide test data for testWithString method.
     *
     * @return test data.
     */
    private static Stream<Arguments> provideWithStringsData() {
        return Stream.of(
                Arguments.of(List.of(Attribute.of("key1", StringValue.of("value1"))),
                        List.of("value1")),
                Arguments.of(List.of(Attribute.of("key1", StringValue.of("value1")),
                        Attribute.of("key0", StringValue.of("value2"))),
                        List.of("value2", "value1")),
                Arguments.of(List.of(Attribute.of("key1", StringValue.of("value1")),
                        Attribute.of("key1", StringValue.of("value2"))),
                        List.of("value2"))
        );
    }
}

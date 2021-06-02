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

import magic.system.hyperion.data.StringValue;
import magic.system.hyperion.generics.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Testing class {@link Document}.
 *
 * @author Thomas Lehmann
 */
@DisplayName("Testing Document")
@SuppressWarnings("checkstyle:multiplestringliterals")
public class DocumentTest {
    /**
     * Testing {@link Document#hashCode()}.
     *
     * @param bExpectedToBeEqual when true both hashCode should be equal.
     * @param documentA          one document.
     * @param documentB          another document.
     */
    @ParameterizedTest(name = "#{index} - equals?: {0}, documentA: {1}, documentB: {2}")
    @MethodSource("provideEqualsHasCodeTestData")
    public void testHashCode(final boolean bExpectedToBeEqual, final Document documentA,
                             final Document documentB) {
        if (bExpectedToBeEqual) {
            assertEquals(documentA.hashCode(), documentB.hashCode());
        } else {
            assertNotEquals(documentA.hashCode(), documentB.hashCode());
        }
    }

    /**
     * Testing {@link Document#equals(Object)}.
     *
     * @param bExpectedToBeEqual when true both documents should be equal.
     * @param documentA          one document.
     * @param documentB          another document.
     */
    @ParameterizedTest(name = "#{index} - equals?: {0}, documentA: {1}, documentB: {2}")
    @MethodSource("provideEqualsHasCodeTestData")
    public void testEquals(final boolean bExpectedToBeEqual, final Document documentA,
                           final Document documentB) {
        if (bExpectedToBeEqual) {
            assertEquals(documentA, documentB);
        } else {
            assertNotEquals(documentA, documentB);
        }
    }

    /**
     * Provide tests data for testing "equals" and "hashCode".
     *
     * @return list of data.
     */
    private static Stream<Arguments> provideEqualsHasCodeTestData() {
        return Stream.of(
                Arguments.of(true, createDocument(null), createDocument(null)),
                Arguments.of(false, createDocument(null),
                        createDocument(Model.of(Pair.of("key1", StringValue.of("value1"))))),
                Arguments.of(true,
                        createDocument(Model.of(Pair.of("key1", StringValue.of("value1")))),
                        createDocument(Model.of(Pair.of("key1", StringValue.of("value1")))))
        );
    }

    /**
     * Creating a test document.
     *
     * @param model a model or null.
     * @return new document instance.
     */
    private static Document createDocument(final Model model) {
        final var document = new Document();
        if (model != null) {
            document.getModel().getData().clear();
            document.getModel().getData().addAll(model.getData());
        }
        return document;
    }
}

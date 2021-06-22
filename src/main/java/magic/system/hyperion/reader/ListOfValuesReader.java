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
package magic.system.hyperion.reader;

import com.fasterxml.jackson.databind.JsonNode;
import magic.system.hyperion.data.AttributeMap;
import magic.system.hyperion.data.ListOfValues;
import magic.system.hyperion.exceptions.HyperionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reader for a list of values.
 *
 * @author Thomas Lehmann
 */
public class ListOfValuesReader implements INodeReader {
    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ListOfValuesReader.class);

    /**
     * List of values (valid instance provided from outside).
     */
    private final ListOfValues listOfValues;

    /**
     * Initializing reader with an instance of {@link ListOfValues}.
     *
     * @param initListOfValues instance of {@link ListOfValues}.
     */
    public ListOfValuesReader(final ListOfValues initListOfValues) {
            this.listOfValues = initListOfValues;
    }

    @Override
    public void read(JsonNode node) throws HyperionException {
        final var iter = node.elements();

        while (iter.hasNext()) {
            final var currentElement = iter.next();
            switch (currentElement.getNodeType()) {
                case STRING:
                case NUMBER:
                case BOOLEAN: {
                    this.listOfValues.add(currentElement.asText());
                    break;
                }
                case OBJECT: {
                    final var newAttributeMap = new AttributeMap();
                    new AttributeMapReader(newAttributeMap).read(currentElement);
                    this.listOfValues.add(newAttributeMap);
                    break;
                }
                default: {
                    LOGGER.warn(DocumentReaderMessage.NODE_TYPE_NOT_SUPPORTED.getMessage(),
                            currentElement.getNodeType());
                }
            }
        }
    }
}

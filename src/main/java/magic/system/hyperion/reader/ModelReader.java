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
import magic.system.hyperion.components.Model;
import magic.system.hyperion.data.AttributeMap;
import magic.system.hyperion.data.ListOfValues;
import magic.system.hyperion.exceptions.HyperionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reader for the model part of the document.
 *
 * @author Thomas Lehmann
 */
public class ModelReader implements INodeReader {
    /**
     * Logger for this class.
     */
    private static  final Logger LOGGER = LoggerFactory.getLogger(ModelReader.class);

    /**
     * Model to fill.
     */
    private final Model model;

    /**
     * Initialize reader with empty model.
     *
     * @param initModel model to fill.
     * @since 1.0.0
     */
    public ModelReader(final Model initModel) {
        this.model = initModel;
    }

    @Override
    public void read(JsonNode node) throws HyperionException {
        this.model.getData().addAll(readAttributeMap(node));
    }

    /**
     * Reading attributes.
     *
     * @param node where to find the attributes.
     * @return attributes.
     */
    private AttributeMap readAttributeMap(final JsonNode node) {
        final AttributeMap newAttributeMap = new AttributeMap();
        final var iter = node.fields();

        while (iter.hasNext()) {
            final var currentField = iter.next();
            switch (currentField.getValue().getNodeType()) {
                case STRING:
                case NUMBER:
                case BOOLEAN: {
                    newAttributeMap.set(currentField.getKey(), currentField.getValue().asText());
                    break;
                }
                case OBJECT: {
                    newAttributeMap.set(currentField.getKey(),
                            readAttributeMap(currentField.getValue()));
                    break;
                }
                case ARRAY: {
                    newAttributeMap.set(currentField.getKey(),
                            readListOfValues(currentField.getValue()));
                    break;
                }
                default: {
                    LOGGER.warn(DocumentReaderMessage.NODE_TYPE_NOT_SUPPORTED.getMessage(),
                            currentField.getValue().getNodeType());
                }
            }
        }

        return newAttributeMap;
    }

    /**
     * Reading a list.
     *
     * @param node the node where to read the list.
     * @return list of values.
     */
    private ListOfValues readListOfValues(final JsonNode node) {
        final var newList = new ListOfValues();
        final var iter = node.elements();

        while (iter.hasNext()) {
            final var currentElement = iter.next();
            switch (currentElement.getNodeType()) {
                case STRING:
                case NUMBER:
                case BOOLEAN: {
                    newList.add(currentElement.asText());
                    break;
                }
                case OBJECT: {
                    newList.add(readAttributeMap(currentElement));
                    break;
                }
                default: {
                    LOGGER.warn(DocumentReaderMessage.NODE_TYPE_NOT_SUPPORTED.getMessage(),
                            currentElement.getNodeType());
                }
            }
        }

        return newList;
    }
}

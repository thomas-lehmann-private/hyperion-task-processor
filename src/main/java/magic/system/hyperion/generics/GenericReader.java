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
package magic.system.hyperion.generics;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reading from String or file for given type.
 *
 * @param <E> Type to handle read for.
 *
 * @author Thomas Lehmann
 */
public final class GenericReader<E> {
    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericReader.class);

    /**
     * The class required for read.
     */
    private final Class<E> classOfE;

    /**
     * Initialize the reader with concrete class.
     *
     * @param initClassOfE the class required for read.
     */
    public GenericReader(final Class<E> initClassOfE) {
        this.classOfE = initClassOfE;
    }

    /**
     * Providing (when possible) an instance of type E read from given YAML
     * string.
     *
     * @param strContent the YAML string.
     * @return instance of type E.
     */
    public E fromYAML(final String strContent) {
        E value;
        final var mapper = new ObjectMapper(new YAMLFactory());

        try {
            value = mapper.readValue(strContent, this.classOfE);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage(), e);
            value = null;
        }
        return value;
    }
}

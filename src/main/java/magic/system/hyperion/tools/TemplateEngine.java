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
package magic.system.hyperion.tools;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.StringLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * Wrapper for concrete template engine. It's rendering a string (not file).
 *
 * @author Thomas Lehmann
 */
public class TemplateEngine {

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(
            TemplateEngine.class);

    /**
     * Concrete template engine.
     */
    private final PebbleEngine engine;

    /**
     * Initialize concrete template engine.
     * @since 1.0.0
     */
    public TemplateEngine() {
        // By default it does render from file but string is wanted here:
        this.engine = new PebbleEngine.Builder().loader(new StringLoader()).build();
    }

    /**
     * Rendering template with given data.
     *
     * @param strTemplate concrete template code.
     * @param context variables.
     * @return rendered text or original if failed.
     * @since 1.0.0
     */
    public String render(final String strTemplate, final Map<String, Object> context) {
        final var compiledTemplated = engine.getTemplate(strTemplate);
        final var writer = new StringWriter();
        var result = strTemplate;
        try {
            compiledTemplated.evaluate(writer, context);
            result = writer.toString();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }
}

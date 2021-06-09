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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testing class {@link TemplateEngine}.
 *
 * @author Thomas Lehmann
 */
@DisplayName("Testing TemplateEngine class")
@TestMethodOrder(value = MethodOrderer.Random.class)
public class TemplateEngineTest {

    /**
     * Test text.
     */
    private static final String HELLO_WORLD = "hello world";

    /**
     * Test key.
     */
    private static final String TEXT_KEY = "text";

    /**
     * Test key.
     */
    private static final String MODEL_KEY = "model";

    /**
     * Testing a very simple hello world example.
     */
    @Test
    public void testSimple() {
        final var engine = new TemplateEngine();
        final var result = engine.render("{{ text }}", Map.of(TEXT_KEY, HELLO_WORLD));
        assertEquals(HELLO_WORLD, result);
    }

    /**
     * Testing loop in template.
     */
    @Test
    public void testLoop() {
        final var engine = new TemplateEngine();
        final var result = engine.render(
                "{% for text in model %}{{ text }}{% endfor %}", Map.of(
                        MODEL_KEY,
                        List.of("hello", "world")));
        assertEquals("helloworld", result);
    }

    /**
     * Testing hierarchical access to object.
     */
    @Test
    public void testHierarchy() {
        final var engine = new TemplateEngine();
        final var result = engine.render("{{ model.text }}", Map.of(MODEL_KEY,
                Map.of(TEXT_KEY, HELLO_WORLD)));
        assertEquals(HELLO_WORLD, result);
    }
}

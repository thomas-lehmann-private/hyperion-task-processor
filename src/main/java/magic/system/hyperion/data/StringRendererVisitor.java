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

import magic.system.hyperion.components.TaskParameters;
import magic.system.hyperion.data.interfaces.IDataVisitor;
import magic.system.hyperion.data.interfaces.IValue;
import magic.system.hyperion.tools.TemplateEngine;

import java.util.Map;

/**
 * Rendering strings.
 */
public class StringRendererVisitor implements IDataVisitor {
    /**
     * Templating context.
     */
    private final Map<String, Object> templatingContext;

    /**
     * Template engine for rendering.
     */
    private final TemplateEngine engine;

    /**
     * Initialize templating context and template engine.
     *
     * @param parameters task parameters with templating context.
     * @since 1.0.0
     */
    public StringRendererVisitor(final TaskParameters parameters) {
        this.templatingContext = parameters.getTemplatingContext();
        this.engine = new TemplateEngine();
    }

    @Override
    public void visit(final AttributeMap attributeMap) {
        attributeMap.accept(this);
    }

    @Override
    public void visit(final ListOfValues listOfValues) {
        listOfValues.accept(this);
    }

    @Override
    public void visit(final StringValue stringValue) {
        stringValue.setValue(engine.render(stringValue.getValue(), this.templatingContext));
    }

    @Override
    public void visit(final IValue value) {
        if (value instanceof StringValue) {
            visit((StringValue) value);
        } else if (value instanceof ListOfValues) {
            visit((ListOfValues) value);
        } else if (value instanceof AttributeMap) {
            visit((AttributeMap) value);
        }
    }
}

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
package magic.system.hyperion.components.tasks;

import magic.system.hyperion.components.Component;
import magic.system.hyperion.components.TaskParameters;
import magic.system.hyperion.components.TaskResult;
import magic.system.hyperion.components.Variable;
import magic.system.hyperion.data.ListOfValues;
import magic.system.hyperion.interfaces.ICopyable;
import magic.system.hyperion.interfaces.IRunnable;
import magic.system.hyperion.tools.TemplateEngine;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A task represents a a concrete task represented as file (script) or inline
 * code.
 *
 * @author Thomas Lehmann
 */
public abstract class AbstractTask extends Component
        implements IRunnable<TaskResult, TaskParameters>, ICopyable<AbstractTask> {
    /**
     * Logger of this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTask.class);

    /**
     * Storing the task result.
     */
    private final Variable variable;

    /**
     * List of tags to be able to filter tasks.
     */
    private final List<String> tags;

    /**
     * Values provided via "with" attribute for repeating task for each entry.
     */
    private ListOfValues withValues;

    /**
     * Initialize task with defaults.
     *
     * @param strInitTitle - title of the task.
     * @since 1.0.0
     */
    public AbstractTask(final String strInitTitle) {
        super(strInitTitle);
        this.variable = new Variable();
        this.variable.setName("default");
        this.tags = new ArrayList<>();
        this.withValues = new ListOfValues();
    }

    /**
     * Provide variable representing task result.
     *
     * @return variable.
     * @since 1.0.0
     */
    public Variable getVariable() {
        return this.variable;
    }

    /**
     * Get list of tags (read only).
     *
     * @return list of tags.
     * @since 1.0.0
     */
    public List<String> getTags() {
        return Collections.unmodifiableList(this.tags);
    }

    /**
     * Adding a tag that doesn't exist yet.
     *
     * @param strTag new tag to add.
     * @since 1.0.0
     */
    public void addTag(final String strTag) {
        if (!this.tags.contains(strTag)) {
            this.tags.add(strTag);
        }
    }

    /**
     * Changing "with" values (if not null).
     *
     * @param initWithValues new values.
     * @since 1.0.0
     */
    public void setWithValues(final ListOfValues initWithValues) {
        if (initWithValues != null) {
            this.withValues = initWithValues;
        }
    }

    /**
     * Get "with" values. When not specified it's an empty list.
     *
     * @return "with" values.
     * @since 1.0.0
     */
    public ListOfValues getWithValues() {
        return this.withValues;
    }

    /**
     * Logging of rendered title.
     *
     * @param parameters task parameters to be use for rendering final title.
     */
    protected void logTitle(final TaskParameters parameters) {
        if (!getTitle().isEmpty()) {
            final var engine = new TemplateEngine();
            final var strRenderedTitle = engine.render(
                    getTitle(), parameters.getTemplatingContext());
            LOGGER.info("Running task '{}'", strRenderedTitle);
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.getTitle())
                .append(this.variable)
                .append(this.tags)
                .append(this.withValues)
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

        final AbstractTask other = (AbstractTask) obj;
        return new EqualsBuilder()
                .append(this.getTitle(), other.getTitle())
                .append(this.variable, other.getVariable())
                .append(this.tags, other.getTags())
                .append(this.withValues, other.getWithValues())
                .build();
    }
}

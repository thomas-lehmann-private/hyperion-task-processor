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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Document containing the tasks.
 *
 * @author Thomas Lehmann
 */
public class Document {

    /**
     * The one and only model in a document.
     */
    private final Model model;

    /**
     * List of task groups.
     */
    private final List<TaskGroup> listOfTaskGroups;

    /**
     * Initialize document.
     */
    public Document() {
        this.listOfTaskGroups = new ArrayList<>();
        this.model = new Model();
    }

    /**
     * Get the model.
     *
     * @return model.
     */
    public Model getModel() {
        return this.model;
    }

    /**
     * Provide list of task groups.
     *
     * @return list of task groups.
     */
    public List<TaskGroup> getListOfTaskGroups() {
        return Collections.unmodifiableList(this.listOfTaskGroups);
    }

    /**
     * Adding a task group.
     *
     * @param taskGroup a new task group.
     */
    public void add(final TaskGroup taskGroup) {
        this.listOfTaskGroups.add(taskGroup);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.listOfTaskGroups).build();
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

        final Document other = (Document) obj;
        return new EqualsBuilder()
                .append(this.listOfTaskGroups, other.getListOfTaskGroups()).build();
    }
}

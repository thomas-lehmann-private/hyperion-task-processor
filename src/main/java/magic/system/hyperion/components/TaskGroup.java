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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import magic.system.hyperion.interfaces.IRunnable;
import magic.system.hyperion.interfaces.IVariable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A group of tasks.
 *
 * @author Thomas Lehmann
 */
public class TaskGroup extends Component implements IRunnable<Boolean, Document> {
    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskGroup.class);

    /**
     * Variable storage per task group accessible to the individual tasks.
     */
    private final Map<String, IVariable> variables;

    /**
     * List of tasks.
     */
    private final List<AbstractTask> listOfTasks;

    /**
     * When true then run tasks in parallel.
     */
    private final boolean bRunTasksInParallel;

    /**
     * Initialize task group.
     *
     * @param strInitTitle title of the group.
     * @param bInitRunTasksInParallel when true then run tasks in parallel.
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public TaskGroup(@JsonProperty("title") final String strInitTitle,
            @JsonProperty("runTasksInParallel") final boolean bInitRunTasksInParallel) {
        super(strInitTitle);
        this.variables = new ConcurrentHashMap<>();
        this.listOfTasks = new ArrayList<>();
        this.bRunTasksInParallel = bInitRunTasksInParallel;
    }

    /**
     * Readonly access to variables.
     *
     * @return variables.
     */
    @JsonIgnore
    public Map<String, IVariable> getVariables() {
        return Collections.unmodifiableMap(this.variables);
    }

    /**
     * Provide list of tasks.
     *
     * @return list of tasks.
     */
    public List<AbstractTask> getListOfTasks() {
        return Collections.unmodifiableList(this.listOfTasks);
    }

    /**
     * Provide whether to run the tasks in parallel.
     *
     * @return when true then run the tasks in parallel.
     */
    public boolean isRunTasksInParallel() {
        return this.bRunTasksInParallel;
    }

    /**
     * Adding new task.
     *
     * @param task new task.
     */
    public void add(final AbstractTask task) {
        this.listOfTasks.add(task);
    }

    @Override
    public Boolean run(Document input) {
        boolean success = true;

        if (!this.bRunTasksInParallel) {
            for (var task : this.listOfTasks) {
                final var result = task.run(this.variables);
                this.variables.put(result.getVariable().getName(), result.getVariable());
                LOGGER.info(String.format("set variable %s=%s",
                        result.getVariable().getName(), result.getVariable().getValue()));
                if (!result.isSuccess()) {
                    success = false;
                }
            }
        }
        return success;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.getTitle())
                .append(this.listOfTasks)
                .append(this.bRunTasksInParallel)
                .build();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final TaskGroup other = (TaskGroup) obj;
        return new EqualsBuilder()
                .append(this.getTitle(), other.getTitle())
                .append(this.bRunTasksInParallel, other.isRunTasksInParallel())
                .append(this.listOfTasks, other.getListOfTasks()).build();
    }

}

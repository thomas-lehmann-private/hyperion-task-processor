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

import magic.system.hyperion.components.tasks.AbstractTask;
import magic.system.hyperion.exceptions.HyperionException;
import magic.system.hyperion.generics.SimplePublisher;
import magic.system.hyperion.interfaces.IRunnable;
import magic.system.hyperion.interfaces.IVariable;
import magic.system.hyperion.tools.Runner;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A group of tasks. The list of task can run in order or in parallel.
 *
 * @author Thomas Lehmann
 */
@SuppressWarnings("checkstyle:classfanoutcomplexity")  // will be fixed later
public class TaskGroup extends Component
        implements IRunnable<Boolean, TaskGroupParameters> {
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
     * Publisher for changed variables.
     */
    private final SimplePublisher<IVariable> variablePublisher;

    /**
     * Initialize task group.
     *
     * @param strInitTitle            title of the group.
     * @param bInitRunTasksInParallel when true then run tasks in parallel.
     * @since 1.0.0
     */
    public TaskGroup(final String strInitTitle,
                     final boolean bInitRunTasksInParallel) {
        super(strInitTitle);
        this.variables = new ConcurrentHashMap<>();
        this.listOfTasks = new ArrayList<>();
        this.bRunTasksInParallel = bInitRunTasksInParallel;
        this.variablePublisher = new SimplePublisher<>();
    }

    /**
     * Readonly access to variables.
     *
     * @return variables.
     * @since 1.0.0
     */
    public Map<String, IVariable> getVariables() {
        return Collections.unmodifiableMap(this.variables);
    }

    /**
     * Provide list of tasks.
     *
     * @return list of tasks.
     * @since 1.0.0
     */
    public List<AbstractTask> getListOfTasks() {
        return Collections.unmodifiableList(this.listOfTasks);
    }

    /**
     * Provide whether to run the tasks in parallel.
     *
     * @return when true then run the tasks in parallel.
     * @since 1.0.0
     */
    public boolean isRunTasksInParallel() {
        return this.bRunTasksInParallel;
    }

    /**
     * Get publisher for variable changes.
     *
     * @return variable publisher.
     * @since 1.0.0
     */
    public SimplePublisher<IVariable> getVariablePublisher() {
        return this.variablePublisher;
    }

    /**
     * Adding new task.
     *
     * @param task new task.
     * @since 1.0.0
     */
    public void add(final AbstractTask task) {
        this.listOfTasks.add(task);
    }

    @Override
    public Boolean run(final TaskGroupParameters parameters) {
        final var errorCounter = new AtomicInteger(0);
        final Map<String, Integer> variableNamesMap = new TreeMap<>();
        final var runnables = getRunnables(parameters, errorCounter, variableNamesMap);

        if (bRunTasksInParallel) {
            variableNamesMap.entrySet().forEach(entry -> {
                if (entry.getValue() > 1) {
                    LOGGER.warn("Variable with name '{}' is used {} times!",
                            entry.getKey(), entry.getValue());
                }
            });
        }

        final var runner = Runner.of(runnables.toArray(Runnable[]::new));
        runner.setTimeout(parameters.getDocumentParameters().getTimeoutTaskgroup());
        runner.setParallel(this.bRunTasksInParallel);

        try {
            runner.runAll();
        } catch (final HyperionException e) {
            LOGGER.error(e.getMessage(), e);
            errorCounter.incrementAndGet();
        }

        return errorCounter.get() == 0;
    }

    /**
     * Get list of runnables (for running run all tasks in order in one thread or run all
     * tasks in parallel).
     *
     * @param parameters       model and matrix parameters.
     * @param errorCounter     counter for errors.
     * @param variableNamesMap counter for duplicate variable names.
     * @return list of runnables.
     */
    private List<Runnable> getRunnables(final TaskGroupParameters parameters,
                                        final AtomicInteger errorCounter,
                                        final Map<String, Integer> variableNamesMap) {
        final List<Runnable> runnables = new ArrayList<>();
        final var tags = parameters.getDocumentParameters().getTags();

        for (var task : this.listOfTasks) {
            // ignore task when its tags do not match the filter (if the task does
            // not have tags the task is also ignored)
            if (!tags.isEmpty() && task.getTags().stream().noneMatch(tags::contains)) {
                continue;
            }

            if (task.getWithValues().isEmpty()) {
                runnables.add(() -> runOneTask(parameters, null, task, errorCounter));
            } else {
                for (int iSubTask = 0; iSubTask < task.getWithValues().size(); ++iSubTask) {
                    final var withParameters
                            = WithParameters.of(iSubTask, task.getWithValues().get(iSubTask));
                    runnables.add(() -> runOneTask(parameters, withParameters,
                            task.copy(), errorCounter));
                }
            }

            if (variableNamesMap.containsKey(task.getVariable().getName())) {
                variableNamesMap.put(task.getVariable().getName(),
                        variableNamesMap.get(task.getVariable().getName()) + 1);
            } else {
                variableNamesMap.put(task.getVariable().getName(), 1);
            }
        }
        return runnables;
    }

    /**
     * Running one task (might run in a thread).
     *
     * @param parameters     model and the matrix parameters.
     * @param withParameters current index and current value of "with values" or null
     *                       if attribute has not been specified.
     * @param task           the concrete task to run.
     * @param errorCounter   the counter to increment on error.
     */
    private void runOneTask(final TaskGroupParameters parameters,
                            final WithParameters withParameters,
                            final AbstractTask task, final AtomicInteger errorCounter) {
        final var result = task.run(TaskParameters.of(
                parameters.getModel(), parameters.getMatrixParameters(),
                this.variables, withParameters));

        final var copiedVariable = result.getVariable().copy();
        this.variables.put(copiedVariable.getName(), copiedVariable);
        LOGGER.info(String.format("set variable %s=%s",
                copiedVariable.getName(), copiedVariable.getValue()));
        this.variablePublisher.submit(copiedVariable);

        if (!result.isSuccess()) {
            errorCounter.incrementAndGet();
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.getTitle())
                .append(this.variables)
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
                .append(this.variables, other.getVariables())
                .append(this.bRunTasksInParallel, other.isRunTasksInParallel())
                .append(this.listOfTasks, other.getListOfTasks()).build();
    }

}

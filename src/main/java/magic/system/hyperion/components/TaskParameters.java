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

import magic.system.hyperion.interfaces.IVariable;

import java.util.Map;
import java.util.TreeMap;

/**
 * Task run parameters.
 *
 * @author Thomas Lehmann
 */
public class TaskParameters {
    /**
     * Model (usually a reference to the model of the document).
     */
    private final Model model;

    /**
     * Variables (usually a reference to the variables of the task group).
     */
    private Map<String, IVariable> variables;

    /**
     * Initialize task parameters (empty model, no variables).
     * @version 1.0.0
     */
    public TaskParameters() {
        this.model = new Model();
        this.variables = new TreeMap<>();
    }

    /**
     * Initialize task parameters.
     *
     * @param initModel model to use.
     * @param initVariables variables to use.
     */
    public TaskParameters(final Model initModel, final Map<String, IVariable> initVariables) {
        this.model = initModel;
        this.variables = initVariables;
    }

    /**
     * Get model.
     *
     * @return model.
     * @version 1.0.0
     */
    public Model getModel() {
        return this.model;
    }

    /**
     * Get variables.
     *
     * @return variables.
     * @version 1.0.0
     */
    public Map<String, IVariable> getVariables() {
        return this.variables;
    }

    /**
     * Creating a task parameters instance.
     *
     * @param model mode to use.
     * @param variables variables to use.
     * @return task parameters instance.
     * @version 1.0.0
     */
    public static TaskParameters of(final Model model, final Map<String, IVariable> variables) {
        return new TaskParameters(model, variables);
    }
}

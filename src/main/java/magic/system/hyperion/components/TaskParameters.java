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

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Task run parameters.
 *
 * @author Thomas Lehmann
 */
public final class TaskParameters {
    /**
     * Model (usually a reference to the model of the document).
     */
    private final Model model;

    /**
     * Matrix parameters.
     */
    private final Map<String, String> matrixParameters;

    /**
     * Variables (usually a reference to the variables of the task group).
     */
    private final Map<String, IVariable> variables;

    /**
     * Current index and current value of the "with" values.
     */
    private final WithParameters withParameters;

    /**
     * Initialize task parameters.
     *
     * @param initModel model to use.
     * @param initMatrixParameters matrix parameters to use.
     * @param initVariables variables to use.
     * @param initWithParameters current index and current value of the "with" values
     * @since 1.0.0
     */
    private TaskParameters(final Model initModel,
                          final Map<String, String> initMatrixParameters,
                          final Map<String, IVariable> initVariables,
                          final WithParameters initWithParameters) {
        this.model = initModel;
        this.matrixParameters = new TreeMap<>(initMatrixParameters);
        this.variables = initVariables;
        this.withParameters = initWithParameters;
    }

    /**
     * Get model.
     *
     * @return model.
     * @since 1.0.0
     */
    public Model getModel() {
        return this.model;
    }

    /**
     * Get matrix parameters.
     *
     * @return matrix parameters.
     * @since 1.0.0
     */
    public Map<String, String> getMatrixParameters() {
        return this.matrixParameters;
    }

    /**
     * Get variables.
     *
     * @return variables.
     * @since 1.0.0
     */
    public Map<String, IVariable> getVariables() {
        return this.variables;
    }

    /**
     * Get current index and current value of "with" values.
     *
     * @return current index and current value of "with" values.
     * @since 1.0.0
     */
    public WithParameters getWithParameters() {
        return this.withParameters;
    }

    /**
     * Get dynamic templating context.
     *
     * @return templating context.
     * @since 1.0.0
     */
    public Map<String, Object> getTemplatingContext() {
        final Map<String, Object> context = new HashMap<>();
        context.put("model", this.model.getData());

        if (!this.matrixParameters.isEmpty()) {
            context.put("matrix", this.matrixParameters);
        }

        if (!this.variables.isEmpty()) {
            context.put("variables", this.variables);
        }

        if (this.withParameters != null) {
            context.put("with", this.withParameters);
        }

        return context;
    }

    /**
     * Creating a task parameters instance.
     *
     * @param model mode to use.
     * @param matrixParameters matrix parameters.
     * @param variables variables to use.
     * @param withParameters current index and current value of "with" values.
     * @return instance of {@link TaskParameters}.
     * @since 1.0.0
     */
    public static TaskParameters of(final Model model,
                                    final Map<String,String> matrixParameters,
                                    final Map<String, IVariable> variables,
                                    final WithParameters withParameters) {
        return new TaskParameters(model,matrixParameters, variables, withParameters);
    }
}

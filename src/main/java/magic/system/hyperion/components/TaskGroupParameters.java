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

import java.util.Map;

/**
 * Parameters for running a task group.
 *
 * @author Thomas Lehmann
 */
public class TaskGroupParameters {
    /**
     * Document parameters passed to the run method of the document.
     */
    private DocumentParameters documentParameters;

    /**
     * Model of the document.
     */
    private Model model;

    /**
     * Matrix parameters of the document (for current matrix item).
     */
    private Map<String, String> matrixParameters;

    /**
     * Get document parameters to the run method of the document.
     *
     * @return document parameters.
     * @since 1.0.0
     */
    public DocumentParameters getDocumentParameters() {
        return this.documentParameters;
    }

    /**
     * Get model of the document.
     *
     * @return model of the document.
     * @since 1.0.0
     */
    public Model getModel() {
        return this.model;
    }

    /**
     * Get matrix parameters of the document for current matrix item.
     *
     * @return matrix parameters.
     * @since 1.0.0
     */
    public Map<String, String> getMatrixParameters() {
        return this.matrixParameters;
    }

    /**
     * Changing document parameters.
     *
     * @param initDocumentParameters new document parameters.
     */
    private void setDocumentParameters(final DocumentParameters initDocumentParameters) {
        this.documentParameters = initDocumentParameters;
    }

    /**
     * Changing model.
     *
     * @param initModel new model.
     */
    private void setModel(final Model initModel) {
        this.model = initModel;
    }

    /**
     * Changing matrix matrix parameters.
     *
     * @param initMatrixParameters new matrix parameters.
     */
    private void setMatrixParameters(final Map<String, String> initMatrixParameters) {
        this.matrixParameters = initMatrixParameters;
    }

    /**
     * Create instance of {@link TaskGroupParameters}.
     *
     * @param documentParameters the document parameters passed through run method of document.
     * @param model model of the document.
     * @param matrixParameters matrix parameters for current matrix item.
     * @return instance of {@link TaskGroupParameters}.
     * @since 1.0.0
     */
    public static TaskGroupParameters of(final DocumentParameters documentParameters,
                                         final Model model,
                                         final Map<String, String> matrixParameters) {
        final var parameters = new TaskGroupParameters();
        parameters.setDocumentParameters(documentParameters);
        parameters.setModel(model);
        parameters.setMatrixParameters(matrixParameters);
        return parameters;
    }
}

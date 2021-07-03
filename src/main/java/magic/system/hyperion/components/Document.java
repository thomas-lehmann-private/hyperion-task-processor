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

import magic.system.hyperion.components.interfaces.IChangeableDocument;
import magic.system.hyperion.interfaces.IRunnable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Document containing the model, the matrix and the list of task groups with its tasks.
 * <pre>
 *  {@code
 *  final var reader = new DocumentReader();
 *  // reading YAML document from given path
 *  final var document = reader.read(path);
 *  // no tags and a timeout for each task group after one minute
 *  final var parameters = DocumentParameters(List.of(), 1);
 *  // processing all task groups and its tasks
 *  document.run(parameters);
 *  }
 * </pre>
 *
 * @author Thomas Lehmann
 */
public class Document implements IChangeableDocument, IRunnable<Integer, DocumentParameters> {
    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Document.class);

    /**
     * The one and only model in a document.
     */
    private final Model model;

    /**
     * List of matrix items. For each item all task groups
     * a processed with the parameters of a one matrix item.
     */
    private final List<MatrixParameters> matrix;

    /**
     * List of task groups.
     */
    private final List<TaskGroup> listOfTaskGroups;

    /**
     * Initialize document with empty model, empty matrix and empty list of task groups.
     * @since 1.0.0
     */
    public Document() {
        this.model = new Model();
        this.matrix = new ArrayList<>();
        this.listOfTaskGroups = new ArrayList<>();
    }

    /**
     * Get the model.
     *
     * @return model.
     * @since 1.0.0
     */
    public Model getModel() {
        return this.model;
    }

    /**
     * Get matrix.
     *
     * @return matrix.
     * @since 1.0.0
     */
    public List<MatrixParameters> getMatrix() {
        return Collections.unmodifiableList(this.matrix);
    }

    /**
     * Provide list of task groups.
     *
     * @return list of task groups.
     * @since 1.0.0
     */
    public List<TaskGroup> getListOfTaskGroups() {
        return Collections.unmodifiableList(this.listOfTaskGroups);
    }

    /**
     * Adding a task group.
     *
     * @param taskGroup a new task group.
     * @since 1.0.0
     */
    @Override
    public void add(final TaskGroup taskGroup) {
        this.listOfTaskGroups.add(taskGroup);
    }

    /**
     * Adding matrix parameters.
     *
     * @param matrixParameters a new matrix parameters.
     * @since 1.0.0
     */
    @Override
    public void add(final MatrixParameters matrixParameters) {
        this.matrix.add(matrixParameters);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.model)
                .append(this.matrix)
                .append(this.listOfTaskGroups)
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

        final Document other = (Document) obj;
        return new EqualsBuilder()
                .append(this.model, other.getModel())
                .append(this.matrix, other.getMatrix())
                .append(this.listOfTaskGroups, other.getListOfTaskGroups()).build();
    }

    @Override
    public Integer run(final DocumentParameters parameters) {
        final var errorCounter = new AtomicInteger();

        if (this.matrix.isEmpty()) {
            this.listOfTaskGroups.forEach(taskGroup -> {
                final boolean bSuccess = taskGroup.run(
                        TaskGroupParameters.of(parameters, this.model, Map.of()));
                if (!bSuccess) {
                    errorCounter.incrementAndGet();
                }
            });
        } else {
            for (final var matrixParameters: this.matrix) {
                LOGGER.info("Running Matrix " + matrixParameters.getTitle());
                this.listOfTaskGroups.forEach(taskGroup -> {
                    final boolean bSuccess = taskGroup.run(
                            TaskGroupParameters.of(parameters, this.model,
                                    matrixParameters.getParameters()));
                    if (!bSuccess) {
                        errorCounter.incrementAndGet();
                    }
                });
            }
        }

        return errorCounter.get();
    }
}

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

/**
 * Document parameters for the run method.
 *
 * @author Thomas Lehmann
 */
public final class DocumentParameters {
    /**
     * List of tags for filtering tasks provided as command line options.
     */
    private List<String> tags;

    /**
     * Please use the "of" method.
     */
    private DocumentParameters() {
        // Nothing else to do.
    }

    /**
     * Readonly access to list of tags.
     *
     * @return list of tags.
     */
    public List<String> getTags() {
        return Collections.unmodifiableList(this.tags);
    }

    /**
     * Changing of the tags.
     *
     * @param initTags new list of tags.
     */
    private void setTags(final List<String> initTags) {
        this.tags = new ArrayList<>(initTags);
    }

    /**
     * Create document parameters (for the run method).
     *
     * @param tags list of tags for filtering of tasks.
     * @return instance of {@link DocumentParameters}.
     */
    public static DocumentParameters of(final List<String> tags) {
        final var parameters = new DocumentParameters();
        parameters.setTags(tags);
        return parameters;
    }
}

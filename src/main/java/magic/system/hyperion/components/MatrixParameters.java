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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * Matrix parameters used to run all defines task groups with it.
 *
 * @author Thomas Lehmann
 */
public class MatrixParameters extends Component {
    /**
     * String parameters by name.
     */
    private final Map<String, String> parameters;

    /**
     * Initialize component.
     *
     * @param strInitTitle - title of the matrix parameters.
     * @version 1.0.0
     */
    public MatrixParameters(String strInitTitle) {
        super(strInitTitle);
        this.parameters = new TreeMap<>();
    }

    /**
     * Get parameters (readonly).
     *
     * @return parameters.
     * @version 1.0.0
     */
    public Map<String, String> getParameters() {
        return Collections.unmodifiableMap(this.parameters);
    }

    /**
     * Replace existing parameters by new ones. Purpose of this method is to
     * be used by {@link magic.system.hyperion.reader.DocumentReader}.
     *
     * @param initParameters replace all existing parameters with given ones.
     * @version 1.0.0
     */
    public void setParameters(final Map<String, String> initParameters) {
        this.parameters.clear();
        this.parameters.putAll(initParameters);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.getTitle())
                .append(this.parameters)
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

        final MatrixParameters other = (MatrixParameters) obj;
        return new EqualsBuilder()
                .append(this.getTitle(), other.getTitle())
                .append(this.parameters, other.getParameters())
                .build();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append(this.getTitle())
                .append(this.parameters)
                .build();
    }
}

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

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.regex.Pattern;
import magic.system.hyperion.interfaces.IVariable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Variable supporting extracting of details via regex.
 *
 * @author Thomas Lehmann
 */
public class Variable implements IVariable {

    /**
     * Name of the variable.
     */
    private String strName;

    /**
     * Value of a task result.
     */
    private String strValue;

    /**
     * Java regex to define.
     */
    private String strRegex;

    /**
     * Regex group to use (default: 0).
     */
    private int iRegexGroup;

    /**
     * When true then apply regex per line.
     */
    private boolean bLineByLine;

    /**
     * Initialize variable.
     */
    public Variable() {
        this.strName = "";
        this.strValue = "";
        this.strRegex = ".*";
        this.iRegexGroup = 0;
        this.bLineByLine = false;
    }

    /**
     * Initialize variable.
     *
     * @param strInitName the name of the variable.
     * @param strInitRegex the regex to use for extracting value.
     * @param iInitRegexGroup the regex group to use.
     * @param bInitLineByLine when true then apply regex per line.
     */
    public Variable(final String strInitName, final String strInitRegex,
            final int iInitRegexGroup, final boolean bInitLineByLine) {
        this.strName = strInitName;
        this.strValue = "";
        this.strRegex = strInitRegex;
        this.iRegexGroup = iInitRegexGroup;
        this.bLineByLine = bInitLineByLine;
    }

    /**
     * Provide name of variable.
     *
     * @return name of variable.
     */
    @Override
    public String getName() {
        return this.strName;
    }

    /**
     * Change name of variable.
     *
     * @param strInitName new name of variable.
     */
    public void setName(final String strInitName) {
        this.strName = strInitName;
    }

    /**
     * Value of the variable.
     *
     * @return value
     */
    @JsonIgnore
    @Override
    public String getValue() {
        return this.strValue;
    }

    /**
     * Get flag whether to apply regex line by line.
     *
     * @return true when regex should be applied line by line.
     */
    public boolean isLineByLine() {
        return this.bLineByLine;
    }

    /**
     * Change line by line flag.
     *
     * @param bInitLineByLine new value for line by handling (regex).
     */
    public void setLineByLine(final boolean bInitLineByLine) {
        this.bLineByLine = bInitLineByLine;
    }

    /**
     * Get defined Regex to extract value.
     *
     * @return defined regex.
     */
    public String getRegex() {
        return this.strRegex;
    }

    /**
     * Change regex for variable.
     *
     * @param strInitRegex new regex expression.
     */
    public void setRegex(final String strInitRegex) {
        this.strRegex = strInitRegex;
    }

    /**
     * Get regex group to use.
     *
     * @return regex group.
     */
    public int getRegexGroup() {
        return this.iRegexGroup;
    }

    /**
     * Change value for regex group.
     *
     * @param iInitRegexGroup new value for regex group.
     */
    public void setRegexGroup(final int iInitRegexGroup) {
        this.iRegexGroup = iInitRegexGroup;
    }

    /**
     * Change value using regex to extract from given string.
     *
     * @param strInitValue some string (text)
     * @return true when regex did work fine.
     */
    public boolean setValue(final String strInitValue) {
        boolean success = false;
        final var pattern = Pattern.compile(this.strRegex);

        if (this.bLineByLine) {
            String strNewValue = "";
            for (var line : List.of(strInitValue.split("\\n"))) {
                final var matcher = pattern.matcher(line);
                if (!matcher.find()) {
                    continue;
                }

                success = true;
                if (!strNewValue.isEmpty()) {
                    strNewValue += "\n";
                }
                strNewValue += matcher.group(this.iRegexGroup);
            }

            if (success) {
                this.strValue = strNewValue;
            }
        } else {
            final var matcher = pattern.matcher(strInitValue);
            if (matcher.find()) {
                success = true;
                this.strValue = matcher.group(this.iRegexGroup);
            }
        }

        return success;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.strName)
                .append(this.strRegex)
                .append(this.iRegexGroup)
                .append(this.bLineByLine)
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
        final Variable other = (Variable) obj;
        return new EqualsBuilder()
                .append(this.strName, other.getName())
                .append(this.strRegex, other.getRegex())
                .append(this.iRegexGroup, other.getRegexGroup())
                .append(this.bLineByLine, isLineByLine())
                .build();
    }
}

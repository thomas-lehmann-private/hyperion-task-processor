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
package magic.system.hyperion.tools;

import java.util.function.Consumer;

/**
 * Printing capabilities of the system.
 *
 * @author Thomas Lehmann
 */
public class CapabilitiesPrinter {
    /**
     * Capability key for OS.
     */
    public static final String OS = "Operating System";

    /**
     * Capability key for Java.
     */
    public static final String JAVA = "Java";

    /**
     * Capability key for Docker.
     */
    public static final String GROOVY = "Groovy (embedded)";

    /**
     * Capability key for Docker.
     */
    public static final String DOCKER = "Docker";

    /**
     * Capability key for Powershell.
     */
    public static final String POWERSHELL = "Powershell";

    /**
     * Value when cabability is not given.
     */
    public static final String NOT_AVAILABLE = "not available";

    /**
     * Configured Groovy version.
     */
    private String strGroovyVersion;

    /**
     * Change Groovy version.
     *
     * @param strInitGroovyVersion new version.
     * @since 1.0.0
     */
    public void setGroovyVersion(final String strInitGroovyVersion) {
        this.strGroovyVersion = strInitGroovyVersion;
    }

    /**
     * Print capabilities of the system.
     *
     * @param consumer it's on the caller to decide how to print.
     * @since 1.0.0
     */
    public void print(final Consumer<String> consumer) {
        consumer.accept(printableKeyValue(JAVA, Capabilities.getJavaVersion(),
                "class version=" + Capabilities.getJavaClassVersion()));

        consumer.accept(printableKeyValue(OS, Capabilities.getOperatingSystemName(),
                "arch=" + Capabilities.getOperatingSystemArchitecture()));

        consumer.accept(printableKeyValue(GROOVY, this.strGroovyVersion, ""));

        if (Capabilities.hasDocker()) {
            consumer.accept(printableKeyValue(DOCKER, Capabilities.getDockerVersion(), ""));
        } else {
            consumer.accept(printableKeyValue(DOCKER, NOT_AVAILABLE, ""));
        }

        if (Capabilities.hasPowershell()) {
            consumer.accept(printableKeyValue(
                    POWERSHELL, Capabilities.getPowershellVersion(), ""));
        } else {
            consumer.accept(printableKeyValue(POWERSHELL, NOT_AVAILABLE, ""));
        }
    }

    /**
     * Format key/value that say look like a aligned table.
     *
     * @param strKey                 some key.
     * @param strValue               some value.
     * @param strOptionalInformation some optional information.
     * @return formatted key/value.
     */
    private static String printableKeyValue(final String strKey, final String strValue,
                                            final String strOptionalInformation) {
        final String strResult;
        final String strFinalKey = strKey + ":";

        if (strOptionalInformation.isEmpty()) {
            strResult = String.format("%-20s %s", strFinalKey, strValue);
        } else {
            strResult = String.format("%-20s %s (%s)", strFinalKey, strValue,
                    strOptionalInformation);
        }

        return strResult;
    }
}

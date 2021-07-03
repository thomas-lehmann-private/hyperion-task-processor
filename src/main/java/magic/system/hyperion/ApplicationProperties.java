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
package magic.system.hyperion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * Wrapper for content of application.properties.
 *
 * @author Thomas Lehmann
 */
public class ApplicationProperties {
    /**
     * Logger of this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationProperties.class);

    /**
     * The key for final name of the jar (without extension).
     */
    private static final String PROPERTY_FINAL_NAME = "finalName";

    /**
     * The key for the product version (see pom.xml).
     */
    private static final String PROPERTY_PRODUCT_VERSION = "productVersion";

    /**
     * They key for the build timestamp.
     */
    private static final String PROPERTY_BUILD_TIMESTAMP = "buildTimestamp";

    /**
     * They key for the architect and developer of the project.
     */
    private static final String PROPERTY_AUTHOR = "author";

    /**
     * They key for the git commit id.
     */
    private static final String PROPERTY_GIT_COMMIT_ID = "gitCommitId";

    /**
     * The key for the groovy version embedded into Hyperion tool.
     */
    private static final String PROPERTY_GROOVY_VERSION = "groovyVersion";

    /**
     * Application properties.
     */
    private final Properties properties;

    /**
     * Initialize with content of application.properties.
     * @since 1.0.0
     */
    public ApplicationProperties() {
        this.properties = loadProperties();
    }

    /**
     * Get final name of application.
     *
     * @return final name of application.
     * @since 1.0.0
     */
    public String getFinalName() {
        return this.properties.getProperty(PROPERTY_FINAL_NAME);
    }

    /**
     * Get product version.
     *
     * @return product version.
     * @since 1.0.0
     */
    public String getProductVersion() {
        return this.properties.getProperty(PROPERTY_PRODUCT_VERSION);
    }

    /**
     * Get build timestamp.
     *
     * @return build timestamp.
     * @since 1.0.0
     */
    public String getBuildTimestamp() {
        return this.properties.getProperty(PROPERTY_BUILD_TIMESTAMP);
    }

    /**
     * Get author.
     *
     * @return author.
     * @since 1.0.0
     */
    public String getAuthor() {
        return this.properties.getProperty(PROPERTY_AUTHOR);
    }

    /**
     * Get git commit id.
     *
     * @return git commit id.
     * @since 1.0.0
     */
    public String getGitCommitId() {
        return this.properties.getProperty(PROPERTY_GIT_COMMIT_ID);
    }

    /**
     * Get Grooovy version.
     *
     * @return Groovy version.
     * @since 1.0.0
     */
    public String getGroovyVersion() {
        return this.properties.getProperty(PROPERTY_GROOVY_VERSION);
    }

    /**
     * Load and provide the application properties.
     *
     * @return application properties.
     */
    private static Properties loadProperties() {
        final var properties = new Properties();

        try (var stream = Application.class.getResourceAsStream("/application.properties")) {
            properties.load(stream);
        } catch (IOException e) {
            // should never happen (the file should always be in the jar too)
            LOGGER.error(e.getMessage(), e);
        }

        return properties;
    }
}

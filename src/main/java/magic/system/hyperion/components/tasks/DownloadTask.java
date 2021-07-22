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
package magic.system.hyperion.components.tasks;

import magic.system.hyperion.components.TaskParameters;
import magic.system.hyperion.components.TaskResult;
import magic.system.hyperion.tools.FileUtils;
import magic.system.hyperion.tools.TemplateEngine;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Task for downloading a file from an url.
 *
 * @author Thomas Lehmann
 */
public class DownloadTask extends AbstractFileTask {
    /**
     * Logger of this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadTask.class);

    /**
     * URL where to download the file from.
     */
    private URL url;

    /**
     * Initialize task with defaults.
     *
     * @param strInitTitle - title of the task.
     * @since 1.0.0
     */
    public DownloadTask(String strInitTitle) {
        super(strInitTitle);
    }

    /**
     * Get url where to download the file from.
     *
     * @return url where to download the file from.
     * @since 2.0.0
     */
    public URL getUrl() {
        return this.url;
    }

    /**
     * Change url.
     *
     * @param initUrl new url.
     * @since 2.0.0
     */
    public void setUrl(final URL initUrl) {
        this.url = initUrl;
    }

    @Override
    public TaskResult run(TaskParameters parameters) {
        TaskResult taskResult = null;
        final var engine = new TemplateEngine();

        logTitle(parameters);

        final var renderedDestinationPath = Paths.get(engine.render(
                getDestinationPath(), parameters.getTemplatingContext()));

        if (!isOverwrite() && FileUtils.isRegularFile(renderedDestinationPath.toString())) {
            LOGGER.error("Overwrite not enabled for {}", renderedDestinationPath.toString());
            taskResult = new TaskResult(false, getVariable());
        } else {
            final var parentPath = renderedDestinationPath.getParent();

            try (var stream = this.url.openStream()) {
                if (parentPath != null && isEnsurePath()) {
                    Files.createDirectories(parentPath);
                }

                if (parentPath != null && Files.exists(parentPath)) {
                    LOGGER.info("Writing file to {}", renderedDestinationPath.toString());
                    Files.write(renderedDestinationPath, stream.readAllBytes());
                    getVariable().setValue(renderedDestinationPath.toString());
                    taskResult = new TaskResult(true, getVariable());
                } else {
                    LOGGER.error("Missing path for {}", renderedDestinationPath.toString());
                    taskResult = new TaskResult(false, getVariable());
                }
            } catch (final IOException e) {
                LOGGER.error(e.getMessage(), e);
                taskResult = new TaskResult(false, getVariable());
            }
        }

        return taskResult;
    }

    @Override
    public AbstractTask copy() {
        final var task = new DownloadTask(getTitle());
        task.setDestinationPath(getDestinationPath());
        task.setOverwrite(isOverwrite());
        task.setEnsurePath(isEnsurePath());
        task.setUrl(this.url);
        return task;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("url", this.url)
                .appendSuper(super.toString())
                .build();
    }
}

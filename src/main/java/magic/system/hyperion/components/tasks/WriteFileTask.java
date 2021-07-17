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
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Task for writing content to a file.
 *
 * @author Thomas Lehmann
 */
public class WriteFileTask extends AbstractFileTask {
    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WriteFileTask.class);

    /**
     * Content to write to file.
     */
    private String strContent;


    /**
     * Initialize task with defaults.
     *
     * @param strInitTitle - title of the task.
     * @since 2.0.0
     */
    public WriteFileTask(String strInitTitle) {
        super(strInitTitle);
    }

    /**
     * Change content.
     *
     * @param strInitContent new content.
     * @since 2.0.0
     */
    public void setContent(final String strInitContent) {
        this.strContent = strInitContent;
    }

    /**
     * Get content.
     *
     * @return content.
     * @since 2.0.0
     */
    public String getContent() {
        return this.strContent;
    }

    @Override
    public TaskResult run(TaskParameters parameters) {
        TaskResult taskResult = null;
        final var engine = new TemplateEngine();

        logTitle(parameters);

        try {
            final var strRenderedContent = engine.render(
                    this.strContent, parameters.getTemplatingContext());
            final var renderedDestinationPath = Paths.get(engine.render(
                    getDestinationPath(), parameters.getTemplatingContext()));

            if (!isOverwrite() && FileUtils.isRegularFile(renderedDestinationPath.toString())) {
                taskResult = new TaskResult(false, getVariable());
            } else {
                final var parentPath = renderedDestinationPath.getParent();

                if (parentPath != null && isEnsurePath()) {
                    Files.createDirectories(parentPath);
                }

                if (parentPath != null && Files.exists(parentPath)) {
                    LOGGER.info("Writing file to {}", renderedDestinationPath.toString());
                    Files.writeString(renderedDestinationPath, strRenderedContent);
                    getVariable().setValue(renderedDestinationPath.toString());
                    taskResult = new TaskResult(true, getVariable());
                } else {
                    taskResult = new TaskResult(false, getVariable());
                }
            }
        } catch (final IOException e) {
            LOGGER.error(e.getMessage(), e);
            taskResult = new TaskResult(false, getVariable());
        }

        return taskResult;
    }

    @Override
    public AbstractTask copy() {
        final var task = new WriteFileTask(getTitle());
        task.setDestinationPath(getDestinationPath());
        task.setOverwrite(isOverwrite());
        task.setEnsurePath(isEnsurePath());
        return task;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString())
                .append("content", this.strContent)
                .build();
    }
}

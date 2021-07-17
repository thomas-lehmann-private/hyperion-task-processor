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
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Task for copying a file from source path to destination path.
 *
 * @author Thomas Lehmann
 */
public class FileCopyTask extends AbstractFileTask {
    /**
     * Logger of this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FileCopyTask.class);

    /**
     * Source location of file.
     */
    private String strSourcePath;

    /**
     * When true the specified destination is directory (default: true).
     */
    private boolean bDestinationIsDirectory;

    /**
     * Initialize task with title.
     * By default the task is not allowed to overwrite an existing file and
     * when the destination path doesn't exist it won't be created and then
     * the copy operation will fail.
     *
     * @param strInitTitle - title of the task.
     * @since 1.0.0
     */
    public FileCopyTask(final String strInitTitle) {
        super(strInitTitle);
        this.bDestinationIsDirectory = true;
    }

    /**
     * Change source location of file.
     *
     * @param strInitSourcePath new source location.
     * @since 1.0.0
     */
    public void setSourcePath(final String strInitSourcePath) {
        this.strSourcePath = strInitSourcePath;
    }

    /**
     * Get source path.
     *
     * @return source path.
     * @since 1.0.0
     */
    public String getSourcePath() {
        return this.strSourcePath;
    }

    /**
     * Changing type of destination.
     *
     * @param bInitDestinationIsDirectory type of destination.
     * @since 1.0.0
     */
    public void setDestinationIsDirectory(final boolean bInitDestinationIsDirectory) {
        this.bDestinationIsDirectory = bInitDestinationIsDirectory;
    }

    /**
     * Get the type of the destination.
     *
     * @return type of the destination.
     * @since 1.0.0
     */
    public boolean isDestinationIsDirectory() {
        return this.bDestinationIsDirectory;
    }

    @Override
    public AbstractTask copy() {
        final var task = new FileCopyTask(getTitle());
        task.setSourcePath(this.strSourcePath);
        task.setDestinationPath(getDestinationPath());
        task.setOverwrite(isOverwrite());
        task.setEnsurePath(isEnsurePath());
        task.setDestinationIsDirectory(this.bDestinationIsDirectory);
        return task;
    }

    @Override
    public TaskResult run(final TaskParameters parameters) {
        TaskResult taskResult = null;
        final var engine = new TemplateEngine();

        logTitle(parameters);

        if (this.strSourcePath != null && getDestinationPath() != null) {
            final var sourcePath = Paths.get(engine.render(
                    this.strSourcePath, parameters.getTemplatingContext()));
            final var destinationPath = Paths.get(engine.render(
                    getDestinationPath(), parameters.getTemplatingContext()));

            // not: missing source or existing destination cannot be overwritten?
            final boolean bIsValid = Files.exists(sourcePath) && Files.isRegularFile(sourcePath)
                    && (isOverwrite() || !(Files.exists(destinationPath)
                    && Files.isRegularFile(destinationPath)));

            if (bIsValid) {
                taskResult = copyFile(sourcePath, destinationPath);
            } else {
                taskResult = new TaskResult(false, getVariable());
            }
        } else {
            // source and destination are null
            taskResult = new TaskResult(false, getVariable());
        }

        return taskResult;
    }

    /**
     * Copy from source to destination.
     *
     * @param sourcePath source file path.
     * @param destinationPath destination file path.
     * @return task result.
     */
    private TaskResult copyFile(final Path sourcePath, final Path destinationPath) {
        TaskResult taskResult;
        try {
            if (isEnsurePath()) {
                ensurePath(destinationPath);
            }

            var finalDestinationPath = destinationPath;

            if (this.bDestinationIsDirectory) {
                final Path fileName = sourcePath.getFileName();
                if (fileName != null) {
                    finalDestinationPath = Paths.get(
                            destinationPath.toString(), fileName.toString());
                }
            }

            LOGGER.info("Copying file from {} to {}",
                    sourcePath.toString(), destinationPath.toString());
            FileUtils.copyFile(sourcePath, finalDestinationPath);
            getVariable().setValue(finalDestinationPath.toString());
            taskResult = new TaskResult(true, getVariable());
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            // failed to copy
            taskResult = new TaskResult(false, getVariable());
        }
        return taskResult;
    }

    /**
     * Ensure path.
     *
     * @param destinationPath the destination path being a file or a directory.
     * @throws IOException when creating directories has failed.
     */
    private void ensurePath(final Path destinationPath) throws IOException {
        if (this.bDestinationIsDirectory) {
            Files.createDirectories(destinationPath);
        } else {
            final Path parentPath = destinationPath.getParent();
            if (parentPath != null) {
                Files.createDirectories(parentPath);
            }
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("sourcePath", this.strSourcePath)
                .appendSuper(super.toString())
                .append("destIsDirectory", this.bDestinationIsDirectory)
                .build();
    }
}

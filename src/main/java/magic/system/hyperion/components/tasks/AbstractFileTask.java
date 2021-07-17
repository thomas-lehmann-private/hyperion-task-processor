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

/**
 * Base class for file based tasks with same options (write, copy, move).
 */
public abstract class AbstractFileTask extends AbstractTask {
    /**
     * Destination location of file.
     */
    private String strDestinationPath;

    /**
     * When true an existing destination file will be overwritten (default: false).
     */
    private boolean bOverwrite;

    /**
     * When true the path where to copy the file will be created (default: false).
     */
    private boolean bEnsurePath;

    /**
     * Initialize task with defaults.
     *
     * @param strInitTitle - title of the task.
     * @since 1.0.0
     */
    public AbstractFileTask(String strInitTitle) {
        super(strInitTitle);
        this.bOverwrite = false;
        this.bEnsurePath = false;
    }
    /**
     * Change destination location of file.
     *
     * @param strInitDestinationPath new destination location.
     * @since 2.0.0
     */
    public void setDestinationPath(final String strInitDestinationPath) {
        this.strDestinationPath = strInitDestinationPath;
    }

    /**
     * Get destination path.
     *
     * @return destination path.
     * @since 2.0.0
     */
    public String getDestinationPath() {
        return this.strDestinationPath;
    }

    /**
     * Change overwrite mode.
     *
     * @param bInitOverwrite new overwrite mode.
     * @since 2.0.0
     */
    public void setOverwrite(final boolean bInitOverwrite) {
        this.bOverwrite = bInitOverwrite;
    }

    /**
     * Get overwrite mode.
     *
     * @return overwrite mode.
     * @since 2.0.0
     */
    public boolean isOverwrite() {
        return this.bOverwrite;
    }

    /**
     * Change mode for ensuring destination path.
     *
     * @param bInitEnsurePath new mode for ensuring destination path.
     * @since 2.0.0
     */
    public void setEnsurePath(final boolean bInitEnsurePath) {
        this.bEnsurePath = bInitEnsurePath;
    }

    /**
     * Get mode ensuring destination path.
     *
     * @return mode ensuring destination path.
     * @since 2.0.0
     */
    public boolean isEnsurePath() {
        return this.bEnsurePath;
    }
}

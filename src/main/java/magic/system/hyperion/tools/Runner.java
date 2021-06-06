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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Running a list of runnables either in order or in parallel.
 *
 * @author Thomas Lehmann
 */
public final class Runner {
    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Runner.class);

    /**
     *  List if runnables.
     */
    private final List<Runnable> runnables;

    /**
     * When true run runnables in parallel otherwise in order.
     */
    private boolean bIsParallel;

    /**
     * Initialize runner with a list of runnables.
     *
     * @param initRunnables list of runnables.
     */
    private Runner(final List<Runnable> initRunnables) {
        this.runnables = initRunnables;
        this.bIsParallel = false;
    }

    /**
     * Change run mode (when true then parallel otherwise in order).
     *
     * @param bInitIsParallel new run mode.
     */
    public void setParallel(final boolean bInitIsParallel) {
        this.bIsParallel = bInitIsParallel;
    }

    /**
     * Running all runnables.
     */
    public void runAll() {
        if (this.bIsParallel) {
            runInParallel();
        } else {
            runInOrder();
        }
    }

    /**
     * Create runner with list of runnables.
     *
     * @param arguments array of runnables.
     * @return runner instance.
     */
    public static Runner of(Runnable... arguments) {
        return new Runner(List.of(arguments));
    }

    /**
     * Running all runnables in order.
     */
    private void runInOrder() {
        this.runnables.forEach(Runnable::run);
    }

    /**
     * Running all runnables in parallel.
     */
    private void runInParallel() {
        final var executor = Executors.newFixedThreadPool(this.runnables.size());
        this.runnables.forEach(executor::submit);
        executor.shutdown();

        try {
            // FIXME: that's something to be configured at
            //        global options (--taskgroup-timeout=60)
            //CHECKSTYLE.OFF: MagicNumber: see fixme note above this line
            executor.awaitTermination(60L, TimeUnit.MINUTES);
            //CHECKSTYLE.ON: MagicNumber
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}

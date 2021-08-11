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

import magic.system.hyperion.exceptions.HyperionException;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Running a list of runnables either in order or in parallel.
 * The default timeout is 10 minutes.
 *
 * @author Thomas Lehmann
 */
public final class Runner {
    /**
     * Default timeout (milliseconds).
     */
    private static final int DEFAULT_TIMEOUT = 10 * 60 * 1000;

    /**
     * List if runnables.
     */
    private final List<Runnable> runnables;

    /**
     * When true run runnables in parallel otherwise in order.
     */
    private boolean bIsParallel;

    /**
     * Timeout for waiting for finishing of runnables (in minutes).
     */
    private int iTimeout;

    /**
     * Initialize runner with a list of runnables.
     *
     * @param initRunnables list of runnables.
     */
    private Runner(final List<Runnable> initRunnables) {
        this.runnables = initRunnables;
        this.bIsParallel = false;
        this.iTimeout = DEFAULT_TIMEOUT;
    }

    /**
     * Change run mode (when true then parallel otherwise in order).
     *
     * @param bInitIsParallel new run mode.
     * @since 1.0.0
     */
    public void setParallel(final boolean bInitIsParallel) {
        this.bIsParallel = bInitIsParallel;
    }

    /**
     * Change timeout for waiting for finishing of runnables.
     *
     * @param iInitTimeout new timeout.
     * @since 1.0.0
     */
    public void setTimeout(final int iInitTimeout) {
        this.iTimeout = iInitTimeout;
    }

    /**
     * Running all runnables.
     *
     * @throws HyperionException when thread execution has failed or timeout did happen.
     * @since 2.0.0
     */
    public void runAll() throws HyperionException {
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
     * @since 1.0.0
     */
    public static Runner of(Runnable... arguments) {
        return new Runner(List.of(arguments));
    }

    /**
     * Running all runnables in order (the list of runnables inside the same thread).
     *
     * @throws HyperionException when thread execution has failed.
     */
    private void runInOrder() throws HyperionException {
        final var executor = Executors.newFixedThreadPool(1);

        try {
            final List<Runnable> wrappedRunnable = List.of(() -> runnables.forEach(Runnable::run));
            wrappedRunnable.forEach(executor::submit);
            executor.shutdown();
            executor.awaitTermination(this.iTimeout, TimeUnit.MILLISECONDS);
        } catch (final InterruptedException | RejectedExecutionException e) {
            throw new HyperionException(e.getMessage());
        }
    }

    /**
     * Running all runnables in parallel.
     *
     * @throws HyperionException when thread execution has failed.
     */
    private void runInParallel() throws HyperionException {
        final var executor = Executors.newFixedThreadPool(this.runnables.size());

        try {
            this.runnables.forEach(executor::submit);
            executor.shutdown();
            executor.awaitTermination(this.iTimeout, TimeUnit.MILLISECONDS);
        } catch (final InterruptedException | RejectedExecutionException e) {
            throw new HyperionException(e.getMessage());
        }
    }
}

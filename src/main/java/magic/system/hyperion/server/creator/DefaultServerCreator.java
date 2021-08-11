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
package magic.system.hyperion.server.creator;

import magic.system.hyperion.annotations.Named;
import magic.system.hyperion.server.Server;

/**
 * Creating instance of a server.
 *
 * @author Thomas Lehmann
 */
@Named("default")
public class DefaultServerCreator implements IServerCreator {

    /**
     * Default Server creator.
     */
    private static final IServerCreator DEFAULT_CREATOR = Server::new;

    /**
     * Current implementation.
     */
    private static IServerCreator implementation = DEFAULT_CREATOR;

    /**
     * Change implementation.
     *
     * @param initImplementation another server creator.
     * @since 2.0.0
     * @apiNote Intention to be used by unit testing.
     */
    public static synchronized void setImplementation(final IServerCreator initImplementation) {
        DefaultServerCreator.implementation = initImplementation;
    }

    @Override
    public Server create() {
        final var current = implementation;
        DefaultServerCreator.setImplementation(DEFAULT_CREATOR);
        return current.create();
    }
}

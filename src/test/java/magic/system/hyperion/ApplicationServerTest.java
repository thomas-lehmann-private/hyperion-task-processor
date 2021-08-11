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

import magic.system.hyperion.command.ServeCommandProcessor;
import magic.system.hyperion.server.IServer;
import magic.system.hyperion.server.creator.DefaultServerCreator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testing application running the serve command (not the REST feature).
 *
 * @author Thomas Lehmann
 */
@SuppressWarnings("checkstyle:multiplestringliterals")
public class ApplicationServerTest {
    /**
     * Test port.
     */
    public static final int TEST_PORT = 1234;

    /**
     * Testing the serve command call with a custom port.
     */
    @Test
    public void testServeCommandCustomPort() {
        final List<Integer> ports = new ArrayList<>();
        changeServerImplementation(ports);

        Application.main(List.of("serve", "--port=" + TEST_PORT).toArray(String[]::new));
        assertEquals(List.of(TEST_PORT), ports);
    }

    /**
     * Testing the serve command call with a custom port.
     */
    @Test
    public void testServeCommandDefaultPort() {
        final List<Integer> ports = new ArrayList<>();
        changeServerImplementation(ports);

        Application.main(List.of("serve").toArray(String[]::new));
        assertEquals(List.of(ServeCommandProcessor.DEFAULT_PORT), ports);
    }

    /**
     * Replacing the start method by a test information.
     *
     * @param ports the list of ports (only one should be added finally)
     */
    private void changeServerImplementation(final List<Integer> ports) {
        DefaultServerCreator.setImplementation(() -> {
            return new IServer() {
                /**
                 * Port passed to the server.
                 */
                private int iPort = 0;

                @Override
                public int getPort() {
                    return iPort;
                }

                @Override
                public void start(final int port) {
                    this.iPort = port;
                    ports.add(this.iPort);
                }

                @Override
                public void stop() {
                    // nothing to do.
                }
            };
        });
    }
}
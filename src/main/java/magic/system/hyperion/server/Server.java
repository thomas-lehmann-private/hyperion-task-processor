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
package magic.system.hyperion.server;

import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;
import magic.system.hyperion.server.paths.creator.IPathsCreator;
import magic.system.hyperion.tools.Factory;

import static io.javalin.apibuilder.ApiBuilder.path;

/**
 * Implementing the server.
 *
 * @author Thomas Lehmann
 */
public class Server {
    /**
     * REST Service application.
     */
    private final Javalin app;

    /**
     * Creating service.
     */
    public Server() {
        final var factory = new Factory<EndpointGroup>(IPathsCreator.class);
        this.app = Javalin.create(config -> {
            config.registerPlugin(new OpenApiConfiguration().create());
        }).routes(() -> {
            path(PathSegment.DOCUMENTS.getSegmentName(),
                    factory.create(PathSegment.DOCUMENTS.getSegmentName()));
        });

    }

    /**
     * Get port used by the Javalin server app.
     *
     * @return port used.
     */
    public int getPort() {
        return this.app.port();
    }

    /**
     * Running the server.
     *
     * @param iPort port to run service on.
     * @since 2.0.0
     */
    public void start(final int iPort) {
        this.app.start(iPort);
    }

    /**
     * Stopping the server.
     */
    public void stop() {
        this.app.stop();
    }
}

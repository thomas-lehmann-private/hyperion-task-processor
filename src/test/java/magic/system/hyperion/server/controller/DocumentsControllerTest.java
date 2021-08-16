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
package magic.system.hyperion.server.controller;

import kong.unirest.Unirest;
import magic.system.hyperion.components.DocumentResult;
import magic.system.hyperion.server.Server;
import magic.system.hyperion.tools.MessagesCollector;
import magic.system.hyperion.tools.TimeTools;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.fasterxml.jackson.module.kotlin.ExtensionsKt.jacksonObjectMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testing of class {@link DocumentsController}.
 *
 * @author Thomas Lehmann
 */
@DisplayName("Testing class DocumentsController")
@TestMethodOrder(value = MethodOrderer.Random.class)
@SuppressWarnings("checkstyle:multiplestringliterals")
class DocumentsControllerTest {
    /**
     * Maximum wait for request: 1 second.
     */
    private static final long TIMEOUT = 5000;

    /**
     * Time to sleep in the wait loop.
     */
    private static final long WAIT = TIMEOUT / 10;

    /**
     * REST Service.
     */
    private Server server;

    /**
     * Creating and starting REST service at random port.
     */
    @BeforeEach
    public void setUp() {
        this.server = new Server();
        this.server.start(0);
    }

    /**
     * Stopping REST service.
     */
    @AfterEach
    public void tearDown() throws InterruptedException {
        this.server.stop();
        this.server = null;
    }

    /**
     * Testing document post request without tags.
     *
     * @throws IOException          when reading YAML has failed.
     * @throws URISyntaxException   when file  has not been found.
     * @throws InterruptedException when wait for condition has been interrupted
     */
    @Test
    public void testRunWithoutTags() throws IOException, URISyntaxException, InterruptedException {
        final var path = Paths.get(getClass().getResource(
                "/documents/document-with-groovy.yml").toURI());

        MessagesCollector.clear();

        final var response = Unirest
                .post("http://localhost:" + this.server.getPort() + "/documents")
                .body(Files.readString(path))
                .asString();

        assertTrue(response.isSuccess());
        assertFalse(response.getBody().isEmpty());

        // ensure that by given timeout the document has been successful processed.
        // and also verify that the document is processed.
        assertTrue(TimeTools.wait(() ->
                        MessagesCollector.hasMessages(
                                List.of("Document request succeeded!", "hello world 2!")),
                TIMEOUT, WAIT));

        // verify status of document result
        final var strId = response.getBody();
        final var statusResponse = Unirest
                .get("http://localhost:" + this.server.getPort() + "/documents/" + strId)
                .asString();

        final var mapper = jacksonObjectMapper().findAndRegisterModules();
        final var result = mapper.readValue(statusResponse.getBody(), DocumentResult.class);

        assertTrue(statusResponse.isSuccess());
        assertTrue(result.isSuccess());
        // it's definitely below TIMEOUT but there should be no assumption about how much;
        // performance is not relevant for processing documents and also in ownership of
        // the provider.
        assertTrue(result.getFinished().isAfter(result.getStarted()));
    }

    /**
     * Testing document post request with tags.
     *
     * @throws IOException          when reading YAML has failed.
     * @throws URISyntaxException   when file  has not been found.
     * @throws InterruptedException when wait for condition has been interrupted
     */
    @Test
    public void testRunWithTags() throws IOException, URISyntaxException, InterruptedException {
        final var path = Paths.get(getClass().getResource(
                "/documents/document-with-groovy.yml").toURI());

        MessagesCollector.clear();
        final var response = Unirest
                .post("http://localhost:" + this.server.getPort() + "/documents")
                .body(Files.readString(path))
                .queryString("tag", "tag support")
                .queryString("tag", "third example")
                .asString();

        assertTrue(response.isSuccess());
        assertFalse(response.getBody().isEmpty());

        // ensure that by given timeout the document has been successful processed.
        // and also verify that the document is processed.
        assertTrue(TimeTools.wait(() ->
                        MessagesCollector.hasMessages(
                                List.of("Document request succeeded", "hello world 2!")),
                TIMEOUT, WAIT));

        // verify status of document result
        final var strId = response.getBody();
        final var statusResponse = Unirest
                .get("http://localhost:" + this.server.getPort() + "/documents/" + strId)
                .asString();

        final var mapper = jacksonObjectMapper().findAndRegisterModules();
        final var result = mapper.readValue(statusResponse.getBody(), DocumentResult.class);

        assertTrue(statusResponse.isSuccess());
        assertTrue(result.isSuccess());
        // it's definitely below TIMEOUT but there should be no assumption about how much;
        // performance is not relevant for processing documents and also in ownership of
        // the provider.
        assertTrue(result.getFinished().isAfter(result.getStarted()));
    }

    /**
     * Testing document post request with tags.
     *
     * @throws IOException          when reading YAML has failed.
     * @throws URISyntaxException   when file  has not been found.
     * @throws InterruptedException when wait for condition has been interrupted
     */
    @Test
    public void testBadDocument() throws IOException, URISyntaxException, InterruptedException {
        MessagesCollector.clear();
        final var response = Unirest
                .post("http://localhost:" + this.server.getPort() + "/documents")
                .body("--> invalid-document-content <--")
                .asString();

        assertTrue(response.isSuccess());
        assertFalse(response.getBody().isEmpty());

        // ensure that by given timeout the document has been successful processed.
        // and also verify that the document is processed.
        assertTrue(TimeTools.wait(() ->
                        MessagesCollector.hasMessages(
                                List.of("Reading Document has failed!")),
                TIMEOUT, WAIT));

        // verify status of document result
        final var strId = response.getBody();
        final var statusResponse = Unirest
                .get("http://localhost:" + this.server.getPort() + "/documents/" + strId)
                .asString();

        final var mapper = jacksonObjectMapper().findAndRegisterModules();
        final var result = mapper.readValue(statusResponse.getBody(), DocumentResult.class);

        assertTrue(statusResponse.isSuccess());
        assertFalse(result.isSuccess());
        // for the reading of the document no time is measured.
        assertEquals(result.getFinished(), result.getStarted());
    }
}

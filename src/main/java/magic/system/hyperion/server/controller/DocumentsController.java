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

import io.javalin.http.Context;
import io.javalin.plugin.openapi.annotations.HttpMethod;
import io.javalin.plugin.openapi.annotations.OpenApi;
import io.javalin.plugin.openapi.annotations.OpenApiContent;
import io.javalin.plugin.openapi.annotations.OpenApiParam;
import io.javalin.plugin.openapi.annotations.OpenApiRequestBody;
import io.javalin.plugin.openapi.annotations.OpenApiResponse;
import magic.system.hyperion.components.DocumentParameters;
import magic.system.hyperion.components.DocumentResult;
import magic.system.hyperion.reader.DocumentReader;
import magic.system.hyperion.server.HttpStatus;
import magic.system.hyperion.tools.TimeTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Controller for processing document requests.
 *
 * @author Thomas Lehmann
 */
public class DocumentsController {
    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentsController.class);

    /**
     * Default timeout for processing task groups.
     */
    private static final int DEFAULT_TASKGROUP_TIMEOUT = TimeTools.minutesAsMilliseconds(5);

    /**
     * Result map for document runs.
     */
    private static final Map<String, DocumentResult> RESULT_MAP = new ConcurrentHashMap<>();

    /**
     * Triggering "generation of document and running it".
     *
     * @param context request/response context.
     */
    @OpenApi(
            summary = "Post document to process tasks",
            operationId = "processDocumentWithTasks",
            path = "/documents",
            method = HttpMethod.POST,
            tags = {"Document"},
            queryParams = {
                    @OpenApiParam(name = "tag", type = String.class, isRepeatable = true,
                            allowEmptyValue = false),
                    @OpenApiParam(name = "timeout", type = Integer.class, isRepeatable = false)
            },
            requestBody = @OpenApiRequestBody(
                    description = "The id for the document processing for querying the result",
                    content = {@OpenApiContent(from = String.class)}),
            responses = {
                    @OpenApiResponse(status = HttpStatus.Constants.OK)
            }
    )
    public static void run(final Context context) {
        final var content = context.body().getBytes(StandardCharsets.UTF_8);
        final var strUniqueId = UUID.randomUUID().toString();

        new Thread(() -> {
            final var tags = context.queryParams("tag");
            final var strTimeout = context.queryParam(
                    "timeout", String.valueOf(DEFAULT_TASKGROUP_TIMEOUT));

            final var document = new DocumentReader().read(content);
            if (document == null) {
                RESULT_MAP.put(strUniqueId, DocumentResult.of());
                LOGGER.info("Reading Document has failed!");
            } else {
                LOGGER.info("Document object created from request body");
                LOGGER.info("Tags: {}, task group timeout: {}", tags, strTimeout);

                final var result = document.run(
                        DocumentParameters.of(tags, Integer.parseInt(strTimeout)));
                if (result.isSuccess()) {
                    LOGGER.info("Document request succeeded!");
                } else {
                    LOGGER.info("Document request failed!");
                }
                RESULT_MAP.put(strUniqueId, result);
            }

        }).start();

        context.status(HttpStatus.OK.getStatus());
        context.result(strUniqueId);
    }

    /**
     * Get status for a document that has been posted to be processed.
     *
     * @param context request/response context.
     */
    @OpenApi(
            summary = "Get result for processed document",
            operationId = "getDocumentStatus",
            path = "/documents/:id",
            pathParams = {@OpenApiParam(name = "id", type = String.class,
                    description = "The id for the document that has been processed")},
            method = HttpMethod.GET,
            tags = {"Document"},
            requestBody = @OpenApiRequestBody(
                    description = "The result of the document processing",
                    content = {@OpenApiContent(from = DocumentResult.class)}),
            responses = {
                    @OpenApiResponse(status = HttpStatus.Constants.OK),
                    @OpenApiResponse(status = HttpStatus.Constants.NOT_FOUND)
            }
    )
    public static void status(final Context context) {
        final var strId = context.pathParam("id", String.class).get();
        final var result = RESULT_MAP.get(strId);
        if (result == null) {
            context.status(HttpStatus.NOT_FOUND.getStatus());
        } else {
            RESULT_MAP.remove(strId);
            context.json(result);
            context.status(HttpStatus.OK.getStatus());
        }
    }
}

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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testing of class {@link DockerImageTask}.
 *
 * @author Thomas Lehmann
 */
@DisplayName("Testing DockerImage class")
@SuppressWarnings("checkstyle:multiplestringliterals")
public class DockerImageTaskTest {
    /**
     * Testing Docker image creation with embedded Dockerfile content.
     */
    @Test
    public void testEmbeddedCode() {
        final var strCode = "FROM centos:latest\n"
                + "COPY target/test-classes/scripts/say-hello-world.sh .\n"
                + "RUN chmod +x /say-hello-world.sh";
        final var dockerImageTask = new DockerImageTask("test", strCode);
        dockerImageTask.setRepositoryTag("image-test:latest");
        var result = dockerImageTask.run(TaskTestsTools.getDefaultTaskParameters());
        assertTrue(result.isSuccess());

        final var dockerContainerTask = new DockerContainerTask("test", "/say-hello-world.sh");
        dockerContainerTask.setImageName("image-test");
        result = dockerContainerTask.run(TaskTestsTools.getDefaultTaskParameters());
        assertEquals("hello world!", result.getVariable().getValue().strip());
    }

    /**
     * Testing Docker image creation with code referencing a path and file.
     */
    @Test
    public void testFromFile() throws URISyntaxException {
        final var scriptUrl = getClass().getResource("/dockerfiles/SimpleDockerfile");
        final var file = new File(scriptUrl.toURI());

        final var dockerImageTask = new DockerImageTask("test", file.getAbsolutePath());
        dockerImageTask.setRepositoryTag("image-test:latest");
        var result = dockerImageTask.run(TaskTestsTools.getDefaultTaskParameters());
        assertTrue(result.isSuccess());

        final var dockerContainerTask = new DockerContainerTask("test", "/say-hello-world.sh");
        dockerContainerTask.setImageName("image-test");
        result = dockerContainerTask.run(TaskTestsTools.getDefaultTaskParameters());
        assertEquals("hello world!", result.getVariable().getValue().strip());
    }

    /**
     * Testing copying of task.
     */
    @Test
    public void testCopy() {
        final var strCode = "FROM centos:latest\n"
                + "COPY target/test-classes/scripts/say-hello-world.sh .\n"
                + "RUN chmod +x /say-hello-world.sh";
        final var dockerImageTask = new DockerImageTask("test", strCode);

        assertEquals(dockerImageTask, dockerImageTask.copy());
    }
}

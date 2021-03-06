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

import magic.system.hyperion.components.Model;
import magic.system.hyperion.components.TaskParameters;
import magic.system.hyperion.tools.Capabilities;
import magic.system.hyperion.tools.ProcessResults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Testing of class {@link DockerContainerTask}.
 *
 * @author Thomas Lehmann
 */
@DisplayName("Testing DockerContainerTask class")
@TestMethodOrder(value = MethodOrderer.Random.class)
@SuppressWarnings("checkstyle:multiplestringliterals")
public class DockerContainerTaskTest {
    /**
     * Testing Docker (Docker image version is 'latest' and the target
     * environment is 'not windows' -> unix).
     */
    @Test
    public void testDockerForTargetUnix() {
        assumeTrue(Capabilities.hasDocker());

        final var task = new DockerContainerTask("test", "echo \"hello world!\"");
        task.setImageName("debian");
        final var result = task.run(TaskParameters.of(Model.of(), Map.of(), Map.of(), null));

        assertEquals("test", task.getTitle());
        assertEquals("debian", task.getImageName());
        assertEquals("latest", task.getImageVersion());
        assertEquals(DockerContainerTask.PLATFORM_UNIX, task.getPlatform());
        assertEquals("hello world!", result.getVariable().getValue().strip());
        assertTrue(result.isSuccess());

        // Just checking the two other setter
        task.setPlatform(DockerContainerTask.PLATFORM_WINDOWS);
        assertEquals(DockerContainerTask.PLATFORM_WINDOWS, task.getPlatform());
        task.setImageVersion("10");
        assertEquals("10", task.getImageVersion());
    }

    /**
     * Testing copying of task.
     */
    @Test
    public void testCopy() {
        final var task = new DockerContainerTask("test", "echo \"hello world!\"");
        task.setImageName("debian");
        assertEquals(task, task.copy());
    }

    /**
     * Testing to run docker container in background mode.
     */
    @Test
    public void testRunInBackground() throws IOException, InterruptedException {
        assumeTrue(Capabilities.hasDocker());

        final var task = new DockerContainerTask("test", "sleep 30");
        task.setImageName("debian");
        task.setDetached(true);
        final var result = task.run(TaskParameters.of(Model.of(), Map.of(), Map.of(), null));

        assertTrue(result.isSuccess());
        assertFalse(result.getVariable().getValue().isEmpty());

        // we just need to stop the container since --rm ensures that the container will be deleted
        final var stopContainerCommand = Capabilities.createCommand(
                String.join(" ", List.of("docker", "stop", result.getVariable().getValue())));
        final var stopContainerProcess = new ProcessBuilder(stopContainerCommand).start();
        final var stopContainerResult = ProcessResults.of(stopContainerProcess);
        assertEquals(0, stopContainerProcess.exitValue());
    }
}

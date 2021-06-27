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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;
import java.util.UUID;

/**
 * File utility for test classes.
 *
 * @author Thomas Lehmann
 */
public final class FileUtils {
    /**
     * Logger of this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    /**
     * Temporary path where to create files.
     */
    private static Path temporaryPath;

    /**
     * Should be never instantiated.
     */
    private FileUtils() {
        // Nothing to do here.
    }

    /**
     * Get Resource as absolute path.
     *
     * @param strName relative resource name and path as string.
     * @return absolute path to the ressource
     * @throws URISyntaxException when the URI cannot be resolved.
     * @since 1.0.0
     */
    public static Path getResourcePath(final String strName) throws URISyntaxException {
        final var url = FileUtils.class.getResource(strName);
        return new File(url.toURI()).toPath();
    }

    /**
     * Change temporary path.
     *
     * @param path new temporary path.
     */
    public static void setTemporaryPath(final Path path) {
        temporaryPath = path;
    }

    /**
     * Create temporary script path. On Unix the script is adjusted for full permission.
     *
     * @param strPrefix        prefix of file name
     * @param strFileExtension the expected file extension.
     * @return path of the file.
     * @throws IOException when creation has failed.
     * @since 1.0.0
     */
    public static Path createTemporaryFile(final String strPrefix,
                                           final String strFileExtension) throws IOException {
        final Path temporaryScriptPath;

        if (Capabilities.isWindows()) {
            if (temporaryPath == null) {
                temporaryScriptPath = Files.createTempFile(
                        strPrefix, UUID.randomUUID() + strFileExtension);
            } else {
                temporaryScriptPath = Files.createTempFile(
                        temporaryPath,
                        strPrefix, UUID.randomUUID() + strFileExtension);
            }
        } else {
            final Set<PosixFilePermission> ownerWritable
                    = PosixFilePermissions.fromString("rwxrwxrwx");
            final FileAttribute<?> permissions
                    = PosixFilePermissions.asFileAttribute(ownerWritable);

            if (temporaryPath == null) {
                temporaryScriptPath = Files.createTempFile(
                        strPrefix, UUID.randomUUID() + strFileExtension, permissions);
            } else {
                temporaryScriptPath = Files.createTempFile(
                        temporaryPath,
                        strPrefix, UUID.randomUUID() + strFileExtension, permissions);
            }
        }

        return temporaryScriptPath;
    }

    /**
     * Deletes a file or folder.
     *
     * @param path path to file or folder.
     * @since 1.0.0
     */
    public static void deletePath(final Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Copying and renaming file.
     *
     * @param sourcePath      path and filename of source.
     * @param destinationPath path and filename of destination.
     * @throws IOException when copy operation has failed.
     * @since 1.0.0
     */
    public static void copyFile(final Path sourcePath, final Path destinationPath)
            throws IOException {
        try (var inStream = new FileInputStream(sourcePath.toString());
             var outStream = new FileOutputStream(destinationPath.toString())) {
            inStream.transferTo(outStream);
        }
    }

    /**
     * Read YAML as tree providing root as {@link JsonNode}.
     *
     * @param path where to read the YAML file from.
     * @return tree
     * @throws IOException when reading of YAML file has failed.
     * @since 1.0.0
     */
    public static JsonNode readYamlTree(final Path path) throws IOException {
        final var mapper = new ObjectMapper(new YAMLFactory());
        return mapper.readTree(path.toUri().toURL());
    }
}

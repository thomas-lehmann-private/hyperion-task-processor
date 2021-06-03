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

import magic.system.hyperion.annotations.Named;
import magic.system.hyperion.interfaces.ICreator;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * Factory for creator of type E.
 *
 * @param <E> the instances of type E that should be created.
 * @author Thomas Lehmann
 */
public class Factory<E> {
    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Factory.class);

    /**
     * The package where to look for the classes.
     */
    private final String strPackageName;

    /**
     * Initialize with package name where to search for the types.
     *
     * @param strInitPackageName the package name.
     */
    public Factory(final String strInitPackageName) {
        this.strPackageName = strInitPackageName;
    }

    /**
     * Trying to create instance of type E for given name.
     *
     * @param strName name of the creator passes to @Named.
     * @return created instance or null if failed.
     */
    public E create(final String strName) {
        final var reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(this.strPackageName))
                .addScanners(new SubTypesScanner()));

        final var foundTypes = reflections.getSubTypesOf(ICreator.class);
        E instance = null;

        try {
            for (final var type : foundTypes) {
                for (final var annotation : type.getAnnotations()) {
                    if (annotation instanceof Named
                            && ((Named) annotation).value().equals(strName)) {
                        // creating the creator instance
                        final var creatorInstance
                                = (ICreator<E>) type.getDeclaredConstructors()[0].newInstance();
                        // creating the instance of interest
                        instance = creatorInstance.create();
                        break;
                    }
                }
            }
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            LOGGER.error(e.getMessage(), e);
            instance = null;
        }

        return instance;
    }
}

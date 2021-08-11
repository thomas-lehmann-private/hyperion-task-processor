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

import java.util.ServiceLoader;

/**
 * Factory for creator of type E.
 *
 * @param <E> creator of type E.
 * @author Thomas Lehmann
 */
public class Factory<E> {
    /**
     * Creator class.
     */
    private final Class<? extends ICreator<E>> creatorClass;

    /**
     * Initialize factory with creator class.
     *
     * @param initCreatorClass creator class.
     * @since 1.0.0
     */
    public Factory(final Class<? extends ICreator<E>> initCreatorClass) {
        this.creatorClass = initCreatorClass;
    }

    /**
     * Trying to create instance of type E for given name.
     *
     * @param strName name of the creator passes to @Named.
     * @return created instance or null if failed.
     * @since 1.0.0
     */
    public E create(final String strName) {
        E instance = null;

        final var loader = ServiceLoader.load(this.creatorClass);
        final var iterator = loader.iterator();

        while (iterator.hasNext()) {
            final var creator = iterator.next();

            for (final var annotation: creator.getClass().getAnnotations()) {
                if (annotation instanceof Named
                        && ((Named) annotation).value().equals(strName)) {
                    instance = (E) creator.create();
                    break;
                }
            }

            if (instance != null) {
                break;
            }
        }

        return instance;
    }
}

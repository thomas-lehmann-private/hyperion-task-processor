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
package magic.system.hyperion.generics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.Flow;

/**
 * Collector for items storing them into a list.
 *
 * @param <E> type of Element.
 *
 * @author Thomas Lehmann
 */
public class ListCollector<E> extends ArrayList<E> implements Flow.Subscriber<E> {
    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ListCollector.class);

    /**
     * Serial number.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Last subscription.
     */
    private transient Flow.Subscription subscription;

    @Override
    public void onSubscribe(Flow.Subscription initSubscription) {
        this.subscription = initSubscription;
        this.subscription.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(final E item) {
        this.add(item);
        this.subscription.request(Long.MAX_VALUE);
    }

    @Override
    public void onError(final Throwable throwable) {
        LOGGER.error(throwable.getMessage(), throwable);
    }

    @Override
    public void onComplete() {
        // Nothing to do.
    }
}

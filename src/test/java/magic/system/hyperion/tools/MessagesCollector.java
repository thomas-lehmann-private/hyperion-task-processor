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

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Log Appender for testing.
 */
public class MessagesCollector extends AppenderBase<ILoggingEvent> {
    /**
     * Collected messages.
     */
    private static final List<String> MESSAGES = Collections.synchronizedList(new ArrayList<>());

    /**
     * Readonly access to list of messages.
     *
     * @return list of messages.
     */
    public static List<String> getMessages() {
        return Collections.unmodifiableList(MESSAGES);
    }

    /**
     * Clearing all collected messages.
     */
    public static void clear() {
        MESSAGES.clear();
    }

    /**
     * Checking the logging to have lines contain given messages.
     *
     * @param messages messages to check.
     * @return true when all messages occur.
     */
    public static boolean hasMessages(final List<String> messages) {
        return messages.stream().allMatch(searchedMessage ->
                getMessages().stream().anyMatch(
                        givenMessage -> givenMessage.contains(searchedMessage)));
    }

    @Override
    protected void append(final ILoggingEvent event) {
        MESSAGES.add(event.getFormattedMessage());
    }
}

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
package magic.system.hyperion.matcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Matcher for a list with custom criteria.
 * <ul>
 * <li> All required values have to be there otherwise each value is optional
 * <li> All allowed values limitate the values otherwise allow all when nothing
 * has been defined. defined.
 * </ul>
 *
 * @param <E> element type of list.
 *
 * @author Thomas Lehmann
 */
public class ListMatcher<E> implements IMatcher<List<E>> {

    private static class Detail {

        /**
         * Minimum number of required occurences (default: 0).
         */
        private int iRequiredCount;

        /**
         * When true the value is allowed (when required also).
         */
        private boolean bAllowed = true;

        /**
         * When false then the require count is the minimum count; means you can
         * have more. When true it has to match exactly (default: false).
         */
        private boolean bExactRequiredCount;

        /**
         * Check required count.
         *
         * @return required count.
         */
        public int requiredCount() {
            return this.iRequiredCount;
        }

        /**
         * Change required count.
         *
         * @param iValue new value for required count.
         * @since 1.0.0
         */
        public void setRequiredCount(final int iValue) {
            this.iRequiredCount = iValue;
            this.bExactRequiredCount = false;

            if (this.iRequiredCount > 0) {
                this.bAllowed = true;
            }
        }

        /**
         * Checking whether exact required count is wanted.
         *
         * @return true when exact required count is wanted.
         * @since 1.0.0
         */
        public boolean isExactRequiredCount() {
            return this.bExactRequiredCount;
        }

        /**
         * Change the default from false to true for matching exact required
         * count.
         * @since 1.0.0
         */
        public void setExactRequiredCount() {
            this.bExactRequiredCount = true;
        }

        /**
         * Checking whether given value is allowed.
         *
         * @return true when value is allowed otherwise false.
         * @since 1.0.0
         */
        public boolean isAllowed() {
            return this.bAllowed;
        }

        /**
         * Change the allowed flag.
         *
         * @param bInitAllowed when true values is allowed otherwise false.
         * @since 1.0.0
         */
        public void setAllowed(final boolean bInitAllowed) {
            this.bAllowed = bInitAllowed;
            if (!this.bAllowed) {
                this.iRequiredCount = 0;
            }
        }
    }

    /**
     * Matcher details.
     */
    private final Map<E, Detail> details;

    /**
     * When strict the not allowed values are rejected. When just using require
     * other values tan those still can be part of the list except you call
     * allow.
     */
    private boolean bStrict;

    /**
     * Initialize Matcher.
     */
    public ListMatcher() {
        this.details = new HashMap<>();
        this.bStrict = false;
    }

    /**
     * Defines a value that is required at least once.
     *
     * @param value value that is required
     * @return matcher itself for chaining.
     * @since 1.0.0
     */
    public ListMatcher<E> requireOnce(final E value) {
        return requireCount(value, 1);
    }

    /**
     * Defines a value that is required exactly once.
     *
     * @param value value that is required
     * @return matcher itself for chaining.
     * @since 1.0.0
     */
    public ListMatcher<E> requireExactlyOnce(final E value) {
        return requireExactCount(value, 1);
    }

    /**
     * Defines the minimum number of occurences. A number of zero means it
     * should never occur except there is not require entry for the value; then
     * any number of the values is allowed.
     *
     * @param value value that is required
     * @param iCount the required minimum count of occurences.
     * @return matcher itself for chaining.
     * @since 1.0.0
     */
    public ListMatcher<E> requireCount(final E value, final int iCount) {
        var detail = this.details.get(value);
        if (detail == null) {
            detail = new Detail();
            this.details.put(value, detail);
        }
        detail.setRequiredCount(iCount);
        return this;
    }

    /**
     * Defines the exact number of occurences. A number of zero means it should
     * never occur except there is not require entry for the value; then any
     * number of the values is allowed.
     *
     * @param value value that is required
     * @param iCount the required exact count of occurences.
     * @return matcher itself for chaining.
     * @since 1.0.0
     */
    public ListMatcher<E> requireExactCount(final E value, final int iCount) {
        var detail = this.details.get(value);
        if (detail == null) {
            detail = new Detail();
            this.details.put(value, detail);
        }
        detail.setRequiredCount(iCount);
        detail.setExactRequiredCount();
        return this;
    }

    /**
     * Allow a value. Switches to strict mode mode.
     * Cannot be undone.
     *
     * @param value the value to allow.
     * @return matcher itself for chaining.
     * @since 1.0.0
     */
    public ListMatcher<E> allow(final E value) {
        this.bStrict = true;

        var detail = this.details.get(value);
        if (detail == null) {
            detail = new Detail();
            this.details.put(value, detail);
        } else {
            detail.setAllowed(true);
        }
        return this;
    }

    @Override
    public boolean matches(List<E> collection) {
        boolean bSuccess = verifyGivenValues(collection);
        if (bSuccess) {
            bSuccess = verifyNotGivenValues(collection);
        }
        return bSuccess;
    }

    private boolean verifyGivenValues(final List<E> collection) {
        boolean bSuccess = true;

        for (var value : collection) {
            final var detail = this.details.get(value);
            if (detail != null) {
                final var iCount = collection.stream().filter(entry -> entry.equals(
                        value)).count();
                final var bCountMatches = detail.isExactRequiredCount()
                        ? iCount == detail.requiredCount() : iCount >= detail.requiredCount();

                if (!detail.isAllowed() || !bCountMatches) {
                    bSuccess = false;
                    break;
                }
            } else {
                if (this.bStrict) {
                    bSuccess = false;
                    break;
                }
            }
        }

        return bSuccess;
    }
       
    private boolean verifyNotGivenValues(final List<E> collection) {
        boolean bSuccess = true;

        for (var entry : this.details.entrySet()) {
            final var iCount = collection.stream().filter(
                    value -> value.equals(entry.getKey())).count();
            final var bCountMatches = entry.getValue().isExactRequiredCount()
                    ? iCount == entry.getValue().requiredCount()
                    : iCount >= entry.getValue().requiredCount();

            if (!bCountMatches) {
                bSuccess = false;
                break;
            }
        }
        
        return bSuccess;
    }
    
}

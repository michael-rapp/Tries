/*
 * Copyright 2017 - 2019 Michael Rapp
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package de.mrapp.tries.util;

import de.mrapp.tries.Sequence;
import de.mrapp.util.Condition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.function.Function;

/**
 * An utility class, which provides methods for handling sequences.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class SequenceUtil {

    /**
     * A comparator, which allows to compare sequences to each other.
     *
     * @param <T> The type of the sequences
     */
    private static class SequenceComparator<T> implements Comparator<T> {

        /**
         * The comparator, which is used to compare keys.
         */
        private final Comparator<? super T> comparator;

        /**
         * Creates a new comparator, which allows to compare keys to each other.
         *
         * @param comparator The comparator, which should be used to compare keys, as an instance of
         *                   the type {@link Comparator} or null, if the natural order of the keys
         *                   should be used
         */
        SequenceComparator(@Nullable final Comparator<? super T> comparator) {
            this.comparator = comparator;
        }

        @SuppressWarnings("unchecked")
        @Override
        public final int compare(@Nullable final T o1, @Nullable final T o2) {
            if (o1 == null) {
                return o2 != null ? -1 : 0;
            } else if (o2 == null) {
                return 1;
            } else if (comparator != null) {
                return comparator.compare(o1, o2);
            } else {
                return ((Comparable<? super T>) o1).compareTo(o2);
            }
        }

    }

    /**
     * Creates a new utility class, which provides methods for handling sequences.
     */
    private SequenceUtil() {

    }

    /**
     * Returns, whether a specific sequence is null or empty.
     *
     * @param sequence The sequence, which should be checked, as an instance of the type {@link
     *                 Sequence} or null
     * @return True, if the given sequence is null or empty, false otherwise
     */
    public static boolean isEmpty(@Nullable final Sequence sequence) {
        return sequence == null || sequence.isEmpty();
    }

    /**
     * Returns a subsequence of a specific sequence, starting at a specific index and spanning to
     * the end. If the given start index is invalid, an {@link IndexOutOfBoundsException} will be
     * thrown.
     *
     * @param sequence The sequence as an instance of the generic type {@link T}. The sequence may
     *                 not be null
     * @param start    The start index (inclusive) of the subsequence to return as an {@link
     *                 Integer} value
     * @param <T>      The type of the sequence
     * @return The subsequence of the given sequence as an instance of the generic type {@link T}.
     * The subsequence may not be null
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public static <T extends Sequence> T subsequence(@NotNull final T sequence, final int start) {
        Condition.INSTANCE.ensureNotNull(sequence, "The sequence may not be null");
        return (T) sequence.subsequence(start);
    }

    /**
     * Returns a subsequence of a specific sequence, starting at a specific index and spanning to an
     * end index. If the given start or end index is invalid, an {@link IndexOutOfBoundsException}
     * will be thrown.
     *
     * @param sequence The sequence as an instance of the generic type {@link T}. The sequence may
     *                 not be null
     * @param start    The start index (inclusive) of the subsequence to return as an {@link
     *                 Integer} value
     * @param end      The end index (exclusive) of the subsequence to return as an {@link Integer}
     *                 value
     * @param <T>      The type of the sequence
     * @return The subsequence of the given sequence as an instance of the generic type {@link T}.
     * The subsequence may not be null
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public static <T extends Sequence> T subsequence(@NotNull final T sequence, final int start,
                                                     final int end) {
        Condition.INSTANCE.ensureNotNull(sequence, "The sequence may not be null");
        return (T) sequence.subsequence(start, end);
    }

    /**
     * Concatenates two sequences. If one of the given sequences is null, the other one is returned.
     * If both are null, null is returned.
     *
     * @param prefix The first sequence as an instance of the generic type {@link T}
     * @param suffix The second sequence to be appended to the first one as an instance of the
     *               generic type {@link T}
     * @param <T>    The type of the sequences
     * @return The concatenated sequence as an instance of the generic type {@link T} or null, if
     * both of the given sequences are null
     */
    @SuppressWarnings("unchecked")
    public static <T extends Sequence> T concat(@Nullable final T prefix,
                                                @Nullable final T suffix) {
        if (prefix == null && suffix == null) {
            return null;
        } else if (prefix == null) {
            return suffix;
        } else if (suffix == null) {
            return prefix;
        }

        return (T) prefix.concat(suffix);
    }

    /**
     * Creates and returns a comparator, which allows to compare sequences.
     *
     * @param comparator The comparator, which should be used to compare the sequences, as an
     *                   instance of the type {@link Comparator} or null, if the natural order
     *                   should be used
     * @param <T>        The type of the sequences
     * @return The comparator, which has been created, as an instance of the type {@link
     * Comparator}. The comparator may not be null
     */
    @NotNull
    public static <T extends Sequence> Comparator<? super T> comparator(
            @Nullable final Comparator<? super T> comparator) {
        return new SequenceComparator<>(comparator);
    }

    /**
     * Returns the longest common prefix of two sequences.
     *
     * @param sequence1 The first sequence as an instance of the generic type {@link T}. The
     *                  sequence may not be null
     * @param sequence2 The second sequence as an instance of the generic type {@link T}. The
     *                  sequence may not be null
     * @param <T>       The type of the sequences
     * @return The longest common prefix of the given sequences as an instance of the generic type
     * {@link T} or null, if the sequences do not have a common prefix
     */
    @Nullable
    public static <T extends Sequence> T getCommonPrefix(@NotNull final T sequence1,
                                                         @NotNull final T sequence2) {
        Condition.INSTANCE.ensureNotNull(sequence1, "The first sequence may not be null");
        Condition.INSTANCE.ensureNotNull(sequence2, "The second sequence may not be null");

        if (sequence1.isEmpty() || sequence2.isEmpty()) {
            return null;
        } else {
            boolean matches = true;
            T commonPrefix = null;
            int length = 0;

            while (matches && length < sequence1.length() && length < sequence2.length()) {
                length++;
                T prefix1 = subsequence(sequence1, 0, length);
                T prefix2 = subsequence(sequence2, 0, length);
                matches = prefix1.equals(prefix2);

                if (matches) {
                    commonPrefix = prefix1;
                }
            }

            return commonPrefix;
        }
    }

    /**
     * Performs a binary search to find a specific sequence.
     *
     * @param size       The total number of sequences as an {@link Integer} value
     * @param getter     A function, which returns the corresponding sequence for a given index, as
     *                   an instance of the type {@link Function}. The function may not be null
     * @param comparator A comparator, which allows to compare sequences to each other, as an
     *                   instance of the type {@link Comparator} or null, if the natural order of
     *                   the sequences should be used
     * @param sequence   The sequence to search for as an instance of the generic type {@link T}.
     *                   The sequence may not be null
     * @param <T>        The type of the sequences
     * @return The index of the given sequence as an {@link Integer} value or -1, if the sequence
     * could not be found
     */
    public static <T extends Sequence> int binarySearch(final int size,
                                                        @NotNull final Function<Integer, T> getter,
                                                        @Nullable final Comparator<? super T> comparator,
                                                        @NotNull final T sequence) {
        Condition.INSTANCE.ensureNotNull(getter, "The function may not be null");

        if (size > 0) {
            Comparator<? super T> comp = SequenceUtil.comparator(comparator);
            int min = 0;
            int max = size - 1;

            while (min <= max) {
                int pivot = (min + max) >>> 1;
                T currentSequence = getter.apply(pivot);
                int order = comp.compare(currentSequence, sequence);

                if (order < 0) {
                    min = pivot + 1;
                } else if (order > 0) {
                    max = pivot - 1;
                } else {
                    return pivot;
                }
            }
        }

        return -1;
    }

}
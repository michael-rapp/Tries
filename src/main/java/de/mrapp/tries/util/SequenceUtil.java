/*
 * Copyright 2017 Michael Rapp
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static de.mrapp.util.Condition.ensureNotNull;

/**
 * An utility class, which provides methods for handling sequences.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class SequenceUtil {

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
        ensureNotNull(sequence, "The sequence may not be null");
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
        ensureNotNull(sequence, "The sequence may not be null");
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

}
/*
 * Copyright 2017 - 2018 Michael Rapp
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
package de.mrapp.tries;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Defines the interface, a sequence, which can be used as the keys of a {@link Trie} or {@link
 * SortedTrie} must implement. A sequence is a ordered list of elements, e.g. characters, numbers or
 * symbols.
 * <p>
 * Implementations of this class must be immutable, i.e. the {@link #concat(Sequence)} and {@link
 * #subsequence(int, int)} methods must return a new object instead of modifying the original one.
 * Furthermore, the {@link #hashCode()} and {@link #equals(Object)} methods must be overridden by
 * implementing classes. If an implementation of this interface should be used together with a
 * {@link SortedTrie}, without requiring to pass a {@link java.util.Comparator} to the trie, the
 * sequence must also implement the interface {@link Comparable}.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public interface Sequence extends Serializable {

    /**
     * Returns a new sequence, which is a subsequence of this sequence, starting at a specific index
     * and spanning to the end.
     *
     * @param start Specifies the index of the first element to be included (inclusive) as an {@link
     *              Integer} value. If the index is invalid, an {@link IndexOutOfBoundsException}
     *              may be thrown
     * @return A new sequence, which consists of the elements within the specified range, as an
     * instance of the type {@link Sequence}. The returned sequence must be a new immutable object
     * and its class must be the same as the class of the original sequence
     */
    default Sequence subsequence(int start) {
        return subsequence(start, length());
    }

    /**
     * Returns a new sequence, which is a subsequence of this sequence.
     *
     * @param start Specifies the index of the first element to be included (inclusive) as an {@link
     *              Integer} value. If the index is invalid, an {@link IndexOutOfBoundsException}
     *              may be thrown
     * @param end   Specifies the index of the last element to be included (exclusive) as an {@link
     *              Integer} value. If the index is invalid, an {@link IndexOutOfBoundsException}
     *              may be thrown
     * @return A new sequence, which consists of the elements within the specified range, as an
     * instance of the type {@link Sequence}. The returned sequence must be a new immutable object
     * and its class must be the same as the class of the original sequence
     */
    Sequence subsequence(int start, int end);

    /**
     * Concatenates this sequence with another one.
     *
     * @param sequence The sequence, which should be appended to this sequence as a suffix, as an
     *                 instance of the type {@link Sequence}. If the given sequence's class is
     *                 different from this classes' one, a {@link ClassCastException} may be thrown
     * @return A new sequence, which contains this sequences as a prefix and the given one as a
     * suffix, as an instance of the type {@link Sequence}. The returned sequence must be a new
     * immutable object and its class must be the same as the class of the original sequence
     */
    Sequence concat(@NotNull Sequence sequence);

    /**
     * Returns, whether the sequence is empty, i.e. its length is 0, or not.
     *
     * @return True, if the sequence is empty, false otherwise
     */
    default boolean isEmpty() {
        return !(length() > 0);
    }

    /**
     * Returns the length of the sequence.
     *
     * @return The length of the sequence as an {@link Integer} value. The length must be at least 0
     */
    int length();

}
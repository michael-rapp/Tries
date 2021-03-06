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
package de.mrapp.tries;

import de.mrapp.tries.datastructure.*;
import de.mrapp.tries.sequence.StringSequence;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class consists exclusively of static methods that operate on or return tries. It can be
 * considered as the trie-related pendant of the utility class {@link java.util.Collections}.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public final class Tries {

    /**
     * The empty {@link Trie} (immutable). This trie is serializable.
     *
     * @see #emptyTrie()
     */
    public static final Trie<?, ?> EMPTY_TRIE = new EmptyTrie<>();

    /**
     * The empty {@link SortedTrie} (immutable). This trie is serializable.
     *
     * @see #emptySortedTrie()
     */
    public static final SortedTrie<?, ?> EMPTY_SORTED_TRIE = new EmptySortedTrie<>();

    /**
     * The empty {@link StringTrie} (immutable). This trie is serializable.
     *
     * @see #emptyStringTrie()
     */
    public static final StringTrie<?> EMPTY_STRING_TRIE = new StringTrieWrapper<>(
            new EmptyTrie<>());

    /**
     * The empty {@link SortedStringTrie} (immutable). This trie is serializable.
     *
     * @see #emptySortedStringTrie()
     */
    public static final SortedStringTrie<?> EMPTY_SORTED_STRING_TRIE = new SortedStringTrieWrapper<>(
            new EmptySortedTrie<>());

    /**
     * Creates a new class that exclusively consists of static methods that operate on or return
     * tries.
     */
    private Tries() {

    }

    /**
     * Returns the empty {@link Trie} (immutable). The returned trie is serializable.
     *
     * @param <K> The type of the sequences, which are used as the trie's keys
     * @param <V> The type of the values, which are stored by the trie
     * @return The empty trie as an instance of the type {@link Trie}. The trie may not be null
     * @see #EMPTY_TRIE
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public static <K extends Sequence, V> Trie<K, V> emptyTrie() {
        return (Trie<K, V>) EMPTY_TRIE;
    }

    /**
     * Returns the empty {@link SortedTrie} (immutable). The returned trie is serializable.
     *
     * @param <K> The type of the sequences, which are used as the trie's keys
     * @param <V> The type of the values, which are stored by the trie
     * @return The empty trie as an instance of the type {@link SortedTrie}. The trie may not be
     * null
     * @see #EMPTY_SORTED_TRIE
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public static <K extends Sequence, V> SortedTrie<K, V> emptySortedTrie() {
        return (SortedTrie<K, V>) EMPTY_SORTED_TRIE;
    }

    /**
     * Returns the empty {@link StringTrie} (immutable). The returned trie is serializable.
     *
     * @param <V> The type of the values, which are stored by the trie
     * @return The empty trie as an instance of the type {@link StringTrie}. The trie may not be
     * null
     * @see #EMPTY_STRING_TRIE
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public static <V> StringTrie<V> emptyStringTrie() {
        return (StringTrie<V>) EMPTY_STRING_TRIE;
    }

    /**
     * Returns the empty {@link SortedStringTrie} (immutable). The returned trie is serializable.
     *
     * @param <V> The type of the values, which are stored by the trie
     * @return The empty trie as an instance of the type {@link SortedStringTrie}. The trie may not
     * be null
     * @see #EMPTY_SORTED_STRING_TRIE
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public static <V> SortedStringTrie<V> emptySortedStringTrie() {
        return (SortedStringTrie<V>) EMPTY_SORTED_STRING_TRIE;
    }

    /**
     * Returns an immutable {@link Trie}, which only contains a single entry. The returned trie is
     * serializable.
     *
     * @param <K>   The type of the sequences, which are used as the trie's keys
     * @param <V>   The type of the values, which are stored by the trie
     * @param key   The key of the entry, which should be stored in the trie, as an instance of the
     *              generic type {@link K} or null
     * @param value The value of the entry, which should be stored in the trie, as an instance of
     *              the generic type {@link V} or null
     * @return An immutable trie, which contains the given key-value mapping, as an instance of the
     * type {@link Trie}. The trie may not be null
     */
    @NotNull
    public static <K extends Sequence, V> Trie<K, V> singletonTrie(@Nullable final K key,
                                                                   @Nullable final V value) {
        return new SingletonTrie<>(key, value);
    }

    /**
     * Returns an immutable {@link SortedTrie}, which only contains a single entry. The returned
     * trie is serializable.
     *
     * @param <K>   The type of the sequences, which are used as the trie's keys
     * @param <V>   The type of the values, which are stored by the trie
     * @param key   The key of the entry, which should be stored in the trie, as an instance of the
     *              generic type {@link K} or null
     * @param value The value of the entry, which should be stored in the trie, as an instance of
     *              the generic type {@link V} or null
     * @return An immutable trie, which contains the given key-value mapping, as an instance of the
     * type {@link SortedTrie}. The trie may not be null
     */
    @NotNull
    public static <K extends Sequence, V> SortedTrie<K, V> singletonSortedTrie(
            @Nullable final K key,
            @Nullable final V value) {
        return new SingletonSortedTrie<>(key, value);
    }

    /**
     * Returns an immutable {@link StringTrie}, which only contains a single entry. The returned
     * trie is serializable.
     *
     * @param <V>   The type of the values, which are stored by the trie
     * @param key   The key of the entry, which should be stored in the trie, as a {@link String} or
     *              null
     * @param value The value of the entry, which should be stored in the trie, as an instance of
     *              the generic type {@link V} or null
     * @return An immutable trie, which contains the given key-value mapping, as an instance of the
     * type {@link StringTrie}. The trie may not be null
     */
    @NotNull
    public static <V> StringTrie<V> singletonStringTrie(@Nullable final String key,
                                                        @Nullable final V value) {
        return new StringTrieWrapper<>(
                new SingletonTrie<>(StringSequence.convertFromString(key), value));
    }

    /**
     * Returns an immutable {@link SortedStringTrie}, which only contains a single entry. The
     * returned trie is serializable.
     *
     * @param <V>   The type of the values, which are stored by the trie
     * @param key   The key of the entry, which should be stored in the trie, as a {@link String} or
     *              null
     * @param value The value of the entry, which should be stored in the trie, as an instance of
     *              the generic type {@link V} or null
     * @return An immutable trie, which contains the given key-value mapping, as an instance of the
     * type {@link SortedStringTrie}. The trie may not be null
     */
    @NotNull
    public static <V> SortedStringTrie<V> singletonSortedStringTrie(@Nullable final String key,
                                                                    @Nullable final V value) {
        return new SortedStringTrieWrapper<>(
                new SingletonSortedTrie<>(StringSequence.convertFromString(key), value));
    }

    /**
     * Returns an unmodifiable view of a specific {@link Trie}. This method allows modules to
     * provide users with "read-only" access to internal tries. Query operations on the returned
     * trie "read through" to the specified trie, whereas attempts to modify the returned trie
     * result in an {@link UnsupportedOperationException}.
     * <p>
     * The returned trie will be serializable if the specified trie is serializable.
     *
     * @param <K>  The type of the sequences, which are used as the trie's keys
     * @param <V>  The type of the values, which are stored by the trie
     * @param trie The trie for which an unmodifiable view should be returned as an instance of the
     *             type {@link Trie}. The trie may not be null
     * @return An unmodifiable view of the given trie as an instance of the type {@link Trie}. The
     * view may not be null
     */
    @NotNull
    public static <K extends Sequence, V> Trie<K, V> unmodifiableTrie(
            @NotNull final Trie<K, V> trie) {
        return new UnmodifiableTrie<>(trie);
    }

    /**
     * Returns an unmodifiable view of a specific {@link SortedTrie}. This method allows modules to
     * provide users with "read-only" access to internal tries. Query operations on the returned
     * trie "read through" to the specified trie, whereas attempts to modify the returned trie
     * result in an {@link UnsupportedOperationException}.
     * <p>
     * The returned trie will be serializable if the specified trie is serializable.
     *
     * @param <K>  The type of the sequences, which are used as the trie's keys
     * @param <V>  The type of the values, which are stored by the trie
     * @param trie The trie for which an unmodifiable view should be returned as an instance of the
     *             type {@link SortedTrie}. The trie may not be null
     * @return An unmodifiable view of the given trie as an instance of the type {@link SortedTrie}.
     * The view may not be null
     */
    @NotNull
    public static <K extends Sequence, V> SortedTrie<K, V> unmodifiableSortedTrie(
            @NotNull final SortedTrie<K, V> trie) {
        return new UnmodifiableSortedTrie<>(trie);
    }

    /**
     * Returns an unmodifiable view of a specific {@link StringTrie}. This method allows modules to
     * provide users with "read-only" access to internal tries. Query operations on the returned
     * trie "read through" to the specified trie, whereas attempts to modify the returned trie
     * result in an {@link UnsupportedOperationException}.
     * <p>
     * The returned trie will be serializable if the specified trie is serializable.
     *
     * @param <V>  The type of the values, which are stored by the trie
     * @param trie The trie for which an unmodifiable view should be returned as an instance of the
     *             type {@link StringTrie}. The trie may not be null
     * @return An unmodifiable view of the given trie as an instance of the type {@link StringTrie}.
     * The view may not be null
     */
    @NotNull
    public static <V> StringTrie<V> unmodifiableStringTrie(@NotNull final StringTrie<V> trie) {
        return new UnmodifiableStringTrie<>(trie);
    }

    /**
     * Returns an unmodifiable view of a specific {@link SortedStringTrie}. This method allows
     * modules to provide users with "read-only" access to internal tries. Query operations on the
     * returned trie "read through" to the specified trie, whereas attempts to modify the returned
     * trie result in an {@link UnsupportedOperationException}.
     * <p>
     * The returned trie will be serializable if the specified trie is serializable.
     *
     * @param <V>  The type of the values, which are stored by the trie
     * @param trie The trie for which an unmodifiable view should be returned as an instance of the
     *             type {@link SortedStringTrie}. The trie may not be null
     * @return An unmodifiable view of the given trie as an instance of the type {@link
     * SortedStringTrie}. The view may not be null
     */
    @NotNull
    public static <V> SortedStringTrie<V> unmodifiableSortedStringTrie(
            @NotNull final SortedStringTrie<V> trie) {
        return new UnmodifiableSortedStringTrie<>(trie);
    }

}
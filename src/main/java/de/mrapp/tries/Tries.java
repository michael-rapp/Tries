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
package de.mrapp.tries;

import de.mrapp.tries.datastructure.EmptySortedTrie;
import de.mrapp.tries.datastructure.EmptyTrie;
import org.jetbrains.annotations.NotNull;

/**
 * This class consists exclusively of static methods that operate on or return tries. It can be
 * considered as the trie-related pendant of the utility class {@link java.util.Collections}.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public final class Tries {

    /**
     * The empty trie (immutable). This trie is serializable.
     *
     * @see #emptyTrie()
     */
    public static final Trie<?, ?> EMPTY_TRIE = new EmptyTrie<>();

    /**
     * The empty sorted trie (immutable). This trie is serializable.
     *
     * @see #emptySortedTrie()
     */
    public static final SortedTrie<?, ?> EMPTY_SORTED_TRIE = new EmptySortedTrie<>();

    /**
     * Creates a new class that exclusively consists of static methods that operate on or return
     * tries.
     */
    private Tries() {

    }

    /**
     * Returns the empty trie (immutable). This trie is serializable.
     *
     * @param <K> The type of the sequences, which are used as the trie's keys
     * @param <V> The type of the values, which are stored by trie
     * @return The empty trie as an instance of the type {@link Trie}. The trie may not be null
     * @see #EMPTY_TRIE
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public static <K extends Sequence, V> Trie<K, V> emptyTrie() {
        return (Trie<K, V>) EMPTY_TRIE;
    }

    /**
     * Returns the empty sorted trie (immutable). This trie is serializable.
     *
     * @param <K> The type of the sequences, which are used as the trie's keys
     * @param <V> The type of the values, which are stored by trie
     * @return The empty sorted trie as an instance of the type {@link SortedTrie}. The trie may not
     * be null
     * @see #EMPTY_SORTED_TRIE
     */
    @SuppressWarnings("unchecked")
    public static <K extends Sequence, V> SortedTrie<K, V> emptySortedTrie() {
        return (SortedTrie<K, V>) EMPTY_SORTED_TRIE;
    }

}
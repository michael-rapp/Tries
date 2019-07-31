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

import de.mrapp.tries.datastructure.SortedStringTrieWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Map;

/**
 * A sorted trie, which stores the successors of nodes in sorted lists. It is the pendant of the
 * class {@link PatriciaTrie} for using character sequences as keys. In contrast to a {@link
 * SortedListStringTrie}, the edges between nodes do not always correspond to a single element of a
 * sequence. Instead, subsequent nodes that only have a single successor are merged to a single node
 * to reduce space complexity. This trie implementation has the same properties as a {@link
 * PatriciaTrie}. It should be preferred when using character sequences, because it offers a less
 * complex API.
 *
 * @param <ValueType> The type of the values, which are stored by the trie
 * @author Michael Rapp
 * @since 1.0.0
 */
public class PatriciaStringTrie<ValueType> extends SortedStringTrieWrapper<ValueType> {

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = -8815428420083659153L;

    /**
     * Creates a new empty, sorted trie for storing character sequences, which uses sorted lists for
     * storing the successors of nodes. Subsequent nodes that only have a single successor are
     * merged to a single node to reduce space complexity. For comparing keys with each other, the
     * natural ordering of the keys is used.
     */
    public PatriciaStringTrie() {
        this((Comparator<? super String>) null);
    }

    /**
     * Creates a new empty, sorted trie for storing character sequences, which uses sorted lists for
     * storing the successors of nodes. Subsequent nodes that only have a single successor are
     * merged to a single node to reduce space complexity.
     *
     * @param comparator The comparator, which should be used to compare keys with each other, as an
     *                   instance of the type {@link Comparator} or null, if the natural ordering of
     *                   the keys should be used
     */
    public PatriciaStringTrie(@Nullable final Comparator<? super String> comparator) {
        super(new PatriciaTrie<>(
                comparator != null ? new StringSequenceComparatorWrapper(comparator) : null));
    }

    /**
     * Creates a new sorted trie for storing character sequences, which uses sorted lists for
     * storing the successors of nodes and contains all key-value pairs that are contained by a map.
     * Subsequent nodes that only have a single successor are merged to a single node to reduce
     * space complexity. For comparing keys with each other, the natural ordering of the keys is
     * used.
     *
     * @param map The map, which contains the key-value pairs that should be added to the trie, as
     *            an instance of the type {@link Map}. The map may not be null
     */
    public PatriciaStringTrie(@NotNull final Map<String, ValueType> map) {
        this(null, map);
    }

    /**
     * Creates a new sorted trie for storing character sequences, which uses sorted lists for
     * storing the successors of nodes and contains all key-value pairs that are contained by a map.
     * Subsequent nodes that only have a single successor are merged to a single node to reduce
     * space complexity.
     *
     * @param comparator The comparator, which should be used to compare keys with each other, as an
     *                   instance of the type {@link Comparator} or null, if the natural ordering of
     *                   the keys should be used
     * @param map        The map, which contains the key-value pairs that should be added to the
     *                   trie, as an instance of the type {@link Map}. The map may not be null
     */
    public PatriciaStringTrie(@Nullable final Comparator<? super String> comparator,
                              @NotNull final Map<String, ValueType> map) {
        super(new PatriciaTrie<>(
                comparator != null ? new StringSequenceComparatorWrapper(comparator) : null));
        putAll(map);
    }

    @Override
    public final String toString() {
        return "PatriciaStringTrie " + entrySet().toString();
    }

}
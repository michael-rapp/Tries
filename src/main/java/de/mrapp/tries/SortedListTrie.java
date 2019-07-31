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

import de.mrapp.tries.datastructure.AbstractSortedTrie;
import de.mrapp.tries.datastructure.node.SortedListNode;
import de.mrapp.tries.structure.SortedStructure;
import de.mrapp.tries.structure.UncompressedSortedStructure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * A sorted trie, which stores the successors of nodes in sorted lists. The edges between nodes
 * always correspond to exactly one element of a sequence. Therefore, even if a subsequence of
 * length n is not shared between multiple keys, the trie contains n nodes to store that
 * subsequence. Successors are looked up using binary search, resulting in logarithmic complexity.
 * Although this is worse than the constant complexity provided by a {@link HashTrie}, this trie
 * implementation should be preferred, if the order of keys is relevant.
 *
 * @param <SequenceType> The type of the sequences, which are used as the trie's keys
 * @param <ValueType>    The type of the values, which are stored by the trie
 * @author Michael Rapp
 * @since 1.0.0
 */
public class SortedListTrie<SequenceType extends Sequence, ValueType>
        extends AbstractSortedTrie<SequenceType, ValueType> {

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = -1139777657107659140L;

    /**
     * Creates a new sorted trie, which stores the successors of nodes in sorted lists.
     *
     * @param rootNode   The root node of the trie as an instance of the type {@link Node} or null,
     *                   if the trie should be empty
     * @param comparator The comparator, which should be used to compare keys with each other, as an
     *                   instance of the type {@link Comparator} or null, if the natural ordering of
     *                   the keys should be used
     */
    private SortedListTrie(@Nullable final Node<SequenceType, ValueType> rootNode,
            @Nullable final Comparator<? super SequenceType> comparator) {
        super(rootNode, comparator);
    }

    /**
     * Creates a new empty, sorted trie, which stores the successors of nodes in sorted lists. For
     * comparing keys with each other, the natural ordering of the keys is used.
     */
    public SortedListTrie() {
        super(null);
    }

    /**
     * Creates a new sorted trie, which stores the successors of nodes in sorted lists and contains
     * all key-value pairs that are contained by a map. For comparing keys with each other, the
     * natural ordering of the keys is used.
     *
     * @param map The map, which contains the key-value pairs that should be added to the trie, as
     *            an instance of the type {@link Map}. The map may not be null
     */
    public SortedListTrie(@NotNull final Map<SequenceType, ValueType> map) {
        super(null, map);

    }

    /**
     * Creates a new empty, sorted trie, which stores the successors of nodes in sorted lists.
     *
     * @param comparator The comparator, which should be used to compare keys with each other, as an
     *                   instance of the type {@link Comparator} or null, if the natural ordering of
     *                   the keys should be used
     */
    public SortedListTrie(@Nullable final Comparator<? super SequenceType> comparator) {
        super(comparator);
    }

    /**
     * Creates a new sorted trie, which stores the successors of nodes in sorted lists and contains
     * all key-value pairs that are contained by a map.
     *
     * @param comparator The comparator, which should be used to compare keys with each other, as an
     *                   instance of the type {@link Comparator} or null, if the natural ordering of
     *                   the keys should be used
     * @param map        The map, which contains the key-value pairs that should be added to the
     *                   trie, as an instance of the type {@link Map}. The map may not be null
     */
    public SortedListTrie(@Nullable final Comparator<? super SequenceType> comparator,
            @NotNull final Map<SequenceType, ValueType> map) {
        super(comparator, map);
    }

    @NotNull
    @Override
    protected final Node<SequenceType, ValueType> createRootNode() {
        return new SortedListNode<>(comparator);
    }

    @NotNull
    @Override
    protected final SortedStructure<SequenceType, ValueType> createStructure() {
        return new UncompressedSortedStructure<>();
    }

    @NotNull
    @Override
    public final SortedListTrie<SequenceType, ValueType> subTrie(
            @Nullable final SequenceType sequence) {
        Node<SequenceType, ValueType> node = getNode(sequence);

        if (node != null) {
            if (node.hasSuccessors()) {
                Node<SequenceType, ValueType> rootNode =
                        structure.getSubTrie(sequence, createRootNode(), node, false);
                return new SortedListTrie<>(rootNode, comparator);
            } else {
                return new SortedListTrie<>(null, comparator);
            }
        }

        throw new NoSuchElementException();
    }

    @Override
    public final String toString() {
        return "SortedListTrie " + entrySet().toString();
    }

}
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

import de.mrapp.tries.datastructure.AbstractSortedTrie;
import de.mrapp.tries.datastructure.node.SortedListNode;
import de.mrapp.tries.structure.PatriciaStructure;
import de.mrapp.tries.structure.SortedStructure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * A sorted trie, which stores the successor of nodes in sorted lists. In contrast to a {@link
 * SortedListTrie}, the edges between nodes do not always correspond to a single element of a
 * sequence. Instead, subsequent nodes that only have a single successor are merged to a single node
 * to reduce space complexity. This requires to reorganize the tree structure when inserting new
 * elements. Consequently, this trie should be preferred over the a {@link SortedListTrie}, if new
 * elements are only inserted sporadically and minimizing memory consumption is important.
 *
 * @param <SequenceType> The type of the sequences, which are used as the trie's keys
 * @param <ValueType>    The type of the values, which are stored by the trie
 * @author Michael Rapp
 * @since 1.0.0
 */
public class PatriciaTrie<SequenceType extends Sequence, ValueType>
        extends AbstractSortedTrie<SequenceType, ValueType> {

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = 3229102065205655196L;

    /**
     * Creates a new Patricia trie.
     *
     * @param rootNode   The root node of the trie as an instance of the type {@link Node} or null,
     *                   if the trie should be empty
     * @param comparator The comparator, which should be used to compare keys with each other, as an
     *                   instance of the type {@link Comparator} or null, if the natural ordering of
     *                   the keys should be used
     */
    protected PatriciaTrie(@Nullable final Node<SequenceType, ValueType> rootNode,
            @Nullable final Comparator<? super SequenceType> comparator) {
        super(rootNode, comparator);
    }

    /**
     * Creates a new, empty Patricia trie.
     */
    public PatriciaTrie() {
        super(null);
    }

    /**
     * Creates a new Patricia trie, which contains all key-value pairs that are contained by a map.
     * For comparing keys with each other, the natural ordering of the keys is used.
     *
     * @param map The map, which contains the key-value pairs that should be added to the trie, as
     *            an instance of the type {@link Map}. The map may not be null
     */
    public PatriciaTrie(@NotNull final Map<SequenceType, ValueType> map) {
        super(null, map);
    }

    /**
     * Creates a new, empty Patricia trie.
     *
     * @param comparator The comparator, which should be used to compare keys with each other, as an
     *                   instance of the type {@link Comparator} or null, if the natural ordering of
     *                   the keys should be used
     */
    public PatriciaTrie(@Nullable final Comparator<? super SequenceType> comparator) {
        super(comparator);
    }

    /**
     * Creates a new Patrica trie, which contains all key-value pairs that are contained by a map.
     *
     * @param comparator The comparator, which should be used to compare keys with each other, as an
     *                   instance of< the type {@link Comparator} or null, if the natural ordering
     *                   of the keys should be used
     * @param map        The map, which contains the key-value pairs that should be added to the
     *                   trie, as an instance of the type {@link Map}. The map may not be null
     */
    public PatriciaTrie(@Nullable final Comparator<? super SequenceType> comparator,
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
        return new PatriciaStructure<>();
    }

    @NotNull
    @Override
    public final SortedTrie<SequenceType, ValueType> subTrie(@NotNull final SequenceType sequence) {
        Node<SequenceType, ValueType> node = getNode(sequence);

        if (node != null) {
            Node<SequenceType, ValueType> rootNode =
                    structure.getSubTrie(sequence, createRootNode(), node);
            return new PatriciaTrie<>(rootNode, comparator);
        }

        throw new NoSuchElementException();
    }

    @Override
    public final String toString() {
        return "PatriciaTrie " + entrySet().toString();
    }

}
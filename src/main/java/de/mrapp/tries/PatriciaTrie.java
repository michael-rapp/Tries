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
import de.mrapp.tries.datastructure.SortedListNode;
import de.mrapp.tries.util.SequenceUtil;
import de.mrapp.util.datastructure.Pair;
import de.mrapp.util.datastructure.Triple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Map;

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
     * Returns the index of a node's successor, which corresponds to a specific sequence.
     *
     * @param node     The node, whose successors should be checked, as an instance of the type
     *                 {@link Node}. The node may not be null
     * @param sequence The sequence, the successor, whose index should be returned, corresponds to,
     *                 as an {@link Integer} value
     * @return A triple, which contains the index of the successor, which corresponds to the given
     * sequence, as well as the common prefix and suffix of the given sequence, as an instance of
     * the class {@link Triple} or null, if no such successor is available for the given node
     */
    @SuppressWarnings("unchecked")
    @Nullable
    private Triple<Integer, SequenceType, SequenceType> indexOfInternal(
            @NotNull final Node<SequenceType, ValueType> node,
            @NotNull final SequenceType sequence) {
        SequenceType firstElement = SequenceUtil.subsequence(sequence, 0, 1);
        int index = SequenceUtil.binarySearch(node.getSuccessorCount(), node::getSuccessorKey,
                (o1, o2) -> ((Comparable<? super SequenceType>) SequenceUtil.subsequence(o1, 0, 1))
                        .compareTo(SequenceUtil.subsequence(o2, 0, 1)), firstElement);

        if (index != -1) {
            SequenceType successorKey = node.getSuccessorKey(index);
            SequenceType commonPrefix = SequenceUtil.getCommonPrefix(sequence, successorKey);

            if (commonPrefix != null) {
                return Triple
                        .create(index, commonPrefix, getSuffix(sequence, commonPrefix.length()));
            }
        }

        return null;
    }

    /**
     * Returns the suffix of a specific sequence, given a specific prefix length. The length of the
     * suffix is calculated based on the length of the given sequence and the suffix length.
     *
     * @param sequence     The sequence, whose suffix should be returned, as an instance of the
     *                     generic type {@link SequenceType}. The sequence may not be null
     * @param prefixLength The length of the prefix as an {@link Integer} value.
     * @return The suffix of the given sequence as an instance of the generic type {@link
     * SequenceType} or null, if the given prefix length equals the length of the given sequence
     */
    @Nullable
    private SequenceType getSuffix(@NotNull final SequenceType sequence, final int prefixLength) {
        return prefixLength < sequence.length() ? SequenceUtil.subsequence(sequence, prefixLength) :
                null;
    }

    /**
     * Removes a specific intermediate node, if possible.
     *
     * @param node The intermediate node, which should be removed, as an instance of the type {@link
     *             Node}. The node may to be null
     */
    private void removeIntermediateNode(@NotNull final Node<SequenceType, ValueType> node) {
        if (node.getSuccessorCount() == 1 && !node.isValueSet()) {
            Map.Entry<SequenceType, Node<SequenceType, ValueType>> entry = node.getPredecessor();

            if (entry != null) {
                SequenceType key = entry.getKey();
                Node<SequenceType, ValueType> predecessor = entry.getValue();
                SequenceType successorKey = node.getFirstSuccessorKey();
                Node<SequenceType, ValueType> successor = node.getFirstSuccessor();
                SequenceType joinedKey = SequenceUtil.concat(key, successorKey);
                predecessor.removeSuccessor(key);
                predecessor.addSuccessor(joinedKey, successor);
            }
        }
    }

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

    @Nullable
    @Override
    protected final Pair<Integer, SequenceType> indexOf(
            @NotNull final Node<SequenceType, ValueType> node,
            @NotNull final SequenceType sequence) {
        Triple<Integer, SequenceType, SequenceType> triple = indexOfInternal(node, sequence);
        return triple != null ? Pair.create(triple.first, triple.third) : null;
    }

    @NotNull
    @Override
    protected final Node<SequenceType, ValueType> createRootNode() {
        return new SortedListNode<>(comparator);
    }

    @Nullable
    @Override
    protected final Pair<Node<SequenceType, ValueType>, SequenceType> onGetSuccessor(
            @NotNull final Node<SequenceType, ValueType> node, @NotNull final SequenceType sequence,
            @NotNull final Operation operation) {
        Triple<Integer, SequenceType, SequenceType> triple = indexOfInternal(node, sequence);

        if (triple != null) {
            int index = triple.first;
            SequenceType prefix = triple.second;
            SequenceType suffix = triple.third;
            SequenceType successorKey = node.getSuccessorKey(index);
            Node<SequenceType, ValueType> successor = node.getSuccessor(index);
            int prefixLength = prefix.length();

            if (operation == Operation.PUT) {
                SequenceType intermediateSuffix = getSuffix(successorKey, prefixLength);

                if (intermediateSuffix != null && !intermediateSuffix.isEmpty()) {
                    node.removeSuccessor(index);
                    Node<SequenceType, ValueType> intermediateNode = node.addSuccessor(prefix);
                    intermediateNode.addSuccessor(intermediateSuffix, successor);
                    successor = intermediateNode;
                }

                return Pair.create(successor, suffix);
            } else if (operation == Operation.REMOVE) {
                return sequence.length() >= prefixLength && prefixLength == successorKey.length() ?
                        Pair.create(successor, suffix) : null;
            } else {
                int sequenceLength = sequence.length();
                return sequenceLength == prefixLength && successorKey.length() > sequenceLength ?
                        null :
                        Pair.create(successor, suffix);
            }
        }

        return null;
    }

    @NotNull
    @Override
    protected final Pair<Node<SequenceType, ValueType>, SequenceType> onAddSuccessor(
            @NotNull final Node<SequenceType, ValueType> node,
            @NotNull final SequenceType sequence) {
        Node<SequenceType, ValueType> successor = node.addSuccessor(sequence);
        return Pair.create(successor, null);
    }

    @Override
    protected final void onRemoveSuccessor(@NotNull final Node<SequenceType, ValueType> node,
                                           @NotNull final SequenceType sequence) {
        node.removeSuccessor(sequence);
        removeIntermediateNode(node);
    }

    @Override
    protected final void onDeletedValue(@NotNull final Node<SequenceType, ValueType> node) {
        removeIntermediateNode(node);
    }

    @NotNull
    @Override
    public final SortedTrie<SequenceType, ValueType> subTrie(@NotNull final SequenceType sequence) {
        // TODO
        return null;
    }

    @Override
    public final String toString() {
        return "PatriciaTrie " + entrySet().toString();
    }

}
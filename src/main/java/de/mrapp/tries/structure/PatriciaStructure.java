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
package de.mrapp.tries.structure;

import de.mrapp.tries.Node;
import de.mrapp.tries.NodeValue;
import de.mrapp.tries.Sequence;
import de.mrapp.tries.util.SequenceUtil;
import de.mrapp.util.datastructure.Pair;
import de.mrapp.util.datastructure.Triple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Defines the structure of a Patricia trie, where the edges between nodes do not always correspond
 * to a single element of a sequence. Instead, subsequent nodes that only have a single successor
 * are merged to a single node to reduce space complexity.
 *
 * @param <SequenceType> The type of the sequences, which are used as the trie's keys
 * @param <ValueType>    The type of the values, which are stored by the trie
 * @author Michael Rapp
 * @since 1.0.0
 */
public class PatriciaStructure<SequenceType extends Sequence, ValueType>
        implements SortedStructure<SequenceType, ValueType> {

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
    @Nullable
    private Triple<Integer, SequenceType, SequenceType> indexOfInternal(
            @NotNull final Node<SequenceType, ValueType> node,
            @NotNull final SequenceType sequence) {
        int index = node.indexOfFirstElement(sequence);

        if (index != -1) {
            SequenceType successorKey = node.getSuccessorKey(index);
            SequenceType commonPrefix = SequenceUtil.getCommonPrefix(sequence, successorKey);

            if (commonPrefix != null) {
                return Triple.Companion
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

    @Nullable
    @Override
    public final Pair<Node<SequenceType, ValueType>, SequenceType> onGetSuccessor(
            @NotNull final Node<SequenceType, ValueType> node, @NotNull final SequenceType sequence,
            @NotNull final Operation operation) {
        Triple<Integer, SequenceType, SequenceType> triple = indexOfInternal(node, sequence);

        if (triple != null) {
            int index = triple.getFirst();
            SequenceType prefix = triple.getSecond();
            SequenceType suffix = triple.getThird();
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

                return Pair.Companion.create(successor, suffix);
            } else if (operation == Operation.REMOVE) {
                return sequence.length() >= prefixLength && prefixLength == successorKey.length() ?
                        Pair.Companion.create(successor, suffix) : null;
            } else {
                int sequenceLength = sequence.length();

                if (sequenceLength == prefixLength && successorKey.length() > sequenceLength) {
                    return operation == Operation.SUB_TRIE ?
                            Pair.Companion.create(successor, successorKey) : null;
                }

                return Pair.Companion.create(successor, suffix);
            }
        }

        return null;
    }

    @NotNull
    @Override
    public final Pair<Node<SequenceType, ValueType>, SequenceType> onAddSuccessor(
            @NotNull final Node<SequenceType, ValueType> node,
            @NotNull final SequenceType sequence) {
        Node<SequenceType, ValueType> successor = node.addSuccessor(sequence);
        return Pair.Companion.create(successor, null);
    }

    @Override
    public final void onRemoveSuccessor(@NotNull final Node<SequenceType, ValueType> node,
                                        @NotNull final SequenceType sequence) {
        node.removeSuccessor(sequence);
        removeIntermediateNode(node);
    }

    @Override
    public final void onDeletedValue(@NotNull final Node<SequenceType, ValueType> node) {
        removeIntermediateNode(node);
    }

    @NotNull
    @Override
    public final Node<SequenceType, ValueType> getSubTrie(@Nullable final SequenceType sequence,
                                                          @NotNull final Node<SequenceType, ValueType> rootNode,
                                                          @NotNull final Node<SequenceType, ValueType> node,
                                                          final boolean includeNodeValue) {
        Node<SequenceType, ValueType> currentNode = rootNode;

        if (sequence != null && !sequence.isEmpty()) {
            Pair<Node<SequenceType, ValueType>, SequenceType> pair = onAddSuccessor(rootNode,
                    sequence);
            currentNode = pair.getFirst();
        }

        if (includeNodeValue && node.isValueSet()) {
            currentNode.setNodeValue(new NodeValue<>(node.getValue()));
        }

        for (SequenceType successorKey : node) {
            Node<SequenceType, ValueType> successor = node.getSuccessor(successorKey);

            if (successor != null) {
                currentNode.addSuccessor(successorKey, successor.clone());
            }
        }

        return rootNode;
    }

    @Nullable
    @Override
    public final Pair<Integer, SequenceType> indexOf(
            @NotNull final Node<SequenceType, ValueType> node,
            @NotNull final SequenceType sequence) {
        Triple<Integer, SequenceType, SequenceType> triple = indexOfInternal(node, sequence);
        return triple != null ? Pair.Companion.create(triple.getFirst(), triple.getThird()) : null;
    }

}
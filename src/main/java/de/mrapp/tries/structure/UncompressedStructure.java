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
package de.mrapp.tries.structure;

import de.mrapp.tries.Node;
import de.mrapp.tries.Sequence;
import de.mrapp.tries.util.SequenceUtil;
import de.mrapp.util.datastructure.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Defines the structure of an uncompressed trie, where the edges between nodes always correspond to
 * exactly one element of a sequence.
 *
 * @param <SequenceType> The type of the sequences, which are used as the trie's keys
 * @param <ValueType>    The type of the values, which are stored by the trie
 * @author Michael Rapp
 * @since 1.0.0
 */
public class UncompressedStructure<SequenceType extends Sequence, ValueType>
        implements Structure<SequenceType, ValueType> {

    @Nullable
    @Override
    public final Pair<Node<SequenceType, ValueType>, SequenceType> onGetSuccessor(
            @NotNull final Node<SequenceType, ValueType> node, @NotNull final SequenceType sequence,
            @NotNull final Operation operation) {
        SequenceType prefix = SequenceUtil.subsequence(sequence, 0, 1);
        Node<SequenceType, ValueType> successor = node.getSuccessor(prefix);

        if (successor != null) {
            SequenceType suffix = SequenceUtil.subsequence(sequence, 1);
            return Pair.create(successor, suffix);
        }

        return null;
    }

    @NotNull
    @Override
    public final Pair<Node<SequenceType, ValueType>, SequenceType> onAddSuccessor(
            @NotNull final Node<SequenceType, ValueType> node,
            @NotNull final SequenceType sequence) {
        SequenceType prefix = SequenceUtil.subsequence(sequence, 0, 1);
        Node<SequenceType, ValueType> successor = node.addSuccessor(prefix);
        SequenceType suffix = SequenceUtil.subsequence(sequence, 1);
        return Pair.create(successor, suffix);
    }

    @Override
    public final void onRemoveSuccessor(@NotNull final Node<SequenceType, ValueType> node,
            @NotNull final SequenceType sequence) {
        node.removeSuccessor(sequence);
    }

    @Override
    public final void onDeletedValue(@NotNull final Node<SequenceType, ValueType> node) {

    }

    @NotNull
    @Override
    public final Node<SequenceType, ValueType> getSubTrie(@NotNull final SequenceType sequence,
            @NotNull final Node<SequenceType, ValueType> rootNode,
            @NotNull final Node<SequenceType, ValueType> node) {
        Node<SequenceType, ValueType> currentNode = rootNode;
        SequenceType suffix = sequence;

        while (suffix != null && !suffix.isEmpty()) {
            Pair<Node<SequenceType, ValueType>, SequenceType> pair =
                    onAddSuccessor(currentNode, suffix);
            Node<SequenceType, ValueType> successor = pair.first;
            suffix = pair.second;
            currentNode = successor;
        }

        for (SequenceType successorKey : node) {
            Node<SequenceType, ValueType> successor = node.getSuccessor(successorKey);

            if (successor != null) {
                currentNode.addSuccessor(successorKey, successor.clone());
            }
        }

        return rootNode;
    }

}
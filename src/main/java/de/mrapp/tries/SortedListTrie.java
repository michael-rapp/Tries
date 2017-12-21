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

import de.mrapp.tries.datastructure.AbstractSortedTrie;
import de.mrapp.tries.datastructure.SortedListNode;
import de.mrapp.tries.util.SequenceUtil;
import de.mrapp.util.datastructure.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.NoSuchElementException;

public class SortedListTrie<SequenceType extends Sequence, ValueType> extends
        AbstractSortedTrie<SequenceType, ValueType> {

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = -1139777657107659140L;

    private SortedListTrie(@Nullable final Node<SequenceType, ValueType> node,
                           @Nullable final Comparator<? super SequenceType> comparator) {
        super(node, comparator);
    }

    public SortedListTrie() {
        this(null);
    }

    public SortedListTrie(@Nullable final Comparator<SequenceType> comparator) {
        super(comparator);
    }

    @NotNull
    @Override
    public final SortedListTrie<SequenceType, ValueType> subTree(
            @NotNull final SequenceType sequence) {
        Node<SequenceType, ValueType> node = getNode(sequence);

        if (node != null) {
            Node<SequenceType, ValueType> newRootNode = createRootNode();
            Node<SequenceType, ValueType> currentNode = newRootNode;
            SequenceType suffix = sequence;

            while (!suffix.isEmpty()) {
                Pair<Node<SequenceType, ValueType>, SequenceType> pair = addSuccessor(currentNode,
                        suffix);
                Node<SequenceType, ValueType> successor = pair.first;
                suffix = pair.second;
                currentNode = successor;
            }

            for (SequenceType key : node) {
                Node<SequenceType, ValueType> successor = node.getSuccessor(key);

                if (successor != null) {
                    currentNode.addSuccessor(key, successor.clone());
                }
            }

            return new SortedListTrie<>(newRootNode, comparator);
        }

        throw new NoSuchElementException();
    }

    @NotNull
    @Override
    protected final Node<SequenceType, ValueType> createRootNode() {
        return new SortedListNode<>(comparator);
    }

    @Nullable
    @Override
    protected final Pair<Node<SequenceType, ValueType>, SequenceType> getSuccessor(
            @NotNull final Node<SequenceType, ValueType> node,
            @NotNull final SequenceType sequence) {
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
    protected final Pair<Node<SequenceType, ValueType>, SequenceType> addSuccessor(
            @NotNull final Node<SequenceType, ValueType> node,
            @NotNull final SequenceType sequence) {
        SequenceType prefix = SequenceUtil.subsequence(sequence, 0, 1);
        Node<SequenceType, ValueType> successor = node.addSuccessor(prefix);
        SequenceType suffix = SequenceUtil.subsequence(sequence, 1);
        return Pair.create(successor, suffix);
    }

    @Override
    protected final void removeSuccessor(@NotNull final Node<SequenceType, ValueType> node,
                                         @NotNull final SequenceType sequence) {
        node.removeSuccessor(sequence);
    }

    @Nullable
    @Override
    protected final Pair<Integer, SequenceType> indexOf(
            @NotNull final Node<SequenceType, ValueType> node,
            @NotNull final SequenceType sequence) {
        SequenceType prefix = SequenceUtil.subsequence(sequence, 0, 1);
        int index = node.indexOf(prefix);

        if (index != -1) {
            SequenceType suffix = SequenceUtil.subsequence(sequence, 1);
            return Pair.create(index, suffix);
        }

        return null;
    }

    @Override
    public final String toString() {
        return "SortedListTrie " + entrySet().toString();
    }

}
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

import de.mrapp.tries.datastructure.AbstractTrie;
import de.mrapp.tries.datastructure.HashNode;
import de.mrapp.tries.util.SequenceUtil;
import de.mrapp.util.datastructure.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.NoSuchElementException;

/**
 * A unsorted trie, which stores the successors of nodes in hash maps. The edges between nodes
 * always correspond to exactly one element of a sequence. Therefore, even if a subsequence of
 * length n is not shared between multiple keys, the trie contains n nodes to store that
 * subsequence. As the used hash maps enable to lookup successors in constant time, this
 * implementation should be preferred over {@link SortedListTrie}, if the order of keys is
 * irrelevant.
 *
 * @param <SequenceType> The type of the sequences, which are used as the trie's keys
 * @param <ValueType>    The type of the values, which are stored by the trie
 * @author Michael Rapp
 * @since 1.0.0
 */
public class HashTrie<SequenceType extends Sequence, ValueType> extends
        AbstractTrie<SequenceType, ValueType> {

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = -2250393346732658811L;

    /**
     * Creates a new unsorted trie, which stores the successors of nodes in hash maps.
     *
     * @param rootNode The root node of the trie as an instance of the type {@link Node} or null, if
     *                 the trie should be empty
     */
    private HashTrie(@Nullable final Node<SequenceType, ValueType> rootNode) {
        super(rootNode);
    }

    /**
     * Creates a new empty, unsorted trie, which stores the successors of nodes in hash maps.
     */
    public HashTrie() {
        super();
    }

    /**
     * Creates a new unsorted trie, which contains all key-value pairs that are contained by a map.
     *
     * @param map The map, which contains the key-value pairs that should be added to the trie, as
     *            an instance of the type {@link Map}. The map may not be null
     */
    public HashTrie(@NotNull final Map<SequenceType, ValueType> map) {
        super(map);
    }

    @NotNull
    @Override
    protected final Node<SequenceType, ValueType> createRootNode() {
        return new HashNode<>();
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

    @NotNull
    @Override
    public HashTrie<SequenceType, ValueType> subTrie(@NotNull final SequenceType key) {
        Node<SequenceType, ValueType> node = getNode(key);

        if (node != null) {
            Node<SequenceType, ValueType> newRootNode = createRootNode();
            Node<SequenceType, ValueType> currentNode = newRootNode;
            SequenceType suffix = key;

            while (suffix != null && !suffix.isEmpty()) {
                Pair<Node<SequenceType, ValueType>, SequenceType> pair = addSuccessor(currentNode,
                        suffix);
                Node<SequenceType, ValueType> successor = pair.first;
                suffix = pair.second;
                currentNode = successor;
            }

            for (SequenceType sequence : node) {
                Node<SequenceType, ValueType> successor = node.getSuccessor(sequence);

                if (successor != null) {
                    currentNode.addSuccessor(sequence, successor.clone());
                }
            }

            return new HashTrie<>(newRootNode);
        }

        throw new NoSuchElementException();
    }

    @Override
    public final String toString() {
        return "HashTrie " + entrySet().toString();
    }

}
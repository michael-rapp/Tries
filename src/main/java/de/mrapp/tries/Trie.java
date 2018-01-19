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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Map;

/**
 * Defines the interface of a trie (also called prefix tree, digital tree or radix tree). A trie is
 * a tree data structure that can be used to store an associative array where the keys are sequences
 * (e.g. sequences of characters or lists of digits).
 * <p>
 * Tries allow to efficiently search for sequences (and their values) that share a common prefix. As
 * nodes with a common prefix share the same predecessors, tries also provide some kind of
 * compression.
 * <p>
 * In a trie values are only associated with the leaf nodes and with some inner nodes. As all
 * successors of a node share a common prefix of the sequence, which is associated with that node,
 * the position of a value in the trie specifies the key it is associated with. The root node
 * corresponds to an empty sequence.
 *
 * @param <SequenceType> The type of the sequences, which are used as the trie's keys
 * @param <ValueType>    The type of the values, which are stored by the trie
 * @author Michael Rapp
 * @since 1.0.0
 */
public interface Trie<SequenceType extends Sequence, ValueType>
        extends Map<SequenceType, ValueType>, Serializable {

    /**
     * Returns the root node of the trie. The returned object is not modifiable, i.e. an {@link
     * UnsupportedOperationException} will be thrown when attempting to change the state of the
     * returned node or one of its successors.
     *
     * @return The root node of the trie as an instance of the type {@link Node} or null, if the
     * trie is empty
     */
    @Nullable
    Node<SequenceType, ValueType> getRootNode();

    /**
     * Returns the subtree of the node, which corresponds to a specific sequence (must not
     * necessarily be a key, which is contained by the trie, but can also be a suffix). If the given
     * sequence is not contained by the trie, a {@link java.util.NoSuchElementException} will be
     * thrown.
     *
     * @param sequence The sequence as an instance of the generic type {@link SequenceType}. The
     *                 sequence may not be null
     * @return The subtree of the node, which corresponds to the given sequence, as an instance of
     * the type {@link Trie}. The subtree may not be null
     */
    @NotNull
    Trie<SequenceType, ValueType> subTrie(@NotNull SequenceType sequence);

}
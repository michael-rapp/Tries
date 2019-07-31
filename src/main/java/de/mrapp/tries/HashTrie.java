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

import de.mrapp.tries.datastructure.AbstractTrie;
import de.mrapp.tries.datastructure.node.HashNode;
import de.mrapp.tries.structure.Structure;
import de.mrapp.tries.structure.UncompressedStructure;
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
public class HashTrie<SequenceType extends Sequence, ValueType>
        extends AbstractTrie<Structure<SequenceType, ValueType>, SequenceType, ValueType> {

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

    @NotNull
    @Override
    protected final Structure<SequenceType, ValueType> createStructure() {
        return new UncompressedStructure<>();
    }

    @NotNull
    @Override
    public HashTrie<SequenceType, ValueType> subTrie(@Nullable final SequenceType sequence) {
        Node<SequenceType, ValueType> node = getNode(sequence);

        if (node != null) {
            if (node.hasSuccessors()) {
                Node<SequenceType, ValueType> rootNode =
                        structure.getSubTrie(sequence, createRootNode(), node, false);
                return new HashTrie<>(rootNode);
            } else {
                return new HashTrie<>((Node<SequenceType, ValueType>) null);
            }
        }

        throw new NoSuchElementException();
    }

    @Override
    public final String toString() {
        return "HashTrie " + entrySet().toString();
    }

}
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
import de.mrapp.tries.structure.Structure.Operation;
import de.mrapp.tries.util.SequenceUtil;
import de.mrapp.util.datastructure.Pair;
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
     * Traverses the trie in order to return the node, which corresponds to a specific sequence. If
     * the sequence does not correspond to a node, the next higher node with the same prefix is
     * returned.
     *
     * @param sequence The sequence, the node, which should be returned, corresponds to, as an
     *                 instance of the generic type {@link SequenceType} or null
     * @return A pair, which contains the node, which corresponds to the given sequence,
     * respectively the next higher node, as well as the key, the returned node corresponds to, as
     * an instance of the class {@link Pair} or null, if the given sequence does not match any node
     */
    @Nullable
    private Pair<Node<SequenceType, ValueType>, SequenceType> getSubTrieRootNode(
            @Nullable final SequenceType sequence) {
        if (rootNode != null) {
            Node<SequenceType, ValueType> currentNode = rootNode;
            SequenceType matchedPrefix = null;
            SequenceType suffix = sequence;

            while (suffix != null && !suffix.isEmpty()) {
                Pair<Node<SequenceType, ValueType>, SequenceType> pair =
                        structure.onGetSuccessor(currentNode, suffix, Operation.GET);

                if (pair == null) {
                    suffix = null;

                    if (matchedPrefix == null || matchedPrefix.isEmpty()) {
                        return null;
                    }
                } else {
                    currentNode = pair.first;
                    suffix = pair.second;
                    matchedPrefix = suffix == null || suffix.isEmpty() ? sequence : SequenceUtil
                            .subsequence(sequence, 0, sequence.length() - suffix.length());
                }
            }

            if (sequence != null && !sequence.isEmpty() && matchedPrefix != null &&
                    matchedPrefix.length() < sequence.length()) {
                SequenceType unmatchedSuffix =
                        SequenceUtil.subsequence(sequence, matchedPrefix.length());
                SequenceType firstElement = SequenceUtil.subsequence(unmatchedSuffix, 0, 1);
                int index = SequenceUtil
                        .binarySearch(currentNode.getSuccessorCount(), currentNode::getSuccessorKey,
                                (o1, o2) -> ((Comparable<? super SequenceType>) SequenceUtil
                                        .subsequence(o1, 0, 1))
                                        .compareTo(SequenceUtil.subsequence(o2, 0, 1)),
                                firstElement);

                if (index != -1) {
                    SequenceType successorKey = currentNode.getSuccessorKey(index);
                    currentNode = currentNode.getSuccessor(index);
                    matchedPrefix = SequenceUtil.concat(matchedPrefix, successorKey);
                }
            }

            return Pair.create(currentNode, matchedPrefix);
        }

        return null;
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
    public final SortedTrie<SequenceType, ValueType> subTrie(
            @Nullable final SequenceType sequence) {
        Pair<Node<SequenceType, ValueType>, SequenceType> pair = getSubTrieRootNode(sequence);

        if (pair != null) {
            Node<SequenceType, ValueType> node = pair.first;

            if (node.hasSuccessors()) {
                Node<SequenceType, ValueType> rootNode =
                        structure.getSubTrie(pair.second, createRootNode(), node);
                return new PatriciaTrie<>(rootNode, comparator);
            } else {
                return new PatriciaTrie<>(null, comparator);
            }
        }

        throw new NoSuchElementException();
    }

    @Override
    public final String toString() {
        return "PatriciaTrie " + entrySet().toString();
    }

}
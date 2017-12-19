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
package de.mrapp.tries.datastructure;

import de.mrapp.tries.Node;
import de.mrapp.tries.Sequence;
import de.mrapp.tries.SortedTrie;
import de.mrapp.tries.util.SequenceUtil;
import de.mrapp.util.datastructure.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class AbstractSortedTrie<SequenceType extends Sequence, ValueType> extends
        AbstractTrie<SequenceType, ValueType> implements SortedTrie<SequenceType, ValueType> {

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = 4126112554524328807L;

    /**
     * The comparator, which is used to compare sequences to each other, or null, if the natural
     * order of the sequences is used.
     */
    protected final Comparator<SequenceType> comparator;

    /**
     * Returns the entry, which corresponds to the first or last entry of the trie.
     *
     * @param first True, if the first entry should be returned, false, if the last entry should be
     *              returned
     * @return The entry, which corresponds to the first or last entry of the trie, depending on the
     * given {@code first} argument, or null, if the trie is empty
     */
    @Nullable
    private Entry<SequenceType, ValueType> firstOrLastEntry(final boolean first) {
        return firstOrLastEntry(rootNode, null, first);
    }

    /**
     * Returns the entry, which corresponds to the first or last entry of a subtrie.
     *
     * @param node  The root node of the subtrie as an instance of the type {@link Node} or null, if
     *              the subtrie is empty
     * @param key   The key of the subtrie's root node as an instance of the generic type {@link
     *              SequenceType} or null, if the subtrie is empty
     * @param first True, if the first entry should be returned, false, if the last entry should be
     *              returned
     * @return The entry, which corresponds to the first or last entry of the given subtrie,
     * depending on the given {@code first} argument, or null, if the trie is empty
     */
    @Nullable
    private Entry<SequenceType, ValueType> firstOrLastEntry(
            @Nullable final Node<SequenceType, ValueType> node,
            @Nullable final SequenceType key, final boolean first) {
        Node<SequenceType, ValueType> currentNode = node;
        SequenceType sequence = key;

        while (currentNode != null &&
                (!first || !currentNode.isValueSet()) && currentNode.hasSuccessors()) {
            SequenceType successorKey =
                    first ? currentNode.getFirstSuccessorKey() : currentNode.getLastSuccessorKey();
            currentNode = first ? currentNode.getFirstSuccessor() : currentNode.getLastSuccessor();
            sequence = SequenceUtil.concat(sequence, successorKey);
        }

        if (currentNode != null && currentNode.isValueSet()) {
            return new AbstractMap.SimpleImmutableEntry<>(sequence, currentNode.getValue());
        }

        return null;
    }

    @Nullable
    private Entry<SequenceType, ValueType> pollFirstOrLastEntry(final boolean first) {
        Entry<SequenceType, ValueType> result = null;

        if (rootNode != null) {
            if (rootNode.isValueSet()) {
                result = new AbstractMap.SimpleImmutableEntry<>(null, rootNode.getValue());
                rootNode.setNodeValue(null);

                if (!rootNode.hasSuccessors()) {
                    clear();
                }
            } else {
                Node<SequenceType, ValueType> currentNode = rootNode;
                Node<SequenceType, ValueType> lastRetainedNode = rootNode;
                SequenceType suffixToRemove = null;
                SequenceType sequence = null;

                while (currentNode != null && !currentNode.isValueSet()) {
                    SequenceType key = first ? currentNode.getFirstSuccessorKey() :
                            currentNode.getLastSuccessorKey();

                    if (currentNode.getSuccessorCount() > 1 || currentNode.getValue() != null) {
                        lastRetainedNode = currentNode;
                        suffixToRemove = key;
                    }

                    currentNode = first ? currentNode.getFirstSuccessor() :
                            currentNode.getLastSuccessor();
                    sequence = SequenceUtil.concat(sequence, key);
                }

                if (currentNode != null && currentNode.isValueSet()) {
                    if (currentNode.hasSuccessors()) {
                        lastRetainedNode = null;
                        suffixToRemove = null;
                    }

                    result = new AbstractMap.SimpleImmutableEntry<>(sequence,
                            currentNode.getValue());
                    currentNode.setNodeValue(null);

                    if (lastRetainedNode == rootNode) {
                        clear();
                    } else {
                        if (suffixToRemove != null) {
                            removeSuccessor(lastRetainedNode, suffixToRemove);
                        }

                        modificationCount++;
                    }
                }
            }
        }

        return result;
    }

    @Nullable
    protected abstract Pair<Integer, SequenceType> indexOf(
            @NotNull final Node<SequenceType, ValueType> node,
            @NotNull final SequenceType sequence);

    protected AbstractSortedTrie(@Nullable final Node<SequenceType, ValueType> node,
                                 @Nullable final Comparator<SequenceType> comparator) {
        super(node);
        this.comparator = comparator;
    }

    public AbstractSortedTrie(@Nullable final Comparator<SequenceType> comparator) {
        this(null, comparator);
    }

    @Override
    public final Comparator<? super SequenceType> comparator() {
        return comparator;
    }

    @Override
    public final SequenceType lowerKey(final SequenceType key) {
        Entry<SequenceType, ValueType> entry = lowerEntry(key);
        return entry != null ? entry.getKey() : null;
    }

    @Override
    public final SequenceType higherKey(final SequenceType key) {
        Entry<SequenceType, ValueType> entry = higherEntry(key);
        return entry != null ? entry.getKey() : null;
    }

    @Override
    public final SequenceType floorKey(final SequenceType key) {
        Entry<SequenceType, ValueType> entry = floorEntry(key);
        return entry != null ? entry.getKey() : null;
    }

    @Override
    public final SequenceType ceilingKey(final SequenceType key) {
        Entry<SequenceType, ValueType> entry = ceilingEntry(key);
        return entry != null ? entry.getKey() : null;
    }

    @Override
    public final SequenceType firstKey() {
        Entry<SequenceType, ValueType> entry = firstEntry();
        return entry != null ? entry.getKey() : null;
    }

    @Override
    public final SequenceType lastKey() {
        Entry<SequenceType, ValueType> entry = lastEntry();
        return entry != null ? entry.getKey() : null;
    }

    @Override
    public final Entry<SequenceType, ValueType> lowerEntry(final SequenceType key) {
        if (rootNode != null) {
            Deque<Pair<Node<SequenceType, ValueType>, SequenceType>> stack = new LinkedList<>();
            Node<SequenceType, ValueType> currentNode = rootNode;
            SequenceType suffix = key;
            stack.push(Pair.create(currentNode, suffix));

            while (suffix != null && !suffix.isEmpty()) {
                Pair<Node<SequenceType, ValueType>, SequenceType> pair = getSuccessor(currentNode,
                        suffix);

                if (pair == null) {
                    return null;
                } else {
                    stack.push(Pair.create(currentNode, suffix));
                    currentNode = pair.first;
                    suffix = pair.second;
                }
            }

            while (currentNode != null && !stack.isEmpty()) {
                Pair<Node<SequenceType, ValueType>, SequenceType> pair = stack.pop();
                Node<SequenceType, ValueType> node = pair.first;

                if (node.isValueSet()) {
                    SequenceType lowerKey = SequenceUtil
                            .subsequence(key, 0, key.length() - pair.second.length());
                    return new AbstractMap.SimpleImmutableEntry<>(
                            lowerKey.isEmpty() ? null : lowerKey, node.getValue());
                } else if (node.getSuccessorCount() > 1) {
                    Pair<Integer, SequenceType> indexPair = indexOf(node, pair.second);

                    if (indexPair != null && indexPair.first > 0) {
                        int index = indexPair.first - 1;
                        Node<SequenceType, ValueType> successor = node.getSuccessor(index);
                        SequenceType successorKey = node.getSuccessorKey(index);
                        SequenceType prefix = SequenceUtil
                                .subsequence(key, 0, key.length() - pair.second.length());
                        prefix = SequenceUtil.concat(prefix, successorKey);
                        return firstOrLastEntry(successor, prefix, false);
                    }
                }
            }
        }

        return null;
    }


    @Override
    public final Entry<SequenceType, ValueType> higherEntry(final SequenceType key) {
        // TODO
        return null;
    }

    @Override
    public final Entry<SequenceType, ValueType> floorEntry(final SequenceType key) {
        // TODO
        return null;
    }

    @Override
    public final Entry<SequenceType, ValueType> ceilingEntry(final SequenceType key) {
        // TODO
        return null;
    }

    @Override
    public final Entry<SequenceType, ValueType> firstEntry() {
        return firstOrLastEntry(true);
    }

    @Override
    public final Entry<SequenceType, ValueType> lastEntry() {
        return firstOrLastEntry(false);
    }

    @Override
    public final Entry<SequenceType, ValueType> pollFirstEntry() {
        return pollFirstOrLastEntry(true);
    }

    @Override
    public final Entry<SequenceType, ValueType> pollLastEntry() {
        return pollFirstOrLastEntry(false);
    }

    @NotNull
    @Override
    public final SortedMap<SequenceType, ValueType> subMap(final SequenceType fromKey,
                                                           final SequenceType toKey) {
        // TODO
        return null;
    }

    @Override
    public final NavigableMap<SequenceType, ValueType> subMap(final SequenceType fromKey,
                                                              final boolean fromInclusive,
                                                              final SequenceType toKey,
                                                              final boolean toInclusive) {
        // TODO
        return null;
    }

    @NotNull
    @Override
    public final SortedMap<SequenceType, ValueType> headMap(final SequenceType toKey) {
        // TODO
        return null;
    }

    @Override
    public final NavigableMap<SequenceType, ValueType> headMap(final SequenceType toKey,
                                                               final boolean inclusive) {
        // TODO
        return null;
    }

    @NotNull
    @Override
    public final SortedMap<SequenceType, ValueType> tailMap(final SequenceType fromKey) {
        // TODO
        return null;
    }

    @Override
    public final NavigableMap<SequenceType, ValueType> tailMap(final SequenceType fromKey,
                                                               final boolean inclusive) {
        // TODO
        return null;
    }

    @Override
    public final NavigableMap<SequenceType, ValueType> descendingMap() {
        // TODO
        return null;
    }

    @Override
    public final NavigableSet<SequenceType> navigableKeySet() {
        // TODO
        return null;
    }

    @Override
    public final NavigableSet<SequenceType> descendingKeySet() {
        // TODO
        return null;
    }

}
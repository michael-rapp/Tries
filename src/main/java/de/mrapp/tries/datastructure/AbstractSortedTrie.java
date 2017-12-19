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

    protected AbstractSortedTrie(@Nullable final Node<SequenceType, ValueType> node,
                                 @Nullable final Comparator<SequenceType> comparator) {
        super(node);
        this.comparator = comparator;
    }

    public AbstractSortedTrie(@Nullable final Comparator<SequenceType> comparator) {
        this(null, comparator);
    }

    @Override
    public final Entry<SequenceType, ValueType> lowerEntry(final SequenceType key) {
        Node<SequenceType, ValueType> node = getNode(key);

        if (node != null) {
            // TODO
        }

        return null;
    }

    @Override
    public final SequenceType lowerKey(final SequenceType key) {
        // TODO
        return null;
    }

    @Override
    public final Entry<SequenceType, ValueType> floorEntry(final SequenceType key) {
        // TODO
        return null;
    }

    @Override
    public final SequenceType floorKey(final SequenceType key) {
        return null;
    }

    @Override
    public final Entry<SequenceType, ValueType> ceilingEntry(final SequenceType key) {
        // TODO
        return null;
    }

    @Override
    public final SequenceType ceilingKey(final SequenceType key) {
        // TODO
        return null;
    }

    @Override
    public final Entry<SequenceType, ValueType> higherEntry(final SequenceType key) {
        // TODO
        return null;
    }

    @Override
    public final SequenceType higherKey(final SequenceType key) {
        // TODO
        return null;
    }

    @Override
    public final Entry<SequenceType, ValueType> firstEntry() {
        Node<SequenceType, ValueType> currentNode = rootNode;
        SequenceType sequence = null;

        while (currentNode != null && !currentNode.isValueSet()) {
            SequenceType key = currentNode.getFirstSuccessorKey();
            currentNode = currentNode.getFirstSuccessor();
            sequence = SequenceUtil.concat(sequence, key);
        }

        if (currentNode != null && currentNode.isValueSet()) {
            return new AbstractMap.SimpleImmutableEntry<>(sequence, currentNode.getValue());
        }

        return null;
    }

    @Override
    public final Entry<SequenceType, ValueType> lastEntry() {
        Node<SequenceType, ValueType> currentNode = rootNode;
        SequenceType sequence = null;

        while (currentNode != null && !currentNode.isValueSet()) {
            SequenceType key = currentNode.getLastSuccessorKey();
            currentNode = currentNode.getLastSuccessor();
            sequence = SequenceUtil.concat(sequence, key);
        }

        if (currentNode != null && currentNode.isValueSet()) {
            return new AbstractMap.SimpleImmutableEntry<>(sequence, currentNode.getValue());
        }

        return null;
    }

    @Override
    public final Entry<SequenceType, ValueType> pollFirstEntry() {
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
                    SequenceType key = currentNode.getFirstSuccessorKey();

                    if (currentNode.getSuccessorCount() > 1 || currentNode.getValue() != null) {
                        lastRetainedNode = currentNode;
                        suffixToRemove = key;
                    }

                    currentNode = currentNode.getFirstSuccessor();
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

    @Override
    public final Entry<SequenceType, ValueType> pollLastEntry() {
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

    @Override
    public final NavigableMap<SequenceType, ValueType> subMap(final SequenceType fromKey,
                                                              final boolean fromInclusive,
                                                              final SequenceType toKey,
                                                              final boolean toInclusive) {
        // TODO
        return null;
    }

    @Override
    public final NavigableMap<SequenceType, ValueType> headMap(final SequenceType toKey,
                                                               final boolean inclusive) {
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
    public final Comparator<? super SequenceType> comparator() {
        return comparator;
    }

    @NotNull
    @Override
    public final SortedMap<SequenceType, ValueType> subMap(final SequenceType fromKey,
                                                           final SequenceType toKey) {
        // TODO
        return null;
    }

    @NotNull
    @Override
    public final SortedMap<SequenceType, ValueType> headMap(final SequenceType toKey) {
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
    public final SequenceType firstKey() {
        Entry<SequenceType, ValueType> entry = firstEntry();
        return entry != null ? entry.getKey() : null;
    }

    @Override
    public final SequenceType lastKey() {
        Entry<SequenceType, ValueType> entry = lastEntry();
        return entry != null ? entry.getKey() : null;
    }

}
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

import de.mrapp.tries.Sequence;
import de.mrapp.tries.SortedTrie;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * An immutable {@link SortedTrie}.
 *
 * @param <SequenceType> The type of the sequences, which are used as the trie's keys
 * @param <ValueType>    The type of the values, which are stored by trie
 * @author Michael Rapp
 * @since 1.0.0
 */
public class UnmodifiableSortedTrie<SequenceType extends Sequence, ValueType> extends
        AbstractUnmodifiableTrie<SequenceType, ValueType, SortedTrie<SequenceType, ValueType>>
        implements SortedTrie<SequenceType, ValueType> {

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = -8271396585626759384L;

    /**
     * Creates a new unmodifiable {@link SortedTrie}.
     *
     * @param trie The trie, which should be encapsulated, as an instance of the generic type {@link
     *             SortedTrie}. The trie may not be null
     */
    public UnmodifiableSortedTrie(@NotNull final SortedTrie<SequenceType, ValueType> trie) {
        super(trie);
    }

    @Override
    public final Entry<SequenceType, ValueType> lowerEntry(final SequenceType key) {
        return trie.lowerEntry(key);
    }

    @Override
    public final SequenceType lowerKey(final SequenceType key) {
        return trie.lowerKey(key);
    }

    @Override
    public final Entry<SequenceType, ValueType> floorEntry(final SequenceType key) {
        return trie.floorEntry(key);
    }

    @Override
    public final SequenceType floorKey(final SequenceType key) {
        return trie.floorKey(key);
    }

    @Override
    public final Entry<SequenceType, ValueType> ceilingEntry(final SequenceType key) {
        return trie.ceilingEntry(key);
    }

    @Override
    public final SequenceType ceilingKey(final SequenceType key) {
        return trie.ceilingKey(key);
    }

    @Override
    public final Entry<SequenceType, ValueType> higherEntry(final SequenceType key) {
        return trie.higherEntry(key);
    }

    @Override
    public final SequenceType higherKey(final SequenceType key) {
        return trie.higherKey(key);
    }

    @Override
    public final Entry<SequenceType, ValueType> firstEntry() {
        return trie.firstEntry();
    }

    @Override
    public final Entry<SequenceType, ValueType> lastEntry() {
        return trie.lastEntry();
    }

    @Override
    public final Entry<SequenceType, ValueType> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final Entry<SequenceType, ValueType> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final NavigableMap<SequenceType, ValueType> descendingMap() {
        return Collections.unmodifiableNavigableMap(trie.descendingMap());
    }

    @Override
    public final NavigableSet<SequenceType> navigableKeySet() {
        return Collections.unmodifiableNavigableSet(trie.navigableKeySet());
    }

    @Override
    public final NavigableSet<SequenceType> descendingKeySet() {
        return Collections.unmodifiableNavigableSet(trie.descendingKeySet());
    }

    @Override
    public final NavigableMap<SequenceType, ValueType> subMap(final SequenceType fromKey,
                                                              final boolean fromInclusive,
                                                              final SequenceType toKey,
                                                              final boolean toInclusive) {
        return Collections
                .unmodifiableNavigableMap(trie.subMap(fromKey, fromInclusive, toKey, toInclusive));
    }

    @Override
    public final NavigableMap<SequenceType, ValueType> headMap(final SequenceType toKey,
                                                               final boolean inclusive) {
        return Collections.unmodifiableNavigableMap(trie.headMap(toKey, inclusive));
    }

    @Override
    public final NavigableMap<SequenceType, ValueType> tailMap(final SequenceType fromKey,
                                                               final boolean inclusive) {
        return Collections.unmodifiableNavigableMap(trie.tailMap(fromKey, inclusive));
    }

    @Override
    public final Comparator<? super SequenceType> comparator() {
        return trie.comparator();
    }

    @NotNull
    @Override
    public final SortedMap<SequenceType, ValueType> subMap(final SequenceType fromKey,
                                                           final SequenceType toKey) {
        return Collections.unmodifiableSortedMap(trie.subMap(fromKey, toKey));
    }

    @NotNull
    @Override
    public final SortedMap<SequenceType, ValueType> headMap(final SequenceType toKey) {
        return Collections.unmodifiableSortedMap(trie.headMap(toKey));
    }

    @Override
    public final SortedMap<SequenceType, ValueType> tailMap(final SequenceType fromKey) {
        return Collections.unmodifiableSortedMap(trie.tailMap(fromKey));
    }

    @Override
    public final SequenceType firstKey() {
        return trie.firstKey();
    }

    @Override
    public final SequenceType lastKey() {
        return trie.lastKey();
    }

    @NotNull
    @Override
    public final SortedTrie<SequenceType, ValueType> subTree(@NotNull final SequenceType sequence) {
        return new UnmodifiableSortedTrie<>(trie.subTree(sequence));
    }

}
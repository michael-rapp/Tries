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
package de.mrapp.tries.datastructure;

import de.mrapp.tries.SortedStringTrie;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * An immutable {@link SortedStringTrie}.
 *
 * @param <ValueType> The type of the values, which are stored by the trie
 * @author Michael Rapp
 * @since 1.0.0
 */
public class UnmodifiableSortedStringTrie<ValueType> extends
        AbstractUnmodifiableStringTrie<ValueType, SortedStringTrie<ValueType>> implements
        SortedStringTrie<ValueType> {

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = 5046452071336030045L;

    /**
     * Creates a new unmodifiable sorted string trie.
     *
     * @param trie The trie, which should be encapsulated, as an instance of the generic type {@link
     *             SortedStringTrie}. The trie may not be null
     */
    public UnmodifiableSortedStringTrie(@NotNull final SortedStringTrie<ValueType> trie) {
        super(trie);
    }

    @Override
    public final Entry<String, ValueType> lowerEntry(final String key) {
        return trie.lowerEntry(key);
    }

    @Override
    public final String lowerKey(final String key) {
        return trie.lowerKey(key);
    }

    @Override
    public final Entry<String, ValueType> floorEntry(final String key) {
        return trie.floorEntry(key);
    }

    @Override
    public final String floorKey(final String key) {
        return trie.floorKey(key);
    }

    @Override
    public final Entry<String, ValueType> ceilingEntry(final String key) {
        return trie.ceilingEntry(key);
    }

    @Override
    public final String ceilingKey(final String key) {
        return trie.ceilingKey(key);
    }

    @Override
    public final Entry<String, ValueType> higherEntry(final String key) {
        return trie.higherEntry(key);
    }

    @Override
    public final String higherKey(final String key) {
        return trie.higherKey(key);
    }

    @Override
    public final Entry<String, ValueType> firstEntry() {
        return trie.firstEntry();
    }

    @Override
    public final Entry<String, ValueType> lastEntry() {
        return trie.lastEntry();
    }

    @Override
    public final Entry<String, ValueType> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final Entry<String, ValueType> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final NavigableMap<String, ValueType> descendingMap() {
        return Collections.unmodifiableNavigableMap(trie.descendingMap());
    }

    @NotNull
    @Override
    public final NavigableSet<String> navigableKeySet() {
        return Collections.unmodifiableNavigableSet(trie.navigableKeySet());
    }

    @Override
    public final NavigableSet<String> descendingKeySet() {
        return Collections.unmodifiableNavigableSet(trie.descendingKeySet());
    }

    @Override
    public final NavigableMap<String, ValueType> subMap(final String fromKey,
                                                        final boolean fromInclusive,
                                                        final String toKey,
                                                        final boolean toInclusive) {
        return Collections
                .unmodifiableNavigableMap(trie.subMap(fromKey, fromInclusive, toKey, toInclusive));
    }

    @Override
    public final NavigableMap<String, ValueType> headMap(final String toKey,
                                                         final boolean inclusive) {
        return Collections.unmodifiableNavigableMap(trie.headMap(toKey, inclusive));
    }

    @Override
    public final NavigableMap<String, ValueType> tailMap(final String fromKey,
                                                         final boolean inclusive) {
        return Collections.unmodifiableNavigableMap(trie.tailMap(fromKey, inclusive));
    }

    @Override
    public final Comparator<? super String> comparator() {
        return trie.comparator();
    }

    @Override
    public final SortedMap<String, ValueType> subMap(final String fromKey, final String toKey) {
        return Collections.unmodifiableSortedMap(trie.subMap(fromKey, toKey));
    }

    @NotNull
    @Override
    public final SortedMap<String, ValueType> headMap(final String toKey) {
        return Collections.unmodifiableSortedMap(trie.headMap(toKey));
    }

    @NotNull
    @Override
    public final SortedMap<String, ValueType> tailMap(final String fromKey) {
        return Collections.unmodifiableSortedMap(trie.tailMap(fromKey));
    }

    @Override
    public final String firstKey() {
        return trie.firstKey();
    }

    @Override
    public final String lastKey() {
        return trie.lastKey();
    }

    @NotNull
    @Override
    public final SortedStringTrie<ValueType> subTrie(@NotNull final String sequence) {
        return new UnmodifiableSortedStringTrie<>(trie.subTrie(sequence));
    }

}
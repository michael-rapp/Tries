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

import de.mrapp.tries.SortedStringTrie;
import de.mrapp.tries.SortedTrie;
import de.mrapp.tries.sequence.StringSequence;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * A wrapper, which implements the interface {@link SortedStringTrie} by delegating all method calls
 * to an encapsulated {@link SortedTrie}.
 *
 * @param <ValueType> The type of the values, which are stored by the trie
 * @author Michael Rapp
 * @since 1.0.0
 */
public class SortedStringTrieWrapper<ValueType> extends
        AbstractStringTrieWrapper<SortedTrie<StringSequence, ValueType>, ValueType> implements
        SortedStringTrie<ValueType> {

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = -1937689954255665817L;

    /**
     * Creates a new wrapper, which implements the interface {@link SortedStringTrie}.
     *
     * @param trie The trie, which should be encapsulated, as an instance of the type {@link
     *             SortedTrie}. The trie may not be null
     */
    public SortedStringTrieWrapper(
            @NotNull final SortedTrie<StringSequence, ValueType> trie) {
        super(trie);
    }

    @Override
    public final Entry<String, ValueType> lowerEntry(final String key) {
        Entry<StringSequence, ValueType> entry = trie.lowerEntry(new StringSequence(key));

        if (entry != null) {
            String lowerKey = entry.getKey() != null ? entry.getKey().toString() : null;
            return new AbstractMap.SimpleImmutableEntry<>(lowerKey, entry.getValue());
        }

        return null;
    }

    @Override
    public final String lowerKey(final String key) {
        Entry<String, ValueType> entry = lowerEntry(key);
        return entry != null ? entry.getKey() : null;
    }

    @Override
    public final Entry<String, ValueType> floorEntry(final String key) {
        Entry<StringSequence, ValueType> entry = trie.floorEntry(new StringSequence(key));

        if (entry != null) {
            String floorKey = entry.getKey() != null ? entry.getKey().toString() : null;
            return new AbstractMap.SimpleImmutableEntry<>(floorKey, entry.getValue());
        }

        return null;
    }

    @Override
    public final String floorKey(final String key) {
        Entry<String, ValueType> entry = floorEntry(key);
        return entry != null ? entry.getKey() : null;
    }

    @Override
    public final Entry<String, ValueType> ceilingEntry(final String key) {
        Entry<StringSequence, ValueType> entry = trie.ceilingEntry(new StringSequence(key));

        if (entry != null) {
            String ceilingKey = entry.getKey() != null ? entry.getKey().toString() : null;
            return new AbstractMap.SimpleImmutableEntry<>(ceilingKey, entry.getValue());
        }

        return null;
    }

    @Override
    public final String ceilingKey(final String key) {
        Entry<String, ValueType> entry = ceilingEntry(key);
        return entry != null ? entry.getKey() : null;
    }

    @Override
    public final Entry<String, ValueType> higherEntry(final String key) {
        Entry<StringSequence, ValueType> entry = trie.higherEntry(new StringSequence(key));

        if (entry != null) {
            String higherKey = entry.getKey() != null ? entry.getKey().toString() : null;
            return new AbstractMap.SimpleImmutableEntry<>(higherKey, entry.getValue());
        }

        return null;
    }

    @Override
    public final String higherKey(final String key) {
        Entry<String, ValueType> entry = higherEntry(key);
        return entry != null ? entry.getKey() : null;
    }

    @Override
    public final Entry<String, ValueType> firstEntry() {
        Entry<StringSequence, ValueType> entry = trie.firstEntry();

        if (entry != null) {
            String key = entry.getKey() != null ? entry.getKey().toString() : null;
            return new AbstractMap.SimpleImmutableEntry<>(key, entry.getValue());
        }

        return null;
    }

    @Override
    public final Entry<String, ValueType> lastEntry() {
        Entry<StringSequence, ValueType> entry = trie.lastEntry();

        if (entry != null) {
            String key = entry.getKey() != null ? entry.getKey().toString() : null;
            return new AbstractMap.SimpleImmutableEntry<>(key, entry.getValue());
        }

        return null;
    }

    @Override
    public final Entry<String, ValueType> pollFirstEntry() {
        Entry<StringSequence, ValueType> entry = trie.pollFirstEntry();

        if (entry != null) {
            String key = entry.getKey() != null ? entry.getKey().toString() : null;
            return new AbstractMap.SimpleImmutableEntry<>(key, entry.getValue());
        }

        return null;
    }

    @Override
    public final Entry<String, ValueType> pollLastEntry() {
        Entry<StringSequence, ValueType> entry = trie.pollLastEntry();

        if (entry != null) {
            String key = entry.getKey() != null ? entry.getKey().toString() : null;
            return new AbstractMap.SimpleImmutableEntry<>(key, entry.getValue());
        }

        return null;
    }

    @Override
    public final NavigableMap<String, ValueType> descendingMap() {
        // TODO
        return null;
    }

    @Override
    public final NavigableSet<String> navigableKeySet() {
        // TODO
        return null;
    }

    @Override
    public final NavigableSet<String> descendingKeySet() {
        // TODO
        return null;
    }

    @Override
    public final NavigableMap<String, ValueType> subMap(final String fromKey,
                                                        final boolean fromInclusive,
                                                        final String toKey,
                                                        final boolean toInclusive) {
        // TODO
        return null;
    }

    @Override
    public final NavigableMap<String, ValueType> headMap(final String toKey,
                                                         final boolean inclusive) {
        // TODO
        return null;
    }

    @Override
    public final NavigableMap<String, ValueType> tailMap(final String fromKey,
                                                         final boolean inclusive) {
        // TODO
        return null;
    }

    @Override
    public Comparator<? super String> comparator() {
        // TODO
        return null;
    }

    @NotNull
    @Override
    public final SortedMap<String, ValueType> subMap(final String fromKey, final String toKey) {
        // TODO
        return null;
    }

    @NotNull
    @Override
    public final SortedMap<String, ValueType> headMap(final String toKey) {
        // TODO
        return null;
    }

    @NotNull
    @Override
    public final SortedMap<String, ValueType> tailMap(final String fromKey) {
        // TODO
        return null;
    }

    @Override
    public final String firstKey() {
        Entry<String, ValueType> entry = firstEntry();
        return entry != null ? entry.getKey() : null;
    }

    @Override
    public final String lastKey() {
        Entry<String, ValueType> entry = lastEntry();
        return entry != null ? entry.getKey() : null;
    }

    @NotNull
    @Override
    public final SortedStringTrie<ValueType> subTree(@NotNull final String key) {
        return new SortedStringTrieWrapper<>(trie.subTree(new StringSequence(key)));
    }

}
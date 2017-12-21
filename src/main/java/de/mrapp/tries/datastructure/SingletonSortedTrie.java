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
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * An immutable {@link SortedTrie}, which contains only a single entry.
 *
 * @param <SequenceType> The type of the sequences, which are used as the trie's keys
 * @param <ValueType>    The type of the values, which are stored by trie
 * @author Michael Rapp
 * @since 1.0.0
 */
public class SingletonSortedTrie<SequenceType extends Sequence, ValueType> extends
        AbstractSingletonTrie<SequenceType, ValueType> implements
        SortedTrie<SequenceType, ValueType> {

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = -1053546882360839829L;

    /**
     * Creates a new immutable {@link SortedTrie}, which only contains a single entry.
     *
     * @param key   The key of the entry as an instance of the generic type {@link SequenceType} or
     *              null
     * @param value The value of the entry as an instance of the generic type {@link ValueType} or
     */
    public SingletonSortedTrie(@Nullable final SequenceType key, @Nullable final ValueType value) {
        super(key, value);
    }

    @Override
    public final Entry<SequenceType, ValueType> lowerEntry(final SequenceType key) {
        return null;
    }

    @Override
    public final SequenceType lowerKey(final SequenceType key) {
        throw new NoSuchElementException();
    }

    @Override
    public final Entry<SequenceType, ValueType> floorEntry(final SequenceType key) {
        return null;
    }

    @Override
    public final SequenceType floorKey(final SequenceType key) {
        throw new NoSuchElementException();
    }

    @Override
    public final Entry<SequenceType, ValueType> ceilingEntry(final SequenceType key) {
        return null;
    }

    @Override
    public final SequenceType ceilingKey(final SequenceType key) {
        throw new NoSuchElementException();
    }

    @Override
    public final Entry<SequenceType, ValueType> higherEntry(final SequenceType key) {
        return null;
    }

    @Override
    public final SequenceType higherKey(final SequenceType key) {
        throw new NoSuchElementException();
    }

    @Override
    public final Entry<SequenceType, ValueType> firstEntry() {
        return new AbstractMap.SimpleImmutableEntry<>(key, value);
    }

    @Override
    public final Entry<SequenceType, ValueType> lastEntry() {
        return new AbstractMap.SimpleImmutableEntry<>(key, value);
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
        return this;
    }

    @Override
    public final NavigableSet<SequenceType> navigableKeySet() {
        NavigableSet<SequenceType> navigableSet = new TreeSet<>();
        navigableSet.add(key);
        return navigableSet;
    }

    @Override
    public final NavigableSet<SequenceType> descendingKeySet() {
        return navigableKeySet();
    }

    @Override
    public final NavigableMap<SequenceType, ValueType> subMap(final SequenceType fromKey,
                                                              final boolean fromInclusive,
                                                              final SequenceType toKey,
                                                              final boolean toInclusive) {
        return fromInclusive && toInclusive && containsKey(fromKey) && containsKey(toKey) ? this :
                Collections.emptyNavigableMap();
    }

    @Override
    public final NavigableMap<SequenceType, ValueType> headMap(final SequenceType toKey,
                                                               final boolean inclusive) {
        return inclusive && containsKey(toKey) ? this : Collections.emptyNavigableMap();
    }

    @Override
    public final NavigableMap<SequenceType, ValueType> tailMap(final SequenceType fromKey,
                                                               final boolean inclusive) {
        return inclusive && containsKey(fromKey) ? this : Collections.emptyNavigableMap();
    }

    @Override
    public final Comparator<? super SequenceType> comparator() {
        return null;
    }

    @NotNull
    @Override
    public final SortedMap<SequenceType, ValueType> subMap(final SequenceType fromKey,
                                                           final SequenceType toKey) {
        return containsKey(fromKey) && containsKey(toKey) ? this : Collections.emptySortedMap();
    }

    @NotNull
    @Override
    public final SortedMap<SequenceType, ValueType> headMap(final SequenceType toKey) {
        return containsKey(toKey) ? this : Collections.emptySortedMap();
    }

    @NotNull
    @Override
    public final SortedMap<SequenceType, ValueType> tailMap(final SequenceType fromKey) {
        return containsKey(fromKey) ? this : Collections.emptySortedMap();
    }

    @Override
    public final SequenceType firstKey() {
        return key;
    }

    @Override
    public final SequenceType lastKey() {
        return key;
    }

    @NotNull
    @Override
    public final SortedTrie<SequenceType, ValueType> subTree(@NotNull final SequenceType sequence) {
        if (containsKey(sequence)) {
            return this;
        }

        throw new NoSuchElementException();
    }

}
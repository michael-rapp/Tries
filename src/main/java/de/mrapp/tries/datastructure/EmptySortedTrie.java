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
 * An unmodifiable and empty sorted trie.
 *
 * @param <SequenceType> The type of the sequences, which are used as the trie's keys
 * @param <ValueType>    The type of the values, which are stored by trie
 * @author Michael Rapp
 * @since 1.0.0
 */
public class EmptySortedTrie<SequenceType extends Sequence, ValueType> extends
        AbstractEmptyTrie<SequenceType, ValueType> implements
        SortedTrie<SequenceType, ValueType> {

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = 6336443796163476401L;

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
        return null;
    }

    @Override
    public final Entry<SequenceType, ValueType> lastEntry() {
        return null;
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
        return Collections.emptyNavigableMap();
    }

    @Override
    public final NavigableSet<SequenceType> navigableKeySet() {
        return Collections.emptyNavigableSet();
    }

    @Override
    public final NavigableSet<SequenceType> descendingKeySet() {
        return Collections.emptyNavigableSet();
    }

    @Override
    public final NavigableMap<SequenceType, ValueType> subMap(final SequenceType fromKey,
                                                              final boolean fromInclusive,
                                                              final SequenceType toKey,
                                                              final boolean toInclusive) {
        return Collections.emptyNavigableMap();
    }

    @Override
    public final NavigableMap<SequenceType, ValueType> headMap(final SequenceType toKey,
                                                               final boolean inclusive) {
        return Collections.emptyNavigableMap();
    }

    @Override
    public final NavigableMap<SequenceType, ValueType> tailMap(final SequenceType fromKey,
                                                               final boolean inclusive) {
        return Collections.emptyNavigableMap();
    }

    @Override
    public final Comparator<? super SequenceType> comparator() {
        return null;
    }

    @NotNull
    @Override
    public final SortedMap<SequenceType, ValueType> subMap(final SequenceType fromKey,
                                                           final SequenceType toKey) {
        return Collections.emptySortedMap();
    }

    @NotNull
    @Override
    public final SortedMap<SequenceType, ValueType> headMap(final SequenceType toKey) {
        return Collections.emptySortedMap();
    }

    @NotNull
    @Override
    public final SortedMap<SequenceType, ValueType> tailMap(final SequenceType fromKey) {
        return Collections.emptySortedMap();
    }

    @Override
    public final SequenceType firstKey() {
        throw new NoSuchElementException();
    }

    @Override
    public final SequenceType lastKey() {
        throw new NoSuchElementException();
    }

    @NotNull
    @Override
    public final SortedTrie<SequenceType, ValueType> subTrie(@NotNull final SequenceType sequence) {
        throw new NoSuchElementException();
    }

    @Override
    public final String toString() {
        return "EmptySortedTrie[]";
    }

}
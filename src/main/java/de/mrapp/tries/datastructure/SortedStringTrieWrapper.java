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
package de.mrapp.tries.datastructure;

import de.mrapp.tries.SortedStringTrie;
import de.mrapp.tries.SortedTrie;
import de.mrapp.tries.sequence.StringSequence;
import de.mrapp.tries.util.EntryUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static de.mrapp.util.Condition.ensureNotNull;

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
     * A comparator, which allows to compare instances of the class {@link StringSequence} by
     * encapsulating a comparator, which compares {@link String}s.
     */
    protected static final class StringSequenceComparatorWrapper implements
            Comparator<StringSequence> {

        /**
         * The encapsulated comparator.
         */
        private final Comparator<? super String> comparator;

        /**
         * Creates a new comparator, which allows to compare instances of the class {@link
         * StringSequence} by encapsulating a comparator, which compares {@link String}s.
         *
         * @param comparator The comparator, which should be encapsulated, as an instance of the
         *                   type {@link Comparator}. The comparator may not be null
         */
        public StringSequenceComparatorWrapper(
                @NotNull final Comparator<? super String> comparator) {
            ensureNotNull(comparator, "The comparator may not be null");
            this.comparator = comparator;
        }

        @Override
        public final int compare(final StringSequence o1, final StringSequence o2) {
            return comparator.compare(StringSequence.convertToString(o1),
                    StringSequence.convertToString(o2));
        }

    }

    /**
     * A comparator, which allows to compare {@link String}s by encapsulating a comparator, which
     * compares instances of the class {@link StringSequence}.
     */
    private static final class StringComparatorWrapper implements Comparator<String> {

        /**
         * The encapsulated comparator.
         */
        private final Comparator<? super StringSequence> comparator;

        /**
         * Creates a new comparator, which allows to compare {@link String}s by encapsulating a
         * comparator, which compares instances of the class {@link StringSequence}.
         *
         * @param comparator The comparator, which should be encapsulated, as an instance of the
         *                   type {@link Comparator}. The comparator may not be null
         */
        StringComparatorWrapper(@NotNull final Comparator<? super StringSequence> comparator) {
            ensureNotNull(comparator, "The comparator may not be null");
            this.comparator = comparator;
        }

        @Override
        public int compare(final String o1, final String o2) {
            return comparator.compare(StringSequence.convertFromString(o1),
                    StringSequence.convertFromString(o2));
        }

    }

    private static class SortedKeySetWrapper<SetType extends SortedSet<StringSequence>> extends
            KeySetWrapper<SetType> implements SortedSet<String> {

        private final Comparator<String> comparator;

        SortedKeySetWrapper(@NotNull final SetType set) {
            super(set);
            Comparator<? super StringSequence> comparator = set.comparator();
            this.comparator = comparator != null ? new StringComparatorWrapper(comparator) : null;
        }

        @Nullable
        @Override
        public final Comparator<? super String> comparator() {
            return comparator;
        }

        @NotNull
        @Override
        public final SortedSet<String> subSet(final String fromElement, final String toElement) {
            return new SortedKeySetWrapper<>(
                    set.subSet(StringSequence.convertFromString(fromElement),
                            StringSequence.convertFromString(toElement)));
        }

        @NotNull
        @Override
        public final SortedSet<String> headSet(final String toElement) {
            return new SortedKeySetWrapper<>(
                    set.headSet(StringSequence.convertFromString(toElement)));
        }

        @NotNull
        @Override
        public final SortedSet<String> tailSet(final String fromElement) {
            return new SortedKeySetWrapper<>(
                    set.tailSet(StringSequence.convertFromString(fromElement)));
        }

        @Override
        public final String first() {
            return StringSequence.convertToString(set.first());
        }

        @Override
        public final String last() {
            return StringSequence.convertToString(set.last());
        }

    }

    private static final class NavigableKeySetWrapper extends
            SortedKeySetWrapper<NavigableSet<StringSequence>> implements NavigableSet<String> {

        private NavigableSet<String> descendingSet;

        NavigableKeySetWrapper(@NotNull final NavigableSet<StringSequence> set) {
            super(set);
        }

        @Nullable
        @Override
        public String lower(final String key) {
            return StringSequence.convertToString(set.lower(StringSequence.convertFromString(key)));
        }

        @Nullable
        @Override
        public String floor(final String key) {
            return StringSequence.convertToString(set.floor(StringSequence.convertFromString(key)));
        }

        @Nullable
        @Override
        public String ceiling(final String key) {
            return StringSequence
                    .convertToString(set.ceiling(StringSequence.convertFromString(key)));
        }

        @Nullable
        @Override
        public String higher(final String key) {
            return StringSequence
                    .convertToString(set.higher(StringSequence.convertFromString(key)));
        }

        @Nullable
        @Override
        public String pollFirst() {
            return StringSequence.convertToString(set.pollFirst());
        }

        @Nullable
        @Override
        public String pollLast() {
            return StringSequence.convertToString(set.pollLast());
        }

        @NotNull
        @Override
        public NavigableSet<String> descendingSet() {
            if (this.descendingSet == null) {
                this.descendingSet = new NavigableKeySetWrapper(set.descendingSet());
            }

            return this.descendingSet;
        }

        @NotNull
        @Override
        public Iterator<String> descendingIterator() {
            return new KeyIteratorWrapper(set.descendingIterator());
        }

        @NotNull
        @Override
        public NavigableSet<String> subSet(final String fromElement, final boolean fromInclusive,
                                           final String toElement, final boolean toInclusive) {
            return new NavigableKeySetWrapper(
                    set.subSet(StringSequence.convertFromString(fromElement), fromInclusive,
                            StringSequence.convertFromString(toElement), toInclusive));
        }

        @NotNull
        @Override
        public NavigableSet<String> headSet(final String toElement, final boolean inclusive) {
            return new NavigableKeySetWrapper(
                    set.headSet(StringSequence.convertFromString(toElement), inclusive));
        }

        @NotNull
        @Override
        public NavigableSet<String> tailSet(final String fromElement, final boolean inclusive) {
            return new NavigableKeySetWrapper(
                    set.tailSet(StringSequence.convertFromString(fromElement), inclusive));
        }

    }

    private static class SortedMapWrapper<V> extends AbstractMap<String, V> implements
            SortedMap<String, V> {

        private final SortedMap<StringSequence, V> map;

        private final Comparator<? super String> comparator;

        SortedMapWrapper(@NotNull final SortedMap<StringSequence, V> map) {
            ensureNotNull(map, "The map may not be null");
            this.map = map;
            Comparator<? super StringSequence> comparator = map.comparator();
            this.comparator = comparator != null ? new StringComparatorWrapper(comparator) : null;
        }

        @Override
        public final Comparator<? super String> comparator() {
            return comparator;
        }

        @NotNull
        @Override
        public final SortedMap<String, V> subMap(final String fromKey, final String toKey) {
            return new SortedMapWrapper<>(map.subMap(StringSequence.convertFromString(fromKey),
                    StringSequence.convertFromString(toKey)));
        }

        @NotNull
        @Override
        public final SortedMap<String, V> headMap(final String toKey) {
            return new SortedMapWrapper<>(map.headMap(StringSequence.convertFromString(toKey)));
        }

        @NotNull
        @Override
        public final SortedMap<String, V> tailMap(final String fromKey) {
            return new SortedMapWrapper<>(map.tailMap(StringSequence.convertFromString(fromKey)));
        }

        @Override
        public final String firstKey() {
            return StringSequence.convertToString(map.firstKey());
        }

        @Override
        public final String lastKey() {
            return StringSequence.convertToString(map.lastKey());
        }

        @NotNull
        @Override
        public final Set<Entry<String, V>> entrySet() {
            return new EntrySetWrapper<>(map.entrySet());
        }

        @Override
        public boolean remove(final Object key, final Object value) {
            return map.remove(StringSequence.convertFromString((String) key), value);
        }

    }

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = -1937689954255665817L;

    /**
     * Converts an entry of the encapsulated trie into an entry of the wrapper.
     *
     * @param entry The entry, which should be encapsulated, as an instance of the type {@link
     *              Entry} or null
     * @return The entry, which has been created, as an instance of the type {@link Entry} or null,
     * if the given entry is null
     */
    @Nullable
    private Entry<String, ValueType> convertEntry(
            @Nullable final Entry<StringSequence, ValueType> entry) {
        if (entry != null) {
            return new AbstractMap.SimpleImmutableEntry<>(
                    StringSequence.convertToString(entry.getKey()), entry.getValue());

        }

        return null;
    }

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
        return convertEntry(trie.lowerEntry(StringSequence.convertFromString(key)));
    }

    @Override
    public final String lowerKey(final String key) {
        return EntryUtil.getKey(lowerEntry(key));
    }

    @Override
    public final Entry<String, ValueType> floorEntry(final String key) {
        return convertEntry(trie.floorEntry(StringSequence.convertFromString(key)));
    }

    @Override
    public final String floorKey(final String key) {
        return EntryUtil.getKey(floorEntry(key));
    }

    @Override
    public final Entry<String, ValueType> ceilingEntry(final String key) {
        return convertEntry(trie.ceilingEntry(StringSequence.convertFromString(key)));
    }

    @Override
    public final String ceilingKey(final String key) {
        return EntryUtil.getKey(ceilingEntry(key));
    }

    @Override
    public final Entry<String, ValueType> higherEntry(final String key) {
        return convertEntry(trie.higherEntry(StringSequence.convertFromString(key)));
    }

    @Override
    public final String higherKey(final String key) {
        return EntryUtil.getKey(higherEntry(key));
    }

    @Override
    public final Entry<String, ValueType> firstEntry() {
        return convertEntry(trie.firstEntry());
    }

    @Override
    public final Entry<String, ValueType> lastEntry() {
        return convertEntry(trie.lastEntry());
    }

    @Override
    public final Entry<String, ValueType> pollFirstEntry() {
        return convertEntry(trie.pollFirstEntry());
    }

    @Override
    public final Entry<String, ValueType> pollLastEntry() {
        return convertEntry(trie.pollLastEntry());
    }

    @Override
    public final NavigableMap<String, ValueType> descendingMap() {
        // TODO
        return null;
    }

    @Override
    public final NavigableSet<String> navigableKeySet() {
        return new NavigableKeySetWrapper(trie.navigableKeySet());
    }

    @Override
    public final NavigableSet<String> descendingKeySet() {
        return new NavigableKeySetWrapper(trie.descendingKeySet());
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
        Comparator<? super StringSequence> comparator = trie.comparator();
        return comparator instanceof StringSequenceComparatorWrapper ?
                ((StringSequenceComparatorWrapper) comparator).comparator : null;
    }

    @NotNull
    @Override
    public final SortedMap<String, ValueType> subMap(final String fromKey, final String toKey) {
        return new SortedMapWrapper<>(trie.subMap(StringSequence.convertFromString(fromKey),
                StringSequence.convertFromString(toKey)));
    }

    @NotNull
    @Override
    public final SortedMap<String, ValueType> headMap(final String toKey) {
        return new SortedMapWrapper<>(trie.headMap(StringSequence.convertFromString(toKey)));
    }

    @NotNull
    @Override
    public final SortedMap<String, ValueType> tailMap(final String fromKey) {
        return new SortedMapWrapper<>(trie.tailMap(StringSequence.convertFromString(fromKey)));
    }

    @Override
    public final String firstKey() {
        return EntryUtil.getKey(firstEntry());
    }

    @Override
    public final String lastKey() {
        return EntryUtil.getKey(lastEntry());
    }

    @NotNull
    @Override
    public final SortedStringTrie<ValueType> subTrie(@NotNull final String sequence) {
        return new SortedStringTrieWrapper<>(trie.subTrie(new StringSequence(sequence)));
    }

}
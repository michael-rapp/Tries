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
            return comparator.compare(o1.toString(), o2.toString());
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
            return comparator.compare(new StringSequence(o1), new StringSequence(o2));
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
                    set.subSet(fromElement != null ? new StringSequence(fromElement) : null,
                            toElement != null ? new StringSequence(toElement) : null));
        }

        @NotNull
        @Override
        public final SortedSet<String> headSet(final String toElement) {
            return new SortedKeySetWrapper<>(
                    set.headSet(toElement != null ? new StringSequence(toElement) : null));
        }

        @NotNull
        @Override
        public final SortedSet<String> tailSet(final String fromElement) {
            return new SortedKeySetWrapper<>(
                    set.tailSet(fromElement != null ? new StringSequence(fromElement) : null));
        }

        @Override
        public final String first() {
            StringSequence firstKey = set.first();
            return firstKey != null ? firstKey.toString() : null;
        }

        @Override
        public final String last() {
            StringSequence lastKey = set.last();
            return lastKey != null ? lastKey.toString() : null;
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
            StringSequence lowerKey = set.lower(new StringSequence(key));
            return lowerKey != null ? lowerKey.toString() : null;
        }

        @Nullable
        @Override
        public String floor(final String key) {
            StringSequence floorKey = set.floor(new StringSequence(key));
            return floorKey != null ? floorKey.toString() : null;
        }

        @Nullable
        @Override
        public String ceiling(final String key) {
            StringSequence ceilingKey = set.ceiling(new StringSequence(key));
            return ceilingKey != null ? ceilingKey.toString() : null;
        }

        @Nullable
        @Override
        public String higher(final String key) {
            StringSequence higherKey = set.higher(new StringSequence(key));
            return higherKey != null ? higherKey.toString() : null;
        }

        @Nullable
        @Override
        public String pollFirst() {
            StringSequence firstKey = set.pollFirst();
            return firstKey != null ? firstKey.toString() : null;
        }

        @Nullable
        @Override
        public String pollLast() {
            StringSequence lastKey = set.pollLast();
            return lastKey != null ? lastKey.toString() : null;
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
                    set.subSet(fromElement != null ? new StringSequence(fromElement) : null,
                            fromInclusive, toElement != null ? new StringSequence(toElement) : null,
                            toInclusive));
        }

        @NotNull
        @Override
        public NavigableSet<String> headSet(final String toElement, final boolean inclusive) {
            return new NavigableKeySetWrapper(
                    set.headSet(toElement != null ? new StringSequence(toElement) : null,
                            inclusive));
        }

        @NotNull
        @Override
        public NavigableSet<String> tailSet(final String fromElement, final boolean inclusive) {
            return new NavigableKeySetWrapper(
                    set.tailSet(fromElement != null ? new StringSequence(fromElement) : null,
                            inclusive));
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
        StringSequence key = EntryUtil.getKey(entry);
        return key != null ?
                new AbstractMap.SimpleImmutableEntry<>(key.toString(), entry.getValue()) : null;
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
        return convertEntry(trie.lowerEntry(new StringSequence(key)));
    }

    @Override
    public final String lowerKey(final String key) {
        return EntryUtil.getKey(lowerEntry(key));
    }

    @Override
    public final Entry<String, ValueType> floorEntry(final String key) {
        return convertEntry(trie.floorEntry(new StringSequence(key)));
    }

    @Override
    public final String floorKey(final String key) {
        return EntryUtil.getKey(floorEntry(key));
    }

    @Override
    public final Entry<String, ValueType> ceilingEntry(final String key) {
        return convertEntry(trie.ceilingEntry(new StringSequence(key)));
    }

    @Override
    public final String ceilingKey(final String key) {
        return EntryUtil.getKey(ceilingEntry(key));
    }

    @Override
    public final Entry<String, ValueType> higherEntry(final String key) {
        return convertEntry(trie.higherEntry(new StringSequence(key)));
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
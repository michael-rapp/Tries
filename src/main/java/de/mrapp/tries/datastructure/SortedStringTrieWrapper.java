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
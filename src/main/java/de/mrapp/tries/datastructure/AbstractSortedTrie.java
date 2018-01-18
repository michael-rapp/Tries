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

import de.mrapp.tries.Node;
import de.mrapp.tries.Sequence;
import de.mrapp.tries.SortedTrie;
import de.mrapp.tries.util.EntryUtil;
import de.mrapp.tries.util.SequenceUtil;
import de.mrapp.util.datastructure.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static de.mrapp.util.Condition.*;

/**
 * An abstract base class for all sorted tries, whose nodes are ordered by their keys. It implements the methods of the
 * interface {@link NavigableMap}. In addition to the abstract methods of the parent class {@link AbstractTrie},
 * subclasses must implement the method {@link #indexOf(Node, Sequence)}}, depending on the trie's structure.
 *
 * @param <SequenceType> The type of the sequences, which are used as the trie's keys
 * @param <ValueType>    The type of the values, which are stored by the trie
 * @author Michael Rapp
 * @since 1.0.0
 */
public abstract class AbstractSortedTrie<SequenceType extends Sequence, ValueType>
        extends AbstractTrie<SequenceType, ValueType> implements SortedTrie<SequenceType, ValueType> {

    /**
     * A key set of a sorted trie as returned by the method {@link SortedTrie#navigableKeySet()}.
     *
     * @param <K> The type of the sequences, which are used as the trie's keys
     */
    private static final class NavigableKeySet<K extends Sequence> extends AbstractKeySet<K, NavigableMap<K, ?>>
            implements NavigableSet<K> {

        /**
         * Creates a new key set of a sorted trie.
         *
         * @param backingMap The backing map, which should be used, as an instance of the type {@link NavigableMap}. The
         *                   backing map may not be null
         */
        NavigableKeySet(@NotNull final NavigableMap<K, ?> backingMap) {
            super(backingMap);
        }

        @SuppressWarnings("unchecked")
        @NotNull
        @Override
        public Iterator<K> iterator() {
            if (backingMap instanceof AbstractSortedTrie) {
                return ((AbstractSortedTrie<K, ?>) backingMap).keyIterator();
            } else {
                return ((AbstractSubMap<K, ?>) backingMap).keyIterator();
            }
        }

        @SuppressWarnings("unchecked")
        @NotNull
        @Override
        public Iterator<K> descendingIterator() {
            if (backingMap instanceof AbstractSortedTrie) {
                return ((AbstractSortedTrie<K, ?>) backingMap).descendingKeyIterator();
            } else {
                return ((AbstractSubMap<K, ?>) backingMap).descendingKeyIterator();
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public Spliterator<K> spliterator() {
            if (backingMap instanceof AbstractSortedTrie) {
                return ((AbstractSortedTrie<K, ?>) backingMap).keySpliterator();
            } else if (backingMap instanceof DescendingSubMap) {
                DescendingSubMap<K, ?> subMap = (DescendingSubMap<K, ?>) backingMap;
                AbstractSortedTrie<K, ?> backingTrie = subMap.trie;

                if (subMap == backingTrie.descendingMap) {
                    return backingTrie.descendingKeySpliterator();
                }
            }

            return ((AbstractSubMap<K, ?>) backingMap).keySpliterator();
        }

        @Override
        public K lower(final K e) {
            return backingMap.lowerKey(e);
        }

        @Override
        public K floor(final K e) {
            return backingMap.floorKey(e);
        }

        @Override
        public K ceiling(final K e) {
            return backingMap.ceilingKey(e);
        }

        @Override
        public K higher(final K e) {
            return backingMap.higherKey(e);
        }

        @Override
        public K first() {
            return backingMap.firstKey();
        }

        @Override
        public K last() {
            return backingMap.lastKey();
        }

        @Override
        public Comparator<? super K> comparator() {
            return backingMap.comparator();
        }

        @Override
        public K pollFirst() {
            return EntryUtil.getKey(backingMap.pollFirstEntry());
        }

        @Override
        public K pollLast() {
            return EntryUtil.getKey(backingMap.pollLastEntry());
        }

        @NotNull
        @Override
        public NavigableSet<K> subSet(final K fromElement, final boolean fromInclusive, final K toElement,
                                      final boolean toInclusive) {
            return new NavigableKeySet<>(backingMap.subMap(fromElement, fromInclusive, toElement, toInclusive));
        }

        @NotNull
        @Override
        public NavigableSet<K> headSet(final K toElement, final boolean inclusive) {
            return new NavigableKeySet<>(backingMap.headMap(toElement, inclusive));
        }

        @NotNull
        @Override
        public NavigableSet<K> tailSet(final K fromElement, final boolean inclusive) {
            return new NavigableKeySet<>(backingMap.tailMap(fromElement, inclusive));
        }

        @NotNull
        @Override
        public SortedSet<K> subSet(final K fromElement, final K toElement) {
            return subSet(fromElement, true, toElement, false);
        }

        @NotNull
        @Override
        public SortedSet<K> headSet(final K toElement) {
            return headSet(toElement, false);
        }

        @NotNull
        @Override
        public SortedSet<K> tailSet(final K fromElement) {
            return tailSet(fromElement, true);
        }

        @NotNull
        @Override
        public NavigableSet<K> descendingSet() {
            return new NavigableKeySet<>(backingMap.descendingMap());
        }

    }

    /**
     * An abstract base class for all navigable maps, which only contain a subset of a trie's entries. The navigable map
     * is backed by an implementation of the class {@link AbstractSubSet}.
     *
     * @param <K> The type of the sequences, which are used as the trie's keys
     * @param <V> The type of the values, which are stored by the trie
     */
    private abstract static class AbstractSubMap<K extends Sequence, V> extends AbstractMap<K, V>
            implements NavigableMap<K, V>, Serializable {

        /**
         * An abstract base class for all sets, which only contain a subset of a trie's entries.
         */
        protected abstract class AbstractSubSet extends AbstractSet<Map.Entry<K, V>> {

            /**
             * The size of the set or -1, if the size has not been obtained yet.
             */
            private transient int size = -1;

            /**
             * The modification count of the backing trie, when the size has been obtained for the last time.
             */
            private transient long sizeModificationCount;

            /**
             * Returns the key, a specific value corresponds to.
             *
             * @param value The value, whose key should be returned, as an instance of the class {@link Object} or null
             * @return The key of the given value as an instance of the generic type {@link K} or null, if no such key
             * exists
             */
            @SuppressWarnings("unchecked")
            @Nullable
            private K getKey(@Nullable final Object value) {
                if (!(value instanceof Map.Entry))
                    return null;
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) value;
                K key = (K) entry.getKey();
                if (!isInOpenRange(key))
                    return null;
                Node<K, V> node = trie.getNode(key);
                if (node == null)
                    return null;
                return EntryUtil.isValueEqual(node, entry) ? key : null;
            }

            @Override
            public final int size() {
                if (fromStart && toEnd) {
                    return trie.size();
                } else if (size == -1 || sizeModificationCount != trie.modificationCount) {
                    sizeModificationCount = trie.modificationCount;
                    size = 0;

                    for (Object ignored : this) {
                        size++;
                    }
                }

                return size;
            }

            @Override
            public final boolean isEmpty() {
                Map.Entry<K, V> entry = getLowestEntry();
                return entry == null || isTooHigh(entry.getKey());
            }

            @Override
            public final boolean contains(final Object o) {
                return getKey(o) != null;
            }

            @Override
            public final boolean remove(Object o) {
                Optional<K> optional = Optional.ofNullable(getKey(o));
                optional.ifPresent(trie::remove);
                return optional.isPresent();
            }

        }

        /**
         * A wrapper, which encapsulates an iterator, which allows to iterate items of the type {@link Map.Entry}, in
         * order to implement an iterator, which iterates the keys of the entries.
         *
         * @param <K> The type of the keys of the iterated entries
         * @param <V> The type of the values of the iterated entries
         */
        static final class KeyIteratorWrapper<K extends Sequence, V> implements Iterator<K> {

            /**
             * The encapsulated iterator.
             */
            private final Iterator<Map.Entry<K, V>> iterator;

            /**
             * Creates a new wrapper, which encapsulates an iterator, which allows to iterate items of the type {@link
             * Map.Entry}, in order to implement an iterator, which iterates the keys of the entries.
             *
             * @param iterator The iterator, which should be encapsulated, as an instance of the type {@link Iterator}.
             *                 The iterator may not be null
             */
            KeyIteratorWrapper(@NotNull final Iterator<Map.Entry<K, V>> iterator) {
                ensureNotNull(iterator, "The iterator may not be null");
                this.iterator = iterator;
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public K next() {
                return iterator.next().getKey();
            }

        }

        /**
         * An iterator, which allows to iterate the entries of a trie's {@link AbstractSubMap} in ascending order.
         *
         * @param <K> The type of the sequences, which are used as the trie's keys
         * @param <V> The type of the values, which are stored by the trie
         */
        static final class AscendingSubMapEntryIterator<K extends Sequence, V>
                extends AbstractSubMapEntryIterator<K, V> {

            /**
             * Creates a new iterator, which allows to iterate the entries of a trie's {@link AbstractSubMap} in
             * ascending order.
             *
             * @param trie  The trie, whose entries should be iterated, as an instance of the class {@link
             *              AbstractSortedTrie}. The trie may not be null
             * @param first The first entry to be iterated as an instance of the type {@link Map.Entry} or null, if the
             *              trie is empty
             * @param fence The entry, which marks the end of the sub map (exclusive), as an instance of the type {@link
             *              Map.Entry} or null, if the map should not be restricted
             */
            AscendingSubMapEntryIterator(@NotNull final AbstractSortedTrie<K, V> trie,
                                         @Nullable final Map.Entry<K, V> first, @Nullable final Map.Entry<K, V> fence) {
                super(trie, first, fence);
            }

            @Override
            public Entry<K, V> next() {
                ensureEqual(expectedModificationCount, trie.modificationCount, null,
                        ConcurrentModificationException.class);
                ensureNotNull(next, null, NoSuchElementException.class);
                ensureFalse(fenceKey.equals(next.getKey()), null, NoSuchElementException.class);
                Map.Entry<K, V> entry = next;
                next = trie.higherEntry(entry.getKey());
                lastReturned = entry;
                return entry;
            }

        }

        /**
         * An iterator, which allows to iterate the entries of a trie's {@link AbstractSubMap} in descending order.
         *
         * @param <K> The type of the sequences, which are used as the trie's keys
         * @param <V> The type of the values, which are stored by the trie
         */
        static final class DescendingSubMapEntryIterator<K extends Sequence, V>
                extends AbstractSubMapEntryIterator<K, V> {

            /**
             * Creates a new iterator, which allows to iterate the entries of a trie's {@link AbstractSubMap} in
             * descending order.
             *
             * @param trie  The trie, whose entries should be iterated, as an instance of the class {@link
             *              AbstractSortedTrie}. The trie may not be null
             * @param first The first entry to be iterated as an instance of the type {@link Map.Entry} or null, if the
             *              trie is empty
             * @param fence The entry, which marks the end of the sub map (exclusive), as an instance of the type {@link
             *              Map.Entry} or null, if the map should not be restricted
             */
            DescendingSubMapEntryIterator(@NotNull final AbstractSortedTrie<K, V> trie,
                                          @Nullable final Map.Entry<K, V> first,
                                          @Nullable final Map.Entry<K, V> fence) {
                super(trie, first, fence);
            }

            @Override
            public Entry<K, V> next() {
                ensureEqual(expectedModificationCount, trie.modificationCount, null,
                        ConcurrentModificationException.class);
                ensureNotNull(next, null, NoSuchElementException.class);
                ensureFalse(fenceKey.equals(next.getKey()), null, NoSuchElementException.class);
                Map.Entry<K, V> entry = next;
                next = trie.lowerEntry(entry.getKey());
                lastReturned = entry;
                return entry;
            }

        }

        /**
         * An abstract base class for all iterators, which allow to iterate the entries of a trie's {@link
         * AbstractSubMap}.
         *
         * @param <K> The type of the sequences, which are used as the trie's keys
         * @param <V> The type of the values, which are stored by the trie
         */
        abstract static class AbstractSubMapEntryIterator<K extends Sequence, V>
                extends AbstractEntryIterator<K, V, Map.Entry<K, V>> {

            /**
             * The key, which is used to indicate that a map is unrestricted.
             */
            private static final Object UNRESTRICTED_KEY = new Object();

            /**
             * The key of the entry, which marks the end of the sub map (exclusive) or {@link #UNRESTRICTED_KEY} if the
             * map is not restricted.
             */
            final Object fenceKey;

            /**
             * Creates a new iterator, which allows to iterate the entries of a trie's {@link AbstractSubMap}.
             *
             * @param trie  The trie, whose entries should be iterated, as an instance of the class {@link
             *              AbstractSortedTrie}. The trie may not be null
             * @param first The first entry to be iterated as an instance of the type {@link Map.Entry} or null, if the
             *              trie is empty
             * @param fence The entry, which marks the end of the sub map (exclusive), as an instance of the type {@link
             *              Map.Entry} or null, if the map should not be restricted
             */
            AbstractSubMapEntryIterator(@NotNull final AbstractSortedTrie<K, V> trie,
                                        @Nullable final Map.Entry<K, V> first, @Nullable final Map.Entry<K, V> fence) {
                super(trie, first);
                this.fenceKey = fence != null ? fence.getKey() : UNRESTRICTED_KEY;
            }

            @Override
            public final boolean hasNext() {
                return super.hasNext() && !fenceKey.equals(next.getKey());
            }

        }

        /**
         * The constant serial version UID.
         */
        private static final long serialVersionUID = 2179020779953916850L;

        /**
         * The backing trie.
         */
        final AbstractSortedTrie<K, V> trie;

        /**
         * The comparator, which is used to compare keys with each other.
         */
        private final Comparator<? super K> comparator;

        final K fromKey, toKey;

        final boolean fromStart, toEnd;

        final boolean fromInclusive, toInclusive;

        /**
         * Returns, whether a key is too low to be included in the sub map.
         *
         * @param key The key, which should be checked, as an instance of the generic type {@link K} or null
         * @return True, if the given key is too low, false otherwise
         */
        private boolean isTooLow(@Nullable final K key) {
            if (!fromStart) {
                int c = comparator.compare(key, fromKey);
                return c < 0 || (c == 0 && !fromInclusive);
            }

            return false;
        }

        /**
         * Returns, whether a key is too high to be included in the sub map.
         *
         * @param key The key, which should be checked, as an instance of the generic type {@link K} or null
         * @return True, if the given key is too high, false otherwise
         */
        private boolean isTooHigh(@Nullable final K key) {
            if (!toEnd) {
                int c = comparator.compare(key, toKey);
                return c > 0 || (c == 0 && !toInclusive);
            }

            return false;
        }

        /**
         * Returns, whether a key is included in the sub map, or not. This method does return true, if the given key is
         * the first or last included key.
         *
         * @param key The key, which should be checked, as an instance of the generic type {@link K} or null
         * @return True, if the given key is included in the sub map, false otherwise
         */
        private boolean isInOpenRange(@Nullable final K key) {
            return !isTooLow(key) && !isTooHigh(key);
        }

        /**
         * Returns, whether a key is included in the sub map, or not. This method does return false, if the given key is
         * the first or last included key.
         *
         * @param key The key, which should be checked, as an instance of the generic type {@link K} or null
         * @return True, if the given key is included in the sub map, false otherwise
         */
        private boolean isInClosedRange(@Nullable final K key) {
            return (fromStart || comparator.compare(key, fromKey) >= 0) &&
                    (toEnd || comparator.compare(toKey, key) >= 0);
        }

        /**
         * Returns, whether a key is included in the sub map, or not.
         *
         * @param key       The key, which should be checked, as an instance of the generic type {@link K} or null
         * @param inclusive True, if the key should be considered as included, if it is the first or last key, false
         *                  otherwise
         * @return True, if the given key is included in the sub map, false otherwise
         */
        final boolean isInRange(@Nullable final K key, final boolean inclusive) {
            return inclusive ? isInOpenRange(key) : isInClosedRange(key);
        }

        /**
         * Returns the lowest entry of the sub map.
         *
         * @return The lowest entry of the sub map as an instance of the type {@link Map.Entry} or null, if the sub map
         * is empty
         */
        @Nullable
        final Map.Entry<K, V> getLowestEntry() {
            Map.Entry<K, V> entry = (fromStart ? trie.firstEntry() :
                    (fromInclusive ? trie.ceilingEntry(fromKey) : trie.higherEntry(fromKey)));
            return (entry == null || isTooHigh(entry.getKey())) ? null : entry;
        }

        /**
         * Returns the highest entry of the sub map.
         *
         * @return The highest entry of the sub map as an instance of the type {@link Map.Entry} or null, if the sub map
         * is empty
         */
        @Nullable
        final Map.Entry<K, V> getHighestEntry() {
            Map.Entry<K, V> entry =
                    (toEnd ? trie.lastEntry() : (toInclusive ? trie.floorEntry(toKey) : trie.lowerEntry(toKey)));
            return (entry == null || isTooLow(entry.getKey())) ? null : entry;
        }

        /**
         * Returns the entry, whose key is greater or equal than a specific key.
         *
         * @return The entry, whose key is greater or equal than the given key, as an instance of the type {@link
         * Map.Entry} or null, if no such entry is available
         */
        @Nullable
        final Map.Entry<K, V> getCeilingEntry(@Nullable final K key) {
            if (isTooLow(key)) {
                return getLowestEntry();
            }

            Map.Entry<K, V> entry = trie.ceilingEntry(key);
            return (entry == null || isTooHigh(entry.getKey())) ? null : entry;
        }

        /**
         * Returns the entry, whose key is greater than a specific key.
         *
         * @return The entry, whose key is greater than the given key, as an instance of the type {@link Map.Entry} or
         * null, if no such entry is available
         */
        @Nullable
        final Map.Entry<K, V> getHigherEntry(@Nullable final K key) {
            if (isTooLow(key)) {
                return getLowestEntry();
            }

            Map.Entry<K, V> entry = trie.higherEntry(key);
            return (entry == null || isTooHigh(entry.getKey())) ? null : entry;
        }

        /**
         * Returns the entry, whose key is less or equal than a specific key.
         *
         * @return The entry, whose key is less or equal than the given key, as an instance of the type {@link
         * Map.Entry} or null, if no such entry is available
         */
        @Nullable
        final Map.Entry<K, V> getFloorEntry(@Nullable final K key) {
            if (isTooHigh(key)) {
                return getHighestEntry();
            }

            Map.Entry<K, V> entry = trie.floorEntry(key);
            return (entry == null || isTooLow(entry.getKey())) ? null : entry;
        }

        /**
         * Returns the entry, whose key is less than a specific key.
         *
         * @return The entry, whose key is less than the given key, as an instance of the type {@link Map.Entry} or
         * null, if no such entry is available
         */
        @Nullable
        final Map.Entry<K, V> getLowerEntry(@Nullable final K key) {
            if (isTooHigh(key)) {
                return getHighestEntry();
            }

            Map.Entry<K, V> entry = trie.lowerEntry(key);
            return (entry == null || isTooLow(entry.getKey())) ? null : entry;
        }

        /**
         * Returns the entry, which marks the upper limit for traversal in ascending order.
         *
         * @return The entry, which marks the upper limit  for traversal in ascending order, as an instance of the type
         * {@link Map.Entry} or null, if the map is unrestricted
         */
        @Nullable
        final Map.Entry<K, V> getHighFence() {
            return (toEnd ? null : (toInclusive ? trie.higherEntry(toKey) : trie.ceilingEntry(toKey)));
        }

        /**
         * Returns the entry, which marks the lower limit for traversal in descending order.
         *
         * @return The entry, which marks the lower limit  for traversal in descending order, as an instance of the type
         * {@link Map.Entry} or null, if the map is unrestricted
         */
        @Nullable
        final Map.Entry<K, V> getLowFence() {
            return (fromStart ? null : (fromInclusive ? trie.lowerEntry(fromKey) : trie.floorEntry(fromKey)));
        }

        /**
         * The method, which is invoked on subclasses in order to invoke the correct method for retrieving the lowest
         * entry, depending on the traversal order
         *
         * @see #getLowestEntry()
         */
        @Nullable
        protected abstract Map.Entry<K, V> onGetLowestEntry();

        /**
         * The method, which is invoked on subclasses in order to invoke the correct method for retrieving the highest
         * entry, depending on the traversal order
         *
         * @see #getHighestEntry()
         */
        @Nullable
        protected abstract Map.Entry<K, V> onGetHighestEntry();

        /**
         * The method, which is invoked on subclasses in order to invoke the correct method for retrieving the ceiling
         * entry, depending on the traversal order
         *
         * @see #getCeilingEntry(Sequence)
         */
        @Nullable
        protected abstract Map.Entry<K, V> onGetCeilingEntry(@Nullable final K key);

        /**
         * The method, which is invoked on subclasses in order to invoke the correct method for retrieving the higher
         * entry, depending on the traversal order
         *
         * @see #getHigherEntry(Sequence)
         */
        @Nullable
        protected abstract Map.Entry<K, V> onGetHigherEntry(@Nullable final K key);

        /**
         * The method, which is invoked on subclasses in order to invoke the correct method for retrieving the floor
         * entry, depending on the traversal order
         *
         * @see #getFloorEntry(Sequence)
         */
        @Nullable
        protected abstract Map.Entry<K, V> onGetFloorEntry(@Nullable final K key);

        /**
         * The method, which is invoked on subclasses in order to invoke the correct method for retrieving the lower
         * entry, depending on the traversal order
         *
         * @see #getLowerEntry(Sequence)
         */
        @Nullable
        protected abstract Map.Entry<K, V> onGetLowerEntry(@Nullable final K key);

        /**
         * The method, which is invoked on subclasses in order to retrieve an iterator, which allows to iterate the sub
         * map's keys in ascending order.
         *
         * @return An iterator, which allows to iterate the sub map's keys in ascending order, as an instance of the
         * type {@link Iterator}. The iterator may not be null
         */
        @NotNull
        protected abstract Iterator<K> keyIterator();

        /**
         * The method, which is invoked on subclasses in order to retrieve an iterator, which allows to iterate the sub
         * map's keys in descending order.
         *
         * @return An iterator, which allows to iterate the sub map's keys in descending order, as an instance of the
         * type {@link Iterator}. The iterator may not be null
         */
        @NotNull
        protected abstract Iterator<K> descendingKeyIterator();

        /**
         * The method, which is invoked on subclasses in order to retrieve a spliterator, which allows to traverse the
         * sub map's keys in ascending order.
         *
         * @return A spliterator, which allows to traverse the sub map's keys in ascending order, as an instance of the
         * type {@link Spliterator}. The spliterator may not be null
         */
        @NotNull
        protected abstract Spliterator<K> keySpliterator();

        /**
         * Creates a new navigable maps, which only contain a subset of a trie's entries.
         *
         * @param trie          The backing trie, which should be used, as an instance of the class {@link
         *                      AbstractSortedTrie}. The trie may not be null
         * @param fromStart     True, if the sub map should start at the first entry of the backing trie, false
         *                      otherwise
         * @param fromKey       The key of the entry, the sub map should start at, as an instance of the generic type
         *                      {@link K} or null if the start of the sub map should be unrestricted
         * @param fromInclusive True, if the {@code fromKey} should be included in the sub map, false otherwise
         * @param toEnd         True, if the sub map should reach until the last entry of the backing trie, false
         *                      otherwise
         * @param toKey         The key of the entry, the sub map should reach to, as an instance of the generic type
         *                      {@link K} or null if the end of the sub map should be unrestricted
         * @param toInclusive   True, if the {@code toKey} should be included in the sub map, false otherwise
         */
        AbstractSubMap(@NotNull final AbstractSortedTrie<K, V> trie, final boolean fromStart, @Nullable final K fromKey,
                       final boolean fromInclusive, final boolean toEnd, @Nullable final K toKey,
                       final boolean toInclusive) {
            ensureNotNull(trie, "The trie may not be null");
            this.trie = trie;
            this.comparator = SequenceUtil.comparator(trie.comparator);

            if (!fromStart && !toEnd && comparator.compare(fromKey, toKey) > 0) {
                throw new IllegalArgumentException("fromKey > toKey");
            }

            this.fromStart = fromStart;
            this.fromKey = fromKey;
            this.fromInclusive = fromInclusive;
            this.toEnd = toEnd;
            this.toKey = toKey;
            this.toInclusive = toInclusive;
        }

        @Override
        public final boolean isEmpty() {
            return (fromStart && toEnd) ? trie.isEmpty() : entrySet().isEmpty();
        }

        @Override
        public final int size() {
            return (fromStart && toEnd) ? trie.size() : entrySet().size();
        }

        @SuppressWarnings("unchecked")
        @Override
        public final boolean containsKey(final Object key) {
            return isInOpenRange((K) key) && trie.containsKey(key);
        }

        @Override
        public final V put(final K key, final V value) {
            ensureTrue(isInOpenRange(key), "Key out of range");
            return trie.put(key, value);
        }

        @SuppressWarnings("unchecked")
        @Override
        public final V get(final Object key) {
            return !isInOpenRange((K) key) ? null : trie.get(key);
        }

        @SuppressWarnings("unchecked")
        @Override
        public final V remove(final Object key) {
            return !isInOpenRange((K) key) ? null : trie.remove(key);
        }

        @Override
        public final Map.Entry<K, V> ceilingEntry(final K key) {
            return onGetCeilingEntry(key);
        }

        @Override
        public final K ceilingKey(final K key) {
            return EntryUtil.getKey(onGetCeilingEntry(key));
        }

        @Override
        public final Map.Entry<K, V> higherEntry(final K key) {
            return onGetHigherEntry(key);
        }

        @Override
        public final K higherKey(final K key) {
            return EntryUtil.getKey(onGetHigherEntry(key));
        }

        @Override
        public final Map.Entry<K, V> floorEntry(final K key) {
            return onGetFloorEntry(key);
        }

        @Override
        public final K floorKey(final K key) {
            return EntryUtil.getKey(onGetFloorEntry(key));
        }

        @Override
        public final Map.Entry<K, V> lowerEntry(final K key) {
            return onGetLowerEntry(key);
        }

        @Override
        public final K lowerKey(final K key) {
            return EntryUtil.getKey(onGetLowerEntry(key));
        }

        @Override
        public final K firstKey() {
            return EntryUtil.getKeyOrThrowException(onGetLowestEntry());
        }

        @Override
        public final K lastKey() {
            return EntryUtil.getKeyOrThrowException(onGetHighestEntry());
        }

        @Override
        public final Map.Entry<K, V> firstEntry() {
            return onGetLowestEntry();
        }

        @Override
        public final Map.Entry<K, V> lastEntry() {
            return onGetHighestEntry();
        }

        @Override
        public final Map.Entry<K, V> pollFirstEntry() {
            Map.Entry<K, V> entry = onGetLowestEntry();

            if (entry != null) {
                trie.remove(entry.getKey());
            }

            return entry;
        }

        @Override
        public final Map.Entry<K, V> pollLastEntry() {
            Map.Entry<K, V> entry = onGetHighestEntry();

            if (entry != null) {
                trie.remove(entry.getKey());
            }

            return entry;
        }

        @NotNull
        @Override
        public final Set<K> keySet() {
            return navigableKeySet();
        }

        @Override
        public final NavigableSet<K> descendingKeySet() {
            return descendingMap().navigableKeySet();
        }

        @NotNull
        @Override
        public final SortedMap<K, V> subMap(final K fromKey, final K toKey) {
            return subMap(fromKey, true, toKey, false);
        }

        @NotNull
        @Override
        public final SortedMap<K, V> headMap(final K toKey) {
            return headMap(toKey, false);
        }

        @NotNull
        @Override
        public final SortedMap<K, V> tailMap(final K fromKey) {
            return tailMap(fromKey, true);
        }

        @NotNull
        @Override
        public final NavigableSet<K> navigableKeySet() {
            return new NavigableKeySet<>(this);
        }

    }

    /**
     * A navigable map, which only contains a subset of a trie's entries. The entries in the sub map are given in the
     * original order.
     *
     * @param <K> The type of the sequences, which are used as the trie's keys
     * @param <V> The type of the values, which are stored by the trie
     */
    private static final class AscendingSubMap<K extends Sequence, V> extends AbstractSubMap<K, V> {

        /**
         * The sub set, which backs the sub map.
         */
        private final class AscendingSubSet extends AbstractSubSet {

            @NotNull
            @Override
            public Iterator<Entry<K, V>> iterator() {
                return new AscendingSubMapEntryIterator<>(trie, getLowestEntry(), getHighFence());
            }

        }

        /**
         * The constant serial version UID.
         */
        private static final long serialVersionUID = 5937476665504869649L;

        /**
         * Creates a new navigable map, which only contains a subset of a trie's entries.
         *
         * @param trie          The backing trie, which should be used, as an instance of the class {@link
         *                      AbstractSortedTrie}. The trie may not be null
         * @param fromStart     True, if the sub map should start at the first entry of the backing trie, false
         *                      otherwise
         * @param fromKey       The key of the entry, the sub map should start at, as an instance of the generic type
         *                      {@link K} or null if the start of the sub map should be unrestricted
         * @param fromInclusive True, if the {@code fromKey} should be included in the sub map, false otherwise
         * @param toEnd         True, if the sub map should reach until the last entry of the backing trie, false
         *                      otherwise
         * @param toKey         The key of the entry, the sub map should reach to, as an instance of the generic type
         *                      {@link K} or null if the end of the sub map should be unrestricted
         * @param toInclusive   True, if the {@code toKey} should be included in the sub map, false otherwise
         */
        AscendingSubMap(@NotNull final AbstractSortedTrie<K, V> trie, final boolean fromStart,
                        @Nullable final K fromKey, final boolean fromInclusive, final boolean toEnd,
                        @Nullable final K toKey, final boolean toInclusive) {
            super(trie, fromStart, fromKey, fromInclusive, toEnd, toKey, toInclusive);
        }

        @Override
        public Comparator<? super K> comparator() {
            return trie.comparator();
        }

        @Override
        public NavigableMap<K, V> subMap(final K fromKey, final boolean fromInclusive, final K toKey,
                                         final boolean toInclusive) {
            ensureTrue(isInRange(fromKey, fromInclusive), "fromKey out of range");
            ensureTrue(isInRange(toKey, toInclusive), "toKey out of range");
            return new AscendingSubMap<>(trie, false, fromKey, fromInclusive, false, toKey, toInclusive);
        }

        @Override
        public NavigableMap<K, V> headMap(final K to, final boolean inclusive) {
            ensureTrue(isInRange(to, inclusive), "Key out of range");
            return new AscendingSubMap<>(trie, fromStart, fromKey, fromInclusive, false, to, inclusive);
        }

        @Override
        public NavigableMap<K, V> tailMap(final K from, final boolean inclusive) {
            ensureTrue(isInRange(from, inclusive), "Key out of range");
            return new AscendingSubMap<>(trie, false, from, inclusive, toEnd, toKey, toInclusive);
        }

        @Override
        public NavigableMap<K, V> descendingMap() {
            return new DescendingSubMap<>(trie, fromStart, fromKey, fromInclusive, toEnd, toKey, toInclusive);
        }

        @NotNull
        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            return new AscendingSubSet();
        }

        @Override
        protected Map.Entry<K, V> onGetLowestEntry() {
            return getLowestEntry();
        }

        @Override
        protected Map.Entry<K, V> onGetHighestEntry() {
            return getHighestEntry();
        }

        @Override
        protected Map.Entry<K, V> onGetCeilingEntry(@Nullable final K key) {
            return getCeilingEntry(key);
        }

        @Override
        protected Map.Entry<K, V> onGetHigherEntry(@Nullable final K key) {
            return getHigherEntry(key);
        }

        @Override
        protected Map.Entry<K, V> onGetFloorEntry(@Nullable final K key) {
            return getFloorEntry(key);
        }

        @Override
        protected Map.Entry<K, V> onGetLowerEntry(@Nullable final K key) {
            return getLowerEntry(key);
        }

        @NotNull
        @Override
        protected Iterator<K> keyIterator() {
            return new KeyIteratorWrapper<>(new AscendingSubMapEntryIterator<>(trie, getLowestEntry(), getHighFence()));
        }

        @NotNull
        @Override
        protected Iterator<K> descendingKeyIterator() {
            return new KeyIteratorWrapper<>(
                    new DescendingSubMapEntryIterator<>(trie, getHighestEntry(), getLowFence()));
        }

        @NotNull
        @Override
        protected Spliterator<K> keySpliterator() {
            return new SpliteratorWrapper<>(trie, new KeyIteratorWrapper<>(
                    new AscendingSubMapEntryIterator<>(trie, getLowestEntry(), getHighFence())));
        }

    }

    /**
     * A navigable map, which only contain a subset of a trie's entries. The entries in the sub map are given in reverse
     * order.
     *
     * @param <K> The type of the sequences, which are used as the trie's keys
     * @param <V> The type of the values, which are stored by the trie
     */
    private static final class DescendingSubMap<K extends Sequence, V> extends AbstractSubMap<K, V> {

        /**
         * The sub set, which backs the sub map.
         */
        private final class DescendingSubSet extends AbstractSubSet {

            @NotNull
            @Override
            public Iterator<Map.Entry<K, V>> iterator() {
                return new DescendingSubMapEntryIterator<>(trie, getHighestEntry(), getLowFence());
            }

        }

        /**
         * The constant serial version UID.
         */
        private static final long serialVersionUID = 912986545866120460L;

        /**
         * The comparator, which is used to order the entries in reverse order.
         */
        private final Comparator<? super K> reverseComparator;

        /**
         * Creates a new navigable map, which only contains a subset of a trie's entries in reverse order.
         *
         * @param trie          The backing trie, which should be used, as an instance of the class {@link
         *                      AbstractSortedTrie}. The trie may not be null
         * @param fromStart     True, if the sub map should start at the first entry of the backing trie, false
         *                      otherwise
         * @param fromKey       The key of the entry, the sub map should start at, as an instance of the generic type
         *                      {@link K} or null if the start of the sub map should be unrestricted
         * @param fromInclusive True, if the {@code fromKey} should be included in the sub map, false otherwise
         * @param toEnd         True, if the sub map should reach until the last entry of the backing trie, false
         *                      otherwise
         * @param toKey         The key of the entry, the sub map should reach to, as an instance of the generic type
         *                      {@link K} or null if the end of the sub map should be unrestricted
         * @param toInclusive   True, if the {@code toKey} should be included in the sub map, false otherwise
         */
        DescendingSubMap(@NotNull final AbstractSortedTrie<K, V> trie, final boolean fromStart,
                         @Nullable final K fromKey, final boolean fromInclusive, final boolean toEnd,
                         @Nullable final K toKey, final boolean toInclusive) {
            super(trie, fromStart, fromKey, fromInclusive, toEnd, toKey, toInclusive);
            this.reverseComparator = trie.comparator() != null ? Collections.reverseOrder(trie.comparator()) : null;
        }

        @Override
        public Comparator<? super K> comparator() {
            return reverseComparator;
        }

        @Override
        public NavigableMap<K, V> subMap(final K fromKey, final boolean fromInclusive, final K toKey,
                                         final boolean toInclusive) {
            ensureTrue(isInRange(fromKey, fromInclusive), "fromKey out of range");
            ensureTrue(isInRange(toKey, toInclusive), "fromKey out of range");
            return new DescendingSubMap<>(trie, false, toKey, toInclusive, false, fromKey, fromInclusive);
        }

        @Override
        public NavigableMap<K, V> headMap(final K to, final boolean inclusive) {
            ensureTrue(isInRange(to, inclusive), "Key out of range");
            return new DescendingSubMap<>(trie, false, to, inclusive, toEnd, toKey, toInclusive);
        }

        @Override
        public NavigableMap<K, V> tailMap(final K from, final boolean inclusive) {
            ensureTrue(isInRange(from, inclusive), "Key out of range");
            return new DescendingSubMap<>(trie, fromStart, fromKey, fromInclusive, false, from, inclusive);
        }

        @Override
        public NavigableMap<K, V> descendingMap() {
            return new AscendingSubMap<>(trie, fromStart, fromKey, fromInclusive, toEnd, toKey, toInclusive);
        }

        @NotNull
        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            return new DescendingSubSet();
        }

        @Override
        protected Map.Entry<K, V> onGetLowestEntry() {
            return getHighestEntry();
        }

        @Override
        protected Map.Entry<K, V> onGetHighestEntry() {
            return getLowestEntry();
        }

        @Override
        protected Map.Entry<K, V> onGetCeilingEntry(@Nullable final K key) {
            return getFloorEntry(key);
        }

        @Override
        protected Map.Entry<K, V> onGetHigherEntry(@Nullable final K key) {
            return getLowerEntry(key);
        }

        @Override
        protected Map.Entry<K, V> onGetFloorEntry(@Nullable final K key) {
            return getCeilingEntry(key);
        }

        @Override
        protected Map.Entry<K, V> onGetLowerEntry(@Nullable final K key) {
            return getHigherEntry(key);
        }

        @NotNull
        @Override
        protected Iterator<K> keyIterator() {
            return new KeyIteratorWrapper<>(
                    new DescendingSubMapEntryIterator<>(trie, getHighestEntry(), getLowFence()));
        }

        @NotNull
        @Override
        protected Iterator<K> descendingKeyIterator() {
            return new KeyIteratorWrapper<>(new AscendingSubMapEntryIterator<>(trie, getLowestEntry(), getHighFence()));
        }

        @NotNull
        @Override
        protected Spliterator<K> keySpliterator() {
            return new SpliteratorWrapper<>(trie, new KeyIteratorWrapper<>(
                    new DescendingSubMapEntryIterator<>(trie, getHighestEntry(), getLowFence())));
        }

    }

    /**
     * An iterator, which allows to iterate the keys of a trie in ascending order.
     *
     * @param <K> The type of the sequences, which are used as the trie's keys
     * @param <V> The type of the values, which are stored by the trie
     */
    private static final class AscendingKeyIterator<K extends Sequence, V> extends AbstractEntryIterator<K, V, K> {

        /**
         * Creates a new iterator, which allows to iterate the keys of the a trie in ascending order.
         *
         * @param trie  The trie, whose keys should be iterated, as an instance of the class {@link AbstractSortedTrie}.
         *              The trie may not be null
         * @param first The first entry to be iterated as an instance of the type {@link Map.Entry} or null, if the trie
         *              is empty
         */
        AscendingKeyIterator(@NotNull final AbstractSortedTrie<K, V> trie, @Nullable final Map.Entry<K, V> first) {
            super(trie, first);
        }

        @Override
        public K next() {
            ensureEqual(expectedModificationCount, trie.modificationCount, null, ConcurrentModificationException.class);
            ensureNotNull(next, null, NoSuchElementException.class);
            Map.Entry<K, V> entry = next;
            next = trie.higherEntry(entry.getKey());
            lastReturned = entry;
            return entry.getKey();
        }

    }

    /**
     * An iterator, which allows to iterate the keys of a trie in descending order.
     *
     * @param <K> The type of the sequences, which are used as the trie's keys
     * @param <V> The type of the values, which are stored by the trie
     */
    private static final class DescendingKeyIterator<K extends Sequence, V> extends AbstractEntryIterator<K, V, K> {

        /**
         * Creates a new iterator, which allows to iterate the keys of the a trie in descending order.
         *
         * @param trie  The trie, whose keys should be iterated, as an instance of the class {@link AbstractSortedTrie}.
         *              The trie may not be null
         * @param first The first entry to be iterated as an instance of the type {@link Map.Entry} or null, if the trie
         *              is empty
         */
        DescendingKeyIterator(@NotNull final AbstractSortedTrie<K, V> trie, @Nullable final Map.Entry<K, V> first) {
            super(trie, first);
        }

        @Override
        public K next() {
            ensureNotNull(next, null, NoSuchElementException.class);
            ensureEqual(expectedModificationCount, trie.modificationCount, null, ConcurrentModificationException.class);
            Map.Entry<K, V> entry = next;
            next = trie.lowerEntry(entry.getKey());
            lastReturned = entry;
            return entry.getKey();
        }

    }

    /**
     * An abstract base class for all iterators, which allow to iterate the entries of a {@link SortedTrie}.
     *
     * @param <K> The type of the sequences, which are used as the trie's keys
     * @param <V> The type of the values, which are stored by the trie
     * @param <T> The type of the iterated items
     */
    private static abstract class AbstractEntryIterator<K extends Sequence, V, T>
            extends AbstractIterator<K, V, T, AbstractSortedTrie<K, V>> {

        /**
         * The next item to be returned.
         */
        Map.Entry<K, V> next;

        /**
         * The item, which was returned the last time when the method {@link #next()} was called.
         */
        Map.Entry<K, V> lastReturned;

        /**
         * Creates a new iterator, which allows to iterate the entries of a {@link SortedTrie} starting at a specific
         * entry.
         *
         * @param trie  The trie, whose entries should be iterated, as an instance of the class {@link
         *              AbstractSortedTrie}. The trie may not be null
         * @param first The first entry to be iterated as an instance of the type {@link Map.Entry} or null, if the trie
         *              is empty
         */
        AbstractEntryIterator(@NotNull final AbstractSortedTrie<K, V> trie, @Nullable final Map.Entry<K, V> first) {
            super(trie);
            this.next = first;
            this.lastReturned = null;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public final void remove() {
            ensureNotNull(lastReturned, null, IllegalStateException.class);
            ensureEqual(expectedModificationCount, trie.modificationCount, null, ConcurrentModificationException.class);
            trie.remove(lastReturned.getKey());
            expectedModificationCount = trie.modificationCount;
            lastReturned = null;
        }

    }

    /**
     * A wrapper, which encapsulates an iterator in order to implement the abstract class {@link
     * AbstractKeySpliterator}.
     *
     * @param <K> The type of the sequences, which are used as the trie's keys
     * @param <V> The type of the values, which are stored by the trie
     */
    private static final class SpliteratorWrapper<K extends Sequence, V> extends AbstractKeySpliterator<K, V> {

        /**
         * The encapsulated iterator.
         */
        private final Iterator<K> iterator;

        /**
         * Creates a new wrapper, which encapsulates an iterator in order to implement the abstract class {@link
         * AbstractKeySpliterator}.
         *
         * @param trie     The trie, the spliterator should operate on, as an instance of the class {@link
         *                 AbstractSortedTrie}. The trie may not be null
         * @param iterator The iterator, which should be encapsulated, as an instance of the type {@link Iterator}. The
         *                 iterator may not be null
         */
        SpliteratorWrapper(@NotNull final AbstractSortedTrie<K, V> trie, @NotNull final Iterator<K> iterator) {
            super(trie);
            ensureNotNull(iterator, "The iterator may not be null");
            this.iterator = iterator;
        }

        @Override
        public boolean tryAdvance(final Consumer<? super K> action) {
            if (iterator.hasNext()) {
                action.accept(iterator.next());
                return true;
            }

            return false;
        }

    }

    /**
     * An abstract base class for all spliterators, which operate on the keys tries.
     *
     * @param <K> The type of the sequences, which are used as the trie's keys
     * @param <V> The type of the values, which are stored by the trie
     */
    private abstract static class AbstractKeySpliterator<K extends Sequence, V> implements Spliterator<K> {

        /**
         * The trie, which is traversed by the spliterator.
         */
        final AbstractSortedTrie<K, V> trie;

        /**
         * The modification count of the {@link #trie} when the spliterator was instantiated.
         */
        long expectedModificationCount;

        /**
         * Creates a new spliterator, which operates on the keys of a trie.
         *
         * @param trie The trie, the spliterator should operate on, as an instance of the class {@link
         *             AbstractSortedTrie}. The trie may not be null
         */
        AbstractKeySpliterator(@NotNull final AbstractSortedTrie<K, V> trie) {
            ensureNotNull(trie, "The trie may not be null");
            this.trie = trie;
            this.expectedModificationCount = trie.modificationCount;
        }

        @Override
        public final void forEachRemaining(final Consumer<? super K> action) {
            boolean hasNext = true;

            while (hasNext) {
                hasNext = tryAdvance(action);
            }
        }

        @Override
        public long estimateSize() {
            return trie.size();
        }

        @Override
        public Spliterator<K> trySplit() {
            return null;
        }

        @Override
        public final Comparator<? super K> getComparator() {
            return trie.comparator();
        }

        @Override
        public int characteristics() {
            return Spliterator.DISTINCT | Spliterator.ORDERED | Spliterator.SIZED;
        }

    }

    /**
     * A spliterator, which traverses the keys of a trie in ascending order.
     *
     * @param <K> The type of the sequences, which are used as the trie's keys
     * @param <V> The type of the values, which are stored by the trie
     */
    private static final class AscendingKeySpliterator<K extends Sequence, V> extends AbstractKeySpliterator<K, V>
            implements Spliterator<K> {

        /**
         * The next entry to be returned.
         */
        private Map.Entry<K, V> next;

        /**
         * Creates a new spliterator, which traverses the keys of a trie in ascending order.
         *
         * @param trie The trie, whose keys should be traversed, as an instance of the class {@link AbstractSortedTrie}.
         *             The trie may not be null
         */
        AscendingKeySpliterator(@NotNull final AbstractSortedTrie<K, V> trie) {
            super(trie);
            this.next = trie.firstEntry();

        }

        @Override
        public boolean tryAdvance(final Consumer<? super K> action) {
            ensureNotNull(action, null, NullPointerException.class);
            ensureEqual(expectedModificationCount, trie.modificationCount, null, ConcurrentModificationException.class);

            if (next != null) {
                Map.Entry<K, V> entry = next;
                next = trie.higherEntry(entry.getKey());
                action.accept(entry.getKey());
                return true;
            }

            return false;
        }

        @Override
        public int characteristics() {
            return super.characteristics() | Spliterator.SORTED;
        }

    }

    /**
     * A spliterator, which traverses the keys of a trie in descending order.
     *
     * @param <K> The type of the sequences, which are used as the trie's keys
     * @param <V> The type of the values, which are stored by the trie
     */
    private static final class DescendingKeySpliterator<K extends Sequence, V> extends AbstractKeySpliterator<K, V>
            implements Spliterator<K> {

        /**
         * The next entry to be returned.
         */
        private Map.Entry<K, V> next;

        /**
         * Creates a new spliterator, which traverses the keys of a trie in descending order.
         *
         * @param trie The trie, whose keys should be traversed, as an instance of the class {@link AbstractSortedTrie}.
         *             The trie may not be null
         */
        DescendingKeySpliterator(@NotNull final AbstractSortedTrie<K, V> trie) {
            super(trie);
            this.next = trie.lastEntry();
        }

        @Override
        public boolean tryAdvance(final Consumer<? super K> action) {
            ensureNotNull(action, null, NullPointerException.class);
            ensureEqual(expectedModificationCount, trie.modificationCount, null, ConcurrentModificationException.class);

            if (next != null) {
                Map.Entry<K, V> entry = next;
                next = trie.lowerEntry(entry.getKey());
                action.accept(entry.getKey());
                return true;
            }

            return false;
        }

    }

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = 4126112554524328807L;

    /**
     * The comparator, which is used to compare sequences to each other, or null, if the natural order of the sequences
     * is used.
     */
    protected final Comparator<? super SequenceType> comparator;

    /**
     * The navigable key set of the trie (see {@link #navigableKeySet()}).
     */
    private transient NavigableSet<SequenceType> navigableKeySet;

    /**
     * The descending map of the trie (see {@link #descendingMap()}).
     */
    private transient NavigableMap<SequenceType, ValueType> descendingMap;

    /**
     * Returns the entry, which corresponds to the first or last entry of the trie.
     *
     * @param first True, if the first entry should be returned, false, if the last entry should be returned
     * @return The entry, which corresponds to the first or last entry of the trie, depending on the given {@code first}
     * argument, or null, if the trie is empty
     */
    @Nullable
    private Entry<SequenceType, ValueType> firstOrLastEntry(final boolean first) {
        return firstOrLastEntry(rootNode, null, first);
    }

    /**
     * Returns the entry, which corresponds to the first or last entry of a subtrie.
     *
     * @param node  The root node of the subtrie as an instance of the type {@link Node} or null, if the subtrie is
     *              empty
     * @param key   The key of the subtrie's root node as an instance of the generic type {@link SequenceType} or null,
     *              if the subtrie is empty
     * @param first True, if the first entry should be returned, false, if the last entry should be returned
     * @return The entry, which corresponds to the first or last entry of the given subtrie, depending on the given
     * {@code first} argument, or null, if the trie is empty
     */
    @Nullable
    private Entry<SequenceType, ValueType> firstOrLastEntry(@Nullable final Node<SequenceType, ValueType> node,
                                                            @Nullable final SequenceType key, final boolean first) {
        Node<SequenceType, ValueType> currentNode = node;
        SequenceType sequence = key;

        while (currentNode != null && (!first || !currentNode.isValueSet()) && currentNode.hasSuccessors()) {
            SequenceType successorKey = first ? currentNode.getFirstSuccessorKey() : currentNode.getLastSuccessorKey();
            currentNode = first ? currentNode.getFirstSuccessor() : currentNode.getLastSuccessor();
            sequence = SequenceUtil.concat(sequence, successorKey);
        }

        if (currentNode != null && currentNode.isValueSet()) {
            return new AbstractMap.SimpleImmutableEntry<>(sequence, currentNode.getValue());
        }

        return null;
    }

    /**
     * Polls the first or last entry of the trie.
     *
     * @param first True, if the first entry should be polled, false, if the last entry should be polled
     * @return The entry, which has been polled, as an instance of the type {@link Entry} or null, if the trie is empty
     */
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

                while (currentNode != null && (!currentNode.isValueSet() || (!first && currentNode.hasSuccessors()))) {
                    SequenceType key = first ? currentNode.getFirstSuccessorKey() : currentNode.getLastSuccessorKey();

                    if (currentNode.getSuccessorCount() > 1 || currentNode.getValue() != null) {
                        lastRetainedNode = currentNode;
                        suffixToRemove = key;
                    }

                    currentNode = first ? currentNode.getFirstSuccessor() : currentNode.getLastSuccessor();
                    sequence = SequenceUtil.concat(sequence, key);
                }

                if (currentNode != null && currentNode.isValueSet()) {
                    if (currentNode.hasSuccessors()) {
                        lastRetainedNode = null;
                        suffixToRemove = null;
                    }

                    result = new AbstractMap.SimpleImmutableEntry<>(sequence, currentNode.getValue());
                    currentNode.setNodeValue(null);

                    if (lastRetainedNode == rootNode) {
                        clear();
                    } else {
                        if (suffixToRemove != null) {
                            onRemoveSuccessor(lastRetainedNode, suffixToRemove);
                        }

                        modificationCount++;
                    }
                }
            }
        }

        return result;
    }

    /**
     * Returns a stack, which contains all nodes, which must be traversed in order to reach the node, which corresponds
     * to a specific key.
     *
     * @param key The key of the node, the path should be retrieved for, as an instance of the generic type {@link
     *            SequenceType}
     * @return A stack, which contains all nodes, which must be traversed in order to reach the node, which corresponds
     * to the given key, as an instance of the type {@link Deque} or null, if no node with the given key is available
     */
    @Nullable
    private Deque<Pair<Node<SequenceType, ValueType>, SequenceType>> getPathToNode(final SequenceType key) {
        if (rootNode != null) {
            Deque<Pair<Node<SequenceType, ValueType>, SequenceType>> stack = new LinkedList<>();
            Node<SequenceType, ValueType> currentNode = rootNode;
            SequenceType suffix = key;

            while (suffix != null && !suffix.isEmpty()) {
                Pair<Node<SequenceType, ValueType>, SequenceType> pair =
                        onGetSuccessor(currentNode, suffix, Operation.GET);

                if (pair == null) {
                    return null;
                } else {
                    stack.push(Pair.create(currentNode, suffix));
                    currentNode = pair.first;
                    suffix = pair.second;
                }
            }

            if (currentNode != null) {
                stack.push(Pair.create(currentNode, null));
                return stack;
            }
        }

        return null;
    }

    /**
     * Creates and returns an entry, given a specific node, the sequence one of its successors corresponds to and the
     * key, which is used by the predecessor to reference the node.
     *
     * @param predecessorSequence The sequence, which corresponds to one of the given node's successors as an instance
     *                            of the generic type {@link SequenceType}. The sequence may not be null
     * @param node                The node, the entry should be created from, as an instance of the type {@link Node}.
     *                            The node may not be null
     * @param key                 The key, which is used by the predecessor to reference the given node, as an instance
     *                            of the generic type {@link SequenceType}. The key may not be null
     * @return The entry, which has been created, as an instance of the type {@link Entry}. The entry may not be null
     */
    @NotNull
    private Map.Entry<SequenceType, ValueType> createLowerEntry(@Nullable final SequenceType predecessorSequence,
                                                                @NotNull final Node<SequenceType, ValueType> node,
                                                                @NotNull final SequenceType key) {
        SequenceType lowerKey = predecessorSequence != null ?
                SequenceUtil.subsequence(predecessorSequence, 0, predecessorSequence.length() - key.length()) : null;
        return new AbstractMap.SimpleImmutableEntry<>(SequenceUtil.isEmpty(lowerKey) ? null : lowerKey,
                node.getValue());
    }

    /**
     * Returns the lower or higher entry, starting at a specific node.
     *
     * @param predecessorSequence The sequence, which corresponds to a predecessor of the given node, as an instance of
     *                            the generic type {@link SequenceType}. The
     * @param indexPair           A pair, which contains the index, the given node is stored at in its predecessor, as
     *                            an instance of the class {@link Pair}. See {@link #indexOf(Node, Sequence)}
     * @param node                The node to start at, as an instance of the type {@link Node}. The node may not be
     *                            null
     * @param key                 The key, which is used by the predecessor to reference the given node, as an instance
     *                            of the generic type {@link SequenceType}. The key may not be null
     * @param indexFunction       A function, which returns the index of the node, which should be used to search for
     *                            the lower or higher entry, depending on the index contained by {@code indexPair}
     * @param higher              True, if the higher entry should be created, false, if the lower entry should be
     *                            created
     * @return The entry, which has been created, as an instance of the type {@link Map.Entry} or null, if no such entry
     * is available
     */
    @Nullable
    private Map.Entry<SequenceType, ValueType> createLowerOrHigherEntry(@NotNull final SequenceType predecessorSequence,
                                                                        @Nullable
                                                                        final Pair<Integer, SequenceType> indexPair,
                                                                        @NotNull
                                                                        final Node<SequenceType, ValueType> node,
                                                                        @NotNull final SequenceType key, @NotNull
                                                                        final Function<Integer, Integer> indexFunction,
                                                                        final boolean higher) {
        if (indexPair != null) {
            int index = indexFunction.apply(indexPair.first);

            if (index >= 0 && index < node.getSuccessorCount()) {
                Node<SequenceType, ValueType> successor = node.getSuccessor(index);
                SequenceType successorKey = node.getSuccessorKey(index);
                SequenceType prefix =
                        SequenceUtil.subsequence(predecessorSequence, 0, predecessorSequence.length() - key.length());
                prefix = SequenceUtil.concat(prefix, successorKey);
                return firstOrLastEntry(successor, prefix, higher);
            }
        }

        return null;
    }

    /**
     * Returns an iterator, which allows to iterate the keys of the trie.
     *
     * @return An iterator, which allows to iterate the keys of the trie, as an instance of the type {@link Iterator}.
     * The iterator may not be null
     */
    @NotNull
    final Iterator<SequenceType> keyIterator() {
        return new AscendingKeyIterator<>(this, firstEntry());
    }

    /**
     * Returns an iterator, which allows to iterate the keys of the trie in descending order.
     *
     * @return An iterator, which allows to iterate the keys of the trie in descending order, as an instance of the type
     * {@link Iterator}. The iterator may not be null
     */
    @NotNull
    final Iterator<SequenceType> descendingKeyIterator() {
        return new DescendingKeyIterator<>(this, lastEntry());
    }

    /**
     * Returns a spliterator, which allows to iterate the keys of the trie.
     *
     * @return A spliterator, which allows to iterate the keys of the trie, as an instance of the type {@link
     * Spliterator}. The spliterator may not be null
     */
    @NotNull
    final Spliterator<SequenceType> keySpliterator() {
        return new AscendingKeySpliterator<>(this);
    }

    /**
     * Returns a spliterator, which allows to iterate the keys of the trie in descending order.
     *
     * @return A spliterator, which allows to iterate the keys of the trie in descending order, as an instance of the
     * type {@link Spliterator}. The spliterator may not be null
     */
    @NotNull
    final Spliterator<SequenceType> descendingKeySpliterator() {
        return new DescendingKeySpliterator<>(this);
    }

    /**
     * The method, which is invoked on subclasses in order to identify the index of a node's successor, which
     * corresponds to a specific sequence.
     *
     * @param node     The node, whose successors should be checked, as an instance of the type {@link Node}. The node
     *                 may not be null
     * @param sequence The sequence, the successor, whose index should be returned, corresponds to, as an {@link
     *                 Integer} value
     * @return The index of the successor, which corresponds to the given sequence, as an {@link Integer} value or -1,
     * if no such successor is available for the given node
     */
    @Nullable
    protected abstract Pair<Integer, SequenceType> indexOf(@NotNull final Node<SequenceType, ValueType> node,
                                                           @NotNull final SequenceType sequence);

    /**
     * Creates a new sorted trie.
     *
     * @param rootNode   The root node of the trie as an instance of the type {@link Node} or null, if the trie should
     *                   be empty
     * @param comparator The comparator, which should be used to compare keys with each other, as an instance of the
     *                   type {@link Comparator} or null, if the natural ordering of the keys should be used
     */
    protected AbstractSortedTrie(@Nullable final Node<SequenceType, ValueType> rootNode,
                                 @Nullable final Comparator<? super SequenceType> comparator) {
        super(rootNode);
        this.comparator = comparator;
    }

    /**
     * Creates a new empty, sorted trie.
     *
     * @param comparator The comparator, which should be used to compare keys with each other, as an instance of the
     *                   type {@link Comparator} or null, if the natural ordering of the keys should be used
     */
    public AbstractSortedTrie(@Nullable final Comparator<? super SequenceType> comparator) {
        this(null, comparator);
    }

    /**
     * Creates a new sorted trie, which contains all key-value pairs that are contained in a map.
     *
     * @param comparator The comparator, which should be used to compare keys with each other, as an instance of the
     *                   type {@link Comparator} or null, if the natural ordering of the keys should be used
     * @param map        The map, which contains the key-value pairs that should be added to the trie, as an instance of
     *                   the type {@link Map}. The map may not be null
     */
    public AbstractSortedTrie(@Nullable final Comparator<? super SequenceType> comparator,
                              @NotNull final Map<SequenceType, ValueType> map) {
        this(comparator);
        putAll(map);
    }

    @Override
    public final Comparator<? super SequenceType> comparator() {
        return comparator;
    }

    @Override
    public final SequenceType lowerKey(final SequenceType key) {
        return EntryUtil.getKey(lowerEntry(key));
    }

    @Override
    public final SequenceType higherKey(final SequenceType key) {
        return EntryUtil.getKey(higherEntry(key));
    }

    @Override
    public final SequenceType floorKey(final SequenceType key) {
        return EntryUtil.getKey(floorEntry(key));
    }

    @Override
    public final SequenceType ceilingKey(final SequenceType key) {
        return EntryUtil.getKey(ceilingEntry(key));
    }

    @Override
    public final SequenceType firstKey() {
        return EntryUtil.getKeyOrThrowException(firstEntry());
    }

    @Override
    public final SequenceType lastKey() {
        return EntryUtil.getKeyOrThrowException(lastEntry());
    }

    @Override
    public final Entry<SequenceType, ValueType> lowerEntry(final SequenceType key) {
        Deque<Pair<Node<SequenceType, ValueType>, SequenceType>> stack = getPathToNode(key);

        if (stack != null) {
            stack.pop();

            while (!stack.isEmpty()) {
                Pair<Node<SequenceType, ValueType>, SequenceType> pair = stack.pop();
                Pair<Integer, SequenceType> indexPair = null;

                if (pair.first.isValueSet()) {
                    if (pair.first.getSuccessorCount() <= 1) {
                        return createLowerEntry(key, pair.first, pair.second);
                    } else {
                        indexPair = indexOf(pair.first, pair.second);

                        if (indexPair != null && indexPair.first == 0) {
                            return createLowerEntry(key, pair.first, pair.second);
                        }
                    }
                }

                if (indexPair == null && pair.first.getSuccessorCount() > 1) {
                    indexPair = indexOf(pair.first, pair.second);
                }

                Map.Entry<SequenceType, ValueType> entry =
                        createLowerOrHigherEntry(key, indexPair, pair.first, pair.second, index -> index - 1, false);

                if (entry != null) {
                    return entry;
                }
            }
        }

        return null;
    }

    @Override
    public final Entry<SequenceType, ValueType> higherEntry(final SequenceType key) {
        Deque<Pair<Node<SequenceType, ValueType>, SequenceType>> stack = getPathToNode(key);

        if (stack != null) {
            Node<SequenceType, ValueType> node = stack.pop().first;

            if (node.hasSuccessors()) {
                Node<SequenceType, ValueType> successor = node.getFirstSuccessor();
                SequenceType successorKey = node.getFirstSuccessorKey();
                return firstOrLastEntry(successor, SequenceUtil.concat(key, successorKey), true);
            } else {
                while (!stack.isEmpty()) {
                    Pair<Node<SequenceType, ValueType>, SequenceType> pair = stack.pop();

                    if (pair.first.getSuccessorCount() > 1) {
                        Pair<Integer, SequenceType> indexPair = indexOf(pair.first, pair.second);
                        Entry<SequenceType, ValueType> entry =
                                createLowerOrHigherEntry(key, indexPair, pair.first, pair.second, index -> index + 1,
                                        true);

                        if (entry != null) {
                            return entry;
                        }
                    }
                }
            }
        }

        return null;
    }

    @Override
    public final Entry<SequenceType, ValueType> floorEntry(final SequenceType key) {
        Node<SequenceType, ValueType> node = getNode(key);
        return node != null ? new AbstractMap.SimpleImmutableEntry<>(key, node.getValue()) : null;
    }

    @Override
    public final Entry<SequenceType, ValueType> ceilingEntry(final SequenceType key) {
        Node<SequenceType, ValueType> node = getNode(key);
        return node != null ? new AbstractMap.SimpleImmutableEntry<>(key, node.getValue()) : null;
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
    public final SortedMap<SequenceType, ValueType> subMap(final SequenceType fromKey, final SequenceType toKey) {
        return subMap(fromKey, true, toKey, false);
    }

    @NotNull
    @Override
    public final NavigableMap<SequenceType, ValueType> subMap(final SequenceType fromKey, final boolean fromInclusive,
                                                              final SequenceType toKey, final boolean toInclusive) {
        return new AscendingSubMap<>(this, false, fromKey, fromInclusive, false, toKey, toInclusive);
    }

    @NotNull
    @Override
    public final SortedMap<SequenceType, ValueType> headMap(final SequenceType toKey) {
        return headMap(toKey, false);
    }

    @NotNull
    @Override
    public final NavigableMap<SequenceType, ValueType> headMap(final SequenceType toKey, final boolean inclusive) {
        return new AscendingSubMap<>(this, true, null, true, false, toKey, inclusive);
    }

    @NotNull
    @Override
    public final SortedMap<SequenceType, ValueType> tailMap(final SequenceType fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public final NavigableMap<SequenceType, ValueType> tailMap(final SequenceType fromKey, final boolean inclusive) {
        return new AscendingSubMap<>(this, false, fromKey, inclusive, true, null, true);
    }

    @NotNull
    @Override
    public final NavigableMap<SequenceType, ValueType> descendingMap() {
        if (this.descendingMap == null) {
            this.descendingMap = new DescendingSubMap<>(this, true, null, true, true, null, true);
        }

        return this.descendingMap;
    }

    @Override
    public final NavigableSet<SequenceType> navigableKeySet() {
        if (this.navigableKeySet == null) {
            this.navigableKeySet = new NavigableKeySet<>(this);
        }

        return navigableKeySet;
    }

    @Override
    public final NavigableSet<SequenceType> descendingKeySet() {
        return descendingMap().navigableKeySet();
    }

}
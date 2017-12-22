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
import de.mrapp.tries.util.EntryUtil;
import de.mrapp.tries.util.SequenceUtil;
import de.mrapp.util.datastructure.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

import static de.mrapp.util.Condition.*;

public abstract class AbstractSortedTrie<SequenceType extends Sequence, ValueType> extends
        AbstractTrie<SequenceType, ValueType> implements SortedTrie<SequenceType, ValueType> {

    private static class KeySet<K extends Sequence> extends AbstractSet<K> implements
            NavigableSet<K> {

        private final NavigableMap<K, ?> map;

        KeySet(@NotNull final NavigableMap<K, ?> map) {
            ensureNotNull(map, "The map may not be null");
            this.map = map;
        }

        @NotNull
        @Override
        public Iterator<K> iterator() {
            // TODO
            return null;
        }

        @NotNull
        @Override
        public Iterator<K> descendingIterator() {
            // TODO
            return null;
        }

        @Override
        public int size() {
            return map.size();
        }

        @Override
        public boolean isEmpty() {
            return map.isEmpty();
        }

        @SuppressWarnings("SuspiciousMethodCalls")
        @Override
        public boolean contains(final Object o) {
            return map.containsKey(o);
        }

        @Override
        public void clear() {
            map.clear();
        }

        @Override
        public K lower(final K e) {
            return map.lowerKey(e);
        }

        @Override
        public K floor(final K e) {
            return map.floorKey(e);
        }

        @Override
        public K ceiling(final K e) {
            return map.ceilingKey(e);
        }

        @Override
        public K higher(final K e) {
            return map.higherKey(e);
        }

        @Override
        public K first() {
            return map.firstKey();
        }

        @Override
        public K last() {
            return map.lastKey();
        }

        @Override
        public Comparator<? super K> comparator() {
            return map.comparator();
        }

        @Override
        public K pollFirst() {
            return EntryUtil.getKey(map.pollFirstEntry());
        }

        @Override
        public K pollLast() {
            return EntryUtil.getKey(map.pollLastEntry());
        }

        @Override
        public boolean remove(final Object o) {
            int oldSize = size();
            map.remove(o);
            return size() != oldSize;
        }

        @NotNull
        @Override
        public NavigableSet<K> subSet(final K fromElement, final boolean fromInclusive,
                                      final K toElement, final boolean toInclusive) {
            return new KeySet<>(map.subMap(fromElement, fromInclusive, toElement, toInclusive));
        }

        @NotNull
        @Override
        public NavigableSet<K> headSet(final K toElement, final boolean inclusive) {
            return new KeySet<>(map.headMap(toElement, inclusive));
        }

        @NotNull
        @Override
        public NavigableSet<K> tailSet(final K fromElement, final boolean inclusive) {
            return new KeySet<>(map.tailMap(fromElement, inclusive));
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
            return new KeySet<>(map.descendingMap());
        }

        @Override
        public Spliterator<K> spliterator() {
            // TODO
            return null;
        }

    }

    private abstract static class AbstractSubMap<K extends Sequence, V> extends
            AbstractMap<K, V> implements NavigableMap<K, V>, Serializable {

        protected abstract class AbstractEntrySet extends AbstractSet<Map.Entry<K, V>> {

            private transient int size = -1;

            private transient long sizeModificationCount;

            @SuppressWarnings("unchecked")
            private K getKey(final Object o) {
                if (!(o instanceof Map.Entry))
                    return null;
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
                K key = (K) entry.getKey();
                if (!isInRange(key))
                    return null;
                Node<K, V> node = trie.getNode(key);
                if (node == null)
                    return null;
                if (node.getValue() == null) {
                    if (entry.getValue() != null)
                        return null;
                } else if (!node.getValue().equals(entry.getValue()))
                    return null;
                return key;
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
            public boolean remove(Object o) {
                Optional<K> optional = Optional.ofNullable(getKey(o));
                optional.ifPresent(trie::remove);
                return optional.isPresent();
            }

        }

        protected abstract class AbstractIterator implements Iterator<Map.Entry<K, V>> {

            final Object fenceKey;
            long modificationCount;
            Map.Entry<K, V> lastReturned;
            Map.Entry<K, V> next;

            AbstractIterator(@Nullable final Map.Entry<K, V> first,
                             @Nullable final Map.Entry<K, V> fence) {
                this.fenceKey = fence != null ? fence.getKey() : UNBOUND_KEY;
                this.modificationCount = trie.modificationCount;
                this.next = first;
                this.lastReturned = null;
            }

            @Override
            public final boolean hasNext() {
                return next != null && !fenceKey.equals(next.getKey());
            }

            @Override
            public final void remove() {
                ensureNotNull(lastReturned, null, IllegalStateException.class);
                ensureEqual(modificationCount, trie.modificationCount, null,
                        ConcurrentModificationException.class);
                trie.remove(lastReturned.getKey());
                lastReturned = null;
                modificationCount = trie.modificationCount;
            }

        }

        /**
         * The constant serial version UID.
         */
        private static final long serialVersionUID = 2179020779953916850L;

        private static final Object UNBOUND_KEY = new Object();

        /**
         * The backing trie.
         */
        protected final AbstractSortedTrie<K, V> trie;

        private final Comparator<K> comparator;

        final K fromKey, toKey;
        final boolean fromStart, toEnd;
        final boolean fromInclusive, toInclusive;

        private boolean isTooLow(@Nullable final K key) {
            if (!fromStart) {
                int c = comparator.compare(key, fromKey);
                return c < 0 || (c == 0 && !fromInclusive);
            }

            return false;
        }

        private boolean isTooHigh(@Nullable final K key) {
            if (!toEnd) {
                int c = comparator.compare(key, toKey);
                return c > 0 || (c == 0 && !toInclusive);
            }

            return false;
        }

        private boolean isInRange(@Nullable final K key) {
            return !isTooLow(key) && !isTooHigh(key);
        }

        private boolean isInClosedRange(@Nullable final K key) {
            return (fromStart || comparator.compare(key, fromKey) >= 0) &&
                    (toEnd || comparator.compare(toKey, key) >= 0);
        }

        final boolean isInRange(@Nullable final K key, final boolean inclusive) {
            return inclusive ? isInRange(key) : isInClosedRange(key);
        }

        @Nullable
        final Map.Entry<K, V> getLowestEntry() {
            Map.Entry<K, V> entry =
                    (fromStart ? trie.firstEntry() :
                            (fromInclusive ? trie.ceilingEntry(fromKey) :
                                    trie.higherEntry(fromKey)));
            return (entry == null || isTooHigh(entry.getKey())) ? null : entry;
        }

        @Nullable
        final Map.Entry<K, V> getHighestEntry() {
            Map.Entry<K, V> entry =
                    (toEnd ? trie.lastEntry() :
                            (toInclusive ? trie.floorEntry(toKey) :
                                    trie.lowerEntry(toKey)));
            return (entry == null || isTooLow(entry.getKey())) ? null : entry;
        }

        @Nullable
        final Map.Entry<K, V> getCeilingEntry(@Nullable final K key) {
            if (isTooLow(key)) {
                return getLowestEntry();
            }

            Map.Entry<K, V> entry = trie.ceilingEntry(key);
            return (entry == null || isTooHigh(entry.getKey())) ? null : entry;
        }

        @Nullable
        final Map.Entry<K, V> getHigherEntry(@Nullable final K key) {
            if (isTooLow(key)) {
                return getLowestEntry();
            }

            Map.Entry<K, V> entry = trie.higherEntry(key);
            return (entry == null || isTooHigh(entry.getKey())) ? null : entry;
        }

        @Nullable
        final Map.Entry<K, V> getFloorEntry(@Nullable final K key) {
            if (isTooHigh(key)) {
                return getHighestEntry();
            }

            Map.Entry<K, V> entry = trie.floorEntry(key);
            return (entry == null || isTooLow(entry.getKey())) ? null : entry;
        }

        @Nullable
        final Map.Entry<K, V> getLowerKey(@Nullable final K key) {
            if (isTooHigh(key)) {
                return getHighestEntry();
            }

            Map.Entry<K, V> entry = trie.lowerEntry(key);
            return (entry == null || isTooLow(entry.getKey())) ? null : entry;
        }

        /**
         * Returns the absolute high fence for ascending traversal
         */
        @Nullable
        final Map.Entry<K, V> getHighFence() {
            return (toEnd ? null :
                    (toInclusive ? trie.higherEntry(toKey) : trie.ceilingEntry(toKey)));
        }

        /**
         * Return the absolute low protected fence for descending traversal
         */
        @Nullable
        final Map.Entry<K, V> getLowFence() {
            return (fromStart ? null : (fromInclusive ? trie.lowerEntry(fromKey) : trie.floorEntry(
                    fromKey)));
        }

        protected abstract Map.Entry<K, V> onGetLowestEntry();

        protected abstract Map.Entry<K, V> onGetHighestEntry();

        protected abstract Map.Entry<K, V> onGetCeilingEntry(@Nullable final K key);

        protected abstract Map.Entry<K, V> onGetHigherEntry(@Nullable final K key);

        protected abstract Map.Entry<K, V> onGetFloorEntry(@Nullable final K key);

        protected abstract Map.Entry<K, V> onGetLowerEntry(@Nullable final K key);

        AbstractSubMap(@NotNull final AbstractSortedTrie<K, V> trie,
                       final boolean fromStart, @Nullable final K fromKey,
                       final boolean fromInclusive, final boolean toEnd, @Nullable final K toKey,
                       final boolean toInclusive) {
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
        public boolean isEmpty() {
            return (fromStart && toEnd) ? trie.isEmpty() : entrySet().isEmpty();
        }

        @Override
        public int size() {
            return (fromStart && toEnd) ? trie.size() : entrySet().size();
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean containsKey(final Object key) {
            return isInRange((K) key) && trie.containsKey(key);
        }

        @Override
        public V put(final K key, final V value) {
            ensureTrue(isInRange(key), "Key out of range");
            return trie.put(key, value);
        }

        @SuppressWarnings("unchecked")
        @Override
        public V get(final Object key) {
            return !isInRange((K) key) ? null : trie.get(key);
        }

        @SuppressWarnings("unchecked")
        @Override
        public V remove(final Object key) {
            return !isInRange((K) key) ? null : trie.remove(key);
        }

        @Override
        public Map.Entry<K, V> ceilingEntry(final K key) {
            return onGetCeilingEntry(key);
        }

        @Override
        public K ceilingKey(final K key) {
            return EntryUtil.getKey(onGetCeilingEntry(key));
        }

        @Override
        public Map.Entry<K, V> higherEntry(final K key) {
            return onGetHigherEntry(key);
        }

        @Override
        public K higherKey(final K key) {
            return EntryUtil.getKey(onGetHigherEntry(key));
        }

        @Override
        public Map.Entry<K, V> floorEntry(final K key) {
            return onGetFloorEntry(key);
        }

        @Override
        public K floorKey(final K key) {
            return EntryUtil.getKey(onGetFloorEntry(key));
        }

        @Override
        public Map.Entry<K, V> lowerEntry(final K key) {
            return onGetLowerEntry(key);
        }

        @Override
        public K lowerKey(final K key) {
            return EntryUtil.getKey(onGetLowerEntry(key));
        }

        @Override
        public K firstKey() {
            return EntryUtil.getKeyOrThrowException(onGetLowestEntry());
        }

        @Override
        public K lastKey() {
            return EntryUtil.getKeyOrThrowException(onGetHighestEntry());
        }

        @Override
        public Map.Entry<K, V> firstEntry() {
            return onGetLowestEntry();
        }

        @Override
        public Map.Entry<K, V> lastEntry() {
            return onGetHighestEntry();
        }

        @Override
        public Map.Entry<K, V> pollFirstEntry() {
            Map.Entry<K, V> entry = onGetLowestEntry();

            if (entry != null) {
                trie.remove(entry.getKey());
            }

            return entry;
        }

        @Override
        public Map.Entry<K, V> pollLastEntry() {
            Map.Entry<K, V> entry = onGetHighestEntry();

            if (entry != null) {
                trie.remove(entry.getKey());
            }

            return entry;
        }

        @NotNull
        @Override
        public Set<K> keySet() {
            return navigableKeySet();
        }

        @Override
        public NavigableSet<K> descendingKeySet() {
            return descendingMap().navigableKeySet();
        }

        @NotNull
        @Override
        public SortedMap<K, V> subMap(final K fromKey, final K toKey) {
            return subMap(fromKey, true, toKey, false);
        }

        @NotNull
        @Override
        public SortedMap<K, V> headMap(final K toKey) {
            return headMap(toKey, false);
        }

        @NotNull
        @Override
        public SortedMap<K, V> tailMap(final K fromKey) {
            return tailMap(fromKey, true);
        }

        @NotNull
        @Override
        public NavigableSet<K> navigableKeySet() {
            return new KeySet<>(this);
        }

    }

    private static class AscendingSubMap<K extends Sequence, V> extends AbstractSubMap<K, V> {

        private class EntrySet extends AbstractEntrySet {

            @NotNull
            @Override
            public Iterator<Entry<K, V>> iterator() {
                return new AbstractIterator(getLowestEntry(), getHighFence()) {

                    @Override
                    public Map.Entry<K, V> next() {
                        ensureEqual(modificationCount, trie.modificationCount, null,
                                ConcurrentModificationException.class);
                        ensureNotNull(next, null, NoSuchElementException.class);
                        ensureFalse(fenceKey.equals(next.getKey()), null,
                                NoSuchElementException.class);
                        Map.Entry<K, V> entry = next;
                        next = trie.higherEntry(entry.getKey());
                        lastReturned = entry;
                        return entry;
                    }

                };
            }

        }

        /**
         * The constant serial version UID.
         */
        private static final long serialVersionUID = 5937476665504869649L;

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
        public NavigableMap<K, V> subMap(final K fromKey, final boolean fromInclusive,
                                         final K toKey, final boolean toInclusive) {
            ensureTrue(isInRange(fromKey, fromInclusive), "fromKey out of range");
            ensureTrue(isInRange(toKey, toInclusive), "toKey out of range");
            return new AscendingSubMap<>(trie, false, fromKey, fromInclusive, false, toKey,
                    toInclusive);
        }

        @Override
        public NavigableMap<K, V> headMap(final K to, final boolean inclusive) {
            ensureTrue(isInRange(to, inclusive), "Key out of range");
            return new AscendingSubMap<>(trie, fromStart, fromKey, fromInclusive, false, to,
                    inclusive);
        }

        @Override
        public NavigableMap<K, V> tailMap(final K from, final boolean inclusive) {
            ensureTrue(isInRange(from, inclusive), "Key out of range");
            return new AscendingSubMap<>(trie, false, from, inclusive, toEnd, toKey, toInclusive);
        }

        @Override
        public NavigableMap<K, V> descendingMap() {
            return new DescendingSubMap<>(trie, fromStart, fromKey, fromInclusive, toEnd, toKey,
                    toInclusive);
        }

        @NotNull
        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            return new EntrySet();
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
            return getLowerKey(key);
        }

    }

    private static final class DescendingSubMap<K extends Sequence, V> extends
            AbstractSubMap<K, V> {

        private class EntrySet extends AbstractEntrySet {

            @NotNull
            @Override
            public Iterator<Map.Entry<K, V>> iterator() {
                return new AbstractIterator(getHighestEntry(), getLowFence()) {

                    @Override
                    public Entry<K, V> next() {
                        ensureEqual(modificationCount, trie.modificationCount, null,
                                ConcurrentModificationException.class);
                        ensureNotNull(next, null, NoSuchElementException.class);
                        ensureFalse(fenceKey.equals(next.getKey()), null,
                                NoSuchElementException.class);
                        Map.Entry<K, V> entry = next;
                        next = trie.lowerEntry(entry.getKey());
                        lastReturned = entry;
                        return entry;
                    }

                };
            }

        }

        /**
         * The constant serial version UID.
         */
        private static final long serialVersionUID = 912986545866120460L;

        private final Comparator<? super K> reverseComparator;

        DescendingSubMap(@NotNull final AbstractSortedTrie<K, V> trie,
                         final boolean fromStart, @Nullable final K fromKey,
                         final boolean fromInclusive, final boolean toEnd, @Nullable final K toKey,
                         final boolean toInclusive) {
            super(trie, fromStart, fromKey, fromInclusive, toEnd, toKey, toInclusive);
            this.reverseComparator =
                    trie.comparator() != null ? Collections.reverseOrder(trie.comparator()) : null;
        }

        @Override
        public Comparator<? super K> comparator() {
            return reverseComparator;
        }

        @Override
        public NavigableMap<K, V> subMap(final K fromKey, final boolean fromInclusive,
                                         final K toKey, final boolean toInclusive) {
            ensureTrue(isInRange(fromKey, fromInclusive), "fromKey out of range");
            ensureTrue(isInRange(toKey, toInclusive), "fromKey out of range");
            return new DescendingSubMap<>(trie, false, toKey, toInclusive, false, fromKey,
                    fromInclusive);
        }

        @Override
        public NavigableMap<K, V> headMap(final K to, final boolean inclusive) {
            ensureTrue(isInRange(to, inclusive), "Key out of range");
            return new DescendingSubMap<>(trie, false, to, inclusive, toEnd, toKey, toInclusive);
        }

        @Override
        public NavigableMap<K, V> tailMap(final K from, final boolean inclusive) {
            ensureTrue(isInRange(from, inclusive), "Key out of range");
            return new DescendingSubMap<>(trie, fromStart, fromKey, fromInclusive, false, from,
                    inclusive);
        }

        @Override
        public NavigableMap<K, V> descendingMap() {
            return new AscendingSubMap<>(trie, fromStart, fromKey, fromInclusive, toEnd, toKey,
                    toInclusive);
        }

        @NotNull
        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            return new EntrySet();
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
            return getLowerKey(key);
        }

        @Override
        protected Map.Entry<K, V> onGetFloorEntry(@Nullable final K key) {
            return getCeilingEntry(key);
        }

        @Override
        protected Map.Entry<K, V> onGetLowerEntry(@Nullable final K key) {
            return getHigherEntry(key);
        }

    }

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = 4126112554524328807L;

    /**
     * The comparator, which is used to compare sequences to each other, or null, if the natural
     * order of the sequences is used.
     */
    protected final Comparator<? super SequenceType> comparator;

    /**
     * Returns the entry, which corresponds to the first or last entry of the trie.
     *
     * @param first True, if the first entry should be returned, false, if the last entry should be
     *              returned
     * @return The entry, which corresponds to the first or last entry of the trie, depending on the
     * given {@code first} argument, or null, if the trie is empty
     */
    @Nullable
    private Entry<SequenceType, ValueType> firstOrLastEntry(final boolean first) {
        return firstOrLastEntry(rootNode, null, first);
    }

    /**
     * Returns the entry, which corresponds to the first or last entry of a subtrie.
     *
     * @param node  The root node of the subtrie as an instance of the type {@link Node} or null, if
     *              the subtrie is empty
     * @param key   The key of the subtrie's root node as an instance of the generic type {@link
     *              SequenceType} or null, if the subtrie is empty
     * @param first True, if the first entry should be returned, false, if the last entry should be
     *              returned
     * @return The entry, which corresponds to the first or last entry of the given subtrie,
     * depending on the given {@code first} argument, or null, if the trie is empty
     */
    @Nullable
    private Entry<SequenceType, ValueType> firstOrLastEntry(
            @Nullable final Node<SequenceType, ValueType> node,
            @Nullable final SequenceType key, final boolean first) {
        Node<SequenceType, ValueType> currentNode = node;
        SequenceType sequence = key;

        while (currentNode != null &&
                (!first || !currentNode.isValueSet()) && currentNode.hasSuccessors()) {
            SequenceType successorKey =
                    first ? currentNode.getFirstSuccessorKey() : currentNode.getLastSuccessorKey();
            currentNode = first ? currentNode.getFirstSuccessor() : currentNode.getLastSuccessor();
            sequence = SequenceUtil.concat(sequence, successorKey);
        }

        if (currentNode != null && currentNode.isValueSet()) {
            return new AbstractMap.SimpleImmutableEntry<>(sequence, currentNode.getValue());
        }

        return null;
    }

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

                while (currentNode != null && !currentNode.isValueSet()) {
                    SequenceType key = first ? currentNode.getFirstSuccessorKey() :
                            currentNode.getLastSuccessorKey();

                    if (currentNode.getSuccessorCount() > 1 || currentNode.getValue() != null) {
                        lastRetainedNode = currentNode;
                        suffixToRemove = key;
                    }

                    currentNode = first ? currentNode.getFirstSuccessor() :
                            currentNode.getLastSuccessor();
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

    /**
     * Returns a stack, which contains all nodes, which must be traversed in order to reach the
     * node, which corresponds to a specific key.
     *
     * @param key The key of the node, the path should be retrieved for, as an instance of the
     *            generic type {@link SequenceType}
     * @return A stack, which contains all nodes, which must be traversed in order to reach the
     * node, which corresponds to the given key, as an instance of the type {@link Deque} or null,
     * if no node with the given key is available
     */
    @Nullable
    private Deque<Pair<Node<SequenceType, ValueType>, SequenceType>> getPathToNode(
            final SequenceType key) {
        if (rootNode != null) {
            Deque<Pair<Node<SequenceType, ValueType>, SequenceType>> stack = new LinkedList<>();
            Node<SequenceType, ValueType> currentNode = rootNode;
            SequenceType suffix = key;
            stack.push(Pair.create(currentNode, suffix));

            while (suffix != null && !suffix.isEmpty()) {
                Pair<Node<SequenceType, ValueType>, SequenceType> pair = getSuccessor(currentNode,
                        suffix);

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

    @Nullable
    private Map.Entry<SequenceType, ValueType> getLowerOrHigherEntry(final SequenceType originalKey,
                                                                     final Node<SequenceType, ValueType> node,
                                                                     final SequenceType key,
                                                                     @NotNull final Function<Integer, Integer> indexFunction,
                                                                     final boolean higher) {
        if (node.getSuccessorCount() > 1) {
            Pair<Integer, SequenceType> indexPair = indexOf(node, key);

            if (indexPair != null) {
                int index = indexFunction.apply(indexPair.first);

                if (index >= 0 && index < node.getSuccessorCount()) {
                    Node<SequenceType, ValueType> successor = node.getSuccessor(index);
                    SequenceType successorKey = node.getSuccessorKey(index);
                    SequenceType prefix = SequenceUtil
                            .subsequence(originalKey, 0, originalKey.length() - key.length());
                    prefix = SequenceUtil.concat(prefix, successorKey);
                    return firstOrLastEntry(successor, prefix, higher);
                }
            }
        }

        return null;
    }

    @Nullable
    protected abstract Pair<Integer, SequenceType> indexOf(
            @NotNull final Node<SequenceType, ValueType> node,
            @NotNull final SequenceType sequence);

    protected AbstractSortedTrie(@Nullable final Node<SequenceType, ValueType> node,
                                 @Nullable final Comparator<? super SequenceType> comparator) {
        super(node);
        this.comparator = comparator;
    }

    public AbstractSortedTrie(@Nullable final Comparator<SequenceType> comparator) {
        this(null, comparator);
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

                if (pair.first.isValueSet()) {
                    SequenceType lowerKey = SequenceUtil
                            .subsequence(key, 0, key.length() - pair.second.length());
                    return new AbstractMap.SimpleImmutableEntry<>(
                            lowerKey.isEmpty() ? null : lowerKey, pair.first.getValue());
                } else {
                    Map.Entry<SequenceType, ValueType> entry = getLowerOrHigherEntry(key,
                            pair.first, pair.second, index -> index - 1, false);

                    if (entry != null) {
                        return entry;
                    }
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
                    Entry<SequenceType, ValueType> entry = getLowerOrHigherEntry(key, pair.first,
                            pair.second, index -> index + 1, true);

                    if (entry != null) {
                        return entry;
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
    public final SortedMap<SequenceType, ValueType> subMap(final SequenceType fromKey,
                                                           final SequenceType toKey) {
        return subMap(fromKey, true, toKey, false);
    }

    @NotNull
    @Override
    public final NavigableMap<SequenceType, ValueType> subMap(final SequenceType fromKey,
                                                              final boolean fromInclusive,
                                                              final SequenceType toKey,
                                                              final boolean toInclusive) {
        return new AscendingSubMap<>(this, false, fromKey, fromInclusive, false, toKey,
                toInclusive);
    }

    @NotNull
    @Override
    public final SortedMap<SequenceType, ValueType> headMap(final SequenceType toKey) {
        return headMap(toKey, false);
    }

    @NotNull
    @Override
    public final NavigableMap<SequenceType, ValueType> headMap(final SequenceType toKey,
                                                               final boolean inclusive) {
        return new AscendingSubMap<>(this, true, null, true, false, toKey, inclusive);
    }

    @NotNull
    @Override
    public final SortedMap<SequenceType, ValueType> tailMap(final SequenceType fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public final NavigableMap<SequenceType, ValueType> tailMap(final SequenceType fromKey,
                                                               final boolean inclusive) {
        return new AscendingSubMap<>(this, false, fromKey, inclusive, true, null, true);
    }

    @NotNull
    @Override
    public final NavigableMap<SequenceType, ValueType> descendingMap() {
        return new DescendingSubMap<>(this, true, null, true, true, null, true);
    }

    @Override
    public final NavigableSet<SequenceType> navigableKeySet() {
        return new KeySet<>(this);
    }

    @Override
    public final NavigableSet<SequenceType> descendingKeySet() {
        return descendingMap().navigableKeySet();
    }

}
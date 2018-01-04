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
import de.mrapp.tries.StringTrie;
import de.mrapp.tries.Trie;
import de.mrapp.tries.sequence.StringSequence;
import de.mrapp.tries.util.EntryUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

import static de.mrapp.util.Condition.ensureNotNull;

/**
 * An abstract base class for all wrappers, which implement an interface, which is extended from
 * {@link StringTrie}, by delegating all method calls to an encapsulated trie.
 *
 * @param <TrieType>  The type of the encapsulated trie
 * @param <ValueType> The type of the values, which are stored by the trie
 * @author Michael Rapp
 * @since 1.0.0
 */
public abstract class AbstractStringTrieWrapper<TrieType extends Trie<StringSequence, ValueType>, ValueType> implements
        StringTrie<ValueType> {

    /**
     * The entry set of a {@link StringTrie}. It encapsulates the entry set of a {@link Trie}.
     *
     * @param <V> The type of the values, which are stored by the trie
     */
    private static final class EntrySetWrapper<V> extends AbstractSet<Entry<String, V>> {

        /**
         * The iterator, which allows to iterate the entries of a {@link StringTrie}'s entry set. It
         * encapsulates the iterator of a {@link Trie}'s entry set.
         *
         * @param <V> The type of the values, which are stored by the trie
         */
        private static final class EntryIteratorWrapper<V> implements Iterator<Entry<String, V>> {

            /**
             * The encapsulated iterator.
             */
            private final Iterator<Entry<StringSequence, V>> iterator;

            /**
             * Creates a new iterator, which allows to iterate the entries of a {@link StringTrie}'s
             * entry set.
             *
             * @param iterator The iterator, which should be encapsulated, as an instance of the
             *                 type {@link Iterator}. The iterator may not be null
             */
            EntryIteratorWrapper(@NotNull final Iterator<Entry<StringSequence, V>> iterator) {
                ensureNotNull(iterator, "The iterator may not be null");
                this.iterator = iterator;
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Entry<String, V> next() {
                Entry<StringSequence, V> next = iterator.next();

                if (next != null) {
                    String key = next.getKey() != null ? next.getKey().toString() : null;
                    return new AbstractMap.SimpleImmutableEntry<>(key, next.getValue());
                }

                return null;
            }

        }

        /**
         * The encapsulated entry set.
         */
        private final Set<Entry<StringSequence, V>> entrySet;

        /**
         * Creates a new entry set of a {@link StringTrie}.
         *
         * @param entrySet The entry set, which should be encapsulated, as an instance of the type
         *                 {@link Set}. The entry set may not be null
         */
        EntrySetWrapper(@NotNull final Set<Entry<StringSequence, V>> entrySet) {
            ensureNotNull(entrySet, "The entry set may not be null");
            this.entrySet = entrySet;
        }

        @NotNull
        @Override
        public Iterator<Entry<String, V>> iterator() {
            return new EntryIteratorWrapper<>(entrySet.iterator());
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean remove(final Object o) {
            Map.Entry<String, V> entry = (Entry<String, V>) o;
            return entrySet.remove(new AbstractMap.SimpleImmutableEntry<>(
                    StringSequence.convertFromString(entry.getKey()), entry.getValue()));
        }

        @Override
        public void clear() {
            entrySet.clear();
        }

        @Override
        public int size() {
            return entrySet.size();
        }

        @Override
        public boolean isEmpty() {
            return entrySet.isEmpty();
        }

        @Override
        public boolean contains(final Object o) {
            Map.Entry<?, ?> entry = (Entry<?, ?>) o;
            String key = (String) entry.getKey();

            for (Map.Entry<StringSequence, V> e : entrySet) {
                if ((e.getKey() == null && key == null) ||
                        (e.getKey() != null && e.getKey().toString().equals(key))) {
                    return EntryUtil.isEqual(e.getValue(), entry.getValue());
                }
            }

            return false;
        }

    }

    /**
     * The key set of a {@link StringTrie}. It encapsulates the key set of a {@link Trie}.
     *
     * @param <SetType> The type of the encapsulated set
     */
    static class KeySetWrapper<SetType extends Set<StringSequence>> extends AbstractSet<String> {

        /**
         * The iterator, which allows to iterate the entries of a {@link StringTrie}'s key set. It
         * encapsulates the iterator of a {@link Trie}'s key set.
         */
        final class KeyIteratorWrapper implements Iterator<String> {

            /**
             * The encapsulated iterator.
             */
            private final Iterator<StringSequence> iterator;

            /**
             * Creates a new iterator, which allows to iterate the entries of a {@link StringTrie}'s
             * key set.
             *
             * @param iterator The iterator, which should be encapsulated, as an instance of the
             *                 type {@link Iterator}. The iterator may not be null
             */
            KeyIteratorWrapper(@NotNull final Iterator<StringSequence> iterator) {
                ensureNotNull(iterator, "The iterator may not be null");
                this.iterator = iterator;
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public String next() {
                StringSequence next = iterator.next();
                return next != null ? next.toString() : null;
            }

        }

        /**
         * The spliterator, which allows to traverse the entries of a {@link StringTrie}'s key set.
         * It encapsulates the spliterator of a {@link Trie}'s key set.
         */
        private final class KeySpliteratorWrapper implements Spliterator<String> {

            /**
             * The encapsulated spliterator.
             */
            private final Spliterator<StringSequence> spliterator;

            /**
             * Creates a new spliterator, which allows to traverse the entries of a {@link
             * StringTrie}'s key set.
             *
             * @param spliterator The spliterator, which should be encapsulated, as an instance of
             *                    the type {@link Spliterator}. The spliterator may not be null
             */
            KeySpliteratorWrapper(@NotNull final Spliterator<StringSequence> spliterator) {
                ensureNotNull(spliterator, "The spliterator may not be null");
                this.spliterator = spliterator;
            }

            @Override
            public boolean tryAdvance(final Consumer<? super String> action) {
                return spliterator.tryAdvance(
                        stringSequence -> action
                                .accept(stringSequence != null ? stringSequence.toString() : null));
            }

            @Override
            public Spliterator<String> trySplit() {
                Spliterator<StringSequence> spliterator = this.spliterator.trySplit();
                return spliterator != null ? new KeySpliteratorWrapper(spliterator) : null;
            }

            @Override
            public long estimateSize() {
                return spliterator.estimateSize();
            }

            @Override
            public int characteristics() {
                return spliterator.characteristics();
            }

        }

        /**
         * The encapsulated set.
         */
        final SetType set;

        /**
         * Creates a new key set of a {@link StringTrie}.
         *
         * @param set The set, which should be encapsulated, as an instance of the generic type
         *            {@link SetType}. The set may not be null
         */
        KeySetWrapper(@NotNull final SetType set) {
            ensureNotNull(set, "The set may not be null");
            this.set = set;
        }

        @NotNull
        @Override
        public final Iterator<String> iterator() {
            return new KeyIteratorWrapper(set.iterator());
        }

        @Override
        public final boolean remove(final Object o) {
            return set.remove(StringSequence.convertFromString((String) o));
        }

        @Override
        public final void clear() {
            set.clear();
        }

        @Override
        public final int size() {
            return set.size();
        }

        @Override
        public final boolean isEmpty() {
            return set.isEmpty();
        }

        @Override
        public final boolean contains(Object o) {
            return set.contains(StringSequence.convertFromString((String) o));
        }

        @Override
        public final Spliterator<String> spliterator() {
            return new KeySpliteratorWrapper(set.spliterator());
        }

    }

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = -2331614627443287430L;

    /**
     * The encapsulated trie.
     */
    protected final TrieType trie;

    /**
     * Creates a new wrapper, which delegates all method calls to an encapsulated trie.
     *
     * @param trie The trie, which should be encapsulated, as an instance of the generic type {@link
     *             TrieType}. The trie may not be null
     */
    AbstractStringTrieWrapper(@NotNull final TrieType trie) {
        ensureNotNull(trie, "The trie may not be null");
        this.trie = trie;
    }

    @Override
    public final int size() {
        return trie.size();
    }

    @Override
    public final boolean isEmpty() {
        return trie.isEmpty();
    }

    @Override
    public final boolean containsKey(final Object key) {
        return trie.containsKey(StringSequence.convertFromString((String) key));
    }

    @Override
    public final boolean containsValue(final Object value) {
        return trie.containsValue(value);
    }

    @Override
    public final ValueType get(final Object key) {
        return trie.get(StringSequence.convertFromString((String) key));
    }

    @Override
    public final ValueType put(final String key, final ValueType value) {
        return trie.put(StringSequence.convertFromString(key), value);
    }

    @Override
    public final ValueType remove(final Object key) {
        return trie.remove(StringSequence.convertFromString((String) key));
    }

    @Override
    public final void putAll(@NotNull final Map<? extends String, ? extends ValueType> map) {
        ensureNotNull(map, "The map may not be null");
        map.forEach(this::put);
    }

    @Override
    public final void clear() {
        trie.clear();
    }

    @NotNull
    @Override
    public final Set<String> keySet() {
        return new KeySetWrapper<>(trie.keySet());
    }

    @NotNull
    @Override
    public final Collection<ValueType> values() {
        return trie.values();
    }

    @NotNull
    @Override
    public final Set<Entry<String, ValueType>> entrySet() {
        return new EntrySetWrapper<>(trie.entrySet());
    }

    @Nullable
    @Override
    public final Node<String, ValueType> getRootNode() {
        Node<StringSequence, ValueType> rootNode = trie.getRootNode();
        return rootNode != null ? new StringNodeWrapper<>(rootNode) : null;
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + trie.hashCode();
        return result;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (getClass() != obj.getClass())
            return false;
        AbstractStringTrieWrapper<?, ?> other = (AbstractStringTrieWrapper<?, ?>) obj;
        return trie.equals(other.trie);
    }

}
package de.mrapp.tries.datastructure;

import de.mrapp.tries.StringTrie;
import de.mrapp.tries.Trie;
import de.mrapp.tries.sequence.StringSequence;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static de.mrapp.util.Condition.ensureNotNull;

public class StringTrieWrapper<ValueType> implements StringTrie<ValueType> {


    private class EntrySetWrapper extends AbstractSet<Entry<String, ValueType>> {

        private class IteratorWrapper implements Iterator<Entry<String, ValueType>> {

            private final Iterator<Entry<StringSequence, ValueType>> iterator;

            IteratorWrapper(@NotNull final Iterator<Entry<StringSequence, ValueType>> iterator) {
                ensureNotNull(iterator, "The iterator may not be null");
                this.iterator = iterator;
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Entry<String, ValueType> next() {
                Entry<StringSequence, ValueType> next = iterator.next();
                return next != null ?
                        new AbstractMap.SimpleImmutableEntry<>(next.getKey().toString(),
                                next.getValue()) : null;
            }

        }

        private final Set<Entry<StringSequence, ValueType>> entrySet;

        EntrySetWrapper(@NotNull final Set<Entry<StringSequence, ValueType>> entrySet) {
            ensureNotNull(entrySet, "The entry set may not be null");
            this.entrySet = entrySet;
        }

        @NotNull
        @Override
        public Iterator<Entry<String, ValueType>> iterator() {
            return new IteratorWrapper(entrySet.iterator());
        }

        @Override
        public int size() {
            return entrySet.size();
        }

    }

    private static class KeySetWrapper extends AbstractSet<String> {

        private class IteratorWrapper implements Iterator<String> {

            private final Iterator<StringSequence> iterator;

            IteratorWrapper(@NotNull final Iterator<StringSequence> iterator) {
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

        private final Set<StringSequence> keySet;

        KeySetWrapper(@NotNull final Set<StringSequence> keySet) {
            ensureNotNull(keySet, "The key set may not be null");
            this.keySet = keySet;
        }

        @NotNull
        @Override
        public Iterator<String> iterator() {
            return new IteratorWrapper(keySet.iterator());
        }

        @Override
        public int size() {
            return keySet.size();
        }

    }

    private static final long serialVersionUID = -2331614627443287430L;

    protected final Trie<StringSequence, String, ValueType> trie;

    public StringTrieWrapper(@NotNull final Trie<StringSequence, String, ValueType> trie) {
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
        return trie.containsKey(new StringSequence((String) key));
    }

    @Override
    public final boolean containsValue(final Object value) {
        return trie.containsValue(value);
    }

    @Override
    public final ValueType get(final Object key) {
        return trie.get(new StringSequence((String) key));
    }

    @Override
    public final ValueType put(final String key, final ValueType value) {
        return trie.put(new StringSequence(key), value);
    }

    @Override
    public final ValueType remove(final Object key) {
        return trie.remove(new StringSequence((String) key));
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
        return new KeySetWrapper(trie.keySet());
    }

    @NotNull
    @Override
    public final Collection<ValueType> values() {
        return trie.values();
    }

    @NotNull
    @Override
    public final Set<Entry<String, ValueType>> entrySet() {
        return new EntrySetWrapper(trie.entrySet());
    }

    @NotNull
    @Override
    public final StringTrie<ValueType> subTree(@NotNull final String key) {
        return new StringTrieWrapper<>(trie.subTree(new StringSequence(key)));
    }

}
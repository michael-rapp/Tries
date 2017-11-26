package de.mrapp.tries;

import de.mrapp.tries.AbstractTrie.Node.Key;
import de.mrapp.tries.AbstractTrie.Node.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static de.mrapp.util.Condition.*;

public abstract class AbstractTrie<SequenceType extends Sequence<SymbolType>, SymbolType, ValueType, NodeType extends AbstractTrie.Node<SymbolType, ValueType, NodeType>>
        implements Trie<SequenceType, SymbolType, ValueType> {

    public static abstract class Node<K, V, NodeType extends Node<K, V, ?>> implements
            java.util.Map.Entry<Node.Key<K>, Node.Value<V>>, java.io.Serializable, Cloneable {

        public static class Key<K> implements java.io.Serializable {

            private static final long serialVersionUID = -5608197174243502873L;

            private final K[] sequence;

            Key() {
                this.sequence = null;
            }

            @SafeVarargs
            Key(K... sequence) {
                ensureNotNull(sequence, "The sequence may not be null");
                this.sequence = sequence;
            }

            public final K[] getSequence() {
                return sequence;
            }

            @Override
            public final String toString() {
                return Arrays.toString(sequence);
            }

            @Override
            public final int hashCode() {
                return Arrays.hashCode(sequence);
            }

            @Override
            public final boolean equals(final Object obj) {
                if (obj == null)
                    return false;
                if (this == obj)
                    return true;
                if (obj.getClass() != getClass())
                    return false;
                Key<?> other = (Key<?>) obj;
                return Arrays.equals(sequence, other.sequence);
            }

        }

        public static class Value<V> implements java.io.Serializable {

            private static final long serialVersionUID = -2272789810845385356L;

            private V value;

            Value(@Nullable final V value) {
                this.value = value;
            }

            @Nullable
            public V getValue() {
                return value;
            }

            @Override
            public final String toString() {
                return value != null ? value.toString() : "null";
            }

            @Override
            public final int hashCode() {
                final int prime = 31;
                int result = 0;
                result = prime * result + (value == null ? 0 : value.hashCode());
                return result;
            }

            @Override
            public final boolean equals(final Object obj) {
                if (obj == null)
                    return false;
                if (this == obj)
                    return true;
                if (obj.getClass() != getClass())
                    return false;
                Value<?> other = (Value<?>) obj;
                if (value == null) {
                    if (other.value != null)
                        return false;
                } else if (!value.equals(other.value))
                    return false;
                return true;
            }

        }

        private static final long serialVersionUID = -5239050242490781683L;

        private final Key<K> key;

        private Value<V> value;

        Node() {
            this(new Key<>());
        }

        Node(@NotNull final Key<K> key) {
            this.key = key;
            this.value = null;
        }

        public abstract void addSuccessor(@NotNull final Key<K> key,
                                          @NotNull final NodeType successor);

        @Nullable
        public abstract NodeType getSuccessor(@NotNull final Key<K> key);

        @NotNull
        public abstract Collection<NodeType> getAllSuccessors();

        @Override
        public final Key<K> getKey() {
            return key;
        }

        @Override
        public final Value<V> getValue() {
            return value;
        }

        @Nullable
        @Override
        public final Value<V> setValue(@Nullable final Value<V> value) {
            Value<V> oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        // TODO: clone, toString, hashCode, equals

    }

    private class EntrySet extends AbstractSet<Map.Entry<SequenceType, ValueType>> {

        private class LeafIterator implements Iterator<Map.Entry<SequenceType, ValueType>> {

            private class Path {

                private final NodeType node;

                private final Collection<SymbolType> sequence;

                Path(final NodeType node) {
                    this.node = node;
                    this.sequence = new LinkedList<>();
                }

                Path(final NodeType node, final Collection<SymbolType> sequence) {
                    ensureNotNull(node, "The node may not be null");
                    ensureNotNull(sequence, "The sequence may not be null");
                    this.node = node;
                    this.sequence = sequence;
                }

            }

            private final Queue<Path> queue;

            private Path nextPath;

            @Nullable
            private Path fetchNext() {
                Path next = null;

                while (!this.queue.isEmpty() && next == null) {
                    Path path = this.queue.poll();

                    if (path.node.getValue() != null) {
                        next = path;
                    }

                    for (NodeType successor : path.node.getAllSuccessors()) {
                        Collection<SymbolType> sequence = new LinkedList<>(path.sequence);
                        sequence.addAll(Arrays.asList(successor.getKey().getSequence()));
                        this.queue.add(new Path(successor, sequence));
                    }
                }

                return next;
            }

            LeafIterator() {
                this.queue = new LinkedList<>();

                if (rootNode != null) {
                    this.queue.add(new Path(rootNode));
                    this.nextPath = fetchNext();
                }
            }

            @Override
            public boolean hasNext() {
                checkForModifications();
                return this.nextPath != null;
            }

            @Override
            public Entry<SequenceType, ValueType> next() {
                checkForModifications();
                ensureTrue(hasNext(), null, NoSuchElementException.class);
                Path result = this.nextPath;
                this.nextPath = fetchNext();
                SequenceType sequence = sequenceBuilder.build(result.sequence);
                return new AbstractMap.SimpleImmutableEntry<>(sequence,
                        result.node.getValue().getValue());
            }
        }

        private final long modificationCount;

        private void checkForModifications() {
            ensureEqual(this.modificationCount, AbstractTrie.this.modificationCount, null,
                    ConcurrentModificationException.class);
        }

        EntrySet(final long modificationCount) {
            this.modificationCount = modificationCount;
        }

        @NotNull
        @Override
        public Iterator<Map.Entry<SequenceType, ValueType>> iterator() {
            return new LeafIterator();
        }

        @Override
        public int size() {
            checkForModifications();
            return AbstractTrie.this.size();
        }

    }

    private class EntryMap extends AbstractMap<SequenceType, ValueType> {

        private final Set<Entry<SequenceType, ValueType>> entrySet;

        EntryMap(final long modificationCount) {
            this.entrySet = Collections.unmodifiableSet(new EntrySet(modificationCount));
        }

        @NotNull
        @Override
        public Set<Entry<SequenceType, ValueType>> entrySet() {
            return entrySet;
        }

    }

    private static final long serialVersionUID = -9049598420902876017L;

    private final Sequence.Builder<SequenceType, SymbolType> sequenceBuilder;

    protected NodeType rootNode;

    private int size;

    private long modificationCount;

    @Nullable
    private NodeType getNode(final SequenceType key) {
        if (rootNode != null) {
            NodeType currentNode = rootNode;
            Iterator<SymbolType> iterator = key.iterator();

            while (iterator.hasNext()) {
                SymbolType symbol = iterator.next();
                Key<SymbolType> symbolKey = new Key<>(symbol);
                currentNode = currentNode.getSuccessor(symbolKey);

                if (currentNode == null) {
                    return null;
                } else if (!iterator.hasNext()) {
                    return currentNode;
                }
            }
        }

        return null;
    }

    protected abstract NodeType createNode(@NotNull final Key<SymbolType> key);

    AbstractTrie(@NotNull final Sequence.Builder<SequenceType, SymbolType> sequenceBuilder) {
        ensureNotNull(sequenceBuilder, "The sequence builder may not be null");
        this.sequenceBuilder = sequenceBuilder;
        this.rootNode = null;
        this.size = 0;
        this.modificationCount = 0;
    }

    @Override
    public final boolean isEmpty() {
        return rootNode == null;
    }

    @Override
    public final int size() {
        return size;
    }

    @Override
    public final boolean containsKey(final Object key) {
        ensureNotNull(key, "The key may not be null");
        return new EntryMap(modificationCount).containsKey(key);
    }

    @Override
    public final boolean containsValue(final Object value) {
        return new EntryMap(modificationCount).containsValue(value);
    }

    @Override
    public final void clear() {
        this.rootNode = null;
        this.size = 0;
        this.modificationCount++;
    }

    @NotNull
    @Override
    public final Set<SequenceType> keySet() {
        return Collections.unmodifiableSet(new EntryMap(modificationCount).keySet());
    }

    @NotNull
    @Override
    public final Collection<ValueType> values() {
        return Collections.unmodifiableCollection(new EntryMap(modificationCount).values());
    }

    @NotNull
    @Override
    public final Set<Entry<SequenceType, ValueType>> entrySet() {
        return Collections.unmodifiableSet(new EntryMap(modificationCount).entrySet);
    }

    @Override
    public final ValueType put(final SequenceType key, final ValueType value) {
        ensureNotNull(key, "The key may not be null");
        modificationCount++;

        if (rootNode == null) {
            rootNode = createNode(new Key<>());
        }

        Value<ValueType> previousValue = null;
        NodeType currentNode = rootNode;
        Iterator<SymbolType> iterator = key.iterator();

        while (iterator.hasNext()) {
            SymbolType symbol = iterator.next(); // TODO use complete suffix instead of next symbol
            Key<SymbolType> symbolKey = new Key<>(symbol);
            NodeType successor = currentNode.getSuccessor(symbolKey);

            if (successor == null) {
                successor = createNode(symbolKey);
                currentNode.addSuccessor(symbolKey, successor);
            }

            if (!iterator.hasNext()) {
                previousValue = successor.setValue(new Value<>(value));
                size += previousValue == null ? 1 : 0;
            }

            currentNode = successor;
        }

        return previousValue != null ? previousValue.getValue() : null;
    }

    @Override
    public final void putAll(@NotNull final Map<? extends SequenceType, ? extends ValueType> map) {
        ensureNotNull(map, "The map may not be null");
        map.forEach(this::put);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final ValueType get(final Object key) {
        ensureNotNull(key, "The key may not be null");
        SequenceType sequence = (SequenceType) key;
        NodeType node = getNode(sequence);
        Value<ValueType> value = node != null ? node.getValue() : null;
        return value != null ? value.getValue() : null;
    }

}
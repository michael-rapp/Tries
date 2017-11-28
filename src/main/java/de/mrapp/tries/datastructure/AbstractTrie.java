package de.mrapp.tries.datastructure;

import de.mrapp.tries.Sequence;
import de.mrapp.tries.Trie;
import de.mrapp.tries.datastructure.AbstractTrie.Node.Key;
import de.mrapp.tries.datastructure.AbstractTrie.Node.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static de.mrapp.util.Condition.*;

public abstract class AbstractTrie<SequenceType extends Sequence<SymbolType>, SymbolType, ValueType, NodeType extends AbstractTrie.Node<SymbolType, ValueType, NodeType>>
        implements Trie<SequenceType, SymbolType, ValueType> {

    public static abstract class Node<K, V, NodeType extends Node<K, V, ?>> implements
            java.util.Map.Entry<Node.Key<K>, Node.Value<V>>, java.io.Serializable {

        public static class Key<K> implements java.io.Serializable {

            private static final long serialVersionUID = -5608197174243502873L;

            private final Collection<K> sequence;

            Key() {
                this.sequence = Collections.emptyList();
            }

            @SafeVarargs
            Key(K... sequence) {
                ensureNotNull(sequence, "The sequence may not be null");
                this.sequence = new ArrayList<>(Arrays.asList(sequence));
            }

            @NotNull
            public final Collection<K> getSequence() {
                return Collections.unmodifiableCollection(sequence);
            }

            @Override
            public final String toString() {
                return sequence.toString();
            }

            @Override
            public final int hashCode() {
                final int prime = 31;
                int result = 0;
                result = prime * result + sequence.hashCode();
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
                Key<?> other = (Key<?>) obj;
                return sequence.equals(other.sequence);
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

        public Node(@NotNull final Key<K> key) {
            ensureNotNull(key, "The key may not be null");
            this.key = key;
            this.value = null;
        }

        public abstract void addSuccessor(@NotNull final Key<K> key,
                                          @NotNull final NodeType successor);

        public abstract void removeSuccessor(@NotNull final Key<K> key);

        @Nullable
        public abstract NodeType getSuccessor(@NotNull final Key<K> key);

        @NotNull
        public abstract Collection<NodeType> getAllSuccessors();

        public abstract int getSuccessorCount();

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

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 0;
            result = prime * result + key.hashCode();
            result = prime * result + (value == null ? 0 : value.hashCode());
            return result;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == null)
                return false;
            if (this == obj)
                return true;
            if (obj.getClass() != getClass())
                return false;
            Node<?, ?, ?> other = (Node<?, ?, ?>) obj;
            if (!key.equals(other.key))
                return false;
            if (value == null) {
                if (other.value != null)
                    return false;
            } else if (!value.equals(other.value))
                return false;
            return true;
        }

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
                    ensureFalse(sequence.isEmpty(), "The sequence may not be empty");
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
                        sequence.addAll(successor.getKey().getSequence());
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

    protected abstract NodeType createNode(@NotNull final Key<SymbolType> key);

    public AbstractTrie(@NotNull final Sequence.Builder<SequenceType, SymbolType> sequenceBuilder) {
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

    @SuppressWarnings("unchecked")
    @Override
    public final boolean containsKey(final Object key) {
        ensureNotNull(key, "The key may not be null");
        SequenceType sequence = (SequenceType) key;
        ensureAtLeast(sequence.length(), 1, "The key may not be empty");
        return new EntryMap(modificationCount).containsKey(sequence);
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
        ensureAtLeast(key.length(), 1, "The key may not be empty");

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
                modificationCount++;
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
    public final ValueType remove(final Object key) {
        ensureNotNull(key, "The key may not be null");
        SequenceType sequence = (SequenceType) key;
        ensureAtLeast(sequence.length(), 1, "The key may not be empty");

        if (rootNode != null) {
            NodeType lastRetainedNode = rootNode;
            Key<SymbolType> successorToRemove = null;
            NodeType currentNode = rootNode;
            Iterator<SymbolType> iterator = sequence.iterator();

            while (iterator.hasNext()) {
                SymbolType symbol = iterator.next();
                Key<SymbolType> symbolKey = new Key<>(symbol);
                NodeType successor = currentNode.getSuccessor(symbolKey);

                if (successor == null) {
                    return null;
                } else {
                    if (currentNode.getSuccessorCount() > 1 || currentNode.getValue() != null) {
                        lastRetainedNode = currentNode;
                        successorToRemove = symbolKey;
                    }

                    if (!iterator.hasNext()) {
                        if (successor.getSuccessorCount() > 0) {
                            lastRetainedNode = null;
                            successorToRemove = null;
                        }

                        Value<ValueType> value = successor.getValue();

                        if (value != null) {
                            successor.setValue(null);

                            if (lastRetainedNode == rootNode) {
                                clear();
                            } else {
                                if (successorToRemove != null) {
                                    lastRetainedNode.removeSuccessor(successorToRemove);
                                }

                                size--;
                                modificationCount++;
                            }

                            return value.getValue();
                        }

                        return null;
                    }
                }

                currentNode = successor;
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final ValueType get(final Object key) {
        ensureNotNull(key, "The key may not be null");
        SequenceType sequence = (SequenceType) key;
        ensureAtLeast(sequence.length(), 1, "The key may not be empty");

        if (rootNode != null) {
            NodeType currentNode = rootNode;
            Iterator<SymbolType> iterator = sequence.iterator();

            while (iterator.hasNext()) {
                SymbolType symbol = iterator.next();
                Key<SymbolType> symbolKey = new Key<>(symbol);
                currentNode = currentNode.getSuccessor(symbolKey);

                if (currentNode == null) {
                    return null;
                } else if (!iterator.hasNext()) {
                    Value<ValueType> value = currentNode.getValue();
                    return value != null ? value.getValue() : null;
                }
            }
        }

        return null;
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 0;
        result = prime * result + (rootNode == null ? 0 : rootNode.hashCode());
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
        AbstractTrie<?, ?, ?, ?> other = (AbstractTrie<?, ?, ?, ?>) obj;
        if (rootNode == null) {
            if (other.rootNode != null)
                return false;
        } else if (!rootNode.equals(other.rootNode))
            return false;
        return true;
    }

}
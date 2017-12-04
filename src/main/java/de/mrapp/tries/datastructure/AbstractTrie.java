package de.mrapp.tries.datastructure;

import de.mrapp.tries.Sequence;
import de.mrapp.tries.Trie;
import de.mrapp.tries.datastructure.AbstractTrie.Node.Value;
import de.mrapp.util.datastructure.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static de.mrapp.util.Condition.*;

public abstract class AbstractTrie<SequenceType extends Sequence, ValueType, NodeType extends AbstractTrie.Node<SequenceType, ValueType, NodeType>>
        implements Trie<SequenceType, ValueType> {

    public static abstract class Node<S extends Sequence, V, NodeType extends Node<S, V, NodeType>>
            implements java.io.Serializable {

        public static class Value<T> implements java.io.Serializable {

            private static final long serialVersionUID = -2272789810845385356L;

            private T value;

            public Value(@Nullable final T value) {
                this.value = value;
            }

            @Nullable
            public T getValue() {
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

        private Value<V> value;

        private int containedValues;

        private Node<S, V, ?> predecessor;

        protected final void increaseContainedValues(final int by) {
            this.containedValues += by;

            if (getPredecessor() != null) {
                getPredecessor().increaseContainedValues(by);
            }
        }

        protected final void decreaseContainedValues(final int by) {
            this.containedValues -= by;

            if (getPredecessor() != null) {
                getPredecessor().decreaseContainedValues(by);
            }
        }

        protected final void setPredecessor(@Nullable final Node<S, V, ?> predecessor) {
            this.predecessor = predecessor;
        }

        @Nullable
        protected Node<S, V, ?> getPredecessor() {
            return predecessor;
        }

        public Node() {
            this.value = null;
            this.containedValues = 0;
            this.predecessor = null;
        }

        protected abstract S onAddSuccessor(@NotNull final S sequence,
                                               @NotNull final NodeType successor);

        protected abstract NodeType onRemoveSuccessor(@NotNull final S sequence);

        @Nullable
        public abstract Pair<NodeType, S> getSuccessor(@NotNull final S sequence);

        @NotNull
        public abstract Map<S, NodeType> getAllSuccessors();

        public abstract int getSuccessorCount();

        public final S addSuccessor(@NotNull final S sequence,
                                       @NotNull final NodeType successor) {
            ensureNotNull(sequence, "The sequence may not be null");
            ensureNotNull(successor, "The successor may not be null");
            S suffix = onAddSuccessor(sequence, successor);
            successor.setPredecessor(this);
            increaseContainedValues(successor.getContainedValues());
            return suffix;
        }

        public final void removeSuccessor(@NotNull final S sequence) {
            ensureNotNull(sequence, "The sequence may not be null");
            NodeType successor = onRemoveSuccessor(sequence);

            if (successor != null) {
                decreaseContainedValues(successor.getContainedValues());
                successor.setPredecessor(null);
            }
        }

        public final int getContainedValues() {
            return containedValues;
        }

        @Nullable
        public final Value<V> getValue() {
            return value;
        }

        @Nullable
        public final Value<V> setValue(@Nullable final Value<V> value) {
            Value<V> oldValue = this.value;

            if (oldValue == null && value != null) {
                increaseContainedValues(1);
            } else if (oldValue != null && value == null) {
                decreaseContainedValues(1);
            }

            this.value = value;
            return oldValue;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 0;
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

                private final SequenceType sequence;

                Path(final NodeType node) {
                    this.node = node;
                    this.sequence = null;
                }

                Path(final NodeType node, final SequenceType sequence) {
                    ensureNotNull(node, "The node may not be null");
                    ensureNotNull(sequence, "The sequence may not be null");
                    ensureFalse(sequence.isEmpty(), "The sequence may not be empty");
                    this.node = node;
                    this.sequence = sequence;
                }

            }

            private final Queue<Path> queue;

            private Path nextPath;

            @SuppressWarnings("unchecked")
            @Nullable
            private Path fetchNext() {
                Path next = null;

                while (!this.queue.isEmpty() && next == null) {
                    Path path = this.queue.poll();

                    if (path.node.getValue() != null) {
                        next = path;
                    }

                    for (Map.Entry<SequenceType, NodeType> entry : path.node.getAllSuccessors()
                            .entrySet()) {
                        SequenceType suffix = entry.getKey();
                        NodeType successor = entry.getValue();
                        SequenceType sequence =
                                path.sequence != null ?
                                        (SequenceType) path.sequence.concat(suffix) : suffix;
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
                return new AbstractMap.SimpleImmutableEntry<>(result.sequence,
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

    protected NodeType rootNode;

    private long modificationCount;

    protected abstract NodeType createNode();

    @SuppressWarnings("unchecked")
    @Nullable
    protected final NodeType getNode(final Object key) {
        ensureNotNull(key, "The key may not be null");
        SequenceType sequence = (SequenceType) key;
        ensureAtLeast(sequence.length(), 1, "The key may not be empty");

        if (rootNode != null) {
            NodeType currentNode = rootNode;
            SequenceType suffix = sequence;

            while (!suffix.isEmpty()) {
                Pair<NodeType, SequenceType> pair = currentNode.getSuccessor(suffix);

                if (pair == null) {
                    return null;
                } else {
                    currentNode = pair.first;
                    suffix = pair.second;
                }
            }

            return currentNode;
        }

        return null;
    }

    protected AbstractTrie(@Nullable final NodeType rootNode) {
        this.rootNode = rootNode;
        this.modificationCount = 0;
    }

    public AbstractTrie() {
        this(null);
    }

    @Override
    public final boolean isEmpty() {
        return rootNode == null;
    }

    @Override
    public final int size() {
        return rootNode != null ? rootNode.getContainedValues() : 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final boolean containsKey(final Object key) {
        ensureNotNull(key, "The key may not be null");
        SequenceType sequence = (SequenceType) key;
        NodeType node = getNode(sequence);
        return node != null && node.getValue() != null;
    }

    @Override
    public final boolean containsValue(final Object value) {
        return new EntryMap(modificationCount).containsValue(value);
    }

    @Override
    public final void clear() {
        this.rootNode = null;
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
            rootNode = createNode();
        }

        NodeType currentNode = rootNode;
        SequenceType suffix = key;

        while (!suffix.isEmpty()) {
            Pair<NodeType, SequenceType> pair = currentNode.getSuccessor(suffix);

            if (pair == null) {
                break;
            } else {
                currentNode = pair.first;
                suffix = pair.second;
            }
        }

        Value<ValueType> previousValue = null;

        if (suffix.isEmpty()) {
            previousValue = currentNode.setValue(new Value<>(value));
        } else {
            while (!suffix.isEmpty()) {
                NodeType successor = createNode();
                suffix = currentNode.addSuccessor(suffix, successor);
                currentNode = successor;

                if (suffix.isEmpty()) {
                    currentNode.setValue(new Value<>(value));
                }
            }
        }

        modificationCount++;
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
            SequenceType suffixToRemove = null;
            NodeType currentNode = rootNode;
            SequenceType suffix = sequence;

            while (!suffix.isEmpty()) {
                Pair<NodeType, SequenceType> pair = currentNode.getSuccessor(suffix);

                if (pair == null) {
                    return null;
                } else {
                    if (currentNode.getSuccessorCount() > 1 || currentNode.getValue() != null) {
                        lastRetainedNode = currentNode;
                        suffixToRemove = suffix;
                    }

                    NodeType successor = pair.first;
                    suffix = pair.second;

                    if (suffix.isEmpty()) {
                        if (successor.getSuccessorCount() > 0) {
                            lastRetainedNode = null;
                            suffixToRemove = null;
                        }

                        Value<ValueType> value = successor.getValue();

                        if (value != null) {
                            successor.setValue(null);

                            if (lastRetainedNode == rootNode) {
                                clear();
                            } else {
                                if (suffixToRemove != null) {
                                    lastRetainedNode.removeSuccessor(suffixToRemove);
                                }

                                modificationCount++;
                            }

                            return value.getValue();
                        }

                        return null;
                    }

                    currentNode = successor;
                }
            }
        }

        return null;
    }

    @Override
    public final ValueType get(final Object key) {
        NodeType node = getNode(key);
        Value<ValueType> value = node != null ? node.getValue() : null;
        return value != null ? value.getValue() : null;
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
        AbstractTrie<?, ?, ?> other = (AbstractTrie<?, ?, ?>) obj;
        if (rootNode == null) {
            if (other.rootNode != null)
                return false;
        } else if (!rootNode.equals(other.rootNode))
            return false;
        return true;
    }

}
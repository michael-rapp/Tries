package de.mrapp.tries;

import de.mrapp.tries.AbstractTrie.Node.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static de.mrapp.util.Condition.ensureNotNull;

public abstract class AbstractTrie<SequenceType extends Sequence<SymbolType>, SymbolType, ValueType, NodeType extends AbstractTrie.Node<SymbolType, ValueType, NodeType>>
        implements Trie<SequenceType, SymbolType, ValueType> {

    public static abstract class Node<K, V, NodeType extends Node<K, V, ?>> implements
            java.util.Map.Entry<Node.Key<K>, V>, java.io.Serializable, Cloneable {

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

        private static final long serialVersionUID = -5239050242490781683L;

        private final Key<K> key;

        private V value;

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
        public final V getValue() {
            return value;
        }

        @Override
        public final V setValue(final V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        // TODO: clone, toString, hashCode, equals

    }

    private static final long serialVersionUID = -9049598420902876017L;

    protected NodeType rootNode;

    private int size;

    private Map<SequenceType, NodeType> leafNodes;

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

    AbstractTrie(final NodeType rootNode, final int size,
                 final Map<SequenceType, NodeType> leafNodes) {
        this.rootNode = rootNode;
        this.size = size;
        this.leafNodes = leafNodes;
    }

    AbstractTrie() {
        this.rootNode = null;
        this.size = 0;
        this.leafNodes = new HashMap<>();
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
        SequenceType sequence = (SequenceType) key;
        return leafNodes.containsKey(sequence);
    }

    @Override
    public final boolean containsValue(final Object value) {
        return leafNodes.containsValue(value);
    }

    @Override
    public final void clear() {
        this.rootNode = null;
        this.size = 0;
        this.leafNodes.clear();
    }

    @NotNull
    @Override
    public final Set<SequenceType> keySet() {
        return leafNodes.keySet();
    }

    @NotNull
    @Override
    public final Collection<ValueType> values() {
        // TODO
        return null;
    }

    @NotNull
    @Override
    public final Set<Entry<SequenceType, ValueType>> entrySet() {
        // TODO
        return null;
    }


    @Override
    public ValueType put(final SequenceType key, final ValueType value) {
        if (rootNode == null) {
            rootNode = createNode(new Key<>());
        }

        ValueType previousValue = null;
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
                previousValue = successor.setValue(value);
                size += previousValue == null ? 1 : 0;
            }

            currentNode = successor;
        }

        return previousValue;
    }

    @Override
    public final void putAll(@NotNull final Map<? extends SequenceType, ? extends ValueType> map) {
        ensureNotNull(map, "The map may not be null");
        map.forEach(this::put);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ValueType get(final Object key) {
        SequenceType sequence = (SequenceType) key;
        NodeType node = getNode(sequence);
        return node != null ? node.getValue() : null;
    }

}
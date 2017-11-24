package de.mrapp.tries;

import de.mrapp.tries.AbstractTrie.Node.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static de.mrapp.util.Condition.ensureNotNull;

public class HashTrie<SequenceType extends Sequence<SymbolType>, SymbolType, ValueType> extends
        AbstractTrie<SequenceType, SymbolType, ValueType, HashTrie.Node<SymbolType, ValueType>> {

    private static final long serialVersionUID = -2250393346732658811L;

    public static class Node<K, V> extends AbstractTrie.Node<K, V, Node<K, V>> {

        private static final long serialVersionUID = 6241483145831567447L;

        private final Map<Key<K>, Node<K, V>> successors;

        public Node() {
            super();
            this.successors = new HashMap<>();
        }

        public Node(@NotNull final Key<K> key) {
            super(key);
            this.successors = new HashMap<>();
        }

        @Override
        public void addSuccessor(@NotNull final Key<K> key,
                                 @NotNull final Node<K, V> successor) {
            ensureNotNull(key, "The key may not be null");
            ensureNotNull(successor, "The successor may not be null");
            this.successors.put(key, successor);
        }

        @Nullable
        @Override
        public Node<K, V> getSuccessor(@NotNull final Key<K> key) {
            ensureNotNull(key, "The key may not be null");
            return this.successors.get(key);
        }

        @NotNull
        @Override
        public Collection<Node<K, V>> getAllSuccessors() {
            return successors.values();
        }

        // TODO: toString, hashCode, equals, clone

    }

    private HashTrie(@NotNull final Node<SymbolType, ValueType> rootNode,
                     final int size,
                     @NotNull final Map<SequenceType, Node<SymbolType, ValueType>> leafNodes) {
        super(rootNode, size, leafNodes);
    }

    public HashTrie() {
        super();
    }

    @Override
    public ValueType remove(final Object key) {
        // TODO
        return null;
    }

    @Override
    public Trie<SequenceType, SymbolType, ValueType> subTree(Object key) {
        // TODO
        return null;
    }

    @Override
    protected final Node<SymbolType, ValueType> createNode(@NotNull final Key<SymbolType> key) {
        return new Node<>(key);
    }

}
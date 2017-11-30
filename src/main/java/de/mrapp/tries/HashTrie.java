package de.mrapp.tries;

import de.mrapp.tries.datastructure.AbstractTrie;
import de.mrapp.tries.datastructure.AbstractTrie.Node.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static de.mrapp.util.Condition.ensureNotNull;

public class HashTrie<SequenceType extends Sequence<SymbolType>, SymbolType, ValueType> extends
        AbstractTrie<SequenceType, SymbolType, ValueType, HashTrie.Node<SymbolType, ValueType>> {

    private static final long serialVersionUID = -2250393346732658811L;

    public static class Node<K, V> extends AbstractTrie.Node<K, V, Node<K, V>> {

        private static final long serialVersionUID = 6241483145831567447L;

        private final Map<Key<K>, Node<K, V>> successors;

        public Node(@NotNull final Key<K> key) {
            super(key);
            this.successors = new HashMap<>();
        }

        public Node(@NotNull final Node<K, V> node) {
            this(node.getKey());
            setValue(node.getValue());
            node.successors.forEach((k, v) -> this.addSuccessor(k, new Node<>(v)));
        }

        @Override
        protected void onAddSuccessor(@NotNull final Key<K> key,
                                      @NotNull final Node<K, V> successor) {
            this.successors.put(key, successor);
        }

        @Override
        public Node<K, V> onRemoveSuccessor(@NotNull final Key<K> key) {
            return this.successors.remove(key);
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

        @Override
        public int getSuccessorCount() {
            return successors.size();
        }

        @Override
        public String toString() {
            return "Node{" +
                    "key=" + getKey() +
                    ", value=" + getValue() +
                    ", successors=" + successors.keySet() +
                    '}';
        }

        @Override
        public final int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + successors.hashCode();
            return result;
        }

        @Override
        public final boolean equals(final Object obj) {
            if (!super.equals(obj))
                return false;
            Node<?, ?> other = (Node<?, ?>) obj;
            return successors.equals(other.successors);
        }

    }

    private HashTrie(@NotNull final Sequence.Builder<SequenceType, SymbolType> sequenceBuilder,
                     @Nullable final Node<SymbolType, ValueType> rootNode) {
        super(sequenceBuilder, rootNode);
    }

    public HashTrie(@NotNull final Sequence.Builder<SequenceType, SymbolType> sequenceBuilder) {
        super(sequenceBuilder);
    }

    @NotNull
    @Override
    public Trie<SequenceType, SymbolType, ValueType> subTree(@NotNull final SequenceType key) {
        Node<SymbolType, ValueType> node = getNode(key);

        if (node != null) {
            Node<SymbolType, ValueType> newRootNode = createNode(new Key<>());
            Node<SymbolType, ValueType> currentNode = newRootNode;

            for (SymbolType symbol : key) {
                Key<SymbolType> symbolKey = new Key<>(symbol);
                Node<SymbolType, ValueType> successor = new Node<>(symbolKey);
                currentNode.addSuccessor(symbolKey, successor);
                currentNode = successor;
            }

            for (Node<SymbolType, ValueType> successor : node.getAllSuccessors()) {
                currentNode.addSuccessor(successor.getKey(), successor);
            }

            return new HashTrie<>(sequenceBuilder, newRootNode);
        }

        throw new NoSuchElementException();
    }

    @Override
    protected final Node<SymbolType, ValueType> createNode(@NotNull final Key<SymbolType> key) {
        return new Node<>(key);
    }

    @Override
    public final String toString() {
        return "HashTrie " + entrySet().toString();
    }

}
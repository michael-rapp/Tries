package de.mrapp.tries;

import de.mrapp.tries.datastructure.AbstractTrie;
import de.mrapp.util.datastructure.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static de.mrapp.util.Condition.ensureNotNull;

public class HashTrie<SequenceType extends Sequence, ValueType> extends
        AbstractTrie<SequenceType, ValueType, HashTrie.Node<SequenceType, ValueType>> {

    private static final long serialVersionUID = -2250393346732658811L;

    public static class Node<S extends Sequence, V> extends AbstractNode<S, V, Node<S, V>> {

        private static final long serialVersionUID = 6241483145831567447L;

        private final Map<S, Node<S, V>> successors;

        public Node() {
            super();
            this.successors = new HashMap<>();
        }

        public Node(@NotNull final Node<S, V> node) {
            this();
            setValue(node.getValue());
            node.successors.forEach((k, v) -> this.addSuccessor(k, new Node<>(v)));
        }

        @Nullable
        @Override
        protected S onAddSuccessor(@NotNull final S sequence,
                                   @NotNull final Node<S, V> successor) {
            S key = (S) sequence.subsequence(0, 1);
            this.successors.put(key, successor);
            return (S) sequence.subsequence(1);
        }

        @Override
        public Node<S, V> onRemoveSuccessor(@NotNull final S sequence) {
            return this.successors.remove(sequence);
        }

        @Nullable
        @Override
        public Pair<Node<S, V>, S> getSuccessor(@NotNull final S sequence) {
            ensureNotNull(sequence, "The sequence may not be null");
            S key = (S) sequence.subsequence(0, 1);
            Node<S, V> node = this.successors.get(key);

            if (node != null) {
                S suffix = (S) sequence.subsequence(1);
                return Pair.create(node, suffix);
            }

            return null;
        }

        @NotNull
        @Override
        public Map<S, Node<S, V>> getAllSuccessors() {
            return Collections.unmodifiableMap(successors);
        }

        @Override
        public int getSuccessorCount() {
            return successors.size();
        }

        @Override
        public String toString() {
            return "Node{" +
                    "value=" + getValue() +
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

    private HashTrie(@Nullable final Node<SequenceType, ValueType> rootNode) {
        super(rootNode);
    }

    public HashTrie() {
        super();
    }

    @NotNull
    @Override
    public HashTrie<SequenceType, ValueType> subTree(@NotNull final SequenceType key) {
        Node<SequenceType, ValueType> node = getNode(key);

        if (node != null) {
            Node<SequenceType, ValueType> newRootNode = createNode();
            Node<SequenceType, ValueType> currentNode = newRootNode;

            for (int i = 0; i < key.length(); i++) {
                Node<SequenceType, ValueType> successor = createNode();
                SequenceType subsequence = (SequenceType) key.subsequence(i, i + 1);
                currentNode.addSuccessor(subsequence, successor);
                currentNode = successor;
            }

            for (Map.Entry<SequenceType, Node<SequenceType, ValueType>> entry : node
                    .getAllSuccessors().entrySet()) {
                currentNode.addSuccessor(entry.getKey(), new Node<>(entry.getValue()));
            }

            return new HashTrie<>(newRootNode);
        }

        throw new NoSuchElementException();
    }

    @Override
    protected final Node<SequenceType, ValueType> createNode() {
        return new Node<>();
    }

    @Override
    public final String toString() {
        return "HashTrie " + entrySet().toString();
    }

}
package de.mrapp.tries.datastructure;

import de.mrapp.tries.Sequence;
import de.mrapp.tries.Trie;
import de.mrapp.tries.datastructure.AbstractTrie.Node.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PatriciaHashTrie<SequenceType extends Sequence<SymbolType>, SymbolType, ValueType> extends
        AbstractTrie<SequenceType, SymbolType, ValueType, PatriciaHashTrie.Node<SymbolType, ValueType>> implements
        Trie<SequenceType, SymbolType, ValueType> {

    public static class Node<K, V> extends AbstractTrie.Node<K, V, Node<K, V>> {

        private static final long serialVersionUID = -222974481446772399L;

        private final Map<Key<K>, Node<K, V>> successors;

        public Node(@NotNull final Key<K> key) {
            super(key);
            this.successors = new HashMap<>();
        }

        @Override
        protected void onAddSuccessor(@NotNull final Key<K> key,
                                      @NotNull final Node<K, V> successor) {
            successors.put(key, successor);
        }

        @Override
        protected Node<K, V> onRemoveSuccessor(@NotNull final Key<K> key) {
            return successors.remove(key);
        }

        @Nullable
        @Override
        public Node<K, V> getSuccessor(@NotNull final Key<K> key) {
            return successors.get(key);
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

    private static final long serialVersionUID = 549869449450138904L;

    protected PatriciaHashTrie(
            @NotNull Sequence.Builder<SequenceType, SymbolType> sequenceBuilder) {
        super(sequenceBuilder);
    }

    @NotNull
    @Override
    public final Trie<SequenceType, SymbolType, ValueType> subTree(
            @NotNull final SequenceType key) {
        // TODO
        return null;
    }


    @Override
    protected final Node<SymbolType, ValueType> createNode(@NotNull final Key<SymbolType> key) {
        return new Node<>(key);
    }

}
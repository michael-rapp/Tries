package de.mrapp.tries;

import de.mrapp.tries.datastructure.AbstractTrie;
import de.mrapp.tries.datastructure.AbstractTrie.Node.Key;
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

        @Override
        public void removeSuccessor(@NotNull final Key<K> key) {
            ensureNotNull(key, "The key may not be null");
            this.successors.remove(key);
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

        // TODO: toString, hashCode, equals, clone

    }

    public HashTrie(@NotNull final Sequence.Builder<SequenceType, SymbolType> sequenceBuilder) {
        super(sequenceBuilder);
    }

    @NotNull
    @Override
    public Trie<SequenceType, SymbolType, ValueType> subTree(@NotNull final SequenceType key) {
        // TODO
        return null;
    }

    @Override
    protected final Node<SymbolType, ValueType> createNode(@NotNull final Key<SymbolType> key) {
        return new Node<>(key);
    }

}
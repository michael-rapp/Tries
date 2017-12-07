package de.mrapp.tries;

import de.mrapp.tries.datastructure.AbstractTrie;
import de.mrapp.tries.datastructure.HashNode;
import de.mrapp.tries.util.SequenceUtil;
import de.mrapp.util.datastructure.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;

public class HashTrie<SequenceType extends Sequence, ValueType> extends
        AbstractTrie<SequenceType, ValueType> {

    private static final long serialVersionUID = -2250393346732658811L;

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
            Node<SequenceType, ValueType> newRootNode = createRootNode();
            Node<SequenceType, ValueType> currentNode = newRootNode;
            SequenceType suffix = key;

            while (!suffix.isEmpty()) {
                Pair<Node<SequenceType, ValueType>, SequenceType> pair = addSuccessor(currentNode,
                        suffix);
                Node<SequenceType, ValueType> successor = pair.first;
                suffix = pair.second;
                currentNode = successor;
            }

            for (SequenceType sequence : node) {
                Node<SequenceType, ValueType> successor = node.getSuccessor(sequence);

                if (successor != null) {
                    currentNode.addSuccessor(sequence, successor.clone());
                }
            }

            return new HashTrie<>(newRootNode);
        }

        throw new NoSuchElementException();
    }

    @NotNull
    @Override
    protected final Node<SequenceType, ValueType> createRootNode() {
        return new HashNode<>();
    }

    @Nullable
    @Override
    protected final Pair<Node<SequenceType, ValueType>, SequenceType> getSuccessor(
            @NotNull final Node<SequenceType, ValueType> node,
            @NotNull final SequenceType sequence) {
        SequenceType prefix = SequenceUtil.subsequence(sequence, 0, 1);
        Node<SequenceType, ValueType> successor = node.getSuccessor(prefix);

        if (successor != null) {
            SequenceType suffix = SequenceUtil.subsequence(sequence, 1);
            return Pair.create(successor, suffix);
        }

        return null;
    }

    @NotNull
    @Override
    protected final Pair<Node<SequenceType, ValueType>, SequenceType> addSuccessor(
            @NotNull final Node<SequenceType, ValueType> node,
            @NotNull final SequenceType sequence) {
        SequenceType prefix = SequenceUtil.subsequence(sequence, 0, 1);
        Node<SequenceType, ValueType> successor = node.addSuccessor(prefix);
        SequenceType suffix = SequenceUtil.subsequence(sequence, 1);
        return Pair.create(successor, suffix);
    }

    @Override
    protected final void removeSuccessor(@NotNull final Node<SequenceType, ValueType> node,
                                         @NotNull final SequenceType sequence) {
        node.removeSuccessor(sequence);
    }

    @Override
    public final String toString() {
        return "HashTrie " + entrySet().toString();
    }

}
package de.mrapp.tries;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Map;

public interface Trie<SequenceType extends Sequence, ValueType> extends
        Map<SequenceType, ValueType>, Serializable {

    @Nullable
    Node<SequenceType, ValueType> getRootNode();

    @NotNull
    Trie<SequenceType, ValueType> subTree(@NotNull SequenceType key);

}
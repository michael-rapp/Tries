package de.mrapp.tries;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Map;

public interface Trie<SequenceType extends Sequence, ValueType> extends
        Map<SequenceType, ValueType>, Serializable {

    @NotNull
    Trie<SequenceType, ValueType> subTree(@NotNull SequenceType key);

}
package de.mrapp.tries;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Map;

public interface Trie<SequenceType extends Sequence<SymbolType>, SymbolType, ValueType> extends
        Map<SequenceType, ValueType>, Serializable {

    @NotNull
    Trie<SequenceType, SymbolType, ValueType> subTree(@NotNull SequenceType key);

}
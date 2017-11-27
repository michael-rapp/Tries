package de.mrapp.tries;

import org.jetbrains.annotations.NotNull;

import java.util.NavigableMap;

public interface SortedTrie<SequenceType extends Sequence<SymbolType>, SymbolType, ValueType> extends
        NavigableMap<SequenceType, ValueType>, Trie<SequenceType, SymbolType, ValueType> {

    @NotNull
    @Override
    SortedTrie<SequenceType, SymbolType, ValueType> subTree(@NotNull SequenceType key);

}
package de.mrapp.tries;

import java.util.NavigableMap;

public interface SortedTrie<SequenceType extends Sequence<SymbolType>, SymbolType, ValueType> extends
        NavigableMap<SequenceType, ValueType>, Trie<SequenceType, SymbolType, ValueType> {

    @Override
    SortedTrie<SequenceType, SymbolType, ValueType> subTree(Object key);

}
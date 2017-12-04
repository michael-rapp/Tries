package de.mrapp.tries;

import org.jetbrains.annotations.NotNull;

import java.util.NavigableMap;

public interface SortedTrie<SequenceType extends Sequence, ValueType>
        extends NavigableMap<SequenceType, ValueType>, Trie<SequenceType, ValueType> {

    @NotNull
    @Override
    SortedTrie<SequenceType, ValueType> subTree(@NotNull SequenceType key);

}
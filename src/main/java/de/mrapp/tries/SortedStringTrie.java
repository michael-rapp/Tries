package de.mrapp.tries;

import org.jetbrains.annotations.NotNull;

import java.util.NavigableMap;

public interface SortedStringTrie<ValueType> extends NavigableMap<String, ValueType>,
        StringTrie<ValueType> {

    @NotNull
    @Override
    SortedStringTrie<ValueType> subTree(@NotNull final String key);

}
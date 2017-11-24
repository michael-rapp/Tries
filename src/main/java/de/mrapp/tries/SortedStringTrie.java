package de.mrapp.tries;

public interface SortedStringTrie<ValueType> extends SortedTrie<StringSequence, String, ValueType> {

    @Override
    SortedStringTrie<ValueType> subTree(Object key);

}
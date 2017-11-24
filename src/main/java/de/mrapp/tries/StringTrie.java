package de.mrapp.tries;

public interface StringTrie<ValueType> extends Trie<StringSequence, String, ValueType> {

    @Override
    StringTrie<ValueType> subTree(Object key);

}
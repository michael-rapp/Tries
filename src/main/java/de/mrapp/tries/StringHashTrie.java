package de.mrapp.tries;

import de.mrapp.tries.datastructure.StringTrieWrapper;

public class StringHashTrie<ValueType> extends StringTrieWrapper<ValueType> {

    private static final long serialVersionUID = -7644211622938905272L;

    public StringHashTrie() {
        super(new HashTrie<>());
    }

    @Override
    public final String toString() {
        return "StringHashTrie " + entrySet().toString();
    }

}
package de.mrapp.tries;

import de.mrapp.tries.datastructure.StringTrieWrapper;
import de.mrapp.tries.sequence.StringSequence;

public class StringHashTrie<ValueType> extends StringTrieWrapper<ValueType> {

    private static final long serialVersionUID = -7644211622938905272L;

    public StringHashTrie() {
        super(new HashTrie<>(new StringSequence.Builder()));
    }

}
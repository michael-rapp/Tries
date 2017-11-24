package de.mrapp.tries;

import java.io.Serializable;
import java.util.Map;

public interface Trie<SequenceType extends Sequence<SymbolType>, SymbolType, ValueType> extends
        Map<SequenceType, ValueType>, Cloneable, Serializable {

    Trie<SequenceType, SymbolType, ValueType> subTree(Object key);

}
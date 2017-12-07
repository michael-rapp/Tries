package de.mrapp.tries;

import de.mrapp.tries.sequence.StringSequence;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Map;

public interface StringTrie<ValueType> extends Map<String, ValueType>, Serializable {

    // TODO: Use wrapper for node?
    @Nullable
    Node<StringSequence, ValueType> getRootNode();

    @NotNull
    StringTrie<ValueType> subTree(@NotNull String key);

}
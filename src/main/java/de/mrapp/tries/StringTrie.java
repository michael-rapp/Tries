package de.mrapp.tries;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Map;

public interface StringTrie<ValueType> extends Map<String, ValueType>, Serializable {

    // TODO: Use wrapper for node?
    @Nullable
    Node<String, ValueType> getRootNode();

    @NotNull
    StringTrie<ValueType> subTree(@NotNull String key);

}
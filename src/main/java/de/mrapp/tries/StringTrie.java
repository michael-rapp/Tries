package de.mrapp.tries;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Map;

public interface StringTrie<ValueType> extends Map<String, ValueType>, Serializable {

    @NotNull
    StringTrie<ValueType> subTree(@NotNull String key);

}
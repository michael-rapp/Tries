package de.mrapp.tries;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public interface Sequence<SymbolType> extends Iterable<SymbolType>, Serializable {

    interface Builder<SequenceType extends Sequence<SymbolType>, SymbolType> {

        SequenceType build(@NotNull Iterable<SymbolType> iterable);

    }

    int length();

}
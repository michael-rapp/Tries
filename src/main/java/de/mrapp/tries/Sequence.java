package de.mrapp.tries;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public interface Sequence extends Serializable {

    default Sequence subsequence(int start) {
        return subsequence(start, length());
    }

    Sequence subsequence(int start, int end);

    Sequence concat(@NotNull Sequence sequence);

    default boolean isEmpty() {
        return !(length() > 0);
    }

    int length();

}
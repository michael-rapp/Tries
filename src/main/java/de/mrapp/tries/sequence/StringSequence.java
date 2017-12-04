package de.mrapp.tries.sequence;

import de.mrapp.tries.Sequence;
import org.jetbrains.annotations.NotNull;

import static de.mrapp.util.Condition.ensureNotNull;

public class StringSequence implements Sequence {

    private static final long serialVersionUID = -5315067045883935279L;

    private final String string;

    public StringSequence(final String string) {
        ensureNotNull(string, "The string may not be null");
        this.string = string;
    }

    @Override
    public final Sequence subsequence(final int start, final int end) {
        return new StringSequence(string.substring(start, end));
    }

    @Override
    public final Sequence concat(@NotNull final Sequence sequence) {
        ensureNotNull(sequence, "The sequence may not be null");
        StringSequence stringSequence = (StringSequence) sequence;
        return new StringSequence(string.concat(stringSequence.string));
    }

    @Override
    public final int length() {
        return string.length();
    }

    @Override
    public final String toString() {
        return string;
    }

    @Override
    public final int hashCode() {
        return string.hashCode();
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StringSequence other = (StringSequence) obj;
        return string.equals(other.string);
    }

}
package de.mrapp.tries;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static de.mrapp.util.Condition.ensureNotNull;

public class StringSequence implements Sequence<String> {

    public static class Builder implements Sequence.Builder<StringSequence, String> {

        @Override
        public final StringSequence build(@NotNull final Iterable<String> iterable) {
            StringBuilder stringBuilder = new StringBuilder();
            iterable.forEach(stringBuilder::append);
            return new StringSequence(stringBuilder.toString());
        }

    }

    private static final long serialVersionUID = 8149493337253738209L;

    private final String string;

    public StringSequence(final String string) {
        ensureNotNull(string, "The string may not be null");
        this.string = string;
    }

    @NotNull
    @Override
    public Iterator<String> iterator() {
        return new Iterator<String>() {

            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < string.length();
            }

            @Override
            public String next() {
                String symbol = Character.toString(string.charAt(i));
                i++;
                return symbol;
            }

        };
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
package de.mrapp.tries;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class StringSequence implements Sequence<String> {

    private static final long serialVersionUID = 8149493337253738209L;

    private final String string;

    public StringSequence(final String string) {
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

}
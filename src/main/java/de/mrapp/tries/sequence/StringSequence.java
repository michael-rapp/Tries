/*
 * Copyright 2017 - 2018 Michael Rapp
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package de.mrapp.tries.sequence;

import de.mrapp.tries.Sequence;
import de.mrapp.tries.SortedStringTrie;
import de.mrapp.tries.StringTrie;
import de.mrapp.util.Condition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A character sequence, which is backed by a {@link String}. It can be used as the keys of a {@link
 * StringTrie} or {@link SortedStringTrie}.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class StringSequence implements Sequence, Comparable<StringSequence> {

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = -5315067045883935279L;

    /**
     * The string, which backs the character sequence.
     */
    private final String string;

    /**
     * Creates a new character sequence.
     *
     * @param string The string, which should back the sequence as a {@link String}. The string may
     *               not be null
     */
    public StringSequence(@NotNull final String string) {
        Condition.INSTANCE.ensureNotNull(string, "The string may not be null");
        this.string = string;
    }

    /**
     * Converts a specific {@link StringSequence} to a string.
     *
     * @param stringSequence The string sequence to convert as an instance of the class {@link
     *                       StringSequence} or null
     * @return The string, which has been created, as a {@link String} or null, if the given string
     * sequence is null
     */
    @Nullable
    public static String convertToString(@Nullable final StringSequence stringSequence) {
        return stringSequence != null ? stringSequence.toString() : null;
    }

    /**
     * Converts a specific {@link String} to a string sequence.
     *
     * @param string The string to convert as a {@link String} or null
     * @return The string sequence, which has been created, as an instance of the class {@link
     * StringSequence} or null, if the given string is null
     */
    @Nullable
    public static StringSequence convertFromString(@Nullable final String string) {
        return string != null ? new StringSequence(string) : null;
    }

    @Override
    public final Sequence subsequence(final int start, final int end) {
        return convertFromString(string.substring(start, end));
    }

    @Override
    public final Sequence concat(@NotNull final Sequence sequence) {
        Condition.INSTANCE.ensureNotNull(sequence, "The sequence may not be null");
        StringSequence stringSequence = (StringSequence) sequence;
        return convertFromString(string.concat(stringSequence.string));
    }

    @Override
    public final int length() {
        return string.length();
    }

    @Override
    public final int compareTo(@NotNull final StringSequence o) {
        return string.compareTo(o.string);
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
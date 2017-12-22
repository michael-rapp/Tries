/*
 * Copyright 2017 Michael Rapp
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
package de.mrapp.tries.datastructure;

import de.mrapp.tries.StringTrie;
import de.mrapp.tries.Trie;
import de.mrapp.tries.sequence.StringSequence;
import org.jetbrains.annotations.NotNull;

/**
 * A wrapper, which implements the interface {@link StringTrie} by delegating all method calls to an
 * encapsulated {@link Trie}.
 *
 * @param <ValueType> The type of the values, which are stored by the trie
 * @author Michael Rapp
 * @since 1.0.0
 */
public class StringTrieWrapper<ValueType> extends
        AbstractStringTrieWrapper<Trie<StringSequence, ValueType>, ValueType> implements
        StringTrie<ValueType> {

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = 7553230667894382156L;

    /**
     * Creates a new wrapper, which implements the interface {@link StringTrie}.
     *
     * @param trie The trie, which should be encapsulated, as an instance of the type {@link Trie}.
     *             The trie may not be null
     */
    public StringTrieWrapper(@NotNull final Trie<StringSequence, ValueType> trie) {
        super(trie);
    }

    @NotNull
    @Override
    public final StringTrieWrapper<ValueType> subTrie(@NotNull final String sequence) {
        return new StringTrieWrapper<>(trie.subTrie(new StringSequence(sequence)));
    }

}
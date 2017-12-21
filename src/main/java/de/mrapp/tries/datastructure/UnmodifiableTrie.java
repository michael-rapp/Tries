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

import de.mrapp.tries.Sequence;
import de.mrapp.tries.Trie;
import org.jetbrains.annotations.NotNull;

/**
 * An immutable {@link Trie}.
 *
 * @param <SequenceType> The type of the sequences, which are used as the trie's keys
 * @param <ValueType>    The type of the values, which are stored by trie
 * @author Michael Rapp
 * @since 1.0.0
 */
public class UnmodifiableTrie<SequenceType extends Sequence, ValueType> extends
        AbstractUnmodifiableTrie<SequenceType, ValueType, Trie<SequenceType, ValueType>> {

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = -2560009535838740381L;

    /**
     * Creates a new unmodifiable trie.
     *
     * @param trie The trie, which should be encapsulated, as an instance of the type {@link Trie}.
     *             The trie may not be null
     */
    public UnmodifiableTrie(@NotNull final Trie<SequenceType, ValueType> trie) {
        super(trie);
    }

    @NotNull
    @Override
    public final Trie<SequenceType, ValueType> subTree(@NotNull final SequenceType sequence) {
        return new UnmodifiableTrie<>(trie.subTree(sequence));
    }

}
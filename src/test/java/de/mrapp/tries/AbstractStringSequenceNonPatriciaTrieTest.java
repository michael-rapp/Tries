/*
 * Copyright 2017 - 2019 Michael Rapp
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
package de.mrapp.tries;

import de.mrapp.tries.sequence.StringSequence;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * An abstract base class for all tests, which test a {@link Trie} implementation, which is not a
 * Patricia trie and uses {@link StringSequence}s as keys.
 *
 * @param <TrieType> The type of the tested trie implementation
 * @author Michael Rapp
 */
public abstract class AbstractStringSequenceNonPatriciaTrieTest<TrieType extends Trie<StringSequence, String>>
        extends AbstractNonPatriciaTrieTest<StringSequence, TrieType> {

    @Override
    final Node<StringSequence, String> getRootNode(@NotNull final TrieType trie) {
        return trie.getRootNode();
    }

    @Override
    final StringSequence convertToSequence(@NotNull final String string) {
        return new StringSequence(string);
    }

}
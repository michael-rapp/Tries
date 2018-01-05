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
package de.mrapp.tries;

import org.junit.Before;
import org.junit.Test;

/**
 * An abstract base class for all tests, which test the functionality of a {@link Trie}
 * implementation.
 *
 * @param <SequenceType> The type of the sequences, which are used as the trie's keys
 * @param <ValueType>    The type of the values, which are stored by the trie
 * @author Michael Rapp
 */
public abstract class AbstractTrieTest<SequenceType extends Sequence, ValueType, TrieType extends Trie<SequenceType, ValueType>> {

    TrieType trie;

    abstract TrieType onCreateTrie();

    @Before
    public void before() {
        this.trie = onCreateTrie();
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testPutAllThrowsExceptionIfMapIsNull() {
        trie.putAll(null);
    }

}
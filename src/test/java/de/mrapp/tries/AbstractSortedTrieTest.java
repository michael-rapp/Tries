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

import de.mrapp.tries.sequence.StringSequence;
import org.junit.Test;

import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * An abstract base class for all tests, which test the functionality of a {@link SortedTrie}
 * implementation.
 *
 * @author Michael Rapp
 */
public abstract class AbstractSortedTrieTest<SequenceType extends Sequence, ValueType, TrieType extends SortedTrie<SequenceType, ValueType>> extends
        AbstractTrieTest<SequenceType, ValueType, TrieType> {

    @Test
    public final void testFirstEntryIfTrieIsEmpty() {
        assertNull(trie.firstEntry());
    }

    @Test(expected = NoSuchElementException.class)
    public final void testFirstKeyIfTrieIsEmpty() {
        trie.firstKey();
    }

    @Test
    public final void testLastEntryIfTrieIsEmpty() {
        assertNull(trie.lastEntry());
    }

    @Test(expected = NoSuchElementException.class)
    public final void testLastKeyIfTrieIsNull() {
        trie.lastKey();
    }

    @Test
    public final void testPollFirstEntryIfTrieIsEmpty() {
        Map.Entry<SequenceType, ValueType> removed = trie.pollFirstEntry();
        assertNull(removed);
        assertTrue(trie.isEmpty());
        assertNull(trie.getRootNode());
    }

    @Test
    public final void testPollLastEntryIfTrieIsEmpty() {
        Map.Entry<SequenceType, ValueType> removed = trie.pollLastEntry();
        assertNull(removed);
        assertTrue(trie.isEmpty());
        assertNull(trie.getRootNode());
    }

}
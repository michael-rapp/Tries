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

import org.junit.Test;

import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

/**
 * An abstract base class for all tests, which test the functionality of a {@link SortedTrie} implementation.
 *
 * @author Michael Rapp
 */
public abstract class AbstractSortedTrieTest<SequenceType extends Sequence, TrieType extends SortedTrie<SequenceType, String>>
        extends AbstractTrieTest<SequenceType, TrieType> {

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
        Map.Entry<SequenceType, String> removed = trie.pollFirstEntry();
        assertNull(removed);
        assertTrue(trie.isEmpty());
        assertNull(trie.getRootNode());
    }

    @Test
    public final void testPollLastEntryIfTrieIsEmpty() {
        Map.Entry<SequenceType, String> removed = trie.pollLastEntry();
        assertNull(removed);
        assertTrue(trie.isEmpty());
        assertNull(trie.getRootNode());
    }

    @Test
    public final void testFloorEntryIfKeyIsNotContained() {
        assertNull(trie.floorEntry(convertToSequence("foo")));
    }

    @Test
    public final void testCeilingEntryIfKeyIsNotContained() {
        assertNull(trie.ceilingEntry(convertToSequence("foo")));
    }

    @Test
    public final void testLastEntryIfNullKeyIsTheOnlyOne() {
        trie.put(null, "empty");
        Map.Entry<SequenceType, String> entry = trie.lastEntry();
        assertNotNull(entry);
        assertNull(entry.getKey());
        assertEquals("empty", entry.getValue());
    }

    @Test
    public final void testLastKeyIfNullKeyIsTheOnlyOne() {
        trie.put(null, "empty");
        SequenceType key = trie.lastKey();
        assertNull(key);
    }

    @Test
    public final void testPollLastEntryIfKeyIsEmpty() {
        String string = "empty";
        trie.put(convertToSequence(""), string);
        Map.Entry<SequenceType, String> removed = trie.pollLastEntry();
        assertNotNull(removed);
        assertNull(removed.getKey());
        assertEquals(string, removed.getValue());
        assertNull(trie.get(convertToSequence("")));
        assertNull(trie.get(null));
        assertEquals(0, trie.size());
        assertTrue(trie.isEmpty());
        assertNull(getRootNode(trie));
    }

    @Test
    public final void testLowerEntryIfKeyIsNotContained() {
        Map.Entry<SequenceType, String> entry = trie.lowerEntry(convertToSequence("foo"));
        assertNull(entry);
    }

}
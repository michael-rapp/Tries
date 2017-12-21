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
package de.mrapp.tries;

import de.mrapp.tries.datastructure.EmptySortedTrie;
import de.mrapp.tries.datastructure.EmptyTrie;
import de.mrapp.tries.sequence.StringSequence;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests the functionality of the class {@link Tries}.
 *
 * @author Michael Rapp
 */
public class TriesTest {

    @Test
    public final void testEmptyTrie() {
        Trie<StringSequence, String> trie = Tries.emptyTrie();
        assertTrue(trie instanceof EmptyTrie);
        assertEquals(Tries.emptyTrie(), trie);
        assertEquals(Tries.EMPTY_TRIE, trie);
    }

    @Test
    public final void testEmptySortedTrie() {
        SortedTrie<StringSequence, String> trie = Tries.emptySortedTrie();
        assertTrue(trie instanceof EmptySortedTrie);
        assertEquals(Tries.emptySortedTrie(), trie);
        assertEquals(Tries.EMPTY_SORTED_TRIE, trie);
    }

}
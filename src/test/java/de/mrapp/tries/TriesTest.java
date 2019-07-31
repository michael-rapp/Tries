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

import de.mrapp.tries.datastructure.*;
import de.mrapp.tries.sequence.StringSequence;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

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
        assertTrue(trie.isEmpty());
        assertEquals(Tries.emptyTrie(), trie);
        assertEquals(Tries.EMPTY_TRIE, trie);
    }

    @Test
    public final void testEmptySortedTrie() {
        SortedTrie<StringSequence, String> trie = Tries.emptySortedTrie();
        assertTrue(trie instanceof EmptySortedTrie);
        assertTrue(trie.isEmpty());
        assertEquals(Tries.emptySortedTrie(), trie);
        assertEquals(Tries.EMPTY_SORTED_TRIE, trie);
    }

    @Test
    public final void testEmptyStringTrie() {
        StringTrie<String> trie = Tries.emptyStringTrie();
        assertTrue(trie instanceof StringTrieWrapper);
        assertTrue(trie.isEmpty());
        assertEquals(Tries.emptyStringTrie(), trie);
        assertEquals(Tries.EMPTY_STRING_TRIE, trie);
    }

    @Test
    public final void testEmptySortedStringTrie() {
        SortedStringTrie<String> trie = Tries.emptySortedStringTrie();
        assertTrue(trie instanceof SortedStringTrieWrapper);
        assertTrue(trie.isEmpty());
        assertEquals(Tries.emptySortedStringTrie(), trie);
        assertEquals(Tries.EMPTY_SORTED_STRING_TRIE, trie);
    }

    @Test
    public final void testSingletonTrie() {
        StringSequence key = new StringSequence("foo");
        String value = "bar";
        Trie<StringSequence, String> trie = Tries.singletonTrie(key, value);
        assertTrue(trie instanceof SingletonTrie);
        assertEquals(1, trie.size());
        assertTrue(trie.containsKey(key));
        assertTrue(trie.containsValue(value));
    }

    @Test
    public final void testSingletonSortedTrie() {
        StringSequence key = new StringSequence("foo");
        String value = "bar";
        SortedTrie<StringSequence, String> trie = Tries.singletonSortedTrie(key, value);
        assertTrue(trie instanceof SingletonSortedTrie);
        assertEquals(1, trie.size());
        assertTrue(trie.containsKey(key));
        assertTrue(trie.containsValue(value));
    }

    @Test
    public final void testSingletonStringTrie() {
        String key = "foo";
        String value = "value";
        StringTrie<String> trie = Tries.singletonStringTrie(key, value);
        assertTrue(trie instanceof StringTrieWrapper);
        assertEquals(1, trie.size());
        assertTrue(trie.containsKey(key));
        assertTrue(trie.containsValue(value));
    }

    @Test
    public final void testSingletonSortedStringTrie() {
        String key = "foo";
        String value = "bar";
        SortedStringTrie<String> trie = Tries.singletonSortedStringTrie(key, value);
        assertTrue(trie instanceof SortedStringTrieWrapper);
        assertEquals(1, trie.size());
        assertTrue(trie.containsKey(key));
        assertTrue(trie.containsValue(value));
    }

    @Test
    public final void testUnmodifiableTrie() {
        Trie<StringSequence, String> trie = mock(Trie.class);
        Trie<StringSequence, String> unmodifiableTrie = Tries.unmodifiableTrie(trie);
        assertTrue(unmodifiableTrie instanceof UnmodifiableTrie);
        unmodifiableTrie.isEmpty();
        verify(trie, times(1)).isEmpty();
    }

    @Test
    public final void testUnmodifiableSortedTrie() {
        SortedTrie<StringSequence, String> trie = mock(SortedTrie.class);
        SortedTrie<StringSequence, String> unmodifiableTrie = Tries.unmodifiableSortedTrie(trie);
        assertTrue(unmodifiableTrie instanceof UnmodifiableSortedTrie);
        unmodifiableTrie.isEmpty();
        verify(trie, times(1)).isEmpty();
    }

    @Test
    public final void testUnmodifiableStringTrie() {
        StringTrie<String> trie = mock(StringTrie.class);
        StringTrie<String> unmodifiableTrie = Tries.unmodifiableStringTrie(trie);
        assertTrue(unmodifiableTrie instanceof UnmodifiableStringTrie);
        unmodifiableTrie.isEmpty();
        verify(trie, times(1)).isEmpty();
    }

    @Test
    public final void testUnmodifiableSortedStringTrie() {
        SortedStringTrie<String> trie = mock(SortedStringTrie.class);
        SortedStringTrie<String> unmodifiableTrie = Tries.unmodifiableSortedStringTrie(trie);
        assertTrue(unmodifiableTrie instanceof UnmodifiableSortedStringTrie);
        unmodifiableTrie.isEmpty();
        verify(trie, times(1)).isEmpty();
    }

}
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
package de.mrapp.tries.datastructure;

import de.mrapp.tries.Trie;
import de.mrapp.tries.sequence.StringSequence;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Tests the functionality of the class {@link EmptyTrie}.
 *
 * @author Michael Rapp
 */
public class EmptyTrieTest {

    private final Trie<StringSequence, String> trie = new EmptyTrie<>();

    @Test
    public final void testSize() {
        assertEquals(0, trie.size());
    }

    @Test
    public final void testIsEmpty() {
        assertTrue(trie.isEmpty());
    }

    @Test
    public final void testContainsKey() {
        assertFalse(trie.containsKey(new StringSequence("foo")));
    }

    @Test
    public final void testContainsValue() {
        assertFalse(trie.containsValue("bar"));
    }

    @Test
    public final void testGet() {
        assertNull(trie.get(new StringSequence("foo")));
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testPut() {
        trie.put(new StringSequence("foo"), "bar");
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testRemove() {
        trie.remove(new StringSequence("foo"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testPutAll() {
        trie.putAll(Collections.singletonMap(new StringSequence("foo"), "bar"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testClear() {
        trie.clear();
    }

    @Test
    public final void testKeySet() {
        Set<StringSequence> set = trie.keySet();
        assertNotNull(set);
        assertTrue(set.isEmpty());
    }

    @Test
    public final void testValues() {
        Collection<String> values = trie.values();
        assertNotNull(values);
        assertTrue(values.isEmpty());
    }

    @Test
    public final void testEntrySet() {
        Set<Map.Entry<StringSequence, String>> set = trie.entrySet();
        assertNotNull(set);
        assertTrue(set.isEmpty());
    }

    @Test
    public final void testGetRootNode() {
        assertNull(trie.getRootNode());
    }

    @Test(expected = NoSuchElementException.class)
    public final void testSubTree() {
        trie.subTrie(new StringSequence("foo"));
    }

    @Test
    public final void testToString() {
        assertEquals("EmptyTrie[]", trie.toString());
    }

}
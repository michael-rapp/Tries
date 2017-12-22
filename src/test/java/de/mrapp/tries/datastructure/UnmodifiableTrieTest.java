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

import de.mrapp.tries.Node;
import de.mrapp.tries.Trie;
import de.mrapp.tries.sequence.StringSequence;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests the functionality of the class {@link UnmodifiableTrie}.
 *
 * @author Michael Rapp
 */
public class UnmodifiableTrieTest {

    private final Trie<StringSequence, String> trie = mock(Trie.class);

    private final Trie<StringSequence, String> unmodifiableTrie = new UnmodifiableTrie<>(trie);

    @Test
    public final void testSize() {
        int size = 1;
        when(trie.size()).thenReturn(size);
        assertEquals(size, unmodifiableTrie.size());
    }

    @Test
    public final void testIsEmpty() {
        boolean isEmpty = true;
        when(trie.isEmpty()).thenReturn(isEmpty);
        assertEquals(isEmpty, unmodifiableTrie.isEmpty());
    }

    @Test
    public final void testContainsKey() {
        StringSequence key = new StringSequence("foo");
        boolean containsKey = true;
        when(trie.containsKey(key)).thenReturn(containsKey);
        assertEquals(containsKey, unmodifiableTrie.containsKey(key));
    }

    @Test
    public final void testContainsValue() {
        String value = "bar";
        boolean containsValue = true;
        when(trie.containsValue(value)).thenReturn(containsValue);
        assertEquals(containsValue, unmodifiableTrie.containsValue(value));
    }

    @Test
    public final void testGet() {
        StringSequence key = new StringSequence("foo");
        String value = "bar";
        when(trie.get(key)).thenReturn(value);
        assertEquals(value, unmodifiableTrie.get(key));
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testPut() {
        unmodifiableTrie.put(new StringSequence("foo"), "bar");
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testRemove() {
        unmodifiableTrie.remove(new StringSequence("foo"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testPutAll() {
        unmodifiableTrie.putAll(Collections.singletonMap(new StringSequence("foo"), "bar"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testClear() {
        unmodifiableTrie.clear();
    }

    @Test
    public final void testKeySet() {
        Set<StringSequence> keySet = new HashSet<>();
        keySet.add(new StringSequence("foo"));
        when(trie.keySet()).thenReturn(keySet);
        Set<StringSequence> set = unmodifiableTrie.keySet();
        assertEquals(keySet, set);

        try {
            set.add(new StringSequence("foo"));
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public final void testValues() {
        Collection<String> values = new LinkedList<>();
        values.add("bar");
        when(trie.values()).thenReturn(values);
        Collection<String> collection = unmodifiableTrie.values();
        assertEquals(values.size(), collection.size());
        assertTrue(values.stream().allMatch(collection::contains));

        try {
            collection.add("foo");
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public final void testEntrySet() {
        Set<Map.Entry<StringSequence, String>> entrySet = new HashSet<>();
        entrySet.add(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("foo"), "bar"));
        when(trie.entrySet()).thenReturn(entrySet);
        Set<Map.Entry<StringSequence, String>> set = unmodifiableTrie.entrySet();
        assertEquals(entrySet, set);

        try {
            set.add(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("what"), "ever"));
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public final void testGetRootNode() {
        Node<StringSequence, String> rootNode = mock(Node.class);
        when(trie.getRootNode()).thenReturn(rootNode);
        assertEquals(rootNode, unmodifiableTrie.getRootNode());
    }

    @Test
    public final void testSubTree() {
        StringSequence sequence = new StringSequence("foo");
        Trie<StringSequence, String> subTrie = mock(Trie.class);
        when(trie.subTrie(sequence)).thenReturn(subTrie);
        Trie<StringSequence, String> result = unmodifiableTrie.subTrie(sequence);
        assertTrue(result instanceof UnmodifiableTrie);
        assertEquals(result, subTrie);
    }

    @Test
    public final void testToString() {
        assertEquals(trie.toString(), unmodifiableTrie.toString());
    }

    @Test
    public final void testHashCode() {
        assertEquals(trie.hashCode(), unmodifiableTrie.hashCode());
    }

    @Test
    public final void testEquals() {
        assertTrue(unmodifiableTrie.equals(trie));
    }

}
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
import de.mrapp.tries.StringTrie;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests the functionality of the class {@link UnmodifiableStringTrie}.
 *
 * @author Michael Rapp
 */
public class UnmodifiableStringTrieTest {

    private final StringTrie<String> trie = mock(StringTrie.class);

    private final StringTrie<String> unmodifiableTrie = new UnmodifiableStringTrie<>(trie);

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
        String key = "foo";
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
        String key = "foo";
        String value = "bar";
        when(trie.get(key)).thenReturn(value);
        assertEquals(value, unmodifiableTrie.get(key));
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testPut() {
        unmodifiableTrie.put("foo", "bar");
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testRemove() {
        unmodifiableTrie.remove("foo");
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testPutAll() {
        unmodifiableTrie.putAll(Collections.singletonMap("foo", "bar"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testClear() {
        unmodifiableTrie.clear();
    }

    @Test
    public final void testKeySet() {
        Set<String> keySet = new HashSet<>();
        keySet.add("foo");
        when(trie.keySet()).thenReturn(keySet);
        Set<String> set = unmodifiableTrie.keySet();
        assertEquals(keySet, set);

        try {
            set.add("foo");
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
        Set<Map.Entry<String, String>> entrySet = new HashSet<>();
        entrySet.add(new AbstractMap.SimpleImmutableEntry<>("foo", "bar"));
        when(trie.entrySet()).thenReturn(entrySet);
        Set<Map.Entry<String, String>> set = unmodifiableTrie.entrySet();
        assertEquals(entrySet, set);

        try {
            set.add(new AbstractMap.SimpleImmutableEntry<>("what", "ever"));
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public final void testGetRootNode() {
        Node<String, String> rootNode = mock(Node.class);
        when(trie.getRootNode()).thenReturn(rootNode);
        assertEquals(rootNode, unmodifiableTrie.getRootNode());
    }

    @Test
    public final void testSubTree() {
        String key = "foo";
        StringTrie<String> subTrie = mock(StringTrie.class);
        when(trie.subTrie(key)).thenReturn(subTrie);
        StringTrie<String> result = unmodifiableTrie.subTrie(key);
        assertTrue(result instanceof UnmodifiableStringTrie);
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
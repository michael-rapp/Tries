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
package de.mrapp.tries.datastructure;

import de.mrapp.tries.HashTrie;
import de.mrapp.tries.StringTrie;
import de.mrapp.tries.Trie;
import de.mrapp.tries.sequence.StringSequence;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests the functionality of the class {@link StringTrieWrapper}.
 *
 * @author Michael Rapp
 */
public class StringTrieWrapperTest {

    private StringTrieWrapper<String> trieWrapper;

    private Trie<StringSequence, String> trie;

    @SuppressWarnings("unchecked")
    @Before
    public final void before() {
        trie = mock(Trie.class);
        trieWrapper = new StringTrieWrapper<>(trie);
    }

    @Test
    public final void testSize() {
        int size = 2;
        when(trie.size()).thenReturn(size);
        assertEquals(size, trieWrapper.size());
    }

    @Test
    public final void testIsEmpty() {
        boolean empty = true;
        when(trie.isEmpty()).thenReturn(empty);
        assertEquals(empty, trieWrapper.isEmpty());
    }

    @Test
    public final void testContainsKey() {
        String key = "key";
        boolean containsKey = true;
        when(trie.containsKey(new StringSequence(key))).thenReturn(containsKey);
        assertEquals(containsKey, trieWrapper.containsKey(key));
    }

    @Test
    public final void testContainsKeyWithNullParameter() {
        boolean containsKey = true;
        when(trie.containsKey(null)).thenReturn(containsKey);
        assertEquals(containsKey, trieWrapper.containsKey(null));
    }

    @Test
    public final void testContainsValue() {
        String value = "value";
        boolean containsValue = true;
        when(trie.containsValue(value)).thenReturn(containsValue);
        assertEquals(containsValue, trieWrapper.containsValue(value));
    }

    @Test
    public final void testGet() {
        String key = "key";
        String value = "value";
        when(trie.get(new StringSequence(key))).thenReturn(value);
        assertEquals(value, trieWrapper.get(key));
    }

    @Test
    public final void testGetWithNullParameter() {
        String value = "value";
        when(trie.get(null)).thenReturn(value);
        assertEquals(value, trieWrapper.get(null));
    }

    @Test
    public final void testPut() {
        String key = "key";
        String value = "value";
        String previous = "previous";
        when(trie.put(new StringSequence(key), value)).thenReturn(previous);
        assertEquals(previous, trieWrapper.put(key, value));
    }

    @Test
    public final void testPutWithNullParameter() {
        String value = "value";
        String previous = "previous";
        when(trie.put(null, value)).thenReturn(previous);
        assertEquals(previous, trieWrapper.put(null, value));
    }

    @Test
    public final void testRemove() {
        String key = "key";
        String value = "value";
        when(trie.remove(new StringSequence(key))).thenReturn(value);
        assertEquals(value, trieWrapper.remove(key));
    }

    @Test
    public final void testRemoveWithNullParameter() {
        String value = "value";
        when(trie.remove(null)).thenReturn(value);
        assertEquals(value, trieWrapper.remove(null));
    }

    @Test
    public final void testPutAll() {
        String key1 = "key1";
        String key2 = "key2";
        String value1 = "value1";
        String value2 = "value2";
        Map<String, String> map = new HashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        trieWrapper.putAll(map);
        verify(trie, times(1)).put(new StringSequence(key1), value1);
        verify(trie, times(1)).put(new StringSequence(key2), value2);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testPutAllThrowsExceptionIfMapIsNull() {
        trieWrapper.putAll(null);
    }

    @Test
    public final void testClear() {
        trieWrapper.clear();
        verify(trie, times(1)).clear();
    }

    @Test
    public final void testKeySetIterator() {
        String key1 = "key1";
        String key2 = "key2";
        Set<StringSequence> keySet = new HashSet<>();
        keySet.add(new StringSequence(key1));
        keySet.add(new StringSequence(key2));
        keySet.add(null);
        when(trie.keySet()).thenReturn(keySet);
        Iterator<String> iterator = trieWrapper.keySet().iterator();
        Collection<String> actualKeys = new ArrayList<>();
        actualKeys.add(key1);
        actualKeys.add(key2);
        actualKeys.add(null);

        for (int i = 0; i < keySet.size(); i++) {
            assertTrue(iterator.hasNext());
            String key = iterator.next();
            assertTrue(actualKeys.contains(key));
            actualKeys.remove(key);
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    public final void testKeySetRemove() {
        String key1 = "key1";
        String key2 = "key2";
        Set<StringSequence> keySet = new HashSet<>();
        keySet.add(new StringSequence(key1));
        keySet.add(new StringSequence(key2));
        keySet.add(null);
        when(trie.keySet()).thenReturn(keySet);
        Set<String> keys = trieWrapper.keySet();
        boolean removed = keys.remove(key2);
        assertTrue(removed);
        assertEquals(2, keys.size());
        assertTrue(keys.contains(key1));
        assertFalse(keys.contains(key2));
        assertTrue(keys.contains(null));
        assertEquals(2, keySet.size());
        assertTrue(keySet.contains(new StringSequence(key1)));
        assertFalse(keySet.contains(new StringSequence(key2)));
        assertTrue(keySet.contains(null));
    }

    @Test
    public final void testKeySetClear() {
        Set<StringSequence> keySet = new HashSet<>();
        keySet.add(new StringSequence("key1"));
        keySet.add(new StringSequence("key2"));
        keySet.add(null);
        when(trie.keySet()).thenReturn(keySet);
        Set<String> keys = trieWrapper.keySet();
        assertFalse(keys.isEmpty());
        assertFalse(keySet.isEmpty());
        keys.clear();
        assertTrue(keys.isEmpty());
        assertTrue(keySet.isEmpty());
    }

    @Test
    public final void testKeySetSize() {
        Set<StringSequence> keySet = new HashSet<>();
        keySet.add(new StringSequence("key1"));
        keySet.add(new StringSequence("key2"));
        keySet.add(null);
        when(trie.keySet()).thenReturn(keySet);
        Set<String> keys = trieWrapper.keySet();
        assertEquals(keys.size(), keySet.size());
    }

    @Test
    public final void testValues() {
        Collection<String> values = new LinkedList<>();
        values.add("value1");
        values.add("value2");
        when(trie.values()).thenReturn(values);
        assertEquals(values, trieWrapper.values());
    }

    @Test
    public final void testEntrySetIterator() {
        String key1 = "key1";
        String value1 = "value1";
        String key2 = "key2";
        String value2 = "value2";
        String value3 = "value3";
        Set<Map.Entry<StringSequence, String>> entrySet = new HashSet<>();
        entrySet.add(new AbstractMap.SimpleImmutableEntry<>(new StringSequence(key1), value1));
        entrySet.add(new AbstractMap.SimpleImmutableEntry<>(new StringSequence(key2), value2));
        entrySet.add(new AbstractMap.SimpleImmutableEntry<>(null, value3));
        when(trie.entrySet()).thenReturn(entrySet);
        Iterator<Map.Entry<String, String>> iterator = trieWrapper.entrySet().iterator();
        Collection<Map.Entry<String, String>> actualEntries = new ArrayList<>();
        actualEntries.add(new AbstractMap.SimpleImmutableEntry<>(key1, value1));
        actualEntries.add(new AbstractMap.SimpleImmutableEntry<>(key2, value2));
        actualEntries.add(new AbstractMap.SimpleImmutableEntry<>(null, value3));

        for (int i = 0; i < entrySet.size(); i++) {
            assertTrue(iterator.hasNext());
            Map.Entry<String, String> entry = iterator.next();
            assertTrue(actualEntries.contains(entry));
            actualEntries.remove(entry);
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    public final void testEntrySetRemove() {
        String key1 = "key1";
        String value1 = "value1";
        String key2 = "key2";
        String value2 = "value2";
        String value3 = "value3";
        Set<Map.Entry<StringSequence, String>> entrySet = new HashSet<>();
        entrySet.add(new AbstractMap.SimpleImmutableEntry<>(new StringSequence(key1), value1));
        entrySet.add(new AbstractMap.SimpleImmutableEntry<>(new StringSequence(key2), value2));
        entrySet.add(new AbstractMap.SimpleImmutableEntry<>(null, value3));
        when(trie.entrySet()).thenReturn(entrySet);
        Set<Map.Entry<String, String>> entries = trieWrapper.entrySet();
        boolean removed = entries.remove(new AbstractMap.SimpleImmutableEntry<>(key2, "invalid"));
        assertFalse(removed);
        removed = entries.remove(new AbstractMap.SimpleImmutableEntry<>(key2, value2));
        assertTrue(removed);
        assertEquals(2, entries.size());
        assertTrue(entries.contains(new AbstractMap.SimpleImmutableEntry<>(key1, value1)));
        assertFalse(entries.contains(new AbstractMap.SimpleImmutableEntry<>(key2, value2)));
        assertTrue(entries.contains(new AbstractMap.SimpleImmutableEntry<>((String) null, value3)));
        assertEquals(2, entrySet.size());
        assertTrue(entrySet.contains(
                new AbstractMap.SimpleImmutableEntry<>(new StringSequence(key1), value1)));
        assertFalse(entrySet.contains(
                new AbstractMap.SimpleImmutableEntry<>(new StringSequence(key2), value2)));
        assertTrue(entrySet.contains(
                new AbstractMap.SimpleImmutableEntry<>((StringSequence) null, value3)));
    }

    @Test
    public final void testEntrySetClear() {
        String key1 = "key1";
        String value1 = "value1";
        String key2 = "key2";
        String value2 = "value2";
        String value3 = "value3";
        Set<Map.Entry<StringSequence, String>> entrySet = new HashSet<>();
        entrySet.add(new AbstractMap.SimpleImmutableEntry<>(new StringSequence(key1), value1));
        entrySet.add(new AbstractMap.SimpleImmutableEntry<>(new StringSequence(key2), value2));
        entrySet.add(new AbstractMap.SimpleImmutableEntry<>(null, value3));
        when(trie.entrySet()).thenReturn(entrySet);
        Set<Map.Entry<String, String>> entries = trieWrapper.entrySet();
        assertFalse(entries.isEmpty());
        assertFalse(entrySet.isEmpty());
        entries.clear();
        assertTrue(entries.isEmpty());
        assertTrue(entrySet.isEmpty());
    }

    @Test
    public final void testEntrySetSize() {
        Set<Map.Entry<StringSequence, String>> entrySet = new HashSet<>();
        entrySet.add(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("key1"), "value1"));
        entrySet.add(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("key2"), "value2"));
        entrySet.add(new AbstractMap.SimpleImmutableEntry<>(null, "value3"));
        when(trie.entrySet()).thenReturn(entrySet);
        Set<Map.Entry<String, String>> entries = trieWrapper.entrySet();
        assertEquals(entries.size(), entrySet.size());
    }

    @SuppressWarnings("unchecked")
    @Test
    public final void testSubTree() {
        String key = "key";
        Trie<StringSequence, String> subTrie = mock(Trie.class);
        when(trie.subTrie(new StringSequence(key))).thenReturn(subTrie);
        StringTrie<String> subStringTrie = trieWrapper.subTrie(key);
        assertTrue(subStringTrie instanceof StringTrieWrapper);
        assertEquals(subTrie, ((StringTrieWrapper) subStringTrie).trie);
    }

    @Test
    public final void testHashCode() {
        Trie<StringSequence, String> trie1 = new HashTrie<>();
        Trie<StringSequence, String> trie2 = new HashTrie<>();
        StringTrieWrapper<String> trieWrapper1 = new StringTrieWrapper<>(trie1);
        StringTrieWrapper<String> trieWrapper2 = new StringTrieWrapper<>(trie2);
        assertEquals(trieWrapper1.hashCode(), trieWrapper2.hashCode());
        assertEquals(trieWrapper1.hashCode(), trieWrapper2.hashCode());
        trieWrapper1.put("key", "value");
        assertNotEquals(trieWrapper1.hashCode(), trieWrapper2.hashCode());
        trieWrapper2.put("key", "value");
        assertEquals(trieWrapper1.hashCode(), trieWrapper2.hashCode());
    }

    @Test
    public final void testEquals() {
        Trie<StringSequence, String> trie1 = new HashTrie<>();
        Trie<StringSequence, String> trie2 = new HashTrie<>();
        StringTrieWrapper<String> trieWrapper1 = new StringTrieWrapper<>(trie1);
        StringTrieWrapper<String> trieWrapper2 = new StringTrieWrapper<>(trie2);
        assertFalse(trieWrapper1.equals(null));
        assertFalse(trieWrapper1.equals(new Object()));
        assertTrue(trieWrapper1.equals(trieWrapper1));
        assertTrue(trieWrapper1.equals(trieWrapper2));
        trieWrapper1.put("key", "value");
        assertFalse(trieWrapper1.equals(trieWrapper2));
        trieWrapper2.put("key", "value");
        assertTrue(trieWrapper1.equals(trieWrapper2));
    }

}
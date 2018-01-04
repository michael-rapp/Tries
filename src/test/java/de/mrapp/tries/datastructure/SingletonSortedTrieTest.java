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

import de.mrapp.tries.Node;
import de.mrapp.tries.SortedTrie;
import de.mrapp.tries.Trie;
import de.mrapp.tries.sequence.StringSequence;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Tests the functionality of the class {@link SingletonSortedTrie}.
 *
 * @author Michael Rapp
 */
public class SingletonSortedTrieTest {

    private final StringSequence key = new StringSequence("foo");

    private final String value = "bar";

    private final SortedTrie<StringSequence, String> trie = new SingletonSortedTrie<>(key, value);

    @Test
    public final void testSize() {
        assertEquals(1, trie.size());
    }

    @Test
    public final void testIsEmpty() {
        assertFalse(trie.isEmpty());
    }

    @Test
    public final void testContainsKey() {
        assertFalse(trie.containsKey(new StringSequence("nop")));
        assertTrue(trie.containsKey(key));
    }

    @Test
    public final void testContainsValue() {
        assertFalse(trie.containsValue("nop"));
        assertTrue(trie.containsValue(value));
    }

    @Test
    public final void testGet() {
        assertNull(trie.get(new StringSequence("nop")));
        assertEquals(value, trie.get(key));
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
        assertEquals(1, set.size());
        assertEquals(key, set.iterator().next());
    }

    @Test
    public final void testValues() {
        Collection<String> values = trie.values();
        assertNotNull(values);
        assertEquals(1, values.size());
        assertEquals(value, values.iterator().next());
    }

    @Test
    public final void testEntrySet() {
        Set<Map.Entry<StringSequence, String>> set = trie.entrySet();
        assertNotNull(set);
        assertEquals(1, set.size());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(key, value), set.iterator().next());
    }

    @Test
    public final void testGetRootNode() {
        Node<StringSequence, String> rootNode = trie.getRootNode();
        assertTrue(rootNode instanceof UnmodifiableNode);
        assertNull(rootNode.getNodeValue());
        Node<StringSequence, String> successor = rootNode.getSuccessor(key);
        assertNotNull(successor);
        assertEquals(value, successor.getValue());
        assertFalse(successor.hasSuccessors());
    }

    @Test
    public final void testGetRootNodeIfKeyIsNull() {
        Trie<StringSequence, String> trie = new SingletonTrie<>(null, value);
        Node<StringSequence, String> rootNode = trie.getRootNode();
        assertTrue(rootNode instanceof UnmodifiableNode);
        assertEquals(value, rootNode.getValue());
        assertFalse(rootNode.hasSuccessors());
    }

    @Test
    public final void testLowerEntry() {
        assertNull(trie.lowerEntry(new StringSequence("foo")));
    }

    @Test(expected = NoSuchElementException.class)
    public final void testLowerKey() {
        trie.lowerKey(new StringSequence("foo"));
    }

    @Test
    public final void testFloorEntry() {
        assertNull(trie.floorEntry(new StringSequence("foo")));
    }

    @Test(expected = NoSuchElementException.class)
    public final void testFloorKey() {
        trie.floorKey(new StringSequence("foo"));
    }

    @Test
    public final void testCeilingEntry() {
        assertNull(trie.ceilingEntry(new StringSequence("foo")));
    }

    @Test(expected = NoSuchElementException.class)
    public final void testCeilingKey() {
        trie.ceilingKey(new StringSequence("foo"));
    }

    @Test
    public final void testHigherEntry() {
        assertNull(trie.higherEntry(new StringSequence("foo")));
    }

    @Test(expected = NoSuchElementException.class)
    public final void testHigherKey() {
        trie.higherKey(new StringSequence("foo"));
    }

    @Test
    public final void testFirstEntry() {
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(key, value), trie.firstEntry());
    }

    @Test
    public final void testLastEntry() {
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(key, value), trie.lastEntry());
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testPollFirstEntry() {
        trie.pollFirstEntry();
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testPollLastEntry() {
        trie.pollLastEntry();
    }

    @Test
    public final void testDescendingMap() {
        NavigableMap<StringSequence, String> map = trie.descendingMap();
        assertNotNull(map);
        assertEquals(1, map.size());
        assertTrue(map.containsKey(key));
        assertTrue(map.containsValue(value));
    }

    @Test
    public final void testNavigableKeySet() {
        NavigableSet<StringSequence> set = trie.navigableKeySet();
        assertNotNull(set);
        assertEquals(1, set.size());
        assertTrue(set.contains(key));
    }

    @Test
    public final void testDescendingKeySet() {
        NavigableSet<StringSequence> set = trie.descendingKeySet();
        assertNotNull(set);
        assertEquals(1, set.size());
        assertTrue(set.contains(key));
    }

    @Test
    public final void testSubMap1() {
        NavigableMap<StringSequence, String> map = trie
                .subMap(new StringSequence("from"), true, new StringSequence("to"), true);
        assertNotNull(map);
        assertTrue(map.isEmpty());
        map = trie.subMap(key, true, key, true);
        assertNotNull(map);
        assertEquals(1, map.size());
        assertTrue(map.containsKey(key));
        assertTrue(map.containsValue(value));
    }

    @Test
    public final void testSubMap2() {
        SortedMap<StringSequence, String> map = trie
                .subMap(new StringSequence("from"), new StringSequence("to"));
        assertNotNull(map);
        assertTrue(map.isEmpty());
        map = trie.subMap(key, key);
        assertNotNull(map);
        assertEquals(1, map.size());
        assertTrue(map.containsKey(key));
        assertTrue(map.containsValue(value));
    }

    @Test
    public final void testHeadMap1() {
        NavigableMap<StringSequence, String> map = trie.headMap(new StringSequence("to"), true);
        assertNotNull(map);
        assertTrue(map.isEmpty());
        map = trie.headMap(key, true);
        assertNotNull(map);
        assertEquals(1, map.size());
        assertTrue(map.containsKey(key));
        assertTrue(map.containsValue(value));
    }

    @Test
    public final void testHeadMap2() {
        SortedMap<StringSequence, String> map = trie.headMap(new StringSequence("to"));
        assertNotNull(map);
        assertTrue(map.isEmpty());
        map = trie.headMap(key);
        assertNotNull(map);
        assertEquals(1, map.size());
        assertTrue(map.containsKey(key));
        assertTrue(map.containsValue(value));
    }

    @Test
    public final void testTailMap1() {
        NavigableMap<StringSequence, String> map = trie.tailMap(new StringSequence("from"), true);
        assertNotNull(map);
        assertTrue(map.isEmpty());
        map = trie.tailMap(key, true);
        assertNotNull(map);
        assertEquals(1, map.size());
        assertTrue(map.containsKey(key));
        assertTrue(map.containsValue(value));
    }

    @Test
    public final void testTailMap2() {
        SortedMap<StringSequence, String> map = trie.tailMap(new StringSequence("from"));
        assertNotNull(map);
        assertTrue(map.isEmpty());
        map = trie.tailMap(key);
        assertNotNull(map);
        assertEquals(1, map.size());
        assertTrue(map.containsKey(key));
        assertTrue(map.containsValue(value));
    }

    @Test
    public final void testComparator() {
        assertNull(trie.comparator());
    }

    @Test
    public final void testFirstKey() {
        assertEquals(key, trie.firstKey());
    }

    @Test
    public final void testLastKey() {
        assertEquals(key, trie.lastKey());
    }

    @Test
    public final void testSubTreeIfKeyIsContained() {
        Trie<StringSequence, String> subTrie = trie.subTrie(key);
        assertNotNull(subTrie);
        assertTrue(subTrie instanceof SingletonSortedTrie);
        assertEquals(trie, subTrie);
    }

    @Test(expected = NoSuchElementException.class)
    public final void testSubTree() {
        trie.subTrie(new StringSequence("nop"));
    }

    @Test
    public final void testToString() {
        assertEquals("SingletonSortedTrie[foo=bar]", trie.toString());
    }

    @Test
    public final void testHashCode() {
        SortedTrie<StringSequence, String> trie1 = new SingletonSortedTrie<>(null, null);
        SortedTrie<StringSequence, String> trie2 = new SingletonSortedTrie<>(null, null);
        assertEquals(trie1.hashCode(), trie1.hashCode());
        assertEquals(trie1.hashCode(), trie2.hashCode());
        trie1 = new SingletonSortedTrie<>(new StringSequence("key1"), null);
        assertNotEquals(trie1.hashCode(), trie2.hashCode());
        trie2 = new SingletonSortedTrie<>(new StringSequence("key2"), null);
        assertNotEquals(trie1.hashCode(), trie2.hashCode());
        trie2 = new SingletonSortedTrie<>(new StringSequence("key1"), null);
        assertEquals(trie1.hashCode(), trie2.hashCode());
        trie1 = new SingletonSortedTrie<>(null, "value1");
        assertNotEquals(trie1.hashCode(), trie2.hashCode());
        trie2 = new SingletonSortedTrie<>(null, "value2");
        assertNotEquals(trie1.hashCode(), trie2.hashCode());
        trie2 = new SingletonSortedTrie<>(null, "value1");
        assertEquals(trie1.hashCode(), trie2.hashCode());
    }

    @Test
    public final void testEquals() {
        SortedTrie<StringSequence, String> trie1 = new SingletonSortedTrie<>(null, null);
        SortedTrie<StringSequence, String> trie2 = new SingletonSortedTrie<>(null, null);
        assertFalse(trie1.equals(null));
        assertFalse(trie2.equals(new Object()));
        assertTrue(trie1.equals(trie1));
        assertTrue(trie1.equals(trie2));
        trie1 = new SingletonSortedTrie<>(new StringSequence("key1"), null);
        assertFalse(trie1.equals(trie2));
        trie2 = new SingletonSortedTrie<>(new StringSequence("key2"), null);
        assertFalse(trie1.equals(trie2));
        trie2 = new SingletonSortedTrie<>(new StringSequence("key1"), null);
        assertTrue(trie1.equals(trie2));
        trie1 = new SingletonSortedTrie<>(null, "value1");
        assertFalse(trie1.equals(trie2));
        trie2 = new SingletonSortedTrie<>(null, "value2");
        assertFalse(trie1.equals(trie2));
        trie2 = new SingletonSortedTrie<>(null, "value1");
        assertTrue(trie1.equals(trie2));
    }

}
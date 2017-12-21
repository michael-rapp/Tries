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

import de.mrapp.tries.SortedTrie;
import de.mrapp.tries.sequence.StringSequence;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Tests the functionality of the class {@link EmptySortedTrie}.
 *
 * @author Michael Rapp
 */
public class EmptySortedTrieTest {

    private final SortedTrie<StringSequence, String> trie = new EmptySortedTrie<>();

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
        assertNull(trie.firstEntry());
    }

    @Test
    public final void testLastEntry() {
        assertNull(trie.lastEntry());
    }

    @Test
    public final void testPollFirstEntry() {
        assertNull(trie.pollFirstEntry());
    }

    @Test
    public final void testPollLastEntry() {
        assertNull(trie.pollLastEntry());
    }

    @Test
    public final void testDescendingMap() {
        NavigableMap<StringSequence, String> map = trie.descendingMap();
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    @Test
    public final void testNavigableKeySet() {
        NavigableSet<StringSequence> set = trie.navigableKeySet();
        assertNotNull(set);
        assertTrue(set.isEmpty());
    }

    @Test
    public final void testDescendingKeySet() {
        NavigableSet<StringSequence> set = trie.descendingKeySet();
        assertNotNull(set);
        assertTrue(set.isEmpty());
    }

    @Test
    public final void testSubMap1() {
        NavigableMap<StringSequence, String> map = trie
                .subMap(new StringSequence("from"), true, new StringSequence("to"), true);
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    @Test
    public final void testSubMap2() {
        SortedMap<StringSequence, String> map = trie
                .subMap(new StringSequence("from"), new StringSequence("to"));
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    @Test
    public final void testHeadMap1() {
        NavigableMap<StringSequence, String> map = trie.headMap(new StringSequence("to"), true);
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    @Test
    public final void testHeadMap2() {
        SortedMap<StringSequence, String> map = trie.headMap(new StringSequence("to"));
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    @Test
    public final void testTailMap1() {
        NavigableMap<StringSequence, String> map = trie.tailMap(new StringSequence("from"), true);
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    @Test
    public final void testTailMap2() {
        SortedMap<StringSequence, String> map = trie.tailMap(new StringSequence("from"));
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    @Test
    public final void testComparator() {
        assertNull(trie.comparator());
    }

    @Test(expected = NoSuchElementException.class)
    public final void testFirstKey() {
        trie.firstKey();
    }

    @Test(expected = NoSuchElementException.class)
    public final void testLastKey() {
        trie.lastKey();
    }

    @Test
    public final void testSubTree() {
        SortedTrie<StringSequence, String> subTrie = trie.subTree(new StringSequence("foo"));
        assertNotNull(subTrie);
        assertTrue(subTrie instanceof EmptySortedTrie);
        assertEquals(trie, subTrie);
    }

}
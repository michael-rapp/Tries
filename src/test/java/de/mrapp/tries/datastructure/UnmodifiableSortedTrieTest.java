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

import de.mrapp.tries.Node;
import de.mrapp.tries.SortedTrie;
import de.mrapp.tries.Trie;
import de.mrapp.tries.sequence.StringSequence;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests the functionality of the class {@link UnmodifiableSortedTrie}.
 *
 * @author Michael Rapp
 */
public class UnmodifiableSortedTrieTest {

    private final SortedTrie<StringSequence, String> trie = mock(SortedTrie.class);

    private final SortedTrie<StringSequence, String> unmodifiableTrie = new UnmodifiableSortedTrie<>(
            trie);

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
    public final void testLowerEntry() {
        StringSequence key = new StringSequence("foo");
        Map.Entry<StringSequence, String> entry = new AbstractMap.SimpleImmutableEntry<>(
                new StringSequence("bar"), "value");
        when(trie.lowerEntry(key)).thenReturn(entry);
        assertEquals(entry, unmodifiableTrie.lowerEntry(key));
    }

    @Test
    public final void testLowerKey() {
        StringSequence key = new StringSequence("foo");
        StringSequence lowerKey = new StringSequence("lower");
        when(trie.lowerKey(key)).thenReturn(lowerKey);
        assertEquals(lowerKey, unmodifiableTrie.lowerKey(key));
    }

    @Test
    public final void testFloorEntry() {
        StringSequence key = new StringSequence("foo");
        Map.Entry<StringSequence, String> entry = new AbstractMap.SimpleImmutableEntry<>(
                new StringSequence("bar"), "value");
        when(trie.floorEntry(key)).thenReturn(entry);
        assertEquals(entry, unmodifiableTrie.floorEntry(key));
    }

    @Test
    public final void testFloorKey() {
        StringSequence key = new StringSequence("foo");
        StringSequence floorKey = new StringSequence("floor");
        when(trie.floorKey(key)).thenReturn(floorKey);
        assertEquals(floorKey, unmodifiableTrie.floorKey(key));
    }

    @Test
    public final void testCeilingEntry() {
        StringSequence key = new StringSequence("foo");
        Map.Entry<StringSequence, String> entry = new AbstractMap.SimpleImmutableEntry<>(
                new StringSequence("bar"), "value");
        when(trie.ceilingEntry(key)).thenReturn(entry);
        assertEquals(entry, unmodifiableTrie.ceilingEntry(key));
    }

    @Test
    public final void testCeilingKey() {
        StringSequence key = new StringSequence("foo");
        StringSequence ceilingKey = new StringSequence("ceiling");
        when(trie.ceilingKey(key)).thenReturn(ceilingKey);
        assertEquals(ceilingKey, unmodifiableTrie.ceilingKey(key));
    }

    @Test
    public final void testHigherEntry() {
        StringSequence key = new StringSequence("foo");
        Map.Entry<StringSequence, String> entry = new AbstractMap.SimpleImmutableEntry<>(
                new StringSequence("bar"), "value");
        when(trie.higherEntry(key)).thenReturn(entry);
        assertEquals(entry, unmodifiableTrie.higherEntry(key));
    }

    @Test
    public final void testHigherKey() {
        StringSequence key = new StringSequence("foo");
        StringSequence higherKey = new StringSequence("higher");
        when(trie.higherKey(key)).thenReturn(higherKey);
        assertEquals(higherKey, unmodifiableTrie.higherKey(key));
    }

    @Test
    public final void testFirstEntry() {
        Map.Entry<StringSequence, String> entry = new AbstractMap.SimpleImmutableEntry<>(
                new StringSequence("bar"), "value");
        when(trie.firstEntry()).thenReturn(entry);
        assertEquals(entry, unmodifiableTrie.firstEntry());
    }

    @Test
    public final void testLastEntry() {
        Map.Entry<StringSequence, String> entry = new AbstractMap.SimpleImmutableEntry<>(
                new StringSequence("bar"), "value");
        when(trie.lastEntry()).thenReturn(entry);
        assertEquals(entry, unmodifiableTrie.lastEntry());
    }

    @Test
    public final void testPollFirstEntry() {
        trie.pollFirstEntry();
    }

    @Test
    public final void testPollLastEntry() {
        trie.pollLastEntry();
    }

    @Test
    public final void testDescendingMap() {
        NavigableMap<StringSequence, String> descendingMap = new TreeMap<>();
        descendingMap.put(new StringSequence("foo"), "bar");
        when(trie.descendingMap()).thenReturn(descendingMap);
        NavigableMap<StringSequence, String> map = unmodifiableTrie.descendingMap();
        assertEquals(descendingMap, map);

        try {
            map.put(new StringSequence("foo"), "bar");
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public final void testNavigableKeySet() {
        NavigableSet<StringSequence> keySet = new TreeSet<>();
        keySet.add(new StringSequence("foo"));
        when(trie.navigableKeySet()).thenReturn(keySet);
        NavigableSet<StringSequence> set = unmodifiableTrie.navigableKeySet();
        assertEquals(keySet, set);

        try {
            set.add(new StringSequence("foo"));
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public final void testDescendingKeySet() {
        NavigableSet<StringSequence> keySet = new TreeSet<>();
        keySet.add(new StringSequence("foo"));
        when(trie.descendingKeySet()).thenReturn(keySet);
        NavigableSet<StringSequence> set = unmodifiableTrie.descendingKeySet();
        assertEquals(keySet, set);

        try {
            set.add(new StringSequence("foo"));
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public final void testSubMap1() {
        NavigableMap<StringSequence, String> subMap = new TreeMap<>();
        subMap.put(new StringSequence("foo"), "bar");
        when(trie.subMap(new StringSequence("from"), true, new StringSequence("to"), true))
                .thenReturn(subMap);
        NavigableMap<StringSequence, String> map = unmodifiableTrie
                .subMap(new StringSequence("from"), true, new StringSequence("to"), true);
        assertEquals(subMap, map);

        try {
            map.put(new StringSequence("foo"), "bar");
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public final void testSubMap2() {
        SortedMap<StringSequence, String> subMap = new TreeMap<>();
        subMap.put(new StringSequence("foo"), "bar");
        when(trie.subMap(new StringSequence("from"), new StringSequence("to")))
                .thenReturn(subMap);
        SortedMap<StringSequence, String> map = unmodifiableTrie
                .subMap(new StringSequence("from"), new StringSequence("to"));
        assertEquals(subMap, map);

        try {
            map.put(new StringSequence("foo"), "bar");
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public final void testHeadMap1() {
        NavigableMap<StringSequence, String> headMap = new TreeMap<>();
        headMap.put(new StringSequence("foo"), "bar");
        when(trie.headMap(new StringSequence("to"), true)).thenReturn(headMap);
        NavigableMap<StringSequence, String> map = unmodifiableTrie
                .headMap(new StringSequence("to"), true);
        assertEquals(headMap, map);

        try {
            map.put(new StringSequence("foo"), "bar");
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public final void testHeadMap2() {
        SortedMap<StringSequence, String> headMap = new TreeMap<>();
        headMap.put(new StringSequence("foo"), "bar");
        when(trie.headMap(new StringSequence("to"))).thenReturn(headMap);
        SortedMap<StringSequence, String> map = unmodifiableTrie.headMap(new StringSequence("to"));
        assertEquals(headMap, map);

        try {
            map.put(new StringSequence("foo"), "bar");
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public final void testTailMap1() {
        NavigableMap<StringSequence, String> tailMap = new TreeMap<>();
        tailMap.put(new StringSequence("foo"), "bar");
        when(trie.tailMap(new StringSequence("from"), true)).thenReturn(tailMap);
        NavigableMap<StringSequence, String> map = unmodifiableTrie
                .tailMap(new StringSequence("from"), true);
        assertEquals(tailMap, map);

        try {
            map.put(new StringSequence("foo"), "bar");
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public final void testTailMap2() {
        SortedMap<StringSequence, String> tailMap = new TreeMap<>();
        tailMap.put(new StringSequence("foo"), "bar");
        when(trie.tailMap(new StringSequence("from"))).thenReturn(tailMap);
        SortedMap<StringSequence, String> map = unmodifiableTrie
                .tailMap(new StringSequence("from"));
        assertEquals(tailMap, map);

        try {
            map.put(new StringSequence("foo"), "bar");
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public final void testComparator() {
        Comparator<? super StringSequence> comparator = mock(Comparator.class);
        doReturn(comparator).when(trie).comparator();
        assertEquals(comparator, unmodifiableTrie.comparator());
    }

    @Test
    public final void testFirstKey() {
        StringSequence firstKey = new StringSequence("first");
        when(trie.firstKey()).thenReturn(firstKey);
        assertEquals(firstKey, unmodifiableTrie.firstKey());
    }

    @Test
    public final void testLastKey() {
        StringSequence lastKey = new StringSequence("last");
        when(trie.lastKey()).thenReturn(lastKey);
        assertEquals(lastKey, unmodifiableTrie.lastKey());
    }

    @Test
    public final void testSubTree() {
        StringSequence sequence = new StringSequence("foo");
        SortedTrie<StringSequence, String> subTrie = mock(SortedTrie.class);
        when(trie.subTrie(sequence)).thenReturn(subTrie);
        Trie<StringSequence, String> result = unmodifiableTrie.subTrie(sequence);
        assertTrue(result instanceof UnmodifiableSortedTrie);
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
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
import de.mrapp.tries.SortedStringTrie;
import de.mrapp.tries.sequence.StringSequence;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests the functionality of the class {@link UnmodifiableSortedStringTrie}.
 *
 * @author Michael Rapp
 */
public class UnmodifiableSortedStringTrieTest {

    private final SortedStringTrie<String> trie = mock(SortedStringTrie.class);

    private final SortedStringTrie<String> unmodifiableTrie = new UnmodifiableSortedStringTrie<>(
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
    public final void testLowerEntry() {
        String key = "foo";
        Map.Entry<String, String> entry = new AbstractMap.SimpleImmutableEntry<>("bar", "value");
        when(trie.lowerEntry(key)).thenReturn(entry);
        assertEquals(entry, unmodifiableTrie.lowerEntry(key));
    }

    @Test
    public final void testLowerKey() {
        String key = "foo";
        String lowerKey = "lower";
        when(trie.lowerKey(key)).thenReturn(lowerKey);
        assertEquals(lowerKey, unmodifiableTrie.lowerKey(key));
    }

    @Test
    public final void testFloorEntry() {
        String key = "foo";
        Map.Entry<String, String> entry = new AbstractMap.SimpleImmutableEntry<>("bar", "value");
        when(trie.floorEntry(key)).thenReturn(entry);
        assertEquals(entry, unmodifiableTrie.floorEntry(key));
    }

    @Test
    public final void testFloorKey() {
        String key = "foo";
        String floorKey = "floor";
        when(trie.floorKey(key)).thenReturn(floorKey);
        assertEquals(floorKey, unmodifiableTrie.floorKey(key));
    }

    @Test
    public final void testCeilingEntry() {
        String key = "foo";
        Map.Entry<String, String> entry = new AbstractMap.SimpleImmutableEntry<>("bar", "value");
        when(trie.ceilingEntry(key)).thenReturn(entry);
        assertEquals(entry, unmodifiableTrie.ceilingEntry(key));
    }

    @Test
    public final void testCeilingKey() {
        String key = "foo";
        String ceilingKey = "ceiling";
        when(trie.ceilingKey(key)).thenReturn(ceilingKey);
        assertEquals(ceilingKey, unmodifiableTrie.ceilingKey(key));
    }

    @Test
    public final void testHigherEntry() {
        String key = "foo";
        Map.Entry<String, String> entry = new AbstractMap.SimpleImmutableEntry<>("bar", "value");
        when(trie.higherEntry(key)).thenReturn(entry);
        assertEquals(entry, unmodifiableTrie.higherEntry(key));
    }

    @Test
    public final void testHigherKey() {
        String key = "foo";
        String higherKey = "higher";
        when(trie.higherKey(key)).thenReturn(higherKey);
        assertEquals(higherKey, unmodifiableTrie.higherKey(key));
    }

    @Test
    public final void testFirstEntry() {
        Map.Entry<String, String> entry = new AbstractMap.SimpleImmutableEntry<>("bar", "value");
        when(trie.firstEntry()).thenReturn(entry);
        assertEquals(entry, unmodifiableTrie.firstEntry());
    }

    @Test
    public final void testLastEntry() {
        Map.Entry<String, String> entry = new AbstractMap.SimpleImmutableEntry<>("bar", "value");
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
        NavigableMap<String, String> descendingMap = new TreeMap<>();
        descendingMap.put("foo", "bar");
        when(trie.descendingMap()).thenReturn(descendingMap);
        NavigableMap<String, String> map = unmodifiableTrie.descendingMap();
        assertEquals(descendingMap, map);

        try {
            map.put("foo", "bar");
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public final void testNavigableKeySet() {
        NavigableSet<String> keySet = new TreeSet<>();
        keySet.add("foo");
        when(trie.navigableKeySet()).thenReturn(keySet);
        NavigableSet<String> set = unmodifiableTrie.navigableKeySet();
        assertEquals(keySet, set);

        try {
            set.add("foo");
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public final void testDescendingKeySet() {
        NavigableSet<String> keySet = new TreeSet<>();
        keySet.add("foo");
        when(trie.descendingKeySet()).thenReturn(keySet);
        NavigableSet<String> set = unmodifiableTrie.descendingKeySet();
        assertEquals(keySet, set);

        try {
            set.add("foo");
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public final void testSubMap1() {
        NavigableMap<String, String> subMap = new TreeMap<>();
        subMap.put("foo", "bar");
        when(trie.subMap("from", true, "to", true)).thenReturn(subMap);
        NavigableMap<String, String> map = unmodifiableTrie.subMap("from", true, "to", true);
        assertEquals(subMap, map);

        try {
            map.put("foo", "bar");
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public final void testSubMap2() {
        SortedMap<String, String> subMap = new TreeMap<>();
        subMap.put("foo", "bar");
        when(trie.subMap("from", "to")).thenReturn(subMap);
        SortedMap<String, String> map = unmodifiableTrie.subMap("from", "to");
        assertEquals(subMap, map);

        try {
            map.put("foo", "bar");
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public final void testHeadMap1() {
        NavigableMap<String, String> headMap = new TreeMap<>();
        headMap.put("foo", "bar");
        when(trie.headMap("to", true)).thenReturn(headMap);
        NavigableMap<String, String> map = unmodifiableTrie.headMap("to", true);
        assertEquals(headMap, map);

        try {
            map.put("foo", "bar");
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public final void testHeadMap2() {
        SortedMap<String, String> headMap = new TreeMap<>();
        headMap.put("foo", "bar");
        when(trie.headMap("to")).thenReturn(headMap);
        SortedMap<String, String> map = unmodifiableTrie.headMap("to");
        assertEquals(headMap, map);

        try {
            map.put("foo", "bar");
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public final void testTailMap1() {
        NavigableMap<String, String> tailMap = new TreeMap<>();
        tailMap.put("foo", "bar");
        when(trie.tailMap("from", true)).thenReturn(tailMap);
        NavigableMap<String, String> map = unmodifiableTrie.tailMap("from", true);
        assertEquals(tailMap, map);

        try {
            map.put("foo", "bar");
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public final void testTailMap2() {
        SortedMap<String, String> tailMap = new TreeMap<>();
        tailMap.put("foo", "bar");
        when(trie.tailMap("from")).thenReturn(tailMap);
        SortedMap<String, String> map = unmodifiableTrie.tailMap("from");
        assertEquals(tailMap, map);

        try {
            map.put("foo", "bar");
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public final void testComparator() {
        Comparator<? super String> comparator = mock(Comparator.class);
        doReturn(comparator).when(trie).comparator();
        assertEquals(comparator, unmodifiableTrie.comparator());
    }

    @Test
    public final void testFirstKey() {
        String firstKey = "first";
        when(trie.firstKey()).thenReturn(firstKey);
        assertEquals(firstKey, unmodifiableTrie.firstKey());
    }

    @Test
    public final void testLastKey() {
        String lastKey = "last";
        when(trie.lastKey()).thenReturn(lastKey);
        assertEquals(lastKey, unmodifiableTrie.lastKey());
    }

    @Test
    public final void testSubTree() {
        String key = "foo";
        SortedStringTrie<String> subTrie = mock(SortedStringTrie.class);
        when(trie.subTrie(key)).thenReturn(subTrie);
        SortedStringTrie<String> result = unmodifiableTrie.subTrie(key);
        assertTrue(result instanceof UnmodifiableSortedStringTrie);
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
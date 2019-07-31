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

import de.mrapp.tries.SortedStringTrie;
import de.mrapp.tries.SortedTrie;
import de.mrapp.tries.sequence.StringSequence;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Tests the functionality of the class {@link SortedStringTrieWrapper}.
 *
 * @author Michael Rapp
 */
public class SortedStringTrieWrapperTest {

    private SortedStringTrieWrapper<String> trieWrapper;

    private SortedTrie<StringSequence, String> trie;

    @SuppressWarnings("unchecked")
    @Before
    public final void before() {
        trie = mock(SortedTrie.class);
        trieWrapper = new SortedStringTrieWrapper<>(trie);
    }

    @Test
    public void testLowerEntry() {
        String key = "key";
        String value = "value";
        when(trie.lowerEntry(new StringSequence(key)))
                .thenReturn(new AbstractMap.SimpleImmutableEntry<>(new StringSequence(key), value));
        Map.Entry<String, String> lowerEntry = trieWrapper.lowerEntry(key);
        assertEquals(key, lowerEntry.getKey());
        assertEquals(value, lowerEntry.getValue());
    }

    @Test
    public void testLowerKey() {
        String key = "key";
        when(trie.lowerKey(new StringSequence(key))).thenReturn(new StringSequence(key));
        String lowerKey = trieWrapper.lowerKey(key);
        assertEquals(key, lowerKey);
    }

    @Test
    public void testFloorEntry() {
        String key = "key";
        String value = "value";
        when(trie.floorEntry(new StringSequence(key)))
                .thenReturn(new AbstractMap.SimpleImmutableEntry<>(new StringSequence(key), value));
        Map.Entry<String, String> floorEntry = trieWrapper.floorEntry(key);
        assertEquals(key, floorEntry.getKey());
        assertEquals(value, floorEntry.getValue());
    }

    @Test
    public void testFloorKey() {
        String key = "key";
        when(trie.floorKey(new StringSequence(key))).thenReturn(new StringSequence(key));
        String floorKey = trieWrapper.floorKey(key);
        assertEquals(key, floorKey);
    }

    @Test
    public void testCeilingEntry() {
        String key = "key";
        String value = "value";
        when(trie.ceilingEntry(new StringSequence(key)))
                .thenReturn(new AbstractMap.SimpleImmutableEntry<>(new StringSequence(key), value));
        Map.Entry<String, String> ceilingEntry = trieWrapper.ceilingEntry(key);
        assertEquals(key, ceilingEntry.getKey());
        assertEquals(value, ceilingEntry.getValue());
    }

    @Test
    public void testCeilingKey() {
        String key = "key";
        when(trie.ceilingKey(new StringSequence(key))).thenReturn(new StringSequence(key));
        String ceilingKey = trieWrapper.ceilingKey(key);
        assertEquals(key, ceilingKey);
    }

    @Test
    public void testHigherEntry() {
        String key = "key";
        String value = "value";
        when(trie.higherEntry(new StringSequence(key)))
                .thenReturn(new AbstractMap.SimpleImmutableEntry<>(new StringSequence(key), value));
        Map.Entry<String, String> higherEntry = trieWrapper.higherEntry(key);
        assertEquals(key, higherEntry.getKey());
        assertEquals(value, higherEntry.getValue());
    }

    @Test
    public void testHigherKey() {
        String key = "key";
        when(trie.higherKey(new StringSequence(key))).thenReturn(new StringSequence(key));
        String higherKey = trieWrapper.higherKey(key);
        assertEquals(key, higherKey);
    }

    @Test
    public void testFirstEntry() {
        String key = "key";
        String value = "value";
        when(trie.firstEntry())
                .thenReturn(new AbstractMap.SimpleImmutableEntry<>(new StringSequence(key), value));
        Map.Entry<String, String> firstEntry = trieWrapper.firstEntry();
        assertEquals(key, firstEntry.getKey());
        assertEquals(value, firstEntry.getValue());
    }

    @Test
    public void testPollFirstEntry() {
        String key = "key";
        String value = "value";
        when(trie.pollFirstEntry())
                .thenReturn(new AbstractMap.SimpleImmutableEntry<>(new StringSequence(key), value));
        Map.Entry<String, String> firstEntry = trieWrapper.pollFirstEntry();
        assertEquals(key, firstEntry.getKey());
        assertEquals(value, firstEntry.getValue());
    }

    @Test
    public void testFirstKey() {
        String key = "key";
        when(trie.firstKey()).thenReturn(new StringSequence(key));
        String firstKey = trieWrapper.firstKey();
        assertEquals(key, firstKey);
    }

    @Test
    public void testLastEntry() {
        String key = "key";
        String value = "value";
        when(trie.lastEntry())
                .thenReturn(new AbstractMap.SimpleImmutableEntry<>(new StringSequence(key), value));
        Map.Entry<String, String> lastEntry = trieWrapper.lastEntry();
        assertEquals(key, lastEntry.getKey());
        assertEquals(value, lastEntry.getValue());
    }

    @Test
    public void testPollLastEntry() {
        String key = "key";
        String value = "value";
        when(trie.pollLastEntry())
                .thenReturn(new AbstractMap.SimpleImmutableEntry<>(new StringSequence(key), value));
        Map.Entry<String, String> lastEntry = trieWrapper.pollLastEntry();
        assertEquals(key, lastEntry.getKey());
        assertEquals(value, lastEntry.getValue());
    }

    @Test
    public void testLastKey() {
        String key = "key";
        when(trie.lastKey()).thenReturn(new StringSequence(key));
        String lastKey = trieWrapper.lastKey();
        assertEquals(key, lastKey);
    }

    @Test
    public void testDescendingMap() {
        NavigableMap<StringSequence, String> navigableMap = mock(NavigableMap.class);
        when(navigableMap.size()).thenReturn(2);
        when(trie.descendingMap()).thenReturn(navigableMap);
        NavigableMap<String, String> descendingMap = trieWrapper.descendingMap();
        assertEquals(navigableMap.size(), descendingMap.size());
    }

    @Test
    public void testNavigableKeySet() {
        NavigableSet<StringSequence> navigableSet = mock(NavigableSet.class);
        when(navigableSet.size()).thenReturn(2);
        when(trie.navigableKeySet()).thenReturn(navigableSet);
        NavigableSet<String> navigableKeySet = trieWrapper.navigableKeySet();
        assertEquals(navigableSet.size(), navigableKeySet.size());
    }

    @Test
    public void testDescendingKeySet() {
        NavigableSet<StringSequence> navigableSet = mock(NavigableSet.class);
        when(navigableSet.size()).thenReturn(2);
        when(trie.descendingKeySet()).thenReturn(navigableSet);
        NavigableSet<String> descendingKeySet = trieWrapper.descendingKeySet();
        assertEquals(navigableSet.size(), descendingKeySet.size());
    }

    @Test
    public void testSubMapWithInclusiveParameters() {
        String key1 = "key1";
        String key2 = "key2";
        boolean inclusive = true;
        NavigableMap<StringSequence, String> navigableMap = mock(NavigableMap.class);
        when(navigableMap.size()).thenReturn(2);
        when(trie.subMap(new StringSequence(key1), inclusive, new StringSequence(key2), inclusive))
                .thenReturn(navigableMap);
        NavigableMap<String, String> subMap = trieWrapper
                .subMap(key1, inclusive, key2, inclusive);
        assertEquals(navigableMap.size(), subMap.size());
    }

    @Test
    public void testHeadMapWithInclusiveParameter() {
        String key = "key";
        boolean inclusive = true;
        NavigableMap<StringSequence, String> navigableMap = mock(NavigableMap.class);
        when(navigableMap.size()).thenReturn(2);
        when(trie.headMap(new StringSequence(key), inclusive)).thenReturn(navigableMap);
        NavigableMap<String, String> headMap = trieWrapper.headMap(key, inclusive);
        assertEquals(navigableMap.size(), headMap.size());
    }

    @Test
    public void testTailMapWithInclusiveParameter() {
        String key = "key";
        boolean inclusive = true;
        NavigableMap<StringSequence, String> navigableMap = mock(NavigableMap.class);
        when(navigableMap.size()).thenReturn(2);
        when(trie.tailMap(new StringSequence(key), inclusive)).thenReturn(navigableMap);
        NavigableMap<String, String> tailMap = trieWrapper.tailMap(key, inclusive);
        assertEquals(navigableMap.size(), tailMap.size());
    }

    @Test
    public void testComparator() {
        Comparator<? super StringSequence> comparator = mock(Comparator.class);
        doReturn(comparator).when(trie).comparator();
        trieWrapper = new SortedStringTrieWrapper<>(trie);
        Comparator<? super String> stringComparator = trieWrapper.comparator();
        String first = "foo";
        String second = "bar";
        int comp = -1;
        when(comparator.compare(new StringSequence(first), new StringSequence(second)))
                .thenReturn(comp);
        assertEquals(comp, stringComparator.compare(first, second));
    }

    @Test
    public void testSubMap() {
        String key1 = "key1";
        String key2 = "key2";
        SortedMap<StringSequence, String> sortedMap = mock(SortedMap.class);
        when(sortedMap.size()).thenReturn(2);
        when(trie.subMap(new StringSequence(key1), new StringSequence(key2))).thenReturn(sortedMap);
        SortedMap<String, String> subMap = trieWrapper.subMap(key1, key2);
        assertEquals(sortedMap.size(), subMap.size());
    }

    @Test
    public void testHeadMap() {
        String key = "key";
        SortedMap<StringSequence, String> sortedMap = mock(SortedMap.class);
        when(sortedMap.size()).thenReturn(2);
        when(trie.headMap(new StringSequence(key))).thenReturn(sortedMap);
        SortedMap<String, String> headMap = trieWrapper.headMap(key);
        assertEquals(sortedMap.size(), headMap.size());
    }

    @Test
    public void testTailMap() {
        String key = "key";
        SortedMap<StringSequence, String> sortedMap = mock(SortedMap.class);
        when(sortedMap.size()).thenReturn(2);
        when(trie.tailMap(new StringSequence(key))).thenReturn(sortedMap);
        SortedMap<String, String> tailMap = trieWrapper.tailMap(key);
        assertEquals(sortedMap.size(), tailMap.size());
    }

    @SuppressWarnings("unchecked")
    @Test
    public final void testSubTree() {
        String key = "key";
        SortedTrie<StringSequence, String> subTrie = mock(SortedTrie.class);
        when(trie.subTrie(new StringSequence(key))).thenReturn(subTrie);
        SortedStringTrie<String> subStringTrie = trieWrapper.subTrie(key);
        assertTrue(subStringTrie instanceof SortedStringTrieWrapper);
        assertEquals(subTrie, ((SortedStringTrieWrapper) subStringTrie).trie);
    }

}
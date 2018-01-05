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
package de.mrapp.tries;

import de.mrapp.tries.sequence.StringSequence;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * An abstract base class for all tests, which test a {@link SortedTrie} implementation, which uses
 * {@link StringSequence}s as keys.
 *
 * @param <TrieType> The type of the tested trie implementation
 * @author Michael Rapp
 */
public abstract class AbstractStringSequenceSortedTrieTest<TrieType extends SortedTrie<StringSequence, String>>
        extends AbstractStringSequenceTrieTest<TrieType> {

    @Test
    public final void testFloorEntryIfKeyIsNotContained() {
        assertNull(trie.floorEntry(new StringSequence("foo")));
    }

    @Test
    public final void testFloorEntry() {
        testPut6();
        StringSequence key = new StringSequence("inn");
        Map.Entry<StringSequence, String> entry = trie.floorEntry(key);
        assertNotNull(entry);
        assertEquals(key, entry.getKey());
        assertEquals("inn", entry.getValue());
    }

    @Test
    public final void testCeilingEntryIfKeyIsNotContained() {
        assertNull(trie.ceilingEntry(new StringSequence("foo")));
    }

    @Test
    public final void testCeilingEntry() {
        testPut6();
        StringSequence key = new StringSequence("inn");
        Map.Entry<StringSequence, String> entry = trie.ceilingEntry(key);
        assertNotNull(entry);
        assertEquals(key, entry.getKey());
        assertEquals("inn", entry.getValue());
    }

    @Test
    public final void testFirstEntry1() {
        testPut6();
        Map.Entry<StringSequence, String> entry = trie.firstEntry();
        assertNotNull(entry);
        assertEquals(new StringSequence("in"), entry.getKey());
        assertEquals("in", entry.getValue());
    }

    @Test
    public void testFirstEntry2() {
        testPutWithEmptyKey();
        Map.Entry<StringSequence, String> entry = trie.firstEntry();
        assertNotNull(entry);
        assertNull(entry.getKey());
        assertEquals("empty", entry.getValue());
    }

    @Test
    public final void testFirstKey1() {
        testPut6();
        StringSequence key = trie.firstKey();
        assertEquals(new StringSequence("in"), key);
    }

    @Test
    public final void testFirstKey2() {
        testPutWithEmptyKey();
        StringSequence key = trie.firstKey();
        assertNull(key);
    }

    @Test
    public final void testLastEntry1() {
        testPut6();
        Map.Entry<StringSequence, String> entry = trie.lastEntry();
        assertNotNull(entry);
        assertEquals(new StringSequence("to"), entry.getKey());
        assertEquals("to", entry.getValue());
    }

    @Test
    public final void testLastEntry2() {
        testPut6();
        trie.put(new StringSequence("too"), "too");
        Map.Entry<StringSequence, String> entry = trie.lastEntry();
        assertNotNull(entry);
        assertEquals(new StringSequence("too"), entry.getKey());
        assertEquals("too", entry.getValue());
    }

    @Test
    public final void testLastEntry3() {
        trie.put(null, "empty");
        Map.Entry<StringSequence, String> entry = trie.lastEntry();
        assertNotNull(entry);
        assertNull(entry.getKey());
        assertEquals("empty", entry.getValue());
    }

    @Test
    public final void testLastKey1() {
        testPut6();
        StringSequence key = trie.lastKey();
        assertEquals(new StringSequence("to"), key);
    }

    @Test
    public final void testLastKey2() {
        testPut6();
        trie.put(new StringSequence("too"), "too");
        StringSequence key = trie.lastKey();
        assertEquals(new StringSequence("too"), key);
    }

    @Test
    public final void testLastKey3() {
        trie.put(null, "empty");
        StringSequence key = trie.lastKey();
        assertNull(key);
    }

    @Test
    public final void testPollFirstEntryIfKeyIsTheOnlyOne() {
        testPut1();
        String string = "tea";
        Map.Entry<StringSequence, String> removed = trie.pollFirstEntry();
        assertNotNull(removed);
        assertEquals(new StringSequence(string), removed.getKey());
        assertEquals(string, removed.getValue());
        assertNull(trie.get(new StringSequence(string)));
        assertNull(trie.getRootNode());
        assertEquals(0, trie.size());
        assertTrue(trie.isEmpty());
    }

    @Test
    public final void testPollFirstEntryIfKeyIsPrefix() {
        testPut6();
        String string = "in";
        Map.Entry<StringSequence, String> removed = trie.pollFirstEntry();
        assertNotNull(removed);
        assertEquals(new StringSequence(string), removed.getKey());
        assertEquals(string, removed.getValue());
        assertNull(trie.get(new StringSequence(string)));
        assertEquals(5, trie.size());
        assertFalse(trie.isEmpty());
        verifyRootNode(trie.getRootNode());
        verifySuccessors(trie.getRootNode(), "i", "t");
        Node<StringSequence, String> successor = getSuccessor(trie.getRootNode(), "i");
        verifySuccessors(successor, "n");
        successor = getSuccessor(successor, "n");
        verifySuccessors(successor, "n");
        successor = getSuccessor(successor, "n");
        verifyLeaf(successor, "inn");
    }

    @Test
    public final void testPollFirstEntryIfKeyIsEmpty() {
        testPutWithEmptyKey();
        String string = "empty";
        Map.Entry<StringSequence, String> removed = trie.pollFirstEntry();
        assertNotNull(removed);
        assertNull(removed.getKey());
        assertEquals(string, removed.getValue());
        assertNull(trie.get(new StringSequence("")));
        assertNull(trie.get(null));
        assertEquals(8, trie.size());
        assertFalse(trie.isEmpty());
        verifyRootNode(trie.getRootNode());
    }

    @Test
    public final void testPollLastEntryIfKeyIsTheOnlyOne() {
        testPut1();
        String string = "tea";
        Map.Entry<StringSequence, String> removed = trie.pollLastEntry();
        assertNotNull(removed);
        assertEquals(new StringSequence(string), removed.getKey());
        assertEquals(string, removed.getValue());
        assertNull(trie.get(new StringSequence(string)));
        assertNull(trie.getRootNode());
        assertEquals(0, trie.size());
        assertTrue(trie.isEmpty());
    }

    @Test
    public final void testPollLastEntryIfKeyIsPrefix() {
        testPut6();
        trie.put(new StringSequence("too"), "too");
        String string = "to";
        Map.Entry<StringSequence, String> removed = trie.pollLastEntry();
        assertNotNull(removed);
        assertEquals(new StringSequence(string), removed.getKey());
        assertEquals(string, removed.getValue());
        assertNull(trie.get(new StringSequence(string)));
        assertEquals(6, trie.size());
        assertFalse(trie.isEmpty());
        verifyRootNode(trie.getRootNode());
        verifySuccessors(trie.getRootNode(), "i", "t");
        Node<StringSequence, String> successor = getSuccessor(trie.getRootNode(), "t");
        verifySuccessors(successor, "e", "o");
        successor = getSuccessor(successor, "o");
        verifySuccessors(successor, "o");
        successor = getSuccessor(successor, "o");
        verifyLeaf(successor, "too");
    }

    @Test
    public final void testPollLastEntryIfKeyIsEmpty() {
        String string = "empty";
        trie.put(new StringSequence(""), string);
        Map.Entry<StringSequence, String> removed = trie.pollLastEntry();
        assertNotNull(removed);
        assertNull(removed.getKey());
        assertEquals(string, removed.getValue());
        assertNull(trie.get(new StringSequence("")));
        assertNull(trie.get(null));
        assertEquals(0, trie.size());
        assertTrue(trie.isEmpty());
        assertNull(trie.getRootNode());
    }

    @Test
    public final void testLowerEntryIfKeyIsNotContained() {
        Map.Entry<StringSequence, String> entry = trie.lowerEntry(new StringSequence("foo"));
        assertNull(entry);
    }

    @Test
    public final void testLowerEntryIfLowerKeyIsPrefix() {
        testPut6();
        Map.Entry<StringSequence, String> entry = trie.lowerEntry(new StringSequence("inn"));
        assertNotNull(entry);
        assertEquals(new StringSequence("in"), entry.getKey());
        assertEquals("in", entry.getValue());
    }

    @Test
    public final void testLowerEntryIfLowerKeySharesPredecessor() {
        testPut6();
        Map.Entry<StringSequence, String> entry = trie.lowerEntry(new StringSequence("ted"));
        assertNotNull(entry);
        assertEquals(new StringSequence("tea"), entry.getKey());
        assertEquals("tea", entry.getValue());
    }

    @Test
    public final void testLowerEntryIfLowerKeySharesPrefix() {
        testPutWithEmptyKey();
        Map.Entry<StringSequence, String> entry = trie.lowerEntry(new StringSequence("tea"));
        assertNotNull(entry);
        assertEquals(new StringSequence("inn"), entry.getKey());
        assertEquals("inn", entry.getValue());
    }

    @Test
    public final void testLowerEntryIfLowerKeyIsNotAvailable() {
        testPut6();
        Map.Entry<StringSequence, String> entry = trie.lowerEntry(new StringSequence("A"));
        assertNull(entry);
    }

    @Test
    public final void testLowerEntryIfLowerKeyIsEmpty() {
        testPutWithEmptyKey();
        Map.Entry<StringSequence, String> entry = trie.lowerEntry(new StringSequence("A"));
        assertNotNull(entry);
        assertNull(entry.getKey());
        assertEquals("empty", entry.getValue());
    }

    @Test
    public final void testLowerKeyIfKeyIsNotContained() {
        assertNull(trie.lowerKey(new StringSequence("foo")));
    }

    @Test
    public final void testLowerKeyIfLowerKeyIsPrefix() {
        testPut6();
        StringSequence lowerKey = trie.lowerKey(new StringSequence("inn"));
        assertEquals(new StringSequence("in"), lowerKey);
    }

    @Test
    public final void testLowerKeyIfLowerKeySharesPredecessor() {
        testPut6();
        StringSequence lowerKey = trie.lowerKey(new StringSequence("ted"));
        assertEquals(new StringSequence("tea"), lowerKey);
    }

    @Test
    public final void testLowerKeyIfLowerKeySharesPrefix() {
        testPutWithEmptyKey();
        StringSequence lowerKey = trie.lowerKey(new StringSequence("tea"));
        assertEquals(new StringSequence("inn"), lowerKey);
    }

    @Test
    public final void testLowerKeyIfLowerKeyIsNotAvailable() {
        testPut6();
        assertNull(trie.lowerKey(new StringSequence("A")));
    }

    @Test
    public final void testLowerKeyIfLowerKeyIsEmpty() {
        testPutWithEmptyKey();
        StringSequence lowerKey = trie.lowerKey(new StringSequence("A"));
        assertNull(lowerKey);
    }

    @Test
    public final void testHigherEntryIfKeyIsNotContained() {
        Map.Entry<StringSequence, String> entry = trie.higherEntry(new StringSequence("foo"));
        assertNull(entry);
    }

    @Test
    public final void testHigherEntryIfHigherKeySharesPrefix() {
        testPut6();
        Map.Entry<StringSequence, String> entry = trie.higherEntry(new StringSequence("in"));
        assertNotNull(entry);
        assertEquals(new StringSequence("inn"), entry.getKey());
        assertEquals("inn", entry.getValue());
    }

    @Test
    public final void testHigherEntryIfHigherKeySharesPredecessor() {
        testPut6();
        Map.Entry<StringSequence, String> entry = trie.higherEntry(new StringSequence("inn"));
        assertNotNull(entry);
        assertEquals(new StringSequence("tea"), entry.getKey());
        assertEquals("tea", entry.getValue());
    }

    @Test
    public final void testHigherEntryIfHigherKeyIsNotAvailable() {
        testPut6();
        Map.Entry<StringSequence, String> entry = trie.higherEntry(new StringSequence("to"));
        assertNull(entry);
    }

    @Test
    public final void testHigherKeyIfKeyIsNotContained() {
        assertNull(trie.higherKey(new StringSequence("foo")));
    }

    @Test
    public final void testHigherKeyIfHigherKeySharesPrefix() {
        testPut6();
        StringSequence key = trie.higherKey(new StringSequence("in"));
        assertEquals(new StringSequence("inn"), key);
    }

    @Test
    public final void testHigherKeyIfHigherKeySharesPredecessor() {
        testPut6();
        StringSequence key = trie.higherKey(new StringSequence("inn"));
        assertEquals(new StringSequence("tea"), key);
    }

    @Test
    public final void testHigherKeyIfHigherKeyIsNotAvailable() {
        testPut6();
        assertNull(trie.higherKey(new StringSequence("to")));
    }

    @Test
    public final void testSubMap1() {
        testPutWithEmptyKey();
        SortedMap<StringSequence, String> subMap = trie
                .subMap(new StringSequence("inn"), new StringSequence("to"));
        assertEquals(4, subMap.size());
        Iterator<Map.Entry<StringSequence, String>> iterator = subMap.entrySet().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("inn"), "inn"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("tea"), "tea"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("ted"), "ted"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("ten"), "ten"),
                iterator.next());
        assertFalse(iterator.hasNext());
        assertEquals(new StringSequence("inn"), subMap.firstKey());
        assertEquals(new StringSequence("ten"), subMap.lastKey());
    }

    @Test
    public final void testSubMap2() {
        testPutWithEmptyKey();
        NavigableMap<StringSequence, String> subMap = trie
                .subMap(new StringSequence("inn"), true, new StringSequence("to"), true);
        assertEquals(5, subMap.size());
        Iterator<Map.Entry<StringSequence, String>> iterator = subMap.entrySet().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("inn"), "inn"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("tea"), "tea"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("ted"), "ted"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("ten"), "ten"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("to"), "to"),
                iterator.next());
        assertFalse(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("inn"), "inn"),
                subMap.firstEntry());
        assertEquals(new StringSequence("inn"), subMap.firstKey());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("to"), "to"),
                subMap.lastEntry());
        assertEquals(new StringSequence("to"), subMap.lastKey());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("tea"), "tea"),
                subMap.higherEntry(new StringSequence("inn")));
        assertEquals(new StringSequence("tea"), subMap.higherKey(new StringSequence("inn")));
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("ten"), "ten"),
                subMap.lowerEntry(new StringSequence("to")));
        assertEquals(new StringSequence("ten"), subMap.lowerKey(new StringSequence("to")));
        assertNull(subMap.lowerEntry(new StringSequence("inn")));
        assertNull(subMap.lowerKey(new StringSequence("inn")));
    }

    @Test
    public final void testSubMapRemove() {
        testPut3();
        NavigableMap<StringSequence, String> subMap = trie
                .subMap(new StringSequence("tea"), true, new StringSequence("to"), true);
        assertEquals(3, subMap.size());
        Iterator<Map.Entry<StringSequence, String>> iterator = subMap.entrySet().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("tea"), "tea"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("ted"), "ted"),
                iterator.next());
        iterator.remove();
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("to"), "to"),
                iterator.next());
        assertFalse(iterator.hasNext());
        assertEquals(2, trie.size());
        assertTrue(trie.containsKey(new StringSequence("tea")));
        assertFalse(trie.containsKey(new StringSequence("ted")));
        assertTrue(trie.containsKey(new StringSequence("to")));
    }

    @Test
    public final void testHeadMap1() {
        testPutWithEmptyKey();
        SortedMap<StringSequence, String> headMap = trie.headMap(new StringSequence("inn"));
        assertEquals(4, headMap.size());
        Iterator<Map.Entry<StringSequence, String>> iterator = headMap.entrySet().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(null, "empty"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("A"), null),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("B"), "tea"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("in"), "in"),
                iterator.next());
        assertFalse(iterator.hasNext());
        assertNull(headMap.firstKey());
        assertEquals(new StringSequence("in"), headMap.lastKey());
    }

    @Test
    public final void testHeadMap2() {
        testPutWithEmptyKey();
        NavigableMap<StringSequence, String> headMap = trie
                .headMap(new StringSequence("inn"), true);
        assertEquals(5, headMap.size());
        Iterator<Map.Entry<StringSequence, String>> iterator = headMap.entrySet().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(null, "empty"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("A"), null),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("B"), "tea"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("in"), "in"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("inn"), "inn"),
                iterator.next());
        assertFalse(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(null, "empty"), headMap.firstEntry());
        assertNull(headMap.firstKey());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("inn"), "inn"),
                headMap.lastEntry());
        assertEquals(new StringSequence("inn"), headMap.lastKey());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("A"), null),
                headMap.higherEntry(null));
        assertEquals(new StringSequence("A"), headMap.higherKey(null));
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("in"), "in"),
                headMap.lowerEntry(new StringSequence("inn")));
        assertEquals(new StringSequence("in"), headMap.lowerKey(new StringSequence("inn")));
        assertNull(headMap.higherEntry(new StringSequence("inn")));
        assertNull(headMap.higherKey(new StringSequence("inn")));
    }

    @Test
    public final void testHeadMapRemove() {
        testPut3();
        NavigableMap<StringSequence, String> subMap = trie.headMap(new StringSequence("to"), true);
        assertEquals(3, subMap.size());
        Iterator<Map.Entry<StringSequence, String>> iterator = subMap.entrySet().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("tea"), "tea"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("ted"), "ted"),
                iterator.next());
        iterator.remove();
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("to"), "to"),
                iterator.next());
        assertFalse(iterator.hasNext());
        assertEquals(2, trie.size());
        assertTrue(trie.containsKey(new StringSequence("tea")));
        assertFalse(trie.containsKey(new StringSequence("ted")));
        assertTrue(trie.containsKey(new StringSequence("to")));
    }

    @Test
    public final void testTailMap1() {
        testPutWithEmptyKey();
        SortedMap<StringSequence, String> tailMap = trie.tailMap(new StringSequence("inn"));
        assertEquals(5, tailMap.size());
        Iterator<Map.Entry<StringSequence, String>> iterator = tailMap.entrySet().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("inn"), "inn"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("tea"), "tea"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("ted"), "ted"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("ten"), "ten"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("to"), "to"),
                iterator.next());
        assertFalse(iterator.hasNext());
        assertEquals(new StringSequence("inn"), tailMap.firstKey());
        assertEquals(new StringSequence("to"), tailMap.lastKey());
    }

    @Test
    public final void testTailMap2() {
        testPutWithEmptyKey();
        NavigableMap<StringSequence, String> tailMap = trie
                .tailMap(new StringSequence("inn"), false);
        assertEquals(4, tailMap.size());
        Iterator<Map.Entry<StringSequence, String>> iterator = tailMap.entrySet().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("tea"), "tea"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("ted"), "ted"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("ten"), "ten"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("to"), "to"),
                iterator.next());
        assertFalse(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("tea"), "tea"),
                tailMap.firstEntry());
        assertEquals(new StringSequence("tea"), tailMap.firstKey());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("to"), "to"),
                tailMap.lastEntry());
        assertEquals(new StringSequence("to"), tailMap.lastKey());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("ted"), "ted"),
                tailMap.higherEntry(new StringSequence("tea")));
        assertEquals(new StringSequence("ted"), tailMap.higherKey(new StringSequence("tea")));
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("ten"), "ten"),
                tailMap.lowerEntry(new StringSequence("to")));
        assertEquals(new StringSequence("ten"), tailMap.lowerKey(new StringSequence("to")));
        assertNull(tailMap.lowerEntry(new StringSequence("tea")));
        assertNull(tailMap.lowerKey(new StringSequence("tea")));
    }

    @Test
    public final void testTailMapRemove() {
        testPut3();
        NavigableMap<StringSequence, String> subMap = trie.tailMap(new StringSequence("tea"), true);
        assertEquals(3, subMap.size());
        Iterator<Map.Entry<StringSequence, String>> iterator = subMap.entrySet().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("tea"), "tea"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("ted"), "ted"),
                iterator.next());
        iterator.remove();
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("to"), "to"),
                iterator.next());
        assertFalse(iterator.hasNext());
        assertEquals(2, trie.size());
        assertTrue(trie.containsKey(new StringSequence("tea")));
        assertFalse(trie.containsKey(new StringSequence("ted")));
        assertTrue(trie.containsKey(new StringSequence("to")));
    }

    @Test
    public final void testDescendingMap() {
        testPutWithEmptyKey();
        NavigableMap<StringSequence, String> map = trie.descendingMap();
        assertEquals(trie.size(), map.size());
        Iterator<Map.Entry<StringSequence, String>> iterator = map.entrySet().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("to"), "to"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("ten"), "ten"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("ted"), "ted"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("tea"), "tea"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("inn"), "inn"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("in"), "in"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("B"), "tea"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("A"), null),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(null, "empty"),
                iterator.next());
        assertFalse(iterator.hasNext());
        trie.forEach((key, value) -> assertEquals(value, map.get(key)));
        assertEquals(trie.firstKey(), map.lastKey());
        assertEquals(trie.higherKey(trie.firstKey()), map.lowerKey(map.lastKey()));
        assertEquals(trie.lastKey(), map.firstKey());
        assertEquals(trie.lowerKey(trie.lastKey()), map.higherKey(map.firstKey()));
    }

    @Test
    public final void testNavigableKeySet() {
        testPutWithEmptyKey();
        NavigableSet<StringSequence> keySet = trie.navigableKeySet();
        assertEquals(9, keySet.size());
        Iterator<StringSequence> iterator = keySet.iterator();
        assertTrue(iterator.hasNext());
        assertNull(iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new StringSequence("A"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new StringSequence("B"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new StringSequence("in"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new StringSequence("inn"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new StringSequence("tea"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new StringSequence("ted"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new StringSequence("ten"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new StringSequence("to"), iterator.next());
        assertFalse(iterator.hasNext());
        assertNull(keySet.first());
        assertEquals(new StringSequence("to"), keySet.last());
        assertEquals(new StringSequence("A"), keySet.higher(null));
        assertEquals(new StringSequence("ten"), keySet.lower(new StringSequence("to")));
    }

    @Test
    public final void testNavigableKeySetDescendingIterator() {
        testPutWithEmptyKey();
        NavigableSet<StringSequence> keySet = trie.navigableKeySet();
        assertEquals(9, keySet.size());
        Iterator<StringSequence> iterator = keySet.descendingIterator();
        assertTrue(iterator.hasNext());
        assertEquals(new StringSequence("to"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new StringSequence("ten"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new StringSequence("ted"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new StringSequence("tea"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new StringSequence("inn"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new StringSequence("in"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new StringSequence("B"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new StringSequence("A"), iterator.next());
        assertTrue(iterator.hasNext());
        assertNull(iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public final void testNavigableKeySetSpliterator() {
        testPutWithEmptyKey();
        NavigableSet<StringSequence> keySet = trie.navigableKeySet();
        assertEquals(9, keySet.size());
        Spliterator<StringSequence> spliterator = keySet.spliterator();
        Collection<StringSequence> keys = new LinkedList<>();
        spliterator.forEachRemaining(keys::add);
        Iterator<StringSequence> iterator = keys.iterator();
        assertNull(iterator.next());
        assertEquals(new StringSequence("A"), iterator.next());
        assertEquals(new StringSequence("B"), iterator.next());
        assertEquals(new StringSequence("in"), iterator.next());
        assertEquals(new StringSequence("inn"), iterator.next());
        assertEquals(new StringSequence("tea"), iterator.next());
        assertEquals(new StringSequence("ted"), iterator.next());
        assertEquals(new StringSequence("ten"), iterator.next());
        assertEquals(new StringSequence("to"), iterator.next());
    }

    @Test
    public final void testDescendingNavigableKeySet() {
        testPutWithEmptyKey();
        NavigableSet<StringSequence> keySet = trie.descendingKeySet();
        assertEquals(9, keySet.size());
        Iterator<StringSequence> iterator = keySet.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(new StringSequence("to"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new StringSequence("ten"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new StringSequence("ted"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new StringSequence("tea"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new StringSequence("inn"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new StringSequence("in"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new StringSequence("B"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new StringSequence("A"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(null, iterator.next());
        assertFalse(iterator.hasNext());
        assertEquals(new StringSequence("to"), keySet.first());
        assertNull(keySet.last());
        assertEquals(new StringSequence("ten"), keySet.higher(new StringSequence("to")));
        assertEquals(new StringSequence("A"), keySet.lower(null));
    }

    @Test
    public final void testDescendingNavigableKeySetDescendingIterator() {
        testPutWithEmptyKey();
        NavigableSet<StringSequence> keySet = trie.descendingKeySet();
        assertEquals(9, keySet.size());
        Iterator<StringSequence> iterator = keySet.descendingIterator();
        assertTrue(iterator.hasNext());
        assertNull(iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new StringSequence("A"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new StringSequence("B"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new StringSequence("in"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new StringSequence("inn"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new StringSequence("tea"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new StringSequence("ted"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new StringSequence("ten"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new StringSequence("to"), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public final void testNavigableKeySetOfTailMapSpliterator() {
        testPutWithEmptyKey();
        NavigableSet<StringSequence> keySet = trie.tailMap(null, true).navigableKeySet();
        assertEquals(9, keySet.size());
        Spliterator<StringSequence> spliterator = keySet.spliterator();
        Collection<StringSequence> keys = new LinkedList<>();
        spliterator.forEachRemaining(keys::add);
        Iterator<StringSequence> iterator = keys.iterator();
        assertNull(iterator.next());
        assertEquals(new StringSequence("A"), iterator.next());
        assertEquals(new StringSequence("B"), iterator.next());
        assertEquals(new StringSequence("in"), iterator.next());
        assertEquals(new StringSequence("inn"), iterator.next());
        assertEquals(new StringSequence("tea"), iterator.next());
        assertEquals(new StringSequence("ted"), iterator.next());
        assertEquals(new StringSequence("ten"), iterator.next());
        assertEquals(new StringSequence("to"), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public final void testDescendingNavigableKeySetSpliterator() {
        testPutWithEmptyKey();
        NavigableSet<StringSequence> keySet = trie.descendingKeySet();
        assertEquals(9, keySet.size());
        Spliterator<StringSequence> spliterator = keySet.spliterator();
        Collection<StringSequence> keys = new LinkedList<>();
        spliterator.forEachRemaining(keys::add);
        Iterator<StringSequence> iterator = keys.iterator();
        assertEquals(new StringSequence("to"), iterator.next());
        assertEquals(new StringSequence("ten"), iterator.next());
        assertEquals(new StringSequence("ted"), iterator.next());
        assertEquals(new StringSequence("tea"), iterator.next());
        assertEquals(new StringSequence("inn"), iterator.next());
        assertEquals(new StringSequence("in"), iterator.next());
        assertEquals(new StringSequence("B"), iterator.next());
        assertEquals(new StringSequence("A"), iterator.next());
        assertNull(iterator.next());
        assertFalse(iterator.hasNext());
    }

}
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

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * An abstract base class for all tests, which test a {@link SortedTrie} implementation, which uses {@link
 * SequenceType}s as keys.
 *
 * @param <SequenceType> The type of the sequences, which are used as the trie's keys
 * @param <TrieType>     The type of the tested trie implementation
 * @author Michael Rapp
 */
public abstract class AbstractNonPatriciaSortedTrieTest<SequenceType, TrieType extends NavigableMap<SequenceType, String>>
        extends AbstractNonPatriciaTrieTest<SequenceType, TrieType> {

    @Test
    public final void testFirstEntryIfTrieIsEmpty() {
        assertNull(trie.firstEntry());
    }

    @Test(expected = NoSuchElementException.class)
    public final void testFirstKeyIfTrieIsEmpty() {
        trie.firstKey();
    }

    @Test
    public final void testLastEntryIfTrieIsEmpty() {
        assertNull(trie.lastEntry());
    }

    @Test(expected = NoSuchElementException.class)
    public final void testLastKeyIfTrieIsNull() {
        trie.lastKey();
    }

    @Test
    public final void testPollFirstEntryIfTrieIsEmpty() {
        Map.Entry<SequenceType, String> removed = trie.pollFirstEntry();
        assertNull(removed);
        assertTrue(trie.isEmpty());
        assertNull(getRootNode(trie));
    }

    @Test
    public final void testPollLastEntryIfTrieIsEmpty() {
        Map.Entry<SequenceType, String> removed = trie.pollLastEntry();
        assertNull(removed);
        assertTrue(trie.isEmpty());
        assertNull(getRootNode(trie));
    }

    @Test
    public final void testFloorEntryIfKeyIsNotContained() {
        assertNull(trie.floorEntry(convertToSequence("foo")));
    }

    @Test
    public final void testCeilingEntryIfKeyIsNotContained() {
        assertNull(trie.ceilingEntry(convertToSequence("foo")));
    }

    @Test
    public final void testLastEntryIfNullKeyIsTheOnlyOne() {
        trie.put(null, "empty");
        Map.Entry<SequenceType, String> entry = trie.lastEntry();
        assertNotNull(entry);
        assertNull(entry.getKey());
        assertEquals("empty", entry.getValue());
    }

    @Test
    public final void testLastKeyIfNullKeyIsTheOnlyOne() {
        trie.put(null, "empty");
        SequenceType key = trie.lastKey();
        assertNull(key);
    }

    @Test
    public final void testFloorEntry() {
        testPut6();
        SequenceType key = convertToSequence("inn");
        Map.Entry<SequenceType, String> entry = trie.floorEntry(key);
        assertNotNull(entry);
        assertEquals(key, entry.getKey());
        assertEquals("inn", entry.getValue());
    }

    @Test
    public final void testCeilingEntry() {
        testPut6();
        SequenceType key = convertToSequence("inn");
        Map.Entry<SequenceType, String> entry = trie.ceilingEntry(key);
        assertNotNull(entry);
        assertEquals(key, entry.getKey());
        assertEquals("inn", entry.getValue());
    }

    @Test
    public final void testFirstEntry1() {
        testPut6();
        Map.Entry<SequenceType, String> entry = trie.firstEntry();
        assertNotNull(entry);
        assertEquals(convertToSequence("in"), entry.getKey());
        assertEquals("in", entry.getValue());
    }

    @Test
    public void testFirstEntry2() {
        testPutWithEmptyKey();
        Map.Entry<SequenceType, String> entry = trie.firstEntry();
        assertNotNull(entry);
        assertNull(entry.getKey());
        assertEquals("empty", entry.getValue());
    }

    @Test
    public final void testFirstKey1() {
        testPut6();
        SequenceType key = trie.firstKey();
        assertEquals(convertToSequence("in"), key);
    }

    @Test
    public final void testFirstKey2() {
        testPutWithEmptyKey();
        SequenceType key = trie.firstKey();
        assertNull(key);
    }

    @Test
    public final void testLastEntry1() {
        testPut6();
        Map.Entry<SequenceType, String> entry = trie.lastEntry();
        assertNotNull(entry);
        assertEquals(convertToSequence("to"), entry.getKey());
        assertEquals("to", entry.getValue());
    }

    @Test
    public final void testLastEntry2() {
        testPut6();
        trie.put(convertToSequence("too"), "too");
        Map.Entry<SequenceType, String> entry = trie.lastEntry();
        assertNotNull(entry);
        assertEquals(convertToSequence("too"), entry.getKey());
        assertEquals("too", entry.getValue());
    }

    @Test
    public final void testLastKey1() {
        testPut6();
        SequenceType key = trie.lastKey();
        assertEquals(convertToSequence("to"), key);
    }

    @Test
    public final void testLastKey2() {
        testPut6();
        trie.put(convertToSequence("too"), "too");
        SequenceType key = trie.lastKey();
        assertEquals(convertToSequence("too"), key);
    }

    @Test
    public final void testPollFirstEntryIfKeyIsTheOnlyOne() {
        testPut1();
        String string = "tea";
        Map.Entry<SequenceType, String> removed = trie.pollFirstEntry();
        assertNotNull(removed);
        assertEquals(convertToSequence(string), removed.getKey());
        assertEquals(string, removed.getValue());
        assertNull(trie.get(convertToSequence(string)));
        assertNull(getRootNode(trie));
        assertEquals(0, trie.size());
        assertTrue(trie.isEmpty());
    }

    @Test
    public final void testPollFirstEntryIfKeyIsPrefix() {
        testPut6();
        String string = "in";
        Map.Entry<SequenceType, String> removed = trie.pollFirstEntry();
        assertNotNull(removed);
        assertEquals(convertToSequence(string), removed.getKey());
        assertEquals(string, removed.getValue());
        assertNull(trie.get(convertToSequence(string)));
        assertEquals(5, trie.size());
        assertFalse(trie.isEmpty());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "i", "t");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "i");
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
        Map.Entry<SequenceType, String> removed = trie.pollFirstEntry();
        assertNotNull(removed);
        assertNull(removed.getKey());
        assertEquals(string, removed.getValue());
        assertNull(trie.get(convertToSequence("")));
        assertNull(trie.get(null));
        assertEquals(8, trie.size());
        assertFalse(trie.isEmpty());
        verifyRootNode(getRootNode(trie));
    }

    @Test
    public final void testPollLastEntryIfKeyIsTheOnlyOne() {
        testPut1();
        String string = "tea";
        Map.Entry<SequenceType, String> removed = trie.pollLastEntry();
        assertNotNull(removed);
        assertEquals(convertToSequence(string), removed.getKey());
        assertEquals(string, removed.getValue());
        assertNull(trie.get(convertToSequence(string)));
        assertNull(getRootNode(trie));
        assertEquals(0, trie.size());
        assertTrue(trie.isEmpty());
    }

    @Test
    public final void testPollLastEntryIfOtherKeyIsPrefix() {
        testPut6();
        String string = "too";
        trie.put(convertToSequence(string), string);
        Map.Entry<SequenceType, String> removed = trie.pollLastEntry();
        assertNotNull(removed);
        assertEquals(convertToSequence(string), removed.getKey());
        assertEquals(string, removed.getValue());
        assertNull(trie.get(convertToSequence(string)));
        assertEquals(6, trie.size());
        assertFalse(trie.isEmpty());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "i", "t");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "t");
        verifySuccessors(successor, "e", "o");
        successor = getSuccessor(successor, "o");
        verifyLeaf(successor, "to");
    }

    @Test
    public final void testPollLastEntryIfKeyIsEmpty() {
        String string = "empty";
        trie.put(convertToSequence(""), string);
        Map.Entry<SequenceType, String> removed = trie.pollLastEntry();
        assertNotNull(removed);
        assertNull(removed.getKey());
        assertEquals(string, removed.getValue());
        assertNull(trie.get(convertToSequence("")));
        assertNull(trie.get(null));
        assertEquals(0, trie.size());
        assertTrue(trie.isEmpty());
        assertNull(getRootNode(trie));
    }

    @Test
    public final void testLowerEntryIfKeyIsNotContained() {
        Map.Entry<SequenceType, String> entry = trie.lowerEntry(convertToSequence("foo"));
        assertNull(entry);
    }

    @Test
    public final void testLowerEntryIfLowerKeyIsPrefix() {
        testPut6();
        Map.Entry<SequenceType, String> entry = trie.lowerEntry(convertToSequence("inn"));
        assertNotNull(entry);
        assertEquals(convertToSequence("in"), entry.getKey());
        assertEquals("in", entry.getValue());
    }

    @Test
    public final void testLowerEntryIfLowerKeySharesPredecessor() {
        testPut6();
        Map.Entry<SequenceType, String> entry = trie.lowerEntry(convertToSequence("ted"));
        assertNotNull(entry);
        assertEquals(convertToSequence("tea"), entry.getKey());
        assertEquals("tea", entry.getValue());
    }

    @Test
    public final void testLowerEntryIfLowerKeySharesPrefix() {
        testPutWithEmptyKey();
        Map.Entry<SequenceType, String> entry = trie.lowerEntry(convertToSequence("tea"));
        assertNotNull(entry);
        assertEquals(convertToSequence("inn"), entry.getKey());
        assertEquals("inn", entry.getValue());
    }

    @Test
    public final void testLowerEntryIfLowerKeySharesPrefixWithKey() {
        testPutWithEmptyKey();
        trie.put(convertToSequence("te"), "te");
        Map.Entry<SequenceType, String> entry = trie.lowerEntry(convertToSequence("ted"));
        assertNotNull(entry);
        assertEquals(convertToSequence("tea"), entry.getKey());
        assertEquals("tea", entry.getValue());
    }

    @Test
    public final void testLowerEntryIfLowerKeyIsNotAvailable() {
        testPut6();
        Map.Entry<SequenceType, String> entry = trie.lowerEntry(convertToSequence("A"));
        assertNull(entry);
    }

    @Test
    public final void testLowerEntryIfLowerKeyIsEmpty() {
        testPutWithEmptyKey();
        Map.Entry<SequenceType, String> entry = trie.lowerEntry(convertToSequence("A"));
        assertNotNull(entry);
        assertNull(entry.getKey());
        assertEquals("empty", entry.getValue());
    }

    @Test
    public final void testLowerKeyIfKeyIsNotContained() {
        assertNull(trie.lowerKey(convertToSequence("foo")));
    }

    @Test
    public final void testLowerKeyIfLowerKeyIsPrefix() {
        testPut6();
        SequenceType lowerKey = trie.lowerKey(convertToSequence("inn"));
        assertEquals(convertToSequence("in"), lowerKey);
    }

    @Test
    public final void testLowerKeyIfLowerKeySharesPredecessor() {
        testPut6();
        SequenceType lowerKey = trie.lowerKey(convertToSequence("ted"));
        assertEquals(convertToSequence("tea"), lowerKey);
    }

    @Test
    public final void testLowerKeyIfLowerKeySharesPrefix() {
        testPutWithEmptyKey();
        SequenceType lowerKey = trie.lowerKey(convertToSequence("tea"));
        assertEquals(convertToSequence("inn"), lowerKey);
    }

    @Test
    public final void testLowerKeyIfLowerKeySharesPrefixWithKey() {
        testPutWithEmptyKey();
        trie.put(convertToSequence("te"), "te");
        SequenceType lowerKey = trie.lowerKey(convertToSequence("ted"));
        assertEquals(convertToSequence("tea"), lowerKey);
    }

    @Test
    public final void testLowerKeyIfLowerKeyIsNotAvailable() {
        testPut6();
        assertNull(trie.lowerKey(convertToSequence("A")));
    }

    @Test
    public final void testLowerKeyIfLowerKeyIsEmpty() {
        testPutWithEmptyKey();
        SequenceType lowerKey = trie.lowerKey(convertToSequence("A"));
        assertNull(lowerKey);
    }

    @Test
    public final void testHigherEntryIfKeyIsNotContained() {
        Map.Entry<SequenceType, String> entry = trie.higherEntry(convertToSequence("foo"));
        assertNull(entry);
    }

    @Test
    public final void testHigherEntryIfHigherKeySharesPrefix() {
        testPut6();
        Map.Entry<SequenceType, String> entry = trie.higherEntry(convertToSequence("in"));
        assertNotNull(entry);
        assertEquals(convertToSequence("inn"), entry.getKey());
        assertEquals("inn", entry.getValue());
    }

    @Test
    public final void testHigherEntryIfHigherKeySharesPredecessor() {
        testPut6();
        Map.Entry<SequenceType, String> entry = trie.higherEntry(convertToSequence("inn"));
        assertNotNull(entry);
        assertEquals(convertToSequence("tea"), entry.getKey());
        assertEquals("tea", entry.getValue());
    }

    @Test
    public final void testHigherEntryIfHigherKeyIsNotAvailable() {
        testPut6();
        Map.Entry<SequenceType, String> entry = trie.higherEntry(convertToSequence("to"));
        assertNull(entry);
    }

    @Test
    public final void testHigherKeyIfKeyIsNotContained() {
        assertNull(trie.higherKey(convertToSequence("foo")));
    }

    @Test
    public final void testHigherKeyIfHigherKeySharesPrefix() {
        testPut6();
        SequenceType key = trie.higherKey(convertToSequence("in"));
        assertEquals(convertToSequence("inn"), key);
    }

    @Test
    public final void testHigherKeyIfHigherKeySharesPredecessor() {
        testPut6();
        SequenceType key = trie.higherKey(convertToSequence("inn"));
        assertEquals(convertToSequence("tea"), key);
    }

    @Test
    public final void testHigherKeyIfHigherKeyIsNotAvailable() {
        testPut6();
        assertNull(trie.higherKey(convertToSequence("to")));
    }

    @Test
    public final void testSubMap1() {
        testPutWithEmptyKey();
        SortedMap<SequenceType, String> subMap = trie
                .subMap(convertToSequence("inn"), convertToSequence("to"));
        assertEquals(4, subMap.size());
        Iterator<Map.Entry<SequenceType, String>> iterator = subMap.entrySet().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("inn"), "inn"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("tea"), "tea"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("ted"), "ted"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("ten"), "ten"),
                iterator.next());
        assertFalse(iterator.hasNext());
        assertEquals(convertToSequence("inn"), subMap.firstKey());
        assertEquals(convertToSequence("ten"), subMap.lastKey());
    }

    @Test
    public final void testSubMap2() {
        testPutWithEmptyKey();
        NavigableMap<SequenceType, String> subMap = trie
                .subMap(convertToSequence("inn"), true, convertToSequence("to"), true);
        assertEquals(5, subMap.size());
        Iterator<Map.Entry<SequenceType, String>> iterator = subMap.entrySet().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("inn"), "inn"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("tea"), "tea"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("ted"), "ted"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("ten"), "ten"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("to"), "to"),
                iterator.next());
        assertFalse(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("inn"), "inn"),
                subMap.firstEntry());
        assertEquals(convertToSequence("inn"), subMap.firstKey());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("to"), "to"),
                subMap.lastEntry());
        assertEquals(convertToSequence("to"), subMap.lastKey());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("tea"), "tea"),
                subMap.higherEntry(convertToSequence("inn")));
        assertEquals(convertToSequence("tea"), subMap.higherKey(convertToSequence("inn")));
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("ten"), "ten"),
                subMap.lowerEntry(convertToSequence("to")));
        assertEquals(convertToSequence("ten"), subMap.lowerKey(convertToSequence("to")));
        assertNull(subMap.lowerEntry(convertToSequence("inn")));
        assertNull(subMap.lowerKey(convertToSequence("inn")));
    }

    @Test
    public final void testSubMapRemove() {
        testPut3();
        NavigableMap<SequenceType, String> subMap = trie
                .subMap(convertToSequence("tea"), true, convertToSequence("to"), true);
        assertEquals(3, subMap.size());
        Iterator<Map.Entry<SequenceType, String>> iterator = subMap.entrySet().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("tea"), "tea"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("ted"), "ted"),
                iterator.next());
        iterator.remove();
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("to"), "to"),
                iterator.next());
        assertFalse(iterator.hasNext());
        assertEquals(2, trie.size());
        assertTrue(trie.containsKey(convertToSequence("tea")));
        assertFalse(trie.containsKey(convertToSequence("ted")));
        assertTrue(trie.containsKey(convertToSequence("to")));
    }

    @Test
    public final void testHeadMap1() {
        testPutWithEmptyKey();
        SortedMap<SequenceType, String> headMap = trie.headMap(convertToSequence("inn"));
        assertEquals(4, headMap.size());
        Iterator<Map.Entry<SequenceType, String>> iterator = headMap.entrySet().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(null, "empty"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("A"), null),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("B"), "tea"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("in"), "in"),
                iterator.next());
        assertFalse(iterator.hasNext());
        assertNull(headMap.firstKey());
        assertEquals(convertToSequence("in"), headMap.lastKey());
    }

    @Test
    public final void testHeadMap2() {
        testPutWithEmptyKey();
        NavigableMap<SequenceType, String> headMap = trie
                .headMap(convertToSequence("inn"), true);
        assertEquals(5, headMap.size());
        Iterator<Map.Entry<SequenceType, String>> iterator = headMap.entrySet().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(null, "empty"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("A"), null),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("B"), "tea"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("in"), "in"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("inn"), "inn"),
                iterator.next());
        assertFalse(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(null, "empty"), headMap.firstEntry());
        assertNull(headMap.firstKey());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("inn"), "inn"),
                headMap.lastEntry());
        assertEquals(convertToSequence("inn"), headMap.lastKey());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("A"), null),
                headMap.higherEntry(null));
        assertEquals(convertToSequence("A"), headMap.higherKey(null));
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("in"), "in"),
                headMap.lowerEntry(convertToSequence("inn")));
        assertEquals(convertToSequence("in"), headMap.lowerKey(convertToSequence("inn")));
        assertNull(headMap.higherEntry(convertToSequence("inn")));
        assertNull(headMap.higherKey(convertToSequence("inn")));
    }

    @Test
    public final void testHeadMapRemove() {
        testPut3();
        NavigableMap<SequenceType, String> subMap = trie.headMap(convertToSequence("to"), true);
        assertEquals(3, subMap.size());
        Iterator<Map.Entry<SequenceType, String>> iterator = subMap.entrySet().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("tea"), "tea"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("ted"), "ted"),
                iterator.next());
        iterator.remove();
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("to"), "to"),
                iterator.next());
        assertFalse(iterator.hasNext());
        assertEquals(2, trie.size());
        assertTrue(trie.containsKey(convertToSequence("tea")));
        assertFalse(trie.containsKey(convertToSequence("ted")));
        assertTrue(trie.containsKey(convertToSequence("to")));
    }

    @Test
    public final void testTailMap1() {
        testPutWithEmptyKey();
        SortedMap<SequenceType, String> tailMap = trie.tailMap(convertToSequence("inn"));
        assertEquals(5, tailMap.size());
        Iterator<Map.Entry<SequenceType, String>> iterator = tailMap.entrySet().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("inn"), "inn"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("tea"), "tea"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("ted"), "ted"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("ten"), "ten"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("to"), "to"),
                iterator.next());
        assertFalse(iterator.hasNext());
        assertEquals(convertToSequence("inn"), tailMap.firstKey());
        assertEquals(convertToSequence("to"), tailMap.lastKey());
    }

    @Test
    public final void testTailMap2() {
        testPutWithEmptyKey();
        NavigableMap<SequenceType, String> tailMap = trie
                .tailMap(convertToSequence("inn"), false);
        assertEquals(4, tailMap.size());
        Iterator<Map.Entry<SequenceType, String>> iterator = tailMap.entrySet().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("tea"), "tea"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("ted"), "ted"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("ten"), "ten"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("to"), "to"),
                iterator.next());
        assertFalse(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("tea"), "tea"),
                tailMap.firstEntry());
        assertEquals(convertToSequence("tea"), tailMap.firstKey());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("to"), "to"),
                tailMap.lastEntry());
        assertEquals(convertToSequence("to"), tailMap.lastKey());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("ted"), "ted"),
                tailMap.higherEntry(convertToSequence("tea")));
        assertEquals(convertToSequence("ted"), tailMap.higherKey(convertToSequence("tea")));
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("ten"), "ten"),
                tailMap.lowerEntry(convertToSequence("to")));
        assertEquals(convertToSequence("ten"), tailMap.lowerKey(convertToSequence("to")));
        assertNull(tailMap.lowerEntry(convertToSequence("tea")));
        assertNull(tailMap.lowerKey(convertToSequence("tea")));
    }

    @Test
    public final void testTailMapRemove() {
        testPut3();
        NavigableMap<SequenceType, String> subMap = trie.tailMap(convertToSequence("tea"), true);
        assertEquals(3, subMap.size());
        Iterator<Map.Entry<SequenceType, String>> iterator = subMap.entrySet().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("tea"), "tea"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("ted"), "ted"),
                iterator.next());
        iterator.remove();
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("to"), "to"),
                iterator.next());
        assertFalse(iterator.hasNext());
        assertEquals(2, trie.size());
        assertTrue(trie.containsKey(convertToSequence("tea")));
        assertFalse(trie.containsKey(convertToSequence("ted")));
        assertTrue(trie.containsKey(convertToSequence("to")));
    }

    @Test
    public final void testDescendingMap() {
        testPutWithEmptyKey();
        NavigableMap<SequenceType, String> map = trie.descendingMap();
        assertEquals(trie.size(), map.size());
        Iterator<Map.Entry<SequenceType, String>> iterator = map.entrySet().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("to"), "to"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("ten"), "ten"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("ted"), "ted"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("tea"), "tea"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("inn"), "inn"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("in"), "in"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("B"), "tea"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("A"), null),
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
        NavigableSet<SequenceType> keySet = trie.navigableKeySet();
        assertEquals(9, keySet.size());
        Iterator<SequenceType> iterator = keySet.iterator();
        assertTrue(iterator.hasNext());
        assertNull(iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("A"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("B"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("in"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("inn"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("tea"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("ted"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("ten"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("to"), iterator.next());
        assertFalse(iterator.hasNext());
        assertNull(keySet.first());
        assertEquals(convertToSequence("to"), keySet.last());
        assertEquals(convertToSequence("A"), keySet.higher(null));
        assertEquals(convertToSequence("ten"), keySet.lower(convertToSequence("to")));
    }

    @Test
    public final void testNavigableKeySetDescendingIterator() {
        testPutWithEmptyKey();
        NavigableSet<SequenceType> keySet = trie.navigableKeySet();
        assertEquals(9, keySet.size());
        Iterator<SequenceType> iterator = keySet.descendingIterator();
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("to"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("ten"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("ted"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("tea"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("inn"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("in"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("B"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("A"), iterator.next());
        assertTrue(iterator.hasNext());
        assertNull(iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public final void testNavigableKeySetSpliterator() {
        testPutWithEmptyKey();
        NavigableSet<SequenceType> keySet = trie.navigableKeySet();
        assertEquals(9, keySet.size());
        Spliterator<SequenceType> spliterator = keySet.spliterator();
        Collection<SequenceType> keys = new LinkedList<>();
        spliterator.forEachRemaining(keys::add);
        Iterator<SequenceType> iterator = keys.iterator();
        assertNull(iterator.next());
        assertEquals(convertToSequence("A"), iterator.next());
        assertEquals(convertToSequence("B"), iterator.next());
        assertEquals(convertToSequence("in"), iterator.next());
        assertEquals(convertToSequence("inn"), iterator.next());
        assertEquals(convertToSequence("tea"), iterator.next());
        assertEquals(convertToSequence("ted"), iterator.next());
        assertEquals(convertToSequence("ten"), iterator.next());
        assertEquals(convertToSequence("to"), iterator.next());
    }

    @Test
    public final void testDescendingNavigableKeySet() {
        testPutWithEmptyKey();
        NavigableSet<SequenceType> keySet = trie.descendingKeySet();
        assertEquals(9, keySet.size());
        Iterator<SequenceType> iterator = keySet.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("to"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("ten"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("ted"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("tea"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("inn"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("in"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("B"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("A"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(null, iterator.next());
        assertFalse(iterator.hasNext());
        assertEquals(convertToSequence("to"), keySet.first());
        assertNull(keySet.last());
        assertEquals(convertToSequence("ten"), keySet.higher(convertToSequence("to")));
        assertEquals(convertToSequence("A"), keySet.lower(null));
    }

    @Test
    public final void testDescendingNavigableKeySetDescendingIterator() {
        testPutWithEmptyKey();
        NavigableSet<SequenceType> keySet = trie.descendingKeySet();
        assertEquals(9, keySet.size());
        Iterator<SequenceType> iterator = keySet.descendingIterator();
        assertTrue(iterator.hasNext());
        assertNull(iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("A"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("B"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("in"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("inn"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("tea"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("ted"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("ten"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("to"), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public final void testNavigableKeySetOfTailMapSpliterator() {
        testPutWithEmptyKey();
        NavigableSet<SequenceType> keySet = trie.tailMap(null, true).navigableKeySet();
        assertEquals(9, keySet.size());
        Spliterator<SequenceType> spliterator = keySet.spliterator();
        Collection<SequenceType> keys = new LinkedList<>();
        spliterator.forEachRemaining(keys::add);
        Iterator<SequenceType> iterator = keys.iterator();
        assertNull(iterator.next());
        assertEquals(convertToSequence("A"), iterator.next());
        assertEquals(convertToSequence("B"), iterator.next());
        assertEquals(convertToSequence("in"), iterator.next());
        assertEquals(convertToSequence("inn"), iterator.next());
        assertEquals(convertToSequence("tea"), iterator.next());
        assertEquals(convertToSequence("ted"), iterator.next());
        assertEquals(convertToSequence("ten"), iterator.next());
        assertEquals(convertToSequence("to"), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public final void testDescendingNavigableKeySetSpliterator() {
        testPutWithEmptyKey();
        NavigableSet<SequenceType> keySet = trie.descendingKeySet();
        assertEquals(9, keySet.size());
        Spliterator<SequenceType> spliterator = keySet.spliterator();
        Collection<SequenceType> keys = new LinkedList<>();
        spliterator.forEachRemaining(keys::add);
        Iterator<SequenceType> iterator = keys.iterator();
        assertEquals(convertToSequence("to"), iterator.next());
        assertEquals(convertToSequence("ten"), iterator.next());
        assertEquals(convertToSequence("ted"), iterator.next());
        assertEquals(convertToSequence("tea"), iterator.next());
        assertEquals(convertToSequence("inn"), iterator.next());
        assertEquals(convertToSequence("in"), iterator.next());
        assertEquals(convertToSequence("B"), iterator.next());
        assertEquals(convertToSequence("A"), iterator.next());
        assertNull(iterator.next());
        assertFalse(iterator.hasNext());
    }

}
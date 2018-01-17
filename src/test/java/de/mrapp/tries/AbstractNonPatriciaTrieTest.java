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
 * An abstract base class for all tests, which test a {@link Trie} implementation, which is not a Patricia trie.
 *
 * @param <SequenceType> The type of the sequences, which are used as the trie's keys
 * @param <TrieType>     The type of the tested trie implementation
 * @author Michael Rapp
 */
public abstract class AbstractNonPatriciaTrieTest<SequenceType, TrieType extends Map<SequenceType, String>>
        extends AbstractTrieTest<SequenceType, TrieType> {

    /**
     * Adds "tea" to the trie.
     */
    @Test
    public final void testPut1() {
        String string = "tea";
        assertTrue(trie.isEmpty());
        String previous = trie.put(convertToSequence(string), string);
        assertNull(previous);
        assertEquals(string, trie.get(convertToSequence(string)));
        assertFalse(trie.isEmpty());
        assertEquals(1, trie.size());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "t");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "t");
        verifySuccessors(successor, "e");
        successor = getSuccessor(successor, "e");
        verifySuccessors(successor, "a");
        successor = getSuccessor(successor, "a");
        verifyLeaf(successor, string);
    }

    /**
     * Adds "tea" and "to" to the trie.
     */
    @Test
    public final void testPut2() {
        testPut1();
        String string = "to";
        String previous = trie.put(convertToSequence(string), string);
        assertNull(previous);
        assertEquals(string, trie.get(convertToSequence(string)));
        assertEquals(2, trie.size());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "t");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "t");
        verifySuccessors(successor, "e", "o");
        successor = getSuccessor(successor, "o");
        verifyLeaf(successor, string);
    }

    /**
     * Adds "tea", "to" and "ted" to the trie.
     */
    @Test
    public final void testPut3() {
        testPut2();
        String string = "ted";
        String previous = trie.put(convertToSequence(string), string);
        assertNull(previous);
        assertEquals(string, trie.get(convertToSequence(string)));
        assertEquals(3, trie.size());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "t");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "t");
        verifySuccessors(successor, "e", "o");
        successor = getSuccessor(successor, "e");
        verifySuccessors(successor, "a", "d");
        successor = getSuccessor(successor, "d");
        verifyLeaf(successor, string);
    }

    /**
     * Adds "tea", "to", "ted" and "ten" to the trie.
     */
    @Test
    public final void testPut4() {
        testPut3();
        String string = "ten";
        String previous = trie.put(convertToSequence(string), string);
        assertNull(previous);
        assertEquals(string, trie.get(convertToSequence(string)));
        assertEquals(4, trie.size());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "t");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "t");
        verifySuccessors(successor, "e", "o");
        successor = getSuccessor(successor, "e");
        verifySuccessors(successor, "a", "d", "n");
        successor = getSuccessor(successor, "n");
        verifyLeaf(successor, string);
    }

    /**
     * Adds "tea", "to", "ted", "ten" and "inn" to the trie.
     */
    @Test
    public final void testPut5() {
        testPut4();
        String string = "inn";
        String previous = trie.put(convertToSequence(string), string);
        assertNull(previous);
        assertEquals(string, trie.get(convertToSequence(string)));
        assertEquals(5, trie.size());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "t", "i");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "i");
        verifySuccessors(successor, "n");
        successor = getSuccessor(successor, "n");
        verifySuccessors(successor, "n");
        successor = getSuccessor(successor, "n");
        verifyLeaf(successor, string);
    }

    /**
     * Adds "tea", "to", "ted", "ten" "inn" and "in" to the trie.
     */
    @Test
    public final void testPut6() {
        testPut5();
        String string = "in";
        String previous = trie.put(convertToSequence(string), string);
        assertNull(previous);
        assertEquals(string, trie.get(convertToSequence(string)));
        assertEquals(6, trie.size());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "t", "i");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "i");
        verifySuccessors(successor, "n");
        successor = getSuccessor(successor, "n");
        assertEquals(new NodeValue<>(string), successor.getNodeValue());
        verifySuccessors(successor, "n");
        successor = getSuccessor(successor, "n");
        verifyLeaf(successor, "inn");
    }


    /**
     * Adds "tea", "to", "ted", "ten" "inn", "in" and "A" (mapped to null value) to the trie.
     */
    @Test
    public final void testPut7() {
        testPut6();
        String string = "A";
        String previous = trie.put(convertToSequence(string), null);
        assertNull(previous);
        assertNull(trie.get(convertToSequence(string)));
        assertFalse(trie.isEmpty());
        assertEquals(7, trie.size());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "t", "i", "A");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "A");
        verifyLeaf(successor, null);
    }

    @Test
    public final void testPutWithDuplicateValue() {
        testPut7();
        String string = "B";
        String duplicate = "tea";
        String previous = trie.put(convertToSequence(string), duplicate);
        assertNull(previous);
        assertEquals(duplicate, trie.get(convertToSequence(string)));
        assertFalse(trie.isEmpty());
        assertEquals(8, trie.size());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "t", "i", "A", "B");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "B");
        verifyLeaf(successor, duplicate);
    }

    @Test
    public final void testPutWithEmptyKey() {
        testPutWithDuplicateValue();
        String string = "empty";
        String previous = trie.put(convertToSequence(""), string);
        assertNull(previous);
        assertEquals(string, trie.get(convertToSequence("")));
        assertFalse(trie.isEmpty());
        assertEquals(9, trie.size());
        verifyRootNode(getRootNode(trie), string);
    }

    @Test
    public final void testPutWithNullKey() {
        testPutWithEmptyKey();
        String string = "null";
        String previous = trie.put(null, string);
        assertEquals("empty", previous);
        assertEquals(string, trie.get(null));
        assertFalse(trie.isEmpty());
        assertEquals(9, trie.size());
        verifyRootNode(getRootNode(trie), string);
    }

    @Test
    public final void testPutReplacesPreviousValue() {
        testPut1();
        String string = "tea";
        String string2 = "tea2";
        assertEquals(1, trie.size());
        assertEquals(string, trie.get(convertToSequence(string)));
        String previous = trie.put(convertToSequence(string), string2);
        assertEquals(string, previous);
        assertEquals(string2, trie.get(convertToSequence(string)));
        assertFalse(trie.isEmpty());
        assertEquals(1, trie.size());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "t");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "t");
        verifySuccessors(successor, "e");
        successor = getSuccessor(successor, "e");
        verifySuccessors(successor, "a");
        successor = getSuccessor(successor, "a");
        verifyLeaf(successor, string2);
    }

    @Test
    public final void testPutAll() {
        String string1 = "tea";
        String string2 = "to";
        Map<SequenceType, String> map = new HashMap<>();
        map.put(convertToSequence(string1), string1);
        map.put(convertToSequence(string2), string2);
        trie.putAll(map);
        assertEquals(2, trie.size());
        assertEquals(string1, trie.get(convertToSequence(string1)));
        assertEquals(string2, trie.get(convertToSequence(string2)));
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "t");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "t");
        verifySuccessors(successor, "e", "o");
        Node<SequenceType, String> successorE = getSuccessor(successor, "e");
        verifySuccessors(successorE, "a");
        successorE = getSuccessor(successorE, "a");
        verifyLeaf(successorE, string1);
        Node<SequenceType, String> successorO = getSuccessor(successor, "o");
        verifyLeaf(successorO, string2);
    }

    @Test
    public final void testClear() {
        testPut7();
        trie.clear();
        assertTrue(trie.isEmpty());
        assertEquals(0, trie.size());
        assertNull(getRootNode(trie));
        assertNull(trie.get(convertToSequence("tea")));
        assertNull(trie.get(convertToSequence("to")));
        assertNull(trie.get(convertToSequence("ted")));
        assertNull(trie.get(convertToSequence("ten")));
        assertNull(trie.get(convertToSequence("inn")));
        assertNull(trie.get(convertToSequence("in")));
        assertNull(trie.get(convertToSequence("A")));
        assertTrue(trie.values().isEmpty());
        assertTrue(trie.keySet().isEmpty());
        assertTrue(trie.entrySet().isEmpty());
    }

    @Test
    public final void testValues() {
        testPutWithEmptyKey();
        Collection<String> values = trie.values();
        assertEquals(9, values.size());
        assertTrue(values.contains("tea"));
        assertTrue(values.contains("to"));
        assertTrue(values.contains("ted"));
        assertTrue(values.contains("ten"));
        assertTrue(values.contains("inn"));
        assertTrue(values.contains("in"));
        assertTrue(values.contains(null));
        assertTrue(values.contains("empty"));
    }

    @Test
    public final void testValuesIterator() {
        testPutWithEmptyKey();
        Collection<String> values = trie.values();
        assertEquals(9, values.size());
        Collection<String> actualValues = new ArrayList<>();
        actualValues.add("tea");
        actualValues.add("tea");
        actualValues.add("to");
        actualValues.add("ted");
        actualValues.add("ten");
        actualValues.add("inn");
        actualValues.add("in");
        actualValues.add("empty");
        actualValues.add(null);
        Iterator<String> iterator = values.iterator();

        for (int i = 0; i < 9; i++) {
            assertTrue(iterator.hasNext());
            String value = iterator.next();
            assertTrue(actualValues.contains(value));
            actualValues.remove(value);
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    public final void testValuesIteratorRemove() {
        testPut3();
        Iterator<String> iterator = trie.values().iterator();
        assertEquals("tea", iterator.next());
        assertEquals("ted", iterator.next());
        iterator.remove();
        assertEquals("to", iterator.next());
        assertEquals(2, trie.size());
        assertTrue(trie.containsKey(convertToSequence("tea")));
        assertFalse(trie.containsKey(convertToSequence("ted")));
        assertTrue(trie.containsKey(convertToSequence("to")));
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testValuesImmutable() {
        testPut6();
        Collection<String> values = trie.values();
        values.add("foo");
    }

    @Test(expected = ConcurrentModificationException.class)
    public final void testValuesIteratorThrowsConcurrentModificationException() {
        testPut6();
        Collection<String> values = trie.values();
        Iterator<String> iterator = values.iterator();
        trie.put(convertToSequence("foo"), "foo");
        iterator.next();
    }

    @Test
    public final void testContainsValue() {
        testPutWithEmptyKey();
        assertEquals(9, trie.size());
        assertTrue(trie.containsValue("tea"));
        assertTrue(trie.containsValue("to"));
        assertTrue(trie.containsValue("ted"));
        assertTrue(trie.containsValue("ten"));
        assertTrue(trie.containsValue("inn"));
        assertTrue(trie.containsValue("in"));
        assertTrue(trie.containsValue(null));
        assertTrue(trie.containsValue("empty"));
    }

    @Test
    public final void testKeySet() {
        testPutWithEmptyKey();
        Collection<SequenceType> keys = trie.keySet();
        assertEquals(9, keys.size());
        assertTrue(keys.contains(convertToSequence("tea")));
        assertTrue(keys.contains(convertToSequence("to")));
        assertTrue(keys.contains(convertToSequence("ted")));
        assertTrue(keys.contains(convertToSequence("ten")));
        assertTrue(keys.contains(convertToSequence("inn")));
        assertTrue(keys.contains(convertToSequence("in")));
        assertTrue(keys.contains(convertToSequence("A")));
        assertTrue(keys.contains(convertToSequence("B")));
        assertTrue(keys.contains(null));
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testKeySetImmutable() {
        testPut6();
        Collection<SequenceType> keys = trie.keySet();
        keys.add(convertToSequence("foo"));
    }

    @Test
    public final void testKeySetIterator() {
        testPutWithEmptyKey();
        Collection<SequenceType> keys = trie.keySet();
        assertEquals(9, keys.size());
        Iterator<SequenceType> iterator = keys.iterator();
        Collection<SequenceType> actualKeys = new ArrayList<>();
        actualKeys.add(convertToSequence("tea"));
        actualKeys.add(convertToSequence("to"));
        actualKeys.add(convertToSequence("ted"));
        actualKeys.add(convertToSequence("ten"));
        actualKeys.add(convertToSequence("inn"));
        actualKeys.add(convertToSequence("in"));
        actualKeys.add(convertToSequence("A"));
        actualKeys.add(convertToSequence("B"));
        actualKeys.add(null);

        for (int i = 0; i < 9; i++) {
            assertTrue(iterator.hasNext());
            SequenceType key = iterator.next();
            assertTrue(actualKeys.contains(key));
            actualKeys.remove(key);
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    public final void testKeySetIteratorRemove() {
        testPut3();
        Iterator<SequenceType> iterator = trie.keySet().iterator();
        assertEquals(convertToSequence("tea"), iterator.next());
        assertEquals(convertToSequence("ted"), iterator.next());
        iterator.remove();
        assertEquals(convertToSequence("to"), iterator.next());
        assertEquals(2, trie.size());
        assertTrue(trie.containsKey(convertToSequence("tea")));
        assertFalse(trie.containsKey(convertToSequence("ted")));
        assertTrue(trie.containsKey(convertToSequence("to")));
    }

    @Test(expected = ConcurrentModificationException.class)
    public final void testKeySetIteratorThrowsConcurrentModificationException() {
        testPut6();
        Collection<SequenceType> keys = trie.keySet();
        Iterator<SequenceType> iterator = keys.iterator();
        trie.put(convertToSequence("foo"), "foo");
        iterator.next();
    }

    @Test
    public final void testContainsKey() {
        testPutWithEmptyKey();
        assertEquals(9, trie.size());
        assertTrue(trie.containsKey(convertToSequence("tea")));
        assertTrue(trie.containsKey(convertToSequence("to")));
        assertTrue(trie.containsKey(convertToSequence("ted")));
        assertTrue(trie.containsKey(convertToSequence("ten")));
        assertTrue(trie.containsKey(convertToSequence("inn")));
        assertTrue(trie.containsKey(convertToSequence("in")));
        assertTrue(trie.containsKey(convertToSequence("A")));
        assertTrue(trie.containsKey(convertToSequence("B")));
        assertTrue(trie.containsKey(convertToSequence("")));
        assertTrue(trie.containsKey(null));
    }

    @Test
    public final void testEntrySet() {
        testPutWithEmptyKey();
        Set<Map.Entry<SequenceType, String>> entrySet = trie.entrySet();
        assertEquals(9, entrySet.size());
        assertTrue(entrySet.contains(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("tea"), "tea")));
        assertTrue(entrySet.contains(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("to"), "to")));
        assertTrue(entrySet.contains(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("ted"), "ted")));
        assertTrue(entrySet.contains(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("ten"), "ten")));
        assertTrue(entrySet.contains(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("inn"), "inn")));
        assertTrue(entrySet.contains(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("in"), "in")));
        assertTrue(entrySet.contains(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("A"), (String) null)));
        assertTrue(entrySet.contains(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("B"), "tea")));
        assertTrue(entrySet.contains(new AbstractMap.SimpleImmutableEntry<>((SequenceType) null, "empty")));
    }

    @Test
    public final void testEntrySetIterator() {
        testPutWithEmptyKey();
        Set<Map.Entry<SequenceType, String>> entrySet = trie.entrySet();
        assertEquals(9, entrySet.size());
        Collection<Map.Entry<SequenceType, String>> actualEntries = new ArrayList<>();
        actualEntries.add(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("tea"), "tea"));
        actualEntries.add(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("to"), "to"));
        actualEntries.add(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("ted"), "ted"));
        actualEntries.add(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("ten"), "ten"));
        actualEntries.add(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("inn"), "inn"));
        actualEntries.add(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("in"), "in"));
        actualEntries.add(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("A"), null));
        actualEntries.add(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("B"), "tea"));
        actualEntries.add(new AbstractMap.SimpleImmutableEntry<>(null, "empty"));
        Iterator<Map.Entry<SequenceType, String>> iterator = entrySet.iterator();

        for (int i = 0; i < 9; i++) {
            assertTrue(iterator.hasNext());
            Map.Entry<SequenceType, String> entry = iterator.next();
            assertTrue(actualEntries.contains(entry));
            actualEntries.remove(entry);
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    public final void testEntrySetIteratorRemove() {
        testPut3();
        Iterator<Map.Entry<SequenceType, String>> iterator = trie.entrySet().iterator();
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("tea"), "tea"), iterator.next());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("ted"), "ted"), iterator.next());
        iterator.remove();
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("to"), "to"), iterator.next());
        assertEquals(2, trie.size());
        assertTrue(trie.containsKey(convertToSequence("tea")));
        assertFalse(trie.containsKey(convertToSequence("ted")));
        assertTrue(trie.containsKey(convertToSequence("to")));
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testEntrySetImmutable() {
        testPut6();
        Set<Map.Entry<SequenceType, String>> entrySet = trie.entrySet();
        entrySet.add(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("foo"), "foo"));
    }

    @Test(expected = ConcurrentModificationException.class)
    public final void testEntrySetIteratorThrowsConcurrentModificationException() {
        testPut6();
        Set<Map.Entry<SequenceType, String>> entrySet = trie.entrySet();
        Iterator<Map.Entry<SequenceType, String>> iterator = entrySet.iterator();
        trie.put(convertToSequence("foo"), "foo");
        iterator.next();
    }

    @Test
    public final void testRemoveIfKeyIsNotContainedAndIsPrefix() {
        testPut1();
        String removed = trie.remove(convertToSequence("te"));
        assertNull(removed);
        assertFalse(trie.isEmpty());
        assertEquals(1, trie.size());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "t");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "t");
        verifySuccessors(successor, "e");
        successor = getSuccessor(successor, "e");
        verifySuccessors(successor, "a");
        successor = getSuccessor(successor, "a");
        verifyLeaf(successor, "tea");
    }

    @Test
    public final void testRemoveIfKeyIsNotContainedAndSharesPrefix() {
        testPut1();
        String removed = trie.remove(convertToSequence("to"));
        assertNull(removed);
        assertFalse(trie.isEmpty());
        assertEquals(1, trie.size());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "t");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "t");
        verifySuccessors(successor, "e");
        successor = getSuccessor(successor, "e");
        verifySuccessors(successor, "a");
        successor = getSuccessor(successor, "a");
        verifyLeaf(successor, "tea");
    }

    @Test
    public final void testRemoveIfKeyIsNotContainedAndContainsOtherKeyAsPrefix() {
        testPut1();
        String removed = trie.remove(convertToSequence("teaa"));
        assertNull(removed);
        assertFalse(trie.isEmpty());
        assertEquals(1, trie.size());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "t");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "t");
        verifySuccessors(successor, "e");
        successor = getSuccessor(successor, "e");
        verifySuccessors(successor, "a");
        successor = getSuccessor(successor, "a");
        verifyLeaf(successor, "tea");
    }

    @Test
    public final void testRemoveIfKeyIsTheOnlyOne() {
        testPut1();
        String string = "tea";
        String removed = trie.remove(convertToSequence(string));
        assertEquals(string, removed);
        assertNull(trie.get(convertToSequence(string)));
        assertNull(getRootNode(trie));
        assertEquals(0, trie.size());
        assertTrue(trie.isEmpty());
    }

    @Test
    public final void testRemoveIfKeySharesPrefix() {
        testPut2();
        String string = "to";
        String removed = trie.remove(convertToSequence(string));
        assertEquals(string, removed);
        assertNull(trie.get(convertToSequence(string)));
        assertEquals(1, trie.size());
        assertFalse(trie.isEmpty());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "t");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "t");
        verifySuccessors(successor, "e");
        successor = getSuccessor(successor, "e");
        verifySuccessors(successor, "a");
        successor = getSuccessor(successor, "a");
        verifyLeaf(successor, "tea");
    }

    @Test
    public final void testRemoveIfKeyIsPrefix() {
        testPut6();
        String string = "in";
        String removed = trie.remove(convertToSequence(string));
        assertEquals(string, removed);
        assertNull(trie.get(convertToSequence(string)));
        assertEquals(5, trie.size());
        assertFalse(trie.isEmpty());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "t", "i");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "i");
        verifySuccessors(successor, "n");
        successor = getSuccessor(successor, "n");
        verifySuccessors(successor, "n");
        successor = getSuccessor(successor, "n");
        verifyLeaf(successor, "inn");
    }

    @Test
    public final void testRemoveIfKeyContainsOtherKeyAsPrefix() {
        testPut6();
        String string = "inn";
        String removed = trie.remove(convertToSequence(string));
        assertEquals(string, removed);
        assertNull(trie.get(convertToSequence(string)));
        assertEquals(5, trie.size());
        assertFalse(trie.isEmpty());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "t", "i");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "i");
        verifySuccessors(successor, "n");
        successor = getSuccessor(successor, "n");
        verifyLeaf(successor, "in");
    }

    @Test
    public final void testRemoveEmptyKey() {
        testPutWithEmptyKey();
        String string = "empty";
        String removed = trie.remove(convertToSequence(""));
        assertEquals(string, removed);
        assertNull(trie.get(convertToSequence("")));
        assertNull(trie.get(null));
        assertEquals(8, trie.size());
        assertFalse(trie.isEmpty());
        verifyRootNode(getRootNode(trie));
    }

    @Test
    public final void testRemoveNullKey() {
        testPutWithEmptyKey();
        String string = "empty";
        String removed = trie.remove(null);
        assertEquals(string, removed);
        assertNull(trie.get(convertToSequence("")));
        assertNull(trie.get(null));
        assertEquals(8, trie.size());
        assertFalse(trie.isEmpty());
        verifyRootNode(getRootNode(trie));
    }

    @Test
    public final void testHashCode() {
        TrieType trie1 = onCreateTrie();
        TrieType trie2 = onCreateTrie();
        assertEquals(trie1.hashCode(), trie1.hashCode());
        assertEquals(trie1.hashCode(), trie2.hashCode());
        trie1.put(convertToSequence("foo"), "value");
        assertNotEquals(trie1.hashCode(), trie2.hashCode());
        trie2.put(convertToSequence("foo"), "value");
        assertEquals(trie1.hashCode(), trie2.hashCode());
        trie1.put(convertToSequence("fob"), "value2");
        assertNotEquals(trie1.hashCode(), trie2.hashCode());
    }

    @Test
    public final void testEquals() {
        TrieType trie1 = onCreateTrie();
        TrieType trie2 = onCreateTrie();
        assertFalse(trie1.equals(null));
        assertFalse(trie1.equals(new Object()));
        assertTrue(trie1.equals(trie1));
        assertTrue(trie1.equals(trie2));
        trie1.put(convertToSequence("foo"), "value");
        assertFalse(trie1.equals(trie2));
        trie2.put(convertToSequence("foo"), "value");
        assertTrue(trie1.equals(trie2));
        trie1.put(convertToSequence("fob"), "value2");
        assertFalse(trie1.equals(trie2));
    }

}
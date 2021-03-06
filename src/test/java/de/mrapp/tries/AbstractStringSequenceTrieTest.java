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
package de.mrapp.tries;

import de.mrapp.tries.sequence.StringSequence;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * An abstract base class for all tests, which test a {@link Trie} implementation, which uses {@link
 * StringSequence}s as keys.
 *
 * @param <TrieType> The type of the tested trie implementation
 * @author Michael Rapp
 */
public abstract class AbstractStringSequenceTrieTest<TrieType extends Trie<StringSequence, String>>
        extends AbstractTrieTest<StringSequence, TrieType> {

    /**
     * Adds "tea" to the trie.
     */
    @Test
    public final void testPut1() {
        String string = "tea";
        assertTrue(trie.isEmpty());
        String previous = trie.put(new StringSequence(string), string);
        assertNull(previous);
        assertEquals(string, trie.get(new StringSequence(string)));
        assertFalse(trie.isEmpty());
        assertEquals(1, trie.size());
        verifyRootNode(trie.getRootNode());
        verifySuccessors(trie.getRootNode(), "t");
        Node<StringSequence, String> successor = getSuccessor(trie.getRootNode(), "t");
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
        String previous = trie.put(new StringSequence(string), string);
        assertNull(previous);
        assertEquals(string, trie.get(new StringSequence(string)));
        assertEquals(2, trie.size());
        verifyRootNode(trie.getRootNode());
        verifySuccessors(trie.getRootNode(), "t");
        Node<StringSequence, String> successor = getSuccessor(trie.getRootNode(), "t");
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
        String previous = trie.put(new StringSequence(string), string);
        assertNull(previous);
        assertEquals(string, trie.get(new StringSequence(string)));
        assertEquals(3, trie.size());
        verifyRootNode(trie.getRootNode());
        verifySuccessors(trie.getRootNode(), "t");
        Node<StringSequence, String> successor = getSuccessor(trie.getRootNode(), "t");
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
        String previous = trie.put(new StringSequence(string), string);
        assertNull(previous);
        assertEquals(string, trie.get(new StringSequence(string)));
        assertEquals(4, trie.size());
        verifyRootNode(trie.getRootNode());
        verifySuccessors(trie.getRootNode(), "t");
        Node<StringSequence, String> successor = getSuccessor(trie.getRootNode(), "t");
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
        String previous = trie.put(new StringSequence(string), string);
        assertNull(previous);
        assertEquals(string, trie.get(new StringSequence(string)));
        assertEquals(5, trie.size());
        verifyRootNode(trie.getRootNode());
        verifySuccessors(trie.getRootNode(), "t", "i");
        Node<StringSequence, String> successor = getSuccessor(trie.getRootNode(), "i");
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
        String previous = trie.put(new StringSequence(string), string);
        assertNull(previous);
        assertEquals(string, trie.get(new StringSequence(string)));
        assertEquals(6, trie.size());
        verifyRootNode(trie.getRootNode());
        verifySuccessors(trie.getRootNode(), "t", "i");
        Node<StringSequence, String> successor = getSuccessor(trie.getRootNode(), "i");
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
        String previous = trie.put(new StringSequence(string), null);
        assertNull(previous);
        assertNull(trie.get(new StringSequence(string)));
        assertFalse(trie.isEmpty());
        assertEquals(7, trie.size());
        verifyRootNode(trie.getRootNode());
        verifySuccessors(trie.getRootNode(), "t", "i", "A");
        Node<StringSequence, String> successor = getSuccessor(trie.getRootNode(), "A");
        verifyLeaf(successor, null);
    }

    @Test
    public final void testPutWithDuplicateValue() {
        testPut7();
        String string = "B";
        String duplicate = "tea";
        String previous = trie.put(new StringSequence(string), duplicate);
        assertNull(previous);
        assertEquals(duplicate, trie.get(new StringSequence(string)));
        assertFalse(trie.isEmpty());
        assertEquals(8, trie.size());
        verifyRootNode(trie.getRootNode());
        verifySuccessors(trie.getRootNode(), "t", "i", "A", "B");
        Node<StringSequence, String> successor = getSuccessor(trie.getRootNode(), "B");
        verifyLeaf(successor, duplicate);
    }

    @Test
    public final void testPutWithEmptyKey() {
        testPutWithDuplicateValue();
        String string = "empty";
        String previous = trie.put(new StringSequence(""), string);
        assertNull(previous);
        assertEquals(string, trie.get(new StringSequence("")));
        assertFalse(trie.isEmpty());
        assertEquals(9, trie.size());
        verifyRootNode(trie.getRootNode(), string);
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
        verifyRootNode(trie.getRootNode(), string);
    }

    @Test
    public final void testPutReplacesPreviousValue() {
        testPut1();
        String string = "tea";
        String string2 = "tea2";
        assertEquals(1, trie.size());
        assertEquals(string, trie.get(new StringSequence(string)));
        String previous = trie.put(new StringSequence(string), string2);
        assertEquals(string, previous);
        assertEquals(string2, trie.get(new StringSequence(string)));
        assertFalse(trie.isEmpty());
        assertEquals(1, trie.size());
        verifyRootNode(trie.getRootNode());
        verifySuccessors(trie.getRootNode(), "t");
        Node<StringSequence, String> successor = getSuccessor(trie.getRootNode(), "t");
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
        Map<StringSequence, String> map = new HashMap<>();
        map.put(new StringSequence(string1), string1);
        map.put(new StringSequence(string2), string2);
        trie.putAll(map);
        assertEquals(2, trie.size());
        assertEquals(string1, trie.get(new StringSequence(string1)));
        assertEquals(string2, trie.get(new StringSequence(string2)));
        verifyRootNode(trie.getRootNode());
        verifySuccessors(trie.getRootNode(), "t");
        Node<StringSequence, String> successor = getSuccessor(trie.getRootNode(), "t");
        verifySuccessors(successor, "e", "o");
        Node<StringSequence, String> successorE = getSuccessor(successor, "e");
        verifySuccessors(successorE, "a");
        successorE = getSuccessor(successorE, "a");
        verifyLeaf(successorE, string1);
        Node<StringSequence, String> successorO = getSuccessor(successor, "o");
        verifyLeaf(successorO, string2);
    }

    @Test
    public final void testClear() {
        testPut7();
        trie.clear();
        assertTrue(trie.isEmpty());
        assertEquals(0, trie.size());
        assertNull(trie.getRootNode());
        assertNull(trie.get(new StringSequence("tea")));
        assertNull(trie.get(new StringSequence("to")));
        assertNull(trie.get(new StringSequence("ted")));
        assertNull(trie.get(new StringSequence("ten")));
        assertNull(trie.get(new StringSequence("inn")));
        assertNull(trie.get(new StringSequence("in")));
        assertNull(trie.get(new StringSequence("A")));
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
        assertTrue(trie.containsKey(new StringSequence("tea")));
        assertFalse(trie.containsKey(new StringSequence("ted")));
        assertTrue(trie.containsKey(new StringSequence("to")));
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
        trie.put(new StringSequence("foo"), "foo");
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
        Collection<StringSequence> keys = trie.keySet();
        assertEquals(9, keys.size());
        assertTrue(keys.contains(new StringSequence("tea")));
        assertTrue(keys.contains(new StringSequence("to")));
        assertTrue(keys.contains(new StringSequence("ted")));
        assertTrue(keys.contains(new StringSequence("ten")));
        assertTrue(keys.contains(new StringSequence("inn")));
        assertTrue(keys.contains(new StringSequence("in")));
        assertTrue(keys.contains(new StringSequence("A")));
        assertTrue(keys.contains(new StringSequence("B")));
        assertTrue(keys.contains(null));
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testKeySetImmutable() {
        testPut6();
        Collection<StringSequence> keys = trie.keySet();
        keys.add(new StringSequence("foo"));
    }

    @Test
    public final void testKeySetIterator() {
        testPutWithEmptyKey();
        Collection<StringSequence> keys = trie.keySet();
        assertEquals(9, keys.size());
        Iterator<StringSequence> iterator = keys.iterator();
        Collection<StringSequence> actualKeys = new ArrayList<>();
        actualKeys.add(new StringSequence("tea"));
        actualKeys.add(new StringSequence("to"));
        actualKeys.add(new StringSequence("ted"));
        actualKeys.add(new StringSequence("ten"));
        actualKeys.add(new StringSequence("inn"));
        actualKeys.add(new StringSequence("in"));
        actualKeys.add(new StringSequence("A"));
        actualKeys.add(new StringSequence("B"));
        actualKeys.add(null);

        for (int i = 0; i < 9; i++) {
            assertTrue(iterator.hasNext());
            StringSequence key = iterator.next();
            assertTrue(actualKeys.contains(key));
            actualKeys.remove(key);
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    public final void testKeySetIteratorRemove() {
        testPut3();
        Iterator<StringSequence> iterator = trie.keySet().iterator();
        assertEquals(new StringSequence("tea"), iterator.next());
        assertEquals(new StringSequence("ted"), iterator.next());
        iterator.remove();
        assertEquals(new StringSequence("to"), iterator.next());
        assertEquals(2, trie.size());
        assertTrue(trie.containsKey(new StringSequence("tea")));
        assertFalse(trie.containsKey(new StringSequence("ted")));
        assertTrue(trie.containsKey(new StringSequence("to")));
    }

    @Test(expected = ConcurrentModificationException.class)
    public final void testKeySetIteratorThrowsConcurrentModificationException() {
        testPut6();
        Collection<StringSequence> keys = trie.keySet();
        Iterator<StringSequence> iterator = keys.iterator();
        trie.put(new StringSequence("foo"), "foo");
        iterator.next();
    }

    @Test
    public final void testContainsKey() {
        testPutWithEmptyKey();
        assertEquals(9, trie.size());
        assertTrue(trie.containsKey(new StringSequence("tea")));
        assertTrue(trie.containsKey(new StringSequence("to")));
        assertTrue(trie.containsKey(new StringSequence("ted")));
        assertTrue(trie.containsKey(new StringSequence("ten")));
        assertTrue(trie.containsKey(new StringSequence("inn")));
        assertTrue(trie.containsKey(new StringSequence("in")));
        assertTrue(trie.containsKey(new StringSequence("A")));
        assertTrue(trie.containsKey(new StringSequence("B")));
        assertTrue(trie.containsKey(new StringSequence("")));
        assertTrue(trie.containsKey(null));
    }

    @Test
    public final void testEntrySet() {
        testPutWithEmptyKey();
        Set<Map.Entry<StringSequence, String>> entrySet = trie.entrySet();
        assertEquals(9, entrySet.size());
        assertTrue(entrySet.contains(
                new AbstractMap.SimpleImmutableEntry<>(new StringSequence("tea"), "tea")));
        assertTrue(entrySet.contains(
                new AbstractMap.SimpleImmutableEntry<>(new StringSequence("to"), "to")));
        assertTrue(entrySet.contains(
                new AbstractMap.SimpleImmutableEntry<>(new StringSequence("ted"), "ted")));
        assertTrue(entrySet.contains(
                new AbstractMap.SimpleImmutableEntry<>(new StringSequence("ten"), "ten")));
        assertTrue(entrySet.contains(
                new AbstractMap.SimpleImmutableEntry<>(new StringSequence("inn"), "inn")));
        assertTrue(entrySet.contains(
                new AbstractMap.SimpleImmutableEntry<>(new StringSequence("in"), "in")));
        assertTrue(entrySet.contains(
                new AbstractMap.SimpleImmutableEntry<>(new StringSequence("A"), (String) null)));
        assertTrue(entrySet.contains(
                new AbstractMap.SimpleImmutableEntry<>(new StringSequence("B"), "tea")));
        assertTrue(entrySet.contains(new AbstractMap.SimpleImmutableEntry<>(null, "empty")));
    }

    @Test
    public final void testEntrySetIterator() {
        testPutWithEmptyKey();
        Set<Map.Entry<StringSequence, String>> entrySet = trie.entrySet();
        assertEquals(9, entrySet.size());
        Collection<Map.Entry<StringSequence, String>> actualEntries = new ArrayList<>();
        actualEntries.add(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("tea"), "tea"));
        actualEntries.add(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("to"), "to"));
        actualEntries.add(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("ted"), "ted"));
        actualEntries.add(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("ten"), "ten"));
        actualEntries.add(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("inn"), "inn"));
        actualEntries.add(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("in"), "in"));
        actualEntries.add(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("A"), null));
        actualEntries.add(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("B"), "tea"));
        actualEntries.add(new AbstractMap.SimpleImmutableEntry<>(null, "empty"));
        Iterator<Map.Entry<StringSequence, String>> iterator = entrySet.iterator();

        for (int i = 0; i < 9; i++) {
            assertTrue(iterator.hasNext());
            Map.Entry<StringSequence, String> entry = iterator.next();
            assertTrue(actualEntries.contains(entry));
            actualEntries.remove(entry);
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    public final void testEntrySetIteratorRemove() {
        testPut3();
        Iterator<Map.Entry<StringSequence, String>> iterator = trie.entrySet().iterator();
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("tea"), "tea"),
                iterator.next());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("ted"), "ted"),
                iterator.next());
        iterator.remove();
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("to"), "to"),
                iterator.next());
        assertEquals(2, trie.size());
        assertTrue(trie.containsKey(new StringSequence("tea")));
        assertFalse(trie.containsKey(new StringSequence("ted")));
        assertTrue(trie.containsKey(new StringSequence("to")));
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testEntrySetImmutable() {
        testPut6();
        Set<Map.Entry<StringSequence, String>> entrySet = trie.entrySet();
        entrySet.add(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("foo"), "foo"));
    }

    @Test(expected = ConcurrentModificationException.class)
    public final void testEntrySetIteratorThrowsConcurrentModificationException() {
        testPut6();
        Set<Map.Entry<StringSequence, String>> entrySet = trie.entrySet();
        Iterator<Map.Entry<StringSequence, String>> iterator = entrySet.iterator();
        trie.put(new StringSequence("foo"), "foo");
        iterator.next();
    }

    @Test
    public final void testRemoveIfKeyIsNotContainedAndIsPrefix() {
        testPut1();
        String removed = trie.remove(new StringSequence("te"));
        assertNull(removed);
        assertFalse(trie.isEmpty());
        assertEquals(1, trie.size());
        verifyRootNode(trie.getRootNode());
        verifySuccessors(trie.getRootNode(), "t");
        Node<StringSequence, String> successor = getSuccessor(trie.getRootNode(), "t");
        verifySuccessors(successor, "e");
        successor = getSuccessor(successor, "e");
        verifySuccessors(successor, "a");
        successor = getSuccessor(successor, "a");
        verifyLeaf(successor, "tea");
    }

    @Test
    public final void testRemoveIfKeyIsNotContainedAndSharesPrefix() {
        testPut1();
        String removed = trie.remove(new StringSequence("to"));
        assertNull(removed);
        assertFalse(trie.isEmpty());
        assertEquals(1, trie.size());
        verifyRootNode(trie.getRootNode());
        verifySuccessors(trie.getRootNode(), "t");
        Node<StringSequence, String> successor = getSuccessor(trie.getRootNode(), "t");
        verifySuccessors(successor, "e");
        successor = getSuccessor(successor, "e");
        verifySuccessors(successor, "a");
        successor = getSuccessor(successor, "a");
        verifyLeaf(successor, "tea");
    }

    @Test
    public final void testRemoveIfKeyIsNotContainedAndContainsOtherKeyAsPrefix() {
        testPut1();
        String removed = trie.remove(new StringSequence("teaa"));
        assertNull(removed);
        assertFalse(trie.isEmpty());
        assertEquals(1, trie.size());
        verifyRootNode(trie.getRootNode());
        verifySuccessors(trie.getRootNode(), "t");
        Node<StringSequence, String> successor = getSuccessor(trie.getRootNode(), "t");
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
        String removed = trie.remove(new StringSequence(string));
        assertEquals(string, removed);
        assertNull(trie.get(new StringSequence(string)));
        assertNull(trie.getRootNode());
        assertEquals(0, trie.size());
        assertTrue(trie.isEmpty());
    }

    @Test
    public final void testRemoveIfKeySharesPrefix() {
        testPut2();
        String string = "to";
        String removed = trie.remove(new StringSequence(string));
        assertEquals(string, removed);
        assertNull(trie.get(new StringSequence(string)));
        assertEquals(1, trie.size());
        assertFalse(trie.isEmpty());
        verifyRootNode(trie.getRootNode());
        verifySuccessors(trie.getRootNode(), "t");
        Node<StringSequence, String> successor = getSuccessor(trie.getRootNode(), "t");
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
        String removed = trie.remove(new StringSequence(string));
        assertEquals(string, removed);
        assertNull(trie.get(new StringSequence(string)));
        assertEquals(5, trie.size());
        assertFalse(trie.isEmpty());
        verifyRootNode(trie.getRootNode());
        verifySuccessors(trie.getRootNode(), "t", "i");
        Node<StringSequence, String> successor = getSuccessor(trie.getRootNode(), "i");
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
        String removed = trie.remove(new StringSequence(string));
        assertEquals(string, removed);
        assertNull(trie.get(new StringSequence(string)));
        assertEquals(5, trie.size());
        assertFalse(trie.isEmpty());
        verifyRootNode(trie.getRootNode());
        verifySuccessors(trie.getRootNode(), "t", "i");
        Node<StringSequence, String> successor = getSuccessor(trie.getRootNode(), "i");
        verifySuccessors(successor, "n");
        successor = getSuccessor(successor, "n");
        verifyLeaf(successor, "in");
    }

    @Test
    public final void testRemoveEmptyKey() {
        testPutWithEmptyKey();
        String string = "empty";
        String removed = trie.remove(new StringSequence(""));
        assertEquals(string, removed);
        assertNull(trie.get(new StringSequence("")));
        assertNull(trie.get(null));
        assertEquals(8, trie.size());
        assertFalse(trie.isEmpty());
        verifyRootNode(trie.getRootNode());
    }

    @Test
    public final void testRemoveNullKey() {
        testPutWithEmptyKey();
        String string = "empty";
        String removed = trie.remove(null);
        assertEquals(string, removed);
        assertNull(trie.get(new StringSequence("")));
        assertNull(trie.get(null));
        assertEquals(8, trie.size());
        assertFalse(trie.isEmpty());
        verifyRootNode(trie.getRootNode());
    }

}
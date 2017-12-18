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
package de.mrapp.tries;

import de.mrapp.tries.sequence.StringSequence;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Tests the functionality of the class {@link SortedListTrie}.
 *
 * @author Michael Rapp
 */
public class SortedListTrieTest {

    private SortedListTrie<StringSequence, String> trie;

    private void verifyRootNode(final Node<StringSequence, String> node) {
        verifyRootNode(node, null);
    }

    private void verifyRootNode(final Node<StringSequence, String> node,
                                @Nullable final String value) {
        assertNotNull(node);
        assertNull(node.getPredecessor());

        if (value == null) {
            assertNull(node.getNodeValue());
        } else {
            assertNotNull(node.getNodeValue());
            assertEquals(value, node.getValue());
        }
    }

    private void verifySuccessors(final Node<StringSequence, String> node,
                                  final String... successors) {
        assertNotNull(node);
        assertEquals(successors.length, node.getSuccessorCount());
        Iterator<StringSequence> iterator = node.iterator();

        for (String successor : successors) {
            StringSequence key = iterator.next();
            assertEquals(successor, key.toString());
            getSuccessor(node, successor);
        }
    }

    private Node<StringSequence, String> getSuccessor(final Node<StringSequence, String> node,
                                                      final String successor) {
        assertNotNull(node);
        Node<StringSequence, String> childNode = node.getSuccessor(new StringSequence(successor));
        assertNotNull(childNode);
        return childNode;
    }

    private void verifyLeaf(final Node<StringSequence, String> node, final String value) {
        assertNotNull(node);
        Assert.assertEquals(new NodeValue<>(value), node.getNodeValue());
        assertEquals(0, node.getSuccessorCount());
    }

    @Before
    public final void before() {
        this.trie = new SortedListTrie<>();
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testPutAllThrowsExceptionIfMapIsNull() {
        trie.putAll(null);
    }

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
        verifySuccessors(trie.getRootNode(), "i", "t");
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
        verifySuccessors(trie.getRootNode(), "i", "t");
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
        verifySuccessors(trie.getRootNode(), "A", "i", "t");
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
        verifySuccessors(trie.getRootNode(), "A", "B", "i", "t");
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
        Iterator<String> iterator = values.iterator();
        assertEquals("empty", iterator.next());
        assertEquals(null, iterator.next());
        assertEquals("tea", iterator.next());
        assertEquals("in", iterator.next());
        assertEquals("inn", iterator.next());
        assertEquals("tea", iterator.next());
        assertEquals("ted", iterator.next());
        assertEquals("ten", iterator.next());
        assertEquals("to", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testValuesImmutable() {
        testPut6();
        Collection<String> values = trie.values();
        values.add("foo");
    }

    @Test(expected = ConcurrentModificationException.class)
    public final void testValuesThrowsConcurrentModificationException() {
        testPut6();
        Collection<String> values = trie.values();
        trie.put(new StringSequence("foo"), "foo");
        values.iterator().next();
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
        Iterator<StringSequence> iterator = keys.iterator();
        assertEquals(null, iterator.next());
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

    @Test(expected = UnsupportedOperationException.class)
    public final void testKeySetImmutable() {
        testPut6();
        Collection<StringSequence> keys = trie.keySet();
        keys.add(new StringSequence("foo"));
    }

    @Test(expected = ConcurrentModificationException.class)
    public final void testKeySetThrowsConcurrentModificationException() {
        testPut6();
        Collection<StringSequence> keys = trie.keySet();
        trie.put(new StringSequence("foo"), "foo");
        keys.iterator().next();
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
        Iterator<Map.Entry<StringSequence, String>> iterator = entrySet.iterator();
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(null, "empty"), iterator.next());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("A"), (String) null),
                iterator.next());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("B"), "tea"),
                iterator.next());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("in"), "in"),
                iterator.next());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("inn"), "inn"),
                iterator.next());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("tea"), "tea"),
                iterator.next());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("ted"), "ted"),
                iterator.next());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("ten"), "ten"),
                iterator.next());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(new StringSequence("to"), "to"),
                iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testEntrySetImmutable() {
        testPut6();
        Set<Map.Entry<StringSequence, String>> entrySet = trie.entrySet();
        entrySet.add(new AbstractMap.SimpleImmutableEntry<>(
                new StringSequence("foo"), "foo"));
    }

    @Test(expected = ConcurrentModificationException.class)
    public final void testEntrySetThrowsConcurrentModificationException() {
        testPut6();
        Set<Map.Entry<StringSequence, String>> entrySet = trie.entrySet();
        trie.put(new StringSequence("foo"), "foo");
        entrySet.iterator().next();
    }

    @Test
    public void testRemoveIfKeyIsNotContainedAndIsPrefix() {
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
    public void testRemoveIfKeyIsNotContainedAndSharesPrefix() {
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
    public void testRemoveIfKeyIsNotContainedAndContainsOtherKeyAsPrefix() {
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
    public void testRemoveIfKeyIsTheOnlyOne() {
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
    public void testRemoveIfKeySharesPrefix() {
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
    public void testRemoveIfKeyIsPrefix() {
        testPut6();
        String string = "in";
        String removed = trie.remove(new StringSequence(string));
        assertEquals(string, removed);
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
    public void testRemoveIfKeyContainsOtherKeyAsPrefix() {
        testPut6();
        String string = "inn";
        String removed = trie.remove(new StringSequence(string));
        assertEquals(string, removed);
        assertNull(trie.get(new StringSequence(string)));
        assertEquals(5, trie.size());
        assertFalse(trie.isEmpty());
        verifyRootNode(trie.getRootNode());
        verifySuccessors(trie.getRootNode(), "i", "t");
        Node<StringSequence, String> successor = getSuccessor(trie.getRootNode(), "i");
        verifySuccessors(successor, "n");
        successor = getSuccessor(successor, "n");
        verifyLeaf(successor, "in");
    }

    @Test
    public void testRemoveEmptyKey() {
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
    public void testRemoveNullKey() {
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

    @Test
    public void testSubTree1() {
        testPut7();
        SortedListTrie<StringSequence, String> subTrie = trie.subTree(new StringSequence("t"));
        assertFalse(subTrie.isEmpty());
        assertEquals(4, subTrie.size());
        verifyRootNode(subTrie.getRootNode());
        verifySuccessors(subTrie.getRootNode(), "t");
        Node<StringSequence, String> tSuccessor = getSuccessor(subTrie.getRootNode(), "t");
        verifySuccessors(tSuccessor, "e", "o");
        Node<StringSequence, String> eSuccessor = getSuccessor(tSuccessor, "e");
        verifySuccessors(eSuccessor, "a", "d", "n");
        Node<StringSequence, String> leaf = getSuccessor(eSuccessor, "a");
        verifyLeaf(leaf, "tea");
        leaf = getSuccessor(eSuccessor, "d");
        verifyLeaf(leaf, "ted");
        leaf = getSuccessor(eSuccessor, "n");
        verifyLeaf(leaf, "ten");
        Node<StringSequence, String> oSuccessor = getSuccessor(tSuccessor, "o");
        verifyLeaf(oSuccessor, "to");
    }

    @Test
    public void testSubTree2() {
        testPut7();
        SortedListTrie<StringSequence, String> subTrie = trie.subTree(new StringSequence("te"));
        assertFalse(subTrie.isEmpty());
        assertEquals(3, subTrie.size());
        verifyRootNode(subTrie.getRootNode());
        verifySuccessors(subTrie.getRootNode(), "t");
        Node<StringSequence, String> tSuccessor = getSuccessor(subTrie.getRootNode(), "t");
        verifySuccessors(tSuccessor, "e");
        Node<StringSequence, String> eSuccessor = getSuccessor(tSuccessor, "e");
        verifySuccessors(eSuccessor, "a", "d", "n");
        Node<StringSequence, String> leaf = getSuccessor(eSuccessor, "a");
        verifyLeaf(leaf, "tea");
        leaf = getSuccessor(eSuccessor, "d");
        verifyLeaf(leaf, "ted");
        leaf = getSuccessor(eSuccessor, "n");
        verifyLeaf(leaf, "ten");
    }

    @Test
    public void testFirstEntry1() {
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
    public void testFirstKey1() {
        testPut6();
        StringSequence key = trie.firstKey();
        assertEquals(new StringSequence("in"), key);
    }

    @Test
    public void testFirstKey2() {
        testPutWithEmptyKey();
        StringSequence key = trie.firstKey();
        assertNull(key);
    }

    @Test
    public void testHashCode() {
        SortedListTrie<StringSequence, String> trie1 = new SortedListTrie<>();
        SortedListTrie<StringSequence, String> trie2 = new SortedListTrie<>();
        assertEquals(trie1.hashCode(), trie1.hashCode());
        assertEquals(trie1.hashCode(), trie2.hashCode());
        trie1.put(new StringSequence("f"), "value");
        assertNotEquals(trie1.hashCode(), trie2.hashCode());
        trie2.put(new StringSequence("f"), "value");
        assertEquals(trie1.hashCode(), trie2.hashCode());
        trie1.put(new StringSequence("fob"), "value2");
        assertNotEquals(trie1.hashCode(), trie2.hashCode());
    }

    @Test
    public void testEquals() {
        SortedListTrie<StringSequence, String> trie1 = new SortedListTrie<>();
        SortedListTrie<StringSequence, String> trie2 = new SortedListTrie<>();
        assertFalse(trie1.equals(null));
        assertFalse(trie1.equals(new Object()));
        assertTrue(trie1.equals(trie1));
        assertTrue(trie1.equals(trie2));
        trie1.put(new StringSequence("foo"), "value");
        assertFalse(trie1.equals(trie2));
        trie2.put(new StringSequence("foo"), "value");
        assertTrue(trie1.equals(trie2));
        trie1.put(new StringSequence("fob"), "value2");
        assertFalse(trie1.equals(trie2));
    }

    @Test
    public void testToString() {
        testPut3();
        assertEquals("SortedListTrie [tea=tea, ted=ted, to=to]", trie.toString());
    }

}
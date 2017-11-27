package de.mrapp.tries.datastructure;

import de.mrapp.tries.HashTrie;
import de.mrapp.tries.HashTrie.Node;
import de.mrapp.tries.datastructure.AbstractTrie.Node.Key;
import de.mrapp.tries.datastructure.AbstractTrie.Node.Value;
import de.mrapp.tries.sequence.StringSequence;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Tests the functionality of the class {@link HashTrie}.
 */
public class HashTrieTest {

    private HashTrie<StringSequence, String, String> trie;

    private void verifyRootNode(final Node<String, String> node) {
        assertNotNull(node);
        Key<String> key = node.getKey();
        assertNotNull(key);
        assertTrue(key.getSequence().isEmpty());
        assertNull(node.getValue());
    }

    private void verifySuccessors(final Node<String, String> node, final String... successors) {
        assertNotNull(node);
        assertEquals(successors.length, node.getAllSuccessors().size());

        for (String successor : successors) {
            getSuccessor(node, successor);
        }
    }

    private Node<String, String> getSuccessor(final Node<String, String> node,
                                              final String successor) {
        assertNotNull(node);
        Node<String, String> childNode = node.getSuccessor(new Key<>(successor));
        assertNotNull(childNode);
        Key<String> key = childNode.getKey();
        Collection<String> sequence = key.getSequence();
        assertEquals(1, sequence.size());
        assertEquals(successor, sequence.iterator().next());
        return childNode;
    }

    private void verifyLeaf(final Node<String, String> node, final String value) {
        assertNotNull(node);
        assertEquals(new Value<>(value), node.getValue());
        assertTrue(node.getAllSuccessors().isEmpty());
    }

    @Before
    public final void before() {
        this.trie = new HashTrie<>(new StringSequence.Builder());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testConstructorThrowsExceptionIfSequenceBuilderIsNull() {
        new HashTrie<StringSequence, String, String>(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testGetThrowsExceptionIfKeyIsNull() {
        trie.get(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testGetThrowsExceptionIfKeyIsEmpty() {
        trie.get(new StringSequence(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testPutThrowsExceptionIfKeyIsNull() {
        trie.put(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testPutThrowsExceptionIfKeyIsEmpty() {
        trie.put(new StringSequence(""), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testPutAllThrowsExceptionIfMapIsNull() {
        trie.putAll(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testPutAllThrowsExceptionIfMapContainsNullKey() {
        Map<StringSequence, String> map = new HashMap<>();
        map.put(null, null);
        trie.putAll(map);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testPutAllThrowsExceptionIfMapContainsEmptyKey() {
        Map<StringSequence, String> map = new HashMap<>();
        map.put(new StringSequence(""), null);
        trie.putAll(map);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testContainsKeyThrowsExceptionIfKeyIsNull() {
        trie.containsKey(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testContainsKeyThrowsExceptionIfKeyIsEmpty() {
        trie.containsKey(new StringSequence(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testRemoveThrowsExceptionIfKeyIsNull() {
        trie.remove(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testRemoveThrowsExceptionIfKeyIsEmpty() {
        trie.remove(new StringSequence(""));
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
        verifyRootNode(trie.rootNode);
        verifySuccessors(trie.rootNode, "t");
        Node<String, String> successor = getSuccessor(trie.rootNode, "t");
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
        verifyRootNode(trie.rootNode);
        verifySuccessors(trie.rootNode, "t");
        Node<String, String> successor = getSuccessor(trie.rootNode, "t");
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
        verifyRootNode(trie.rootNode);
        verifySuccessors(trie.rootNode, "t");
        Node<String, String> successor = getSuccessor(trie.rootNode, "t");
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
        verifyRootNode(trie.rootNode);
        verifySuccessors(trie.rootNode, "t");
        Node<String, String> successor = getSuccessor(trie.rootNode, "t");
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
        verifyRootNode(trie.rootNode);
        verifySuccessors(trie.rootNode, "t", "i");
        Node<String, String> successor = getSuccessor(trie.rootNode, "i");
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
        verifyRootNode(trie.rootNode);
        verifySuccessors(trie.rootNode, "t", "i");
        Node<String, String> successor = getSuccessor(trie.rootNode, "i");
        verifySuccessors(successor, "n");
        successor = getSuccessor(successor, "n");
        assertEquals(new Value<>(string), successor.getValue());
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
        verifyRootNode(trie.rootNode);
        verifySuccessors(trie.rootNode, "t", "i", "A");
        Node<String, String> successor = getSuccessor(trie.rootNode, "A");
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
        verifyRootNode(trie.rootNode);
        verifySuccessors(trie.rootNode, "t", "i", "A", "B");
        Node<String, String> successor = getSuccessor(trie.rootNode, "B");
        verifyLeaf(successor, duplicate);
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
        verifyRootNode(trie.rootNode);
        verifySuccessors(trie.rootNode, "t");
        Node<String, String> successor = getSuccessor(trie.rootNode, "t");
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
        verifyRootNode(trie.rootNode);
        verifySuccessors(trie.rootNode, "t");
        Node<String, String> successor = getSuccessor(trie.rootNode, "t");
        verifySuccessors(successor, "e", "o");
        Node<String, String> successorE = getSuccessor(successor, "e");
        verifySuccessors(successorE, "a");
        successorE = getSuccessor(successorE, "a");
        verifyLeaf(successorE, string1);
        Node<String, String> successorO = getSuccessor(successor, "o");
        verifyLeaf(successorO, string2);
    }

    @Test
    public final void testClear() {
        testPut7();
        trie.clear();
        assertTrue(trie.isEmpty());
        assertEquals(0, trie.size());
        assertNull(trie.rootNode);
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
        testPutWithDuplicateValue();
        Collection<String> values = trie.values();
        assertEquals(8, values.size());
        assertTrue(values.contains("tea"));
        assertTrue(values.contains("to"));
        assertTrue(values.contains("ted"));
        assertTrue(values.contains("ten"));
        assertTrue(values.contains("inn"));
        assertTrue(values.contains("in"));
        assertTrue(values.contains(null));
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
        testPutWithDuplicateValue();
        assertEquals(8, trie.size());
        assertTrue(trie.containsValue("tea"));
        assertTrue(trie.containsValue("to"));
        assertTrue(trie.containsValue("ted"));
        assertTrue(trie.containsValue("ten"));
        assertTrue(trie.containsValue("inn"));
        assertTrue(trie.containsValue("in"));
        assertTrue(trie.containsValue(null));
    }

    @Test
    public final void testKeySet() {
        testPutWithDuplicateValue();
        Collection<StringSequence> keys = trie.keySet();
        assertEquals(8, keys.size());
        assertTrue(keys.contains(new StringSequence("tea")));
        assertTrue(keys.contains(new StringSequence("to")));
        assertTrue(keys.contains(new StringSequence("ted")));
        assertTrue(keys.contains(new StringSequence("ten")));
        assertTrue(keys.contains(new StringSequence("inn")));
        assertTrue(keys.contains(new StringSequence("in")));
        assertTrue(keys.contains(new StringSequence("A")));
        assertTrue(keys.contains(new StringSequence("B")));
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
        testPutWithDuplicateValue();
        assertEquals(8, trie.size());
        assertTrue(trie.containsKey(new StringSequence("tea")));
        assertTrue(trie.containsKey(new StringSequence("to")));
        assertTrue(trie.containsKey(new StringSequence("ted")));
        assertTrue(trie.containsKey(new StringSequence("ten")));
        assertTrue(trie.containsKey(new StringSequence("inn")));
        assertTrue(trie.containsKey(new StringSequence("in")));
        assertTrue(trie.containsKey(new StringSequence("A")));
        assertTrue(trie.containsKey(new StringSequence("B")));
    }

    @Test
    public final void testEntrySet() {
        testPutWithDuplicateValue();
        Set<Map.Entry<StringSequence, String>> entrySet = trie.entrySet();
        assertEquals(8, entrySet.size());
        assertTrue(entrySet.contains(new AbstractMap.SimpleImmutableEntry<>(
                new StringSequence("tea"), "tea")));
        assertTrue(entrySet.contains(new AbstractMap.SimpleImmutableEntry<>(
                new StringSequence("to"), "to")));
        assertTrue(entrySet.contains(new AbstractMap.SimpleImmutableEntry<>(
                new StringSequence("ted"), "ted")));
        assertTrue(entrySet.contains(new AbstractMap.SimpleImmutableEntry<>(
                new StringSequence("ten"), "ten")));
        assertTrue(entrySet.contains(new AbstractMap.SimpleImmutableEntry<>(
                new StringSequence("inn"), "inn")));
        assertTrue(entrySet.contains(new AbstractMap.SimpleImmutableEntry<>(
                new StringSequence("in"), "in")));
        assertTrue(entrySet.contains(new AbstractMap.SimpleImmutableEntry<>(
                new StringSequence("A"), (String) null)));
        assertTrue(entrySet.contains(new AbstractMap.SimpleImmutableEntry<>(
                new StringSequence("B"), "tea")));
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
        verifyRootNode(trie.rootNode);
        verifySuccessors(trie.rootNode, "t");
        Node<String, String> successor = getSuccessor(trie.rootNode, "t");
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
        verifyRootNode(trie.rootNode);
        verifySuccessors(trie.rootNode, "t");
        Node<String, String> successor = getSuccessor(trie.rootNode, "t");
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
        verifyRootNode(trie.rootNode);
        verifySuccessors(trie.rootNode, "t");
        Node<String, String> successor = getSuccessor(trie.rootNode, "t");
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
        assertNull(trie.rootNode);
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
        verifyRootNode(trie.rootNode);
        verifySuccessors(trie.rootNode, "t");
        Node<String, String> successor = getSuccessor(trie.rootNode, "t");
        verifySuccessors(successor, "e");
        successor = getSuccessor(successor, "e");
        verifySuccessors(successor, "a");
        successor = getSuccessor(successor, "a");
        verifyLeaf(successor, "tea");
    }

    @Test
    public void testRemoveKeyIfKeyIsPrefix() {
        testPut6();
        String string = "in";
        String removed = trie.remove(new StringSequence(string));
        assertEquals(string, removed);
        assertNull(trie.get(new StringSequence(string)));
        assertEquals(5, trie.size());
        assertFalse(trie.isEmpty());
        verifyRootNode(trie.rootNode);
        verifySuccessors(trie.rootNode, "t", "i");
        Node<String, String> successor = getSuccessor(trie.rootNode, "i");
        verifySuccessors(successor, "n");
        successor = getSuccessor(successor, "n");
        verifySuccessors(successor, "n");
        successor = getSuccessor(successor, "n");
        verifyLeaf(successor, "inn");
    }

    @Test
    public void testRemoveKeyIfKeyContainsOtherKeyAsPrefix() {
        testPut6();
        String string = "inn";
        String removed = trie.remove(new StringSequence(string));
        assertEquals(string, removed);
        assertNull(trie.get(new StringSequence(string)));
        assertEquals(5, trie.size());
        assertFalse(trie.isEmpty());
        verifyRootNode(trie.rootNode);
        verifySuccessors(trie.rootNode, "t", "i");
        Node<String, String> successor = getSuccessor(trie.rootNode, "i");
        verifySuccessors(successor, "n");
        successor = getSuccessor(successor, "n");
        verifyLeaf(successor, "in");
    }

}
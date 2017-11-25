package de.mrapp.tries;

import de.mrapp.tries.AbstractTrie.Node.Key;
import de.mrapp.tries.HashTrie.Node;
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
        assertNull(key.getSequence());
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
        Object[] sequence = key.getSequence();
        assertEquals(1, sequence.length);
        assertEquals(successor, sequence[0]);
        assertTrue(childNode.getValue() == null || childNode.getAllSuccessors().isEmpty());
        return childNode;
    }

    private void verifyLeaf(final Node<String, String> node, final String value) {
        assertNotNull(node);
        assertEquals(value, node.getValue());
        assertTrue(node.getAllSuccessors().isEmpty());
    }

    @Before
    public final void before() {
        this.trie = new HashTrie<>(new StringSequence.Builder());
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
        testPut1();
        trie.clear();
        assertTrue(trie.isEmpty());
        assertEquals(0, trie.size());
        assertNull(trie.rootNode);
        assertNull(trie.get(new StringSequence("tea")));
        assertTrue(trie.values().isEmpty());
        assertTrue(trie.keySet().isEmpty());
        assertTrue(trie.entrySet().isEmpty());

    }

    @Test
    public final void testValues() {
        testPut5();
        Collection<String> values = trie.values();
        assertEquals(5, values.size());
        assertTrue(values.contains("tea"));
        assertTrue(values.contains("to"));
        assertTrue(values.contains("ted"));
        assertTrue(values.contains("ten"));
        assertTrue(values.contains("inn"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testValuesImmutable() {
        testPut5();
        Collection<String> values = trie.values();
        values.add("foo");
    }

    @Test(expected = ConcurrentModificationException.class)
    public final void testValuesThrowsConcurrentModificationException() {
        testPut5();
        Collection<String> values = trie.values();
        trie.put(new StringSequence("foo"), "foo");
        values.iterator().next();
    }

    @Test
    public final void testContainsValue() {
        testPut5();
        assertTrue(trie.containsValue("tea"));
        assertTrue(trie.containsValue("to"));
        assertTrue(trie.containsValue("ted"));
        assertTrue(trie.containsValue("ten"));
        assertTrue(trie.containsValue("inn"));
        assertFalse(trie.containsValue("te"));
    }

    @Test
    public final void testKeySet() {
        testPut5();
        Collection<StringSequence> keys = trie.keySet();
        assertEquals(5, keys.size());
        assertTrue(keys.contains(new StringSequence("tea")));
        assertTrue(keys.contains(new StringSequence("to")));
        assertTrue(keys.contains(new StringSequence("ted")));
        assertTrue(keys.contains(new StringSequence("ten")));
        assertTrue(keys.contains(new StringSequence("inn")));
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testKeySetImmutable() {
        testPut5();
        Collection<StringSequence> keys = trie.keySet();
        keys.add(new StringSequence("foo"));
    }

    @Test(expected = ConcurrentModificationException.class)
    public final void testKeySetThrowsConcurrentModificationException() {
        testPut5();
        Collection<StringSequence> keys = trie.keySet();
        trie.put(new StringSequence("foo"), "foo");
        keys.iterator().next();
    }

    @Test
    public final void testContainsKey() {
        testPut5();
        assertTrue(trie.containsKey(new StringSequence("tea")));
        assertTrue(trie.containsKey(new StringSequence("to")));
        assertTrue(trie.containsKey(new StringSequence("ted")));
        assertTrue(trie.containsKey(new StringSequence("ten")));
        assertTrue(trie.containsKey(new StringSequence("inn")));
        assertFalse(trie.containsKey(new StringSequence("te")));
    }

    @Test
    public final void testEntrySet() {
        testPut5();
        Set<Map.Entry<StringSequence, String>> entrySet = trie.entrySet();
        assertEquals(5, entrySet.size());
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
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testEntrySetImmutable() {
        testPut5();
        Set<Map.Entry<StringSequence, String>> entrySet = trie.entrySet();
        entrySet.add(new AbstractMap.SimpleImmutableEntry<>(
                new StringSequence("foo"), "foo"));
    }

    @Test(expected = ConcurrentModificationException.class)
    public final void testEntrySetThrowsConcurrentModificationException() {
        testPut5();
        Set<Map.Entry<StringSequence, String>> entrySet = trie.entrySet();
        trie.put(new StringSequence("foo"), "foo");
        entrySet.iterator().next();
    }

}
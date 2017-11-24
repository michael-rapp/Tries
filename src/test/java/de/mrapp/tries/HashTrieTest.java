package de.mrapp.tries;

import de.mrapp.tries.AbstractTrie.Node.Key;
import de.mrapp.tries.HashTrie.Node;
import org.junit.Before;
import org.junit.Test;

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
        this.trie = new HashTrie<>();
    }

    /**
     * Adds "tea" to the trie.
     */
    @Test
    public final void testPut1() {
        String string = "tea";
        String previous = trie.put(new StringSequence(string), string);
        assertNull(previous);
        assertEquals(string, trie.get(new StringSequence(string)));
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

}
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
import de.mrapp.tries.NodeValue;
import de.mrapp.tries.sequence.StringSequence;
import org.junit.Test;

import java.util.Iterator;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Tests the functionality of the class {@link SortedListNode}.
 *
 * @author Michael Rapp
 */
public class SortedListNodeTest {

    @Test
    public final void testConstructor() {
        SortedListNode<StringSequence, String> node = new SortedListNode<>(null);
        assertEquals(0, node.getSuccessorCount());
        assertEquals(0, node.getSuccessorValueCount());
        assertNull(node.getPredecessor());
        assertFalse(node.iterator().hasNext());
        assertNull(node.getNodeValue());
        assertNull(node.getValue());
        assertFalse(node.isValueSet());
    }

    @Test
    public final void testSetNodeValue() {
        String value = "value";
        NodeValue<String> nodeValue = new NodeValue<>(value);
        SortedListNode<StringSequence, String> node = new SortedListNode<>(null);
        node.setNodeValue(nodeValue);
        assertEquals(nodeValue, node.getNodeValue());
        assertEquals(value, node.getValue());
        assertTrue(node.isValueSet());
    }

    @Test
    public final void testAddSuccessor1() {
        StringSequence key = new StringSequence("key");
        SortedListNode<StringSequence, String> node = new SortedListNode<>(null);
        Node<StringSequence, String> successor = node.addSuccessor(key);
        assertNotNull(successor);
        assertEquals(1, node.getSuccessorCount());
        assertEquals(successor, node.getSuccessor(key));
        assertEquals(0, node.getSuccessorValueCount());
        Iterator<StringSequence> iterator = node.iterator();
        assertEquals(key, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public final void testAddSuccessor2() {
        StringSequence key = new StringSequence("key");
        Node<StringSequence, String> successor = new SortedListNode<>(null);
        successor.setNodeValue(new NodeValue<>("value"));
        SortedListNode<StringSequence, String> node = new SortedListNode<>(null);
        Node<StringSequence, String> result = node.addSuccessor(key, successor);
        assertEquals(successor, result);
        assertEquals(1, node.getSuccessorCount());
        assertEquals(successor, node.getSuccessor(key));
        assertEquals(1, node.getSuccessorValueCount());
        Iterator<StringSequence> iterator = node.iterator();
        assertEquals(key, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public final void testRemoveSuccessor() {
        StringSequence key = new StringSequence("key");
        SortedListNode<StringSequence, String> node = new SortedListNode<>(null);
        node.addSuccessor(key);
        node.removeSuccessor(key);
        assertEquals(0, node.getSuccessorCount());
        assertNull(node.getSuccessor(key));
        assertEquals(0, node.getSuccessorValueCount());
        Iterator<StringSequence> iterator = node.iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public final void testIncreaseSuccessorValueCount() {
        int by = 2;
        SortedListNode<StringSequence, String> node = new SortedListNode<>(null);
        node.increaseSuccessorValueCount(by);
        assertEquals(by, node.getSuccessorValueCount());
    }

    @Test
    public final void testDecreaseSuccessorValueCount() {
        int by = 2;
        SortedListNode<StringSequence, String> node = new SortedListNode<>(null);
        node.increaseSuccessorValueCount(by);
        node.decreaseSuccessorValueCount(by);
        assertEquals(0, node.getSuccessorValueCount());
    }

    @Test
    public final void testSetPredecessor() {
        Node<StringSequence, String> predecessor = new SortedListNode<>(null);
        SortedListNode<StringSequence, String> node = new SortedListNode<>(null);
        node.increaseSuccessorValueCount(1);
        node.setPredecessor(predecessor);
        assertEquals(predecessor, node.getPredecessor());
    }

    @Test
    public final void testGetSuccessorByIndex() {
        Node<StringSequence, String> successor = mock(Node.class);
        SortedListNode<StringSequence, String> node = new SortedListNode<>(null);
        node.addSuccessor(new StringSequence("a"));
        node.addSuccessor(new StringSequence("b"));
        node.addSuccessor(new StringSequence("c"), successor);
        node.addSuccessor(new StringSequence("d"));
        node.addSuccessor(new StringSequence("e"));
        node.addSuccessor(new StringSequence("f"));
        assertEquals(successor, node.getSuccessor(2));
    }

    @Test
    public final void testGetFirstSuccessor() {
        Node<StringSequence, String> successor = mock(Node.class);
        SortedListNode<StringSequence, String> node = new SortedListNode<>(null);
        node.addSuccessor(new StringSequence("a"), successor);
        node.addSuccessor(new StringSequence("b"));
        node.addSuccessor(new StringSequence("c"));
        node.addSuccessor(new StringSequence("d"));
        node.addSuccessor(new StringSequence("e"));
        node.addSuccessor(new StringSequence("f"));
        assertEquals(successor, node.getFirstSuccessor());
    }

    @Test
    public final void testGetLastSuccessor() {
        Node<StringSequence, String> successor = mock(Node.class);
        SortedListNode<StringSequence, String> node = new SortedListNode<>(null);
        node.addSuccessor(new StringSequence("a"));
        node.addSuccessor(new StringSequence("b"));
        node.addSuccessor(new StringSequence("c"));
        node.addSuccessor(new StringSequence("d"));
        node.addSuccessor(new StringSequence("e"));
        node.addSuccessor(new StringSequence("f"), successor);
        assertEquals(successor, node.getLastSuccessor());
    }

    @Test
    public final void testGetSuccessorKey() {
        SortedListNode<StringSequence, String> node = new SortedListNode<>(null);
        node.addSuccessor(new StringSequence("a"));
        node.addSuccessor(new StringSequence("b"));
        node.addSuccessor(new StringSequence("c"));
        node.addSuccessor(new StringSequence("d"));
        node.addSuccessor(new StringSequence("e"));
        node.addSuccessor(new StringSequence("f"));
        assertEquals(new StringSequence("c"), node.getSuccessorKey(2));
    }

    @Test
    public final void testGetFirstSuccessorKey() {
        SortedListNode<StringSequence, String> node = new SortedListNode<>(null);
        node.addSuccessor(new StringSequence("a"));
        node.addSuccessor(new StringSequence("b"));
        node.addSuccessor(new StringSequence("c"));
        node.addSuccessor(new StringSequence("d"));
        node.addSuccessor(new StringSequence("e"));
        node.addSuccessor(new StringSequence("f"));
        assertEquals(new StringSequence("a"), node.getFirstSuccessorKey());
    }

    @Test
    public final void testGetLastSuccessorKey() {
        SortedListNode<StringSequence, String> node = new SortedListNode<>(null);
        node.addSuccessor(new StringSequence("a"));
        node.addSuccessor(new StringSequence("b"));
        node.addSuccessor(new StringSequence("c"));
        node.addSuccessor(new StringSequence("d"));
        node.addSuccessor(new StringSequence("e"));
        node.addSuccessor(new StringSequence("f"));
        assertEquals(new StringSequence("f"), node.getLastSuccessorKey());
    }

    @Test
    public final void testIndexOf() {
        SortedListNode<StringSequence, String> node = new SortedListNode<>(null);
        node.addSuccessor(new StringSequence("a"));
        node.addSuccessor(new StringSequence("b"));
        node.addSuccessor(new StringSequence("c"));
        node.addSuccessor(new StringSequence("d"));
        node.addSuccessor(new StringSequence("e"));
        node.addSuccessor(new StringSequence("f"));
        assertEquals(2, node.indexOf(new StringSequence("c")));
    }

    @Test
    public final void testClone() {
        StringSequence key = new StringSequence("key");
        SortedListNode<StringSequence, String> node = new SortedListNode<>(null);
        node.setNodeValue(new NodeValue<>("value"));
        node.addSuccessor(key);
        Node<StringSequence, String> clone = node.clone();
        assertTrue(node.equals(clone));
        assertFalse(node == clone);
        assertFalse(node.getSuccessor(key) == clone.getSuccessor(key));
    }

    @Test
    public final void testToString() {
        String value = "value";
        StringSequence key = new StringSequence("key");
        SortedListNode<StringSequence, String> node = new SortedListNode<>(null);
        node.setNodeValue(new NodeValue<>(value));
        node.addSuccessor(key);
        assertEquals("Node{value=" + value + ", successors=[" + key + "]}", node.toString());
    }

    @Test
    public final void testHashCode() {
        SortedListNode<StringSequence, String> node1 = new SortedListNode<>(null);
        SortedListNode<StringSequence, String> node2 = new SortedListNode<>(null);
        assertEquals(node1.hashCode(), node1.hashCode());
        assertEquals(node1.hashCode(), node2.hashCode());
        node1.setNodeValue(new NodeValue<>("foo"));
        assertNotEquals(node1.hashCode(), node2.hashCode());
        node2.setNodeValue(new NodeValue<>("bar"));
        assertNotEquals(node1.hashCode(), node2.hashCode());
        node2.setNodeValue(new NodeValue<>("foo"));
        assertEquals(node1.hashCode(), node2.hashCode());
        node1.addSuccessor(new StringSequence("foo"));
        assertNotEquals(node1.hashCode(), node2.hashCode());
        node2.addSuccessor(new StringSequence("bar"));
        assertNotEquals(node1.hashCode(), node2.hashCode());
        node1.addSuccessor(new StringSequence("bar"));
        node2.addSuccessor(new StringSequence("foo"));
        assertEquals(node1.hashCode(), node2.hashCode());
    }

    @Test
    public final void testEquals() {
        SortedListNode<StringSequence, String> node1 = new SortedListNode<>(null);
        SortedListNode<StringSequence, String> node2 = new SortedListNode<>(null);
        assertFalse(node1.equals(null));
        assertFalse(node1.equals(new Object()));
        assertTrue(node1.equals(node1));
        assertTrue(node1.equals(node2));
        node1.setNodeValue(new NodeValue<>("foo"));
        assertFalse(node1.equals(node2));
        node2.setNodeValue(new NodeValue<>("bar"));
        assertFalse(node1.equals(node2));
        node2.setNodeValue(new NodeValue<>("foo"));
        assertTrue(node1.equals(node2));
        node1.addSuccessor(new StringSequence("foo"));
        assertFalse(node1.equals(node2));
        node2.addSuccessor(new StringSequence("bar"));
        assertFalse(node1.equals(node2));
        node1.addSuccessor(new StringSequence("bar"));
        node2.addSuccessor(new StringSequence("foo"));
        assertTrue(node1.equals(node2));
    }

}
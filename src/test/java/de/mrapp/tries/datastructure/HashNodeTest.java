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
package de.mrapp.tries.datastructure;

import de.mrapp.tries.Node;
import de.mrapp.tries.NodeValue;
import org.junit.Test;

import java.util.Iterator;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Tests the functionality of the class {@link HashNode}.
 *
 * @author Michael Rapp
 */
public class HashNodeTest {

    @Test
    public final void testConstructor() {
        HashNode<String, String> node = new HashNode<>();
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
        HashNode<String, String> node = new HashNode<>();
        node.setNodeValue(nodeValue);
        assertEquals(nodeValue, node.getNodeValue());
        assertEquals(value, node.getValue());
        assertTrue(node.isValueSet());
    }

    @Test
    public final void testAddSuccessor1() {
        String key = "key";
        HashNode<String, String> node = new HashNode<>();
        Node<String, String> successor = node.addSuccessor(key);
        assertNotNull(successor);
        assertEquals(1, node.getSuccessorCount());
        assertEquals(successor, node.getSuccessor(key));
        assertEquals(0, node.getSuccessorValueCount());
        Iterator<String> iterator = node.iterator();
        assertEquals(key, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public final void testAddSuccessor2() {
        String key = "key";
        Node<String, String> successor = new HashNode<>();
        successor.setNodeValue(new NodeValue<>("value"));
        HashNode<String, String> node = new HashNode<>();
        Node<String, String> result = node.addSuccessor(key, successor);
        assertEquals(successor, result);
        assertEquals(1, node.getSuccessorCount());
        assertEquals(successor, node.getSuccessor(key));
        assertEquals(1, node.getSuccessorValueCount());
        Iterator<String> iterator = node.iterator();
        assertEquals(key, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public final void testRemoveSuccessor() {
        String key = "key";
        HashNode<String, String> node = new HashNode<>();
        node.addSuccessor(key);
        node.removeSuccessor(key);
        assertEquals(0, node.getSuccessorCount());
        assertNull(node.getSuccessor(key));
        assertEquals(0, node.getSuccessorValueCount());
        Iterator<String> iterator = node.iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public final void testIncreaseSuccessorValueCount() {
        int by = 2;
        HashNode<String, String> node = new HashNode<>();
        node.increaseSuccessorValueCount(by);
        assertEquals(by, node.getSuccessorValueCount());
    }

    @Test
    public final void testDecreaseSuccessorValueCount() {
        int by = 2;
        HashNode<String, String> node = new HashNode<>();
        node.increaseSuccessorValueCount(by);
        node.decreaseSuccessorValueCount(by);
        assertEquals(0, node.getSuccessorValueCount());
    }

    @Test
    public final void testSetPredecessor() {
        Node<String, String> predecessor = new HashNode<>();
        HashNode<String, String> node = new HashNode<>();
        node.increaseSuccessorValueCount(1);
        node.setPredecessor(predecessor);
        assertEquals(predecessor, node.getPredecessor());
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testGetSuccessorByIndex() {
        HashNode<String, String> node = new HashNode<>();
        node.getSuccessor(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testGetFirstSuccessor() {
        HashNode<String, String> node = new HashNode<>();
        node.getFirstSuccessor();
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testGetLastSuccessor() {
        HashNode<String, String> node = new HashNode<>();
        node.getLastSuccessor();
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testGetSuccessorKey() {
        HashNode<String, String> node = new HashNode<>();
        node.getSuccessorKey(2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testGetFirstSuccessorKey() {
        HashNode<String, String> node = new HashNode<>();
        node.getFirstSuccessorKey();
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testGetLastSuccessorKey() {
        HashNode<String, String> node = new HashNode<>();
        node.getLastSuccessorKey();
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testIndexOf() {
        HashNode<String, String> node = new HashNode<>();
        node.indexOf("c");
    }

    @Test
    public final void testClone() {
        String key = "key";
        HashNode<String, String> node = new HashNode<>();
        node.setNodeValue(new NodeValue<>("value"));
        node.addSuccessor(key);
        Node<String, String> clone = node.clone();
        assertTrue(node.equals(clone));
        assertFalse(node == clone);
        assertFalse(node.getSuccessor(key) == clone.getSuccessor(key));
    }

    @Test
    public final void testToString() {
        String value = "value";
        String key = "key";
        HashNode<String, String> node = new HashNode<>();
        node.setNodeValue(new NodeValue<>(value));
        node.addSuccessor(key);
        assertEquals("Node{value=" + value + ", successors=[" + key + "]}", node.toString());
    }

    @Test
    public final void testHashCode() {
        HashNode<String, String> node1 = new HashNode<>();
        HashNode<String, String> node2 = new HashNode<>();
        assertEquals(node1.hashCode(), node1.hashCode());
        assertEquals(node1.hashCode(), node2.hashCode());
        node1.setNodeValue(new NodeValue<>("foo"));
        assertNotEquals(node1.hashCode(), node2.hashCode());
        node2.setNodeValue(new NodeValue<>("bar"));
        assertNotEquals(node1.hashCode(), node2.hashCode());
        node2.setNodeValue(new NodeValue<>("foo"));
        assertEquals(node1.hashCode(), node2.hashCode());
        node1.addSuccessor("foo");
        assertNotEquals(node1.hashCode(), node2.hashCode());
        node2.addSuccessor("bar");
        assertNotEquals(node1.hashCode(), node2.hashCode());
        node1.addSuccessor("bar");
        node2.addSuccessor("foo");
        assertEquals(node1.hashCode(), node2.hashCode());
    }

    @Test
    public final void testEquals() {
        HashNode<String, String> node1 = new HashNode<>();
        HashNode<String, String> node2 = new HashNode<>();
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
        node1.addSuccessor("foo");
        assertFalse(node1.equals(node2));
        node2.addSuccessor("bar");
        assertFalse(node1.equals(node2));
        node1.addSuccessor("bar");
        node2.addSuccessor("foo");
        assertTrue(node1.equals(node2));
    }

}
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
package de.mrapp.tries.datastructure.node;

import de.mrapp.tries.Node;
import de.mrapp.tries.NodeValue;
import de.mrapp.tries.datastructure.node.HashNode;
import de.mrapp.tries.sequence.StringSequence;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.*;

/**
 * Tests the functionality of the class {@link HashNode}.
 *
 * @author Michael Rapp
 */
public class HashNodeTest {

    @Test
    public final void testConstructor() {
        HashNode<StringSequence, String> node = new HashNode<>();
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
        HashNode<StringSequence, String> node = new HashNode<>();
        node.setNodeValue(nodeValue);
        assertEquals(nodeValue, node.getNodeValue());
        assertEquals(value, node.getValue());
        assertTrue(node.isValueSet());
    }

    @Test
    public final void testAddSuccessor1() {
        StringSequence key = new StringSequence("key");
        HashNode<StringSequence, String> node = new HashNode<>();
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
        Node<StringSequence, String> successor = new HashNode<>();
        successor.setNodeValue(new NodeValue<>("value"));
        HashNode<StringSequence, String> node = new HashNode<>();
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
        HashNode<StringSequence, String> node = new HashNode<>();
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
        HashNode<StringSequence, String> node = new HashNode<>();
        node.increaseSuccessorValueCount(by);
        assertEquals(by, node.getSuccessorValueCount());
    }

    @Test
    public final void testDecreaseSuccessorValueCount() {
        int by = 2;
        HashNode<StringSequence, String> node = new HashNode<>();
        node.increaseSuccessorValueCount(by);
        node.decreaseSuccessorValueCount(by);
        assertEquals(0, node.getSuccessorValueCount());
    }

    @Test
    public final void testSetPredecessor() {
        Node<StringSequence, String> predecessor = new HashNode<>();
        Map.Entry<StringSequence, Node<StringSequence, String>> entry =
                new AbstractMap.SimpleImmutableEntry<>(new StringSequence("foo"), predecessor);
        HashNode<StringSequence, String> node = new HashNode<>();
        node.increaseSuccessorValueCount(1);
        node.setPredecessor(entry);
        assertEquals(entry, node.getPredecessor());
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testGetSuccessorByIndex() {
        HashNode<StringSequence, String> node = new HashNode<>();
        node.getSuccessor(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testGetFirstSuccessor() {
        HashNode<StringSequence, String> node = new HashNode<>();
        node.getFirstSuccessor();
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testGetLastSuccessor() {
        HashNode<StringSequence, String> node = new HashNode<>();
        node.getLastSuccessor();
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testGetSuccessorKey() {
        HashNode<StringSequence, String> node = new HashNode<>();
        node.getSuccessorKey(2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testGetFirstSuccessorKey() {
        HashNode<StringSequence, String> node = new HashNode<>();
        node.getFirstSuccessorKey();
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testGetLastSuccessorKey() {
        HashNode<StringSequence, String> node = new HashNode<>();
        node.getLastSuccessorKey();
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testIndexOf() {
        HashNode<StringSequence, String> node = new HashNode<>();
        node.indexOf(new StringSequence("c"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testIndexOfFirstElement() {
        HashNode<StringSequence, String> node = new HashNode<>();
        node.indexOfFirstElement(new StringSequence("c"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testRemoveSuccessorByIndex() {
        HashNode<StringSequence, String> node = new HashNode<>();
        node.removeSuccessor(2);
    }

    @Test
    public final void testClone() {
        StringSequence key = new StringSequence("key");
        HashNode<StringSequence, String> node = new HashNode<>();
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
        HashNode<StringSequence, String> node = new HashNode<>();
        node.setNodeValue(new NodeValue<>(value));
        node.addSuccessor(key);
        assertEquals("Node{value=" + value + ", successors=[" + key + "]}", node.toString());
    }

    @Test
    public final void testHashCode() {
        HashNode<StringSequence, String> node1 = new HashNode<>();
        HashNode<StringSequence, String> node2 = new HashNode<>();
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
        HashNode<StringSequence, String> node1 = new HashNode<>();
        HashNode<StringSequence, String> node2 = new HashNode<>();
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
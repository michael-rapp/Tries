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
import de.mrapp.tries.sequence.StringSequence;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Iterator;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests the functionality of the class {@link StringNodeWrapper}.
 *
 * @author Michael Rapp
 */
@RunWith(MockitoJUnitRunner.class)
public class StringNodeWrapperTest {

    @InjectMocks
    private StringNodeWrapper<String> nodeWrapper;

    @Mock
    private Node<StringSequence, String> node;

    @Test(expected = IllegalArgumentException.class)
    public final void testConstructorThrowsExceptionIfNodeIsNull() {
        new UnmodifiableNode<>(null);
    }

    @Test
    public final void testGetNodeValue() {
        NodeValue<String> nodeValue = mock(NodeValue.class);
        when(node.getNodeValue()).thenReturn(nodeValue);
        assertEquals(nodeValue, nodeWrapper.getNodeValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testSetNodeValue() {
        nodeWrapper.setNodeValue(null);
    }

    @Test
    public final void testGetSuccessorCount() {
        int successorCount = 2;
        when(node.getSuccessorCount()).thenReturn(successorCount);
        assertEquals(successorCount, nodeWrapper.getSuccessorCount());
    }

    @Test
    public final void testGetSuccessor() {
        Node<StringSequence, String> successor = mock(Node.class);
        when(node.getSuccessor(new StringSequence("key"))).thenReturn(successor);
        assertTrue(nodeWrapper.getSuccessor("key") instanceof StringNodeWrapper);
        assertEquals(successor.toString(), nodeWrapper.getSuccessor("key").toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testAddSuccessor1() {
        nodeWrapper.addSuccessor("key");
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testAddSuccessor2() {
        nodeWrapper.addSuccessor("key", null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testRemoveSuccessor() {
        nodeWrapper.removeSuccessor("key");
    }

    @Test
    public final void testGetSuccessorValueCount() {
        int successorValueCount = 2;
        when(node.getSuccessorValueCount()).thenReturn(successorValueCount);
        assertEquals(successorValueCount, nodeWrapper.getSuccessorValueCount());
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testIncreaseSuccessorValueCount() {
        nodeWrapper.increaseSuccessorValueCount(2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testDecreaseSuccessorValueCount() {
        nodeWrapper.decreaseSuccessorValueCount(2);
    }

    @Test
    public final void testGetPredecessor() {
        Node<StringSequence, String> predecessor = mock(Node.class);
        when(node.getPredecessor()).thenReturn(predecessor);
        assertTrue(nodeWrapper.getPredecessor() instanceof StringNodeWrapper);
        assertEquals(predecessor.toString(), nodeWrapper.getPredecessor().toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testSetPredecessor() {
        nodeWrapper.setPredecessor(null);
    }

    @Test
    public final void testIterator() {
        Iterator<StringSequence> iterator = mock(Iterator.class);
        when(iterator.hasNext()).thenReturn(true, true, true, false);
        when(iterator.next()).thenReturn(new StringSequence("1"), new StringSequence("2"),
                new StringSequence("3"), null);
        when(node.iterator()).thenReturn(iterator);
        Iterator<String> stringIterator = nodeWrapper.iterator();
        assertTrue(stringIterator.hasNext());
        assertEquals("1", stringIterator.next());
        assertTrue(stringIterator.hasNext());
        assertEquals("2", stringIterator.next());
        assertTrue(stringIterator.hasNext());
        assertEquals("3", stringIterator.next());
        assertFalse(stringIterator.hasNext());
    }

    @Test
    public final void testClone() {
        when(node.clone()).thenReturn(node);
        Node<String, String> clone = nodeWrapper.clone();
        assertEquals(clone.toString(), nodeWrapper.toString());
    }

    @Test
    public final void testToString() {
        assertEquals(node.toString(), nodeWrapper.toString());
    }

    @Test
    public final void testHashCode() {
        Node<StringSequence, String> node1 = new HashNode<>();
        Node<StringSequence, String> node2 = new HashNode<>();
        StringNodeWrapper<String> nodeWrapper1 = new StringNodeWrapper<>(node1);
        StringNodeWrapper<String> nodeWrapper2 = new StringNodeWrapper<>(node2);
        assertEquals(nodeWrapper1.hashCode(), nodeWrapper1.hashCode());
        assertEquals(nodeWrapper1.hashCode(), nodeWrapper2.hashCode());
        node2.setNodeValue(new NodeValue<>("foo"));
        assertNotEquals(nodeWrapper1.hashCode(), nodeWrapper2.hashCode());
    }

    @Test
    public final void testEquals() {
        Node<StringSequence, String> node1 = new HashNode<>();
        Node<StringSequence, String> node2 = new HashNode<>();
        StringNodeWrapper<String> nodeWrapper1 = new StringNodeWrapper<>(node1);
        StringNodeWrapper<String> nodeWrapper2 = new StringNodeWrapper<>(node2);
        assertFalse(nodeWrapper1.equals(null));
        assertFalse(nodeWrapper1.equals(new Object()));
        assertTrue(nodeWrapper1.equals(nodeWrapper1));
        assertTrue(nodeWrapper1.equals(nodeWrapper2));
        node2.setNodeValue(new NodeValue<>("foo"));
        assertFalse(nodeWrapper1.equals(nodeWrapper2));
    }

}
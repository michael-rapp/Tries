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
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests the functionality of the class {@link UnmodifiableNode}.
 *
 * @author Michael Rapp
 */
@RunWith(MockitoJUnitRunner.class)
public class UnmodifiableNodeTest {

    @InjectMocks
    private UnmodifiableNode<StringSequence, String> unmodifiableNode;

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
        assertEquals(nodeValue, unmodifiableNode.getNodeValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testSetNodeValue() {
        unmodifiableNode.setNodeValue(null);
    }

    @Test
    public final void testGetSuccessorCount() {
        int successorCount = 2;
        when(node.getSuccessorCount()).thenReturn(successorCount);
        assertEquals(successorCount, unmodifiableNode.getSuccessorCount());
    }

    @Test
    public final void testGetSuccessor() {
        StringSequence sequence = mock(StringSequence.class);
        Node<StringSequence, String> successor = mock(Node.class);
        when(node.getSuccessor(sequence)).thenReturn(successor);
        assertTrue(unmodifiableNode.getSuccessor(sequence) instanceof UnmodifiableNode);
        assertEquals(successor.hashCode(), unmodifiableNode.getSuccessor(sequence).hashCode());
    }

    @Test
    public final void testGetSuccessorKey() {
        StringSequence sequence = new StringSequence("foo");
        int index = 1;
        when(node.getSuccessorKey(index)).thenReturn(sequence);
        assertEquals(sequence, unmodifiableNode.getSuccessorKey(index));
    }

    @Test
    public final void testGetSuccessorByIndex() {
        Node<StringSequence, String> successor = mock(Node.class);
        int index = 1;
        when(node.getSuccessor(index)).thenReturn(successor);
        assertEquals(successor.hashCode(), unmodifiableNode.getSuccessor(index).hashCode());
    }

    @Test
    public final void testIndexOf() {
        int index = 1;
        StringSequence key = mock(StringSequence.class);
        when(node.indexOf(key)).thenReturn(index);
        assertEquals(index, unmodifiableNode.indexOf(key));
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testAddSuccessor1() {
        unmodifiableNode.addSuccessor(mock(StringSequence.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testAddSuccessor2() {
        unmodifiableNode.addSuccessor(mock(StringSequence.class), null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testRemoveSuccessor() {
        unmodifiableNode.removeSuccessor(mock(StringSequence.class));
    }

    @Test
    public final void testGetSuccessorValueCount() {
        int successorValueCount = 2;
        when(node.getSuccessorValueCount()).thenReturn(successorValueCount);
        assertEquals(successorValueCount, unmodifiableNode.getSuccessorValueCount());
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testIncreaseSuccessorValueCount() {
        unmodifiableNode.increaseSuccessorValueCount(2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testDecreaseSuccessorValueCount() {
        unmodifiableNode.decreaseSuccessorValueCount(2);
    }

    @Test
    public final void testGetPredecessor() {
        StringSequence key = new StringSequence("foo");
        Node<StringSequence, String> predecessor = mock(Node.class);
        Map.Entry<StringSequence, Node<StringSequence, String>> entry =
                new AbstractMap.SimpleImmutableEntry<>(key, predecessor);
        when(node.getPredecessor()).thenReturn(entry);
        Map.Entry<StringSequence, Node<StringSequence, String>> returnedEntry = unmodifiableNode
                .getPredecessor();
        assertNotNull(returnedEntry);
        assertEquals(returnedEntry.getKey(), key);
        assertTrue(returnedEntry.getValue() instanceof UnmodifiableNode);
        assertEquals(predecessor.hashCode(), returnedEntry.getValue().hashCode());
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testSetPredecessor() {
        unmodifiableNode.setPredecessor(null);
    }

    @Test
    public final void testIterator() {
        Iterator<StringSequence> iterator = mock(Iterator.class);
        when(node.iterator()).thenReturn(iterator);
        assertEquals(iterator, unmodifiableNode.iterator());
    }

    @Test
    public final void testClone() {
        Node<StringSequence, String> clone = unmodifiableNode.clone();
        assertEquals(clone.hashCode(), unmodifiableNode.hashCode());
    }

    @Test
    public final void testToString() {
        assertEquals(node.toString(), unmodifiableNode.toString());
    }

    @Test
    public final void testHashCode() {
        assertEquals(node.hashCode(), unmodifiableNode.hashCode());
    }

    @Test
    public final void testEquals() {
        assertTrue(unmodifiableNode.equals(node));
    }

}
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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * An abstract base class for all tests, which test the functionality of a {@link Trie}
 * implementation.
 *
 * @param <SequenceType> The type of the sequences, which are used as the trie's keys
 * @author Michael Rapp
 */
public abstract class AbstractTrieTest<SequenceType, TrieType extends Map<SequenceType, String>> {

    TrieType trie;

    abstract TrieType onCreateTrie();

    abstract SequenceType convertToSequence(@NotNull final String string);

    abstract Node<SequenceType, String> getRootNode(@NotNull final TrieType trie);

    final void verifyRootNode(@Nullable final Node<SequenceType, String> node) {
        verifyRootNode(node, null);
    }

    final void verifyRootNode(@Nullable final Node<SequenceType, String> node,
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

    final void verifySuccessors(@Nullable final Node<SequenceType, String> node,
                                @NotNull final String... successors) {
        assertNotNull(node);
        assertEquals(successors.length, node.getSuccessorCount());

        for (String successor : successors) {
            getSuccessor(node, successor);
        }
    }

    final Node<SequenceType, String> getSuccessor(
            @Nullable final Node<SequenceType, String> node,
            @NotNull final String successor) {
        assertNotNull(node);
        Node<SequenceType, String> childNode = node.getSuccessor(convertToSequence(successor));
        assertNotNull(childNode);
        return childNode;
    }

    final void verifyLeaf(@Nullable final Node<SequenceType, String> node,
                          @Nullable final String value) {
        assertNotNull(node);
        Assert.assertEquals(new NodeValue<>(value), node.getNodeValue());
        assertEquals(0, node.getSuccessorCount());
    }

    @Before
    public void before() {
        this.trie = onCreateTrie();
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testPutAllThrowsExceptionIfMapIsNull() {
        trie.putAll(null);
    }

    @Test
    public final void testRemoveEmptyKeyIfKeyIsTheOnlyOne() {
        String string = "empty";
        trie.put(convertToSequence(""), string);
        String removed = trie.remove(convertToSequence(""));
        assertEquals(string, removed);
        assertNull(trie.get(convertToSequence("")));
        assertNull(trie.get(null));
        assertEquals(0, trie.size());
        assertTrue(trie.isEmpty());
        assertNull(getRootNode(trie));
    }

    @Test
    public final void testRemoveNullKeyIfKeyIsTheOnlyOne() {
        String string = "empty";
        trie.put(convertToSequence(""), string);
        String removed = trie.remove(null);
        assertEquals(string, removed);
        assertNull(trie.get(convertToSequence("")));
        assertNull(trie.get(null));
        assertEquals(0, trie.size());
        assertTrue(trie.isEmpty());
        assertNull(getRootNode(trie));
    }

    @Test
    public final void testHashCode() {
        TrieType trie1 = onCreateTrie();
        TrieType trie2 = onCreateTrie();
        assertEquals(trie1.hashCode(), trie1.hashCode());
        assertEquals(trie1.hashCode(), trie2.hashCode());
        trie1.put(convertToSequence("foo"), "value");
        assertNotEquals(trie1.hashCode(), trie2.hashCode());
        trie2.put(convertToSequence("foo"), "value");
        assertEquals(trie1.hashCode(), trie2.hashCode());
        trie1.put(convertToSequence("fob"), "value2");
        assertNotEquals(trie1.hashCode(), trie2.hashCode());
    }

    @Test
    public final void testEquals() {
        TrieType trie1 = onCreateTrie();
        TrieType trie2 = onCreateTrie();
        assertFalse(trie1.equals(null));
        assertFalse(trie1.equals(new Object()));
        assertTrue(trie1.equals(trie1));
        assertTrue(trie1.equals(trie2));
        trie1.put(convertToSequence("foo"), "value");
        assertFalse(trie1.equals(trie2));
        trie2.put(convertToSequence("foo"), "value");
        assertTrue(trie1.equals(trie2));
        trie1.put(convertToSequence("fob"), "value2");
        assertFalse(trie1.equals(trie2));
    }

}
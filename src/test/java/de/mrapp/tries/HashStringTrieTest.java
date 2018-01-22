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
package de.mrapp.tries;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the functionality of the class {@link HashStringTrie}.
 *
 * @author Michael Rapp
 */
public class HashStringTrieTest extends AbstractStringNonPatriciaTrieTest<HashStringTrie<String>> {

    @Override
    HashStringTrie<String> onCreateTrie() {
        return new HashStringTrie<>();
    }

    @Test
    public final void testSubTrieWithEmptySequence() {
        testPutWithNullKey();
        StringTrie<String> subTrie = trie.subTrie("");
        assertFalse(subTrie.isEmpty());
        assertEquals(8, subTrie.size());
        assertNull("A", subTrie.get("A"));
        assertEquals("tea", subTrie.get("B"));
        assertEquals("tea", subTrie.get("tea"));
        assertEquals("ted", subTrie.get("ted"));
        assertEquals("ten", subTrie.get("ten"));
        assertEquals("to", subTrie.get("to"));
        assertEquals("in", subTrie.get("in"));
        assertEquals("inn", subTrie.get("inn"));
        verifyRootNode(subTrie.getRootNode());
        verifySuccessors(subTrie.getRootNode(), "A", "B", "t", "i");
        Node<String, String> ASuccessor = getSuccessor(subTrie.getRootNode(), "A");
        verifyLeaf(ASuccessor, null);
        Node<String, String> BSuccessor = getSuccessor(subTrie.getRootNode(), "B");
        verifyLeaf(BSuccessor, "tea");
        Node<String, String> tSuccessor = getSuccessor(subTrie.getRootNode(), "t");
        verifySuccessors(tSuccessor, "e", "o");
        Node<String, String> eSuccessor = getSuccessor(tSuccessor, "e");
        verifySuccessors(eSuccessor, "a", "d", "n");
        Node<String, String> aSuccessor = getSuccessor(eSuccessor, "a");
        verifyLeaf(aSuccessor, "tea");
        Node<String, String> dSuccessor = getSuccessor(eSuccessor, "d");
        verifyLeaf(dSuccessor, "ted");
        Node<String, String> nSuccessor = getSuccessor(eSuccessor, "n");
        verifyLeaf(nSuccessor, "ten");
        Node<String, String> oSuccessor = getSuccessor(tSuccessor, "o");
        verifyLeaf(oSuccessor, "to");
        Node<String, String> iSuccessor = getSuccessor(subTrie.getRootNode(), "i");
        verifySuccessors(iSuccessor, "n");
        Node<String, String> n2Successor = getSuccessor(iSuccessor, "n");
        verifySuccessors(n2Successor, "n");
        Node<String, String> n3Successor = getSuccessor(n2Successor, "n");
        verifyLeaf(n3Successor, "inn");
    }

    @Test
    public final void testSubTrieWithNullSequence() {
        testPutWithNullKey();
        StringTrie<String> subTrie = trie.subTrie(null);
        assertFalse(subTrie.isEmpty());
        assertEquals(8, subTrie.size());
        assertNull("A", subTrie.get("A"));
        assertEquals("tea", subTrie.get("B"));
        assertEquals("tea", subTrie.get("tea"));
        assertEquals("ted", subTrie.get("ted"));
        assertEquals("ten", subTrie.get("ten"));
        assertEquals("to", subTrie.get("to"));
        assertEquals("in", subTrie.get("in"));
        assertEquals("inn", subTrie.get("inn"));
        verifyRootNode(subTrie.getRootNode());
        verifySuccessors(subTrie.getRootNode(), "A", "B", "t", "i");
        Node<String, String> ASuccessor = getSuccessor(subTrie.getRootNode(), "A");
        verifyLeaf(ASuccessor, null);
        Node<String, String> BSuccessor = getSuccessor(subTrie.getRootNode(), "B");
        verifyLeaf(BSuccessor, "tea");
        Node<String, String> tSuccessor = getSuccessor(subTrie.getRootNode(), "t");
        verifySuccessors(tSuccessor, "e", "o");
        Node<String, String> eSuccessor = getSuccessor(tSuccessor, "e");
        verifySuccessors(eSuccessor, "a", "d", "n");
        Node<String, String> aSuccessor = getSuccessor(eSuccessor, "a");
        verifyLeaf(aSuccessor, "tea");
        Node<String, String> dSuccessor = getSuccessor(eSuccessor, "d");
        verifyLeaf(dSuccessor, "ted");
        Node<String, String> nSuccessor = getSuccessor(eSuccessor, "n");
        verifyLeaf(nSuccessor, "ten");
        Node<String, String> oSuccessor = getSuccessor(tSuccessor, "o");
        verifyLeaf(oSuccessor, "to");
        Node<String, String> iSuccessor = getSuccessor(subTrie.getRootNode(), "i");
        verifySuccessors(iSuccessor, "n");
        Node<String, String> n2Successor = getSuccessor(iSuccessor, "n");
        verifySuccessors(n2Successor, "n");
        Node<String, String> n3Successor = getSuccessor(n2Successor, "n");
        verifyLeaf(n3Successor, "inn");
    }

    @Test
    public final void testSubTrie1() {
        testPutWithNullKey();
        StringTrie<String> subTrie = trie.subTrie("t");
        assertFalse(subTrie.isEmpty());
        assertEquals(4, subTrie.size());
        verifyRootNode(subTrie.getRootNode());
        verifySuccessors(subTrie.getRootNode(), "t");
        Node<String, String> tSuccessor = getSuccessor(subTrie.getRootNode(), "t");
        verifySuccessors(tSuccessor, "e", "o");
        Node<String, String> eSuccessor = getSuccessor(tSuccessor, "e");
        verifySuccessors(eSuccessor, "a", "d", "n");
        Node<String, String> leaf = getSuccessor(eSuccessor, "a");
        verifyLeaf(leaf, "tea");
        leaf = getSuccessor(eSuccessor, "d");
        verifyLeaf(leaf, "ted");
        leaf = getSuccessor(eSuccessor, "n");
        verifyLeaf(leaf, "ten");
        Node<String, String> oSuccessor = getSuccessor(tSuccessor, "o");
        verifyLeaf(oSuccessor, "to");
    }

    @Test
    public final void testSubTrie2() {
        testPutWithNullKey();
        StringTrie<String> subTrie = trie.subTrie("te");
        assertFalse(subTrie.isEmpty());
        assertEquals(3, subTrie.size());
        verifyRootNode(subTrie.getRootNode());
        verifySuccessors(subTrie.getRootNode(), "t");
        Node<String, String> tSuccessor = getSuccessor(subTrie.getRootNode(), "t");
        verifySuccessors(tSuccessor, "e");
        Node<String, String> eSuccessor = getSuccessor(tSuccessor, "e");
        verifySuccessors(eSuccessor, "a", "d", "n");
        Node<String, String> leaf = getSuccessor(eSuccessor, "a");
        verifyLeaf(leaf, "tea");
        leaf = getSuccessor(eSuccessor, "d");
        verifyLeaf(leaf, "ted");
        leaf = getSuccessor(eSuccessor, "n");
        verifyLeaf(leaf, "ten");
    }

    @Test
    public final void testSubTrieIfSequenceCorrespondsToNode() {
        testPutWithNullKey();
        StringTrie<String> subTrie = trie.subTrie("in");
        assertFalse(subTrie.isEmpty());
        assertEquals(1, subTrie.size());
        assertEquals("inn", subTrie.get("inn"));
        verifyRootNode(subTrie.getRootNode());
        verifySuccessors(subTrie.getRootNode(), "i");
        Node<String, String> iSuccessor = getSuccessor(subTrie.getRootNode(), "i");
        verifySuccessors(iSuccessor, "n");
        Node<String, String> nSuccessor = getSuccessor(iSuccessor, "n");
        verifySuccessors(nSuccessor, "n");
        Node<String, String> n2Successor = getSuccessor(nSuccessor, "n");
        verifyLeaf(n2Successor, "inn");
    }

    @Test
    public final void testSubTrieIsEmpty() {
        testPutWithNullKey();
        StringTrie<String> subTrie = trie.subTrie("tea");
        assertTrue(subTrie.isEmpty());
        assertEquals(0, subTrie.size());
        assertNull(subTrie.getRootNode());
    }

    @Test
    public final void testToString() {
        testPut3();
        assertEquals("HashStringTrie [tea=tea, ted=ted, to=to]", trie.toString());
    }

}
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

import de.mrapp.tries.sequence.StringSequence;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

/**
 * Tests the functionality of the class {@link HashTrie}.
 *
 * @author Michael Rapp
 */
public class HashTrieTest
        extends AbstractStringSequenceNonPatriciaTrieTest<HashTrie<StringSequence, String>> {

    @Override
    final HashTrie<StringSequence, String> onCreateTrie() {
        return new HashTrie<>();
    }

    @Test
    public void testConstructorWithMapParameter() {
        String value1 = "foo";
        String value2 = "bar";
        Map<StringSequence, String> map = new HashMap<>();
        map.put(new StringSequence(value1), value1);
        map.put(new StringSequence(value2), value2);
        Trie<StringSequence, String> trie = new HashTrie<>(map);
        assertEquals(2, trie.size());
        assertEquals(value1, trie.get(new StringSequence(value1)));
        assertEquals(value2, trie.get(new StringSequence(value2)));
    }

    @Test
    public final void testSubTrieWithEmptySequence() {
        testPutWithNullKey();
        Trie<StringSequence, String> subTrie = trie.subTrie(new StringSequence(""));
        assertFalse(subTrie.isEmpty());
        assertEquals(8, subTrie.size());
        assertNull("A", subTrie.get(new StringSequence("A")));
        assertEquals("tea", subTrie.get(new StringSequence("B")));
        assertEquals("tea", subTrie.get(new StringSequence("tea")));
        assertEquals("ted", subTrie.get(new StringSequence("ted")));
        assertEquals("ten", subTrie.get(new StringSequence("ten")));
        assertEquals("to", subTrie.get(new StringSequence("to")));
        assertEquals("in", subTrie.get(new StringSequence("in")));
        assertEquals("inn", subTrie.get(new StringSequence("inn")));
        verifyRootNode(subTrie.getRootNode());
        verifySuccessors(subTrie.getRootNode(), "A", "B", "t", "i");
        Node<StringSequence, String> ASuccessor = getSuccessor(subTrie.getRootNode(), "A");
        verifyLeaf(ASuccessor, null);
        Node<StringSequence, String> BSuccessor = getSuccessor(subTrie.getRootNode(), "B");
        verifyLeaf(BSuccessor, "tea");
        Node<StringSequence, String> tSuccessor = getSuccessor(subTrie.getRootNode(), "t");
        verifySuccessors(tSuccessor, "e", "o");
        Node<StringSequence, String> eSuccessor = getSuccessor(tSuccessor, "e");
        verifySuccessors(eSuccessor, "a", "d", "n");
        Node<StringSequence, String> aSuccessor = getSuccessor(eSuccessor, "a");
        verifyLeaf(aSuccessor, "tea");
        Node<StringSequence, String> dSuccessor = getSuccessor(eSuccessor, "d");
        verifyLeaf(dSuccessor, "ted");
        Node<StringSequence, String> nSuccessor = getSuccessor(eSuccessor, "n");
        verifyLeaf(nSuccessor, "ten");
        Node<StringSequence, String> oSuccessor = getSuccessor(tSuccessor, "o");
        verifyLeaf(oSuccessor, "to");
        Node<StringSequence, String> iSuccessor = getSuccessor(subTrie.getRootNode(), "i");
        verifySuccessors(iSuccessor, "n");
        Node<StringSequence, String> n2Successor = getSuccessor(iSuccessor, "n");
        verifySuccessors(n2Successor, "n");
        Node<StringSequence, String> n3Successor = getSuccessor(n2Successor, "n");
        verifyLeaf(n3Successor, "inn");
    }

    @Test
    public final void testSubTrieWithNullSequence() {
        testPutWithNullKey();
        Trie<StringSequence, String> subTrie = trie.subTrie(null);
        assertFalse(subTrie.isEmpty());
        assertEquals(8, subTrie.size());
        assertNull("A", subTrie.get(new StringSequence("A")));
        assertEquals("tea", subTrie.get(new StringSequence("B")));
        assertEquals("tea", subTrie.get(new StringSequence("tea")));
        assertEquals("ted", subTrie.get(new StringSequence("ted")));
        assertEquals("ten", subTrie.get(new StringSequence("ten")));
        assertEquals("to", subTrie.get(new StringSequence("to")));
        assertEquals("in", subTrie.get(new StringSequence("in")));
        assertEquals("inn", subTrie.get(new StringSequence("inn")));
        verifyRootNode(subTrie.getRootNode());
        verifySuccessors(subTrie.getRootNode(), "A", "B", "t", "i");
        Node<StringSequence, String> ASuccessor = getSuccessor(subTrie.getRootNode(), "A");
        verifyLeaf(ASuccessor, null);
        Node<StringSequence, String> BSuccessor = getSuccessor(subTrie.getRootNode(), "B");
        verifyLeaf(BSuccessor, "tea");
        Node<StringSequence, String> tSuccessor = getSuccessor(subTrie.getRootNode(), "t");
        verifySuccessors(tSuccessor, "e", "o");
        Node<StringSequence, String> eSuccessor = getSuccessor(tSuccessor, "e");
        verifySuccessors(eSuccessor, "a", "d", "n");
        Node<StringSequence, String> aSuccessor = getSuccessor(eSuccessor, "a");
        verifyLeaf(aSuccessor, "tea");
        Node<StringSequence, String> dSuccessor = getSuccessor(eSuccessor, "d");
        verifyLeaf(dSuccessor, "ted");
        Node<StringSequence, String> nSuccessor = getSuccessor(eSuccessor, "n");
        verifyLeaf(nSuccessor, "ten");
        Node<StringSequence, String> oSuccessor = getSuccessor(tSuccessor, "o");
        verifyLeaf(oSuccessor, "to");
        Node<StringSequence, String> iSuccessor = getSuccessor(subTrie.getRootNode(), "i");
        verifySuccessors(iSuccessor, "n");
        Node<StringSequence, String> n2Successor = getSuccessor(iSuccessor, "n");
        verifySuccessors(n2Successor, "n");
        Node<StringSequence, String> n3Successor = getSuccessor(n2Successor, "n");
        verifyLeaf(n3Successor, "inn");
    }

    @Test
    public final void testSubTrie1() {
        testPutWithNullKey();
        Trie<StringSequence, String> subTrie = trie.subTrie(new StringSequence("t"));
        assertFalse(subTrie.isEmpty());
        assertEquals(4, subTrie.size());
        verifyRootNode(subTrie.getRootNode());
        verifySuccessors(subTrie.getRootNode(), "t");
        Node<StringSequence, String> tSuccessor = getSuccessor(subTrie.getRootNode(), "t");
        verifySuccessors(tSuccessor, "e", "o");
        Node<StringSequence, String> eSuccessor = getSuccessor(tSuccessor, "e");
        verifySuccessors(eSuccessor, "a", "d", "n");
        Node<StringSequence, String> leaf = getSuccessor(eSuccessor, "a");
        verifyLeaf(leaf, "tea");
        leaf = getSuccessor(eSuccessor, "d");
        verifyLeaf(leaf, "ted");
        leaf = getSuccessor(eSuccessor, "n");
        verifyLeaf(leaf, "ten");
        Node<StringSequence, String> oSuccessor = getSuccessor(tSuccessor, "o");
        verifyLeaf(oSuccessor, "to");
    }

    @Test
    public final void testSubTrie2() {
        testPutWithNullKey();
        Trie<StringSequence, String> subTrie = trie.subTrie(new StringSequence("te"));
        assertFalse(subTrie.isEmpty());
        assertEquals(3, subTrie.size());
        verifyRootNode(subTrie.getRootNode());
        verifySuccessors(subTrie.getRootNode(), "t");
        Node<StringSequence, String> tSuccessor = getSuccessor(subTrie.getRootNode(), "t");
        verifySuccessors(tSuccessor, "e");
        Node<StringSequence, String> eSuccessor = getSuccessor(tSuccessor, "e");
        verifySuccessors(eSuccessor, "a", "d", "n");
        Node<StringSequence, String> leaf = getSuccessor(eSuccessor, "a");
        verifyLeaf(leaf, "tea");
        leaf = getSuccessor(eSuccessor, "d");
        verifyLeaf(leaf, "ted");
        leaf = getSuccessor(eSuccessor, "n");
        verifyLeaf(leaf, "ten");
    }

    @Test
    public final void testSubTrieIfSequenceCorrespondsToNode() {
        testPutWithNullKey();
        Trie<StringSequence, String> subTrie = trie.subTrie(new StringSequence("in"));
        assertFalse(subTrie.isEmpty());
        assertEquals(1, subTrie.size());
        assertEquals("inn", subTrie.get(new StringSequence("inn")));
        verifyRootNode(subTrie.getRootNode());
        verifySuccessors(subTrie.getRootNode(), "i");
        Node<StringSequence, String> iSuccessor = getSuccessor(subTrie.getRootNode(), "i");
        verifySuccessors(iSuccessor, "n");
        Node<StringSequence, String> nSuccessor = getSuccessor(iSuccessor, "n");
        verifySuccessors(nSuccessor, "n");
        Node<StringSequence, String> n2Successor = getSuccessor(nSuccessor, "n");
        verifyLeaf(n2Successor, "inn");
    }

    @Test
    public void testToString() {
        testPut3();
        assertEquals("HashTrie [tea=tea, ted=ted, to=to]", trie.toString());
    }

}
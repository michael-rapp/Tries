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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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
    public final void testToString() {
        testPut3();
        assertEquals("HashStringTrie [tea=tea, ted=ted, to=to]", trie.toString());
    }

}
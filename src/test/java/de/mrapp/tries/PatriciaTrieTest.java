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
import org.jetbrains.annotations.NotNull;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the functionality of the class {@link PatriciaTrie}.
 *
 * @author Michael Rapp
 */
public class PatriciaTrieTest
        extends AbstractSortedTrieTest<StringSequence, String, PatriciaTrie<StringSequence, String>> {

    @Override
    final PatriciaTrie<StringSequence, String> onCreateTrie() {
        return new PatriciaTrie<>();
    }

    @Override
    final StringSequence convertToSequence(@NotNull String string) {
        return new StringSequence(string);
    }

    @Override
    final Node<StringSequence, String> getRootNode(@NotNull final PatriciaTrie<StringSequence, String> trie) {
        return trie.getRootNode();
    }

    /**
     * Adds "romane" to the trie.
     */
    @Test
    public final void testPut1() {
        String string = "romane";
        assertTrue(trie.isEmpty());
        String previous = trie.put(convertToSequence(string), string);
        assertNull(previous);
        assertEquals(string, trie.get(convertToSequence(string)));
        assertFalse(trie.isEmpty());
        assertEquals(1, trie.size());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "romane");
        Node<StringSequence, String> successor = getSuccessor(getRootNode(trie), "romane");
        verifyLeaf(successor, string);
    }

    /**
     * Adds "romane" and "romanus" to the trie.
     */
    @Test
    public final void testPut2() {
        testPut1();
        String string = "romanus";
        String previous = trie.put(convertToSequence(string), string);
        assertNull(previous);
        assertEquals(string, trie.get(convertToSequence(string)));
        assertEquals(2, trie.size());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "roman");
        Node<StringSequence, String> successor = getSuccessor(getRootNode(trie), "roman");
        verifySuccessors(successor, "e", "us");
        Node<StringSequence, String> successorE = getSuccessor(successor, "e");
        verifyLeaf(successorE, "romane");
        Node<StringSequence, String> successorUs = getSuccessor(successor, "us");
        verifyLeaf(successorUs, string);
    }

    /**
     * Adds "romane", "romanus" and "romulus" to the trie.
     */
    @Test
    public final void testPut3() {
        testPut2();
        String string = "romulus";
        String previous = trie.put(convertToSequence(string), string);
        assertNull(previous);
        assertEquals(string, trie.get(convertToSequence(string)));
        assertEquals(3, trie.size());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "rom");
        Node<StringSequence, String> successor = getSuccessor(getRootNode(trie), "rom");
        verifySuccessors(successor, "an", "ulus");
        Node<StringSequence, String> successorAn = getSuccessor(successor, "an");
        verifySuccessors(successorAn, "e", "us");
        Node<StringSequence, String> successorE = getSuccessor(successorAn, "e");
        verifyLeaf(successorE, "romane");
        Node<StringSequence, String> successorUs = getSuccessor(successorAn, "us");
        verifyLeaf(successorUs, "romanus");
        Node<StringSequence, String> successorUlus = getSuccessor(successor, "ulus");
        verifyLeaf(successorUlus, string);
    }

    /**
     * Adds "romane", "romanus", "romulus" and "rubens" to the trie.
     */
    @Test
    public final void testPut4() {
        testPut3();
        String string = "rubens";
        String previous = trie.put(convertToSequence(string), string);
        assertNull(previous);
        assertEquals(string, trie.get(convertToSequence(string)));
        assertEquals(4, trie.size());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "r");
        Node<StringSequence, String> successor = getSuccessor(getRootNode(trie), "r");
        verifySuccessors(successor, "om", "ubens");
        Node<StringSequence, String> successorOm = getSuccessor(successor, "om");
        verifySuccessors(successorOm, "an", "ulus");
        Node<StringSequence, String> successorAn = getSuccessor(successorOm, "an");
        verifySuccessors(successorAn, "e", "us");
        Node<StringSequence, String> successorE = getSuccessor(successorAn, "e");
        verifyLeaf(successorE, "romane");
        Node<StringSequence, String> successorUs = getSuccessor(successorAn, "us");
        verifyLeaf(successorUs, "romanus");
        Node<StringSequence, String> successorUlus = getSuccessor(successorOm, "ulus");
        verifyLeaf(successorUlus, "romulus");
        Node<StringSequence, String> successorUbens = getSuccessor(successor, "ubens");
        verifyLeaf(successorUbens, string);
    }

    /**
     * Adds "romane", "romanus", romulus", "rubens" and "ruber" to the trie.
     */
    @Test
    public final void testPut5() {
        testPut4();
        String string = "ruber";
        String previous = trie.put(convertToSequence(string), string);
        assertNull(previous);
        assertEquals(string, trie.get(convertToSequence(string)));
        assertEquals(5, trie.size());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "r");
        Node<StringSequence, String> successor = getSuccessor(getRootNode(trie), "r");
        verifySuccessors(successor, "om", "ube");
        Node<StringSequence, String> successorOm = getSuccessor(successor, "om");
        verifySuccessors(successorOm, "an", "ulus");
        Node<StringSequence, String> successorAn = getSuccessor(successorOm, "an");
        verifySuccessors(successorAn, "e", "us");
        Node<StringSequence, String> successorE = getSuccessor(successorAn, "e");
        verifyLeaf(successorE, "romane");
        Node<StringSequence, String> successorUs = getSuccessor(successorAn, "us");
        verifyLeaf(successorUs, "romanus");
        Node<StringSequence, String> successorUlus = getSuccessor(successorOm, "ulus");
        verifyLeaf(successorUlus, "romulus");
        Node<StringSequence, String> successorUbe = getSuccessor(successor, "ube");
        verifySuccessors(successorUbe, "ns", "r");
        Node<StringSequence, String> successorNs = getSuccessor(successorUbe, "ns");
        verifyLeaf(successorNs, "rubens");
        Node<StringSequence, String> successorR = getSuccessor(successorUbe, "r");
        verifyLeaf(successorR, "ruber");
    }

}
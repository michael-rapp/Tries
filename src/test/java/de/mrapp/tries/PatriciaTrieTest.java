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

}
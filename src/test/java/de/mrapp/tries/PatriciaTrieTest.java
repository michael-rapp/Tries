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

    /**
     * Adds "romane", "romanus", romulus", "rubens", "ruber" and "rubicon" to the trie.
     */
    @Test
    public final void testPut6() {
        testPut5();
        String string = "rubicon";
        String previous = trie.put(convertToSequence(string), string);
        assertNull(previous);
        assertEquals(string, trie.get(convertToSequence(string)));
        assertEquals(6, trie.size());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "r");
        Node<StringSequence, String> successor = getSuccessor(getRootNode(trie), "r");
        verifySuccessors(successor, "om", "ub");
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
        Node<StringSequence, String> successorUb = getSuccessor(successor, "ub");
        verifySuccessors(successorUb, "e", "icon");
        Node<StringSequence, String> successorE2 = getSuccessor(successorUb, "e");
        verifySuccessors(successorE2, "ns", "r");
        Node<StringSequence, String> successorNs = getSuccessor(successorE2, "ns");
        verifyLeaf(successorNs, "rubens");
        Node<StringSequence, String> successorR = getSuccessor(successorE2, "r");
        verifyLeaf(successorR, "ruber");
        Node<StringSequence, String> successorIcon = getSuccessor(successorUb, "icon");
        verifyLeaf(successorIcon, string);
    }

    /**
     * Adds "romane", "romanus", romulus", "rubens", "ruber", "rubicon" and "rubicundus" to the trie.
     */
    @Test
    public final void testPut7() {
        testPut6();
        String string = "rubicundus";
        String previous = trie.put(convertToSequence(string), string);
        assertNull(previous);
        assertEquals(string, trie.get(convertToSequence(string)));
        assertEquals(7, trie.size());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "r");
        Node<StringSequence, String> successor = getSuccessor(getRootNode(trie), "r");
        verifySuccessors(successor, "om", "ub");
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
        Node<StringSequence, String> successorUb = getSuccessor(successor, "ub");
        verifySuccessors(successorUb, "e", "ic");
        Node<StringSequence, String> successorE2 = getSuccessor(successorUb, "e");
        verifySuccessors(successorE2, "ns", "r");
        Node<StringSequence, String> successorNs = getSuccessor(successorE2, "ns");
        verifyLeaf(successorNs, "rubens");
        Node<StringSequence, String> successorR = getSuccessor(successorE2, "r");
        verifyLeaf(successorR, "ruber");
        Node<StringSequence, String> successorIc = getSuccessor(successorUb, "ic");
        verifySuccessors(successorIc, "on", "undus");
        Node<StringSequence, String> successorOn = getSuccessor(successorIc, "on");
        verifyLeaf(successorOn, "rubicon");
        Node<StringSequence, String> successorUndus = getSuccessor(successorIc, "undus");
        verifyLeaf(successorUndus, string);
    }

    /**
     * Adds "romane", "romanus", romulus", "rubens", "ruber", "rubicon", "rubicundus" and "A" (mapped to null value) to
     * the trie.
     */
    @Test
    public final void testPut8() {
        testPut7();
        String string = "A";
        String previous = trie.put(convertToSequence(string), null);
        assertNull(previous);
        assertNull(string, trie.get(convertToSequence(string)));
        assertEquals(8, trie.size());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "r", "A");
        Node<StringSequence, String> successor = getSuccessor(getRootNode(trie), "r");
        verifySuccessors(successor, "om", "ub");
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
        Node<StringSequence, String> successorUb = getSuccessor(successor, "ub");
        verifySuccessors(successorUb, "e", "ic");
        Node<StringSequence, String> successorE2 = getSuccessor(successorUb, "e");
        verifySuccessors(successorE2, "ns", "r");
        Node<StringSequence, String> successorNs = getSuccessor(successorE2, "ns");
        verifyLeaf(successorNs, "rubens");
        Node<StringSequence, String> successorR = getSuccessor(successorE2, "r");
        verifyLeaf(successorR, "ruber");
        Node<StringSequence, String> successorIc = getSuccessor(successorUb, "ic");
        verifySuccessors(successorIc, "on", "undus");
        Node<StringSequence, String> successorOn = getSuccessor(successorIc, "on");
        verifyLeaf(successorOn, "rubicon");
        Node<StringSequence, String> successorUndus = getSuccessor(successorIc, "undus");
        verifyLeaf(successorUndus, "rubicundus");
        Node<StringSequence, String> successorA = getSuccessor(getRootNode(trie), "A");
        verifyLeaf(successorA, null);
    }

    @Test
    public final void testPutWithDuplicateValue() {
        testPut8();
        String string = "B";
        String duplicate = "romulus";
        String previous = trie.put(convertToSequence(string), duplicate);
        assertNull(previous);
        assertEquals(duplicate, trie.get(convertToSequence(string)));
        assertEquals(9, trie.size());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "r", "A", "B");
        Node<StringSequence, String> successor = getSuccessor(getRootNode(trie), "r");
        verifySuccessors(successor, "om", "ub");
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
        Node<StringSequence, String> successorUb = getSuccessor(successor, "ub");
        verifySuccessors(successorUb, "e", "ic");
        Node<StringSequence, String> successorE2 = getSuccessor(successorUb, "e");
        verifySuccessors(successorE2, "ns", "r");
        Node<StringSequence, String> successorNs = getSuccessor(successorE2, "ns");
        verifyLeaf(successorNs, "rubens");
        Node<StringSequence, String> successorR = getSuccessor(successorE2, "r");
        verifyLeaf(successorR, "ruber");
        Node<StringSequence, String> successorIc = getSuccessor(successorUb, "ic");
        verifySuccessors(successorIc, "on", "undus");
        Node<StringSequence, String> successorOn = getSuccessor(successorIc, "on");
        verifyLeaf(successorOn, "rubicon");
        Node<StringSequence, String> successorUndus = getSuccessor(successorIc, "undus");
        verifyLeaf(successorUndus, "rubicundus");
        Node<StringSequence, String> successorA = getSuccessor(getRootNode(trie), "A");
        verifyLeaf(successorA, null);
        Node<StringSequence, String> successorB = getSuccessor(getRootNode(trie), "B");
        verifyLeaf(successorB, duplicate);
    }

    @Test
    public final void testPutWithEmptyKey() {
        testPutWithDuplicateValue();
        String string = "empty";
        String previous = trie.put(convertToSequence(""), string);
        assertNull(previous);
        assertEquals(string, trie.get(convertToSequence("")));
        assertEquals(10, trie.size());
        verifyRootNode(getRootNode(trie), string);
        verifySuccessors(getRootNode(trie), "r", "A", "B");
        Node<StringSequence, String> successor = getSuccessor(getRootNode(trie), "r");
        verifySuccessors(successor, "om", "ub");
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
        Node<StringSequence, String> successorUb = getSuccessor(successor, "ub");
        verifySuccessors(successorUb, "e", "ic");
        Node<StringSequence, String> successorE2 = getSuccessor(successorUb, "e");
        verifySuccessors(successorE2, "ns", "r");
        Node<StringSequence, String> successorNs = getSuccessor(successorE2, "ns");
        verifyLeaf(successorNs, "rubens");
        Node<StringSequence, String> successorR = getSuccessor(successorE2, "r");
        verifyLeaf(successorR, "ruber");
        Node<StringSequence, String> successorIc = getSuccessor(successorUb, "ic");
        verifySuccessors(successorIc, "on", "undus");
        Node<StringSequence, String> successorOn = getSuccessor(successorIc, "on");
        verifyLeaf(successorOn, "rubicon");
        Node<StringSequence, String> successorUndus = getSuccessor(successorIc, "undus");
        verifyLeaf(successorUndus, "rubicundus");
        Node<StringSequence, String> successorA = getSuccessor(getRootNode(trie), "A");
        verifyLeaf(successorA, null);
        Node<StringSequence, String> successorB = getSuccessor(getRootNode(trie), "B");
        verifyLeaf(successorB, "romulus");
    }

    @Test
    public final void testPutWithNullKey() {
        testPutWithEmptyKey();
        String string = "null";
        String previous = trie.put(null, string);
        assertEquals("empty", previous);
        assertEquals(string, trie.get(null));
        assertEquals(10, trie.size());
        verifyRootNode(getRootNode(trie), string);
        verifySuccessors(getRootNode(trie), "r", "A", "B");
        Node<StringSequence, String> successor = getSuccessor(getRootNode(trie), "r");
        verifySuccessors(successor, "om", "ub");
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
        Node<StringSequence, String> successorUb = getSuccessor(successor, "ub");
        verifySuccessors(successorUb, "e", "ic");
        Node<StringSequence, String> successorE2 = getSuccessor(successorUb, "e");
        verifySuccessors(successorE2, "ns", "r");
        Node<StringSequence, String> successorNs = getSuccessor(successorE2, "ns");
        verifyLeaf(successorNs, "rubens");
        Node<StringSequence, String> successorR = getSuccessor(successorE2, "r");
        verifyLeaf(successorR, "ruber");
        Node<StringSequence, String> successorIc = getSuccessor(successorUb, "ic");
        verifySuccessors(successorIc, "on", "undus");
        Node<StringSequence, String> successorOn = getSuccessor(successorIc, "on");
        verifyLeaf(successorOn, "rubicon");
        Node<StringSequence, String> successorUndus = getSuccessor(successorIc, "undus");
        verifyLeaf(successorUndus, "rubicundus");
        Node<StringSequence, String> successorA = getSuccessor(getRootNode(trie), "A");
        verifyLeaf(successorA, null);
        Node<StringSequence, String> successorB = getSuccessor(getRootNode(trie), "B");
        verifyLeaf(successorB, "romulus");
    }

    @Test
    public final void testPutReplacesPreviousValue() {
        testPut1();
        String string = "romane";
        String string2 = "romane2";
        assertEquals(1, trie.size());
        assertEquals(string, trie.get(convertToSequence(string)));
        String previous = trie.put(convertToSequence(string), string2);
        assertEquals(string, previous);
        assertEquals(string2, trie.get(convertToSequence(string)));
        assertFalse(trie.isEmpty());
        assertEquals(1, trie.size());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "romane");
        Node<StringSequence, String> successor = getSuccessor(getRootNode(trie), "romane");
        verifyLeaf(successor, string2);
    }

}
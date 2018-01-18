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

import java.util.*;

import static org.junit.Assert.*;

/**
 * Tests the functionality of the class {@link PatriciaTrie}.
 *
 * @author Michael Rapp
 */
public abstract class AbstractPatriciaTrieTest<SequenceType, TrieType extends NavigableMap<SequenceType, String>> extends
        AbstractSortedTrieTest<SequenceType, TrieType> {

    @Test
    public final void testPollFirstEntryIfTrieIsEmpty() {
        Map.Entry<SequenceType, String> removed = trie.pollFirstEntry();
        assertNull(removed);
        assertTrue(trie.isEmpty());
        assertNull(getRootNode(trie));
    }

    @Test
    public final void testPollLastEntryIfTrieIsEmpty() {
        Map.Entry<SequenceType, String> removed = trie.pollLastEntry();
        assertNull(removed);
        assertTrue(trie.isEmpty());
        assertNull(getRootNode(trie));
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
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "romane");
        verifyLeaf(successor, string);
    }

    @Test
    public final void testGetIfKeyIsNotContainedAndIsPrefix() {
        String string = "romane";
        trie.put(convertToSequence(string), string);
        assertNull(trie.get(convertToSequence("rom")));
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
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "roman");
        verifySuccessors(successor, "e", "us");
        Node<SequenceType, String> successorE = getSuccessor(successor, "e");
        verifyLeaf(successorE, "romane");
        Node<SequenceType, String> successorUs = getSuccessor(successor, "us");
        verifyLeaf(successorUs, string);
    }

    /**
     * Adds "romane", "romanus" and "rom" to the trie.
     */
    @Test
    public final void testPut3() {
        testPut2();
        String string = "rom";
        String previous = trie.put(convertToSequence(string), string);
        assertNull(previous);
        assertEquals(string, trie.get(convertToSequence(string)));
        assertEquals(3, trie.size());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "rom");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "rom");
        verifySuccessors(successor, "an");
        Node<SequenceType, String> successorAn = getSuccessor(successor, "an");
        verifySuccessors(successorAn, "e", "us");
        Node<SequenceType, String> successorE = getSuccessor(successorAn, "e");
        verifyLeaf(successorE, "romane");
        Node<SequenceType, String> successorUs = getSuccessor(successorAn, "us");
        verifyLeaf(successorUs, "romanus");
    }

    /**
     * Adds "romane", "romanus", "rom" and "romulus" to the trie.
     */
    @Test
    public final void testPut4() {
        testPut3();
        String string = "romulus";
        String previous = trie.put(convertToSequence(string), string);
        assertNull(previous);
        assertEquals(string, trie.get(convertToSequence(string)));
        assertEquals(4, trie.size());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "rom");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "rom");
        verifySuccessors(successor, "an", "ulus");
        Node<SequenceType, String> successorAn = getSuccessor(successor, "an");
        verifySuccessors(successorAn, "e", "us");
        Node<SequenceType, String> successorE = getSuccessor(successorAn, "e");
        verifyLeaf(successorE, "romane");
        Node<SequenceType, String> successorUs = getSuccessor(successorAn, "us");
        verifyLeaf(successorUs, "romanus");
        Node<SequenceType, String> successorUlus = getSuccessor(successor, "ulus");
        verifyLeaf(successorUlus, string);
    }

    /**
     * Adds "romane", "romanus", "rom", "romulus" and "rubens" to the trie.
     */
    @Test
    public final void testPut5() {
        testPut4();
        String string = "rubens";
        String previous = trie.put(convertToSequence(string), string);
        assertNull(previous);
        assertEquals(string, trie.get(convertToSequence(string)));
        assertEquals(5, trie.size());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "r");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "r");
        verifySuccessors(successor, "om", "ubens");
        Node<SequenceType, String> successorOm = getSuccessor(successor, "om");
        verifySuccessors(successorOm, "an", "ulus");
        Node<SequenceType, String> successorAn = getSuccessor(successorOm, "an");
        verifySuccessors(successorAn, "e", "us");
        Node<SequenceType, String> successorE = getSuccessor(successorAn, "e");
        verifyLeaf(successorE, "romane");
        Node<SequenceType, String> successorUs = getSuccessor(successorAn, "us");
        verifyLeaf(successorUs, "romanus");
        Node<SequenceType, String> successorUlus = getSuccessor(successorOm, "ulus");
        verifyLeaf(successorUlus, "romulus");
        Node<SequenceType, String> successorUbens = getSuccessor(successor, "ubens");
        verifyLeaf(successorUbens, string);
    }

    /**
     * Adds "romane", "romanus", "rom", "romulus", "rubens" and "ruber" to the trie.
     */
    @Test
    public final void testPut6() {
        testPut5();
        String string = "ruber";
        String previous = trie.put(convertToSequence(string), string);
        assertNull(previous);
        assertEquals(string, trie.get(convertToSequence(string)));
        assertEquals(6, trie.size());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "r");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "r");
        verifySuccessors(successor, "om", "ube");
        Node<SequenceType, String> successorOm = getSuccessor(successor, "om");
        verifySuccessors(successorOm, "an", "ulus");
        Node<SequenceType, String> successorAn = getSuccessor(successorOm, "an");
        verifySuccessors(successorAn, "e", "us");
        Node<SequenceType, String> successorE = getSuccessor(successorAn, "e");
        verifyLeaf(successorE, "romane");
        Node<SequenceType, String> successorUs = getSuccessor(successorAn, "us");
        verifyLeaf(successorUs, "romanus");
        Node<SequenceType, String> successorUlus = getSuccessor(successorOm, "ulus");
        verifyLeaf(successorUlus, "romulus");
        Node<SequenceType, String> successorUbe = getSuccessor(successor, "ube");
        verifySuccessors(successorUbe, "ns", "r");
        Node<SequenceType, String> successorNs = getSuccessor(successorUbe, "ns");
        verifyLeaf(successorNs, "rubens");
        Node<SequenceType, String> successorR = getSuccessor(successorUbe, "r");
        verifyLeaf(successorR, "ruber");
    }

    /**
     * Adds "romane", "romanus", "rom", "romulus", "rubens", "ruber" and "rubicon" to the trie.
     */
    @Test
    public final void testPut7() {
        testPut6();
        String string = "rubicon";
        String previous = trie.put(convertToSequence(string), string);
        assertNull(previous);
        assertEquals(string, trie.get(convertToSequence(string)));
        assertEquals(7, trie.size());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "r");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "r");
        verifySuccessors(successor, "om", "ub");
        Node<SequenceType, String> successorOm = getSuccessor(successor, "om");
        verifySuccessors(successorOm, "an", "ulus");
        Node<SequenceType, String> successorAn = getSuccessor(successorOm, "an");
        verifySuccessors(successorAn, "e", "us");
        Node<SequenceType, String> successorE = getSuccessor(successorAn, "e");
        verifyLeaf(successorE, "romane");
        Node<SequenceType, String> successorUs = getSuccessor(successorAn, "us");
        verifyLeaf(successorUs, "romanus");
        Node<SequenceType, String> successorUlus = getSuccessor(successorOm, "ulus");
        verifyLeaf(successorUlus, "romulus");
        Node<SequenceType, String> successorUb = getSuccessor(successor, "ub");
        verifySuccessors(successorUb, "e", "icon");
        Node<SequenceType, String> successorE2 = getSuccessor(successorUb, "e");
        verifySuccessors(successorE2, "ns", "r");
        Node<SequenceType, String> successorNs = getSuccessor(successorE2, "ns");
        verifyLeaf(successorNs, "rubens");
        Node<SequenceType, String> successorR = getSuccessor(successorE2, "r");
        verifyLeaf(successorR, "ruber");
        Node<SequenceType, String> successorIcon = getSuccessor(successorUb, "icon");
        verifyLeaf(successorIcon, string);
    }

    /**
     * Adds "romane", "romanus", "rom", "romulus", "rubens", "ruber", "rubicon" and "rubicundus" to
     * the trie.
     */
    @Test
    public final void testPut8() {
        testPut7();
        String string = "rubicundus";
        String previous = trie.put(convertToSequence(string), string);
        assertNull(previous);
        assertEquals(string, trie.get(convertToSequence(string)));
        assertEquals(8, trie.size());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "r");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "r");
        verifySuccessors(successor, "om", "ub");
        Node<SequenceType, String> successorOm = getSuccessor(successor, "om");
        verifySuccessors(successorOm, "an", "ulus");
        Node<SequenceType, String> successorAn = getSuccessor(successorOm, "an");
        verifySuccessors(successorAn, "e", "us");
        Node<SequenceType, String> successorE = getSuccessor(successorAn, "e");
        verifyLeaf(successorE, "romane");
        Node<SequenceType, String> successorUs = getSuccessor(successorAn, "us");
        verifyLeaf(successorUs, "romanus");
        Node<SequenceType, String> successorUlus = getSuccessor(successorOm, "ulus");
        verifyLeaf(successorUlus, "romulus");
        Node<SequenceType, String> successorUb = getSuccessor(successor, "ub");
        verifySuccessors(successorUb, "e", "ic");
        Node<SequenceType, String> successorE2 = getSuccessor(successorUb, "e");
        verifySuccessors(successorE2, "ns", "r");
        Node<SequenceType, String> successorNs = getSuccessor(successorE2, "ns");
        verifyLeaf(successorNs, "rubens");
        Node<SequenceType, String> successorR = getSuccessor(successorE2, "r");
        verifyLeaf(successorR, "ruber");
        Node<SequenceType, String> successorIc = getSuccessor(successorUb, "ic");
        verifySuccessors(successorIc, "on", "undus");
        Node<SequenceType, String> successorOn = getSuccessor(successorIc, "on");
        verifyLeaf(successorOn, "rubicon");
        Node<SequenceType, String> successorUndus = getSuccessor(successorIc, "undus");
        verifyLeaf(successorUndus, string);
    }

    /**
     * Adds "romane", "romanus", "rom", "romulus", "rubens", "ruber", "rubicon", "rubicundus",
     * "rubicun" to the trie.
     */
    @Test
    public final void testPut9() {
        testPut8();
        String string = "rubicun";
        String previous = trie.put(convertToSequence(string), string);
        assertNull(previous);
        assertEquals(string, trie.get(convertToSequence(string)));
        assertEquals(9, trie.size());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "r");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "r");
        verifySuccessors(successor, "om", "ub");
        Node<SequenceType, String> successorOm = getSuccessor(successor, "om");
        verifySuccessors(successorOm, "an", "ulus");
        Node<SequenceType, String> successorAn = getSuccessor(successorOm, "an");
        verifySuccessors(successorAn, "e", "us");
        Node<SequenceType, String> successorE = getSuccessor(successorAn, "e");
        verifyLeaf(successorE, "romane");
        Node<SequenceType, String> successorUs = getSuccessor(successorAn, "us");
        verifyLeaf(successorUs, "romanus");
        Node<SequenceType, String> successorUlus = getSuccessor(successorOm, "ulus");
        verifyLeaf(successorUlus, "romulus");
        Node<SequenceType, String> successorUb = getSuccessor(successor, "ub");
        verifySuccessors(successorUb, "e", "ic");
        Node<SequenceType, String> successorE2 = getSuccessor(successorUb, "e");
        verifySuccessors(successorE2, "ns", "r");
        Node<SequenceType, String> successorNs = getSuccessor(successorE2, "ns");
        verifyLeaf(successorNs, "rubens");
        Node<SequenceType, String> successorR = getSuccessor(successorE2, "r");
        verifyLeaf(successorR, "ruber");
        Node<SequenceType, String> successorIc = getSuccessor(successorUb, "ic");
        verifySuccessors(successorIc, "on", "un");
        Node<SequenceType, String> successorOn = getSuccessor(successorIc, "on");
        verifyLeaf(successorOn, "rubicon");
        Node<SequenceType, String> successorUn = getSuccessor(successorIc, "un");
        verifySuccessors(successorUn, "dus");
        Node<SequenceType, String> successorDus = getSuccessor(successorUn, "dus");
        verifyLeaf(successorDus, "rubicundus");
    }

    /**
     * Adds "romane", "romanus", "rom", "romulus", "rubens", "ruber", "rubicon", "rubicundus" and
     * "A" (mapped to null value) to the trie.
     */
    @Test
    public final void testPut10() {
        testPut9();
        String string = "A";
        String previous = trie.put(convertToSequence(string), null);
        assertNull(previous);
        assertNull(string, trie.get(convertToSequence(string)));
        assertEquals(10, trie.size());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "r", "A");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "r");
        verifySuccessors(successor, "om", "ub");
        Node<SequenceType, String> successorOm = getSuccessor(successor, "om");
        verifySuccessors(successorOm, "an", "ulus");
        Node<SequenceType, String> successorAn = getSuccessor(successorOm, "an");
        verifySuccessors(successorAn, "e", "us");
        Node<SequenceType, String> successorE = getSuccessor(successorAn, "e");
        verifyLeaf(successorE, "romane");
        Node<SequenceType, String> successorUs = getSuccessor(successorAn, "us");
        verifyLeaf(successorUs, "romanus");
        Node<SequenceType, String> successorUlus = getSuccessor(successorOm, "ulus");
        verifyLeaf(successorUlus, "romulus");
        Node<SequenceType, String> successorUb = getSuccessor(successor, "ub");
        verifySuccessors(successorUb, "e", "ic");
        Node<SequenceType, String> successorE2 = getSuccessor(successorUb, "e");
        verifySuccessors(successorE2, "ns", "r");
        Node<SequenceType, String> successorNs = getSuccessor(successorE2, "ns");
        verifyLeaf(successorNs, "rubens");
        Node<SequenceType, String> successorR = getSuccessor(successorE2, "r");
        verifyLeaf(successorR, "ruber");
        Node<SequenceType, String> successorIc = getSuccessor(successorUb, "ic");
        verifySuccessors(successorIc, "on", "un");
        Node<SequenceType, String> successorOn = getSuccessor(successorIc, "on");
        verifyLeaf(successorOn, "rubicon");
        Node<SequenceType, String> successorUn = getSuccessor(successorIc, "un");
        verifySuccessors(successorUn, "dus");
        Node<SequenceType, String> successorDus = getSuccessor(successorUn, "dus");
        verifyLeaf(successorDus, "rubicundus");
        Node<SequenceType, String> successorA = getSuccessor(getRootNode(trie), "A");
        verifyLeaf(successorA, null);
    }

    @Test
    public final void testPutWithDuplicateValue() {
        testPut10();
        String string = "B";
        String duplicate = "romulus";
        String previous = trie.put(convertToSequence(string), duplicate);
        assertNull(previous);
        assertEquals(duplicate, trie.get(convertToSequence(string)));
        assertEquals(11, trie.size());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "r", "A", "B");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "r");
        verifySuccessors(successor, "om", "ub");
        Node<SequenceType, String> successorOm = getSuccessor(successor, "om");
        verifySuccessors(successorOm, "an", "ulus");
        Node<SequenceType, String> successorAn = getSuccessor(successorOm, "an");
        verifySuccessors(successorAn, "e", "us");
        Node<SequenceType, String> successorE = getSuccessor(successorAn, "e");
        verifyLeaf(successorE, "romane");
        Node<SequenceType, String> successorUs = getSuccessor(successorAn, "us");
        verifyLeaf(successorUs, "romanus");
        Node<SequenceType, String> successorUlus = getSuccessor(successorOm, "ulus");
        verifyLeaf(successorUlus, "romulus");
        Node<SequenceType, String> successorUb = getSuccessor(successor, "ub");
        verifySuccessors(successorUb, "e", "ic");
        Node<SequenceType, String> successorE2 = getSuccessor(successorUb, "e");
        verifySuccessors(successorE2, "ns", "r");
        Node<SequenceType, String> successorNs = getSuccessor(successorE2, "ns");
        verifyLeaf(successorNs, "rubens");
        Node<SequenceType, String> successorR = getSuccessor(successorE2, "r");
        verifyLeaf(successorR, "ruber");
        Node<SequenceType, String> successorIc = getSuccessor(successorUb, "ic");
        verifySuccessors(successorIc, "on", "un");
        Node<SequenceType, String> successorOn = getSuccessor(successorIc, "on");
        verifyLeaf(successorOn, "rubicon");
        Node<SequenceType, String> successorUn = getSuccessor(successorIc, "un");
        verifySuccessors(successorUn, "dus");
        Node<SequenceType, String> successorDus = getSuccessor(successorUn, "dus");
        verifyLeaf(successorDus, "rubicundus");
        Node<SequenceType, String> successorA = getSuccessor(getRootNode(trie), "A");
        verifyLeaf(successorA, null);
        Node<SequenceType, String> successorB = getSuccessor(getRootNode(trie), "B");
        verifyLeaf(successorB, duplicate);
    }

    @Test
    public final void testPutWithEmptyKey() {
        testPutWithDuplicateValue();
        String string = "empty";
        String previous = trie.put(convertToSequence(""), string);
        assertNull(previous);
        assertEquals(string, trie.get(convertToSequence("")));
        assertEquals(12, trie.size());
        verifyRootNode(getRootNode(trie), string);
        verifySuccessors(getRootNode(trie), "r", "A", "B");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "r");
        verifySuccessors(successor, "om", "ub");
        Node<SequenceType, String> successorOm = getSuccessor(successor, "om");
        verifySuccessors(successorOm, "an", "ulus");
        Node<SequenceType, String> successorAn = getSuccessor(successorOm, "an");
        verifySuccessors(successorAn, "e", "us");
        Node<SequenceType, String> successorE = getSuccessor(successorAn, "e");
        verifyLeaf(successorE, "romane");
        Node<SequenceType, String> successorUs = getSuccessor(successorAn, "us");
        verifyLeaf(successorUs, "romanus");
        Node<SequenceType, String> successorUlus = getSuccessor(successorOm, "ulus");
        verifyLeaf(successorUlus, "romulus");
        Node<SequenceType, String> successorUb = getSuccessor(successor, "ub");
        verifySuccessors(successorUb, "e", "ic");
        Node<SequenceType, String> successorE2 = getSuccessor(successorUb, "e");
        verifySuccessors(successorE2, "ns", "r");
        Node<SequenceType, String> successorNs = getSuccessor(successorE2, "ns");
        verifyLeaf(successorNs, "rubens");
        Node<SequenceType, String> successorR = getSuccessor(successorE2, "r");
        verifyLeaf(successorR, "ruber");
        Node<SequenceType, String> successorIc = getSuccessor(successorUb, "ic");
        verifySuccessors(successorIc, "on", "un");
        Node<SequenceType, String> successorOn = getSuccessor(successorIc, "on");
        verifyLeaf(successorOn, "rubicon");
        Node<SequenceType, String> successorUn = getSuccessor(successorIc, "un");
        verifySuccessors(successorUn, "dus");
        Node<SequenceType, String> successorDus = getSuccessor(successorUn, "dus");
        verifyLeaf(successorDus, "rubicundus");
        Node<SequenceType, String> successorA = getSuccessor(getRootNode(trie), "A");
        verifyLeaf(successorA, null);
        Node<SequenceType, String> successorB = getSuccessor(getRootNode(trie), "B");
        verifyLeaf(successorB, "romulus");
    }

    @Test
    public final void testPutWithNullKey() {
        testPutWithEmptyKey();
        String string = "null";
        String previous = trie.put(null, string);
        assertEquals("empty", previous);
        assertEquals(string, trie.get(null));
        assertEquals(12, trie.size());
        verifyRootNode(getRootNode(trie), string);
        verifySuccessors(getRootNode(trie), "r", "A", "B");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "r");
        verifySuccessors(successor, "om", "ub");
        Node<SequenceType, String> successorOm = getSuccessor(successor, "om");
        verifySuccessors(successorOm, "an", "ulus");
        Node<SequenceType, String> successorAn = getSuccessor(successorOm, "an");
        verifySuccessors(successorAn, "e", "us");
        Node<SequenceType, String> successorE = getSuccessor(successorAn, "e");
        verifyLeaf(successorE, "romane");
        Node<SequenceType, String> successorUs = getSuccessor(successorAn, "us");
        verifyLeaf(successorUs, "romanus");
        Node<SequenceType, String> successorUlus = getSuccessor(successorOm, "ulus");
        verifyLeaf(successorUlus, "romulus");
        Node<SequenceType, String> successorUb = getSuccessor(successor, "ub");
        verifySuccessors(successorUb, "e", "ic");
        Node<SequenceType, String> successorE2 = getSuccessor(successorUb, "e");
        verifySuccessors(successorE2, "ns", "r");
        Node<SequenceType, String> successorNs = getSuccessor(successorE2, "ns");
        verifyLeaf(successorNs, "rubens");
        Node<SequenceType, String> successorR = getSuccessor(successorE2, "r");
        verifyLeaf(successorR, "ruber");
        Node<SequenceType, String> successorIc = getSuccessor(successorUb, "ic");
        verifySuccessors(successorIc, "on", "un");
        Node<SequenceType, String> successorOn = getSuccessor(successorIc, "on");
        verifyLeaf(successorOn, "rubicon");
        Node<SequenceType, String> successorUn = getSuccessor(successorIc, "un");
        verifySuccessors(successorUn, "dus");
        Node<SequenceType, String> successorDus = getSuccessor(successorUn, "dus");
        verifyLeaf(successorDus, "rubicundus");
        Node<SequenceType, String> successorA = getSuccessor(getRootNode(trie), "A");
        verifyLeaf(successorA, null);
        Node<SequenceType, String> successorB = getSuccessor(getRootNode(trie), "B");
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
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "romane");
        verifyLeaf(successor, string2);
    }

    @Test
    public final void testPutAll() {
        String string1 = "romane";
        String string2 = "romanus";
        Map<SequenceType, String> map = new HashMap<>();
        map.put(convertToSequence(string1), string1);
        map.put(convertToSequence(string2), string2);
        trie.putAll(map);
        assertEquals(2, trie.size());
        assertEquals(string1, trie.get(convertToSequence(string1)));
        assertEquals(string2, trie.get(convertToSequence(string2)));
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "roman");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "roman");
        verifySuccessors(successor, "e", "us");
        Node<SequenceType, String> successorE = getSuccessor(successor, "e");
        verifyLeaf(successorE, string1);
        Node<SequenceType, String> successorUs = getSuccessor(successor, "us");
        verifyLeaf(successorUs, string2);
    }

    @Test
    public final void testClear() {
        testPut7();
        trie.clear();
        assertTrue(trie.isEmpty());
        assertEquals(0, trie.size());
        assertNull(getRootNode(trie));
        assertNull(trie.get(convertToSequence("romane")));
        assertNull(trie.get(convertToSequence("romanus")));
        assertNull(trie.get(convertToSequence("romulus")));
        assertNull(trie.get(convertToSequence("rubens")));
        assertNull(trie.get(convertToSequence("ruber")));
        assertNull(trie.get(convertToSequence("rubicon")));
        assertNull(trie.get(convertToSequence("rubicundus")));
        assertTrue(trie.values().isEmpty());
        assertTrue(trie.keySet().isEmpty());
        assertTrue(trie.entrySet().isEmpty());
    }

    @Test
    public final void testValues() {
        testPutWithEmptyKey();
        Collection<String> values = trie.values();
        assertEquals(12, values.size());
        assertTrue(values.contains("romane"));
        assertTrue(values.contains("romanus"));
        assertTrue(values.contains("rom"));
        assertTrue(values.contains("romulus"));
        assertTrue(values.contains("rubens"));
        assertTrue(values.contains("ruber"));
        assertTrue(values.contains("rubicon"));
        assertTrue(values.contains("rubicundus"));
        assertTrue(values.contains("rubicun"));
        assertTrue(values.contains(null));
        assertTrue(values.contains("empty"));
    }

    @Test
    public final void testValuesIterator() {
        testPutWithEmptyKey();
        Collection<String> values = trie.values();
        assertEquals(12, values.size());
        Collection<String> actualValues = new ArrayList<>();
        actualValues.add("romane");
        actualValues.add("romanus");
        actualValues.add("rom");
        actualValues.add("romulus");
        actualValues.add("romulus");
        actualValues.add("rubens");
        actualValues.add("ruber");
        actualValues.add("rubicon");
        actualValues.add("rubicundus");
        actualValues.add("rubicun");
        actualValues.add(null);
        actualValues.add("empty");
        Iterator<String> iterator = values.iterator();

        for (int i = 0; i < values.size(); i++) {
            assertTrue(iterator.hasNext());
            String value = iterator.next();
            assertTrue(actualValues.contains(value));
            actualValues.remove(value);
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    public final void testValuesIteratorRemove() {
        testPut3();
        Iterator<String> iterator = trie.values().iterator();
        assertEquals("rom", iterator.next());
        assertEquals("romane", iterator.next());
        iterator.remove();
        assertEquals("romanus", iterator.next());
        assertEquals(2, trie.size());
        assertTrue(trie.containsKey(convertToSequence("rom")));
        assertFalse(trie.containsKey(convertToSequence("romane")));
        assertTrue(trie.containsKey(convertToSequence("romanus")));
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testValuesImmutable() {
        testPut6();
        Collection<String> values = trie.values();
        values.add("foo");
    }

    @Test(expected = ConcurrentModificationException.class)
    public final void testValuesIteratorThrowsConcurrentModificationException() {
        testPut6();
        Collection<String> values = trie.values();
        Iterator<String> iterator = values.iterator();
        trie.put(convertToSequence("foo"), "foo");
        iterator.next();
    }

    @Test
    public final void testContainsValue() {
        testPutWithEmptyKey();
        assertEquals(12, trie.size());
        assertTrue(trie.containsValue("romane"));
        assertTrue(trie.containsValue("romanus"));
        assertTrue(trie.containsValue("rom"));
        assertTrue(trie.containsValue("romulus"));
        assertTrue(trie.containsValue("rubens"));
        assertTrue(trie.containsValue("ruber"));
        assertTrue(trie.containsValue("rubicon"));
        assertTrue(trie.containsValue("rubicundus"));
        assertTrue(trie.containsValue("rubicun"));
        assertTrue(trie.containsValue(null));
        assertTrue(trie.containsValue("empty"));
    }

    @Test
    public final void testKeySet() {
        testPutWithEmptyKey();
        Collection<SequenceType> keys = trie.keySet();
        assertEquals(12, keys.size());
        assertTrue(keys.contains(convertToSequence("romane")));
        assertTrue(keys.contains(convertToSequence("romanus")));
        assertTrue(keys.contains(convertToSequence("rom")));
        assertTrue(keys.contains(convertToSequence("romulus")));
        assertTrue(keys.contains(convertToSequence("rubens")));
        assertTrue(keys.contains(convertToSequence("ruber")));
        assertTrue(keys.contains(convertToSequence("rubicon")));
        assertTrue(keys.contains(convertToSequence("rubicundus")));
        assertTrue(keys.contains(convertToSequence("rubicun")));
        assertTrue(keys.contains(convertToSequence("A")));
        assertTrue(keys.contains(convertToSequence("B")));
        assertTrue(keys.contains(null));
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testKeySetImmutable() {
        testPut6();
        Collection<SequenceType> keys = trie.keySet();
        keys.add(convertToSequence("foo"));
    }

    @Test
    public final void testKeySetIterator() {
        testPutWithEmptyKey();
        Collection<SequenceType> keys = trie.keySet();
        assertEquals(12, keys.size());
        Iterator<SequenceType> iterator = keys.iterator();
        Collection<SequenceType> actualKeys = new ArrayList<>();
        actualKeys.add(convertToSequence("romane"));
        actualKeys.add(convertToSequence("romanus"));
        actualKeys.add(convertToSequence("rom"));
        actualKeys.add(convertToSequence("romulus"));
        actualKeys.add(convertToSequence("rubens"));
        actualKeys.add(convertToSequence("ruber"));
        actualKeys.add(convertToSequence("rubicon"));
        actualKeys.add(convertToSequence("rubicundus"));
        actualKeys.add(convertToSequence("rubicun"));
        actualKeys.add(convertToSequence("A"));
        actualKeys.add(convertToSequence("B"));
        actualKeys.add(null);

        for (int i = 0; i < keys.size(); i++) {
            assertTrue(iterator.hasNext());
            SequenceType key = iterator.next();
            assertTrue(actualKeys.contains(key));
            actualKeys.remove(key);
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    public final void testKeySetIteratorRemove() {
        testPut3();
        Iterator<SequenceType> iterator = trie.keySet().iterator();
        assertEquals(convertToSequence("rom"), iterator.next());
        assertEquals(convertToSequence("romane"), iterator.next());
        iterator.remove();
        assertEquals(convertToSequence("romanus"), iterator.next());
        assertEquals(2, trie.size());
        assertTrue(trie.containsKey(convertToSequence("rom")));
        assertFalse(trie.containsKey(convertToSequence("romane")));
        assertTrue(trie.containsKey(convertToSequence("romanus")));
    }

    @Test(expected = ConcurrentModificationException.class)
    public final void testKeySetIteratorThrowsConcurrentModificationException() {
        testPut6();
        Collection<SequenceType> keys = trie.keySet();
        Iterator<SequenceType> iterator = keys.iterator();
        trie.put(convertToSequence("foo"), "foo");
        iterator.next();
    }

    @Test
    public final void testContainsKey() {
        testPutWithEmptyKey();
        assertEquals(12, trie.size());
        assertTrue(trie.containsKey(convertToSequence("romane")));
        assertTrue(trie.containsKey(convertToSequence("romanus")));
        assertTrue(trie.containsKey(convertToSequence("rom")));
        assertTrue(trie.containsKey(convertToSequence("romulus")));
        assertTrue(trie.containsKey(convertToSequence("rubens")));
        assertTrue(trie.containsKey(convertToSequence("ruber")));
        assertTrue(trie.containsKey(convertToSequence("rubicon")));
        assertTrue(trie.containsKey(convertToSequence("rubicundus")));
        assertTrue(trie.containsKey(convertToSequence("rubicun")));
        assertTrue(trie.containsKey(convertToSequence("A")));
        assertTrue(trie.containsKey(convertToSequence("B")));
        assertTrue(trie.containsKey(convertToSequence("")));
        assertTrue(trie.containsKey(null));
    }

    @Test
    public final void testEntrySet() {
        testPutWithEmptyKey();
        Set<Map.Entry<SequenceType, String>> entrySet = trie.entrySet();
        assertEquals(12, entrySet.size());
        assertTrue(entrySet.contains(
                new AbstractMap.SimpleImmutableEntry<>(convertToSequence("romane"), "romane")));
        assertTrue(entrySet.contains(
                new AbstractMap.SimpleImmutableEntry<>(convertToSequence("romanus"), "romanus")));
        assertTrue(entrySet.contains(
                new AbstractMap.SimpleImmutableEntry<>(convertToSequence("rom"), "rom")));
        assertTrue(entrySet.contains(
                new AbstractMap.SimpleImmutableEntry<>(convertToSequence("romulus"), "romulus")));
        assertTrue(entrySet.contains(
                new AbstractMap.SimpleImmutableEntry<>(convertToSequence("rubens"), "rubens")));
        assertTrue(entrySet.contains(
                new AbstractMap.SimpleImmutableEntry<>(convertToSequence("ruber"), "ruber")));
        assertTrue(entrySet.contains(
                new AbstractMap.SimpleImmutableEntry<>(convertToSequence("rubicon"), "rubicon")));
        assertTrue(entrySet.contains(
                new AbstractMap.SimpleImmutableEntry<>(convertToSequence("rubicundus"),
                        "rubicundus")));
        assertTrue(entrySet.contains(
                new AbstractMap.SimpleImmutableEntry<>(convertToSequence("rubicun"), "rubicun")));
        assertTrue(entrySet.contains(
                new AbstractMap.SimpleImmutableEntry<>(convertToSequence("A"), (String) null)));
        assertTrue(entrySet.contains(
                new AbstractMap.SimpleImmutableEntry<>(convertToSequence("B"), "romulus")));
        assertTrue(entrySet.contains(
                new AbstractMap.SimpleImmutableEntry<>((SequenceType) null, "empty")));
    }

    @Test
    public final void testEntrySetIterator() {
        testPutWithEmptyKey();
        Set<Map.Entry<SequenceType, String>> entrySet = trie.entrySet();
        assertEquals(12, entrySet.size());
        Collection<Map.Entry<SequenceType, String>> actualEntries = new ArrayList<>();
        actualEntries
                .add(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("romane"), "romane"));
        actualEntries.add(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("romanus"),
                "romanus"));
        actualEntries.add(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("rom"),
                "rom"));
        actualEntries.add(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("romulus"),
                "romulus"));
        actualEntries
                .add(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("rubens"), "rubens"));
        actualEntries
                .add(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("ruber"), "ruber"));
        actualEntries.add(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("rubicon"),
                "rubicon"));
        actualEntries.add(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("rubicundus"),
                "rubicundus"));
        actualEntries.add(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("rubicun"),
                "rubicun"));
        actualEntries.add(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("A"), null));
        actualEntries
                .add(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("B"), "romulus"));
        actualEntries.add(new AbstractMap.SimpleImmutableEntry<>(null, "empty"));
        Iterator<Map.Entry<SequenceType, String>> iterator = entrySet.iterator();

        for (int i = 0; i < entrySet.size(); i++) {
            assertTrue(iterator.hasNext());
            Map.Entry<SequenceType, String> entry = iterator.next();
            assertTrue(actualEntries.contains(entry));
            actualEntries.remove(entry);
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    public final void testEntrySetIteratorRemove() {
        testPut3();
        Iterator<Map.Entry<SequenceType, String>> iterator = trie.entrySet().iterator();
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("rom"), "rom"),
                iterator.next());
        assertEquals(
                new AbstractMap.SimpleImmutableEntry<>(convertToSequence("romane"), "romane"),
                iterator.next());
        iterator.remove();
        assertEquals(
                new AbstractMap.SimpleImmutableEntry<>(convertToSequence("romanus"), "romanus"),
                iterator.next());
        assertEquals(2, trie.size());
        assertTrue(trie.containsKey(convertToSequence("rom")));
        assertFalse(trie.containsKey(convertToSequence("romane")));
        assertTrue(trie.containsKey(convertToSequence("romanus")));
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testEntrySetImmutable() {
        testPut6();
        Set<Map.Entry<SequenceType, String>> entrySet = trie.entrySet();
        entrySet.add(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("foo"), "foo"));
    }

    @Test(expected = ConcurrentModificationException.class)
    public final void testEntrySetIteratorThrowsConcurrentModificationException() {
        testPut6();
        Set<Map.Entry<SequenceType, String>> entrySet = trie.entrySet();
        Iterator<Map.Entry<SequenceType, String>> iterator = entrySet.iterator();
        trie.put(convertToSequence("foo"), "foo");
        iterator.next();
    }

    @Test
    public final void testRemoveIfKeyIsNotContainedAndIsPrefix() {
        testPut1();
        String removed = trie.remove(convertToSequence("rom"));
        assertNull(removed);
        assertFalse(trie.isEmpty());
        assertEquals(1, trie.size());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "romane");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "romane");
        verifyLeaf(successor, "romane");
    }

    @Test
    public final void testRemoveIfKeyIsNotContainedAndSharesPrefix() {
        testPut2();
        String removed = trie.remove(convertToSequence("romanu"));
        assertNull(removed);
        assertFalse(trie.isEmpty());
        assertEquals(2, trie.size());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "roman");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "roman");
        verifySuccessors(successor, "e", "us");
        Node<SequenceType, String> successorE = getSuccessor(successor, "e");
        verifyLeaf(successorE, "romane");
        Node<SequenceType, String> successorUs = getSuccessor(successor, "us");
        verifyLeaf(successorUs, "romanus");
    }

    @Test
    public final void testRemoveIfKeyIsNotContainedAndContainsOtherKeyAsPrefix() {
        testPut1();
        String removed = trie.remove(convertToSequence("romanee"));
        assertNull(removed);
        assertFalse(trie.isEmpty());
        assertEquals(1, trie.size());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "romane");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "romane");
        verifyLeaf(successor, "romane");
    }

    @Test
    public final void testRemoveIfKeyIsTheOnlyOne() {
        String string = "romane";
        testPut1();
        String removed = trie.remove(convertToSequence(string));
        assertEquals(string, removed);
        assertNull(trie.get(convertToSequence(string)));
        assertNull(getRootNode(trie));
        assertEquals(0, trie.size());
        assertTrue(trie.isEmpty());
    }

    @Test
    public final void testRemoveIfKeySharesPrefix() {
        testPut2();
        String string = "romanus";
        String removed = trie.remove(convertToSequence(string));
        assertEquals(string, removed);
        assertNull(trie.get(convertToSequence(string)));
        assertEquals(1, trie.size());
        assertFalse(trie.isEmpty());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "romane");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "romane");
        verifyLeaf(successor, "romane");
    }

    @Test
    public final void testRemoveIfKeyIsPrefix() {
        testPut9();
        String string = "rubicun";
        String removed = trie.remove(convertToSequence("rubicun"));
        assertEquals(string, removed);
        assertNull(trie.get(convertToSequence(string)));
        assertEquals(8, trie.size());
        assertFalse(trie.isEmpty());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "r");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "r");
        verifySuccessors(successor, "om", "ub");
        Node<SequenceType, String> successorOm = getSuccessor(successor, "om");
        verifySuccessors(successorOm, "an", "ulus");
        Node<SequenceType, String> successorAn = getSuccessor(successorOm, "an");
        verifySuccessors(successorAn, "e", "us");
        Node<SequenceType, String> successorE = getSuccessor(successorAn, "e");
        verifyLeaf(successorE, "romane");
        Node<SequenceType, String> successorUs = getSuccessor(successorAn, "us");
        verifyLeaf(successorUs, "romanus");
        Node<SequenceType, String> successorUlus = getSuccessor(successorOm, "ulus");
        verifyLeaf(successorUlus, "romulus");
        Node<SequenceType, String> successorUb = getSuccessor(successor, "ub");
        verifySuccessors(successorUb, "e", "ic");
        Node<SequenceType, String> successorE2 = getSuccessor(successorUb, "e");
        verifySuccessors(successorE2, "ns", "r");
        Node<SequenceType, String> successorNs = getSuccessor(successorE2, "ns");
        verifyLeaf(successorNs, "rubens");
        Node<SequenceType, String> successorR = getSuccessor(successorE2, "r");
        verifyLeaf(successorR, "ruber");
        Node<SequenceType, String> successorIc = getSuccessor(successorUb, "ic");
        verifySuccessors(successorIc, "on", "undus");
        Node<SequenceType, String> successorOn = getSuccessor(successorIc, "on");
        verifyLeaf(successorOn, "rubicon");
        Node<SequenceType, String> successorUndus = getSuccessor(successorIc, "undus");
        verifyLeaf(successorUndus, "rubicundus");
    }

    @Test
    public final void testRemoveIfKeyContainsOtherKeyAsPrefix() {
        testPut9();
        String string = "rubicundus";
        String removed = trie.remove(convertToSequence(string));
        assertEquals(string, removed);
        assertNull(trie.get(convertToSequence(string)));
        assertEquals(8, trie.size());
        assertFalse(trie.isEmpty());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "r");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "r");
        verifySuccessors(successor, "om", "ub");
        Node<SequenceType, String> successorOm = getSuccessor(successor, "om");
        verifySuccessors(successorOm, "an", "ulus");
        Node<SequenceType, String> successorAn = getSuccessor(successorOm, "an");
        verifySuccessors(successorAn, "e", "us");
        Node<SequenceType, String> successorE = getSuccessor(successorAn, "e");
        verifyLeaf(successorE, "romane");
        Node<SequenceType, String> successorUs = getSuccessor(successorAn, "us");
        verifyLeaf(successorUs, "romanus");
        Node<SequenceType, String> successorUlus = getSuccessor(successorOm, "ulus");
        verifyLeaf(successorUlus, "romulus");
        Node<SequenceType, String> successorUb = getSuccessor(successor, "ub");
        verifySuccessors(successorUb, "e", "ic");
        Node<SequenceType, String> successorE2 = getSuccessor(successorUb, "e");
        verifySuccessors(successorE2, "ns", "r");
        Node<SequenceType, String> successorNs = getSuccessor(successorE2, "ns");
        verifyLeaf(successorNs, "rubens");
        Node<SequenceType, String> successorR = getSuccessor(successorE2, "r");
        verifyLeaf(successorR, "ruber");
        Node<SequenceType, String> successorIc = getSuccessor(successorUb, "ic");
        verifySuccessors(successorIc, "on", "un");
        Node<SequenceType, String> successorOn = getSuccessor(successorIc, "on");
        verifyLeaf(successorOn, "rubicon");
        Node<SequenceType, String> successorUn = getSuccessor(successorIc, "un");
        verifyLeaf(successorUn, "rubicun");
    }

    @Test
    public final void testRemoveEmptyKey() {
        testPutWithEmptyKey();
        String string = "empty";
        String removed = trie.remove(convertToSequence(""));
        assertEquals(string, removed);
        assertNull(trie.get(convertToSequence("")));
        assertNull(trie.get(null));
        assertEquals(11, trie.size());
        assertFalse(trie.isEmpty());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "r", "A", "B");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "r");
        verifySuccessors(successor, "om", "ub");
        Node<SequenceType, String> successorOm = getSuccessor(successor, "om");
        verifySuccessors(successorOm, "an", "ulus");
        Node<SequenceType, String> successorAn = getSuccessor(successorOm, "an");
        verifySuccessors(successorAn, "e", "us");
        Node<SequenceType, String> successorE = getSuccessor(successorAn, "e");
        verifyLeaf(successorE, "romane");
        Node<SequenceType, String> successorUs = getSuccessor(successorAn, "us");
        verifyLeaf(successorUs, "romanus");
        Node<SequenceType, String> successorUlus = getSuccessor(successorOm, "ulus");
        verifyLeaf(successorUlus, "romulus");
        Node<SequenceType, String> successorUb = getSuccessor(successor, "ub");
        verifySuccessors(successorUb, "e", "ic");
        Node<SequenceType, String> successorE2 = getSuccessor(successorUb, "e");
        verifySuccessors(successorE2, "ns", "r");
        Node<SequenceType, String> successorNs = getSuccessor(successorE2, "ns");
        verifyLeaf(successorNs, "rubens");
        Node<SequenceType, String> successorR = getSuccessor(successorE2, "r");
        verifyLeaf(successorR, "ruber");
        Node<SequenceType, String> successorIc = getSuccessor(successorUb, "ic");
        verifySuccessors(successorIc, "on", "un");
        Node<SequenceType, String> successorOn = getSuccessor(successorIc, "on");
        verifyLeaf(successorOn, "rubicon");
        Node<SequenceType, String> successorUn = getSuccessor(successorIc, "un");
        verifySuccessors(successorUn, "dus");
        Node<SequenceType, String> successorDus = getSuccessor(successorUn, "dus");
        verifyLeaf(successorDus, "rubicundus");
        Node<SequenceType, String> successorA = getSuccessor(getRootNode(trie), "A");
        verifyLeaf(successorA, null);
        Node<SequenceType, String> successorB = getSuccessor(getRootNode(trie), "B");
        verifyLeaf(successorB, "romulus");
    }

    @Test
    public final void testRemoveNullKey() {
        testPutWithEmptyKey();
        String string = "empty";
        String removed = trie.remove(null);
        assertEquals(string, removed);
        assertNull(trie.get(null));
        assertNull(trie.get(convertToSequence("")));
        assertEquals(11, trie.size());
        assertFalse(trie.isEmpty());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "r", "A", "B");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "r");
        verifySuccessors(successor, "om", "ub");
        Node<SequenceType, String> successorOm = getSuccessor(successor, "om");
        verifySuccessors(successorOm, "an", "ulus");
        Node<SequenceType, String> successorAn = getSuccessor(successorOm, "an");
        verifySuccessors(successorAn, "e", "us");
        Node<SequenceType, String> successorE = getSuccessor(successorAn, "e");
        verifyLeaf(successorE, "romane");
        Node<SequenceType, String> successorUs = getSuccessor(successorAn, "us");
        verifyLeaf(successorUs, "romanus");
        Node<SequenceType, String> successorUlus = getSuccessor(successorOm, "ulus");
        verifyLeaf(successorUlus, "romulus");
        Node<SequenceType, String> successorUb = getSuccessor(successor, "ub");
        verifySuccessors(successorUb, "e", "ic");
        Node<SequenceType, String> successorE2 = getSuccessor(successorUb, "e");
        verifySuccessors(successorE2, "ns", "r");
        Node<SequenceType, String> successorNs = getSuccessor(successorE2, "ns");
        verifyLeaf(successorNs, "rubens");
        Node<SequenceType, String> successorR = getSuccessor(successorE2, "r");
        verifyLeaf(successorR, "ruber");
        Node<SequenceType, String> successorIc = getSuccessor(successorUb, "ic");
        verifySuccessors(successorIc, "on", "un");
        Node<SequenceType, String> successorOn = getSuccessor(successorIc, "on");
        verifyLeaf(successorOn, "rubicon");
        Node<SequenceType, String> successorUn = getSuccessor(successorIc, "un");
        verifySuccessors(successorUn, "dus");
        Node<SequenceType, String> successorDus = getSuccessor(successorUn, "dus");
        verifyLeaf(successorDus, "rubicundus");
        Node<SequenceType, String> successorA = getSuccessor(getRootNode(trie), "A");
        verifyLeaf(successorA, null);
        Node<SequenceType, String> successorB = getSuccessor(getRootNode(trie), "B");
        verifyLeaf(successorB, "romulus");
    }

    @Test
    public final void testFloorEntry() {
        testPut7();
        SequenceType key = convertToSequence("romulus");
        Map.Entry<SequenceType, String> entry = trie.floorEntry(key);
        assertNotNull(entry);
        assertEquals(key, entry.getKey());
        assertEquals("romulus", entry.getValue());
    }

    @Test
    public final void testCeilingEntry() {
        testPut7();
        SequenceType key = convertToSequence("romulus");
        Map.Entry<SequenceType, String> entry = trie.ceilingEntry(key);
        assertNotNull(entry);
        assertEquals(key, entry.getKey());
        assertEquals("romulus", entry.getValue());
    }

    @Test
    public final void testFirstEntry1() {
        testPut7();
        Map.Entry<SequenceType, String> entry = trie.firstEntry();
        assertNotNull(entry);
        assertEquals(convertToSequence("rom"), entry.getKey());
        assertEquals("rom", entry.getValue());
    }

    @Test
    public void testFirstEntry2() {
        testPutWithEmptyKey();
        Map.Entry<SequenceType, String> entry = trie.firstEntry();
        assertNotNull(entry);
        assertNull(entry.getKey());
        assertEquals("empty", entry.getValue());
    }

    @Test
    public final void testFirstKey1() {
        testPut7();
        SequenceType key = trie.firstKey();
        assertEquals(convertToSequence("rom"), key);
    }

    @Test
    public final void testFirstKey2() {
        testPutWithEmptyKey();
        SequenceType key = trie.firstKey();
        assertNull(key);
    }

    @Test
    public final void testLastEntry1() {
        testPut7();
        Map.Entry<SequenceType, String> entry = trie.lastEntry();
        assertNotNull(entry);
        assertEquals(convertToSequence("rubicon"), entry.getKey());
        assertEquals("rubicon", entry.getValue());
    }

    @Test
    public final void testLastEntry2() {
        testPut7();
        trie.put(convertToSequence("x"), "x");
        Map.Entry<SequenceType, String> entry = trie.lastEntry();
        assertNotNull(entry);
        assertEquals(convertToSequence("x"), entry.getKey());
        assertEquals("x", entry.getValue());
    }

    @Test
    public final void testLastKey1() {
        testPut7();
        SequenceType key = trie.lastKey();
        assertEquals(convertToSequence("rubicon"), key);
    }

    @Test
    public final void testLastKey2() {
        testPut7();
        trie.put(convertToSequence("x"), "x");
        SequenceType key = trie.lastKey();
        assertEquals(convertToSequence("x"), key);
    }

    @Test
    public final void testPollFirstEntryIfKeyIsTheOnlyOne() {
        testPut1();
        String string = "romane";
        Map.Entry<SequenceType, String> removed = trie.pollFirstEntry();
        assertNotNull(removed);
        assertEquals(convertToSequence(string), removed.getKey());
        assertEquals(string, removed.getValue());
        assertNull(trie.get(convertToSequence(string)));
        assertNull(getRootNode(trie));
        assertEquals(0, trie.size());
        assertTrue(trie.isEmpty());
    }

    @Test
    public final void testPollFirstEntryIfKeyIsPrefix() {
        testPut7();
        String string = "rom";
        Map.Entry<SequenceType, String> removed = trie.pollFirstEntry();
        assertNotNull(removed);
        assertEquals(convertToSequence(string), removed.getKey());
        assertEquals(string, removed.getValue());
        assertNull(trie.get(convertToSequence(string)));
        assertEquals(6, trie.size());
        assertFalse(trie.isEmpty());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "r");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "r");
        verifySuccessors(successor, "om", "ub");
        Node<SequenceType, String> successorOm = getSuccessor(successor, "om");
        verifySuccessors(successorOm, "an", "ulus");
        Node<SequenceType, String> successorAn = getSuccessor(successorOm, "an");
        verifySuccessors(successorAn, "e", "us");
        Node<SequenceType, String> successorE = getSuccessor(successorAn, "e");
        verifyLeaf(successorE, "romane");
        Node<SequenceType, String> successorUs = getSuccessor(successorAn, "us");
        verifyLeaf(successorUs, "romanus");
        Node<SequenceType, String> successorUlus = getSuccessor(successorOm, "ulus");
        verifyLeaf(successorUlus, "romulus");
        Node<SequenceType, String> successorUb = getSuccessor(successor, "ub");
        verifySuccessors(successorUb, "e", "icon");
        Node<SequenceType, String> successorE2 = getSuccessor(successorUb, "e");
        verifySuccessors(successorE2, "ns", "r");
        Node<SequenceType, String> successorNs = getSuccessor(successorE2, "ns");
        verifyLeaf(successorNs, "rubens");
        Node<SequenceType, String> successorR = getSuccessor(successorE2, "r");
        verifyLeaf(successorR, "ruber");
        Node<SequenceType, String> successorIcon = getSuccessor(successorUb, "icon");
        verifyLeaf(successorIcon, "rubicon");
    }

    @Test
    public final void testPollFirstEntryIfKeyIsEmpty() {
        testPutWithEmptyKey();
        String string = "empty";
        Map.Entry<SequenceType, String> removed = trie.pollFirstEntry();
        assertNotNull(removed);
        assertNull(removed.getKey());
        assertEquals(string, removed.getValue());
        assertNull(trie.get(convertToSequence("")));
        assertNull(trie.get(null));
        assertEquals(11, trie.size());
        assertFalse(trie.isEmpty());
        verifyRootNode(getRootNode(trie));
    }

    @Test
    public final void testPollLastEntryIfKeyIsTheOnlyOne() {
        testPut1();
        String string = "romane";
        Map.Entry<SequenceType, String> removed = trie.pollLastEntry();
        assertNotNull(removed);
        assertEquals(convertToSequence(string), removed.getKey());
        assertEquals(string, removed.getValue());
        assertNull(trie.get(convertToSequence(string)));
        assertNull(getRootNode(trie));
        assertEquals(0, trie.size());
        assertTrue(trie.isEmpty());
    }

    @Test
    public final void testPollLastEntryIfOtherKeyIsPrefix() {
        testPut7();
        String string = "rubiconX";
        trie.put(convertToSequence(string), string);
        Map.Entry<SequenceType, String> removed = trie.pollLastEntry();
        assertNotNull(removed);
        assertEquals(convertToSequence(string), removed.getKey());
        assertEquals(string, removed.getValue());
        assertNull(trie.get(convertToSequence(string)));
        assertEquals(7, trie.size());
        assertFalse(trie.isEmpty());
        verifyRootNode(getRootNode(trie));
        verifySuccessors(getRootNode(trie), "r");
        Node<SequenceType, String> successor = getSuccessor(getRootNode(trie), "r");
        verifySuccessors(successor, "om", "ub");
        Node<SequenceType, String> successorOm = getSuccessor(successor, "om");
        verifySuccessors(successorOm, "an", "ulus");
        Node<SequenceType, String> successorAn = getSuccessor(successorOm, "an");
        verifySuccessors(successorAn, "e", "us");
        Node<SequenceType, String> successorE = getSuccessor(successorAn, "e");
        verifyLeaf(successorE, "romane");
        Node<SequenceType, String> successorUs = getSuccessor(successorAn, "us");
        verifyLeaf(successorUs, "romanus");
        Node<SequenceType, String> successorUlus = getSuccessor(successorOm, "ulus");
        verifyLeaf(successorUlus, "romulus");
        Node<SequenceType, String> successorUb = getSuccessor(successor, "ub");
        verifySuccessors(successorUb, "e", "icon");
        Node<SequenceType, String> successorE2 = getSuccessor(successorUb, "e");
        verifySuccessors(successorE2, "ns", "r");
        Node<SequenceType, String> successorNs = getSuccessor(successorE2, "ns");
        verifyLeaf(successorNs, "rubens");
        Node<SequenceType, String> successorR = getSuccessor(successorE2, "r");
        verifyLeaf(successorR, "ruber");
        Node<SequenceType, String> successorIcon = getSuccessor(successorUb, "icon");
        verifyLeaf(successorIcon, "rubicon");
    }

    @Test
    public final void testLowerEntryIfLowerKeyIsPrefix() {
        testPut7();
        Map.Entry<SequenceType, String> entry = trie.lowerEntry(convertToSequence("romane"));
        assertNotNull(entry);
        assertEquals(convertToSequence("rom"), entry.getKey());
        assertEquals("rom", entry.getValue());
    }

    @Test
    public final void testLowerEntryIfLowerKeySharesPredecessor() {
        testPut7();
        Map.Entry<SequenceType, String> entry = trie.lowerEntry(convertToSequence("romanus"));
        assertNotNull(entry);
        assertEquals(convertToSequence("romane"), entry.getKey());
        assertEquals("romane", entry.getValue());
    }

    @Test
    public final void testLowerEntryIfLowerKeySharesPrefix() {
        testPutWithEmptyKey();
        Map.Entry<SequenceType, String> entry = trie.lowerEntry(convertToSequence("romulus"));
        assertNotNull(entry);
        assertEquals(convertToSequence("romanus"), entry.getKey());
        assertEquals("romanus", entry.getValue());
    }

    @Test
    public final void testLowerEntryIfLowerKeyIsNotAvailable() {
        testPut7();
        Map.Entry<SequenceType, String> entry = trie.lowerEntry(convertToSequence("A"));
        assertNull(entry);
    }

    @Test
    public final void testLowerEntryIfLowerKeyIsEmpty() {
        testPutWithEmptyKey();
        Map.Entry<SequenceType, String> entry = trie.lowerEntry(convertToSequence("A"));
        assertNotNull(entry);
        assertNull(entry.getKey());
        assertEquals("empty", entry.getValue());
    }

    @Test
    public final void testLowerKeyIfKeyIsNotContained() {
        assertNull(trie.lowerKey(convertToSequence("foo")));
    }

    @Test
    public final void testLowerKeyIfLowerKeyIsPrefix() {
        testPut6();
        SequenceType lowerKey = trie.lowerKey(convertToSequence("romane"));
        assertEquals(convertToSequence("rom"), lowerKey);
    }

    @Test
    public final void testLowerKeyIfLowerKeySharesPredecessor() {
        testPut6();
        SequenceType lowerKey = trie.lowerKey(convertToSequence("romanus"));
        assertEquals(convertToSequence("romane"), lowerKey);
    }

    @Test
    public final void testLowerKeyIfLowerKeySharesPrefix() {
        testPutWithEmptyKey();
        SequenceType lowerKey = trie.lowerKey(convertToSequence("romulus"));
        assertEquals(convertToSequence("romanus"), lowerKey);
    }

    @Test
    public final void testLowerKeyIfLowerKeyIsNotAvailable() {
        testPut7();
        assertNull(trie.lowerKey(convertToSequence("A")));
    }

    @Test
    public final void testLowerKeyIfLowerKeyIsEmpty() {
        testPutWithEmptyKey();
        SequenceType lowerKey = trie.lowerKey(convertToSequence("A"));
        assertNull(lowerKey);
    }

    @Test
    public final void testHigherEntryIfHigherKeySharesPrefix() {
        testPut7();
        Map.Entry<SequenceType, String> entry = trie.higherEntry(convertToSequence("rom"));
        assertNotNull(entry);
        assertEquals(convertToSequence("romane"), entry.getKey());
        assertEquals("romane", entry.getValue());
    }

    @Test
    public final void testHigherEntryIfHigherKeySharesPredecessor() {
        testPut6();
        Map.Entry<SequenceType, String> entry = trie.higherEntry(convertToSequence("romane"));
        assertNotNull(entry);
        assertEquals(convertToSequence("romanus"), entry.getKey());
        assertEquals("romanus", entry.getValue());
    }

    @Test
    public final void testHigherEntryIfHigherKeyIsNotAvailable() {
        testPut7();
        Map.Entry<SequenceType, String> entry = trie.higherEntry(convertToSequence("rubicon"));
        assertNull(entry);
    }

    @Test
    public final void testHigherKeyIfHigherKeySharesPrefix() {
        testPut7();
        String string = "rubiconX";
        trie.put(convertToSequence(string), string);
        SequenceType key = trie.higherKey(convertToSequence("rubicon"));
        assertEquals(convertToSequence(string), key);
    }

    @Test
    public final void testHigherKeyIfHigherKeySharesPredecessor() {
        testPut7();
        SequenceType key = trie.higherKey(convertToSequence("romane"));
        assertEquals(convertToSequence("romanus"), key);
    }

    @Test
    public final void testHigherKeyIfHigherKeyIsNotAvailable() {
        testPut7();
        assertNull(trie.higherKey(convertToSequence("rubicon")));
    }

    @Test
    public final void testSubMap1() {
        testPutWithEmptyKey();
        SortedMap<SequenceType, String> subMap = trie
                .subMap(convertToSequence("romane"), convertToSequence("rubicon"));
        assertEquals(5, subMap.size());
        Iterator<Map.Entry<SequenceType, String>> iterator = subMap.entrySet().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("romane"), "romane"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(
                new AbstractMap.SimpleImmutableEntry<>(convertToSequence("romanus"), "romanus"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(
                new AbstractMap.SimpleImmutableEntry<>(convertToSequence("romulus"), "romulus"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("rubens"), "rubens"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("ruber"), "ruber"),
                iterator.next());
        assertFalse(iterator.hasNext());
        assertEquals(convertToSequence("romane"), subMap.firstKey());
        assertEquals(convertToSequence("ruber"), subMap.lastKey());
    }

    @Test
    public final void testSubMap2() {
        testPutWithEmptyKey();
        NavigableMap<SequenceType, String> subMap = trie
                .subMap(convertToSequence("romane"), true, convertToSequence("rubicon"), true);
        assertEquals(6, subMap.size());
        Iterator<Map.Entry<SequenceType, String>> iterator = subMap.entrySet().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("romane"), "romane"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(
                new AbstractMap.SimpleImmutableEntry<>(convertToSequence("romanus"), "romanus"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(
                new AbstractMap.SimpleImmutableEntry<>(convertToSequence("romulus"), "romulus"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("rubens"), "rubens"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("ruber"), "ruber"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(
                new AbstractMap.SimpleImmutableEntry<>(convertToSequence("rubicon"), "rubicon"),
                iterator.next());
        assertFalse(iterator.hasNext());
        assertEquals(convertToSequence("romane"), subMap.firstKey());
        assertEquals(
                new AbstractMap.SimpleImmutableEntry<>(convertToSequence("rubicon"), "rubicon"),
                subMap.lastEntry());
        assertEquals(convertToSequence("rubicon"), subMap.lastKey());
        assertEquals(
                new AbstractMap.SimpleImmutableEntry<>(convertToSequence("romanus"), "romanus"),
                subMap.higherEntry(convertToSequence("romane")));
        assertEquals(convertToSequence("romanus"), subMap.higherKey(convertToSequence("romane")));
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("ruber"), "ruber"),
                subMap.lowerEntry(convertToSequence("rubicon")));
        assertEquals(convertToSequence("ruber"), subMap.lowerKey(convertToSequence("rubicon")));
        assertNull(subMap.lowerEntry(convertToSequence("romane")));
        assertNull(subMap.lowerKey(convertToSequence("romane")));
    }

    @Test
    public final void testSubMapRemove() {
        testPut3();
        NavigableMap<SequenceType, String> subMap = trie
                .subMap(convertToSequence("rom"), true, convertToSequence("romanus"), true);
        assertEquals(3, subMap.size());
        Iterator<Map.Entry<SequenceType, String>> iterator = subMap.entrySet().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("rom"), "rom"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("romane"), "romane"),
                iterator.next());
        iterator.remove();
        assertTrue(iterator.hasNext());
        assertEquals(
                new AbstractMap.SimpleImmutableEntry<>(convertToSequence("romanus"), "romanus"),
                iterator.next());
        assertFalse(iterator.hasNext());
        assertEquals(2, trie.size());
        assertTrue(trie.containsKey(convertToSequence("rom")));
        assertFalse(trie.containsKey(convertToSequence("romane")));
        assertTrue(trie.containsKey(convertToSequence("romanus")));
    }

    @Test
    public final void testHeadMap1() {
        testPutWithEmptyKey();
        SortedMap<SequenceType, String> headMap = trie.headMap(convertToSequence("romanus"));
        assertEquals(5, headMap.size());
        Iterator<Map.Entry<SequenceType, String>> iterator = headMap.entrySet().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(null, "empty"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("A"), null),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("B"), "romulus"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("rom"), "rom"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("romane"), "romane"),
                iterator.next());
        assertFalse(iterator.hasNext());
        assertNull(headMap.firstKey());
        assertEquals(convertToSequence("romane"), headMap.lastKey());
    }

    @Test
    public final void testHeadMap2() {
        testPutWithEmptyKey();
        NavigableMap<SequenceType, String> headMap = trie
                .headMap(convertToSequence("romanus"), true);
        assertEquals(6, headMap.size());
        Iterator<Map.Entry<SequenceType, String>> iterator = headMap.entrySet().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(null, "empty"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("A"), null),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("B"), "romulus"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("rom"), "rom"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("romane"), "romane"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(
                new AbstractMap.SimpleImmutableEntry<>(convertToSequence("romanus"), "romanus"),
                iterator.next());
        assertFalse(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(null, "empty"), headMap.firstEntry());
        assertNull(headMap.firstKey());
        assertEquals(
                new AbstractMap.SimpleImmutableEntry<>(convertToSequence("romanus"), "romanus"),
                headMap.lastEntry());
        assertEquals(convertToSequence("romanus"), headMap.lastKey());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("A"), null),
                headMap.higherEntry(null));
        assertEquals(convertToSequence("A"), headMap.higherKey(null));
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("romane"), "romane"),
                headMap.lowerEntry(convertToSequence("romanus")));
        assertEquals(convertToSequence("romane"), headMap.lowerKey(convertToSequence("romanus")));
        assertNull(headMap.higherEntry(convertToSequence("romanus")));
        assertNull(headMap.higherKey(convertToSequence("romanus")));
    }

    @Test
    public final void testHeadMapRemove() {
        testPut3();
        NavigableMap<SequenceType, String> subMap = trie
                .headMap(convertToSequence("romanus"), true);
        assertEquals(3, subMap.size());
        Iterator<Map.Entry<SequenceType, String>> iterator = subMap.entrySet().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("rom"), "rom"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("romane"), "romane"),
                iterator.next());
        iterator.remove();
        assertTrue(iterator.hasNext());
        assertEquals(
                new AbstractMap.SimpleImmutableEntry<>(convertToSequence("romanus"), "romanus"),
                iterator.next());
        assertFalse(iterator.hasNext());
        assertEquals(2, trie.size());
        assertTrue(trie.containsKey(convertToSequence("rom")));
        assertFalse(trie.containsKey(convertToSequence("romane")));
        assertTrue(trie.containsKey(convertToSequence("romanus")));
    }

    @Test
    public final void testTailMap1() {
        testPutWithEmptyKey();
        SortedMap<SequenceType, String> tailMap = trie.tailMap(convertToSequence("romulus"));
        assertEquals(6, tailMap.size());
        Iterator<Map.Entry<SequenceType, String>> iterator = tailMap.entrySet().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(
                new AbstractMap.SimpleImmutableEntry<>(convertToSequence("romulus"), "romulus"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("rubens"), "rubens"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("ruber"), "ruber"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(
                new AbstractMap.SimpleImmutableEntry<>(convertToSequence("rubicon"), "rubicon"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(
                new AbstractMap.SimpleImmutableEntry<>(convertToSequence("rubicun"), "rubicun"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("rubicundus"),
                        "rubicundus"),
                iterator.next());
        assertFalse(iterator.hasNext());
        assertEquals(convertToSequence("romulus"), tailMap.firstKey());
        assertEquals(convertToSequence("rubicundus"), tailMap.lastKey());
    }

    @Test
    public final void testTailMap2() {
        testPutWithEmptyKey();
        NavigableMap<SequenceType, String> tailMap = trie
                .tailMap(convertToSequence("romulus"), false);
        assertEquals(5, tailMap.size());
        Iterator<Map.Entry<SequenceType, String>> iterator = tailMap.entrySet().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("rubens"), "rubens"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("ruber"), "ruber"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(
                new AbstractMap.SimpleImmutableEntry<>(convertToSequence("rubicon"), "rubicon"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(
                new AbstractMap.SimpleImmutableEntry<>(convertToSequence("rubicun"), "rubicun"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("rubicundus"),
                        "rubicundus"),
                iterator.next());
        assertFalse(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("rubens"), "rubens"),
                tailMap.firstEntry());
        assertEquals(convertToSequence("rubens"), tailMap.firstKey());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("rubicundus"),
                        "rubicundus"),
                tailMap.lastEntry());
        assertEquals(convertToSequence("rubicundus"), tailMap.lastKey());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("ruber"), "ruber"),
                tailMap.higherEntry(convertToSequence("rubens")));
        assertEquals(convertToSequence("ruber"), tailMap.higherKey(convertToSequence("rubens")));
        assertEquals(
                new AbstractMap.SimpleImmutableEntry<>(convertToSequence("rubicun"), "rubicun"),
                tailMap.lowerEntry(convertToSequence("rubicundus")));
        assertEquals(convertToSequence("rubicun"),
                tailMap.lowerKey(convertToSequence("rubicundus")));
        assertNull(tailMap.lowerEntry(convertToSequence("rubens")));
        assertNull(tailMap.lowerKey(convertToSequence("rubens")));
    }

    @Test
    public final void testTailMapRemove() {
        testPut3();
        NavigableMap<SequenceType, String> subMap = trie.tailMap(convertToSequence("rom"), true);
        assertEquals(3, subMap.size());
        Iterator<Map.Entry<SequenceType, String>> iterator = subMap.entrySet().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("rom"), "rom"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("romane"), "romane"),
                iterator.next());
        iterator.remove();
        assertTrue(iterator.hasNext());
        assertEquals(
                new AbstractMap.SimpleImmutableEntry<>(convertToSequence("romanus"), "romanus"),
                iterator.next());
        assertFalse(iterator.hasNext());
        assertEquals(2, trie.size());
        assertTrue(trie.containsKey(convertToSequence("rom")));
        assertFalse(trie.containsKey(convertToSequence("romane")));
        assertTrue(trie.containsKey(convertToSequence("romanus")));
    }

    @Test
    public final void testDescendingMap() {
        testPutWithEmptyKey();
        NavigableMap<SequenceType, String> map = trie.descendingMap();
        assertEquals(trie.size(), map.size());
        Iterator<Map.Entry<SequenceType, String>> iterator = map.entrySet().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("rubicundus"),
                        "rubicundus"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(
                new AbstractMap.SimpleImmutableEntry<>(convertToSequence("rubicun"), "rubicun"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(
                new AbstractMap.SimpleImmutableEntry<>(convertToSequence("rubicon"), "rubicon"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("ruber"), "ruber"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("rubens"), "rubens"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(
                new AbstractMap.SimpleImmutableEntry<>(convertToSequence("romulus"), "romulus"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(
                new AbstractMap.SimpleImmutableEntry<>(convertToSequence("romanus"), "romanus"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("romane"), "romane"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("rom"), "rom"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("B"), "romulus"),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(convertToSequence("A"), null),
                iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new AbstractMap.SimpleImmutableEntry<>(null, "empty"),
                iterator.next());
        assertFalse(iterator.hasNext());
        trie.forEach((key, value) -> assertEquals(value, map.get(key)));
        assertEquals(trie.firstKey(), map.lastKey());
        assertEquals(trie.higherKey(trie.firstKey()), map.lowerKey(map.lastKey()));
        assertEquals(trie.lastKey(), map.firstKey());
        assertEquals(trie.lowerKey(trie.lastKey()), map.higherKey(map.firstKey()));
    }

    @Test
    public final void testNavigableKeySet() {
        testPutWithEmptyKey();
        NavigableSet<SequenceType> keySet = trie.navigableKeySet();
        assertEquals(12, keySet.size());
        Iterator<SequenceType> iterator = keySet.iterator();
        assertTrue(iterator.hasNext());
        assertNull(iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("A"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("B"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("rom"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("romane"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("romanus"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("romulus"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("rubens"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("ruber"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("rubicon"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("rubicun"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("rubicundus"), iterator.next());
        assertFalse(iterator.hasNext());
        assertNull(keySet.first());
        assertEquals(convertToSequence("rubicundus"), keySet.last());
        assertEquals(convertToSequence("A"), keySet.higher(null));
        assertEquals(convertToSequence("rubicun"), keySet.lower(convertToSequence("rubicundus")));
    }

    @Test
    public final void testNavigableKeySetDescendingIterator() {
        testPutWithEmptyKey();
        NavigableSet<SequenceType> keySet = trie.navigableKeySet();
        assertEquals(12, keySet.size());
        Iterator<SequenceType> iterator = keySet.descendingIterator();
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("rubicundus"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("rubicun"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("rubicon"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("ruber"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("rubens"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("romulus"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("romanus"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("romane"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("rom"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("B"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("A"), iterator.next());
        assertTrue(iterator.hasNext());
        assertNull(iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public final void testNavigableKeySetSpliterator() {
        testPutWithEmptyKey();
        NavigableSet<SequenceType> keySet = trie.navigableKeySet();
        assertEquals(12, keySet.size());
        Spliterator<SequenceType> spliterator = keySet.spliterator();
        Collection<SequenceType> keys = new LinkedList<>();
        spliterator.forEachRemaining(keys::add);
        Iterator<SequenceType> iterator = keys.iterator();
        assertNull(iterator.next());
        assertEquals(convertToSequence("A"), iterator.next());
        assertEquals(convertToSequence("B"), iterator.next());
        assertEquals(convertToSequence("rom"), iterator.next());
        assertEquals(convertToSequence("romane"), iterator.next());
        assertEquals(convertToSequence("romanus"), iterator.next());
        assertEquals(convertToSequence("romulus"), iterator.next());
        assertEquals(convertToSequence("rubens"), iterator.next());
        assertEquals(convertToSequence("ruber"), iterator.next());
        assertEquals(convertToSequence("rubicon"), iterator.next());
        assertEquals(convertToSequence("rubicun"), iterator.next());
        assertEquals(convertToSequence("rubicundus"), iterator.next());
    }

    @Test
    public final void testDescendingNavigableKeySet() {
        testPutWithEmptyKey();
        NavigableSet<SequenceType> keySet = trie.descendingKeySet();
        assertEquals(12, keySet.size());
        Iterator<SequenceType> iterator = keySet.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("rubicundus"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("rubicun"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("rubicon"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("ruber"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("rubens"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("romulus"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("romanus"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("romane"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("rom"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("B"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("A"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(null, iterator.next());
        assertFalse(iterator.hasNext());
        assertEquals(convertToSequence("rubicundus"), keySet.first());
        assertNull(keySet.last());
        assertEquals(convertToSequence("rubicun"), keySet.higher(convertToSequence("rubicundus")));
        assertEquals(convertToSequence("A"), keySet.lower(null));
    }

    @Test
    public final void testDescendingNavigableKeySetDescendingIterator() {
        testPutWithEmptyKey();
        NavigableSet<SequenceType> keySet = trie.descendingKeySet();
        assertEquals(12, keySet.size());
        Iterator<SequenceType> iterator = keySet.descendingIterator();
        assertTrue(iterator.hasNext());
        assertNull(iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("A"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("B"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("rom"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("romane"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("romanus"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("romulus"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("rubens"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("ruber"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("rubicon"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("rubicun"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(convertToSequence("rubicundus"), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public final void testNavigableKeySetOfTailMapSpliterator() {
        testPutWithEmptyKey();
        NavigableSet<SequenceType> keySet = trie.tailMap(null, true).navigableKeySet();
        assertEquals(12, keySet.size());
        Spliterator<SequenceType> spliterator = keySet.spliterator();
        Collection<SequenceType> keys = new LinkedList<>();
        spliterator.forEachRemaining(keys::add);
        Iterator<SequenceType> iterator = keys.iterator();
        assertNull(iterator.next());
        assertEquals(convertToSequence("A"), iterator.next());
        assertEquals(convertToSequence("B"), iterator.next());
        assertEquals(convertToSequence("rom"), iterator.next());
        assertEquals(convertToSequence("romane"), iterator.next());
        assertEquals(convertToSequence("romanus"), iterator.next());
        assertEquals(convertToSequence("romulus"), iterator.next());
        assertEquals(convertToSequence("rubens"), iterator.next());
        assertEquals(convertToSequence("ruber"), iterator.next());
        assertEquals(convertToSequence("rubicon"), iterator.next());
        assertEquals(convertToSequence("rubicun"), iterator.next());
        assertEquals(convertToSequence("rubicundus"), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public final void testDescendingNavigableKeySetSpliterator() {
        testPutWithEmptyKey();
        NavigableSet<SequenceType> keySet = trie.descendingKeySet();
        assertEquals(12, keySet.size());
        Spliterator<SequenceType> spliterator = keySet.spliterator();
        Collection<SequenceType> keys = new LinkedList<>();
        spliterator.forEachRemaining(keys::add);
        Iterator<SequenceType> iterator = keys.iterator();
        assertEquals(convertToSequence("rubicundus"), iterator.next());
        assertEquals(convertToSequence("rubicun"), iterator.next());
        assertEquals(convertToSequence("rubicon"), iterator.next());
        assertEquals(convertToSequence("ruber"), iterator.next());
        assertEquals(convertToSequence("rubens"), iterator.next());
        assertEquals(convertToSequence("romulus"), iterator.next());
        assertEquals(convertToSequence("romanus"), iterator.next());
        assertEquals(convertToSequence("romane"), iterator.next());
        assertEquals(convertToSequence("rom"), iterator.next());
        assertEquals(convertToSequence("B"), iterator.next());
        assertEquals(convertToSequence("A"), iterator.next());
        assertNull(iterator.next());
        assertFalse(iterator.hasNext());
    }

}
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

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Tests the functionality of the class {@link PatriciaTrie}.
 *
 * @author Michael Rapp
 */
public class PatriciaTrieTest
        extends AbstractPatriciaTrieTest<StringSequence, PatriciaTrie<StringSequence, String>> {

    @Override
    final PatriciaTrie<StringSequence, String> onCreateTrie() {
        return new PatriciaTrie<>();
    }

    @Override
    final StringSequence convertToSequence(@NotNull String string) {
        return new StringSequence(string);
    }

    @Override
    final Node<StringSequence, String> getRootNode(
            @NotNull final PatriciaTrie<StringSequence, String> trie) {
        return trie.getRootNode();
    }

    @Test
    public void testConstructorWithComparatorParameter() {
        Comparator<? super StringSequence> comparator = mock(Comparator.class);
        PatriciaTrie<StringSequence, String> trie = new PatriciaTrie<>(comparator);
        assertEquals(comparator, trie.comparator());
    }

    @Test
    public void testConstructorWithMapParameter() {
        String value1 = "foo";
        String value2 = "bar";
        Map<StringSequence, String> map = new HashMap<>();
        map.put(new StringSequence(value1), value1);
        map.put(new StringSequence(value2), value2);
        PatriciaTrie<StringSequence, String> trie = new PatriciaTrie<>(map);
        assertEquals(2, trie.size());
        assertEquals(value1, trie.get(new StringSequence(value1)));
        assertEquals(value2, trie.get(new StringSequence(value2)));
    }

    @Test
    public void testConstructorWithComparatorAndMapParameter() {
        String value1 = "foo";
        String value2 = "bar";
        Map<StringSequence, String> map = new HashMap<>();
        map.put(new StringSequence(value1), value1);
        map.put(new StringSequence(value2), value2);
        Comparator<? super StringSequence> comparator =
                (Comparator<StringSequence>) (o1, o2) -> o1.toString().compareTo(o2.toString());
        PatriciaTrie<StringSequence, String> trie = new PatriciaTrie<>(comparator, map);
        assertEquals(2, trie.size());
        assertEquals(value1, trie.get(new StringSequence(value1)));
        assertEquals(value2, trie.get(new StringSequence(value2)));
        assertEquals(comparator, trie.comparator());
    }

    @Test
    public final void testSubTrieWithEmptySequence() {
        testPutWithNullKey();
        Trie<StringSequence, String> subTrie = trie.subTrie(new StringSequence(""));
        assertFalse(subTrie.isEmpty());
        assertEquals(11, subTrie.size());
        assertNull("A", subTrie.get(new StringSequence("A")));
        assertEquals("romulus", subTrie.get(new StringSequence("B")));
        assertEquals("romane", subTrie.get(new StringSequence("romane")));
        assertEquals("romanus", subTrie.get(new StringSequence("romanus")));
        assertEquals("rom", subTrie.get(new StringSequence("rom")));
        assertEquals("romulus", subTrie.get(new StringSequence("romulus")));
        assertEquals("rubens", subTrie.get(new StringSequence("rubens")));
        assertEquals("ruber", subTrie.get(new StringSequence("ruber")));
        assertEquals("rubicon", subTrie.get(new StringSequence("rubicon")));
        assertEquals("rubicundus", subTrie.get(new StringSequence("rubicundus")));
        verifyRootNode(getRootNode(trie), "null");
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
        verifySuccessors(successorIc, "on", "un");
        Node<StringSequence, String> successorOn = getSuccessor(successorIc, "on");
        verifyLeaf(successorOn, "rubicon");
        Node<StringSequence, String> successorUn = getSuccessor(successorIc, "un");
        verifySuccessors(successorUn, "dus");
        Node<StringSequence, String> successorDus = getSuccessor(successorUn, "dus");
        verifyLeaf(successorDus, "rubicundus");
        Node<StringSequence, String> successorA = getSuccessor(getRootNode(trie), "A");
        verifyLeaf(successorA, null);
        Node<StringSequence, String> successorB = getSuccessor(getRootNode(trie), "B");
        verifyLeaf(successorB, "romulus");
    }

    @Test
    public final void testSubTrieWithNullSequence() {
        testPutWithNullKey();
        Trie<StringSequence, String> subTrie = trie.subTrie(null);
        assertFalse(subTrie.isEmpty());
        assertEquals(11, subTrie.size());
        assertNull("A", subTrie.get(new StringSequence("A")));
        assertEquals("romulus", subTrie.get(new StringSequence("B")));
        assertEquals("romane", subTrie.get(new StringSequence("romane")));
        assertEquals("romanus", subTrie.get(new StringSequence("romanus")));
        assertEquals("rom", subTrie.get(new StringSequence("rom")));
        assertEquals("romulus", subTrie.get(new StringSequence("romulus")));
        assertEquals("rubens", subTrie.get(new StringSequence("rubens")));
        assertEquals("ruber", subTrie.get(new StringSequence("ruber")));
        assertEquals("rubicon", subTrie.get(new StringSequence("rubicon")));
        assertEquals("rubicundus", subTrie.get(new StringSequence("rubicundus")));
        verifyRootNode(getRootNode(trie), "null");
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
        verifySuccessors(successorIc, "on", "un");
        Node<StringSequence, String> successorOn = getSuccessor(successorIc, "on");
        verifyLeaf(successorOn, "rubicon");
        Node<StringSequence, String> successorUn = getSuccessor(successorIc, "un");
        verifySuccessors(successorUn, "dus");
        Node<StringSequence, String> successorDus = getSuccessor(successorUn, "dus");
        verifyLeaf(successorDus, "rubicundus");
        Node<StringSequence, String> successorA = getSuccessor(getRootNode(trie), "A");
        verifyLeaf(successorA, null);
        Node<StringSequence, String> successorB = getSuccessor(getRootNode(trie), "B");
        verifyLeaf(successorB, "romulus");
    }

    @Test
    public final void testSubTrieIfSequenceCorrespondsToNode() {
        testPutWithNullKey();
        SortedTrie<StringSequence, String> subTrie = trie.subTrie(new StringSequence("rom"));
        assertFalse(subTrie.isEmpty());
        assertEquals(3, subTrie.size());
        assertEquals("romane", subTrie.get(new StringSequence("romane")));
        assertEquals("romanus", subTrie.get(new StringSequence("romanus")));
        assertEquals("romulus", subTrie.get(new StringSequence("romulus")));
        verifyRootNode(subTrie.getRootNode());
        verifySuccessors(subTrie.getRootNode(), "rom");
        Node<StringSequence, String> romSuccessor = getSuccessor(subTrie.getRootNode(), "rom");
        verifySuccessors(romSuccessor, "an", "ulus");
        Node<StringSequence, String> anSuccessor = getSuccessor(romSuccessor, "an");
        verifySuccessors(anSuccessor, "e", "us");
        Node<StringSequence, String> leaf = getSuccessor(anSuccessor, "e");
        verifyLeaf(leaf, "romane");
        leaf = getSuccessor(anSuccessor, "us");
        verifyLeaf(leaf, "romanus");
        leaf = getSuccessor(romSuccessor, "ulus");
        verifyLeaf(leaf, "romulus");
    }

    @Test
    public void testToString() {
        testPut3();
        assertEquals("PatriciaTrie [rom=rom, romane=romane, romanus=romanus]", trie.toString());
    }

}
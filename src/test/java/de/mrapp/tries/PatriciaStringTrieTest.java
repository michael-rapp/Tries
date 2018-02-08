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

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests the functionality of the class {@link PatriciaStringTrie}.
 *
 * @author Michael Rapp
 */
public class PatriciaStringTrieTest extends AbstractPatriciaTrieTest<String, PatriciaStringTrie<String>> {

    @Override
    final PatriciaStringTrie<String> onCreateTrie() {
        return new PatriciaStringTrie<>();
    }

    @Override
    final String convertToSequence(@NotNull final String string) {
        return string;
    }

    @Override
    final Node<String, String> getRootNode(@NotNull final PatriciaStringTrie<String> trie) {
        return trie.getRootNode();
    }

    @Test
    public final void testDefaultConstructor() {
        PatriciaStringTrie<String> trie = new PatriciaStringTrie<>();
        assertNull(trie.comparator());
    }

    @Test
    public void testConstructorWithMapParameter() {
        String value1 = "foo";
        String value2 = "bar";
        Map<String, String> map = new HashMap<>();
        map.put(value1, value1);
        map.put(value2, value2);
        PatriciaStringTrie<String> trie = new PatriciaStringTrie<>(map);
        assertEquals(2, trie.size());
        assertEquals(value1, trie.get(value1));
        assertEquals(value2, trie.get(value2));
    }

    @Test
    public final void testConstructorWithComparatorParameter() {
        Comparator<? super String> comparator = mock(Comparator.class);
        PatriciaStringTrie<String> trie = new PatriciaStringTrie<>(comparator);
        assertNotNull(trie.comparator());
        when(comparator.compare("a", "b")).thenReturn(-1);
        assertEquals(-1, trie.comparator().compare("a", "b"));
    }

    @Test
    public void testConstructorWithComparatorAndMapParameter() {
        String value1 = "foo";
        String value2 = "bar";
        Map<String, String> map = new HashMap<>();
        map.put(value1, value1);
        map.put(value2, value2);
        Comparator<? super String> comparator = (Comparator<String>) (o1, o2) -> o1.compareTo(o2);
        PatriciaStringTrie<String> trie = new PatriciaStringTrie<>(comparator, map);
        assertEquals(2, trie.size());
        assertEquals(value1, trie.get(value1));
        assertEquals(value2, trie.get(value2));
        assertNotNull(trie.comparator());
    }

    @Test
    public final void testSubTrieWithEmptySequence() {
        testPutWithNullKey();
        SortedStringTrie<String> subTrie = trie.subTrie("");
        assertFalse(subTrie.isEmpty());
        assertEquals(11, subTrie.size());
        assertNull("A", subTrie.get("A"));
        assertEquals("romulus", subTrie.get("B"));
        assertEquals("romane", subTrie.get("romane"));
        assertEquals("romanus", subTrie.get("romanus"));
        assertEquals("rom", subTrie.get("rom"));
        assertEquals("romulus", subTrie.get("romulus"));
        assertEquals("rubens", subTrie.get("rubens"));
        assertEquals("ruber", subTrie.get("ruber"));
        assertEquals("rubicon", subTrie.get("rubicon"));
        assertEquals("rubicundus", subTrie.get("rubicundus"));
        verifyRootNode(getRootNode(trie), "null");
        verifySuccessors(getRootNode(trie), "r", "A", "B");
        Node<String, String> successor = getSuccessor(getRootNode(trie), "r");
        verifySuccessors(successor, "om", "ub");
        Node<String, String> successorOm = getSuccessor(successor, "om");
        verifySuccessors(successorOm, "an", "ulus");
        Node<String, String> successorAn = getSuccessor(successorOm, "an");
        verifySuccessors(successorAn, "e", "us");
        Node<String, String> successorE = getSuccessor(successorAn, "e");
        verifyLeaf(successorE, "romane");
        Node<String, String> successorUs = getSuccessor(successorAn, "us");
        verifyLeaf(successorUs, "romanus");
        Node<String, String> successorUlus = getSuccessor(successorOm, "ulus");
        verifyLeaf(successorUlus, "romulus");
        Node<String, String> successorUb = getSuccessor(successor, "ub");
        verifySuccessors(successorUb, "e", "ic");
        Node<String, String> successorE2 = getSuccessor(successorUb, "e");
        verifySuccessors(successorE2, "ns", "r");
        Node<String, String> successorNs = getSuccessor(successorE2, "ns");
        verifyLeaf(successorNs, "rubens");
        Node<String, String> successorR = getSuccessor(successorE2, "r");
        verifyLeaf(successorR, "ruber");
        Node<String, String> successorIc = getSuccessor(successorUb, "ic");
        verifySuccessors(successorIc, "on", "un");
        Node<String, String> successorOn = getSuccessor(successorIc, "on");
        verifyLeaf(successorOn, "rubicon");
        Node<String, String> successorUn = getSuccessor(successorIc, "un");
        verifySuccessors(successorUn, "dus");
        Node<String, String> successorDus = getSuccessor(successorUn, "dus");
        verifyLeaf(successorDus, "rubicundus");
        Node<String, String> successorA = getSuccessor(getRootNode(trie), "A");
        verifyLeaf(successorA, null);
        Node<String, String> successorB = getSuccessor(getRootNode(trie), "B");
        verifyLeaf(successorB, "romulus");
    }

    @Test
    public final void testSubTrieWithNullSequence() {
        testPutWithNullKey();
        SortedStringTrie<String> subTrie = trie.subTrie(null);
        assertFalse(subTrie.isEmpty());
        assertEquals(11, subTrie.size());
        assertNull("A", subTrie.get("A"));
        assertEquals("romulus", subTrie.get("B"));
        assertEquals("romane", subTrie.get("romane"));
        assertEquals("romanus", subTrie.get("romanus"));
        assertEquals("rom", subTrie.get("rom"));
        assertEquals("romulus", subTrie.get("romulus"));
        assertEquals("rubens", subTrie.get("rubens"));
        assertEquals("ruber", subTrie.get("ruber"));
        assertEquals("rubicon", subTrie.get("rubicon"));
        assertEquals("rubicundus", subTrie.get("rubicundus"));
        verifyRootNode(getRootNode(trie), "null");
        verifySuccessors(getRootNode(trie), "r", "A", "B");
        Node<String, String> successor = getSuccessor(getRootNode(trie), "r");
        verifySuccessors(successor, "om", "ub");
        Node<String, String> successorOm = getSuccessor(successor, "om");
        verifySuccessors(successorOm, "an", "ulus");
        Node<String, String> successorAn = getSuccessor(successorOm, "an");
        verifySuccessors(successorAn, "e", "us");
        Node<String, String> successorE = getSuccessor(successorAn, "e");
        verifyLeaf(successorE, "romane");
        Node<String, String> successorUs = getSuccessor(successorAn, "us");
        verifyLeaf(successorUs, "romanus");
        Node<String, String> successorUlus = getSuccessor(successorOm, "ulus");
        verifyLeaf(successorUlus, "romulus");
        Node<String, String> successorUb = getSuccessor(successor, "ub");
        verifySuccessors(successorUb, "e", "ic");
        Node<String, String> successorE2 = getSuccessor(successorUb, "e");
        verifySuccessors(successorE2, "ns", "r");
        Node<String, String> successorNs = getSuccessor(successorE2, "ns");
        verifyLeaf(successorNs, "rubens");
        Node<String, String> successorR = getSuccessor(successorE2, "r");
        verifyLeaf(successorR, "ruber");
        Node<String, String> successorIc = getSuccessor(successorUb, "ic");
        verifySuccessors(successorIc, "on", "un");
        Node<String, String> successorOn = getSuccessor(successorIc, "on");
        verifyLeaf(successorOn, "rubicon");
        Node<String, String> successorUn = getSuccessor(successorIc, "un");
        verifySuccessors(successorUn, "dus");
        Node<String, String> successorDus = getSuccessor(successorUn, "dus");
        verifyLeaf(successorDus, "rubicundus");
        Node<String, String> successorA = getSuccessor(getRootNode(trie), "A");
        verifyLeaf(successorA, null);
        Node<String, String> successorB = getSuccessor(getRootNode(trie), "B");
        verifyLeaf(successorB, "romulus");
    }

    @Test
    public final void testSubTrieIfSequenceCorrespondsToNode() {
        testPutWithNullKey();
        SortedStringTrie<String> subTrie = trie.subTrie("rom");
        assertFalse(subTrie.isEmpty());
        assertEquals(3, subTrie.size());
        assertEquals("romane", subTrie.get("romane"));
        assertEquals("romanus", subTrie.get("romanus"));
        assertEquals("romulus", subTrie.get("romulus"));
        verifyRootNode(subTrie.getRootNode());
        verifySuccessors(subTrie.getRootNode(), "rom");
        Node<String, String> romSuccessor = getSuccessor(subTrie.getRootNode(), "rom");
        verifySuccessors(romSuccessor, "an", "ulus");
        Node<String, String> anSuccessor = getSuccessor(romSuccessor, "an");
        verifySuccessors(anSuccessor, "e", "us");
        Node<String, String> leaf = getSuccessor(anSuccessor, "e");
        verifyLeaf(leaf, "romane");
        leaf = getSuccessor(anSuccessor, "us");
        verifyLeaf(leaf, "romanus");
        leaf = getSuccessor(romSuccessor, "ulus");
        verifyLeaf(leaf, "romulus");
    }

    @Test
    public final void testSubTrie1() {
        testPutWithNullKey();
        SortedStringTrie<String> subTrie = trie.subTrie("roman");
        assertFalse(subTrie.isEmpty());
        assertEquals(2, subTrie.size());
        assertEquals("romane", subTrie.get("romane"));
        assertEquals("romanus", subTrie.get("romanus"));
        verifyRootNode(subTrie.getRootNode());
        verifySuccessors(subTrie.getRootNode(), "roman");
        Node<String, String> romanSuccessor = getSuccessor(subTrie.getRootNode(), "roman");
        verifySuccessors(romanSuccessor, "e", "us");
        Node<String, String> leaf = getSuccessor(romanSuccessor, "e");
        verifyLeaf(leaf, "romane");
        leaf = getSuccessor(romanSuccessor, "us");
        verifyLeaf(leaf, "romanus");
    }

    @Test
    public final void testSubTrie2() {
        testPutWithNullKey();
        SortedStringTrie<String> subTrie = trie.subTrie("roma");
        assertFalse(subTrie.isEmpty());
        assertEquals(2, subTrie.size());
        assertEquals("romane", subTrie.get("romane"));
        assertEquals("romanus", subTrie.get("romanus"));
        verifyRootNode(subTrie.getRootNode());
        verifySuccessors(subTrie.getRootNode(), "roman");
        Node<String, String> romanSuccessor = getSuccessor(subTrie.getRootNode(), "roman");
        verifySuccessors(romanSuccessor, "e", "us");
        Node<String, String> leaf = getSuccessor(romanSuccessor, "e");
        verifyLeaf(leaf, "romane");
        leaf = getSuccessor(romanSuccessor, "us");
        verifyLeaf(leaf, "romanus");
    }

    @Test
    public final void testSubTrie3() {
        testPutWithNullKey();
        SortedStringTrie<String> subTrie = trie.subTrie("ro");
        assertFalse(subTrie.isEmpty());
        assertEquals(3, subTrie.size());
        assertEquals("romane", subTrie.get("romane"));
        assertEquals("romanus", subTrie.get("romanus"));
        assertEquals("romulus", subTrie.get("romulus"));
        verifyRootNode(subTrie.getRootNode());
        verifySuccessors(subTrie.getRootNode(), "rom");
        Node<String, String> romSuccessor = getSuccessor(subTrie.getRootNode(), "rom");
        verifySuccessors(romSuccessor, "an", "ulus");
        Node<String, String> anSuccessor = getSuccessor(romSuccessor, "an");
        verifySuccessors(anSuccessor, "e", "us");
        Node<String, String> eSuccessor = getSuccessor(anSuccessor, "e");
        verifyLeaf(eSuccessor, "romane");
        Node<String, String> usSuccessor = getSuccessor(anSuccessor, "us");
        verifyLeaf(usSuccessor, "romanus");
        Node<String, String> ulusSuccessor = getSuccessor(romSuccessor, "ulus");
        verifyLeaf(ulusSuccessor, "romulus");
    }

    @Test
    public final void testSubTrie4() {
        trie.put("bacon", "bacon");
        trie.put("bagels", "bagels");
        trie.put("baguette", "baguette");
        trie.put("bananas", "bananas");
        SortedStringTrie<String> subTrie = trie.subTrie("bac");
        assertFalse(subTrie.isEmpty());
        assertEquals(1, subTrie.size());
        assertEquals("bacon", subTrie.get("bacon"));
        verifyRootNode(subTrie.getRootNode());
        verifySuccessors(subTrie.getRootNode(), "bacon");
        Node<String, String> baconSuccessor = getSuccessor(subTrie.getRootNode(), "bacon");
        verifyLeaf(baconSuccessor, "bacon");
    }

    @Test
    public final void testSubTrie5() {
        trie.put("icetea", "icetea");
        trie.put("ice cream", "ice cream");
        SortedStringTrie<String> subTrie = trie.subTrie("ic");
        assertFalse(subTrie.isEmpty());
        assertEquals(2, subTrie.size());
        assertEquals("icetea", subTrie.get("icetea"));
        assertEquals("ice cream", subTrie.get("ice cream"));
        verifyRootNode(subTrie.getRootNode());
        verifySuccessors(subTrie.getRootNode(), "ice");
        Node<String, String> iceSuccessor = getSuccessor(subTrie.getRootNode(), "ice");
        verifySuccessors(iceSuccessor, "tea", " cream");
        Node<String, String> teaSuccessor = getSuccessor(iceSuccessor, "tea");
        verifyLeaf(teaSuccessor, "icetea");
        Node<String, String> creamSuccessor = getSuccessor(iceSuccessor, " cream");
        verifyLeaf(creamSuccessor, "ice cream");
    }

    @Test(expected = NoSuchElementException.class)
    public final void testSubTrieIfSequenceIsNotContained1() {
        testPutWithNullKey();
        trie.subTrie("romax");
    }

    @Test(expected = NoSuchElementException.class)
    public final void testSubTrieIfSequenceIsNotContained2() {
        trie.put("icetea", "icetea");
        trie.put("ice cream", "ice cream");
        trie.subTrie("ix");
    }

    @Test
    public final void testSubTrieIsEmpty() {
        testPutWithNullKey();
        SortedStringTrie<String> subTrie = trie.subTrie("rubicundus");
        assertTrue(subTrie.isEmpty());
        assertEquals(0, subTrie.size());
        assertNull(subTrie.getRootNode());
    }

    @Test
    public final void testToString() {
        PatriciaStringTrie<String> trie = new PatriciaStringTrie<>();
        trie.put("key1", "value1");
        trie.put("key2", "value2");
        assertEquals("PatriciaStringTrie [key1=value1, key2=value2]", trie.toString());
    }

}
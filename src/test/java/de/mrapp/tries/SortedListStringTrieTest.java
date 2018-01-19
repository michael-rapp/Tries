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

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests the functionality of the class {@link SortedListStringTrie}.
 *
 * @author Michael Rapp
 */
public class SortedListStringTrieTest
        extends AbstractStringNonPatriciaSortedTrieTest<SortedListStringTrie<String>> {

    @Override
    final SortedListStringTrie<String> onCreateTrie() {
        return new SortedListStringTrie<>();
    }

    @Test
    public final void testDefaultConstructor() {
        SortedListStringTrie<String> trie = new SortedListStringTrie<>();
        assertNull(trie.comparator());
    }

    @Test
    public void testConstructorWithMapParameter() {
        String value1 = "foo";
        String value2 = "bar";
        Map<String, String> map = new HashMap<>();
        map.put(value1, value1);
        map.put(value2, value2);
        SortedStringTrie<String> trie = new SortedListStringTrie<>(map);
        assertEquals(2, trie.size());
        assertEquals(value1, trie.get(value1));
        assertEquals(value2, trie.get(value2));
    }

    @Test
    public final void testConstructorWithComparatorParameter() {
        Comparator<? super String> comparator = mock(Comparator.class);
        SortedListStringTrie<String> trie = new SortedListStringTrie<>(comparator);
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
        SortedStringTrie<String> trie = new SortedListStringTrie<>(comparator, map);
        assertEquals(2, trie.size());
        assertEquals(value1, trie.get(value1));
        assertEquals(value2, trie.get(value2));
        assertNotNull(trie.comparator());
    }

    @Test
    public final void testSubTrie1() {
        testPutWithNullKey();
        SortedStringTrie<String> subTrie = trie.subTrie("t");
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
        SortedStringTrie<String> subTrie = trie.subTrie("te");
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
        SortedListStringTrie<String> trie = new SortedListStringTrie<>();
        trie.put("key1", "value1");
        trie.put("key2", "value2");
        assertEquals("SortedListStringTrie [key1=value1, key2=value2]", trie.toString());
    }

}
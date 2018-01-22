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
    public final void testSubTrieIfSequenceCorrespondsToNode() {
        testPutWithNullKey();
        SortedTrie<StringSequence, String> subTrie = trie.subTrie(new StringSequence("rom"));
        assertFalse(subTrie.isEmpty());
        assertEquals(4, subTrie.size());
        assertEquals("rom", subTrie.get(new StringSequence("rom")));
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

    // TODO: test subTrie method

}
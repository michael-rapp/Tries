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
public class PatriciaTrieTest extends AbstractPatriciaTrieTest<StringSequence, PatriciaTrie<StringSequence, String>> {

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
    public final void testHashCode() {
        PatriciaTrie<StringSequence, String> trie1 = new PatriciaTrie<>();
        PatriciaTrie<StringSequence, String> trie2 = new PatriciaTrie<>();
        assertEquals(trie1.hashCode(), trie1.hashCode());
        assertEquals(trie1.hashCode(), trie2.hashCode());
        trie1.put(convertToSequence("foo"), "value");
        assertNotEquals(trie1.hashCode(), trie2.hashCode());
        trie2.put(convertToSequence("foo"), "value");
        assertEquals(trie1.hashCode(), trie2.hashCode());
        trie1.put(convertToSequence("fob"), "value2");
        assertNotEquals(trie1.hashCode(), trie2.hashCode());
    }

    @Test
    public final void testEquals() {
        PatriciaTrie<StringSequence, String> trie1 = new PatriciaTrie<>();
        PatriciaTrie<StringSequence, String> trie2 = new PatriciaTrie<>();
        assertFalse(trie1.equals(null));
        assertFalse(trie1.equals(new Object()));
        assertTrue(trie1.equals(trie1));
        assertTrue(trie1.equals(trie2));
        trie1.put(convertToSequence("foo"), "value");
        assertFalse(trie1.equals(trie2));
        trie2.put(convertToSequence("foo"), "value");
        assertTrue(trie1.equals(trie2));
        trie1.put(convertToSequence("fob"), "value2");
        assertFalse(trie1.equals(trie2));
    }

}
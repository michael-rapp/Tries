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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests the functionality of the class {@link StringSortedListTrie}.
 *
 * @author Michael Rapp
 */
public class StringSortedListTrieTest extends
        AbstractStringNonPatriciaSortedTrieTest<StringSortedListTrie<String>> {

    @Override
    final StringSortedListTrie<String> onCreateTrie() {
        return new StringSortedListTrie<>();
    }

    @Test
    public final void testDefaultConstructor() {
        StringSortedListTrie<String> trie = new StringSortedListTrie<>();
        assertNull(trie.comparator());
    }

    @Test
    public void testConstructorWithMapParameter() {
        String value1 = "foo";
        String value2 = "bar";
        Map<String, String> map = new HashMap<>();
        map.put(value1, value1);
        map.put(value2, value2);
        SortedStringTrie<String> trie = new StringSortedListTrie<>(map);
        assertEquals(2, trie.size());
        assertEquals(value1, trie.get(value1));
        assertEquals(value2, trie.get(value2));
    }

    @Test
    public final void testConstructorWithComparatorParameter() {
        Comparator<? super String> comparator = mock(Comparator.class);
        StringSortedListTrie<String> trie = new StringSortedListTrie<>(comparator);
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
        SortedStringTrie<String> trie = new StringSortedListTrie<>(comparator, map);
        assertEquals(2, trie.size());
        assertEquals(value1, trie.get(value1));
        assertEquals(value2, trie.get(value2));
        assertNotNull(trie.comparator());
    }

    @Test
    public final void testToString() {
        StringSortedListTrie<String> trie = new StringSortedListTrie<>();
        trie.put("key1", "value1");
        trie.put("key2", "value2");
        assertEquals("StringSortedListTrie [key1=value1, key2=value2]", trie.toString());
    }

}
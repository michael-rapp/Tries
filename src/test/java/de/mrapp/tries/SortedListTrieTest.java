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
import org.junit.Test;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests the functionality of the class {@link SortedListTrie}.
 *
 * @author Michael Rapp
 */
public class SortedListTrieTest extends
        AbstractStringSequenceNonPatriciaSortedTrieTest<SortedListTrie<StringSequence, String>> {

    @Override
    final SortedListTrie<StringSequence, String> onCreateTrie() {
        return new SortedListTrie<>();
    }

    @Test
    public void testConstructorWithComparatorParameter() {
        Comparator<? super StringSequence> comparator = mock(Comparator.class);
        SortedTrie<StringSequence, String> trie = new SortedListTrie<>(comparator);
        assertEquals(comparator, trie.comparator());
    }

    @Test
    public void testConstructorWithMapParameter() {
        String value1 = "foo";
        String value2 = "bar";
        Map<StringSequence, String> map = new HashMap<>();
        map.put(new StringSequence(value1), value1);
        map.put(new StringSequence(value2), value2);
        SortedTrie<StringSequence, String> trie = new SortedListTrie<>(map);
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
        Comparator<? super StringSequence> comparator = (Comparator<StringSequence>) (o1, o2) -> o1
                .toString().compareTo(o2.toString());
        SortedTrie<StringSequence, String> trie = new SortedListTrie<>(comparator, map);
        assertEquals(2, trie.size());
        assertEquals(value1, trie.get(new StringSequence(value1)));
        assertEquals(value2, trie.get(new StringSequence(value2)));
        assertEquals(comparator, trie.comparator());
    }

    @Test
    public void testToString() {
        testPut3();
        assertEquals("SortedListTrie [tea=tea, ted=ted, to=to]", trie.toString());
    }

}
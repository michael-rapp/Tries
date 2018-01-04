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

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests the functionality of the class {@link StringSortedListTrie}.
 *
 * @author Michael Rapp
 */
public class StringSortedListTrieTest {

    @Test
    public final void testDefaultConstructor() {
        StringSortedListTrie<String> trie = new StringSortedListTrie<>();
        assertNull(trie.comparator());
    }

    @Test
    public final void testConstructorWithComparatorParameter() {
        Comparator<? super String> comparator = mock(Comparator.class);
        StringSortedListTrie<String> trie = new StringSortedListTrie<>(comparator);
        Comparator<? super String> comparator2 = trie.comparator();
        assertNotNull(comparator);
        when(comparator.compare("a", "b")).thenReturn(-1);
        assertEquals(-1, comparator.compare("a", "b"));
    }

    @Test
    public final void testToString() {
        StringSortedListTrie<String> trie = new StringSortedListTrie<>();
        trie.put("key1", "value1");
        trie.put("key2", "value2");
        assertEquals("StringSortedListTrie [key1=value1, key2=value2]", trie.toString());
    }

}
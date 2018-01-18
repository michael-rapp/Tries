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

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests the functionality of the class {@link StringPatriciaTrie}.
 *
 * @author Michael Rapp
 */
public class StringPatriciaTrieTest extends AbstractPatriciaTrieTest<String, StringPatriciaTrie<String>> {

    @Override
    final StringPatriciaTrie<String> onCreateTrie() {
        return new StringPatriciaTrie<>();
    }

    @Override
    final String convertToSequence(@NotNull final String string) {
        return string;
    }

    @Override
    final Node<String, String> getRootNode(@NotNull final StringPatriciaTrie<String> trie) {
        return trie.getRootNode();
    }

    @Test
    public final void testDefaultConstructor() {
        StringPatriciaTrie<String> trie = new StringPatriciaTrie<>();
        assertNull(trie.comparator());
    }

    @Test
    public final void testConstructorWithComparatorParameter() {
        Comparator<? super String> comparator = mock(Comparator.class);
        StringPatriciaTrie<String> trie = new StringPatriciaTrie<>(comparator);
        assertNotNull(trie.comparator());
        when(comparator.compare("a", "b")).thenReturn(-1);
        assertEquals(-1, trie.comparator().compare("a", "b"));
    }

    // TODO: Test subTrie method

    @Test
    public final void testToString() {
        StringPatriciaTrie<String> trie = new StringPatriciaTrie<>();
        trie.put("key1", "value1");
        trie.put("key2", "value2");
        assertEquals("StringPatriciaTrie [key1=value1, key2=value2]", trie.toString());
    }

}
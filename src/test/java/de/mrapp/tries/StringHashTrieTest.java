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

import static org.junit.Assert.assertEquals;

/**
 * Tests the functionality of the class {@link StringHashTrie}.
 *
 * @author Michael Rapp
 */
public class StringHashTrieTest extends AbstractStringNonPatriciaTrieTest<StringHashTrie<String>> {

    @Override
    StringHashTrie<String> onCreateTrie() {
        return new StringHashTrie<>();
    }

    @Test
    public final void testToString() {
        testPut3();
        assertEquals("StringHashTrie [tea=tea, ted=ted, to=to]", trie.toString());
    }

}
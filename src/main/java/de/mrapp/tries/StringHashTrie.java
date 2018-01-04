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

import de.mrapp.tries.datastructure.StringTrieWrapper;

/**
 * An unsorted trie, which uses hash maps for storing the successors of nodes. It is the pendant of
 * the class {@link HashTrie} for using character sequences as keys. This trie implementation has
 * the same properties as a {@link HashTrie}. It should be preferred when using character sequences,
 * because it offers a less complex API.
 *
 * @param <ValueType> The type of the values, which are stored by the trie
 * @author Michael Rapp
 * @since 1.0.0
 */
public class StringHashTrie<ValueType> extends StringTrieWrapper<ValueType> {

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = -7644211622938905272L;

    /**
     * Creates a new empty, unsorted trie for storing character sequences, which uses hash maps for
     * storing the successors of nodes.
     */
    public StringHashTrie() {
        super(new HashTrie<>());
    }

    @Override
    public final String toString() {
        return "StringHashTrie " + entrySet().toString();
    }

}
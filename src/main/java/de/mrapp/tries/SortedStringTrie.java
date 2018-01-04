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

import java.util.NavigableMap;

/**
 * Defines the interface of a sorted trie, which uses character sequences, represented by the class
 * {@link String}, as keys. This is the character-oriented pendant of the interface {@link
 * SortedTrie} and a specialization of the interface {@link StringTrie} for implementations that
 * guarantee the trie's keys to be stored in a particular order. When iterating the trie, its
 * entries, keys and values are guaranteed to be traversed in the correct order.
 *
 * @param <ValueType> The type of the values, which are stored by trie
 * @author Michael Rapp
 * @since 1.0.0
 */
public interface SortedStringTrie<ValueType> extends NavigableMap<String, ValueType>,
        StringTrie<ValueType> {

    /**
     * @see StringTrie#subTrie(String)
     */
    @NotNull
    @Override
    SortedStringTrie<ValueType> subTrie(@NotNull final String sequence);

}
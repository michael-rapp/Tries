/*
 * Copyright 2017 Michael Rapp
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
 * Defines the interface of a sorted trie. This is a specialization of the interface {@link Trie}
 * for implementations that guarantee the trie's keys to be stored in a particular order. When
 * iterating the trie, its entries, keys and values are guaranteed to be traversed in the correct
 * order.
 *
 * To specify the order of the sequences, which are used as the trie's keys. The used sequence must
 * either implement the interface {@link java.lang.Comparable} or a {@link java.util.Comparator}
 * implementation must be provided to the trie.
 *
 * @param <SequenceType> The type of the sequences, which are used as the trie's keys
 * @param <ValueType>    The type of the values, which are stored by trie
 * @author Michael Rapp
 * @since 1.0.0
 */
public interface SortedTrie<SequenceType extends Sequence, ValueType>
        extends NavigableMap<SequenceType, ValueType>, Trie<SequenceType, ValueType> {

    /**
     * see {@link Trie#subTrie(Sequence)}
     */
    @NotNull
    @Override
    SortedTrie<SequenceType, ValueType> subTrie(@NotNull SequenceType sequence);

}
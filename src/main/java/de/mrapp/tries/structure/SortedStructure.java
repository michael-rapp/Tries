/*
 * Copyright 2017 - 2019 Michael Rapp
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
package de.mrapp.tries.structure;

import de.mrapp.tries.Node;
import de.mrapp.tries.Sequence;
import de.mrapp.util.datastructure.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Defines the interface, a class, which defines the structure of a sorted trie, must implement. In
 * addition to the method of the interface {@link Structure}, this interface also provides a method
 * for retrieving the index of the successor, which corresponds to a specific sequence.
 *
 * @param <K> The type of the sequences, which are used as the trie's keys
 * @param <V> The type of the values, which are stored by the trie
 * @author Michael Rapp
 * @since 1.0.0
 */
public interface SortedStructure<K extends Sequence, V> extends Structure<K, V> {


    /**
     * The method, which is invoked in order to identify the index of a node's successor, which
     * corresponds to a specific sequence.
     *
     * @param node     The node, whose successors should be checked, as an instance of the type
     *                 {@link Node}. The node may not be null
     * @param sequence The sequence, the successor, whose index should be returned, corresponds to,
     *                 as an {@link Integer} value
     * @return The index of the successor, which corresponds to the given sequence, as an {@link
     * Integer} value or -1, if no such successor is available for the given node
     */
    @Nullable Pair<Integer, K> indexOf(@NotNull Node<K, V> node, @NotNull final K sequence);

}
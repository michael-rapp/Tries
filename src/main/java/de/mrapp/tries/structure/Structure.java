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
 * Defines the interface, a class, which defines the structure of a trie, must implement. This interface provides
 * methods, which are invoked when inserting, removing or retrieving keys from a trie. By implementing these methods
 * accordingly, the structure of the trie can be adjusted.
 *
 * @param <SequenceType> The type of the sequences, which are used as the trie's keys
 * @param <ValueType>    The type of the values, which are stored by the trie
 * @author Michael Rapp
 * @since 1.0.0
 */
public interface Structure<SequenceType extends Sequence, ValueType> {

    /**
     * An enum, which contains all types of operations, which can be performed on a trie.
     */
    enum Operation {

        /**
         * An operation, which retrieves data from the trie.
         */
        GET,

        /**
         * An operation, which removes data from the trie.
         */
        REMOVE,

        /**
         * An operation, which puts data into the trie.
         */
        PUT,

        /**
         * An operation, which creates a subtrie of the trie.
         */
        SUB_TRIE

    }

    /**
     * The method, which is invoked in order to retrieve the successor of a specific node, which corresponds to a
     * specific sequence. Depending on the trie's structure, the suffix of the sequence can be processed to any extend.
     *
     * @param node      The node, whose successor should be returned, as an instance of the type {@link Node}. The node
     *                  may not be null
     * @param sequence  The sequence, the successor corresponds to, as an instance of the generic type {@link
     *                  SequenceType}. The sequence may neither be null, nor empty
     * @param operation The operation, the method invocation is part of, as a value of the enum {@link Operation}. The
     *                  operation may not be null
     * @return A pair, which contains the successor, which corresponds to the given sequence, as well as the suffix of
     * the sequence, depending on how far it has been processed, as an instance of the class {@link Pair} or null, if no
     * matching successor is available
     */
    @Nullable Pair<Node<SequenceType, ValueType>, SequenceType> onGetSuccessor(
            @NotNull Node<SequenceType, ValueType> node, @NotNull SequenceType sequence, @NotNull Operation operation);

    /**
     * The method, which is invoked in order to add a successor to a specific node. Depending on the trie's structure,
     * the given sequence can be processed to any extend.
     *
     * @param node     The node, the successor should be added to, as an instance of the type {@link Node}. The node may
     *                 not be null
     * @param sequence The sequence, the successor, which should be added, corresponds to, as an instance of the generic
     *                 type {@link SequenceType}. The sequence may neither be null, nor empty
     * @return A pair, which contains the successor, which has been created, as well as the suffix of the given
     * sequence, depending on how far it has been processed, as an instance of the class {@link Pair}. The pair may not
     * be null
     */
    @NotNull Pair<Node<SequenceType, ValueType>, SequenceType> onAddSuccessor(
            @NotNull Node<SequenceType, ValueType> node, @NotNull SequenceType sequence);

    /**
     * The method, which is invoked on subclasses in order to remove the successor, which corresponds to a specific
     * sequence, from a specific node. Depending on the trie's structure the given sequence can be processed to any
     * extend.
     *
     * @param node     The node, the successor should be removed from, as an instance of the type {@link Node}. The node
     *                 may not be null
     * @param sequence The sequence, the successor, which should be removed, corresponds to, as an instance of the
     *                 generic type {@link SequenceType}. The sequence may neither be null, nor empty
     */
    void onRemoveSuccessor(@NotNull Node<SequenceType, ValueType> node, @NotNull SequenceType sequence);

    /**
     * The method, which is invoked on subclasses, when the value of a not was deleted. This happens, when a key, which
     * corresponds to an inner node, has been removed. This method may be overridden by subclasses in order to adapt the
     * structure of the trie.
     *
     * @param node The node, whose values was deleted, as an instance of the type {@link Node}. The node may not be
     *             null
     */
    void onDeletedValue(@NotNull Node<SequenceType, ValueType> node);

    /**
     * Returns the subtree of the node, which corresponds to a specific sequence (must not necessarily be a key, which
     * is contained by the trie, but can also be a suffix).
     *
     * @param sequence         The sequence as an instance of the generic type {@link SequenceType}. The sequence may
     *                         not be null
     * @param rootNode         The root node of the subtree, which should be returned, as an instance of the type {@link
     *                         Node}. The root node may not be null
     * @param node             The node, which corresponds the given sequence as an instance of the type {@link Node}.
     *                         The node may not be null
     * @param includeNodeValue True, if the value of the given node should be included in the sub trie, false otherwise
     * @return The root node of the subtree as an instance of the type {@link Node}. The root node may not be null
     */
    @NotNull Node<SequenceType, ValueType> getSubTrie(@Nullable SequenceType sequence,
            @NotNull Node<SequenceType, ValueType> rootNode, @NotNull Node<SequenceType, ValueType> node,
            boolean includeNodeValue);

}
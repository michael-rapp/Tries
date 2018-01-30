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
package de.mrapp.tries.structure;

import de.mrapp.tries.Node;
import de.mrapp.tries.Sequence;
import de.mrapp.tries.util.SequenceUtil;
import de.mrapp.util.datastructure.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Defines the structure of an uncompressed sorted trie, where the edges between nodes always
 * correspond to exactly one element of a sequence.
 *
 * @param <SequenceType> The type of the sequences, which are used as the trie's keys
 * @param <ValueType>    The type of the values, which are stored by the trie
 * @author Michael Rapp
 * @since 1.0.0
 */
public class UncompressedSortedStructure<SequenceType extends Sequence, ValueType>
        extends UncompressedStructure<SequenceType, ValueType>
        implements SortedStructure<SequenceType, ValueType> {

    @Nullable
    @Override
    public final Pair<Integer, SequenceType> indexOf(
            @NotNull final Node<SequenceType, ValueType> node,
            @NotNull final SequenceType sequence) {
        SequenceType prefix = SequenceUtil.subsequence(sequence, 0, 1);
        int index = node.indexOf(prefix);

        if (index != -1) {
            SequenceType suffix = SequenceUtil.subsequence(sequence, 1);
            return Pair.create(index, suffix);
        }

        return null;
    }

}
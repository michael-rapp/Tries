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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Map;

/**
 * Defines the interface of a trie, which allows to use character sequences, represented by the
 * class {@link String}, as keys.
 *
 * This interface and its implementing classes should be preferred over the interface {@link Trie},
 * if character sequences should be stored in a trie. This is, because it offers a less complex API,
 * hiding the actual sequence implementation that is used internally (which is {@link
 * StringSequence}) and providing API methods that exclusively make use of the class {@link
 * String}.
 *
 * @param <ValueType> The type of the values, which are stored by trie
 * @author Michael Rapp
 * @since 1.0.0
 */
public interface StringTrie<ValueType> extends Map<String, ValueType>, Serializable {

    /**
     * @see Trie#getRootNode()
     */
    @Nullable
    Node<String, ValueType> getRootNode();

    /**
     * @see Trie#subTrie(Sequence)
     */
    @NotNull
    StringTrie<ValueType> subTrie(@NotNull String sequence);

}
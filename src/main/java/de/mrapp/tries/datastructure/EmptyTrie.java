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
package de.mrapp.tries.datastructure;

import de.mrapp.tries.Node;
import de.mrapp.tries.Sequence;
import de.mrapp.tries.Trie;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * An unmodifiable and empty trie.
 *
 * @param <SequenceType> The type of the sequences, which are used as the trie's keys
 * @param <ValueType>    The type of the values, which are stored by trie
 * @author Michael Rapp
 * @since 1.0.0
 */
public class EmptyTrie<SequenceType extends Sequence, ValueType> implements
        Trie<SequenceType, ValueType> {

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = 8066375897452793032L;

    @Override
    public final int size() {
        return 0;
    }

    @Override
    public final boolean isEmpty() {
        return true;
    }

    @Override
    public final boolean containsKey(final Object key) {
        return false;
    }

    @Override
    public final boolean containsValue(final Object value) {
        return false;
    }

    @Override
    public final ValueType get(final Object key) {
        return null;
    }

    @Override
    public final ValueType put(final SequenceType key, final ValueType value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final ValueType remove(final Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void putAll(@NotNull final Map<? extends SequenceType, ? extends ValueType> map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void clear() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public final Set<SequenceType> keySet() {
        return Collections.emptySet();
    }

    @NotNull
    @Override
    public final Collection<ValueType> values() {
        return Collections.emptyList();
    }

    @NotNull
    @Override
    public final Set<Entry<SequenceType, ValueType>> entrySet() {
        return Collections.emptySet();
    }


    @Nullable
    @Override
    public final Node<SequenceType, ValueType> getRootNode() {
        return null;
    }

    @NotNull
    @Override
    public final Trie<SequenceType, ValueType> subTree(@NotNull final SequenceType sequence) {
        return this;
    }

}
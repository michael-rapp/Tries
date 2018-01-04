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

import static de.mrapp.util.Condition.ensureNotNull;

/**
 * An abstract base class for all tries, which forward read-only method calls to an encapsulated
 * trie and throw {@link UnsupportedOperationException}s when calling a method, which attempts to
 * change the trie's state.
 *
 * @param <SequenceType> The type of the sequences, which are used as the trie's keys
 * @param <ValueType>    The type of the values, which are stored by trie
 * @author Michael Rapp
 * @since 1.0.0
 */
public abstract class AbstractUnmodifiableTrie<SequenceType extends Sequence, ValueType, TrieType extends Trie<SequenceType, ValueType>>
        implements Trie<SequenceType, ValueType> {

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = 8786467157930306130L;

    /**
     * The encapsulated trie.
     */
    final TrieType trie;

    /**
     * Creates a new immutable trie.
     *
     * @param trie The trie, which should be encapsulated, as an instance of the generic type {@link
     *             TrieType}. The trie may not be null
     */
    public AbstractUnmodifiableTrie(@NotNull final TrieType trie) {
        ensureNotNull(trie, "The trie may not be null");
        this.trie = trie;
    }

    @Override
    public final int size() {
        return trie.size();
    }

    @Override
    public final boolean isEmpty() {
        return trie.isEmpty();
    }

    @Override
    public final boolean containsKey(final Object key) {
        return trie.containsKey(key);
    }

    @Override
    public final boolean containsValue(final Object value) {
        return trie.containsValue(value);
    }

    @Override
    public final ValueType get(final Object key) {
        return trie.get(key);
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
        return Collections.unmodifiableSet(trie.keySet());
    }

    @NotNull
    @Override
    public final Collection<ValueType> values() {
        return Collections.unmodifiableCollection(trie.values());
    }

    @NotNull
    @Override
    public final Set<Entry<SequenceType, ValueType>> entrySet() {
        return Collections.unmodifiableSet(trie.entrySet());
    }

    @Nullable
    @Override
    public final Node<SequenceType, ValueType> getRootNode() {
        return trie.getRootNode();
    }

    @Override
    public final String toString() {
        return trie.toString();
    }

    @Override
    public final int hashCode() {
        return trie.hashCode();
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public final boolean equals(final Object obj) {
        return trie.equals(obj);
    }

}
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
import de.mrapp.tries.NodeValue;
import de.mrapp.tries.Sequence;
import de.mrapp.tries.Trie;
import de.mrapp.tries.util.SequenceUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * An abstract base class for all immutable tries, which contains only a single entry.
 *
 * @param <SequenceType> The type of the sequences, which are used as the trie's keys
 * @param <ValueType>    The type of the values, which are stored by trie
 * @author Michael Rapp
 * @since 1.0.0
 */
public abstract class AbstractSingletonTrie<SequenceType extends Sequence, ValueType> implements
        Trie<SequenceType, ValueType> {

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = 5841613135852958466L;

    /**
     * The key of the entry, which is contained by the trie.
     */
    final SequenceType key;

    /**
     * The value of the entry, which is contained by the trie.
     */
    final ValueType value;

    /**
     * The root node of the trie.
     */
    private final Node<SequenceType, ValueType> unmodifiableRootNode;

    /**
     * Creates a new immutable trie, which only contains a single entry.
     *
     * @param key   The key of the entry as an instance of the generic type {@link SequenceType} or
     *              null
     * @param value The value of the entry as an instance of the generic type {@link ValueType} or
     *              null
     */
    public AbstractSingletonTrie(@Nullable final SequenceType key,
                                 @Nullable final ValueType value) {
        this.key = key;
        this.value = value;
        Node<SequenceType, ValueType> rootNode = new HashNode<>();

        if (SequenceUtil.isEmpty(key)) {
            rootNode.setNodeValue(new NodeValue<>(value));
        } else {
            Node<SequenceType, ValueType> successor = rootNode.addSuccessor(key);
            successor.setNodeValue(new NodeValue<>(value));
        }

        this.unmodifiableRootNode = new UnmodifiableNode<>(rootNode);
    }

    @Override
    public final int size() {
        return 1;
    }

    @Override
    public final boolean isEmpty() {
        return false;
    }

    @Override
    public final boolean containsKey(final Object key) {
        if (this.key == null) {
            if (key != null)
                return false;
        } else if (!this.key.equals(key))
            return false;
        return true;
    }

    @Override
    public final boolean containsValue(final Object value) {
        if (this.value == null) {
            if (value != null)
                return false;
        } else if (!this.value.equals(value))
            return false;
        return true;
    }

    @Override
    public final ValueType get(final Object key) {
        return containsKey(key) ? value : null;
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
        return Collections.singleton(key);
    }

    @NotNull
    @Override
    public final Collection<ValueType> values() {
        return Collections.singletonList(value);
    }

    @NotNull
    @Override
    public final Set<Entry<SequenceType, ValueType>> entrySet() {
        return Collections.singleton(new AbstractMap.SimpleImmutableEntry<>(key, value));
    }

    @Nullable
    @Override
    public final Node<SequenceType, ValueType> getRootNode() {
        return unmodifiableRootNode;
    }

}
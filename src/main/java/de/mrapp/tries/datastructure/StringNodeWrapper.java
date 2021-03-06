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
package de.mrapp.tries.datastructure;

import de.mrapp.tries.Node;
import de.mrapp.tries.NodeValue;
import de.mrapp.tries.sequence.StringSequence;
import de.mrapp.util.Condition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;

/**
 * An implementation of the interface {@link Node}, where predecessors correspond to keys of the
 * type {@link String}. It forwards read-only method calls to an encapsulated node by mapping {@link
 * String}s to {@link StringSequence}s and throws {@link UnsupportedOperationException}s when
 * calling a method, which attempts to change the node's state.
 *
 * @param <ValueType> The type of the node's value
 * @author Michael Rapp
 * @since 1.0.0
 */
public class StringNodeWrapper<ValueType> implements Node<String, ValueType> {

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = -3206714018385499449L;

    /**
     * The encapsulated node.
     */
    private final Node<StringSequence, ValueType> node;

    /**
     * Creates a new implementation of the interface {@link Node}, where predecessors correspond to
     * keys of the type {@link String}.
     *
     * @param node The node, which should be encapsulated, as an instance of the type {@link Node}.
     *             The node may not be null
     */
    public StringNodeWrapper(@NotNull final Node<StringSequence, ValueType> node) {
        Condition.INSTANCE.ensureNotNull(node, "The node may not be null");
        this.node = node;
    }

    @Nullable
    @Override
    public final NodeValue<ValueType> getNodeValue() {
        return node.getNodeValue();
    }

    @Nullable
    @Override
    public final NodeValue<ValueType> setNodeValue(@Nullable final NodeValue<ValueType> nodeValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final int getSuccessorCount() {
        return node.getSuccessorCount();
    }

    @Nullable
    @Override
    public final Node<String, ValueType> getSuccessor(@NotNull final String key) {
        Node<StringSequence, ValueType> successor = node.getSuccessor(new StringSequence(key));
        return successor != null ? new StringNodeWrapper<>(successor) : null;
    }

    @NotNull
    @Override
    public final Node<String, ValueType> addSuccessor(@NotNull final String key,
                                                      @Nullable final Node<String, ValueType> successor) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void removeSuccessor(@NotNull final String key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final int getSuccessorValueCount() {
        return node.getSuccessorValueCount();
    }

    @Override
    public final void increaseSuccessorValueCount(final int by) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void decreaseSuccessorValueCount(final int by) {
        throw new UnsupportedOperationException();
    }

    @Nullable
    @Override
    public final Map.Entry<String, Node<String, ValueType>> getPredecessor() {
        Map.Entry<StringSequence, Node<StringSequence, ValueType>> entry = node.getPredecessor();
        return entry != null ? new AbstractMap.SimpleImmutableEntry<>(entry.getKey().toString(),
                new StringNodeWrapper<>(entry.getValue())) : null;
    }

    @Override
    public final void setPredecessor(
            @Nullable final Map.Entry<String, Node<String, ValueType>> predecessor) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public final Node<String, ValueType> clone() {
        return new StringNodeWrapper<>(node.clone());
    }

    @NotNull
    @Override
    public final Iterator<String> iterator() {
        return new Iterator<String>() {

            private final Iterator<StringSequence> iterator = node.iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public String next() {
                return StringSequence.convertToString(iterator.next());
            }

        };
    }

    @Override
    public final String toString() {
        return node.toString();
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 0;
        result = prime * result + node.hashCode();
        return result;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (obj.getClass() != getClass())
            return false;
        StringNodeWrapper<?> other = (StringNodeWrapper<?>) obj;
        return node.equals(other.node);
    }

}
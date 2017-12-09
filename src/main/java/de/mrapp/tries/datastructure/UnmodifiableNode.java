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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

import static de.mrapp.util.Condition.ensureNotNull;

/**
 * An implementation of the interface {@link Node}, which forwards read-only method calls to an
 * encapsulated node and throws {@link UnsupportedOperationException}s when calling a method, which
 * attempts to change the node's state.
 *
 * @param <SequenceType> The type of the sequences, which correspond to the node's successors
 * @param <ValueType>    The type of the node's value
 * @author Michael Rapp
 * @since 1.0.0
 */
public class UnmodifiableNode<SequenceType extends Sequence, ValueType> implements
        Node<SequenceType, ValueType> {

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = -821100483227210297L;

    /**
     * The encapsulated node.
     */
    private final Node<SequenceType, ValueType> node;

    /**
     * Creates a new unmodifiable node.
     *
     * @param node The node, which should be encapsulated, as an instance of the type {@link Node}.
     *             The node may not be null
     */
    public UnmodifiableNode(@NotNull final Node<SequenceType, ValueType> node) {
        ensureNotNull(node, "The node may not be null");
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
    public final Node<SequenceType, ValueType> getSuccessor(@NotNull final SequenceType sequence) {
        Node<SequenceType, ValueType> successor = node.getSuccessor(sequence);
        return successor != null ? new UnmodifiableNode<>(successor) : null;
    }

    @NotNull
    @Override
    public final Node<SequenceType, ValueType> addSuccessor(@NotNull final SequenceType sequence,
                                                            @Nullable final Node<SequenceType, ValueType> successor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void removeSuccessor(@NotNull final SequenceType sequence) {
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
    public final Node<SequenceType, ValueType> getPredecessor() {
        Node<SequenceType, ValueType> predecessor = node.getPredecessor();
        return predecessor != null ? new UnmodifiableNode<>(predecessor) : null;
    }

    @Override
    public final void setPredecessor(@Nullable final Node<SequenceType, ValueType> predecessor) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public final Iterator<SequenceType> iterator() {
        return node.iterator();
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Node<SequenceType, ValueType> clone() {
        try {
            return (Node<SequenceType, ValueType>) super.clone();
        } catch (CloneNotSupportedException e) {
            // Should never happen
            return null;
        }
    }

    @Override
    public final String toString() {
        return node.toString();
    }

    @Override
    public final int hashCode() {
        return node.hashCode();
    }

    @Override
    public final boolean equals(final Object obj) {
        return node.equals(obj);
    }

}
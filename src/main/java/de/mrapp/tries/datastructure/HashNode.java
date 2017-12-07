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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static de.mrapp.util.Condition.ensureNotNull;

public class HashNode<SequenceType extends Sequence, ValueType> extends
        AbstractNode<SequenceType, ValueType> {

    private static final long serialVersionUID = 6241483145831567447L;

    private final Map<SequenceType, Node<SequenceType, ValueType>> successors;

    public HashNode() {
        this.successors = new HashMap<>();
    }

    @NotNull
    @Override
    protected final Node<SequenceType, ValueType> onAddSuccessor(
            @NotNull final SequenceType sequence,
            @Nullable final Node<SequenceType, ValueType> successor) {
        Node<SequenceType, ValueType> successorToAdd =
                successor == null ? new HashNode<>() : successor;
        successors.put(sequence, successorToAdd);
        return successorToAdd;
    }

    @Override
    protected final Node<SequenceType, ValueType> onRemoveSuccessor(
            @NotNull final SequenceType sequence) {
        return successors.remove(sequence);
    }

    @Nullable
    @Override
    public final Node<SequenceType, ValueType> getSuccessor(@NotNull final SequenceType sequence) {
        ensureNotNull(sequence, "The sequence may not be null");
        return successors.get(sequence);
    }

    @Override
    public final int getSuccessorCount() {
        return successors.size();
    }

    @NotNull
    @Override
    public final Iterator<SequenceType> iterator() {
        return successors.keySet().iterator();
    }

    @Override
    public final Node<SequenceType, ValueType> clone() {
        HashNode<SequenceType, ValueType> clone = new HashNode<>();
        clone.setNodeValue(getNodeValue() != null ? getNodeValue().clone() : null);
        cloneSuccessors(this, clone);
        return clone;
    }

    @Override
    public final String toString() {
        return "Node{" +
                "value=" + (getNodeValue() != null ? getNodeValue().getValue() : "null") +
                ", successors=" + successors.keySet() +
                '}';
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + successors.hashCode();
        return result;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        HashNode<?, ?> other = (HashNode<?, ?>) obj;
        return successors.equals(other.successors);
    }

}
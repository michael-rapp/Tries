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

/**
 * A node of a trie, which stores its successors in a {@link HashMap}.
 *
 * @param <KeyType>   The type of the keys, which are associated with the node's successors
 * @param <ValueType> The type of the node's value
 * @author Michael Rapp
 * @since 1.0.0
 */
public class HashNode<KeyType extends Sequence, ValueType> extends
        AbstractNode<KeyType, ValueType> {

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = 6241483145831567447L;

    /**
     * The hash map, the successors of the node are stored in.
     */
    private final Map<KeyType, Node<KeyType, ValueType>> successors;

    /**
     * Creates a new node of a trie, which stores its successors in a {@link HashMap}.
     */
    public HashNode() {
        this.successors = new HashMap<>();
    }

    @NotNull
    @Override
    protected final Node<KeyType, ValueType> onAddSuccessor(@NotNull final KeyType key,
                                                            @Nullable final Node<KeyType, ValueType> successor) {
        Node<KeyType, ValueType> successorToAdd =
                successor == null ? new HashNode<>() : successor;
        successors.put(key, successorToAdd);
        return successorToAdd;
    }

    @Override
    protected final Node<KeyType, ValueType> onRemoveSuccessor(@NotNull final KeyType key) {
        return successors.remove(key);
    }

    @Nullable
    @Override
    public final Node<KeyType, ValueType> getSuccessor(@NotNull final KeyType key) {
        ensureNotNull(key, "The key may not be null");
        return successors.get(key);
    }

    @Override
    public final int getSuccessorCount() {
        return successors.size();
    }

    @NotNull
    @Override
    public final Iterator<KeyType> iterator() {
        return successors.keySet().iterator();
    }

    @Override
    public final HashNode<KeyType, ValueType> clone() {
        HashNode<KeyType, ValueType> clone = new HashNode<>();
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
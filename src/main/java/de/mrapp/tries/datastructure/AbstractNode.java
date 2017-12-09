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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static de.mrapp.util.Condition.ensureAtLeast;
import static de.mrapp.util.Condition.ensureNotNull;

/**
 * An abstract base for all nodes of a trie.
 *
 * @param <KeyType>   The type of the keys, which are associated with the node's successors
 * @param <ValueType> The type of the node's value
 * @author Michael Rapp
 * @since 1.0.0
 */
public abstract class AbstractNode<KeyType, ValueType> implements
        Node<KeyType, ValueType> {

    private static final long serialVersionUID = -5239050242490781683L;

    private NodeValue<ValueType> nodeValue;

    private int successorValueCount;

    private Node<KeyType, ValueType> predecessor;

    protected final void cloneSuccessors(@NotNull final Node<KeyType, ValueType> source,
                                         @NotNull final Node<KeyType, ValueType> target) {
        for (KeyType keys : source) {
            Node<KeyType, ValueType> successor = source.getSuccessor(keys);

            if (successor != null) {
                Node<KeyType, ValueType> clonedSuccessor = target.addSuccessor(keys);
                clonedSuccessor.setNodeValue(
                        successor.getNodeValue() != null ? successor.getNodeValue().clone() : null);
                cloneSuccessors(successor, clonedSuccessor);
            }
        }
    }

    @NotNull
    protected abstract Node<KeyType, ValueType> onAddSuccessor(
            @NotNull final KeyType key, @Nullable final Node<KeyType, ValueType> successor);

    @Nullable
    protected abstract Node<KeyType, ValueType> onRemoveSuccessor(@NotNull final KeyType key);

    AbstractNode() {
        this.nodeValue = null;
        this.successorValueCount = 0;
        this.predecessor = null;
    }

    @Nullable
    @Override
    public final NodeValue<ValueType> getNodeValue() {
        return nodeValue;
    }

    @Nullable
    @Override
    public final NodeValue<ValueType> setNodeValue(@Nullable final NodeValue<ValueType> nodeValue) {
        NodeValue<ValueType> oldValue = this.nodeValue;

        if (oldValue == null && nodeValue != null) {
            increaseSuccessorValueCount(1);
        } else if (oldValue != null && nodeValue == null) {
            decreaseSuccessorValueCount(1);
        }

        this.nodeValue = nodeValue;
        return oldValue;
    }

    @Override
    public abstract int getSuccessorCount();

    @NotNull
    public final Node<KeyType, ValueType> addSuccessor(@NotNull final KeyType key,
                                                       @Nullable final Node<KeyType, ValueType> successor) {
        ensureNotNull(key, "The key may not be null");
        Node<KeyType, ValueType> addedSuccessor = onAddSuccessor(key, successor);
        addedSuccessor.setPredecessor(this);
        increaseSuccessorValueCount(addedSuccessor.getSuccessorValueCount());
        return addedSuccessor;
    }

    @Override
    public final void removeSuccessor(@NotNull final KeyType key) {
        ensureNotNull(key, "The key may not be null");
        Node<KeyType, ValueType> successor = onRemoveSuccessor(key);

        if (successor != null) {
            decreaseSuccessorValueCount(successor.getSuccessorValueCount());
            successor.setPredecessor(null);
        }
    }

    @Override
    public final int getSuccessorValueCount() {
        return successorValueCount;
    }

    @Override
    public final void increaseSuccessorValueCount(final int by) {
        ensureAtLeast(by, 0, "The amount must be at least 0");
        this.successorValueCount += by;

        if (predecessor != null) {
            predecessor.increaseSuccessorValueCount(by);
        }
    }

    @Override
    public final void decreaseSuccessorValueCount(final int by) {
        ensureAtLeast(by, 0, "The amount must be at least 0");
        this.successorValueCount -= by;

        if (predecessor != null) {
            predecessor.decreaseSuccessorValueCount(by);
        }
    }

    @Nullable
    @Override
    public final Node<KeyType, ValueType> getPredecessor() {
        return predecessor;
    }

    @Override
    public final void setPredecessor(@Nullable final Node<KeyType, ValueType> predecessor) {
        this.predecessor = predecessor;
    }

    @Override
    public abstract Node<KeyType, ValueType> clone();

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 0;
        result = prime * result + (nodeValue == null ? 0 : nodeValue.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (obj.getClass() != getClass())
            return false;
        AbstractNode<?, ?> other = (AbstractNode<?, ?>) obj;
        if (nodeValue == null) {
            if (other.nodeValue != null)
                return false;
        } else if (!nodeValue.equals(other.nodeValue))
            return false;
        return true;
    }

}
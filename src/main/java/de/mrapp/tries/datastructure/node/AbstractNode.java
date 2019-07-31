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
package de.mrapp.tries.datastructure.node;

import de.mrapp.tries.Node;
import de.mrapp.tries.NodeValue;
import de.mrapp.tries.Sequence;
import de.mrapp.util.Condition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractMap;
import java.util.Map;

/**
 * An abstract base for all nodes of a trie.
 *
 * @param <KeyType>   The type of the keys, which are associated with the node's successors
 * @param <ValueType> The type of the node's value
 * @author Michael Rapp
 * @since 1.0.0
 */
public abstract class AbstractNode<KeyType extends Sequence, ValueType> implements
        Node<KeyType, ValueType> {

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = -5239050242490781683L;

    /**
     * The value of the node.
     */
    private NodeValue<ValueType> nodeValue;

    /**
     * The number of successors of the node for which a value is set.
     */
    private int successorValueCount;

    /**
     * The predecessor of the node.
     */
    private Map.Entry<KeyType, Node<KeyType, ValueType>> predecessor;

    /**
     * Clones all successors of a specific node recursively and adds them to another node.
     *
     * @param source The node, whose successors should be cloned, as an instance of the type {@link
     *               Node}. The node may not be null
     * @param target The node, the clones should be added to, as an instance of the type {@link
     *               Node}. The node may not be null
     */
    protected final void cloneSuccessors(@NotNull final Node<KeyType, ValueType> source,
                                         @NotNull final Node<KeyType, ValueType> target) {
        for (KeyType keys : source) {
            Node<KeyType, ValueType> successor = source.getSuccessor(keys);

            if (successor != null) {
                Node<KeyType, ValueType> clonedSuccessor = target.addSuccessor(keys);
                clonedSuccessor
                        .setNodeValue(successor.getNodeValue() != null ?
                                successor.getNodeValue().clone() : null);
                cloneSuccessors(successor, clonedSuccessor);
            }
        }
    }

    /**
     * The method, which is invoked on subclasses in order to add a specific successor to the node.
     *
     * @param key       The key, which corresponds to the successor as an instance of the generic
     *                  type {@link KeyType}. The key may not be null
     * @param successor The successor, which should be added, as an instance of the type {@link
     *                  Node} or null, if a new node should be created
     * @return The node, which has been added as a successor, as an instance of the type {@link
     * Node}. The node may not be null
     */
    @NotNull
    protected abstract Node<KeyType, ValueType> onAddSuccessor(@NotNull final KeyType key,
                                                               @Nullable final Node<KeyType, ValueType> successor);

    /**
     * The method, which is invoked on subclasses in order to remove a specific successor from the
     * node.
     *
     * @param key The key, which corresponds to the successor, which should be removed, as an
     *            instance of the generic type {@link KeyType}. The key may not be null
     * @return The successor, which has been removed, as an instance of the type {@link Node} or
     * null, if no successor corresponds to the given key
     */
    @Nullable
    protected abstract Node<KeyType, ValueType> onRemoveSuccessor(@NotNull final KeyType key);

    /**
     * Creates a new node of a trie.
     */
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

    @NotNull
    public final Node<KeyType, ValueType> addSuccessor(@NotNull final KeyType key,
                                                       @Nullable final Node<KeyType, ValueType> successor) {
        Condition.INSTANCE.ensureNotNull(key, "The key may not be null");
        Node<KeyType, ValueType> addedSuccessor = onAddSuccessor(key, successor);
        addedSuccessor.setPredecessor(new AbstractMap.SimpleImmutableEntry<>(key, this));
        increaseSuccessorValueCount(addedSuccessor.getSuccessorValueCount());
        return addedSuccessor;
    }

    @Override
    public final void removeSuccessor(@NotNull final KeyType key) {
        Condition.INSTANCE.ensureNotNull(key, "The key may not be null");
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
        Condition.INSTANCE.ensureAtLeast(by, 0, "The amount must be at least 0");
        this.successorValueCount += by;

        if (predecessor != null) {
            predecessor.getValue().increaseSuccessorValueCount(by);
        }
    }

    @Override
    public final void decreaseSuccessorValueCount(final int by) {
        Condition.INSTANCE.ensureAtLeast(by, 0, "The amount must be at least 0");
        this.successorValueCount -= by;

        if (predecessor != null) {
            predecessor.getValue().decreaseSuccessorValueCount(by);
        }
    }

    @Nullable
    @Override
    public final Map.Entry<KeyType, Node<KeyType, ValueType>> getPredecessor() {
        return predecessor;
    }

    @Override
    public final void setPredecessor(
            @Nullable final Map.Entry<KeyType, Node<KeyType, ValueType>> predecessor) {
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
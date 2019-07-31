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

import de.mrapp.util.Condition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Map;
import java.util.RandomAccess;

/**
 * Defines the interface, a node of a trie must implement. It extends the interface {@link Iterable}
 * to be able to iterate the keys, which correspond to the node's successors.
 * <p>
 * If the successors of the node are sorted, sublasses must implement the interface {@link
 * RandomAccess}.
 *
 * @param <KeyType>   The type of the keys, which are associated with the node's successors
 * @param <ValueType> The type of the node's value
 * @author Michael Rapp
 * @since 1.0.0
 */
public interface Node<KeyType, ValueType> extends Iterable<KeyType>, Serializable, Cloneable {

    /**
     * Returns the value of the node.
     *
     * @return The value of the node encapsulated by a {@link NodeValue} wrapper or null, if the
     * node does not correspond to a key, which has been put into the trie
     */
    @Nullable NodeValue<ValueType> getNodeValue();

    /**
     * Sets the value of the node.
     *
     * @param nodeValue A wrapper, which encapsulates the value, which should be set, as an instance
     *                  of the class {@link NodeValue} or null, if the node does not correspond to a
     *                  key, which has been put into the trie
     * @return The previous value of the node as an instance of the class {@link NodeValue} or null,
     * if no value was set
     */
    @Nullable NodeValue<ValueType> setNodeValue(@Nullable final NodeValue<ValueType> nodeValue);

    /**
     * Returns the unboxed value of the node, i.e. null, if {@link #getNodeValue()} returns null, or
     * the encapsulated value of the returned {@link NodeValue} otherwise.
     *
     * @return The unboxed value of the node as an instance of the generic type {@link ValueType}
     */
    default ValueType getValue() {
        NodeValue<ValueType> nodeValue = getNodeValue();
        return nodeValue != null ? nodeValue.getValue() : null;
    }

    /**
     * Returns, whether a value is set for the node, or not. If a value is set, the node corresponds
     * to a key, which has been put into the trie.
     *
     * @return True, if a value is set for the node, false otherwise
     */
    default boolean isValueSet() {
        return getNodeValue() != null;
    }

    /**
     * Returns the number of the node's successors.
     *
     * @return The number of the node's successor as an {@link Integer} value
     */
    int getSuccessorCount();

    /**
     * Returns, whether the node has successors, or not.
     *
     * @return True, if the node has successors, false otherwise
     */
    default boolean hasSuccessors() {
        return getSuccessorCount() > 0;
    }

    /**
     * Returns the successor of the node, which corresponds to a specific key.
     *
     * @param key The key of the successor, which should be returned, as an instance of the generic
     *            type {@link KeyType}. The key may not be null
     * @return The successor, which corresponds to the given key, as an instance of the type {@link
     * Node} or null, if no successor corresponds to the given key
     */
    @Nullable Node<KeyType, ValueType> getSuccessor(@NotNull KeyType key);

    /**
     * Creates a new successor and adds it to the node.
     *
     * @param key The key, which corresponds to the successor to be created, as an instance of the
     *            generic type {@link KeyType}. The key may not be null
     * @return The successor, which has been added, as an instance of the type {@link Node}. The
     * successor may not be null
     */
    @NotNull
    default Node<KeyType, ValueType> addSuccessor(@NotNull final KeyType key) {
        return addSuccessor(key, null);
    }

    /**
     * Adds a specific successor to the node.
     *
     * @param key       The key, which corresponds to the successor to be created, as an instance of
     *                  the generic type {@link KeyType}. The key may not be null
     * @param successor The successor, which should be added, as an instance of the type {@link
     *                  Node} or null, if the successor should be created
     * @return The successor, which has been added, as an instance of the type {@link Node}. The
     * successor may not be null
     */
    @NotNull Node<KeyType, ValueType> addSuccessor(@NotNull final KeyType key,
                                                   @Nullable final Node<KeyType, ValueType> successor);

    /**
     * Removes the successor, which corresponds to a specific key.
     *
     * @param key The key, which corresponds to the successor, which should be removed, as an
     *            instance of the generic type {@link KeyType}. The key may not be null
     */
    void removeSuccessor(@NotNull final KeyType key);

    /**
     * Removes the successor at a specific index. If the index is invalid, an {@link
     * IndexOutOfBoundsException} is thrown. If the node's successors are not sorted, an {@link
     * UnsupportedOperationException} is thrown.
     *
     * @param index The index of the successor, which should be removed, as an {@link Integer}
     *              value
     */
    default void removeSuccessor(final int index) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the number of successors for which values are set, i.e. {@link #isValueSet()}
     * returns
     * <code>true</code>. All successors of the node are taken into account recursively down to the
     * leaf nodes.
     *
     * @return The number of successor, for which values are set, as an {@link Integer} value
     */
    int getSuccessorValueCount();

    /**
     * Returns the key, which corresponds to the successor at a specific index. If the index is
     * invalid, an {@link IndexOutOfBoundsException} is thrown. If the node's successor or are not
     * sorted, an {@link UnsupportedOperationException} is thrown.
     *
     * @param index The index of the key, which should be returned, as an {@link Integer} value
     * @return The key of the successor at the given index as an instance of the generic type {@link
     * KeyType}. The key may not be null
     */
    @NotNull
    default KeyType getSuccessorKey(final int index) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the key, which corresponds to the first successor. If the node's successors are not
     * sorted, an {@link UnsupportedOperationException} is thrown.
     *
     * @return The key of the first successor as an instance of the generic type {@link KeyType} or
     * null, if the node does not have any successors
     */
    @Nullable
    default KeyType getFirstSuccessorKey() {
        Condition.INSTANCE.ensureTrue(RandomAccess.class.isAssignableFrom(getClass()), null,
                UnsupportedOperationException.class);
        return hasSuccessors() ? getSuccessorKey(0) : null;
    }

    /**
     * Returns the key, which corresponds to the last successor. If the node's successors are not
     * sorted, an {@link UnsupportedOperationException} is thrown.
     *
     * @return The key of the last successor as an instance of the generic type {@link KeyType} or
     * null, if the node does not have any successors
     */
    @Nullable
    default KeyType getLastSuccessorKey() {
        Condition.INSTANCE.ensureTrue(RandomAccess.class.isAssignableFrom(getClass()), null,
                UnsupportedOperationException.class);
        int successorCount = getSuccessorCount();
        return successorCount > 0 ? getSuccessorKey(successorCount - 1) : null;
    }

    /**
     * Returns the successor at a specific index. If the index is invalid, an {@link
     * IndexOutOfBoundsException} is thrown. If the node's successors are not sorted, an {@link
     * UnsupportedOperationException} is thrown.
     *
     * @param index The index of the successor, which which should be returned, as an {@link
     *              Integer} value
     * @return The successor at the given index as an instance of the type {@link Node}. The
     * successor may not be null
     */
    @NotNull
    default Node<KeyType, ValueType> getSuccessor(final int index) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the first successor. If the node's successors are not sorted, an {@link
     * UnsupportedOperationException} is thrown.
     *
     * @return The first successor as an instance of the type {@link Node} or null, if the node does
     * not have any successors
     */
    @Nullable
    default Node<KeyType, ValueType> getFirstSuccessor() {
        Condition.INSTANCE.ensureTrue(RandomAccess.class.isAssignableFrom(getClass()), null,
                UnsupportedOperationException.class);
        return hasSuccessors() ? getSuccessor(0) : null;
    }

    /**
     * Returns the last successor. If the node's successors are not sorted, an {@link
     * UnsupportedOperationException} is thrown.
     *
     * @return The last successor as an instance of the type {@link Node} or null, if the node does
     * not have any successors
     */
    @Nullable
    default Node<KeyType, ValueType> getLastSuccessor() {
        Condition.INSTANCE.ensureTrue(RandomAccess.class.isAssignableFrom(getClass()), null,
                UnsupportedOperationException.class);
        int successorCount = getSuccessorCount();
        return successorCount > 0 ? getSuccessor(successorCount - 1) : null;
    }

    /**
     * Returns the index of the successor, which corresponds to a specific key. If the node's
     * successors are not sorted, an {@link UnsupportedOperationException} is thrown.
     *
     * @param key The key of the successor, whose index should be returned, as an instance of the
     *            type {@link KeyType}. The key may not be null
     * @return The index of the successor, which corresponds to the given key, as an {@link Integer}
     * value or -1, if no such successor is available
     */
    default int indexOf(@NotNull KeyType key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the index of the successor, whose key starts with the first element of another key.
     * The index is obtained by using a binary search. If the node's successors are not sorted, an
     * {@link UnsupportedOperationException} is thrown.
     *
     * @param key The key, whose first element should be searched, as an instance of the generic
     *            type {@link KeyType}. The key may not be null
     * @return The index of the successor, whose key starts with the first element of the given key,
     * as an {@link Integer} value or -1, if no such successor is available
     */
    default int indexOfFirstElement(@NotNull KeyType key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Increases the number of successors for which values are set by a specific amount. This causes
     * the number of successors of the node's predecessors to be increased recursively as well.
     *
     * @param by The amount, the number of successor should be increased by, as an {@link Integer}
     *           value. The amount must be at least 0
     */
    void increaseSuccessorValueCount(int by);

    /**
     * Decreases the number of successors for which values are set by a specific amount. This causes
     * the number of successors of the node's predecessors to be decreased recursively as well.
     *
     * @param by The amount, the number of successor should be decreased by, as an {@link Integer}
     *           value. The amount must be at least 0
     */
    void decreaseSuccessorValueCount(int by);

    /**
     * Returns the predecessor of the node.
     *
     * @return An entry, which contains the predecessor of this node, as well as the key this node
     * is referenced by in the predecessor, as an instance of the type {@link Map.Entry} or null, if
     * no predecessor is set
     */
    @Nullable Map.Entry<KeyType, Node<KeyType, ValueType>> getPredecessor();

    /**
     * Sets the predecessor of the node.
     *
     * @param predecessor An entry, which contains the predecessor, which should be set, as well as
     *                    the key this node is referenced by in the predecessor, as an instance of
     *                    the type {@link Map.Entry} or null, if no predecessor should be set
     */
    void setPredecessor(@Nullable final Map.Entry<KeyType, Node<KeyType, ValueType>> predecessor);

    /**
     * Creates and returns a deep copy of the node and its successors.
     *
     * @return The copy, which has been created, as an instance of the type {@link Node}
     */
    Node<KeyType, ValueType> clone();

}
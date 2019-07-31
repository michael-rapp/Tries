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
import de.mrapp.tries.Sequence;
import de.mrapp.tries.util.SequenceUtil;
import de.mrapp.util.Condition;
import de.mrapp.util.datastructure.SortedArrayList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.RandomAccess;

/**
 * A node of a trie, which stores its successors in a {@link SortedArrayList}.
 *
 * @param <KeyType>   The type of the keys, which are associated with the node's successors
 * @param <ValueType> The type of the node's value
 * @author Michael Rapp
 * @since 1.0.0
 */
public class SortedListNode<KeyType extends Sequence, ValueType>
        extends AbstractNode<KeyType, ValueType> implements RandomAccess {

    /**
     * A directed edge, which references a successor of a node.
     *
     * @param <K> The type of the key, the successor corresponds to
     * @param <V> The type of the successor's value
     */
    private static class Edge<K extends Sequence, V>
            implements Serializable, Comparable<Edge<K, V>> {

        /**
         * The constant serial version UID.
         */
        private static final long serialVersionUID = 7839269878163723764L;

        /**
         * The key, the successor corresponds to.
         */
        private final K key;

        /**
         * The successor.
         */
        private final Node<K, V> successor;

        /**
         * The comparator, which is used to compare the keys of edges with each other.
         */
        private final Comparator<? super K> comparator;

        /**
         * Creates a new directed edge, which references the successor of a node.
         *
         * @param key        The key, the successor corresponds to, as a value of the generic type
         *                   {@link }. The key may not be null
         * @param successor  The successor as an instance of the type {@link Node}. The successor
         *                   may not be null
         * @param comparator The comparator, which should be used to compare the keys of edges with
         *                   each other, as an instance of the type {@link Comparator} or null, if
         *                   the natural order of the keys should be used
         */
        Edge(@NotNull final K key, @NotNull final Node<K, V> successor,
             @Nullable final Comparator<? super K> comparator) {
            Condition.INSTANCE.ensureNotNull(key, "The key may not be null");
            Condition.INSTANCE.ensureNotNull(successor, "The successor may not be null");
            this.key = key;
            this.successor = successor;
            this.comparator = SequenceUtil.comparator(comparator);
        }

        @Override
        public int compareTo(@NotNull final Edge<K, V> other) {
            return comparator.compare(key, other.key);
        }

        @Override
        public String toString() {
            return key.toString();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + key.hashCode();
            result = prime * result + successor.hashCode();
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
            Edge<?, ?> other = (Edge<?, ?>) obj;
            return key.equals(other.key) && successor.equals(other.successor);
        }

    }

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = 3618406941487648205L;

    /**
     * The list, which stores edge to the node's successors.
     */
    private final SortedArrayList<Edge<KeyType, ValueType>> successors;

    /**
     * The comparator, which is used to compare the successors of the node to each other, or null,
     * if the natural order of the successors' keys is used.
     */
    private final Comparator<? super KeyType> comparator;

    /**
     * Creates a new node of a trie, which stores its successors in a sorted list.
     *
     * @param comparator The comparator, which should be used to compare the successors to each
     *                   other, as an instance of the type {@link Comparator} or null, if the
     *                   natural order of the successors' keys should be used
     */
    public SortedListNode(@Nullable final Comparator<? super KeyType> comparator) {
        this.successors = new SortedArrayList<>();
        this.comparator = comparator;
    }

    @NotNull
    @Override
    protected final Node<KeyType, ValueType> onAddSuccessor(@NotNull final KeyType key,
                                                            @Nullable final Node<KeyType, ValueType> successor) {
        Node<KeyType, ValueType> successorToAdd =
                successor == null ? new SortedListNode<>(comparator) : successor;
        successors.add(new Edge<>(key, successorToAdd, comparator));
        return successorToAdd;
    }

    @Nullable
    @Override
    protected final Node<KeyType, ValueType> onRemoveSuccessor(@NotNull final KeyType key) {
        int index = indexOf(key);

        if (index != -1) {
            Edge<KeyType, ValueType> removed = successors.remove(index);
            return removed.successor;
        }

        return null;
    }

    @Override
    public final int getSuccessorCount() {
        return successors.size();
    }

    @Nullable
    @Override
    public final Node<KeyType, ValueType> getSuccessor(@NotNull final KeyType key) {
        int index = indexOf(key);

        if (index != -1) {
            Edge<KeyType, ValueType> edge = successors.get(index);
            return edge.successor;
        }

        return null;
    }

    @NotNull
    @Override
    public final KeyType getSuccessorKey(final int index) {
        Edge<KeyType, ValueType> edge = successors.get(index);
        return edge.key;
    }

    @NotNull
    @Override
    public final Node<KeyType, ValueType> getSuccessor(final int index) {
        Edge<KeyType, ValueType> edge = successors.get(index);
        return edge.successor;
    }

    @Override
    public final int indexOf(@NotNull final KeyType key) {
        return SequenceUtil
                .binarySearch(getSuccessorCount(), index -> successors.get(index).key, comparator,
                        key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final int indexOfFirstElement(@NotNull final KeyType key) {
        Condition.INSTANCE.ensureTrue(RandomAccess.class.isAssignableFrom(getClass()), null,
                UnsupportedOperationException.class);
        KeyType firstElement = SequenceUtil.subsequence(key, 0, 1);
        return SequenceUtil.binarySearch(getSuccessorCount(), this::getSuccessorKey,
                (o1, o2) -> ((Comparable<? super KeyType>) SequenceUtil.subsequence(o1, 0, 1))
                        .compareTo(SequenceUtil.subsequence(o2, 0, 1)), firstElement);
    }

    @Override
    public final void removeSuccessor(final int index) {
        Edge<KeyType, ValueType> edge = successors.remove(index);
        Node<KeyType, ValueType> successor = edge.successor;
        decreaseSuccessorValueCount(successor.getSuccessorValueCount());
        successor.setPredecessor(null);
    }

    @NotNull
    @Override
    public final Iterator<KeyType> iterator() {
        return new Iterator<KeyType>() {

            private final Iterator<Edge<KeyType, ValueType>> iterator = successors.iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public KeyType next() {
                Edge<KeyType, ValueType> edge = iterator.next();
                return edge != null ? edge.key : null;
            }

        };
    }

    @Override
    public final SortedListNode<KeyType, ValueType> clone() {
        SortedListNode<KeyType, ValueType> clone = new SortedListNode<>(comparator);
        clone.setNodeValue(getNodeValue() != null ? getNodeValue().clone() : null);
        cloneSuccessors(this, clone);
        return clone;
    }

    @Override
    public final String toString() {
        return "Node{" + "value=" + (getNodeValue() != null ? getNodeValue().getValue() : "null") +
                ", successors=" + successors + '}';
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
        SortedListNode<?, ?> other = (SortedListNode<?, ?>) obj;
        return successors.equals(other.successors);
    }

}
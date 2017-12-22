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
import de.mrapp.util.datastructure.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static de.mrapp.util.Condition.*;

/**
 * An abstract base class for all tries. It implements the methods of the interface {@link Map}. In
 * particular it provides general implementations of lookup, insert and delete operations without
 * forcing constraints on the trie's structure. Subclasses must implement the methods {@link
 * #getSuccessor(Node, Sequence)}, {@link #addSuccessor(Node, Sequence)} and {@link
 * #removeSuccessor(Node, Sequence)} depending on the trie's structure.
 *
 * @param <SequenceType> The type of the sequences, which are used as the trie's keys
 * @param <ValueType>    The type of the values, which are stored by trie
 * @author Michael Rapp
 * @since 1.0.0
 */
public abstract class AbstractTrie<SequenceType extends Sequence, ValueType>
        implements Trie<SequenceType, ValueType> {

    /**
     * The entry set of a trie as returned by the method {@link #entrySet()}. The entry set is
     * backed by an iterator, which traverses all nodes of the trie in order to identify the (inner
     * or leaf) nodes, which contains values.
     */
    private class EntrySet extends AbstractSet<Map.Entry<SequenceType, ValueType>> {

        /**
         * An iterator, which traverses in all nodes of the trie in order to identify those that
         * contain values. Only nodes for which a value (null or non-null) is set, are returned by
         * the iterator's {@link Iterator#next()} method.
         */
        private class ValueIterator implements Iterator<Map.Entry<SequenceType, ValueType>> {

            /**
             * Represents a path from the root to a specific node.
             */
            private class Path {

                /**
                 * The node the path leads to.
                 */
                private final Node<SequenceType, ValueType> node;

                /**
                 * The sequence, which corresponds to the node.
                 */
                private final SequenceType sequence;

                /**
                 * The iterator, which allows to iterate the successors of the node.
                 */
                private Iterator<SequenceType> iterator;

                /**
                 * Creates a new empty path.
                 *
                 * @param rootNode The root node of the trie, as an instance of the type {@link
                 *                 Node} or null, if the trie is empty
                 */
                Path(@Nullable final Node<SequenceType, ValueType> rootNode) {
                    this.node = rootNode;
                    this.sequence = null;
                    this.iterator = null;
                }

                /**
                 * Creates a new path, which leads to a specific node.
                 *
                 * @param node     The node, the path should lead to, as an instance of the type
                 *                 {@link Node}. The node may not be null
                 * @param sequence The sequence, which corresponds to the given node, as an instance
                 *                 of the generic type {@link SequenceType}. The sequence may
                 *                 neither be null, nor empty
                 */
                Path(@NotNull final Node<SequenceType, ValueType> node,
                     @NotNull final SequenceType sequence) {
                    ensureNotNull(node, "The node may not be null");
                    ensureNotNull(sequence, "The sequence may not be null");
                    ensureFalse(sequence.isEmpty(), "The sequence may not be empty");
                    this.node = node;
                    this.sequence = sequence;
                    this.iterator = null;
                }

            }

            /**
             * A stack, which contains the nodes, which remain to be traversed.
             */
            private final Deque<Path> stack;

            /**
             * The path, which leads to the node, which is returned when the {@link #next()} method
             * is called for the next time.
             */
            private Path nextPath;

            /**
             * Fetches the path, which leads to the node, which should be returned when the
             * iterator's {@link #next()} method is called for the next time.
             *
             * @return The path, which leads to the next node, as an instance of the class {@link
             * Path} or null, if no nodes with a value are left
             */
            @Nullable
            private Path fetchNext() {
                while (!this.stack.isEmpty()) {
                    Path path = this.stack.poll();
                    Path previousPath = path;
                    Path downPath = descend(path);

                    while (downPath != null) {
                        previousPath = downPath;
                        downPath = descend(downPath);
                    }

                    if (previousPath.node.isValueSet()) {
                        return previousPath;
                    }
                }

                return null;
            }

            /**
             * Descends in the trie until the next node with a value is found.
             *
             * @param path The path, which points to the node from which the descend should start,
             *             as an instance of the class {@link Path}. The path may not be null
             * @return The path, which points to the next node with a value, as an instance of the
             * class {@link Path} or null, if no ndoes with a value are left
             */
            @Nullable
            private Path descend(@NotNull final Path path) {
                if (path.iterator == null) {
                    path.iterator = path.node.iterator();

                    if (path.node.isValueSet()) {
                        if (path.iterator.hasNext()) {
                            stack.push(path);
                        }

                        return null;
                    }
                }

                if (path.iterator != null && path.iterator.hasNext()) {
                    SequenceType key = path.iterator.next();

                    if (path.iterator.hasNext()) {
                        stack.push(path);
                    }

                    if (key != null) {
                        Node<SequenceType, ValueType> successor = path.node.getSuccessor(key);

                        if (successor != null) {
                            SequenceType sequence = SequenceUtil.concat(path.sequence, key);
                            return new Path(successor, sequence);
                        }
                    }
                }

                return null;
            }

            /**
             * Creates a new iterator, which allows to iterate all nodes of the trie, which contain
             * a value.
             */
            ValueIterator() {
                stack = new LinkedList<>();

                if (rootNode != null) {
                    stack.add(new Path(rootNode));
                    nextPath = fetchNext();
                }
            }

            @Override
            public boolean hasNext() {
                checkForModifications();
                return nextPath != null;
            }

            @Override
            public Entry<SequenceType, ValueType> next() {
                checkForModifications();
                ensureTrue(hasNext(), null, NoSuchElementException.class);
                Path result = nextPath;
                nextPath = fetchNext();
                return new AbstractMap.SimpleImmutableEntry<>(result.sequence,
                        result.node.getValue());
            }
        }

        /**
         * The modification count of the trie at the time, when the entry set was created.
         */
        private final long modificationCount;

        /**
         * Throws a {@link ConcurrentModificationException} if the entry set's modification count
         * differs from the modification count of the trie, it has been created from.
         */
        private void checkForModifications() {
            ensureEqual(this.modificationCount, AbstractTrie.this.modificationCount, null,
                    ConcurrentModificationException.class);
        }

        /**
         * Creates a new entry set of a trie.
         *
         * @param modificationCount The current modification count of the trie as a {@link Long}
         *                          value
         */
        EntrySet(final long modificationCount) {
            this.modificationCount = modificationCount;
        }

        @NotNull
        @Override
        public Iterator<Map.Entry<SequenceType, ValueType>> iterator() {
            return new ValueIterator();
        }

        @Override
        public int size() {
            checkForModifications();
            return AbstractTrie.this.size();
        }

    }

    /**
     * A map, which contains the key-value mappings of a trie. The entry set is backed by an {@link
     * EntrySet}.
     */
    private class EntryMap extends AbstractMap<SequenceType, ValueType> {

        /**
         * The backing entry set.
         */
        private final Set<Entry<SequenceType, ValueType>> entrySet;

        /**
         * Creates a new map, which contains the key-value mappings of a trie.
         *
         * @param modificationCount The current modification count of the trie as a {@link Long}
         *                          value
         */
        EntryMap(final long modificationCount) {
            this.entrySet = Collections.unmodifiableSet(new EntrySet(modificationCount));
        }

        @NotNull
        @Override
        public Set<Entry<SequenceType, ValueType>> entrySet() {
            return entrySet;
        }

    }

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = -9049598420902876017L;

    /**
     * The root node of the trie.
     */
    Node<SequenceType, ValueType> rootNode;

    /**
     * A counter, which is increased whenever the trie is modified. It is used to fast-fail
     * iterators.
     */
    long modificationCount;

    /**
     * The method, which is invoked on subclasses in order to create the trie's root node.
     *
     * @return The root node, which has been created, as an instance of the type {@link Node}. The
     * root node may not be null
     */
    @NotNull
    protected abstract Node<SequenceType, ValueType> createRootNode();

    /**
     * The method, which is invoked on subclasses in order to retrieve the successor of a specific
     * node, which corresponds to a specific sequence. Depending on the trie's structure, the suffix
     * of the sequence can be processed to any extend.
     *
     * @param node     The node, whose successor should be returned, as an instance of the type
     *                 {@link Node}. The node may not be null
     * @param sequence The sequence, the successor corresponds to, as an instance of the generic
     *                 type {@link SequenceType}. The sequence may neither be null, nor empty
     * @return A pair, which contains the successor, which corresponds to the given sequence, as
     * well as the suffix of the sequence, depending on how far it has been processed, as an
     * instance of the class {@link Pair} or null, if no matching successor is available
     */
    @Nullable
    protected abstract Pair<Node<SequenceType, ValueType>, SequenceType> getSuccessor(
            @NotNull final Node<SequenceType, ValueType> node,
            @NotNull final SequenceType sequence);

    /**
     * The method, which is invoked on subclasses in order to add a successor to a specific node.
     * Depending on the trie's structure, the given sequence can be processed to any extend.
     *
     * @param node     The node, the successor should be added to, as an instance of the type {@link
     *                 Node}. The node may not be null
     * @param sequence The sequence, the successor, which should be added, corresponds to, as an
     *                 instance of the generic type {@link SequenceType}. The sequence may neither
     *                 be null, nor empty
     * @return A pair, which contains the successor, which has been created, as well as the suffix
     * of the given sequence, depending on how far it has been processed, as an instance of the
     * class {@link Pair}. The pair may not be null
     */
    @NotNull
    protected abstract Pair<Node<SequenceType, ValueType>, SequenceType> addSuccessor(
            @NotNull final Node<SequenceType, ValueType> node,
            @NotNull final SequenceType sequence);

    /**
     * The method, which is invoked on subclasses in order to remove the successor, which
     * corresponds to a specific sequence, from a specific node. Depending on the trie's structure
     * the given sequence can be processed to any extend.
     *
     * @param node     The node, the successor should be removed from, as an instance of the type
     *                 {@link Node}. The node may not be null
     * @param sequence The sequence, the successor, which should be removed, corresponds to, as an
     *                 instance of the generic type {@link SequenceType}. The sequence may neither
     *                 be null, nor empty
     */
    protected abstract void removeSuccessor(@NotNull final Node<SequenceType, ValueType> node,
                                            @NotNull final SequenceType sequence);

    /**
     * Traverses the trie in order to returns the node, which corresponds to a specific key.
     *
     * @param key The key, the node, which should be returned, corresponds to, as an instance of the
     *            class {@link Object}. If the key cannot be cast to the generic type {@link
     *            SequenceType}, a {@link ClassCastException} will be thrown
     * @return The node, which corresponds to the given key, as an instance of the type {@link Node}
     * or null, if no such node is available
     */
    @SuppressWarnings("unchecked")
    @Nullable
    protected final Node<SequenceType, ValueType> getNode(final Object key) {
        SequenceType sequence = (SequenceType) key;

        if (rootNode != null) {
            Node<SequenceType, ValueType> currentNode = rootNode;
            SequenceType suffix = sequence;

            while (suffix != null && !suffix.isEmpty()) {
                Pair<Node<SequenceType, ValueType>, SequenceType> pair = getSuccessor(currentNode,
                        suffix);

                if (pair == null) {
                    return null;
                } else {
                    currentNode = pair.first;
                    suffix = pair.second;
                }
            }

            return currentNode;
        }

        return null;
    }

    /**
     * Creates a new trie.
     *
     * @param rootNode The root node of the trie as an instance of the type {@link Node} or null, if
     *                 the trie should be empty
     */
    protected AbstractTrie(@Nullable final Node<SequenceType, ValueType> rootNode) {
        this.rootNode = rootNode;
        this.modificationCount = 0;
    }

    /**
     * Creates a new empty trie.
     */
    public AbstractTrie() {
        this(null);
    }

    @Nullable
    @Override
    public final Node<SequenceType, ValueType> getRootNode() {
        return rootNode != null ? new UnmodifiableNode<>(rootNode) : null;
    }

    @Override
    public final boolean isEmpty() {
        return rootNode == null;
    }

    @Override
    public final int size() {
        return rootNode != null ? rootNode.getSuccessorValueCount() : 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final boolean containsKey(final Object key) {
        SequenceType sequence = (SequenceType) key;
        Node<SequenceType, ValueType> node = getNode(sequence);
        return node != null && node.getNodeValue() != null;
    }

    @Override
    public final boolean containsValue(final Object value) {
        return new EntryMap(modificationCount).containsValue(value);
    }

    @Override
    public final void clear() {
        this.rootNode = null;
        this.modificationCount++;
    }

    @NotNull
    @Override
    public final Set<SequenceType> keySet() {
        return Collections.unmodifiableSet(new EntryMap(modificationCount).keySet());
    }

    @NotNull
    @Override
    public final Collection<ValueType> values() {
        return Collections.unmodifiableCollection(new EntryMap(modificationCount).values());
    }

    @NotNull
    @Override
    public final Set<Entry<SequenceType, ValueType>> entrySet() {
        return Collections.unmodifiableSet(new EntryMap(modificationCount).entrySet);
    }

    @Override
    public final ValueType put(final SequenceType key, final ValueType value) {
        if (rootNode == null) {
            rootNode = createRootNode();
        }

        Node<SequenceType, ValueType> currentNode = rootNode;
        SequenceType suffix = key;

        while (suffix != null && !suffix.isEmpty()) {
            Pair<Node<SequenceType, ValueType>, SequenceType> pair = getSuccessor(currentNode,
                    suffix);

            if (pair == null) {
                break;
            } else {
                currentNode = pair.first;
                suffix = pair.second;
            }
        }

        NodeValue<ValueType> previousValue = null;

        if (suffix == null || suffix.isEmpty()) {
            previousValue = currentNode.setNodeValue(new NodeValue<>(value));
        } else {
            while (!suffix.isEmpty()) {
                Pair<Node<SequenceType, ValueType>, SequenceType> pair = addSuccessor(currentNode,
                        suffix);
                Node<SequenceType, ValueType> successor = pair.first;
                suffix = pair.second;

                if (suffix.isEmpty()) {
                    successor.setNodeValue(new NodeValue<>(value));
                }

                currentNode = successor;
            }
        }

        modificationCount++;
        return previousValue != null ? previousValue.getValue() : null;
    }

    @Override
    public final void putAll(@NotNull final Map<? extends SequenceType, ? extends ValueType> map) {
        ensureNotNull(map, "The map may not be null");
        map.forEach(this::put);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final ValueType remove(final Object key) {
        SequenceType sequence = (SequenceType) key;

        if (rootNode != null) {
            if (sequence == null || sequence.isEmpty()) {
                NodeValue<ValueType> previous = rootNode.setNodeValue(null);

                if (!rootNode.hasSuccessors()) {
                    clear();
                }

                return previous != null ? previous.getValue() : null;
            } else {
                Node<SequenceType, ValueType> lastRetainedNode = rootNode;
                SequenceType suffixToRemove = null;
                Node<SequenceType, ValueType> currentNode = rootNode;
                SequenceType suffix = sequence;

                while (!suffix.isEmpty()) {
                    Pair<Node<SequenceType, ValueType>, SequenceType> pair = getSuccessor(
                            currentNode, suffix);

                    if (pair == null) {
                        return null;
                    } else {
                        if (currentNode.getSuccessorCount() > 1 || currentNode.getValue() != null) {
                            lastRetainedNode = currentNode;
                            suffixToRemove = suffix;
                        }

                        Node<SequenceType, ValueType> successor = pair.first;
                        suffix = pair.second;

                        if (suffix.isEmpty()) {
                            if (successor.hasSuccessors()) {
                                lastRetainedNode = null;
                                suffixToRemove = null;
                            }

                            NodeValue<ValueType> value = successor.getNodeValue();

                            if (value != null) {
                                successor.setNodeValue(null);

                                if (lastRetainedNode == rootNode) {
                                    clear();
                                } else {
                                    if (suffixToRemove != null) {
                                        removeSuccessor(lastRetainedNode, suffixToRemove);
                                    }

                                    modificationCount++;
                                }

                                return value.getValue();
                            }

                            return null;
                        }

                        currentNode = successor;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public final ValueType get(final Object key) {
        Node<SequenceType, ValueType> node = getNode(key);
        return node != null ? node.getValue() : null;
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (rootNode == null ? 0 : rootNode.hashCode());
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
        AbstractTrie<?, ?> other = (AbstractTrie<?, ?>) obj;
        if (rootNode == null) {
            if (other.rootNode != null)
                return false;
        } else if (!rootNode.equals(other.rootNode))
            return false;
        return true;
    }

}
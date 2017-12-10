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

public abstract class AbstractTrie<SequenceType extends Sequence, ValueType>
        implements Trie<SequenceType, ValueType> {

    private class EntrySet extends AbstractSet<Map.Entry<SequenceType, ValueType>> {

        private class LeafIterator implements Iterator<Map.Entry<SequenceType, ValueType>> {

            private class Path {

                private final Node<SequenceType, ValueType> node;

                private final SequenceType sequence;

                Path(@Nullable final Node<SequenceType, ValueType> node) {
                    this.node = node;
                    this.sequence = null;
                }

                Path(@NotNull final Node<SequenceType, ValueType> node,
                     @NotNull final SequenceType sequence) {
                    ensureNotNull(node, "The node may not be null");
                    ensureNotNull(sequence, "The sequence may not be null");
                    ensureFalse(sequence.isEmpty(), "The sequence may not be empty");
                    this.node = node;
                    this.sequence = sequence;
                }

            }

            private final Queue<Path> queue;

            private Path nextPath;

            @Nullable
            private Path fetchNext() {
                Path next = null;

                while (!this.queue.isEmpty() && next == null) {
                    Path path = this.queue.poll();

                    if (path.node.isValueSet()) {
                        next = path;
                    }

                    for (SequenceType suffix : path.node) {
                        Node<SequenceType, ValueType> successor = path.node.getSuccessor(suffix);

                        if (successor != null) {
                            SequenceType sequence = SequenceUtil.concat(path.sequence, suffix);
                            this.queue.add(new Path(successor, sequence));
                        }
                    }
                }

                return next;
            }

            LeafIterator() {
                queue = new LinkedList<>();

                if (rootNode != null) {
                    queue.add(new Path(rootNode));
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

        private final long modificationCount;

        private void checkForModifications() {
            ensureEqual(this.modificationCount, AbstractTrie.this.modificationCount, null,
                    ConcurrentModificationException.class);
        }

        EntrySet(final long modificationCount) {
            this.modificationCount = modificationCount;
        }

        @NotNull
        @Override
        public Iterator<Map.Entry<SequenceType, ValueType>> iterator() {
            return new LeafIterator();
        }

        @Override
        public int size() {
            checkForModifications();
            return AbstractTrie.this.size();
        }

    }

    private class EntryMap extends AbstractMap<SequenceType, ValueType> {

        private final Set<Entry<SequenceType, ValueType>> entrySet;

        EntryMap(final long modificationCount) {
            this.entrySet = Collections.unmodifiableSet(new EntrySet(modificationCount));
        }

        @NotNull
        @Override
        public Set<Entry<SequenceType, ValueType>> entrySet() {
            return entrySet;
        }

    }

    private static final long serialVersionUID = -9049598420902876017L;

    private Node<SequenceType, ValueType> rootNode;

    private long modificationCount;

    @NotNull
    protected abstract Node<SequenceType, ValueType> createRootNode();

    @Nullable
    protected abstract Pair<Node<SequenceType, ValueType>, SequenceType> getSuccessor(
            @NotNull final Node<SequenceType, ValueType> node,
            @NotNull final SequenceType sequence);

    @NotNull
    protected abstract Pair<Node<SequenceType, ValueType>, SequenceType> addSuccessor(
            @NotNull final Node<SequenceType, ValueType> node,
            @NotNull final SequenceType sequence);

    protected abstract void removeSuccessor(@NotNull final Node<SequenceType, ValueType> node,
                                            @NotNull final SequenceType sequence);

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

    protected AbstractTrie(@Nullable final Node<SequenceType, ValueType> rootNode) {
        this.rootNode = rootNode;
        this.modificationCount = 0;
    }

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
                            if (successor.getSuccessorCount() > 0) {
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
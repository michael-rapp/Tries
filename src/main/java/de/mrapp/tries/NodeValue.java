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
package de.mrapp.tries;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * A wrapper, which encapsulates the value of a trie's node. A wrapper class for a node's value is
 * needed to distinguish between nodes that correspond to one of the keys, which have been put into
 * the trie, but are associated with the value <code>null</code> and those that do not correspond to
 * a key.
 *
 * @param <T> The type of the value
 * @author Michael Rapp
 * @since 1.0.0
 */
public class NodeValue<T> implements Serializable, Cloneable {

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = -2272789810845385356L;

    /**
     * The value of the node.
     */
    private T value;

    /**
     * Creates a new wrapper, which encapsulates the value of a trie's node.
     *
     * @param value The encapsulated value as an instance of the generic type {@link T} or null, if
     *              the encapsulated value is null
     */
    public NodeValue(@Nullable final T value) {
        this.value = value;
    }

    /**
     * Returns the encapsulated value.
     *
     * @return The encapsulated value as an instance of the generic type {@link T} or null, if the
     * encapsulated value is null
     */
    @Nullable
    public T getValue() {
        return value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final NodeValue<T> clone() {
        try {
            return (NodeValue<T>) super.clone();
        } catch (CloneNotSupportedException e) {
            // Should never happen
            return null;
        }
    }

    @Override
    public final String toString() {
        return value != null ? value.toString() : "null";
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (value == null ? 0 : value.hashCode());
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
        NodeValue<?> other = (NodeValue<?>) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

}
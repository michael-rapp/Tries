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
package de.mrapp.tries.util;

import de.mrapp.tries.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.NoSuchElementException;

/**
 * An utility class, which provides static methods for handling instances of the type {@link
 * Map.Entry}.
 */
public final class EntryUtil {

    /**
     * Creates a new utility class, which provides static methods for handling instances of the type
     * {@link Map.Entry}.
     */
    private EntryUtil() {

    }

    /**
     * Returns, whether the values of a specific node and entry are equal, or not.
     *
     * @param node  The node as an instance of the type {@link Node}. The node may not be null
     * @param entry The entry as an instance of the type {@link Map.Entry}. The entry may not be
     *              null
     * @return True, if the values of the given node and entry are equal, false otherwise
     */
    public static boolean isValueEqual(@NotNull final Node<?, ?> node,
                                       @NotNull final Map.Entry<?, ?> entry) {
        return isEqual(node.getValue(), entry.getValue());
    }

    /**
     * Returns, whether two objects are equal, or not.
     *
     * @param o1 The first object as an instance of the class {@link Object} or null
     * @param o2 The second object as an instance of the class {@link Object} or null
     * @return True, if the given objects are equal, false otherwise
     */
    public static boolean isEqual(@Nullable final Object o1, @Nullable final Object o2) {
        if (o1 == null) {
            if (o2 != null)
                return false;
        } else if (!o1.equals(o2))
            return false;
        return true;
    }

    /**
     * Returns the key of a specific entry or null, if the entry is null.
     *
     * @param entry The entry, whose key should be returned, as an instance of the type {@link
     *              Map.Entry} or null
     * @param <K>   The type of the entry's key
     * @return The key of the given entry as an instance of the generic type {@link K} or null, if
     * the key or the given entry is null
     */
    @Nullable
    public static <K> K getKey(@Nullable final Map.Entry<K, ?> entry) {
        return entry != null ? entry.getKey() : null;
    }

    /**
     * Returns the key of a specific entry or throws a {@link NoSuchElementException}, if the entry
     * is null.
     *
     * @param entry The entry, whose key should be returned, as an instance of the type {@link
     *              Map.Entry} or null
     * @param <K>   The type of the entry's key
     * @return The key of the given entry as an instance of the generic type {@link K} or null, if
     * the key is null
     */
    @Nullable
    public static <K> K getKeyOrThrowException(@Nullable final Map.Entry<K, ?> entry) {
        if (entry != null) {
            return entry.getKey();
        }

        throw new NoSuchElementException();
    }

}
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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.NoSuchElementException;

/**
 * An utility class, which provides methods for handing instances of the type {@link Map.Entry}.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public final class EntryUtil {

    /**
     * Creates a new utility class, which provides methods for handling instances of the type {@link
     * Map.Entry}.
     */
    private EntryUtil() {

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
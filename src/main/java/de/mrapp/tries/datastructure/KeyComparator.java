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

import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

/**
 * A comparator, which allows to compare keys to each other.
 *
 * @param <K> The type of the keys
 * @author Michael Rapp
 * @since 1.0.0
 */
public class KeyComparator<K> implements Comparator<K> {

    /**
     * The comparator, which is used to compare keys.
     */
    private final Comparator<? super K> comparator;

    /**
     * Creates a new comparator, which allows to compare keys to each other.
     *
     * @param comparator The comparator, which should be used to compare keys, as an instance of the
     *                   type {@link Comparator} or null, if the natural order of the keys should be
     *                   used
     */
    KeyComparator(@Nullable final Comparator<? super K> comparator) {
        this.comparator = comparator;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final int compare(final K o1, final K o2) {
        if (comparator != null) {
            return comparator.compare(o1, o2);
        } else {
            return ((Comparable<? super K>) o1).compareTo(o2);
        }
    }

}
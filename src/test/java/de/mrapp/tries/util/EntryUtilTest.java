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
package de.mrapp.tries.util;

import de.mrapp.tries.Node;
import de.mrapp.tries.NodeValue;
import de.mrapp.tries.datastructure.node.HashNode;
import de.mrapp.tries.sequence.StringSequence;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

/**
 * Tests the functionality of the class {@link EntryUtil}.
 *
 * @author Michael Rapp
 */
public class EntryUtilTest {

    @Test
    public void testIsValueEqual() {
        String value = "value";
        Node<StringSequence, String> node = new HashNode<>();
        Map.Entry<String, String> entry = new AbstractMap.SimpleImmutableEntry<>("key", null);
        assertTrue(EntryUtil.isValueEqual(node, entry));
        entry = new AbstractMap.SimpleImmutableEntry<>("key", value);
        assertFalse(EntryUtil.isValueEqual(node, entry));
        node.setNodeValue(new NodeValue<>(value));
        entry = new AbstractMap.SimpleImmutableEntry<>("key", null);
        assertFalse(EntryUtil.isValueEqual(node, entry));
        entry = new AbstractMap.SimpleImmutableEntry<>("key", value);
        assertTrue(EntryUtil.isValueEqual(node, entry));
    }

    @Test
    public void testIsEqual() {
        assertTrue(EntryUtil.isEqual(null, null));
        assertFalse(EntryUtil.isEqual(null, new Object()));
        assertFalse(EntryUtil.isEqual("foo", new Object()));
        assertFalse(EntryUtil.isEqual(new Object(), null));
        assertFalse(EntryUtil.isEqual(new Object(), "foo"));
        assertFalse(EntryUtil.isEqual("foo", "bar"));
        assertTrue(EntryUtil.isEqual("foo", "foo"));
    }

    @Test
    public void testGetKey() {
        String key = "key";
        assertNull(EntryUtil.getKey(null));
        assertNull(EntryUtil.getKey(new AbstractMap.SimpleImmutableEntry<>(null, null)));
        assertEquals(key, EntryUtil.getKey(new AbstractMap.SimpleImmutableEntry<>(key, null)));
    }

    @Test
    public void testGetKeyOrThrowException() {
        String key = "key";
        assertEquals(key, EntryUtil
                .getKeyOrThrowException(new AbstractMap.SimpleImmutableEntry<>(key, null)));
        assertNull(EntryUtil
                .getKeyOrThrowException(new AbstractMap.SimpleImmutableEntry<>(null, null)));

        try {
            EntryUtil.getKeyOrThrowException(null);
            fail();
        } catch (NoSuchElementException e) {
            // Expected
        }
    }

}
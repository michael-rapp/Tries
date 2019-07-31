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

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the functionality of the class {@link NodeValue}.
 *
 * @author Michael Rapp
 */
public class NodeValueTest {

    @Test
    public final void testConstructor() {
        String value = "foo";
        NodeValue<String> nodeValue = new NodeValue<>(value);
        assertEquals(value, nodeValue.getValue());
    }

    @Test
    public final void testConstructorIfValueIsNull() {
        NodeValue<String> nodeValue = new NodeValue<>(null);
        assertNull(nodeValue.getValue());
    }

    @Test
    public final void testToString() {
        String value = "foo";
        assertEquals(value, new NodeValue<>(value).toString());
    }

    @Test
    public final void testToStringIfValueIsNull() {
        assertEquals("null", new NodeValue<String>(null).toString());
    }

    @Test
    public final void testHashCode() {
        NodeValue<String> nodeValue1 = new NodeValue<>(null);
        NodeValue<String> nodeValue2 = new NodeValue<>(null);
        assertEquals(nodeValue1.hashCode(), nodeValue1.hashCode());
        assertEquals(nodeValue1.hashCode(), nodeValue2.hashCode());
        nodeValue1 = new NodeValue<>("foo");
        assertNotEquals(nodeValue1.hashCode(), nodeValue2.hashCode());
        nodeValue2 = new NodeValue<>("foo");
        assertEquals(nodeValue1.hashCode(), nodeValue2.hashCode());
        nodeValue1 = new NodeValue<>("bar");
        assertNotEquals(nodeValue1.hashCode(), nodeValue2.hashCode());
    }

    @Test
    public final void testEquals() {
        NodeValue<String> nodeValue1 = new NodeValue<>(null);
        NodeValue<String> nodeValue2 = new NodeValue<>(null);
        assertFalse(nodeValue1.equals(null));
        assertFalse(nodeValue1.equals(new Object()));
        assertTrue(nodeValue1.equals(nodeValue1));
        assertTrue(nodeValue1.equals(nodeValue2));
        nodeValue1 = new NodeValue<>("foo");
        assertFalse(nodeValue1.equals(nodeValue2));
        nodeValue2 = new NodeValue<>("foo");
        assertTrue(nodeValue1.equals(nodeValue2));
        nodeValue1 = new NodeValue<>("bar");
        assertFalse(nodeValue1.equals(nodeValue2));
    }

}
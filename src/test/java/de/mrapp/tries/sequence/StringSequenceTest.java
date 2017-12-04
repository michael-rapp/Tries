package de.mrapp.tries.sequence;

import org.junit.Test;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * Tests the functionality of the class {@link StringSequence}.
 */
public class StringSequenceTest {

    @Test(expected = IllegalArgumentException.class)
    public final void testConstructorThrowsExceptionIfStringIsNull() {
        new StringSequence(null);
    }

    @Test
    public final void testSubsequenceWithStartParameter() {
        assertEquals(new StringSequence("23"), new StringSequence("123").subsequence(1));
    }

    @Test
    public final void testSubsequenceWithStartAndEndParameter() {
        assertEquals(new StringSequence(""), new StringSequence("123").subsequence(1, 1));
        assertEquals(new StringSequence("2"), new StringSequence("123").subsequence(1, 2));
        assertEquals(new StringSequence("23"), new StringSequence("123").subsequence(1, 3));
    }

    @Test
    public final void testIsEmpty() {
        assertTrue(new StringSequence("").isEmpty());
        assertFalse(new StringSequence("foo").isEmpty());
    }

    @Test
    public final void testLength() {
        assertEquals(0, new StringSequence("").length());
        assertEquals(3, new StringSequence("foo").length());
    }

    @Test
    public final void testToString() {
        String string = "foo";
        StringSequence stringSequence = new StringSequence(string);
        assertEquals(string, stringSequence.toString());
    }

    @Test
    public final void testHashCode() {
        assertEquals(new StringSequence("foo").hashCode(), new StringSequence("foo").hashCode());
        assertEquals(new StringSequence("").hashCode(), new StringSequence("").hashCode());
        assertNotEquals(new StringSequence("foo").hashCode(), new StringSequence("bar").hashCode());
    }

    @Test
    public final void testEquals() {
        assertFalse(new StringSequence("foo").equals(null));
        assertFalse(new StringSequence("foo").equals(new Object()));
        assertTrue(new StringSequence("").equals(new StringSequence("")));
        assertTrue(new StringSequence("foo").equals(new StringSequence("foo")));
        assertFalse(new StringSequence("foo").equals(new StringSequence("bar")));
        assertFalse(new StringSequence("foo").equals(new StringSequence("")));
    }

}
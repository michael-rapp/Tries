package de.mrapp.tries;

import de.mrapp.tries.sequence.StringSequence;
import org.junit.Test;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * Tests the functionality of the class {@link StringSequence}.
 */
public class StringSequenceTest {

    @Test
    public final void testBuilder() {
        StringSequence.Builder builder = new StringSequence.Builder();
        Collection<String> sequence = new LinkedList<>();
        sequence.add("f");
        sequence.add("o");
        sequence.add("o");
        StringSequence stringSequence = builder.build(sequence);
        assertEquals("foo", stringSequence.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testConstructorThrowsExceptionIfStringIsNull() {
        new StringSequence(null);
    }

    @Test
    public final void testIterator() {
        StringSequence stringSequence = new StringSequence("foo");
        Iterator<String> iterator = stringSequence.iterator();
        assertEquals("f", iterator.next());
        assertEquals("o", iterator.next());
        assertEquals("o", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public final void testIteratorWithEmptyString() {
        StringSequence stringSequence = new StringSequence("");
        Iterator<String> iterator = stringSequence.iterator();
        assertFalse(iterator.hasNext());
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
package de.mrapp.tries;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the functionality of the class {@link StringSequence}.
 */
public class StringSequenceTest {

    @Test
    public final void testEquals() {
        Assert.assertFalse(new StringSequence("foo").equals(null));
        Assert.assertFalse(new StringSequence("foo").equals(new Object()));
        Assert.assertTrue(new StringSequence("foo").equals(new StringSequence("foo")));
        Assert.assertFalse(new StringSequence("foo").equals(new StringSequence("bar")));
        Assert.assertFalse(new StringSequence("foo").equals(new StringSequence("")));
    }

}
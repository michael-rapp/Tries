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

import de.mrapp.tries.sequence.StringSequence;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests the functionality of the class {@link SequenceUtil}.
 *
 * @author Michael Rapp
 */
public class SequenceUtilTest {

    @Test
    public final void testSubsequenceWithStartParameter() {
        StringSequence sequence = new StringSequence("foo");
        assertEquals(new StringSequence("oo"), SequenceUtil.subsequence(sequence, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testSubsequenceWithStartParameterThrowsException() {
        SequenceUtil.subsequence(null, 1);
    }

    @Test
    public final void testSubsequenceWithStartAndEndParameter() {
        StringSequence sequence = new StringSequence("foo");
        assertEquals(new StringSequence("o"), SequenceUtil.subsequence(sequence, 1, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testSubsequenceWithStartAndEndParameterThrowsException() {
        SequenceUtil.subsequence(null, 1, 2);
    }

    @Test
    public final void testConcat() {
        StringSequence prefix = new StringSequence("foo");
        StringSequence suffix = new StringSequence("bar");
        assertEquals(new StringSequence("foobar"), SequenceUtil.concat(prefix, suffix));
    }

    @Test
    public final void testConcatIfBothSequencesAreNull() {
        assertNull(SequenceUtil.concat(null, null));
    }

    @Test
    public final void testConcatIfFirstSequenceIsNull() {
        StringSequence sequence = new StringSequence("foo");
        assertEquals(sequence, SequenceUtil.concat(null, sequence));
    }

    @Test
    public final void testConcatIfSecondSequenceIsNull() {
        StringSequence sequence = new StringSequence("foo");
        assertEquals(sequence, SequenceUtil.concat(sequence, null));
    }

}
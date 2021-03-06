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

import de.mrapp.tries.sequence.StringSequence;
import org.junit.Test;

import java.util.Comparator;
import java.util.function.Function;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests the functionality of the class {@link SequenceUtil}.
 *
 * @author Michael Rapp
 */
public class SequenceUtilTest {

    @Test
    public final void testIsEmpty() {
        assertTrue(SequenceUtil.isEmpty(null));
        assertTrue(SequenceUtil.isEmpty(new StringSequence("")));
        assertFalse(SequenceUtil.isEmpty(new StringSequence("foo")));
    }

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

    @Test
    public final void testCompareIfFirstKeyIsNullAndSecondKeyIsNotNull() {
        Comparator<? super StringSequence> sequenceComparator = SequenceUtil.comparator(null);
        assertEquals(-1, sequenceComparator.compare(null, new StringSequence("foo")));
    }

    @Test
    public final void testCompareIfFirstKeyIsNullAndSecondKeyIsNull() {
        Comparator<? super StringSequence> sequenceComparator = SequenceUtil.comparator(null);
        assertEquals(0, sequenceComparator.compare(null, null));
    }

    @Test
    public final void testCompareIfFirstKeyIsNotNullAndSecondKeyIsNull() {
        Comparator<? super StringSequence> sequenceComparator = SequenceUtil.comparator(null);
        assertEquals(1, sequenceComparator.compare(new StringSequence("foo"), null));
    }

    @Test
    public final void testCompareIfComparatorIsNotNull() {
        StringSequence sequence1 = new StringSequence("foo");
        StringSequence sequence2 = new StringSequence("bar");
        Comparator<StringSequence> comparator = mock(Comparator.class);
        Comparator<? super StringSequence> sequenceComparator = SequenceUtil.comparator(comparator);
        sequenceComparator.compare(sequence1, sequence2);
        verify(comparator, times(1)).compare(sequence1, sequence2);
    }

    @Test
    public final void testCompareIfComparatorIsNull() {
        StringSequence sequence1 = new StringSequence("foo");
        StringSequence sequence2 = new StringSequence("bar");
        Comparator<? super StringSequence> sequenceComparator = SequenceUtil.comparator(null);
        int c = sequenceComparator.compare(sequence1, sequence2);
        assertEquals("foo".compareTo("bar"), c);
    }

    @Test
    public final void testGetCommonPrefix() {
        assertNull(SequenceUtil.getCommonPrefix(new StringSequence(""), new StringSequence("foo")));
        assertNull(SequenceUtil.getCommonPrefix(new StringSequence("foo"), new StringSequence("")));
        assertNull(
                SequenceUtil.getCommonPrefix(new StringSequence("foo"), new StringSequence("bar")));
        assertEquals(new StringSequence("f"), SequenceUtil
                .getCommonPrefix(new StringSequence("foo"), new StringSequence("fxx")));
        assertEquals(new StringSequence("fo"), SequenceUtil
                .getCommonPrefix(new StringSequence("foo"), new StringSequence("fox")));
        assertEquals(new StringSequence("foo"), SequenceUtil
                .getCommonPrefix(new StringSequence("foo"), new StringSequence("foo")));
    }

    @Test
    public final void testBinarySearch() {
        StringSequence[] sequences = new StringSequence[]{new StringSequence(
                "bar"), new StringSequence("bla"), new StringSequence("fasel"), new StringSequence(
                "foo"), new StringSequence("foobar")};
        Function<Integer, StringSequence> getter = index -> sequences[index];
        assertEquals(-1, SequenceUtil.binarySearch(0, getter, null, new StringSequence("foobar")));
        assertEquals(-1, SequenceUtil
                .binarySearch(sequences.length, getter, null, new StringSequence("invalid")));
        assertEquals(0, SequenceUtil
                .binarySearch(sequences.length, getter, null, new StringSequence("bar")));
        assertEquals(1, SequenceUtil
                .binarySearch(sequences.length, getter, null, new StringSequence("bla")));
        assertEquals(2, SequenceUtil
                .binarySearch(sequences.length, getter, null, new StringSequence("fasel")));
        assertEquals(3, SequenceUtil
                .binarySearch(sequences.length, getter, null, new StringSequence("foo")));
        assertEquals(4, SequenceUtil
                .binarySearch(sequences.length, getter, null, new StringSequence("foobar")));
    }

}
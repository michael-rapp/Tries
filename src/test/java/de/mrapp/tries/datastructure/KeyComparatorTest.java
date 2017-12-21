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

import de.mrapp.tries.sequence.StringSequence;
import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Tests the functionality of the class {@link KeyComparator}.
 *
 * @author Michael Rapp
 */
public class KeyComparatorTest {

    @Test
    public final void testCompareIfComparatorIsNotNull() {
        StringSequence sequence1 = new StringSequence("foo");
        StringSequence sequence2 = new StringSequence("bar");
        Comparator<StringSequence> comparator = mock(Comparator.class);
        KeyComparator<StringSequence> keyComparator = new KeyComparator<>(comparator);
        keyComparator.compare(sequence1, sequence2);
        verify(comparator, times(1)).compare(sequence1, sequence2);
    }

    @Test
    public final void testCompareIfComparatorIsNull() {
        StringSequence sequence1 = new StringSequence("foo");
        StringSequence sequence2 = new StringSequence("bar");
        KeyComparator<StringSequence> keyComparator = new KeyComparator<>(null);
        int c = keyComparator.compare(sequence1, sequence2);
        assertEquals("foo".compareTo("bar"), c);
    }

}
package de.mrapp.tries.datastructure;

import de.mrapp.tries.StringTrie;
import de.mrapp.tries.Trie;
import de.mrapp.tries.sequence.StringSequence;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class StringTrieWrapperTest {

    private StringTrieWrapper<String> trieWrapper;

    private Trie<StringSequence, String, String> trie;

    @SuppressWarnings("unchecked")
    @Before
    public final void before() {
        trie = mock(Trie.class);
        trieWrapper = new StringTrieWrapper<>(trie);
    }

    @Test
    public final void testSize() {
        int size = 2;
        when(trie.size()).thenReturn(size);
        assertEquals(size, trieWrapper.size());
    }

    @Test
    public final void testIsEmpty() {
        boolean empty = true;
        when(trie.isEmpty()).thenReturn(empty);
        assertEquals(empty, trieWrapper.isEmpty());
    }

    @Test
    public final void testContainsKey() {
        String key = "key";
        boolean containsKey = true;
        when(trie.containsKey(new StringSequence(key))).thenReturn(containsKey);
        assertEquals(containsKey, trieWrapper.containsKey(key));
    }

    @Test
    public final void testContainsValue() {
        String value = "value";
        boolean containsValue = true;
        when(trie.containsValue(value)).thenReturn(containsValue);
        assertEquals(containsValue, trieWrapper.containsValue(value));
    }

    @Test
    public final void testGet() {
        String key = "key";
        String value = "value";
        when(trie.get(new StringSequence(key))).thenReturn(value);
        assertEquals(value, trieWrapper.get(key));
    }

    @Test
    public final void testPut() {
        String key = "key";
        String value = "value";
        String previous = "previous";
        when(trie.put(new StringSequence(key), value)).thenReturn(previous);
        assertEquals(previous, trieWrapper.put(key, value));
    }

    @Test
    public final void testRemove() {
        String key = "key";
        String value = "value";
        when(trie.remove(new StringSequence(key))).thenReturn(value);
        assertEquals(value, trieWrapper.remove(key));
    }

    @Test
    public final void testPutAll() {
        String key1 = "key1";
        String key2 = "key2";
        String value1 = "value1";
        String value2 = "value2";
        Map<String, String> map = new HashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        trieWrapper.putAll(map);
        verify(trie, times(1)).put(new StringSequence(key1), value1);
        verify(trie, times(1)).put(new StringSequence(key2), value2);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testPutAllThrowsExceptionIfMapIsNull() {
        trieWrapper.putAll(null);
    }

    @Test
    public final void testClear() {
        trieWrapper.clear();
        verify(trie, times(1)).clear();
    }

    @Test
    public final void testKeySet() {
        String key1 = "key1";
        String key2 = "key2";
        Set<StringSequence> keySet = new HashSet<>();
        keySet.add(new StringSequence(key1));
        keySet.add(new StringSequence(key2));
        when(trie.keySet()).thenReturn(keySet);
        Set<String> actualKeySet = trieWrapper.keySet();
        assertEquals(keySet.size(), actualKeySet.size());
        assertTrue(keySet.stream().allMatch(x -> actualKeySet.contains(x.toString())));
    }

    @Test
    public final void testValues() {
        Collection<String> values = new LinkedList<>();
        values.add("value1");
        values.add("value2");
        when(trie.values()).thenReturn(values);
        assertEquals(values, trieWrapper.values());
    }

    @Test
    public final void testEntrySet() {
        String key1 = "key1";
        String key2 = "key2";
        String value1 = "value1";
        String value2 = "value2";
        Set<Map.Entry<StringSequence, String>> entrySet = new HashSet<>();
        entrySet.add(new AbstractMap.SimpleImmutableEntry<>(
                new StringSequence(key1), value1));
        entrySet.add(new AbstractMap.SimpleImmutableEntry<>(
                new StringSequence(key2), value2));
        when(trie.entrySet()).thenReturn(entrySet);
        Set<Map.Entry<String, String>> actualEntrySet = trieWrapper.entrySet();
        assertTrue(entrySet.stream().allMatch(x -> actualEntrySet
                .contains(
                        new AbstractMap.SimpleImmutableEntry<String, String>(x.getKey().toString(),
                                x.getValue()))));
    }

    @SuppressWarnings("unchecked")
    @Test
    public final void testSubTree() {
        String key = "key";
        Trie<StringSequence, String, String> subTrie = mock(Trie.class);
        when(trie.subTree(new StringSequence(key))).thenReturn(subTrie);
        StringTrie<String> subStringTrie = trieWrapper.subTree(key);
        assertTrue(subStringTrie instanceof StringTrieWrapper);
        assertEquals(subTrie, ((StringTrieWrapper) subStringTrie).trie);
    }

}
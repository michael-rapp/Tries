package de.mrapp.tries;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests the functionality of the class {@link StringHashTrie}.
 *
 * @author Michael Rapp
 */
public class StringHashTrieTest {

    @Test
    public final void testToString() {
        StringHashTrie<String> trie = new StringHashTrie<>();
        trie.put("key1", "value1");
        trie.put("key2", "value2");
        assertEquals("StringHashTrie [key1=value1, key2=value2]", trie.toString());
    }

}
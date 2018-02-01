# Tries - RELEASE NOTES

## Version 1.0.1 (Feb. 1st 2018)

A bugfix release, which fixes the following issues:

- Fixed an issue, which may cause the sub tries of a `PatriciaTrie` or `PatriciaStringTrie` to lack some values.

## Version 1.0.0 (Jan. 25th 2018)

The first stable release of the library, which provides the following features:

- The class `HashTrie` (and `HashStringTrie` respectively) implements an unsorted trie
- The classes `SortedListTrie` and `PatriciaTrie` (and `SortedListStringTrie` or `PatriciaStringTrie` respectively) implement sorted tries
- The utility class `Tries` provides static methods for creating empty, singleton or unmodifiable tries.
# Tries - RELEASE NOTES

## Version 1.0.3 (Aug. 1st 2019)

A minor release, which introduces the following changes:

- Updated dependency "JavaUtil" to version 2.4.0.
- Updated dependency "Annotations" to version 17.0.
- Updated dependency "Mockito" to version 3.0.0.

## Version 1.0.2 (Feb. 8th 2018)

A bugfix release, which fixes the following issues:

- Fixed another issue, which may cause the sub tries of a `PatriciaTrie` or `PatriciaStringTrie` to lack some values.
- Fixed no `NoSuchElementException` being thrown when passing an invalid sequence to the `subTrie`-method of a `PatriciaTrie` or `PatriciaStringTrie`. 

## Version 1.0.1 (Feb. 2nd 2018)

A bugfix release, which fixes the following issues:

- Fixed an issue, which may cause the sub tries of a `PatriciaTrie` or `PatriciaStringTrie` to lack some values.

## Version 1.0.0 (Jan. 30th 2018)

The first stable release of the library, which provides the following features:

- The class `HashTrie` (and `HashStringTrie` respectively) implements an unsorted trie
- The classes `SortedListTrie` and `PatriciaTrie` (and `SortedListStringTrie` or `PatriciaStringTrie` respectively) implement sorted tries
- The utility class `Tries` provides static methods for creating empty, singleton or unmodifiable tries.
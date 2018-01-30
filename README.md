# Tries - README

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) [![Donate](https://img.shields.io/badge/Donate-PayPal-green.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=X75YSLEJV3DWE)

This is a Java library, which provides different implementations of "Tries" (also referred to as "prefix trees", "digital trees" or "radix trees"). Tries are data structures that can be used to store associative arrays where the keys are sequences (e.g. sequences of characters or digits). Tries allow to efficiently search for sequences (and their associated values) that share a common prefix.

The library currently provides the following features:

* Provides an unsorted trie implementation, which stores successors of nodes in hash maps (`HashTrie`).
* Provides a sorted trie implementation, which stores successors of nodes in sorted lists (`SortedListTrie`).
* Provides a Patricia trie implementation (`PatriciaTrie`).
* For each available trie implementation a dedicated variant for using character sequences as keys is available.
* The utility class `Tries` allows to create empty, singleton and unmodifiable instances of all available trie implementations.  

## License Agreement

This project is distributed under the Apache License version 2.0. For further information about this license agreement's content please refer to its full version, which is available at http://www.apache.org/licenses/LICENSE-2.0.txt.

## Download

The latest release of this library can be downloaded as a zip archive from the download section of the project's Github page, which is available [here](https://github.com/michael-rapp/Tries/releases). Furthermore, the library's source code is available as a Git repository, which can be cloned using the URL https://github.com/michael-rapp/Tries.git.

Alternatively, the library can be added to your project as a Gradle dependency by adding the following dependency to the `build.gradle` file:

```groovy
dependencies {
    compile 'com.github.michael-rapp:tries:1.0.0'
}
```

When using Maven, the following dependency can be added to the `pom.xml`:

```xml
<dependency>
    <groupId>com.github.michael-rapp</groupId>
    <artifactId>tries</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Tries

The image below illustrates the structure of an (unsorted) trie. The following key-value pairs (where the keys are character sequences and the values are integers) are stored by the trie:

```
A -> 21
to -> 7
tea -> 13
ted -> 8
ten -> 9
in -> 14
inn -> 16
```  

![](/doc/images/trie_example.png)

As the given example illustrates, all leaf nodes of a trie are associated with values (highlighted in blue). In addition, some of the inner nodes may also correspond to values. The predecessors of a node specify the sequence it corresponds to. E.g. the node that corresponds to the key "tea" has the predecessors t -> e -> a. The root node corresponds to an empty sequence. Because nodes with a common prefix share the same predecessors, tries provide some kind of compression.

Most importantly, this library provides two interfaces - `Trie` and `SortedTrie`. The first of both interfaces extends the interface `java.util.Map`, whereas the latter extends the interface `java.util.NavigableMap`. Similar to the map implementations the Java SDK provides, key-value pairs can be added to a trie using the `put`-method. For retrieving values by their key, the `get`-method can be used. Whereas the order of keys is not taken into account by unsorted tries, when iterating a sorted trie, the order of the keys preserved. The following table provides an overview of the different implementations of the interfaces `Trie` and `SortedTrie`:

| Interface                              | Implementations                                 | Description                                                                                                                                                                                |
|----------------------------------------|-------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `Trie<SequenceType, ValueType>`        | `HashTrie<SequenceType, ValueType>`             | An unsorted trie, which stores the successors of nodes in hash maps. This enables to lookup keys with linear complexity.                                                                   |
| `StringTrie<ValueType>`                | `HashStringTrie<ValueType>`                     | The pendant of the class `HashTrie` for using character sequences, i.e. Strings, as keys.                                                                                                  |
| `SortedTrie<SequenceType, ValueType>`  | `SortedListTrie<SequenceType, ValueType>`       | A sorted trie, which stores the successors of nodes in sorted lists. As binary searches are used to search for successors, looking up keys comes at logarithmic costs.                     |
|                                        | `PatriciaTrie<SequenceType, ValueType>`         | A sorted trie similar to a `SortedListTrie`, where edges between nodes do not always correspond to a single element. Subsequent nodes with a single successor are merged to a single node. |
| `SortedStringTrie<ValueType>`          | `SortedListStringTrie<ValueType>`               | The pendant of the class `SortedListTrie` for using character sequences, i.e. Strings, as keys.                                                                                            |
|                                        | `PatriciaStringTrie<SequenceType, ValueType>`   | The pendant of the class `PatriciaTrie` for using character sequences, i.e. Strings, as keys.                                                                                              |

Whereas the values of a trie can be of an arbitrary type (referred to as the generic type `ValueType`), the type of the keys (referred to as `SequenceType`) must implement the interface `Sequence`. The tries for storing character sequences internally use the class `StringSequence`, which implements that interface. The following example illustrates how key-value pairs can be added and looked up using a generic `HashTrie`.  

```java
Trie<StringSequence, Integer> trie = new HashTrie<>();
trie.put(new StringSequence("A"), 21);
trie.put(new StringSequence("to"), 7);
// ...
int value = trie.get(new StringSequence("to"));
```

Using a `StringTrie` instead of a `Trie` simplifies the handling of keys:

```java
StringTrie<Integer> trie = new HashStringTrie<>();
trie.put("A", 21);
trie.put("to", 7);
// ...
int value = trie.get("to");
```

The nodes of a trie are implemented as classes implementing the interface `Node`. If necessary, the root node of a trie can be retrieved by using the `getRootNode`-method. Although the returned node may not be modified, this enables to traverse the tree structure of the trie:

```java
Node<StringSequence, Integer> rootNode = trie.getRootNode();

if (rootNode.hasSuccessors()) {
    for (StringSequence successorKey : rootNote)
        Node<StringSequence, Integer> successor = rootNode.getSuccessor(successorKey);
        // ...
    }
}
```

The interfaces [`Map`](https://docs.oracle.com/javase/8/docs/api/java/util/Map.html), respectively [`NavigableMap`](https://docs.oracle.com/javase/8/docs/api/java/util/NavigableMap.html), which are extends by the interfaces `Trie` and `SortedTrie` provide various methods for modifying a trie or retrieving its key or values. For a more detailed documentation of these interfaces, please refer to their API documentation. In addition to the methods, which are provides by these interfaces, each trie provides a `subTrie`-method. It enables to create a new trie from an existing one, which will only contain a subset of the original key-value pairs. In the example below, the resulting sub trie only contains keys that start with the element "t".

```java
Trie<StringSequence, Integer> subTrie = trie.subTrie(new StringSequence("t"));
```

## Patricia Tries

Patricia tries use a structure, which is optimized in terms of the required space. Unlike a `HashTrie` or a `SortedListTrie`, the edges between a `PatriciaTrie`'s nodes do not always correspond to a single element. Instead, subsequent nodes that only have a single successor are merged into a single node to reduce space complexity. As this requires to reorganize the tree structure when inserting or removing elements, patricia tries should be preferred over a `SortedListTrie` if elements are only added or removed sporadically and optimizing memory consumption is important. 

The image below illustrates the structure of a Patricia trie, which contains the following key value-pairs. 

```
roman -> 7
romane -> 13
romanus -> 8
romulus -> 9
ruber -> 17
rubicon -> 14
```  

![](/doc/images/patricia_trie_example.png)

## Utility methods

Similar to the Java SDK's class `java.util.Collections`, the class `Tries` provides various static utility methods regarding tries. By using the following methods, empty and unmodifiable trie can be created:

```java
Trie<SequenceType, ValueType> trie = Tries.emptyTrie();
StringTrie<ValueType> stringTrie = Tries.emptyStringTrie();
SortedTrie<SequenceType, ValueType> sortedTrie = Tries.emptySortedTrie();
SortedStringTrie<ValueType> sortedStringTrie = Tries.emptySortedStringTrie();
```

Furthermore, the class `Tries` provides the following methods to create unmodifiable tries that consist of a single entry:

```java
Trie<SequenceType, ValueType> trie = Tries.singletonTrie(key, value);
StringTrie<ValueType> stringTrie = Tries.singletonStringTrie(key, value);
SortedTrie<SequenceType, ValueType> sortedTrie = Tries.singletonSortedTrie(key, value);
SortedStringTrie<ValueType> sortedStringTrie = Tries.singletonSortedStringTrie(key, value);
``` 

In order to create an unmodifiable instance of an existing trie, the following utility methods can be used. This enables to return a trie from an API, which only provides read-only access to some data.

```java
Trie<SequenceType, ValueType> unmodifiableTrie = Trie.unmodifiableTrie(trie);
StringTrie<ValueType> unmodifiableStringTrie = Trie.unmodifiableStringTrie(stringTrie);
SortedTrie<SequenceType, ValueType> unmodifiableSortedTrie = Trie.unmodifiableSortedTrie(sortedTrie);
SortedStringTrie<ValueType> unmodifiableSortedStringTrie = Trie.unmodifiableSortedStringTrie(sortedStringTrie);
```
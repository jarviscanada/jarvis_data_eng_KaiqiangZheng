package ca.jrvs.apps.practice.dataStructure.map;

import java.util.*;

public class HashJMap<K, V> implements JMap<K, V> {

  static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // 16
  static final float DEFAULT_LOAD_FACTOR = 0.75f;

  final float loadFactor;
  Node<K, V>[] table;
  Set<Map.Entry<K, V>> entrySet;
  int size;
  int threshold;

  public HashJMap() {
    this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
  }

  public HashJMap(int initialCapacity, float loadFactor) {
    if (initialCapacity < 0)
      throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
    if (loadFactor <= 0 || Float.isNaN(loadFactor))
      throw new IllegalArgumentException("Illegal load factor: " + loadFactor);

    // ensure power of 2 capacity
    int cap = 1;
    while (cap < initialCapacity) cap <<= 1;

    this.loadFactor = loadFactor;
    this.threshold = (int) (cap * loadFactor);
    this.table = new Node[cap];
    this.entrySet = new HashSet<>();
  }

  // -----------------------------
  // Hash helpers
  // -----------------------------
  private int hash(Object key) {
    int h = key.hashCode();
    return h ^ (h >>> 16);  // spread bits (same as HashMap)
  }

  private int indexFor(int hash, int length) {
    return hash & (length - 1);
  }

  // -----------------------------
  // PUT
  // -----------------------------
  @Override
  public V put(K key, V value) {
    if (key == null || value == null)
      throw new NullPointerException("Null key or value not allowed");

    if (table == null) {
      table = new Node[DEFAULT_INITIAL_CAPACITY];
      threshold = (int) (DEFAULT_INITIAL_CAPACITY * loadFactor);
    }

    int hash = hash(key);
    int index = indexFor(hash, table.length);

    Node<K, V> head = table[index];
    Node<K, V> curr = head;

    // 1. check if key exists ¡ú update
    while (curr != null) {
      if (curr.hash == hash && curr.key.equals(key)) {
        V old = curr.value;
        curr.value = value;
        return old;
      }
      curr = curr.next;
    }

    // 2. Insert new node at head (classic hashmap chaining)
    Node<K, V> newNode = new Node<>(hash, key, value, head);
    table[index] = newNode;
    size++;

    entrySet.add(newNode);

    // 3. Rehash if needed
    if (size > threshold) {
      resize();
    }

    return null;
  }

  // -----------------------------
  // RESIZE AND REHASH
  // -----------------------------
  private void resize() {
    int newCap = table.length << 1; // double size
    Node<K, V>[] newTable = new Node[newCap];

    // rehash all entries
    for (Map.Entry<K, V> entry : entrySet) {
      Node<K, V> n = (Node<K, V>) entry;
      int newIndex = indexFor(n.hash, newCap);

      n.next = newTable[newIndex];
      newTable[newIndex] = n;
    }

    table = newTable;
    threshold = (int) (newCap * loadFactor);
  }

  // -----------------------------
  // containsKey
  // -----------------------------
  @Override
  public boolean containsKey(Object key) {
    if (key == null)
      throw new NullPointerException("Null key not allowed");

    int hash = hash(key);
    int index = indexFor(hash, table.length);

    Node<K, V> curr = table[index];
    while (curr != null) {
      if (curr.hash == hash && curr.key.equals(key)) return true;
      curr = curr.next;
    }

    return false;
  }

  // -----------------------------
  // get
  // -----------------------------
  @Override
  public V get(Object key) {
    if (key == null)
      throw new NullPointerException("Null key not allowed");

    int hash = hash(key);
    int index = indexFor(hash, table.length);

    Node<K, V> curr = table[index];
    while (curr != null) {
      if (curr.hash == hash && curr.key.equals(key)) return curr.value;
      curr = curr.next;
    }

    return null;
  }

  // -----------------------------
  // Size
  // -----------------------------
  @Override
  public int size() {
    return size;
  }

  // -----------------------------
  // Entry set
  // -----------------------------
  @Override
  public Set<Map.Entry<K, V>> entrySet() {
    return entrySet;
  }

  // -----------------------------
  // Node class
  // -----------------------------
  static class Node<K, V> implements Map.Entry<K, V> {

    final int hash;
    final K key;
    V value;
    HashJMap.Node<K, V> next;

    Node(int hash, K key, V value, HashJMap.Node<K, V> next) {
      this.hash = hash;
      this.key = key;
      this.value = value;
      this.next = next;
    }

    public final K getKey() {
      return key;
    }

    public final V getValue() {
      return value;
    }

    public final int hashCode() {
      return Objects.hashCode(key) ^ Objects.hashCode(value);
    }

    public final V setValue(V newValue) {
      V oldValue = value;
      value = newValue;
      return oldValue;
    }

    public final boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      if (o instanceof Map.Entry) {
        Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
        if (Objects.equals(key, e.getKey()) &&
            Objects.equals(value, e.getValue())) {
          return true;
        }
      }
      return false;
    }

    @Override
    public String toString() {
      return key + "=" + value;
    }
  }
}

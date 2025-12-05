package ca.jrvs.apps.practice.dataStructure.set;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HashJSet<E> implements JSet<E> {

  private static final int DEFAULT_CAPACITY = 16;
  private static final float LOAD_FACTOR = 0.75f;

  private List<E>[] buckets;
  private int size = 0;

  public HashJSet() {
    this(DEFAULT_CAPACITY);
  }

  @SuppressWarnings("unchecked")
  public HashJSet(int capacity) {
    buckets = new List[capacity];
  }

  private int hash(Object key) {
    return (key.hashCode() & 0x7fffffff) % buckets.length;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean contains(Object o) {
    if (o == null) throw new NullPointerException("null is not allowed");

    int index = hash(o);
    List<E> bucket = buckets[index];
    if (bucket == null) return false;

    return bucket.contains(o);
  }

  @Override
  public boolean add(E e) {
    if (e == null) throw new NullPointerException("null is not allowed");

    if ((size + 1.0) / buckets.length > LOAD_FACTOR) {
      resize();
    }

    int index = hash(e);
    if (buckets[index] == null) {
      buckets[index] = new ArrayList<>();
    }

    List<E> bucket = buckets[index];
    if (bucket.contains(e)) return false; // already exists

    bucket.add(e);
    size++;
    return true;
  }

  @Override
  public boolean remove(Object o) {
    if (o == null) throw new NullPointerException("null is not allowed");

    int index = hash(o);
    List<E> bucket = buckets[index];
    if (bucket == null) return false;

    boolean removed = bucket.remove(o);
    if (removed) size--;
    return removed;
  }

  @Override
  public void clear() {
    for (int i = 0; i < buckets.length; i++) {
      buckets[i] = null;
    }
    size = 0;
  }

  @SuppressWarnings("unchecked")
  private void resize() {
    List<E>[] oldBuckets = buckets;
    buckets = new List[oldBuckets.length * 2];
    size = 0;

    for (List<E> bucket : oldBuckets) {
      if (bucket != null) {
        for (E element : bucket) {
          add(element); // rehash
        }
      }
    }
  }
}

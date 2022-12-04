package hello.world.util;

import java.util.*;

public class RingBuffer<T> extends AbstractList<T> implements Deque<T> {
    private int capacity=0;
    private int size=0;
    private int firstIndex=0;
    private int lastIndex=0;
    private Object[] innerBuffer=null;
    public RingBuffer(int capacity){
        setCapacity(capacity);
    }

    public void setCapacity(int capacity){
        this.capacity=(int) Math.round(Math.pow(2,Math.floor(Math.log(capacity)/Math.log(2) + 1))) - 1;
        innerBuffer = new Object[this.capacity+1];

    }

    private int getRawIndex(int index){
        return (firstIndex+index)&capacity;
    }

    @Override
    public void addFirst(T item) {
        if (size==capacity)throw new IllegalStateException();
        if (item==null)throw new NullPointerException();
        innerBuffer[getRawIndex(-1)]=item;
        firstIndex=getRawIndex(-1);
        size++;
    }

    @Override
    public void addLast(T item) {
        if (size>capacity) pollFirst();
        if (item==null)throw new NullPointerException();
        innerBuffer[getRawIndex(size)]=item;
        lastIndex=getRawIndex(size);
        size++;
    }

    @Override
    public boolean offerFirst(T item) {
        if (size==capacity)return false;
        addFirst(item);
        return true;
    }

    @Override
    public boolean offerLast(T item) {
        if (size>capacity){
            removeFirst();
        };
        addLast(item);
        return true;
    }

    @Override
    public T removeFirst() {
        if (size==0)throw new NoSuchElementException();
        T item= (T) innerBuffer[getRawIndex(0)];
        innerBuffer[getRawIndex(0)]=null;
        firstIndex=getRawIndex(1);
        size--;
        return item;
    }

    @Override
    public T removeLast() {
        if (size==0)throw new NoSuchElementException();
        T item=(T) innerBuffer[getRawIndex(size-1)];
        innerBuffer[getRawIndex(size-1)]=null;
        firstIndex=getRawIndex(size-2);
        size--;
        return null;
    }

    @Override
    public T pollFirst() {
        if (size==0)return null;
        return removeFirst();
    }

    @Override
    public T pollLast() {
        if (size==0)return null;
        return removeLast();
    }

    @Override
    public T getFirst() {
        if (size==0)throw new NoSuchElementException();
        return (T) innerBuffer[getRawIndex(0)];
    }

    @Override
    public T getLast() {
        if (size==0)throw new NoSuchElementException();
        return (T) innerBuffer[getRawIndex(size-1)];
    }

    @Override
    public T peekFirst() {
        if (size==0)return null;
        return (T) innerBuffer[getRawIndex(0)];
    }

    @Override
    public T peekLast() {
        if (size==0)return null;
        return (T) innerBuffer[getRawIndex(size-1)];
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        for (int i = 0; i < size; i++) {
            T item= (T) innerBuffer[getRawIndex(i)];
            if (o.equals(item)){
                for (int j = i; j < size-1; j++) {
                    innerBuffer[getRawIndex(j)]=innerBuffer[getRawIndex(j+1)];
                    innerBuffer[getRawIndex(j+1)]=null;
                }
                size--;
                lastIndex=getRawIndex(size);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        for (int i = size-1; i >= 0; i--) {
            T item=(T) innerBuffer[getRawIndex(i)];
        }
        //TODO
        return false;
    }

    @Override
    public boolean add(T item) {
        addLast(item);
        return true;
    }

    @Override
    public T get(int index) {
        if (index<0 || size<=index)throw new IllegalArgumentException();
        return (T) innerBuffer[getRawIndex(index)];
    }

    @Override
    public boolean offer(T item) {
        if (size>capacity) {
            removeFirst();
        };
        return offerLast(item);
    }

    @Override
    public T remove() {
        return removeFirst();
    }

    @Override
    public T poll() {
        return pollFirst();
    }

    @Override
    public T element() {
        return getFirst();
    }

    @Override
    public T peek() {
        return peekFirst();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        for (T item:c) {
            offerLast(item);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        for (Object item:c) {
            remove(item);
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            innerBuffer[getRawIndex(i)]=null;
        }
        firstIndex=0;
        lastIndex=0;
        size=0;
    }

    @Override
    public void push(T item) {
        addFirst(item);
    }

    @Override
    public T pop() {
        return removeFirst();
    }

    @Override
    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        for (Iterator<T> it = iterator(); it.hasNext(); ) {
            T item = it.next();
            if (item.equals(o))return true;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size==0;
    }

    @Override
    public Iterator<T> iterator() {
        return new RingBufferIterator<>();
    }

    @Override
    public Object[] toArray() {
        Object[] array=new Object[size];
        for (int i = 0; i < size; i++) {
            array[i]=innerBuffer[getRawIndex(i)];
        }
        return array;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;
    }

    @Override
    public Iterator<T> descendingIterator() {
        return new RingBufferDescendingIterator<>();
    }
    private class RingBufferIterator<T> implements Iterator<T> {
        int index=-1;
        @Override
        public boolean hasNext() {
            return size-1>index;
        }
        @Override
        public T next() {
            return (T) innerBuffer[getRawIndex(++index)];
        }
    }
    private class RingBufferDescendingIterator<T> implements Iterator<T>{
        int index=-1;
        @Override
        public boolean hasNext() {
            return size-1>index;
        }
        @Override
        public T next() {
            index++;
            return (T) innerBuffer[getRawIndex(size-1 - index)];
        }
    }
}
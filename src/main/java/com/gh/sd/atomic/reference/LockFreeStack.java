package com.gh.sd.atomic.reference;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

/**
 * A thread-safe lock-free stack implementation using atomic references.
 * This implementation provides non-blocking push and pop operations.
 *
 * @param <T> the type of elements held in this stack
 */
public class LockFreeStack<T> {
    private final AtomicReference<StackNode<T>> head = new AtomicReference<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    /**
     * Pushes an element onto the top of this stack.
     *
     * @param value the element to push
     */
    public void push(T value) {
        StackNode<T> newHeadNode = new StackNode<>(value);

        while (true) {
            StackNode<T> currentHeadNode = head.get();
            newHeadNode.next = currentHeadNode;
            if (head.compareAndSet(currentHeadNode, newHeadNode)) {
                break;
            }
            LockSupport.parkNanos(1);
        }
        counter.incrementAndGet();
    }

    /**
     * Removes and returns the element at the top of this stack.
     *
     * @return an Optional containing the element at the top of this stack,
     *         or an empty Optional if this stack is empty
     */
    public Optional<T> pop() {
        StackNode<T> currentHeadNode = head.get();
        StackNode<T> newHeadNode;

        while (currentHeadNode != null) {
            newHeadNode = currentHeadNode.next;
            if (head.compareAndSet(currentHeadNode, newHeadNode)) {
                break;
            }
            LockSupport.parkNanos(1);
            currentHeadNode = head.get();
        }
        counter.incrementAndGet();
        return Optional.ofNullable(currentHeadNode).map(node -> node.value);
    }

    /**
     * Returns the total number of operations performed on this stack.
     *
     * @return the operation counter value
     */
    public int getCounter() {
        return counter.get();
    }

    /**
     * Node class for the stack implementation.
     *
     * @param <T> the type of value held in this node
     */
    private static class StackNode<T> {
        private final T value;
        private StackNode<T> next;

        StackNode(T value) {
            this.value = value;
        }
    }
} 
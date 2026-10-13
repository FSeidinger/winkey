package de.do9fse.winkey.lib.core.port.out;

import de.do9fse.winkey.lib.core.model.Job;

/**
 * The supported subset of queue operations for the job queue.
 */
public interface JobQueue {
    /**
     * Retrieves, but does not remove, the head of this queue, or returns null
     * if this queue is empty.
     *
     * @return the head of this queue, or null if this queue is empty
     */
    Job peek();

    /**
     * Retrieves and removes the head of this queue, or returns null if this queue is empty.
     * 
     * @return the head of this queue, or null if this queue is empty
     */
    Job poll();

    /**
     * Inserts the specified job into this queue
     * 
     * @param job the element to add
     */
    boolean offer(final Job job);

    /**
     * Retrieves and removes the last element of this queue, or returns null if this queue is empty.
     * 
     * @return the tail of this queue, or null if this queue is empty
     */
    Job pollLast();

    /**
     * Completes exceptionally and removes all queued jobs
     *
     * @param e The exception to exceptionally complete all queued jobs
     */
    void failPendingJobs(final Exception e);

    /**
     * Locks the queue for the current thread
     *
     * <p>
     * Used to synchronize between the sender and the receiver thread
     * </p>
     */
    void lock();

    /**
     * Unlocks the previously locked queue
     */
    void unlock();
}

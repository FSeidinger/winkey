package de.do9fse.winkey.lib.infrastructure.winkey;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import de.do9fse.winkey.lib.core.model.Job;
import de.do9fse.winkey.lib.core.model.commands.host.immediate.BackspaceCommand;
import de.do9fse.winkey.lib.core.model.responses.CommandResponse;
import de.do9fse.winkey.lib.core.port.out.JobQueue;

class DefaultJobQueueTest {
    @Test
    void supportsTheWinKeyQueueContractIncludingRemovingTheTail() {
        final JobQueue queue = new DefaultJobQueue();
        final Job firstJob = newJob();
        final Job lastJob = newJob();

        assertNull(queue.peek());
        assertNull(queue.poll());
        assertNull(queue.pollLast());

        assertTrue(queue.offer(firstJob));
        assertTrue(queue.offer(lastJob));
        assertSame(firstJob, queue.peek());
        assertSame(lastJob, queue.pollLast());
        assertSame(firstJob, queue.poll());
        assertNull(queue.pollLast());
    }

    @Test
    void lockPreventsTheReceiverFromPollingUntilTheSenderUnlocks() throws Exception {
        final JobQueue queue = new DefaultJobQueue();
        final Job job = newJob();
        final CountDownLatch pollStarted = new CountDownLatch(1);
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        queue.offer(job);
        final Future<Job> polledJob;

        queue.lock();
        try {
            polledJob = executor.submit(() -> {
                pollStarted.countDown();
                return queue.poll();
            });

            assertTrue(pollStarted.await(1, TimeUnit.SECONDS));
            assertFalse(polledJob.isDone());
        } finally {
            queue.unlock();
        }

        try {
            assertSame(job, polledJob.get(1, TimeUnit.SECONDS));
        } finally {
            executor.shutdownNow();
        }
    }

    private static Job newJob() {
        return new Job(new BackspaceCommand(), new CompletableFuture<CommandResponse>());
    }
}

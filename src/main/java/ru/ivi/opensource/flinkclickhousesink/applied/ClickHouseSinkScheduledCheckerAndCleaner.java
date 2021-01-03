package ru.ivi.opensource.flinkclickhousesink.applied;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ivi.opensource.flinkclickhousesink.model.ClickHouseSinkCommonParams;
import ru.ivi.opensource.flinkclickhousesink.util.ThreadUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Predicate;

public class ClickHouseSinkScheduledCheckerAndCleaner implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(ClickHouseSinkScheduledCheckerAndCleaner.class);

    private final ScheduledExecutorService scheduledExecutorService;
    private final List<ClickHouseSinkBuffer> clickHouseSinkBuffers = new ArrayList<>();
    private final List<CompletableFuture<Boolean>> futures;
    private final Predicate<CompletableFuture<Boolean>> filter;

    public ClickHouseSinkScheduledCheckerAndCleaner(ClickHouseSinkCommonParams props, List<CompletableFuture<Boolean>> futures) {
        this.futures = futures;
        this.filter = getFuturesFilter(props.isIgnoringClickHouseSendingExceptionEnabled());
        ThreadFactory factory = ThreadUtil.threadFactory("clickhouse-writer-checker-and-cleaner");
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(factory);
        // 通过线程池来写时遍历任务池

        // 第一个command参数是任务实例，
        // 第二个initialDelay参数是初始换延迟时间，
        // 第三个delay参数是延迟间隔时间，
        // 第四个unit参数是时间单元
        scheduledExecutorService.scheduleWithFixedDelay(getTask(), props.getTimeout(), props.getTimeout(), TimeUnit.SECONDS);
        logger.info("Build Sink scheduled checker, timeout (sec) = {}", props.getTimeout());
    }

    public void addSinkBuffer(ClickHouseSinkBuffer clickHouseSinkBuffer) {
        synchronized (this) {
            clickHouseSinkBuffers.add(clickHouseSinkBuffer);
        }
        logger.debug("Add sinkBuffer, target table = {}", clickHouseSinkBuffer.getTargetTable());
    }

    private Runnable getTask() {
        return () -> {
            synchronized (this) {
                logger.debug("Start checking buffers and cleanup futures: Before cleanup = {}." +
                        " Current count of buffers = {}", futures.size(), clickHouseSinkBuffers.size());
                futures.removeIf(filter);
                clickHouseSinkBuffers.forEach(ClickHouseSinkBuffer::tryAddToQueue);
            }
        };
    }

    private static Predicate<CompletableFuture<Boolean>> getFuturesFilter(boolean ignoringExceptionEnabled) {
        if (ignoringExceptionEnabled) {
            return CompletableFuture::isDone;
        } else {
            return f -> f.isDone() && !f.isCompletedExceptionally();
        }
    }

    @Override
    public void close() throws Exception {
        logger.info("ClickHouseSinkScheduledCheckerAndCleaner is shutting down.");
        ThreadUtil.shutdownExecutorService(scheduledExecutorService);
        logger.info("ClickHouseSinkScheduledCheckerAndCleaner shutdown complete.");
    }
}

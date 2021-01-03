package ru.ivi.opensource.flinkclickhousesink.model;

import com.google.common.base.Preconditions;

import java.util.Map;

import static ru.ivi.opensource.flinkclickhousesink.model.ClickHouseSinkConst.*;

// 数据写入ClickHouse 共公参数类
public class ClickHouseSinkCommonParams {

    private final ClickHouseClusterSettings clickHouseClusterSettings;

    private final String failedRecordsPath; // 失败时，记录存放路径
    private final int numWriters; // 写工作线程数
    private final int queueMaxCapacity;
    private final boolean ignoringClickHouseSendingExceptionEnabled;

    private final int timeout; // 线程池中定时检测任务时间间隔，单位：秒
    private final int maxRetries;

    public ClickHouseSinkCommonParams(Map<String, String> params) {
        Preconditions.checkNotNull(params.get(IGNORING_CLICKHOUSE_SENDING_EXCEPTION_ENABLED),
                "Parameter " + IGNORING_CLICKHOUSE_SENDING_EXCEPTION_ENABLED + " must be initialized");

        this.clickHouseClusterSettings = new ClickHouseClusterSettings(params);
        this.numWriters = Integer.parseInt(params.get(NUM_WRITERS));
        this.queueMaxCapacity = Integer.parseInt(params.get(QUEUE_MAX_CAPACITY));
        this.maxRetries = Integer.parseInt(params.get(NUM_RETRIES));
        this.timeout = Integer.parseInt(params.get(TIMEOUT_SEC));
        this.failedRecordsPath = params.get(FAILED_RECORDS_PATH);
        this.ignoringClickHouseSendingExceptionEnabled = Boolean.parseBoolean(params.get(IGNORING_CLICKHOUSE_SENDING_EXCEPTION_ENABLED));

        Preconditions.checkNotNull(failedRecordsPath);
        Preconditions.checkArgument(queueMaxCapacity > 0);
        Preconditions.checkArgument(numWriters > 0);
        Preconditions.checkArgument(timeout > 0);
        Preconditions.checkArgument(maxRetries > 0);
    }

    public int getNumWriters() {
        return numWriters;
    }

    public int getQueueMaxCapacity() {
        return queueMaxCapacity;
    }

    public boolean isIgnoringClickHouseSendingExceptionEnabled() {
        return ignoringClickHouseSendingExceptionEnabled;
    }

    public ClickHouseClusterSettings getClickHouseClusterSettings() {
        return clickHouseClusterSettings;
    }

    public int getTimeout() {
        return timeout;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public String getFailedRecordsPath() {
        return failedRecordsPath;
    }

    @Override
    public String toString() {
        return "ClickHouseSinkCommonParams{" +
                "clickHouseClusterSettings=" + clickHouseClusterSettings +
                ", failedRecordsPath='" + failedRecordsPath + '\'' +
                ", numWriters=" + numWriters +
                ", queueMaxCapacity=" + queueMaxCapacity +
                ", ignoringClickHouseSendingExceptionEnabled=" + ignoringClickHouseSendingExceptionEnabled +
                ", timeout=" + timeout +
                ", maxRetries=" + maxRetries +
                '}';
    }
}

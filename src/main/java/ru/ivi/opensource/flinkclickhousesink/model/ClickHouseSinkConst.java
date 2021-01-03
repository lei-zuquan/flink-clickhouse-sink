package ru.ivi.opensource.flinkclickhousesink.model;

// 写入Clickhouse 常量类
public final class ClickHouseSinkConst {
    private ClickHouseSinkConst() {
    }

    // clickhouse 目录表名
    public static final String TARGET_TABLE_NAME = "clickhouse.sink.target-table";
    // 本地内存缓冲区大小，即缓存多少行数据
    public static final String MAX_BUFFER_SIZE = "clickhouse.sink.max-buffer-size";
    // 写工作线程数
    public static final String NUM_WRITERS = "clickhouse.sink.num-writers";
    // 队列的最大容量（批次）
    public static final String QUEUE_MAX_CAPACITY = "clickhouse.sink.queue-max-capacity";
    // 线程池中定时检测任务时间间隔，单位：秒
    public static final String TIMEOUT_SEC = "clickhouse.sink.timeout-sec";
    // 重试次数
    public static final String NUM_RETRIES = "clickhouse.sink.retries";
    // 数据存储至clickhouse失败时，记录保存路径
    public static final String FAILED_RECORDS_PATH = "clickhouse.sink.failed-records-path";
    // 是否忽略写入clickhouse产生的异常
    public static final String IGNORING_CLICKHOUSE_SENDING_EXCEPTION_ENABLED = "clickhouse.sink.ignoring-clickhouse-sending-exception-enabled";
}

/*
 * Fake Apache Package
 */
package org.apache.log4j;

import org.apache.log4j.bridge.AppenderWrapper;
import org.apache.log4j.bridge.LayoutAdapter;
import org.apache.log4j.bridge.LogEventWrapper;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rolling.CompositeTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.RolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;

/**
 * Wrapper project for Rolling File Appender, will wrap all log4j 1.x Rolling
 * File Appender to 2.x
 *
 * @author Zen
 * @version 1.x
 */
public class RollingFileAppender extends AppenderWrapper {
    
    protected static final long FILE_SIZE = 20 * 1024 * 1024;
    protected static final int BACKUP = 10;
    
    protected int maxBackupIndex;
    protected long maximumFileSize;
    protected String maxFileSize;

    /**
     * Returns the value of the <b>MaxBackupIndex</b> option.
     *
     * @return maxBackupIndex - int
     */
    public int getMaxBackupIndex() {
        return maxBackupIndex;
    }

    /**
     * Get the maximum size that the output file is allowed to reach before
     * being rolled over to backup files.
     *
     * @return maxFileSize - int
     */
    public long getMaximumFileSize() {
        return maximumFileSize;
    }
    
    public void rollover() {
        //do nothing, as automatically handling by log4j2 rollingfileappender
    }
    
    public RollingFileAppender(org.apache.logging.log4j.core.Appender appender) {
        super(appender);
        this.setMaxFileSize(FILE_SIZE + "B");
        this.setMaxBackupIndex(BACKUP);
    }
    
    public RollingFileAppender(Layout layout, String filename, boolean append) {
        super(createAppender(filename, layout, filename, true, false, 1000));
    }
    
    public RollingFileAppender(Layout layout, String filename) {
        this(layout, filename, true);
    }
    
    private static org.apache.logging.log4j.core.appender.RollingFileAppender createAppender(
            String name, Layout layout, String filename, boolean append, boolean bufferedIO, int bufferedSize) {
        TriggeringPolicy timePolicy = TimeBasedTriggeringPolicy.newBuilder().withModulate(true).build();
        SizeBasedTriggeringPolicy sizePolicy = SizeBasedTriggeringPolicy.createPolicy(FILE_SIZE + "B");
        CompositeTriggeringPolicy policy = CompositeTriggeringPolicy.createPolicy(sizePolicy, timePolicy);
int numofBackups = 10;
        RolloverStrategy strategy = DefaultRolloverStrategy.newBuilder()
                .withMax(String.valueOf(BACKUP))
                .build();
        return org.apache.logging.log4j.core.appender.RollingFileAppender.newBuilder()
                .setName(name)
                .setLayout(new LayoutAdapter(layout))
                .withAppend(append)
                .withBufferedIo(bufferedIO)
                .withBufferSize(bufferedSize)
                .withImmediateFlush(!bufferedIO)
                .withFileName(filename)
                .withFilePattern(filename +"%d{yyy-MM-dd}")
                .withPolicy(policy)
                .withStrategy(strategy)
                .build();
    }

    /**
     * Rebuild RollingFileAppender object
     *
     * @param fileName - String
     * @param append - boolean
     * @param bufferedIO - boolean
     * @param bufferSize - int
     */
    public synchronized void setFile(String fileName, boolean append,
            boolean bufferedIO, int bufferSize) {
        //unsupported
    }

    /**
     * cannot change rollover strategy after fix
     *
     * @param maxBackups - int
     */
    public void setMaxBackupIndex(int maxBackups) {
        //not supported, die die must be 10
        this.maxBackupIndex = maxBackups;
    }

    /**
     * log4j2 does not allow to change file size after creation
     *
     * @param maxFileSize - long
     */
    public void setMaximumFileSize(long maxFileSize) {
        //not supported - die die must be 20MB
        this.maximumFileSize = maxFileSize;
    }

    /**
     * log4j2 does not allow to change file size after creation
     *
     * @param maxFileSize - long
     */
    public void setMaxFileSize(String maxFileSize) {
        //not supported - die die must be 20MB
        this.maxFileSize = maxFileSize;
    }

    /**
     * @param event - LoggingEvent
     */
    protected void subAppend(LoggingEvent event) {
        getAppender().append(new LogEventWrapper(event));
    }
    
    public void append(LogEvent event) {
        getAppender().append(event);
    }
    
}

/*
 * 
 */
package org.apache.log4j;

import org.apache.log4j.bridge.AppenderWrapper;
import org.apache.log4j.bridge.LayoutAdapter;
import org.apache.log4j.bridge.LogEventWrapper;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.logging.log4j.core.LogEvent;

/**
 * This class is wrapper for org.apache.log4j.FileAppender. It will act like
 * interface for org.apache.logging.log4j.core.appender.FileAppender
 *
 * @author Zen
 * @version 1.0
 */
public class FileAppender extends AppenderWrapper {

    public void rollover() {
        //do nothing, as automatically handling by log4j2 rollingfileappender
    }

    public FileAppender(org.apache.logging.log4j.core.Appender appender) {
        super(appender);
    }

    public FileAppender(Layout layout, String filename, boolean append) {
        super(createAppender(filename, layout, filename, true, false, 1000));
    }

    public FileAppender(Layout layout, String filename) {
        this(layout, filename, true);
    }

    private static org.apache.logging.log4j.core.appender.FileAppender createAppender(
            String name, Layout layout, String filename, boolean append,
            boolean bufferedIO, int bufferedSize) {
        return org.apache.logging.log4j.core.appender.FileAppender.newBuilder()
                .setName(name)
                .setLayout(new LayoutAdapter(layout))
                .withAppend(append)
                .withBufferedIo(bufferedIO)
                .withBufferSize(bufferedSize)
                .withImmediateFlush(!bufferedIO)
                .withFileName(filename)
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
     * @param event - LoggingEvent
     */
    protected void subAppend(LoggingEvent event) {
        getAppender().append(new LogEventWrapper(event));
    }

    public void append(LogEvent event) {
        getAppender().append(event);
    }
}

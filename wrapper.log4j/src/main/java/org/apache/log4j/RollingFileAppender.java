/*
 * Fake Apache Package
 */
package org.apache.log4j;

import com.innovationfantasy.wrapper.others.apache.log4j.LayoutWrapper;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;

/**
 * Wrapper project for Rolling File Appender, will wrap all log4j 1.x Rolling
 * File Appender to 2.x
 *
 * @author Zen
 * @version 1.x
 */
public class RollingFileAppender implements Appender {

    /**
     * The default maximum file size is 10MB.
     */
    protected long maxFileSize = 10 * 1024 * 1024;
    /**
     * There is one backup file by default.
     */
    protected int maxBackupIndex = 1;

    private final org.apache.logging.log4j.core.appender.RollingFileAppender.Builder builder;
    /*
     * place holder for apache log4j2 RollingFileAppender object
     */
    private org.apache.logging.log4j.core.appender.RollingFileAppender appender;

    /**
     * The default constructor simply calls its {@link
     * FileAppender#FileAppender parents constructor}.
     */
    public RollingFileAppender() {
        this.builder = org.apache.logging.log4j.core.appender.RollingFileAppender
                .newBuilder();
        this.appender = this.builder.build();
    }

    /**
     * Instantiate a RollingFileAppender and open the file designated by
     * <code>filename</code>. The opened filename will become the ouput
     * destination for this appender.
     *
     * <p>
     * If the <code>append</code> parameter is true, the file will be appended
     * to. Otherwise, the file designated by <code>filename</code> will be
     * truncated before being opened.
     *
     * @param layout - org.apache.log4j.Layout
     * @param filename - String
     * @param append - boolean
     */
    public RollingFileAppender(Layout layout, String filename, boolean append) {
        this.builder = org.apache.logging.log4j.core.appender.RollingFileAppender
                .newBuilder().withFileName(filename).withAppend(append)
                .setLayout(LayoutWrapper.layout1to2(layout));
        this.appender = this.builder.build();
    }

    /**
     * Instantiate a FileAppender and open the file designated by
     * <code>filename</code>. The opened filename will become the output
     * destination for this appender.
     *
     * <p>
     * The file will be appended to.
     *
     * @param layout - org.apache.log4j.Layout
     * @param filename - String
     */
    public RollingFileAppender(Layout layout, String filename) {
        this.builder = org.apache.logging.log4j.core.appender.RollingFileAppender
                .newBuilder().withFileName(filename).withAppend(true)
                .setLayout(LayoutWrapper.layout1to2(layout));
        this.appender = this.builder.build();
    }

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
        return maxFileSize;
    }

    public void rollover() {
        //do nothing, as automatically handling by log4j2 rollingfileappender
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
        this.builder.withFileName(fileName).withAppend(append)
                .withBufferedIo(bufferedIO).withBufferSize(bufferSize);
        this.appender = this.builder.build();
    }

    /**
     * Set the maximum number of backup files to keep around.
     *
     * <p>
     * The <b>MaxBackupIndex</b> option determines how many backup files are
     * kept before the oldest is erased. This option takes a positive integer
     * value. If set to zero, then there will be no backup files and the log
     * file will be truncated when it reaches <code>MaxFileSize</code>.
     *
     * @param maxBackups - int
     */
    public void setMaxBackupIndex(int maxBackups) {
        this.builder.withStrategy(new DefaultRolloverStrategy.Builder()
                .withMax(String.valueOf(maxBackups)).build());
        this.maxBackupIndex = maxBackups;

    }

    /**
     * Set the maximum size that the output file is allowed to reach before
     * being rolled over to backup files.
     *
     * <p>
     * This method is equivalent to {@link #setMaxFileSize} except that it is
     * required for differentiating the setter taking a <code>long</code>
     * argument from the setter taking a <code>String</code> argument by the
     * JavaBeans {@link
     * java.beans.Introspector Introspector}.
     *
     * @see #setMaxFileSize(String)
     * @param maxFileSize - long
     */
    public void setMaximumFileSize(long maxFileSize) {
        this.maxFileSize = maxFileSize;

    }

    /**
     * Set the maximum size that the output file is allowed to reach before
     * being rolled over to backup files.
     *
     * <p>
     * In configuration files, the <b>MaxFileSize</b> option takes an long
     * integer in the range 0 - 2^63. You can specify the value with the
     * suffixes "KB", "MB" or "GB" so that the integer is interpreted being
     * expressed respectively in kilobytes, megabytes or gigabytes. For example,
     * the value "10KB" will be interpreted as 10240.
     *
     * @param value - Size
     */
    public void setMaxFileSize(String value) {
        this.builder.withPolicy(SizeBasedTriggeringPolicy.createPolicy(value));
        this.appender = this.builder.build();
    }

    /**
     * @param event - LoggingEvent
     */
    protected void subAppend(LoggingEvent event) {
        this.appender.append(event.getLogEvent());
    }

    @Override
    public void addFilter(Filter newFilter) {
        // do nothing
    }

    @Override
    public Filter getFilter() {
        //return this.appender.getFilter();
        return null;
    }

    @Override
    public void clearFilters() {
        //do nothing
    }

    @Override
    public void close() {
        this.appender.stop();
        this.appender = null;
    }

    @Override
    public void doAppend(LoggingEvent event) {
        this.appender.append(event.getLogEvent());
    }

    /**
     * Get the name of this appender.
     *
     * @return name, may be null.
     */
    @Override
    public String getName() {
        return this.builder.getName();
    }

    @Override
    public void setErrorHandler(ErrorHandler errorHandler) {
        //do nothing 
        //this.appender.setHandler(errorHandler);
    }

    @Override
    public void setName(String name) {
        this.builder.setName(name);
    }

    @Override
    public Layout getLayout() {
        return null;
    }

    @Override
    public void setLayout(Layout layout) {
        this.builder.setLayout(LayoutWrapper.layout1to2(layout));
    }

    @Override
    public ErrorHandler getErrorHandler() {
        return null;
    }

    @Override
    public boolean requiresLayout() {
        return true;
    }
}

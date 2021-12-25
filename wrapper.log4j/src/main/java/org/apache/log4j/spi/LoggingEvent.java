/*
 */
package org.apache.log4j.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.Priority;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager.Log4jMarker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.message.ObjectMessage;

/**
 *
 * @author Zen
 */
public class LoggingEvent {

    private static long startTime = System.currentTimeMillis();

    /**
     * The number of milliseconds elapsed from 1/1/1970 until logging event was
     * created.
     */
    public final long timeStamp;
    // Serialization
    static final long serialVersionUID = -868428216207166145L;

    private Category category;
    private LogEvent logEvent;

    /**
     * Instantiate a LoggingEvent from the supplied parameters.
     *
     * <p>
     * Except {@link #timeStamp} all the other fields of
     * <code>LoggingEvent</code> are filled when actually needed.
     * <p>
     * @param fqnOfCategoryClass String
     * @param logger The logger generating this event.
     * @param level The level of this event.
     * @param message The message of this event.
     * @param throwable The throwable of this event.
     */
    public LoggingEvent(String fqnOfCategoryClass, Category logger,
            Priority level, Object message, Throwable throwable) {
        timeStamp = System.currentTimeMillis();
        this.category = logger;
        Marker marker = new Log4jMarker(logger.getName());
        this.logEvent = new Log4jLogEvent(logger.getName(), marker,
                fqnOfCategoryClass,
                org.apache.logging.log4j.Level.toLevel(level.toString()),
                new ObjectMessage(message),
                new ArrayList<>(), throwable);
    }

    /**
     * Instantiate a LoggingEvent from the supplied parameters.
     *
     * <p>
     * Except {@link #timeStamp} all the other fields of
     * <code>LoggingEvent</code> are filled when actually needed.
     * <p>
     * @param fqnOfCategoryClass String
     * @param logger The logger generating this event.
     * @param timeStamp the timestamp of this logging event
     * @param level The level of this event.
     * @param message The message of this event.
     * @param throwable The throwable of this event.
     */
    public LoggingEvent(String fqnOfCategoryClass, Category logger,
            long timeStamp, Priority level, Object message,
            Throwable throwable) {
        this.timeStamp = timeStamp;
        this.category = logger;
        Marker marker = new Log4jMarker(logger.getName());
        this.logEvent = new Log4jLogEvent(logger.getName(), marker,
                fqnOfCategoryClass,
                org.apache.logging.log4j.Level.toLevel(level.toString()),
                new ObjectMessage(message),
                new ArrayList<>(), throwable);
    }

    /**
     * Create new instance.
     *
     * @since 1.2.15
     * @param fqnOfCategoryClass Fully qualified class name of Logger
     * implementation.
     * @param logger The logger generating this event.
     * @param timeStamp the timestamp of this logging event
     * @param level The level of this event.
     * @param message The message of this event.
     * @param threadName thread name
     * @param throwable The throwable of this event.
     * @param ndc Nested diagnostic context
     * @param info Location info
     * @param properties MDC properties
     */
    public LoggingEvent(final String fqnOfCategoryClass,
            final Category logger,
            final long timeStamp,
            final Level level,
            final Object message,
            final String threadName,
            final ThrowableInformation throwable,
            final String ndc,
            final LocationInfo info,
            final java.util.Map properties) {
        this.timeStamp = timeStamp;
        this.category = logger;
        Marker marker = new Log4jMarker(logger.getName());
        List<Property> propertyList = new ArrayList<>();
        for (Object kv : properties.keySet()) {
            Property newProperty = Property.createProperty(kv.toString(),
                    properties.get(kv).toString());
            propertyList.add(newProperty);
        }
        this.logEvent = new Log4jLogEvent(logger.getName(), marker,
                fqnOfCategoryClass,
                org.apache.logging.log4j.Level.toLevel(level.toString()),
                new ObjectMessage(message), propertyList,
                throwable.getThrowable());
    }

    public LogEvent getLogEvent() {
        return this.logEvent;
    }

    /**
     * @return null - always null, as this deprecated in log4j2
     */
    public LocationInfo getLocationInformation() {
        return null;
    }

    /**
     * Return the level of this event. Use this form instead of directly
     * accessing the <code>level</code> field.
     *
     * @return level
     */
    public Level getLevel() {
        return Level.toLevel(this.logEvent.getLevel().toString());
    }

    /**
     * Return the name of the logger. Use this form instead of directly
     * accessing the <code>categoryName</code> field.
     *
     * @return loggerName
     */
    public String getLoggerName() {
        return this.logEvent.getLoggerName();
    }

    /**
     * Gets the logger of the event. Use should be restricted to cloning events.
     *
     * @return logger wrapper
     */
    public Category getLogger() {
        return category;
    }

    /**
     * Return the message for this logging event.
     *
     * @return object
     */
    public Object getMessage() {
        return this.logEvent.getMessage().toString();
    }

    /**
     * always return null since no longer supported
     *
     * @return null
     */
    public String getNDC() {
        return null;
    }

    /**
     * @param key - don't matter
     * @return null - always return null
     */
    public Object getMDC(String key) {
        return null;
    }

    /**
     * do nothing, as some might still call this function
     */
    public void getMDCCopy() {
        //do nothing
    }

    /**
     * @return string representation of message
     */
    public String getRenderedMessage() {
        return this.getMessage().toString();
    }

    /**
     * Returns the time when the application started, in milliseconds elapsed
     *
     * @return start time
     */
    public static long getStartTime() {
        return startTime;
    }

    /**
     * No longer supported in log4j2
     *
     * @return null
     */
    public String getThreadName() {
        return null;
    }

    /**
     * @return throwable
     */
    public ThrowableInformation getThrowableInformation() {
        return new ThrowableInformation(this.logEvent.getThrown());
    }

    /**
     * @return null
     */
    public String[] getThrowableStrRep() {
        return null;
    }

    /**
     * do nothing as this no longer supported in log4j2
     *
     * @param propName
     * @param propValue
     */
    public final void setProperty(final String propName,
            final String propValue) {
        //
    }

    /**
     * always return null, as mdc is deprecated
     *
     * @param key property name
     * @return property value or null if property not set
     */
    public final String getProperty(final String key) {
        return null;
    }

    /**
     * @return false -- not supported anymore
     */
    public final boolean locationInformationExists() {
        return false;
    }

    /**
     * Getter for the event's time stamp. The time stamp is calculated starting
     * from 1970-01-01 GMT.
     *
     * @return timestamp
     */
    public final long getTimeStamp() {
        return timeStamp;
    }

    /**
     * @return Set an unmodifiable set of the property keys.
     */
    public Set getPropertyKeySet() {
        return null;
    }

    /**
     * Returns the set of properties for the event.
     *
     * The returned set is unmodifiable by the caller.
     *
     * Provided for compatibility with log4j 1.3
     *
     * @return Set an unmodifiable map of the properties.
     */
    public Map getProperties() {
        return null;
    }

    /**
     * Get the fully qualified name of the calling logger sub-class/wrapper.
     * Provided for compatibility with log4j 1.3
     *
     * @return fully qualified class name, may be null.
     */
    public String getFQNOfLoggerClass() {
        return this.logEvent.getLoggerFqcn();
    }

    /**
     * not implemented
     *
     * @param propName the property name to remove
     * @return null
     */
    public Object removeProperty(String propName) {
        return null;
    }
}

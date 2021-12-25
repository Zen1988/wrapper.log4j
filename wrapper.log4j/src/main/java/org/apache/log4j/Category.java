/*
 * 
 */
package org.apache.log4j;

import java.util.Enumeration;
import org.apache.log4j.helpers.AppenderAttachableImpl;
import org.apache.log4j.helpers.NullEnumeration;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Lazy implementation for category logger
 *
 * @author Zen
 * @version 1.0
 */
public class Category {

    /**
     * The name of this category.
     */
    protected String name;

    /**
     * The assigned level of this category. The <code>level</code> variable need
     * not be assigned a value in which case it is inherited form the hierarchy.
     */
    volatile protected Level level;
    /**
     * The parent of this category. All categories have at least one ancestor
     * which is the root category.
     */
    volatile protected Category parent;
    /**
     * The fully qualified name of the Category class. See also the getFQCN
     * method.
     */
    private static final String FQCN = Category.class.getName();
    // Categories need to know what Hierarchy they are in
    protected LoggerRepository repository;
    /**
     * Additivity is set to true by default, that is children inherit the
     * appenders of their ancestors by default. If this variable is set to
     * <code>false</code> then the appenders found in the ancestors of this
     * category are not used. However, the children of this category will
     * inherit its appenders, unless the children have their additivity flag set
     * to <code>false</code> too. See the user manual for more details.
     */
    protected boolean additive = true;
    private AppenderAttachableImpl aai;

    /**
     * This constructor created a new <code>Category</code> instance and sets
     * its name.
     *
     * <p>
     * It is intended to be used by sub-classes only. You should not create
     * categories directly.
     *
     * @param name The name of the category.
     */
    protected Category(String name) {
        this.name = name;
    }

    /**
     * Add <code>newAppender</code> to the list of appenders of this Category
     * instance.
     *
     * <p>
     * If <code>newAppender</code> is already in the list of appenders, then it
     * won't be added again.
     *
     * @param newAppender - Appender
     */
    synchronized public void addAppender(Appender newAppender) {
        if (aai == null) {
            aai = new AppenderAttachableImpl();
        }
        aai.addAppender(newAppender);
        repository.fireAddAppenderEvent(this, newAppender);
    }

    /**
     *
     * @param event the event to log.
     */
    public void callAppenders(LoggingEvent event) {
        int writes = 0;

        for (Category c = this; c != null; c = c.parent) {
            // Protected against simultaneous call to addAppender, removeAppender,...
            synchronized (c) {
                if (c.aai != null) {
                    writes += c.aai.appendLoopOnAppenders(event);
                }
                if (!c.additive) {
                    break;
                }
            }
        }

        if (writes == 0) {
            repository.emitNoAppenderWarning(this);
        }
    }

    /**
     * This method creates a new logging event and logs the event without
     * further checks.
     *
     * @param fqcn
     * @param level
     * @param message
     * @param t
     */
    protected void forcedLog(String fqcn, Priority level, Object message, Throwable t) {
        callAppenders(new LoggingEvent(fqcn, this, level, message, t));
    }

    public String getName() {
        return this.name;
    }

    /**
     * Close all attached appenders implementing the AppenderAttachable
     * interface.
     *
     * @since 1.0
     */
    synchronized void closeNestedAppenders() {
        Enumeration enumeration = this.getAllAppenders();
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                Appender a = (Appender) enumeration.nextElement();
                a.close();
            }
        }
    }

    /**
     * @param message the message object to log.
     */
    public void debug(Object message) {
        if (repository.isDisabled(Level.DEBUG_INT)) {
            return;
        }
        if (Level.DEBUG.isGreaterOrEqual(this.getEffectiveLevel())) {
            forcedLog(FQCN, Level.DEBUG, message, null);
        }
    }

    /**
     * @param message the message object to log.
     * @param t the exception to log, including its stack trace.
     */
    public void debug(Object message, Throwable t) {
        if (repository.isDisabled(Level.DEBUG_INT)) {
            return;
        }
        if (Level.DEBUG.isGreaterOrEqual(this.getEffectiveLevel())) {
            forcedLog(FQCN, Level.DEBUG, message, t);
        }
    }

    /**
     * @param message the message object to log
     */
    public void error(Object message) {
        if (repository.isDisabled(Level.ERROR_INT)) {
            return;
        }
        if (Level.ERROR.isGreaterOrEqual(this.getEffectiveLevel())) {
            forcedLog(FQCN, Level.ERROR, message, null);
        }
    }

    /**
     * Log a message object with the <code>ERROR</code> level including the
     * stack trace of the {@link Throwable} <code>t</code> passed as parameter.
     *
     * <p>
     * See {@link #error(Object)} form for more detailed information.
     *
     * @param message the message object to log.
     * @param t the exception to log, including its stack trace.
     */
    public void error(Object message, Throwable t) {
        if (repository.isDisabled(Level.ERROR_INT)) {
            return;
        }
        if (Level.ERROR.isGreaterOrEqual(this.getEffectiveLevel())) {
            forcedLog(FQCN, Level.ERROR, message, t);
        }

    }

    /**
     * Log a message object with the {@link Level#FATAL FATAL} Level.
     *
     * <p>
     * This method first checks if this category is <code>FATAL</code> enabled
     * by comparing the level of this category with {@link
     * Level#FATAL FATAL} Level. If the category is <code>FATAL</code> enabled,
     * then it converts the message object passed as parameter to a string by
     * invoking the appropriate {@link org.apache.log4j.or.ObjectRenderer}. It
     * proceeds to call all the registered appenders in this category and also
     * higher in the hierarchy depending on the value of the additivity flag.
     *
     * <p>
     * <b>WARNING</b> Note that passing a {@link Throwable} to this method will
     * print the name of the Throwable but no stack trace. To print a stack
     * trace use the {@link #fatal(Object, Throwable)} form instead.
     *
     * @param message the message object to log
     */
    public void fatal(Object message) {
        if (repository.isDisabled(Level.FATAL_INT)) {
            return;
        }
        if (Level.FATAL.isGreaterOrEqual(this.getEffectiveLevel())) {
            forcedLog(FQCN, Level.FATAL, message, null);
        }
    }

    /**
     * Log a message object with the <code>FATAL</code> level including the
     * stack trace of the {@link Throwable} <code>t</code> passed as parameter.
     *
     * <p>
     * See {@link #fatal(Object)} for more detailed information.
     *
     * @param message the message object to log.
     * @param t the exception to log, including its stack trace.
     */
    public void fatal(Object message, Throwable t) {
        if (repository.isDisabled(Level.FATAL_INT)) {
            return;
        }
        if (Level.FATAL.isGreaterOrEqual(this.getEffectiveLevel())) {
            forcedLog(FQCN, Level.FATAL, message, t);
        }
    }

    /**
     * Get the appenders contained in this category as an {@link
     * Enumeration}. If no appenders can be found, then a
     * {@link NullEnumeration} is returned.
     *
     * @return Enumeration An enumeration of the appenders in this category.
     */
    synchronized public Enumeration getAllAppenders() {
        if (aai == null) {
            return NullEnumeration.getInstance();
        } else {
            return aai.getAllAppenders();
        }
    }

    /**
     * Look for the appender named as <code>name</code>.
     *
     * <p>
     * Return the appender with that name if in the list. Return
     * <code>null</code> otherwise.
     *
     * @param name - String
     * @return appender with name
     */
    synchronized public Appender getAppender(String name) {
        if (aai == null || name == null) {
            return null;
        }

        return aai.getAppender(name);
    }

    /**
     * @return level
     */
    public Level getEffectiveLevel() {
        for (Category c = this; c != null; c = c.parent) {
            if (c.level != null) {
                return c.level;
            }
        }
        return null; // If reached will cause an NullPointerException.
    }

    /**
     * @param message the message object to log
     */
    public void info(Object message) {
        if (repository.isDisabled(Level.INFO_INT)) {
            return;
        }
        if (Level.INFO.isGreaterOrEqual(this.getEffectiveLevel())) {
            forcedLog(FQCN, Level.INFO, message, null);
        }
    }

    /**
     * @param message the message object to log.
     * @param t the exception to log, including its stack trace.
     */
    public void info(Object message, Throwable t) {
        if (repository.isDisabled(Level.INFO_INT)) {
            return;
        }
        if (Level.INFO.isGreaterOrEqual(this.getEffectiveLevel())) {
            forcedLog(FQCN, Level.INFO, message, t);
        }
    }

    /**
     * @param appender
     * @return boolean
     */
    public boolean isAttached(Appender appender) {
        if (appender == null || aai == null) {
            return false;
        } else {
            return aai.isAttached(appender);
        }
    }

    /**
     * Check whether this category is enabled for the Level.
     *
     * @return boolean - <code>true</code> if this category is debug enabled,
     * <code>false</code> otherwise.
     *
     */
    public boolean isDebugEnabled() {
        if (repository.isDisabled(Level.DEBUG_INT)) {
            return false;
        }
        return Level.DEBUG.isGreaterOrEqual(this.getEffectiveLevel());
    }

    /**
     * Check whether this category is enabled for a given {@link
     * Level} passed as parameter.
     *
     * See also {@link #isDebugEnabled}.
     *
     * @param level - Priority
     * @return boolean True if this category is enabled for <code>level</code>.
     */
    public boolean isEnabledFor(Priority level) {
        if (repository.isDisabled(level.level)) {
            return false;
        }
        return level.isGreaterOrEqual(this.getEffectiveLevel());
    }

    /**
     * Check whether this category is enabled for the info Level. See also
     * {@link #isDebugEnabled}.
     *
     * @return boolean - <code>true</code> if this category is enabled for level
     * info, <code>false</code> otherwise.
     */
    public boolean isInfoEnabled() {
        if (repository.isDisabled(Level.INFO_INT)) {
            return false;
        }
        return Level.INFO.isGreaterOrEqual(this.getEffectiveLevel());
    }

    /**
     * skip the implementation
     *
     * @param priority
     * @param key
     * @param t
     */
    public void l7dlog(Priority priority, String key, Throwable t) {
        //
    }

    /**
     * skip the implementation
     *
     * @param priority
     * @param key
     * @param params
     * @param t
     */
    public void l7dlog(Priority priority, String key, Object[] params, Throwable t) {
        //
    }

    /**
     * This generic form is intended to be used by wrappers.
     *
     * @param priority
     * @param message
     * @param t
     */
    public void log(Priority priority, Object message, Throwable t) {
        if (repository.isDisabled(priority.level)) {
            return;
        }
        if (priority.isGreaterOrEqual(this.getEffectiveLevel())) {
            forcedLog(FQCN, priority, message, t);
        }
    }

    /**
     * This generic form is intended to be used by wrappers.
     *
     * @param priority
     * @param message
     */
    public void log(Priority priority, Object message) {
        if (repository.isDisabled(priority.level)) {
            return;
        }
        if (priority.isGreaterOrEqual(this.getEffectiveLevel())) {
            forcedLog(FQCN, priority, message, null);
        }
    }

    /**
     *
     * This is the most generic printing method. It is intended to be invoked by
     * <b>wrapper</b> classes.
     *
     * @param callerFQCN The wrapper class' fully qualified class name.
     * @param level The level of the logging request.
     * @param message The message of the logging request.
     * @param t The throwable of the logging request, may be null.
     */
    public void log(String callerFQCN, Priority level, Object message, Throwable t) {
        if (repository.isDisabled(level.level)) {
            return;
        }
        if (level.isGreaterOrEqual(this.getEffectiveLevel())) {
            forcedLog(callerFQCN, level, message, t);
        }
    }

    /**
     * Remove all previously added appenders from this Category instance.
     *
     * <p>
     * This is useful when re-reading configuration information.
     */
    synchronized public void removeAllAppenders() {
        if (aai != null) {
            aai.removeAllAppenders();
            aai = null;
        }
    }

    /**
     * Remove the appender passed as parameter form the list of appenders.
     *
     * @param appender
     */
    synchronized public void removeAppender(Appender appender) {
        if (appender == null || aai == null) {
            return;
        }
        boolean wasAttached = aai.isAttached(appender);
        if (wasAttached) {
            aai.removeAppender(appender);
        }
    }

    /**
     * Remove the appender with the name passed as parameter form the list of
     * appenders.
     *
     * @param name
     */
    synchronized public void removeAppender(String name) {
        if (name == null || aai == null) {
            return;
        }
        aai.removeAppender(name);
    }

    /**
     * Set the additivity flag for this Category instance.
     *
     * @param additive
     */
    public void setAdditivity(boolean additive) {
        this.additive = additive;
    }

    /**
     * Only the Hiearchy class can set the hiearchy of a category. Default
     * package access is MANDATORY here.
     *
     * @param repository
     */
    final void setHierarchy(LoggerRepository repository) {
        this.repository = repository;
    }

    /**
     * @param level
     */
    public void setLevel(Level level) {
        this.level = level;

    }

    /**
     * @param priority
     */
    public void setPriority(Priority priority) {
        this.level = (Level) priority;
    }

    /**
     * @param message the message object to log.
     */
    public void warn(Object message) {
        if (repository.isDisabled(Level.WARN_INT)) {
            return;
        }

        if (Level.WARN.isGreaterOrEqual(this.getEffectiveLevel())) {
            forcedLog(FQCN, Level.WARN, message, null);
        }
    }

    /**
     * Log a message with the <code>WARN</code> level including the stack trace
     * of the {@link Throwable} <code>t</code> passed as parameter.
     *
     * <p>
     * See {@link #warn(Object)} for more detailed information.
     *
     * @param message the message object to log.
     * @param t the exception to log, including its stack trace.
     */
    public void warn(Object message, Throwable t) {
        if (repository.isDisabled(Level.WARN_INT)) {
            return;
        }
        if (Level.WARN.isGreaterOrEqual(this.getEffectiveLevel())) {
            forcedLog(FQCN, Level.WARN, message, t);
        }
    }

    /**
     * dont know, maybe this better?
     */
    public static void shutdown() {
        LogManager.shutdown();
    }
}

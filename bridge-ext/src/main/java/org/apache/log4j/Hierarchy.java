/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.apache.log4j;

import java.util.Enumeration;
import java.util.HashMap;
import org.apache.log4j.spi.HierarchyEventListener;
import org.apache.log4j.spi.LoggerFactory;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.logging.log4j.core.LoggerContext;

/**
 * Some "today"s' application still called this deprecated class
 *
 * @author Zen
 * @version 1.0
 */
public class Hierarchy implements LoggerRepository {

    static final HashMap<String, Logger> loggers = new HashMap<>();
    private final Logger root;

    public Hierarchy(Logger root) {

        this.root = root;
        // Enable all level levels by default.
        this.root.setLevel(Level.ALL);
    }

    /**
     * Add a {@link HierarchyEventListener} event to the repository.
     *
     * @param listener The listener
     */
    @Override
    public void addHierarchyEventListener(HierarchyEventListener listener) {
        //do nothing
    }

    /**
     * Returns whether this repository is disabled for a given level. The answer
     * depends on the repository threshold and the <code>level</code> parameter.
     * See also {@link #setThreshold} method.
     *
     * @param level The level
     * @return whether this repository is disabled.
     */
    @Override
    public boolean isDisabled(int level) {
        return root.isEnabledFor(Level.toLevel(level));
    }

    /**
     * Set the repository-wide threshold. All logging requests below the
     * threshold are immediately dropped. By default, the threshold is set to
     * <code>Level.ALL</code> which has the lowest possible rank.
     *
     * @param level The level
     */
    @Override
    public void setThreshold(Level level) {
        root.setLevel(level);
    }

    /**
     * Another form of {@link #setThreshold(Level)} accepting a string parameter
     * instead of a <code>Level</code>.
     *
     * @param val The threshold value
     */
    @Override
    public void setThreshold(String val) {
        root.setLevel(Level.toLevel(val));
    }

    @Override
    public void emitNoAppenderWarning(Category cat) {
        //do nothing
    }

    /**
     * Get the repository-wide threshold. See {@link #setThreshold(Level)} for
     * an explanation.
     *
     * @return the level.
     */
    @Override
    public Level getThreshold() {
        return root.getLevel();
    }

    @Override
    public Logger getLogger(String name) {
        return Category.getInstance(getLoggerContext(), name);
    }

    public LoggerContext getLoggerContext() {
        return new LoggerContext(Logger.getRootLogger().getName());
    }

    @Override
    public Logger getLogger(String name, LoggerFactory factory) {
        return factory.makeNewLoggerInstance(name);
    }

    @Override
    public Logger getRootLogger() {
        return root;
    }

    @Override
    public Logger exists(String name) {
        return getLogger(name);
    }

    @Override
    public void shutdown() {
        //do nothing
    }

    @Deprecated
    @Override
    @SuppressWarnings("rawtypes")
    public Enumeration getCurrentLoggers() {
        return Category.getCurrentCategories();
    }

    /**
     * Deprecated. Please use {@link #getCurrentLoggers} instead.
     *
     * @return an enumeration of loggers.
     */
    @Override
    @SuppressWarnings("rawtypes")
    public Enumeration getCurrentCategories() {
        return getCurrentLoggers();
    }

    @Override
    public void fireAddAppenderEvent(Category logger, Appender appender) {
        //do nothing
    }

    @Override
    public void resetConfiguration() {
        //do nothing
    }
}

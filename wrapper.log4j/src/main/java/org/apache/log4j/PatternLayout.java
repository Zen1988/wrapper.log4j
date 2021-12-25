/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.apache.log4j;

import org.apache.log4j.spi.LoggingEvent;

/**
 * Override api org.apache.log4j.PatternLayout
 *
 * @author Zen
 * @version 1.0
 */
public class PatternLayout extends org.apache.log4j.Layout {

    /**
     * Default pattern string for log output. Currently set to the string
     * <b>"%m%n"</b> which just prints the application supplied message.
     */
    public final static String DEFAULT_CONVERSION_PATTERN = "%m%n";

    /**
     * A conversion pattern equivalent to the TTCCCLayout. Current value is
     * <b>%r [%t] %p %c %x - %m%n</b>.
     */
    public final static String TTCC_CONVERSION_PATTERN
            = "%r [%t] %p %c %x - %m%n";
    protected final int BUF_SIZE = 256;
    protected final int MAX_CAPACITY = 1024;
    private String pattern;
    private org.apache.logging.log4j.core.layout.PatternLayout patternLayout;

    /**
     * Constructs a PatternLayout using the DEFAULT_LAYOUT_PATTERN.
     *
     * The default pattern just produces the application supplied message.
     */
    public PatternLayout() {
        this(DEFAULT_CONVERSION_PATTERN);
    }

    /**
     * Constructs a PatternLayout using the supplied conversion pattern.
     *
     * @param pattern - String
     */
    public PatternLayout(String pattern) {
        this.pattern = pattern;
        this.patternLayout = org.apache.logging.log4j.core.layout.PatternLayout
                .newBuilder()
                .withPattern(pattern)
                .build();
    }

    /**
     * Set the <b>ConversionPattern</b> option. This is the string which
     * controls formatting and consists of a mix of literal content and
     * conversion specifiers.
     *
     * @param conversionPattern - String
     */
    public void setConversionPattern(String conversionPattern) {
        pattern = conversionPattern;
        this.patternLayout = org.apache.logging.log4j.core.layout.PatternLayout
                .newBuilder()
                .withPattern(conversionPattern)
                .build();
    }

    /**
     * Returns the value of the <b>ConversionPattern</b> option.
     *
     * @return pattern - String
     */
    public String getConversionPattern() {
        return pattern;
    }

    /**
     * The PatternLayout does not handle the throwable contained within
     * {@link LoggingEvent LoggingEvents}. Thus, it returns <code>true</code>.
     *
     * @since 0.8.4
     * @return true - boolean
     */
    @Override
    public boolean ignoresThrowable() {
        return true;
    }

    /**
     * Format logging event to string
     *
     * @param event - LoggingEvent
     */
    @Override
    public String format(final LoggingEvent event) {
        return new String(this.patternLayout.toByteArray(event.getLogEvent()));
    }

    /**
     * @return patternLayout -
     * org.apache.logging.log4j.core.layout.PatternLayout
     */
    public org.apache.logging.log4j.core.layout.PatternLayout getPatternLayout() {
        return this.patternLayout;
    }
}

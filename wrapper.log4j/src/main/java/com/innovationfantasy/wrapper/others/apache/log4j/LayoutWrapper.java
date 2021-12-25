/*
 * 
 */
package com.innovationfantasy.wrapper.others.apache.log4j;

import org.apache.log4j.PatternLayout;

/**
 * This class is to convert log4j 1.x Layout object to 2.x Layout object
 *
 * @author Zen/yunnchyuanchan
 * @version 1.0
 */
public class LayoutWrapper {

    /**
     * This function will convert layout from log4j 1.x to log4j 2.x
     *
     * @param layout - org.apache.log4j.Layout
     * @return org.apache.logging.log4j.core.Layout
     */
    public static org.apache.logging.log4j.core.Layout layout1to2(
            org.apache.log4j.Layout layout) {
        if (layout.getClass().getSimpleName().toLowerCase()
                .equals("patternlayout")) {
            PatternLayout pLayout = (PatternLayout) layout;
            org.apache.logging.log4j.core.layout.PatternLayout pLayout2
                    = org.apache.logging.log4j.core.layout.PatternLayout
                            .newBuilder()
                            .withPattern(pLayout.getConversionPattern())
                            .build();
            return pLayout2;
        }
        return org.apache.logging.log4j.core.layout.PatternLayout
                .createDefaultLayout();
    }
}

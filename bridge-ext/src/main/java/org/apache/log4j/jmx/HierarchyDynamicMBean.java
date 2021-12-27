/*
 */
package org.apache.log4j.jmx;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import org.apache.log4j.Appender;
import org.apache.log4j.Category;
import org.apache.log4j.spi.HierarchyEventListener;

/**
 * Hierarchy is long deprecated, move on if possible
 *
 * @author Zen
 * @version 1.0
 */
public class HierarchyDynamicMBean implements HierarchyEventListener {

    static final String ADD_APPENDER = "addAppender.";
    static final String THRESHOLD = "threshold";

    private final NotificationBroadcasterSupport nbs = new NotificationBroadcasterSupport();

    @Override
    public void addAppenderEvent(Category cat, Appender appender) {
        Notification n = new Notification(ADD_APPENDER + cat.getName(), this, 0);
        n.setUserData(appender);
        cat.debug("sending notification.");
        nbs.sendNotification(n);
    }

    @Override
    public void removeAppenderEvent(Category cat, Appender appender) {
        //do nothing
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.apache.log4j.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
import javax.xml.parsers.FactoryConfigurationError;
import org.apache.log4j.config.PropertySetter;
import org.apache.log4j.spi.Configurator;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.w3c.dom.Element;

/**
 * This deprecated since 10 years ago, yet still can find in latest patches no
 * choice, have to create this
 *
 * @author Zen
 * @version 1.0
 */
public class DOMConfigurator implements Configurator {

    public static HashMap<String, LoggerContext> contexts = new HashMap<>();
    private XmlConfiguration configurator;
    private final int defaultinterval = 5;

    public DOMConfigurator() {
        //might be required
    }

    public void doConfigure(String fullPath, LoggerRepository repository) {
        String loggerContextName = repository.getRootLogger().getName();
        LoggerContext loggerContext;
        if (contexts.containsKey(loggerContextName)) {
            loggerContext = contexts.get(loggerContextName);
        } else {
            loggerContext = new LoggerContext(loggerContextName);
            contexts.put(loggerContextName, loggerContext);
        }
        try {
            doConfigure(new FileInputStream(new File(fullPath)), loggerContext);
        } catch (FileNotFoundException ex) {
            //do nothing
        }
    }

    public DOMConfigurator(final LoggerContext loggerContext, final ConfigurationSource source,
            int monitorIntervalSeconds) {
        this.configurator = new XmlConfiguration(loggerContext, source, monitorIntervalSeconds);
    }

    public void doConfigure(InputStream inputStream, LoggerRepository repository) {
        String loggerContextName = repository.getRootLogger().getName();
        LoggerContext loggerContext;
        if (contexts.containsKey(loggerContextName)) {
            loggerContext = contexts.get(loggerContextName);
        } else {
            loggerContext = new LoggerContext(loggerContextName);
            contexts.put(loggerContextName, loggerContext);
        }
        doConfigure(inputStream, loggerContext);
    }

    public void doConfigure(URL url, LoggerRepository repository) {
        try {
            doConfigure(url.openStream(), repository);
        } catch (IOException ex) {
            //do nothing
        }
    }

    @Override
    public void doConfigure(InputStream inputStream, final LoggerContext loggerContext) {
        try {
            if (!contexts.containsKey(loggerContext.getName())) {
                contexts.put(loggerContext.getName(), loggerContext);
            }
            ConfigurationSource newSource = new ConfigurationSource(inputStream);
            this.configurator = new XmlConfiguration(loggerContext, newSource, defaultinterval);
            this.configurator.doConfigure();
        } catch (IOException ex) {
            //do nothing
        }
    }

    @Override
    public void doConfigure(URL url, final LoggerContext loggerContext) {
        try {
            doConfigure(url.openStream(), loggerContext);
        } catch (IOException ex) {
            //do nothing
        }
    }

 public static void configure(final Element element) {
    }

    public static void configureAndWatch(final String configFilename) {
    }

    public static void configureAndWatch(final String configFilename, final long delay) {
    }

    public static void configure(final String filename) throws FactoryConfigurationError {
    }

    public static void configure(final URL url) throws FactoryConfigurationError {
    }

    public static String subst(final String value, final Properties props) {
        return value;
    }

    public static void setParameter(final Element elem, final PropertySetter propSetter, final Properties props) {

    }

    public static Object parseElement(final Element element, final Properties props,
                                      @SuppressWarnings("rawtypes") final Class expectedClass)
        throws Exception {
        return null;
    }
}

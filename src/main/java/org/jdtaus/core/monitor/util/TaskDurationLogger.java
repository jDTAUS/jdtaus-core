/*
 *  jDTAUS Core Utilities
 *  Copyright (c) 2005 Christian Schulte
 *
 *  Christian Schulte, Haldener Strasse 72, 58095 Hagen, Germany
 *  <cs@jdtaus.org> (+49 2331 3543887)
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */
package org.jdtaus.core.monitor.util;

import java.util.Date;
import java.util.Locale;
import org.jdtaus.core.container.ContainerFactory;
import org.jdtaus.core.container.ContextFactory;
import org.jdtaus.core.container.ContextInitializer;
import org.jdtaus.core.container.Implementation;
import org.jdtaus.core.container.ModelFactory;
import org.jdtaus.core.container.Properties;
import org.jdtaus.core.container.Property;
import org.jdtaus.core.container.PropertyException;
import org.jdtaus.core.logging.spi.Logger;
import org.jdtaus.core.monitor.TaskEvent;
import org.jdtaus.core.monitor.TaskListener;

/**
 * {@code TaskListener} logging the duration of an operation performed by
 * a {@code Task}.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 *
 * @see #onTaskEvent(TaskEvent)
 */
public final class TaskDurationLogger implements TaskListener
{
    //--Implementation----------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausImplementation
    // This section is managed by jdtaus-container-mojo.

    /** Meta-data describing the implementation. */
    private static final Implementation META =
        ModelFactory.getModel().getModules().
        getImplementation(TaskDurationLogger.class.getName());
// </editor-fold>//GEN-END:jdtausImplementation

    //----------------------------------------------------------Implementation--
    //--Constructors------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausConstructors
    // This section is managed by jdtaus-container-mojo.

    /**
     * Initializes the properties of the instance.
     *
     * @param meta the property values to initialize the instance with.
     *
     * @throws NullPointerException if {@code meta} is {@code null}.
     */
    private void initializeProperties(final Properties meta)
    {
        Property p;

        if(meta == null)
        {
            throw new NullPointerException("meta");
        }

        p = meta.getProperty("loggingThresholdMillis");
        this.pLoggingThresholdMillis = ((java.lang.Long) p.getValue()).longValue();

    }
// </editor-fold>//GEN-END:jdtausConstructors

    //------------------------------------------------------------Constructors--
    //--Dependencies------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausDependencies
    // This section is managed by jdtaus-container-mojo.

    /** Configured <code>Logger</code> implementation. */
    private transient Logger dLogger;

    /**
     * Gets the configured <code>Logger</code> implementation.
     *
     * @return the configured <code>Logger</code> implementation.
     */
    private Logger getLogger()
    {
        Logger ret = null;
        if(this.dLogger != null)
        {
            ret = this.dLogger;
        }
        else
        {
            ret = (Logger) ContainerFactory.getContainer().
                getDependency(TaskDurationLogger.class,
                "Logger");

            if(ModelFactory.getModel().getModules().
                getImplementation(TaskDurationLogger.class.getName()).
                getDependencies().getDependency("Logger").
                isBound())
            {
                this.dLogger = ret;
            }
        }

        if(ret instanceof ContextInitializer && !((ContextInitializer) ret).
            isInitialized(ContextFactory.getContext()))
        {
            ((ContextInitializer) ret).initialize(ContextFactory.getContext());
        }

        return ret;
    }
// </editor-fold>//GEN-END:jdtausDependencies

    //------------------------------------------------------------Dependencies--
    //--Properties--------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausProperties
    // This section is managed by jdtaus-container-mojo.

    /**
     * Property {@code loggingThresholdMillis}.
     * @serial
     */
    private long pLoggingThresholdMillis;

    /**
     * Gets the value of property <code>loggingThresholdMillis</code>.
     *
     * @return the value of property <code>loggingThresholdMillis</code>.
     */
    public long getLoggingThresholdMillis()
    {
        return this.pLoggingThresholdMillis;
    }

// </editor-fold>//GEN-END:jdtausProperties

    //--------------------------------------------------------------Properties--
    //--TaskListener------------------------------------------------------------

    /**
     * {@inheritDoc}
     * <p>This method measures the time a task is running and logs
     * information for tasks running longer than specified by configuration
     * property {@code loggingThresholdMillis} (defaults to 60000).</p>
     *
     * @param event the event send by a {@code Task}.
     */
    public void onTaskEvent( final TaskEvent event )
    {
        if ( event != null )
        {
            final long now = System.currentTimeMillis();
            final long start = event.getTask().getTimestamp();

            if ( TaskEvent.ENDED == event.getType() &&
                now - start > this.getLoggingThresholdMillis() )
            {
                this.getLogger().info(
                    TaskDurationLoggerBundle.getInstance().
                    getDurationInfoMessage( Locale.getDefault() ).
                    format( new Object[] { event.getTask().getDescription().
                                           getText( Locale.getDefault() ),
                                           new Date( start ),
                                           new Date( now ),
                                           new Long( now - start )
                        } ) );
            }
        }
    }

    //------------------------------------------------------------TaskListener--
    //--TaskDurationLogger------------------------------------------------------

    /** Creates a new {@code TaskDurationLogger} instance. */
    public TaskDurationLogger()
    {
        super();
        this.initializeProperties( META.getProperties() );
        this.assertValidProperties();
    }

    /**
     * Creates a new {@code TaskDurationLogger} instance taking the number of
     * milliseconds a task at least needs to run to trigger a message when
     * finished.
     *
     * @param loggingThresholdMillis the number of milliseconds a task at least
     * needs to run to trigger a message when finished.
     *
     * @throws PropertyException if {@code loggingThresholdMillis} is negative
     * or zero.
     */
    public TaskDurationLogger( final long loggingThresholdMillis )
    {
        super();
        this.initializeProperties( META.getProperties() );
        this.pLoggingThresholdMillis = loggingThresholdMillis;
        this.assertValidProperties();
    }

    /**
     * Checks configured properties.
     *
     * @throws PropertyException for illegal property value.
     */
    private void assertValidProperties()
    {
        if ( this.getLoggingThresholdMillis() < 0L )
        {
            throw new PropertyException(
                "loggingThresholdMillis",
                Long.toString( this.getLoggingThresholdMillis() ) );

        }
    }

    //------------------------------------------------------TaskDurationLogger--
}

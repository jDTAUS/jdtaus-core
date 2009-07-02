/*
 *  jDTAUS Core Utilities
 *  Copyright (c) 2005 Christian Schulte
 *
 *  Christian Schulte, Haldener Strasse 72, 58095 Hagen, Germany
 *  <schulte2005@users.sourceforge.net> (+49 2331 3543887)
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
import org.jdtaus.core.logging.spi.Logger;
import org.jdtaus.core.monitor.TaskEvent;
import org.jdtaus.core.monitor.TaskListener;

/**
 * {@code TaskListener} logging the duration of an operation performed by
 * a {@code Task}.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $Id$
 *
 * @see #onTaskEvent(TaskEvent)
 */
public final class TaskDurationLogger implements TaskListener
{
    //--Dependencies------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausDependencies
    // This section is managed by jdtaus-container-mojo.

    /**
     * Gets the configured <code>Logger</code> implementation.
     *
     * @return The configured <code>Logger</code> implementation.
     */
    private Logger getLogger()
    {
        return (Logger) ContainerFactory.getContainer().
            getDependency( this, "Logger" );

    }

    /**
     * Gets the configured <code>Locale</code> implementation.
     *
     * @return The configured <code>Locale</code> implementation.
     */
    private Locale getLocale()
    {
        return (Locale) ContainerFactory.getContainer().
            getDependency( this, "Locale" );

    }

// </editor-fold>//GEN-END:jdtausDependencies

    //------------------------------------------------------------Dependencies--
    //--Properties--------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausProperties
    // This section is managed by jdtaus-container-mojo.

    /**
     * Gets the value of property <code>defaultLoggingThresholdMillis</code>.
     *
     * @return Default number of milliseconds a task at least needs to run to trigger a message when finished.
     */
    private java.lang.Long getDefaultLoggingThresholdMillis()
    {
        return (java.lang.Long) ContainerFactory.getContainer().
            getProperty( this, "defaultLoggingThresholdMillis" );

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
                    this.getDurationInfoMessage(
                    this.getLocale(),
                    event.getTask().getDescription().
                    getText( this.getLocale() ),
                    new Date( start ),
                    new Date( now ),
                    new Long( now - start ) ) );

            }
        }
    }

    //------------------------------------------------------------TaskListener--
    //--TaskDurationLogger------------------------------------------------------

    /**
     * The number of milliseconds a task at least needs to run to trigger a
     * message when finished.
     */
    private Long loggingTresholdMillis;

    /** Creates a new {@code TaskDurationLogger} instance. */
    public TaskDurationLogger()
    {
        super();
    }

    /**
     * Creates a new {@code TaskDurationLogger} instance taking the number of
     * milliseconds a task at least needs to run to trigger a message when
     * finished.
     *
     * @param loggingThresholdMillis the number of milliseconds a task at least
     * needs to run to trigger a message when finished.
     */
    public TaskDurationLogger( final long loggingThresholdMillis )
    {
        super();

        if ( loggingThresholdMillis > 0 )
        {
            this.loggingTresholdMillis = new Long( loggingThresholdMillis );
        }
    }

    /**
     * Gets the number of milliseconds a task at least needs to run to trigger
     * a message when finished.
     *
     * @return the number of milliseconds a task at least needs to run to
     * trigger a message when finished.
     */
    public long getLoggingThresholdMillis()
    {
        if ( this.loggingTresholdMillis == null )
        {
            this.loggingTresholdMillis =
                this.getDefaultLoggingThresholdMillis();

        }

        return this.loggingTresholdMillis.intValue();
    }

    //------------------------------------------------------TaskDurationLogger--
    //--Messages----------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausMessages
    // This section is managed by jdtaus-container-mojo.

    /**
     * Gets the text of message <code>durationInfo</code>.
     * <blockquote><pre>Taskinformation:
     *    Beschreibung ... : {0}
     *    Start .......... : {1,date,long} um {1,time,long}
     *    Ende ........... : {2,date,long} um {2,time,long}
     *    Laufzeit ....... : {3}ms</pre></blockquote>
     * <blockquote><pre>Taskinformation:
     *    Description ... : {0}
     *    Start ......... : {1,date,long} at {1,time,long}
     *    End ........... : {2,date,long} at {2,time,long}
     *    Duration ...... : {3}ms</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     * @param taskDescription The description of the task.
     * @param startDate The start date of the task.
     * @param endDate The end date of the task.
     * @param durationMillis The number of milliseconds the task ran.
     *
     * @return Information about a task.
     */
    private String getDurationInfoMessage( final Locale locale,
            final java.lang.String taskDescription,
            final java.util.Date startDate,
            final java.util.Date endDate,
            final java.lang.Number durationMillis )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "durationInfo", locale,
                new Object[]
                {
                    taskDescription,
                    startDate,
                    endDate,
                    durationMillis
                });

    }

// </editor-fold>//GEN-END:jdtausMessages

    //----------------------------------------------------------------Messages--
}

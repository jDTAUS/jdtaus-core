/*
 *  jDTAUS Core RI Task Monitor
 *  Copyright (C) 2005 Christian Schulte
 *  <schulte2005@users.sourceforge.net>
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
package org.jdtaus.core.monitor.ri;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javax.swing.event.EventListenerList;
import org.jdtaus.core.container.ContainerFactory;
import org.jdtaus.core.logging.spi.Logger;
import org.jdtaus.core.monitor.Task;
import org.jdtaus.core.monitor.TaskEvent;
import org.jdtaus.core.monitor.TaskListener;
import org.jdtaus.core.monitor.spi.TaskMonitor;
import org.jdtaus.core.text.Message;

/**
 * jDTAUS Core SPI {@code TaskMonitor} reference implementation.
 * <p>The reference implementation uses a thread checking the state of all tasks
 * in the system periodically which is started upon initialization and runs
 * endlessly. Monitoring is controlled by property {@code pollIntervalMillis}
 * specifying the milliseconds of one period. Each time a period ends, tasks
 * are checked for state changes and corresponding events are fired. Property
 * {@code pollIntervalMillis} defaults to {@code 250ms}.</p>
 *
 * <p><b>Note:</b><br/>
 * {@code TaskEvent}s of type {@code STARTED} and {@code ENDED} are fired by the
 * thread executing the task's operation. Since tasks are monitored
 * asynchronously, {@code TaskEvent}s of type {@code CHANGED_STATE} are fired by
 * the monitor thread, not by the thread executing the task's operation. Make
 * sure {@code TaskListener} implementations are prepared for being notified
 * by a different thread than the one executing a task's operation.</p>
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $JDTAUS$
 *
 * @see org.jdtaus.core.container.Container
 */
public class DefaultTaskMonitor implements TaskMonitor
{
    //--Constructors------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausConstructors
    // This section is managed by jdtaus-container-mojo.

    /** Standard implementation constructor <code>org.jdtaus.core.monitor.ri.DefaultTaskMonitor</code>. */
    public DefaultTaskMonitor()
    {
        super();
    }

// </editor-fold>//GEN-END:jdtausConstructors

    //------------------------------------------------------------Constructors--
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
     * Gets the configured <code>TaskListener</code> implementation.
     *
     * @return The configured <code>TaskListener</code> implementation.
     */
    private TaskListener[] getTaskListener()
    {
        return (TaskListener[]) ContainerFactory.getContainer().
            getDependency( this, "TaskListener" );

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
     * Gets the value of property <code>defaultPollIntervalMillis</code>.
     *
     * @return Default number of milliseconds per poll interval.
     */
    private java.lang.Long getDefaultPollIntervalMillis()
    {
        return (java.lang.Long) ContainerFactory.getContainer().
            getProperty( this, "defaultPollIntervalMillis" );

    }

// </editor-fold>//GEN-END:jdtausProperties

    //--------------------------------------------------------------Properties--
    //--TaskEventSource---------------------------------------------------------

    public void addTaskListener( final TaskListener listener )
    {
        if ( listener == null )
        {
            throw new NullPointerException( "listener" );
        }

        this.taskListeners.add( TaskListener.class, listener );
    }

    public void removeTaskListener( final TaskListener listener )
    {
        if ( listener == null )
        {
            throw new NullPointerException( "listener" );
        }

        this.taskListeners.remove( TaskListener.class, listener );
    }

    public TaskListener[] getTaskListeners()
    {
        return (TaskListener[]) this.taskListeners.getListeners(
            TaskListener.class );

    }

    //---------------------------------------------------------TaskEventSource--
    //--TaskMonitor-------------------------------------------------------------

    public void monitor( final Task task )
    {
        if ( task == null )
        {
            throw new NullPointerException( "task" );
        }

        synchronized ( this.stateMap )
        {
            this.checkMonitorThread();
            this.createTaskState( task );
            this.fireTaskEvent( new TaskEvent( task, TaskEvent.STARTED ) );
        }
    }

    public void finish( final Task task )
    {
        if ( task == null )
        {
            throw new NullPointerException( "task" );
        }

        synchronized ( this.stateMap )
        {
            if ( this.changedState( task ) )
            {
                this.fireTaskEvent( new TaskEvent( task,
                                                   TaskEvent.CHANGED_STATE ) );

            }

            this.removeTaskState( task );
            this.fireTaskEvent( new TaskEvent( task, TaskEvent.ENDED ) );
        }
    }

    //-------------------------------------------------------------TaskMonitor--
    //--DefaultTaskMonitor------------------------------------------------------

    /** List of {@code TaskListener}s. */
    private final EventListenerList taskListeners = new EventListenerList();

    /** The thread monitoring tasks. */
    private MonitorThread monitorThread;

    /** Maps {@code Task}s to corresponding {@code TaskState} instances. */
    private final Map stateMap = new HashMap( 1000 );

    /** Number of milliseconds per poll interval. */
    private Long pollIntervalMillis;

    /**
     * Creates a new {@code DefaultTaskMonitor} instance taking the
     * milliseconds of one period.
     *
     * @param pollIntervalMillis the number of milliseconds per poll interval.
     */
    public DefaultTaskMonitor( final long pollIntervalMillis )
    {
        if ( pollIntervalMillis > 0L )
        {
            this.pollIntervalMillis = new Long( pollIntervalMillis );
        }
    }

    /**
     * Gets the value of property {@code pollIntervalMillis}.
     *
     * @return the number of milliseconds per poll interval.
     */
    private long getPollIntervalMillis()
    {
        if ( this.pollIntervalMillis == null )
        {
            this.pollIntervalMillis = this.getDefaultPollIntervalMillis();
        }

        return this.pollIntervalMillis.longValue();
    }

    /** Caches the state of a task. */
    private static final class TaskState
    {

        boolean indeterminate;

        boolean cancelable;

        boolean cancelled;

        int minimum;

        int maximum;

        int progress;

        Message progressDescription;

    }

    /** Thread monitoring all currently running {@code Task}s for changes. */
    private class MonitorThread extends Thread
    {

        /** Milliseconds per poll interval. */
        private final long pollIntervalMillis;

        /** Creates a new {@code MonitorThread} instance. */
        private MonitorThread( final long pollIntervalMillis )
        {
            super( "DefaultTaskMonitor" );
            this.pollIntervalMillis = pollIntervalMillis;
        }

        /** {@inheritDoc} */
        public void run()
        {
            while ( true )
            {
                try
                {
                    Thread.sleep( this.pollIntervalMillis );
                    this.checkTasks();
                }
                catch ( InterruptedException e )
                {
                    this.checkTasks();
                }
            }
        }

        public void start()
        {
            super.start();
            getLogger().debug( getThreadStartedMessage(
                getLocale(), new Long( this.pollIntervalMillis ) ) );

        }

        /**
         * Checks the state of all currently running tasks for changes and
         * fires corresponding events.
         */
        private void checkTasks()
        {
            synchronized ( DefaultTaskMonitor.this.stateMap )
            {
                for ( Iterator it = DefaultTaskMonitor.this.stateMap.keySet().
                    iterator(); it.hasNext(); )
                {
                    final Task task = (Task) it.next();
                    if ( changedState( task ) )
                    {
                        fireTaskEvent( new TaskEvent(
                            task, TaskEvent.CHANGED_STATE ) );

                    }
                }
            }
        }

    }

    /**
     * Gets the monitor thread.
     *
     * @return the monitor thread.
     */
    private synchronized void checkMonitorThread()
    {
        if ( this.monitorThread == null )
        {
            this.monitorThread =
            new MonitorThread( this.getPollIntervalMillis() );

            this.monitorThread.start();
        }

        if ( !this.monitorThread.isAlive() )
        { // Monitoring died for some reason; start a new thread.
            this.getLogger().warn( this.getThreadDiedMessage(
                this.getLocale() ) );

            this.monitorThread =
            new MonitorThread( this.getPollIntervalMillis() );

            this.monitorThread.start();
        }
    }

    /**
     * Notifies all registered {@code TaskListener}s about {@code TaskEvent}s.
     *
     * @param e The event to be provided to the listeners.
     */
    private void fireTaskEvent( final TaskEvent e )
    {
        if ( e == null )
        {
            throw new NullPointerException( "e" );
        }


        final Object[] listeners = this.taskListeners.getListenerList();
        for ( int i = listeners.length - 2; i >= 0; i -= 2 )
        {
            if ( listeners[i] == TaskListener.class )
            {
                ( (TaskListener) listeners[i + 1] ).onTaskEvent( e );
            }
        }

        final TaskListener[] taskListener = this.getTaskListener();
        for ( int i = taskListener.length - 1; i >= 0; i-- )
        {
            taskListener[i].onTaskEvent( e );
        }
    }

    /**
     * Caches the state of a {@code Task}.
     *
     * @param task the task to cache state for.
     *
     * @throws NullPointerException if {@code task} is {@code null}.
     * @throws IllegalStateException if the cache already holds state for
     * {@code task}.
     */
    private void createTaskState( final Task task )
    {
        final TaskState state = new TaskState();
        state.cancelable = task.isCancelable();
        state.indeterminate = task.isIndeterminate();
        state.cancelled = state.cancelable
                          ? task.isCancelled()
                          : false;

        state.progressDescription = task.getProgressDescription();

        if ( state.indeterminate )
        {
            state.maximum = Integer.MIN_VALUE;
            state.minimum = Integer.MIN_VALUE;
            state.progress = Integer.MIN_VALUE;
        }
        else
        {
            state.maximum = task.getMaximum();
            state.minimum = task.getMinimum();
            state.progress = task.getProgress();
        }

        if ( this.stateMap.put( task, state ) != null )
        {
            throw new IllegalStateException( this.getTaskAlreadyStartedMessage(
                this.getLocale(),
                task.getDescription().getText( this.getLocale() ),
                new Date( task.getTimestamp() ) ) );

        }
    }

    /**
     * Removes the cached state of a {@code Task}.
     *
     * @param task the task to remove the cached state of.
     *
     * @throws NullPointerException if {@code task} is {@code null}.
     */
    private void removeTaskState( final Task task )
    {
        if ( task == null )
        {
            throw new NullPointerException( "task" );
        }

        this.stateMap.remove( task );
    }

    /**
     * Checks the state of a given task for changes.
     *
     * @param task the task to check for state changes.
     *
     * @return {@code true} if the state of {@code task} changed since the last
     * time this method got called; {@code false} if the state did not change.
     *
     * @throws NullPointerException if {@code task} is {@code null}.
     * @throws IllegalStateException if no cached state exists for {@code task}.
     */
    private boolean changedState( final Task task )
    {
        if ( task == null )
        {
            throw new NullPointerException( "task" );
        }


        boolean changedState = false;
        final TaskState state = (TaskState) this.stateMap.get( task );

        if ( state == null )
        {
            throw new IllegalStateException();
        }

        if ( state.indeterminate )
        {
            state.indeterminate = task.isIndeterminate();
            if ( !state.indeterminate )
            {
                state.minimum = task.getMinimum();
                state.maximum = task.getMaximum();
                state.progress = task.getProgress();
                changedState = true;
            }
        }
        else
        {
            state.indeterminate = task.isIndeterminate();
            if ( state.indeterminate )
            {
                changedState = true;
            }
            else
            {
                if ( state.minimum != task.getMinimum() )
                {
                    state.minimum = task.getMinimum();
                    changedState = true;
                }
                if ( state.maximum != task.getMaximum() )
                {
                    state.maximum = task.getMaximum();
                    changedState = true;
                }
                if ( state.progress != task.getProgress() )
                {
                    state.progress = task.getProgress();
                    changedState = true;
                }
            }
        }

        if ( state.cancelable )
        {
            state.cancelable = task.isCancelable();
            if ( !state.cancelable )
            {
                changedState = true;
            }
            else
            {
                if ( state.cancelled != task.isCancelled() )
                {
                    state.cancelled = task.isCancelled();
                    changedState = true;
                }
            }
        }
        else
        {
            state.cancelable = task.isCancelable();
            if ( !state.cancelable )
            {
                state.cancelled = false;
                changedState = true;
            }
        }

        if ( state.progressDescription != task.getProgressDescription() )
        {
            state.progressDescription = task.getProgressDescription();
            changedState = true;
        }
        else if ( state.progressDescription != null &&
                  !state.progressDescription.getText( this.getLocale() ).
            equals( task.getProgressDescription().getText(
            this.getLocale() ) ) )
        {
            changedState = true;
        }

        return changedState;
    }

    //------------------------------------------------------DefaultTaskMonitor--
    //--Messages----------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausMessages
    // This section is managed by jdtaus-container-mojo.

    /**
     * Gets the text of message <code>threadStarted</code>.
     * <blockquote><pre>Neuen Thread gestartet. Abtastperiode {0,number}ms.</pre></blockquote>
     * <blockquote><pre>New thread started. Period {0,number}ms.</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     * @param periodMillis Period of the started thread.
     *
     * @return Information about a started thread.
     */
    private String getThreadStartedMessage( final Locale locale,
            final java.lang.Number periodMillis )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "threadStarted", locale,
                new Object[]
                {
                    periodMillis
                });

    }

    /**
     * Gets the text of message <code>threadDied</code>.
     * <blockquote><pre>Thread abnorm beendet. Konsultieren Sie die Protokolle für mögliche Ursachen.</pre></blockquote>
     * <blockquote><pre>The monitor thread terminated abnormally. Consult the logs for any causes.</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     *
     * @return Information about a dead thread.
     */
    private String getThreadDiedMessage( final Locale locale )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "threadDied", locale, null );

    }

    /**
     * Gets the text of message <code>taskAlreadyStarted</code>.
     * <blockquote><pre>Ein Vorgang mit Beschreibung {0} wurde bereits um {1,time,long} gestartet.</pre></blockquote>
     * <blockquote><pre>A task with description {0} already has been started at {1,time,long}.</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     * @param taskDescription Description of the already running task.
     * @param startTime Time the already running task got started.
     *
     * @return Information about an already running task.
     */
    private String getTaskAlreadyStartedMessage( final Locale locale,
            final java.lang.String taskDescription,
            final java.util.Date startTime )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "taskAlreadyStarted", locale,
                new Object[]
                {
                    taskDescription,
                    startTime
                });

    }

// </editor-fold>//GEN-END:jdtausMessages

    //----------------------------------------------------------------Messages--
}

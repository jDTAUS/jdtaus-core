/*
 *  jDTAUS Core Test Suite
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
package org.jdtaus.core.monitor.spi.it;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import junit.framework.Assert;
import java.util.Locale;
import org.jdtaus.core.monitor.TaskEvent;
import org.jdtaus.core.monitor.TaskEventSource;
import org.jdtaus.core.monitor.TaskListener;
import org.jdtaus.core.monitor.it.TaskEventSourceTest;
import org.jdtaus.core.monitor.spi.Task;
import org.jdtaus.core.monitor.spi.TaskMonitor;
import org.jdtaus.core.text.Message;

/**
 * Testcase for {@code TaskMonitor} implementations.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class TaskMonitorTest extends TaskEventSourceTest
{
    //--TaskEventSourceTest-----------------------------------------------------

    /**
     * {@inheritDoc}
     * @see #getTaskMonitor()
     */
    public final TaskEventSource getTaskEventSource()
    {
        return this.monitor;
    }

    //-----------------------------------------------------TaskEventSourceTest--
    //--TaskMonitorTest---------------------------------------------------------

    /** Implementation to test. */
    private TaskMonitor monitor;

    /**
     * Gets the {@code TaskMonitor} implementation tests are performed with.
     *
     * @return the {@code TaskMonitor} implementation tests are performed
     * with.
     */
    public TaskMonitor getTaskMonitor()
    {
        return this.monitor;
    }

    /**
     * Sets the {@code TaskMonitor} implementation tests are performed with.
     *
     * @param value the {@code TaskMonitor} implementation to perform tests
     * with.
     */
    public final void setTaskMonitor( final TaskMonitor value )
    {
        this.monitor = value;
        this.setTaskEventSource( value );
    }

    /** {@code Task} implementation monitoring is tested with. */
    public static final class TestMessage extends Message
    {

        /** @serial */
        String text = TaskMonitorTest.class.getName();

        public Object[] getFormatArguments( final Locale locale )
        {
            return new Object[ 0 ];
        }

        public String getText( final Locale locale )
        {
            return this.text;
        }

        public void setText( final String value )
        {
            this.text = value;
        }

    }

    /** {@code TaskListener} implementation monitoring is tested with. */
    public static final class TestTaskListener implements TaskListener
    {

        /** The instance of the tested task. */
        private Task task;

        /** The timestamp the tested task started. */
        private long startTime;

        /** The timestamp the tested task ended. */
        private long endTime;

        /** Flag indicating that the tested task changed its state. */
        private boolean changedState;

        /**
         * Creates a new {@code TestTaskListener} instance taking the tested
         * task.
         *
         * @param task the tested task.
         */
        public TestTaskListener( final Task task )
        {
            if ( task == null )
            {
                throw new NullPointerException( "task" );
            }

            this.task = task;
            this.reset();
        }

        /**
         * Gets the timestamp the tested task started.
         *
         * @return the timestamp the tested task started or {@code -1} if no
         * corresponding event got fired.
         */
        public long getStartTime()
        {
            return this.startTime;
        }

        /**
         * Sets the timestamp the tested task ended.
         *
         * @return the timestamp the tested task ended or {@code -1} if no
         * corresponding event got fired.
         */
        public long getEndTime()
        {
            return this.endTime;
        }

        /**
         * Flag indicating that corresponding events caused by the task
         * changing its state were fired.
         *
         * @return {@code true} if corresponding event got fired indicating
         * the task having changed state; {@code false} if no corresponding
         * events got fired.
         */
        public boolean getChangedState()
        {
            return this.changedState;
        }

        /** Resets the state of the listener. */
        public void reset()
        {
            this.startTime = -1L;
            this.endTime = -1L;
            this.changedState = false;
        }

        public void onTaskEvent( final TaskEvent taskEvent )
        {
            if ( taskEvent.getTask().equals( this.task ) )
            {
                final int type = taskEvent.getType();
                if ( type == TaskEvent.STARTED )
                {
                    assert this.startTime == -1;
                    this.startTime = System.currentTimeMillis();
                }
                else if ( type == TaskEvent.ENDED )
                {
                    assert this.endTime == -1;
                    this.endTime = System.currentTimeMillis();
                }
                else if ( type == TaskEvent.CHANGED_STATE )
                {
                    this.changedState = true;
                }
                else
                {
                    throw new IllegalStateException( Integer.toString( type ) );
                }
            }
        }

    }

    //---------------------------------------------------------TaskMonitorTest--
    //--Tests-------------------------------------------------------------------

    /**
     * Tests the {@link TaskMonitor#monitor(Task)} and
     * {@link TaskMonitor#finish(Task)} implementation to handle illegal
     * arguments correctly by throwing a corresponding
     * {@code NullPointerException}.
     */
    public void testIllegalArguments() throws Exception
    {
        assert this.getTaskMonitor() != null;

        try
        {
            this.getTaskMonitor().monitor( null );
            throw new AssertionError();
        }
        catch ( NullPointerException e )
        {
        }

        try
        {
            this.getTaskMonitor().finish( null );
            throw new AssertionError();
        }
        catch ( NullPointerException e )
        {
        }


    }

    /**
     * Tests the {@link TaskMonitor#monitor(Task)} and
     * {@link TaskMonitor#finish(Task)} implementation to fire corresponding
     * events during task monitoring.
     */
    public void testMonitoring() throws Exception
    {
        assert this.getTaskMonitor() != null;

        final Task task = new Task();
        final TestTaskListener listener = new TestTaskListener( task );

        task.setDescription( new TestMessage() );

        this.getTaskEventSource().addTaskListener( listener );

        task.setIndeterminate( false );
        task.setMinimum( 0 );
        task.setMaximum( 10 );
        task.setProgress( 0 );

        this.getTaskMonitor().monitor( task );

        for ( int i = 10; i > 0; i-- )
        {
            task.setProgress( i );
            Thread.currentThread().sleep( 1000 );
        }

        this.getTaskMonitor().finish( task );

        Assert.assertTrue( listener.getStartTime() > 0 );
        Assert.assertTrue( listener.getEndTime() > 0 );
        Assert.assertTrue( listener.getChangedState() );
    }

    /**
     * Tests the {@link TaskMonitor#monitor(Task)} and
     * {@link TaskMonitor#finish(Task)} implementation to fire corresponding
     * events during task monitoring for property {@code progressDescription}.
     */
    public void testProgressDescriptionMonitoring() throws Exception
    {
        assert this.getTaskMonitor() != null;

        final Task task = new Task();
        final TestTaskListener listener = new TestTaskListener( task );
        final TestMessage progressDescription = new TestMessage();

        task.setDescription( new TestMessage() );
        task.setProgressDescription( progressDescription );

        this.getTaskEventSource().addTaskListener( listener );

        task.setIndeterminate( true );

        this.getTaskMonitor().monitor( task );

        for ( int i = 2; i > 0; i-- )
        {
            Thread.currentThread().sleep( 1000 );
            progressDescription.setText( Integer.toString( i ) );
        }

        this.getTaskMonitor().finish( task );

        Assert.assertTrue( listener.getStartTime() > 0 );
        Assert.assertTrue( listener.getEndTime() > 0 );
        Assert.assertTrue( listener.getChangedState() );
    }

    /**
     * Tests the {@link TaskMonitor#monitor(Task)} and
     * {@link TaskMonitor#finish(Task)} implementation to fire corresponding
     * events during task monitoring when an application updates property
     * {@code cancelled}.
     */
    public void testCancelable() throws Exception
    {
        assert this.getTaskMonitor() != null;

        final Task task = new Task();
        final TestTaskListener listener = new TestTaskListener( task );

        this.getTaskEventSource().addTaskListener( listener );

        task.setCancelable( true );
        task.setDescription( new TestMessage() );

        this.getTaskMonitor().monitor( task );

        for ( int i = 10; i > 0; i-- )
        {
            if ( i == 10 )
            {
                task.setCancelled( true );
            }

            Thread.currentThread().sleep( 1000 );
        }

        this.getTaskMonitor().finish( task );

        Assert.assertTrue( listener.getStartTime() > 0 );
        Assert.assertTrue( listener.getEndTime() > 0 );
        Assert.assertTrue( listener.getChangedState() );
    }

    /**
     * Tests the {@link TaskMonitor#monitor(Task)} and
     * {@link TaskMonitor#finish(Task)} implementation to not throw any
     * exceptions for parallel accessing tasks.
     */
    public void testSynchronization() throws Exception
    {
        final List threads = new LinkedList();
        for ( int i = 10; i > 0; i-- )
        {
            final Thread thread = new Thread()
            {

                public void run()
                {
                    try
                    {
                        final Task task = new Task();
                        task.setIndeterminate( true );
                        task.setDescription( new TestMessage() );

                        getTaskMonitor().monitor( task );
                        for ( int j = 10; j > 0; j-- )
                        {
                            Thread.currentThread().sleep( 1000 );
                        }
                        getTaskMonitor().finish( task );
                    }
                    catch ( InterruptedException e )
                    {
                        throw new AssertionError( e );
                    }
                }

            };

            threads.add( thread );
            thread.start();
        }

        for ( Iterator it = threads.iterator(); it.hasNext();)
        {
            ( ( Thread ) it.next() ).join();
        }
    }

    //-------------------------------------------------------------------Tests--
}

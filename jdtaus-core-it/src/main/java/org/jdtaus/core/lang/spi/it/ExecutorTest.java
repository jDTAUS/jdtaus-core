/*
 *  jDTAUS Core Test Suite
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
package org.jdtaus.core.lang.spi.it;

import junit.framework.Assert;
import org.jdtaus.core.lang.ExceptionEvent;
import org.jdtaus.core.lang.ExceptionEventSource;
import org.jdtaus.core.lang.ExceptionListener;
import org.jdtaus.core.lang.it.ExceptionEventSourceTest;
import org.jdtaus.core.lang.spi.Executor;

/**
 * Testcase for {@code Executor} implementations.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public abstract class ExecutorTest extends ExceptionEventSourceTest
{
    //--ExceptionEventSourceTest------------------------------------------------

    /**
     * {@inheritDoc}
     * @see #getExecutor()
     */
    public final ExceptionEventSource getExceptionEventSource()
    {
        return this.getExecutor();
    }

    //------------------------------------------------ExceptionEventSourceTest--
    //--ExecutorTest------------------------------------------------------------

    /**
     * Gets the {@code Executor} implementation tests are performed with.
     *
     * @return the {@code Executor} implementation tests are performed with.
     */
    public abstract Executor getExecutor();

    //------------------------------------------------------------ExecutorTest--
    //--Tests-------------------------------------------------------------------

    /** Exception expected to be reported. */
    public static final class ExecutionException extends RuntimeException
    {
    }

    /** {@code Runnable} throwing {@code ExecutionException}. */
    public static final Runnable runnable = new Runnable()
    {

        public void run()
        {
            throw new ExecutionException();
        }

    };

    /** {@code ExceptionListener} providing the last reported event. */
    public static final class TestListener implements ExceptionListener
    {

        /** The last reported event. */
        private ExceptionEvent event;

        /**
         * Gets the last reported event.
         *
         * @return the last reported event.
         */
        public ExceptionEvent getEvent()
        {
            return this.event;
        }

        public void onException( final ExceptionEvent event )
        {
            this.event = event;
        }

    }

    /**
     * Tests the {@link Executor#executeAsynchronously(Runnable)} method to
     * handle {@code null} arguments correctly by throwing a corresponding
     * {@code NullPointerException}.
     */
    public void testNullArguments() throws Exception
    {
        super.testNullArguments();

        assert this.getExecutor() != null;

        try
        {
            this.getExecutor().executeAsynchronously( null );
            throw new AssertionError();
        }
        catch ( NullPointerException e )
        {
            Assert.assertNotNull( e.getMessage() );
            System.out.println( e.toString() );
        }

    }

    /**
     * Tests the {@link Executor#executeAsynchronously(Runnable)} method to
     * report any exceptions thrown during execution.
     */
    public void testOnException() throws Exception
    {
        assert this.getExecutor() != null;

        final TestListener listener = new TestListener();
        this.getExecutor().addExceptionListener( listener );
        this.getExecutor().executeAsynchronously( runnable );

        // Wait 5 seconds and test the listener.
        Thread.sleep( 5000L );

        Assert.assertTrue( listener.getEvent().
                           getException() instanceof ExecutionException );

    }

    //-------------------------------------------------------------------Tests--
}

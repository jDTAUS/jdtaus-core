/*
 *  jDTAUS Core Test Suite
 *  Copyright (C) 2005 Christian Schulte
 *  <cs@schulte.it>
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
package org.jdtaus.core.text.spi.it;

import java.util.Locale;
import junit.framework.Assert;
import org.jdtaus.core.text.Message;
import org.jdtaus.core.text.MessageEvent;
import org.jdtaus.core.text.MessageEventSource;
import org.jdtaus.core.text.MessageListener;
import org.jdtaus.core.text.it.MessageEventSourceTest;
import org.jdtaus.core.text.spi.ApplicationLogger;

/**
 * Testcase for {@code ApplicationLogger} implementations.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class ApplicationLoggerTest extends MessageEventSourceTest
{
    //--MessageEventSourceTest--------------------------------------------------

    /**
     * {@inheritDoc}
     * @see #getApplicationLogger()
     */
    public final MessageEventSource getMessageEventSource()
    {
        return this.logger;
    }

    //--------------------------------------------------MessageEventSourceTest--
    //--ApplicationLoggerTest---------------------------------------------------

    /** Implementation to test. */
    private ApplicationLogger logger;

    /** Creates a new {@code ApplicationLoggerTest} instance. */
    public ApplicationLoggerTest()
    {
        super();
    }

    /**
     * Gets the {@code ApplicationLogger} implementation tests are performed
     * with.
     *
     * @return the {@code ApplicationLogger} implementation tests are performed
     * with.
     */
    public ApplicationLogger getApplicationLogger()
    {
        return this.logger;
    }

    /**
     * Sets the {@code ApplicationLogger} implementation tests are performed
     * with.
     *
     * @param value the {@code ApplicationLogger} implementation to perform
     * tests with.
     */
    public final void setApplicationLogger( final ApplicationLogger value )
    {
        this.logger = value;
        this.setMessageEventSource( value );
    }

    /** {@code MessageListener} implementation tests are performed with. */
    public static final class TestMessageListener implements MessageListener
    {

        /** The last event provided to this listener. */
        private MessageEvent lastEvent;

        /** Creates a new {@code TestMessageListener} instance. */
        public TestMessageListener()
        {
            super();
        }

        /**
         * Gets the last event provided to this listener.
         *
         * @return the last event provided to this listener.
         */
        public MessageEvent getLastEvent()
        {
            return this.lastEvent;
        }

        public void onMessage( final MessageEvent messageEvent )
        {
            this.lastEvent = messageEvent;
        }

    }

    //---------------------------------------------------ApplicationLoggerTest--
    //--Tests-------------------------------------------------------------------

    /**
     * Tests the {@link ApplicationLogger#log(MessageEvent)} method to handle
     * illegal arguments correctly by throwing a corresponding
     * {@code NullPointerException}.
     */
    public void testIllegalArguments() throws Exception
    {
        assert this.getApplicationLogger() != null;

        try
        {
            this.getApplicationLogger().log( null );
            throw new AssertionError();
        }
        catch ( final NullPointerException e )
        {
            Assert.assertNotNull( e.getMessage() );
            System.out.println( e.toString() );
        }

    }

    /**
     * Tests the {@link ApplicationLogger#log(MessageEvent)} method to fire
     * corresponding events.
     */
    public void testLog() throws Exception
    {
        assert this.getApplicationLogger() != null;

        final TestMessageListener listener = new TestMessageListener();
        final Message message = new Message()
        {

            public Object[] getFormatArguments( final Locale locale )
            {
                return new Object[ 0 ];
            }

            public String getText( final Locale locale )
            {
                return ApplicationLoggerTest.class.getName();
            }

        };

        this.getMessageEventSource().addMessageListener( listener );

        final MessageEvent evt =
            new MessageEvent( this, message, MessageEvent.INFORMATION );

        this.getApplicationLogger().log( evt );
        Assert.assertNotNull( listener.getLastEvent() );
        Assert.assertTrue( listener.getLastEvent() == evt );
    }

    //-------------------------------------------------------------------Tests--
}

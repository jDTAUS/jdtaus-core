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
package org.jdtaus.core.text.it;

import junit.framework.Assert;
import org.jdtaus.core.text.MessageEventSource;

/**
 * Testcase for {@code MessageEventSource} implementations.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class MessageEventSourceTest
{
    //--MessageEventSourceTest--------------------------------------------------

    /** Implementation to test. */
    private MessageEventSource source;

    /**
     * Gets the {@code MessageEventSource} implementation tests are performed
     * with.
     *
     * @return the {@code MessageEventSource} implementation tests are
     * performed with.
     */
    public MessageEventSource getMessageEventSource()
    {
        return this.source;
    }

    /**
     * Sets the {@code MessageEventSource} implementation tests are performed
     * with.
     *
     * @param value the {@code MessageEventSource} implementation to perform
     * tests with.
     */
    public void setMessageEventSource( final MessageEventSource value )
    {
        this.source = value;
    }

    //--------------------------------------------------MessageEventSourceTest--
    //--Tests-------------------------------------------------------------------

    /**
     * Tests the {@link MessageEventSource#addMessageListener(MessageListener)}
     * method to handle {@code null} listener values correctly by throwing a
     * corresponding {@code NullPointerException}.
     */
    public void testAddMessageListener() throws Exception
    {
        assert this.getMessageEventSource() != null;

        try
        {
            this.getMessageEventSource().addMessageListener( null );
            throw new AssertionError();
        }
        catch ( NullPointerException e )
        {
            Assert.assertNotNull( e.getMessage() );
            System.out.println( e.toString() );
        }

    }

    /**
     * Tests the
     * {@link MessageEventSource#removeMessageListener(MessageListener)}
     * method to handle {@code null} listener values correctly by throwing a
     * corresponding {@code NullPointerException}.
     */
    public void testRemoveMessageListener() throws Exception
    {
        assert this.getMessageEventSource() != null;

        try
        {
            this.getMessageEventSource().removeMessageListener( null );
            throw new AssertionError();
        }
        catch ( NullPointerException e )
        {
            Assert.assertNotNull( e.getMessage() );
            System.out.println( e.toString() );
        }

    }

    /**
     * Tests the {@link MessageEventSource#getMessageListeners()} method to not
     * return a {@code null} value instead of an empty array when no listeners
     * are registered.
     */
    public void testGetMessageListeners() throws Exception
    {
        assert this.getMessageEventSource() != null;

        Assert.assertNotNull(
            this.getMessageEventSource().getMessageListeners() );

    }

    //-------------------------------------------------------------------Tests--
}

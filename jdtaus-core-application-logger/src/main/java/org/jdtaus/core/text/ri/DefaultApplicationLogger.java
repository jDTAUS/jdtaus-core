/*
 *  jDTAUS Core RI Application Logger
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
package org.jdtaus.core.text.ri;

import javax.swing.event.EventListenerList;
import org.jdtaus.core.container.ContainerFactory;
import org.jdtaus.core.text.MessageEvent;
import org.jdtaus.core.text.MessageListener;
import org.jdtaus.core.text.spi.ApplicationLogger;

/**
 * jDTAUS Core SPI {@code ApplicationLogger} reference implementation.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 *
 * @see org.jdtaus.core.container.Container
 */
public class DefaultApplicationLogger implements ApplicationLogger
{
    //--Constructors------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausConstructors
    // This section is managed by jdtaus-container-mojo.

    /** Standard implementation constructor <code>org.jdtaus.core.text.ri.DefaultApplicationLogger</code>. */
    public DefaultApplicationLogger()
    {
        super();
    }

// </editor-fold>//GEN-END:jdtausConstructors

    //------------------------------------------------------------Constructors--
    //--MessageEventSource------------------------------------------------------

    /** Holds {@code MessageListener}s. */
    private final EventListenerList listeners = new EventListenerList();

    public void addMessageListener( final MessageListener listener )
    {
        if ( listener == null )
        {
            throw new NullPointerException( "listener" );
        }

        this.listeners.add( MessageListener.class, listener );
    }

    public void removeMessageListener( final MessageListener listener )
    {
        if ( listener == null )
        {
            throw new NullPointerException( "listener" );
        }

        this.listeners.remove( MessageListener.class, listener );
    }

    public MessageListener[] getMessageListeners()
    {
        return (MessageListener[]) this.listeners.getListeners(
            MessageListener.class );

    }

    //------------------------------------------------------MessageEventSource--
    //--ApplicationLogger-------------------------------------------------------

    public void log( final MessageEvent e )
    {
        if ( e == null )
        {
            throw new NullPointerException( "e" );
        }

        final Object[] list = this.listeners.getListenerList();
        for ( int i = list.length - 2; i >= 0; i -= 2 )
        {
            if ( list[i] == MessageListener.class )
            {
                ( (MessageListener) list[i + 1] ).onMessage( e );
            }
        }

        final MessageListener[] messageListener = this.getMessageListener();
        for ( int i = messageListener.length - 1; i >= 0; i-- )
        {
            messageListener[i].onMessage( e );
        }
    }

    //-------------------------------------------------------ApplicationLogger--
    //--Dependencies------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausDependencies
    // This section is managed by jdtaus-container-mojo.

    /**
     * Gets the configured <code>MessageListener</code> implementation.
     *
     * @return The configured <code>MessageListener</code> implementation.
     */
    private MessageListener[] getMessageListener()
    {
        return (MessageListener[]) ContainerFactory.getContainer().
            getDependency( this, "MessageListener" );

    }

// </editor-fold>//GEN-END:jdtausDependencies

    //------------------------------------------------------------Dependencies--
}

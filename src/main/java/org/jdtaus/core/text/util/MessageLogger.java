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
package org.jdtaus.core.text.util;

import java.util.Locale;
import org.jdtaus.core.container.ContainerFactory;
import org.jdtaus.core.logging.spi.Logger;
import org.jdtaus.core.text.MessageEvent;
import org.jdtaus.core.text.MessageListener;

/**
 * {@code MessageListener} logging messages to a system logger.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 *
 * @see #onMessage(MessageEvent)
 */
public final class MessageLogger implements MessageListener
{
    //--Dependencies------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausDependencies
    // This section is managed by jdtaus-container-mojo.

    /**
     * Gets the configured <code>Logger</code> implementation.
     *
     * @return the configured <code>Logger</code> implementation.
     */
    private Logger getLogger()
    {
        return (Logger) ContainerFactory.getContainer().
            getDependency( this, "Logger" );

    }

// </editor-fold>//GEN-END:jdtausDependencies

    //------------------------------------------------------------Dependencies--
    //--MessageListener---------------------------------------------------------

    /**
     * {@inheritDoc}
     * <p>This method logs all messages given by the event using the
     * corresponding log level.</p>
     *
     * @param event the event holding messages.
     */
    public void onMessage( final MessageEvent event )
    {
        if ( event != null )
        {
            final int numMessages = event.getMessages().length;
            final StringBuffer messages = new StringBuffer( numMessages * 200 );
            for ( int i = 0; i < numMessages; i++ )
            {
                messages.append( event.getMessages()[i].getText(
                                 Locale.getDefault() ) ).append( '\n' );

            }

            switch ( event.getType() )
            {
                case MessageEvent.ERROR:
                    this.getLogger().error( messages.toString() );
                    break;

                case MessageEvent.INFORMATION:
                case MessageEvent.NOTIFICATION:
                    this.getLogger().info( messages.toString() );
                    break;

                case MessageEvent.WARNING:
                    this.getLogger().warn( messages.toString() );
                    break;

                default:
                    this.getLogger().warn(
                        this.getUnknownMessageEventTypeMessage(
                        new Integer( event.getType() ) ) );

            }
        }
    }

    //---------------------------------------------------------MessageListener--
    //--Messages----------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausMessages
    // This section is managed by jdtaus-container-mojo.

    /**
     * Gets the text of message <code>unknownMessageEventType</code>.
     * <blockquote><pre>Meldung unbekannten Typs {0,number} ignoriert.</pre></blockquote>
     * <blockquote><pre>Ignored message event of unknown type {0,number}.</pre></blockquote>
     *
     * @param unknownEventType The unknown event type.
     *
     * @return Message stating that an unknown message event got ignored.
     */
    private String getUnknownMessageEventTypeMessage(
            java.lang.Number unknownEventType )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "unknownMessageEventType",
                new Object[]
                {
                    unknownEventType
                });

    }

// </editor-fold>//GEN-END:jdtausMessages

    //----------------------------------------------------------------Messages--
}

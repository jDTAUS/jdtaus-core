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
            for ( int i = 0; i < event.getMessages().length; i++ )
            {
                switch ( event.getType() )
                {
                    case MessageEvent.ERROR:
                        this.getLogger().error( event.getMessages()[i].getText(
                            this.getLocale() ) );

                        break;

                    case MessageEvent.INFORMATION:
                    case MessageEvent.NOTIFICATION:
                        this.getLogger().info( event.getMessages()[i].getText(
                            this.getLocale() ) );

                        break;

                    case MessageEvent.WARNING:
                        this.getLogger().warn( event.getMessages()[i].getText(
                            this.getLocale() ) );

                        break;

                    default:
                        this.getLogger().warn(
                            this.getUnknownMessageEventTypeMessage(
                            this.getLocale(),
                            new Integer( event.getType() ) ) );

                }
            }
        }
    }

    //---------------------------------------------------------MessageListener--
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
    //--Messages----------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausMessages
    // This section is managed by jdtaus-container-mojo.

    /**
     * Gets the text of message <code>unknownMessageEventType</code>.
     * <blockquote><pre>Meldung unbekannten Typs {0,number} ignoriert.</pre></blockquote>
     * <blockquote><pre>Ignored message event of unknown type {0,number}.</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     * @param unknownEventType The unknown event type.
     *
     * @return Message stating that an unknown message event got ignored.
     */
    private String getUnknownMessageEventTypeMessage( final Locale locale,
            final java.lang.Number unknownEventType )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "unknownMessageEventType", locale,
                new Object[]
                {
                    unknownEventType
                });

    }

// </editor-fold>//GEN-END:jdtausMessages

    //----------------------------------------------------------------Messages--
}

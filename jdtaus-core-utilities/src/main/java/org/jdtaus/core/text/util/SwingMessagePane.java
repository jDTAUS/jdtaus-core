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

import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.jdtaus.core.container.ContainerFactory;
import org.jdtaus.core.logging.spi.Logger;
import org.jdtaus.core.text.MessageEvent;
import org.jdtaus.core.text.MessageListener;

/**
 * {@code MessageListener} displaying messages using Swing's
 * {@code JOptionPane}.
 * <p>This implementation displays a dialog for each {@code MessageEvent} this
 * {@code MessageListener} is notified about. Since a {@code MessageEvent} can
 * hold multiple messages a maximum number of messages to display per event
 * can be specified by property {@code maximumMessages} (defaults to 25).</p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 *
 * @see #onMessage(MessageEvent)
 */
public final class SwingMessagePane implements MessageListener
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
     * Gets the value of property <code>defaultMaximumMessages</code>.
     *
     * @return Default maximum number of messages displayed per event.
     */
    private java.lang.Integer getDefaultMaximumMessages()
    {
        return (java.lang.Integer) ContainerFactory.getContainer().
            getProperty( this, "defaultMaximumMessages" );

    }

// </editor-fold>//GEN-END:jdtausProperties

    //--------------------------------------------------------------Properties--
    //--MessageListener---------------------------------------------------------

    /**
     * {@inheritDoc}
     * <p>This method uses Swing's {@link JOptionPane} to display messages
     * given by the event. It will block the event dispatch thread until the
     * user confirms the message pane.</p>
     *
     * @param event the event holding messages.
     */
    public void onMessage( final MessageEvent event )
    {
        if ( event != null )
        {
            SwingUtilities.invokeLater(
                new Runnable()
                {

                    public void run()
                    {
                        final Object[] messages =
                            new Object[ event.getMessages().length ];

                        for ( int i = 0; i < messages.length &&
                                         i < getMaximumMessages(); i++ )
                        {
                            messages[i] = event.getMessages()[i].getText(
                                getLocale() );

                        }

                        switch ( event.getType() )
                        {
                            case MessageEvent.INFORMATION:
                                JOptionPane.showMessageDialog(
                                    getParent(), messages,
                                    getInformationMessage( getLocale() ),
                                    JOptionPane.INFORMATION_MESSAGE );

                                break;

                            case MessageEvent.NOTIFICATION:
                                JOptionPane.showMessageDialog(
                                    getParent(), messages,
                                    getNotificationMessage( getLocale() ),
                                    JOptionPane.INFORMATION_MESSAGE );

                                break;

                            case MessageEvent.WARNING:
                                JOptionPane.showMessageDialog(
                                    getParent(), messages,
                                    getWarningMessage( getLocale() ),
                                    JOptionPane.WARNING_MESSAGE );

                                break;

                            case MessageEvent.ERROR:
                                UIManager.getLookAndFeel().
                                    provideErrorFeedback( getParent() );

                                JOptionPane.showMessageDialog(
                                    getParent(), messages,
                                    getErrorMessage( getLocale() ),
                                    JOptionPane.ERROR_MESSAGE );

                                break;

                            default:
                                getLogger().warn(
                                    getUnknownMessageEventTypeMessage(
                                    getLocale(),
                                    new Integer( event.getType() ) ) );

                        }
                    }

                } );
        }
    }

    //---------------------------------------------------------MessageListener--
    //--SwingMessagePane--------------------------------------------------------

    /** The current parent component to use when displaying messages. */
    private Component parent;

    /** Maximum number of messages displayed per event. */
    private Integer maximumMessages;

    /**
     * Creates a new {@code SwingMessagePane} instance taking the parent
     * component to use when displaying messages.
     *
     * @param parent the parent component to use when displaying messages.
     *
     * @throws NullPointerException if {@code parent} is {@code null}.
     * @throws HeadlessException if this class is used in an environment
     * not providing a keyboard, display, or mouse.
     */
    public SwingMessagePane( final Component parent )
    {
        if ( parent == null )
        {
            throw new NullPointerException( "parent" );
        }

        if ( GraphicsEnvironment.isHeadless() )
        {
            throw new HeadlessException();
        }

        this.parent = parent;
    }

    /**
     * Creates a new {@code SwingMessagePane} instance taking the parent
     * component to use when displaying messages and the maximum number of
     * messages displayed per event.
     *
     * @param parent the parent component to use when displaying messages.
     * @param maximumMessages maximum number of messages displayed per
     * event.
     *
     * @throws NullPointerException if {@code parent} is {@code null}.
     * @throws HeadlessException if this class is used in an environment
     * not providing a keyboard, display, or mouse.
     */
    public SwingMessagePane( final Component parent,
                             final int maximumMessages )
    {
        if ( parent == null )
        {
            throw new NullPointerException( "parent" );
        }

        if ( GraphicsEnvironment.isHeadless() )
        {
            throw new HeadlessException();
        }

        this.parent = parent;

        if ( maximumMessages > 0 )
        {
            this.maximumMessages = new Integer( maximumMessages );
        }
    }

    /**
     * Gets the parent component for any message displays.
     *
     * @return the parent component for any message displays.
     */
    public Component getParent()
    {
        return this.parent;
    }

    /**
     * Sets the parent component used by any message displays.
     *
     * @param parent the parent component used by any message displays.
     *
     * @throws NullPointerException if {@code parent} is {@code null}.
     */
    public void setParent( final Component parent )
    {
        if ( parent == null )
        {
            throw new NullPointerException( "parent" );
        }

        this.parent = parent;
    }

    /**
     * Gets the maximum number of messages displayed per event.
     *
     * @return the maximum number of messages displayed per event.
     */
    public int getMaximumMessages()
    {
        if ( this.maximumMessages == null )
        {
            this.maximumMessages = this.getDefaultMaximumMessages();
        }

        return this.maximumMessages.intValue();
    }

    //--------------------------------------------------------SwingMessagePane--
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

    /**
     * Gets the text of message <code>information</code>.
     * <blockquote><pre>Information</pre></blockquote>
     * <blockquote><pre>Information</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     *
     * @return Information title.
     */
    private String getInformationMessage( final Locale locale )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "information", locale, null );

    }

    /**
     * Gets the text of message <code>notification</code>.
     * <blockquote><pre>Hinweis</pre></blockquote>
     * <blockquote><pre>Notification</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     *
     * @return Notification title.
     */
    private String getNotificationMessage( final Locale locale )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "notification", locale, null );

    }

    /**
     * Gets the text of message <code>warning</code>.
     * <blockquote><pre>Warnung</pre></blockquote>
     * <blockquote><pre>Warning</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     *
     * @return Warning title.
     */
    private String getWarningMessage( final Locale locale )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "warning", locale, null );

    }

    /**
     * Gets the text of message <code>error</code>.
     * <blockquote><pre>Fehler</pre></blockquote>
     * <blockquote><pre>Error</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     *
     * @return Error title.
     */
    private String getErrorMessage( final Locale locale )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "error", locale, null );

    }

// </editor-fold>//GEN-END:jdtausMessages

    //----------------------------------------------------------------Messages--
}

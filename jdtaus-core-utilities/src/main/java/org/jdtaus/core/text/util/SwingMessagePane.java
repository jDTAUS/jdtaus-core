/*
 *  jDTAUS Core Utilities
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
package org.jdtaus.core.text.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;
import org.jdtaus.core.container.ContainerFactory;
import org.jdtaus.core.logging.spi.Logger;
import org.jdtaus.core.text.MessageEvent;
import org.jdtaus.core.text.MessageListener;

/**
 * {@code MessageListener} displaying messages using Swing's {@code JOptionPane}.
 * <p>This implementation displays a dialog for each {@code MessageEvent} this {@code MessageListener} is notified
 * about. Since a {@code MessageEvent} can hold multiple messages a maximum number of messages to display per event
 * can be specified by property {@code maximumMessages} (defaults to 25).</p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
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
     * Gets the value of property <code>defaultResizable</code>.
     *
     * @return Default resizability of the message pane.
     */
    private java.lang.Boolean isDefaultResizable()
    {
        return (java.lang.Boolean) ContainerFactory.getContainer().
            getProperty( this, "defaultResizable" );

    }

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

    /**
     * Gets the value of property <code>defaultColumns</code>.
     *
     * @return Default number of columns the preferred width of the message pane is computed with.
     */
    private java.lang.Integer getDefaultColumns()
    {
        return (java.lang.Integer) ContainerFactory.getContainer().
            getProperty( this, "defaultColumns" );

    }

// </editor-fold>//GEN-END:jdtausProperties

    //--------------------------------------------------------------Properties--
    //--MessageListener---------------------------------------------------------

    /**
     * {@inheritDoc}
     * <p>This method uses Swing's {@link JOptionPane} to display messages given by the event. It will block the event
     * dispatch thread until the user confirms the message pane.</p>
     *
     * @param event The event holding messages.
     */
    public void onMessage( final MessageEvent event )
    {
        if ( event != null )
        {
            try
            {
                final Runnable r = new Runnable()
                {

                    public void run()
                    {
                        final JPanel panel = new JPanel();
                        panel.setLayout( new GridBagLayout() );

                        for ( int i = 0, s0 = event.getMessages().length; i < s0 && i < getMaximumMessages(); i++ )
                        {
                            String text = event.getMessages()[i].getText( getLocale() );

                            if ( !BasicHTML.isHTMLString( text ) )
                            {
                                text = "<html>" + HtmlEntities.escapeHtml( text ) + "</html>";
                            }

                            final JLabel label = new HtmlLabel( getColumns() );
                            label.setText( text );

                            final GridBagConstraints c = new GridBagConstraints();
                            c.anchor = GridBagConstraints.NORTHWEST;
                            c.fill = GridBagConstraints.BOTH;
                            c.gridheight = 1;
                            c.gridwidth = 1;
                            c.gridx = 0;
                            c.gridy = i;
                            c.weightx = 1.0D;

                            panel.add( label, c );

                            if ( !( i + 1 < s0 && i + 1 < getMaximumMessages() ) )
                            {
                                final JPanel p = new JPanel();
                                c.anchor = GridBagConstraints.SOUTH;
                                c.weighty = 1.0D;
                                c.gridy = i + 1;
                                panel.add( p, c );
                            }
                        }

                        JOptionPane optionPane = null;
                        JDialog dialog = null;

                        switch ( event.getType() )
                        {
                            case MessageEvent.INFORMATION:
                                optionPane = new JOptionPane();
                                optionPane.setMessage( panel );
                                optionPane.setMessageType( JOptionPane.INFORMATION_MESSAGE );
                                dialog = optionPane.createDialog( getParent(), getInformationMessage( getLocale() ) );
                                dialog.setResizable( isResizable() );
                                dialog.setVisible( true );
                                dialog.dispose();
                                break;

                            case MessageEvent.NOTIFICATION:
                                optionPane = new JOptionPane();
                                optionPane.setMessage( panel );
                                optionPane.setMessageType( JOptionPane.INFORMATION_MESSAGE );
                                dialog = optionPane.createDialog( getParent(), getNotificationMessage( getLocale() ) );
                                dialog.setResizable( isResizable() );
                                dialog.setVisible( true );
                                dialog.dispose();
                                break;

                            case MessageEvent.WARNING:
                                optionPane = new JOptionPane();
                                optionPane.setMessage( panel );
                                optionPane.setMessageType( JOptionPane.WARNING_MESSAGE );
                                dialog = optionPane.createDialog( getParent(), getWarningMessage( getLocale() ) );
                                dialog.setResizable( isResizable() );
                                dialog.setVisible( true );
                                dialog.dispose();
                                break;

                            case MessageEvent.ERROR:
                                optionPane = new JOptionPane();
                                optionPane.setMessage( panel );
                                optionPane.setMessageType( JOptionPane.ERROR_MESSAGE );
                                dialog = optionPane.createDialog( getParent(), getErrorMessage( getLocale() ) );
                                dialog.setResizable( isResizable() );
                                dialog.setVisible( true );
                                dialog.dispose();
                                break;

                            default:
                                getLogger().warn( getUnknownMessageEventTypeMessage(
                                    getLocale(), new Integer( event.getType() ) ) );

                        }
                    }

                };

                if ( SwingUtilities.isEventDispatchThread() )
                {
                    r.run();
                }
                else
                {
                    SwingUtilities.invokeAndWait( r );
                }
            }
            catch ( final InterruptedException e )
            {
                this.getLogger().error( e );
            }
            catch ( final InvocationTargetException e )
            {
                this.getLogger().error( e );
            }
        }
    }

    //---------------------------------------------------------MessageListener--
    //--SwingMessagePane--------------------------------------------------------

    /** The current parent component to use when displaying messages. */
    private Component parent;

    /** Maximum number of messages displayed per event. */
    private Integer maximumMessages;

    /** Number of columns the preferred width of the message pane is computed with. */
    private Integer columns;

    /** Flag indicating the message pane is resizable. */
    private Boolean resizable;

    /**
     * Creates a new {@code SwingMessagePane} instance taking the parent component to use when displaying messages.
     *
     * @param parent The parent component to use when displaying messages.
     *
     * @throws NullPointerException if {@code parent} is {@code null}.
     * @throws HeadlessException if this class is used in an environment not providing a keyboard, display, or mouse.
     */
    public SwingMessagePane( final Component parent )
    {
        this( parent, Integer.MIN_VALUE, Integer.MIN_VALUE, false );
    }

    /**
     * Creates a new {@code SwingMessagePane} instance taking the parent component to use when displaying messages and
     * the maximum number of messages displayed per event.
     *
     * @param parent The parent component to use when displaying messages.
     * @param maximumMessages Maximum number of messages displayed per event.
     *
     * @throws NullPointerException if {@code parent} is {@code null}.
     * @throws HeadlessException if this class is used in an environment not providing a keyboard, display, or mouse.
     */
    public SwingMessagePane( final Component parent, final int maximumMessages )
    {
        this( parent, maximumMessages, Integer.MIN_VALUE, false );
    }

    /**
     * Creates a new {@code SwingMessagePane} instance taking the parent component to use when displaying messages, the
     * maximum number of messages displayed per event and a number of columns to compute the preferred width of the
     * message pane with.
     *
     * @param parent The parent component to use when displaying messages.
     * @param maximumMessages Maximum number of messages displayed per event.
     * @param columns Number of columns the preferred width of the message pane should be computed with.
     *
     * @throws NullPointerException if {@code parent} is {@code null}.
     * @throws HeadlessException if this class is used in an environment not providing a keyboard, display, or mouse.
     *
     * @since 1.10
     */
    public SwingMessagePane( final Component parent, final int maximumMessages, final int columns )
    {
        this( parent, maximumMessages, columns, false );
    }

    /**
     * Creates a new {@code SwingMessagePane} instance taking the parent component to use when displaying messages, the
     * maximum number of messages displayed per event, a number of columns to compute the preferred width of the
     * message pane with and a flag indicating the message pane is resizable.
     *
     * @param parent The parent component to use when displaying messages.
     * @param maximumMessages Maximum number of messages displayed per event.
     * @param columns Number of columns the preferred width of the message pane should be computed with.
     * @param resizable {@code true}, if the message pane should be resizable; {@code false} if the message pane should
     * have a fixed size.
     *
     * @throws NullPointerException if {@code parent} is {@code null}.
     * @throws HeadlessException if this class is used in an environment not providing a keyboard, display, or mouse.
     *
     * @since 1.11
     */
    public SwingMessagePane( final Component parent, final int maximumMessages, final int columns,
                             final boolean resizable )
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
        if ( columns > 0 )
        {
            this.columns = new Integer( columns );
        }

        this.resizable = Boolean.valueOf( resizable );
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

    /**
     * Gets the number of columns the preferred width of the message pane is computed with.
     *
     * @return The number of columns the preferred width of the message pane is computed with.
     *
     * @since 1.10
     */
    public int getColumns()
    {
        if ( this.columns == null )
        {
            this.columns = this.getDefaultColumns();
        }

        return this.columns.intValue();
    }

    /**
     * Gets a flag indicating the message pane is resizable.
     *
     * @return {@code true}, if the message pane is resizable; {@code false} if the message pane is not resizable.
     *
     * @since 1.11
     */
    public boolean isResizable()
    {
        if ( this.resizable == null )
        {
            this.resizable = this.isDefaultResizable();
        }

        return this.resizable.booleanValue();
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

/**
 * Extension to {@code JLabel} adding support for a preferred width based on a number of columns.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
class HtmlLabel extends JLabel
{

    /** Serial version UID for backwards compatibility with 1.10.x classes. */
    private static final long serialVersionUID = -3022796716512336911L;

    /** The number of columns the preferred width of the label is computed with. */
    private int columns;

    /**
     * Creates a new {@code HtmlLabel} instance taking a number of columns to compute the preferred width of the label
     * with.
     *
     * @param columns The number of columns to compute the preferred width of the label with.
     */
    HtmlLabel( final int columns )
    {
        super();
        this.columns = columns;
        this.setVerticalAlignment( SwingConstants.TOP );
        this.setVerticalTextPosition( SwingConstants.TOP );

        if ( columns > 0 )
        {
            this.addPropertyChangeListener( new PropertyChangeListener()
            {

                public void propertyChange( final PropertyChangeEvent evt )
                {
                    if ( ( "text".equals( evt.getPropertyName() )
                           || "font".equals( evt.getPropertyName() )
                           || "border".equals( evt.getPropertyName() ) )
                         && getText() != null
                         && BasicHTML.isHTMLString( getText() ) )
                    {
                        setPreferredSize( computePreferredSize() );
                    }
                }

            } );
        }
    }

    private Dimension computePreferredSize()
    {
        Dimension preferredSize = null;
        final Insets insets = this.getInsets();
        final int width = this.columns * this.getFontMetrics( this.getFont() ).charWidth( 'W' )
                          + ( insets != null ? insets.left + insets.right : 0 );

        final JLabel label = new JLabel( this.getText() );
        final Object html = label.getClientProperty( BasicHTML.propertyKey );

        if ( html instanceof View )
        {
            final View view = (View) html;
            view.setSize( width, 0 );
            preferredSize = new Dimension( (int) view.getPreferredSpan( View.X_AXIS ),
                                           (int) view.getPreferredSpan( View.Y_AXIS ) );

        }

        return preferredSize;
    }

}

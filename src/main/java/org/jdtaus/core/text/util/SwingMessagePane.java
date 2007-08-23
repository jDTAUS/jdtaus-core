/*
 *  jDTAUS - DTAUS fileformat.
 *  Copyright (c) 2005 Christian Schulte <cs@schulte.it>
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
import org.jdtaus.core.container.ContextFactory;
import org.jdtaus.core.container.ContextInitializer;
import org.jdtaus.core.container.Implementation;
import org.jdtaus.core.container.ModelFactory;
import org.jdtaus.core.container.Properties;
import org.jdtaus.core.container.Property;
import org.jdtaus.core.container.PropertyException;
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
    //--Implementation----------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausImplementation
    // This section is managed by jdtaus-container-mojo.

    /** Meta-data describing the implementation. */
    private static final Implementation META =
        ModelFactory.getModel().getModules().
        getImplementation(SwingMessagePane.class.getName());
// </editor-fold>//GEN-END:jdtausImplementation

    //----------------------------------------------------------Implementation--
    //--Constructors------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausConstructors
    // This section is managed by jdtaus-container-mojo.

    /**
     * Initializes the properties of the instance.
     *
     * @param meta the property values to initialize the instance with.
     *
     * @throws NullPointerException if {@code meta} is {@code null}.
     */
    private void initializeProperties(final Properties meta)
    {
        Property p;

        if(meta == null)
        {
            throw new NullPointerException("meta");
        }

        p = meta.getProperty("maximumMessages");
        this._maximumMessages = ((java.lang.Integer) p.getValue()).intValue();

    }
// </editor-fold>//GEN-END:jdtausConstructors

    //------------------------------------------------------------Constructors--
    //--Dependencies------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausDependencies
    // This section is managed by jdtaus-container-mojo.

    /** Configured <code>Logger</code> implementation. */
    private transient Logger _dependency0;

    /**
     * Gets the configured <code>Logger</code> implementation.
     *
     * @return the configured <code>Logger</code> implementation.
     */
    private Logger getLogger()
    {
        Logger ret = null;
        if(this._dependency0 != null)
        {
            ret = this._dependency0;
        }
        else
        {
            ret = (Logger) ContainerFactory.getContainer().
                getDependency(SwingMessagePane.class,
                "Logger");

            if(ModelFactory.getModel().getModules().
                getImplementation(SwingMessagePane.class.getName()).
                getDependencies().getDependency("Logger").
                isBound())
            {
                this._dependency0 = ret;
            }
        }

        if(ret instanceof ContextInitializer && !((ContextInitializer) ret).
            isInitialized(ContextFactory.getContext()))
        {
            ((ContextInitializer) ret).initialize(ContextFactory.getContext());
        }

        return ret;
    }
// </editor-fold>//GEN-END:jdtausDependencies

    //------------------------------------------------------------Dependencies--
    //--Properties--------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausProperties
    // This section is managed by jdtaus-container-mojo.

    /**
     * Property {@code maximumMessages}.
     * @serial
     */
    private int _maximumMessages;

    /**
     * Gets the value of property <code>maximumMessages</code>.
     *
     * @return the value of property <code>maximumMessages</code>.
     */
    public int getMaximumMessages()
    {
        return this._maximumMessages;
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
    public void onMessage(final MessageEvent event)
    {
        if(event != null)
        {
            SwingUtilities.invokeLater(new Runnable()
            {
                public void run()
                {
                    final Object[] messages =
                        new Object[event.getMessages().length];

                    for(int i = 0; i < messages.length &&
                        i < getMaximumMessages(); i++)
                    {
                        messages[i] = event.getMessages()[i].
                            getText(Locale.getDefault());

                    }

                    switch(event.getType())
                    {
                        case MessageEvent.INFORMATION:
                            JOptionPane.showMessageDialog(getParent(),
                                messages, SwingMessagePaneBundle.
                                getInformationText(Locale.getDefault()),
                                JOptionPane.INFORMATION_MESSAGE);

                            break;

                        case MessageEvent.NOTIFICATION:
                            JOptionPane.showMessageDialog(getParent(),
                                messages, SwingMessagePaneBundle.
                                getNotificationText(Locale.getDefault()),
                                JOptionPane.INFORMATION_MESSAGE);

                            break;

                        case MessageEvent.WARNING:
                            JOptionPane.showMessageDialog(getParent(),
                                messages, SwingMessagePaneBundle.
                                getWarningText(Locale.getDefault()),
                                JOptionPane.WARNING_MESSAGE);

                            break;

                        case MessageEvent.ERROR:
                            UIManager.getLookAndFeel().provideErrorFeedback(
                                getParent());

                            JOptionPane.showMessageDialog(getParent(),
                                messages, SwingMessagePaneBundle.
                                getErrorText(Locale.getDefault()),
                                JOptionPane.ERROR_MESSAGE);

                            break;

                        default:
                            getLogger().warn(SwingMessagePaneBundle.
                                getUnknownMessageTypeMessage(
                                Locale.getDefault()).format(new Object[] {
                                new Integer(event.getType()) }));

                    }
                }
            });
        }
    }

    //---------------------------------------------------------MessageListener--
    //--SwingMessagePane--------------------------------------------------------

    /** The current parent component to use when displaying messages. */
    private Component parent;

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
    public SwingMessagePane(final Component parent)
    {
        if(parent == null)
        {
            throw new NullPointerException("parent");
        }

        if(GraphicsEnvironment.isHeadless())
        {
            throw new HeadlessException();
        }

        this.initializeProperties(META.getProperties());
        this.assertValidProperties();

        this.parent = parent;
    }

    /**
     * Creates a new {@code SwingMessagePane} instance taking the parent
     * component to use when displaying messages and the maximum number of
     * messages to be displayed per event.
     *
     * @param parent the parent component to use when displaying messages.
     * @param maximumMessages maximum number of messages to be displayed per
     * event.
     *
     * @throws NullPointerException if {@code parent} is {@code null}.
     * @throws PropertyException if {@code maximumMessages} is not positive.
     * @throws HeadlessException if this class is used in an environment
     * not providing a keyboard, display, or mouse.
     */
    public SwingMessagePane(final Component parent, final int maximumMessages)
    {
        if(parent == null)
        {
            throw new NullPointerException("parent");
        }

        if(GraphicsEnvironment.isHeadless())
        {
            throw new HeadlessException();
        }

        this.initializeProperties(META.getProperties());
        this._maximumMessages = maximumMessages;
        this.assertValidProperties();

        this.parent = parent;
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
     * Sets the parent component to be used by any message displays.
     *
     * @param parent the parent component to be used by any message displays.
     *
     * @throws NullPointerException if {@code parent} is {@code null}.
     */
    public void setParent(final Component parent)
    {
        if(parent == null)
        {
            throw new NullPointerException("parent");
        }

        this.parent = parent;
    }

    /**
     * Checks configured properties.
     *
     * @throws PropertyException for illegal property values.
     */
    private void assertValidProperties()
    {
        if(this.getMaximumMessages() <= 0)
        {
            throw new PropertyException("maximumMessages",
                Integer.toString(this.getMaximumMessages()));

        }
    }

    //--------------------------------------------------------SwingMessagePane--
}

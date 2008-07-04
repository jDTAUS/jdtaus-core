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
import org.jdtaus.core.container.ContextFactory;
import org.jdtaus.core.container.ContextInitializer;
import org.jdtaus.core.container.Implementation;
import org.jdtaus.core.container.ModelFactory;
import org.jdtaus.core.container.Properties;
import org.jdtaus.core.container.Property;
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
    //--Implementation----------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausImplementation
    // This section is managed by jdtaus-container-mojo.

    /** Meta-data describing the implementation. */
    private static final Implementation META =
        ModelFactory.getModel().getModules().
        getImplementation(MessageLogger.class.getName());
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

    }
// </editor-fold>//GEN-END:jdtausConstructors

    //------------------------------------------------------------Constructors--
    //--Dependencies------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausDependencies
    // This section is managed by jdtaus-container-mojo.

    /** Configured <code>Logger</code> implementation. */
    private transient Logger dLogger;

    /**
     * Gets the configured <code>Logger</code> implementation.
     *
     * @return the configured <code>Logger</code> implementation.
     */
    private Logger getLogger()
    {
        Logger ret = null;
        if(this.dLogger != null)
        {
            ret = this.dLogger;
        }
        else
        {
            ret = (Logger) ContainerFactory.getContainer().
                getDependency(MessageLogger.class,
                "Logger");

            if(ModelFactory.getModel().getModules().
                getImplementation(MessageLogger.class.getName()).
                getDependencies().getDependency("Logger").
                isBound())
            {
                this.dLogger = ret;
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

// </editor-fold>//GEN-END:jdtausProperties

    //--------------------------------------------------------------Properties--
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
                        MessageLoggerBundle.getInstance().
                        getUnknownMessageTypeMessage( Locale.getDefault() ).
                        format( new Object[] {
                                new Integer( event.getType() )
                            } ) );

            }
        }
    }

    //---------------------------------------------------------MessageListener--
    //--MessageLogger-----------------------------------------------------------

    /** Creates a new {@code MessageLogger} instance. */
    public MessageLogger()
    {
        super();
        this.initializeProperties( META.getProperties() );
        this.assertValidProperties();
    }

    /**
     * Checks configured properties.
     *
     * @throws PropertyException for illegal property values.
     */
    private void assertValidProperties()
    {
    }

    //-----------------------------------------------------------MessageLogger--
}

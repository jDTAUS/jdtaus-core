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
package org.jdtaus.core.lang.util;

import java.io.File;
import java.net.URL;
import org.jdtaus.core.container.ContainerFactory;
import org.jdtaus.core.container.ContextFactory;
import org.jdtaus.core.container.ContextInitializer;
import org.jdtaus.core.container.Implementation;
import org.jdtaus.core.container.ModelFactory;
import org.jdtaus.core.container.Properties;
import org.jdtaus.core.container.Property;
import org.jdtaus.core.container.Specification;
import org.jdtaus.core.lang.ExceptionEvent;
import org.jdtaus.core.lang.ExceptionListener;
import org.jdtaus.core.messages.BugReportMessage;
import org.jdtaus.core.messages.ExceptionMessage;
import org.jdtaus.core.messages.UndefinedApplicationStateMessage;
import org.jdtaus.core.text.Message;
import org.jdtaus.core.text.MessageEvent;
import org.jdtaus.core.text.Messages;
import org.jdtaus.core.text.spi.ApplicationLogger;

/**
 * {@code ExceptionListener} resolving exceptions to corresponding application
 * messages.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 *
 * @see #onException(ExceptionEvent)
 * @see org.jdtaus.core.text.util.SwingMessagePane
 * @see org.jdtaus.core.text.util.MessageLogger
 */
public final class ExceptionMessageProducer implements ExceptionListener
{
    //--Implementation----------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausImplementation
    // This section is managed by jdtaus-container-mojo.

    /** Meta-data describing the implementation. */
    private static final Implementation META =
        ModelFactory.getModel().getModules().
        getImplementation(ExceptionMessageProducer.class.getName());
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

    /** Configured <code>ApplicationLogger</code> implementation. */
    private transient ApplicationLogger dApplicationLogger;

    /**
     * Gets the configured <code>ApplicationLogger</code> implementation.
     *
     * @return the configured <code>ApplicationLogger</code> implementation.
     */
    private ApplicationLogger getApplicationLogger()
    {
        ApplicationLogger ret = null;
        if(this.dApplicationLogger != null)
        {
            ret = this.dApplicationLogger;
        }
        else
        {
            ret = (ApplicationLogger) ContainerFactory.getContainer().
                getDependency(ExceptionMessageProducer.class,
                "ApplicationLogger");

            if(ModelFactory.getModel().getModules().
                getImplementation(ExceptionMessageProducer.class.getName()).
                getDependencies().getDependency("ApplicationLogger").
                isBound())
            {
                this.dApplicationLogger = ret;
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
    //--ExceptionListener-------------------------------------------------------

    /**
     * {@inheritDoc}
     * <p>This method resolves exceptions provided by an applications's
     * {@code ExceptionEventSource} to corresponding {@code MessageEvent}s which
     * an application may react to by registering a listener to its
     * {@code MessageEventSource}. Messages for checked exceptions are resolved
     * by using any available {@code ExceptionMessageResolver} implementation
     * stopping at the first implementation not returning {@code null}.
     * For exceptions which are instances of {@code RuntimeException} an
     * additional message informing the user that the application's state is
     * undefined and that a restart is recommended is added to the produced
     * {@code MessageEvent}. Optionally an additional message informing the user
     * on how to report a bug is added to the produced {@code MessageEvent} if
     * the instance got constructed using the constructor taking that
     * information.</p>
     *
     * @param event the event holding the exception.
     *
     * @see ExceptionMessageResolver#resolve(Exception)
     */
    public void onException( final ExceptionEvent event )
    {
        if ( event != null )
        {
            final Throwable exception = event.getException();
            final Throwable rootCause = event.getRootCause();
            final Messages messages = new Messages();

            messages.addMessage( new ExceptionMessage( exception ) );

            if ( rootCause instanceof RuntimeException )
            {
                messages.addMessage( new UndefinedApplicationStateMessage() );

                if ( this.logDirectory != null )
                {
                    messages.addMessage(
                        new BugReportMessage( this.logDirectory,
                                              this.trackerUrl,
                                              this.reportAddress ) );

                }
            }
            else if ( rootCause instanceof Exception )
            {
                final Message[] resolved =
                    this.resolveMessages( ( Exception ) rootCause );

                if ( resolved != null )
                {
                    messages.addMessages( resolved );
                }
            }

            this.getApplicationLogger().log(
                new MessageEvent( this,
                                  messages.getMessages(),
                                  MessageEvent.ERROR ) );

        }
    }

    //-------------------------------------------------------ExceptionListener--
    //--ExceptionReporter-------------------------------------------------------

    /**
     * Directory holding the application's log files.
     * @serial
     */
    private File logDirectory;

    /**
     * URL of the online bugtracking system.
     * @serial
     */
    private URL trackerUrl;

    /**
     * Mail address to send the bugreport to.
     * @serial
     */
    private String reportAddress;

    /** Creates a new {@code ExceptionMessageProducer} instance. */
    public ExceptionMessageProducer()
    {
        super();

        this.initializeProperties( META.getProperties() );

        this.logDirectory = null;
        this.trackerUrl = null;
        this.reportAddress = null;
    }

    /**
     * Creates a new {@code ExceptionMessageProducer} instance taking the
     * application's logfile directory, an URL to the application's online
     * bugtracking system, and an email address where to send bugreports to
     * alternatively.
     *
     * @param logDirectory the directory holding the application's logfiles.
     * @param trackerUrl an URL to the application's online bugtracking system.
     * @param reportAddress an email address to alternatively send bugreports
     * to.
     *
     * @throws NullPointerException if either {@code logDirectory},
     * {@code trackerUrl} or {@code reportAddress} is {@code null}.
     * @throws IllegalArgumentException if {@code logDirectory} is not a
     * directory.
     */
    public ExceptionMessageProducer( final File logDirectory,
                                      final URL trackerUrl,
                                      final String reportAddress )
    {
        super();
        if ( logDirectory == null )
        {
            throw new NullPointerException( "logDirectory" );
        }
        if ( !logDirectory.isDirectory() )
        {
            throw new IllegalArgumentException( logDirectory.getAbsolutePath() );
        }
        if ( trackerUrl == null )
        {
            throw new NullPointerException( "trackerUrl" );
        }
        if ( reportAddress == null )
        {
            throw new NullPointerException( "reportAddress" );
        }

        this.initializeProperties( META.getProperties() );

        this.logDirectory = logDirectory;
        this.trackerUrl = trackerUrl;
        this.reportAddress = reportAddress;
    }

    /**
     * Resolves application messages for a given exception by querying any
     * available {@code ExceptionMessageResolver} implementation stopping at the
     * first implementation not returning {@code null}.
     *
     * @param exception the exception to resolve application messages for.
     *
     * @throws NullPointerException if {@code exception} is {@code null}.
     */
    private Message[] resolveMessages( final Exception exception )
    {
        if ( exception == null )
        {
            throw new NullPointerException( "exception" );
        }

        Message[] messages = null;
        final Specification spec = ModelFactory.getModel().getModules().
            getSpecification( ExceptionMessageResolver.class.getName() );

        final Implementation[] resolvers = spec.getImplementations().
            getImplementations();

        for ( int i = resolvers.length - 1; i >= 0 && messages == null; i-- )
        {
            final ExceptionMessageResolver resolver =
                ( ExceptionMessageResolver ) ContainerFactory.getContainer().
                getImplementation( ExceptionMessageResolver.class,
                                   resolvers[i].getName() );

            messages = resolver.resolve( exception );
        }

        return messages;
    }

    //-------------------------------------------------------ExceptionReporter--
}

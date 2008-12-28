/*
 *  jDTAUS Core Messages
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
package org.jdtaus.core.messages;

import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.util.Locale;
import org.jdtaus.core.container.ContainerFactory;
import org.jdtaus.core.text.Message;

/**
 * {@code Message} stating that an exception occured.
 * <p>The {@code Throwable} passed to the constructor of this class is used to
 * determine the message's format arguments. For chained exceptions, that is,
 * {@code getCause()} returns a non-{@code null} value, the root cause is used.
 * </p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public final class ExceptionMessage extends Message
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.0.x classes. */
    private static final long serialVersionUID = 3771927969335656661L;

    //---------------------------------------------------------------Constants--
    //--ExceptionMessage--------------------------------------------------------

    /**
     * Throwable of the message.
     * @serial
     */
    private final Throwable throwable;

    /**
     * Root cause to report.
     * @serial
     */
    private Throwable rootCause;

    /**
     * Creates a new {@code ExceptionMessage} taking the {@code Throwable} to
     * report.
     *
     * @param throwable the {@code Throwable} to report.
     *
     * @throws NullPointerException if {@code throwable} is {@code null}.
     */
    public ExceptionMessage( final Throwable throwable )
    {
        if ( throwable == null )
        {
            throw new NullPointerException( "throwable" );
        }

        Throwable current = throwable;
        Throwable report = current;
        while ( ( current = current.getCause() ) != null )
        {
            report = current;
        }

        this.throwable = throwable;
        this.rootCause = report;
    }

    //--------------------------------------------------------ExceptionMessage--
    //--Serializable------------------------------------------------------------

    /**
     * Takes care of initializing the {@code rootCause} field when constructed
     * from an 1.0.x object stream.
     *
     * @throws ObjectStreamException if no root cause can be resolved.
     */
    private Object readResolve() throws ObjectStreamException
    {
        if ( this.throwable != null && this.rootCause == null )
        {
            Throwable current = this.throwable;
            Throwable report = current;
            while ( ( current = current.getCause() ) != null )
            {
                report = current;
            }
            this.rootCause = report;
        }
        else if ( !( this.throwable != null && this.rootCause != null ) )
        {
            throw new InvalidObjectException(
                this.getMissingObjectStreamFieldMessage( this.getLocale() ) );

        }

        return this;
    }

    //------------------------------------------------------------Serializable--
    //--Message-----------------------------------------------------------------

    /**
     * {@inheritDoc}
     * <p>This method traverses up the chained hierarchy. The arguments
     * returned are constructed using the root cause.</p>
     *
     * @return Strings describing the throwable.
     * <ul>
     * <li>[0]: the fully qualified classname of the root cause.</li>
     * <li>[1]: the classname of the root cause without package.</li>
     * <li>[2]: the message of the root cause.</li>
     * </ul>
     */
    public Object[] getFormatArguments( final Locale locale )
    {
        String name = this.rootCause.getClass().getName();
        final int lastDot = name.lastIndexOf( '.' );

        if ( lastDot >= 0 )
        {
            name = name.substring( lastDot + 1 );
        }

        String message = this.rootCause.getMessage();
        if ( message == null )
        {
            message = this.getNoDetailsAvailableMessage( locale );
        }

        return new Object[]
            {
                this.rootCause.getClass().getName(), name, message
            };
    }

    /**
     * {@inheritDoc}
     *
     * @return The corresponding text from the message's {@code ResourceBundle}:
     * <blockquote><pre>
     * An {1} occured.
     * Details: {2}
     * </pre></blockquote>
     */
    public String getText( final Locale locale )
    {
        String name = this.rootCause.getClass().getName();
        final int lastDot = name.lastIndexOf( '.' );

        if ( lastDot >= 0 )
        {
            name = name.substring( lastDot + 1 );
        }

        String message = this.rootCause.getMessage();
        if ( message == null )
        {
            message = this.getNoDetailsAvailableMessage( locale );
        }

        String text;
        if ( this.rootCause instanceof Error )
        {
            text = this.getErrorInfoMessage( locale, name, message );
        }
        else
        {
            text = this.getExceptionInfoMessage( locale, name, message );
        }

        return text;
    }

    //-----------------------------------------------------------------Message--
    //--Dependencies------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausDependencies
    // This section is managed by jdtaus-container-mojo.

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
     * Gets the text of message <code>exceptionInfo</code>.
     * <blockquote><pre>Eine {0} ist aufgetreten.
     *{1}
     *
     *</pre></blockquote>
     * <blockquote><pre>A {0} occured.
     *{1}
     *
     *</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     * @param exceptionName Name of the exception.
     * @param exceptionMessage Message of the exception.
     *
     * @return Information about an exception.
     */
    private String getExceptionInfoMessage( final Locale locale,
            final java.lang.String exceptionName,
            final java.lang.String exceptionMessage )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "exceptionInfo", locale,
                new Object[]
                {
                    exceptionName,
                    exceptionMessage
                });

    }

    /**
     * Gets the text of message <code>errorInfo</code>.
     * <blockquote><pre>Ein {0} ist aufgetreten.
     *{1}
     *
     *</pre></blockquote>
     * <blockquote><pre>A {0} occured.
     *{1}
     *
     *</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     * @param errorName Name of the error.
     * @param errorMessage Message of the error.
     *
     * @return Information about an error.
     */
    private String getErrorInfoMessage( final Locale locale,
            final java.lang.String errorName,
            final java.lang.String errorMessage )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "errorInfo", locale,
                new Object[]
                {
                    errorName,
                    errorMessage
                });

    }

    /**
     * Gets the text of message <code>noDetailsAvailable</code>.
     * <blockquote><pre>Keine Details verfügbar.</pre></blockquote>
     * <blockquote><pre>No details available.</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     *
     * @return Information that no details are available.
     */
    private String getNoDetailsAvailableMessage( final Locale locale )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "noDetailsAvailable", locale, null );

    }

    /**
     * Gets the text of message <code>missingObjectStreamField</code>.
     * <blockquote><pre>Fehlende Felder im Datenstrom.</pre></blockquote>
     * <blockquote><pre>Missing fields in object stream.</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     *
     * @return Information that a field in an object stream is missong.
     */
    private String getMissingObjectStreamFieldMessage( final Locale locale )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "missingObjectStreamField", locale, null );

    }

// </editor-fold>//GEN-END:jdtausMessages

    //----------------------------------------------------------------Messages--
}

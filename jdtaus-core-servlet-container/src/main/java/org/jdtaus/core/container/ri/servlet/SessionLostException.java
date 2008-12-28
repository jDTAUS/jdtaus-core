/*
 *  jDTAUS Core RI Servlet Container
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
package org.jdtaus.core.container.ri.servlet;

import java.io.ObjectStreamException;
import java.util.Locale;
import org.jdtaus.core.container.ContainerFactory;

/**
 * Gets thrown when the {@code HttpSession} for a thread of execution is
 * {@code null}.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class SessionLostException extends IllegalStateException
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.0.x classes. */
    private static final long serialVersionUID = -7663896854184348690L;

    //---------------------------------------------------------------Constants--
    //--SessionLostException----------------------------------------------------

    /**
     * The locale of the exception.
     * @serial
     */
    private Locale locale;

    /***
     * The thread accessing the session.
     * @serial
     */
    private Thread thread;

    /**
     * Creates a new instance of {@code SessionLostException} taking
     * the {@code Thread} trying to access the session.
     *
     * @param thread the {@code Thread} accessing the session.
     * @deprecated Replaced by {@link SessionLostException#SessionLostException(java.util.Locale, java.lang.Thread) }
     */
    public SessionLostException( final Thread thread )
    {
        super();
        this.thread = thread;
        this.locale = Locale.getDefault();
    }

    /**
     * Creates a new instance of {@code SessionLostException} taking
     * the {@code Locale} of the exception and the {@code Thread} trying to
     * access the session.
     *
     * @param locale The locale of the exception.
     * @param thread the {@code Thread} accessing the session.
     */
    public SessionLostException( final Locale locale, final Thread thread )
    {
        super();
        this.locale = locale;
        this.thread = thread;
    }

    /**
     * Gets the thread accessing the session.
     *
     * @return the thread accessing the session or {@code null}.
     */
    public Thread getThread()
    {
        return this.thread;
    }

    /**
     * Returns the detail message string of this throwable.
     *
     * @return the detail message string of this {@code Throwable} instance or
     * {@code null}.
     */
    public String getMessage()
    {
        return this.getSessionLostMessage(
            this.locale, this.thread == null
                         ? null : this.thread.getName() );

    }

    //----------------------------------------------------SessionLostException--
    //--Serializable------------------------------------------------------------

    /**
     * Takes care of initializing fields when constructed from an 1.0.x object
     * stream.
     *
     * @throws ObjectStreamException if resolution fails.
     */
    private Object readResolve() throws ObjectStreamException
    {
        if ( this.locale == null )
        {
            this.locale = Locale.getDefault();
        }

        return this;
    }

    //------------------------------------------------------------Serializable--
    //--Messages----------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausMessages
    // This section is managed by jdtaus-container-mojo.

    /**
     * Gets the text of message <code>sessionLost</code>.
     * <blockquote><pre>Keine Sitzung für Thread {0}.</pre></blockquote>
     * <blockquote><pre>No session available for thread {0}.</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     * @param threadName Name of the thread without session.
     *
     * @return Information about the thread without session.
     */
    private String getSessionLostMessage( final Locale locale,
            final java.lang.String threadName )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "sessionLost", locale,
                new Object[]
                {
                    threadName
                });

    }

// </editor-fold>//GEN-END:jdtausMessages

    //----------------------------------------------------------------Messages--
}

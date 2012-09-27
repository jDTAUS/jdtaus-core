/*
 *  jDTAUS Core API
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
package org.jdtaus.core.lang;

import java.util.EventObject;

/**
 * Event holding an exception.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 *
 * @see ExceptionEventSource
 */
public class ExceptionEvent extends EventObject
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.4.x classes. */
    private static final long serialVersionUID = 5909424199091260187L;

    //---------------------------------------------------------------Constants--
    //--Constructors------------------------------------------------------------

    /**
     * Creates a new {@code ExceptionEvent} instance taking an exception and
     * a corresponding thread.
     *
     * @param source the source of the new event.
     * @param thread the thread {@code throwable} occured in.
     * @param throwable the exception which occured in {@code thread}.
     *
     * @throws NullPointerException if either {@code thread} or
     * {@code throwable} is {@code null}.
     */
    public ExceptionEvent( final Object source, final Thread thread,
                           final Throwable throwable )
    {
        super( source );

        if ( thread == null )
        {
            throw new NullPointerException( "thread" );
        }
        if ( throwable == null )
        {
            throw new NullPointerException( "throwable" );
        }

        this.thread = thread;
        this.throwable = throwable;
    }

    //------------------------------------------------------------Constructors--
    //--ExceptionEvent----------------------------------------------------------

    /** Thread {@code throwable} occured in. */
    private transient Thread thread;

    /**
     * Exception which occured in {@code thread}.
     * @serial
     */
    private Throwable throwable;

    /**
     * Getter for property {@code thread}.
     *
     * @return the thread of the event or {@code null}.
     */
    public Thread getThread()
    {
        return this.thread;
    }

    /**
     * Getter for property {@code exception}.
     *
     * @return the exception of the event.
     */
    public Throwable getException()
    {
        return this.throwable;
    }

    /**
     * Gets the root cause of the event's exception by traversing up the chained
     * exception hierarchy.
     *
     * @return the root cause of the event's exception.
     */
    public final Throwable getRootCause()
    {
        Throwable current = this.getException();
        Throwable root = current;

        while ( ( current = current.getCause() ) != null )
        {
            root = current;
        }

        return root;
    }

    /**
     * Creates a string representing the properties of the instance.
     *
     * @return a string representing the properties of the instance.
     */
    private String internalString()
    {
        return new StringBuffer( 500 ).append( '{' ).
            append( "thread=" ).append( this.thread ).
            append( ", throwable=" ).append( this.throwable ).
            append( '}' ).toString();

    }

    //----------------------------------------------------------ExceptionEvent--
    //--Object------------------------------------------------------------------

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object.
     */
    public String toString()
    {
        return super.toString() + this.internalString();
    }

    //------------------------------------------------------------------Object--
}

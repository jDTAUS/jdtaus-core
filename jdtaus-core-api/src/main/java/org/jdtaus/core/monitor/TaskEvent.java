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
package org.jdtaus.core.monitor;

import java.util.EventObject;

/**
 * Event produced by a {@code Task}.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 *
 * @see TaskEventSource
 */
public class TaskEvent extends EventObject
{
    //--Constants---------------------------------------------------------------

    /** Event constant indicating the start of a {@code Task}. */
    public static final int STARTED = 1001;

    /** Event constant indicating that state of a {@code Task} changed. */
    public static final int CHANGED_STATE = 1002;

    /** Event constant indicating the end of a {@code Task}. */
    public static final int ENDED = 1003;

    /** Serial version UID for backwards compatibility with 1.0.x classes. */
    private static final long serialVersionUID = 4764885368541939098L;

    //---------------------------------------------------------------Constants--
    //--Constructors------------------------------------------------------------

    /**
     * Creates a new instance of {@code TaskEvent}.
     *
     * @param source the task producing the new event.
     * @param type constant indicating the type of the event.
     *
     * @throws IllegalArgumentException if {@code type} is not equal to one of
     * the constants {@code STARTED}, {@code CHANGED_STATE} or {@code ENDED}.
     */
    public TaskEvent( final Task source, final int type )
    {
        super( source );

        if ( type != STARTED && type != CHANGED_STATE && type != ENDED )
        {
            throw new IllegalArgumentException( Integer.toString( type ) );
        }

        this.type = type;
    }

    //------------------------------------------------------------Constructors--
    //--TaskEvent---------------------------------------------------------------

    /**
     * Event type.
     * @serial
     */
    private final int type;

    /**
     * Getter for property {@code type}.
     *
     * @return the type of the event.
     */
    public final int getType()
    {
        return this.type;
    }

    /**
     * Gets the {@code Task} producing the event.
     *
     * @return the source of the event.
     */
    public final Task getTask()
    {
        return (Task) this.getSource();
    }

    /**
     * Creates a string representing the properties of the instance.
     *
     * @return a string representing the properties of the instance.
     */
    private String internalString()
    {
        return new StringBuffer( 500 ).append( '{' ).
            append( "source=" ).append( this.source ).
            append( ", type=" ).append( this.type ).
            append( '}' ).toString();

    }

    //---------------------------------------------------------------TaskEvent--
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

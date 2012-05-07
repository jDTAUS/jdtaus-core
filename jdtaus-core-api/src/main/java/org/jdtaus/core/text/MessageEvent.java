/*
 *  jDTAUS Core API
 *  Copyright (C) 2005 Christian Schulte
 *  <schulte2005@users.sourceforge.net>
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
package org.jdtaus.core.text;

import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.util.EventObject;
import java.util.Locale;

/**
 * Event holding messages.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $JDTAUS$
 *
 * @see MessageEventSource
 */
public class MessageEvent extends EventObject
{
    //--Constants---------------------------------------------------------------

    /** Event constant for information messages. */
    public static final int INFORMATION = 2001;

    /** Event constant for notification messages. */
    public static final int NOTIFICATION = 2002;

    /** Event constant for warning messages. */
    public static final int WARNING = 2003;

    /** Event constant for error messages. */
    public static final int ERROR = 2004;

    /** Serial version UID for backwards compatibility with 1.0.x classes. */
    private static final long serialVersionUID = 7335694054201632443L;

    //---------------------------------------------------------------Constants--
    //--Constructors------------------------------------------------------------

    /**
     * Creates a new {@code MessageEvent} instance taking a single message.
     *
     * @param source the source of the new event.
     * @param message the message for the new event.
     * @param type constant indicating the type of the events' messages.
     *
     * @throws NullPointerException if {@code message} is {@code null}.
     * @throws IllegalArgumentException if {@code source} is {@code null} or
     * {@code type} is not equal to one of the constants {@code INFORMATION},
     * {@code NOTIFICATION}, {@code WARNING} or {@code ERROR}.
     */
    public MessageEvent( final Object source, final Message message,
                         final int type )
    {
        this( source, new Message[]
              {
                  message
              }, type );
    }

    /**
     * Creates a new {@code MessageEvent} instance taking multiple messages.
     *
     * @param source the source of the new event.
     * @param messages the messages for the new event.
     * @param type constant indicating the type of the events' messages.
     *
     * @throws NullPointerException if {@code messages} is {@code null}.
     * @throws IllegalArgumentException if {@code source} is {@code null} or
     * {@code type} is not equal to one of the constants {@code INFORMATION},
     * {@code NOTIFICATION}, {@code WARNING} or {@code ERROR} or the length of
     * {@code messages} is zero.
     */
    public MessageEvent( final Object source, final Message[] messages,
                         final int type )
    {
        super( source );

        if ( messages == null )
        {
            throw new NullPointerException( "messages" );
        }
        if ( messages.length == 0 )
        {
            throw new IllegalArgumentException(
                Integer.toString( messages.length ) );

        }

        this.assertValidType( type );

        this.message = messages[0];
        this.messages = messages;
        this.type = type;
    }

    /**
     * Checks an integer to be equal to one of the constants
     * {@code INFORMATION}, {@code NOTIFICATION}, {@code WARNING} or
     * {@code ERROR}.
     *
     * @param type the integer to check.
     *
     * @throws IllegalArgumentException if {@code type} is not equal to
     * one of the constants {@code INFORMATION}, {@code NOTIFICATION},
     * {@code WARNING} and {@code ERROR}.
     */
    private void assertValidType( final int type )
    {
        if ( type != INFORMATION && type != NOTIFICATION &&
            type != WARNING && type != ERROR )
        {
            throw new IllegalArgumentException( Integer.toString( type ) );
        }
    }

    //------------------------------------------------------------Constructors--
    //--MessageEvent------------------------------------------------------------

    /**
     * Single message of the event.
     * <p>Kept for backward compatibility with 1.0.x object streams.</p>
     * @serial
     */
    private final Message message;

    /**
     * Messages of the event.
     * @serial
     */
    private Message[] messages;

    /**
     * Type of the event.
     * @serial
     */
    private final int type;

    /**
     * Getter for property {@code message}.
     *
     * @return the single message of the event.
     *
     * @deprecated Replaced by {@link #getMessages()}.
     */
    public final Message getMessage()
    {
        return this.message;
    }

    /**
     * Getter for property {@code messages}.
     *
     * @return the messages of the event.
     */
    public final Message[] getMessages()
    {
        return this.messages;
    }

    /**
     * Getter for property {@code type}.
     *
     * @return the type of the events' messages.
     */
    public final int getType()
    {
        return this.type;
    }

    /**
     * Creates a string representing the properties of the instance.
     *
     * @return a string representing the properties of the instance.
     */
    private String internalString()
    {
        final StringBuffer buf = new StringBuffer( 500 ).append( '{' );
        buf.append( "type=" ).append( this.type );

        for ( int i = 0; i < this.messages.length; i++ )
        {
            buf.append( ", messages[" ).append( i ).append( "]=" ).
                append( this.messages[i] );

        }

        return buf.append( '}' ).toString();
    }

    //------------------------------------------------------------MessageEvent--
    //--Serializable------------------------------------------------------------

    /**
     * Takes care of initializing the {@code messages} field when constructed
     * from an 1.0.x object stream.
     *
     * @throws ObjectStreamException if no messages can be resolved.
     */
    private Object readResolve() throws ObjectStreamException
    {
        if ( this.message != null && this.messages == null )
        {
            this.messages = new Message[]
                {
                    this.message
                };
        }
        else if ( !( this.message != null && this.messages != null ) )
        {
            throw new InvalidObjectException(
                MessageEventBundle.getInstance().
                getMissingMessagesMessage( Locale.getDefault() ) );

        }

        return this;
    }

    //------------------------------------------------------------Serializable--
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

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
package org.jdtaus.core.container;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Collection of messages.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class Messages extends ModelObject implements Cloneable, Serializable
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.5.x classes. */
    private static final long serialVersionUID = -4280995595863489214L;

    //---------------------------------------------------------------Constants--
    //--Messages----------------------------------------------------------------

    /**
     * The messages of the collection.
     * @serial
     */
    private Message[] messages;

    /**
     * Maps message names to messages.
     * @serial
     */
    private final Map names = new HashMap();

    /**
     * Hash code.
     * @serial
     */
    private int hashCode;

    /** Creates a new {@code Messages} instance. */
    public Messages()
    {
        super();
    }

    /**
     * Gets the messages of the collection.
     *
     * @return the messages of the collection.
     */
    public Message[] getMessages()
    {
        if ( this.messages == null )
        {
            this.messages = new Message[ 0 ];
            this.hashCode = 0;
        }

        return this.messages;
    }

    /**
     * Setter for property {@code messages}.
     *
     * @param value the new messages for the instance.
     *
     * @throws DuplicateMessageException if {@code value} contains
     * duplicate messages.
     */
    public void setMessages( final Message[] value )
    {
        this.names.clear();
        this.hashCode = 0;
        this.messages = null;

        if ( value != null )
        {
            for ( int i = value.length - 1; i >= 0; i-- )
            {
                this.hashCode += value[i].hashCode();
                if ( this.names.put( value[i].getName(), value[i] ) != null )
                {
                    this.names.clear();
                    this.hashCode = 0;

                    throw new DuplicateMessageException( value[i].getName() );
                }
            }

            this.messages = value;
        }
    }

    /**
     * Gets a message for a name.
     *
     * @param name the name of the message to return.
     *
     * @return a reference to the message with name {@code name}.
     *
     * @throws NullPointerException if {@code name} is {@code null}.
     * @throws MissingMessageException if no message matching
     * {@code name} exists in the collection.
     */
    public Message getMessage( final String name )
    {
        if ( name == null )
        {
            throw new NullPointerException( "name" );
        }

        final Message ret = (Message) this.names.get( name );

        if ( ret == null )
        {
            throw new MissingMessageException( name );
        }

        return ret;
    }

    /**
     * Gets a message for an index.
     *
     * @param index the index of the message to return.
     *
     * @return a reference to the message at {@code index}.
     *
     * @throws IndexOutOfBoundsException if {@code index} is negativ,
     * greater than or equal to {@code size()}.
     */
    public final Message getMessage( final int index )
    {
        if ( index < 0 || index >= this.size() )
        {
            throw new ArrayIndexOutOfBoundsException( index );
        }

        return this.getMessages()[index];
    }

    /**
     * Gets the number of messages held by the instance.
     *
     * @return the number of messages held by the instance.
     */
    public final int size()
    {
        return this.getMessages().length;
    }

    /**
     * Creates a string representing the properties of the instance.
     *
     * @return a string representing the properties of the instance.
     */
    private String internalString()
    {
        final StringBuffer buf = new StringBuffer( 200 ).append( '{' );
        buf.append( this.internalString( this ) );

        final Message[] msgs = this.getMessages();
        for ( int i = msgs.length - 1; i >= 0; i-- )
        {
            buf.append( ", [" ).append( i ).append( "]=" ).append( msgs[i] );
        }

        buf.append( '}' );
        return buf.toString();
    }

    //----------------------------------------------------------------Messages--
    //--Object------------------------------------------------------------------

    /**
     * Indicates whether some other object is equal to this one by comparing
     * the values of all properties.
     *
     * @param o the reference object with which to compare.
     *
     * @return {@code true} if this object is the same as {@code o};
     * {@code false} otherwise.
     */
    public boolean equals( final Object o )
    {
        boolean equal = this == o;

        if ( !equal && o instanceof Messages )
        {
            final Messages that = (Messages) o;
            final Collection these = Arrays.asList( this.getMessages() );
            final Collection those = Arrays.asList( that.getMessages() );

            equal = this.size() == that.size() && these.containsAll( those );
        }

        return equal;
    }

    /**
     * Returns a hash code value for this object.
     *
     * @return a hash code value for this object.
     */
    public int hashCode()
    {
        return this.hashCode;
    }

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object.
     */
    public String toString()
    {
        return super.toString() + this.internalString();
    }

    /**
     * Creates and returns a deep copy of this object.
     *
     * @return a clone of this instance.
     */
    public Object clone()
    {
        try
        {
            final Messages ret = (Messages) super.clone();
            final Message[] msgs = this.getMessages();
            final Message[] cloned = new Message[ messages.length ];

            for ( int i = msgs.length - 1; i >= 0; i-- )
            {
                cloned[i] = (Message) msgs[i].clone();
            }

            ret.setMessages( cloned );
            return ret;
        }
        catch ( final CloneNotSupportedException e )
        {
            throw new AssertionError( e );
        }
    }

    //------------------------------------------------------------------Object--
}

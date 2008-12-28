/*
 *  jDTAUS Core API
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
package org.jdtaus.core.text;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Collection of messages.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class Messages implements Cloneable, Serializable
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.0.x classes. */
    private static final long serialVersionUID = 834125519895929330L;

    //---------------------------------------------------------------Constants--
    //--Messages----------------------------------------------------------------

    /**
     * Value of property {@code messages}.
     * @serial
     */
    private Collection messages;

    /**
     * Getter for property {@code messages}.
     *
     * @return messages held by the collection.
     */
    public Message[] getMessages()
    {
        if ( this.messages == null )
        {
            this.messages = new LinkedList();
        }

        return ( Message[] ) this.messages.toArray(
            new Message[ this.messages.size() ] );

    }

    /**
     * Accessor to messages of a given type.
     *
     * @param type the type of the messages to return.
     *
     * @return a collection of messages of type {@code type}.
     *
     * @throws NullPointerException if {@code type} is {@code null}.
     */
    public final Messages getMessages( final Class type )
    {
        if ( type == null )
        {
            throw new NullPointerException( "type" );
        }

        final Message[] msgs = this.getMessages();
        final Messages ret = new Messages();
        for ( int i = msgs.length - 1; i >= 0; i-- )
        {
            if ( type.isAssignableFrom( msgs[i].getClass() ) )
            {
                ret.addMessage( msgs[i] );
            }
        }

        return ret;
    }

    /**
     * Accessor to an indexed message.
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
     * Adds a message to the collection.
     *
     * @param message the message to add to the collection.
     *
     * @throws NullPointerException if {@code message} is {@code null}.
     */
    public void addMessage( final Message message )
    {
        if ( message == null )
        {
            throw new NullPointerException( "message" );
        }

        if ( this.messages == null )
        {
            this.messages = new LinkedList();
        }

        this.messages.add( message );
    }

    /**
     * Adds messages to the collection.
     *
     * @param messages collection of messages to add to the collection.
     *
     * @throws NullPointerException if {@code messages} is {@code null}.
     */
    public final void addMessages( final Messages messages )
    {
        if ( messages == null )
        {
            throw new NullPointerException( "messages" );
        }

        final Message[] msgs = messages.getMessages();
        for ( int i = msgs.length - 1; i >= 0; i-- )
        {
            this.addMessage( msgs[i] );
        }
    }

    /**
     * Adds an array of messages to the collection.
     *
     * @param messages array of messages to add to the collection.
     *
     * @throws NullPointerException if {@code messages} is {@code null} or
     * contains {@code null} elements.
     */
    public final void addMessages( final Message[] messages )
    {
        if ( messages == null )
        {
            throw new NullPointerException( "messages" );
        }

        for ( int i = messages.length - 1; i >= 0; i-- )
        {
            this.addMessage( messages[i] );
        }
    }

    /**
     * Removes a message from the collection.
     *
     * @param message the message to remove from the collection.
     *
     * @throws NullPointerException if {@code message} is {@code null}.
     */
    public void removeMessage( final Message message )
    {
        if ( message == null )
        {
            throw new NullPointerException( "message" );
        }

        if ( this.messages == null )
        {
            this.messages = new LinkedList();
        }

        this.messages.remove( message );
    }

    /**
     * Removes messages from the collection.
     *
     * @param messages collection of messages to remove from the collection.
     *
     * @throws NullPointerException if {@code messages} is {@code null}.
     */
    public final void removeMessages( final Messages messages )
    {
        if ( messages == null )
        {
            throw new NullPointerException( "messages" );
        }

        final Message[] msgs = messages.getMessages();
        for ( int i = msgs.length - 1; i >= 0; i-- )
        {
            this.removeMessage( msgs[i] );
        }
    }

    /**
     * Removes messages of a given type.
     *
     * @param type the type of the messages to remove.
     *
     * @return the collection of messages of type {@code type} removed from the
     * collection.
     *
     * @throws NullPointerException if {@code type} is {@code null}.
     */
    public final Messages removeMessages( final Class type )
    {
        if ( type == null )
        {
            throw new NullPointerException( "type" );
        }

        final Messages removed = this.getMessages( type );
        this.removeMessages( removed );
        return removed;
    }

    /**
     * Getter for property {@code size}.
     *
     * @return the number of elements in this collection.
     */
    public final int size()
    {
        return this.getMessages().length;
    }

    /** Removes all messages from the collection. */
    public void clear()
    {
        if ( this.messages == null )
        {
            this.messages = new LinkedList();
        }

        this.messages.clear();
    }

    //----------------------------------------------------------------Messages--
    //--Object------------------------------------------------------------------

    /**
     * Creates and returns a deep copy of this object.
     *
     * @return a clone of this instance.
     */
    public Object clone()
    {
        try
        {
            final Message[] msgs = this.getMessages();
            final Messages ret = ( Messages ) super.clone();

            ret.clear();
            for ( int i = msgs.length - 1; i >= 0; i-- )
            {
                ret.addMessage( ( Message ) msgs[i].clone() );
            }

            return ret;
        }
        catch ( CloneNotSupportedException e )
        {
            throw new AssertionError( e );
        }
    }

    //------------------------------------------------------------------Object--
}

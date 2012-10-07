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
package org.jdtaus.core.text;

import java.io.Serializable;
import java.rmi.server.UID;
import java.util.Comparator;
import java.util.Locale;

/**
 * Application message.
 * <p>Application messages consist of at least the two properties
 * {@code timestamp} and {@code formatArguments}. The {@code timestamp} property
 * will be initialized during instantiation to hold the timestamp of instance
 * creation. Property {@code formatArguments} holds the arguments to use
 * for formatting message text. It is recommended that subclasses of this class
 * are declared {@code final} so that calling {@code getClass()} on a message
 * instance always returns a type uniquely identifying a message type in the
 * system.</p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 *
 * @see MessageEvent
 */
public abstract class Message implements Cloneable, Serializable
{
    //--Constants---------------------------------------------------------------

    /**
     * Comparator for sorting messages in ascending order of occurence.
     * <p><b>Note:</b><br/>
     * This comparator imposes orderings that are inconsistent with equals.</p>
     */
    public static final Comparator ASCENDING =
        new AscendingMessageComparator();

    /**
     * Comparator for sorting messages in descending order of occurence.
     * <p><b>Note:</b><br/>
     * This comparator imposes orderings that are inconsistent with equals.</p>
     */
    public static final Comparator DESCENDING =
        new DescendingMessageComparator();

    /** Serial version UID for backwards compatibility with 1.0.x classes. */
    private static final long serialVersionUID = -5747726994506247015L;

    //---------------------------------------------------------------Constants--
    //--Constructors------------------------------------------------------------

    /** Creates a new {@code Message} instance. */
    public Message()
    {
        super();
        this.timestamp = System.currentTimeMillis();
        this.uid = new UID();
    }

    //------------------------------------------------------------Constructors--
    //--Message-----------------------------------------------------------------

    /**
     * Unique message identifier.
     * @serial
     */
    private UID uid;

    /**
     * The timestamp this message got created.
     * @serial
     */
    private long timestamp;

    /**
     * Getter for property {@code timestamp}.
     *
     * @return the timestamp this message got created.
     */
    public final long getTimestamp()
    {
        return this.timestamp;
    }

    /**
     * Getter for property {@code formatArguments}.
     *
     * @param locale the locale to be used for the arguments to return.
     *
     * @return the arguments to use when formatting the message text.
     */
    public abstract Object[] getFormatArguments( Locale locale );

    /**
     * Gets the formatted message text.
     *
     * @param locale the locale to be used for the text to return.
     *
     * @return the text of the message for {@code locale}.
     */
    public abstract String getText( Locale locale );

    /**
     * Creates a string representing the properties of the instance.
     *
     * @return a string representing the properties of the instance.
     */
    private String internalString()
    {
        final StringBuffer buf = new StringBuffer( 500 );

        buf.append( "\n\ttimestamp=" ).append( this.timestamp ).
            append( "\n\tuid=" ).append( this.uid ).
            append( "\n\ttext=" ).append( this.getText( Locale.getDefault() ) );

        final Object[] args = this.getFormatArguments( Locale.getDefault() );
        for ( int i = 0; i < args.length; i++ )
        {
            buf.append( "\n\tformatArguments[" ).append( i ).append( "]=" ).
                append( args[i] );

        }

        return buf.toString();
    }

    //-----------------------------------------------------------------Message--
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

    /**
     * Returns a hash code value for this object.
     *
     * @return a hash code value for this object.
     */
    public final int hashCode()
    {
        return this.uid.hashCode();
    }

    /**
     * Indicates whether some other object is equal to this one.
     * <p>Messages internally cary a UID which is created during instantiation.
     * This UID is used for comparing {@code o} with the instance.</p>
     *
     * @param o the reference object with which to compare.
     *
     * @return {@code true} if this object is the same as {@code o};
     * {@code false} otherwise.
     *
     * @see UID
     */
    public final boolean equals( final Object o )
    {
        return o == this || ( o instanceof Message &&
            ( ( Message ) o ).uid.equals( this.uid ) );

    }

    /**
     * Creates and returns a copy of this object.
     *
     * @return a clone of this instance.
     */
    public Object clone()
    {
        try
        {
            return super.clone();
        }
        catch ( final CloneNotSupportedException e )
        {
            throw new AssertionError( e );
        }
    }

    //------------------------------------------------------------------Object--
}

/**
 * Comparator for sorting messages in ascending order of occurence.
 * <p><b>Note:</b><br/>
 * This comparator imposes orderings that are inconsistent with equals.</p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
class AscendingMessageComparator implements Comparator, Serializable
{

    /** Creates a new {@code AscendingMessageComparator} instance. */
    AscendingMessageComparator()
    {
        super();
    }

    //--Comparator--------------------------------------------------------------

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException if either {@code o1} or {@code o2} is
     * {@code null}.
     * @throws ClassCastException if either {@code o1} or {@code o2} is
     * not an instance of {@code Message}.
     */
    public int compare( final Object o1, final Object o2 )
    {
        if ( o1 == null )
        {
            throw new NullPointerException( "o1" );
        }
        if ( o2 == null )
        {
            throw new NullPointerException( "o2" );
        }
        if ( !( o1 instanceof Message ) )
        {
            throw new ClassCastException( o1.getClass().getName() );
        }
        if ( !( o2 instanceof Message ) )
        {
            throw new ClassCastException( o2.getClass().getName() );
        }

        // TODO JDK 1.5 Long.valueOf(long)
        final Long l1 = new Long( ( ( Message ) o1 ).getTimestamp() );
        final Long l2 = new Long( ( ( Message ) o2 ).getTimestamp() );
        return l1.compareTo( l2 );
    }

    //--------------------------------------------------------------Comparator--
}

/**
 * Comparator for sorting messages in descending order of occurence.
 * <p><b>Note:</b><br/>
 * This comparator imposes orderings that are inconsistent with equals.</p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
class DescendingMessageComparator
    extends AscendingMessageComparator
{

    /** Creates a new {@code DescendingMessageComparator} instance. */
    DescendingMessageComparator()
    {
        super();
    }

    //--Comparator--------------------------------------------------------------

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException if either {@code o1} or {@code o2} is
     * {@code null}.
     * @throws ClassCastException if either {@code o1} or {@code o2} is
     * not an instance of {@code Message}.
     */
    public int compare( final Object o1, final Object o2 )
    {
        return super.compare( o2, o1 );
    }

    //--------------------------------------------------------------Comparator--
}

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
package org.jdtaus.core.container;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Collection of implementations.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class Implementations extends ModelObject
    implements Cloneable, Serializable
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.0.x classes. */
    private static final long serialVersionUID = 5611937453792676565L;

    //---------------------------------------------------------------Constants--
    //--Implementations---------------------------------------------------------

    /**
     * The implementations held by the instance.
     * @serial
     */
    private Implementation[] implementations;

    /**
     * Maps implementation identifiers to implementations.
     * @serial
     */
    private final Map identifiers = new HashMap();

    /**
     * Hash code.
     * @serial
     */
    private int hashCode;

    /**
     * Gets all implementations of the collection.
     *
     * @return all implementations of the collection.
     */
    public Implementation[] getImplementations()
    {
        if ( this.implementations == null )
        {
            this.implementations = new Implementation[ 0 ];
            this.hashCode = 0;
        }

        return this.implementations;
    }

    /**
     * Setter for property {@code implementations}.
     *
     * @param value the new implementations for the instance.
     *
     * @throws DuplicateImplementationException if {@code value} contains
     * duplicate implementations.
     */
    public void setImplementations( final Implementation[] value )
    {
        this.identifiers.clear();
        this.hashCode = 0;
        this.implementations = null;

        if ( value != null )
        {
            for ( int i = value.length - 1; i >= 0; i-- )
            {
                this.hashCode += value[i].hashCode();

                if ( this.identifiers.put( value[i].getIdentifier(),
                                           value[i] ) != null )
                {
                    this.identifiers.clear();
                    this.hashCode = 0;

                    throw new DuplicateImplementationException(
                        value[i].getIdentifier() );

                }
            }

            this.implementations = value;
        }
    }

    /**
     * Gets an implementation for an identifier.
     *
     * @param identifier the identifier of the implementation to return.
     *
     * @return a reference to the implementation identified by
     * {@code identifier}.
     *
     * @throws NullPointerException if {@code identifier} is {@code null}.
     * @throws MissingImplementationException if no implementation matching
     * {@code identifier} exists in the collection.
     */
    public Implementation getImplementation( final String identifier )
    {
        if ( identifier == null )
        {
            throw new NullPointerException( "identifier" );
        }

        final Implementation ret =
            (Implementation) this.identifiers.get( identifier );

        if ( ret == null )
        {
            throw new MissingImplementationException( identifier );
        }

        return ret;
    }

    /**
     * Gets an implementation for an index.
     *
     * @param index the index of the implementation to return.
     *
     * @return a reference to the implementation at {@code index}.
     *
     * @throws IndexOutOfBoundsException if {@code index} is negativ,
     * greater than or equal to {@code size()}.
     */
    public final Implementation getImplementation( final int index )
    {
        if ( index < 0 || index >= this.size() )
        {
            throw new ArrayIndexOutOfBoundsException( index );
        }

        return this.getImplementations()[index];
    }

    /**
     * Gets the number of implementations held by the instance.
     *
     * @return the number of implementations held by the instance.
     */
    public final int size()
    {
        return this.getImplementations().length;
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

        final Implementation[] impls = this.getImplementations();
        for ( int i = impls.length - 1; i >= 0; i-- )
        {
            buf.append( ", [" ).append( i ).append( "]=" ).
                append( impls[i] );

        }

        buf.append( '}' );
        return buf.toString();
    }

    //---------------------------------------------------------Implementations--
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

        if ( !equal && o instanceof Implementations )
        {
            final Implementations that = (Implementations) o;
            final Collection these = Arrays.asList( this.getImplementations() );
            final Collection those = Arrays.asList( that.getImplementations() );

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
            final Implementations ret = (Implementations) super.clone();
            final Implementation[] impls = this.getImplementations();
            final Implementation[] cloned = new Implementation[ impls.length ];

            for ( int i = impls.length - 1; i >= 0; i-- )
            {
                cloned[i] = (Implementation) impls[i].clone();
            }

            ret.setImplementations( cloned );
            return ret;
        }
        catch ( CloneNotSupportedException e )
        {
            throw new AssertionError( e );
        }
    }

    //------------------------------------------------------------------Object--
}

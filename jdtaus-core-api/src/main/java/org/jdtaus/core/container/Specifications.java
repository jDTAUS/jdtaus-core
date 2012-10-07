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
 * Collection of specifications.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class Specifications extends ModelObject
    implements Cloneable, Serializable
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.0.x classes. */
    private static final long serialVersionUID = 9166476268404849994L;

    //---------------------------------------------------------------Constants--
    //--Specifications----------------------------------------------------------

    /**
     * The specifications of the collection.
     * @serial
     */
    private Specification[] specifications;

    /**
     * Maps specification identifiers to specifications.
     * @serial
     */
    private final Map identifiers = new HashMap();

    /**
     * Hash code.
     * @serial
     */
    private int hashCode;

    /** Creates a new {@code Specifications} instance. */
    public Specifications()
    {
        super();
    }

    /**
     * Gets the specifications of the collection.
     *
     * @return the specifications of the collection.
     */
    public Specification[] getSpecifications()
    {
        if ( this.specifications == null )
        {
            this.specifications = new Specification[ 0 ];
            this.hashCode = 0;
        }

        return this.specifications;
    }

    /**
     * Setter for property {@code specifications}.
     *
     * @param value the new specifications for the instance.
     *
     * @throws DuplicateSpecificationException if {@code value} contains
     * duplicate specifications.
     */
    public void setSpecifications( final Specification[] value )
    {
        this.identifiers.clear();
        this.hashCode = 0;
        this.specifications = null;

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

                    throw new DuplicateSpecificationException(
                        value[i].getIdentifier() );

                }
            }

            this.specifications = value;
        }
    }

    /**
     * Gets a specification for an identifier.
     *
     * @param identifier the identifier of the specification to return.
     *
     * @return a reference to the specification identified by
     * {@code identifier}.
     *
     * @throws NullPointerException if {@code identifier} is {@code null}.
     * @throws MissingSpecificationException if no specification matching
     * {@code identifier} exists in the collection.
     */
    public Specification getSpecification( final String identifier )
    {
        if ( identifier == null )
        {
            throw new NullPointerException( "identifier" );
        }

        final Specification ret =
            (Specification) this.identifiers.get( identifier );

        if ( ret == null )
        {
            throw new MissingSpecificationException( identifier );
        }

        return ret;
    }

    /**
     * Gets a specification for an index.
     *
     * @param index the index of the specification to return.
     *
     * @return a reference to the specification at {@code index}.
     *
     * @throws IndexOutOfBoundsException if {@code index} is negativ,
     * greater than or equal to {@code size()}.
     */
    public final Specification getSpecification( final int index )
    {
        if ( index < 0 || index >= this.size() )
        {
            throw new ArrayIndexOutOfBoundsException( index );
        }

        return this.getSpecifications()[index];
    }

    /**
     * Gets the number of specifications held by the instance.
     *
     * @return the number of specifications held by the instance.
     */
    public final int size()
    {
        return this.getSpecifications().length;
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

        final Specification[] specs = this.getSpecifications();
        for ( int i = specs.length - 1; i >= 0; i-- )
        {
            buf.append( ", [" ).append( i ).append( "]=" ).append( specs[i] );
        }

        buf.append( '}' );
        return buf.toString();
    }

    //----------------------------------------------------------Specifications--
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

        if ( !equal && o instanceof Specifications )
        {
            final Specifications that = (Specifications) o;
            final Collection these = Arrays.asList( this.getSpecifications() );
            final Collection those = Arrays.asList( that.getSpecifications() );

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
            final Specifications ret = (Specifications) super.clone();
            final Specification[] specs = this.getSpecifications();
            final Specification[] cloned = new Specification[ specs.length ];

            for ( int i = specs.length - 1; i >= 0; i-- )
            {
                cloned[i] = (Specification) specs[i].clone();
            }

            ret.setSpecifications( cloned );
            return ret;
        }
        catch ( final CloneNotSupportedException e )
        {
            throw new AssertionError( e );
        }
    }

    //------------------------------------------------------------------Object--
}

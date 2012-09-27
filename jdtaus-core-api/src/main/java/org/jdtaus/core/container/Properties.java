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
 * Collection of properties.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class Properties extends ModelObject implements Cloneable, Serializable
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.0.x classes. */
    private static final long serialVersionUID = 3703581195509652826L;

    //---------------------------------------------------------------Constants--
    //--Properties--------------------------------------------------------------

    /**
     * The properties held by the instance.
     * @serial
     */
    private Property[] properties;

    /**
     * Maps property names to properties.
     * @serial
     */
    private final Map names = new HashMap();

    /**
     * Hash code.
     * @serial
     */
    private int hashCode;

    /**
     * Gets the properties of the collection.
     *
     * @return the properties of the collection.
     */
    public Property[] getProperties()
    {
        if ( this.properties == null )
        {
            this.properties = new Property[ 0 ];
            this.hashCode = 0;
        }

        return this.properties;
    }

    /**
     * Setter for property {@code properties}.
     *
     * @param value the new collection of properties.
     *
     * @throws DuplicatePropertyException if {@code value} contains duplicate
     * properties.
     */
    public void setProperties( final Property[] value )
    {
        this.names.clear();
        this.hashCode = 0;
        this.properties = null;

        if ( value != null )
        {
            for ( int i = value.length - 1; i >= 0; i-- )
            {
                this.hashCode += value[i].hashCode();
                if ( this.names.put( value[i].getName(), value[i] ) != null )
                {
                    this.names.clear();
                    this.hashCode = 0;

                    throw new DuplicatePropertyException( value[i].getName() );
                }
            }

            this.properties = value;
        }
    }

    /**
     * Gets a property for a name.
     *
     * @param name the name of the property to return.
     *
     * @return a reference to the property named {@code name}.
     *
     * @throws NullPointerException if {@code name} is {@code null}.
     * @throws MissingPropertyException if no property matching {@code name}
     * exists in the collection.
     */
    public Property getProperty( final String name )
    {
        if ( name == null )
        {
            throw new NullPointerException( "name" );
        }

        final Property ret = (Property) this.names.get( name );

        if ( ret == null )
        {
            throw new MissingPropertyException( name );
        }

        return ret;
    }

    /**
     * Gets a property for an index.
     *
     * @param index the index of the property to return.
     *
     * @return a reference to the property at {@code index}.
     *
     * @throws IndexOutOfBoundsException if {@code index} is negativ,
     * greater than or equal to {@code size()}.
     */
    public final Property getProperty( final int index )
    {
        if ( index < 0 || index >= this.size() )
        {
            throw new ArrayIndexOutOfBoundsException( index );
        }

        return this.getProperties()[index];
    }

    /**
     * Setter for a named property from property {@code properties}.
     *
     * @param property the property to update or to add to property
     * {@code properties}.
     *
     * @return previous value of the property named {@code property.getName()}
     * or {@code null} if no property with name {@code property.getName()}
     * existed before.
     *
     * @throws NullPointerException if {@code property} is {@code null}.
     * @throws IllegalArgumentException if a property with the same name but
     * different type exists.
     */
    public Property setProperty( final Property property )
    {
        if ( property == null )
        {
            throw new NullPointerException( "property" );
        }

        Property ret = null;

        try
        {
            final Property p = this.getProperty( property.getName() );
            if ( !p.getType().equals( property.getType() ) )
            {
                throw new IllegalArgumentException( p.getType().getName() );
            }
            else
            {
                ret = (Property) p.clone();
                this.hashCode -= p.hashCode();
                p.setValue( property.getValue() );
                this.hashCode += p.hashCode();
            }

        }
        catch ( MissingPropertyException e )
        {
            final Collection props = Arrays.asList( this.getProperties() );
            props.add( property );
            this.setProperties( (Property[]) props.toArray(
                                new Property[ props.size() ] ) );

        }

        return ret;
    }

    /**
     * Gets the number of properties held by the instance.
     *
     * @return the number of properties held by the instance.
     */
    public final int size()
    {
        return this.getProperties().length;
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

        final Property[] props = this.getProperties();
        for ( int i = props.length - 1; i >= 0; i-- )
        {
            buf.append( ", [" ).append( i ).append( "]=" ).append( props[i] );
        }

        buf.append( '}' );
        return buf.toString();
    }

    //--------------------------------------------------------------Properties--
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

        if ( !equal && o instanceof Properties )
        {
            final Properties that = (Properties) o;
            final Collection these = Arrays.asList( this.getProperties() );
            final Collection those = Arrays.asList( that.getProperties() );

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
            final Properties ret = (Properties) super.clone();
            final Property[] props = this.getProperties();
            final Property[] cloned = new Property[ props.length ];

            for ( int i = props.length - 1; i >= 0; i-- )
            {
                cloned[i] = (Property) props[i].clone();
            }

            ret.setProperties( cloned );
            return ret;
        }
        catch ( CloneNotSupportedException e )
        {
            throw new AssertionError( e );
        }
    }

    //------------------------------------------------------------------Object--
}

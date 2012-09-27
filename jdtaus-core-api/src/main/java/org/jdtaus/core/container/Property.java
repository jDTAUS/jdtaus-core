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

/**
 * Property meta-data.
 * <p>A property consists of the properties {@code name}, {@code type} and
 * {@code value}. Property {@code name} holds the name uniquely identifying the
 * property in a collection of properties. Property {@code type} holds the type
 * of the property. Property {@code value} holds the properties value which is
 * of type {@code type}. The {@code api} flag indicates if the property is part
 * of a public API.</p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class Property extends ModelObject implements Cloneable, Serializable
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.0.x classes. */
    private static final long serialVersionUID = -5450366074586725711L;

    //---------------------------------------------------------------Constants--
    //--Property----------------------------------------------------------------

    /**
     * The name of the property.
     * @serial
     */
    private String name;

    /**
     * The type of the property.
     * @serial
     */
    private Class type;

    /**
     * The value of the property.
     * @serial
     */
    private Object value;

    /**
     * Flag indicating that the property is part of a public API.
     * @serial
     */
    private boolean api;

    /**
     * Gets the name of the property.
     *
     * @return the name of the property.
     */
    public String getName()
    {
        if ( this.name == null )
        {
            this.name = "";
        }

        return this.name;
    }

    /**
     * Setter for property {@code name}.
     *
     * @param value the new name of the property.
     */
    public void setName( final String value )
    {
        this.name = value;
    }

    /**
     * Gets the type of the property.
     *
     * @return the type of the property.
     */
    public Class getType()
    {
        if ( this.type == null )
        {
            this.type = String.class;
        }

        return this.type;
    }

    /**
     * Setter for property {@code type}.
     *
     * @param value the new type of the property.
     */
    public void setType( final Class value )
    {
        this.type = value;
    }

    /**
     * Gets the value of the property.
     *
     * @return the value of the property or {@code null}.
     */
    public Object getValue()
    {
        return this.value;
    }

    /**
     * Setter for property {@code value}.
     *
     * @param value the new value of the property.
     */
    public void setValue( final Object value )
    {
        this.value = value;
    }

    /**
     * Gets the flag indicating if the property is part of a public API.
     *
     * @return {@code true} if the property is part of a public API.
     */
    public boolean isApi()
    {
        return this.api;
    }

    /**
     * Setter for property {@code api}.
     *
     * @param value {@code true} if the property is part of a public API.
     */
    public void setApi( final boolean value )
    {
        this.api = value;
    }

    /**
     * Creates a string representing the properties of the instance.
     *
     * @return a string representing the properties of the instance.
     */
    private String internalString()
    {
        return new StringBuffer( 500 ).append( '{' ).
            append( super.internalString( this ) ).
            append( ", api=" ).append( this.api ).
            append( ", name=" ).append( this.name ).
            append( ", type=" ).append( this.type ).
            append( ", value=" ).append( this.value ).
            append( '}' ).toString();

    }

    //----------------------------------------------------------------Property--
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

        if ( !equal && o instanceof Property )
        {
            final Property that = (Property) o;
            equal = this.getName().equals( that.getName() ) &&
                this.getType().equals( that.getType() ) &&
                ( this.getValue() == null
                ? that.getValue() == null
                : this.getValue().equals( that.getValue() ) );

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
        return this.getName().hashCode() +
            this.getType().hashCode() +
            ( this.getValue() == null ? 0 : this.getValue().hashCode() );

    }

    /**
     * Creates and returns a copy of this object. This method  performs a
     * "shallow copy" of this object, not a "deep copy" operation.
     *
     * @return a clone of this instance.
     */
    public Object clone()
    {
        try
        {
            return super.clone();
        }
        catch ( CloneNotSupportedException e )
        {
            throw new AssertionError( e );
        }
    }

    //------------------------------------------------------------------Object--
}

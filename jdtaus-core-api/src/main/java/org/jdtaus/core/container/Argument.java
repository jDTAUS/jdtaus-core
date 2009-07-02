/*
 *  jDTAUS Core API
 *  Copyright (c) 2005 Christian Schulte
 *
 *  Christian Schulte, Haldener Strasse 72, 58095 Hagen, Germany
 *  <schulte2005@users.sourceforge.net> (+49 2331 3543887)
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
 * Argument meta-data.
 * <p>An argument consists of the properties {@code index}, {@code name} and
 * {@code type}. Property {@code index} holds the index of the argument in a
 * list of arguments. Property {@code name} holds the name uniquely identifying
 * the argument in a set of arguments. Property {@code type} holds the type
 * of the argument.</p>
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $Id$
 */
public class Argument extends ModelObject implements Cloneable, Serializable
{
    //--Constants---------------------------------------------------------------

    /** Constant for property {@code type}. */
    public static final int TYPE_NUMBER = 27000;

    /** Constant for property {@code type}. */
    public static final int TYPE_DATE = 27001;

    /** Constant for property {@code type}. */
    public static final int TYPE_TIME = 27002;

    /** Constant for property {@code type}. */
    public static final int TYPE_TEXT = 27003;

    /** Serial version UID for backwards compatibility with 1.5.x classes. */
    private static final long serialVersionUID = 5250117542077493369L;

    //---------------------------------------------------------------Constants--
    //--Argument----------------------------------------------------------------

    /**
     * The index of the argument.
     * @serial
     */
    private int index;

    /**
     * The name of the argument.
     * @serial
     */
    private String name;

    /**
     * The type of the argument.
     * @serial
     */
    private int type;

    /**
     * Gets the index of the argument.
     *
     * @return the index of the argument.
     */
    public int getIndex()
    {
        return this.index;
    }

    /**
     * Setter for property {@code index}.
     *
     * @param value the new index of the argument.
     */
    public void setIndex( final int value )
    {
        this.index = value;
    }

    /**
     * Gets the name of the argument.
     *
     * @return the name of the argument.
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
     * @param value the new name of the argument.
     */
    public void setName( final String value )
    {
        this.name = value;
    }

    /**
     * Gets the type of the argument.
     *
     * @return the type of the argument.
     */
    public int getType()
    {
        return this.type;
    }

    /**
     * Setter for property {@code type}.
     *
     * @param value the new type of the argument.
     *
     * @throws IllegalArgumentException if {@code value} is not equal to one of
     * the constants {@code TYPE_NUMBER}, {@code TYPE_DATE}, {@code TYPE_TIME}
     * or {@code TYPE_TEXT}.
     */
    public void setType( final int value )
    {
        if ( value != TYPE_NUMBER && value != TYPE_DATE &&
            value != TYPE_TIME && value != TYPE_TEXT )
        {
            throw new IllegalArgumentException( Integer.toString( value ) );
        }

        this.type = value;
    }

    /**
     * Creates a string representing the properties of the instance.
     *
     * @return a string representing the properties of the instance.
     */
    private String internalString()
    {
        final StringBuffer buf = new StringBuffer( 500 ).append( '{' ).
            append( this.internalString( this ) ).
            append( ", index=" ).append( this.index ).
            append( ", name=" ).append( this.name ).
            append( ", type=" );

        if ( this.type == TYPE_NUMBER )
        {
            buf.append( "number" );
        }
        else if ( this.type == TYPE_DATE )
        {
            buf.append( "date" );
        }
        else if ( this.type == TYPE_TIME )
        {
            buf.append( "time" );
        }
        else if ( this.type == TYPE_TEXT )
        {
            buf.append( "text" );
        }

        return buf.append( '}' ).toString();
    }

    //----------------------------------------------------------------Argument--
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

        if ( !equal && o instanceof Argument )
        {
            final Argument that = (Argument) o;
            equal = this.getIndex() == that.getIndex() &&
                this.getType() == that.getType() &&
                this.getName().equals( that.getName() );

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
        int hc = 23;
        hc = 37 * hc + this.getIndex();
        hc = 37 * hc + this.getName().hashCode();
        hc = 37 * hc + this.getType();
        return hc;
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

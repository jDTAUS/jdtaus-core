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
 * Collection of arguments.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class Arguments extends ModelObject
    implements Cloneable, Serializable
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.5.x classes. */
    private static final long serialVersionUID = -36607029931578091L;

    //---------------------------------------------------------------Constants--
    //--Arguments---------------------------------------------------------------

    /**
     * The arguments of the collection.
     * @serial
     */
    private Argument[] arguments;

    /**
     * Maps argument names to arguments.
     * @serial
     */
    private final Map names = new HashMap();

    /**
     * Hash code.
     * @serial
     */
    private int hashCode;

    /** Creates a new {@code Arguments} instance. */
    public Arguments()
    {
        super();
    }

    /**
     * Gets the arguments of the collection.
     *
     * @return the arguments of the collection.
     */
    public Argument[] getArguments()
    {
        if ( this.arguments == null )
        {
            this.arguments = new Argument[ 0 ];
            this.hashCode = 0;
        }

        return this.arguments;
    }

    /**
     * Setter for property {@code arguments}.
     *
     * @param value the new arguments for the instance.
     *
     * @throws DuplicateArgumentException if {@code value} contains
     * duplicate arguments.
     */
    public void setArguments( final Argument[] value )
    {
        this.names.clear();
        this.hashCode = 0;
        this.arguments = null;

        if ( value != null )
        {
            for ( int i = 0; i < value.length; i++ )
            {
                this.hashCode += value[i].hashCode();
                if ( this.names.put( value[i].getName(), value[i] ) != null )
                {
                    this.names.clear();
                    this.hashCode = 0;

                    throw new DuplicateArgumentException( value[i].getName() );
                }
            }

            this.arguments = value;
        }
    }

    /**
     * Gets an argument for a name.
     *
     * @param name the name of the argument to return.
     *
     * @return a reference to the argument with name {@code name}.
     *
     * @throws NullPointerException if {@code name} is {@code null}.
     * @throws MissingArgumentException if no argument matching {@code name}
     * exists in the collection.
     */
    public Argument getArgument( final String name )
    {
        if ( name == null )
        {
            throw new NullPointerException( "name" );
        }

        final Argument ret = (Argument) this.names.get( name );

        if ( ret == null )
        {
            throw new MissingArgumentException( name );
        }

        return ret;
    }

    /**
     * Gets an argument for an index.
     *
     * @param index the index of the argument to return.
     *
     * @return a reference to the argument at {@code index}.
     *
     * @throws IndexOutOfBoundsException if {@code index} is negativ,
     * greater than or equal to {@code size()}.
     */
    public final Argument getArgument( final int index )
    {
        if ( index < 0 || index >= this.size() )
        {
            throw new ArrayIndexOutOfBoundsException( index );
        }

        return this.getArguments()[index];
    }

    /**
     * Gets the number of arguments held by the instance.
     *
     * @return the number of arguments held by the instance.
     */
    public final int size()
    {
        return this.getArguments().length;
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

        final Argument[] args = this.getArguments();
        for ( int i = args.length - 1; i >= 0; i-- )
        {
            buf.append( ", [" ).append( i ).append( "]=" ).
                append( args[i] );

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

        if ( !equal && o instanceof Arguments )
        {
            final Arguments that = (Arguments) o;
            final Collection these = Arrays.asList( this.getArguments() );
            final Collection those = Arrays.asList( that.getArguments() );

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
            final Arguments ret = (Arguments) super.clone();
            final Argument[] args = this.getArguments();
            final Argument[] cloned = new Argument[ args.length ];

            for ( int i = args.length - 1; i >= 0; i-- )
            {
                cloned[i] = (Argument) args[i].clone();
            }

            ret.setArguments( cloned );
            return ret;
        }
        catch ( final CloneNotSupportedException e )
        {
            throw new AssertionError( e );
        }
    }

    //------------------------------------------------------------------Object--
}

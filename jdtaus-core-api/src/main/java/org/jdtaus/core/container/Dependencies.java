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
package org.jdtaus.core.container;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Collection of dependencies.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class Dependencies extends ModelObject implements Cloneable, Serializable
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.0.x classes. */
    private static final long serialVersionUID = 4207539567564921413L;

    //---------------------------------------------------------------Constants--
    //--Dependencies------------------------------------------------------------

    /**
     * The dependencies held by the instance.
     * @serial
     */
    private Dependency[] dependencies;

    /**
     * Maps dependency names to dependencies.
     * @serial
     */
    private final Map names = new HashMap();

    /**
     * Hash code.
     * @serial
     */
    private int hashCode;

    /**
     * Gets all dependencies of the collection.
     *
     * @return all dependencies of the collection.
     */
    public Dependency[] getDependencies()
    {
        if ( this.dependencies == null )
        {
            this.dependencies = new Dependency[ 0 ];
            this.hashCode = 0;
        }

        return this.dependencies;
    }

    /**
     * Setter for property {@code dependencies}.
     *
     * @param value the new dependencies for the collection.
     *
     * @throws DuplicateDependencyException if {@code value} contains duplicate
     * dependencies.
     */
    public void setDependencies( final Dependency[] value )
    {
        this.names.clear();
        this.hashCode = 0;
        this.dependencies = null;

        if ( value != null )
        {
            for ( int i = value.length - 1; i >= 0; i-- )
            {
                this.hashCode += value[i].hashCode();
                if ( this.names.put( value[i].getName(), value[i] ) != null )
                {
                    this.names.clear();
                    this.hashCode = 0;

                    throw new DuplicateDependencyException( value[i].getName() );
                }

            }

            this.dependencies = value;
        }
    }

    /**
     * Gets a dependency for a name.
     *
     * @param name the name of the dependency to return.
     *
     * @return a reference to the dependency named {@code name}.
     *
     * @throws NullPointerException if {@code name} is {@code null}.
     * @throws MissingDependencyException if no dependency matching {@code name}
     * exists in the collection.
     */
    public Dependency getDependency( final String name )
    {
        if ( name == null )
        {
            throw new NullPointerException( "name" );
        }

        final Dependency ret = (Dependency) this.names.get( name );

        if ( ret == null )
        {
            throw new MissingDependencyException( name );
        }

        return ret;
    }

    /**
     * Gets a dependency for an index.
     *
     * @param index the index of the dependency to return.
     *
     * @return a reference to the dependency at {@code index}.
     *
     * @throws IndexOutOfBoundsException if {@code index} is negativ,
     * greater than or equal to {@code size()}.
     */
    public final Dependency getDependency( final int index )
    {
        if ( index < 0 || index >= this.size() )
        {
            throw new ArrayIndexOutOfBoundsException( index );
        }

        return this.getDependencies()[index];
    }

    /**
     * Gets the number of dependencies held by the instance.
     *
     * @return the number of dependencies held by the instance.
     */
    public final int size()
    {
        return this.getDependencies().length;
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

        final Dependency[] deps = this.getDependencies();
        for ( int i = deps.length - 1; i >= 0; i-- )
        {
            buf.append( ", [" ).append( i ).append( "]=" ).
                append( deps[i] );

        }
        buf.append( '}' );
        return buf.toString();
    }

    //------------------------------------------------------------Dependencies--
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

        if ( !equal && o instanceof Dependencies )
        {
            final Dependencies that = (Dependencies) o;
            final Collection these = Arrays.asList( this.getDependencies() );
            final Collection those = Arrays.asList( that.getDependencies() );

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
            final Dependencies ret = (Dependencies) super.clone();
            final Dependency[] deps = this.getDependencies();
            final Dependency[] cloned = new Dependency[ deps.length ];
            for ( int i = deps.length - 1; i >= 0; i-- )
            {
                cloned[i] = (Dependency) deps[i].clone();
            }

            ret.setDependencies( cloned );
            return ret;
        }
        catch ( CloneNotSupportedException e )
        {
            throw new AssertionError( e );
        }
    }

    //------------------------------------------------------------------Object--
}

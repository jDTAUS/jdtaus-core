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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Collection of modules.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $Id$
 */
public class Modules extends ModelObject implements Cloneable, Serializable
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.0.x classes. */
    private static final long serialVersionUID = 6139694129590900933L;

    //---------------------------------------------------------------Constants--
    //--Modules-----------------------------------------------------------------

    /**
     * The modules held by the instance.
     * @serial
     */
    private Module[] modules;

    /**
     * The specifications of all modules.
     * @serial
     * @deprecated The list of specifications is no longer cached. It needs
     * to be computed on the fly to reflect changes of the model.
     */
    private Specifications specifications;

    /**
     * The implementations of all modules.
     * @serial
     * @deprecated The list of implementations is no longer cached. It needs
     * to be computed on the fly to reflect changes of the model.
     */
    private Implementations implementations;

    /**
     * Maps module names to modules.
     * @serial
     */
    private Map names = new HashMap( 1000 );

    /**
     * Maps specification identifiers to specifications.
     * @serial
     */
    private Map specificationMap = new HashMap( 1000 );

    /**
     * Maps implementation identifiers to implementations.
     * @serial
     */
    private Map implementationMap = new HashMap( 1000 );

    /**
     * Hash code.
     * @serial
     */
    private int hashCode;

    /**
     * Gets the modules of the collection.
     *
     * @return the modules of the collection.
     */
    public Module[] getModules()
    {
        if ( this.modules == null )
        {
            this.modules = new Module[ 0 ];
            this.hashCode = 0;
        }

        return this.modules;
    }

    /**
     * Setter for property {@code modules}.
     *
     * @param value the new collection of modules.
     *
     * @throws DuplicateModuleException if {@code value} contains duplicate
     * modules.
     * @throws DuplicateSpecificationException if {@code value} contains
     * duplicate specifications.
     * @throws DuplicateImplementationException if {@code value} contains
     * duplicate implementations.
     */
    public void setModules( final Module[] value )
    {
        Specification spec;
        Specifications specs;
        Implementation impl;
        Implementations impls;

        this.implementations = null;
        this.specifications = null;
        this.names.clear();
        this.specificationMap.clear();
        this.implementationMap.clear();
        this.hashCode = 0;
        this.modules = null;

        if ( value != null )
        {
            for ( int i = value.length - 1; i >= 0; i-- )
            {
                this.hashCode += value[i].hashCode();

                // Check module name uniqueness.
                if ( this.names.put( value[i].getName(), value[i] ) != null )
                {
                    this.names.clear();
                    this.specificationMap.clear();
                    this.implementationMap.clear();
                    this.hashCode = 0;

                    throw new DuplicateModuleException( value[i].getName() );
                }

                // Check specification identifier uniqueness.
                specs = value[i].getSpecifications();
                for ( int j = specs.size() - 1; j >= 0; j-- )
                {
                    spec = specs.getSpecification( j );
                    if ( this.specificationMap.put(
                        spec.getIdentifier(), spec ) != null )
                    {
                        this.names.clear();
                        this.specificationMap.clear();
                        this.implementationMap.clear();
                        this.hashCode = 0;

                        throw new DuplicateSpecificationException(
                            spec.getIdentifier() );

                    }
                }

                // Check implementation identifier uniqueness.
                impls = value[i].getImplementations();
                for ( int j = impls.size() - 1; j >= 0; j-- )
                {
                    impl = impls.getImplementation( j );
                    if ( this.implementationMap.put(
                        impl.getIdentifier(), impl ) != null )
                    {
                        this.names.clear();
                        this.specificationMap.clear();
                        this.implementationMap.clear();
                        this.hashCode = 0;

                        throw new DuplicateImplementationException(
                            impl.getIdentifier() );

                    }
                }
            }

            this.modules = value;
        }
    }

    /**
     * Gets a module for a name.
     *
     * @param name the name of the module to return.
     *
     * @return a reference to the module named {@code name}.
     *
     * @throws NullPointerException if {@code name} is {@code null}.
     * @throws MissingModuleException if no module matching {@code name} exists
     * in the collection.
     */
    public Module getModule( final String name )
    {
        if ( name == null )
        {
            throw new NullPointerException( "name" );
        }

        final Module ret = (Module) this.names.get( name );

        if ( ret == null )
        {
            throw new MissingModuleException( name );
        }

        return ret;
    }

    /**
     * Gets a module for an index.
     *
     * @param index the index of the module to return.
     *
     * @return a reference to the module at {@code index}.
     *
     * @throws IndexOutOfBoundsException if {@code index} is negativ,
     * greater than or equal to {@code size()}.
     */
    public final Module getModule( final int index )
    {
        if ( index < 0 || index >= this.size() )
        {
            throw new ArrayIndexOutOfBoundsException( index );
        }

        return this.getModules()[index];
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
     * {@code identifier} exists.
     */
    public Specification getSpecification( final String identifier )
    {
        if ( identifier == null )
        {
            throw new NullPointerException( "identifier" );
        }

        Specification ret =
            (Specification) this.specificationMap.get( identifier );

        if ( ret == null )
        {
            // Check the instances for changes.
            for ( int i = this.size() - 1; i >= 0 && ret == null; i-- )
            {
                final Module mod = this.getModule( i );
                for ( int j = mod.getSpecifications().size() - 1; j >= 0; j-- )
                {
                    final Specification spec =
                        mod.getSpecifications().getSpecification( j );

                    if ( spec.getIdentifier().equals( identifier ) )
                    {
                        ret = spec;
                        break;
                    }
                }
            }

            if ( ret == null )
            {
                throw new MissingSpecificationException( identifier );
            }
        }

        return ret;
    }

    /**
     * Gets a collection of all specifications of all modules.
     *
     * @return a reference to all specifications of all modules held by the
     * instance.
     */
    public Specifications getSpecifications()
    {
        if ( this.specifications == null )
        {
            this.specifications = new Specifications();
        }

        final Collection col = new ArrayList( this.specifications.size() );

        for ( int i = this.size() - 1; i >= 0; i-- )
        {
            final Module mod = this.getModule( i );
            for ( int j = mod.getSpecifications().size() - 1; j >= 0; j-- )
            {
                col.add( mod.getSpecifications().getSpecification( j ) );
            }
        }

        this.specifications.setSpecifications(
            (Specification[]) col.toArray( new Specification[ col.size() ] ) );

        return this.specifications;
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
     * {@code identifier} exists.
     */
    public Implementation getImplementation( final String identifier )
    {
        if ( identifier == null )
        {
            throw new NullPointerException( "identifier" );
        }

        Implementation ret =
            (Implementation) this.implementationMap.get( identifier );

        if ( ret == null )
        {
            // Check the instances for changes.
            for ( int i = this.size() - 1; i >= 0 && ret == null; i-- )
            {
                final Module mod = this.getModule( i );
                for ( int j = mod.getImplementations().size() - 1; j >= 0; j-- )
                {
                    final Implementation impl =
                        mod.getImplementations().getImplementation( j );

                    if ( impl.getIdentifier().equals( identifier ) )
                    {
                        ret = impl;
                        break;
                    }
                }
            }

            if ( ret == null )
            {
                throw new MissingImplementationException( identifier );
            }
        }

        return ret;
    }

    /**
     * Gets a collection of all implementations of all modules held by the
     * instance.
     *
     * @return a reference to all implementations of all modules held by the
     * instance.
     */
    public Implementations getImplementations()
    {
        if ( this.implementations == null )
        {
            this.implementations = new Implementations();
        }

        final Collection col = new ArrayList( this.implementations.size() );

        for ( int i = this.size() - 1; i >= 0; i-- )
        {
            final Module mod = this.getModule( i );
            for ( int j = mod.getImplementations().size() - 1; j >= 0; j-- )
            {
                col.add( mod.getImplementations().getImplementation( j ) );
            }
        }

        this.implementations.setImplementations(
            (Implementation[]) col.toArray(
            new Implementation[ col.size() ] ) );

        return this.implementations;
    }

    /**
     * Gets the number of modules held by the instance.
     *
     * @return the number of modules held by the instance.
     */
    public final int size()
    {
        return this.getModules().length;
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

        final Module[] mods = this.getModules();
        for ( int i = mods.length - 1; i >= 0; i-- )
        {
            buf.append( ", [" ).append( i ).append( "]=" ).
                append( mods[i] );

        }

        buf.append( '}' );
        return buf.toString();
    }

    //-----------------------------------------------------------------Modules--
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

        if ( !equal && o instanceof Modules )
        {
            final Modules that = (Modules) o;
            final Collection these = Arrays.asList( this.getModules() );
            final Collection those = Arrays.asList( that.getModules() );

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
            final Modules ret = (Modules) super.clone();
            final Module[] mods = this.getModules();
            final Module[] cloned = new Module[ mods.length ];

            for ( int i = mods.length - 1; i >= 0; i-- )
            {
                cloned[i] = (Module) mods[i].clone();
            }

            ret.setModules( cloned );
            return ret;
        }
        catch ( CloneNotSupportedException e )
        {
            throw new AssertionError( e );
        }
    }

    //------------------------------------------------------------------Object--
}

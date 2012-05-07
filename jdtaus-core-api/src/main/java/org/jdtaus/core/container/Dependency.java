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
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Dependency meta-data.
 * <p>A dependency consists of a name uniquely identifying the dependency in a
 * set of dependencies and pairs a specification with corresponding
 * implementations from a set of available implementations. Properties set with
 * a dependency overwrite properties of the dependency's specification. The
 * {@code bound} flag indicates if the instance of the dependency is bound to
 * the declaring implementation.</p>
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class Dependency extends ModelObject implements Cloneable, Serializable
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.0.x classes. */
    private static final long serialVersionUID = -8556956703006143247L;

    //---------------------------------------------------------------Constants--
    //--Dependency--------------------------------------------------------------

    /**
     * The name of the dependency.
     * @serial
     */
    private String name;

    /**
     * The specification of the dependency.
     * @serial
     */
    private Specification specification;

    /**
     * The implementation of the dependency.
     * @serial
     */
    private Implementation implementation;

    /**
     * The properties of the dependency.
     * @serial
     */
    private Properties properties;

    /**
     * Flag indicating if the dependency is bound to the requesting
     * implementation.
     * @serial
     */
    private boolean bound;

    /**
     * Gets the name of the dependency.
     *
     * @return the name of the dependency.
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
     * @param value the new name of the dependency.
     */
    public void setName( final String value )
    {
        this.name = value;
    }

    /**
     * Gets the specification of the dependency.
     *
     * @return the specification of the dependency.
     */
    public Specification getSpecification()
    {
        return this.specification;
    }

    /**
     * Setter for property {@code specification}.
     *
     * @param value the new specification of the dependency.
     */
    public void setSpecification( final Specification value )
    {
        this.specification = value;
    }

    /**
     * Gets the implementation of the dependency.
     *
     * @return the implementation of the dependency or {@code null}.
     */
    public Implementation getImplementation()
    {
        return this.implementation;
    }

    /**
     * Setter for property {@code implementation}.
     *
     * @param value the new implementation of the dependency.
     */
    public void setImplementation( final Implementation value )
    {
        this.implementation = value;
    }

    /**
     * Gets the properties of the dependency.
     * <p>The properties of a dependency are a merged set of the properties
     * declared for the dependency's implementation overwritten by any
     * properties declared for the dependency.</p>
     *
     * @return the properties of the dependency.
     *
     * @throws IllegalPropertyTypeException for any property type collisions
     * during merging.
     *
     * @see PropertyOverwriteConstraintException
     */
    public Properties getProperties()
    {
        final Map declaredProperties = new TreeMap();
        final Map implementationProperties = new TreeMap();
        final Map dependencyProperties = new TreeMap();

        for ( int i = this.getDeclaredProperties().size() - 1; i >= 0; i-- )
        {
            final Property declaredProperty =
                this.getDeclaredProperties().getProperty( i );

            declaredProperties.put( declaredProperty.getName(),
                                    declaredProperty );

        }

        if ( this.getImplementation() != null )
        {
            for ( int i = this.getImplementation().getProperties().
                size() - 1; i >= 0; i-- )
            {
                final Property implementationProperty =
                    this.getImplementation().getProperties().
                    getProperty( i );

                implementationProperties.put(
                    implementationProperty.getName(),
                    implementationProperty );

            }
        }

        dependencyProperties.putAll( implementationProperties );

        for ( Iterator it = declaredProperties.entrySet().iterator();
            it.hasNext();)
        {
            final Map.Entry entry = (Map.Entry) it.next();
            final String propertyName = (String) entry.getKey();
            final Property property = (Property) entry.getValue();

            final Property dependencyProperty =
                (Property) dependencyProperties.get( propertyName );

            if ( dependencyProperty != null )
            {
                if ( !dependencyProperty.getType().equals(
                    property.getType() ) )
                {
                    throw new IllegalPropertyTypeException(
                        propertyName, property.getType(),
                        dependencyProperty.getType() );

                }
                if ( property.getDocumentation().getValue() == null &&
                    dependencyProperty.getDocumentation().
                    getValue() != null )
                {
                    property.setDocumentation(
                        dependencyProperty.getDocumentation() );

                }

                dependencyProperties.put( propertyName, property );
            }
        }

        final Properties p = new Properties();
        p.setProperties( (Property[]) dependencyProperties.values().toArray(
                         new Property[ dependencyProperties.size() ] ) );

        return p;
    }

    /**
     * Setter for property {@code properties}.
     *
     * @param value the new properties of the dependency.
     *
     * @see PropertyOverwriteConstraintException
     */
    public void setProperties( final Properties value )
    {
        this.properties = value;
    }

    /**
     * Gets the declared properties of the dependency.
     *
     * @return the declared properties of the dependency.
     */
    public Properties getDeclaredProperties()
    {
        if ( this.properties == null )
        {
            this.properties = new Properties();
        }

        return this.properties;
    }

    /**
     * Gets the flag indicating if the dependency is bound to a requesting
     * implementation.
     *
     * @return {@code true} if the dependency object is bound to the declaring
     * implementation; {@code false} if not.
     */
    public boolean isBound()
    {
        return this.bound;
    }

    /**
     * Setter for property {@code bound}.
     *
     * @param value {@code true} if the dependency object should be bound to the
     * declaring implementation; {@code false} if not.
     */
    public void setBound( boolean value )
    {
        this.bound = value;
    }

    /**
     * Creates a string representing the properties of the instance.
     *
     * @return a string representing the properties of the instance.
     */
    private String internalString()
    {
        return new StringBuffer( 500 ).append( '{' ).
            append( this.internalString( this ) ).
            append( ", name=" ).append( this.name ).
            append( ", bound=" ).append( this.bound ).
            append( ", implementation=" ).
            append( this.implementation == null
                    ? "null"
                    : this.implementation.getIdentifier() + "@" +
                    this.implementation.getVersion() ).
            append( ", specification=" ).
            append( this.specification == null
                    ? "null"
                    : this.specification.getIdentifier() + "@" +
                    this.specification.getVersion() ).
            append( ", properties=" ).append( this.properties ).
            append( '}' ).toString();

    }

    //--------------------------------------------------------------Dependency--
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

        if ( !equal && o instanceof Dependency )
        {
            final Dependency that = (Dependency) o;
            equal = this.getName().equals( that.getName() ) &&
                ( this.implementation == null
                ? that.implementation == null
                : this.implementation.equals( that.implementation ) ) &&
                ( this.specification == null
                ? that.specification == null
                : this.specification.equals( that.specification ) );

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
            ( this.implementation == null
            ? 0
            : this.implementation.hashCode() ) +
            ( this.specification == null
            ? 0
            : this.specification.hashCode() );

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

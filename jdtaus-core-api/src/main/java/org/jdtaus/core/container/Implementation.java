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
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Implementation meta-data.
 * <p>An implementation consists of the properties {@code identifier},
 * {@code name}, {@code description}, {@code vendor} and {@code version}.
 * Property {@code identifier} holds an identifier uniquely identifying the
 * implementation in a collection of implementations. Property {@code name}
 * holds a name of the implementation uniquely identifying the implementation
 * for a specification. Property {@code description} holds a textual
 * description. Property {@code vendor} holds vendor information for the vendor
 * providing the implementation. Property {@code version} holds a textual
 * version of the implementation. Properties, dependencies and implemented
 * specifications may be inherited from a {@code parent} up the hierarchy.
 * Property {@code final} flags an implementation as the final node in an
 * inheritance hierarchy.</p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class Implementation extends ModelObject
    implements Cloneable, Serializable
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.0.x classes. */
    private static final long serialVersionUID = -7590949013151607828L;

    //---------------------------------------------------------------Constants--
    //--Implementation----------------------------------------------------------

    /**
     * The parent to inherit from.
     * @serial
     */
    private Implementation parent;

    /**
     * The name of the module holding the implementation.
     * @serial
     */
    private String moduleName;

    /**
     * The specifications the implementation implements.
     * @serial
     */
    private Specifications implementedSpecifications;

    /**
     * The dependencies of the implementation.
     * @serial
     */
    private Dependencies dependencies;

    /**
     * The properties of the implementation.
     * @serial
     */
    private Properties properties;

    /**
     * The messages of the implementation.
     * @serial
     */
    private Messages messages;

    /**
     * The identifier of the implementation.
     * @serial
     */
    private String identifier;

    /**
     * The name of the implementation.
     * @serial
     */
    private String name;

    /**
     * The vendor of the implementation.
     * @serial
     */
    private String vendor;

    /**
     * The version of the implementation.
     * @serial
     */
    private String version;

    /**
     * Flag indicating if the implementation is a final node in an inheritance
     * hierarchy.
     * @serial
     */
    private boolean finalFlag;

    /** Creates a new {@code Implementation} instance. */
    public Implementation()
    {
        super();
    }

    /**
     * Gets the name of the module holding the implementation.
     *
     * @return the name of the module holding the implementation.
     */
    public String getModuleName()
    {
        if ( this.moduleName == null )
        {
            this.moduleName = "";
        }

        return this.moduleName;
    }

    /**
     * Setter for property {@code moduleName}.
     *
     * @param value the new name of the module holding the implementation.
     */
    public void setModuleName( final String value )
    {
        this.moduleName = value;
    }

    /**
     * Gets the specifications the implementation implements.
     * <p>The specifications an implementation implements are a merged set of
     * all specifications implemented by any parent overwritten by any declared
     * implemented specifications.</p>
     *
     * @return the specifications the implementation implements.
     */
    public Specifications getImplementedSpecifications()
    {
        Specifications specs = this.getDeclaredImplementedSpecifications();

        if ( this.getParent() != null )
        {
            final TreeMap map = new TreeMap();
            for ( int i = specs.size() - 1; i >= 0; i-- )
            {
                final Specification declaredSpecification =
                    specs.getSpecification( i );

                map.put( declaredSpecification.getIdentifier(),
                         declaredSpecification );

            }

            final Specifications superSpecifications =
                this.getParent().getImplementedSpecifications();

            for ( int i = superSpecifications.size() - 1; i >= 0; i-- )
            {
                final Specification superSpecification =
                    superSpecifications.getSpecification( i );

                if ( !map.containsKey( superSpecification.getIdentifier() ) )
                {
                    map.put( superSpecification.getIdentifier(),
                             superSpecification );

                }
            }

            specs = new Specifications();
            specs.setSpecifications( (Specification[]) map.values().toArray(
                                     new Specification[ map.size() ] ) );

        }

        return specs;
    }

    /**
     * Setter for property {@code implementedSpecifications}.
     *
     * @param value the new specifications the implementation implements.
     */
    public void setImplementedSpecifications( final Specifications value )
    {
        this.implementedSpecifications = value;
    }

    /**
     * Gets the specifications the implementation declares to implement.
     *
     * @return the specifications the implementation declares to implement.
     */
    public Specifications getDeclaredImplementedSpecifications()
    {
        if ( this.implementedSpecifications == null )
        {
            this.implementedSpecifications = new Specifications();
        }

        return this.implementedSpecifications;
    }

    /**
     * Gets the dependencies of the implementation.
     * <p>The dependencies of an implementation are a merged set of all parent
     * dependencies overwritten by any declared dependencies.</p>
     *
     * @return the dependencies the implementation depends on.
     *
     * @throws IllegalDependencyTypeException for any dependency type collisions
     * during merging.
     */
    public Dependencies getDependencies()
    {
        Dependencies deps = this.getDeclaredDependencies();

        if ( this.getParent() != null )
        {
            final Map map = new TreeMap();
            for ( int i = deps.size() - 1; i >= 0; i-- )
            {
                final Dependency declaredDependency = deps.getDependency( i );
                map.put( declaredDependency.getName(), declaredDependency );
            }

            final Dependencies superDependencies =
                this.getParent().getDependencies();

            for ( int i = superDependencies.size() - 1; i >= 0; i-- )
            {
                final Dependency superDependency =
                    superDependencies.getDependency( i );

                final Dependency declaredDependency =
                    (Dependency) map.get( superDependency.getName() );

                if ( declaredDependency != null )
                {
                    if ( !declaredDependency.getSpecification().getIdentifier().
                        equals( superDependency.getSpecification().
                                getIdentifier() ) )
                    {
                        throw new IllegalDependencyTypeException(
                            this.getIdentifier(),
                            declaredDependency.getName(),
                            declaredDependency.getSpecification().
                            getIdentifier(),
                            superDependency.getSpecification().
                            getIdentifier() );

                    }
                }
                else
                {
                    map.put( superDependency.getName(), superDependency );
                }
            }

            deps = new Dependencies();
            deps.setDependencies( (Dependency[]) map.values().toArray(
                                  new Dependency[ map.size() ] ) );

        }

        return deps;
    }

    /**
     * Setter for property {@code dependencies}.
     *
     * @param value the new dependencies of the implementation.
     */
    public void setDependencies( final Dependencies value )
    {
        this.dependencies = value;
    }

    /**
     * Gets the declared dependencies of the implementation.
     *
     * @return the declared dependencies of the implementation.
     */
    public Dependencies getDeclaredDependencies()
    {
        if ( this.dependencies == null )
        {
            this.dependencies = new Dependencies();
        }

        return this.dependencies;
    }

    /**
     * Gets the properties of the implementation.
     * <p>The properties of an implementation are a merged set of all parent
     * properties overwritten by any declared properties.</p>
     *
     * @return the properties of the implementation.
     *
     * @throws IllegalPropertyTypeException for any property type collisions
     * during merging.
     * @throws PropertyOverwriteConstraintException if the implementation does
     * not provide values for all properties declared for its implemented
     * specifications.
     */
    public Properties getProperties()
    {
        final Map declaredProperties = new TreeMap();
        final Map implementedProperties = new TreeMap();

        for ( int i = this.getDeclaredProperties().size() - 1; i >= 0; i-- )
        {
            final Property declaredProperty =
                this.getDeclaredProperties().getProperty( i );

            declaredProperties.put( declaredProperty.getName(),
                                    declaredProperty );

        }

        if ( this.getParent() != null )
        {

            final Properties superProperties =
                this.getParent().getProperties();

            for ( int i = superProperties.size() - 1; i >= 0; i-- )
            {
                final Property superProperty =
                    superProperties.getProperty( i );

                final Property declaredProperty =
                    (Property) declaredProperties.get(
                    superProperty.getName() );

                if ( declaredProperty != null )
                {
                    if ( !declaredProperty.getType().equals(
                        superProperty.getType() ) )
                    {
                        throw new IllegalPropertyTypeException(
                            declaredProperty.getName(),
                            declaredProperty.getType(),
                            superProperty.getType() );

                    }

                    if ( declaredProperty.getDocumentation().
                        getValue() == null &&
                        superProperty.getDocumentation().
                        getValue() != null )
                    {
                        declaredProperty.setDocumentation(
                            superProperty.getDocumentation() );

                    }
                }
                else
                {
                    declaredProperties.put( superProperty.getName(),
                                            superProperty );

                }
            }
        }

        final Specifications specs = this.getImplementedSpecifications();

        for ( int i = specs.size() - 1; i >= 0; i-- )
        {
            final Specification spec = specs.getSpecification( i );
            for ( int p = spec.getProperties().size() - 1; p >= 0; p-- )
            {
                final Property implementedProperty =
                    spec.getProperties().getProperty( p );

                final Property alreadyImplemented =
                    (Property) implementedProperties.put(
                    implementedProperty.getName(),
                    implementedProperty );

                if ( alreadyImplemented != null &&
                    !alreadyImplemented.getType().equals(
                    implementedProperty.getType() ) )
                {
                    throw new IllegalPropertyTypeException(
                        implementedProperty.getName(),
                        implementedProperty.getType(),
                        alreadyImplemented.getType() );

                }

                final Property declaredProperty =
                    (Property) declaredProperties.get(
                    implementedProperty.getName() );

                if ( declaredProperty != null )
                {
                    if ( !declaredProperty.getType().equals(
                        implementedProperty.getType() ) )
                    {
                        throw new IllegalPropertyTypeException(
                            declaredProperty.getName(),
                            declaredProperty.getType(),
                            implementedProperty.getType() );

                    }

                    if ( declaredProperty.getDocumentation().
                        getValue() == null &&
                        implementedProperty.getDocumentation().
                        getValue() != null )
                    {
                        declaredProperty.setDocumentation(
                            implementedProperty.getDocumentation() );

                    }

                    declaredProperty.setApi( true );
                }
                else
                {
                    throw new PropertyOverwriteConstraintException(
                        this.getIdentifier(), spec.getIdentifier(),
                        implementedProperty.getName() );

                }
            }
        }

        final Properties p = new Properties();
        p.setProperties( (Property[]) declaredProperties.values().toArray(
                         new Property[ declaredProperties.size() ] ) );

        return p;
    }

    /**
     * Setter for property {@code properties}.
     *
     * @param value new properties of the implementation.
     */
    public void setProperties( final Properties value )
    {
        this.properties = value;
    }

    /**
     * Gets the declared properties of the implementation.
     *
     * @return the declared properties of the implementation.
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
     * Gets the messages of the implementation.
     * <p>The messages of an implementation are a merged set of all parent
     * messages overwritten by any declared messages.</p>
     *
     * @return the messages of the implementation.
     */
    public Messages getMessages()
    {
        Messages msgs = this.getDeclaredMessages();

        if ( this.getParent() != null )
        {
            final Set set = new TreeSet( new Comparator()
            {

                public int compare( final Object o1, final Object o2 )
                {
                    return ( (Message) o1 ).getName().
                        compareTo( ( (Message) o2 ).getName() );

                }

            } );

            for ( int i = msgs.size() - 1; i >= 0; i-- )
            {
                set.add( msgs.getMessage( i ) );
            }

            final Messages parentMessages = this.getParent().getMessages();
            for ( int i = parentMessages.size() - 1; i >= 0; i-- )
            {
                final Message parentMessage = parentMessages.getMessage( i );
                if ( !set.contains( parentMessage ) )
                {
                    set.add( parentMessage );
                }
            }

            msgs = new Messages();
            msgs.setMessages(
                (Message[]) set.toArray( new Message[ set.size() ] ) );

        }

        return msgs;
    }

    /**
     * Setter for property {@code messages}.
     *
     * @param value new messages of the implementation.
     */
    public void setMessages( final Messages value )
    {
        this.messages = value;
    }

    /**
     * Gets the declared messages of the implementation.
     *
     * @return the declared messages of the implementation.
     */
    public Messages getDeclaredMessages()
    {
        if ( this.messages == null )
        {
            this.messages = new Messages();
        }

        return this.messages;
    }

    /**
     * Gets the identifier of the implementation.
     *
     * @return the unique identifier of the implementation.
     */
    public String getIdentifier()
    {
        if ( this.identifier == null )
        {
            this.identifier = "";
        }

        return this.identifier;
    }

    /**
     * Setter for property {@code identifier}.
     *
     * @param value the new identifier of the implementation.
     */
    public void setIdentifier( final String value )
    {
        this.identifier = value;
    }

    /**
     * Gets the name of the implementation.
     *
     * @return the name of the implementation.
     */
    public String getName()
    {
        if ( this.name == null )
        {
            this.name = "";
        }

        return name;
    }

    /**
     * Setter for property {@code name}.
     *
     * @param value the new name of the implementation.
     */
    public void setName( final String value )
    {
        this.name = value;
    }

    /**
     * Gets the parent implementation the implementation inherits from.
     *
     * @return the parent implementation the implementation inherits from or
     * {@code null} if the implementation has no parent.
     *
     * @see InheritanceConstraintException
     */
    public Implementation getParent()
    {
        return this.parent;
    }

    /**
     * Setter for property {@code parent}.
     *
     * @param value the new parent implementation of the implementation.
     *
     * @throws InheritanceConstraintException if {@code value} is flagged as
     * final.
     */
    public void setParent( final Implementation value )
    {
        if ( value != null && value.isFinal() )
        {
            throw new InheritanceConstraintException( this.getIdentifier() );
        }

        this.parent = value;
    }

    /**
     * Gets the vendor of the implementation.
     *
     * @return the vendor of the implementation.
     */
    public String getVendor()
    {
        if ( this.vendor == null )
        {
            this.vendor = "";
        }

        return this.vendor;
    }

    /**
     * Setter for property {@code name}.
     *
     * @param value the new vendor of the implementation.
     */
    public void setVendor( final String value )
    {
        this.vendor = value;
    }

    /**
     * Gets the version of the implementation.
     *
     * @return the version of the implementation or {@code null}.
     */
    public String getVersion()
    {
        return this.version;
    }

    /**
     * Setter for property {@code version}.
     *
     * @param value the new version of the implementation.
     */
    public void setVersion( final String value )
    {
        this.version = value;
    }

    /**
     * Gets a flag indicating if the implementation is a final node
     * in an inheritance hierarchy.
     *
     * @return {@code true} if the implementation is a final node in an
     * inheritance hierarchy; {@code false} if the implementation is allowed
     * to be the parent of another implementation.
     *
     * @see InheritanceConstraintException
     */
    public boolean isFinal()
    {
        return this.finalFlag;
    }

    /**
     * Setter for property {@code final}.
     *
     * @param value {@code true} if the implementation is a final node in an
     * inheritance hierarchy; {@code false} if the implementation is allowed
     * to be the parent of another implementation.
     *
     * @see InheritanceConstraintException
     */
    public void setFinal( final boolean value )
    {
        this.finalFlag = value;
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
            append( ", identifier=" ).append( this.identifier ).
            append( ", moduleName=" ).append( this.moduleName ).
            append( ", name=" ).append( this.name ).
            append( ", parent=" ).append( this.parent ).
            append( ", properties=" ).append( this.properties ).
            append( ", vendor=" ).append( this.vendor ).
            append( ", version=" ).append( this.version ).
            append( ", final=" ).append( this.finalFlag ).
            append( ", dependencies=" ).append( this.dependencies ).
            append( ", messages=" ).append( this.messages ).
            append( ", specifications={" );

        for ( int i = this.getImplementedSpecifications().size() - 1; i > 0;
            i-- )
        {
            final Specification s =
                this.getImplementedSpecifications().getSpecification( i );

            buf.append( "[" ).append( i ).append( "]=" ).
                append( s.getIdentifier() ).append( "@" ).
                append( s.getVersion() );

        }

        buf.append( "}}" );
        return buf.toString();
    }

    //----------------------------------------------------------Implementation--
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
        catch ( final CloneNotSupportedException e )
        {
            throw new AssertionError( e );
        }
    }

    /**
     * Indicates whether some other object is equal to this one by comparing
     * properties {@code identifier} and {@code version}.
     *
     * @param o the reference object with which to compare.
     *
     * @return {@code true} if this object is the same as {@code o};
     * {@code false} otherwise.
     */
    public final boolean equals( final Object o )
    {
        boolean equal = this == o;

        if ( !equal && o instanceof Implementation )
        {
            final Implementation that = (Implementation) o;
            equal = this.getIdentifier().equals( that.getIdentifier() ) &&
                ( this.getVersion() == null ? that.getVersion() == null
                : this.getVersion().equals( that.getVersion() ) );

        }

        return equal;
    }

    /**
     * Returns a hash code value for this object.
     *
     * @return a hash code value for this object.
     */
    public final int hashCode()
    {
        return this.getIdentifier().hashCode() +
            ( this.getVersion() == null ? 0 : this.getVersion().hashCode() );

    }

    //------------------------------------------------------------------Object--
}

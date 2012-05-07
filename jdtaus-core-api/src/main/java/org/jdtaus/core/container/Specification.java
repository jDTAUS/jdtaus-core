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

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

/**
 * Specification meta-data.
 * <p>A specification consists of the properties {@code identifier},
 * {@code vendor}, {@code description} and {@code version}. Property
 * {@code identifier} holds an identifier uniquely identifying the specification
 * in a set of specifications. Property {@code vendor} holds vendor information
 * for the vendor providing the specification. Property {@code description}
 * holds a textual description and property {@code version} holds a textual
 * version of the specification. The {@code stateless} flag indicates that state
 * does not need to be retained across operations for instances to operate as
 * specified. Property {@code multiplicity} specifies the number of
 * implementations allowed to exist among a set of modules. A specification with
 * {@code MULTIPLICITY_ONE} specifies that exactly one implementation of the
 * specification must exist among a set of modules. A specification with
 * {@code MULTIPLICITY_MANY} specifies that multiple implementations of the
 * specification are allowed to exist among a set of modules (including none).
 * Property {@code scope} specifies the scope the specification applies to.
 * In multiton scope, a new instance is created whenever requested. In context
 * scope, instances are bound to a system's context. An instance is only created
 * if not already available in context. In singleton scope, instances are bound
 * to a system's single instance store. An instance is only created if not
 * already available in that single instance store.</p>
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class Specification extends ModelObject
    implements Cloneable, Serializable
{
    //--Constants---------------------------------------------------------------

    /**
     * Constant for property {@code multiplicity}.
     * <p>A specification with {@code MULTIPLICITY_ONE} specifies that exactly
     * one implementation of the specification must exist among a set of
     * modules.</p>
     */
    public static final int MULTIPLICITY_ONE = 25000;

    /**
     * Constant for property {@code multiplicity}.
     * <p>A specification with {@code MULTIPLICITY_MANY} specifies that multiple
     * implementations of the specification are allowed to exist among a set of
     * modules (including none).</p>
     */
    public static final int MULTIPLICITY_MANY = 25001;

    /**
     * Constant for property {@code scope}.
     * <p>In multiton scope, a new instance is created whenever requested.</p>
     */
    public static final int SCOPE_MULTITON = 26000;

    /**
     * Constant for property {@code scope}.
     * <p>In context scope, instances are bound to a system's context. An
     * instance is only created if not already available in context.</p>
     */
    public static final int SCOPE_CONTEXT = 26001;

    /**
     * Constant for property {@code scope}.
     * <p>In singleton scope, instances are bound to a system's single instance
     * store. An instance is only created if not already available in that
     * single instance store.</p>
     */
    public static final int SCOPE_SINGLETON = 26002;

    /** Serial version UID for backwards compatibility with 1.0.x classes. */
    private static final long serialVersionUID = -1829249262406961967L;

    //---------------------------------------------------------------Constants--
    //--Specification-----------------------------------------------------------

    /**
     * The name of the module holding the specification.
     * @serial
     */
    private String moduleName;

    /**
     * The description of the specification.
     * @serial
     * @deprecated Replaced by property {@code documentation}.
     */
    private String description;

    /**
     * The identifier of the specification.
     * @serial
     */
    private String identifier;

    /**
     * The flag indicating that instances of implementations of the
     * specification should be created using a singleton strategy.
     * @serial
     * @deprecated Replaced by {@link #scope}.
     */
    private boolean singleton;

    /**
     * The vendor of the specification.
     * @serial
     */
    private String vendor;

    /**
     * The version of the specification.
     * @serial
     */
    private String version;

    /**
     * The implementation multiplicity of the specification.
     * @serial
     */
    private int multiplicity = MULTIPLICITY_MANY;

    /**
     * The scope the specification applies to.
     * @serial
     */
    private int scope = SCOPE_MULTITON;

    /**
     * The flag indicating if state need not be retained across method
     * invocations for implementations to operate as specified.
     * @serial
     */
    private boolean stateless;

    /**
     * The implementations available for the specification.
     * @serial
     */
    private Implementations implementations;

    /**
     * Maps implementation names to implementations.
     * @serial
     */
    private final Map implementationNames = new TreeMap();

    /**
     * The properties of the specification.
     * @serial
     */
    private Properties properties;

    /**
     * Gets the name of the module holding the specification.
     *
     * @return the name of the module holding the specification.
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
     * @param value the new name of the module holding the specification.
     */
    public void setModuleName( final String value )
    {
        this.moduleName = value;
    }

    /**
     * Gets the description of the specification.
     *
     * @return the description of the specification or {@code null}.
     *
     * @deprecated Replaced by {@link #getDocumentation() getDocumentation().getValue()}.
     */
    public String getDescription()
    {
        return this.getDocumentation().getValue();
    }

    /**
     * Setter for property {@code description}.
     *
     * @param value the new description of the specification.
     * @deprecated Replaced by {@link #getDocumentation() getDocumentation().setValue(value)}.
     */
    public void setDescription( final String value )
    {
        this.getDocumentation().setValue( value );
    }

    /**
     * Gets the identifier of the specification.
     *
     * @return the unique identifier of the specification.
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
     * @param value the new identifier of the specification.
     */
    public void setIdentifier( final String value )
    {
        this.identifier = value;
    }

    /**
     * Gets the flag indicating the instantiation strategy of the specification.
     *
     * @return {@code true} if the specification is specifying a singleton;
     * {@code false} if not.
     *
     * @see PropertyOverwriteConstraintException
     * @deprecated Replaced by {@link #getScope() getScope() == SCOPE_SINGLETON}.
     */
    public boolean isSingleton()
    {
        return this.getScope() == SCOPE_SINGLETON;
    }

    /**
     * Setter for property {@code singleton}.
     *
     * @param value {@code true} to flag the specification as a singleton;
     * {@code false} to not flag the specification as a singleton.
     *
     * @see PropertyOverwriteConstraintException
     * @deprecated Replaced by {@link #setScope(int) setScope(value ? SCOPE_SINGLETON : SCOPE_MULTITON)}.
     */
    public void setSingleton( boolean value )
    {
        this.scope = value ? SCOPE_SINGLETON : SCOPE_MULTITON;
    }

    /**
     * Gets the scope the specification applies to.
     *
     * @return scope the specification applies to.
     *
     * @see #SCOPE_MULTITON
     * @see #SCOPE_CONTEXT
     * @see #SCOPE_SINGLETON
     * @see PropertyOverwriteConstraintException
     */
    public int getScope()
    {
        return this.scope;
    }

    /**
     * Setter for property {@code scope}.
     *
     * @param value new scope the specification applies to.
     *
     * @throws IllegalArgumentException if {@code value} is not equal to one of
     * the constants {@code SCOPE_MULTITON}, {@code SCOPE_CONTEXT} or
     * {@code SCOPE_SINGLETON}.
     *
     * @see #SCOPE_MULTITON
     * @see #SCOPE_CONTEXT
     * @see #SCOPE_SINGLETON
     * @see PropertyOverwriteConstraintException
     */
    public void setScope( final int value )
    {
        if ( value != SCOPE_MULTITON && value != SCOPE_CONTEXT &&
             value != SCOPE_SINGLETON )
        {
            throw new IllegalArgumentException( Integer.toString( value ) );
        }

        this.scope = value;
    }

    /**
     * Gets the flag indicating if state need not be retained across method
     * invocations for implementations to operate as specified.
     *
     * @return {@code true} if state need not be retained across method
     * invocations for implementations to operate as specified; {@code false} if
     * state must be retained across method invocations for implementations
     * to operate as specified.
     */
    public boolean isStateless()
    {
        return this.stateless;
    }

    /**
     * Setter for property {@code stateless}.
     *
     * @param value {@code true} if state need not be retained across method
     * invocations for implementations to operate as specified; {@code false} if
     * state must be retained across method invocations for implementations to
     * operate as specified.
     */
    public void setStateless( boolean value )
    {
        this.stateless = value;
    }

    /**
     * Gets the implementation multiplicity of the specification.
     *
     * @return one of the constants {@code MULTIPLICITY_ONE} or
     * {@code MULTIPLICITY_MANY}.
     *
     * @see #MULTIPLICITY_ONE
     * @see #MULTIPLICITY_MANY
     * @see MultiplicityConstraintException
     */
    public int getMultiplicity()
    {
        return this.multiplicity;
    }

    /**
     * Setter for property {@code multiplicity}.
     *
     * @param value the new implementation multiplicity of the specification.
     *
     * @throws IllegalArgumentException if {@code value} is not equal to one of
     * the constants {@code MULTIPLICITY_ONE} or {@code MULTIPLICITY_MANY}.
     * @throws MultiplicityConstraintException if {@code value} equals
     * {@code MULTIPLICITY_ONE} and the specification currently has more than
     * one implementation defined.
     *
     * @see #MULTIPLICITY_ONE
     * @see #MULTIPLICITY_MANY
     */
    public void setMultiplicity( final int value )
    {
        if ( value != MULTIPLICITY_ONE && value != MULTIPLICITY_MANY )
        {
            throw new IllegalArgumentException( Integer.toString( value ) );
        }
        if ( value == MULTIPLICITY_ONE && this.getImplementations().size() > 1 )
        {
            throw new MultiplicityConstraintException( this.getIdentifier() );
        }

        this.multiplicity = value;
    }

    /**
     * Gets the vendor of the specification.
     *
     * @return the vendor of the specification.
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
     * Setter for property {@code vendor}.
     *
     * @param value the new vendor of the specification.
     */
    public void setVendor( final String value )
    {
        this.vendor = value;
    }

    /**
     * Gets the version of the specification.
     *
     * @return the version of the specification or {@code null}.
     */
    public String getVersion()
    {
        return this.version;
    }

    /**
     * Setter for property {@code version}.
     *
     * @param value the new version of the specification.
     */
    public void setVersion( final String value )
    {
        this.version = value;
    }

    /**
     * Gets an implementation for a name.
     *
     * @param name the name of the implementation to return.
     *
     * @return a reference to the implementation named {@code name}.
     *
     * @throws NullPointerException if {@code name} is {@code null}.
     * @throws MissingImplementationException if no implementation matching
     * {@code name} exists.
     */
    public Implementation getImplementation( final String name )
    {
        if ( name == null )
        {
            throw new NullPointerException( "name" );
        }

        final Implementation ret =
            (Implementation) this.implementationNames.get( name );

        if ( ret == null )
        {
            throw new MissingImplementationException( name );
        }

        return ret;
    }

    /**
     * Gets all available implementations of the specification.
     *
     * @return all available implementations of the specification.
     */
    public Implementations getImplementations()
    {
        if ( this.implementations == null )
        {
            this.implementations = new Implementations();
        }

        return this.implementations;
    }

    /**
     * Setter for property {@code implementations}.
     *
     * @param value the new implementations of the specification.
     *
     * @throws DuplicateImplementationException if {@code value} contains
     * duplicate implementations.
     * @throws MultiplicityConstraintException if the specification's
     * multiplicity equals {@code MULTIPLICITY_ONE} and {@code value} contains
     * no or more than one implementation.
     */
    public void setImplementations( final Implementations value )
    {
        if ( this.getMultiplicity() == MULTIPLICITY_ONE && value != null &&
             value.size() != 1 )
        {
            throw new MultiplicityConstraintException( this.getIdentifier() );
        }

        this.implementationNames.clear();
        this.implementations = null;

        if ( value != null )
        {
            for ( int i = value.size() - 1; i >= 0; i-- )
            {
                if ( this.implementationNames.put(
                    value.getImplementation( i ).getName(),
                    value.getImplementation( i ) ) != null )
                {
                    this.implementationNames.clear();

                    throw new DuplicateImplementationException(
                        value.getImplementation( i ).getName() );

                }
            }

            this.implementations = value;
        }
    }

    /**
     * Gets the properties of the specification.
     *
     * @return the properties of the specification.
     */
    public Properties getProperties()
    {
        if ( this.properties == null )
        {
            this.properties = new Properties();
        }

        return this.properties;
    }

    /**
     * Setter for property {@code properties}.
     *
     * @param value new properties of the specification.
     */
    public void setProperties( final Properties value )
    {
        this.properties = value;
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
            append( ", stateless=" ).append( this.stateless ).
            append( ", multiplicity=" ).
            append( this.multiplicity == MULTIPLICITY_ONE ? "one" : "many" ).
            append( ", scope=" ).append( this.scope == SCOPE_MULTITON
                                         ? "multiton"
                                         : this.scope == SCOPE_CONTEXT
                                           ? "context"
                                           : "singleton" ).
            append( ", vendor=" ).append( this.vendor ).
            append( ", version=" ).append( this.version ).
            append( ", properties=" ).append( this.getProperties() ).
            append( ", implementations={" );

        for ( int i = this.getImplementations().size() - 1; i >= 0; i-- )
        {
            final Implementation impl =
                                 this.getImplementations().getImplementation( i );

            buf.append( "[" ).append( i ).append( "]=" ).
                append( impl.getIdentifier() ).append( "@" ).
                append( impl.getVersion() );

            if ( i - 1 >= 0 )
            {
                buf.append( ", " );
            }
        }

        buf.append( "}}" );
        return buf.toString();
    }

    //-----------------------------------------------------------Specification--
    //--Serializable------------------------------------------------------------

    /**
     * Takes care of initializing fields when constructed from an 1.0.x object
     * stream.
     *
     * @throws ObjectStreamException if no scope can be resolved.
     */
    private Object readResolve() throws ObjectStreamException
    {
        if ( this.scope == SCOPE_MULTITON && this.singleton )
        {
            this.scope = SCOPE_SINGLETON;
        }
        if ( this.getDocumentation().getValue() == null &&
             this.description != null )
        {
            this.getDocumentation().setValue( this.description );
        }

        return this;
    }

    //------------------------------------------------------------Serializable--
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
        catch ( CloneNotSupportedException e )
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
        boolean equal = o == this;
        if ( !equal && o instanceof Specification )
        {
            final Specification that = (Specification) o;
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

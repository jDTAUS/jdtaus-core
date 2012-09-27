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

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * Module meta-data.
 * <p>A module consists of the properties {@code name}, {@code description}
 * and {@code version}. Property {@code name} holds the name of the module
 * uniquely identifying the module in a collection of modules. Property
 * {@code description} holds a textual description, property {@code version}
 * a textual version of the module. A module defines specifications,
 * implementations and properties.</p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class Module extends ModelObject implements Cloneable, Serializable
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.0.x classes. */
    private static final long serialVersionUID = 2518888867819463746L;

    //---------------------------------------------------------------Constants--
    //--Module------------------------------------------------------------------

    /**
     * The specifications of the module.
     * @serial
     */
    private Specifications specifications;

    /**
     * The implementations of the module.
     * @serial
     */
    private Implementations implementations;

    /**
     * The properties of the module.
     * @serial
     */
    private Properties properties;

    /**
     * The messages of the module.
     * @serial
     */
    private Messages messages;

    /**
     * The description of the module.
     * @serial
     * @deprecated Replaced by property {@code documentation}.
     */
    private String description;

    /**
     * The name of the module.
     * @serial
     */
    private String name;

    /**
     * The version of the module.
     * @serial
     */
    private String version;

    /**
     * Gets the specifications of the module.
     *
     * @return the specifications of the module.
     */
    public Specifications getSpecifications()
    {
        if ( this.specifications == null )
        {
            this.specifications = new Specifications();
        }

        return this.specifications;
    }

    /**
     * Setter for property {@code specifications}.
     *
     * @param value the new specifications of the module.
     */
    public void setSpecifications( final Specifications value )
    {
        this.specifications = value;
    }

    /**
     * Gets the implementations of the module.
     *
     * @return implementations of the module.
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
     * @param value the new implementations of the module.
     */
    public void setImplementations( final Implementations value )
    {
        this.implementations = value;
    }

    /**
     * Gets the properties of the module.
     *
     * @return the properties of the module.
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
     * @param value the new properties of the module.
     */
    public void setProperties( final Properties value )
    {
        this.properties = value;
    }

    /**
     * Gets the messages of the module.
     *
     * @return the messages of the module.
     */
    public Messages getMessages()
    {
        if ( this.messages == null )
        {
            this.messages = new Messages();
        }

        return this.messages;
    }

    /**
     * Setter for property {@code messages}.
     *
     * @param value new messages of the module.
     */
    public void setMessages( final Messages value )
    {
        this.messages = value;
    }

    /**
     * Gets the description of the module.
     *
     * @return the description of the module or {@code null}.
     * @deprecated Replaced by {@link #getDocumentation() getDocumentation().getValue()}.
     */
    public String getDescription()
    {
        return this.getDocumentation().getValue();
    }

    /**
     * Setter for property {@code description}.
     *
     * @param value the new description of the module.
     * @deprecated Replaced by {@link #setDocumentation(org.jdtaus.core.container.Text) getDocumentation().setValue( value )}.
     */
    public void setDescription( final String value )
    {
        this.getDocumentation().setValue( value );
    }

    /**
     * Gets the name of the module.
     *
     * @return the unique name of the module.
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
     * @param value the new name of the module.
     */
    public void setName( final String value )
    {
        this.name = value;
    }

    /**
     * Gets the version of the module.
     *
     * @return the version of the module or {@code null}.
     */
    public String getVersion()
    {
        return this.version;
    }

    /**
     * Setter for property {@code version}.
     *
     * @param value the new version of the module.
     */
    public void setVersion( final String value )
    {
        this.version = value;
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
            append( ", name=" ).append( this.name ).
            append( ", version=" ).append( this.version ).
            append( ", properties=" ).append( this.properties ).
            append( ", messages=" ).append( this.messages ).
            append( ", specifications=" ).append( this.getSpecifications() ).
            append( ", implementations=" ).append( this.getImplementations() );

        buf.append( '}' ).toString();
        return buf.toString();
    }

    //------------------------------------------------------------------Module--
    //--Serializable------------------------------------------------------------

    /**
     * Takes care of initializing fields when constructed from an 1.0.x object
     * stream.
     *
     * @throws ObjectStreamException if no scope can be resolved.
     */
    private Object readResolve() throws ObjectStreamException
    {
        if ( this.getDocumentation().getValue() == null &&
            this.description != null )
        {
            this.getDocumentation().setValue( this.description );
        }
        if ( "".equals( this.version ) )
        {
            this.version = null;
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
     * properties {@code name} and {@code version}.
     *
     * @param o the reference object with which to compare.
     *
     * @return {@code true} if this object is the same as {@code o};
     * {@code false} otherwise.
     */
    public final boolean equals( final Object o )
    {
        boolean equal = this == o;

        if ( !equal && o instanceof Module )
        {
            final Module that = (Module) o;
            equal = this.getName().equals( that.getName() ) &&
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
        return this.getName().hashCode() +
            ( this.getVersion() == null ? 0 : this.getVersion().hashCode() );

    }

    //------------------------------------------------------------------Object--
}

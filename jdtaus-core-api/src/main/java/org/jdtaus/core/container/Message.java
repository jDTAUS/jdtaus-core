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
 * Message meta-data.
 * <p>A message consists of the properties {@code name}, {@code template} and
 * {@code arguments}. Property {@code name} holds a name uniquely identifying
 * the message in a set of messages. Property {@code template} holds the
 * template of the message. Property {@code arguments} holds meta-data
 * describing arguments to format the message with.</p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class Message extends ModelObject implements Cloneable, Serializable
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.5.x classes. */
    private static final long serialVersionUID = 8433823757935477327L;

    //---------------------------------------------------------------Constants--
    //--Message.----------------------------------------------------------------

    /**
     * The name of the message.
     * @serial
     */
    private String name;

    /**
     * The name of the module holding the message.
     * @serial
     */
    private String moduleName;

    /**
     * The template of the message.
     * @serial
     */
    private Text template;

    /**
     * The arguments of the message.
     * @serial
     */
    private Arguments arguments;

    /**
     * Gets the name of the message.
     *
     * @return the name of the message.
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
     * @param value the new name of the message.
     */
    public void setName( final String value )
    {
        this.name = value;
    }

    /**
     * Gets the name of the module holding the message.
     *
     * @return the name of the module holding the message.
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
     * @param value the new name of the module holding the message.
     */
    public void setModuleName( final String value )
    {
        this.moduleName = value;
    }

    /**
     * Gets the template of the message.
     *
     * @return the template of the message.
     */
    public Text getTemplate()
    {
        if ( this.template == null )
        {
            this.template = new Text();
        }

        return this.template;
    }

    /**
     * Setter for property {@code template}.
     *
     * @param value the new template of the message.
     */
    public void setTemplate( final Text value )
    {
        this.template = value;
    }

    /**
     * Gets the arguments of the message.
     *
     * @return the arguments of the message.
     */
    public Arguments getArguments()
    {
        if ( this.arguments == null )
        {
            this.arguments = new Arguments();
        }

        return this.arguments;
    }

    /**
     * Setter for property {@code arguments}.
     *
     * @param value the new arguments of the message.
     */
    public void setArguments( final Arguments value )
    {
        this.arguments = value;
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
            append( ", moduleName=" ).append( this.moduleName ).
            append( ", template=" ).append( this.template ).
            append( ", arguments=" ).append( this.arguments );

        buf.append( '}' );
        return buf.toString();
    }

    //----------------------------------------------------------------Template--
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
     * properties {@code name} and {@code moduleName}.
     *
     * @param o the reference object with which to compare.
     *
     * @return {@code true} if this object is the same as {@code o};
     * {@code false} otherwise.
     */
    public final boolean equals( final Object o )
    {
        boolean equal = o == this;
        if ( !equal && o instanceof Message )
        {
            final Message that = (Message) o;
            equal = this.getName().equals( that.getName() ) &&
                this.getModuleName().equals( that.getModuleName() );

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
        return this.getName().hashCode() + this.getModuleName().hashCode();
    }

    //------------------------------------------------------------------Object--
}

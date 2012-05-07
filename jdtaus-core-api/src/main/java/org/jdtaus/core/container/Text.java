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
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * Localized text.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class Text implements Cloneable, Serializable
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.5.x classes. */
    private static final long serialVersionUID = 6677913442223787679L;

    //---------------------------------------------------------------Constants--
    //--Text--------------------------------------------------------------------

    /**
     * The value of the text.
     * @serial
     */
    private String value;

    /**
     * Maps locales to values.
     * @serial
     */
    private final Map values = new TreeMap();

    /**
     * Gets the value of the text for the default language.
     *
     * @return the value of the text for the default language or {@code null}.
     */
    public String getValue()
    {
        return this.value;
    }

    /**
     * Setter for property {@code value}.
     *
     * @param value the new value of the text for the default language.
     */
    public void setValue( final String value )
    {
        this.value = value;
    }

    /**
     * Gets the value of the text for a given locale.
     *
     * @param locale the locale of the value to return.
     *
     * @return the value of the text for {@code locale} or {@code null}.
     *
     * @throws NullPointerException if {@code locale} is {@code null}.
     */
    public String getValue( final Locale locale )
    {
        if ( locale == null )
        {
            throw new NullPointerException( "locale" );
        }

        String v =
            (String) this.values.get( locale.getLanguage().toLowerCase() );

        if ( v == null )
        {
            v = this.getValue();
        }

        return v;
    }

    /**
     * Setter for property {@code value} for a given locale.
     *
     * @param locale the locale to store {@code value} with.
     * @param value the new value of the text for {@code locale}.
     *
     * @throws NullPointerException if {@code locale} is {@code null}.
     */
    public void setValue( final Locale locale, final String value )
    {
        if ( locale == null )
        {
            throw new NullPointerException( "locale" );
        }

        this.values.put( locale.getLanguage().toLowerCase(), value );
    }

    /**
     * Gets all locales for which the instance holds values.
     *
     * @return all locales for which the instance holds values.
     */
    public Locale[] getLocales()
    {
        final Locale[] locales = new Locale[ this.values.size() ];
        int i = locales.length - 1;

        for ( Iterator it = this.values.keySet().iterator(); it.hasNext();)
        {
            locales[i--] = new Locale( (String) it.next() );
        }

        return locales;
    }

    /**
     * Creates a string representing the properties of the instance.
     *
     * @return a string representing the properties of the instance.
     */
    private String internalString()
    {
        final StringBuffer buf = new StringBuffer( 500 ).append( '{' );
        final Locale[] locales = this.getLocales();
        for ( int i = locales.length - 1; i >= 0; i-- )
        {
            buf.append( "[" ).append( locales[i] ).append( "]=" ).
                append( this.getValue( locales[i] ) );

            if ( i - 1 >= 0 )
            {
                buf.append( ", " );
            }
        }

        buf.append( '}' );
        return buf.toString();
    }

    //--------------------------------------------------------------------Text--
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

    //------------------------------------------------------------------Object--
}

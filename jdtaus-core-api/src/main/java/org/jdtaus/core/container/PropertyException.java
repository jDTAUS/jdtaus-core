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

import java.util.Locale;

/**
 * Gets thrown for illegal property values.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class PropertyException extends IllegalStateException
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.0.x classes. */
    private static final long serialVersionUID = -8921078803137740601L;

    //---------------------------------------------------------------Constants--
    //--Constructors------------------------------------------------------------

    /**
     * Creates a new instance of {@code PropertyException} taking a property
     * name and the invalid value.
     *
     * @param name the name of the property with invalid value.
     * @param value the invalid value of the property with name {@code name}.
     */
    public PropertyException( final String name, final Object value )
    {
        super( PropertyExceptionBundle.getInstance().
               getPropertyExceptionMessage( Locale.getDefault(), name,
                                            value.toString() ) );

        this.name = name;
        this.value = value;
    }

    /**
     * Creates a new instance of {@code PropertyException} taking a property
     * name, the invalid value and a causing throwable.
     *
     * @param name the name of the property with invalid value.
     * @param value the invalid value of the property with name {@code name}.
     * @param cause the causing throwable.
     */
    public PropertyException( final String name, final Object value,
                              final Throwable cause )
    {

        super( PropertyExceptionBundle.getInstance().
               getPropertyExceptionMessage( Locale.getDefault(), name,
                                            value.toString() ) );

        this.initCause( cause );
        this.name = name;
        this.value = value;
    }

    //------------------------------------------------------------Constructors--
    //--PropertyException-------------------------------------------------------

    /**
     * The name of the invalid property.
     * @serial
     */
    private final String name;

    /**
     * The value of the invalid property.
     * @serial
     */
    private final Object value;

    /**
     * Gets the name of the invalid property.
     *
     * @return the name of the invalid property or {@code null}.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Gets the value of the invalid property.
     *
     * @return the value of the invalid property or {@code null}.
     */
    public Object getValue()
    {
        return this.value;
    }

    //-------------------------------------------------------PropertyException--
}

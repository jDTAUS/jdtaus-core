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
 * Gets thrown for type collisions of inherited properties, implemented
 * properties, or dependency properties.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class IllegalPropertyTypeException extends IllegalStateException
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.5.x classes. */
    private static final long serialVersionUID = -2081711860347150219L;

    //---------------------------------------------------------------Constants--
    //--Constructors------------------------------------------------------------

    /**
     * Creates a new instance of {@code IllegalPropertyTypeException} taking a
     * property name and the illegal type.
     *
     * @param name the name of the property with illegal type.
     * @param type the illegal type of the property with name {@code name}.
     * @param expectedType the expected type of the property with name
     * {@code name}.
     */
    public IllegalPropertyTypeException( final String name, final Class type,
                                         final Class expectedType )
    {
        super( IllegalPropertyTypeExceptionBundle.getInstance().
               getIllegalPropertyTypeMessage( Locale.getDefault(), name,
                                              type.getName(),
                                              expectedType.getName() ) );

        this.name = name;
        this.type = type;
        this.expectedType = expectedType;
    }

    //------------------------------------------------------------Constructors--
    //--IllegalPropertyTypeException--------------------------------------------

    /**
     * The name of the property with illegal type.
     * @serial
     */
    private String name;

    /**
     * The illegal type.
     * @serial
     */
    private Class type;

    /**
     * The expected type.
     * @serial
     */
    private Class expectedType;

    /**
     * Gets the name of the property with illegal type.
     *
     * @return the name of the property with illegal type.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Gets the illegal type of the property.
     *
     * @return the illegal type of the property.
     */
    public Class getType()
    {
        return this.type;
    }

    /**
     * Gets the expected type of the property.
     *
     * @return the expected type of the property.
     */
    public Class getExpectedType()
    {
        return this.expectedType;
    }

    //--------------------------------------------IllegalPropertyTypeException--
}

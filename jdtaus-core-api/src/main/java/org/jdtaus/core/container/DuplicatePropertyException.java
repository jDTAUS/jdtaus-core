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

import java.util.Locale;

/**
 * Gets thrown for duplicate properties.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class DuplicatePropertyException extends IllegalArgumentException
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.0.x classes. */
    private static final long serialVersionUID = 6487289126296131392L;

    //---------------------------------------------------------------Constants--
    //--Constructors------------------------------------------------------------

    /**
     * Creates a new instance of {@code DuplicatePropertyException} taking the
     * name of the duplicate property.
     *
     * @param name the name of the duplicate property.
     */
    public DuplicatePropertyException( final String name )
    {
        super( DuplicatePropertyExceptionBundle.getInstance().
               getDuplicatePropertyMessage( Locale.getDefault(), name ) );

        this.name = name;
    }

    //------------------------------------------------------------Constructors--
    //--DuplicatePropertyException----------------------------------------------

    /***
     * The name of the duplicate property.
     * @serial
     */
    private String name;

    /**
     * Gets the name of the duplicate property.
     *
     * @return the name of the duplicate property or {@code null}.
     */
    public String getName()
    {
        return this.name;
    }

    //----------------------------------------------DuplicatePropertyException--
}

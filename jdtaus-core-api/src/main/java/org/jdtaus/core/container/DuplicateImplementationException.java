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

import java.util.Locale;

/**
 * Gets thrown for duplicate implementations.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class DuplicateImplementationException extends IllegalArgumentException
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.0.x classes. */
    private static final long serialVersionUID = -4694152492308442995L;

    //---------------------------------------------------------------Constants--
    //--Constructors------------------------------------------------------------

    /**
     * Creates a new instance of {@code DuplicateImplementationException} taking
     * the identifier of the duplicate implementation.
     *
     * @param identifier the identifier of the duplicate implementation.
     */
    public DuplicateImplementationException( final String identifier )
    {
        super( DuplicateImplementationExceptionBundle.getInstance().
               getDuplicateImplementationMessage( Locale.getDefault(),
                                                  identifier ) );

        this.identifier = identifier;
    }

    //------------------------------------------------------------Constructors--
    //--DuplicateImplementationException----------------------------------------

    /***
     * The identifier of the duplicate implementation.
     * @serial
     */
    private String identifier;

    /**
     * Gets the identifier of the duplicate implementation.
     *
     * @return the identifier of the duplicate implementation or {@code null}.
     */
    public String getIdentifier()
    {
        return this.identifier;
    }

    //----------------------------------------DuplicateImplementationException--
}

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
 * Gets thrown for duplicate specifications.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class DuplicateSpecificationException extends IllegalArgumentException
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.0.x classes. */
    private static final long serialVersionUID = 7349613537818168838L;

    //---------------------------------------------------------------Constants--
    //--Constructors------------------------------------------------------------

    /**
     * Creates a new instance of {@code DuplicateSpecificationException} taking
     * the identifier of the duplicate specification.
     *
     * @param identifier the identifier of the duplicate specification.
     */
    public DuplicateSpecificationException( final String identifier )
    {
        super( DuplicateSpecificationExceptionBundle.getInstance().
               getDuplicateSpecificationMessage( Locale.getDefault(),
                                                 identifier ) );

        this.identifier = identifier;
    }

    //------------------------------------------------------------Constructors--
    //--DuplicateSpecificationException-----------------------------------------

    /***
     * The identifier of the duplicate specification.
     * @serial
     */
    private final String identifier;

    /**
     * Gets the identifier of the duplicate specification.
     *
     * @return the identifier of the duplicate specification or {@code null}.
     */
    public String getIdentifier()
    {
        return this.identifier;
    }

    //-----------------------------------------DuplicateSpecificationException--
}

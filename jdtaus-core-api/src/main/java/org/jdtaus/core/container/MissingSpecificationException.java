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
 * Gets thrown when specification meta-data is missing.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class MissingSpecificationException extends IllegalStateException
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.0.x classes. */
    private static final long serialVersionUID = -4832521512370925145L;

    //---------------------------------------------------------------Constants--
    //--Constructors------------------------------------------------------------

    /**
     * Creates a new instance of {@code MissingSpecificationException} taking
     * the identifier of the missing specification.
     *
     * @param identifier the identifier of the missing specification.
     */
    public MissingSpecificationException( final String identifier )
    {
        super( MissingSpecificationExceptionBundle.getInstance().
               getMissingSpecificationMessage( Locale.getDefault(),
                                               identifier ) );

        this.identifier = identifier;
    }

    //------------------------------------------------------------Constructors--
    //--MissingSpecificationException-------------------------------------------

    /***
     * The identifier of the missing specification.
     * @serial
     */
    private String identifier;

    /**
     * Gets the identifier of the missing specification.
     *
     * @return the identifier of the missing specification or {@code null}.
     */
    public String getIdentifier()
    {
        return this.identifier;
    }

    //-------------------------------------------MissingSpecificationException--
}

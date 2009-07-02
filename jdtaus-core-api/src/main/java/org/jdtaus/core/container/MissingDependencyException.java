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
 * Gets thrown when dependency meta-data is missing.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $Id$
 */
public class MissingDependencyException extends IllegalStateException
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.0.x classes. */
    private static final long serialVersionUID = 450263751326101917L;

    //---------------------------------------------------------------Constants--
    //--Constructors------------------------------------------------------------

    /**
     * Creates a new instance of {@code MissingDependencyException} taking
     * the identifier of the missing dependency.
     *
     * @param identifier the identifier of the missing dependency.
     */
    public MissingDependencyException( final String identifier )
    {
        super( MissingDependencyExceptionBundle.getInstance().
               getMissingDependencyMessage( Locale.getDefault(), identifier ) );

        this.identifier = identifier;
    }

    //------------------------------------------------------------Constructors--
    //--MissingDependencyException----------------------------------------------

    /***
     * The identifier of the missing dependency.
     * @serial
     */
    private String identifier;

    /**
     * Gets the identifier of the missing dependency.
     *
     * @return the identifier of the missing dependency or {@code null}.
     */
    public String getIdentifier()
    {
        return this.identifier;
    }

    //----------------------------------------------MissingDependencyException--
}

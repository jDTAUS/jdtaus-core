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
 * Gets thrown when argument meta-data is missing.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $Id$
 */
public class MissingArgumentException extends IllegalStateException
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.5.x classes. */
    private static final long serialVersionUID = 3802349052536479127L;

    //---------------------------------------------------------------Constants--
    //--Constructors------------------------------------------------------------

    /**
     * Creates a new instance of {@code MissingArgumentException} taking
     * the name of the missing argument.
     *
     * @param name the name of the missing argument.
     */
    public MissingArgumentException( final String name )
    {
        super( MissingArgumentExceptionBundle.getInstance().
               getMissingArgumentMessage( Locale.getDefault(), name ) );

        this.name = name;
    }

    //------------------------------------------------------------Constructors--
    //--MissingArgumentException------------------------------------------------

    /***
     * The name of the missing argument.
     * @serial
     */
    private String name;

    /**
     * Gets the name of the missing argument.
     *
     * @return the name of the missing argument or {@code null}.
     */
    public String getName()
    {
        return this.name;
    }

    //------------------------------------------------MissingArgumentException--
}

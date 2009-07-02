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
 * Gets thrown when creating an instance of an implementation fails.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class InstantiationException extends IllegalStateException
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.0.x classes. */
    private static final long serialVersionUID = -7299094102825525833L;

    //---------------------------------------------------------------Constants--
    //--Constructors------------------------------------------------------------

    /**
     * Creates a new {@code InstantiationException} instance.
     *
     * @param identifier the identifier of the implementation for which creating
     * an instance failed.
     * @param cause the cause of the exception.
     */
    public InstantiationException( final String identifier,
                                   final Throwable cause )
    {

        super( InstantiationExceptionBundle.getInstance().
               getInstantiationExceptionMessage( Locale.getDefault(),
                                                 identifier ) );

        this.initCause( cause );
        this.identifier = identifier;
    }

    //------------------------------------------------------------Constructors--
    //--InstantiationException--------------------------------------------------

    /**
     * The identifier of the implementation for which creating an instance
     * failed.
     * @serial
     */
    private String identifier;

    /**
     * Gets the identifier of the implementation for which creating an instance
     * failed.
     *
     * @return the identifier of the implementation for which creating an
     * instance failed or {@code null}.
     */
    public String getIdentifier()
    {
        return this.identifier;
    }

    //--------------------------------------------------InstantiationException--
}

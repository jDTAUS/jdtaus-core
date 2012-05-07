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
 * Unrecoverable error thrown if no working {@code Context} is available in the
 * system.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class ContextError extends Error
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.0.x classes. */
    private static final long serialVersionUID = 5504595041273140818L;

    //---------------------------------------------------------------Constants--
    //--Constructors------------------------------------------------------------

    /**
     * Creates a new instance of {@code ContextError} taking a causing
     * {@code Throwable}.
     *
     * @param cause the causing throwable.
     */
    public ContextError( final Throwable cause )
    {
        super( ContextErrorBundle.getInstance().
               getContextErrorMessage( Locale.getDefault(),
                                       cause != null
                                       ? cause.getMessage()
                                       : null ), cause );

    }

    /**
     * Creates a new instance of {@code ContextError} taking a message.
     *
     * @param msg the message describing the error.
     */
    public ContextError( final String msg )
    {
        super( ContextErrorBundle.getInstance().
               getContextErrorMessage( Locale.getDefault(), msg ) );

    }

    //------------------------------------------------------------Constructors--
}

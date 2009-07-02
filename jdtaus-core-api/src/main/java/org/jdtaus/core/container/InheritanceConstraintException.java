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
 * Gets thrown for inheritance constraint violations.
 * <p>An {@code Implementation} is only allowed to be the parent of another
 * implementation if the value of property {@code final} is {@code false}.
 * This exception gets thrown for any implementation violating this
 * constraint.</p>
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $Id$
 *
 * @see Implementation#getParent()
 * @see Implementation#isFinal()
 */
public class InheritanceConstraintException extends IllegalStateException
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.2.x classes. */
    private static final long serialVersionUID = 8681879454481971406L;

    //---------------------------------------------------------------Constants--
    //--Constructors------------------------------------------------------------

    /**
     * Creates a new {@code InheritanceConstraintException} instance
     * taking the identifier of the implementation violating the constraint.
     *
     * @param implementationIdentifier identifier of the implementation
     * violating the constraint.
     */
    public InheritanceConstraintException(
        final String implementationIdentifier )
    {
        super( InheritanceConstraintExceptionBundle.getInstance().
               getInheritanceConstraintMessage( Locale.getDefault(),
                                                implementationIdentifier ) );

        this.implementationIdentifier = implementationIdentifier;
    }

    //------------------------------------------------------------Constructors--
    //--InheritanceConstraintException------------------------------------------

    /**
     * Identifier of the implementation violating the constraint.
     * @serial
     */
    private final String implementationIdentifier;

    /**
     * Gets the identifier of the implementation violating the constraint.
     *
     * @return identifier of the implementation violating the constraint.
     */
    public String getImplementationIdentifier()
    {
        return this.implementationIdentifier;
    }

    //------------------------------------------InheritanceConstraintException--
}

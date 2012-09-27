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
 * Gets thrown for multiplicity constraint violations.
 * <p>Specifications with a multiplicity of {@code MULTIPLICITY_ONE} specify
 * that exactly one corresponding implementation must be available among a set
 * of modules. This exception gets thrown for any specification violating this
 * constraint.</p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 *
 * @see Specification#getMultiplicity()
 */
public class MultiplicityConstraintException extends IllegalStateException
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.2.x classes. */
    private static final long serialVersionUID = 344879434134284092L;

    //---------------------------------------------------------------Constants--
    //--Constructors------------------------------------------------------------

    /**
     * Creates a new {@code MultiplicityConstraintException} taking the
     * identifier of the specification for which the multiplicity constraint
     * is violated.
     *
     * @param specificationIdentifier identifier of the specification for
     * which the constraint is violated.
     */
    public MultiplicityConstraintException( final String specificationIdentifier )
    {
        super( MultiplicityConstraintExceptionBundle.getInstance().
               getMultiplicityConstraintMessage( Locale.getDefault(),
                                                 specificationIdentifier ) );

        this.specificationIdentifier = specificationIdentifier;
    }

    //------------------------------------------------------------Constructors--
    //--MultiplicityConstraintException-----------------------------------------

    /**
     * Identifier of the specification for which the multiplicity constraint
     * is violated.
     * @serial
     */
    private final String specificationIdentifier;

    /**
     * Gets the identifier of the specification for which the multiplicity
     * constraint is violated.
     *
     * @return the identifier of the specification for which the multiplicity
     * constraint is violated.
     */
    public String getSpecificationIdentifier()
    {
        return this.specificationIdentifier;
    }

    //-----------------------------------------MultiplicityConstraintException--
}

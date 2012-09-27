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
 * Gets thrown for implementation incompatibilities.
 * <p>An {@code Implementation} is required to implement and to depend on a
 * specification version compatible with the version in use. This exception
 * gets thrown for any implementation implementing or depending on a
 * specification version incompatible with the version in use.</p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 *
 * @see Specification#getVersion()
 * @see Implementation#getImplementedSpecifications()
 */
public class IncompatibleImplementationException
    extends IllegalStateException
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.5.x classes. */
    private static final long serialVersionUID = 4329145399712314886L;

    //---------------------------------------------------------------Constants--
    //--Constructors------------------------------------------------------------

    /**
     * Creates a new {@code IncompatibleImplementationException} instance
     * taking the implementation not implementing the specification version in
     * use or depending on a specification version incompatible to the
     * version in use.
     *
     * @param specificationIdentifier the identifier of the specification.
     * @param specifiedVersion the version of the specification in use.
     * @param implementationIdentifier the identifier of the implementation
     * incompatible to {@code specifiedVersion}.
     * @param implementedVersion the version implemented or {@code null} if
     * the implementation does not implement the specification.
     * @param requiredVersion the version the implementation depends on or
     * {@code null} if the implementation does not depend on the specification.
     */
    public IncompatibleImplementationException(
        final String specificationIdentifier,
        final String specifiedVersion,
        final String implementationIdentifier,
        final String implementedVersion,
        final String requiredVersion )
    {
        super( IncompatibleImplementationExceptionBundle.getInstance().
               getIncompatibleImplementationMessage(
               Locale.getDefault(), ( implementedVersion != null
               ? new Integer( 0 )
               : new Integer( 1 ) ), specificationIdentifier, specifiedVersion,
               implementationIdentifier, implementedVersion, requiredVersion ) );

        this.specificationIdentifier = specificationIdentifier;
        this.specifiedVersion = specifiedVersion;
        this.implementationIdentifier = implementationIdentifier;
        this.implementedVersion = implementedVersion;
        this.requiredVersion = requiredVersion;
    }

    //------------------------------------------------------------Constructors--
    //--IncompatibleImplementationException-------------------------------------

    /**
     * The identifier of the specification.
     * @serial
     */
    private String specificationIdentifier;

    /**
     * The version of the specification in use.
     * @serial
     */
    private String specifiedVersion;

    /**
     * The identifier of the implementation not implementing the specification
     * version in use.
     * @serial
     */
    private String implementationIdentifier;

    /**
     * The implemented version of the specification in use or {@code null} if
     * the implementation does not implement the specification.
     * @serial
     */
    private String implementedVersion;

    /**
     * The version the implementation depends on or {@code null} if the
     * implementation does not depend on the specification.
     * @serial
     */
    private String requiredVersion;

    /**
     * Gets the identifier of the specification.
     *
     * @return the identifier of the specification.
     */
    public String getSpecificationIdentifier()
    {
        return this.specificationIdentifier;
    }

    /**
     * Gets the version of the specification in use.
     *
     * @return the version of the specification in use.
     */
    public String getSpecifiedVersion()
    {
        return this.specifiedVersion;
    }

    /**
     * Gets the identifier of the implementation not implementing the
     * specification version in use.
     *
     * @return the identifier of the implementation not implementing the
     * specification version in use.
     */
    public String getImplementationIdentifier()
    {
        return this.implementationIdentifier;
    }

    /**
     * Gets the implemented version of the specification in use.
     *
     * @return the implemented version of the specification in use or
     * {@code null} if the implementation does not implement the specification.
     */
    public String getImplementedVersion()
    {
        return this.implementedVersion;
    }

    /**
     * Gets the required version of the specification in use.
     *
     * @return the version the implementation depends on or {@code null} if the
     * implementation does not depend on the specification.
     */
    public String getRequiredVersion()
    {
        return this.requiredVersion;
    }

    //-------------------------------------IncompatibleImplementationException--
}

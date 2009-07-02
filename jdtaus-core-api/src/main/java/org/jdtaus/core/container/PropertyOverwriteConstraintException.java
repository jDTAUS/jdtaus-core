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
 * Gets thrown for property overwrite constraint violations.
 * <p>A {@code Dependency} is allowed to provide values for any properties
 * declared for its specification only if the specification applies to scope
 * {@code SCOPE_MULTITON}. An implementation must provide values for all
 * properties declared for its implemented specifications. This exception gets
 * thrown for any violations to these restrictions.</p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 *
 * @see Implementation#getProperties()
 * @see Dependency#getProperties()
 */
public class PropertyOverwriteConstraintException extends IllegalStateException
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.2.x classes. */
    private static final long serialVersionUID = -6382737345293763298L;

    //---------------------------------------------------------------Constants--
    //--Constructors------------------------------------------------------------

    /**
     * Creates a new {@code PropertyOverwriteConstraintException} instance
     * taking the identifier of an implementation defining a dependency
     * providing property values although the specification does not apply to
     * the multiton scope.
     *
     * @param implementationIdentifier identifier of the implementation defining
     * a dependency violating the constraint.
     * @param dependencyName name of the dependency defined for the
     * implementation identified by {@code implementationIdentifier} violating
     * the constraint.
     */
    public PropertyOverwriteConstraintException(
        final String implementationIdentifier, final String dependencyName )
    {
        super( PropertyOverwriteConstraintExceptionBundle.getInstance().
               getPropertyOverwriteConstraintMessage( Locale.getDefault(),
                                                      implementationIdentifier,
                                                      dependencyName ) );

        this.implementationIdentifier = implementationIdentifier;
        this.specificationIdentifier = null;
        this.dependencyName = dependencyName;
        this.propertyName = null;
    }

    /**
     * Creates a new {@code PropertyOverwriteConstraintException} instance
     * taking the identifier of an implementation not implementing a
     * specification property together with information regarding the
     * unimplemented property.
     *
     * @param implementationIdentifier identifier of the implementation not
     * implementing a specification property.
     * @param specificationIdentifier identifier of the specification an
     * implementation does not implement a property of.
     * @param propertyName name of the unimplemented property.
     */
    public PropertyOverwriteConstraintException(
        final String implementationIdentifier,
        final String specificationIdentifier, final String propertyName )
    {
        super( PropertyOverwriteConstraintExceptionBundle.getInstance().
               getPropertyNotImplementedMessage( Locale.getDefault(),
                                                 implementationIdentifier,
                                                 specificationIdentifier,
                                                 propertyName ) );

        this.implementationIdentifier = implementationIdentifier;
        this.specificationIdentifier = specificationIdentifier;
        this.propertyName = propertyName;
        this.dependencyName = null;
    }

    //------------------------------------------------------------Constructors--
    //--PropertyOverwriteConstraintException------------------------------------

    /**
     * Identifier of the implementation defining a dependency violating the
     * constraint.
     * @serial
     */
    private final String implementationIdentifier;

    /**
     * Identifier of the specification defining a property not implemented by
     * the implementation.
     * @serial
     */
    private final String specificationIdentifier;

    /**
     * Name of the dependency defined for the implementation identified by
     * property {@code implementationIdentifier} violating the constraint.
     * @serial
     */
    private final String dependencyName;

    /**
     * Name of the property not implemented by the implementation.
     * @serial
     */
    private final String propertyName;

    /**
     * Gets the identifier of the implementation defining a dependency
     * violating the constraint.
     *
     * @return identifier of the implementation defining a dependency
     * violating the constraint.
     */
    public String getImplementationIdentifier()
    {
        return this.implementationIdentifier;
    }

    /**
     * Gets the identifier of the specification defining a property not
     * implemented by the implementation.
     *
     * @return identifier of the specification defining a property not
     * implemented by the implementation or {@code null}.
     */
    public String getSpecificationIdentifier()
    {
        return this.specificationIdentifier;
    }

    /**
     * Gets the name of the dependency defined for the implementation identified
     * by the value of property {@code implementationIdentifier} violating the
     * constraint.
     *
     * @return name of the dependency defined for the implementation identified
     * by the value of property {@code implementationIdentifier} violating the
     * constraint or {@code null}.
     */
    public String getDependencyName()
    {
        return this.dependencyName;
    }

    /**
     * Gets the name of the property not implemented by the implementation.
     *
     * @return name of the property not implemented by the implementation or
     * {@code null}.
     */
    public String getPropertyName()
    {
        return this.propertyName;
    }

    //------------------------------------PropertyOverwriteConstraintException--
}

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
 * Gets thrown for type collisions of inherited dependencies.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class IllegalDependencyTypeException extends IllegalStateException
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.5.x classes. */
    private static final long serialVersionUID = 1616079465342123026L;

    //---------------------------------------------------------------Constants--
    //--Constructors------------------------------------------------------------

    /**
     * Creates a new instance of {@code IllegalDependencyTypeException} taking
     * information about the dependency with illegal type.
     *
     * @param implementationIdentifier the identifier of the implementation with
     * a dependency of illegal type.
     * @param name the name of the dependency with illegal type.
     * @param type the illegal type of the dependency with name {@code name}.
     * @param expectedType the expected type of the dependency with name
     * {@code name}.
     */
    public IllegalDependencyTypeException( final String implementationIdentifier,
                                           final String name, final String type,
                                           final String expectedType )
    {
        super( IllegalDependencyTypeExceptionBundle.getInstance().
               getIllegalDependencyTypeMessage( Locale.getDefault(),
                                                implementationIdentifier, name,
                                                type, expectedType ) );

        this.implementationIdentifier = implementationIdentifier;
        this.name = name;
        this.type = type;
        this.expectedType = expectedType;
    }

    //------------------------------------------------------------Constructors--
    //--IllegalDependencyTypeException------------------------------------------

    /**
     * The identifier of the implementation with a dependency of illegal type.
     * @serial
     */
    private String implementationIdentifier;

    /**
     * The name of the dependency with illegal type.
     * @serial
     */
    private String name;

    /**
     * The illegal type.
     * @serial
     */
    private String type;

    /**
     * The expected type.
     * @serial
     */
    private String expectedType;

    /**
     * Gets the identifier of the implementation with a dependency of illegal
     * type.
     *
     * @return the identifier of the implementation with a dependency of illegal
     * type.
     */
    public String getImplementationIdentifier()
    {
        return this.implementationIdentifier;
    }

    /**
     * Gets the name of the dependency with illegal type.
     *
     * @return the name of the dependency with illegal type.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Gets the illegal type of the dependency.
     *
     * @return the illegal type of the dependency.
     */
    public String getType()
    {
        return this.type;
    }

    /**
     * Gets the expected type of the dependency.
     *
     * @return the expected type of the dependency.
     */
    public String getExpectedType()
    {
        return this.expectedType;
    }

    //------------------------------------------IllegalDependencyTypeException--
}

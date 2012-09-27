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
 * Gets thrown for cyclic dependency graphs.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class DependencyCycleException extends IllegalStateException
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.5.x classes. */
    private static final long serialVersionUID = 8471828485552467121L;

    //---------------------------------------------------------------Constants--
    //--Constructors------------------------------------------------------------

    /**
     * Creates a new {@code DependencyCycleException} taking the involved
     * implementations.
     *
     * @param impl1 the implementation the cycle was detected for.
     * @param impl2 the implementation introducing the cycle to {@code impl1}.
     *
     * @throws NullPointerException if either {@code impl1} or {@code impl2}
     * is {@code null}.
     */
    public DependencyCycleException( final Implementation impl1,
                                     final Implementation impl2 )
    {
        super( DependencyCycleExceptionBundle.getInstance().
               getDependencyCycleMessage( Locale.getDefault(),
                                          impl1.getIdentifier(),
                                          impl2.getIdentifier() ) );

        this.implementations = new Implementation[]
            {
                impl1, impl2
            };
    }

    /**
     * Creates a new {@code DependencyCycleException} taking identifiers of the
     * involved implementations.
     *
     * @param identifier1 the identifier of the implementation the cycle was
     * detected for.
     * @param identifier2 the identifier of the implementation introducing the
     * cycle.
     *
     * @throws NullPointerException if either {@code identifier1} or
     * {@code identifier2} is {@code null}.
     */
    public DependencyCycleException( final String identifier1,
                                     final String identifier2 )
    {
        super( DependencyCycleExceptionBundle.getInstance().
               getDependencyCycleMessage( Locale.getDefault(),
                                          identifier1,
                                          identifier2 ) );

    }

    //------------------------------------------------------------Constructors--
    //--DependencyCycleException------------------------------------------------

    /**
     * The implementations introducing a cycle.
     * @serial
     */
    private Implementation[] implementations;

    /**
     * Gets the implementation introducing a cycle.
     *
     * @return the implementations introducing a cycle.
     */
    public Implementation[] getImplementations()
    {
        return this.implementations;
    }

    //------------------------------------------------DependencyCycleException--
}

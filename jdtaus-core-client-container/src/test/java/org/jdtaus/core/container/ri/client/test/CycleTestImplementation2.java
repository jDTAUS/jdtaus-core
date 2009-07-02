/*
 *  jDTAUS Core RI Client Container
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
package org.jdtaus.core.container.ri.client.test;

import org.jdtaus.core.container.ContainerFactory;
import org.jdtaus.core.container.ContainerInitializer;
import org.jdtaus.core.container.PropertyException;

/**
 * Implementation for testing dependency cycle detection.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $Id$
 */
public final class CycleTestImplementation2 implements CycleTestSpecification2,
                                                       ContainerInitializer
{
    //--Implementation----------------------------------------------------------

    // This section is managed by jdtaus-container-mojo.


    //----------------------------------------------------------Implementation--
    //--Constructors------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausConstructors
    // This section is managed by jdtaus-container-mojo.

    /** Standard implementation constructor <code>org.jdtaus.core.container.ri.client.test.CycleTestImplementation2</code>. */
    public CycleTestImplementation2()
    {
        super();
    }

// </editor-fold>//GEN-END:jdtausConstructors

    //------------------------------------------------------------Constructors--
    //--Dependencies------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausDependencies
    // This section is managed by jdtaus-container-mojo.

    /**
     * Gets the configured <code>Dependency</code> implementation.
     *
     * @return the configured <code>Dependency</code> implementation.
     */
    private CycleTestSpecification3 getDependency()
    {
        return (CycleTestSpecification3) ContainerFactory.getContainer().
            getDependency( this, "Dependency");

    }

// </editor-fold>//GEN-END:jdtausDependencies

    //------------------------------------------------------------Dependencies--
    //--Properties--------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausProperties
    // This section is managed by jdtaus-container-mojo.

// </editor-fold>//GEN-END:jdtausProperties

    //--------------------------------------------------------------Properties--
    //--ContainerInitializer----------------------------------------------------

    /**
     * Initializes the instance.
     *
     * @see #assertValidProperties
     */
    public void initialize()
    {
        this.assertValidProperties();
        this.getDependency();
    }

    //----------------------------------------------------ContainerInitializer--

    /**
     * Checks configured properties.
     *
     * @throws PropertyException for illegal property values.
     */
    private void assertValidProperties()
    {
    }

}

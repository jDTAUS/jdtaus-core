/*
 *  jDTAUS Core Test Suite
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
package org.jdtaus.core.container.it;

import org.jdtaus.core.container.Container;

/**
 * Testcase for {@code Container} implementations.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class ContainerTest
{
    //--ContainerTest-----------------------------------------------------------

    /** The implementation to test. */
    private Container container;

    /**
     * Gets the {@code Container} implementation tests are performed with.
     *
     * @return the {@code Container} implementation tests are performed
     * with.
     */
    public Container getContainer()
    {
        return this.container;
    }

    /**
     * Sets the {@code Container} implementation to test.
     *
     * @param value the {@code Container} implementation to test.
     */
    public final void setContainer( final Container value )
    {
        this.container = value;
    }

    //-----------------------------------------------------------ContainerTest--
    //--Tests-------------------------------------------------------------------
    //-------------------------------------------------------------------Tests--
}

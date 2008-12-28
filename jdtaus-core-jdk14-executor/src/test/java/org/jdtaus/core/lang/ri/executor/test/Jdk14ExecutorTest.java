/*
 *  jDTAUS Core RI JDK 1.4 Executor
 *  Copyright (c) 2005 Christian Schulte
 *
 *  Christian Schulte, Haldener Strasse 72, 58095 Hagen, Germany
 *  <cs@jdtaus.org> (+49 2331 3543887)
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
package org.jdtaus.core.lang.ri.executor.test;

import org.jdtaus.core.lang.ri.executor.Jdk14Executor;
import org.jdtaus.core.lang.spi.Executor;
import org.jdtaus.core.lang.spi.it.ExecutorTest;

/**
 * Tests the {@link Jdk14Executor} implementation.
 *
 * @author <a href="mailto:cs@schulte.it">Cest.java 2469 2007-04-11 03:03:04Z schulte2005 $
 */
public class Jdk14ExecutorTest extends ExecutorTest
{
    //--ExecutorTest------------------------------------------------------------

    /** The implementation to test. */
    private Executor executor;

    public Executor getExecutor()
    {
        if ( this.executor == null )
        {
            this.executor = new Jdk14Executor();
        }

        return this.executor;
    }

    //------------------------------------------------------------ExecutorTest--
}

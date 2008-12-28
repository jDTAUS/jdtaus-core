/*
 *  jDTAUS Core Test Suite
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
package org.jdtaus.core.lang.it;

import junit.framework.Assert;
import org.jdtaus.core.lang.Runtime;

/**
 * Testcase for {@code Runtime} implementations.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class RuntimeTest
{
    //--RuntimeTest-------------------------------------------------------------

    /** Implementation to test. */
    private Runtime runtime;

    /**
     * Gets the {@code Runtime} implementation tests are performed with.
     *
     * @return the {@code Runtime} implementation tests are performed with.
     */
    public Runtime getRuntime()
    {
        return this.runtime;
    }

    /**
     * Sets the {@code Runtime} implementation tests are performed with.
     *
     * @param value the {@code Runtime} implementation to perform tests with.
     */
    public final void setRuntime( final Runtime value )
    {
        this.runtime = value;
    }

    //-------------------------------------------------------------RuntimeTest--
    //--Tests-------------------------------------------------------------------

    /**
     * Tests the {@link Runtime#getAllocatedPercent()} method to return sane
     * values.
     */
    public void testGetAllocatedPercent() throws Exception
    {
        assert this.getRuntime() != null;

        final long allocatedPercent = this.getRuntime().getAllocatedPercent();
        Assert.assertTrue( allocatedPercent >= 0 && allocatedPercent <= 100 );
    }

    //-------------------------------------------------------------------Tests--
}

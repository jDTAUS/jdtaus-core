/*
 *  jDTAUS - DTAUS fileformat.
 *  Copyright (c) 2005 Christian Schulte <cs@schulte.it>
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
package org.jdtaus.core.client.util.test;

import junit.framework.Assert;
import org.jdtaus.core.client.util.Application;

/**
 * Testcase for {@code Application} implementations.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public abstract class ApplicationTest
{
    //--ApplicationTest---------------------------------------------------------

    /** The implementation to test. */
    private Application application;

    /**
     * Gets the {@code Application} implementation tests are performed with.
     *
     * @return the {@code Application} implementation tests are performed
     * with.
     */
    public Application getApplication()
    {
        return this.application;
    }

    /**
     * Sets the {@code Application} implementation to test.
     *
     * @param value the {@code Application} implementation to test.
     */
    public final void setApplication(final Application value)
    {
        this.application = value;
    }

    //---------------------------------------------------------ApplicationTest--
    //--Tests-------------------------------------------------------------------

    /**
     * Tests that all methods returning a directory do not return {@code null}
     * values.
     */
    public void testDirectories()
    {
        assert this.getApplication() != null;

        Assert.assertNotNull(this.getApplication().getBinDirectory());
        Assert.assertNotNull(this.getApplication().getConfigDirectory());
        Assert.assertNotNull(this.getApplication().getDataDirectory());
        Assert.assertNotNull(this.getApplication().getStateDirectory());
    }

    //-------------------------------------------------------------------Tests--
}

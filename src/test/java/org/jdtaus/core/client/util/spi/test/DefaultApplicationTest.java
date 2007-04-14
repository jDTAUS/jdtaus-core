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
package org.jdtaus.core.client.util.spi.test;

import org.jdtaus.core.client.util.Application;
import org.jdtaus.core.client.util.spi.DefaultApplication;
import org.jdtaus.core.client.util.test.ApplicationTest;

/**
 * Testcase for the {@code DefaultApplication} implementation.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class DefaultApplicationTest extends ApplicationTest
{
    //--ApplicationTest---------------------------------------------------------

    /** The implementation tests are performed with. */
    private Application application;

    public Application getApplication()
    {
        if(this.application == null)
        {
            System.setProperty(DefaultApplication.class.getName() +
                ".baseDirectory", System.getProperty("java.io.tmpdir"));

            this.application = new DefaultApplication();
            this.setApplication(this.application);
        }

        return super.getApplication();
    }

    //---------------------------------------------------------ApplicationTest--
}

/*
 *  jDTAUS Core RI Application Logger
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
package org.jdtaus.core.text.ri.test;

import org.jdtaus.core.text.ri.DefaultApplicationLogger;
import org.jdtaus.core.text.spi.it.ApplicationLoggerTest;

/**
 * Tests the {@link DefaultApplicationLogger} implementation.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class DefaultApplicationLoggerTest extends ApplicationLoggerTest
{
    //--ApplicationLoggerTest---------------------------------------------------

    /** Creates a new {@code DefaultApplicationLoggerTest} instance. */
    public DefaultApplicationLoggerTest()
    {
        super();
        this.setApplicationLogger( new DefaultApplicationLogger() );
    }

    //---------------------------------------------------ApplicationLoggerTest--
}

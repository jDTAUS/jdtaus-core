/*
 *  jDTAUS Core RI Log4J Logging
 *  Copyright (C) 2005 Christian Schulte
 *  <schulte2005@users.sourceforge.net>
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
package org.jdtaus.core.logging.ri.log4j.test;

import org.jdtaus.core.logging.ri.log4j.Log4JLogger;
import org.jdtaus.core.logging.spi.Logger;
import org.jdtaus.core.logging.spi.it.LoggerTest;

/**
 * Tests the {@link Log4JLogger} implementation.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class Log4JLoggerTest extends LoggerTest
{
    //--Log4JLoggerTest---------------------------------------------------------

    /** The implementation to test. */
    private final Logger logger = new Log4JLogger();

    public Logger getLogger()
    {
        this.setLogger(this.logger);
        return super.getLogger();
    }

    //---------------------------------------------------------Log4JLoggerTest--
    //--Tests-------------------------------------------------------------------

    //-------------------------------------------------------------------Tests--
}

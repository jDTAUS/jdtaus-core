/*
 *  jDTAUS Core Test Suite
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
package org.jdtaus.core.logging.spi.it;

import org.jdtaus.core.logging.spi.Logger;

/**
 * Testcase for {@code Logger} implementations.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class LoggerTest
{
    //--LoggerTest--------------------------------------------------------------

    /** Implementation to test. */
    private Logger logger;

    /**
     * Gets the {@code Logger} implementation tests are performed with.
     *
     * @return the {@code Logger} implementation tests are performed with.
     */
    public Logger getLogger()
    {
        return this.logger;
    }

    /**
     * Sets the {@code Logger} implementation tests are performed with.
     *
     * @param value the {@code Logger} implementation to perform tests with.
     */
    public final void setLogger( final Logger value )
    {
        this.logger = value;
    }

    //--------------------------------------------------------------LoggerTest--
    //--Tests-------------------------------------------------------------------

    /**
     * Tests the {@link Logger#isInfoEnabled() isXxxEnabled()} methods to not
     * throw any exceptions.
     */
    public void testIsEnabled() throws Exception
    {
        assert this.getLogger() != null;

        this.getLogger().isDebugEnabled();
        this.getLogger().isErrorEnabled();
        this.getLogger().isFatalEnabled();
        this.getLogger().isInfoEnabled();
        this.getLogger().isTraceEnabled();
        this.getLogger().isWarnEnabled();
    }

    /**
     * Test the various logger methods to not throw any exceptions.
     */
    public void testLog() throws Exception
    {
        assert this.getLogger() != null;

        this.getLogger().debug( "TEST" );
        this.getLogger().debug( new Exception() );

        this.getLogger().error( "TEST" );
        this.getLogger().error( new Exception() );

        this.getLogger().fatal( "TEST" );
        this.getLogger().fatal( new Exception() );

        this.getLogger().info( "TEST" );
        this.getLogger().info( new Exception() );

        this.getLogger().trace( "TEST" );
        this.getLogger().trace( new Exception() );

        this.getLogger().warn( "TEST" );
        this.getLogger().warn( new Exception() );
    }

    //-------------------------------------------------------------------Tests--
}

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
package org.jdtaus.core.lang.it;

import junit.framework.Assert;
import org.jdtaus.core.lang.ExceptionEventSource;

/**
 * Testcase for {@code ExceptionEventSource} implementations.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
public abstract class ExceptionEventSourceTest
{
    //--ExceptionEventSourceTest------------------------------------------------

    /**
     * Gets the {@code ExceptionEventSource} implementation tests are performed
     * with.
     *
     * @return the {@code ExceptionEventSource} implementation tests are
     * performed with.
     */
    public abstract ExceptionEventSource getExceptionEventSource();

    //------------------------------------------------ExceptionEventSourceTest--
    //--Tests-------------------------------------------------------------------

    /**
     * Tests the
     * {@link ExceptionEventSource#addExceptionListener(ExceptionListener)} and
     * {@link ExceptionEventSource#removeExceptionListener(ExceptionListener)}
     * methods to correctly handle {@code null} arguments by throwing a
     * corresponding {@code NullPointerException}.
     */
    public void testNullArguments() throws Exception
    {
        assert this.getExceptionEventSource() != null;

        try
        {
            this.getExceptionEventSource().addExceptionListener( null );
            throw new AssertionError();
        }
        catch ( NullPointerException e )
        {
            Assert.assertNotNull( e.getMessage() );
            System.out.println( e.toString() );
        }

        try
        {
            this.getExceptionEventSource().removeExceptionListener( null );
            throw new AssertionError();
        }
        catch ( NullPointerException e )
        {
            Assert.assertNotNull( e.getMessage() );
            System.out.println( e.toString() );
        }

    }

    //-------------------------------------------------------------------Tests--
}

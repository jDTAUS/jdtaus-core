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
package org.jdtaus.core.lang.spi.it;

import junit.framework.Assert;
import org.jdtaus.core.lang.it.RuntimeTest;
import org.jdtaus.core.lang.spi.MemoryManager;

/**
 * Testcase for {@code MemoryManager} implementations.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class MemoryManagerTest extends RuntimeTest
{
    //--MemoryManagerTest-------------------------------------------------------

    /** Implementation to test. */
    private MemoryManager manager;

    /**
     * Gets the {@code MemoryManager} implementation tests are performed with.
     *
     * @return the {@code MemoryManager} implementation tests are performed
     * with.
     */
    public MemoryManager getMemoryManager()
    {
        return this.manager;
    }

    /**
     * Sets the {@code MemoryManager} implementation tests are performed with.
     *
     * @param value the {@code MemoryManager} implementation to perform tests
     * with.
     */
    public final void setMemoryManager( final MemoryManager value )
    {
        this.manager = value;
        this.setRuntime( value );
    }

    //-------------------------------------------------------MemoryManagerTest--
    //--Tests-------------------------------------------------------------------

    /**
     * Tests the {@link MemoryManager#allocateBytes(int) acclocateXxx()} methods
     * to handle illegal arguments correctly.
     */
    public void testIllegalArguments() throws Exception
    {
        assert this.getMemoryManager() != null;

        try
        {
            this.getMemoryManager().allocateBoolean( Integer.MIN_VALUE );
            throw new AssertionError();
        }
        catch ( IllegalArgumentException e )
        {
            Assert.assertNotNull( e.getMessage() );
            System.out.println( e.toString() );
        }

        try
        {
            this.getMemoryManager().allocateBytes( Integer.MIN_VALUE );
            throw new AssertionError();
        }
        catch ( IllegalArgumentException e )
        {
            Assert.assertNotNull( e.getMessage() );
            System.out.println( e.toString() );
        }

        try
        {
            this.getMemoryManager().allocateChars( Integer.MIN_VALUE );
            throw new AssertionError();
        }
        catch ( IllegalArgumentException e )
        {
            Assert.assertNotNull( e.getMessage() );
            System.out.println( e.toString() );
        }

        try
        {
            this.getMemoryManager().allocateDoubles( Integer.MIN_VALUE );
            throw new AssertionError();
        }
        catch ( IllegalArgumentException e )
        {
            Assert.assertNotNull( e.getMessage() );
            System.out.println( e.toString() );
        }

        try
        {
            this.getMemoryManager().allocateFloats( Integer.MIN_VALUE );
            throw new AssertionError();
        }
        catch ( IllegalArgumentException e )
        {
            Assert.assertNotNull( e.getMessage() );
            System.out.println( e.toString() );
        }

        try
        {
            this.getMemoryManager().allocateIntegers( Integer.MIN_VALUE );
            throw new AssertionError();
        }
        catch ( IllegalArgumentException e )
        {
            Assert.assertNotNull( e.getMessage() );
            System.out.println( e.toString() );
        }

        try
        {
            this.getMemoryManager().allocateLongs( Integer.MIN_VALUE );
            throw new AssertionError();
        }
        catch ( IllegalArgumentException e )
        {
            Assert.assertNotNull( e.getMessage() );
            System.out.println( e.toString() );
        }

        try
        {
            this.getMemoryManager().allocateShorts( Integer.MIN_VALUE );
            throw new AssertionError();
        }
        catch ( IllegalArgumentException e )
        {
            Assert.assertNotNull( e.getMessage() );
            System.out.println( e.toString() );
        }

    }

    //-------------------------------------------------------------------Tests--
}

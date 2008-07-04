/*
 *  jDTAUS Core Utilities
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
package org.jdtaus.core.io.util.test;

import junit.framework.Assert;
import org.jdtaus.core.io.it.FileOperationsTest;
import org.jdtaus.core.io.util.FlushableFileOperations;
import org.jdtaus.core.io.util.MemoryFileOperations;

/**
 * Testcase for {@code FlushableFileOperations} implementations.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public abstract class FlushableFileOperationsTest extends FileOperationsTest
{
    //--FlushableFileOperationsTest---------------------------------------------

    /** {@code MemoryFileOperations} backing the test instance. */
    private MemoryFileOperations memoryOps;

    /**
     * Gets the {@code MemoryFileOperations} backing the test instance.
     *
     * @return the {@code MemoryFileOperations} backing the test instance.
     */
    public final MemoryFileOperations getMemoryFileOperations()
    {
        if ( this.memoryOps == null )
        {
            this.memoryOps = new MemoryFileOperations();
        }

        return this.memoryOps;
    }

    /**
     * Gets the {@code FlushableFileOperations} implementation tests are
     * performed with.
     *
     * @return the {@code FlushableFileOperations} implementation tests are
     * performed with.
     */
    public final FlushableFileOperations getFlushableFileOperations()
    {
        return ( FlushableFileOperations ) this.getFileOperations();
    }

    //---------------------------------------------FlushableFileOperationsTest--
    //--Tests-------------------------------------------------------------------

    /**
     * Tests the {@link FlushableFileOperations#flush()} method.
     */
    public void testFlush() throws Exception
    {
        assert this.getFlushableFileOperations() != null;

        final FlushableFileOperations ops = this.getFlushableFileOperations();

        ops.write( this.getTestFile() );
        ops.flush();

        final byte[] x = new byte[ 1 ];
        x[0] = "X".getBytes( "US-ASCII" )[0];
        ops.setFilePointer( 0L );
        ops.write( x, 0, 1 );
        ops.setFilePointer( ops.getLength() - 1L );
        ops.write( x, 0, 1 );
        ops.flush();

        final String flushed = new String( this.getMemoryFileOperations().
                                            getData(), "US-ASCII" );

        Assert.assertEquals( "XBCDEFGHIJKLMNOPQRSTUVWXYX", flushed );
        ops.setLength( 0L );
    }

    //-------------------------------------------------------------------Tests--
}

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

import java.io.IOException;
import java.util.Arrays;
import junit.framework.Assert;
import org.jdtaus.core.io.FileOperations;
import org.jdtaus.core.io.util.ReadAheadFileOperations;

/**
 * Testcase for {@code ReadAheadFileOperations} implementations.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class ReadAheadFileOperationsTest extends FlushableFileOperationsTest
{
    //--FileOperationsTest------------------------------------------------------

    public FileOperations getFileOperations()
    {
        try
        {
            return new ReadAheadFileOperations( this.getMemoryFileOperations() );
        }
        catch ( IOException e )
        {
            throw new AssertionError( e );
        }
    }

    //------------------------------------------------------FileOperationsTest--
    //--ReadAheadFileOperationsTest---------------------------------------------

    /**
     * Tests the {@link ReadAheadFileOperations#write(byte[],int,int)} to
     * correctly update the cache.
     */
    public void testWriteUpdatesCache() throws Exception
    {
        final FileOperations ops = this.getFileOperations();
        final byte[] testBuf = new byte[ 101 ];
        final byte[] buf = new byte[ 101 ];

        int totalRead = 0;
        int toRead = 100;
        int read = 0;

        Arrays.fill( testBuf, ( byte ) 100 );
        Arrays.fill( buf, ( byte ) 0 );
        buf[100] = ( byte ) 100;

        ops.setLength( 0L );
        ops.write( testBuf, 0, 100 );

        Assert.assertEquals( 100L, ops.getFilePointer() );

        ops.setFilePointer( 0L );

        do
        {
            read = ops.read( buf, totalRead, toRead );
            assert read != -1;
            totalRead += read;
            toRead -= read;
        }
        while ( totalRead < 100 );

        Assert.assertTrue( Arrays.equals( testBuf, buf ) );

        ops.setFilePointer( 0L );
        ops.write( new byte[] { ( byte ) 1 }, 0, 1 );
        ops.setFilePointer( 100L );
        ops.write( new byte[] { ( byte ) 1 }, 0, 1 );

        testBuf[0] = ( byte ) 1;
        testBuf[100] = ( byte ) 1;

        ops.setFilePointer( 0L );
        Arrays.fill( buf, ( byte ) 0 );

        toRead = buf.length;
        totalRead = 0;

        do
        {
            read = ops.read( buf, totalRead, toRead );
            assert read != -1;
            totalRead += read;
            toRead -= read;
        }
        while ( totalRead < buf.length );

        Assert.assertTrue( Arrays.equals( testBuf, buf ) );
        Assert.assertEquals( -1, ops.read( buf, 0, buf.length ) );
        ops.setLength( 0L );
    }

    //---------------------------------------------ReadAheadFileOperationsTest--
}

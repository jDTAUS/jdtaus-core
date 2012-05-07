/*
 *  jDTAUS Core Utilities
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
package org.jdtaus.core.io.util.test;

import java.io.IOException;
import java.util.Arrays;
import junit.framework.Assert;
import org.jdtaus.core.io.FileOperations;
import org.jdtaus.core.io.util.ReadAheadFileOperations;

/**
 * Testcase for {@code ReadAheadFileOperations} implementations.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class ReadAheadFileOperationsUnevenTest
    extends ReadAheadFileOperationsTest
{
    //--FileOperationsTest------------------------------------------------------

    public FileOperations getFileOperations()
    {
        try
        {
            return new ReadAheadFileOperations(
                this.getMemoryFileOperations(), 3 );

        }
        catch ( IOException e )
        {
            throw new AssertionError( e );
        }
    }

    //------------------------------------------------------FileOperationsTest--
    //--TestCase----------------------------------------------------------------

    protected void runTest() throws Throwable
    {
        super.runTest();
        this.testReadBeyondCacheEof();
        this.testReadBeyondCacheNoEof();
    }

    //----------------------------------------------------------------TestCase--
    //--ReadAheadFileOperationsUnevenTest---------------------------------------

    /**
     * Tests the {@link ReadAheadFileOperations#read(byte[],int,int)} method
     * to correctly read beyond the cache limit when more data to read is
     * requested than available for the file.
     */
    public void testReadBeyondCacheEof() throws Exception
    {
        final FileOperations ops = this.getFileOperations();
        final byte[] buf = new byte[ 100 ];
        final byte[] eofBuf = new byte[ 200 ];

        int toRead = buf.length;
        int totalRead = 0;
        int read = -1;

        Arrays.fill( buf, ( byte ) 100 );
        ops.setLength( 0L );
        ops.setFilePointer( 0L );
        ops.write( buf, 0, buf.length );

        ops.setFilePointer( 0L );

        do
        {
            read = ops.read( buf, totalRead, toRead );
            assert read != -1;
            totalRead += read;
            toRead -= read;
        }
        while ( totalRead < buf.length );

        Assert.assertEquals( buf.length, totalRead );

        ops.setFilePointer( 0L );

        totalRead = 0;
        toRead = eofBuf.length;

        do
        {
            read = ops.read( eofBuf, totalRead, toRead );

            if ( read != -1 )
            {
                totalRead += read;
                toRead -= read;
            }
        }
        while ( totalRead < toRead && read != -1 );

        Assert.assertEquals( 100L, totalRead );
    }

    /**
     * Tests the {@link ReadAheadFileOperations#read(byte[],int,int)} method
     * to correctly read beyond the cache limit when less data to read is
     * requested than available for the file.
     */
    public void testReadBeyondCacheNoEof() throws Exception
    {
        final FileOperations ops = this.getFileOperations();
        final byte[] buf = new byte[ 100 ];
        final byte[] noEofBuf = new byte[ 200 ];
        Arrays.fill( buf, ( byte ) 100 );
        ops.setLength( 0L );
        ops.setFilePointer( 0L );
        ops.write( buf, 0, buf.length );

        int toRead = buf.length;
        int totalRead = 0;
        int read = -1;

        ops.setFilePointer( 0L );
        do
        {
            read = ops.read( buf, totalRead, toRead );
            if ( read != -1 )
            {
                totalRead += read;
                toRead -= read;
            }
        }
        while ( totalRead < buf.length && read != -1 );

        Assert.assertEquals( buf.length, totalRead );
        Assert.assertEquals( -1L, ops.read( buf, 0, buf.length ) );
        ops.setFilePointer( 0L );
        ops.setLength( 10000L );

        totalRead = 0;
        toRead = noEofBuf.length;

        do
        {
            read = ops.read( noEofBuf, totalRead, toRead );
            if ( read != -1 )
            {
                totalRead += read;
                toRead -= read;
            }
        }
        while ( totalRead < noEofBuf.length && read != -1 );

        Assert.assertEquals( noEofBuf.length, totalRead );
    }

    //---------------------------------------ReadAheadFileOperationsUnevenTest--
}

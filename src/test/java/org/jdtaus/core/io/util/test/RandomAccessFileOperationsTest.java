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
package org.jdtaus.core.io.util.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.jdtaus.core.io.FileOperations;
import org.jdtaus.core.io.it.FileOperationsTest;
import org.jdtaus.core.io.util.RandomAccessFileOperations;

/**
 * Testcase for {@code RandomAccessFileOperations} implementations.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class RandomAccessFileOperationsTest extends FileOperationsTest
{
    //--FileOperationsTest------------------------------------------------------

    /** Temporary random access file. */
    private File tmp;

    public FileOperations getFileOperations()
    {
        try
        {
            if ( this.tmp != null )
            {
                this.tmp.delete();
            }

            this.tmp = File.createTempFile( "jdtaus", "tmp" );
            this.tmp.deleteOnExit();
            return new RandomAccessFileOperations(
                new RandomAccessFile( this.tmp, "rw" ) );

        }
        catch ( IOException e )
        {
            throw new AssertionError( e );
        }
    }

    //------------------------------------------------------FileOperationsTest--
    //--RandomAccessFileOperationsTest------------------------------------------

    /**
     * Tests the {@link FileOperations#read(OutputStream}} and
     * {@link FileOperations#write(InputStream)} methods.
     * <p><ol>
     * <li>Writes a testfile from an {@code InputStream} to the file, then reads
     * the file contents into a {@code ByteArrayOutputStream} and checks
     * that the read data matches the written data.</li>
     * </ol></p>
     */
    public void testStreaming() throws Exception
    {
        final File testFile = File.createTempFile( "jdtaus", "tmp" );
        final RandomAccessFileOperations ops = new RandomAccessFileOperations(
            new RandomAccessFile( testFile, "rw" ) );

        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        ops.write( this.getTestFile() );
        ops.read( out );
        out.close();
        this.assertValidTestFile( new String( out.toByteArray(), "UTF-8" ) );
        testFile.delete();
    }

    //------------------------------------------RandomAccessFileOperationsTest--
    //--Object------------------------------------------------------------------

    public void finalize()
    {
        if ( this.tmp != null && this.tmp.exists() )
        {
            this.tmp.delete();
        }
    }

    //------------------------------------------------------------------Object--
}

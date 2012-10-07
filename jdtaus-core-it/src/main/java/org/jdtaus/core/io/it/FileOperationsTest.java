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
package org.jdtaus.core.io.it;

import java.io.InputStream;
import java.util.Arrays;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.jdtaus.core.io.FileOperations;

/**
 * Testcase for {@code FileOperations} implementations.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class FileOperationsTest extends TestCase
{
    //--FileOperationsTest------------------------------------------------------

    /** The implementation to test. */
    private FileOperations fileOperations;

    /** Creates a new {@code FileOperationsTest} instance. */
    public FileOperationsTest()
    {
        super();
    }

    /**
     * Gets the {@code FileOperations} implementation tests are performed with.
     *
     * @return the {@code FileOperations} implementation tests are performed
     * with.
     */
    public FileOperations getFileOperations()
    {
        return this.fileOperations;
    }

    /**
     * Sets the {@code FileOperations} implementation to test.
     *
     * @param value the {@code FileOperations} implementation to test.
     */
    public final void setFileOperations( final FileOperations value )
    {
        this.fileOperations = value;
    }

    /**
     * Gets a testfile resource.
     *
     * @return an {@code InputStream} for reading the testfile content from.
     */
    protected InputStream getTestFile()
    {
        final InputStream ret = Thread.currentThread().getContextClassLoader().
            getResourceAsStream( "org/jdtaus/core/io/it/testfile" );

        Assert.assertNotNull( ret );
        return ret;
    }

    /**
     * Checks that a given {@code String} contains exactly the same contents
     * as the content of {@code getTestFile()}.
     *
     * @param testfile The string to test.
     *
     * @throws RuntimeException if {@code testfile} does not hold the same
     * characters than the contents of the testfile.
     *
     * @see #getTestFile()
     */
    protected void assertValidTestFile( final String testfile )
    {
        Assert.assertEquals( "ABCDEFGHIJKLMNOPQRSTUVWXYZ", testfile );
    }

    //------------------------------------------------------FileOperationsTest--
    //--TestCase----------------------------------------------------------------

    protected void runTest() throws Throwable
    {
        this.testGetLength();
        this.testSetLength();
        this.testSetLengthUpdatesFilePointer();
        this.testWriteBeyondIncreasesLength();
        this.testEndOfFile();
        this.testRead();
        this.testWrite();
    }

    //----------------------------------------------------------------TestCase--
    //--Tests-------------------------------------------------------------------

    /**
     * Tests the {@link FileOperations#getLength()} method.
     * <p><ol>
     * <li>Tests that the length of the provided {@code FileOperations}
     * implementation is {@code 0}.</li></ol></p>
     */
    public void testGetLength() throws Exception
    {
        Assert.assertEquals( 0L, this.getFileOperations().getLength() );
    }

    /**
     * Tests the {@link FileOperations#setLength(long)} method.
     * <p><ol>
     * <li>Sets the length of the provided {@code FileOperations} implementation
     * to {@code 100L} and checks that the corresponding {@code getLength()}
     * method returns {@code 100L} afterwars.</li>
     * <li>Sets the length of the provided {@code FileOperations} implementation
     * to {@code 0L} and checks that the corresponding {@code getLength()}
     * method returns {@code 0L} afterwars.</li>
     * <li>Sets the length to a negative value and checks for an
     * {@code IllegalArgumentException} to be thrown.</li>
     * </ol></p>
     */
    public void testSetLength() throws Exception
    {
        final FileOperations ops = this.getFileOperations();
        ops.setLength( 100L );
        Assert.assertEquals( 100L, ops.getLength() );
        ops.setLength( 0L );
        Assert.assertEquals( 0L, ops.getLength() );

        try
        {
            ops.setLength( Long.MIN_VALUE );
            throw new AssertionError( String.valueOf( Long.MIN_VALUE ) );
        }
        catch ( final IllegalArgumentException e )
        {
            Assert.assertNotNull( e.getMessage() );
            System.out.println( e.toString() );
        }
    }

    /**
     * Tests the {@link FileOperations#setLength(long)} method to correctly
     * position the file pointer on truncating the file.
     */
    public void testSetLengthUpdatesFilePointer() throws Exception
    {
        final FileOperations ops = this.getFileOperations();

        ops.setLength( 10000L );
        ops.setFilePointer( 0L );
        ops.setLength( 100L );
        Assert.assertEquals( 0L, ops.getFilePointer() );
        ops.setFilePointer( 50L );
        ops.setLength( 10L );
        Assert.assertEquals( 10L, ops.getFilePointer() );
        ops.setLength( 0L );
    }

    /**
     * Tests the {@link FileOperations#write(byte[],int,int)} method to
     * correctly increase the length when writing beyond the current length.
     */
    public void testWriteBeyondIncreasesLength() throws Exception
    {
        final FileOperations ops = this.getFileOperations();

        ops.setLength( 0L );
        ops.setFilePointer( 0L );
        ops.write( new byte[] { ( byte ) 1 }, 0, 1 );
        Assert.assertEquals( 1L, ops.getLength() );
        ops.setLength( 0L );

        ops.setLength( 10L );
        ops.setFilePointer( 10L );
        ops.write( new byte[] { ( byte ) 1 }, 0, 1 );
        Assert.assertEquals( 11L, ops.getLength() );
        ops.setLength( 0L );

        ops.setLength( 0L );
        ops.setFilePointer( 0L );
        ops.write( new byte[] {}, 0, 0 );
        Assert.assertEquals( 0L, ops.getLength() );
        ops.setLength( 0L );
        ops.setFilePointer( 0L );

        ops.write( new byte[ 0 ], 0, 0 );
        Assert.assertEquals( 0L, ops.getLength() );
        Assert.assertEquals( 0L, ops.getFilePointer() );

        ops.setLength( 0L );
    }

    /**
     * Tests the {@link FileOperations#read} method.
     * <ol>
     * <li>Sets the length to {@code 1} and the filepointer to {@code 1} and
     * tries to read {@code 1} byte checking for EOF.</li>
     * </ol>
     */
    public void testEndOfFile() throws Exception
    {
        final FileOperations ops = this.getFileOperations();
        ops.setLength( 1L );
        ops.setFilePointer( 1L );

        Assert.assertEquals( 0, ops.read( new byte[ 0 ], 0, 0 ) );
        Assert.assertEquals( FileOperations.EOF,
                             ops.read( new byte[ 1 ], 0, 1 ) );

        ops.setLength( 100000L );
        ops.setFilePointer( 100000L );
        Assert.assertEquals( 0, ops.read( new byte[ 0 ], 0, 0 ) );
        Assert.assertEquals( FileOperations.EOF,
                             ops.read( new byte[ 1 ], 0, 1 ) );

        ops.setLength( 0L );
    }

    /**
     * Tests the {@link FileOperations#read} method.
     * <p><ol>
     * <li>Reads {@code 101} byte from a buffer of {@code 100} byte and checks
     * that the method returns {@code EOF} for end of file.</li>
     * <li>Writes some digits to the file and checks that property
     * {@code length} matches the expected value after writing.</li>
     * <li>Sets the file pointer to the middle of the file and reads more bytes
     * than available in the file checking to get the expected number of read
     * bytes returned from the method.</li>
     * <li>Reads one more byte and checks that the method returns {@code EOF}
     * for end of file.</li>
     * <li>Reads all written data and checks that the read data matches the
     * written data.</li>
     * </ol></p>
     */
    public void testRead() throws Exception
    {
        final FileOperations ops = this.getFileOperations();
        final byte[] digits = new byte[ 100 ]; // Muss durch 2 teilbar sein.
        final byte[] buf = new byte[ digits.length ];
        Arrays.fill( digits, ( byte ) '1' );

        int toRead = 0;
        int totalRead = 0;
        int read = 0;

        // EOF
        Assert.assertEquals( FileOperations.EOF,
                             ops.read( new byte[ 100 ], 0, 100 ) );

        ops.setFilePointer( 0L );
        ops.write( digits, 0, digits.length );
        Assert.assertEquals( digits.length, ops.getLength() );

        // Rest before EOF
        ops.setFilePointer( digits.length / 2 );

        toRead = digits.length;
        do
        {
            read = ops.read( buf, totalRead, toRead );
            if ( read != FileOperations.EOF )
            {
                totalRead += read;
                toRead -= read;
            }
        }
        while ( totalRead < digits.length && read != FileOperations.EOF );

        Assert.assertEquals( digits.length / 2, totalRead );
        Assert.assertEquals( FileOperations.EOF,
                             ops.read( buf, buf.length - 1, 1 ) );

        // Read the written data.
        ops.setFilePointer( 0L );
        toRead = digits.length;
        totalRead = 0;
        do
        {
            read = ops.read( buf, totalRead, toRead );
            if ( read != FileOperations.EOF )
            {
                totalRead += read;
                toRead -= read;
            }
        }
        while ( totalRead < digits.length && read != FileOperations.EOF );

        Assert.assertEquals( digits.length, totalRead );
        Assert.assertTrue( Arrays.equals( digits, buf ) );
        ops.setLength( 0 );
    }

    /**
     * Tests the {@link FileOperations#write} method.
     * <p><ol>
     * <li>Writes some digits to the file and checks that property
     * {@code length} matches the expected value after writing.</li>
     * <li>Sets the file pointer after the end of the file and writes some
     * digits to the file checking that property {@code length} matches the
     * expected value after writing.</li>
     * <li>Reads the written data and checks that the read data matches the
     * written data.</li>
     * </ol></p>
     */
    public void testWrite() throws Exception
    {
        final long off;
        final FileOperations ops = this.getFileOperations();
        final byte[] digits = new byte[ 100 ]; // Muss durch 2 teilbar sein.
        final byte[] buf = new byte[ digits.length ];
        int totalRead = 0;
        int toRead = 0;
        int read = 0;
        Arrays.fill( digits, ( byte ) '1' );

        ops.setFilePointer( 0L );
        ops.write( digits, 0, 100 );
        Assert.assertEquals( digits.length, ops.getLength() );

        off = ops.getLength() * 2;
        ops.setFilePointer( off );
        ops.write( digits, 0, 100 );
        ops.setFilePointer( off );

        toRead = digits.length;
        do
        {
            read = ops.read( buf, totalRead, toRead );
            if ( read != FileOperations.EOF )
            {
                totalRead += read;
                toRead -= read;
            }
        }
        while ( totalRead < digits.length && read != FileOperations.EOF );
        Assert.assertEquals( digits.length, totalRead );
        Assert.assertTrue( Arrays.equals( digits, buf ) );
        ops.setLength( 0 );
    }

    //-------------------------------------------------------------------Tests--
}

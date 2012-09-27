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

import java.util.Arrays;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.jdtaus.core.io.StructuredFile;

/**
 * Testcase for {@code StructuredFile} implementations.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
public abstract class StructuredFileTest extends TestCase
{
    //--Constants---------------------------------------------------------------

    /** Value for property {@code blockSize} the tests are run with. */
    public static final int BLOCK_SIZE = 1;

    /** Number of iterations to test. */
    private static final int ITERATIONS = 20;

    /** Value to initialize new blocks with. */
    private static final byte INIT_CHAR = ( byte ) ' ';

    //---------------------------------------------------------------Constants--
    //--Fields------------------------------------------------------------------

    /** The implementation to test. */
    private StructuredFile structuredFile;

    /** Expected data to read for known written data. */
    private byte[] expectedData;

    //------------------------------------------------------------------Fields--
    //--StructuredFileTest------------------------------------------------------

    /**
     * Reads the contents of the {@code StructuredFile} implementation
     * beeing tested as an array of byte.
     *
     * @return contents of the {@code StructuredFile} implementation beeing
     * tested.
     */
    protected abstract byte[] getStructuredData();

    /**
     * Gets a new instance of the {@code StructuredFile} implementation to test.
     *
     * @return a new instance of the {@code StructuredFile} implementation to
     * test.
     */
    protected abstract StructuredFile getStructuredFile();

    //------------------------------------------------------StructuredFileTest--
    //--Helpermethods-----------------------------------------------------------

    private byte[] getRandomBlock( final int size )
    {
        final byte[] random = new byte[ size ];
        for ( int i = 0; i < random.length; i++ )
        {
            random[i] = ( byte ) ( Math.random() * 0xFF );
        }

        return random;
    }

    private byte[] getFilledBlock( final int size, final byte fill )
    {
        final byte[] filled = new byte[ size ];
        Arrays.fill( filled, fill );
        return filled;
    }

    //-----------------------------------------------------------Helpermethods--
    //--TestCase----------------------------------------------------------------

    protected void setUp() throws Exception
    {
        this.structuredFile = this.getStructuredFile();
        this.expectedData = new byte[ StructuredFileTest.BLOCK_SIZE ];
        Arrays.fill( this.expectedData, StructuredFileTest.INIT_CHAR );
    }

    protected void tearDown() throws Exception
    {
        this.structuredFile = null;
    }

    protected void runTest() throws Throwable
    {
        this.testGetBlockSize();
        this.testGetBlockCount();
        this.testInsertBlocks();
        this.testDeleteBlocks();
        this.testReadWriteBlock();
    }

    //----------------------------------------------------------------TestCase--
    //--Tests-------------------------------------------------------------------

    /**
     * Test of getBlockSize method of class org.jdtaus.common.io.StructuredFile.
     */
    public void testGetBlockSize() throws Exception
    {
        Assert.assertEquals( StructuredFileTest.BLOCK_SIZE,
                             this.structuredFile.getBlockSize() );

    }

    /**
     * Test of getBlockCount method of class org.jdtaus.common.io.StructuredFile.
     */
    public void testGetBlockCount() throws Exception
    {
        Assert.assertEquals( 0L, this.structuredFile.getBlockCount() );
    }

    /**
     * Test of insertBlocks method of class org.jdtaus.common.io.StructuredFile.
     */
    public void testInsertBlocks() throws Exception
    {
        this.structuredFile.insertBlocks( 0L, 1 );
        Assert.assertEquals( 1L, this.structuredFile.getBlockCount() );
    }

    /**
     * Test of deleteBlocks method of class org.jdtaus.common.io.StructuredFile.
     */
    public void testDeleteBlocks() throws Exception
    {
        this.structuredFile.deleteBlocks( 0L,
                                          this.structuredFile.getBlockCount() );

        Assert.assertEquals( 0L, this.structuredFile.getBlockCount() );
    }

    /**
     * Test of writeBlock method of class org.jdtaus.common.io.StructuredFile.
     */
    public void testReadWriteBlock() throws Exception
    {
        final long startTime = System.currentTimeMillis();

        int i;
        int j;
        int written;
        long count;
        long insertIndex;
        long writeIndex;
        long maxIndex;
        long oldBlocks;
        byte[] filled;
        final int blockSize = this.structuredFile.getBlockSize();
        byte[] newExpectedData = {};
        byte[] expectedData = {};
        final byte[] read = new byte[ blockSize ];
        final byte[] write = new byte[ blockSize ];
        final byte[] initBlock = new byte[ blockSize ];

        Arrays.fill( initBlock, StructuredFileTest.INIT_CHAR );

        // Creates blocks filled with random data and checks that written
        // data is read unchanged.
        for ( i = 0, insertIndex = 0L; i <
            StructuredFileTest.ITERATIONS; i++, insertIndex++ )
        {

            count = StructuredFileTest.ITERATIONS - insertIndex;
            maxIndex = insertIndex + count;
            filled =
                this.getFilledBlock( ( int ) count * blockSize, ( byte ) i );

            oldBlocks = this.structuredFile.getBlockCount();
            this.structuredFile.insertBlocks( insertIndex, count );
            Assert.assertEquals( oldBlocks + count,
                                 this.structuredFile.getBlockCount() );

            // Initialize new blocks.
            for ( j = 0; j < count; j++ )
            {
                this.structuredFile.writeBlock( insertIndex + j, 0,
                                                initBlock );

            }
            newExpectedData = new byte[ expectedData.length + filled.length ];
            System.arraycopy( expectedData, 0, newExpectedData, 0,
                              ( int ) ( insertIndex * blockSize ) );

            System.arraycopy( expectedData, ( int ) ( insertIndex * blockSize ),
                              newExpectedData, ( int ) ( maxIndex * blockSize ),
                              ( int ) ( expectedData.length - insertIndex *
                              blockSize ) );

            Arrays.fill( newExpectedData, ( int ) ( insertIndex * blockSize ),
                         ( int ) ( ( insertIndex + count ) * blockSize ),
                         StructuredFileTest.INIT_CHAR );

            expectedData = newExpectedData;
            Assert.assertEquals( true, Arrays.equals(
                                 expectedData, this.getStructuredData() ) );

            maxIndex = insertIndex + count;
            for ( writeIndex = insertIndex, written = 0; writeIndex < maxIndex;
                writeIndex++, written++ )
            {

                System.arraycopy( filled, written * blockSize, write, 0,
                                  blockSize );

                this.structuredFile.writeBlock( writeIndex, 0, write );
                this.structuredFile.readBlock( writeIndex, 0, read );
                Assert.assertEquals( true, Arrays.equals( write, read ) );

                System.arraycopy( write, 0, expectedData,
                                  ( int ) ( writeIndex * blockSize ), blockSize );

            }

            Assert.assertEquals( true, Arrays.equals(
                                 expectedData, this.getStructuredData() ) );

        }

        Assert.assertEquals( true, Arrays.equals(
                             expectedData, this.getStructuredData() ) );

        this.structuredFile.deleteBlocks( 0L,
                                          this.structuredFile.getBlockCount() );

        Assert.assertTrue( this.structuredFile.getBlockCount() == 0L );

    //System.out.println("Ran " + (System.currentTimeMillis() - startTime) +
    //    " ms.");

    }

    //-------------------------------------------------------------------Tests--
}

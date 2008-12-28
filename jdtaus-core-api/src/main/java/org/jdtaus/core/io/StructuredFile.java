/*
 *  jDTAUS Core API
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
package org.jdtaus.core.io;

import java.io.IOException;

/**
 * Block oriented I/O operations.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public interface StructuredFile
{
    //--StructuredFile----------------------------------------------------------

    /**
     * Reads the length of one block in byte.
     *
     * @return length of one block in byte.
     *
     * @throws IOException if getting the block size fails.
     */
    int getBlockSize() throws IOException;

    /**
     * Reads the total amount of existing blocks.
     *
     * @return total amount of existing blocks.
     *
     * @throws IOException if getting the block count fails.
     */
    long getBlockCount() throws IOException;

    /**
     * Inserts new blocks. The content of the new blocks is not further
     * specified. The new index of the block previously at {@code index} will
     * be at {@code index + count}.
     *
     * @param index index of the first new block.
     * @param count number of blocks to insert starting at {@code index}.
     *
     * @throws IndexOutOfBoundsException if {@code index} or {@code count} is
     * negativ or {@code index} is greater than {@code getBlockCount()} or
     * {@code count} is zero or greater than
     * {@code Long.MAX_VALUE - getBlockCount()}.
     * @throws IOException if inserting blocks fails.
     */
    void insertBlocks( long index, long count ) throws IOException;

    /**
     * Removes blocks. The new index of the block previously at
     * {@code index + count} will be at {@code index}.
     *
     * @param index index of the first block to start removing {@code count}
     * blocks.
     * @param count number of blocks to remove.
     *
     * @throws IndexOutOfBoundsException if {@code index} is negative or
     * {@code count} is negative or zero or {@code index} is greater than
     * {@code getBlockCount() - count}.
     * @throws IOException if deleting blocks fails.
     */
    void deleteBlocks( long index, long count ) throws IOException;

    /**
     * Reads {@code buf.length} byte starting at position {@code off} in
     * {@code block}. Same as {@code readBlock(block, off, buf, 0, buf.length)}.
     *
     * @param block index of the block to read data from.
     * @param off starting offset to the data to read from {@code block}.
     * @param buf array to store the data in.
     *
     * @throws NullPointerException if {@code buf} is {@code null}.
     * @throws IndexOutOfBoundsException if {@code block} is negative,
     * greater than or equal to {@code getBlockCount()}, or {@code off} is
     * negative, greater than or equal to {@code getBlockSize()}, or the length
     * of {@code buf} is greater than {@code getBlockSize() - off}.
     * @throws IOException if reading fails.
     */
    void readBlock( long block, int off, byte[] buf ) throws IOException;

    /**
     * Reads {@code length} byte starting at position {@code off} in
     * {@code block} into {@code buf} starting at {@code index} inclusive.
     *
     * @param block index of the block to read data from.
     * @param off starting offset of the data to read from {@code block}.
     * @param buf array to store the data in.
     * @param index offset to start writing data into {@code buf}.
     * @param length number of byte to read.
     *
     * @throws NullPointerException if {@code buf} is {@code null}.
     * @throws IndexOutOfBoundsException if {@code block} is negative,
     * greater than or equal to {@code getBlockCount()}, or {@code off} is
     * negative, greater than or equal to {@code getBlockSize()}, or
     * {@code index} is negative, greater than or equal to the length of
     * {@code buf}, or {@code length} is negative or greater than the
     * length of {@code buf} minus {@code index} or greater than
     * {@code getBlockSize() - off}.
     * @throws IOException if reading fails.
     */
    void readBlock( long block, int off, byte[] buf, int index,
                    int length ) throws IOException;

    /**
     * Writes {@code buf.length} byte from {@code buf} into {@code block}
     * starting at {@code off} inclusive. Same as
     * {@code writeBlock(block, off, buf, 0, buf.length)}.
     *
     * @param block index of the block to write into.
     * @param off inclusive offset to start writing into {@code block}.
     * @param buf data to write into {@code block} beginning at {@code offset}.
     *
     * @throws NullPointerException if {@code buf} is {@code null}.
     * @throws IndexOutOfBoundsException if {@code block} is negative,
     * greater than or equal to {@code getBlockCount()}, or {@code off} is
     * greater than or equal to {@code getBlockSize()}, or the length of
     * {@code buf} is greater than {@code getBlockSize() - off}.
     * @throws IOException if writing fails.
     */
    void writeBlock( long block, int off, byte[] buf ) throws IOException;

    /**
     * Writes {@code length} byte from {@code buf} starting at {@code index}
     * inclusive into {@code block} starting at {@code off} inclusive.
     *
     * @param block index of the block to write into.
     * @param off inclusive offset to start writing into {@code block}.
     * @param buf data to write into {@code block} beginning at {@code offset}.
     * @param index inclusive offset to start reading data from {@code buf}.
     * @param length number of byte to read from {@code buf} starting at
     * {@code index}.
     *
     * @throws NullPointerException if {@code buf} is {@code null}.
     * @throws IndexOutOfBoundsException if {@code block} is negative,
     * greater than or equal to {@code getBlockCount()}, or {@code off} is
     * negative, greater than or equal to {@code getBlockSize()}, or
     * {@code index} is negative, greater than or equal to the length of
     * {@code buf}, or {@code length} is negative or greater than the
     * length of {@code buf} minus {@code index} or greater than
     * {@code getBlockSize() - off}.
     * @throws IOException if writing fails.
     */
    void writeBlock( long block, int off, byte[] buf, int index,
                     int length ) throws IOException;

    /**
     * Releases any system resources associated with an instance. A closed
     * instance cannot perform input or output operations and cannot be
     * reopened.
     *
     * @throws IOException if releasing system resources fails.
     */
    void close() throws IOException;

    /**
     * Adds a {@code StructuredFileListener} to the listener list.
     *
     * @param listener The listener to be added to the listener list.
     *
     * @throws NullPointerException if {@code listener} is {@code null}.
     */
    void addStructuredFileListener( StructuredFileListener listener );

    /**
     * Removes a {@code StructuredFileListener} from the listener list.
     *
     * @param listener The listener to be removed from the listener list.
     *
     * @throws NullPointerException if {@code listener} is {@code null}.
     */
    void removeStructuredFileListener( StructuredFileListener listener );

    /**
     * Gets all currently registered {@code StructuredFileListener}s.
     *
     * @return all currently registered {@code StructuredFileListener}s.
     */
    StructuredFileListener[] getStructuredFileListeners();

    //----------------------------------------------------------StructuredFile--
}

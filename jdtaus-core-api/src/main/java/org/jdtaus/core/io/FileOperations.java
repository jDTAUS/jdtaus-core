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
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Elementary I/O operations.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public interface FileOperations
{
    //--FileOperations----------------------------------------------------------

    /**
     * Constant returned by the {@link #read(byte[],int,int)} method if the
     * end of the file has been reached.
     */
    final int EOF = -1;

    /**
     * Gets the length of the file.
     *
     * @return the length measured in bytes.
     *
     * @throws IOException if getting the length fails.
     */
    long getLength() throws IOException;

    /**
     * Sets the length of the file.
     * <p>If the present length of the file as returned by the
     * {@code getLength()} method is greater than the {@code newLength} argument
     * then the file will be truncated. In this case, if the file offset as
     * returned by the {@code getFilePointer()} method is greater than
     * {@code newLength} then after this method returns the offset will be equal
     * to {@code newLength}.</p>
     * <p>If the present length of the file as returned by the
     * {@code getLength()} method is smaller than the {@code newLength} argument
     * then the file will be extended. In this case, the contents of the
     * extended portion of the file are not defined.</p>
     *
     * @param newLength The desired length of the file.
     *
     * @throws IllegalArgumentException if {@code newLength} is negative.
     * @throws IOException if setting the length fails.
     */
    void setLength( long newLength ) throws IOException;

    /**
     * Returns the current offset in the file.
     *
     * @return the offset from the beginning of the file, in bytes, at which the
     * next read or write occurs.
     *
     * @throws IOException if getting the current offset in the file fails.
     */
    long getFilePointer() throws IOException;

    /**
     * Sets the file-pointer offset, measured from the beginning of the file,
     * at which the next read or write occurs. The offset may be set beyond the
     * end of the file. Setting the offset beyond the end of the file does not
     * change the file length. The file length will change only by writing after
     * the offset has been set beyond the end of the file.
     *
     * @param pos the offset position, measured in bytes from the beginning of
     * the file, at which to set the file pointer.
     *
     * @throws IllegalArgumentException if {@code pos} is negative.
     * @throws IOException if setting the file-pointer offset fails.
     */
    void setFilePointer( long pos ) throws IOException;

    /**
     * Reads up to {@code len} bytes of data from the file into an array of
     * bytes. An attempt is made to read as many as {@code len} bytes, but a
     * smaller number may be read.
     *
     * @param buf the buffer into which the data is read.
     * @param off the start offset of the data.
     * @param len the maximum number of bytes read.
     *
     * @return the total number of bytes read into the buffer, or {@link #EOF}
     * if there is no more data because the end of the file has been reached.
     *
     * @throws NullPointerException if {@code buf} is {@code null}.
     * @throws IndexOutOfBoundsException if either {@code off} or {@code len}
     * is negative or {@code off + len} is greater than the length of
     * {@code buf}.
     * @throws IOException if reading fails.
     */
    int read( byte[] buf, int off, int len ) throws IOException;

    /**
     * Writes {@code len} bytes from the specified byte array starting at
     * offset {@code off} to the file.
     *
     * @param buf the data.
     * @param off the start offset in the data.
     * @param len the number of bytes to write.
     *
     * @throws IndexOutOfBoundsException if either {@code off} or {@code len}
     * is negative or {@code off + len} is greater than the length of
     * {@code buf}.
     * @throws IOException if writing fails.
     */
    void write( byte[] buf, int off, int len ) throws IOException;

    /**
     * Reads all data from the file to some {@code OutputStream}. The stream
     * will not get closed after all data has been written.
     *
     * @param out an output stream to stream to.
     *
     * @throws NullPointerException if {@code out} is {@code null}.
     * @throws IOException if reading fails.
     */
    void read( OutputStream out ) throws IOException;

    /**
     * Writes all data available from some {@code InputStream}. The stream will
     * not get closed after all data has been read.
     *
     * @param in an input stream providing the data to be written.
     *
     * @throws NullPointerException if {@code in} is {@code null}.
     * @throws IOException if writing fails.
     */
    void write( InputStream in ) throws IOException;

    /**
     * Releases any system resources associated with an instance. A closed
     * instance cannot perform input or output operations and cannot be
     * reopened.
     *
     * @throws IOException if releasing system resources fails.
     */
    void close() throws IOException;

    //----------------------------------------------------------FileOperations--
}

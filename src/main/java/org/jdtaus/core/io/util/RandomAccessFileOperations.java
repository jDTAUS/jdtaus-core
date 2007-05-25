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
package org.jdtaus.core.io.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import org.jdtaus.core.container.Implementation;
import org.jdtaus.core.container.ModelFactory;
import org.jdtaus.core.container.Properties;
import org.jdtaus.core.container.Property;
import org.jdtaus.core.container.PropertyException;
import org.jdtaus.core.io.FileOperations;

/**
 * Adapts a {@link java.io.RandomAccessFile} to {@code FileOperations}.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public final class RandomAccessFileOperations implements FileOperations
{
    //--Implementation----------------------------------------------------------

    // This section is managed by jdtaus-container-mojo.

    /** Meta-data describing the implementation. */
    private static final Implementation META =
        ModelFactory.getModel().getModules().
        getImplementation(RandomAccessFileOperations.class.getName());

    //----------------------------------------------------------Implementation--
    //--Constructors------------------------------------------------------------

    // This section is managed by jdtaus-container-mojo.

    /**
     * Initializes the properties of the instance.
     *
     * @param meta the property values to initialize the instance with.
     *
     * @throws NullPointerException if {@code meta} is {@code null}.
     */
    protected void initializeProperties(final Properties meta)
    {
        Property p;

        if(meta == null)
        {
            throw new NullPointerException("meta");
        }

        p = meta.getProperty("bufferSize");
        this._bufferSize = ((java.lang.Integer) p.getValue()).intValue();

    }

    //------------------------------------------------------------Constructors--
    //--Properties--------------------------------------------------------------

    // This section is managed by jdtaus-container-mojo.

    /**
     * Property {@code bufferSize}.
     * @serial
     */
    private int _bufferSize;

    /**
     * Gets the value of property <code>bufferSize</code>.
     *
     * @return the value of property <code>bufferSize</code>.
     */
    protected int getBufferSize()
    {
        return this._bufferSize;
    }


    //--------------------------------------------------------------Properties--
    //--FileOperations----------------------------------------------------------

    /** Cache for the value of property {@code length}. */
    private transient long cachedLength = -1L;

    public long getLength() throws IOException
    {
        return this.cachedLength >= 0L ?
            this.cachedLength : (this.cachedLength =
            this.getRandomAccessFile().length());

    }

    public void setLength(long newLength) throws IOException
    {
        if(newLength < 0L)
        {
            throw new IllegalArgumentException(Long.toString(newLength));
        }

        this.getRandomAccessFile().setLength(newLength);
        this.cachedLength = newLength;
    }

    public long getFilePointer() throws IOException
    {
        return this.getRandomAccessFile().getFilePointer();
    }

    public void setFilePointer(long pos)  throws IOException
    {
        this.getRandomAccessFile().seek(pos);
    }

    public void write(byte[] buf, int off, int len)  throws IOException
    {
        final RandomAccessFile file = this.getRandomAccessFile();
        final long pointer = file.getFilePointer();

        file.write(buf, off, len);

        if(this.cachedLength >= 0L && pointer + len > this.cachedLength)
        {
            this.cachedLength = file.length();
        }
    }


    public int read(byte[] buf, int off, int len) throws IOException
    {
        return this.getRandomAccessFile().read(buf, off, len);
    }

    public void read(final OutputStream out) throws IOException
    {
        if(out == null)
        {
            throw new NullPointerException("out");
        }

        int read;
        long toRead = this.getLength();
        final byte[] buf = this.getDefaultBuffer();

        if(toRead > 0L)
        {
            this.setFilePointer(0L);
            do
            {
                read = this.read(buf, 0 , buf.length);
                toRead -= read;
                out.write(buf, 0, read);
            } while(toRead > 0L);
        }
    }

    public void write(final InputStream in) throws IOException
    {
        if(in == null)
        {
            throw new NullPointerException("in");
        }

        int read;
        final byte[] buf = this.getDefaultBuffer();

        while((read = in.read(buf, 0, buf.length)) != - 1)
        {
            this.write(buf, 0, read);
        }
    }

    //----------------------------------------------------------FileOperations--
    //--RandomAccessFileOperations----------------------------------------------

    /**
     * Default temporary buffer.
     * @serial
     */
    private byte[] defaultBuffer;

    /** {@code RandomAccessFile} requirement. */
    private transient RandomAccessFile randomAccessFile;

    /**
     * Creates a new {@code RandomAccessFileOperations} instance adapting
     * {@code file}.
     *
     * @param file an {@code RandomAccessFile} instance to use as a
     * {@code FileOperations} implementation.
     *
     * @throws NullPointerException if {@code file} is {@code null}.
     */
    public RandomAccessFileOperations(final RandomAccessFile file)
    {
        super();

        if(file == null)
        {
            throw new NullPointerException("file");
        }

        this.initializeProperties(META.getProperties());
        this.assertValidProperties();

        this.randomAccessFile = file;
    }

    /**
     * Gets the random access file operations are performed with.
     *
     * @return the {@code RandomAccessFile} instance operations are performed
     * with or {@code null}.
     */
    public RandomAccessFile getRandomAccessFile()
    {
        return this.randomAccessFile;
    }

    /**
     * Checks configured properties.
     *
     * @throws PropertyException if property {@code bufferSize} is negative or
     * {@code 0}.
     */
    private void assertValidProperties()
    {
        if(this.getBufferSize() <= 0)
        {
            throw new PropertyException("bufferSize",
                new Integer(this.getBufferSize()));

        }
    }

    /**
     * Getter for property {@code defaultBuffer}.
     *
     * @return a buffer for operations which need temporary memory.
     */
    private byte[] getDefaultBuffer()
    {
        if(this.defaultBuffer == null)
        {
            this.defaultBuffer = new byte[this.getBufferSize()];
        }

        return this.defaultBuffer;
    }

    //----------------------------------------------RandomAccessFileOperations--
}

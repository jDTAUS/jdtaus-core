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
package org.jdtaus.core.io.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Arrays;
import org.jdtaus.core.container.ContainerFactory;
import org.jdtaus.core.io.FileOperations;
import org.jdtaus.core.lang.spi.MemoryManager;
import org.jdtaus.core.logging.spi.Logger;

/**
 * Implementation of elementary I/O operations in heap memory.
 * <p>This implementation performs I/O in memory. The value of property
 * {@code length} is limited to a maximum of {@code Integer.MAX_VALUE} (4 GB).
 * </p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public final class MemoryFileOperations
    implements FileOperations, Serializable, Cloneable
{
    //--Fields------------------------------------------------------------------

    /**
     * Data to operate on.
     * @serial
     */
    private byte[] data;

    /**
     * FilePointer.
     * @serial
     */
    private long filePointer;

    /**
     * Actual length.
     * @serial
     */
    private int length;

    /**
     * Default temporary buffer.
     * @serial
     */
    private byte[] defaultBuffer;

    //------------------------------------------------------------------Fields--
    //--Properties--------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausProperties
    // This section is managed by jdtaus-container-mojo.

    /**
     * Gets the value of property <code>streamBufferSize</code>.
     *
     * @return Size of the buffer for buffering streams.
     */
    private int getStreamBufferSize()
    {
        return ( (java.lang.Integer) ContainerFactory.getContainer().
            getProperty( this, "streamBufferSize" ) ).intValue();

    }

// </editor-fold>//GEN-END:jdtausProperties

    //--------------------------------------------------------------Properties--
    //--Dependencies------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausDependencies
    // This section is managed by jdtaus-container-mojo.

    /**
     * Gets the configured <code>Logger</code> implementation.
     *
     * @return the configured <code>Logger</code> implementation.
     */
    private Logger getLogger()
    {
        return (Logger) ContainerFactory.getContainer().
            getDependency( this, "Logger" );

    }

    /**
     * Gets the configured <code>MemoryManager</code> implementation.
     *
     * @return the configured <code>MemoryManager</code> implementation.
     */
    private MemoryManager getMemoryManager()
    {
        return (MemoryManager) ContainerFactory.getContainer().
            getDependency( this, "MemoryManager" );

    }

// </editor-fold>//GEN-END:jdtausDependencies

    //------------------------------------------------------------Dependencies--
    //--FileOperations----------------------------------------------------------

    public long getLength()
    {
        return this.length;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if {@code newLength} is negative or
     * greater than {@code Integer.MAX_VALUE}.
     */
    public void setLength( final long newLength )
    {
        if ( newLength < 0L || newLength > Integer.MAX_VALUE )
        {
            throw new IllegalArgumentException( Long.toString( newLength ) );
        }

        this.ensureCapacity( (int) newLength );
        this.length = (int) newLength;
        if ( this.filePointer > this.length )
        {
            this.filePointer = this.length;
        }
    }

    public long getFilePointer()
    {
        return this.filePointer;
    }

    public void setFilePointer( final long pos )
    {
        // Preconditions.
        if ( pos < 0L || pos > Integer.MAX_VALUE )
        {
            throw new IllegalArgumentException( Long.toString( pos ) );
        }

        this.filePointer = pos;
    }

    public int read( final byte[] buf, final int off, final int len )
    {
        final int ret;

        // Preconditions.
        if ( buf == null )
        {
            throw new NullPointerException( "buf" );
        }
        if ( off < 0 )
        {
            throw new ArrayIndexOutOfBoundsException( off );
        }
        if ( len < 0 )
        {
            throw new ArrayIndexOutOfBoundsException( len );
        }
        if ( off + len > buf.length )
        {
            throw new ArrayIndexOutOfBoundsException( off + len );
        }
        if ( this.filePointer + len > Integer.MAX_VALUE )
        {
            throw new ArrayIndexOutOfBoundsException( Integer.MAX_VALUE );
        }

        if ( len == 0 )
        {
            ret = 0;
        }
        else if ( this.filePointer >= this.length )
        {
            // EOF
            ret = FileOperations.EOF;
        }
        else if ( this.filePointer + len > this.length )
        {
            // less than len byte before EOF
            final int remaining = (int) ( this.length - this.filePointer );
            System.arraycopy( this.data, (int) this.filePointer, buf, off,
                              remaining );

            this.filePointer += remaining;
            ret = remaining;
        }
        else
        {
            // copy len byte into buf.
            System.arraycopy(
                this.data, (int) this.filePointer, buf, off, len );

            this.filePointer += len;
            ret = len;
        }

        return ret;
    }

    public void write( final byte[] buf, final int off, final int len )
    {
        // Preconditions.
        if ( buf == null )
        {
            throw new NullPointerException( "buf" );
        }
        if ( off < 0 )
        {
            throw new ArrayIndexOutOfBoundsException( off );
        }
        if ( len < 0 )
        {
            throw new ArrayIndexOutOfBoundsException( len );
        }
        if ( off + len > buf.length )
        {
            throw new ArrayIndexOutOfBoundsException( off + len );
        }

        final long newLen = this.filePointer + len;
        if ( newLen > Integer.MAX_VALUE )
        {
            throw new ArrayIndexOutOfBoundsException( Integer.MAX_VALUE );
        }

        if ( newLen > this.length )
        {
            this.setLength( newLen );
        }

        System.arraycopy( buf, off, this.data, (int) this.filePointer, len );
        this.filePointer += len;
    }

    public void read( final OutputStream out ) throws IOException
    {
        if ( out == null )
        {
            throw new NullPointerException( "out" );
        }

        out.write( this.data, 0, this.length );
        this.filePointer = this.length;
    }

    public void write( final InputStream in ) throws IOException
    {
        if ( in == null )
        {
            throw new NullPointerException( "in" );
        }

        int read;
        final byte[] buf = this.getStreamBuffer();

        while ( ( read = in.read( buf, 0, buf.length ) ) != FileOperations.EOF )
        {
            this.write( buf, 0, read );
        }
    }

    /**
     * {@inheritDoc}
     * <p>Since this implementation does not use any system resources to
     * release, this method does nothing. In contrast to other
     * {@code FileOperations} implementations the instance can still be used
     * after calling this method. It would be a mistake to write an application
     * which relies on this behaviour, however.</p>
     */
    public void close()
    {
    }

    //----------------------------------------------------------FileOperations--
    //--MemoryFileOperations----------------------------------------------------

    /** Creates a new {@code MemoryFileOperations} instance of no length. */
    public MemoryFileOperations()
    {
        this.filePointer = 0L;
        this.data = new byte[ 0 ];
        this.length = 0;
    }

    /**
     * Creates a new {@code MemoryFileOperations} instance of no length
     * taking an initial capacity.
     *
     * @param initialCapacity the number of bytes to pre-allocate when
     * creating the new instance.
     *
     * @throws OutOfMemoryError if not enough memory is available to create a
     * buffer with a length of {@code initialCapacity}.
     *
     * @see #ensureCapacity(int)
     */
    public MemoryFileOperations( final int initialCapacity )
    {
        this.filePointer = 0L;
        this.length = 0;
        this.data = this.getMemoryManager().allocateBytes( initialCapacity );
    }

    /**
     * Creates a new {@code MemoryFileOperations} instance holding {@code buf}.
     *
     * @param buf bytes to initialize the instance with.
     *
     * @throws NullPointerException if {@code buf} is {@code null}.
     */
    public MemoryFileOperations( final byte[] buf )
    {
        this();

        if ( buf == null )
        {
            throw new NullPointerException( "buf" );
        }

        this.data = buf;
        this.length = buf.length;
        this.filePointer = 0L;
    }

    /**
     * Gets the capacity of the instance.
     *
     * @return the capacity of the instance in byte.
     */
    public int getCapacity()
    {
        return this.data.length;
    }

    /**
     * Increases the capacity of the instance, if necessary, to ensure that it
     * can hold at least the number of bytes specified by the minimum capacity
     * argument.
     *
     * @param minimumCapacity the minimum capacity to ensure.
     */
    public void ensureCapacity( final int minimumCapacity )
    {
        final int oldLength = this.data.length;

        while ( this.data.length < minimumCapacity )
        {
            final byte[] newData = this.getMemoryManager().allocateBytes(
                this.data.length * 2 >= minimumCapacity
                ? this.data.length * 2
                : minimumCapacity );

            Arrays.fill( newData, (byte) 0 );
            System.arraycopy( this.data, 0, newData, 0, this.data.length );
            this.data = newData;
        }

        if ( oldLength != this.data.length &&
            this.getLogger().isDebugEnabled() )
        {
            this.getLogger().debug(
                this.getLogResizeMessage( new Long( this.data.length ) ) );

        }
    }

    /**
     * Gets the file contents.
     *
     * @return the file contents of the instance.
     *
     * @throws OutOfMemoryError if not enough memory is available.
     */
    public byte[] getData()
    {
        final int len = (int) this.getLength();
        final byte[] ret = this.getMemoryManager().allocateBytes( len );

        System.arraycopy( this.data, 0, ret, 0, len );

        return ret;
    }

    /**
     * Gets a buffer for buffering streams.
     *
     * @return a buffer for buffering streams.
     */
    private byte[] getStreamBuffer()
    {
        if ( this.defaultBuffer == null )
        {
            this.defaultBuffer = this.getMemoryManager().
                allocateBytes( this.getStreamBufferSize() < 0
                               ? 0
                               : this.getStreamBufferSize() );

        }

        return this.defaultBuffer;
    }

    //----------------------------------------------------MemoryFileOperations--
    //--Object------------------------------------------------------------------

    /**
     * Indicates whether some other object is equal to this one by comparing
     * the contents from memory.
     *
     * @param o the reference object with which to compare.
     *
     * @return {@code true} if this object is the same as {@code o};
     * {@code false} otherwise.
     * @deprecated Replaced by {@code Arrays.equals( getData(), ( (MemoryFileOperations) o ).getData() )}
     */
    public boolean equals( final Object o )
    {
        return super.equals( o );
    }

    /**
     * Returns a hash code value for this object.
     *
     * @return a hash code value for this object.
     * @deprecated Replaced by {@code getData().hashCode()}
     */
    public int hashCode()
    {
        return super.hashCode();
    }

    /**
     * Creates and returns a deep copy of this object.
     *
     * @return a clone of this instance.
     */
    public Object clone()
    {
        try
        {
            return super.clone();
        }
        catch ( CloneNotSupportedException e )
        {
            throw new AssertionError( e );
        }
    }

    //------------------------------------------------------------------Object--
    //--Messages----------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausMessages
    // This section is managed by jdtaus-container-mojo.

    /**
     * Gets the text of message <code>logResize</code>.
     * <blockquote><pre>aktuelle Puffergröße: {0, number} Byte</pre></blockquote>
     * <blockquote><pre>current buffer size: {0, number} byte</pre></blockquote>
     *
     * @param bufferSize The current size of the internal buffer.
     *
     * @return Information about the size of the internal buffer.
     */
    private String getLogResizeMessage(
            java.lang.Number bufferSize )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "logResize",
                new Object[]
                {
                    bufferSize
                });

    }

// </editor-fold>//GEN-END:jdtausMessages

    //----------------------------------------------------------------Messages--
}

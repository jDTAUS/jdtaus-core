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
package org.jdtaus.core.io.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Locale;
import org.jdtaus.core.container.ContainerFactory;
import org.jdtaus.core.io.FileOperations;
import org.jdtaus.core.lang.spi.MemoryManager;

/**
 * Adapts a {@link java.io.RandomAccessFile} to {@code FileOperations}.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $JDTAUS$
 */
public final class RandomAccessFileOperations implements FileOperations
{
    //--Dependencies------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausDependencies
    // This section is managed by jdtaus-container-mojo.

    /**
     * Gets the configured <code>MemoryManager</code> implementation.
     *
     * @return The configured <code>MemoryManager</code> implementation.
     */
    private MemoryManager getMemoryManager()
    {
        return (MemoryManager) ContainerFactory.getContainer().
            getDependency( this, "MemoryManager" );

    }

    /**
     * Gets the configured <code>Locale</code> implementation.
     *
     * @return The configured <code>Locale</code> implementation.
     */
    private Locale getLocale()
    {
        return (Locale) ContainerFactory.getContainer().
            getDependency( this, "Locale" );

    }

// </editor-fold>//GEN-END:jdtausDependencies

    //------------------------------------------------------------Dependencies--
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
    //--FileOperations----------------------------------------------------------

    /** Cache for the value of property {@code length}. */
    private transient long cachedLength = NO_CACHEDLENGTH;

    private static final long NO_CACHEDLENGTH = Long.MIN_VALUE;

    public long getLength() throws IOException
    {
        this.assertNotClosed();

        return this.cachedLength != NO_CACHEDLENGTH
               ? this.cachedLength
               : ( this.cachedLength =
            this.getRandomAccessFile().length() );

    }

    public void setLength( long newLength ) throws IOException
    {
        if ( newLength < 0L )
        {
            throw new IllegalArgumentException( Long.toString( newLength ) );
        }

        this.assertNotClosed();

        this.getRandomAccessFile().setLength( newLength );
        this.cachedLength = newLength;
    }

    public long getFilePointer() throws IOException
    {
        this.assertNotClosed();

        return this.getRandomAccessFile().getFilePointer();
    }

    public void setFilePointer( long pos ) throws IOException
    {
        this.assertNotClosed();

        this.getRandomAccessFile().seek( pos );
    }

    public void write( byte[] buf, int off, int len ) throws IOException
    {
        this.assertNotClosed();

        final RandomAccessFile file = this.getRandomAccessFile();
        final long pointer = file.getFilePointer();

        file.write( buf, off, len );

        if ( this.cachedLength != NO_CACHEDLENGTH &&
             pointer + len > this.cachedLength )
        {
            this.cachedLength = file.length();
        }
    }

    public int read( byte[] buf, int off, int len ) throws IOException
    {
        this.assertNotClosed();

        return this.getRandomAccessFile().read( buf, off, len );
    }

    public void read( final OutputStream out ) throws IOException
    {
        if ( out == null )
        {
            throw new NullPointerException( "out" );
        }

        this.assertNotClosed();

        int read;
        long toRead = this.getLength();
        final byte[] buf = this.getStreamBuffer();

        if ( toRead > 0L )
        {
            this.setFilePointer( 0L );
            do
            {
                read = this.read( buf, 0, buf.length );

                assert read != FileOperations.EOF : "Unexpected end of file.";

                toRead -= read;
                out.write( buf, 0, read );
            }
            while ( toRead > 0L );
        }
    }

    public void write( final InputStream in ) throws IOException
    {
        if ( in == null )
        {
            throw new NullPointerException( "in" );
        }

        this.assertNotClosed();

        int read;
        final byte[] buf = this.getStreamBuffer();

        while ( ( read = in.read( buf, 0, buf.length ) ) != FileOperations.EOF )
        {
            this.write( buf, 0, read );
        }
    }

    /**
     * {@inheritDoc}
     * Closes the {@code RandomAccessFile} backing the instance.
     *
     * @throws IOException if closing the {@code RandomAccessFile} backing the
     * instance fails.
     */
    public void close() throws IOException
    {
        this.assertNotClosed();

        this.getRandomAccessFile().close();
        this.closed = true;
    }

    //----------------------------------------------------------FileOperations--
    //--RandomAccessFileOperations----------------------------------------------

    /** Stream buffer. */
    private byte[] streamBuffer;

    /** Flags the instance as beeing closed. */
    private boolean closed;

    /** {@code RandomAccessFile} requirement. */
    private RandomAccessFile randomAccessFile;

    /**
     * Creates a new {@code RandomAccessFileOperations} instance adapting
     * {@code file}.
     *
     * @param file an {@code RandomAccessFile} instance to use as a
     * {@code FileOperations} implementation.
     *
     * @throws NullPointerException if {@code file} is {@code null}.
     */
    public RandomAccessFileOperations( final RandomAccessFile file )
    {
        super();

        if ( file == null )
        {
            throw new NullPointerException( "file" );
        }

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
     * Checks that the instance is not closed.
     *
     * @throws IOException if the instance is closed.
     */
    private void assertNotClosed() throws IOException
    {
        if ( this.closed )
        {
            throw new IOException( this.getAlreadyClosedMessage(
                this.getLocale() ) );

        }
    }

    /**
     * Gets a buffer for buffering streams.
     *
     * @return a buffer for buffering streams.
     */
    private byte[] getStreamBuffer()
    {
        if ( this.streamBuffer == null )
        {
            this.streamBuffer = this.getMemoryManager().
                allocateBytes( this.getStreamBufferSize() < 0
                               ? 0
                               : this.getStreamBufferSize() );

        }

        return this.streamBuffer;
    }

    //----------------------------------------------RandomAccessFileOperations--
    //--Messages----------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausMessages
    // This section is managed by jdtaus-container-mojo.

    /**
     * Gets the text of message <code>alreadyClosed</code>.
     * <blockquote><pre>Instanz geschlossen - keine E/A-Operationen möglich.</pre></blockquote>
     * <blockquote><pre>Instance closed - cannot perform I/O.</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     *
     * @return Message stating that an instance is already closed.
     */
    private String getAlreadyClosedMessage( final Locale locale )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "alreadyClosed", locale, null );

    }

// </editor-fold>//GEN-END:jdtausMessages

    //----------------------------------------------------------------Messages--
}

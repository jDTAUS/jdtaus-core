/*
 *  jDTAUS Core Utilities
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
package org.jdtaus.core.io.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import org.jdtaus.core.container.ContainerFactory;
import org.jdtaus.core.io.FileOperations;
import org.jdtaus.core.lang.spi.MemoryManager;

/**
 * Read-ahead {@code FileOperations} cache.
 * <p>This implementation implements a read-ahead cache for
 * {@code FileOperations} implementations. The cache is controlled by
 * configuration property {@code cacheSize} holding the number of bytes
 * to read-ahead. By default property {@code cacheSize} is initialized to
 * {@code 16384} leading to a cache size of 16 kB. All memory is allocated
 * during instantiation so that an {@code OutOfMemoryError} may be thrown
 * when constructing the cache but not when working with the instance.</p>
 *
 * <p><b>Note:</b><br>
 * This implementation is not thread-safe and concurrent changes to the
 * underlying {@code FileOperations} implementation are not supported.</p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
public final class ReadAheadFileOperations implements FlushableFileOperations
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
     * Gets the value of property <code>defaultCacheSize</code>.
     *
     * @return Default cache size in byte.
     */
    private java.lang.Integer getDefaultCacheSize()
    {
        return (java.lang.Integer) ContainerFactory.getContainer().
            getProperty( this, "defaultCacheSize" );

    }

// </editor-fold>//GEN-END:jdtausProperties

    //--------------------------------------------------------------Properties--
    //--FileOperations----------------------------------------------------------

    public long getLength() throws IOException
    {
        this.assertNotClosed();

        return this.fileOperations.getLength();
    }

    public void setLength( final long newLength ) throws IOException
    {
        this.assertNotClosed();

        final long oldLength = this.getLength();
        this.fileOperations.setLength( newLength );
        if ( this.filePointer > newLength )
        {
            this.filePointer = newLength;
        }

        if ( oldLength > newLength && this.cachePosition != NO_CACHEPOSITION &&
             this.cachePosition + this.cacheLength >= newLength )
        { // Discard the end of file cache.
            this.cachePosition = NO_CACHEPOSITION;
        }
    }

    public long getFilePointer() throws IOException
    {
        this.assertNotClosed();

        return this.filePointer;
    }

    public void setFilePointer( final long pos ) throws IOException
    {
        this.assertNotClosed();

        this.filePointer = pos;
    }

    public int read( final byte[] buf, int off, int len )
        throws IOException
    {
        if ( buf == null )
        {
            throw new NullPointerException( "buf" );
        }
        if ( off < 0 )
        {
            throw new IndexOutOfBoundsException( Integer.toString( off ) );
        }
        if ( len < 0 )
        {
            throw new IndexOutOfBoundsException( Integer.toString( len ) );
        }
        if ( off + len > buf.length )
        {
            throw new IndexOutOfBoundsException( Integer.toString( off + len ) );
        }

        this.assertNotClosed();

        int read = FileOperations.EOF;

        final long fileLength = this.getLength();

        if ( len == 0 )
        {
            read = 0;
        }
        else if ( this.filePointer < fileLength )
        {
            if ( this.cachePosition == NO_CACHEPOSITION ||
                 !( this.filePointer >= this.cachePosition &&
                    this.filePointer < this.cachePosition + this.cacheLength ) )
            { // Cache not initialized or file pointer outside the cached area.
                this.fillCache();
            }

            final long cacheStart = this.filePointer - this.cachePosition;

            assert cacheStart <= Integer.MAX_VALUE :
                "Unexpected implementation limit reached.";

            final int cachedLength = len > this.cacheLength -
                                           (int) cacheStart
                                     ? this.cacheLength - (int) cacheStart
                                     : len;

            System.arraycopy( this.getCache(), (int) cacheStart, buf, off,
                              cachedLength );

            len -= cachedLength;
            off += cachedLength;
            read = cachedLength;
            this.filePointer += cachedLength;
        }

        return read;
    }

    public void write( final byte[] buf, final int off, final int len )
        throws IOException
    {
        if ( buf == null )
        {
            throw new NullPointerException( "buf" );
        }
        if ( off < 0 )
        {
            throw new IndexOutOfBoundsException( Integer.toString( off ) );
        }
        if ( len < 0 )
        {
            throw new IndexOutOfBoundsException( Integer.toString( len ) );
        }
        if ( off + len > buf.length )
        {
            throw new IndexOutOfBoundsException( Integer.toString( off + len ) );
        }

        this.assertNotClosed();

        if ( this.cachePosition != NO_CACHEPOSITION &&
             this.filePointer >= this.cachePosition &&
             this.filePointer < this.cachePosition + this.cacheLength )
        { // Cache needs updating.
            final long cacheStart = this.filePointer - this.cachePosition;

            assert cacheStart <= Integer.MAX_VALUE :
                "Unexpected implementation limit reached.";

            final int cachedLength = len > this.cacheLength -
                                           (int) cacheStart
                                     ? this.cacheLength - (int) cacheStart
                                     : len;

            System.arraycopy( buf, off, this.getCache(), (int) cacheStart,
                              cachedLength );

        }

        this.fileOperations.setFilePointer( this.filePointer );
        this.fileOperations.write( buf, off, len );
        this.filePointer += len;
    }

    public void read( final OutputStream out ) throws IOException
    {
        this.assertNotClosed();

        this.fileOperations.read( out );
        this.filePointer = this.fileOperations.getFilePointer();
    }

    public void write( final InputStream in ) throws IOException
    {
        this.assertNotClosed();

        this.fileOperations.write( in );
        this.filePointer = this.fileOperations.getFilePointer();
    }

    /**
     * {@inheritDoc}
     * Flushes the cache and closes the {@code FileOperations} implementation
     * backing the instance.
     *
     * @throws IOException if closing the {@code FileOperations} implementation
     * backing the instance fails or if the instance already is closed.
     */
    public void close() throws IOException
    {
        this.assertNotClosed();

        this.flush();
        this.getFileOperations().close();
        this.closed = true;
    }

    //----------------------------------------------------------FileOperations--
    //--FlushableFileOperations-------------------------------------------------

    /**
     * {@inheritDoc}
     * This method calls the {@code flush()} method of an underlying
     * {@code FlushableFileOperations} implementation, if any.
     */
    public void flush() throws IOException
    {
        this.assertNotClosed();

        if ( this.fileOperations instanceof FlushableFileOperations )
        {
            ( (FlushableFileOperations) this.fileOperations ).flush();
        }
    }

    //-------------------------------------------------FlushableFileOperations--
    //--ReadAheadFileOperations-------------------------------------------------

    /** The {@code FileOperations} backing the instance. */
    private final FileOperations fileOperations;

    /** Cached bytes. */
    private byte[] cache;

    /** Position in the file {@code cache} starts. */
    private long cachePosition;

    private static final long NO_CACHEPOSITION = Long.MIN_VALUE;

    /** Length of the cached data. */
    private int cacheLength;

    /** File pointer value. */
    private long filePointer;

    /** Flags the instance as beeing closed. */
    private boolean closed;

    /** Cache size in byte. */
    private Integer cacheSize;

    /**
     * Creates a new {@code ReadAheadFileOperations} instance taking the
     * {@code FileOperations} backing the instance.
     *
     * @param fileOperations the {@code FileOperations} backing the instance.
     *
     * @throws NullPointerException if {@code fileOperations} is {@code null}.
     * @throws IOException if reading fails.
     */
    public ReadAheadFileOperations( final FileOperations fileOperations )
        throws IOException
    {
        super();

        if ( fileOperations == null )
        {
            throw new NullPointerException( "fileOperations" );
        }

        this.fileOperations = fileOperations;
        this.filePointer = fileOperations.getFilePointer();
    }

    /**
     * Creates a new {@code ReadAheadFileOperations} instance taking the
     * {@code FileOperations} backing the instance and the size of the cache.
     *
     * @param fileOperations the {@code FileOperations} backing the instance.
     * @param cacheSize the number of bytes to read-ahead.
     *
     * @throws NullPointerException if {@code fileOperations} is {@code null}.
     * @throws IOException if reading fails.
     */
    public ReadAheadFileOperations( final FileOperations fileOperations,
                                    final int cacheSize ) throws IOException
    {
        this( fileOperations );

        if ( cacheSize > 0 )
        {
            this.cacheSize = new Integer( cacheSize );
        }
    }

    /**
     * Gets the {@code FileOperations} implementation operations are performed
     * with.
     *
     * @return the {@code FileOperations} implementation operations are
     * performed with.
     */
    public FileOperations getFileOperations()
    {
        return this.fileOperations;
    }

    /**
     * Gets the size of the cache in byte.
     *
     * @return the size of the cache in byte.
     */
    public int getCacheSize()
    {
        if ( this.cacheSize == null )
        {
            this.cacheSize = this.getDefaultCacheSize();
        }

        return this.cacheSize.intValue();
    }

    /**
     * Gets the cache buffer.
     *
     * @return the cache buffer.
     */
    private byte[] getCache()
    {
        if ( this.cache == null )
        {
            this.cache =
                this.getMemoryManager().allocateBytes( this.getCacheSize() );

        }

        return this.cache;
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
     * Fills the cache starting at the current file pointer value.
     *
     * @throws IOException if reading fails.
     */
    private void fillCache() throws IOException
    {
        final long delta = this.getLength() - this.filePointer;
        final int toRead = delta > this.getCache().length
                           ? this.getCache().length
                           : (int) delta;

        this.cachePosition = this.filePointer;

        int totalRead = 0;
        int readLength = toRead;

        do
        {
            this.fileOperations.setFilePointer( this.filePointer );
            final int read = this.fileOperations.read(
                this.getCache(), totalRead, readLength );

            assert read != FileOperations.EOF : "Unexpected end of file.";

            totalRead += read;
            readLength -= read;

        }
        while ( totalRead < toRead );

        this.cacheLength = toRead;
    }

    //-------------------------------------------------ReadAheadFileOperations--
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

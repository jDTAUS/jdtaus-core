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
import org.jdtaus.core.container.ContainerFactory;
import org.jdtaus.core.container.ContextFactory;
import org.jdtaus.core.container.ContextInitializer;
import org.jdtaus.core.container.Implementation;
import org.jdtaus.core.container.ModelFactory;
import org.jdtaus.core.container.Properties;
import org.jdtaus.core.container.Property;
import org.jdtaus.core.container.PropertyException;
import org.jdtaus.core.io.FileOperations;
import org.jdtaus.core.lang.spi.MemoryManager;

/**
 * Read-ahead {@code FileOperations} cache.
 * <p>This implementation implements a read-ahead cache for
 * {@code FileOperations} implementations. The cache is controlled by
 * configuration property {@code cacheSize} holding the number of bytes
 * to read-ahead. By default property {@code cacheSize} is initialized to
 * {@code 16384} leading to a cache size of 16 kB. All memory is allocated
 * during instantiation so that a {@code OutOfMemoryError} may be thrown
 * when constructing the cache but not when working with the instance.</p>
 *
 * <p><b>Note:</b><br>
 * This implementation is not thread-safe and concurrent changes to the
 * underlying {@code FileOperations} implementation are not supported.</p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public final class ReadAheadFileOperations implements FlushableFileOperations
{
    //--Implementation----------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausImplementation
    // This section is managed by jdtaus-container-mojo.

    /** Meta-data describing the implementation. */
    private static final Implementation META =
        ModelFactory.getModel().getModules().
        getImplementation(ReadAheadFileOperations.class.getName());
// </editor-fold>//GEN-END:jdtausImplementation

    //----------------------------------------------------------Implementation--
    //--Constructors------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausConstructors
    // This section is managed by jdtaus-container-mojo.

    /**
     * Initializes the properties of the instance.
     *
     * @param meta the property values to initialize the instance with.
     *
     * @throws NullPointerException if {@code meta} is {@code null}.
     */
    private void initializeProperties(final Properties meta)
    {
        Property p;

        if(meta == null)
        {
            throw new NullPointerException("meta");
        }

        p = meta.getProperty("cacheSize");
        this._cacheSize = ((java.lang.Integer) p.getValue()).intValue();

    }
// </editor-fold>//GEN-END:jdtausConstructors

    //------------------------------------------------------------Constructors--
    //--Dependencies------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausDependencies
    // This section is managed by jdtaus-container-mojo.

    /** Configured <code>MemoryManager</code> implementation. */
    private transient MemoryManager _dependency0;

    /**
     * Gets the configured <code>MemoryManager</code> implementation.
     *
     * @return the configured <code>MemoryManager</code> implementation.
     */
    private MemoryManager getMemoryManager()
    {
        MemoryManager ret = null;
        if(this._dependency0 != null)
        {
            ret = this._dependency0;
        }
        else
        {
            ret = (MemoryManager) ContainerFactory.getContainer().
                getDependency(ReadAheadFileOperations.class,
                "MemoryManager");

            if(ModelFactory.getModel().getModules().
                getImplementation(ReadAheadFileOperations.class.getName()).
                getDependencies().getDependency("MemoryManager").
                isBound())
            {
                this._dependency0 = ret;
            }
        }

        if(ret instanceof ContextInitializer && !((ContextInitializer) ret).
            isInitialized(ContextFactory.getContext()))
        {
            ((ContextInitializer) ret).initialize(ContextFactory.getContext());
        }

        return ret;
    }
// </editor-fold>//GEN-END:jdtausDependencies

    //------------------------------------------------------------Dependencies--
    //--Properties--------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausProperties
    // This section is managed by jdtaus-container-mojo.

    /**
     * Property {@code cacheSize}.
     * @serial
     */
    private int _cacheSize;

    /**
     * Gets the value of property <code>cacheSize</code>.
     *
     * @return the value of property <code>cacheSize</code>.
     */
    private int getCacheSize()
    {
        return this._cacheSize;
    }

// </editor-fold>//GEN-END:jdtausProperties

    //--------------------------------------------------------------Properties--
    //--FileOperations----------------------------------------------------------

    public long getLength() throws IOException
    {
        return this.fileOperations.getLength();
    }

    public void setLength(final long newLength) throws IOException
    {
        final long oldLength = this.getLength();
        this.fileOperations.setLength(newLength);
        if(this.filePointer > newLength)
        {
            this.filePointer = newLength;
        }

        if(oldLength > newLength && this.cachePosition != NO_CACHEPOSITION &&
            this.cachePosition + this.cacheLength >= newLength)
        { // Discard the end of file cache.
            this.cachePosition = NO_CACHEPOSITION;
        }
    }

    public long getFilePointer() throws IOException
    {
        return this.filePointer;
    }

    public void setFilePointer(final long pos) throws IOException
    {
        this.filePointer = pos;
    }

    public int read(final byte[] buf, int off, int len)
    throws IOException
    {
        if(buf == null)
        {
            throw new NullPointerException("buf");
        }
        if(off < 0)
        {
            throw new IndexOutOfBoundsException(Integer.toString(off));
        }
        if(len < 0)
        {
            throw new IndexOutOfBoundsException(Integer.toString(len));
        }
        if(off + len > buf.length)
        {
            throw new IndexOutOfBoundsException(Integer.toString(off + len));
        }

        int read = FileOperations.EOF;

        final long fileLength = this.getLength();

        if(len == 0)
        {
            read = 0;
        }
        else if(this.filePointer < fileLength)
        {
            if(this.cachePosition == NO_CACHEPOSITION ||
                !(this.filePointer >= this.cachePosition &&
                this.filePointer < this.cachePosition + this.cacheLength))
            { // Cache not initialized or file pointer outside the cached area.
                this.fillCache();
            }

            final long cacheStart = this.filePointer - this.cachePosition;

            assert cacheStart <= Integer.MAX_VALUE :
                "Unexpected implementation limit reached.";

            final int cachedLength = len > this.cacheLength - (int) cacheStart ?
                this.cacheLength - (int) cacheStart : len;

            System.arraycopy(this.cache, (int) cacheStart, buf, off,
                cachedLength);

            len -= cachedLength;
            off += cachedLength;
            read = cachedLength;
            this.filePointer += cachedLength;
        }

        return read;
    }

    public void write(final byte[] buf, final int off, final int len)
    throws IOException
    {
        if(buf == null)
        {
            throw new NullPointerException("buf");
        }
        if(off < 0)
        {
            throw new IndexOutOfBoundsException(Integer.toString(off));
        }
        if(len < 0)
        {
            throw new IndexOutOfBoundsException(Integer.toString(len));
        }
        if(off + len > buf.length)
        {
            throw new IndexOutOfBoundsException(Integer.toString(off + len));
        }

        if(this.cachePosition != NO_CACHEPOSITION &&
            this.filePointer >= this.cachePosition &&
            this.filePointer < this.cachePosition + this.cacheLength)
        { // Cache needs updating.
            final long cacheStart = this.filePointer - this.cachePosition;

            assert cacheStart <= Integer.MAX_VALUE :
                "Unexpected implementation limit reached.";

            final int cachedLength = len > this.cacheLength - (int) cacheStart ?
                this.cacheLength - (int) cacheStart : len;

            System.arraycopy(buf, off, this.cache, (int) cacheStart,
                cachedLength);

        }

        this.fileOperations.setFilePointer(this.filePointer);
        this.fileOperations.write(buf, off, len);
        this.filePointer += len;
    }

    public void read(final OutputStream out) throws IOException
    {
        this.fileOperations.read(out);
        this.filePointer = this.fileOperations.getFilePointer();
    }

    public void write(final InputStream in) throws IOException
    {
        this.fileOperations.write(in);
        this.filePointer = this.fileOperations.getFilePointer();
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
        if(this.fileOperations instanceof FlushableFileOperations)
        {
            ((FlushableFileOperations) this.fileOperations).flush();
        }
    }

    //-------------------------------------------------FlushableFileOperations--
    //--ReadAheadFileOperations-------------------------------------------------

    /** The {@code FileOperations} backing the instance. */
    private final FileOperations fileOperations;

    /** Cached bytes. */
    private final byte[] cache;

    /** Position in the file {@code cache} starts. */
    private long cachePosition;
    private static final long NO_CACHEPOSITION = Long.MIN_VALUE;

    /** Length of the cached data. */
    private int cacheLength;

    /** File pointer value. */
    private long filePointer;

    /**
     * Creates a new {@code ReadAheadFileOperations} instance taking the
     * {@code FileOperations} backing the instance.
     *
     * @param fileOperations the {@code FileOperations} backing the instance.
     *
     * @throws NullPointerException if {@code fileOperations} is {@code null}.
     * @throws IOException if reading fails.
     */
    public ReadAheadFileOperations(final FileOperations fileOperations)
    throws IOException
    {
        super();

        if(fileOperations == null)
        {
            throw new NullPointerException("fileOperations");
        }

        this.initializeProperties(META.getProperties());
        this.assertValidProperties();

        this.fileOperations = fileOperations;
        this.filePointer = fileOperations.getFilePointer();

        // Pre-allocate the cache.
        this.cache = this.getMemoryManager().allocateBytes(this.getCacheSize());
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
    public ReadAheadFileOperations(final FileOperations fileOperations,
        final int cacheSize) throws IOException
    {
        super();

        if(fileOperations == null)
        {
            throw new NullPointerException("fileOperations");
        }

        this.initializeProperties(META.getProperties());
        this._cacheSize = cacheSize;
        this.assertValidProperties();

        this.fileOperations = fileOperations;
        this.filePointer = fileOperations.getFilePointer();

        // Pre-allocate the cache.
        this.cache = this.getMemoryManager().allocateBytes(this.getCacheSize());
    }

    /**
     * Checks configured properties.
     *
     * @throws PropertyException for illegal property values.
     */
    private void assertValidProperties()
    {
        if(this.getCacheSize() <= 0)
        {
            throw new PropertyException("cacheSize",
                Integer.toString(this.getCacheSize()));

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
        final int toRead = delta > this.cache.length ?
            this.cache.length : (int) delta;

        this.cachePosition = this.filePointer;

        int totalRead = 0;
        int readLength = toRead;

        do
        {
            this.fileOperations.setFilePointer(this.filePointer);
            final int read = this.fileOperations.read(
                this.cache, totalRead, readLength);

            assert read != FileOperations.EOF : "Unexpected end of file.";

            totalRead += read;
            readLength -= read;

        } while(totalRead < toRead);

        this.cacheLength = toRead;
    }

    //-------------------------------------------------ReadAheadFileOperations--
}

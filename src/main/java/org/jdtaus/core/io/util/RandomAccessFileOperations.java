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
import java.util.Locale;
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
 * Adapts a {@link java.io.RandomAccessFile} to {@code FileOperations}.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public final class RandomAccessFileOperations implements FileOperations
{
    //--Implementation----------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausImplementation
    // This section is managed by jdtaus-container-mojo.

    /** Meta-data describing the implementation. */
    private static final Implementation META =
        ModelFactory.getModel().getModules().
        getImplementation(RandomAccessFileOperations.class.getName());
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

        p = meta.getProperty("bufferSize");
        this.pBufferSize = ((java.lang.Integer) p.getValue()).intValue();

    }
// </editor-fold>//GEN-END:jdtausConstructors

    //------------------------------------------------------------Constructors--
    //--Dependencies------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausDependencies
    // This section is managed by jdtaus-container-mojo.

    /** Configured <code>MemoryManager</code> implementation. */
    private transient MemoryManager dMemoryManager;

    /**
     * Gets the configured <code>MemoryManager</code> implementation.
     *
     * @return the configured <code>MemoryManager</code> implementation.
     */
    private MemoryManager getMemoryManager()
    {
        MemoryManager ret = null;
        if(this.dMemoryManager != null)
        {
            ret = this.dMemoryManager;
        }
        else
        {
            ret = (MemoryManager) ContainerFactory.getContainer().
                getDependency(RandomAccessFileOperations.class,
                "MemoryManager");

            if(ModelFactory.getModel().getModules().
                getImplementation(RandomAccessFileOperations.class.getName()).
                getDependencies().getDependency("MemoryManager").
                isBound())
            {
                this.dMemoryManager = ret;
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
     * Property {@code bufferSize}.
     * @serial
     */
    private int pBufferSize;

    /**
     * Gets the value of property <code>bufferSize</code>.
     *
     * @return the value of property <code>bufferSize</code>.
     */
    private int getBufferSize()
    {
        return this.pBufferSize;
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
        final byte[] buf = this.getDefaultBuffer();

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
        final byte[] buf = this.getDefaultBuffer();

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

    /** Default temporary buffer. */
    private byte[] defaultBuffer;

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

        this.initializeProperties( META.getProperties() );
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
        if ( this.getBufferSize() <= 0 )
        {
            throw new PropertyException( "bufferSize",
                                         new Integer( this.getBufferSize() ) );

        }
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
            throw new IOException( RandomAccessFileOperationsBundle.getInstance().
                                   getAlreadyClosedText( Locale.getDefault() ) );

        }
    }

    /**
     * Getter for property {@code defaultBuffer}.
     *
     * @return a buffer for operations which need temporary memory.
     */
    private byte[] getDefaultBuffer()
    {
        if ( this.defaultBuffer == null )
        {
            this.defaultBuffer = this.getMemoryManager().
                allocateBytes( this.getBufferSize() );

        }

        return this.defaultBuffer;
    }

    //----------------------------------------------RandomAccessFileOperations--
}

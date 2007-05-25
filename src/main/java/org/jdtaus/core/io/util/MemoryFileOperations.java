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
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Arrays;
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
import org.jdtaus.core.logging.spi.Logger;

/**
 * Implementation of elementary I/O operations in heap memory.
 * <p>This implementation performs IO in memory. The value of property
 * {@code length} is limited to a maximum of {@code Integer.MAX_VALUE} (4 GB).
 * </p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public final class MemoryFileOperations implements
    FileOperations, Serializable, Cloneable
{
    //--Fields------------------------------------------------------------------

    /**
     * Data to operate on.
     * @serial
     */
    private byte[] data = { (byte) 0 };

    /**
     * FilePointer.
     * @serial
     */
    private long filePointer = 0L;

    /**
     * Actual length.
     * @serial
     */
    private int length = 0;

    /**
     * Default temporary buffer.
     * @serial
     */
    private byte[] defaultBuffer;

    //------------------------------------------------------------------Fields--
    //--Implementation----------------------------------------------------------

    // This section is managed by jdtaus-container-mojo.

    /** Meta-data describing the implementation. */
    private static final Implementation META =
        ModelFactory.getModel().getModules().
        getImplementation(MemoryFileOperations.class.getName());

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
    //--Dependencies------------------------------------------------------------

    // This section is managed by jdtaus-container-mojo.

    /** Configured <code>MemoryManager</code> implementation. */
    private transient MemoryManager _dependency1;

    /**
     * Gets the configured <code>MemoryManager</code> implementation.
     *
     * @return the configured <code>MemoryManager</code> implementation.
     */
    private MemoryManager getMemoryManager()
    {
        MemoryManager ret = null;
        if(this._dependency1 != null)
        {
            ret = this._dependency1;
        }
        else
        {
            ret = (MemoryManager) ContainerFactory.getContainer().
                getDependency(MemoryFileOperations.class,
                "MemoryManager");

            if(ModelFactory.getModel().getModules().
                getImplementation(MemoryFileOperations.class.getName()).
                getDependencies().getDependency("MemoryManager").
                isBound())
            {
                this._dependency1 = ret;
            }
        }

        if(ret instanceof ContextInitializer && !((ContextInitializer) ret).
            isInitialized(ContextFactory.getContext()))
        {
            ((ContextInitializer) ret).initialize(ContextFactory.getContext());
        }

        return ret;
    }
    /** Configured <code>Logger</code> implementation. */
    private transient Logger _dependency0;

    /**
     * Gets the configured <code>Logger</code> implementation.
     *
     * @return the configured <code>Logger</code> implementation.
     */
    private Logger getLogger()
    {
        Logger ret = null;
        if(this._dependency0 != null)
        {
            ret = this._dependency0;
        }
        else
        {
            ret = (Logger) ContainerFactory.getContainer().
                getDependency(MemoryFileOperations.class,
                "Logger");

            if(ModelFactory.getModel().getModules().
                getImplementation(MemoryFileOperations.class.getName()).
                getDependencies().getDependency("Logger").
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
    public void setLength(final long newLength)
    {
        if(newLength < 0L || newLength > Integer.MAX_VALUE)
        {
            throw new IllegalArgumentException(Long.toString(newLength));
        }

        this.resize((int) newLength);
        if(this.filePointer > this.data.length)
        {
            this.filePointer = this.data.length;
        }

        this.length = (int) newLength;
    }

    public long getFilePointer()
    {
        return this.filePointer;
    }

    public void setFilePointer(final long pos)
    {
        // Preconditions.
        if(pos < 0L || pos > Integer.MAX_VALUE)
        {
            throw new IllegalArgumentException(Long.toString(pos));
        }

        this.filePointer = pos;
    }

    public int read(final byte[] buf, final int off, final int len)
    {
        final int ret;

        // Preconditions.
        if(buf == null)
        {
            throw new NullPointerException("buf");
        }
        if(off < 0)
        {
            throw new ArrayIndexOutOfBoundsException(off);
        }
        if(len < 0)
        {
            throw new ArrayIndexOutOfBoundsException(len);
        }
        if(off + len > buf.length)
        {
            throw new ArrayIndexOutOfBoundsException(off + len);
        }
        if(this.filePointer + len > Integer.MAX_VALUE)
        {
            throw new ArrayIndexOutOfBoundsException(Integer.MAX_VALUE);
        }

        if(len == 0)
        {
            ret = 0;
        }
        else if(this.filePointer >= this.length)
        {
            // EOF
            ret = -1;
        }
        else if(this.filePointer + len > this.length)
        {
            // less than len byte before EOF
            final int remaining = (int) (this.length - this.filePointer);
            System.arraycopy(this.data, (int) this.filePointer, buf, off,
                remaining);

            this.filePointer += remaining;
            ret = remaining;
        }
        else
        {
            // copy len byte into buf.
            System.arraycopy(this.data, (int) this.filePointer, buf, off, len);
            this.filePointer += len;
            ret = len;
        }

        return ret;
    }

    public void write(final byte[] buf, final int off, final int len)
    {
        // Preconditions.
        if(buf == null)
        {
            throw new NullPointerException("buf");
        }
        if(off < 0)
        {
            throw new ArrayIndexOutOfBoundsException(off);
        }
        if(len < 0)
        {
            throw new ArrayIndexOutOfBoundsException(len);
        }
        if(off + len > buf.length)
        {
            throw new ArrayIndexOutOfBoundsException(off + len);
        }

        final long newLen = this.filePointer + len;
        if(newLen > Integer.MAX_VALUE)
        {
            throw new ArrayIndexOutOfBoundsException(Integer.MAX_VALUE);
        }

        if(newLen > this.length - 1L)
        {
            this.setLength(newLen);
        }

        System.arraycopy(buf, off, this.data, (int) this.filePointer, len);
        this.filePointer += len;
    }

    public void read(final OutputStream out) throws IOException
    {
        if(out == null)
        {
            throw new NullPointerException("out");
        }

        out.write(this.data, 0, this.length);
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
    //--MemoryFileOperations----------------------------------------------------

    /** Creates a new {@code MemoryFileOperations} instance of no length. */
    public MemoryFileOperations()
    {
        this.initializeProperties(META.getProperties());
        this.assertValidProperties();
    }

    /**
     * Creates a new {@code MemoryFileOperations} instance holding {@code buf}.
     *
     * @param buf bytes to initialize the instance with.
     *
     * @throws NullPointerException if {@code buf} is {@code null}.
     */
    public MemoryFileOperations(final byte[] buf)
    {
        this();

        if(buf == null)
        {
            throw new NullPointerException("buf");
        }

        this.data = buf;
        this.length = buf.length;
        this.filePointer = 0L;
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
        final int length = (int) this.getLength();
        final byte[] ret = this.getMemoryManager().allocateBytes(length);

        System.arraycopy(this.data, 0, ret, 0, length);

        return ret;
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

    /**
     * Resizes the internal buffer.
     *
     * @param newSize maximum size the internal buffer needs to hold.
     */
    private void resize(int newSize)
    {
        final int oldLength = this.data.length;

        while(this.data.length < newSize)
        {
            final byte[] newData = this.getMemoryManager().
                allocateBytes(this.data.length * 2);

            Arrays.fill(newData, (byte) 0);
            System.arraycopy(this.data, 0, newData, 0, this.data.length);
            this.data = newData;
        }

        if(oldLength != this.data.length &&
            this.getLogger().isDebugEnabled())
        {

            final MessageFormat fmt = MemoryFileOperationsBundle.
                getLogResizeMessage(Locale.getDefault());

            this.getLogger().debug(fmt.format(new Object[] {
                new Long(this.data.length) }));

        }
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
     */
    public boolean equals(final Object o)
    {
        boolean ret = o == this;
        final MemoryFileOperations that;

        if(!ret && o instanceof MemoryFileOperations)
        {
            that = (MemoryFileOperations) o;
            ret = Arrays.equals(this.getData(), that.getData());
        }

        return ret;
    }

    /**
     * Returns a hash code value for this object.
     *
     * @return a hash code value for this object.
     */
    public int hashCode()
    {
        return this.getData().hashCode();
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
        catch(CloneNotSupportedException e)
        {
            throw new AssertionError(e);
        }
    }

    //------------------------------------------------------------------Object--
}

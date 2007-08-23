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
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
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
 * Coalescing {@code FileOperations} cache.
 * <p>This implementation implements a coalescing cache for
 * {@code FileOperations} implementations. The cache is controlled by
 * configuration property {@code blockSize}. By default property
 * {@code blockSize} is initialized to {@code 2097152} leading to a cache
 * size of 10 MB (multiplied by property {@code cacheSize} which defaults to
 * {@code 5}). All memory is allocated during instantiation so that an
 * {@code OutOfMemoryError} may be thrown when constructing the cache but not
 * when working with the instance.</p>
 *
 * <p><b>Note:</b><br>
 * This implementation is not thread-safe and concurrent changes to the
 * underlying {@code FileOperations} implementation are not supported.</p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public final class CoalescingFileOperations implements FlushableFileOperations
{
    //--Implementation----------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausImplementation
    // This section is managed by jdtaus-container-mojo.

    /** Meta-data describing the implementation. */
    private static final Implementation META =
        ModelFactory.getModel().getModules().
        getImplementation(CoalescingFileOperations.class.getName());
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


        p = meta.getProperty("blockSize");
        this._blockSize = ((java.lang.Integer) p.getValue()).intValue();

    }
// </editor-fold>//GEN-END:jdtausConstructors

    //------------------------------------------------------------Constructors--
    //--Dependencies------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausDependencies
    // This section is managed by jdtaus-container-mojo.

    /** Configured <code>Logger</code> implementation. */
    private transient Logger _dependency1;

    /**
     * Gets the configured <code>Logger</code> implementation.
     *
     * @return the configured <code>Logger</code> implementation.
     */
    private Logger getLogger()
    {
        Logger ret = null;
        if(this._dependency1 != null)
        {
            ret = this._dependency1;
        }
        else
        {
            ret = (Logger) ContainerFactory.getContainer().
                getDependency(CoalescingFileOperations.class,
                "Logger");

            if(ModelFactory.getModel().getModules().
                getImplementation(CoalescingFileOperations.class.getName()).
                getDependencies().getDependency("Logger").
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
                getDependency(CoalescingFileOperations.class,
                "MemoryManager");

            if(ModelFactory.getModel().getModules().
                getImplementation(CoalescingFileOperations.class.getName()).
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

    /**
     * Property {@code blockSize}.
     * @serial
     */
    private int _blockSize;

    /**
     * Gets the value of property <code>blockSize</code>.
     *
     * @return the value of property <code>blockSize</code>.
     */
    public int getBlockSize()
    {
        return this._blockSize;
    }

// </editor-fold>//GEN-END:jdtausProperties

    //--------------------------------------------------------------Properties--
    //--FileOperations----------------------------------------------------------

    public long getLength() throws IOException
    {
        this.assertNotClosed();

        return this.fileOperations.getLength();
    }

    public void setLength(final long newLength) throws IOException
    {
        this.assertNotClosed();

        // Update the length of any cache nodes involved in the operation.
        final long oldLength = this.getLength();
        if(newLength > oldLength)
        {
            final long delta = newLength - oldLength;

            assert delta <= Integer.MAX_VALUE :
                "Unexpected implementation limit reached.";

            final Node[] nodes =
                this.getCacheNodesForLength(oldLength, (int) delta);

            for(int i = 0; i < nodes.length; i++)
            {
                final long startPos = nodes[i].block * this.getBlockSize();
                final long blockDelta = newLength - startPos;

                assert blockDelta <= Integer.MAX_VALUE :
                    "Unexpected implementation limit reached.";

                nodes[i].length = blockDelta >= this.getBlockSize() ?
                    this.getBlockSize() : (int) blockDelta;

            }
        }
        else if(newLength < oldLength)
        {
            final long delta = oldLength - newLength;

            assert delta <= Integer.MAX_VALUE :
                "Unexpected implementation limit reached.";

            final Node[] nodes =
                this.getCacheNodesForLength(newLength, (int) delta);

            for(int i = 0; i < nodes.length; i++)
            {
                final long startPos = nodes[i].block * this.getBlockSize();
                if(startPos > newLength)
                { // Discard the block.
                    this.root.remove(new Long(nodes[i].block));
                }
                else
                { // Update the blocks length.
                    final long blockDelta = newLength - startPos;

                    assert blockDelta <= Integer.MAX_VALUE :
                        "Unexpected implementation limit reached.";

                    nodes[i].length = blockDelta >= this.getBlockSize() ?
                        this.getBlockSize() : (int) blockDelta;

                }
            }
        }

        this.fileOperations.setLength(newLength);

        if(this.filePointer > newLength)
        {
            this.filePointer = newLength;
        }
    }

    public long getFilePointer() throws IOException
    {
        this.assertNotClosed();

        return this.filePointer;
    }

    public void setFilePointer(final long pos) throws IOException
    {
        this.assertNotClosed();

        this.filePointer = pos;
    }

    public int read(final byte[] buf, int off, int len) throws IOException
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

        this.assertNotClosed();

        int read = FileOperations.EOF;

        if(len == 0)
        {
            read = 0;
        }
        else if(this.filePointer < this.getLength())
        { // End of file not reached.
            final Node[] nodes =
                this.getCacheNodesForLength(this.filePointer, len);

            // Ensure cache holds the data of the involved blocks.
            this.fillCache(nodes);

            int copied = 0;
            for(int i = 0; i < nodes.length; i++)
            {
                if(nodes[i].length == FileOperations.EOF)
                { // Skip any end of file nodes.
                    continue;
                }

                if(nodes[i].cacheIndex != Node.NO_CACHEINDEX)
                { // Nodes is associated with cache memory; cache is used.

                    // Use the current file pointer as the starting index.
                    final long delta = nodes[i].cacheIndex + (this.filePointer -
                        nodes[i].block * this.getBlockSize());

                    assert delta <= Integer.MAX_VALUE :
                        "Unexpected implementation limit reached.";

                    final int blockOffset = (int) delta;
                    final int blockDelta = nodes[i].length -
                        (blockOffset - nodes[i].cacheIndex);

                    final int copyLength = len >  blockDelta ? blockDelta : len;
                    System.arraycopy(this.cache, blockOffset, buf, off,
                        copyLength);

                    off += copyLength;
                    len -= copyLength;
                    copied += copyLength;
                    this.filePointer += copyLength;
                }
                else
                { // Node is not associated with cache memory; read directly.
                    this.fileOperations.setFilePointer(this.filePointer);
                    copied += this.fileOperations.read(buf, off, len);
                    this.filePointer += len;

                    this.getLogger().debug(CoalescingFileOperationsBundle.
                        getReadBypassesCacheMessage(Locale.getDefault()).
                        format(new Object[] { new Integer(this.getBlockSize()),
                        new Integer(this.getCacheSize()), new Integer(len) }));

                    break;
                }
            }

            read = copied;
        }

        return read;
    }

    public void write(final byte[] buf, int off, int len) throws IOException
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

        this.assertNotClosed();

        if(this.filePointer + len > this.getLength())
        { // Expand the file of the backing instance.
            this.setLength(this.filePointer + len);
        }

        final Node[] nodes =
            this.getCacheNodesForLength(this.filePointer, len);

        // Ensure cache holds the data of the involved blocks.
        this.fillCache(nodes);

        for(int i = 0; i < nodes.length; i++)
        {
            // Check for correct file length update.
            assert nodes[i].length != FileOperations.EOF :
                "Unexpected cache state.";

            if(nodes[i].cacheIndex != Node.NO_CACHEINDEX)
            { // Nodes is associated with cache memory; cache is used.

                // Use the current file pointer as the starting index.
                final long delta = nodes[i].cacheIndex + (this.filePointer -
                    nodes[i].block * this.getBlockSize());

                assert delta <= Integer.MAX_VALUE :
                    "Unexpected implementation limit reached.";

                final int blockOffset = (int) delta;
                final int blockDelta = nodes[i].length -
                    (blockOffset - nodes[i].cacheIndex);

                final int copyLength = len > blockDelta ? blockDelta : len;
                System.arraycopy(buf, off, this.cache, blockOffset, copyLength);
                off += copyLength;
                len -= copyLength;
                this.filePointer += copyLength;
                nodes[i].dirty = true;
            }
            else
            { // Node is not associated with cache memory; write out directly.
                this.fileOperations.setFilePointer(this.filePointer);
                this.fileOperations.write(buf, off, len);
                this.filePointer += len;

                this.getLogger().debug(CoalescingFileOperationsBundle.
                    getWriteBypassesCacheMessage(Locale.getDefault()).
                    format(new Object[] { new Integer(this.getBlockSize()),
                    new Integer(this.getCacheSize()), new Integer(len) }));

                break;
            }
        }
    }

    public void read(final OutputStream out) throws IOException
    {
        this.assertNotClosed();

        this.fileOperations.read(out);
        this.filePointer = this.fileOperations.getFilePointer();
    }

    public void write(final InputStream in) throws IOException
    {
        this.assertNotClosed();

        this.fileOperations.write(in);
        this.filePointer = this.fileOperations.getFilePointer();
    }

    /**
     * {@inheritDoc}
     * Flushes the cache and closes the {@code FileOperations} implementation
     * backing the instance.
     *
     * @throws IOException if flushing or closing the {@code FileOperations}
     * implementation backing the instance fails, or if the instance already
     * is closed.
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
     *
     * @throws IOException if writing any pending changes fails or if the
     * instance is closed.
     */
    public void flush() throws IOException
    {
        this.assertNotClosed();

        this.defragmentCache();

        long startPos = FileOperations.EOF;
        int startIndex = FileOperations.EOF;
        int length = FileOperations.EOF;
        Node previous = null;
        boolean dirty = false;

        for(Iterator it = this.root.entrySet().iterator(); it.hasNext();)
        {
            final Map.Entry entry = (Map.Entry) it.next();
            final long block = ((Long) entry.getKey()).longValue();
            final Node current = (Node) entry.getValue();

            // Skip any end of file nodes and nodes not associated with memory.
            if(current.length == FileOperations.EOF ||
                current.cacheIndex == Node.NO_CACHEINDEX)
            {
                continue;
            }

            assert current.block == block : "Unexpected cache state.";

            if(previous == null)
            { // Start the first chunk.
                previous = current;
                startPos = current.block * this.getBlockSize();
                startIndex = current.cacheIndex;
                length = current.length;
                dirty = current.dirty;
            }
            else if(current.block == previous.block + 1L)
            { // Expand the current chunk.

                assert current.cacheIndex == previous.cacheIndex +
                    this.getBlockSize() : "Unexpected cache state.";

                previous = current;
                length += current.length;
                if(!dirty)
                {
                    dirty = current.dirty;
                }
            }
            else
            { // Write out the current chunk and start a new one.
                if(dirty)
                {
                    this.fileOperations.setFilePointer(startPos);
                    this.fileOperations.write(
                        this.cache, startIndex, length);

                }

                previous = current;
                startPos = current.block * this.getBlockSize();
                startIndex = current.cacheIndex;
                length = current.length;
                dirty = current.dirty;
            }
        }

        if(dirty)
        { // Write the remaining chunk.
            this.fileOperations.setFilePointer(startPos);
            this.fileOperations.write(
                this.cache, startIndex, length);

        }

        // Reset cache state.
        for(Iterator it = this.root.entrySet().iterator(); it.hasNext();)
        {
            final Map.Entry entry = (Map.Entry) it.next();
            final Node current = (Node) entry.getValue();

            current.cacheIndex = Node.NO_CACHEINDEX;
            current.dirty = false;
        }

        this.nextCacheIndex = 0;

        if(this.fileOperations instanceof FlushableFileOperations)
        { // Cache of the backing instance also needs to get flushed.
            ((FlushableFileOperations) this.fileOperations).flush();
        }
    }

    //-------------------------------------------------FlushableFileOperations--
    //--CoalescingFileOperations------------------------------------------------

    /** Node describing a cache block. */
    private static final class Node
    {
        private static final int NO_CACHEINDEX = Integer.MIN_VALUE;

        long block;
        int cacheIndex = NO_CACHEINDEX;
        int length;
        boolean dirty;
    }

    /** The {@code FileOperations} backing the instance. */
    private final FileOperations fileOperations;

    /** Cached blocks. */
    private final byte[] cache;

    /** Second cache memory used during defragmentation. */
    private final byte[] defragCache;

    /** Index of the next free cached block. */
    private int nextCacheIndex;

    /** Maps blocks to corresponding {@code Node}s. */
    private final Map root = new TreeMap();

    /** File pointer. */
    private long filePointer;

    /** Caches the value returned by method {@code getFilePointerBlock}. */
    private long cachedFilePointerBlock = NO_FILEPOINTERBLOCK;
    private long cachedFilePointerBlockStart = FileOperations.EOF;
    private static final long NO_FILEPOINTERBLOCK = Long.MIN_VALUE;

    /** Flags the instance as beeing closed. */
    private boolean closed;

    /**
     * Creates a new {@code CoalescingFileOperations} instance taking the
     * {@code FileOperations} backing the instance.
     *
     * @param fileOperations the {@code FileOperations} backing the instance.
     *
     * @throws NullPointerException if {@code fileOperations} is {@code null}.
     * @throws IOException if reading fails.
     */
    public CoalescingFileOperations(final FileOperations fileOperations)
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

        // Pre-allocate the cache memory.
        this.cache = this.getMemoryManager().allocateBytes(
            this.getBlockSize() * this.getCacheSize());

        this.defragCache = this.getMemoryManager().allocateBytes(
            this.getBlockSize() * this.getCacheSize());

        this.filePointer = fileOperations.getFilePointer();
    }

    /**
     * Creates a new {@code CoalescingFileOperations} instance taking the
     * {@code FileOperations} backing the instance and the number of bytes
     * occupied by one cache block.
     *
     * @param fileOperations the {@code FileOperations} backing the instance.
     * @param blockSize the number of bytes occupied by one cache block.
     *
     * @throws NullPointerException if {@code fileOperations} is {@code null}.
     * @throws PropertyException if {@code blockSize} is negative or zero.
     * @throws IOException if reading fails.
     */
    public CoalescingFileOperations(final FileOperations fileOperations,
        final int blockSize) throws IOException
    {
        super();

        if(fileOperations == null)
        {
            throw new NullPointerException("fileOperations");
        }

        this.initializeProperties(META.getProperties());
        this._blockSize = blockSize;
        this.assertValidProperties();

        this.fileOperations = fileOperations;

        // Pre-allocate the cache memory.
        this.cache = this.getMemoryManager().allocateBytes(
            this.getBlockSize() * this.getCacheSize());

        this.defragCache = this.getMemoryManager().allocateBytes(
            this.getBlockSize() * this.getCacheSize());

        this.filePointer = fileOperations.getFilePointer();
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
     * Checks configured properties.
     *
     * @throws PropertyException for illegal property values.
     */
    private void assertValidProperties()
    {
        if(this.getBlockSize() <= 0)
        {
            throw new PropertyException("blockSize",
                Integer.toString(this.getBlockSize()));

        }
        if(this.getCacheSize() <= 0)
        {
            throw new PropertyException("cacheSize",
                Integer.toString(this.getCacheSize()));

        }
    }

    /**
     * Checks that the instance is not closed.
     *
     * @throws IOException if the instance is closed.
     */
    private void assertNotClosed() throws IOException
    {
        if(this.closed)
        {
            throw new IOException(CoalescingFileOperationsBundle.
                getAlreadyClosedText(Locale.getDefault()));

        }
    }

    /**
     * Gets the block pointed to by a given file pointer value.
     *
     * @param filePointer the file pointer value for which to return the
     * corresponding block.
     *
     * @return the block pointed to by {@code filePointer}.
     */
    private long getFilePointerBlock(final long filePointer)
    {
        if(this.cachedFilePointerBlock == NO_FILEPOINTERBLOCK)
        {
            this.cachedFilePointerBlock = (filePointer / this.getBlockSize()) -
                ((filePointer % this.getBlockSize()) / this.getBlockSize());

            this.cachedFilePointerBlockStart =
                this.cachedFilePointerBlock * this.getBlockSize();

        }
        else
        {
            if(!(filePointer >= this.cachedFilePointerBlockStart &&
                filePointer <= this.cachedFilePointerBlockStart +
                this.getBlockSize()))
            {
                this.cachedFilePointerBlock =
                    (filePointer / this.getBlockSize()) -
                    ((filePointer % this.getBlockSize()) / this.getBlockSize());

                this.cachedFilePointerBlockStart =
                    this.cachedFilePointerBlock * this.getBlockSize();

            }
        }

        return this.cachedFilePointerBlock;
    }

    /**
     * Gets the cache nodes for all blocks involved in a read or write operation
     * of a given length for a given file pointer value.
     *
     * @param filePointer the file pointer value to use for computing the
     * number of involved blocks for a read or write operation of
     * {@code length}.
     * @param length the length of the operation to perform.
     *
     * @return an array of cache nodes for all blocks involved in the
     * operation in the order corresponding to the operation's needs.
     */
    private Node[] getCacheNodesForLength(final long filePointer,
        final int length)
    {
        final long startingBlock = this.getFilePointerBlock(filePointer);
        final long endingBlock = this.getFilePointerBlock(filePointer + length);

        assert endingBlock - startingBlock <= Integer.MAX_VALUE :
            "Unexpected implementation limit reached.";

        final Node[] nodes = new Node[(int) (endingBlock - startingBlock + 1L)];

        if(startingBlock == endingBlock)
        {
            nodes[0] = this.getCacheNode(startingBlock);
        }
        else
        {
            int i;
            long block;

            for(block = startingBlock, i = 0; block <= endingBlock;
            block++, i++)
            {
                nodes[i] = this.getCacheNode(block);
            }
        }

        return nodes;
    }

    /**
     * Fills the cache for a given set of cache nodes.
     * <p>This method ensures that each given node gets associated with
     * corresponding cache memory possibly flushing the cache before
     * reading.</p>
     *
     * @param nodes the nodes to fill the cache for.
     *
     * @throws NullPointerException if {@code nodes} is {@code null}.
     * @throws IOException if reading fails.
     */
    private void fillCache(final Node[] nodes) throws IOException
    {
        if(nodes == null)
        {
            throw new NullPointerException("nodes");
        }

        // Calculate the amount of bytes needed to be available in the cache
        // and flush the cache if nodes would not fit.
        long neededBytes = 0L;
        for(int i = nodes.length - 1; i >= 0; i--)
        {
            if(nodes[i].cacheIndex == Node.NO_CACHEINDEX)
            { // Node's block needs to be read.
                neededBytes += this.getBlockSize();
            }
        }

        if(this.nextCacheIndex + neededBytes > this.cache.length)
        { // Cache cannot hold the needed blocks so needs flushing.
            this.flush();
        }

        // Associate each node with cache memory for nodes not already
        // associated with cache memory and fill these nodes' cache memory.
        for(int i = nodes.length - 1; i >= 0; i--)
        {
            if(nodes[i].cacheIndex == Node.NO_CACHEINDEX &&
                this.nextCacheIndex < this.cache.length)
            { // Node is not associated with any cache memory and can be read.

                // Update the length field of the node for the block checking
                // for a possible end of file condition.
                long pos = nodes[i].block * this.getBlockSize();
                if(pos > this.getLength())
                { // Node is behind the end of the file.
                    nodes[i].length = FileOperations.EOF;
                    continue;
                }
                else if(pos + this.getBlockSize() > this.getLength())
                {
                    final long delta = this.getLength() - pos;

                    assert delta <= Integer.MAX_VALUE :
                        "Unexpected implementation limit reached.";

                    nodes[i].length = (int) delta;
                }
                else
                {
                    nodes[i].length = this.getBlockSize();
                }

                // Associated the node with cache memory.
                nodes[i].cacheIndex = this.nextCacheIndex;
                this.nextCacheIndex += this.getBlockSize();

                // Read the node's block into cache.
                int read = FileOperations.EOF;
                int totalRead = 0;
                int toRead = nodes[i].length;
                this.fileOperations.setFilePointer(pos);

                do
                {
                    read = this.fileOperations.read(this.cache,
                        nodes[i].cacheIndex + totalRead, toRead);

                    assert read != FileOperations.EOF :
                        "Unexpected end of file.";

                    totalRead += read;
                    toRead -= read;

                } while(totalRead < nodes[i].length);
            }
        }
    }

    /** Defragments the cache. */
    private void defragmentCache()
    {
        int defragIndex = 0;

        // Step through the cached blocks and defragment the cache.
        for(Iterator it = this.root.entrySet().iterator(); it.hasNext();)
        {
            final Map.Entry entry = (Map.Entry) it.next();
            final long block = ((Long) entry.getKey()).longValue();
            final Node current = (Node) entry.getValue();

            // Skip any end of file nodes and nodes not associated with memory.
            if(current.length == FileOperations.EOF ||
                current.cacheIndex == Node.NO_CACHEINDEX)
            {
                continue;
            }

            assert current.block == block : "Unexpected cache state.";

            System.arraycopy(this.cache, current.cacheIndex, this.defragCache,
                defragIndex, this.getBlockSize());

            current.cacheIndex = defragIndex;
            defragIndex += this.getBlockSize();
        }

        System.arraycopy(this.defragCache, 0, this.cache, 0, this.cache.length);
    }

    /**
     * Gets the cache node for a given block.
     *
     * @param block the block to return the corresponding cache node for.
     *
     * @return the cache node for {@code block}.
     */
    private Node getCacheNode(final long block)
    {
        final Long key = new Long(block);
        Node node = (Node) this.root.get(key);
        if(node == null)
        {
            node = new Node();
            node.block = block;
            this.root.put(key, node);
        }

        return node;
    }

    /**
     * Gets the cache node for a given cache index.
     *
     * @param cacheIndex the index where the node's data is kept in the cache.
     *
     * @return the cache node for {@code cacheIndex}.
     */
    private Node getCacheNode(final int cacheIndex)
    {
        Node node = null;

        for(Iterator it = this.root.entrySet().iterator(); it.hasNext();)
        {
            final Map.Entry entry = (Map.Entry) it.next();
            if(((Node) entry.getValue()).cacheIndex == cacheIndex)
            {
                node = (Node) entry.getValue();
                break;
            }
        }

        return node;
    }

    //------------------------------------------------CoalescingFileOperations--
}

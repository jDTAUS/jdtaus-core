/*
 *  jDTAUS Core RI Memory Manager
 *  Copyright (c) 2005 Christian Schulte
 *
 *  Christian Schulte, Haldener Strasse 72, 58095 Hagen, Germany
 *  <schulte2005@users.sourceforge.net> (+49 2331 3543887)
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
package org.jdtaus.core.lang.ri;

import java.util.Locale;
import org.jdtaus.core.container.ContainerFactory;
import org.jdtaus.core.lang.spi.MemoryManager;
import org.jdtaus.core.logging.spi.Logger;

/**
 * jDTAUS Core SPI {@code MemoryManager} reference implementation.
 * <p>The reference implementation leaves a configurable amount of memory free
 * and throws an {@code OutOfMemoryError} although the system has memory
 * available. This free memory is then available for proper exception handling
 * or for releasing resources in the system properly. It is configured with the
 * two configuration properties {@code maximumPercent} and
 * {@code maximumRetries}. Whenever an allocation would consume more than
 * {@code maximumPercent} percent of all available memory this implementation
 * tries to free memory by forcing garbage collection {@code maximumRetries}
 * times before throwing an {@code OutOfMemoryError} exception. The default for
 * property {@code maximumPercent} is {@code 98} and the default for property
 * {@code maximumRetries} is {@code 1}.</p>
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $Id$
 *
 * @see org.jdtaus.core.container.Container
 */
public class DefaultMemoryManager implements MemoryManager
{
    //--Constructors------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausConstructors
    // This section is managed by jdtaus-container-mojo.

    /** Standard implementation constructor <code>org.jdtaus.core.lang.ri.DefaultMemoryManager</code>. */
    public DefaultMemoryManager()
    {
        super();
    }

// </editor-fold>//GEN-END:jdtausConstructors

    //------------------------------------------------------------Constructors--
    //--Dependencies------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausDependencies
    // This section is managed by jdtaus-container-mojo.

    /**
     * Gets the configured <code>Logger</code> implementation.
     *
     * @return The configured <code>Logger</code> implementation.
     */
    private Logger getLogger()
    {
        return (Logger) ContainerFactory.getContainer().
            getDependency( this, "Logger" );

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
     * Gets the value of property <code>defaultMaximumRetries</code>.
     *
     * @return Default number of retries when trying to free memory.
     */
    private java.lang.Integer getDefaultMaximumRetries()
    {
        return (java.lang.Integer) ContainerFactory.getContainer().
            getProperty( this, "defaultMaximumRetries" );

    }

    /**
     * Gets the value of property <code>defaultMaximumPercent</code>.
     *
     * @return Default maximum percent of memory for use by the implementation.
     */
    private java.lang.Integer getDefaultMaximumPercent()
    {
        return (java.lang.Integer) ContainerFactory.getContainer().
            getProperty( this, "defaultMaximumPercent" );

    }

// </editor-fold>//GEN-END:jdtausProperties

    //--------------------------------------------------------------Properties--
    //--MemoryManager-----------------------------------------------------------

    public long getAllocatedPercent()
    {
        final Runtime rt = Runtime.getRuntime();
        final long max = rt.maxMemory();
        final long limit = ( max / 100L ) * this.getMaximumPercent();
        return rt.totalMemory() * ( 100L / limit );
    }

    public long getAvailableBytes()
    {
        final Runtime rt = Runtime.getRuntime();
        final long all = rt.maxMemory() - rt.totalMemory() + rt.freeMemory();
        return ( all / 100L ) * this.getMaximumPercent();
    }

    public byte[] allocateBytes( int requested ) throws OutOfMemoryError
    {
        if ( requested < 0 )
        {
            throw new IllegalArgumentException( Integer.toString( requested ) );
        }

        byte[] ret = null;
        int retries = this.getMaximumRetries();

        do
        {
            final long available = this.getAvailableBytes();

            if ( available < requested )
            {
                this.logOutOfMemoryWarning( new Long( requested ),
                                            new Long( available ) );

                this.forceGarbageCollection(
                    this.getMaximumRetries() - retries );

            }
            else
            {
                ret = new byte[ requested ];
            }

        }
        while ( ret == null && retries-- > 0 );

        if ( ret == null )
        {
            throw new OutOfMemoryError();
        }

        return ret;
    }

    public long getAvailableShorts()
    {
        return this.getAvailableBytes() / 2;
    }

    public short[] allocateShorts( int requested ) throws OutOfMemoryError
    {
        if ( requested < 0 )
        {
            throw new IllegalArgumentException( Integer.toString( requested ) );
        }

        short[] ret = null;
        int retries = this.getMaximumRetries();

        do
        {
            final long available = this.getAvailableShorts();

            if ( available < requested )
            {
                this.logOutOfMemoryWarning( new Long( requested * 2 ),
                                            new Long( available * 2 ) );

                this.forceGarbageCollection(
                    this.getMaximumRetries() - retries );

            }
            else
            {
                ret = new short[ requested ];
            }

        }
        while ( ret == null && retries-- > 0 );

        if ( ret == null )
        {
            throw new OutOfMemoryError();
        }

        return ret;
    }

    public long getAvailableIntegers()
    {
        return this.getAvailableBytes() / 4;
    }

    public int[] allocateIntegers( int requested ) throws OutOfMemoryError
    {
        if ( requested < 0 )
        {
            throw new IllegalArgumentException( Integer.toString( requested ) );
        }

        int[] ret = null;
        int retries = this.getMaximumRetries();

        do
        {
            final long available = this.getAvailableIntegers();
            if ( available < requested )
            {
                this.logOutOfMemoryWarning( new Long( requested * 4 ),
                                            new Long( available * 4 ) );

                this.forceGarbageCollection(
                    this.getMaximumRetries() - retries );

            }
            else
            {
                ret = new int[ requested ];
            }


        }
        while ( ret == null && retries-- > 0 );

        if ( ret == null )
        {
            throw new OutOfMemoryError();
        }

        return ret;
    }

    public long getAvailableLongs()
    {
        return this.getAvailableBytes() / 8;
    }

    public long[] allocateLongs( int requested ) throws OutOfMemoryError
    {
        if ( requested < 0 )
        {
            throw new IllegalArgumentException( Integer.toString( requested ) );
        }

        long[] ret = null;
        int retries = this.getMaximumRetries();

        do
        {
            final long available = this.getAvailableLongs();

            if ( available < requested )
            {
                this.logOutOfMemoryWarning( new Long( requested * 8 ),
                                            new Long( available * 8 ) );

                this.forceGarbageCollection(
                    this.getMaximumRetries() - retries );

            }
            else
            {
                ret = new long[ requested ];
            }

        }
        while ( ret == null && retries-- > 0 );

        if ( ret == null )
        {
            throw new OutOfMemoryError();
        }

        return ret;
    }

    public long getAvailableChars()
    {
        return this.getAvailableBytes() / 2;
    }

    public char[] allocateChars( int requested ) throws OutOfMemoryError
    {
        if ( requested < 0 )
        {
            throw new IllegalArgumentException( Integer.toString( requested ) );
        }

        char[] ret = null;
        int retries = this.getMaximumRetries();

        do
        {
            final long available = this.getAvailableChars();

            if ( available < requested )
            {
                this.logOutOfMemoryWarning( new Long( requested * 2 ),
                                            new Long( available * 2 ) );

                this.forceGarbageCollection(
                    this.getMaximumRetries() - retries );

            }
            else
            {
                ret = new char[ requested ];
            }

        }
        while ( ret == null && retries-- > 0 );

        if ( ret == null )
        {
            throw new OutOfMemoryError();
        }

        return ret;
    }

    public long getAvailableFloats()
    {
        return this.getAvailableBytes() / 4;
    }

    public float[] allocateFloats( int requested ) throws OutOfMemoryError
    {
        if ( requested < 0 )
        {
            throw new IllegalArgumentException( Integer.toString( requested ) );
        }

        float[] ret = null;
        int retries = this.getMaximumRetries();

        do
        {
            final long available = this.getAvailableFloats();

            if ( available < requested )
            {
                this.logOutOfMemoryWarning( new Long( requested * 4 ),
                                            new Long( available * 4 ) );

                this.forceGarbageCollection(
                    this.getMaximumRetries() - retries );

            }
            else
            {
                ret = new float[ requested ];
            }

        }
        while ( ret == null && retries-- > 0 );

        if ( ret == null )
        {
            throw new OutOfMemoryError();
        }

        return ret;
    }

    public long getAvailableDoubles()
    {
        return this.getAvailableBytes() / 8;
    }

    public double[] allocateDoubles( int requested ) throws OutOfMemoryError
    {
        if ( requested < 0 )
        {
            throw new IllegalArgumentException( Integer.toString( requested ) );
        }

        double[] ret = null;
        int retries = this.getMaximumRetries();

        do
        {
            final long available = this.getAvailableDoubles();

            if ( available < requested )
            {
                this.logOutOfMemoryWarning( new Long( requested * 8 ),
                                            new Long( available * 8 ) );

                this.forceGarbageCollection(
                    this.getMaximumRetries() - retries );

            }
            else
            {
                ret = new double[ requested ];
            }

        }
        while ( ret == null && retries-- > 0 );

        if ( ret == null )
        {
            throw new OutOfMemoryError();
        }

        return ret;
    }

    public long getAvailableBooleans()
    {
        return this.getAvailableBytes();
    }

    public boolean[] allocateBoolean( int requested ) throws OutOfMemoryError
    {
        if ( requested < 0 )
        {
            throw new IllegalArgumentException( Integer.toString( requested ) );
        }

        boolean[] ret = null;
        int retries = this.getMaximumRetries();

        do
        {
            final long available = this.getAvailableBooleans();

            if ( available < requested )
            {
                this.logOutOfMemoryWarning( new Long( requested ),
                                            new Long( available ) );

                this.forceGarbageCollection(
                    this.getMaximumRetries() - retries );

            }
            else
            {
                ret = new boolean[ requested ];
            }

        }
        while ( ret == null && retries-- > 0 );

        if ( ret == null )
        {
            throw new OutOfMemoryError();
        }

        return ret;
    }

    //-----------------------------------------------------------MemoryManager--
    //--DefaultMemoryManager----------------------------------------------------

    /** The maximum percent of memory for use by the implementation. */
    private Integer maximumPercent;

    /** The number of retries used when trying to free memory. */
    private Integer maximumRetries;

    /**
     * Creates a new {@code DefaultMemoryManager} instance taking the maximum
     * percent of memory for use by the implementation and the number of retries
     * used when trying to free memory.
     *
     * @param maximumPercent the maximum percent of memory for use by the
     * implementation.
     * @param maximumRetries the number of retries used when trying to free
     * memory.
     */
    public DefaultMemoryManager( final int maximumPercent,
                                 final int maximumRetries )
    {
        if ( maximumPercent >= 0 && maximumPercent <= 100 )
        {
            this.maximumPercent = new Integer( maximumPercent );
        }
        if ( maximumRetries > 0 )
        {
            this.maximumRetries = new Integer( maximumRetries );
        }
    }

    /**
     * Gets the value of property {@code maximumRetries}.
     *
     * @return the number of retries when trying to free memory.
     */
    private int getMaximumRetries()
    {
        if ( this.maximumRetries == null )
        {
            this.maximumRetries = this.getDefaultMaximumRetries();
        }

        return this.maximumRetries.intValue();
    }

    /**
     * Gets the value of property {@code maximumPercent}.
     *
     * @return the maximum percent of memory for use by the implementation.
     */
    private int getMaximumPercent()
    {
        if ( this.maximumPercent == null )
        {
            this.maximumPercent = this.getDefaultMaximumPercent();
        }

        return this.maximumPercent.intValue();
    }

    /**
     * Logs a warning message for out of memory conditions.
     *
     * @param requestedByte number of byte requested to be allocated.
     * @param availableByte number of byte available when {@code requestedByte}
     * were requested.
     */
    private void logOutOfMemoryWarning( final Long requestedByte,
                                        final Long availableByte )
    {
        this.getLogger().warn( this.getOutOfMemoryWarningMessage(
            this.getLocale(), availableByte, requestedByte ) );

    }

    /**
     * Forces garbage collection and logs a warning message.
     *
     * @param repetition number of times garbage collection was already forced.
     *
     * @see System#gc()
     */
    private void forceGarbageCollection( final int repetition )
    {
        this.getLogger().warn( this.getForcingGarbageCollectionMessage(
            this.getLocale(), new Integer( repetition ) ) );

        System.gc();
    }

    //----------------------------------------------------DefaultMemoryManager--
    //--Messages----------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausMessages
    // This section is managed by jdtaus-container-mojo.

    /**
     * Gets the text of message <code>outOfMemoryWarning</code>.
     * <blockquote><pre>Wenig Hauptspeicher (verfügbar: {1,number}, benötigt {0,number}).</pre></blockquote>
     * <blockquote><pre>Memory low (needed {0,number}, available {1,number}).</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     * @param neededMemory Needed number of bytes.
     * @param availableMemory Available bytes.
     *
     * @return Out of memory warning.
     */
    private String getOutOfMemoryWarningMessage( final Locale locale,
            final java.lang.Number neededMemory,
            final java.lang.Number availableMemory )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "outOfMemoryWarning", locale,
                new Object[]
                {
                    neededMemory,
                    availableMemory
                });

    }

    /**
     * Gets the text of message <code>forcingGarbageCollection</code>.
     * <blockquote><pre>Speicherbereinigung erzwungen ({0,number}).</pre></blockquote>
     * <blockquote><pre>Forcing garbage collection ({0,number}).</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     * @param retry Number of currently forced garbage collections.
     *
     * @return Information about a forced garbage collection.
     */
    private String getForcingGarbageCollectionMessage( final Locale locale,
            final java.lang.Number retry )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "forcingGarbageCollection", locale,
                new Object[]
                {
                    retry
                });

    }

// </editor-fold>//GEN-END:jdtausMessages

    //----------------------------------------------------------------Messages--
}

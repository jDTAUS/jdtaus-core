/*
 *  jDTAUS Core SPI
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
package org.jdtaus.core.lang.spi;

/**
 * Manages memory allocations.
 * <p>jDTAUS Core SPI {@code MemoryManager} specification to be used by
 * implementations when allocating memory.</p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 * @see org.jdtaus.core.container.Container
 */
public interface MemoryManager extends org.jdtaus.core.lang.Runtime
{
    //--MemoryManager-----------------------------------------------------------

    /**
     * Creates a new buffer of byte values.
     *
     * @param requested desired size of the buffer.
     *
     * @return a new buffer with length {@code requested}.
     *
     * @throws IllegalArgumentException if {@code requested} is negative.
     * @throws OutOfMemoryError if there is not enough memory available.
     */
    byte[] allocateBytes( int requested );

    /**
     * Creates a new buffer of short values.
     *
     * @param requested desired size of the buffer.
     *
     * @return a new buffer with length {@code requested}.
     *
     * @throws IllegalArgumentException if {@code requested} is negative.
     * @throws OutOfMemoryError if there is not enough memory available.
     */
    short[] allocateShorts( int requested );

    /**
     * Creates a new buffer of integer values.
     *
     * @param requested desired size of the buffer.
     *
     * @return a new buffer with length {@code requested}.
     *
     * @throws IllegalArgumentException if {@code requested} is negative.
     * @throws OutOfMemoryError if there is not enough memory available.
     */
    int[] allocateIntegers( int requested );

    /**
     * Creates a new buffer of long values.
     *
     * @param requested desired size of the buffer.
     *
     * @return a new buffer with length {@code requested}.
     *
     * @throws IllegalArgumentException if {@code requested} is negative.
     * @throws OutOfMemoryError if there is not enough memory available.
     */
    long[] allocateLongs( int requested );

    /**
     * Creates a new buffer of char values.
     *
     * @param requested desired size of the buffer.
     *
     * @return a new buffer with length {@code requested}.
     *
     * @throws IllegalArgumentException if {@code requested} is negative.
     * @throws OutOfMemoryError if there is not enough memory available.
     */
    char[] allocateChars( int requested );

    /**
     * Creates a new buffer of float values.
     *
     * @param requested desired size of the buffer.
     *
     * @return a new buffer with length {@code requested}.
     *
     * @throws IllegalArgumentException if {@code requested} is negative.
     * @throws OutOfMemoryError if there is not enough memory available.
     */
    float[] allocateFloats( int requested );

    /**
     * Creates a new buffer of double values.
     *
     * @param requested desired size of the buffer.
     *
     * @return a new buffer with length {@code requested}.
     *
     * @throws IllegalArgumentException if {@code requested} is negative.
     * @throws OutOfMemoryError if there is not enough memory available.
     */
    double[] allocateDoubles( int requested );

    /**
     * Creates a new buffer of boolean values.
     *
     * @param requested desired size of the buffer.
     *
     * @return a new buffer with length {@code requested}.
     *
     * @throws IllegalArgumentException if {@code requested} is negative.
     * @throws OutOfMemoryError if there is not enough memory available.
     */
    boolean[] allocateBoolean( int requested );

    //-----------------------------------------------------------MemoryManager--
}

/*
 *  jDTAUS Core API
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
package org.jdtaus.core.lang;

/**
 * Implementation runtime.
 * <p>The implementation runtime allows applications to interface with the
 * environment implementations are running in.</p>
 * <p>Example: Accessing the runtime of all jDTAUS Core SPI compliant
 * implementations in the system<br/><pre>
 * Runtime runtime =
 *     (Runtime) ContainerFactory.getContainer().
 *     getImplementation(Runtime.class, "jDTAUS Core SPI");
 * </pre></p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public interface Runtime
{
    //--Runtime-----------------------------------------------------------------

    /**
     * Computes the amount of allocated memory in percent.
     *
     * @return percent of memory currently allocated
     * (so that {@code 100 - getAllocatedPercent()}% of memory is currently
     * available to allocation).
     */
    long getAllocatedPercent();

    /**
     * Computes the maximum number of byte values currently being available to
     * allocation.
     *
     * @return number of byte values currently available to allocation.
     */
    long getAvailableBytes();

    /**
     * Computes the maximum number of short values currently being available to
     * allocation.
     *
     * @return number of short values currently available to allocation.
     */
    long getAvailableShorts();

    /**
     * Computes the maximum number of integer values currently being available
     * to allocation.
     *
     * @return number of integer values currently available to allocation.
     */
    long getAvailableIntegers();

    /**
     * Computes the maximum number of long values currently being available
     * to allocation.
     *
     * @return number of long values currently available to allocation.
     */
    long getAvailableLongs();

    /**
     * Computes the maximum number of char values currently being available
     * to allocation.
     *
     * @return number of char values currently available to allocation.
     */
    long getAvailableChars();

    /**
     * Computes the maximum number of float values currently being available
     * to allocation.
     *
     * @return number of float values currently available to allocation.
     */
    long getAvailableFloats();

    /**
     * Computes the maximum number of double values currently being available
     * to allocation.
     *
     * @return number of double values currently available to allocation.
     */
    long getAvailableDoubles();

    /**
     * Computes the maximum number of boolean values currently being available
     * to allocation.
     *
     * @return number of boolean values currently available to allocation.
     */
    long getAvailableBooleans();

    //-----------------------------------------------------------------Runtime--
}

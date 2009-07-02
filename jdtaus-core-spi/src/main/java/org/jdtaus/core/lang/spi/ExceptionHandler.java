/*
 *  jDTAUS Core SPI
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
package org.jdtaus.core.lang.spi;

import org.jdtaus.core.lang.ExceptionEvent;
import org.jdtaus.core.lang.ExceptionEventSource;

/**
 * Handles exceptions.
 * <p>jDTAUS Core SPI {@code ExceptionHandler} specification to be used by
 * implementations to handle exceptions.</p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public interface ExceptionHandler extends ExceptionEventSource
{
    //--ExceptionHandler--------------------------------------------------------

    /**
     * Gets all {@code ExceptionEvent}s handled by the handler.
     *
     * @return an array of all {@code ExceptionEvent}s handled by the handler.
     * @deprecated Starting with version 1.8, the {@code ExceptionListener}s
     * provided to a system should be used to gather information about handled
     * exceptions.
     */
     ExceptionEvent[] getExceptionEvents();

    /**
     * Handles a given exception.
     *
     * @param t the {@code Throwable} that was caught.
     *
     * @throws NullPointerException if {@code t} is {@code null}.
     */
    void handle( Throwable t );

    //--------------------------------------------------------ExceptionHandler--
}

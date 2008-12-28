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
 * Manages asynchronous executions.
 * <p>jDTAUS Core SPI {@code Executor} specification to be used by
 * implementations to perform operations asynchronously.</p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 * @see org.jdtaus.core.container.Container
 */
public interface Executor extends ExceptionHandler
{
    //--Executor----------------------------------------------------------------

    /**
     * Executes a given {@code Runnable} asynchronously.
     * <p>This method will notify any registered {@code ExceptionListener}s
     * about exceptions thrown by the given {@code Runnable}'s {@code run()}
     * method.</p>
     *
     * @param runnable the {@code Runnable} to execute asynchronously.
     *
     * @throws NullPointerException if {@code runnable} is {@code null}.
     */
    void executeAsynchronously( Runnable runnable );

    //----------------------------------------------------------------Executor--
}

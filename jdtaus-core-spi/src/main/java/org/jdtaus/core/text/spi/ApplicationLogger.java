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
package org.jdtaus.core.text.spi;

import org.jdtaus.core.text.MessageEvent;
import org.jdtaus.core.text.MessageEventSource;

/**
 * Logs messages to applications.
 * <p>jDTAUS Core SPI {@code ApplicationLogger} specification to be used by
 * implementations to log messages to applications.</p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 * @see org.jdtaus.core.container.Container
 */
public interface ApplicationLogger extends MessageEventSource
{
    //--ApplicationLogger-------------------------------------------------------

    /**
     * Logs a message to applications.
     *
     * @param e the event to provide to applications.
     *
     * @throws NullPointerException if {@code e} is {@code null}.
     */
    void log( MessageEvent e );

    //-------------------------------------------------------ApplicationLogger--
}

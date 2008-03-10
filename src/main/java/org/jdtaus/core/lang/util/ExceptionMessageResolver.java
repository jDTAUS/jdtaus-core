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
package org.jdtaus.core.lang.util;

import org.jdtaus.core.text.Message;

/**
 * Resolves {@code Message}s for given exceptions.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 *
 * @see ExceptionMessageProducer#onException(ExceptionEvent)
 */
public interface ExceptionMessageResolver
{
    //--ExceptionMessageResolver------------------------------------------------

    /**
     * Resolves {@code Message}s for a given exception.
     *
     * @param exception the exception to resolve any messages for.
     *
     * @return an array of application messages resolved for {@code exception}
     * or {@code null} if the implementation cannot resolve any application
     * messages for {@code exception}.
     *
     * @throws NullPointerException if {@code exception} is {@code null}.
     *
     * @see ExceptionMessageProducer#onException(ExceptionEvent)
     */
    Message[] resolve( Exception exception );

    //------------------------------------------------ExceptionMessageResolver--
}

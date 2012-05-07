/*
 *  jDTAUS Core API
 *  Copyright (C) 2005 Christian Schulte
 *  <schulte2005@users.sourceforge.net>
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
 * {@code ExceptionEventSource} is implemented by classes firing
 * {@code ExceptionEvent}s.
 * <p>Example: Listening to all jDTAUS Core SPI compliant implementations in the
 * system<blockquote><pre>
 * ExceptionEventSource source =
 *     (ExceptionEventSource) ContainerFactory.getContainer().
 *     getObject( ExceptionEventSource.class );
 *
 * source.addExceptionListener(<i>your application's listener</i>);
 * </pre></blockquote></p>
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $JDTAUS$
 */
public interface ExceptionEventSource
{
    //--ExceptionEventSource----------------------------------------------------

    /**
     * Adds an {@code ExceptionListener} to the listener list.
     *
     * @param listener The listener to be added to the listener list.
     *
     * @throws NullPointerException if {@code listener} is {@code null}.
     */
    void addExceptionListener( ExceptionListener listener );

    /**
     * Removes a {@code ExceptionListener} from the listener list.
     *
     * @param listener The listener to be removed from the listener list.
     *
     * @throws NullPointerException if {@code listener} is {@code null}.
     */
    void removeExceptionListener( ExceptionListener listener );

    /**
     * Gets all currently registered {@code ExceptionListener}s.
     *
     * @return all currently registered {@code ExceptionListener}s.
     */
    ExceptionListener[] getExceptionListeners();

    //----------------------------------------------------ExceptionEventSource--
}

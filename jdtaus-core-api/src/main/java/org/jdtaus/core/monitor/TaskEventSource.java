/*
 *  jDTAUS Core API
 *  Copyright (C) 2005 Christian Schulte
 *  <cs@schulte.it>
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
package org.jdtaus.core.monitor;

/**
 * {@code TaskEventSource} is implemented by classes firing
 * {@code TaskEvent}s.
 * <p>Example: Listening to all jDTAUS Core SPI compliant implementations in the
 * system<blockquote><pre>
 * TaskEventSource source =
 *     (TaskEventSource) ContainerFactory.getContainer().
 *     getObject( TaskEventSource.class );
 *
 * source.addTaskListener(<i>your application's listener</i>);
 * </pre></blockquote></p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
public interface TaskEventSource
{
    //--TaskEventSource---------------------------------------------------------

    /**
     * Adds a {@code TaskListener} to the listener list.
     *
     * @param listener The listener to be added to the listener list.
     *
     * @throws NullPointerException if {@code listener} is {@code null}.
     */
    void addTaskListener( TaskListener listener );

    /**
     * Removes a {@code TaskListener} from the listener list.
     *
     * @param listener The listener to be removed from the listener list.
     *
     * @throws NullPointerException if {@code listener} is {@code null}.
     */
    void removeTaskListener( TaskListener listener );

    /**
     * Gets all currently registered {@code TaskListener}s.
     *
     * @return all currently registered {@code TaskListener}s.
     */
    TaskListener[] getTaskListeners();

    //---------------------------------------------------------TaskEventSource--
}

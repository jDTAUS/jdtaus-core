/*
 *  jDTAUS Core SPI
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
package org.jdtaus.core.monitor.spi;

import org.jdtaus.core.monitor.Task;
import org.jdtaus.core.monitor.TaskEventSource;

/**
 * Monitors tasks.
 * <p>jDTAUS Core SPI {@code TaskMonitor} specification to be used by
 * implementations to provide {@code TaskEvent}s to applications.</p>
 * <p>Example: Monitoring a task<br/>
 * <pre>
 * Task task = new ConcreteTask();
 * try
 * {
 *   // Start monitoring.
 *   this.getTaskMonitor().monitor(task);
 *
 *   // Perform operations updating <i>task</i> which is now polled for changes.
 *   ...
 *
 * }
 * finally
 * {
 *   this.getTaskMonitor().finish(task);
 * }</pre></p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 * @see org.jdtaus.core.container.Container
 */
public interface TaskMonitor extends TaskEventSource
{
    //--TaskMonitor-------------------------------------------------------------

    /**
     * Starts monitoring a {@code Task}.
     *
     * @param task the task to monitor.
     *
     * @throws NullPointerException if {@code task} is {@code null}.
     */
    void monitor( Task task );

    /**
     * Stops monitoring a {@code Task}.
     *
     * @param task the task to stop monitoring.
     *
     * @throws NullPointerException if {@code task} is {@code null}.
     */
    void finish( Task task );

    //-------------------------------------------------------------TaskMonitor--
}

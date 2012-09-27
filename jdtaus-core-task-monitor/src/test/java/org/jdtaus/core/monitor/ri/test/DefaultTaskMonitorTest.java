/*
 *  jDTAUS Core RI Task Monitor
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
package org.jdtaus.core.monitor.ri.test;

import org.jdtaus.core.monitor.ri.DefaultTaskMonitor;
import org.jdtaus.core.monitor.spi.TaskMonitor;
import org.jdtaus.core.monitor.spi.it.TaskMonitorTest;

/**
 * Tests the {@link DefaultTaskMonitor} implementation.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class DefaultTaskMonitorTest extends TaskMonitorTest
{
    //--TaskMonitorTest---------------------------------------------------------

    /** The implementation to test. */
    private final TaskMonitor taskMonitor = new DefaultTaskMonitor();

    public TaskMonitor getTaskMonitor()
    {
        this.setTaskMonitor( this.taskMonitor );
        return super.getTaskMonitor();
    }

    //---------------------------------------------------------TaskMonitorTest--
}

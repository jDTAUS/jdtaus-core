/*
 *  jDTAUS Core API
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
package org.jdtaus.core.monitor;

import java.util.EventListener;

/**
 * Listener to be notified about {@code TaskEvent}s.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public interface TaskListener extends EventListener
{
    //--TaskListener------------------------------------------------------------

    /**
     * Called for events produced by {@code Task}s.
     *
     * @param event the event send by a {@code Task}.
     */
    void onTaskEvent( TaskEvent event );

    //------------------------------------------------------------TaskListener--
}

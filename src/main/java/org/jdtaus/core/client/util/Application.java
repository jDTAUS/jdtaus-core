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
package org.jdtaus.core.client.util;

import java.io.File;

/**
 * Client application.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public interface Application
{

    //--Application-------------------------------------------------------------

    /**
     * Gets the directory holding the executables of the application.
     *
     * @return the directory holding the executables of the application.
     */
    File getBinDirectory();

    /**
     * Gets the directory holding the configuration of the application.
     *
     * @return the directory holding the configuration of the application.
     */
    File getConfigDirectory();

    /**
     * Gets the directory holding the shared data of the application.
     *
     * @return the directory holding the shared data of the application.
     */
    File getDataDirectory();

    /**
     * Gets the directory holding the persistent state of the application.
     *
     * @return the directory holding the persistent state of the application.
     */
    File getStateDirectory();

    //-------------------------------------------------------------Application--

}

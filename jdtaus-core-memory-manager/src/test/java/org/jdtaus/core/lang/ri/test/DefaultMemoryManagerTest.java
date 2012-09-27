/*
 *  jDTAUS Core RI Memory Manager
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
package org.jdtaus.core.lang.ri.test;

import org.jdtaus.core.lang.ri.DefaultMemoryManager;
import org.jdtaus.core.lang.spi.MemoryManager;
import org.jdtaus.core.lang.spi.it.MemoryManagerTest;

/**
 * Tests the {@link DefaultMemoryManager} implementation.
 *
 * @author <a href="mailto:cs@schulte.it">Cest.java 2469 2007-04-11 03:03:04Z schulte2005 $
 */
public class DefaultMemoryManagerTest extends MemoryManagerTest
{
    //--MemoryManagerTest-------------------------------------------------------

    /** The implementation to test. */
    private final MemoryManager manager = new DefaultMemoryManager();

    public MemoryManager getMemoryManager()
    {
        this.setMemoryManager(this.manager);
        return super.getMemoryManager();
    }

    //-------------------------------------------------------MemoryManagerTest--
}

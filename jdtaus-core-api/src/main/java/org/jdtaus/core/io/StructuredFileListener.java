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
package org.jdtaus.core.io;

import java.io.IOException;
import java.util.EventListener;

/**
 * Listener for structural changes of {@code StructuredFile}s.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $JDTAUS$
 */
public interface StructuredFileListener extends EventListener
{
    //--StructuredFileListener--------------------------------------------------

    /**
     * Gets called whenever blocks were inserted into the {@code StructuredFile}
     * the listener is registered with. The block previously at {@code index}
     * will have moved to {@code index + insertedBlocks}.
     *
     * @param index the index of the first inserted block.
     * @param insertedBlocks the number of blocks which were inserted at
     * {@code index}.
     *
     * @throws IOException if reading or writing fails.
     */
    void blocksInserted( long index, long insertedBlocks ) throws IOException;

    /**
     * Gets called whenever blocks were deleted from the {@code StructuredFile}
     * the listener is registered with. The block previously at
     * {@code index + deletedBlocks} will have moved to {@code index}.
     *
     * @param index the index of the first deleted block.
     * @param deletedBlocks the number of blocks which were deleted starting
     * at {@code index} inclusive.
     *
     * @throws IOException if reading or writing fails.
     */
    void blocksDeleted( long index, long deletedBlocks ) throws IOException;

    //--------------------------------------------------StructuredFileListener--
}

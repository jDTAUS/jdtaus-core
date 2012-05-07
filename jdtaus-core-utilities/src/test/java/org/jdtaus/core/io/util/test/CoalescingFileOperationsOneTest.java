/*
 *  jDTAUS Core Utilities
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
package org.jdtaus.core.io.util.test;

import java.io.IOException;
import org.jdtaus.core.io.FileOperations;
import org.jdtaus.core.io.util.CoalescingFileOperations;

/**
 * Testcase for {@code CoalescingFileOperations} implementations.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class CoalescingFileOperationsOneTest extends FlushableFileOperationsTest
{
    //--FileOperationsTest------------------------------------------------------

    public FileOperations getFileOperations()
    {
        try
        {
            return new CoalescingFileOperations(
                this.getMemoryFileOperations(), 1 );

        }
        catch ( IOException e )
        {
            throw new AssertionError( e );
        }
    }

    //------------------------------------------------------FileOperationsTest--
}

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
import org.jdtaus.core.io.StructuredFile;
import org.jdtaus.core.io.it.StructuredFileTest;
import org.jdtaus.core.io.util.MemoryFileOperations;
import org.jdtaus.core.io.util.ReadAheadFileOperations;
import org.jdtaus.core.io.util.StructuredFileOperations;

/**
 * Testcase for {@code StructuredFileOperations} implementations.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class StructuredFileOperationsReadAheadTest extends StructuredFileTest
{
    //--StructuredFileTest------------------------------------------------------

    /** {@code FileOperations} backing the {@code StructuredFile}. */
    private final MemoryFileOperations memOps = new MemoryFileOperations();

    /** {@code StructuredFile} implementation being tested. */
    private StructuredFileOperations structuredFile;

    protected byte[] getStructuredData()
    {
        try
        {
            this.structuredFile.flush();
            return this.memOps.getData();
        }
        catch ( IOException e )
        {
            throw new AssertionError( e );
        }
    }

    protected StructuredFile getStructuredFile()
    {
        try
        {
            if ( this.structuredFile == null )
            {
                this.structuredFile = new StructuredFileOperations(
                    StructuredFileTest.BLOCK_SIZE,
                    new ReadAheadFileOperations( this.memOps ) );

            }

            return this.structuredFile;
        }
        catch ( IOException e )
        {
            throw new AssertionError( e );
        }
    }

    //------------------------------------------------------StructuredFileTest--
}

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
package org.jdtaus.core.io.util.test;

import java.io.ByteArrayOutputStream;
import org.jdtaus.core.io.FileOperations;
import org.jdtaus.core.io.it.FileOperationsTest;
import org.jdtaus.core.io.util.MemoryFileOperations;

/**
 * Testcase for {@code MemoryFileOperations} implementations.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class MemoryFileOperationsTest extends FileOperationsTest
{
    //--FileOperationsTest------------------------------------------------------
    
    public FileOperations getFileOperations()
    {
        return new MemoryFileOperations();
    }
    
    //------------------------------------------------------FileOperationsTest--
    //--MemoryFileOperationsTest------------------------------------------------
    
    /**
     * Tests the {@link FileOperations#read(OutputStream}} and
     * {@link FileOperations#write(InputStream)} methods.
     * <p><ol>
     * <li>Writes a testfile from an {@code InputStream} to the file, then reads
     * the file contents into a {@code ByteArrayOutputStream} and checks
     * that the read data matches the written data.</li>
     * </ol></p>
     */
    public void testStreaming() throws Exception
    {
        final MemoryFileOperations ops = new MemoryFileOperations();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        ops.write(this.getTestFile());
        this.assertValidTestFile(new String(ops.getData(), "UTF-8"));
        ops.read(out);
        out.close();
        this.assertValidTestFile(new String(out.toByteArray(), "UTF-8"));
    }
    
    //------------------------------------------------MemoryFileOperationsTest--
    
}

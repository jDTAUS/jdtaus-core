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
package org.jdtaus.core.client.util.test;

import java.util.Locale;
import junit.framework.Assert;
import org.jdtaus.core.client.util.IconSet;

/**
 * Testcase for {@code IconSet} implementations.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public abstract class IconSetTest
{
    //--IconSetTest-------------------------------------------------------------

    /** The implementation to test. */
    private IconSet iconSet;

    /**
     * Gets the {@code IconSet} implementation tests are performed with.
     *
     * @return the {@code IconSet} implementation tests are performed
     * with.
     */
    public IconSet getIconSet()
    {
        return this.iconSet;
    }

    /**
     * Sets the {@code IconSet} implementation to test.
     *
     * @param value the {@code IconSet} implementation to test.
     */
    public final void setIconSet(final IconSet value)
    {
        this.iconSet = value;
    }

    //-------------------------------------------------------------IconSetTest--
    //--Tests-------------------------------------------------------------------

    /**
     * Tests that an implementation does provide all icons it supports.
     */
    public void testGetIcon()
    {
        assert this.getIconSet() != null;

        String[] names = this.getIconSet().getSupportedIcons();
        for(int i = names.length - 1; i >= 0; i--)
        {
            Assert.assertNotNull(this.getIconSet().getIcon(names[i],
                IconSet.SIZE_16X16, Locale.getDefault()));

            Assert.assertNotNull(this.getIconSet().getIcon(names[i],
                IconSet.SIZE_24X24, Locale.getDefault()));

        }
    }

    //-------------------------------------------------------------------Tests--
}

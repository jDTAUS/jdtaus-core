/*
 *  jDTAUS Core RI Servlet Container
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
package org.jdtaus.core.container.ri.servlet;

import java.util.Locale;

/**
 * {@code java.util.Locale} implementation.
 * <p>This class overwrites the automatically discovered default
 * {@code java.util.Locale} implementation and is used to provide the locale of
 * the current servlet request in service.</p>
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $Id$
 */
public class ServletRequestLocale
{
    //--Constructors------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausConstructors
    // This section is managed by jdtaus-container-mojo.

    /** Standard implementation constructor <code>org.jdtaus.core.container.ri.servlet.ServletRequestLocale</code>. */
    public ServletRequestLocale()
    {
        super();
    }

// </editor-fold>//GEN-END:jdtausConstructors

    //------------------------------------------------------------Constructors--
    //--ServletRequestLocale----------------------------------------------------

    /**
     * Gets the current thread's locale from the servlet filter.
     *
     * @return The current thread's locale from the servlet filter.
     *
     * @see ServletFilter#getLocale()
     */
    public Locale java_util_Locale()
    {
        return ServletFilter.getLocale();
    }

    //----------------------------------------------------ServletRequestLocale--
}

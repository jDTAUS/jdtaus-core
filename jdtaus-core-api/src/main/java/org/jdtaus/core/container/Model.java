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
package org.jdtaus.core.container;

/**
 * Object management and configuration model.
 * <p>The object management and configuration model destinguishes between
 * specifications and implementations defined in modules. Specifications specify
 * some public programming interface to be implemented by implementations.
 * Implementations may define configuration properties and may depend on other
 * implementations. When defining dependencies, properties defined for the
 * implementation of the dependency may be overwritten by properties defined for
 * the dependency.</p>
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $Id$
 *
 * @see ModelFactory
 */
public interface Model
{
    //--Model-------------------------------------------------------------------

    /**
     * Gets the modules of the model.
     *
     * @return the modules of the model.
     *
     * @throws ModelError for unrecoverable model errors.
     */
    Modules getModules();

    //-------------------------------------------------------------------Model--
}

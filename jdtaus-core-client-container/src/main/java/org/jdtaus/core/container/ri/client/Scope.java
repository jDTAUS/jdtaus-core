/*
 *  jDTAUS Core RI Client Container
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
package org.jdtaus.core.container.ri.client;

/**
 * Scope a specification applies to.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $JDTAUS$
 */
interface Scope
{

    /**
     * Gets an object from the scope.
     *
     * @param identifier The identifier of the object to get from the scope.
     *
     * @return The object identified by {@code identifier} or {@code null} if
     * no object identified by {@code identifier} exists in the scope.
     */
    Object getObject( String identifier );

    /**
     * Puts an object into the scope.
     *
     * @param identifier The identifier of the object to put into the scope.
     * @param object The object to put into the scope.
     *
     * @return The previous object from the scope or {@code null} if there was
     * no object identified by {@code identifier} in the scope.
     */
    Object putObject( String identifier, Object object );

    /**
     * Removes an object from the scope.
     *
     * @param identifier The identifier of the object to remove from the scope.
     *
     * @return The removed object or {@code null} if there was no object
     * identified by {@code identifier} in the scope.
     */
    Object removeObject( String identifier );

}

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
package org.jdtaus.core.container;

/**
 * Context initialization.
 * <p>By implementing this interface implementations indicate that state may be
 * bound to a client.</p>
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $JDTAUS$
 * @deprecated Removed without replacement. An implementation's scope is
 * specified by the specifications it implements.
 */
public interface ContextInitializer
{
    //--ContextInitializer------------------------------------------------------

    /**
     * Checks if a context is initialized.
     *
     * @param ctx the context to check.
     *
     * @return {@code true} if {@code ctx} is initialized; {@code false} if not.
     *
     * @throws NullPointerException if {@code ctx} is {@code null}.
     * @throws ContextError for unrecoverable context errors.
     */
    boolean isInitialized( Context ctx );

    /**
     * Initializes a context.
     *
     * @param ctx the context to initialze.
     *
     * @throws NullPointerException if {@code ctx} is {@code null}.
     * @throws ContextError for unrecoverable context errors.
     */
    void initialize( Context ctx );

    //------------------------------------------------------ContextInitializer--
}

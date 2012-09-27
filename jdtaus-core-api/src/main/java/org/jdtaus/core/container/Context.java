/*
 *  jDTAUS Core API
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
package org.jdtaus.core.container;

import java.io.Serializable;
import java.util.Collection;

/**
 * Object context.
 * <p>The context stores key-value pairs bound to a client. For every method
 * invocation identical clients get identical contexts.</p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 *
 * @see ContextFactory
 */
public interface Context
{
    //--Context-----------------------------------------------------------------

    /**
     * Gets an attribute from the context.
     *
     * @param key Key of the attribute to return.
     *
     * @return The attribute with key {@code key} or {@code null} if no
     * such attribute is held by the context. Returning {@code null} may also
     * indicate that the context associated {@code null} with the specified key
     * if the implementation supports {@code null} values.
     *
     * @throws NullPointerException if {@code key} is {@code null}.
     * @deprecated Replaced by {@link #getObject(java.lang.String)}.
     */
     Object getAttribute( String key );

    /**
     * Sets an attribute in the context.
     *
     * @param key Key of the attribute to store {@code o} with.
     * @param o Object to store with key {@code key}.
     *
     * @return The previous value associated with {@code key} or {@code null}
     * if there was no attribute for {@code key}. Returning {@code null} may
     * also indicate that the context previously associated {@code null} with
     * the specified key if the implementation supports {@code null} values.
     *
     * @throws NullPointerException if {@code key} is {@code null}.
     * @deprecated Replaced by {@link #setObject(java.lang.String, java.lang.Object)}.
     */
     Object setAttribute( String key, Serializable o );

    /**
     * Removes an attribute from the context.
     *
     * @param key Key of the attribute to remove.
     *
     * @return The previous value associated with {@code key} or {@code null}
     * if there was no attribute for {@code key}. Returning {@code null} may
     * also indicate that the context previously associated {@code null} with
     * the specified key if the implementation supports {@code null} values.
     *
     * @throws NullPointerException if {@code key} is {@code null}.
     * @deprecated Replaced by {@link #removeObject(java.lang.String)}.
     */
     Object removeAttribute( String key );

    /**
     * Gets a collection containing all object keys from the context.
     *
     * @return A collection containing all object keys from the context.
     */
    Collection getObjectKeys();

    /**
     * Gets an object from the context.
     *
     * @param key Key of the object to return.
     *
     * @return The object with key {@code key} or {@code null} if no
     * such object is held by the context. Returning {@code null} may also
     * indicate that the context associated {@code null} with the specified key
     * if the implementation supports {@code null} values.
     *
     * @throws NullPointerException if {@code key} is {@code null}.
     */
    Object getObject( String key );

    /**
     * Sets an object in the context.
     *
     * @param key Key to store {@code o} with.
     * @param o Object to store with key {@code key}.
     *
     * @return The previous value associated with {@code key} or {@code null}
     * if there was no object for {@code key}. Returning {@code null} may
     * also indicate that the context previously associated {@code null} with
     * the specified key if the implementation supports {@code null} values.
     *
     * @throws NullPointerException if {@code key} is {@code null}.
     */
    Object setObject( String key, Object o );

    /**
     * Removes an object from the context.
     *
     * @param key Key of the object to remove.
     *
     * @return The previous value associated with {@code key} or {@code null}
     * if there was no object for {@code key}. Returning {@code null} may
     * also indicate that the context previously associated {@code null} with
     * the specified key if the implementation supports {@code null} values.
     *
     * @throws NullPointerException if {@code key} is {@code null}.
     */
    Object removeObject( String key );

    //-----------------------------------------------------------------Context--
}

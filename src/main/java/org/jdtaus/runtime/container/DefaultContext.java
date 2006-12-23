/*
 *  jDTAUS - DTAUS fileformat.
 *  Copyright (C) 2005 Christian Schulte <cs@schulte.it>
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
package org.jdtaus.runtime.container;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.jdtaus.common.container.Context;

/**
 * Default {@code Context} implementation.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 *
 * @see org.jdtaus.common.container.ContextFactory
 */
public class DefaultContext implements Serializable, Context {

    //--Constructors------------------------------------------------------------

    /** Protected default constructor. */
    protected DefaultContext() {
    }

    //------------------------------------------------------------Constructors--
    //--Context-----------------------------------------------------------------

    public final Object getAttribute(final String key) {
        if(key == null) {
            throw new NullPointerException("key");
        }

        return this.getMap().get(key);
    }

    public final Object setAttribute(final String key, final Serializable o) {
        if(key == null) {
            throw new NullPointerException("key");
        }

        return this.getMap().put(key, o);
    }

    public final Object removeAttribute(final String key) {
        if(key == null) {
            throw new NullPointerException("key");
        }

        return this.getMap().remove(key);
    }

    //-----------------------------------------------------------------Context--
    //--DefaultContext----------------------------------------------------------

    /**
     * {@code Map} holding the key-value pairs.
     * @serial
     */
    private final Map map = new HashMap(100);

    /**
     * Accessor to the {@code Map} backing the instance.
     *
     * @return map holding the context's key-value pairs.
     */
    protected Map getMap() {
        return this.map;
    }

    //----------------------------------------------------------DefaultContext--

}

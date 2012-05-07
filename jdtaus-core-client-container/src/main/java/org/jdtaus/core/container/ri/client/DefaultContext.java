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

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.jdtaus.core.container.Context;

/**
 * {@code Context} reference implementation.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $JDTAUS$
 *
 * @see org.jdtaus.core.container.ContextFactory
 */
public class DefaultContext implements Serializable, Context
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.0.x classes. */
    private static final long serialVersionUID = -5373539407961564598L;

    //---------------------------------------------------------------Constants--
    //--Context-----------------------------------------------------------------

    public Collection getObjectKeys()
    {
        return Collections.unmodifiableCollection( this.getMap().keySet() );
    }

    public Object getObject( final String key )
    {
        if ( key == null )
        {
            throw new NullPointerException( "key" );
        }

        return this.getMap().get( key );
    }

    public Object setObject( final String key, final Object o )
    {
        if ( key == null )
        {
            throw new NullPointerException( "key" );
        }

        return this.getMap().put( key, o );
    }

    public Object removeObject( final String key )
    {
        if ( key == null )
        {
            throw new NullPointerException( "key" );
        }

        return this.getMap().remove( key );
    }

    public final Object getAttribute( final String key )
    {
        return this.getObject( key );
    }

    public final Object setAttribute( final String key, final Serializable o )
    {
        return this.setObject( key, o );
    }

    public final Object removeAttribute( final String key )
    {
        return this.removeObject( key );
    }

    //-----------------------------------------------------------------Context--
    //--DefaultContext----------------------------------------------------------

    /**
     * {@code Map} holding the key-value pairs.
     * @serial
     */
    private final Map map = new HashMap( 100 );

    /**
     * Gets the {@code Map} backing the instance.
     *
     * @return map holding the context's key-value pairs.
     */
    protected Map getMap()
    {
        return this.map;
    }

    //----------------------------------------------------------DefaultContext--
}

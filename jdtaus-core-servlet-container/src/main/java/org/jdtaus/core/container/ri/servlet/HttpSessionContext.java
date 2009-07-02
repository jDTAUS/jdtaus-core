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

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import javax.servlet.http.HttpSession;
import org.jdtaus.core.container.Context;

/**
 * HTTP session {@code Context} implementation.
 * <p>This implementation needs to be configured manually for use by
 * {@link org.jdtaus.core.container.ContextFactory#newContext()}. See the
 * factory for how to configure this class to be used.</p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 *
 * @see ServletFilter
 */
public class HttpSessionContext implements Context
{
    //--Context-----------------------------------------------------------------

    public final Collection getObjectKeys()
    {
        return Collections.list( this.getSession().getAttributeNames() );
    }

    public final Object getObject( final String key )
    {
        if ( key == null )
        {
            throw new NullPointerException( "key" );
        }

        return this.getSession().getAttribute( key );
    }

    public final Object setObject( final String key, final Object o )
    {
        if ( key == null )
        {
            throw new NullPointerException( "key" );
        }

        final Object old = this.getSession().getAttribute( key );
        this.getSession().setAttribute( key, o );

        return old;
    }

    public final Object removeObject( final String key )
    {
        if ( key == null )
        {
            throw new NullPointerException( "key" );
        }

        final Object old = this.getSession().getAttribute( key );
        this.getSession().removeAttribute( key );

        return old;
    }

    public final Object getAttribute( final String key )
    {
        return this.getObject( key );
    }

    public final Object setAttribute( final String key, final Serializable o )
    {
        return this.setObject( key, o );
    }

    public final Object removeAttribute( String key )
    {
        return this.removeObject( key );
    }

    //-----------------------------------------------------------------Context--
    //--HttpSessionContext------------------------------------------------------

    /** @see ServletFilter#getHttpSession() */
    public HttpSession getSession()
    {
        return ServletFilter.getHttpSession();
    }

    //------------------------------------------------------HttpSessionContext--
}

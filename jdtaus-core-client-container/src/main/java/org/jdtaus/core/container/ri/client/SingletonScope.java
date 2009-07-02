/*
 *  jDTAUS Core RI Client Container
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
package org.jdtaus.core.container.ri.client;

import java.util.HashMap;

/**
 * Singleton scope.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
class SingletonScope extends HashMap implements Scope
{

    /**
     * Creates a new {@code SingletonScope} instance initializing the instance
     * with an initial capacity of {@code 1024}.
     */
    SingletonScope()
    {
        super( 1024 );
    }

    /**
     * {@inheritDoc}
     *
     * @see HashMap#get(java.lang.Object)
     */
    public Object getObject( final String identifier )
    {
        return this.get( identifier );
    }

    /**
     * {@inheritDoc}
     *
     * @see HashMap#put(java.lang.Object, java.lang.Object)
     */
    public Object putObject( final String identifier, final Object object )
    {
        return this.put( identifier, object );
    }

    /**
     * {@inheritDoc}
     *
     * @see HashMap#remove(java.lang.Object)
     */
    public Object removeObject( final String identifier )
    {
        return this.remove( identifier );
    }

}

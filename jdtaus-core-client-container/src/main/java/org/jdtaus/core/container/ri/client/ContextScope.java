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

import org.jdtaus.core.container.Context;
import org.jdtaus.core.container.ContextFactory;

/**
 * Context scope.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 *
 * @see org.jdtaus.core.container.ContainerFactory
 */
class ContextScope implements Scope
{

    /** Creates a new {@code ContextScope} instance. */
    ContextScope()
    {
        super();
    }

    /**
     * {@inheritDoc}
     *
     * @see Context#getObject(java.lang.String)
     */
    public Object getObject( final String identifier )
    {
        return ContextFactory.getContext().getObject( identifier );
    }

    /**
     * {@inheritDoc}
     *
     * @see Context#setObject(java.lang.String, java.lang.Object)
     */
    public Object putObject( final String identifier, final Object object )
    {
        return ContextFactory.getContext().setObject( identifier, object );
    }

    /**
     * {@inheritDoc}
     *
     * @see Context#removeObject(java.lang.String)
     */
    public Object removeObject( final String identifier )
    {
        return ContextFactory.getContext().removeObject( identifier );
    }

}

/*
 *  jDTAUS Core RI Servlet Container
 *  Copyright (c) 2005 Christian Schulte
 *
 *  Christian Schulte, Haldener Strasse 72, 58095 Hagen, Germany
 *  <cs@jdtaus.org> (+49 2331 3543887)
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

import javax.servlet.ServletContext;
import org.jdtaus.core.container.Container;
import org.jdtaus.core.container.ContainerFactory;
import org.jdtaus.core.container.Context;
import org.jdtaus.core.container.ContextFactory;
import org.jdtaus.core.container.Model;
import org.jdtaus.core.container.ModelFactory;

/**
 * Factories using {@code ServletContext}s for singleton instances.
 * <p>This class provides implementations for the
 * {@link ContainerFactory#getContainer()}, {@link ContextFactory#getContext()}
 * and {@link ModelFactory#getModel()} methods. See the factories for how
 * to configure this class to be used.</p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 *
 * @see ServletFilter
 */
public abstract class ServletContextFactories
{
    //--ServletContextFactories-------------------------------------------------

    /** @see ContainerFactory#getContainer() */
    public static Container getContainer()
    {
        Container instance =
            (Container) ServletContextFactories.getServletContext().
            getAttribute( Container.class.getName() );

        if ( instance == null )
        {
            instance = ContainerFactory.newContainer();
            ServletContextFactories.getServletContext().
                setAttribute( Container.class.getName(), instance );

        }

        return instance;
    }

    /** @see ContextFactory#getContext() */
    public static Context getContext()
    {
        Context instance =
            (Context) ServletContextFactories.getServletContext().
            getAttribute( Context.class.getName() );

        if ( instance == null )
        {
            instance = ContextFactory.newContext();
            ServletContextFactories.getServletContext().
                setAttribute( Context.class.getName(), instance );

        }

        return instance;
    }

    /** @see ModelFactory#getModel() */
    public static Model getModel()
    {
        Model instance =
            (Model) ServletContextFactories.getServletContext().
            getAttribute( Model.class.getName() );

        if ( instance == null )
        {
            instance = ModelFactory.newModel();
            ServletContextFactories.getServletContext().
                setAttribute( Model.class.getName(), instance );

        }

        return instance;
    }

    /** @see ServletFilter#getServletContext() */
    public static ServletContext getServletContext()
    {
        return ServletFilter.getServletContext();
    }

    //-------------------------------------------------ServletContextFactories--
}

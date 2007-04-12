/*
 *  jDTAUS - DTAUS fileformat.
 *  Copyright (c) 2005 Christian Schulte <cs@schulte.it>
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
package org.jdtaus.core.client.util.spi;

import java.awt.Component;
import java.util.EmptyStackException;
import java.util.Stack;
import org.jdtaus.core.client.util.UserInterface;
import org.jdtaus.core.container.Dependency;
import org.jdtaus.core.container.Implementation;
import org.jdtaus.core.container.ModelFactory;

/**
 * {@code UserInterface} reference implementation.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class DefaultUserInterface implements UserInterface
{

    //--Implementation----------------------------------------------------------

    // This section is generated by jdtaus-source-plugin.

    /** Meta-data describing the implementation. */
    private static final Implementation META =
        ModelFactory.getModel().getModules().
        getImplementation(DefaultUserInterface.class.getName());

    //----------------------------------------------------------Implementation--
    //--Constructors------------------------------------------------------------

    // This section is generated by jdtaus-source-plugin.

    /** Protected <code>DefaultUserInterface</code> implementation constructor.
    * @param meta Implementation meta-data.
    */
    protected DefaultUserInterface(final Implementation meta)
    {
        super();
    }
    /** Protected <code>DefaultUserInterface</code> dependency constructor.
    * @param meta Dependency meta-data.
    */
    protected DefaultUserInterface(final Dependency meta)
    {
        super();
    }

    //------------------------------------------------------------Constructors--
    //--Dependencies------------------------------------------------------------

    // This section is generated by jdtaus-source-plugin.


    //------------------------------------------------------------Dependencies--
    //--Properties--------------------------------------------------------------

    // This section is generated by jdtaus-source-plugin.


    //--------------------------------------------------------------Properties--
    //--UserInterface-----------------------------------------------------------

    /**
     * {@inheritDoc}
     *
     * @throws EmptyStackException if the parent component stack is empty.
     */
    public Component getParent() throws EmptyStackException
    {
        return (Component) this.components.peek();
    }

    //-----------------------------------------------------------UserInterface--
    //--DefaultUserInterface----------------------------------------------------

    /**
     * Stack holding parent components.
     */
    private Stack components = new Stack();

    /**
     * Pushes a component onto the stack of parent components.
     *
     * @param component  the current parent component.
     *
     * @throws NullPointerException if {@code component} is {@code null}.
     */
    public final void push(final Component component)
    {
        if(component == null)
        {
            throw new NullPointerException("component");
        }

        this.components.push(component);
    }

    /**
     * Pops the current parent component off the stack of parent components.
     *
     * @return the current parent component popped off the stack.
     *
     * @throws EmptyStackException if the parent component stack is empty.
     */
    public final Component pop()
    {
        return (Component) this.components.pop();
    }

    //----------------------------------------------------DefaultUserInterface--

}
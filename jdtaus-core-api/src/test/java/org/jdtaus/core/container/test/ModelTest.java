/*
 *  jDTAUS Core API
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
package org.jdtaus.core.container.test;

import junit.framework.Assert;
import org.jdtaus.core.container.Dependencies;
import org.jdtaus.core.container.Dependency;
import org.jdtaus.core.container.Implementation;
import org.jdtaus.core.container.Implementations;
import org.jdtaus.core.container.Module;
import org.jdtaus.core.container.Modules;
import org.jdtaus.core.container.Properties;
import org.jdtaus.core.container.Property;
import org.jdtaus.core.container.Specification;
import org.jdtaus.core.container.Specifications;

/**
 * Unit tests for the container model.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $Id$
 */
public class ModelTest
{
    //--ModelTest---------------------------------------------------------------

    /**
     * Creates a linked test model.
     *
     * @return a linked test model.
     */
    public static final Modules getTestModel()
    {
        final Specification spec = new Specification();
        final Specifications specs = new Specifications();
        final Implementation impl = new Implementation();
        final Implementations impls = new Implementations();
        final Dependency dep = new Dependency();
        final Dependencies deps = new Dependencies();
        final Property prop = new Property();
        final Properties props = new Properties();
        final Module mod = new Module();
        final Modules mods = new Modules();

        spec.setIdentifier( Specification.class.getName() );
        impl.setIdentifier( Implementation.class.getName() );
        impl.setName( Implementation.class.getName() );
        dep.setName( Dependency.class.getName() );
        prop.setName( Property.class.getName() );
        prop.setType( String.class );
        mod.setName( Module.class.getName() );

        specs.setSpecifications( new Specification[] { spec } );
        impls.setImplementations( new Implementation[] { impl } );
        deps.setDependencies( new Dependency[] { dep } );
        props.setProperties( new Property[] { prop } );
        mods.setModules( new Module[] { mod } );

        spec.setImplementations( impls );
        impl.setDependencies( deps );
        impl.setImplementedSpecifications( specs );
        impl.setProperties( props );
        dep.setImplementation( impl );
        dep.setProperties( props );
        dep.setSpecification( spec );
        mod.setImplementations( impls );
        mod.setProperties( props );
        mod.setSpecifications( specs );

        return mods;
    }

    //---------------------------------------------------------------ModelTest--
    //--Tests-------------------------------------------------------------------

    /**
     * Tests the {@code equals(Object)}, {@code hashCode()}, {@code toString()}
     * and {@code clone()} methods of all model objects.
     */
    public void testObject() throws Exception
    {
        final Modules mods = ModelTest.getTestModel();

        Assert.assertEquals( mods, mods );
        Assert.assertEquals( mods.hashCode(), mods.hashCode() );
        System.out.println( mods );
        mods.clone();

        final Specifications specs = mods.getSpecifications();

        Assert.assertEquals( specs, specs );
        Assert.assertEquals( specs.hashCode(), specs.hashCode() );
        System.out.println( specs );
        specs.clone();

        final Implementations impls = mods.getImplementations();

        Assert.assertEquals( impls, impls );
        Assert.assertEquals( impls.hashCode(), impls.hashCode() );
        System.out.println( impls );
        impls.clone();
    }

    //-------------------------------------------------------------------Tests--
}

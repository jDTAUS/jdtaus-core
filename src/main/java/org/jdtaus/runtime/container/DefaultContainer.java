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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import org.jdtaus.common.container.Container;
import org.jdtaus.common.container.ContainerError;
import org.jdtaus.common.container.ContainerInitializer;
import org.jdtaus.common.container.ContextFactory;
import org.jdtaus.common.container.ContextInitializer;
import org.jdtaus.common.container.Dependency;
import org.jdtaus.common.container.Implementation;
import org.jdtaus.common.container.ModelFactory;
import org.jdtaus.common.container.Specification;

/**
 * Default {@code Container} implementation.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class DefaultContainer implements Container {

    //--Constants---------------------------------------------------------------

    /** Empty Class-Array. */
    private static final Class[] EMPTY = {};

    /** Implementation Class-Array. */
    private static final Class[] IMPL_CTOR = { Implementation.class };

    /** Dependency Class-Array. */
    private static final Class[] DEP_CTOR = { Dependency.class };

    //---------------------------------------------------------------Constants--
    //--Constructors------------------------------------------------------------

    /** Protected default constructor. */
    protected DefaultContainer() {
    }

    //------------------------------------------------------------Constructors--
    //--Container---------------------------------------------------------------

    /** Maps specification identifiers to a map of implementation instances. */
    private final Map singletons = new HashMap(100);

    public final Object getImplementation(final Class specification,
        final String implementationName) throws ContainerError {

        Map map;
        Object instance = null;
        Implementation impl = null;
        Specification spec = null;

        if(specification == null) {
            throw new NullPointerException("specification");
        }
        if(implementationName == null) {
            throw new NullPointerException("implementationName");
        }

        // Find the implementation.
        spec = ModelFactory.getModel().getModules().
            getSpecification(specification.getName());

        if(spec == null) {
            throw new ContainerError(specification.getName());
        }

        impl = spec.getImplementation(implementationName);
        if(impl == null) {
            throw new ContainerError(implementationName);
        }

        if(spec.isSingleton()) {
            // Return the singleton instance for impl.
            map = (Map) this.singletons.get(specification);
            if(map == null) {
                map = new HashMap(100);
                this.singletons.put(specification, map);
            }

            instance = map.get(impl.getIdentifier());
            if(instance == null) {
                instance = this.instantiateImplementation(impl);
                map.put(impl.getIdentifier(), instance);
            }
        } else {
            // Create a new instance.
            instance = this.instantiateImplementation(impl);
        }

        DefaultContainer.initializeContext(instance);
        return instance;
    }

    public final Object getDependency(final Class implementation,
        final String dependencyName) throws ContainerError {

        Map map;
        final Implementation impl;
        Object instance = null;

        if(implementation == null) {
            throw new NullPointerException("identifier");
        }
        impl = ModelFactory.getModel().getModules().
            getImplementation(implementation.getName());

        if(impl == null) {
            throw new ContainerError(implementation.getName());
        }

        final Dependency dep = impl.getDependencies().
            getDependency(dependencyName);

        if(dep == null) {
            throw new ContainerError(dependencyName);
        }

        if(dep.getSpecification().isSingleton()) {
            // Return the singleton instance for impl.
            map = (Map) this.singletons.get(dep.getSpecification().
                getIdentifier());

            if(map == null) {
                map = new HashMap(100);
                this.singletons.put(dep.getSpecification().
                    getIdentifier(), map);

            }

            instance = map.get(dep.getImplementation().getIdentifier());
            if(instance == null) {
                instance = this.instantiateDependency(dep);
                map.put(dep.getImplementation().getIdentifier(), instance);
            }
        } else {
            instance = this.instantiateDependency(dep);
        }

        DefaultContainer.initializeContext(instance);
        return instance;
    }

    //---------------------------------------------------------------Container--
    //--DefaultContainer--------------------------------------------------------

    static ClassLoader getClassLoader() throws ContainerError {
        ClassLoader classLoader = Thread.currentThread().
            getContextClassLoader();

        if(classLoader == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }

        if(classLoader == null) {
            throw new ContainerError("classLoader");
        }

        return classLoader;
    }

    static Class loadClass(final String name) throws ContainerError {
        try {
            return DefaultContainer.getClassLoader().loadClass(name);
        } catch (ClassNotFoundException e) {
            throw new ContainerError(e);
        }
    }

    protected Object instantiateImplementation(final Implementation impl) {
        final Object instance;
        final Constructor ctor;
        final Class clazz = DefaultContainer.loadClass(impl.getIdentifier());

        try {
            ctor = clazz.getDeclaredConstructor(DefaultContainer.IMPL_CTOR);
            ctor.setAccessible(true);
            instance = ctor.newInstance(new Object[] { impl });
            DefaultContainer.initialize(instance);
            return instance;
        } catch(NoSuchMethodException e) {
            throw new ContainerError(e);
        } catch (IllegalArgumentException e) {
            throw new ContainerError(e);
        } catch (InvocationTargetException e) {
            throw new ContainerError(e.getTargetException() == null ?
                e : e.getTargetException());

        } catch (IllegalAccessException e) {
            throw new ContainerError(e);
        } catch (InstantiationException e) {
            throw new ContainerError(e);
        }
    }

    protected Object instantiateDependency(final Dependency dep) {
        final Object instance;
        final Constructor ctor;
        final Class clazz = DefaultContainer.loadClass(
            dep.getImplementation().getIdentifier());

        try {
            ctor = clazz.getDeclaredConstructor(DefaultContainer.DEP_CTOR);
            ctor.setAccessible(true);
            instance = ctor.newInstance(new Object[] { dep });
            DefaultContainer.initialize(instance);
            return instance;
        } catch(NoSuchMethodException e) {
            throw new ContainerError(e);
        } catch (IllegalArgumentException e) {
            throw new ContainerError(e);
        } catch (InvocationTargetException e) {
            throw new ContainerError(e.getTargetException() == null ?
                e : e.getTargetException());

        } catch (IllegalAccessException e) {
            throw new ContainerError(e);
        } catch (InstantiationException e) {
            throw new ContainerError(e);
        }
    }

    protected static void initialize(final Object instance) {
        if(instance instanceof ContainerInitializer) {
            ((ContainerInitializer) instance).initialize();
        }
    }

    protected static void initializeContext(final Object instance) {
        if(instance instanceof ContextInitializer &&
            !((ContextInitializer) instance).isInitialized(
            ContextFactory.getContext())) {

            ((ContextInitializer) instance).
                initialize(ContextFactory.getContext());

        }
    }

    //--------------------------------------------------------DefaultContainer--

}

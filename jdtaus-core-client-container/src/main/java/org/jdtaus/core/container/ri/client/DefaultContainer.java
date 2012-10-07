/*
 *  jDTAUS Core RI Client Container
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
package org.jdtaus.core.container.ri.client;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdtaus.core.container.Container;
import org.jdtaus.core.container.ContainerError;
import org.jdtaus.core.container.ContainerInitializer;
import org.jdtaus.core.container.ContextFactory;
import org.jdtaus.core.container.ContextInitializer;
import org.jdtaus.core.container.Dependency;
import org.jdtaus.core.container.DependencyCycleException;
import org.jdtaus.core.container.Implementation;
import org.jdtaus.core.container.MissingImplementationException;
import org.jdtaus.core.container.Model;
import org.jdtaus.core.container.ModelFactory;
import org.jdtaus.core.container.MultiplicityConstraintException;
import org.jdtaus.core.container.Specification;
import org.jdtaus.core.container.ri.client.versioning.VersionParser;

/**
 * {@code Container} reference implementation.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 *
 * @see org.jdtaus.core.container.ContainerFactory
 */
public class DefaultContainer implements Container
{
    //--Constants---------------------------------------------------------------

    /** Empty Class-Array. */
    private static final Class[] EMPTY =
    {
    };

    /** Implementation Class-Array. */
    private static final Class[] IMPL_CTOR =
    {
        Implementation.class
    };

    /** Dependency Class-Array. */
    private static final Class[] DEP_CTOR =
    {
        Dependency.class
    };

    /** Array of scope implementations. */
    private static final Scope[] SCOPES =
    {
        null, new ContextScope(), new SingletonScope()
    };

    //---------------------------------------------------------------Constants--
    //--Container---------------------------------------------------------------

    /** Mutex used to detect cyclic instantiations. */
    private final Object cycle = new Object();

    /** Maps objects to model object. */
    private final Map objects = new WeakIdentityHashMap( 1024 );

    public Object getObject( final Class specification )
    {
        if ( specification == null )
        {
            throw new NullPointerException( "specification" );
        }

        return this.getObjectInternal( this.getClassLoader( specification ),
                                       specification.getName(), null, false );

    }

    public Object getObject( final Class specification,
                             final String implementationName )
    {
        if ( specification == null )
        {
            throw new NullPointerException( "specification" );
        }
        if ( implementationName == null )
        {
            throw new NullPointerException( "implementationName" );
        }

        return this.getObjectInternal( this.getClassLoader( specification ),
                                       specification.getName(),
                                       implementationName, true );

    }

    public Object getDependency( final Object object,
                                 final String dependencyName )
    {
        if ( object == null )
        {
            throw new NullPointerException( "object" );
        }
        if ( dependencyName == null )
        {
            throw new NullPointerException( "dependencyName" );
        }

        try
        {
            final Implementation impl = this.getImplementation( object );
            final Instance instance = this.getInstance( object, impl );

            synchronized ( instance )
            {
                return this.getDependency(
                    instance.getClassLoader(), instance,
                    impl.getDependencies().getDependency( dependencyName ) );

            }
        }
        catch ( final ClassNotFoundException e )
        {
            throw new ContainerError( e );
        }
        catch ( final NoSuchMethodException e )
        {
            throw new ContainerError( e );
        }
        catch ( final IllegalAccessException e )
        {
            throw new ContainerError( e );
        }
        catch ( final InvocationTargetException e )
        {
            throw new ContainerError( e );
        }
    }

    public Object getProperty( final Object object,
                               final String propertyName )
    {
        if ( object == null )
        {
            throw new NullPointerException( "object" );
        }
        if ( propertyName == null )
        {
            throw new NullPointerException( "propertyName" );
        }

        return this.getInstance( object, this.getImplementation( object ) ).
            getProperties().getProperty( propertyName ).getValue();

    }

    public String getMessage( final Object object, final String messageName,
                              final Locale locale, final Object arguments )
    {
        if ( object == null )
        {
            throw new NullPointerException( "object" );
        }
        if ( locale == null )
        {
            throw new NullPointerException( "locale" );
        }
        if ( messageName == null )
        {
            throw new NullPointerException( "messageName" );
        }

        return new MessageFormat(
            this.getInstance( object, this.getImplementation( object ) ).
            getMessages().getMessage( messageName ).getTemplate().
            getValue( locale ), locale ).format( arguments );

    }

    public final Object getObject( final String specificationIdentifier )
    {
        return this.getObjectInternal( this.getClassLoader( this.getClass() ),
                                       specificationIdentifier, null,
                                       false );

    }

    public final Object getObject( final String specificationIdentifier,
                                   final String implementationName )
    {
        return this.getObjectInternal( this.getClassLoader( this.getClass() ),
                                       specificationIdentifier,
                                       implementationName, true );

    }

    public final String getMessage( final Object object,
                                    final String messageName,
                                    final Object arguments )
    {
        if ( object == null )
        {
            throw new NullPointerException( "object" );
        }
        if ( messageName == null )
        {
            throw new NullPointerException( "messageName" );
        }

        return this.getMessage( object, messageName, Locale.getDefault(),
                                arguments );

    }

    public final Object getImplementation(
        final Class specification, final String implementationName )
    {
        if ( specification == null )
        {
            throw new NullPointerException( "specification" );
        }
        if ( implementationName == null )
        {
            throw new NullPointerException( "implementationName" );
        }

        return this.getObject( specification, implementationName );
    }

    public final Object getDependency(
        final Class implementation, final String dependencyName )
    {
        if ( implementation == null )
        {
            throw new NullPointerException( "implementation" );
        }
        if ( dependencyName == null )
        {
            throw new NullPointerException( "dependencyName" );
        }

        try
        {
            final Object dependencyObject = this.getDependency(
                this.getClassLoader( implementation ), null,
                ModelFactory.getModel().getModules().
                getImplementation( implementation.getName() ).
                getDependencies().getDependency( dependencyName ) );

            this.initializeContext( dependencyObject );
            return dependencyObject;
        }
        catch ( final ClassNotFoundException e )
        {
            throw new ContainerError( e );
        }
        catch ( final NoSuchMethodException e )
        {
            throw new ContainerError( e );
        }
        catch ( final IllegalAccessException e )
        {
            throw new ContainerError( e );
        }
        catch ( final InvocationTargetException e )
        {
            throw new ContainerError( e );
        }
    }

    //---------------------------------------------------------------Container--
    //--DefaultContainer--------------------------------------------------------

    /** Creates a new {@code DefaultContainer} instance. */
    public DefaultContainer()
    {
        super();
    }

    /**
     * Creates a new instance of an implementation.
     * <p>The reference implementation loads the class corresponding to the
     * implementation and creates a new instance of that class by calling the
     * constructor taking an {@code Implementation} as an argument.</p>
     *
     * @param classLoader The classloader to use for loading classes.
     * @param impl the implementation to return a new instance for.
     *
     * @return a new instance of the class corresponding to {@code impl}.
     *
     * @throws NullPointerException if {@code classLoader} or {@code impl} is
     * {@code null}.
     * @throws org.jdtaus.core.container.InstantiationException
     * if no instance can be created.
     * @deprecated Replaced by {@link #instantiateObject(org.jdtaus.core.container.ri.client.Instance)
     */
    private Object instantiateImplementation( final ClassLoader classLoader,
                                              final Implementation impl )
    {
        if ( classLoader == null )
        {
            throw new NullPointerException( "classLoader" );
        }
        if ( impl == null )
        {
            throw new NullPointerException( "impl" );
        }

        Constructor ctor = null;

        try
        {
            final Class clazz = Class.forName( impl.getIdentifier(), true,
                                               classLoader );

            ctor = clazz.getDeclaredConstructor( IMPL_CTOR );
            ctor.setAccessible( true );

            final Object object = ctor.newInstance( new Object[]
                {
                    impl
                } );

            return object;
        }
        catch ( final Throwable e )
        {
            throw new org.jdtaus.core.container.InstantiationException(
                impl.getIdentifier(), e );

        }
        finally
        {
            if ( ctor != null )
            {
                ctor.setAccessible( false );
            }
        }
    }

    /**
     * Creates a new instance of a dependency.
     * <p>The reference implementation loads the class corresponding to the
     * dependency and creates a new instance of that class by calling the
     * constructor taking a {@code Dependency} as an argument.</p>
     *
     * @param classLoader The classloader to use for loading classes.
     * @param dep the dependency to return a new instance for.
     *
     * @return a new instance of the class corresponding to {@code dep}.
     *
     * @throws NullPointerException if {@code classLoader} or {@code dep} is
     * {@code null}.
     * @throws org.jdtaus.core.container.InstantiationException
     * if no instance can be created.
     * @deprecated Replaced by {@link #instantiateObject(org.jdtaus.core.container.ri.client.Instance)
     */
    private Object instantiateDependency( final ClassLoader classLoader,
                                          final Dependency dep )
    {
        if ( classLoader == null )
        {
            throw new NullPointerException( "classLoader" );
        }
        if ( dep == null )
        {
            throw new NullPointerException( "dep" );
        }

        Constructor ctor = null;

        try
        {
            final Class clazz = Class.forName( dep.getImplementation().
                getIdentifier(), true, classLoader );

            ctor = clazz.getDeclaredConstructor( DEP_CTOR );
            ctor.setAccessible( true );

            final Object object = ctor.newInstance( new Object[]
                {
                    dep
                } );

            return object;
        }
        catch ( final Throwable e )
        {
            throw new org.jdtaus.core.container.InstantiationException(
                dep.getImplementation().getIdentifier(), e );

        }
        finally
        {
            if ( ctor != null )
            {
                ctor.setAccessible( false );
            }
        }
    }

    /**
     * Performs initialization of an object.
     *
     * @param object The object to initialize.
     *
     * @throws NullPointerException if {@code object} is {@code null}.
     * @throws Exception if initialization fails.
     */
    private void initializeObject( final Object object ) throws Exception
    {
        if ( object == null )
        {
            throw new NullPointerException( "object" );
        }

        if ( object instanceof ContainerInitializer )
        {
            ( (ContainerInitializer) object ).initialize();
        }
    }

    /**
     * Performs context initialization of an object.
     *
     * @param object The object to perform context initialization with.
     *
     * @throws NullPointerException if {@code object} is {@code null}.
     * @throws org.jdtaus.core.container.ContextError for unrecoverable context
     * errors.
     */
    private void initializeContext( final Object object )
    {
        if ( object == null )
        {
            throw new NullPointerException( "object" );
        }

        if ( object instanceof ContextInitializer &&
             !( (ContextInitializer) object ).isInitialized(
            ContextFactory.getContext() ) )
        {
            ( (ContextInitializer) object ).initialize(
                ContextFactory.getContext() );

        }
    }

    private Object getObjectInternal( final ClassLoader classLoader,
                                      final String specificationIdentifier,
                                      final String implementationName,
                                      final boolean implementationNameWarning )
    {
        if ( classLoader == null )
        {
            throw new NullPointerException( "classLoader" );
        }
        if ( specificationIdentifier == null )
        {
            throw new NullPointerException( "specificationIdentifier" );
        }

        try
        {
            final Object object;
            final Specification specification = ModelFactory.getModel().
                getModules().getSpecification( specificationIdentifier );

            if ( implementationName != null )
            {
                final Class specClass =
                    Class.forName( specification.getIdentifier(), true,
                                   classLoader );

                final Implementation implementation =
                    specification.getImplementation( implementationName );

                final Instance instance = new Instance(
                    classLoader, specification.getScope(),
                    implementation.getIdentifier(),
                    implementation.getModelVersion(),
                    implementation.getModuleName() );

                instance.setImplementation( implementation );
                object = this.requestImplementation(
                    specClass, specification, this.getObject( instance ) );

            }
            else
            {
                if ( implementationNameWarning )
                {
                    final Throwable x = new Throwable();
                    final StackTraceElement[] elements = x.getStackTrace();

                    String cname = "unknown";
                    String method = "unknown";
                    StackTraceElement caller = null;

                    if ( elements != null && elements.length > 2 )
                    {
                        caller = elements[2];
                        cname = caller.getClassName();
                        method = caller.getMethodName();
                    }

                    Logger.getLogger( DefaultContainer.class.getName() ).
                        log( Level.SEVERE, DefaultContainerBundle.getInstance().
                        getNullImplementationNameWarningMessage(
                        Locale.getDefault(), specificationIdentifier, cname,
                        method ), x );

                }

                object = this.resolveImplementation( classLoader,
                                                     specification );

            }

            return object;
        }
        catch ( final IllegalAccessException e )
        {
            // Cannot happen - method got set accessible.
            throw new AssertionError( e );
        }
        catch ( final InvocationTargetException e )
        {
            throw new ContainerError( e );
        }
        catch ( final NoSuchMethodException e )
        {
            throw new ContainerError( e );
        }
        catch ( final ClassNotFoundException e )
        {
            throw new ContainerError( e );
        }
    }

    private Object getObject( final Instance instance )
    {
        return this.getScopedObject(
            SCOPES[instance.getScope() - Specification.SCOPE_MULTITON],
            instance );

    }

    private Object instantiateObject( final Instance instance )
    {
        try
        {
            Object object = null;

            if ( VersionParser.compare( instance.getModelVersion(),
                                        "1.3" ) < 0 )
            {
                if ( instance.getImplementation() != null )
                {
                    object = this.instantiateImplementation(
                        instance.getClassLoader(),
                        instance.getImplementation() );

                }
                else if ( instance.getDependency() != null )
                {
                    object = this.instantiateDependency(
                        instance.getClassLoader(),
                        instance.getDependency() );

                }
                else
                {
                    throw new AssertionError();
                }
            }
            else
            {
                final Class clazz = Class.forName(
                    instance.getClassName(), true, instance.getClassLoader() );

                if ( Model.class.getName().equals( instance.getModuleName() ) )
                {
                    // Automatically discovered default implementations must use
                    // the static getDefault method.
                    final Method accessor =
                        clazz.getMethod( "getDefault", EMPTY );

                    object = accessor.invoke( null, EMPTY );
                }
                else
                {
                    object = clazz.newInstance();
                }
            }

            synchronized ( this.objects )
            {
                this.objects.put( object, instance );
            }

            this.initializeObject( object );
            return object;
        }
        catch ( final Throwable t )
        {
            if ( t instanceof Error )
            {
                throw (Error) t;
            }
            else if ( t instanceof RuntimeException )
            {
                throw (RuntimeException) t;
            }
            else
            {
                throw new org.jdtaus.core.container.InstantiationException(
                    instance.getClassName(), t );

            }
        }
    }

    private Implementation getImplementation( final Object object )
    {
        MissingImplementationException exception = null;
        Implementation implementation = null;
        Class clazz = object.getClass();

        do
        {
            try
            {
                implementation = ModelFactory.getModel().getModules().
                    getImplementation( clazz.getName() );

                break;
            }
            catch ( final MissingImplementationException e )
            {
                if ( exception == null )
                {
                    exception = e;
                }
            }
        }
        while ( ( clazz = clazz.getSuperclass() ) != null );

        if ( implementation == null )
        {
            throw exception;
        }

        return implementation;
    }

    private Instance getInstance( final Object object,
                                  final Implementation impl )
    {
        synchronized ( this.objects )
        {
            Instance instance = (Instance) this.objects.get( object );

            if ( instance == null )
            {
                instance =
                    new Instance( this.getClassLoader( object.getClass() ),
                                  Specification.SCOPE_MULTITON,
                                  impl.getIdentifier(),
                                  impl.getModelVersion(),
                                  impl.getModuleName() );

                instance.setImplementation( impl );
                this.objects.put( object, instance );
            }

            return instance;
        }
    }

    private Object getDependency( final ClassLoader classLoader,
                                  final Instance instance,
                                  final Dependency dependency )
        throws ClassNotFoundException, NoSuchMethodException,
               IllegalAccessException, InvocationTargetException
    {
        Object dependencyObject =
            instance != null
            ? instance.getDependencyObject( dependency.getName() )
            : null;

        if ( dependencyObject == null )
        {
            if ( dependency.getImplementation() != null )
            {
                final Class specClass =
                    Class.forName( dependency.getSpecification().getIdentifier(),
                                   true, classLoader );

                final Instance dependencyInstance = new Instance(
                    classLoader, dependency.getSpecification().getScope(),
                    dependency.getImplementation().getIdentifier(),
                    dependency.getImplementation().getModelVersion(),
                    dependency.getImplementation().getModuleName() );

                dependencyInstance.setDependency( dependency );
                dependencyObject = this.requestImplementation(
                    specClass, dependency.getSpecification(),
                    this.getObject( dependencyInstance ) );

            }
            else
            {
                dependencyObject = this.resolveDependency( classLoader,
                                                           dependency );

            }
        }

        if ( instance != null && dependency.isBound() )
        {
            instance.setDependencyObject( dependency.getName(),
                                          dependencyObject );

        }

        return dependencyObject;
    }

    private Object resolveDependency( final ClassLoader classLoader,
                                      final Dependency dependency )
        throws ClassNotFoundException, NoSuchMethodException,
               IllegalAccessException, InvocationTargetException
    {
        Implementation impl;
        Instance dependencyInstance;
        Dependency clone;
        final Object resolved;

        final Class specClass =
            Class.forName( dependency.getSpecification().getIdentifier(),
                           true, classLoader );

        switch ( dependency.getSpecification().getMultiplicity() )
        {
            case Specification.MULTIPLICITY_ONE:
                if ( dependency.getSpecification().
                    getImplementations().size() != 1 )
                {
                    throw new MultiplicityConstraintException(
                        dependency.getSpecification().
                        getIdentifier() );

                }

                impl = dependency.getSpecification().getImplementations().
                    getImplementation( 0 );

                dependencyInstance = new Instance(
                    classLoader, dependency.getSpecification().getScope(),
                    impl.getIdentifier(), impl.getModelVersion(),
                    impl.getModuleName() );

                clone = (Dependency) dependency.clone();
                clone.setImplementation( impl );
                dependencyInstance.setDependency( clone );
                resolved = this.requestImplementation( specClass, dependency.
                    getSpecification(), this.getObject( dependencyInstance ) );

                break;

            case Specification.MULTIPLICITY_MANY:
                final List list = new ArrayList( dependency.getSpecification().
                    getImplementations().size() );

                for ( int i = dependency.getSpecification().
                    getImplementations().size() - 1; i >= 0; i-- )
                {
                    impl = dependency.getSpecification().getImplementations().
                        getImplementation( i );

                    dependencyInstance = new Instance(
                        classLoader, dependency.getSpecification().getScope(),
                        impl.getIdentifier(), impl.getModelVersion(),
                        impl.getModuleName() );

                    clone = (Dependency) dependency.clone();
                    clone.setImplementation( impl );
                    dependencyInstance.setDependency( clone );
                    list.add( this.requestImplementation(
                        specClass, dependency.getSpecification(),
                        this.getObject( dependencyInstance ) ) );

                }

                final Object[] implementations =
                    (Object[]) Array.newInstance( specClass,
                                                  list.size() );

                resolved = list.toArray( implementations );
                break;

            default:
                throw new AssertionError( Integer.toString(
                    dependency.getSpecification().getMultiplicity() ) );

        }

        return resolved;
    }

    private Object resolveImplementation( final ClassLoader classLoader,
                                          final Specification spec )
        throws ClassNotFoundException, NoSuchMethodException,
               IllegalAccessException, InvocationTargetException
    {
        Instance instance;
        Implementation impl;

        final Object resolved;
        final Class specClass = Class.forName(
            spec.getIdentifier(), true, classLoader );

        switch ( spec.getMultiplicity() )
        {
            case Specification.MULTIPLICITY_ONE:
                if ( spec.getImplementations().size() != 1 )
                {
                    throw new MultiplicityConstraintException(
                        spec.getIdentifier() );

                }

                impl = spec.getImplementations().getImplementation( 0 );
                instance = new Instance( classLoader,
                                         spec.getScope(),
                                         impl.getIdentifier(),
                                         impl.getModelVersion(),
                                         impl.getModuleName() );

                instance.setImplementation( impl );
                resolved = this.requestImplementation(
                    specClass, spec, this.getObject( instance ) );

                break;

            case Specification.MULTIPLICITY_MANY:
                final List list = new ArrayList(
                    spec.getImplementations().size() );

                for ( int i = spec.getImplementations().size() - 1; i >= 0;
                      i-- )
                {
                    impl = spec.getImplementations().getImplementation( i );
                    instance = new Instance( classLoader,
                                             spec.getScope(),
                                             impl.getIdentifier(),
                                             impl.getModelVersion(),
                                             impl.getModuleName() );

                    instance.setImplementation( impl );
                    list.add( this.requestImplementation(
                        specClass, spec, this.getObject( instance ) ) );

                }

                final Object[] implementations =
                    (Object[]) Array.newInstance( specClass,
                                                  list.size() );

                resolved = list.toArray( implementations );
                break;

            default:
                throw new AssertionError( Integer.toString(
                    spec.getMultiplicity() ) );

        }

        return resolved;
    }

    private Object getScopedObject( final Scope scope, final Instance instance )
    {
        Object object;

        if ( scope != null )
        {
            object = scope.getObject( instance.getClassName() );

            if ( object == null || object instanceof Instance )
            {
                synchronized ( this.cycle )
                {
                    object = scope.getObject( instance.getClassName() );

                    if ( object == null )
                    {
                        scope.putObject( instance.getClassName(), instance );

                        try
                        {
                            object = this.instantiateObject( instance );
                        }
                        catch ( final Throwable t )
                        {
                            scope.removeObject( instance.getClassName() );
                            if ( t instanceof Error )
                            {
                                throw (Error) t;
                            }
                            else if ( t instanceof RuntimeException )
                            {
                                throw (RuntimeException) t;
                            }
                            else
                            {
                                throw new org.jdtaus.core.container.InstantiationException(
                                    instance.getClassName(), t );

                            }
                        }

                        scope.putObject( instance.getClassName(), object );
                    }
                    else if ( object instanceof Instance )
                    {
                        throw new DependencyCycleException(
                            ( (Instance) object ).getClassName(),
                            instance.getClassName() );

                    }
                }
            }
        }
        else
        {
            object = this.instantiateObject( instance );
        }

        return object;
    }

    private ClassLoader getClassLoader( final Class clazz )
    {
        ClassLoader classLoader = clazz.getClassLoader();
        if ( classLoader == null )
        {
            classLoader = ClassLoader.getSystemClassLoader();
        }

        return classLoader;
    }

    private Object requestImplementation( final Class specClass,
                                          final Specification spec,
                                          final Object object )
        throws NoSuchMethodException, IllegalAccessException,
               InvocationTargetException
    {
        Object resolved = object;
        Method accessor = null;

        try
        {
            if ( !specClass.isAssignableFrom( object.getClass() ) )
            {
                accessor = object.getClass().getDeclaredMethod(
                    spec.getIdentifier().replace( '.', '_' ), EMPTY );

                accessor.setAccessible( true );
                resolved = accessor.invoke( object, EMPTY );
            }

            return resolved;
        }
        finally
        {
            if ( accessor != null )
            {
                accessor.setAccessible( false );
            }
        }
    }

    //--------------------------------------------------------DefaultContainer--
}

/*
 *  jDTAUS Core Container Mojo
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
package org.jdtaus.core.container.mojo;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Set;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jdtaus.core.container.ContainerError;
import org.jdtaus.core.container.ContextError;
import org.jdtaus.core.container.Dependencies;
import org.jdtaus.core.container.Dependency;
import org.jdtaus.core.container.Implementation;
import org.jdtaus.core.container.Implementations;
import org.jdtaus.core.container.MissingImplementationException;
import org.jdtaus.core.container.MissingSpecificationException;
import org.jdtaus.core.container.Model;
import org.jdtaus.core.container.ModelError;
import org.jdtaus.core.container.ModelFactory;
import org.jdtaus.core.container.Module;
import org.jdtaus.core.container.Modules;
import org.jdtaus.core.container.Specification;
import org.jdtaus.core.container.Specifications;

/**
 * Mojo to validate set of modules.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 * @goal verify-model
 * @phase verify
 * @requiresDependencyResolution runtime
 */
public class VerifyModelMojo extends AbstractContainerMojo
{
    //--AbstractMojo------------------------------------------------------------

    /** Flag indicating model violations. */
    private boolean validModel;

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        // Setup all runtime dependencies to be available for classloading.
        final ClassLoader mavenLoader =
            Thread.currentThread().getContextClassLoader();

        try
        {
            Thread.currentThread().setContextClassLoader(
                this.getClassLoader( mavenLoader ) );

            this.validModel = true;
            this.assertValidModel( ModelFactory.getModel() );

            if ( this.validModel )
            {
                this.getLog().info( VerifyModelMojoBundle.getInstance().
                                    getValidModelMessage( Locale.getDefault() ) );

            }
            else
            {
                throw new MojoExecutionException(
                    VerifyModelMojoBundle.getInstance().
                    getModelViolationsMessage( Locale.getDefault() ) );

            }
        }
        catch ( ContextError e )
        {
            throw new MojoExecutionException( e.getMessage(), e );
        }
        catch ( ContainerError e )
        {
            throw new MojoExecutionException( e.getMessage(), e );
        }
        catch ( ModelError e )
        {
            throw new MojoExecutionException( e.getMessage(), e );
        }
        catch ( Exception e )
        {
            throw new MojoExecutionException( e.getMessage(), e );
        }
        finally
        {
            Thread.currentThread().setContextClassLoader( mavenLoader );
        }
    }

    //------------------------------------------------------------AbstractMojo--
    //--VerifyModelMojo---------------------------------------------------------

    protected ClassLoader getClassLoader( final ClassLoader parent )
        throws Exception
    {
        return this.getRuntimeClassLoader( parent );
    }

    private void assertValidModel( final Model model )
        throws MojoExecutionException, MojoFailureException
    {
        final Specifications specs = model.getModules().getSpecifications();
        for ( int i = specs.size() - 1; i >= 0; i-- )
        {
            final Specification spec = specs.getSpecification( i );
            final Implementations impls = spec.getImplementations();

            this.assertClassAvailable( spec.getIdentifier() );

            for ( int j = impls.size() - 1; j >= 0; j-- )
            {
                final Implementation impl = impls.getImplementation( j );
                final Class clazz = this.assertClassAvailable(
                    impl.getIdentifier() );

                if ( clazz != null )
                {
                    this.assertImplementsAllSpecifications(
                        impl, model.getModules(), clazz );

                }
            }
        }

        // Check for unresolved dependencies.
        final Dependencies unresolved = this.getUnresolvedDependencies( model );
        final Set renderedSpecifications = new HashSet();

        if ( unresolved.size() > 0 )
        {
            this.validModel = false;

            this.getLog().error(
                VerifyModelMojoBundle.getInstance().
                getUnresolvedDependenciesMessage( Locale.getDefault() ) );

            for ( int i = unresolved.size() - 1; i >= 0; i-- )
            {
                final Dependency dep = unresolved.getDependency( i );

                if ( !renderedSpecifications.contains(
                    dep.getSpecification().getIdentifier() ) )
                {
                    this.getLog().error(
                        "  " + VerifyModelMojoBundle.getInstance().
                        getUnresolvedDependencyMessage(
                        Locale.getDefault(),
                        dep.getSpecification().getIdentifier(),
                        dep.getSpecification().getModuleName(),
                        dep.getSpecification().getVersion() ) );

                    renderedSpecifications.add(
                        dep.getSpecification().getIdentifier() );

                }
            }
        }
    }

    private Class assertClassAvailable( final String className )
    {
        Class clazz = null;

        try
        {
            clazz = Thread.currentThread().getContextClassLoader().
                loadClass( className );

        }
        catch ( ClassNotFoundException e )
        {
            this.validModel = false;

            this.getLog().error(
                VerifyModelMojoBundle.getInstance().
                getClassNotFoundMessage( Locale.getDefault(), e.getMessage() ) );

            clazz = null;
        }

        return clazz;
    }

    private void assertImplementsAllSpecifications(
        final Implementation impl, final Modules modules, final Class clazz )
    {
        final Set allInterfaces = new HashSet();
        this.getAllImplementedInterfaces( clazz, allInterfaces );
        final Class[] interfaces = (Class[]) allInterfaces.toArray(
            new Class[ allInterfaces.size() ] );

        if ( impl.getImplementedSpecifications().size() > 0 )
        {
            for ( int i = interfaces.length - 1; i >= 0; i-- )
            {
                try
                {
                    modules.getSpecification( interfaces[i].getName() ).
                        getImplementation( impl.getName() );

                }
                catch ( MissingImplementationException e )
                {
                    this.validModel = false;

                    this.getLog().error(
                        VerifyModelMojoBundle.getInstance().
                        getMissingImplementedSpecificationMessage(
                        Locale.getDefault(), impl.getIdentifier(),
                        interfaces[i].getName() ) );

                }
                catch ( MissingSpecificationException e )
                {
                    // Classes are allowed to implement additional interfaces
                    // not defined in the model.
                }
            }
        }

        for ( int i = impl.getImplementedSpecifications().size() - 1; i >= 0;
            i-- )
        {
            final Specification spec = impl.getImplementedSpecifications().
                getSpecification( i );

            boolean isImplemented =
                spec.getIdentifier().equals( impl.getIdentifier() );

            for ( int j = interfaces.length - 1; j >= 0; j-- )
            {
                if ( interfaces[j].getName().equals( spec.getIdentifier() ) )
                {
                    isImplemented = true;
                    break;
                }
            }

            if ( !isImplemented )
            {
                this.validModel = false;

                this.getLog().error(
                    VerifyModelMojoBundle.getInstance().
                    getMissingInterfaceMessage( Locale.getDefault(),
                                                impl.getIdentifier(),
                                                spec.getIdentifier() ) );

            }
        }
    }

    /**
     * Gets all interfaces implemented by a given class.
     *
     * @param clazz the class to get all implemented interfaces from.
     * @param interfaces set for collecting interfaces recursively.
     */
    private void getAllImplementedInterfaces( final Class clazz,
                                              final Set interfaces )
    {
        final Class[] current = clazz.getInterfaces();
        for ( int i = current.length - 1; i >= 0; i-- )
        {
            this.getAllImplementedInterfaces( current[i], interfaces );
            interfaces.add( current[i] );
        }
    }

    /**
     * Gets all unresolved dependencies from a given model.
     *
     * @param model the model to gets unresolved dependencies from.
     *
     * @throws NullPointerException if {@code model} is {@code null}.
     */
    private Dependencies getUnresolvedDependencies( final Model model )
    {
        String unresolvedName = "Unresolved-";
        final Collection deps = new LinkedList();

        for ( int m = model.getModules().size() - 1; m >= 0; m-- )
        {
            final Module mod = model.getModules().getModule( m );

            for ( int i = mod.getImplementations().size() - 1; i >= 0; i-- )
            {
                final Implementation impl = mod.getImplementations().
                    getImplementation( i );

                for ( int d = impl.getDependencies().size() - 1; d >= 0; d-- )
                {
                    final Dependency dep = (Dependency) impl.getDependencies().
                        getDependency( d ).clone();

                    if ( dep.getImplementation() == null &&
                        dep.getSpecification().getMultiplicity() ==
                        Specification.MULTIPLICITY_ONE &&
                        dep.getSpecification().getImplementations().
                        size() != 1 )
                    {
                        dep.setName(
                            unresolvedName + "-" + m + "-" + i + "-" + d );

                        deps.add( dep );
                    }
                }
            }
        }

        final Dependencies dependencies = new Dependencies();
        dependencies.setDependencies(
            (Dependency[]) deps.toArray( new Dependency[ deps.size() ] ) );

        return dependencies;
    }

    //---------------------------------------------------------VerifyModelMojo--
}

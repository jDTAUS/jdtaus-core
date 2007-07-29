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
package org.jdtaus.core.container.mojo;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jdtaus.core.container.Implementation;
import org.jdtaus.core.container.Implementations;
import org.jdtaus.core.container.MissingImplementationException;
import org.jdtaus.core.container.MissingSpecificationException;
import org.jdtaus.core.container.ModelFactory;
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
public class VerifyModelMojo extends AbstractSourceMojo
{
    //--AbstractMojo------------------------------------------------------------

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        // Setup all runtime dependencies to be available for classloading.
        final ClassLoader mavenLoader =
            Thread.currentThread().getContextClassLoader();

        Thread.currentThread().
            setContextClassLoader(this.getRuntimeClassLoader());

        this.assertValidModel(ModelFactory.getModel().getModules());

        Thread.currentThread().setContextClassLoader(mavenLoader);
    }

    //------------------------------------------------------------AbstractMojo--
    //--VerifyModelMojo---------------------------------------------------------

    private void assertValidModel(final Modules modules)
    throws MojoExecutionException, MojoFailureException
    {
        final Specifications specs = modules.getSpecifications();
        for(int i = specs.size() - 1; i >= 0; i--)
        {
            final Specification spec = specs.getSpecification(i);
            final Implementations impls = spec.getImplementations();

            this.assertClassAvailable(spec.getIdentifier());

            for(int j = impls.size() - 1; j >= 0; j--)
            {
                final Implementation impl = impls.getImplementation(j);
                final Class clazz = this.assertClassAvailable(
                    impl.getIdentifier());

                this.assertImplementsAllSpecifications(impl, modules, clazz);
            }

            // Check mandatory implementations to exist.
            if(spec.getMultiplicity() == Specification.MULTIPLICITY_ONE &&
                impls.size() == 0)
            {
                throw new MojoExecutionException(
                    VerifyModelMojoBundle.getMissingImplementationMessage(
                    Locale.getDefault()).format(new Object[] {
                    spec.getIdentifier() }));

            }
        }
    }

    private Class assertClassAvailable(final String className)
    throws MojoExecutionException, MojoFailureException
    {
        try
        {
            return this.getContextClassLoader().loadClass(className);
        }
        catch(ClassNotFoundException e)
        {
            throw new MojoExecutionException(
                VerifyModelMojoBundle.getClassNotFoundMessage(
                Locale.getDefault()).format(new Object[] { className }));

        }
    }

    private void assertImplementsAllSpecifications(final Implementation impl,
        final Modules modules, final Class clazz) throws MojoExecutionException
    {
        final Set allInterfaces = new HashSet();
        this.getAllImplementedInterfaces(clazz, allInterfaces);
        final Class[] interfaces = (Class[]) allInterfaces.
            toArray(new Class[allInterfaces.size()]);

        if(impl.getImplementedSpecifications().size() > 0)
        {
            for(int i = interfaces.length - 1; i >= 0; i--)
            {
                try
                {
                    modules.getSpecification(interfaces[i].getName()).
                        getImplementation(impl.getName());

                }
                catch(MissingImplementationException e)
                {
                    throw new MojoExecutionException(VerifyModelMojoBundle.
                        getMissingImplementedSpecificationMessage(
                        Locale.getDefault()).format(new Object[] {
                        impl.getIdentifier(), interfaces[i].getName()
                    }));

                }
                catch(MissingSpecificationException e)
                {}

            }
        }

        for(int i = impl.getImplementedSpecifications().size() - 1; i >= 0; i--)
        {
            final Specification spec = impl.getImplementedSpecifications().
                getSpecification(i);

            boolean isImplemented =
                spec.getIdentifier().equals(impl.getIdentifier());

            for(int j = interfaces.length - 1; j >= 0; j--)
            {
                if(interfaces[j].getName().equals(spec.getIdentifier()))
                {
                    isImplemented = true;
                    break;
                }
            }

            if(!isImplemented)
            {
                throw new MojoExecutionException(VerifyModelMojoBundle.
                    getMissingInterfaceMessage(Locale.getDefault()).
                    format(new Object[] { impl.getIdentifier(),
                    spec.getIdentifier() }));

            }
        }
    }

    private void assertFinalModifier(final Implementation impl)
    throws MojoExecutionException, MojoFailureException
    {
        final Class clazz = this.assertClassAvailable(impl.getIdentifier());
        if(Modifier.isFinal(clazz.getModifiers()) != impl.isFinal())
        {
            throw new MojoExecutionException(VerifyModelMojoBundle.
                getFinalModifierMismatchMessage(Locale.getDefault()).
                format(new Object[] { impl.getIdentifier() }));

        }
    }

    /**
     * Gets all interfaces implemented by a given class.
     *
     * @param clazz the class to get all implemented interfaces from.
     * @param interfaces set for collecting interfaces recursively.
     */
    private void getAllImplementedInterfaces(final Class clazz,
        final Set interfaces)
    {
        final Class[] current = clazz.getInterfaces();
        for(int i = current.length - 1; i >= 0; i--)
        {
            final Class[] parents = current[i].getInterfaces();

            for(int j = parents.length - 1; j >= 0; j--)
            {
                this.getAllImplementedInterfaces(parents[j], interfaces);
                interfaces.add(parents[j]);
            }

            interfaces.add(current[i]);
        }
    }

    //---------------------------------------------------------VerifyModelMojo--
}

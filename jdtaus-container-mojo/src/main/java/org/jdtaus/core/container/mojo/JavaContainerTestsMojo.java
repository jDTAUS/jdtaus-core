/*
 *  jDTAUS Core Container Mojo
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
package org.jdtaus.core.container.mojo;

import java.util.Locale;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jdtaus.core.container.ContainerError;
import org.jdtaus.core.container.ContextError;
import org.jdtaus.core.container.Implementations;
import org.jdtaus.core.container.ModelError;
import org.jdtaus.core.container.Module;
import org.jdtaus.core.container.Specifications;

/**
 * Mojo to generate test java code.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 * @goal java-container-tests
 * @phase process-test-resources
 * @requiresDependencyResolution test
 */
public class JavaContainerTestsMojo extends JavaContainerMojo
{

    /** Creates a new {@code JavaContainerTestsMojo} instance. */
    public JavaContainerTestsMojo()
    {
        super();
    }

    /** {@inheritDoc} */
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        final ClassLoader mavenLoader = Thread.currentThread().
            getContextClassLoader();

        try
        {
            this.model = null;

            Thread.currentThread().setContextClassLoader(
                this.getTestClassLoader( mavenLoader ) );

            enableThreadContextClassLoader();

            final Module testMod = this.getTestModule();

            if ( testMod != null )
            {
                final Specifications specs = testMod.getSpecifications();
                final Implementations impls = testMod.getImplementations();

                for ( int i = specs.size() - 1; i >= 0; i-- )
                {
                    this.generateSpecification(
                        this.getMavenProject().getTestCompileSourceRoots(),
                        specs.getSpecification( i ) );

                }

                for ( int i = impls.size() - 1; i >= 0; i-- )
                {
                    this.generateImplementation(
                        this.getMavenProject().getTestCompileSourceRoots(),
                        impls.getImplementation( i ) );

                }

                this.writeContainerReport( this.getModel(),
                                           "container-tests-report.xml" );

                this.getLog().info( JavaContainerMojoBundle.getInstance().
                    getProcessingModuleMessage( Locale.getDefault(),
                                                testMod.getName() ) );

            }
        }
        catch ( final ContextError e )
        {
            throw new MojoExecutionException( e.getMessage(), e );
        }
        catch ( final ContainerError e )
        {
            throw new MojoExecutionException( e.getMessage(), e );
        }
        catch ( final ModelError e )
        {
            throw new MojoExecutionException( e.getMessage(), e );
        }
        catch ( final Exception e )
        {
            throw new MojoExecutionException( e.getMessage(), e );
        }
        finally
        {
            disableThreadContextClassLoader();
            Thread.currentThread().setContextClassLoader( mavenLoader );
        }
    }

}

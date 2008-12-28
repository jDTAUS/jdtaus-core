/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @version $Id$
 * @goal java-container-tests
 * @phase process-test-resources
 * @requiresDependencyResolution test
 */
public class JavaContainerTestsMojo extends JavaContainerMojo
{

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
            disableThreadContextClassLoader();
            Thread.currentThread().setContextClassLoader( mavenLoader );
        }
    }

}

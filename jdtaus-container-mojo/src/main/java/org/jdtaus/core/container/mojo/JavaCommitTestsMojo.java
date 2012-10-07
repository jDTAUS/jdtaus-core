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

import java.io.File;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jdtaus.core.container.ContainerError;
import org.jdtaus.core.container.ContextError;
import org.jdtaus.core.container.Implementation;
import org.jdtaus.core.container.ModelError;
import org.jdtaus.core.container.ModelFactory;
import org.jdtaus.core.container.Module;

/**
 * Mojo to commit container meta-data to compiled java test classes.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 * @goal java-commit-tests
 * @phase process-test-classes
 * @requiresDependencyResolution test
 */
public class JavaCommitTestsMojo extends JavaCommitMojo
{

    /** Creates a new {@code JavaCommitTestsMojo} instance. */
    public JavaCommitTestsMojo()
    {
        super();
    }

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        final ClassLoader mavenLoader = Thread.currentThread().
            getContextClassLoader();

        try
        {
            Thread.currentThread().setContextClassLoader(
                this.getTestClassLoader( mavenLoader ) );

            enableThreadContextClassLoader();

            final Module module = this.getTestModule();
            if ( module != null )
            {
                for ( int i = module.getImplementations().size() - 1; i >= 0;
                      i-- )
                {
                    final Implementation impl = module.getImplementations().
                        getImplementation( i );

                    this.commitImplementation(
                        ModelFactory.newModel(), impl,
                        new File( this.getMavenProject().getBuild().
                        getTestOutputDirectory() ) );

                }
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

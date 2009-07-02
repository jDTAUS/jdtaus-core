/*
 *  jDTAUS Core Container Mojo
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
 * @version $Id$
 * @goal java-commit-tests
 * @phase process-test-classes
 * @requiresDependencyResolution test
 */
public class JavaCommitTestsMojo extends JavaCommitMojo
{

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

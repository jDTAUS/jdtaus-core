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
import java.net.URL;
import java.util.Locale;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Mojo to validate a set of modules from a single jar file.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 * @goal verify-jar
 * @phase verify
 * @requiresDependencyResolution test
 */
public class VerifyJarMojo extends VerifyModelMojo
{

    /**
     * @parameter expression="${jarFile}" default-value="${project.build.directory}/${project.build.finalName}.jar"
     * @optional
     */
    private File jarFile;

    /** Creates a new {@code VerifyJarMojo} instance. */
    public VerifyJarMojo()
    {
        super();
    }

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        this.getLog().info( VerifyJarMojoBundle.getInstance().
            getJarFileMessage( Locale.getDefault(),
                               this.jarFile.getAbsolutePath() ) );

        super.execute();
    }

    protected ClassLoader getClassLoader( final ClassLoader parent )
        throws Exception
    {
        final ClassLoader projectClassloader = super.getClassLoader( parent );
        final URL[] urls = new URL[]
        {
            this.jarFile.toURI().toURL()
        };

        return new ResourceLoader( urls, projectClassloader );
    }

}

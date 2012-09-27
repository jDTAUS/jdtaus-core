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
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.DirectoryScanner;

/**
 * Mojo to cleanup source files (e.g. remove trailing spaces).
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 * @goal clean-sources
 */
public class CleanMojo extends AbstractContainerMojo
{

    /**
     * Scans a project's source directory for files to clean.
     *
     * @return a {@code Collection} holding {@code File} instances for all
     * files to clean found in the project's source directory.
     */
    protected final Collection getCleanSources()
    {
        final Collection files = new LinkedList();
        final File sourceDirectory =
            new File( this.getMavenProject().getBasedir(), "src" );

        if ( sourceDirectory.exists() && sourceDirectory.isDirectory() )
        {
            final DirectoryScanner scanner = new DirectoryScanner();
            scanner.setBasedir( sourceDirectory );
            scanner.setIncludes( DEFAULT_SOURCE_INCLUDES );
            scanner.addDefaultExcludes();
            scanner.scan();

            for ( Iterator it = Arrays.asList( scanner.getIncludedFiles() ).
                iterator(); it.hasNext();)
            {
                files.add( new File( sourceDirectory, (String) it.next() ) );
            }
        }

        return files;
    }

    //--AbstractMojo------------------------------------------------------------

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        final Collection sources = this.getCleanSources();
        final SourceEditor editor = new RemoveTrailingSpacesEditor();

        for ( Iterator it = sources.iterator(); it.hasNext();)
        {
            final File file = (File) it.next();
            final String content = this.load( file );

            if ( this.getLog().isDebugEnabled() )
            {
                this.getLog().debug( file.getAbsolutePath() );
            }

            final String edited = this.edit( content, editor );
            if ( !content.equals( edited ) )
            {
                this.save( file, edited );
            }
        }
    }

    //------------------------------------------------------------AbstractMojo--
    //--CleanMojo---------------------------------------------------------------

    /** Removes trailing spaces. */
    public static class RemoveTrailingSpacesEditor
        implements AbstractContainerMojo.SourceEditor
    {

        private boolean modified;

        public String editLine( final String line ) throws MojoFailureException
        {
            String ret = null;

            if ( line != null )
            {
                StringBuffer spaces = null;
                boolean sawSpace = false;
                final StringBuffer replacement =
                    new StringBuffer( line.length() );

                final char[] chars = line.toCharArray();

                for ( int i = 0; i < chars.length; i++ )
                {
                    if ( chars[i] == ' ' )
                    {
                        if ( spaces == null )
                        {
                            spaces = new StringBuffer();
                        }

                        spaces.append( chars[i] );
                        sawSpace = true;
                    }
                    else
                    {
                        if ( sawSpace )
                        {
                            replacement.append( spaces );
                            sawSpace = false;
                            spaces = null;
                        }
                        replacement.append( chars[i] );
                    }
                }

                ret = replacement.toString();
                this.modified = !ret.equals( line );
            }

            return ret;
        }

        public boolean isModified()
        {
            return this.modified;
        }
    }

    //---------------------------------------------------------------CleanMojo--
}

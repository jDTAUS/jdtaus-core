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

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jdtaus.core.container.mojo.AbstractSourceMojo.SourceEditor;

/**
 * Mojo to cleanup source files (e.g. remove trailing spaces).
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 * @goal clean-sources
 */
public class CleanMojo extends AbstractSourceMojo
{

    //--AbstractMojo------------------------------------------------------------

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        File file;
        String contents;
        String edited;
        final Collection sources = new LinkedList();
        final SourceEditor editor = new RemoveTrailingSpacesEditor();

        if(new File(this.getMavenProject().getBasedir(),
            "src/main/java").exists())
        {
            sources.addAll(this.getAllSources());
        }
        if(new File(this.getMavenProject().getBasedir(),
            "src/test/java").exists())
        {
            sources.addAll(this.getTestSources());
        }

        for(Iterator it = sources.iterator(); it.hasNext();)
        {
            file = (File) it.next();
            contents = this.load(file);
            edited = this.edit(contents, editor);
            if(!contents.equals(edited))
            {
                this.save(file, edited);
            }
        }
    }

    //------------------------------------------------------------AbstractMojo--
    //--CleanMojo---------------------------------------------------------------

    /** Removes trailing spaces. */
    public static class RemoveTrailingSpacesEditor
        implements AbstractSourceMojo.SourceEditor
    {

        private boolean modified;

        public String editLine(final String line) throws MojoFailureException
        {
            if(line == null)
            {
                return null;
            }

            StringBuffer spaces = null;
            boolean sawSpace = false;
            final String ret;
            final StringBuffer replacement = new StringBuffer(line.length());
            final char[] chars = line.toCharArray();

            for(int i = 0; i < chars.length; i++)
            {
                if(chars[i] == ' ')
                {
                    if(spaces == null)
                    {
                        spaces = new StringBuffer();
                    }

                    spaces.append(chars[i]);
                    sawSpace = true;
                }
                else
                {
                    if(sawSpace)
                    {
                        replacement.append(spaces);
                        sawSpace = false;
                        spaces = null;
                    }
                    replacement.append(chars[i]);
                }
            }

            ret = replacement.toString();
            this.modified = !ret.equals(line);

            return ret;
        }

        public boolean isModified()
        {
            return this.modified;
        }

    }

    //---------------------------------------------------------------CleanMojo--

}

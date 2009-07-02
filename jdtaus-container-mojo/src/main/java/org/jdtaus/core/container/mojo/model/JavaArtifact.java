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
package org.jdtaus.core.container.mojo.model;

import java.io.File;
import java.util.Set;
import java.util.TreeSet;

/**
 * Java artifact.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $Id$
 */
public class JavaArtifact
{

    private String cName;

    private Set imports;

    public JavaArtifact( final String className )
    {
        this.cName = className;
    }

    public String getName()
    {
        return this.cName.substring( this.cName.lastIndexOf( '.' ) + 1 );
    }

    public String getClassName()
    {
        return this.cName;
    }

    public String getPackageName()
    {
        return this.cName.substring( 0, this.cName.lastIndexOf( '.' ) );
    }

    public String getPackagePath()
    {
        return this.getPackageName().replace( '.', File.separatorChar );
    }

    public Set getImports()
    {
        if ( this.imports == null )
        {
            this.imports = new TreeSet();
        }

        return this.imports;
    }
}

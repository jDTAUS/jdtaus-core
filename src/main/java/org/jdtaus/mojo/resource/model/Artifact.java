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
package org.jdtaus.mojo.resource.model;

/**
 * Generated artifact.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id: AbstractPropertyFileProcessorMojo.java 3553 2007-05-25 22:37:09Z schulte2005 $
 */
public class Artifact
{

    /** Name of the artifact's package. */
    private String packageName;

    /** Name of the artifact's class. */
    private String className;

    /**
     * Gets the package name of the artifact.
     *
     * @return the package name of the artifact.
     */
    public String getPackageName()
    {
        return packageName;
    }

    /**
     * Sets the package name of the artifact.
     *
     * @param packageName the package name of the artifact.
     */
    public void setPackageName( String packageName )
    {
        this.packageName = packageName;
    }

    /**
     * Gets the class name of the artifact.
     *
     * @return the class name of the artifact.
     */
    public String getClassName()
    {
        return className;
    }

    /**
     * Sets the class name of the artifact.
     *
     * @param className the class name of the artifact.
     */
    public void setClassName( String className )
    {
        this.className = className;
    }

}

/*
 *  jDTAUS Core Resource Mojo
 *  Copyright (c) 2005 Christian Schulte
 *
 *  Christian Schulte, Haldener Strasse 72, 58095 Hagen, Germany
 *  <cs@jdtaus.org> (+49 2331 3543887)
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
package org.jdtaus.mojo.resource.util;

import java.util.ResourceBundle;

/**
 * Generated {@code ResourceBundle}.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class Bundle extends Artifact
{

    /** The name of the project of the bundle. */
    private String projectName;

    /** The inception year of the project of the bundle. */
    private String inceptionYear;

    /** The developers of the project of the bundle. */
    private String[] developers;

    /** The {@code ResourceBundle} of the artifact. */
    private ResourceBundle resourceBundle;

    /**
     * Gets the name of the project of the bundle.
     *
     * @return the name of the project of the bundle.
     */
    public String getProjectName()
    {
        return projectName;
    }

    /**
     * Sets the name of the project of the bundle.
     *
     * @param projectName the name of the project of the bundle.
     */
    public void setProjectName( String projectName )
    {
        this.projectName = projectName;
    }

    /**
     * Gets the inception year of the project of the bundle.
     *
     * @return the inception year of the project of the bundle.
     */
    public String getInceptionYear()
    {
        return inceptionYear;
    }

    /**
     * Sets the inception year of the project of the bundle.
     *
     * @param inceptionYear the inception year of the project of the bundle.
     */
    public void setInceptionYear( String inceptionYear )
    {
        this.inceptionYear = inceptionYear;
    }

    /**
     * Gets the developers of the project of the bundle.
     *
     * @return the developers of the project of the bundle.
     */
    public String[] getDevelopers()
    {
        return developers;
    }

    /**
     * Sets the developers of the project of the bundle.
     *
     * @param developers the developers of the project of the bundle.
     */
    public void setDevelopers( String[] developers )
    {
        this.developers = developers;
    }

    /**
     * Gets the {@code ResourceBundle} of the artifact.
     *
     * @return the {@code ResourceBundle} of the artifact.
     */
    public ResourceBundle getResourceBundle()
    {
        return resourceBundle;
    }

    /**
     * Sets the {@code ResourceBundle} of the artifact.
     *
     * @param resourceBundle the {@code ResourceBundle} of the artifact.
     */
    public void setResourceBundle( ResourceBundle resourceBundle )
    {
        this.resourceBundle = resourceBundle;
    }

    /**
     * Gets the location of the artifact's {@code ResourceBundle}.
     *
     * @return the location of the artifact's {@code ResourceBundle}.
     */
    public String getResourceLocation()
    {
        final StringBuffer buf = new StringBuffer( 500 );
        buf.append( this.getPackageName() ).append( '.' ).
            append( this.getClassName() );

        return buf.toString().replaceAll( "\\.", "/" );
    }

}

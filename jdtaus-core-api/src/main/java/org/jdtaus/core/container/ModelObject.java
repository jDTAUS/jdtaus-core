/*
 *  jDTAUS Core API
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
package org.jdtaus.core.container;

import java.io.Serializable;

/**
 * Base model object.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class ModelObject implements Serializable
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.5.x classes. */
    private static final long serialVersionUID = 994580334374636235L;

    //---------------------------------------------------------------Constants--
    //--ModelObject-------------------------------------------------------------

    /**
     * The model version of the object.
     * @serial
     */
    private String modelVersion;

    /**
     * The documentation of the object.
     * @serial
     */
    private Text documentation;

    /**
     * Gets the model version of the object.
     *
     * @return the model version of the object.
     */
    public String getModelVersion()
    {
        return this.modelVersion;
    }

    /**
     * Setter for property {@code modelVersion}.
     *
     * @param value the new model version of the object.
     */
    public void setModelVersion( final String value )
    {
        this.modelVersion = value;
    }

    /**
     * Gets the documentation of the object.
     *
     * @return the documentation of the object.
     */
    public Text getDocumentation()
    {
        if ( this.documentation == null )
        {
            this.documentation = new Text();
        }

        return this.documentation;
    }

    /**
     * Setter for property {@code documentation}.
     *
     * @param value the new documentation of the object.
     */
    public void setDocumentation( final Text value )
    {
        this.documentation = value;
    }

    /**
     * Creates a string representing the properties of an instance.
     *
     * @param modelObject the instance to create a string for.
     *
     * @return a string representing the properties of {@code modelObject}.
     */
    String internalString( final ModelObject modelObject )
    {
        return new StringBuffer( 500 ).append( "modelVersion=" ).append(
            modelObject.getModelVersion() ).
            append( ", documentation=" ).
            append( modelObject.getDocumentation() ).toString();

    }
    //-------------------------------------------------------------ModelObject--
}

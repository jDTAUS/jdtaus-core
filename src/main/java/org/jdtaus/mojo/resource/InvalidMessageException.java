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
package org.jdtaus.mojo.resource;

import org.apache.maven.plugin.MojoExecutionException;

/**
 * Gets thrown for any invalid message.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class InvalidMessageException extends MojoExecutionException
{

    /**
     * Creates a new {@code InvalidMessageException} taking the invalid message
     * and a cause.
     *
     * @param invalidMessage the invalid message.
     * @param cause the cause of this excepion.
     */
    public InvalidMessageException( final String invalidMessage,
                                     final Throwable cause )
    {
        super( cause.getMessage() + " (" + invalidMessage + ")", cause );
    }

}

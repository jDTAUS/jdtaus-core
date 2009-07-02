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

import java.util.Locale;

/**
 * Gets thrown when an implementation fails to operate.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class ImplementationException extends IllegalStateException
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.0.x classes. */
    private static final long serialVersionUID = -3001627267463476552L;

    //---------------------------------------------------------------Constants--
    //--Constructors------------------------------------------------------------

    /**
     * Creates a new instance of {@code ImplementationException} taking
     * meta-data of the failing implementation.
     *
     * @param implementation the failing implementation.
     *
     * @throws NullPointerException if {@code implementation} is {@code null}.
     */
    public ImplementationException( final Implementation implementation )
    {
        super( ImplementationExceptionBundle.getInstance().
               getImplementationExceptionMessage(
               Locale.getDefault(), implementation.getIdentifier(),
               implementation.getVersion(), implementation.getModuleName(),
               "" ) );

        this.implementation = implementation;
    }

    /**
     * Creates a new instance of {@code ImplementationException} taking a
     * causing {@code Throwable}.
     *
     * @param implementation the failing implementation.
     * @param cause the causing throwable.
     *
     * @throws NullPointerException if either {@code implementation} or
     * {@code cause} is {@code null}.
     */
    public ImplementationException( final Implementation implementation,
                                    final Throwable cause )
    {
        super( ImplementationExceptionBundle.getInstance().
               getImplementationExceptionMessage(
               Locale.getDefault(), implementation.getIdentifier(),
               implementation.getVersion(), implementation.getModuleName(),
               cause.getMessage() ) );

        this.initCause( cause );
        this.implementation = implementation;
    }

    /**
     * Creates a new instance of {@code ImplementationException} taking a
     * message.
     *
     * @param implementation the failing implementation.
     * @param msg the message describing the error.
     *
     * @throws NullPointerException if {@code implementation} is {@code null}.
     */
    public ImplementationException( final Implementation implementation,
                                    final String msg )
    {
        super( ImplementationExceptionBundle.getInstance().
               getImplementationExceptionMessage(
               Locale.getDefault(), implementation.getIdentifier(),
               implementation.getVersion(), implementation.getModuleName(),
               msg ) );

        this.implementation = implementation;
    }

    //------------------------------------------------------------Constructors--
    //--ImplementationException-------------------------------------------------

    /**
     * The failing implementation.
     * @serial
     */
    private Implementation implementation;

    /**
     * Gets the failing implementation.
     *
     * @return the failing implementation or {@code null}.
     */
    public Implementation getImplementation()
    {
        return this.implementation;
    }

    //-------------------------------------------------ImplementationException--
}

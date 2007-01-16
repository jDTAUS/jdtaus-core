/*
 *  jDTAUS - DTAUS fileformat.
 *  Copyright (C) 2005 - 2007 Christian Schulte <cs@schulte.it>
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
package org.jdtaus.runtime.container;

import java.io.Serializable;
import org.jdtaus.common.container.Dependency;

/**
 * Extension to {@code Dependency} adding properties for XML processing.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class XMLDependency extends Dependency
    implements Cloneable, Serializable {

    //--XMLDependency-----------------------------------------------------------

    /**
     * Value of property {@code specificationIdentifier}.
     * @serial
     */
    private String specificationIdentifier;

    /**
     * Value of property {@code implementationName}.
     * @serial
     */
    private String implementationName;

    /**
     * Getter for property {@code specificationIdentifier}.
     *
     * @return identifier of the specification.
     */
    public String getSpecificationIdentifier() {
        if(this.specificationIdentifier == null) {
            this.specificationIdentifier = "";
        }

        return this.specificationIdentifier;
    }

    /**
     * Setter for property {@code specificationIdentifier}.
     *
     * @param value new identifier of the specification.
     */
    public void setSpecificationIdentifier(final String value) {
        if(this.specificationIdentifier == null ?
            value != null : !this.specificationIdentifier.equals(value)) {

            this.setSpecification(null);
        }

        this.specificationIdentifier = value;
    }

    /**
     * Getter for property {@code implementationName}.
     *
     * @return name of the implementation.
     */
    public String getImplementationName() {
        if(this.implementationName == null) {
            this.implementationName = "";
        }

        return this.implementationName;
    }

    /**
     * Setter for property {@code implementationName}.
     *
     * @param value new name of the implementation.
     */
    public void setImplementationName(final String value) {
        if(this.implementationName == null ?
            value != null : !this.implementationName.equals(value)) {

            this.setImplementation(null);
        }

        this.implementationName = value;
    }

    //-----------------------------------------------------------XMLDependency--

}

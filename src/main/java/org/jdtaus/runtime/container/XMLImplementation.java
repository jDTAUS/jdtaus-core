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
import java.util.Arrays;
import org.jdtaus.common.container.Implementation;

/**
 * Extension to {@code Implementation} adding properties for XML processing.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class XMLImplementation extends Implementation
    implements Cloneable, Serializable {

    //--XMLImplementation-------------------------------------------------------

    /**
     * Value of property {@code parentIdentifier}.
     * @serial
     */
    private String parentIdentifier;

    /**
     * Value of property {@code implementedIdentifiers}.
     * @serial
     */
    private String[] implementedIdentifiers;

    /**
     * Getter for property {@code parentIdentifier}.
     *
     * @return identifier of the parent implementation.
     */
    public String getParentIdentifier() {
        return this.parentIdentifier;
    }

    /**
     * Setter for property {@code parentIdentifier}.
     *
     * @param value new identifier of the parent implementation.
     */
    public void setParentIdentifier(final String value) {
        if(this.parentIdentifier == null ?
            value != null : !this.parentIdentifier.equals(value)) {

            this.setParent(null);
        }

        this.parentIdentifier = value;
    }

    /**
     * Getter for property {@code implementedIdentifiers}.
     *
     * @return identifiers of implemented specifications.
     */
    public String[] getImplementedIdentifiers() {
        if(this.implementedIdentifiers == null) {
            this.implementedIdentifiers = new String[0];
        }

        return this.implementedIdentifiers;
    }

    /**
     * Setter for property {@code implementedIdentifiers}.
     *
     * @param value new name of identifiers of implemented specifications.
     */
    public void setImplementedIdentifiers(final String[] value) {
        if(this.implementedIdentifiers == null ?
            value != null : value == null ? false :
                Arrays.equals(value, this.implementedIdentifiers)) {

            this.setImplementedSpecifications(null);
        }

        this.implementedIdentifiers = value;
    }

    //-------------------------------------------------------XMLImplementation--

}

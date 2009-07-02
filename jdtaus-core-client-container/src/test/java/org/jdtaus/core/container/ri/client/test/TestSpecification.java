/*
 *  jDTAUS Core RI Client Container
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
package org.jdtaus.core.container.ri.client.test;

/**
 * Specification used for testing the {@code Container} runtime.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public interface TestSpecification
{
    //--TestSpecification-------------------------------------------------------

    /**
     * Flag indicating if the {@code ContainerInitializer} contract is
     * implemented correctly by a {@code Container} implementation.
     *
     * @return {@code true} if the {@code ContainerInitializer} contract is
     * implemented correctly by a {@code Container} implementation;
     * {@code false} if no initialization is implemented by the
     * {@code Container}.
     */
    boolean isInitialized();

    // Test properties.
    boolean isBoolean();

    byte getByte();

    char getChar();

    double getDouble();

    float getFloat();

    int getInt();

    long getLong();

    short getShort();

    Boolean isBooleanObject();

    Byte getByteObject();

    Character getCharacterObject();

    Double getDoubleObject();

    Float getFloatObject();

    Integer getIntegerObject();

    Long getLongObject();

    Short getShortObject();

    String getStringObject();

    MultitonSpecification getDependency();

    //-------------------------------------------------------TestSpecification--
}

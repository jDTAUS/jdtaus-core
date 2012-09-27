/*
 *  jDTAUS Core RI Client Container
 *  Copyright (C) 2005 Christian Schulte
 *  <cs@schulte.it>
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
 * @version $JDTAUS$
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

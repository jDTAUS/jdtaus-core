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

import org.jdtaus.core.container.ContainerFactory;
import org.jdtaus.core.container.ContainerInitializer;

/**
 * Implementation used for testing the {@code Container} runtime.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class TestImplementation
    implements MultitonSpecification, ContainerInitializer
{
    //--Implementation----------------------------------------------------------

    // This section is managed by jdtaus-container-mojo.


    //----------------------------------------------------------Implementation--
    //--Constructors------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausConstructors
    // This section is managed by jdtaus-container-mojo.

    /** Standard implementation constructor <code>org.jdtaus.core.container.ri.client.test.TestImplementation</code>. */
    public TestImplementation()
    {
        super();
    }

// </editor-fold>//GEN-END:jdtausConstructors

    //------------------------------------------------------------Constructors--
    //--ContainerInitializer----------------------------------------------------

    /** Flag indicating that the {@link #initialize()} method got called. */
    private boolean initialized;

    public void initialize()
    {
        this.initialized = true;
    }

    //----------------------------------------------------ContainerInitializer--
    //--Dependencies------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausDependencies
    // This section is managed by jdtaus-container-mojo.

    /**
     * Gets the configured <code>CycleTestSpecification</code> implementation.
     *
     * @return the configured <code>CycleTestSpecification</code> implementation.
     */
    protected CycleTestSpecification getCycleTestSpecification()
    {
        return (CycleTestSpecification) ContainerFactory.getContainer().
            getDependency( this, "CycleTestSpecification");

    }

    /**
     * Gets the configured <code>MultitonSpecification</code> implementation.
     *
     * @return the configured <code>MultitonSpecification</code> implementation.
     */
    protected MultitonSpecification getMultitonSpecification()
    {
        return (MultitonSpecification) ContainerFactory.getContainer().
            getDependency( this, "MultitonSpecification");

    }

// </editor-fold>//GEN-END:jdtausDependencies

    //------------------------------------------------------------Dependencies--
    //--Properties--------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausProperties
    // This section is managed by jdtaus-container-mojo.

    /**
     * Gets the value of property <code>stringObject</code>.
     *
     * @return Test-Property
     */
    public java.lang.String getStringObject()
    {
        return (java.lang.String) ContainerFactory.getContainer().
            getProperty( this, "stringObject" );

    }

    /**
     * Gets the value of property <code>shortObject</code>.
     *
     * @return Test-Property
     */
    public java.lang.Short getShortObject()
    {
        return (java.lang.Short) ContainerFactory.getContainer().
            getProperty( this, "shortObject" );

    }

    /**
     * Gets the value of property <code>short</code>.
     *
     * @return Test-Property
     */
    public short getShort()
    {
        return ( (java.lang.Short) ContainerFactory.getContainer().
            getProperty( this, "short" ) ).shortValue();

    }

    /**
     * Gets the value of property <code>longObject</code>.
     *
     * @return Test-Property
     */
    public java.lang.Long getLongObject()
    {
        return (java.lang.Long) ContainerFactory.getContainer().
            getProperty( this, "longObject" );

    }

    /**
     * Gets the value of property <code>long</code>.
     *
     * @return Test-Property
     */
    public long getLong()
    {
        return ( (java.lang.Long) ContainerFactory.getContainer().
            getProperty( this, "long" ) ).longValue();

    }

    /**
     * Gets the value of property <code>integerObject</code>.
     *
     * @return Test-Property
     */
    public java.lang.Integer getIntegerObject()
    {
        return (java.lang.Integer) ContainerFactory.getContainer().
            getProperty( this, "integerObject" );

    }

    /**
     * Gets the value of property <code>int</code>.
     *
     * @return Test-Property
     */
    public int getInt()
    {
        return ( (java.lang.Integer) ContainerFactory.getContainer().
            getProperty( this, "int" ) ).intValue();

    }

    /**
     * Gets the value of property <code>floatObject</code>.
     *
     * @return Test-Property
     */
    public java.lang.Float getFloatObject()
    {
        return (java.lang.Float) ContainerFactory.getContainer().
            getProperty( this, "floatObject" );

    }

    /**
     * Gets the value of property <code>float</code>.
     *
     * @return Test-Property
     */
    public float getFloat()
    {
        return ( (java.lang.Float) ContainerFactory.getContainer().
            getProperty( this, "float" ) ).floatValue();

    }

    /**
     * Gets the value of property <code>doubleObject</code>.
     *
     * @return Test-Property
     */
    public java.lang.Double getDoubleObject()
    {
        return (java.lang.Double) ContainerFactory.getContainer().
            getProperty( this, "doubleObject" );

    }

    /**
     * Gets the value of property <code>double</code>.
     *
     * @return Test-Property
     */
    public double getDouble()
    {
        return ( (java.lang.Double) ContainerFactory.getContainer().
            getProperty( this, "double" ) ).doubleValue();

    }

    /**
     * Gets the value of property <code>characterObject</code>.
     *
     * @return Test-Property
     */
    public java.lang.Character getCharacterObject()
    {
        return (java.lang.Character) ContainerFactory.getContainer().
            getProperty( this, "characterObject" );

    }

    /**
     * Gets the value of property <code>char</code>.
     *
     * @return Test-Property
     */
    public char getChar()
    {
        return ( (java.lang.Character) ContainerFactory.getContainer().
            getProperty( this, "char" ) ).charValue();

    }

    /**
     * Gets the value of property <code>byteObject</code>.
     *
     * @return Test-Property
     */
    public java.lang.Byte getByteObject()
    {
        return (java.lang.Byte) ContainerFactory.getContainer().
            getProperty( this, "byteObject" );

    }

    /**
     * Gets the value of property <code>byte</code>.
     *
     * @return Test-Property
     */
    public byte getByte()
    {
        return ( (java.lang.Byte) ContainerFactory.getContainer().
            getProperty( this, "byte" ) ).byteValue();

    }

    /**
     * Gets the value of property <code>booleanObject</code>.
     *
     * @return Test-Property
     */
    public java.lang.Boolean isBooleanObject()
    {
        return (java.lang.Boolean) ContainerFactory.getContainer().
            getProperty( this, "booleanObject" );

    }

    /**
     * Gets the value of property <code>boolean</code>.
     *
     * @return Test-Property
     */
    public boolean isBoolean()
    {
        return ( (java.lang.Boolean) ContainerFactory.getContainer().
            getProperty( this, "boolean" ) ).booleanValue();

    }

// </editor-fold>//GEN-END:jdtausProperties

    //--------------------------------------------------------------Properties--
    //--TestSpecification-------------------------------------------------------

    public boolean isInitialized()
    {
        return this.initialized;
    }

    public MultitonSpecification getDependency()
    {
        return this.getMultitonSpecification();
    }

    //-------------------------------------------------------TestSpecification--
}

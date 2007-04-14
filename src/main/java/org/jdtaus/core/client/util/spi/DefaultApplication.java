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
package org.jdtaus.core.client.util.spi;

import java.io.File;
import org.jdtaus.core.client.util.Application;
import org.jdtaus.core.container.ContainerInitializer;
import org.jdtaus.core.container.Dependency;
import org.jdtaus.core.container.Implementation;
import org.jdtaus.core.container.ImplementationException;
import org.jdtaus.core.container.ModelFactory;
import org.jdtaus.core.container.Property;
import org.jdtaus.core.container.PropertyException;

/**
 * {@code Application} reference implementation.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class DefaultApplication implements Application, ContainerInitializer
{

    //--Implementation----------------------------------------------------------

    // This section is generated by jdtaus-source-plugin.

    /** Meta-data describing the implementation. */
    private static final Implementation META =
        ModelFactory.getModel().getModules().
        getImplementation(DefaultApplication.class.getName());

    //----------------------------------------------------------Implementation--
    //--Constructors------------------------------------------------------------

    // This section is generated by jdtaus-source-plugin.

    /** Protected <code>DefaultApplication</code> implementation constructor.
    * @param meta Implementation meta-data.
    */
    protected DefaultApplication(final Implementation meta)
    {
        super();
        Property p;

        p = meta.getProperties().getProperty("stateDirectoryName");
        this._stateDirectoryName = (java.lang.String) p.getValue();


        p = meta.getProperties().getProperty("dataDirectoryName");
        this._dataDirectoryName = (java.lang.String) p.getValue();


        p = meta.getProperties().getProperty("configDirectoryName");
        this._configDirectoryName = (java.lang.String) p.getValue();


        p = meta.getProperties().getProperty("binDirectoryName");
        this._binDirectoryName = (java.lang.String) p.getValue();

    }
    /** Protected <code>DefaultApplication</code> dependency constructor.
    * @param meta Dependency meta-data.
    */
    protected DefaultApplication(final Dependency meta)
    {
        super();
        Property p;

        p = meta.getProperties().getProperty("stateDirectoryName");
        this._stateDirectoryName = (java.lang.String) p.getValue();


        p = meta.getProperties().getProperty("dataDirectoryName");
        this._dataDirectoryName = (java.lang.String) p.getValue();


        p = meta.getProperties().getProperty("configDirectoryName");
        this._configDirectoryName = (java.lang.String) p.getValue();


        p = meta.getProperties().getProperty("binDirectoryName");
        this._binDirectoryName = (java.lang.String) p.getValue();

    }

    //------------------------------------------------------------Constructors--
    //--ContainerInitializer----------------------------------------------------

    /**
     * Checks configured property values by calling
     * {@link #assertValidProperties()}.
     *
     * @throws PropertyException for properties with invalid values.
     */
    public void initialize()
    {
        this.assertValidProperties();
    }

    //----------------------------------------------------ContainerInitializer--
    //--Dependencies------------------------------------------------------------

    // This section is generated by jdtaus-source-plugin.


    //------------------------------------------------------------Dependencies--
    //--Properties--------------------------------------------------------------

    // This section is generated by jdtaus-source-plugin.

    /**
     * Property {@code stateDirectoryName}.
     * @serial
     */
    private java.lang.String _stateDirectoryName;

    /** <code>stateDirectoryName</code> property getter. */
    protected java.lang.String getStateDirectoryName()
    {
        return this._stateDirectoryName;
    }

    /**
     * Property {@code dataDirectoryName}.
     * @serial
     */
    private java.lang.String _dataDirectoryName;

    /** <code>dataDirectoryName</code> property getter. */
    protected java.lang.String getDataDirectoryName()
    {
        return this._dataDirectoryName;
    }

    /**
     * Property {@code configDirectoryName}.
     * @serial
     */
    private java.lang.String _configDirectoryName;

    /** <code>configDirectoryName</code> property getter. */
    protected java.lang.String getConfigDirectoryName()
    {
        return this._configDirectoryName;
    }

    /**
     * Property {@code binDirectoryName}.
     * @serial
     */
    private java.lang.String _binDirectoryName;

    /** <code>binDirectoryName</code> property getter. */
    protected java.lang.String getBinDirectoryName()
    {
        return this._binDirectoryName;
    }


    //--------------------------------------------------------------Properties--
    //--Application-------------------------------------------------------------

    public final File getBinDirectory()
    {
        final File ret = new File(this.getBaseDirectory(),
            this.getBinDirectoryName());

        if(!ret.exists())
        {
            ret.mkdirs();
        }

        return ret;
    }

    public final File getConfigDirectory()
    {
        final File ret = new File(this.getBaseDirectory(),
            this.getConfigDirectoryName());

        if(!ret.exists())
        {
            ret.mkdirs();
        }

        return ret;
    }

    public final File getDataDirectory()
    {
        final File ret = new File(this.getBaseDirectory(),
            this.getDataDirectoryName());

        if(!ret.exists())
        {
            ret.mkdirs();
        }

        return ret;
    }

    public final File getStateDirectory()
    {
        final File ret = new File(this.getBaseDirectory(),
            this.getStateDirectoryName());

        if(!ret.exists())
        {
            ret.mkdirs();
        }

        return ret;
    }

    //-------------------------------------------------------------Application--
    //--DefaultApplication------------------------------------------------------

    /** Name of the system property holding the path of the base directory. */
    private static final String SYS_BASEDIRECTORY =
        DefaultApplication.class.getName() + ".baseDirectory";

    /** Creates a new {@code DefaultApplication} instance. */
    public DefaultApplication()
    {
        this(DefaultApplication.META);
        this.initialize();
    }

    /**
     * Gets the base directory the application is installed in.
     * <p>The base directory the application is installed in is specified via
     * the system property with name
     * {@code org.jdtaus.core.client.spi.DefaultApplication.baseDirectory}
     * holding the absolute path of the directory the application is installed
     * in.</p>
     *
     * @return the base directory the application is installed in.
     *
     * @throws PropertyException if the system property is not set to any value.
     * @throws ImplementationException if the base directory does not exist.
     */
    public File getBaseDirectory()
    {
        final File file;
        final String path =
            System.getProperty(DefaultApplication.SYS_BASEDIRECTORY);

        if(path == null)
        {
            throw new PropertyException(DefaultApplication.SYS_BASEDIRECTORY,
                path);

        }

        file = new File(path);

        if(!file.exists() || !file.isDirectory())
        {
            throw new PropertyException(DefaultApplication.SYS_BASEDIRECTORY,
                path);

        }

        return file;
    }

    /**
     * Checks configured properties.
     *
     * @throws PropertyException for invalid property values.
     */
    protected void assertValidProperties()
    {
        if(this.getBinDirectoryName() == null)
        {
            throw new PropertyException("binDirectoryName", null);
        }
        if(this.getConfigDirectoryName() == null)
        {
            throw new PropertyException("configDirectoryName", null);
        }
        if(this.getDataDirectoryName() == null)
        {
            throw new PropertyException("dataDirectoryName", null);
        }
        if(this.getStateDirectoryName() == null)
        {
            throw new PropertyException("stateDirectoryName", null);
        }
    }

    //------------------------------------------------------DefaultApplication--

}

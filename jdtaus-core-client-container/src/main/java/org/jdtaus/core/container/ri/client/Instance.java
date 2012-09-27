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
package org.jdtaus.core.container.ri.client;

import java.util.HashMap;
import java.util.Map;
import org.jdtaus.core.container.Dependency;
import org.jdtaus.core.container.Implementation;
import org.jdtaus.core.container.Messages;
import org.jdtaus.core.container.Properties;

/**
 * Instance meta-data.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
class Instance
{

    /** Scope the instance applies to. */
    private int scope;

    /** Class name of the instance. */
    private String className;

    /** Model version of the instance. */
    private String modelVersion;

    /** Name of the module defining the instance. */
    private String moduleName;

    /** Map of dependency objects of the instance. */
    private Map dependencies;

    /** Implementation meta-data of the instance. */
    private Implementation implementation;

    /** Dependency meta-data of the instance. */
    private Dependency dependency;

    /** The classloader loading the instance. */
    private ClassLoader classLoader;

    public Instance( final ClassLoader classLoader, final int scope,
                     final String className, final String modelVersion,
                     final String moduleName )
    {
        this.classLoader = classLoader;
        this.scope = scope;
        this.className = className;
        this.modelVersion = modelVersion;
        this.moduleName = moduleName;
    }

    public ClassLoader getClassLoader()
    {
        return this.classLoader;
    }

    public int getScope()
    {
        return this.scope;
    }

    public String getClassName()
    {
        return this.className;
    }

    public String getModelVersion()
    {
        return this.modelVersion;
    }

    public String getModuleName()
    {
        return this.moduleName;
    }

    public Implementation getImplementation()
    {
        return this.implementation;
    }

    public void setImplementation( final Implementation implementation )
    {
        this.implementation = implementation;
    }

    public Dependency getDependency()
    {
        return this.dependency;
    }

    public void setDependency( final Dependency dependency )
    {
        this.dependency = dependency;
    }

    public Properties getProperties()
    {
        Properties properties = null;

        if ( this.implementation != null )
        {
            properties = this.implementation.getProperties();
        }
        else if ( this.dependency != null )
        {
            properties = this.dependency.getProperties();
        }

        return properties;
    }

    public Messages getMessages()
    {
        Messages messages = null;

        if ( this.implementation != null )
        {
            messages = this.implementation.getMessages();
        }
        else if ( this.dependency != null )
        {
            messages = this.dependency.getImplementation().getMessages();
        }

        return messages;
    }

    public Object getDependencyObject(
        final String dependencyName )
    {
        if ( this.dependencies == null )
        {
            this.dependencies = new HashMap();
        }

        return this.dependencies.get( dependencyName );
    }

    public void setDependencyObject( final String dependencyName,
                                     final Object object )
    {
        if ( this.dependencies == null )
        {
            this.dependencies = new HashMap();
        }

        this.dependencies.put( dependencyName, object );
    }

}

// SECTION-START[License Header]
// <editor-fold defaultstate="collapsed" desc=" Generated License ">
/*
 *   jDTAUS
 *   Copyright (C) Christian Schulte, 2012-039
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published
 *   by the Free Software Foundation, either version 2.1 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *   $JDTAUS$
 *
 */
// </editor-fold>
// SECTION-END
package org.jdtaus.core.container.ri.jomc;

import java.util.Locale;
import org.jdtaus.core.container.Container;
import org.jdtaus.core.container.ContainerFactory;
import org.jomc.ObjectManagerFactory;

// SECTION-START[Documentation]
// <editor-fold defaultstate="collapsed" desc=" Generated Documentation ">
/**
 * jDTAUS {@code Container} implementation delegating to the JOMC {@code ObjectManager}.
 *
 * <dl>
 *   <dt><b>Identifier:</b></dt><dd>jDTAUS Core &#8273; JOMC Container &#8273; ObjectManagerContainer</dd>
 *   <dt><b>Name:</b></dt><dd>jDTAUS Core &#8273; JOMC Container &#8273; ObjectManagerContainer</dd>
 *   <dt><b>Specifications:</b></dt>
 *     <dd>org.jdtaus.core.container.Container</dd>
 *   <dt><b>Abstract:</b></dt><dd>No</dd>
 *   <dt><b>Final:</b></dt><dd>No</dd>
 *   <dt><b>Stateless:</b></dt><dd>No</dd>
 * </dl>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a> 1.0
 * @version 1.13
 */
// </editor-fold>
// SECTION-END
// SECTION-START[Annotations]
// <editor-fold defaultstate="collapsed" desc=" Generated Annotations ">
@javax.annotation.Generated( value = "org.jomc.tools.SourceFileProcessor 1.3", comments = "See http://jomc.sourceforge.net/jomc/1.3/jomc-tools-1.3" )
// </editor-fold>
// SECTION-END
public class ObjectManagerContainer
    implements
    org.jdtaus.core.container.Container
{
    // SECTION-START[Container]

    private static final Container instance = ContainerFactory.newContainer();

    public static Container getContainer()
    {
        return instance;
    }

    @Override
    public Object getObject( final Class specification )
    {
        return ObjectManagerFactory.getObjectManager( this.getClass().getClassLoader() ).getObject( specification );
    }

    @Override
    public Object getObject( final Class specification, final String implementationName )
    {
        return ObjectManagerFactory.getObjectManager( this.getClass().getClassLoader() ).
            getObject( specification, implementationName );

    }

    @Override
    public Object getDependency( final Object object, final String dependencyName )
    {
        return ObjectManagerFactory.getObjectManager( this.getClass().getClassLoader() ).
            getDependency( object, dependencyName );

    }

    @Override
    public Object getProperty( final Object object, final String propertyName )
    {
        return ObjectManagerFactory.getObjectManager( this.getClass().getClassLoader() ).
            getProperty( object, propertyName );

    }

    @Override
    public String getMessage( final Object object, final String messageName, final Locale locale,
                              final Object arguments )
    {
        return ObjectManagerFactory.getObjectManager( this.getClass().getClassLoader() ).
            getMessage( object, messageName, locale, (Object[]) arguments );

    }

    @Override
    @Deprecated
    public Object getImplementation( final Class specification, final String implementationName )
    {
        return this.getObject( specification, implementationName );
    }

    @Override
    @Deprecated
    public Object getDependency( final Class implementation, final String dependencyName )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public Object getObject( final String specificationIdentifier )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public Object getObject( final String specificationIdentifier, final String implementationName )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public String getMessage( final Object object, final String messageName, final Object arguments )
    {
        return this.getMessage( object, messageName, Locale.getDefault(), arguments );
    }

    // SECTION-END
    // SECTION-START[ObjectManagerContainer]
    // SECTION-END
    // SECTION-START[Constructors]
    // <editor-fold defaultstate="collapsed" desc=" Generated Constructors ">
    /** Creates a new {@code ObjectManagerContainer} instance. */
    @javax.annotation.Generated( value = "org.jomc.tools.SourceFileProcessor 1.3", comments = "See http://jomc.sourceforge.net/jomc/1.3/jomc-tools-1.3" )
    public ObjectManagerContainer()
    {
        // SECTION-START[Default Constructor]
        super();
        // SECTION-END
    }
    // </editor-fold>
    // SECTION-END
    // SECTION-START[Dependencies]
    // SECTION-END
    // SECTION-START[Properties]
    // SECTION-END
    // SECTION-START[Messages]
    // SECTION-END
}

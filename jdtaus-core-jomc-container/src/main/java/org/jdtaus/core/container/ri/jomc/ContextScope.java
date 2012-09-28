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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.jdtaus.core.container.ContextFactory;

// SECTION-START[Documentation]
// <editor-fold defaultstate="collapsed" desc=" Generated Documentation ">
/**
 * JOMC {@code Scope} implementation delegating to the jDTAUS {@code Context}.
 *
 * <dl>
 *   <dt><b>Identifier:</b></dt><dd>jDTAUS Core &#8273; JOMC Container &#8273; Context Scope</dd>
 *   <dt><b>Name:</b></dt><dd>jDTAUS Context</dd>
 *   <dt><b>Specifications:</b></dt>
 *     <dd>org.jomc.spi.Scope @ 1.0</dd>
 *   <dt><b>Abstract:</b></dt><dd>No</dd>
 *   <dt><b>Final:</b></dt><dd>No</dd>
 *   <dt><b>Stateless:</b></dt><dd>No</dd>
 * </dl>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a> 1.0
 * @version 1.13-SNAPSHOT
 */
// </editor-fold>
// SECTION-END
// SECTION-START[Annotations]
// <editor-fold defaultstate="collapsed" desc=" Generated Annotations ">
@javax.annotation.Generated( value = "org.jomc.tools.SourceFileProcessor 1.3", comments = "See http://jomc.sourceforge.net/jomc/1.3/jomc-tools-1.3" )
// </editor-fold>
// SECTION-END
public class ContextScope
    implements
    org.jomc.spi.Scope
{
    // SECTION-START[Scope]

    @Override
    public Map<String, Object> getObjects()
    {
        final Collection<String> objectKeys = ContextFactory.getContext().getObjectKeys();
        final Map<String, Object> objectMap = new HashMap<String, Object>( objectKeys.size() );

        for ( final String objectKey : objectKeys )
        {
            objectMap.put( objectKey, this.getObject( objectKey ) );
        }

        return Collections.unmodifiableMap( objectMap );
    }

    @Override
    public Object getObject( final String string ) throws NullPointerException
    {
        return ContextFactory.getContext().getObject( string );
    }

    @Override
    public Object putObject( final String string, final Object o ) throws NullPointerException
    {
        return ContextFactory.getContext().setObject( string, o );
    }

    @Override
    public Object removeObject( final String string ) throws NullPointerException
    {
        return ContextFactory.getContext().removeObject( string );
    }

    // SECTION-END
    // SECTION-START[ContextScope]
    // SECTION-END
    // SECTION-START[Constructors]
    // <editor-fold defaultstate="collapsed" desc=" Generated Constructors ">
    /** Creates a new {@code ContextScope} instance. */
    @javax.annotation.Generated( value = "org.jomc.tools.SourceFileProcessor 1.3", comments = "See http://jomc.sourceforge.net/jomc/1.3/jomc-tools-1.3" )
    public ContextScope()
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

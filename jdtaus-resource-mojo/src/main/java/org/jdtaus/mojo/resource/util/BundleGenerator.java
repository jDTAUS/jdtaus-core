/*
 *  jDTAUS Core Resource Mojo
 *  Copyright (c) 2005 Christian Schulte
 *
 *  Christian Schulte, Haldener Strasse 72, 58095 Hagen, Germany
 *  <cs@jdtaus.org> (+49 2331 3543887)
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
package org.jdtaus.mojo.resource.util;

import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.jdtaus.mojo.resource.model.Implementation;
import org.jdtaus.mojo.resource.model.ModelManager;
import org.jdtaus.mojo.resource.model.Module;

/**
 * Generates source code of an accessor class to a {@code ResourceBundle}.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 * @plexus.component role="org.jdtaus.mojo.resource.util.BundleGenerator"
 *                   role-hint="default"
 */
public class BundleGenerator
{

    /** Name of the generator. */
    private static final String GENERATOR_NAME =
        BundleGenerator.class.getName();

    /** Constant for the version of the generator. */
    private static final String GENERATOR_VERSION = "3.0";

    /** Name of the velocity classpath resource loader implementation. */
    private static final String VELOCITY_RESOURCE_LOADER =
        "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader";

    /** Location of the {@code Bundle.java.vm} template. */
    private static final String TEMPLATE_LOCATION =
        "META-INF/templates/Bundle.java.vm";

    /** {@code VelocityEngine} of the generator. */
    private VelocityEngine velocityEngine;

    /**
     * {@code ModelManager} of the genrator.
     * @plexus.requirement
     */
    private ModelManager modelManager;

    /**
     * Generates a java source file for a bundle.
     *
     * @param module the module defining the {@code bundle}.
     * @param implementation the implementation to generate a java source file
     * for.
     * @param writer the writer to write the java source file to.
     *
     * @throws Exception if generating the bundle fails.
     */
    public void generateJava( final Module module,
                              final Implementation implementation,
                              final Writer writer )
        throws Exception
    {
        final VelocityContext ctx = new VelocityContext();
        ctx.put( "module", module );
        ctx.put( "implementation", implementation );
        ctx.put( "modelManager", this.modelManager );
        ctx.put( "generatorName", GENERATOR_NAME );
        ctx.put( "generatorVersion", GENERATOR_VERSION );
        ctx.put( "templateLocation", TEMPLATE_LOCATION );
        ctx.put( "comment", Boolean.TRUE );
        ctx.put( "now", new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSSZ" ).
                 format( new Date() ) );

        this.getVelocity().mergeTemplate( TEMPLATE_LOCATION, ctx, writer );
    }

    /**
     * Gets the {@code VelocityEngine} used for generating source code.
     *
     * @return the {@code VelocityEngine} used for generating source code.
     *
     * @throws Exception if initializing a new velocity engine fails.
     */
    private VelocityEngine getVelocity() throws Exception
    {
        if ( this.velocityEngine == null )
        {
            final VelocityEngine engine = new VelocityEngine();
            final Properties props = new Properties();
            props.put( "resource.loader", "class" );
            props.put( "class.resource.loader.class",
                       VELOCITY_RESOURCE_LOADER );

            engine.init( props );
            this.velocityEngine = engine;
        }

        return this.velocityEngine;
    }
}

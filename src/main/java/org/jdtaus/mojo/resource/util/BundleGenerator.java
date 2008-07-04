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
import java.util.Properties;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 * Generates source code of an accessor class to a {@code ResourceBundle}.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class BundleGenerator
{

  /** Singleton instance. */
  private static BundleGenerator instance = new BundleGenerator();

  /** Name of the velocity classpath resource loader implementation. */
  private static final String VELOCITY_RESOURCE_LOADER =
      "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader";

  /** Location of the {@code Bundle.java.vm} template. */
  private static final String TEMPLATE_LOCATION =
      "META-INF/templates/Bundle.java.vm";

  /** {@code VelocityEngine} of the generator. */
  private VelocityEngine velocityEngine;

  /**
   * Gets the singleton {@code BundleGenerator} instance.
   *
   * @return the singleton {@code BundleGenerator} instance.
   */
  public static BundleGenerator getInstance()
  {
    return instance;
  }

  /**
   * Writes the source code to a writer.
   *
   * @param artifact the artifact to write.
   * @param bundle the bundle to write.
   * @param includeComments {@code true} to write comments along the generated
   * source code; {@code false} to not write any comments.
   * @param includeLicense {@code true} to write a license at the beginning of
   * the generated source code; {@code false} to not write any license texts.
   * @param writer the writer to write the source code to.
   *
   * @throws NullPointerException if {@code artifact}, {@code bundle} or
   * {@code writer} is {@code null}.
   * @throws Exception if generating the bundle fails.
   */
  public void generate( final Artifact artifact, final Bundle bundle,
                        final boolean includeComments,
                        final boolean includeLicense, final Writer writer )
      throws Exception
  {
    if ( artifact == null )
    {
      throw new NullPointerException( "artifact" );
    }
    if ( bundle == null )
    {
      throw new NullPointerException( "bundle" );
    }
    if ( writer == null )
    {
      throw new NullPointerException( "writer" );
    }

    final VelocityContext ctx = new VelocityContext();
    ctx.put( "artifact", artifact );
    ctx.put( "bundle", bundle );
    ctx.put( "includeComments", Boolean.valueOf( includeComments ) );
    ctx.put( "includeLicense", Boolean.valueOf( includeLicense ) );
    ctx.put( "generator", this );

    this.getVelocity().mergeTemplate( TEMPLATE_LOCATION, ctx, writer );
  }

  /**
   * Gets the name of a getter method returning an instance of
   * {@code MessageFormat} for a given bundle key.
   *
   * @param key a key from a bundle to get the name of the corresponding getter
   * method for.
   *
   * @return the name of the getter method for a given bundle key.
   *
   * @throws NullPointerException if {@code key} is {@code null}.
   */
  public String getMessageGetterNameForKey( String key )
  {
    if ( key == null )
    {
      throw new NullPointerException( "key" );
    }

    final char[] c = key.toCharArray();
    if ( Character.isLowerCase( c[0] ) )
    {
      c[0] = Character.toUpperCase( c[0] );
    }

    key = String.valueOf( c );

    return new StringBuffer( 255 ).append( "get" ).append( key ).
        append( "Message" ).toString();

  }

  /**
   * Gets the name of a getter method returning an instance of {@code String}
   * for a given bundle key.
   *
   * @param key a key from a bundle to get the name of the corresponding getter
   * method for.
   *
   * @return the name of the getter method for a given bundle key.
   *
   * @throws NullPointerException if {@code key} is {@code null}.
   */
  public String getStringGetterNameForKey( String key )
  {
    if ( key == null )
    {
      throw new NullPointerException( "key" );
    }

    final char[] c = key.toCharArray();
    if ( Character.isLowerCase( c[0] ) )
    {
      c[0] = Character.toUpperCase( c[0] );
    }

    key = String.valueOf( c );

    return new StringBuffer( 255 ).append( "get" ).append( key ).
        append( "Text" ).toString();

  }

  /**
   * Removes any opening or closing java comment markers from a given string.
   *
   * @param str the string to normalize.
   *
   * @return {@code str} with any opening or closing java comment marker
   * removed.
   */
  public String normalizeComment( final String str )
  {
    String normalized = str == null
        ? ""
        : str;

    normalized = normalized.replaceAll( "\\/\\*\\*", "/*" );
    normalized = normalized.replaceAll( "\\*/", "/" );

    return normalized;
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
      props.put( "class.resource.loader.class", VELOCITY_RESOURCE_LOADER );
      engine.init( props );
      this.velocityEngine = engine;
    }

    return this.velocityEngine;
  }

}

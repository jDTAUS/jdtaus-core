/*
 *  jDTAUS Container Mojo
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
package org.jdtaus.core.container.mojo;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Set;
import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.doxia.tools.SiteTool;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReport;
import org.apache.maven.reporting.MavenReportException;
import org.jdtaus.core.container.ContainerError;
import org.jdtaus.core.container.ContextError;
import org.jdtaus.core.container.Dependencies;
import org.jdtaus.core.container.Dependency;
import org.jdtaus.core.container.Implementation;
import org.jdtaus.core.container.Message;
import org.jdtaus.core.container.Messages;
import org.jdtaus.core.container.MissingModuleException;
import org.jdtaus.core.container.Model;
import org.jdtaus.core.container.ModelError;
import org.jdtaus.core.container.ModelFactory;
import org.jdtaus.core.container.Module;
import org.jdtaus.core.container.Properties;
import org.jdtaus.core.container.Property;
import org.jdtaus.core.container.Specification;

/**
 * Generates a report for the artifact's container metadata.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 * @goal report
 * @requiresDependencyResolution runtime
 */
public class ContainerReport extends AbstractMavenReport
{
    //--Configuration-----------------------------------------------------------

    /**
     * The output directory for the report. Note that this parameter is only
     * evaluated if the goal is run directly from the command line. If the goal
     * is run indirectly as part of a site generation, the output directory
     * configured in Maven Site Plugin is used instead.
     *
     * @parameter default-value="${project.reporting.outputDirectory}"
     * @required
     */
    private File outputDirectory;

    /**
     * Build output directory.
     * @parameter expression="${project.build.directory}/container"
     */
    private File buildOutputDirectory;

    /**
     * Name of the module to generate a report for.
     *
     * @parameter default-value="${project.name}"
     * @optional
     */
    private String moduleName;

    /**
     * Location of the javadoc documentation to link to. Keep this in sync with
     * the javadoc plugin.
     *
     * @parameter default-value="apidocs"
     * @optional
     */
    private String javadocDirectory;

    /**
     * {@code true} to generate links to javadoc documentation. {@code false}
     * to not generate links to javadoc documentation.
     *
     * @parameter default-value="true"
     * @optional
     */
    private boolean linkJavadoc;

    /**
     * Generates the site report
     *
     * @component
     * @required
     * @readonly
     */
    private Renderer siteRenderer;

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * SiteTool.
     *
     * @component role="org.apache.maven.doxia.tools.SiteTool"
     * @required
     * @readonly
     */
    private SiteTool siteTool;

    /**
     * Gets the Site Tool implementation.
     *
     * @return Site Tool implementation.
     */
    protected final SiteTool getSiteTool()
    {
        return this.siteTool;
    }

    //-----------------------------------------------------------Configuration--
    //--AbstractMavenReport-----------------------------------------------------

    public String getOutputName()
    {
        return "container-report";
    }

    public String getCategoryName()
    {
        return MavenReport.CATEGORY_PROJECT_REPORTS;
    }

    public String getName( final Locale locale )
    {
        return ContainerReportBundle.getInstance().
            getReportNameMessage( locale );

    }

    public String getDescription( final Locale locale )
    {
        return ContainerReportBundle.getInstance().
            getReportDescriptionMessage( locale );

    }

    public void setReportOutputDirectory( final File reportOutputDirectory )
    {
        super.setReportOutputDirectory( reportOutputDirectory );
        this.outputDirectory = reportOutputDirectory;
    }

    public boolean canGenerateReport()
    {
        boolean canGenerate = super.canGenerateReport();

        if ( canGenerate )
        {
            final File modelFile = new File( this.buildOutputDirectory,
                                             "container-report.xml" );

            canGenerate = modelFile.exists();
        }

        return canGenerate;
    }

    protected Renderer getSiteRenderer()
    {
        return this.siteRenderer;
    }

    protected String getOutputDirectory()
    {
        return this.outputDirectory.getAbsolutePath();
    }

    protected MavenProject getProject()
    {
        return this.project;
    }

    protected void executeReport( final Locale locale )
        throws MavenReportException
    {
        final ClassLoader mavenLoader =
                          Thread.currentThread().getContextClassLoader();

        try
        {
            final File modelFile = new File( this.buildOutputDirectory,
                                             "container-report.xml" );

            final ResourceLoader resourceLoader = new ResourceLoader(
                this.getClass().getClassLoader() );

            resourceLoader.addResource( "META-INF/jdtaus/module.xml",
                                        modelFile.toURI().toURL() );

            Thread.currentThread().setContextClassLoader( resourceLoader );
            AbstractContainerMojo.enableThreadContextClassLoader();

            this.getSink().head();
            this.getSink().title();
            this.getSink().text( this.getName( locale ) );
            this.getSink().title_();
            this.getSink().head_();

            this.getSink().body();

            this.getSink().section1();
            this.getSink().sectionTitle1();
            this.getSink().text(
                ContainerReportBundle.getInstance().
                getOverviewTitleMessage( locale ) );

            this.getSink().sectionTitle1_();

            this.getSink().paragraph();
            this.getSink().text( this.getDescription( locale ) );
            this.getSink().paragraph_();

            final Model model = ModelFactory.newModel();
            if ( this.moduleName != null )
            {
                final Module module = model.getModules().
                    getModule( this.moduleName );

                this.generateModuleReport( module, locale );

                this.generateUnresolvedDependenciesReport(
                    this.getUnresolvedDependencies( model ), locale );

                // End the overview section.
                this.getSink().section1_();

                if ( module.getImplementations().size() > 0 ||
                     module.getSpecifications().size() > 0 )
                {
                    this.getSink().section1();
                    this.getSink().sectionTitle1();
                    this.getSink().text(
                        ContainerReportBundle.getInstance().
                        getDetailsTitleMessage( locale ) );

                    this.getSink().sectionTitle1_();

                    this.generateDetails( module, locale );

                    this.getSink().section1_();
                }
            }
            else
            {
                this.generateUnresolvedDependenciesReport(
                    this.getUnresolvedDependencies( model ), locale );

                // End the overview section.
                this.getSink().section1_();
            }

            this.getSink().body_();
        }
        catch ( ContextError e )
        {
            final MavenReportException mre =
                new MavenReportException( e.getMessage() );

            mre.initCause( e );
            throw mre;
        }
        catch ( ContainerError e )
        {
            final MavenReportException mre =
                new MavenReportException( e.getMessage() );

            mre.initCause( e );
            throw mre;
        }
        catch ( ModelError e )
        {
            final MavenReportException mre =
                new MavenReportException( e.getMessage() );

            mre.initCause( e );
            throw mre;
        }
        catch ( MissingModuleException e )
        {
            this.getSink().paragraph();
            this.getSink().text( ContainerReportBundle.getInstance().
                getNotAModuleMessage( locale ) );

            this.getSink().paragraph_();
            // End the overview section.
            this.getSink().section1_();
            this.getSink().body_();
        }
        catch ( Exception e )
        {
            throw new MavenReportException( e.getMessage(), e );
        }
        finally
        {
            AbstractContainerMojo.disableThreadContextClassLoader();
            Thread.currentThread().setContextClassLoader( mavenLoader );
        }
    }

    //-----------------------------------------------------AbstractMavenReport--
    //--ContainerReport---------------------------------------------------------

    private void generateModuleReport( final Module model,
                                       final Locale locale )
    {
        this.getSink().section2();
        this.getSink().sectionTitle2();
        this.getSink().text(
            ContainerReportBundle.getInstance().
            getModuleTitleMessage( locale ) );

        this.getSink().sectionTitle2_();

        this.getSink().paragraph();
        this.getSink().text(
            ContainerReportBundle.getInstance().
            getModuleDescriptionMessage( locale ) );

        this.getSink().table();
        this.getSink().tableRow();
        this.getSink().tableHeaderCell();
        this.getSink().text(
            ContainerReportBundle.getInstance().
            getModuleNameHeaderMessage( locale ) );

        this.getSink().tableHeaderCell_();

        this.getSink().tableCell();
        this.getSink().monospaced();
        this.getSink().text( model.getName() );
        this.getSink().monospaced_();
        this.getSink().tableCell_();

        this.getSink().tableRow_();

        if ( model.getDocumentation().getLocales().length > 0 )
        {
            this.getSink().tableRow();

            this.getSink().tableHeaderCell();
            this.getSink().text(
                ContainerReportBundle.getInstance().
                getModuleDescriptionHeaderMessage( locale ) );

            this.getSink().tableHeaderCell_();

            this.getSink().tableCell();
            this.getSink().monospaced();
            this.getSink().text( model.getDocumentation().getValue( locale ) );
            this.getSink().monospaced_();
            this.getSink().tableCell_();
            this.getSink().tableRow_();
        }

        this.getSink().tableRow();

        this.getSink().tableHeaderCell();
        this.getSink().text(
            ContainerReportBundle.getInstance().
            getModuleVersionHeaderMessage( locale ) );

        this.getSink().tableHeaderCell_();

        this.getSink().tableCell();
        this.getSink().monospaced();
        this.getSink().text( model.getVersion() );
        this.getSink().monospaced_();
        this.getSink().tableCell_();

        this.getSink().tableRow_();
        this.getSink().table_();

        this.getSink().paragraph_();
        this.getSink().section2_();

        this.getSink().section2();
        this.getSink().sectionTitle2();
        this.getSink().text(
            ContainerReportBundle.getInstance().
            getModuleSpecTitleMessage( locale ) );

        this.getSink().sectionTitle2_();

        this.getSink().paragraph();

        if ( model.getSpecifications().size() > 0 )
        {
            this.getSink().text(
                ContainerReportBundle.getInstance().
                getModuleSpecificationDescriptionMessage( locale ) );

            this.getSink().table();
            this.getSink().tableRow();

            this.getSink().tableHeaderCell();
            this.getSink().text(
                ContainerReportBundle.getInstance().
                getModuleSpecIdentifierHeaderMessage( locale ) );

            this.getSink().tableHeaderCell_();

            this.getSink().tableHeaderCell();
            this.getSink().text(
                ContainerReportBundle.getInstance().
                getModuleSpecVendorHeaderMessage( locale ) );

            this.getSink().tableHeaderCell_();

            this.getSink().tableHeaderCell();
            this.getSink().text(
                ContainerReportBundle.getInstance().
                getModuleSpecVersionHeaderMessage( locale ) );

            this.getSink().tableHeaderCell_();

            this.getSink().tableHeaderCell();
            this.getSink().text(
                ContainerReportBundle.getInstance().
                getModuleSpecMultiplicityHeaderMessage( locale ) );

            this.getSink().tableHeaderCell_();

            this.getSink().tableHeaderCell();
            this.getSink().text(
                ContainerReportBundle.getInstance().
                getModuleSpecScopeHeaderMessage( locale ) );

            this.getSink().tableHeaderCell_();

            this.getSink().tableRow_();

            for ( int s = model.getSpecifications().size() - 1; s >= 0; s-- )
            {
                final Specification spec = model.getSpecifications().
                    getSpecification( s );

                this.getSink().tableRow();

                this.getSink().tableCell();
                this.getSink().link( "specification_" + spec.getIdentifier() );
                this.getSink().monospaced();
                this.getSink().text( spec.getIdentifier() );
                this.getSink().monospaced_();
                this.getSink().tableCell_();

                this.getSink().tableCell();
                this.getSink().monospaced();
                this.getSink().text( spec.getVendor() );
                this.getSink().monospaced_();
                this.getSink().tableCell_();

                this.getSink().tableCell();
                this.getSink().monospaced();
                this.getSink().text( spec.getVersion() );
                this.getSink().monospaced_();
                this.getSink().tableCell_();

                this.getSink().tableCell();
                this.getSink().monospaced();
                this.getSink().text(
                    ContainerReportBundle.getInstance().getMultiplicityMessage(
                    locale, new Integer( spec.getMultiplicity() ) ) );

                this.getSink().monospaced_();
                this.getSink().tableCell_();

                this.getSink().tableCell();
                this.getSink().monospaced();
                this.getSink().text(
                    ContainerReportBundle.getInstance().
                    getScopeMessage( locale, new Integer( spec.getScope() ) ) );

                this.getSink().monospaced_();
                this.getSink().tableCell_();

                this.getSink().tableRow_();
            }

            this.getSink().table_();
        }
        else
        {
            this.getSink().text(
                ContainerReportBundle.getInstance().
                getNoSpecificationsMessage( Locale.getDefault() ) );

        }

        this.getSink().paragraph_();
        this.getSink().section2_();

        this.getSink().section2();
        this.getSink().sectionTitle2();
        this.getSink().text(
            ContainerReportBundle.getInstance().
            getModuleImplTitleMessage( locale ) );

        this.getSink().sectionTitle2_();

        this.getSink().paragraph();

        if ( model.getImplementations().size() > 0 )
        {
            this.getSink().text(
                ContainerReportBundle.getInstance().
                getModuleImplementationDescriptionMessage( locale ) );

            this.getSink().table();
            this.getSink().tableRow();

            this.getSink().tableHeaderCell();
            this.getSink().text(
                ContainerReportBundle.getInstance().
                getModuleImplIdentifierHeaderMessage( locale ) );

            this.getSink().tableHeaderCell_();

            this.getSink().tableHeaderCell();
            this.getSink().text(
                ContainerReportBundle.getInstance().
                getModuleImplNameHeaderMessage( locale ) );

            this.getSink().tableHeaderCell_();

            this.getSink().tableHeaderCell();
            this.getSink().text(
                ContainerReportBundle.getInstance().
                getModuleImplVersionHeaderMessage( locale ) );

            this.getSink().tableHeaderCell_();

            this.getSink().tableHeaderCell();
            this.getSink().text(
                ContainerReportBundle.getInstance().
                getModuleImplFinalHeaderMessage( locale ) );

            this.getSink().tableHeaderCell_();

            this.getSink().tableHeaderCell();
            this.getSink().text(
                ContainerReportBundle.getInstance().
                getModuleImplSpecificationsHeaderMessage( locale ) );

            this.getSink().tableHeaderCell_();

            this.getSink().tableRow_();

            for ( int i = model.getImplementations().size() - 1; i >= 0; i-- )
            {
                final Implementation impl = model.getImplementations().
                    getImplementation( i );

                this.getSink().tableRow();

                this.getSink().tableCell();
                this.getSink().link( "implementation_" + impl.getIdentifier() );
                this.getSink().monospaced();
                this.getSink().text( impl.getIdentifier() );
                this.getSink().monospaced_();
                this.getSink().tableCell_();

                this.getSink().tableCell();
                this.getSink().monospaced();
                this.getSink().text( impl.getName() );
                this.getSink().monospaced_();
                this.getSink().tableCell_();

                this.getSink().tableCell();
                this.getSink().monospaced();
                this.getSink().text( impl.getVersion() );
                this.getSink().monospaced_();
                this.getSink().tableCell_();

                this.getSink().tableCell();
                this.getSink().monospaced();
                this.getSink().text( Boolean.toString( impl.isFinal() ) );
                this.getSink().monospaced_();
                this.getSink().tableCell_();

                this.getSink().tableCell();

                for ( int is = impl.getImplementedSpecifications().size() - 1;
                      is >= 0; is-- )
                {
                    final Specification spec =
                                        impl.getImplementedSpecifications().
                        getSpecification( is );

                    this.getSink().monospaced();
                    this.getSink().text( spec.getIdentifier() );
                    this.getSink().nonBreakingSpace();
                    this.getSink().text( spec.getVersion() );
                    this.getSink().lineBreak();
                    this.getSink().monospaced_();
                }

                this.getSink().tableCell_();

                this.getSink().tableRow_();
            }

            this.getSink().table_();
        }
        else
        {
            this.getSink().text(
                ContainerReportBundle.getInstance().
                getNoImplementationsMessage( locale ) );

        }

        this.getSink().paragraph_();
        this.getSink().section2_();
    }

    private void generateUnresolvedDependenciesReport(
        final Dependencies unresolved, final Locale locale )
    {
        this.getSink().section2();
        this.getSink().sectionTitle2();
        this.getSink().text(
            ContainerReportBundle.getInstance().
            getUnresolvedDependenciesTitleMessage( locale ) );

        this.getSink().sectionTitle2_();

        this.getSink().paragraph();

        if ( unresolved.size() > 0 )
        {
            this.getSink().text(
                ContainerReportBundle.getInstance().
                getUnresolvedDependenciesDescriptionMessage( locale ) );

            this.getSink().table();
            this.getSink().tableRow();
            this.getSink().tableHeaderCell();
            this.getSink().text(
                ContainerReportBundle.getInstance().
                getSpecificationIdentifierTitleMessage( locale ) );

            this.getSink().tableHeaderCell_();

            this.getSink().tableHeaderCell();
            this.getSink().text(
                ContainerReportBundle.getInstance().
                getModuleNameTitleMessage( locale ) );

            this.getSink().tableHeaderCell_();
            this.getSink().tableHeaderCell();
            this.getSink().text(
                ContainerReportBundle.getInstance().
                getMinimumVersionTitleMessage( locale ) );

            this.getSink().tableHeaderCell_();
            this.getSink().tableRow_();

            final Set rendered = new HashSet();

            for ( int i = unresolved.size() - 1; i >= 0; i-- )
            {
                final Dependency dep = unresolved.getDependency( i );

                if ( rendered.contains( dep.getSpecification().
                    getIdentifier() ) )
                {
                    continue;
                }

                rendered.add( dep.getSpecification().getIdentifier() );

                this.getSink().tableRow();

                this.getSink().tableCell();
                this.getSink().monospaced();
                this.getSink().text( dep.getSpecification().getIdentifier() );
                if ( dep.getSpecification().getDocumentation().
                    getLocales().length > 0 )
                {
                    this.getSink().paragraph();
                    this.getSink().text( dep.getSpecification().
                        getDocumentation().
                        getValue( locale ) );

                    this.getSink().paragraph_();
                }

                this.getSink().monospaced_();
                this.getSink().tableCell_();

                this.getSink().tableCell();
                this.getSink().monospaced();
                this.getSink().text( dep.getSpecification().getModuleName() );
                this.getSink().monospaced_();
                this.getSink().tableCell_();

                this.getSink().tableCell();
                this.getSink().monospaced();
                this.getSink().text( dep.getSpecification().getVersion() );
                this.getSink().monospaced_();
                this.getSink().tableCell_();

                this.getSink().tableRow_();
            }

            this.getSink().table_();
        }
        else
        {
            this.getSink().text(
                ContainerReportBundle.getInstance().
                getNoUnresolvedDependenciesMessage( locale ) );

        }

        this.getSink().paragraph_();
        this.getSink().section2_();
    }

    private void generateDetails( final Module model,
                                  final Locale locale )
    {
        for ( int i = model.getSpecifications().size() - 1;
              i >= 0; i-- )
        {
            final Specification spec = model.getSpecifications().
                getSpecification( i );

            this.getSink().section2();
            this.getSink().anchor( "specification_" + spec.getIdentifier() );
            this.getSink().sectionTitle2();
            this.getSink().text(
                ContainerReportBundle.getInstance().
                getSpecificationDetailTitleMessage(
                locale, spec.getIdentifier() ) );

            this.getSink().sectionTitle2_();
            this.getSink().anchor_();

            if ( spec.getDocumentation().getLocales().length > 0 )
            {
                this.getSink().paragraph();
                this.getSink().text( spec.getDocumentation().
                    getValue( locale ) );

                this.getSink().paragraph_();
            }

            this.getSink().paragraph();
            this.generatePropertiesTable(
                spec.getProperties(), false, false,
                ContainerReportBundle.getInstance().
                getSpecificationPropertiesTableDescriptionMessage( locale ),
                ContainerReportBundle.getInstance().
                getNoSpecificationPropertiesMessage( locale ),
                locale );

            this.getSink().paragraph_();

            if ( this.linkJavadoc )
            {
                this.getSink().paragraph();
                this.getSink().link(
                    this.javadocDirectory + '/' +
                    spec.getIdentifier().replaceAll( "\\.", "/" ) + ".html" );

                this.getSink().text( ContainerReportBundle.getInstance().
                    getSeeJavadocLinkMessage( locale ) );

                this.getSink().link_();
                this.getSink().paragraph_();
            }

            this.getSink().section2_();
        }

        for ( int i = model.getImplementations().size() - 1;
              i >= 0; i-- )
        {
            final Implementation impl = model.getImplementations().
                getImplementation( i );

            this.getSink().section2();
            this.getSink().anchor( "implementation_" + impl.getIdentifier() );
            this.getSink().sectionTitle2();
            this.getSink().text(
                ContainerReportBundle.getInstance().
                getImplementationDetailTitleMessage(
                locale, impl.getIdentifier() ) );

            this.getSink().sectionTitle2_();
            this.getSink().anchor_();

            if ( impl.getDocumentation().getLocales().length > 0 )
            {
                this.getSink().paragraph();
                this.getSink().text( impl.getDocumentation().
                    getValue( locale ) );

                this.getSink().paragraph_();
            }

            this.getSink().paragraph();
            this.generatePropertiesTable(
                impl.getProperties(), true, true,
                ContainerReportBundle.getInstance().
                getImplementationPropertiesTableDescriptionMessage( locale ),
                ContainerReportBundle.getInstance().
                getNoImplementationPropertiesMessage( locale ),
                locale );

            this.getSink().paragraph_();

            this.getSink().paragraph();

            if ( impl.getDependencies().size() > 0 )
            {
                this.getSink().text(
                    ContainerReportBundle.getInstance().
                    getDependenciesTableDescriptionMessage( locale ) );

                this.getSink().table();
                this.getSink().tableRow();

                this.getSink().tableHeaderCell();
                this.getSink().text(
                    ContainerReportBundle.getInstance().
                    getDependencyNameHeaderMessage( locale ) );

                this.getSink().tableHeaderCell_();

                this.getSink().tableHeaderCell();
                this.getSink().text(
                    ContainerReportBundle.getInstance().
                    getDependencySpecificationHeaderMessage( locale ) );

                this.getSink().tableHeaderCell_();

                this.getSink().tableHeaderCell();
                this.getSink().text(
                    ContainerReportBundle.getInstance().
                    getDependencyImplementationHeaderMessage( locale ) );

                this.getSink().tableHeaderCell_();

                this.getSink().tableHeaderCell();
                this.getSink().text(
                    ContainerReportBundle.getInstance().
                    getDependencyRequiredVersionHeaderMessage( locale ) );

                this.getSink().tableHeaderCell_();

                this.getSink().tableRow_();

                for ( int d = impl.getDependencies().size() - 1; d >= 0; d-- )
                {
                    final Dependency dr = impl.getDependencies().
                        getDependency( d );

                    this.getSink().tableRow();

                    this.getSink().tableCell();
                    this.getSink().monospaced();
                    this.getSink().text( dr.getName() );
                    this.getSink().monospaced_();
                    this.getSink().tableCell_();

                    this.getSink().tableCell();
                    this.getSink().monospaced();
                    this.getSink().text( dr.getSpecification().getIdentifier() );
                    this.getSink().monospaced_();
                    this.getSink().tableCell_();

                    this.getSink().tableCell();
                    this.getSink().monospaced();

                    if ( dr.getImplementation() != null )
                    {
                        this.getSink().text( dr.getImplementation().getName() );
                    }
                    else
                    {
                        this.getSink().text(
                            ContainerReportBundle.getInstance().
                            getAnyAvailableMessage( locale ) );

                    }

                    this.getSink().monospaced_();
                    this.getSink().tableCell_();

                    this.getSink().tableCell();
                    this.getSink().monospaced();
                    this.getSink().text( dr.getSpecification().getVersion() );
                    this.getSink().monospaced_();
                    this.getSink().tableCell_();

                    this.getSink().tableRow_();
                }

                this.getSink().table_();
            }
            else
            {
                this.getSink().text(
                    ContainerReportBundle.getInstance().
                    getNoDependenciesMessage( locale ) );

            }

            this.getSink().paragraph_();

            this.getSink().paragraph();
            this.generateMessagesTable( impl.getMessages(),
                                        ContainerReportBundle.getInstance().
                getMessagesTableDescriptionMessage(
                locale ),
                                        ContainerReportBundle.getInstance().
                getNoMessagesMessage( locale ),
                                        locale );

            this.getSink().paragraph_();
            if ( this.linkJavadoc )
            {
                this.getSink().paragraph();
                this.getSink().link(
                    this.javadocDirectory + '/' +
                    impl.getIdentifier().replaceAll( "\\.", "/" ) + ".html" );

                this.getSink().text( ContainerReportBundle.getInstance().
                    getSeeJavadocLinkMessage( locale ) );

                this.getSink().link_();
                this.getSink().paragraph_();
            }

            this.getSink().section2_();
        }

    }

    /**
     * Generates a table showing property details.
     *
     * @param properties the properties to generate the table for.
     * @param valueColumn {@code true} to include a column showing a property's
     * value; {@code false} to not include a column for the property's value.
     * @param apiColumn {@code true} to include a column showing a property's
     * api flag; {@code false} to not include a column for the property's api
     * flag.
     * @param tableDescription presentation description of the table.
     * @param emptyDescription presentation description of the table when empty.
     * @param locale the locale of the table.
     */
    private void generatePropertiesTable( final Properties properties,
                                          final boolean valueColumn,
                                          final boolean apiColumn,
                                          final String tableDescription,
                                          final String emptyDescription,
                                          final Locale locale )
    {
        if ( properties.size() > 0 )
        {
            this.getSink().text( tableDescription );

            this.getSink().table();
            this.getSink().tableRow();

            this.getSink().tableHeaderCell();
            this.getSink().text(
                ContainerReportBundle.getInstance().
                getPropertyNameHeaderMessage( locale ) );

            this.getSink().tableHeaderCell_();

            this.getSink().tableHeaderCell();
            this.getSink().text(
                ContainerReportBundle.getInstance().
                getPropertyTypeHeaderMessage( locale ) );

            this.getSink().tableHeaderCell_();

            if ( apiColumn )
            {
                this.getSink().tableHeaderCell();
                this.getSink().text(
                    ContainerReportBundle.getInstance().
                    getPropertyApiFlagHeaderMessage( locale ) );

                this.getSink().tableHeaderCell_();
            }

            if ( valueColumn )
            {
                this.getSink().tableHeaderCell();
                this.getSink().text(
                    ContainerReportBundle.getInstance().
                    getPropertyValueHeaderMessage( locale ) );

                this.getSink().tableHeaderCell_();
            }

            this.getSink().tableRow_();

            for ( int p = properties.size() - 1; p >= 0; p-- )
            {
                final Property pr = properties.getProperty( p );

                this.getSink().tableRow();

                this.getSink().tableCell();
                this.getSink().monospaced();
                this.getSink().text( pr.getName() );
                if ( pr.getDocumentation().getLocales().length > 0 )
                {
                    this.getSink().paragraph();
                    this.getSink().text( pr.getDocumentation().
                        getValue( locale ) );

                    this.getSink().paragraph_();
                }
                this.getSink().monospaced_();
                this.getSink().tableCell_();

                this.getSink().tableCell();
                this.getSink().monospaced();
                this.getSink().text( pr.getType().toString() );
                this.getSink().monospaced_();
                this.getSink().tableCell_();

                if ( apiColumn )
                {
                    this.getSink().tableCell();
                    this.getSink().monospaced();
                    this.getSink().text(
                        ContainerReportBundle.getInstance().
                        getYesNoMessage( locale,
                                         new Integer( pr.isApi() ? 1 : 0 ) ) );

                    this.getSink().monospaced_();
                    this.getSink().tableCell_();
                }

                if ( valueColumn )
                {
                    this.getSink().tableCell();
                    this.getSink().monospaced();
                    this.getSink().text( pr.getValue() != null
                                         ? pr.getValue().toString()
                                         : "" );
                    this.getSink().monospaced_();
                    this.getSink().tableCell_();
                }

                this.getSink().tableRow_();
            }

            this.getSink().table_();
        }
        else
        {
            this.getSink().text( emptyDescription );
        }
    }

    /**
     * Generates a table showing implementation messages.
     *
     * @param messages the messages to generate the table for.
     * @param tableDescription presentation description of the table.
     * @param emptyDescription presentation description of the table when empty.
     * @param locale the locale of the table.
     */
    private void generateMessagesTable( final Messages messages,
                                        final String tableDescription,
                                        final String emptyDescription,
                                        final Locale locale )
    {
        if ( messages.size() > 0 )
        {
            this.getSink().text( tableDescription );

            this.getSink().table();
            this.getSink().tableRow();

            this.getSink().tableHeaderCell();
            this.getSink().text(
                ContainerReportBundle.getInstance().
                getMessageNameHeaderMessage( locale ) );

            this.getSink().tableHeaderCell_();

            this.getSink().tableHeaderCell();
            this.getSink().text(
                ContainerReportBundle.getInstance().
                getMessageTemplatesHeaderMessage( locale ) );

            this.getSink().tableHeaderCell_();
            this.getSink().tableRow_();

            for ( int m = messages.size() - 1; m >= 0; m-- )
            {
                final Message msg = messages.getMessage( m );

                this.getSink().tableRow();

                this.getSink().tableCell();
                this.getSink().text( msg.getName() );
                if ( msg.getDocumentation().getLocales().length > 0 )
                {
                    this.getSink().paragraph();
                    this.getSink().monospaced();
                    this.getSink().text( msg.getDocumentation().
                        getValue( locale ) );

                    this.getSink().monospaced_();
                    this.getSink().paragraph_();
                }
                this.getSink().tableCell_();

                this.getSink().tableCell();
                final Locale[] msgLocales = msg.getTemplate().getLocales();
                for ( int l = msgLocales.length - 1; l >= 0; l-- )
                {
                    final String template = msg.getTemplate().
                        getValue( msgLocales[l] );

                    this.getSink().paragraph();
                    this.getSink().text(
                        msgLocales[l].getDisplayLanguage( msgLocales[l] ) +
                        ':' );

                    this.getSink().lineBreak();
                    this.getSink().verbatim( false );
                    this.getSink().text( template );
                    this.getSink().verbatim_();
                    this.getSink().paragraph_();
                }

                this.getSink().tableCell_();
                this.getSink().tableRow_();
            }

            this.getSink().table_();
        }
        else
        {
            this.getSink().text( emptyDescription );
        }
    }

    /**
     * Gets all unresolved dependencies from a given model.
     *
     * @param model the model to get unresolved dependencies from.
     *
     * @throws NullPointerException if {@code model} is {@code null}.
     */
    private Dependencies getUnresolvedDependencies( final Model model )
    {
        String unresolvedName = "Unresolved";
        final Collection deps = new LinkedList();

        for ( int m = model.getModules().size() - 1; m >= 0; m-- )
        {
            final Module mod = model.getModules().getModule( m );

            for ( int i = mod.getImplementations().size() - 1; i >= 0; i-- )
            {
                final Implementation impl = mod.getImplementations().
                    getImplementation( i );

                for ( int d = impl.getDependencies().size() - 1; d >= 0; d-- )
                {
                    Dependency dep = (Dependency) impl.getDependencies().
                        getDependency( d ).clone();

                    if ( dep.getImplementation() == null &&
                         dep.getSpecification().getMultiplicity() ==
                         Specification.MULTIPLICITY_ONE &&
                         dep.getSpecification().getImplementations().
                        size() != 1 )
                    {
                        dep = (Dependency) dep.clone();
                        dep.setName(
                            unresolvedName + "-" + m + "-" + i + "-" + d );

                        deps.add( dep );
                    }
                }
            }
        }

        final Dependencies dependencies = new Dependencies();
        dependencies.setDependencies(
            (Dependency[]) deps.toArray( new Dependency[ deps.size() ] ) );

        return dependencies;
    }

    //---------------------------------------------------------ContainerReport--
}

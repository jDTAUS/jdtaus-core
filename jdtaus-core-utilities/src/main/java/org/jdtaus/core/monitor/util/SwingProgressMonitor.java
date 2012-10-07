/*
 *  jDTAUS Core Utilities
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
package org.jdtaus.core.monitor.util;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import org.jdtaus.core.container.ContainerFactory;
import org.jdtaus.core.logging.spi.Logger;
import org.jdtaus.core.monitor.Task;
import org.jdtaus.core.monitor.TaskEvent;
import org.jdtaus.core.monitor.TaskListener;
import org.jdtaus.core.text.Message;

/**
 * {@code TaskListener} displaying progress using a Swing dialog.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 *
 * @see #onTaskEvent(TaskEvent)
 */
public final class SwingProgressMonitor implements TaskListener
{
    //--Dependencies------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausDependencies
    // This section is managed by jdtaus-container-mojo.

    /**
     * Gets the configured <code>Logger</code> implementation.
     *
     * @return The configured <code>Logger</code> implementation.
     */
    private Logger getLogger()
    {
        return (Logger) ContainerFactory.getContainer().
            getDependency( this, "Logger" );

    }

    /**
     * Gets the configured <code>Locale</code> implementation.
     *
     * @return The configured <code>Locale</code> implementation.
     */
    private Locale getLocale()
    {
        return (Locale) ContainerFactory.getContainer().
            getDependency( this, "Locale" );

    }

// </editor-fold>//GEN-END:jdtausDependencies

    //------------------------------------------------------------Dependencies--
    //--Properties--------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausProperties
    // This section is managed by jdtaus-container-mojo.

    /**
     * Gets the value of property <code>defaultMinimumTaskDuration</code>.
     *
     * @return Default minimum number of milliseconds a task should be shown in the dialog.
     */
    private java.lang.Integer getDefaultMinimumTaskDuration()
    {
        return (java.lang.Integer) ContainerFactory.getContainer().
            getProperty( this, "defaultMinimumTaskDuration" );

    }

    /**
     * Gets the value of property <code>defaultMillisToPopup</code>.
     *
     * @return Default number of milliseconds visibility of the dialog is delayed.
     */
    private java.lang.Integer getDefaultMillisToPopup()
    {
        return (java.lang.Integer) ContainerFactory.getContainer().
            getProperty( this, "defaultMillisToPopup" );

    }

    /**
     * Gets the value of property <code>defaultMillisToDecideToPopup</code>.
     *
     * @return Default number of milliseconds to pass before all running tasks are checked for theire duration.
     */
    private java.lang.Integer getDefaultMillisToDecideToPopup()
    {
        return (java.lang.Integer) ContainerFactory.getContainer().
            getProperty( this, "defaultMillisToDecideToPopup" );

    }

    /**
     * Gets the value of property <code>defaultColumns</code>.
     *
     * @return Default number of columns the preferred width of the progress dialog is computed with.
     */
    private java.lang.Integer getDefaultColumns()
    {
        return (java.lang.Integer) ContainerFactory.getContainer().
            getProperty( this, "defaultColumns" );

    }

// </editor-fold>//GEN-END:jdtausProperties

    //--------------------------------------------------------------Properties--
    //--TaskListener------------------------------------------------------------

    /**
     * {@inheritDoc}
     * <p>This method controls a dialog displaying a panel for each task showing the progress of that task optionally
     * providing a cancel button if the corresponding task is cancelable. The dialog will show up only if the operation
     * performed by at least one task is believed to run longer than specified by property {@code millisToPopup}.
     * Property {@code millisToDecideToPopup} controls the number of milliseconds to pass before all currently running
     * tasks are checked for their duration. Properties {@code millisToDecideToPopup} and {@code millisToPopup} are
     * used in the same way as specified for Swing's {@link javax.swing.ProgressMonitor}. The default for property
     * {@code millisToDecideToPopup} is 500ms and the default for property {@code millisToPopup} is 2000ms.</p>
     *
     * @param event The event send by a {@code Task}.
     */
    public void onTaskEvent( final TaskEvent event )
    {
        if ( event != null )
        {
            switch ( event.getType() )
            {
                case TaskEvent.STARTED:
                    this.onTaskStarted( event );
                    break;

                case TaskEvent.CHANGED_STATE:
                    this.onTaskChangedState( event );
                    break;

                case TaskEvent.ENDED:
                    this.onTaskEnded( event );
                    break;

                default:
                    getLogger().warn( this.getUnknownTaskEventTypeMessage(
                        this.getLocale(), new Integer( event.getType() ) ) );

            }
        }

        updateProgressDialog();
    }

    //------------------------------------------------------------TaskListener--
    //--SwingProgressMonitor----------------------------------------------------

    /** State of a {@code Task} being monitored. */
    private static final class MonitorState
    {

        /** The {@code Task} being monitored. */
        final Task task;

        /** The panel to use for displaying progress of {@code task}. */
        final ProgressPanel panel;

        /** {@code ActionListener} listening for the cancel button. */
        ActionListener cancelListener;

        /** The time the {@code task}'s panel was set visible or negative, if the panel is not visible. */
        long visibleMillis = Long.MIN_VALUE;

        /**
         * Creates a new {@code MonitorState} instance.
         *
         * @param task The {@code Task} being monitored.
         * @param panel The panel to use for displaying progress of {@code task}.
         * @param cancelListener The listener to listen for the cancel button.
         */
        MonitorState( final Task task, final ProgressPanel panel )
        {
            super();
            this.task = task;
            this.panel = panel;
        }

    }

    /** The current parent component to use when displaying progress. */
    private Component parent;

    /** Maps {@code Task} instances to {@code TaskMonitorState} instances. */
    private final Map tasks = new HashMap( 100 );

    /** The dialog displaying progress of all {@code Task}s. */
    private ProgressDialog dialog;

    /** Number of milliseconds to pass before all running tasks are checked for their duration. */
    private Integer millisToDecideToPopup;

    /** Number of milliseconds visibility of the dialog is delayed. */
    private Integer millisToPopup;

    /** Minimum number of milliseconds a task is shown. */
    private Integer minimumTaskDuration;

    /** Number of columns the preferred width of the progress pane is computed with. */
    private Integer columns;

    /**
     * The time the decision was made to popup a dialog, if an operation of any of the tasks being monitored would take
     * longer than {@code millisToPopup}.
     */
    private long popupDecisionMillis = NO_POPUPDECISION;
    private static final long NO_POPUPDECISION = Long.MIN_VALUE;

    /** Timer used to delay removal of panels. */
    private final Timer timer = new Timer( true );

    /**
     * Creates a new {@code SwingProgressMonitor} instance taking the parent component to use when displaying progress.
     *
     * @param parent The parent component to use when displaying progress.
     *
     * @throws NullPointerException if {@code parent} is {@code null}.
     * @throws HeadlessException if this class is used in an environment not providing a keyboard, display, or mouse.
     *
     * @see #onTaskEvent(TaskEvent)
     */
    public SwingProgressMonitor( final Component parent )
    {
        this( parent, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE );
    }

    /**
     * Creates a new {@code SwingProgressMonitor} instance taking the parent component to use when displaying progress
     * and configuration for how long to wait before showing the progress dialog.
     *
     * @param parent The parent component to use when displaying progress.
     * @param millisToDecideToPopup The number of milliseconds which have to pass before the duration of any currently
     * running task is computed.
     * @param millisToPopup The number of milliseconds at least one task has to run before the progress dialog shows up.
     *
     * @throws NullPointerException if {@code parent} is {@code null}.
     * @throws HeadlessException if this class is used in an environment not providing a keyboard, display, or mouse.
     *
     * @see #onTaskEvent(TaskEvent)
     */
    public SwingProgressMonitor( final Component parent, final int millisToDecideToPopup, final int millisToPopup )
    {
        this( parent, millisToDecideToPopup, millisToPopup, Integer.MIN_VALUE, Integer.MIN_VALUE );
    }

    /**
     * Creates a new {@code SwingProgressMonitor} instance taking the parent component to use when displaying progress
     * and configuration for how long to wait before showing the progress dialog and for how long a task is displayed
     * minimally.
     *
     * @param parent The parent component to use when displaying progress.
     * @param millisToDecideToPopup The number of milliseconds which have to pass before the duration of any currently
     * running task is computed.
     * @param millisToPopup The number of milliseconds at least one task has to run before the progress dialog shows up.
     * @param minimumTaskDurationMillis The number of milliseconds a task is held visible, if a task is ended right
     * after having been shown.
     *
     * @throws NullPointerException if {@code parent} is {@code null}.
     * @throws HeadlessException if this class is used in an environment not providing a keyboard, display, or mouse.
     *
     * @see #onTaskEvent(TaskEvent)
     * @since 1.10
     */
    public SwingProgressMonitor( final Component parent, final int millisToDecideToPopup, final int millisToPopup,
                                 final int minimumTaskDurationMillis )
    {
        this( parent, millisToDecideToPopup, millisToPopup, minimumTaskDurationMillis, Integer.MIN_VALUE );
    }

    /**
     * Creates a new {@code SwingProgressMonitor} instance taking the parent component to use when displaying progress
     * and configuration for how long to wait before showing the progress dialog, for how long a task is displayed
     * minimally and a number of columns the preferred width of the progress dialog is computed with.
     *
     * @param parent The parent component to use when displaying progress.
     * @param millisToDecideToPopup The number of milliseconds which have to pass before the duration of any currently
     * running task is computed.
     * @param millisToPopup The number of milliseconds at least one task has to run before the progress dialog shows up.
     * @param minimumTaskDurationMillis The number of milliseconds a task is held visible, if a task is ended right
     * after having been shown.
     * @param columns The number of columns the preferred width of the progress dialog is computed with.
     *
     * @throws NullPointerException if {@code parent} is {@code null}.
     * @throws HeadlessException if this class is used in an environment not providing a keyboard, display, or mouse.
     *
     * @see #onTaskEvent(TaskEvent)
     * @since 1.10
     */
    public SwingProgressMonitor( final Component parent, final int millisToDecideToPopup, final int millisToPopup,
                                 final int minimumTaskDurationMillis, final int columns )
    {
        if ( parent == null )
        {
            throw new NullPointerException( "parent" );
        }

        if ( GraphicsEnvironment.isHeadless() )
        {
            throw new HeadlessException();
        }

        if ( millisToDecideToPopup > 0 )
        {
            this.millisToDecideToPopup = new Integer( millisToDecideToPopup );
        }
        if ( millisToPopup > 0 )
        {
            this.millisToPopup = new Integer( millisToPopup );
        }
        if ( minimumTaskDurationMillis > 0 )
        {
            this.minimumTaskDuration = new Integer( minimumTaskDurationMillis );
        }
        if ( columns > 0 )
        {
            this.columns = new Integer( columns );
        }

        this.parent = parent;
    }

    /**
     * Gets the parent component for any progress displays.
     *
     * @return The parent component for any progress displays.
     */
    public Component getParent()
    {
        return this.parent;
    }

    /**
     * Sets the parent component to be used by any progress displays.
     *
     * @param parent The parent component to be used by any progress displays.
     *
     * @throws NullPointerException if {@code parent} is {@code null}.
     */
    public void setParent( final Component parent )
    {
        if ( parent == null )
        {
            throw new NullPointerException( "parent" );
        }

        this.parent = parent;
    }

    /**
     * Gets the number of milliseconds visibility of the dialog is delayed.
     *
     * @return The number of milliseconds visibility of the dialog is delayed.
     */
    public int getMillisToPopup()
    {
        if ( this.millisToPopup == null )
        {
            this.millisToPopup = this.getDefaultMillisToPopup();
        }

        return this.millisToPopup.intValue();
    }

    /**
     * Gets the number of milliseconds to pass before all currently running tasks are checked for theire duration.
     *
     * @return The number of milliseconds to pass before all currently running tasks are checked for theire duration.
     */
    public int getMillisToDecideToPopup()
    {
        if ( this.millisToDecideToPopup == null )
        {
            this.millisToDecideToPopup = this.getDefaultMillisToDecideToPopup();
        }

        return this.millisToDecideToPopup.intValue();
    }

    /**
     * Gets the minimum number of milliseconds to pass before a visible task is set invisible.
     *
     * @return The minimum number of milliseconds to pass before a visible task is set invisible.
     *
     * @since 1.10
     */
    public int getMinimumTaskDuration()
    {
        if ( this.minimumTaskDuration == null )
        {
            this.minimumTaskDuration = this.getDefaultMinimumTaskDuration();
        }

        return this.minimumTaskDuration.intValue();
    }

    /**
     * Gets the number of columns the preferred width of a progress pane is computed with.
     *
     * @return The number of columns the preferred width of a progress pane is computed with.
     *
     * @since 1.10
     */
    public int getColumns()
    {
        if ( this.columns == null )
        {
            this.columns = this.getDefaultColumns();
        }

        return this.columns.intValue();
    }

    /**
     * Gets the dialog displaying progress of all {@code Task}s.
     *
     * @return The dialog displaying progress of all {@code Task}s.
     */
    private ProgressDialog getDialog()
    {
        if ( this.dialog == null )
        {
            final Window window = this.getWindowForComponent( this.getParent() );

            if ( window instanceof Frame )
            {
                this.dialog = new ProgressDialog( (Frame) window );
            }
            else if ( window instanceof Dialog )
            {
                this.dialog = new ProgressDialog( (Dialog) window );
            }
        }

        return this.dialog;
    }

    /** Closes and disposes the dialog. */
    private void closeDialog()
    {
        if ( this.dialog != null )
        {
            this.dialog.setVisible( false );
            this.dialog.dispose();
            this.dialog = null;
        }
    }

    /**
     * Returns the specified component's top-level {@code Frame} or {@code Dialog}.
     *
     * @param parentComponent The {@code Component} to check for a {@code Frame} or {@code Dialog}.
     *
     * @return the {@code Frame} or {@code Dialog} that contains {@code parentComponent}, or the default frame if the
     * component is {@code null}, or does not have a valid {@code Frame} or {@code Dialog} parent.
     *
     * @throws HeadlessException if {@code GraphicsEnvironment.isHeadless()} returns {@code true}.
     *
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    private Window getWindowForComponent( final Component parentComponent ) throws HeadlessException
    {
        Window window = JOptionPane.getRootFrame();

        if ( parentComponent != null )
        {
            if ( parentComponent instanceof Frame || parentComponent instanceof Dialog )
            {
                window = (Window) parentComponent;
            }
            else
            {
                this.getWindowForComponent( parentComponent.getParent() );
            }
        }

        return window;
    }

    /**
     * Starts monitoring a task.
     *
     * @param event The {@code TaskEvent} indicating the start of a {@code Task}.
     *
     * @throws NullPointerException if {@code event} is {@code null}.
     * @throws IllegalArgumentException if {@code event.getType()} is not equal to {@link TaskEvent#STARTED}.
     */
    private void onTaskStarted( final TaskEvent event )
    {
        if ( event == null )
        {
            throw new NullPointerException( "event" );
        }
        if ( event.getType() != TaskEvent.STARTED )
        {
            throw new IllegalArgumentException( Integer.toString( event.getType() ) );
        }

        synchronized ( this.tasks )
        {
            final MonitorState state =
                new MonitorState( event.getTask(), new ProgressPanel( getColumns() ) );

            state.cancelListener = new ActionListener()
            {

                public void actionPerformed( final ActionEvent e )
                {
                    if ( !state.task.isCancelled() )
                    {
                        state.task.setCancelled( true );

                        SwingUtilities.invokeLater( new Runnable()
                        {

                            public void run()
                            {
                                state.panel.getCancelButton().setText( getTaskCancelledMessage( getLocale() ) );
                                state.panel.getCancelButton().setEnabled( false );
                            }

                        } );
                    }
                }

            };

            if ( this.tasks.put( state.task, state ) != null )
            {
                throw new IllegalStateException( getTaskAlreadyStartedMessage(
                    getLocale(), state.task.getDescription().getText( getLocale() ),
                    new Date( state.task.getTimestamp() ) ) );

            }

            SwingUtilities.invokeLater( new Runnable()
            {

                public void run()
                {
                    final TitledBorder border =
                        BorderFactory.createTitledBorder( state.task.getDescription().getText( getLocale() ) );

                    state.panel.setBorder( border );

                    if ( state.task.getProgressDescription() != null )
                    {
                        state.panel.getProgressDescriptionLabel().setText(
                            state.task.getProgressDescription().getText( getLocale() ) );

                    }
                    else
                    {
                        state.panel.getProgressDescriptionLabel().setVisible( false );
                    }

                    state.panel.getProgressBar().setIndeterminate( true );

                    if ( !state.task.isIndeterminate() )
                    {
                        state.panel.getProgressBar().setMinimum( state.task.getMinimum() );
                        state.panel.getProgressBar().setMaximum( state.task.getMaximum() );
                        state.panel.getProgressBar().setValue( state.task.getProgress() );
                        state.panel.getTimeLabel().setText( getComputingExpectedDurationMessage( getLocale() ) );
                    }
                    else
                    {
                        state.panel.getTimeLabel().setText( getIndeterminateDurationMessage( getLocale() ) );
                    }

                    state.panel.getCancelButton().setVisible( state.task.isCancelable() );
                    state.panel.getCancelButton().addActionListener( state.cancelListener );
                    state.panel.setVisible( false );
                    getDialog().add( state.panel );
                }

            } );
        }
    }

    /**
     * Finishes monitoring a task.
     *
     * @param event The {@code TaskEvent} indicating the end of a {@code Task}.
     *
     * @throws NullPointerException if {@code event} is {@code null}.
     * @throws IllegalArgumentException if {@code event.getType()} is not equal to {@link TaskEvent#ENDED}.
     */
    private void onTaskEnded( final TaskEvent event )
    {
        if ( event == null )
        {
            throw new NullPointerException( "event" );
        }
        if ( event.getType() != TaskEvent.ENDED )
        {
            throw new IllegalArgumentException( Integer.toString( event.getType() ) );
        }

        final Runnable taskEnded = new Runnable()
        {

            public void run()
            {
                synchronized ( tasks )
                {
                    final MonitorState state = (MonitorState) tasks.remove( event.getTask() );

                    assert state != null : "Expected a started task.";

                    state.visibleMillis = Long.MIN_VALUE;

                    SwingUtilities.invokeLater( new Runnable()
                    {

                        public void run()
                        {
                            state.panel.getCancelButton().removeActionListener( state.cancelListener );
                            state.panel.setVisible( false );
                            getDialog().remove( state.panel );
                            updateProgressDialog(); // Ensure dialog gets closed.
                        }

                    } );
                }
            }

        };

        synchronized ( tasks )
        {
            final MonitorState state = (MonitorState) tasks.get( event.getTask() );

            assert state != null : "Expected a started task.";

            SwingUtilities.invokeLater( new Runnable()
            {

                public void run()
                {
                    state.panel.getTimeLabel().setText( getTaskCompletedMessage( getLocale(), new Date() ) );
                }

            } );

            if ( state.visibleMillis > 0L
                 && System.currentTimeMillis() - state.visibleMillis < getMinimumTaskDuration() )
            {
                try
                {
                    timer.schedule( new TimerTask()
                    {

                        public void run()
                        {
                            taskEnded.run();
                        }

                    }, getMinimumTaskDuration() );
                }
                catch ( final IllegalStateException e )
                {
                    getLogger().error( e );
                    taskEnded.run();
                }
            }
            else
            {
                taskEnded.run();
            }
        }
    }

    /**
     * Monitors a {@code Task}'s state.
     *
     * @param event The {@code TaskEvent} indicating a state change of a {@code Task}.
     *
     * @throws NullPointerException if {@code event} is {@code null}.
     * @throws IllegalArgumentException if {@code event.getType()} is not equal to {@link TaskEvent#CHANGED_STATE}.
     */
    private void onTaskChangedState( final TaskEvent event )
    {
        if ( event == null )
        {
            throw new NullPointerException( "event" );
        }
        if ( event.getType() != TaskEvent.CHANGED_STATE )
        {
            throw new IllegalArgumentException( Integer.toString( event.getType() ) );
        }

        synchronized ( tasks )
        {
            final MonitorState state = (MonitorState) tasks.get( event.getTask() );

            assert state != null : "Expected a started task.";

            final int progress;
            final boolean indeterminate;
            final Message progressDescription = state.task.getProgressDescription();

            if ( !state.task.isIndeterminate() )
            {
                progress = state.task.getProgress();
                indeterminate = false;
            }
            else
            {
                progress = Integer.MIN_VALUE;
                indeterminate = true;
            }

            SwingUtilities.invokeLater( new Runnable()
            {

                public void run()
                {
                    if ( !indeterminate )
                    {
                        state.panel.getProgressBar().setValue( progress );
                    }

                    if ( progressDescription != null )
                    {
                        final String oldText = state.panel.getProgressDescriptionLabel().getText();
                        final String newText = progressDescription.getText( getLocale() );

                        if ( oldText == null || !oldText.equals( newText ) )
                        {
                            state.panel.getProgressDescriptionLabel().setText( newText );

                            if ( !state.panel.getProgressDescriptionLabel().isVisible() )
                            {
                                state.panel.getProgressDescriptionLabel().setVisible( true );
                            }
                        }
                    }
                    else if ( state.panel.getProgressDescriptionLabel().isVisible() )
                    {
                        state.panel.getProgressDescriptionLabel().setVisible( false );
                    }
                }

            } );
        }
    }

    /** Updates the {@code ProgressDialog}. */
    private void updateProgressDialog()
    {
        synchronized ( this.tasks )
        {
            if ( this.tasks.size() > 0 )
            {
                final long now = System.currentTimeMillis();

                if ( this.popupDecisionMillis == NO_POPUPDECISION )
                {
                    this.popupDecisionMillis = now;
                }
                else if ( now - this.popupDecisionMillis > this.getMillisToDecideToPopup() )
                {
                    // If any task's operation runs longer than millisToPopup, show the dialog.
                    for ( final Iterator it = this.tasks.entrySet().iterator(); it.hasNext(); )
                    {
                        final Map.Entry entry = (Map.Entry) it.next();
                        final MonitorState state = (MonitorState) entry.getValue();

                        if ( !state.task.isIndeterminate() )
                        {
                            final long progressed = state.task.getProgress() - state.task.getMinimum();
                            final long predicted = ( now - state.task.getTimestamp() )
                                                   * ( state.task.getMaximum() - state.task.getMinimum() )
                                                   / ( progressed == 0L ? 1L : progressed );

                            final Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis( state.task.getTimestamp() + predicted );

                            if ( progressed > 0L )
                            {
                                SwingUtilities.invokeLater( new Runnable()
                                {

                                    public void run()
                                    {
                                        state.panel.getTimeLabel().setText( getExpectedEndMessage(
                                            getLocale(), cal.getTime() ) );

                                        if ( state.panel.getProgressBar().isIndeterminate() )
                                        {
                                            state.panel.getProgressBar().setIndeterminate( false );
                                        }
                                    }

                                } );
                            }
                        }

                        if ( state.visibleMillis < 0L && now - state.task.getTimestamp() > this.getMillisToPopup() )
                        {
                            state.visibleMillis = System.currentTimeMillis();

                            SwingUtilities.invokeLater( new Runnable()
                            {

                                public void run()
                                {
                                    state.panel.setVisible( true );
                                    getDialog().pack();

                                    if ( !getDialog().isVisible() )
                                    {
                                        getDialog().setLocationRelativeTo( getParent() );
                                        getDialog().setVisible( true );
                                    }
                                }

                            } );
                        }
                    }
                }
            }
            else
            {
                SwingUtilities.invokeLater( new Runnable()
                {

                    public void run()
                    {
                        closeDialog();
                    }

                } );

                this.popupDecisionMillis = NO_POPUPDECISION;
            }
        }
    }

    /** Finalizes the instance by canceling timers. */
    protected void finalize() throws Throwable
    {
        this.timer.cancel();
        super.finalize();
    }

    //----------------------------------------------------SwingProgressMonitor--
    //--Messages----------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausMessages
    // This section is managed by jdtaus-container-mojo.

    /**
     * Gets the text of message <code>unknownTaskEventType</code>.
     * <blockquote><pre>Verarbeitungsereignis unbekannten Typs {0,number} ignoriert.</pre></blockquote>
     * <blockquote><pre>Ignored task event of unknown type {0,number}.</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     * @param unknownEventType The unknown type of the event.
     *
     * @return Message stating that an unknown task event got ignored.
     */
    private String getUnknownTaskEventTypeMessage( final Locale locale,
            final java.lang.Number unknownEventType )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "unknownTaskEventType", locale,
                new Object[]
                {
                    unknownEventType
                });

    }

    /**
     * Gets the text of message <code>taskAlreadyStarted</code>.
     * <blockquote><pre>Verarbeitung "{0}" wurde um {1,time,long} bereits gestartet.</pre></blockquote>
     * <blockquote><pre>Task "{0}" has already been started at {1,time,long}.</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     * @param taskDescription The description of the task.
     * @param startDate The time the task started.
     *
     * @return Message stating that a task already has been started.
     */
    private String getTaskAlreadyStartedMessage( final Locale locale,
            final java.lang.String taskDescription,
            final java.util.Date startDate )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "taskAlreadyStarted", locale,
                new Object[]
                {
                    taskDescription,
                    startDate
                });

    }

    /**
     * Gets the text of message <code>expectedEnd</code>.
     * <blockquote><pre>Voraussichtliches Ende am {0,date,full} um {0,time,medium} Uhr.</pre></blockquote>
     * <blockquote><pre>Expected end approximately at {0,date,full} {0,time,medium}.</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     * @param expectedEnd The expected end of the task.
     *
     * @return Message stating the expected end of a task.
     */
    private String getExpectedEndMessage( final Locale locale,
            final java.util.Date expectedEnd )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "expectedEnd", locale,
                new Object[]
                {
                    expectedEnd
                });

    }

    /**
     * Gets the text of message <code>computingExpectedDuration</code>.
     * <blockquote><pre>Berechnet voraussichtliche Dauer...</pre></blockquote>
     * <blockquote><pre>Computing expected duration...</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     *
     * @return Message stating the expected duration of a task is being computed.
     */
    private String getComputingExpectedDurationMessage( final Locale locale )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "computingExpectedDuration", locale, null );

    }

    /**
     * Gets the text of message <code>indeterminateDuration</code>.
     * <blockquote><pre>Keine Laufzeitinformationen verfügbar.</pre></blockquote>
     * <blockquote><pre>No duration information available.</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     *
     * @return Message stating the a task is indeterminate.
     */
    private String getIndeterminateDurationMessage( final Locale locale )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "indeterminateDuration", locale, null );

    }

    /**
     * Gets the text of message <code>taskCompleted</code>.
     * <blockquote><pre>Vorgang am {0,date,full} um {0,time,medium} Uhr abgeschlossen.</pre></blockquote>
     * <blockquote><pre>Task completed at {0,date,full} {0,time,medium}.</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     * @param completedDate The date the task completed.
     *
     * @return Message stating that a task completed.
     */
    private String getTaskCompletedMessage( final Locale locale,
            final java.util.Date completedDate )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "taskCompleted", locale,
                new Object[]
                {
                    completedDate
                });

    }

    /**
     * Gets the text of message <code>taskCancelled</code>.
     * <blockquote><pre>Abgebrochen</pre></blockquote>
     * <blockquote><pre>Cancelled</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     *
     * @return Message stating that a task is cancelled.
     */
    private String getTaskCancelledMessage( final Locale locale )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "taskCancelled", locale, null );

    }

// </editor-fold>//GEN-END:jdtausMessages

    //----------------------------------------------------------------Messages--
}

/**
 * {@code JPanel} displaying the progress for a {@code Task}.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
class ProgressPanel extends JPanel
{
    //--ProgressPanel-----------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.1.x classes. */
    private static final long serialVersionUID = 7471966908616369847L;

    /** {@code JLabel} for displaying a {@code Task}s progress description. */
    private final JLabel progressDescriptionLabel;

    /** {@code JLabel} for displaying the time to run. */
    private final JLabel timeLabel;

    /** {@code JProgressBar} for displaying a {@code Task}s progress. */
    private final JProgressBar progressBar;

    /** {@code JButton} for canceling a {@code Task}. */
    private final JButton cancelButton;

    /** Creates a new {@code ProgressPanel} instance. */
    ProgressPanel( final int columns )
    {
        final char[] chars = new char[ columns ];
        Arrays.fill( chars, 'W' );

        this.timeLabel = new JLabel();
        this.timeLabel.setFont( this.timeLabel.getFont().deriveFont( this.timeLabel.getFont().getSize2D() - 2.0F ) );
        this.timeLabel.setText( String.valueOf( chars ) );
        // Use the peer's preferred size for columns times 'W' char.
        this.timeLabel.setPreferredSize( this.timeLabel.getPreferredSize() );
        this.timeLabel.setText( null );
        this.progressDescriptionLabel = new JLabel();

        this.progressBar = new JProgressBar();
        this.cancelButton = new JButton();
        this.cancelButton.setText( UIManager.getString( "OptionPane.cancelButtonText" ) );
        this.setLayout( new GridBagLayout() );

        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weightx = 1.0D;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets( 10, 25, 10, 25 );
        this.add( this.getProgressBar(), c );

        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weightx = 1.0D;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets( 0, 25, 10, 25 );
        this.add( this.getProgressDescriptionLabel(), c );

        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.SOUTHEAST;
        c.weightx = 1.0D;
        c.insets = new Insets( 10, 25, 10, 25 );
        this.add( this.getCancelButton(), c );

        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1.0D;
        c.weighty = 1.0D;
        c.anchor = GridBagConstraints.SOUTHWEST;
        c.insets = new Insets( 10, 25, 0, 25 );
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add( this.getTimeLabel(), c );
    }

    JLabel getProgressDescriptionLabel()
    {
        return this.progressDescriptionLabel;
    }

    JLabel getTimeLabel()
    {
        return this.timeLabel;
    }

    JProgressBar getProgressBar()
    {
        return this.progressBar;
    }

    JButton getCancelButton()
    {
        return this.cancelButton;
    }

    //-----------------------------------------------------------ProgressPanel--
}

/**
 * {@code JDialog} displaying the progress of all {@code Task}s.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
class ProgressDialog extends JDialog
{
    //--ProgressDialog----------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.1.x classes. */
    private static final long serialVersionUID = -8959350486356163001L;

    /**
     * Creates a new {@code ProgressDialog} taking the owning frame.
     *
     * @param owner the frame owning the dialog.
     */
    ProgressDialog( final Frame owner )
    {
        super( owner, true );
        this.initializeDialog();
    }

    /**
     * Creates a new {@code ProgressDialog} taking the owning dialog.
     *
     * @param owner the dialog owning the dialog.
     */
    ProgressDialog( final Dialog owner )
    {
        super( owner, true );
        this.initializeDialog();
    }

    /** Sets the layout and title of the dialog. */
    private void initializeDialog()
    {
        this.getContentPane().setLayout( new GridBagLayout() );
        this.setDefaultCloseOperation( DO_NOTHING_ON_CLOSE );
        this.setTitle( UIManager.getString( "ProgressMonitor.progressText" ) );
    }

    void add( final ProgressPanel panel )
    {
        if ( panel == null )
        {
            throw new NullPointerException( "panel" );
        }

        final GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weightx = 1.0D;
        c.weighty = 1.0D;
        c.fill = GridBagConstraints.BOTH;

        synchronized ( this.getTreeLock() )
        {
            this.getContentPane().add( panel, c );
        }
    }

    void remove( final ProgressPanel panel )
    {
        if ( panel == null )
        {
            throw new NullPointerException( "panel" );
        }

        synchronized ( this.getTreeLock() )
        {
            this.getContentPane().remove( panel );
            this.validate();
        }
    }

    //----------------------------------------------------------ProgressDialog--
}

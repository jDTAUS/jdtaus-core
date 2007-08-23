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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
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
import org.jdtaus.core.container.ContextFactory;
import org.jdtaus.core.container.ContextInitializer;
import org.jdtaus.core.container.Implementation;
import org.jdtaus.core.container.ModelFactory;
import org.jdtaus.core.container.Properties;
import org.jdtaus.core.container.Property;
import org.jdtaus.core.container.PropertyException;
import org.jdtaus.core.logging.spi.Logger;
import org.jdtaus.core.monitor.Task;
import org.jdtaus.core.monitor.TaskEvent;
import org.jdtaus.core.monitor.TaskListener;

/**
 * {@code TaskListener} displaying progress using a Swing dialog.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 *
 * @see #onTaskEvent(TaskEvent)
 */
public final class SwingProgressMonitor implements TaskListener
{
    //--Implementation----------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausImplementation
    // This section is managed by jdtaus-container-mojo.

    /** Meta-data describing the implementation. */
    private static final Implementation META =
        ModelFactory.getModel().getModules().
        getImplementation(SwingProgressMonitor.class.getName());
// </editor-fold>//GEN-END:jdtausImplementation

    //----------------------------------------------------------Implementation--
    //--Constructors------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausConstructors
    // This section is managed by jdtaus-container-mojo.

    /**
     * Initializes the properties of the instance.
     *
     * @param meta the property values to initialize the instance with.
     *
     * @throws NullPointerException if {@code meta} is {@code null}.
     */
    private void initializeProperties(final Properties meta)
    {
        Property p;

        if(meta == null)
        {
            throw new NullPointerException("meta");
        }

        p = meta.getProperty("millisToPopup");
        this._millisToPopup = ((java.lang.Integer) p.getValue()).intValue();


        p = meta.getProperty("millisToDecideToPopup");
        this._millisToDecideToPopup = ((java.lang.Integer) p.getValue()).intValue();

    }
// </editor-fold>//GEN-END:jdtausConstructors

    //------------------------------------------------------------Constructors--
    //--Dependencies------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausDependencies
    // This section is managed by jdtaus-container-mojo.

    /** Configured <code>Logger</code> implementation. */
    private transient Logger _dependency0;

    /**
     * Gets the configured <code>Logger</code> implementation.
     *
     * @return the configured <code>Logger</code> implementation.
     */
    private Logger getLogger()
    {
        Logger ret = null;
        if(this._dependency0 != null)
        {
            ret = this._dependency0;
        }
        else
        {
            ret = (Logger) ContainerFactory.getContainer().
                getDependency(SwingProgressMonitor.class,
                "Logger");

            if(ModelFactory.getModel().getModules().
                getImplementation(SwingProgressMonitor.class.getName()).
                getDependencies().getDependency("Logger").
                isBound())
            {
                this._dependency0 = ret;
            }
        }

        if(ret instanceof ContextInitializer && !((ContextInitializer) ret).
            isInitialized(ContextFactory.getContext()))
        {
            ((ContextInitializer) ret).initialize(ContextFactory.getContext());
        }

        return ret;
    }
// </editor-fold>//GEN-END:jdtausDependencies

    //------------------------------------------------------------Dependencies--
    //--Properties--------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausProperties
    // This section is managed by jdtaus-container-mojo.

    /**
     * Property {@code millisToPopup}.
     * @serial
     */
    private int _millisToPopup;

    /**
     * Gets the value of property <code>millisToPopup</code>.
     *
     * @return the value of property <code>millisToPopup</code>.
     */
    public int getMillisToPopup()
    {
        return this._millisToPopup;
    }

    /**
     * Property {@code millisToDecideToPopup}.
     * @serial
     */
    private int _millisToDecideToPopup;

    /**
     * Gets the value of property <code>millisToDecideToPopup</code>.
     *
     * @return the value of property <code>millisToDecideToPopup</code>.
     */
    public int getMillisToDecideToPopup()
    {
        return this._millisToDecideToPopup;
    }

// </editor-fold>//GEN-END:jdtausProperties

    //--------------------------------------------------------------Properties--
    //--TaskListener------------------------------------------------------------

    /**
     * {@inheritDoc}
     * <p>This method controls a dialog displaying a panel for each task showing
     * the progress of that task optionally providing a cancel button if the
     * corresponding task is cancelable. The dialog will show up only if
     * the operation performed by at least one task is believed to run longer
     * than specified by property {@code millisToPopup}. Property
     * {@code millisToDecideToPopup} controls the number of milliseconds to pass
     * before all currently running tasks are checked for theire duration.
     * Properties {@code millisToDecideToPopup} and {@code millisToPopup} are
     * used in the same way as specified for Swing's
     * {@link javax.swing.ProgressMonitor}. The default for property
     * {@code millisToDecideToPopup} is 500ms and the default for property
     * {@code millisToPopup} is 2000ms.</p>
     *
     * @param event the event send by a {@code Task}.
     */
    public synchronized void onTaskEvent(final TaskEvent event)
    {
        if(event != null)
        {
            final MonitorState state;

            switch(event.getType())
            {
                case TaskEvent.STARTED:
                    final ActionListener cancelListener =
                        new ActionListener()
                    {
                        public void actionPerformed(
                            final ActionEvent e)
                        {
                            if(!event.getTask().isCancelled())
                            {
                                event.getTask().setCancelled(true);
                            }
                        }
                    };

                    state = new MonitorState(event.getTask(),
                        new ProgressPanel(), cancelListener,
                        System.currentTimeMillis());

                    final TitledBorder border =
                        BorderFactory.createTitledBorder(
                        state.task.getDescription().getText(
                        Locale.getDefault()));

                    state.panel.setBorder(border);

                    if(state.task.getProgressDescription() != null)
                    {
                        state.panel.getProgressDescriptionLabel().
                            setText(
                            state.task.getProgressDescription().
                            getText(Locale.getDefault()));

                    }
                    else
                    {
                        state.panel.getProgressDescriptionLabel().
                            setVisible(false);

                    }

                    state.panel.getProgressBar().setIndeterminate(
                        state.task.isIndeterminate());

                    if(!state.task.isIndeterminate())
                    {
                        state.panel.getProgressBar().setMinimum(
                            state.task.getMinimum());

                        state.panel.getProgressBar().setMaximum(
                            state.task.getMaximum());

                        state.panel.getProgressBar().setValue(
                            state.task.getProgress());

                    }

                    state.panel.getCancelButton().setVisible(
                        state.task.isCancelable());

                    state.panel.getCancelButton().addActionListener(
                        state.cancelListener);

                    SwingUtilities.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            state.panel.setVisible(false);
                            getDialog().add(state.panel);
                        }
                    });

                    if(tasks.put(state.task, state) != null)
                    {
                        throw new IllegalStateException(
                            SwingProgressMonitorBundle.
                            getTaskAlreadyStartedMessage(Locale.getDefault()).
                            format(new Object[] {
                            state.task.getDescription().getText(
                                Locale.getDefault()),
                            new Date(state.task.getTimestamp()) }));

                    }
                    break;

                case TaskEvent.CHANGED_STATE:
                    state = (MonitorState) tasks.get(event.getTask());

                    assert state != null :
                        "Expected a started task.";

                    SwingUtilities.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            if(!state.task.isIndeterminate())
                            {
                                state.panel.getProgressBar().setValue(
                                    state.task.getProgress());

                            }

                            if(state.task.getProgressDescription() != null)
                            {
                                final String oldText =
                                    state.panel.getProgressDescriptionLabel().
                                    getText();

                                final String newText =
                                    state.task.getProgressDescription().
                                    getText(Locale.getDefault());

                                if(oldText == null ||
                                    !oldText.equals(newText))
                                {
                                    state.panel.getProgressDescriptionLabel().
                                        setText(newText);
                                }

                                if(!state.panel.getProgressDescriptionLabel().
                                    isVisible())
                                {
                                    state.panel.getProgressDescriptionLabel().
                                        setVisible(true);

                                }
                            }
                            else
                            {
                                if(state.panel.getProgressDescriptionLabel().
                                    isVisible())
                                {
                                    state.panel.getProgressDescriptionLabel().
                                        setVisible(false);

                                }
                            }
                        }
                    });
                    break;

                case TaskEvent.ENDED:
                    state = (MonitorState) tasks.remove(event.getTask());

                    assert state != null :
                        "Expected a started task.";

                    SwingUtilities.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            state.panel.getCancelButton().
                                removeActionListener(state.cancelListener);

                            getDialog().remove(state.panel);
                        }
                    });
                    break;

                default:
                    getLogger().warn(SwingProgressMonitorBundle.
                        getUnknownEventTypeMessage(Locale.getDefault()).
                        format(new Object[] { new Integer(event.getType()) }));

            }
        }

        if(this.tasks.size() > 0)
        {
            final long now = System.currentTimeMillis();
            if(this.popupDecisionMillis == NO_POPUPDECISION)
            {
                this.popupDecisionMillis = now;
            }
            else if(now - this.popupDecisionMillis >
                this.getMillisToDecideToPopup())
            {
                // If any task's operation runs longer than millisToPopup
                // show the dialog.
                for(Iterator it = this.tasks.entrySet().iterator();
                it.hasNext();)
                {
                    final Map.Entry entry = (Map.Entry) it.next();
                    final MonitorState state =
                        (MonitorState) entry.getValue();

                    if(!state.task.isIndeterminate())
                    {
                        if(state.task.getProgress() > state.task.getMinimum())
                        {
                            final long predicted = (now - state.startMillis) *
                                (state.task.getMaximum() -
                                state.task.getMinimum()) /
                                (state.task.getProgress() -
                                state.task.getMinimum());

                            if(predicted > getMillisToPopup() &&
                                !state.panel.isVisible())
                            {
                                // If the task is believed to last longer than
                                // millisToPopup add the panel to the dialog.
                                SwingUtilities.invokeLater(new Runnable()
                                {
                                    public void run()
                                    {
                                        state.panel.setVisible(true);
                                        getDialog().pack();
                                        if(!getDialog().isVisible())
                                        {
                                            getDialog().setLocationRelativeTo(
                                                getParent());

                                            getDialog().setVisible(true);
                                        }
                                    }
                                });
                            }
                        }
                    }
                    else if(!state.panel.isVisible() &&
                        now - state.task.getTimestamp() >
                        this.getMillisToPopup())
                    { // Only show long running indeterminate tasks.
                        SwingUtilities.invokeLater(new Runnable()
                        {
                            public void run()
                            {
                                state.panel.setVisible(true);
                                getDialog().pack();
                                if(!getDialog().isVisible())
                                {
                                    getDialog().setLocationRelativeTo(
                                        getParent());

                                    getDialog().setVisible(true);
                                }
                            }
                        });
                    }
                }
            }
        }
        else
        {
            SwingUtilities.invokeLater(new Runnable()
            {
                public void run()
                {
                    closeDialog();
                }
            });
            this.popupDecisionMillis = NO_POPUPDECISION;
        }
    }

    //------------------------------------------------------------TaskListener--
    //--SwingProgressMonitor----------------------------------------------------

    /** State of a {@code Task} being monitored. */
    private static final class MonitorState
    {
        /** The {@code Task} being monitored. */
        final Task task;

        /** The panel to use for disaplying progress of {@code task}. */
        final ProgressPanel panel;

        /** The time {@code task} got started. */
        final long startMillis;

        /** {@code ActionListener} listening for the cancel button. */
        final ActionListener cancelListener;

        /**
         * Creates a new {@code MonitorState} instance.
         *
         * @param task the {@code Task} being monitored.
         * @param panel the panel to use for displaying progress of
         * {@code task}.
         * @param startMillis the time {@code task} got started.
         * @param cancelListener the listener to listen for the cancel button.
         */
        MonitorState(final Task task, final ProgressPanel panel,
            final ActionListener cancelListener, final long startMillis)
        {
            this.task = task;
            this.panel = panel;
            this.cancelListener = cancelListener;
            this.startMillis = startMillis;
        }
    }

    /** The current parent component to use when displaying progress. */
    private Component parent;

    /** Maps {@code Task} instances to {@code TaskMonitorState} instances. */
    private final Map tasks = new HashMap(100);

    /** The dialog displaying progress of all {@code Task}s. */
    private ProgressDialog dialog;

    /**
     * The time the decision was made to popup a dialog, if an operation of any
     * of the tasks being monitored would take longer than
     * {@code millisToPopup}.
     */
    private long popupDecisionMillis = NO_POPUPDECISION;
    private static final long NO_POPUPDECISION = Long.MIN_VALUE;

    /**
     * Creates a new {@code SwingProgressMonitor} instance taking the parent
     * component to use when displaying progress.
     *
     * @param parent the parent component to use when displaying progress.
     *
     * @throws NullPointerException if {@code parent} is {@code null}.
     * @throws HeadlessException if this class is used in an environment
     * not providing a keyboard, display, or mouse.
     *
     * @see #onTaskEvent(TaskEvent)
     */
    public SwingProgressMonitor(final Component parent)
    {
        if(parent == null)
        {
            throw new NullPointerException("parent");
        }

        if(GraphicsEnvironment.isHeadless())
        {
            throw new HeadlessException();
        }

        this.initializeProperties(META.getProperties());
        this.assertValidProperties();

        this.parent = parent;
    }

    /**
     * Creates a new {@code SwingProgressMonitor} instance taking the parent
     * component to use when displaying progress and configuration for how long
     * to wait before showing the progress dialog.
     *
     * @param parent the parent component to use when displaying progress.
     * @param millisToDecideToPopup the number of milliseconds which have to
     * pass before the duration of any currently running task is computed.
     * @param millisToPopup the number of milliseconds at least one task has
     * to run before the progress dialog shows up.
     *
     * @throws NullPointerException if {@code parent} is {@code null}.
     * @throws PropertyException if either {@code millisToDecideToPopup} or
     * {@code millisToPopup} is negative or zero.
     * @throws HeadlessException if this class is used in an environment
     * not providing a keyboard, display, or mouse.
     *
     * @see #onTaskEvent(TaskEvent)
     */
    public SwingProgressMonitor(final Component parent,
        final int millisToDecideToPopup, final int millisToPopup)
    {
        if(parent == null)
        {
            throw new NullPointerException("parent");
        }

        if(GraphicsEnvironment.isHeadless())
        {
            throw new HeadlessException();
        }

        this.initializeProperties(META.getProperties());
        this._millisToDecideToPopup = millisToDecideToPopup;
        this._millisToPopup = millisToPopup;
        this.assertValidProperties();

        this.parent = parent;
    }

    /**
     * Gets the parent component for any progress displays.
     *
     * @return the parent component for any progress displays.
     */
    public Component getParent()
    {
        return this.parent;
    }

    /**
     * Sets the parent component to be used by any progress displays.
     *
     * @param parent the parent component to be used by any progress displays.
     *
     * @throws NullPointerException if {@code parent} is {@code null}.
     */
    public void setParent(final Component parent)
    {
        if(parent == null)
        {
            throw new NullPointerException("parent");
        }

        this.parent = parent;
    }

    /**
     * Checks configured properties.
     *
     * @throws PropertyException for illegal property values.
     */
    private void assertValidProperties()
    {
        if(this.getMillisToDecideToPopup() < 0)
        {
            throw new PropertyException("millisToDecideToPopup",
                Integer.toString(this.getMillisToDecideToPopup()));

        }
        if(this.getMillisToPopup() < 0)
        {
            throw new PropertyException("millisToPopup",
                Integer.toString(this.getMillisToPopup()));

        }
    }

    /**
     * Gets the dialog displaying progress of all {@code Task}s.
     *
     * @return the dialog displaying progress of all {@code Task}s.
     */
    private synchronized ProgressDialog getDialog()
    {
        if(this.dialog == null)
        {
            final Window window = this.getWindowForComponent(this.getParent());
            if(window instanceof Frame)
            {
                this.dialog = new ProgressDialog((Frame) window);
            }
            else if(window instanceof Dialog)
            {
                this.dialog = new ProgressDialog((Dialog) window);
            }
        }

        return this.dialog;
    }

    /** Closes and disposes the dialog. */
    private synchronized void closeDialog()
    {
        if(this.dialog != null)
        {
            this.dialog.setVisible(false);
            this.dialog.dispose();
            this.dialog = null;
        }
    }

    /**
     * Returns the specified component's toplevel {@code Frame} or
     * {@code Dialog}.
     *
     * @param parentComponent the {@code Component} to check for a {@code Frame}
     * or {@code Dialog}.
     *
     * @return the {@code Frame} or {@code Dialog} that contains
     * {@code parentComponent}, or the default frame if the component is
     * {@code null}, or does not have a valid {@code Frame} or {@code Dialog}
     * parent.
     *
     * @throws HeadlessException if {@code GraphicsEnvironment.isHeadless()}
     * returns {@code true}.
     *
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    private Window getWindowForComponent(final Component parentComponent)
    throws HeadlessException
    {
        Window window = JOptionPane.getRootFrame();
        if (parentComponent != null)
        {
            if(parentComponent instanceof Frame ||
                parentComponent instanceof Dialog)
            {
                window = (Window) parentComponent;
            }
            else
            {
                this.getWindowForComponent(parentComponent.getParent());
            }
        }

        return window;
    }

    //----------------------------------------------------SwingProgressMonitor--
}

/**
 * {@code JPanel} displaying the progress for a {@code Task}.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
class ProgressPanel extends JPanel
{
    //--ProgressPanel-----------------------------------------------------------

    /** {@code JLabel} for displaying a {@code Task}s progress description. */
    private final JLabel progressDescriptionLabel;

    /** {@code JProgressBar} for displaying a {@code Task}s progress. */
    private final JProgressBar progressBar;

    /** {@code JButton} for cancelling a {@code Task}. */
    private final JButton cancelButton;

    /** Creates a new {@code ProgressPanel} instance. */
    ProgressPanel()
    {
        this.progressDescriptionLabel = new JLabel();
        this.progressBar = new JProgressBar();
        this.cancelButton = new JButton();
        this.cancelButton.setText(
            UIManager.getString("OptionPane.cancelButtonText"));

        this.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weightx = 1.0D;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 25, 10, 25);
        this.add(this.getProgressBar(), c);

        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weightx = 1.0D;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(2, 25, 2, 25);
        this.add(this.getProgressDescriptionLabel(), c);

        c = new GridBagConstraints();
        c.gridheight = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.SOUTHEAST;
        c.weighty = 1.0D;
        c.insets = new Insets(2, 5, 2, 5);
        this.add(this.getCancelButton(), c);
    }

    JLabel getProgressDescriptionLabel()
    {
        return this.progressDescriptionLabel;
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
 * @version $Id$
 */
class ProgressDialog extends JDialog
{
    //--ProgressDialog----------------------------------------------------------

    /**
     * Creates a new {@code ProgressDialog} taking the owning frame.
     *
     * @param owner the frame owning the dialog.
     */
    ProgressDialog(final Frame owner)
    {
        super(owner, true);
        this.initializeDialog();
    }

    /**
     * Creates a new {@code ProgressDialog} taking the owning dialog.
     *
     * @param owner the dialog owning the dialog.
     */
    ProgressDialog(final Dialog owner)
    {
        super(owner, true);
        this.initializeDialog();
    }

    /** Sets the layout and title of the dialog. */
    private void initializeDialog()
    {
        this.getContentPane().setLayout(new GridBagLayout());
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setTitle(UIManager.getString("ProgressMonitor.progressText"));
    }

    void add(final ProgressPanel panel)
    {
        if(panel == null)
        {
            throw new NullPointerException("panel");
        }

        final GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weightx = 1.0D;
        c.weighty = 1.0D;
        c.fill = GridBagConstraints.BOTH;

        this.getContentPane().add(panel, c);
    }

    void remove(final ProgressPanel panel)
    {
        if(panel == null)
        {
            throw new NullPointerException("panel");
        }

        this.getContentPane().remove(panel);
        this.validate();
    }

    //----------------------------------------------------------ProgressDialog--
}

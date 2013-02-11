package view.tools.flowmanager;

import java.io.IOException;

import model.overview.Switch;
import model.tools.flowmanager.Action;
import model.tools.flowmanager.Flow;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.wb.swt.layout.grouplayout.GroupLayout;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Button;

import controller.floodlightprovider.FloodlightProvider;
import controller.tools.flowmanager.push.ActionManagerPusher;
import controller.tools.flowmanager.push.FlowManagerPusher;
import controller.tools.flowmanager.push.MatchManagerPusher;
import controller.tools.flowmanager.table.ActionToTable;
import controller.tools.flowmanager.table.MatchToTable;
import controller.util.JSONException;

import view.About;
import view.util.DisplayMessage;

import org.eclipse.swt.widgets.Combo;

public class StaticFlowManager {

	private static Shell shell;
	public Tree tree_switches, tree_flows, action_tree;
	protected TableEditor flowEditor, actionEditor, matchEditor;
	final int EDITABLECOLUMN = 1;
	private static Switch currSwitch;
	private static Flow flow;
	protected static boolean unsavedProgress, newFlow;
	private Table action_table;
	private Table match_table;
	private Text txtFlowName;
	private Text txtFlowPriority;
	private static Action currAction;
	protected String[][] matchTableFormat;
	protected Combo combo;
	protected int currSwitchIndex;
	
	/**
     * @wbp.parser.constructor
     */

	public StaticFlowManager() {
		open();
	}

	public StaticFlowManager(int index) {
		open(index);
	}

	public static Shell getShell(){
	    return shell;
	}
	
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	// This is a second open method for pre-selecting a switch
	public void open(int index) {
		Display display = Display.getDefault();
		createContents();
		populateFlowTree(index);
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	public void disposeEditors(String editor){
		// Dispose the editor do it doesn't leave a ghost table item
		if(actionEditor.getEditor() != null && ((editor.equals("action") || editor.equals("all"))))
		    actionEditor.getEditor().dispose();
		if(matchEditor.getEditor() != null && ((editor.equals("match") || editor.equals("all"))))
		    matchEditor.getEditor().dispose();
	}

	/**
	 * Populate the switch tree with DPIDs of the switches known by the controller
	 */
	private void populateSwitchTree() {

	    /* Clean the slate */
        disposeEditors("all");
        tree_flows.removeAll();
        tree_switches.removeAll();
        action_tree.removeAll();
        match_table.removeAll();
        txtFlowName.setText("");
        txtFlowPriority.setText("");
		flow = null;
		currSwitch = null;
		newFlow = false;
		
		// Update and check if switches exist
		if (!FloodlightProvider.getSwitches(true).isEmpty()) {		
			for (Switch sw : FloodlightProvider.getSwitches(false)) {
				new TreeItem(tree_switches, SWT.NONE).setText(sw.getDpid());
			}
		}
		else{
		    new TreeItem(tree_switches, SWT.NONE).setText("No Switches");
		}
	}

	private void populateFlowTree(int index) {

	    /* Clean the slate */
		disposeEditors("all");
		tree_flows.removeAll();
		action_tree.removeAll();
		match_table.removeAll();
		txtFlowName.setText("");
        txtFlowPriority.setText("");
        newFlow = false;
		
        currSwitchIndex = index;
		currSwitch = FloodlightProvider.getSwitches(false).get(currSwitchIndex);
		flow = null;
		// This just makes sure the selection is noted in the event of
		// pre-selection
		tree_switches.select(tree_switches.getItem(index));

		// Fill the tree with the flows
		if (!FloodlightProvider.getStaticFlows(currSwitch.getDpid(), true).isEmpty()) {
			for (Flow flow : FloodlightProvider.getStaticFlows(currSwitch.getDpid(), false)) {
				if (flow.getName() != null)
					new TreeItem(tree_flows, SWT.NONE).setText(flow.getName());
			}
		} else {
			new TreeItem(tree_flows, SWT.NONE).setText("No Static Flows Set");
		}
	}

	/**
	 * Populates the view with the flow selected
	 * @param index The index of the flow in the list of a switch's flows
	 */
	private void populateFlowView(int index) {

		flow = FloodlightProvider.getStaticFlows(currSwitch.getDpid(), false).get(index);
        txtFlowName.setText(flow.getName());
        newFlow = false;
        if(flow.getPriority() != null)
            txtFlowPriority.setText(flow.getPriority());
        
        populateActionTree();
        populateMatchTable();
        
	}

	/**
	 * Generates a new flow, and updates the view accordingly
	 */
	public void setupNewFlow() {

	    if(currSwitch != null){
    		flow = new Flow(currSwitch.getDpid());
    		txtFlowName.setText("");
    	    txtFlowPriority.setText("");
    	    newFlow = true;
    	    
    	    populateActionTree();
            populateMatchTable();
	    }
	    else{
	        DisplayMessage.displayError(shell, "You must select a switch before creating a new flow");
	    } 
	}

	/* ACTION METHODS */
	
	// This method will populate the table with a list of the current actions
    protected void populateActionTree() {

        // Set the current action to null since the table has cleared, and a new
        // selection must be made
        currAction = null;
        disposeEditors("actions");

        // Clear the tables of any data
        action_table.removeAll();
        action_tree.removeAll();

        if (!flow.getActions().isEmpty()) {
            for (Action action : flow.getActions()) {
                new TreeItem(action_tree, SWT.NONE).setText(action.getType());
            }
        } else {
            new TreeItem(action_tree, SWT.NONE).setText("None Set");
        }
    }

    // This method will populate the table with a the selected actions
    // parameters
    protected void populateActionTable(int index) {

        currAction = flow.getActions().get(index);
        action_table.removeAll();

        for (String[] s : ActionToTable.getActionTableFormat(currAction)) {
            new TableItem(action_table, SWT.NO_FOCUS).setText(s);
        }
    }
    
	protected void setupAction(String actionType) {

        // Set the current action to null since
        currAction = new Action(actionType);

        // Clear the tables of any data
       action_table.removeAll();

        for (String[] s : ActionToTable.getNewActionTableFormat(currAction)) {
            new TableItem(action_table, SWT.NO_FOCUS).setText(s);
        }
    }
	
	/* MATCH METHODS */
	
	// This method will populate the table with a the selected actions
    // parameters
    protected void populateMatchTable() {

        // Clear the table of any data
        match_table.removeAll();
        matchTableFormat = MatchToTable.getMatchTableFormat(flow.getMatch());

        for (String[] s : matchTableFormat) {
            new TableItem(match_table, SWT.NO_FOCUS).setText(s);
        }
    }

     protected void clearValues() {
    
     // Clear the tables of any data
     match_table.removeAll();
    
     for (String[] s : MatchToTable.getNewMatchTableFormat()) {
         new TableItem(match_table, SWT.NO_FOCUS).setText(s);
         }
     }
     
     /* SWT */
	
	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(1194, 464);
		shell.setText("Static Flow Manager");

		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		shell.addListener(SWT.Close, new Listener() {
			@Override
			public void handleEvent(Event event) {
				// Create an "are you sure" box in the event that there is unsaved progress
				if (unsavedProgress) {
					int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
					MessageBox messageBox = new MessageBox(shell, style);
					messageBox.setText("Are you sure?!");
					messageBox
							.setMessage("Are you sure you wish to exit the flow manager? Any unsaved changes will not be pushed.");
					event.doit = messageBox.open() == SWT.YES;
					unsavedProgress = false;
				}
			}
		});

		MenuItem mntmNewSubmenu = new MenuItem(menu, SWT.CASCADE);
		mntmNewSubmenu.setText("File");

		Menu menu_1 = new Menu(mntmNewSubmenu);
		mntmNewSubmenu.setMenu(menu_1);

		MenuItem mntmClose = new MenuItem(menu_1, SWT.NONE);
		mntmClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		mntmClose.setText("Close");

		MenuItem mntmAbout = new MenuItem(menu, SWT.CASCADE);
		mntmAbout.setText("Help");

		Menu menu_2 = new Menu(mntmAbout);
		mntmAbout.setMenu(menu_2);

		MenuItem mntmInfo = new MenuItem(menu_2, SWT.NONE);
		mntmInfo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new About();
			}
		});
		mntmInfo.setText("About");

		Composite composite = new Composite(shell, SWT.NONE);
		GroupLayout gl_shell = new GroupLayout(shell);
		gl_shell.setHorizontalGroup(
		    gl_shell.createParallelGroup(GroupLayout.TRAILING)
		        .add(GroupLayout.LEADING, gl_shell.createSequentialGroup()
		            .add(composite, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
		            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_shell.setVerticalGroup(
		    gl_shell.createParallelGroup(GroupLayout.LEADING)
		        .add(gl_shell.createSequentialGroup()
		            .add(composite, GroupLayout.PREFERRED_SIZE, 514, GroupLayout.PREFERRED_SIZE)
		            .addContainerGap(26, Short.MAX_VALUE))
		);
		composite.setLayout(null);

		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setBounds(210, 42, 968, 41);
		composite_1.setLayout(null);
		
		txtFlowName = new Text(composite_1, SWT.BORDER);
		txtFlowName.setBounds(93, 10, 159, 26);
		
		Label lblFlowName = new Label(composite_1, SWT.NONE);
		lblFlowName.setBounds(10, 15, 76, 21);
		lblFlowName.setText("Flow Name :");
		
		Label lblFlowPriority = new Label(composite_1, SWT.NONE);
		lblFlowPriority.setBounds(277, 13, 94, 23);
		lblFlowPriority.setText("Flow Priority ");
		
		txtFlowPriority = new Text(composite_1, SWT.BORDER);
		txtFlowPriority.setBounds(377, 10, 93, 26);
		
		Button btnSave_3 = new Button(composite_1, SWT.NONE);
		btnSave_3.setBounds(761, 10, 75, 25);
		btnSave_3.setText("Save");
		
		Button btnDefaultValues = new Button(composite_1, SWT.NONE);
		btnDefaultValues.setBounds(842, 11, 110, 25);
		btnDefaultValues.setText("Default Values");
		btnDefaultValues.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	if(flow != null){
            		clearValues();
            	}
            	else{
            		DisplayMessage.displayError(shell, "You must select a flow or create a new one before modifying matches!");
            	}
            }
        });
		btnSave_3.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
            	if(flow != null){
	                if(MatchToTable.errorChecksPassed(currSwitch,match_table.getItems())){
	                    flow.setMatch(MatchManagerPusher.addMatch(match_table
	                        .getItems()));
	                    unsavedProgress = true;
	                    disposeEditors("match");
	                }
            	}
            	else{
            		DisplayMessage.displayError(shell, "You must select a flow or create a new one before modifying matches!");
            	}
            }
        });
		Composite composite_2 = new Composite(composite, SWT.NONE);
		composite_2.setBounds(10, 0, 194, 430);

		tree_switches = new Tree(composite_2, SWT.BORDER | SWT.NO_FOCUS
				| SWT.NONE);
		tree_switches.setBounds(0, 33, 185, 149);
		tree_switches.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem[] switch_selection = tree_switches.getSelection();
				if (switch_selection.length != 0) {
					populateFlowTree(tree_switches
							.indexOf(switch_selection[0]));
				}
			}
		});

		tree_flows = new Tree(composite_2, SWT.BORDER | SWT.NO_FOCUS | SWT.NONE);
		tree_flows.setBounds(0, 211, 185, 219);
		tree_flows.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem[] selection_switches = tree_switches.getSelection();
				TreeItem[] selection_flows = tree_flows.getSelection();

				if (selection_switches.length > 0
						&& selection_flows.length > 0) {
					if (!selection_flows[0].getText().equals(
							"No Static Flows Set")) {
							populateFlowView(tree_flows.indexOf(selection_flows[0]));
					}
				}
			}
		});

		Label lblSwitches = new Label(composite_2, SWT.NONE);
		lblSwitches.setBounds(0, 10, 70, 17);
		lblSwitches.setText("Switches");

		Label lblFlows = new Label(composite_2, SWT.NONE);
		lblFlows.setBounds(0, 188, 70, 17);
		lblFlows.setText("Flows");

		Composite composite_3 = new Composite(composite, SWT.NONE);
		composite_3.setBounds(210, 0, 978, 35);
		composite_3.setLayout(null);

		Button btnRefresh = new Button(composite_3, SWT.NONE);
		btnRefresh.setBounds(3, 3, 125, 29);
		btnRefresh.setText("Refresh");
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				populateSwitchTree();
			}
		});

		Button btnNewFLow = new Button(composite_3, SWT.NONE);
		btnNewFLow.setBounds(131, 3, 125, 29);
		btnNewFLow.setText("New Flow");
		btnNewFLow.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setupNewFlow();
			}
		});

		// Save button logic
		Button btnSave = new Button(composite_3, SWT.NONE);
		btnSave.setBounds(259, 3, 125, 29);
		btnSave.setText("Push");
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (flow != null) {
					if (flow.getName() != null
							|| !txtFlowName.getText().equals("")) {

						if(FlowManagerPusher.errorChecksPassed(txtFlowPriority.getText())){
						    
						// Parse the changes made to the flow
                        flow.setName(txtFlowName.getText());
                        flow.setPriority(txtFlowPriority.getText());
							
						// Push the flow and get the response
						String response;
                        try {
                            response = FlowManagerPusher.push(flow);
                            if (response.equals("Flow successfully pushed down to switches")) {
                                if(currSwitch != null){
                                    populateFlowTree(tree_switches
                                            .indexOf(tree_switches.getItem(currSwitchIndex)));
                                }
                                else{
                                    populateSwitchTree();
                                }
                                
                                disposeEditors("all");
                                unsavedProgress = false;
                            }
                            DisplayMessage.displayStatus(shell,response);
                        } catch (IOException | JSONException e1) {
                            DisplayMessage.displayError(shell,"Problem occured while pushing flow, please view the log for details");
                            e1.printStackTrace();
                            }
						}

					} else {
						DisplayMessage.displayError(shell, "Your flow must have a name");
					}
				}
				else {
					DisplayMessage.displayError(shell, "You do not have a flow to push!");
				}
			}
		});

		Button btnClear = new Button(composite_3, SWT.NONE);
		btnClear.setBounds(387, 3, 125, 29);
		btnClear.setText("Clear");
		btnClear.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setupNewFlow();
			}
		});

		Button btnDeleteFlow = new Button(composite_3, SWT.NONE);
		btnDeleteFlow.setBounds(515, 3, 125, 29);
		btnDeleteFlow.setText("Delete Flow");
		btnDeleteFlow.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (flow != null && newFlow == false) {
					String response;
                    try {
                        response = FlowManagerPusher.remove(flow);
                        if (response.equals("Entry " + flow.getName()
                                + " deleted"))
                            populateFlowTree(tree_switches
                                    .indexOf(tree_switches.getSelection()[0]));

                        disposeEditors("all");
                        DisplayMessage.displayStatus(shell, response);
                    } catch (IOException | JSONException e1) {
                        DisplayMessage.displayError(shell, "Problem occured while deleting flow. View the log for more details.");
                        e1.printStackTrace();
                    }		
				} else {
					DisplayMessage.displayError(shell, "You must select a flow to delete!");
				}
			}
		});

		// Delete all flows button logic
		Button btnDeleteAllFlows = new Button(composite_3, SWT.NONE);
		btnDeleteAllFlows.setBounds(643, 3, 125, 29);
		btnDeleteAllFlows.setText("Delete All Flows");
		
		Composite composite_4 = new Composite(composite, SWT.NONE);
		composite_4.setBounds(210, 89, 178, 340);
		
		action_tree = new Tree(composite_4, SWT.BORDER);
		action_tree.setBounds(0, 0, 178, 340);
		action_tree.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {

                disposeEditors("action");

                // Populate the action table, if we actually have actions.
                if (!action_tree.getSelection()[0].getText(0).equals("None Set"))
                    populateActionTable(action_tree.indexOf(action_tree.getSelection()[0]));
            }
        });
		
		Composite composite_6 = new Composite(composite, SWT.NONE);
		composite_6.setBounds(393, 90, 392, 339);
		
		action_table = new Table(composite_6, SWT.BORDER | SWT.FULL_SELECTION);
		action_table.setLocation(0, 76);
		action_table.setSize(392, 263);
		action_table.setHeaderVisible(true);
		action_table.setLinesVisible(true);
		// Editor
		actionEditor = new TableEditor(action_table);
		actionEditor.horizontalAlignment = SWT.LEFT;
		actionEditor.grabHorizontal = true;
		actionEditor.minimumWidth = 50;

        action_table.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {

                disposeEditors("action");

                // Identify the selected row
                TableItem item = (TableItem) e.item;
                if (item == null)
                    return;

                // The control that will be the editor must be a child of the
                // Table
                Text newEditor = new Text(action_table, SWT.NONE);
                newEditor.setText(item.getText(EDITABLECOLUMN));
                newEditor.addModifyListener(new ModifyListener() {
                    @Override
                    public void modifyText(ModifyEvent me) {
                        Text text = (Text) actionEditor.getEditor();
                        actionEditor.getItem()
                                .setText(EDITABLECOLUMN, text.getText());
                    }
                });
                newEditor.selectAll();
                newEditor.setFocus();
                actionEditor.setEditor(newEditor, item, EDITABLECOLUMN);
            }
        });
		
		TableColumn tblclmnParameter_1 = new TableColumn(action_table, SWT.NONE);
		tblclmnParameter_1.setWidth(161);
		tblclmnParameter_1.setText("Action Parameter");
		
		TableColumn tblclmnValue_1 = new TableColumn(action_table, SWT.NONE);
		tblclmnValue_1.setWidth(226);
		tblclmnValue_1.setText("Value");
		
		Button btnSave_1 = new Button(composite_6, SWT.NONE);
		btnSave_1.setBounds(33, 10, 75, 25);
		btnSave_1.setText("Save");
		
		Button btnRemove = new Button(composite_6, SWT.NONE);
		btnRemove.setBounds(33, 41, 75, 25);
		btnRemove.setText("Remove");
		
		combo = new Combo(composite_6, SWT.NONE);
		combo.setBounds(277, 37, 91, 29);
		combo.setItems(new String[] { "output", "enqueue", "strip-vlan",
                "set-vlan-id", "set-vlan-priority", "set-src-mac",
                "set-dst-mac", "set-tos-bits", "set-src-ip", "set-dst-ip",
                "set-src-port", "set-dst-port" });
		
		Label lblNewAction = new Label(composite_6, SWT.NONE);
		lblNewAction.setBounds(178, 41, 91, 15);
		lblNewAction.setText("New Action :");
		
		Button btnRemoveAllActions = new Button(composite_6, SWT.NONE);
		btnRemoveAllActions.setBounds(176, 10, 145, 25);
		btnRemoveAllActions.setText("Remove All Actions");
		btnRemoveAllActions.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                // Remove all the actions and refresh the action tree
            	if(flow != null){
                ActionManagerPusher.removeAllActions(flow);
                populateActionTree();
            	}
            	else{
            		DisplayMessage.displayError(shell, "You must select a flow or create a new one before modifying actions!");
            	}
            }
        });
		combo.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event e) {

                disposeEditors("action");
                if(flow != null)
                	setupAction(combo.getItem(combo.getSelectionIndex()));
                else
                	DisplayMessage.displayError(shell, "You must select a flow or create a new one before modifying actions!");
            }
        });
		btnRemove.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                // Remove the action
            	if(flow != null){
	                if (currAction != null) {
	                    ActionManagerPusher.removeAction(flow, currAction);
	                    populateActionTree();
	                }
            	}
            	else{
            		DisplayMessage.displayError(shell,"You must select a flow or create a new one before modifying actions!");
            	}
            }
        });
		btnSave_1.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	if(flow != null){
	                if (currAction != null) {
	                    if (!action_table.getItems()[0].getText(1).isEmpty()) {
	                        if (ActionToTable.errorChecksPassed(
	                                currSwitch, currAction,
	                                action_table.getItems())) {
	                            ActionManagerPusher.addAction(
	                                    action_table.getItems(), currAction, flow);
	
	                            disposeEditors("action");
	                            populateActionTree();
	                        }
	                    } else {
	                        DisplayMessage.displayError(shell, "You must enter a value before you save an action!");
	                    }
	                } else {
	                    DisplayMessage.displayError(shell, "You must create an action to save!");
	                }
            	}
            	else{
            		DisplayMessage.displayError(shell, "You must select a flow or create a new one before modifying actions!");
            	}
            }
        });
		
		Composite composite_8 = new Composite(composite, SWT.NONE);
		composite_8.setBounds(791, 89, 387, 339);
		
		match_table = new Table(composite_8, SWT.BORDER | SWT.FULL_SELECTION);
		match_table.setLocation(0, 0);
		match_table.setSize(387, 339);
		match_table.setHeaderVisible(true);
		match_table.setLinesVisible(true);
		matchEditor = new TableEditor(match_table);
        matchEditor.horizontalAlignment = SWT.LEFT;
        matchEditor.grabHorizontal = true;
        matchEditor.minimumWidth = 50;
        match_table.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {

                disposeEditors("match");

                // Identify the selected row
                TableItem item = (TableItem) e.item;
                if (item == null)
                    return;

                // The control that will be the editor must be a child of the
                // Table
                Text newEditor = new Text(match_table, SWT.NONE);
                newEditor.setText(item.getText(EDITABLECOLUMN));
                newEditor.addModifyListener(new ModifyListener() {
                    public void modifyText(ModifyEvent me) {
                        Text text = (Text) matchEditor.getEditor();
                        matchEditor.getItem()
                                .setText(EDITABLECOLUMN, text.getText());
                    }
                });
                newEditor.selectAll();
                newEditor.setFocus();
                matchEditor.setEditor(newEditor, item, EDITABLECOLUMN);
            }
        });
		
		TableColumn tblclmnParameter = new TableColumn(match_table, SWT.NONE);
		tblclmnParameter.setWidth(176);
		tblclmnParameter.setText("Match Parameter");
		
		TableColumn tblclmnValue = new TableColumn(match_table, SWT.NONE);
		tblclmnValue.setWidth(168);
		tblclmnValue.setText("Value");
		btnDeleteAllFlows.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// are you sure? warning
				int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
				MessageBox messageBox = new MessageBox(shell, style);
				messageBox.setText("Are you sure?!");
				messageBox
						.setMessage("Are you sure you wish to delete all flows?");
				if (messageBox.open() == SWT.YES) {
					// Delete all the flows for the current switch, populate
					// the table with the new information
				    if(currSwitch != null){
						try {
                            FlowManagerPusher.deleteAll(currSwitch.getDpid());
                            populateFlowTree(tree_switches.indexOf(tree_switches
                                    .getSelection()[0]));
                        } catch (IOException | JSONException e1) {
                            DisplayMessage.displayError(shell, "Problem ocurred deleting all flows, view the log for details.");
                            e1.printStackTrace();
                        }
				    }
				    else{
				        DisplayMessage.displayError(shell, "You must select a switch before deleting all of its flows!");
				    }
				}
			}
		});
		populateSwitchTree();
		if(FloodlightProvider.getSwitches(false).isEmpty())
            FloodlightProvider.getSwitches(true);
		shell.setLayout(gl_shell);
	}
}

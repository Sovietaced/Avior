package view.detailed;

import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.avior.Avior;
import net.floodlightcontroller.avior.controller.Action;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.custom.TableEditor;
import org.openflow.protocol.OFFlowMod;
import org.openflow.protocol.action.OFAction;


public class ActionsDisplay {
	
	protected Shell shell;
	protected Table table;
	protected Table table_action;
	protected Combo combo;
	protected Composite composite_3;
	protected OFFlowMod flow;
	protected String flowName, switchID;
	protected int currActionIndex;
	protected OFAction action, currAction;
	protected String[][] actionsSummary, actionSummary;
	final int EDITABLECOLUMN = 1;
	protected static TableEditor editor;
	protected static String newActionType;
	
	public ActionsDisplay(OFFlowMod selectedFlow, IOFSwitch selectedSwitch){
		switchID = selectedSwitch.getStringId();
		flow = selectedFlow;
		open();
	}

	/**
	 * Open the window.
	 */
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
		shell.dispose();
	}
	
	// This method will populate the table with a list of the current actions
	protected void displayActions(OFFlowMod selectedFlow){
		
  	  	// Set the current action to null since the table has cleared, and a new selection must be made
  	  	currAction = null;
  	  
		// Clear the tables of any data
		table.removeAll();
		table_action.removeAll();
		
		// Get the summaries of all the actions and populates the table with the summaries
		actionsSummary = Action.getActionsSummaryData(flow.getActions());
		for(String[] s: actionsSummary){
			new TableItem(table, SWT.NO_FOCUS).setText(s);
		}
	}
	
	// This method will populate the table with a the selected actions parameters
	protected void displayAction(int index){
  	  
		// Clear the table of any data
		table_action.removeAll();
		
		// Gets the action with the action's index.
		currAction = flow.getActions().get(index);
		actionSummary = Action.getActionSummaryData(currAction);
		
		for(String[] s : actionSummary){
			new TableItem(table_action, SWT.NO_FOCUS).setText(s);
		}
	}
	// Not complete yet
	protected void setupAction(String newAction){
		// Set the current action to null since 
  	  	currAction = null;
  	  
		// Clear the tables of any data
		table.removeAll();
		table_action.removeAll();
		newActionType = newAction;
		
		actionSummary = Action.getActionDefaultValues(newActionType);
		for(String[] s : actionSummary){
			new TableItem(table_action, SWT.NO_FOCUS).setText(s);
		}
		
	}
	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(800, 350);
		shell.setText("Action Information");
		shell.setLayout(new GridLayout(2, false));
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
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
			}
		});
		mntmInfo.setText("About");
		
		Composite composite = new Composite(shell, SWT.NONE);
		GridData gd_composite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite.widthHint = 217;
		composite.setLayoutData(gd_composite);
		
		Button btnRefresh = new Button(composite, SWT.NONE);
		btnRefresh.setBounds(10, 0, 91, 29);
		btnRefresh.setText("Refresh");
		btnRefresh.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event e) {
		    	  displayActions(flow);
		      } 
		    });
		
		Label lblNewAction = new Label(composite, SWT.NONE);
		lblNewAction.setBounds(120, 5, 97, 19);
		lblNewAction.setText("New Action : ");
		
		Composite composite_1 = new Composite(shell, SWT.NONE);
		GridData gd_composite_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite_1.heightHint = 39;
		gd_composite_1.widthHint = 567;
		composite_1.setLayoutData(gd_composite_1);
		
		combo = new Combo(composite_1, SWT.READ_ONLY);
		combo.setBounds(0, 5, 189, 29);
		combo.setItems(new String[] {"Output",
									"Enqueue",
									"Strip Vlan",
									"Set Vlan ID",
									"Set Vlan Priority",
									"Set Source Mac",
									"Set Destination Mac",
									"Set Tos Bits",
									"Set Source IP",
									"Set Desination IP",
									"Set Transport Source Port",
									"Set Transport Destination Port"});
		combo.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event e) {
		    	  int newSelection = combo.getSelectionIndex();
		    	  setupAction(combo.getItem(newSelection));
		    	  } 
		   });
		
		
		Button btnRemoveAllActions = new Button(composite_1, SWT.NONE);
		btnRemoveAllActions.setBounds(406, 5, 151, 29);
		btnRemoveAllActions.setText("Remove All Actions");
		btnRemoveAllActions.addListener(SWT.Selection, new Listener() {
	      public void handleEvent(Event e) {
	    	  // Clears the actions, of any exist
	    	  if(flow.getActions() != null){
	    	  flow.getActions().clear();
	    	  
	    	  // Add the flow with no actions
	    	  Avior.getStaticFlowEntryPusher().addFlow(flowName, flow, switchID);
	    	  displayActions(flow);
	    	  }
	      } 
	   });
		
		Button btnSave = new Button(composite_1, SWT.NONE);
		btnSave.setBounds(206, 5, 91, 29);
		btnSave.setText("Save");
		btnSave.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event e) {
		    		  
		    		  if(currAction != null){
		    	  // Parse the changes to the table and update the flow's actions accordingly
		    	  FlowDisplay.currFlow.setActions(Action.saveExisting(table_action.getItems(), currAction, flow, currActionIndex));

		    	  // Dispose the editor do it doesn't leave a ghost table item
		    	  if(editor.getEditor() != null){
			    		editor.getEditor().dispose();
		    	  }
			    
		    	  
		    	  // Clear the tables and refresh the information with the new actions!
		    	  displayActions(flow);
		    		  }
		    		  else if (newActionType != null){
		    			// Parse the changes to the table and update the flow's actions accordingly
		    			FlowDisplay.currFlow.setActions(Action.saveNew(table_action.getItems(), newActionType, flow));
				    	
				    	// Dispose the editor do it doesn't leave a ghost table item
				    	 if(editor.getEditor() != null){
					    		editor.getEditor().dispose();
				    	  }
				    	 
				    	 newActionType = null;
				    	 
				    	// Clear the tables and refresh the information with the new actions!
				    	displayActions(flow);	    	  
		    		  }
		      } 
		   });
		
		Button btnRemove = new Button(composite_1, SWT.NONE);
		btnRemove.setBounds(309, 5, 91, 29);
		btnRemove.setText("Remove");
		btnRemove.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event e) {
		    	  if(currAction != null){
		    	  flow.getActions().remove(currAction);
		    	  
		    	  // Add the flow
		    	  Avior.getStaticFlowEntryPusher().addFlow(flowName, flow, switchID);
		    	  displayActions(flow);
		    	  }		    	  
		      } 
		   });
		
		Composite composite_2 = new Composite(shell, SWT.NONE);
		GridData gd_composite_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite_2.heightHint = 225;
		composite_2.setLayoutData(gd_composite_2);
		
		table = new Table(composite_2, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(0, 0, 214, 225);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event e) {
		    	  TableItem[] selection = table.getSelection();
		    	  currActionIndex = Integer.valueOf(selection[0].getText(0))-1;
		    	  displayAction(currActionIndex);
		      } 
		   });
		
		TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setWidth(50);
		tableColumn.setText("#");
		
		TableColumn tblclmnType = new TableColumn(table, SWT.NONE);
		tblclmnType.setWidth(100);
		tblclmnType.setText("Action Type");
		
		Composite composite_3 = new Composite(shell, SWT.NONE);
		GridData gd_composite_3 = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_composite_3.heightHint = 224;
		gd_composite_3.widthHint = 567;
		composite_3.setLayoutData(gd_composite_3);
		
		table_action = new Table(composite_3, SWT.BORDER | SWT.FULL_SELECTION);
		table_action.setBounds(0, 0, 567, 224);
		table_action.setHeaderVisible(true);
		table_action.setLinesVisible(true);
		
		TableColumn tblclmnParameter = new TableColumn(table_action, SWT.NONE);
		tblclmnParameter.setWidth(200);
		tblclmnParameter.setText("Parameter");
		
		TableColumn tblclmnValue = new TableColumn(table_action, SWT.NONE);
		tblclmnValue.setWidth(100);
		tblclmnValue.setText("Value");
		
		editor = new TableEditor(table_action);
		//The editor must have the same size as the cell and must
		//not be any smaller than 50 pixels.
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		editor.minimumWidth = 50;
		
		table_action.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
				
				// Clean up any previous editor control
				Control oldEditor = editor.getEditor();
				if (oldEditor != null) oldEditor.dispose();
		
				// Identify the selected row
				TableItem item = (TableItem)e.item;
				if (item == null) return;
		
				// The control that will be the editor must be a child of the Table
				Text newEditor = new Text(table_action, SWT.NONE);
				newEditor.setText(item.getText(EDITABLECOLUMN));
				newEditor.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent me) {
						Text text = (Text)editor.getEditor();
						editor.getItem().setText(EDITABLECOLUMN, text.getText());
					}
				});
				newEditor.selectAll();
				newEditor.setFocus();
				editor.setEditor(newEditor, item, EDITABLECOLUMN);
			}
		});
		
		GridData gd_label = new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 2);
		
		// Attempt to load any actions, if there are any
		if(flow.getActions() != null){
			displayActions(flow);
		}
	}	
}

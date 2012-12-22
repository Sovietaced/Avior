package view.tools.firewall;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.overview.Switch;
import model.tools.firewall.Rule;
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

import controller.tools.firewall.json.RuleJSON;
import controller.tools.flowmanager.push.FlowManagerPusher;
import controller.tools.flowmanager.table.FlowToTable;
import controller.util.JSONException;

import view.About;

public class Firewall {

	protected static Shell shell;
	public Tree tree_rules;
	protected Table table_flow;
	protected TableEditor editor;
	final int EDITABLECOLUMN = 1;
	public static Switch currSwitch;
	public static Rule rule;
	protected List<Switch> switches = new ArrayList<Switch>();
	protected List<Rule> rules = new ArrayList<Rule>();
	protected static boolean unsavedProgress;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */

	public Firewall() {
		open();
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
	
	public void disposeEditor(){
		// Dispose the editor do it doesn't leave a ghost table item
		if (editor.getEditor() != null) {
			editor.getEditor().dispose();
		}
	}
	
	public static void displayError(String msg){
		MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR
				| SWT.OK);
		mb.setText("Error!");
		mb.setMessage(msg);
		mb.open();
	}

	public static void displayStatus(String msg){
		MessageBox mb = new MessageBox(shell,
				SWT.ICON_WORKING | SWT.OK);
		mb.setText("Status");
		mb.setMessage(msg);
		mb.open();
	}
	
	public static Rule getRule() {
		return rule;
	}

	public static void setRule(Rule r) {
		rule = r;
	}


	public static Switch getCurrSwitch() {
		return currSwitch;
	}

	public static void setCurrSwitch(Switch sw) {
		currSwitch = sw;
	}


	private void populateRuleTree() {

		// Clear the trees of any old data
		tree_rules.removeAll();
		rule = null;
		
		try {
			// Here we get the static flows only
			rules = RuleJSON.getRules();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (Rule r : rules) {
			new TreeItem(tree_rules, SWT.NONE).setText(String.valueOf(r.getRuleid()));
		}
	}

	private void populateRuleTable(Rule r) {

		table_flow.removeAll();

			for (String[] row : FlowToTable.getFlowTableFormat(r)) {
				new TableItem(table_flow, SWT.NONE).setText(row);
		}
	}

	// This method creates a new flow with default values.
	public void setupNewFlow() {

		table_flow.removeAll();

			for (String[] row : FlowToTable.getNewFlowTableFormat()) {
				new TableItem(table_flow, SWT.NONE).setText(row);
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(1200, 800);
		shell.setText("Floodlight Firewall Manager");

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
		gl_shell.setHorizontalGroup(gl_shell.createParallelGroup(
				GroupLayout.TRAILING).add(GroupLayout.LEADING, composite,
				GroupLayout.DEFAULT_SIZE, 1198, Short.MAX_VALUE));
		gl_shell.setVerticalGroup(gl_shell.createParallelGroup(
				GroupLayout.LEADING).add(
				gl_shell.createSequentialGroup()
						.add(composite, GroupLayout.PREFERRED_SIZE, 752,
								GroupLayout.PREFERRED_SIZE)
						.addContainerGap(36, Short.MAX_VALUE)));
		composite.setLayout(null);

		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setBounds(210, 42, 978, 700);
		composite_1.setLayout(null);

		table_flow = new Table(composite_1, SWT.BORDER | SWT.FULL_SELECTION
				| SWT.NO_FOCUS);
		table_flow.setBounds(0, 0, 978, 700);
		table_flow.setHeaderVisible(true);
		table_flow.setLinesVisible(true);
		table_flow.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String selection = table_flow.getSelection()[0].getText();
				
			}
		});

		editor = new TableEditor(table_flow);
		// The editor must have the same size as the cell and must
		// not be any smaller than 50 pixels.
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		editor.minimumWidth = 50;

		table_flow.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				disposeEditor();

				// Identify the selected row
				TableItem item = (TableItem) e.item;
				if (item == null)
					return;

				// The control that will be the editor must be a child of the
				// Table
				Text newEditor = new Text(table_flow, SWT.NONE);
				newEditor.setText(item.getText(EDITABLECOLUMN));
				newEditor.addModifyListener(new ModifyListener() {
					@Override
					public void modifyText(ModifyEvent me) {
						Text text = (Text) editor.getEditor();
						editor.getItem()
								.setText(EDITABLECOLUMN, text.getText());
						unsavedProgress = true;
					}
				});
				newEditor.selectAll();
				newEditor.setFocus();
				editor.setEditor(newEditor, item, EDITABLECOLUMN);
			}
		});

		TableColumn param = new TableColumn(table_flow, SWT.NONE);
		param.setWidth(300);
		param.setText("Parameter");

		TableColumn value = new TableColumn(table_flow, SWT.NONE);
		value.setWidth(300);
		value.setText("Value");

		Composite composite_2 = new Composite(composite, SWT.NONE);
		composite_2.setBounds(10, 0, 194, 742);

		tree_rules = new Tree(composite_2, SWT.BORDER | SWT.NO_FOCUS
				| SWT.NONE);
		tree_rules.setBounds(0, 33, 185, 699);
		tree_rules.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem[] switch_selection = tree_rules.getSelection();
				if (switch_selection.length != 0) {
				
				}
			}
		});

		Label lblSwitches = new Label(composite_2, SWT.NONE);
		lblSwitches.setBounds(0, 10, 70, 17);
		lblSwitches.setText("Rules");

		Composite composite_3 = new Composite(composite, SWT.NONE);
		composite_3.setBounds(210, 0, 978, 35);
		composite_3.setLayout(null);

		Button btnRefresh = new Button(composite_3, SWT.NONE);
		btnRefresh.setBounds(3, 3, 125, 29);
		btnRefresh.setText("Refresh");
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//populateSwitchTree();
			}
		});

		Button btnNewFLow = new Button(composite_3, SWT.NONE);
		btnNewFLow.setBounds(131, 3, 125, 29);
		btnNewFLow.setText("New Rule");
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
		btnDeleteFlow.setText("Delete Rule");
		btnDeleteFlow.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
			}
		});

		// Delete all flows button logic
		Button btnDeleteAllFlows = new Button(composite_3, SWT.NONE);
		btnDeleteAllFlows.setBounds(643, 3, 125, 29);
		btnDeleteAllFlows.setText("Delete All Rules");
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
					try {
						// Delete all the flows for the current switch, populate
						// the table with the new information
						FlowManagerPusher.deleteAll(currSwitch.getDpid());
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});

		// Populate the switch tree with the current switches on the network on
		// construction
		populateRuleTree();
		shell.setLayout(gl_shell);
	}
}

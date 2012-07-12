package view.tools.flowmanager;

import java.util.List;

import model.tools.flowmanager.Action;

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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.custom.TableEditor;

import controller.tools.flowmanager.push.ActionManagerPusher;
import controller.tools.flowmanager.table.ActionToTable;

public class ActionManager {

	protected Shell shell;
	protected Table table_action;
	protected Combo combo;
	protected Composite composite_3;
	protected String flowName, switchID;
	protected static int currActionIndex;
	protected static String currAction, actionType;
	protected boolean newAction = false;
	protected String[][] actionsSummary, actionTableFormat;
	final int EDITABLECOLUMN = 1;
	protected static TableEditor editor;
	protected static Tree tree;
	protected List<Action> actions;

	public ActionManager() {
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
	protected void populateActionTree() {

		// Set the current action to null since the table has cleared, and a new
		// selection must be made
		currAction = null;
		currActionIndex = -1;

		// Clear the tables of any data
		table_action.removeAll();
		tree.removeAll();

		actions = FlowManager.getActions();

		if (actions != null) {
			for (Action action : actions) {
				new TreeItem(tree, SWT.NONE).setText(action.getType());
			}
		}
	}

	// This method will populate the table with a the selected actions
	// parameters
	protected void populateActionTable(int index) {

		currActionIndex = index;
		currAction = tree.getItem(index).getText();
		actionType = currAction;
		// Clear the table of any data
		table_action.removeAll();

		actionTableFormat = ActionToTable.getActionTableFormat(index);

		for (String[] s : actionTableFormat) {
			new TableItem(table_action, SWT.NO_FOCUS).setText(s);
		}
	}

	protected void setupAction(String selectedAction) {

		newAction = true;

		// Set the current action to null since
		currAction = null;
		currActionIndex = -1;

		// Clear the tables of any data
		table_action.removeAll();

		actionTableFormat = ActionToTable
				.getNewActionTableFormat(selectedAction);

		if (actionTableFormat != null) {
			for (String[] s : actionTableFormat) {
				new TableItem(table_action, SWT.NO_FOCUS).setText(s);
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(800, 350);
		if (FlowManager.getFlow() != null)
			shell.setText("Action Information for Switch : "
					+ FlowManager.getFlow().getSwitch() + " Flow : "
					+ FlowManager.getFlow().getName());
		else
			shell.setText("Action Information for Switch : "
					+ FlowManager.getCurrSwitch() + " New Flow");
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
		GridData gd_composite = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_composite.widthHint = 217;
		composite.setLayoutData(gd_composite);

		Button btnRefresh = new Button(composite, SWT.NONE);
		btnRefresh.setBounds(10, 0, 91, 29);
		btnRefresh.setText("Refresh");
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				populateActionTree();
			}
		});

		Label lblNewAction = new Label(composite, SWT.NONE);
		lblNewAction.setBounds(120, 5, 97, 19);
		lblNewAction.setText("New Action : ");

		Composite composite_1 = new Composite(shell, SWT.NONE);
		GridData gd_composite_1 = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_composite_1.heightHint = 39;
		gd_composite_1.widthHint = 567;
		composite_1.setLayoutData(gd_composite_1);

		combo = new Combo(composite_1, SWT.READ_ONLY);
		combo.setBounds(0, 5, 189, 29);
		combo.setItems(new String[] { "output", "enqueue", "strip-vlan",
				"set-vlan-id", "set-vlan-priority", "set-src-mac",
				"set-dst-mac", "set-tos-bits", "set-src-ip", "set-dst-ip IP",
				"set-src-port", "set-dst-port" });
		combo.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				int newSelection = combo.getSelectionIndex();
				actionType = combo.getItem(newSelection);

				// Dispose the editor do it doesn't leave a ghost table item
				if (editor.getEditor() != null) {
					editor.getEditor().dispose();
				}

				setupAction(actionType);
			}
		});

		Button btnRemoveAllActions = new Button(composite_1, SWT.NONE);
		btnRemoveAllActions.setBounds(406, 5, 151, 29);
		btnRemoveAllActions.setText("Remove All Actions");
		btnRemoveAllActions.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ActionManagerPusher.removeAllActions();
				populateActionTree();
			}
		});

		Button btnSave = new Button(composite_1, SWT.NONE);
		btnSave.setBounds(206, 5, 91, 29);
		btnSave.setText("Save");
		btnSave.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (currAction == null && newAction == false) {
					MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR
							| SWT.OK);
					mb.setText("Error!");
					mb.setMessage("You have nothing to save.");
					mb.open();
				} else if (currAction == null && newAction == true) {
					// Failsafe for if the table item is empty
					if (!table_action.getItems()[0].getText(1).isEmpty()) {
						FlowManager.setActions(ActionManagerPusher
								.addNewAction(table_action.getItems(),
										actionType));
						// Dispose the editor do it doesn't leave a ghost table
						// item
						if (editor.getEditor() != null) {
							editor.getEditor().dispose();
						}

						table_action.removeAll();
					} else {
						MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR
								| SWT.OK);
						mb.setText("Error!");
						mb.setMessage("You must enter a value before you save an action!");
						mb.open();
					}
				}
				// Save existing flow changes
				else if (currAction != null) {
					if (!table_action.getItems()[0].getText(1).isEmpty()) {
						FlowManager.setActions(ActionManagerPusher.addAction(
								table_action.getItems(), actionType,
								currActionIndex));

						// Dispose the editor do it doesn't leave a ghost table
						// item
						if (editor.getEditor() != null) {
							editor.getEditor().dispose();
						}
					} else {
						MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR
								| SWT.OK);
						mb.setText("Error!");
						mb.setMessage("You must enter a value before you save an action!");
						mb.open();
					}
				}
				// Refresh the results
				populateActionTree();
			}
		});

		Button btnRemove = new Button(composite_1, SWT.NONE);
		btnRemove.setBounds(309, 5, 91, 29);
		btnRemove.setText("Remove");
		btnRemove.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// Remove the action
				if (newAction == false) {
					FlowManager.setActions(ActionManagerPusher
							.removeAction(currActionIndex));
					populateActionTree();
				} else {
					table_action.removeAll();
					newAction = false;
				}
			}
		});

		Composite composite_2 = new Composite(shell, SWT.NONE);
		GridData gd_composite_2 = new GridData(SWT.FILL, SWT.TOP, false, false,
				1, 1);
		gd_composite_2.heightHint = 224;
		composite_2.setLayoutData(gd_composite_2);

		tree = new Tree(composite_2, SWT.BORDER);
		tree.setBounds(0, 0, 215, 224);
		tree.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// Dispose the editor do it doesn't leave a ghost table item
				if (editor.getEditor() != null) {
					editor.getEditor().dispose();
				}

				populateActionTable(tree.indexOf(tree.getSelection()[0]));
			}
		});

		Composite composite_3 = new Composite(shell, SWT.NONE);
		GridData gd_composite_3 = new GridData(SWT.LEFT, SWT.TOP, false, false,
				1, 1);
		gd_composite_3.heightHint = 224;
		gd_composite_3.widthHint = 567;
		composite_3.setLayoutData(gd_composite_3);

		table_action = new Table(composite_3, SWT.BORDER | SWT.FULL_SELECTION);
		table_action.setBounds(0, 0, 567, 224);
		table_action.setHeaderVisible(true);
		table_action.setLinesVisible(true);

		editor = new TableEditor(table_action);
		// The editor must have the same size as the cell and must
		// not be any smaller than 50 pixels.
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		editor.minimumWidth = 50;

		table_action.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				// Clean up any previous editor control
				Control oldEditor = editor.getEditor();
				if (oldEditor != null)
					oldEditor.dispose();

				// Identify the selected row
				TableItem item = (TableItem) e.item;
				if (item == null)
					return;

				// The control that will be the editor must be a child of the
				// Table
				Text newEditor = new Text(table_action, SWT.NONE);
				newEditor.setText(item.getText(EDITABLECOLUMN));
				newEditor.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent me) {
						Text text = (Text) editor.getEditor();
						editor.getItem()
								.setText(EDITABLECOLUMN, text.getText());
					}
				});
				newEditor.selectAll();
				newEditor.setFocus();
				editor.setEditor(newEditor, item, EDITABLECOLUMN);
			}
		});

		TableColumn tblclmnParameter = new TableColumn(table_action, SWT.NONE);
		tblclmnParameter.setWidth(200);
		tblclmnParameter.setText("Parameter");

		TableColumn tblclmnValue = new TableColumn(table_action, SWT.NONE);
		tblclmnValue.setWidth(100);
		tblclmnValue.setText("Value");
		GridData gd_label = new GridData(SWT.LEFT, SWT.CENTER, false, true, 1,
				2);

		populateActionTree();
	}
}

package view.detailed;

import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.avior.Avior;
import net.floodlightcontroller.avior.controller.Flow;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
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
import org.openflow.protocol.OFFlowMod;
import org.openflow.protocol.OFMatch;

import view.About;

public class FlowDisplay {

	protected Shell shell;
	protected String[][] flowsData;
	protected IOFSwitch selectedSwitch;
	public static OFFlowMod flow, currFlow;
	protected Table table, table_flow;
	protected String[][] flowSummary;
	protected String selectedFlow, switchID;
	protected long tableID;
	protected TableEditor editor;
	protected boolean newFlow = false;
	final int EDITABLECOLUMN = 1;
	protected TableItem[] selection;
	public static final String TABLE_NAME = "controller_staticflowtableentry";
	public static final String COLUMN_NAME = "name";

	public FlowDisplay(IOFSwitch switchWanted) {
		selectedSwitch = switchWanted;
		switchID = selectedSwitch.getStringId();
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

	private void displayFlows() {
		// Clears the table prior to populating it with data
		table.removeAll();
		table_flow.removeAll();
		flowsData = Flow.getFlowData(selectedSwitch.getStringId());

		for (String[] data : flowsData) {
			new TableItem(table, SWT.NONE).setText(data);
		}
	}

	private void displayFlowMod(String name) {
		// Clears the table prior to populating it with data
		table_flow.removeAll();
		flow = Flow.getFlowByName(name, selectedSwitch);
		flowSummary = Flow.getFlowSummary(name, flow);

		for (String[] s : flowSummary) {
			new TableItem(table_flow, SWT.NONE).setText(s);
		}
	}

	private void displayNewFlow() {
		table_flow.removeAll();
		newFlow = true;
		// Create a new flow mod
		currFlow = new OFFlowMod();
		// Give the flow a default match
		currFlow.setMatch(new OFMatch());
		// Get the default values for the flow mod
		flowSummary = Flow.getNewFlowSummary(currFlow);

		for (String[] s : flowSummary) {
			new TableItem(table_flow, SWT.NONE).setText(s);
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setMaximized(true);
		shell.setText("Flow Information for table " + tableID + " on switch "
				+ selectedSwitch.getStringId());
		shell.setLayout(new GridLayout(4, false));

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
				new About();
			}
		});
		mntmInfo.setText("About");

		Composite composite = new Composite(shell, SWT.NONE);
		GridData gd_composite = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_composite.widthHint = 303;
		composite.setLayoutData(gd_composite);

		Button btnRefresh = new Button(composite, SWT.NONE);
		btnRefresh.setBounds(10, 10, 91, 29);
		btnRefresh.setText("Refresh");
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Refresh the table displaying the current flows
				displayFlows();
			}
		});

		Button btnNewFlow = new Button(composite, SWT.NONE);
		btnNewFlow.setBounds(107, 10, 91, 29);
		btnNewFlow.setText("New Flow");
		btnNewFlow.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Fill the table with default flow data
				displayNewFlow();
			}
		});

		Button btnDeleteFlows = new Button(composite, SWT.NONE);
		btnDeleteFlows.setBounds(204, 10, 100, 29);
		btnDeleteFlows.setText("Delete Flows");
		btnDeleteFlows.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Deletes all flows and displays a hopefully empty table
				if (!Avior.getStaticFlowEntryPusher()
						.getFlows(selectedSwitch.getStringId()).isEmpty()) {
					Avior.getStaticFlowEntryPusher().deleteFlowsForSwitch(
							selectedSwitch.getId());
					Avior.getStaticFlowEntryPusher()
							.getFlows(selectedSwitch.getStringId()).clear();
					selectedSwitch.clearAllFlowMods();
				}
				table_flow.removeAll();
				displayFlows();
			}
		});

		Label label = new Label(shell, SWT.SEPARATOR | SWT.VERTICAL);
		new Label(shell, SWT.NONE);

		Composite composite_1 = new Composite(shell, SWT.NONE);
		GridData gd_composite_1 = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_composite_1.widthHint = 317;
		composite_1.setLayoutData(gd_composite_1);

		Button btnSave = new Button(composite_1, SWT.NONE);
		btnSave.setBounds(0, 10, 91, 29);
		btnSave.setText("Save");
		btnSave.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {

				if (currFlow != null && newFlow == false) {
					Flow.saveExisting(currFlow, table_flow.getItems());

					// Dispose the editor do it doesn't leave a ghost table item
					if (editor.getEditor() != null) {
						editor.getEditor().dispose();
					}

					// Reset values to null now that the flow we're operating on
					// is saved
					selectedFlow = null;
					currFlow = null;

					// Display the flows
					displayFlows();

				}

				else if (currFlow != null && newFlow == true) {

					// This method parses the table data, saves it to the flow
					// in real time, and returns
					// the string value of the user gave the flow
					String newFlowName = Flow.saveNew(currFlow,
							table_flow.getItems());

					if (!newFlowName.equals("")) {

						// Dispose the editor do it doesn't leave a ghost table
						// item
						if (editor.getEditor() != null) {
							editor.getEditor().dispose();
						}
						if (currFlow.getActions() != null) {
							Avior.getStaticFlowEntryPusher().addFlow(
									newFlowName, currFlow, switchID);
							newFlow = false;
							currFlow = null;
							displayFlows();
						} else {
							MessageBox mb = new MessageBox(shell,
									SWT.ICON_ERROR | SWT.OK);
							mb.setText("Error!");
							mb.setMessage("You must give your flow some actions before saving it!");
							mb.open();
						}
					} else {
						MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR
								| SWT.OK);
						mb.setText("Error!");
						mb.setMessage("You must give your flow a name before saving it!");
						mb.open();
					}
				} else {
					System.out.println("No flow for you to save!");
				}
			}
		});

		Button btnDelete = new Button(composite_1, SWT.NONE);
		btnDelete.setBounds(100, 10, 91, 29);
		btnDelete.setText("Delete");
		btnDelete.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				// Delet the selected flow
				if (Avior.getStaticFlowEntryPusher().getFlows()
						.get(selectedFlow) != null) {
					Avior.getStaticFlowEntryPusher().deleteFlow(selectedFlow);
					displayFlows();
				}
			}
		});

		Button btnClear = new Button(composite_1, SWT.NONE);
		btnClear.setBounds(197, 10, 91, 29);
		btnClear.setText("Clear/Reset");
		btnClear.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				// Clear all entries in the table
				table_flow.removeAll();
			}
		});

		Composite composite_2 = new Composite(shell, SWT.NONE);
		GridData gd_composite_2 = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_composite_2.heightHint = 516;
		composite_2.setLayoutData(gd_composite_2);

		table = new Table(composite_2, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(0, 0, 298, 516);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				// Get the TableItem selection made by the user
				TableItem[] selection = table.getSelection();
				selectedFlow = selection[0].getText(1);
				currFlow = Flow.getFlowByName(selectedFlow, selectedSwitch);
				displayFlowMod(selectedFlow);
			}
		});
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);

		Composite composite_3 = new Composite(shell, SWT.NONE);
		composite_3.setLayout(null);
		GridData gd_composite_3 = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_composite_3.heightHint = 516;
		gd_composite_3.widthHint = 604;
		composite_3.setLayoutData(gd_composite_3);

		table_flow = new Table(composite_3, SWT.BORDER | SWT.SINGLE);
		table_flow.setBounds(0, 0, 600, 516);
		table_flow.setHeaderVisible(true);
		table_flow.setLinesVisible(true);

		TableColumn tblclmnNewColumnnn = new TableColumn(table_flow, SWT.NONE);
		tblclmnNewColumnnn.setWidth(200);
		tblclmnNewColumnnn.setText("Parameter");

		TableColumn tblclmnNewColumnn = new TableColumn(table_flow, SWT.NONE);
		tblclmnNewColumnn.setWidth(400);
		tblclmnNewColumnn.setText("Value");

		table_flow.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				// Get the TableItem selection made by the user
				TableItem[] selection = table_flow.getSelection();
				if (selection[0].getText(0).equals("Actions")) {
					new ActionsDisplay(currFlow, selectedSwitch);
				}
				if (selection[0].getText(0).equals("Match")) {
					new MatchDisplay(currFlow, selectedFlow);
				}
			}
		});

		editor = new TableEditor(table_flow);
		// The editor must have the same size as the cell and must
		// not be any smaller than 50 pixels.
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		editor.minimumWidth = 50;

		table_flow.addSelectionListener(new SelectionAdapter() {
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
				Text newEditor = new Text(table_flow, SWT.NONE);
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

		TableColumn tableColumn_1 = new TableColumn(table, SWT.NONE);
		tableColumn_1.setWidth(50);
		tableColumn_1.setText("#");

		TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_1.setWidth(200);
		tblclmnNewColumn_1.setText("Flow Name");
		GridData gd_label = new GridData(SWT.LEFT, SWT.CENTER, false, true, 1,
				2);

		// Try to populate the table on creation, if the table even has flows.
		if (Avior.getStaticFlowEntryPusher().getFlows(
				selectedSwitch.getStringId()) != null) {
			displayFlows();
		}
	}
}
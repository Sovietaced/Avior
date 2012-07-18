package view.tools.flowmanager;

import java.io.IOException;
import java.util.List;

import model.tools.flowmanager.Action;
import model.tools.flowmanager.Flow;
import model.tools.flowmanager.Match;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.eclipse.swt.widgets.Tree;
import org.eclipse.wb.swt.layout.grouplayout.GroupLayout;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Button;

import avior.json.JSONException;

import controller.tools.flowmanager.json.FlowManagerJSON;
import controller.tools.flowmanager.push.FlowManagerPusher;
import controller.tools.flowmanager.table.FlowToTable;

import view.About;

public class FlowManager {
 
	protected Shell shell;
	protected Tree tree_switches, tree_flows;
	protected Table table_flow;
	protected TableEditor editor;
	final int EDITABLECOLUMN = 1;
	protected static String currFlow, currSwitch;
	protected static List<Action> actions;
	protected static Match match;
	protected String serializedMatch;
	protected static Flow flow;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */

	public FlowManager() {
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
	}

	public static Flow getFlow() {
		return flow;
	}

	public static void setFlow(Flow f) {
		flow = f;
	}

	public static List<Action> getActions() {
		return flow.getActions();
	}

	public static void setActions(List<Action> acts) {
		flow.setActions(acts);
	}

	public static Match getMatch() {
		return flow.getMatch();
	}

	public static void setMatch(Match m) {
		flow.setMatch(m);
	}

	public String getSerializedMatch() {
		return this.serializedMatch;
	}

	public void setSerializedMatch(String match) {
		serializedMatch = match;
	}

	public static String getCurrSwitch() {
		return currSwitch;
	}

	public static void setCurrSwitch(String dpid) {
		currSwitch = dpid;
	}

	private void populateSwitchTree() {

		// Clear the trees of any old data
		tree_flows.removeAll();
		tree_switches.removeAll();

		List<String> switches = null;
		try {
			switches = FlowManagerJSON.getSwitchList();
		} catch (JSONException e) {
			System.out.println("Unable to parse switches from JSON");
		} catch (IOException e) {
			System.out.println("Unable to get switches from JSON");
		}

		if (switches != null) {
			for (String sw : switches) {
				new TreeItem(tree_switches, SWT.NONE).setText(sw);
			}
		}
	}

	private void populateFlowTree(String dpid) {

		List<String> flows = null;
		setCurrSwitch(dpid);
		tree_flows.removeAll();
		table_flow.removeAll();

		try {
			flows = FlowManagerJSON.getFlowList(dpid);
		} catch (JSONException e) {
			System.out.println("No flows exist for  this switch!");
		} catch (IOException e) {
			System.out.println("Unable to get flows from JSON");
		}

		if (flows != null) {
			for (String flow : flows) {
				new TreeItem(tree_flows, SWT.NONE).setText(flow);
			}
		}
	}

	private void populateFlowTable(Flow f) {

		currFlow = f.getName();
		table_flow.removeAll();

		String[][] flowTable = null;
		flowTable = FlowToTable.getFlowTableFormat(f);

		if (flowTable != null) {
			for (String[] row : flowTable) {
				new TableItem(table_flow, SWT.NONE).setText(row);
			}
		}
	}

	public void setupNewFlow() {

		currFlow = null;
		flow = new Flow(getCurrSwitch());
		table_flow.removeAll();

		String[][] flowTable = null;
		flowTable = FlowToTable.getNewFlowTableFormat();

		if (flowTable != null) {
			for (String[] row : flowTable) {
				new TableItem(table_flow, SWT.NONE).setText(row);
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(1200, 800);
		shell.setText("Floodlight Flow Manager");

		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		shell.addListener(SWT.Close, new Listener() {
			@Override
			public void handleEvent(Event event) {
				int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
				MessageBox messageBox = new MessageBox(shell, style);
				messageBox.setText("Are you sure?!");
				messageBox
						.setMessage("Are you sure you wish to exit the flow manager? Any unsaved changed will not be pushed.");
				event.doit = messageBox.open() == SWT.YES;
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
				if (table_flow.getSelection()[0].getText().equals("Actions")) {
					new ActionManager();
				} else if (table_flow.getSelection()[0].getText().equals(
						"Match")) {
					new MatchManager();
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
			@Override
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
					@Override
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

		TableColumn param = new TableColumn(table_flow, SWT.NONE);
		param.setWidth(300);
		param.setText("Parameter");

		TableColumn value = new TableColumn(table_flow, SWT.NONE);
		value.setWidth(300);
		value.setText("Value");

		Composite composite_2 = new Composite(composite, SWT.NONE);
		composite_2.setBounds(10, 0, 194, 742);

		tree_switches = new Tree(composite_2, SWT.BORDER | SWT.NO_FOCUS
				| SWT.NONE);
		tree_switches.setBounds(0, 33, 185, 268);
		tree_switches.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				populateFlowTree(tree_switches.getSelection()[0].getText());
			}
		});

		tree_flows = new Tree(composite_2, SWT.BORDER | SWT.None);
		tree_flows.setBounds(0, 330, 185, 412);
		tree_flows.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					if(tree_flows.getSelection()[0].getText() != null){
					flow = FlowManagerJSON.getFlow(
							tree_switches.getSelection()[0].getText(),
							tree_flows.getSelection()[0].getText());
					populateFlowTable(flow);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		Label lblSwitches = new Label(composite_2, SWT.NONE);
		lblSwitches.setBounds(0, 10, 70, 17);
		lblSwitches.setText("Switches");

		Label lblFlows = new Label(composite_2, SWT.NONE);
		lblFlows.setBounds(0, 307, 70, 17);
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

		Button btnSave = new Button(composite_3, SWT.NONE);
		btnSave.setBounds(259, 3, 125, 29);
		btnSave.setText("Push");
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println(flow.serialize());
				try {
					try {
						if (flow.getName() != null) {
							setFlow(FlowManagerPusher
									.parseTableChanges(table_flow.getItems()));
							String response = FlowManagerPusher.push(flow);
							if (response.equals("Entry pushed"))
								populateFlowTree(tree_switches.getSelection()[0]
										.getText());

							// Dispose the editor do it doesn't leave a ghost
							// table
							// item
							if (editor.getEditor() != null)
								editor.getEditor().dispose();

							MessageBox mb = new MessageBox(shell,
									SWT.ICON_ERROR | SWT.OK);
							mb.setText("Status");
							mb.setMessage(response);
							mb.open();
						} else {
							MessageBox mb = new MessageBox(shell,
									SWT.ICON_ERROR | SWT.OK);
							mb.setText("Error");
							mb.setMessage("Your flow must have a name");
							mb.open();
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		Button btnDeleteClear = new Button(composite_3, SWT.NONE);
		btnDeleteClear.setBounds(387, 3, 125, 29);
		btnDeleteClear.setText("Clear");

		Button btnDeleteFlow = new Button(composite_3, SWT.NONE);
		btnDeleteFlow.setBounds(515, 3, 125, 29);
		btnDeleteFlow.setText("Delete Flow");
		btnDeleteFlow.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					try {
						String response = FlowManagerPusher.remove(flow);
						if (response.equals("Entry " + flow.getName()
								+ " deleted"))
							populateFlowTree(flow.getSwitch());

						// Dispose the editor do it doesn't leave a ghost table
						// item
						if (editor.getEditor() != null)
							editor.getEditor().dispose();

						MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR
								| SWT.OK);
						mb.setText("Status");
						mb.setMessage(response);
						mb.open();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		Button btnDeleteAllFlows = new Button(composite_3, SWT.NONE);
		btnDeleteAllFlows.setBounds(643, 3, 125, 29);
		btnDeleteAllFlows.setText("Delete All Flows");
		btnDeleteAllFlows.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				try {
					FlowManagerPusher.removeAll(getCurrSwitch());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

		populateSwitchTree();

		shell.setLayout(gl_shell);
	}
}

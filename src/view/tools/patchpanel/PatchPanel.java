package view.tools.patchpanel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.overview.Port;
import model.overview.Switch;
import model.tools.flowmanager.Flow;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.wb.swt.layout.grouplayout.GroupLayout;

import controller.tools.flowmanager.json.StaticFlowManagerJSON;
import controller.tools.flowmanager.push.FlowManagerPusher;
import controller.tools.patchpanel.push.PatchPanelPusher;
import controller.util.JSONException;

import view.About;
import view.Gui;

public class PatchPanel {

	protected static Shell shell;
	protected Table table;
	protected Display display;
	protected Table table_1, table_ports1, table_ports2;
	protected Tree tree_switches, tree_patches;
	protected List<Switch> switches;
	protected List<Flow> patches;
	protected Flow patch;
	protected String[] pair = new String[2];
	protected String currSwitch;

	public PatchPanel() {
		open();
	}

	public void open() {
		display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public void displayError(String msg){
		MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR
				| SWT.OK);
		mb.setText("Patch Panel");
		mb.setMessage(msg);
		mb.open();
	}
	
	public void displayStatus(String msg){
		MessageBox mb = new MessageBox(shell,
				SWT.ICON_WORKING | SWT.OK);
		mb.setText("Status");
		mb.setMessage(msg);
		mb.open();
	}
	private void populateSwitchTree() {

		// Clear the trees of any old data
		tree_patches.removeAll();
		tree_switches.removeAll();

		if(Gui.switchesLoaded == false){
			Gui.loadSwitches();
		}
		
		switches = Gui.getSwitches();

		if (!switches.isEmpty()) {
			for (Switch sw : switches) {
				new TreeItem(tree_switches, SWT.NONE).setText(sw.getDpid());
			}
		}
	}
	
	private boolean freeToPatch(TableItem[] items){
		for(Flow p : patches){
			String input = p.getMatch().getInputPort();
			String output = p.getActions().get(0).getValue();
			String port1 = items[0].getText(1);
			String port2 = items[0].getText(2);
			if(input.equals(port1)
					|| input.equals(port2)
					|| output.equals(port1)
					|| output.equals(port2)){
				return false;
			}
		}
		return true;
	}

	private void populatePatchTree(int index) {

		// Clear the trees of any old data
		tree_patches.removeAll();
		table_1.removeAll();
		patches = new ArrayList<Flow>();
		patch = null;
		currSwitch = Gui.getSwitches().get(index).getDpid();

		List<Flow> flows = new ArrayList<Flow>();
		try {
			flows = StaticFlowManagerJSON.getFlows(currSwitch);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Loop through the flows, find valid patch panel flows, and add them to
		// the tree
		if (!flows.isEmpty()) {
			for (Flow flow : flows) {
				if (flow.getName().contains("pp")
						&& flow.getMatch().getInputPort() != null
						&& !flow.getActions().isEmpty()) {
					if (flow.getActions().get(0).getType().equals("output")) {
						patches.add(flow);
						new TreeItem(tree_patches, SWT.NONE).setText(flow
								.getName());
					}
				}
			}
		}
		if(patches.isEmpty())
			new TreeItem(tree_patches, SWT.NONE).setText("None Set");
	}

	private void populatePortTables(int index) {

		// Clear the trees of any old data
		table_ports1.removeAll();
		table_ports2.removeAll();

		Switch sw = Gui.getSwitches().get(index);

		if (sw != null) {
			for (Port p : sw.getPorts().values()) {
				String[] portData = { p.getPortNumber(), p.getStatus() };
				new TableItem(table_ports1, SWT.NONE).setText(portData);
				new TableItem(table_ports2, SWT.NONE).setText(portData);
			}
		}
	}

	private void populatePatchTable(int index) {

		// Clear the table of any old data
		table_1.removeAll();

		patch = patches.get(index);

		if (patch != null) {
			// Here we grab data about the flow so we can display it in the
			// table, the input port and the action output port
			String[] patchData = { patch.getName(),
					patch.getMatch().getInputPort(),
					patch.getActions().get(0).getValue() };
			new TableItem(table_1, SWT.NONE).setText(patchData);
		}
	}

	private void loadNewPatch() {

		// Clear the table of any old data
		table_1.removeAll();
		// Set the patch to null since it's new
		patch = null;
		// Insert default data into the table
		String[] patchData = { "pp-[port1]to[port2]", "None Set", "None Set" };
		new TableItem(table_1, SWT.NONE).setText(patchData);

	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {

		shell = new Shell();
		shell.setSize(800, 800);
		shell.setText("Patch Panel");

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
		GroupLayout gl_shell = new GroupLayout(shell);
		gl_shell.setHorizontalGroup(gl_shell.createParallelGroup(
				GroupLayout.TRAILING).add(GroupLayout.LEADING, composite,
				GroupLayout.DEFAULT_SIZE, 798, Short.MAX_VALUE));
		gl_shell.setVerticalGroup(gl_shell.createParallelGroup(
				GroupLayout.LEADING).add(
				gl_shell.createSequentialGroup()
						.add(composite, GroupLayout.PREFERRED_SIZE, 752,
								GroupLayout.PREFERRED_SIZE)
						.addContainerGap(36, Short.MAX_VALUE)));
		composite.setLayout(new FormLayout());

		Composite composite_1 = new Composite(composite, SWT.NONE);
		FormData fd_composite_1 = new FormData();
		fd_composite_1.bottom = new FormAttachment(0, 742);
		fd_composite_1.right = new FormAttachment(0, 217);
		fd_composite_1.top = new FormAttachment(0);
		fd_composite_1.left = new FormAttachment(0);
		composite_1.setLayoutData(fd_composite_1);

		Composite composite_2 = new Composite(composite, SWT.NONE);
		FormData fd_composite_2 = new FormData();
		fd_composite_2.bottom = new FormAttachment(composite_1, 0, SWT.BOTTOM);
		fd_composite_2.top = new FormAttachment(composite_1, 0, SWT.TOP);
		fd_composite_2.right = new FormAttachment(composite_1, 573, SWT.RIGHT);
		fd_composite_2.left = new FormAttachment(composite_1, 6);

		tree_switches = new Tree(composite_1, SWT.BORDER | SWT.NO_FOCUS);
		tree_switches.setBounds(10, 37, 207, 334);
		tree_switches.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				populatePatchTree(tree_switches.indexOf(tree_switches
						.getSelection()[0]));
				populatePortTables(tree_switches.indexOf(tree_switches
						.getSelection()[0]));
			}
		});

		tree_patches = new Tree(composite_1, SWT.BORDER | SWT.NO_FOCUS);
		tree_patches.setBounds(10, 408, 207, 334);
		tree_patches.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (!tree_patches.getSelection()[0].getText()
						.equals("None Set"))
					populatePatchTable(tree_patches.indexOf(tree_patches
							.getSelection()[0]));
			}
		});

		Label lblSwitches = new Label(composite_1, SWT.NONE);
		lblSwitches.setBounds(10, 10, 82, 17);
		lblSwitches.setText("Switches");

		Label lblPatches = new Label(composite_1, SWT.NONE);
		lblPatches.setBounds(10, 385, 150, 17);
		lblPatches.setText("Patch Flows");
		composite_2.setLayoutData(fd_composite_2);

		table_ports1 = new Table(composite_2, SWT.BORDER | SWT.NO_FOCUS);
		table_ports1.setBounds(50, 198, 200, 544);
		table_ports1.setHeaderVisible(true);
		table_ports1.setLinesVisible(true);
		table_ports1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (table_1.getItems().length != 0)
					table_1.getItems()[0].setText(1,
							table_ports1.getSelection()[0].getText());
			}
		});

		TableColumn ports1 = new TableColumn(table_ports1, SWT.NONE);
		ports1.setWidth(40);
		ports1.setText("#");

		TableColumn status1 = new TableColumn(table_ports1, SWT.NONE);
		status1.setWidth(110);
		status1.setText("Status");

		table_ports2 = new Table(composite_2, SWT.BORDER | SWT.NO_FOCUS);
		table_ports2.setBounds(337, 198, 200, 544);
		table_ports2.setHeaderVisible(true);
		table_ports2.setLinesVisible(true);
		table_ports2.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (table_1.getItems().length != 0)
					table_1.getItems()[0].setText(2,
							table_ports2.getSelection()[0].getText());
			}
		});

		TableColumn ports2 = new TableColumn(table_ports2, SWT.NONE);
		ports2.setWidth(40);
		ports2.setText("#");

		TableColumn status2 = new TableColumn(table_ports2, SWT.NONE);
		status2.setWidth(110);
		status2.setText("Status");

		Label lblInputPort = new Label(composite_2, SWT.NONE);
		lblInputPort.setBounds(50, 175, 250, 17);
		lblInputPort.setText("First Patch Panel Port");

		Label lblOutputPort = new Label(composite_2, SWT.NONE);
		lblOutputPort.setBounds(337, 175, 250, 17);
		lblOutputPort.setText("Second Patch Panel Port");

		table_1 = new Table(composite_2, SWT.BORDER | SWT.NONE | SWT.NO_FOCUS);
		table_1.setBounds(50, 37, 487, 50);
		table_1.setHeaderVisible(true);
		table_1.setLinesVisible(true);

		TableColumn tblclmnName = new TableColumn(table_1, SWT.NONE);
		tblclmnName.setWidth(193);
		tblclmnName.setText("Name");

		TableColumn tblclmnInputPort = new TableColumn(table_1, SWT.NONE);
		tblclmnInputPort.setWidth(100);
		tblclmnInputPort.setText("Input Port");

		TableColumn tblclmnOutportPort = new TableColumn(table_1, SWT.NONE);
		tblclmnOutportPort.setWidth(100);
		tblclmnOutportPort.setText("Output Port");

		Button btnPatch = new Button(composite_2, SWT.NONE);
		btnPatch.setBounds(244, 123, 91, 29);
		btnPatch.setText("Patch!");
		btnPatch.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (table_1.getItems().length != 0) {
					if (!table_1.getItems()[0].getText(1).equals("None Set")
							&& !table_1.getItems()[0].getText(2).equals(
									"None Set")) {
						if (!table_1.getItems()[0].getText(1).equals(
								table_1.getItems()[0].getText(2))) {
								if(freeToPatch(table_1.getItems())){
									displayStatus(PatchPanelPusher.push(
											table_1.getItems(), currSwitch));
									populatePatchTree(tree_switches
											.indexOf(tree_switches.getSelection()[0]));
								}
								else{
									displayError("Another patch that uses those ports already exists!");
								}
						} else {
							displayError("You cannot make a patch with two of the same ports!");
						}
					} else {
						displayError("Your new patch is missing a port! Make sure you have set both before attempting to patch!");
					}
				} else {
					displayError("You must first create a patch!");
				}
			}
		});

		Button btnNewPatch = new Button(composite_2, SWT.NONE);
		btnNewPatch.setBounds(147, 123, 91, 29);
		btnNewPatch.setText("New Patch");
		btnNewPatch.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				loadNewPatch();
			}
		});

		Button btnRemovePatch = new Button(composite_2, SWT.NONE);
		btnRemovePatch.setBounds(339, 123, 118, 29);
		btnRemovePatch.setText("Remove Patch");
		btnRemovePatch.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (patch != null) {
					try {
						try {
							String response = FlowManagerPusher.remove(patch);
							if (response.equals("Entry " + patch.getName()
									+ " deleted")) {
								populatePatchTree(tree_switches
										.indexOf(tree_switches.getSelection()[0]));
							}

							displayStatus(response);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					displayError("You must select a patch flow to delete!");
				}
			}
		});

		shell.setLayout(gl_shell);

		populateSwitchTree();
	}
}

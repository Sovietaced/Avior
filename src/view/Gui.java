package view;

import avior.json.JSONException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
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

import view.tools.flowmanager.FlowManager;

import controller.overview.json.Controller;
import controller.overview.json.Devices;
import controller.overview.json.Switches;

public class Gui {

	protected Shell shell;
	protected Table switches_table;
	protected Table devices_table;
	protected Composite controllerOverview;
	protected Label lblInsertHostname, lblInsertHealthy, lblInsertJvmMemory,
			lblInsertModules;
	protected boolean dispose;
	protected Display display;
	public static String IP;

	/**
	 * Launch the application.
	 */
	public Gui(String controllerIP) {
		IP = controllerIP;
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
		// If the window is closed, stop the entire application.
		display.dispose();
	}

	private void displaySwitchData() {
		// Clears the table prior to populating it with data
		switches_table.removeAll();

		try {
			String[][] switchSummariesTable = Switches.switchSummariesToTable();
			for (String[] data : switchSummariesTable) {
				new TableItem(switches_table, SWT.NONE).setText(data);
			}
			shell.setText("Switches Overview");
		} catch (Exception o) {
			o.printStackTrace();
			MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
			mb.setText("Error!");
			mb.setMessage("Failed to display switches, no switches found!");
			mb.open();
		}
	}

	private void displayControllerInfo() throws JSONException {
		String[] controllerInfo = Controller.getControllerInfo();
		if (controllerInfo != null) {
			lblInsertHostname.setText(controllerInfo[0]);
			lblInsertHealthy.setText(controllerInfo[1]);
			lblInsertJvmMemory.setText(controllerInfo[2]);
			lblInsertModules.setText(controllerInfo[3]);

		} else {
			MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
			mb.setText("Error!");
			mb.setMessage("Failed to display controller, no controller found!");
			mb.open();
		}
	}

	private void displayDevicesData() {
		// Clears the table prior to populating it with data
		devices_table.removeAll();
		String[][] devicesSummaryTable = Devices.deviceSummariesToTable();
		if (devicesSummaryTable != null) {
			for (String[] data : devicesSummaryTable) {
				new TableItem(devices_table, SWT.NONE).setText(data);
			}
		} else {
			MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
			mb.setText("Error!");
			mb.setMessage("Failed to display devices, no devices found!");
			mb.open();
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {

		shell = new Shell();
		shell.setSize(1200, 800);
		shell.setText("Floodlight Control Panel");

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
				GroupLayout.DEFAULT_SIZE, 1198, Short.MAX_VALUE));
		gl_shell.setVerticalGroup(gl_shell.createParallelGroup(
				GroupLayout.LEADING).add(
				gl_shell.createSequentialGroup()
						.add(composite, GroupLayout.PREFERRED_SIZE, 752,
								GroupLayout.PREFERRED_SIZE)
						.addContainerGap(36, Short.MAX_VALUE)));
		composite.setLayout(new FormLayout());

		final Composite composite_1 = new Composite(composite, SWT.NONE);
		FormData fd_composite_1 = new FormData();
		fd_composite_1.right = new FormAttachment(100, -10);
		fd_composite_1.top = new FormAttachment(0, 10);
		fd_composite_1.bottom = new FormAttachment(0, 742);
		composite_1.setLayoutData(fd_composite_1);

		// Create new stack layout that we reference and modify
		final StackLayout stackLayout = new StackLayout();

		// Set the layout of the composite to the stack layout we just created
		composite_1.setLayout(stackLayout);

		Composite composite_2 = new Composite(composite, SWT.NONE);
		fd_composite_1.left = new FormAttachment(0, 143);
		FormData fd_composite_2 = new FormData();
		fd_composite_2.top = new FormAttachment(0);
		fd_composite_2.right = new FormAttachment(composite_1, -6);
		fd_composite_2.bottom = new FormAttachment(0, 742);
		fd_composite_2.left = new FormAttachment(0, 10);
		composite_2.setLayoutData(fd_composite_2);

		final Tree tree = new Tree(composite_2, SWT.BORDER);
		tree.setBounds(0, 36, 127, 705);
		tree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem[] selection = tree.getSelection();
				// Handler for Switches tree item
				if (selection[0].getText(0).equals("Switches")) {
					stackLayout.topControl = switches_table;
					composite_1.layout();
					displaySwitchData();
				}

				// Handler for Controller tree item
				if (selection[0].getText(0).equals("Controller")) {
					stackLayout.topControl = controllerOverview;
					composite_1.layout();
					try {
						displayControllerInfo();
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				// Handler for Devices tree item
				if (selection[0].getText(0).equals("Devices")) {
					stackLayout.topControl = devices_table;
					composite_1.layout();
					displayDevicesData();
				}

				// Handler for Flow Manager tree item
				if (selection[0].getText(0).equals("Flow Manager")) {
					new FlowManager();
				}

				// Handler for Firewall tree item
				if (selection[0].getText(0).equals("Firewall ")) {
					System.out.println("Swagtein!");
					// new Firewall();
				}
			}
		});

		Label lblM = new Label(composite_2, SWT.NONE);
		lblM.setBounds(10, 10, 107, 17);
		lblM.setText("Avior v1.1");

		TreeItem trtmTest = new TreeItem(tree, SWT.NONE);
		trtmTest.setText("Overview");

		TreeItem trtmController = new TreeItem(trtmTest, SWT.NONE);
		trtmController.setText("Controller");

		TreeItem trtmSwitches = new TreeItem(trtmTest, SWT.NONE);
		trtmSwitches.setText("Switches");

		TreeItem trtmDevices = new TreeItem(trtmTest, SWT.NONE);
		trtmDevices.setText("Devices");
		trtmTest.setExpanded(true);

		TreeItem trtmTools = new TreeItem(tree, SWT.NONE);
		trtmTools.setText("Tools");

		TreeItem trtmFlowManager = new TreeItem(trtmTools, SWT.NONE);
		trtmFlowManager.setText("Flow Manager");

		TreeItem trtmFirewall = new TreeItem(trtmTools, SWT.NONE);
		trtmFirewall.setText("Firewall");

		switches_table = new Table(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
		switches_table.setHeaderVisible(true);
		switches_table.setLinesVisible(true);

		TableColumn tableColumn_1 = new TableColumn(switches_table, SWT.NONE);
		tableColumn_1.setWidth(50);
		tableColumn_1.setText("#");

		TableColumn tblclmnNewColumn_1 = new TableColumn(switches_table,
				SWT.NONE);
		tblclmnNewColumn_1.setWidth(200);
		tblclmnNewColumn_1.setText("DPID");

		TableColumn tblclmnNewColumn_2 = new TableColumn(switches_table,
				SWT.NONE);
		tblclmnNewColumn_2.setWidth(200);
		tblclmnNewColumn_2.setText("Vendor");

		TableColumn tblclmnNewColumn = new TableColumn(switches_table, SWT.NONE);
		tblclmnNewColumn.setWidth(100);
		tblclmnNewColumn.setText("Packets");

		TableColumn tblclmnNewColumn_3 = new TableColumn(switches_table,
				SWT.NONE);
		tblclmnNewColumn_3.setWidth(100);
		tblclmnNewColumn_3.setText("Bytes");

		TableColumn tblclmnNewColumn_9 = new TableColumn(switches_table,
				SWT.NONE);
		tblclmnNewColumn_9.setWidth(100);
		tblclmnNewColumn_9.setText("Flows");

		devices_table = new Table(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
		devices_table.setHeaderVisible(true);
		devices_table.setLinesVisible(true);

		TableColumn tableColumn = new TableColumn(devices_table, SWT.NONE);
		tableColumn.setText("#");
		tableColumn.setWidth(50);

		TableColumn tblclmnIp = new TableColumn(devices_table, SWT.NONE);
		tblclmnIp.setWidth(200);
		tblclmnIp.setText("MAC");

		TableColumn tblclmnMac = new TableColumn(devices_table, SWT.NONE);
		tblclmnMac.setWidth(150);
		tblclmnMac.setText("IP");

		TableColumn tblclmnVlanId = new TableColumn(devices_table, SWT.NONE);
		tblclmnVlanId.setWidth(200);
		tblclmnVlanId.setText("Attached Switch");

		TableColumn swport = new TableColumn(devices_table, SWT.NONE);
		swport.setWidth(100);
		swport.setText("Switch Port");

		TableColumn last = new TableColumn(devices_table, SWT.NONE);
		last.setWidth(300);
		last.setText("Last Seen");

		controllerOverview = new Composite(composite_1, SWT.NONE);

		Label lblControllerOverview = new Label(controllerOverview, SWT.NONE);
		lblControllerOverview.setBounds(39, 10, 149, 17);
		lblControllerOverview.setText("Controller Overview");

		Label lblHostname = new Label(controllerOverview, SWT.NONE);
		lblHostname.setBounds(10, 45, 79, 17);
		lblHostname.setText("Hostname:");

		Label lblHealthy = new Label(controllerOverview, SWT.NONE);
		lblHealthy.setBounds(10, 68, 64, 17);
		lblHealthy.setText("Healthy :");

		Label lblJvmMemoryBloat = new Label(controllerOverview, SWT.NONE);
		lblJvmMemoryBloat.setBounds(10, 91, 138, 17);
		lblJvmMemoryBloat.setText("JVM Memory Bloat:");

		Label lblModulesLoaded = new Label(controllerOverview, SWT.NONE);
		lblModulesLoaded.setBounds(10, 114, 116, 17);
		lblModulesLoaded.setText("Modules Loaded:");

		lblInsertHostname = new Label(controllerOverview, SWT.NONE);
		lblInsertHostname.setBounds(95, 45, 116, 17);

		lblInsertHealthy = new Label(controllerOverview, SWT.NONE);
		lblInsertHealthy.setBounds(78, 68, 159, 17);

		lblInsertJvmMemory = new Label(controllerOverview, SWT.NONE);
		lblInsertJvmMemory.setBounds(154, 91, 339, 17);

		lblInsertModules = new Label(controllerOverview, SWT.WRAP);
		lblInsertModules.setLayoutData("width 500:pref:pref");
		lblInsertModules.setBounds(132, 114, 637, 700);

		// Set to controller tab on load
		stackLayout.topControl = controllerOverview;
		composite_1.layout(true);

		shell.setLayout(gl_shell);

	}
}
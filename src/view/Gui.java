package view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.overview.Switch;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
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

import view.tools.flowmanager.StaticFlowManager;
import view.tools.firewall.Firewall;

import controller.floodlightprovider.FloodlightProvider;
import controller.overview.json.ControllerJSON;
import controller.overview.table.DeviceToTable;
import controller.overview.table.FlowToTable;
import controller.overview.table.PortToTable;
import controller.overview.table.SwitchToTable;
import controller.util.JSONException;

public class Gui {

	private Shell shell;
	private Table devices_table, table_ports, table_flows;
	private Table switches_table;
	private Composite controllerOverview, detailed_switch;
	private Label lblInsertHostname, lblInsertHealthy, lblInsertJvmMemory,
	lblInsertModules, lblSn, lblHardware, lblSoftware, lblManufacturer;
	private TreeItem trtmSwitches, trtmDevices;
	private static Switch currSwitch;
	private static boolean switchesLoaded;
	private Display display;
	private static List<String> controllerInfo = new ArrayList<String>();

	/**
	 * Launch the application.
	 */
	public Gui(String controllerIP) {
		FloodlightProvider.setIP(controllerIP);
		open();
	}

	public void open() {
		display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		displayControllerInfo();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		// If the window is closed, stop the entire application.
		display.dispose();
	}

	public void displayError(String msg) {
		MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
		mb.setText("Error!");
		mb.setMessage(msg);
		mb.open();
	}

	/**
	 * This updates information for a specific switch when viewing detailed
	 * display
	 * 
	 * @param sw
	 *            The switch we are viewing and updating
	 */
	private void liveUpdate(final Switch sw) {
		Thread thread = new Thread() {
			public void run() {
				try {
					while (currSwitch != null && sw.equals(currSwitch)) {
						Display.getDefault().asyncExec(new Runnable() {
							public void run() {
								loadSwitchData(sw);
							}
						});
						sleep(1000);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();
	}

	/**
	 * Loads all controller overview related info
	 */
	private void displayControllerInfo() {
		try {
			controllerInfo = ControllerJSON.getControllerInfo();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (controllerInfo.size() == 4) {
			lblInsertHostname.setText(controllerInfo.get(0));
			lblInsertHealthy.setText(controllerInfo.get(1));
			lblInsertJvmMemory.setText(controllerInfo.get(2));
			lblInsertModules.setText(controllerInfo.get(3));
		} else {
			displayError("Failed to display controller, no controller found!");
		}
	}

	/**
	 * Loads and displays device overview info
	 */
	private void displayDevicesData() {

		devices_table.removeAll();

		for (String[] data : DeviceToTable.deviceSummariesToTable()) {
			new TableItem(devices_table, SWT.NONE).setText(data);
		}
	}

	/**
	 * Gets all data about switches known by the controller
	 */
	public void loadSwitchesData() {

		if (switchesLoaded == false)
			switchesLoaded = true;

		// Since we are updating the data, clear the table
		switches_table.removeAll();
		// No single switch is selected so set the current switch to null
		currSwitch = null;
	}

	/**
	 * Populates the switches tree with switch DPIDs
	 */
	private void populateSwitchesTree() {
		// No single switch is selected so set the current switch to null
		currSwitch = null;
		// Clear the tree before we populate it with fresh information
		trtmSwitches.removeAll();

		// If there are switches and the tree is not disposed, populate it
		if (trtmSwitches != null) {
			for (Switch sw : FloodlightProvider.getSwitches(true)) {
				new TreeItem(trtmSwitches, SWT.NONE).setText(sw.getDpid());
			}
		}
	}

	/**
	 * This loads and displays information about all the switches
	 */
	private void displaySwitchesData() {

		loadSwitchesData();
		populateSwitchesTree();

		for (String[] data : SwitchToTable.getSwitchTableFormat(FloodlightProvider.getSwitches(true))) {
			new TableItem(switches_table, SWT.NONE).setText(data);
		}

		shell.setText("Overview for all switches");
	}

	/**
	 * Loads a specific switch's data, optimized for updating detailed view
	 * 
	 * @param sw
	 *            The switch we wish to update
	 */
	private void loadSwitchData(Switch sw) {
		
		table_ports.removeAll();
		table_flows.removeAll();
		// Set the current switch the to switch selected
		currSwitch = sw;

		sw = FloodlightProvider.getSwitch(sw.getDpid(), true);

		for (String[] data : FlowToTable.getFlowTableFormat(sw.getFlows())) {
			new TableItem(table_flows, SWT.NONE).setText(data);
		}

		for (String[] data : PortToTable.getPortTableFormat(sw.getPorts())) {
			new TableItem(table_ports, SWT.NONE).setText(data);
		}

		shell.setText("Overview for switch : " + sw.getDpid());
		lblManufacturer.setText("Manufacturer : "
				+ sw.getManufacturerDescription());
		lblSoftware.setText("Software : " + sw.getSoftwareDescription());
		lblHardware.setText("Hardware : " + sw.getHardwareDescription());
		lblSn.setText("Serial Number : " + sw.getSerialNumber());
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {

		shell = new Shell();
		shell.setSize(1200, 800);
		shell.setText("Avior");

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
		fd_composite_1.bottom = new FormAttachment(0, 710);
		composite_1.setLayoutData(fd_composite_1);

		// Create new stack layout that we reference and modify
		final StackLayout stackLayout = new StackLayout();

		// Set the layout of the composite to the stack layout we just created
		composite_1.setLayout(stackLayout);

		Composite composite_2 = new Composite(composite, SWT.NONE);
		fd_composite_1.left = new FormAttachment(0, 231);
		FormData fd_composite_2 = new FormData();
		fd_composite_2.right = new FormAttachment(composite_1, -6);
		fd_composite_2.left = new FormAttachment(0, 10);
		fd_composite_2.top = new FormAttachment(0);
		fd_composite_2.bottom = new FormAttachment(0, 710);
		composite_2.setLayoutData(fd_composite_2);

		final Tree tree = new Tree(composite_2, SWT.BORDER | SWT.NO_FOCUS
				| SWT.NONE);
		tree.setBounds(0, 36, 215, 674);
		tree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem[] selection = tree.getSelection();
				// Handler for Switches tree item
				if (selection.length != 0) {
					if (selection[0].getText().equals("Switches")) {
						stackLayout.topControl = switches_table;
						composite_1.layout();
						displaySwitchesData();
					}

					// Handler for Controller tree item
					else if (selection[0].getText().equals("Controller")) {
						stackLayout.topControl = controllerOverview;
						composite_1.layout();
						displayControllerInfo();
					}

					// Handler for Devices tree item
					else if (selection[0].getText().equals("Devices")) {
						stackLayout.topControl = devices_table;
						composite_1.layout();
						displayDevicesData();
					}

					// Handler for Static Flow Manager tree item
					else if (selection[0].getText().equals("Static Flow Manager")) {
						new StaticFlowManager();
					}

					// Handler for Flow Manager tree item
					else if (selection[0].getText().equals("Firewall")) {
						new Firewall();
					}

					// Handler for Firewall tree item
					else if (selection[0].getText().equals("Firewall")) {
						System.out.println("Feature not available yet!");
						// new FirewallManager();
					} else if (selection[0].getText().length() == 23) {
						for (Switch sw : FloodlightProvider.getSwitches(false)) {
							if (sw.getDpid().equals(selection[0].getText())) {
								loadSwitchData(sw);
								liveUpdate(sw);
								stackLayout.topControl = detailed_switch;
								composite_1.layout();
							}
						}
					}
				}
			}
		});

		Label lblM = new Label(composite_2, SWT.NONE);
		lblM.setBounds(10, 10, 107, 17);
		lblM.setText("Avior v1.3");

		TreeItem trtmTest = new TreeItem(tree, SWT.NONE);
		trtmTest.setText("Overview");

		TreeItem trtmController = new TreeItem(trtmTest, SWT.NONE);
		trtmController.setText("Controller");

		trtmSwitches = new TreeItem(trtmTest, SWT.NONE);
		trtmSwitches.setText("Switches");

		trtmDevices = new TreeItem(trtmTest, SWT.NONE);
		trtmDevices.setText("Devices");
		trtmTest.setExpanded(true);

		TreeItem trtmTools = new TreeItem(tree, SWT.NONE);
		trtmTools.setText("Tools");

		TreeItem trtmFlowManager = new TreeItem(trtmTools, SWT.NONE);
		trtmFlowManager.setText("Static Flow Manager");

		TreeItem trtmFirewall = new TreeItem(trtmTools, SWT.NONE);
		trtmFirewall.setText("Firewall");

		TreeItem trtmQos = new TreeItem(trtmTools, SWT.NONE);
		trtmQos.setText("QoS");

		TreeItem trtmVirtualnetworkfilter = new TreeItem(trtmTools, SWT.NONE);
		trtmVirtualnetworkfilter.setText("VirtualNetworkFilter");

		TreeItem trtmLoadbalancer = new TreeItem(trtmTools, SWT.NONE);
		trtmLoadbalancer.setText("LoadBalancer");
		trtmTools.setExpanded(true);

		switches_table = new Table(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
		switches_table.setHeaderVisible(true);
		switches_table.setLinesVisible(true);
		final Menu switchMenu = new Menu(switches_table);
		switches_table.setMenu(switchMenu);
		switchMenu.addMenuListener(new MenuAdapter() {
			public void menuShown(MenuEvent e) {
				// Get rid of existing menu items
				MenuItem[] items = switchMenu.getItems();
				for (int i = 0; i < items.length; i++) {
					((MenuItem) items[i]).dispose();
				}
				// Add menu items for current selection
				MenuItem newItem = new MenuItem(switchMenu, SWT.NONE);
				newItem.setText("Manage Flows");
				newItem.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						new StaticFlowManager(switches_table
								.indexOf(switches_table.getSelection()[0]));
					}
				});
			}
		});

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

		detailed_switch = new Composite(composite_1, SWT.NONE);

		table_ports = new Table(detailed_switch, SWT.BORDER
				| SWT.FULL_SELECTION);
		table_ports.setBounds(10, 117, 947, 265);
		table_ports.setHeaderVisible(true);
		table_ports.setLinesVisible(true);

		TableColumn tblclmnnum = new TableColumn(table_ports, SWT.NONE);
		tblclmnnum.setWidth(50);
		tblclmnnum.setText("#");

		TableColumn tblclmnname = new TableColumn(table_ports, SWT.NONE);
		tblclmnname.setWidth(200);
		tblclmnname.setText("Link Status");

		TableColumn tblclmnlink = new TableColumn(table_ports, SWT.NONE);
		tblclmnlink.setWidth(100);
		tblclmnlink.setText("TX Bytes");

		TableColumn tblclmnrxb = new TableColumn(table_ports, SWT.NONE);
		tblclmnrxb.setWidth(100);
		tblclmnrxb.setText("RX Bytes");

		TableColumn tblclmntxp = new TableColumn(table_ports, SWT.NONE);
		tblclmntxp.setWidth(100);
		tblclmntxp.setText("TX Pkts");

		TableColumn tblclmnrxp = new TableColumn(table_ports, SWT.NONE);
		tblclmnrxp.setWidth(100);
		tblclmnrxp.setText("RX Pkts");

		TableColumn tblclmndropped = new TableColumn(table_ports, SWT.NONE);
		tblclmndropped.setWidth(100);
		tblclmndropped.setText("Dropped");

		TableColumn tblclmnerrors = new TableColumn(table_ports, SWT.NONE);
		tblclmnerrors.setWidth(100);
		tblclmnerrors.setText("Errors");

		table_flows = new Table(detailed_switch, SWT.BORDER
				| SWT.FULL_SELECTION);
		table_flows.setBounds(10, 412, 947, 288);
		table_flows.setHeaderVisible(true);
		table_flows.setLinesVisible(true);

		TableColumn flownum = new TableColumn(table_flows, SWT.NONE);
		flownum.setWidth(30);
		flownum.setText("#");

		TableColumn flowpriority = new TableColumn(table_flows, SWT.NONE);
		flowpriority.setWidth(65);
		flowpriority.setText("Priority");

		TableColumn flowmatch = new TableColumn(table_flows, SWT.NONE);
		flowmatch.setWidth(425);
		flowmatch.setText("Match");

		TableColumn flowaction = new TableColumn(table_flows, SWT.NONE);
		flowaction.setWidth(100);
		flowaction.setText("Action");

		TableColumn flowpackets = new TableColumn(table_flows, SWT.NONE);
		flowpackets.setWidth(75);
		flowpackets.setText("Packets");

		TableColumn flowbytes = new TableColumn(table_flows, SWT.NONE);
		flowbytes.setWidth(75);
		flowbytes.setText("Bytes");

		TableColumn flowage = new TableColumn(table_flows, SWT.NONE);
		flowage.setWidth(75);
		flowage.setText("Age");

		TableColumn flowtime = new TableColumn(table_flows, SWT.NONE);
		flowtime.setWidth(75);
		flowtime.setText("Timeout");

		fd_composite_2.left = new FormAttachment(0, 10);
		fd_composite_2.top = new FormAttachment(0);
		fd_composite_2.bottom = new FormAttachment(0, 742);
		composite_2.setLayoutData(fd_composite_2);

		lblManufacturer = new Label(detailed_switch, SWT.NONE);
		lblManufacturer.setBounds(10, 0, 500, 17);
		lblManufacturer.setText("Manufacturer: ");

		lblSn = new Label(detailed_switch, SWT.NONE);
		lblSn.setBounds(10, 69, 500, 17);
		lblSn.setText("S/N: ");

		lblHardware = new Label(detailed_switch, SWT.NONE);
		lblHardware.setBounds(10, 23, 500, 17);
		lblHardware.setText("Hardware:");

		lblSoftware = new Label(detailed_switch, SWT.NONE);
		lblSoftware.setBounds(10, 46, 500, 17);
		lblSoftware.setText("Software:");

		Label lblFlows = new Label(detailed_switch, SWT.NONE);
		lblFlows.setBounds(10, 389, 70, 17);
		lblFlows.setText("Flows");

		Label lblPorts = new Label(detailed_switch, SWT.NONE);
		lblPorts.setBounds(10, 94, 70, 17);
		lblPorts.setText("Ports");
		fd_composite_2.bottom = new FormAttachment(0, 710);
		composite_2.setLayoutData(fd_composite_2);

		Button btnManageFlows = new Button(detailed_switch, SWT.NONE);
		btnManageFlows.setBounds(837, 80, 110, 29);
		btnManageFlows.setText("Manage Flows");
		btnManageFlows.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				new StaticFlowManager(FloodlightProvider.getSwitches(false).indexOf(currSwitch));
			}
		});

		switchesLoaded = false;
		stackLayout.topControl = controllerOverview;
		composite_1.layout(true);

		shell.setLayout(gl_shell);

	}
}
package view.overview;

import net.floodlightcontroller.core.IOFSwitch;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.layout.grouplayout.GroupLayout;
import org.eclipse.wb.swt.layout.grouplayout.LayoutStyle;

import view.About;
import view.detailed.FlowDisplay;

public class TablesDisplay {

	protected Shell shell;
	protected String[][] switchTableData;
	protected org.eclipse.swt.widgets.Table switchTable_table;
	protected IOFSwitch selectedSwitch;

	public TablesDisplay(IOFSwitch switchWanted) {
		selectedSwitch = switchWanted;
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

	private void displaySwitchTableData(String[][] currSwitchTableData) {
		// Clears the table prior to populating it with data
		switchTable_table.removeAll();
		// Iterate through and create TableItems for each String array
		for (String[] data : currSwitchTableData) {
			new TableItem(switchTable_table, SWT.NONE).setText(data);
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setMaximized(true);
		shell.setText("Table Information for Switch ID:"
				+ selectedSwitch.getStringId());

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

		final ToolBar toolBar = new ToolBar(shell, SWT.FLAT | SWT.RIGHT);
		toolBar.setDragDetect(false);

		final Composite composite = new Composite(shell, SWT.NONE);
		GroupLayout gl_shell = new GroupLayout(shell);
		gl_shell.setHorizontalGroup(gl_shell.createParallelGroup(
				GroupLayout.LEADING).add(
				GroupLayout.TRAILING,
				gl_shell.createSequentialGroup()
						.addContainerGap()
						.add(gl_shell
								.createParallelGroup(GroupLayout.TRAILING)
								.add(GroupLayout.LEADING, composite,
										GroupLayout.DEFAULT_SIZE, 774,
										Short.MAX_VALUE)
								.add(GroupLayout.LEADING, toolBar,
										GroupLayout.DEFAULT_SIZE, 774,
										Short.MAX_VALUE)).addContainerGap()));
		gl_shell.setVerticalGroup(gl_shell.createParallelGroup(
				GroupLayout.LEADING).add(
				gl_shell.createSequentialGroup()
						.addContainerGap()
						.add(toolBar, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.RELATED)
						.add(composite, GroupLayout.PREFERRED_SIZE, 544,
								GroupLayout.PREFERRED_SIZE)
						.addContainerGap(GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)));

		// Create new stack layout that we reference and modify
		final StackLayout stackLayout = new StackLayout();

		// Set the layout of the composite to the stack layout we just created
		composite.setLayout(stackLayout);

		switchTable_table = new Table(composite, SWT.BORDER | SWT.SINGLE);
		switchTable_table.setHeaderVisible(true);
		switchTable_table.setLinesVisible(true);
		switchTable_table.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				// Get the TableItem selection made by the user
				TableItem[] selection = switchTable_table.getSelection();
				// At the moment the 1.0 spec only really supports one table and
				// we can't specify to get flows from a certain table ID.
				// So we just pass the switch and get all the static flows from
				// that switch.
				// try{
				new FlowDisplay(selectedSwitch);
				// }
				// catch(Exception i){
				// System.out.println("Failed to create a flow display!");
				// }
			}
		});

		TableColumn tableColumn_1 = new TableColumn(switchTable_table, SWT.NONE);
		tableColumn_1.setWidth(25);
		tableColumn_1.setText("#");

		TableColumn tblclmnNewColumn_1 = new TableColumn(switchTable_table,
				SWT.NONE);
		tblclmnNewColumn_1.setWidth(100);
		tblclmnNewColumn_1.setText("Name");

		TableColumn tblclmnNewColumn_2 = new TableColumn(switchTable_table,
				SWT.NONE);
		tblclmnNewColumn_2.setWidth(50);
		tblclmnNewColumn_2.setText("ID");

		TableColumn tblclmnNewColumnn = new TableColumn(switchTable_table,
				SWT.NONE);
		tblclmnNewColumnn.setWidth(150);
		tblclmnNewColumnn.setText("Active Count");

		TableColumn tblclmnNewColumn = new TableColumn(switchTable_table,
				SWT.NONE);
		tblclmnNewColumn.setWidth(150);
		tblclmnNewColumn.setText("Lookup Count");

		TableColumn tblclmnNewColumn_3 = new TableColumn(switchTable_table,
				SWT.NONE);
		tblclmnNewColumn_3.setWidth(150);
		tblclmnNewColumn_3.setText("Matched Count");

		TableColumn tblclmnNewColumn_4 = new TableColumn(switchTable_table,
				SWT.NONE);
		tblclmnNewColumn_4.setWidth(100);
		tblclmnNewColumn_4.setText("Max Entries");

		TableColumn tblclmnNewColumn_5 = new TableColumn(switchTable_table,
				SWT.NONE);
		tblclmnNewColumn_5.setWidth(100);
		tblclmnNewColumn_5.setText("Wildcards");

		final ToolItem tltmSwitches = new ToolItem(toolBar, SWT.NONE);
		tltmSwitches.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				switchTableData = net.floodlightcontroller.avior.controller.Table
						.getTableData(selectedSwitch);
				displaySwitchTableData(switchTableData);
			}
		});
		tltmSwitches.setText("Refresh");

		// Populate the table box on load
		if (net.floodlightcontroller.avior.controller.Table
				.getTableData(selectedSwitch) != null) {
			switchTableData = net.floodlightcontroller.avior.controller.Table
					.getTableData(selectedSwitch);
			displaySwitchTableData(switchTableData);
		}

		stackLayout.topControl = switchTable_table;
		composite.layout(true);
		shell.setLayout(gl_shell);
	}

}
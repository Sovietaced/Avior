package view.detailed;

import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.avior.controller.Match;

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
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.openflow.protocol.OFFlowMod;

public class MatchDisplay {

	protected Shell shell;
	protected String[][] switchTableData;
	protected IOFSwitch selectedSwitch;
	protected OFFlowMod flow;
	protected Table table;
	protected String flowName;
	final int EDITABLECOLUMN = 1;
	protected static TableEditor editor;

	public MatchDisplay(OFFlowMod flowSelected, String name) {
		flowName = name;
		flow = flowSelected;
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

	private void displayMatch(String[][] matchData) {
		// Clears the table prior to populating it with data
		table.removeAll();
		// Iterate through and create TableItems for each String array
		for (String[] data : matchData) {
			new TableItem(table, SWT.NONE).setText(data);
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(400, 600);
		// TODO fix this
		shell.setText("Match information for flow " + flowName);
		shell.setLayout(new GridLayout(1, false));

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
		gd_composite.widthHint = 391;
		composite.setLayoutData(gd_composite);

		Button btnRefresh = new Button(composite, SWT.NONE);
		btnRefresh.setBounds(10, 0, 91, 29);
		btnRefresh.setText("Refresh");
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Refresh the match table with current data
				displayMatch(Match.getMatchdata(flow));
			}
		});

		Button btnClear = new Button(composite, SWT.NONE);
		btnClear.setBounds(122, 0, 109, 29);
		btnClear.setText("Clear/Reset");
		btnClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Fills the table with default match data
				displayMatch(Match.getDefaultData(flow));
			}
		});

		Button btnSave = new Button(composite, SWT.NONE);
		btnSave.setBounds(254, 0, 91, 29);
		btnSave.setText("Save");
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//
				FlowDisplay.currFlow.setMatch(Match.saveMatch(table.getItems()));

				// Dispose the editor do it doesn't leave a ghost table item
				if (editor.getEditor() != null) {
					editor.getEditor().dispose();
				}

				displayMatch(Match.getMatchdata(flow));
			}
		});

		Composite composite_1 = new Composite(shell, SWT.NONE);
		GridData gd_composite_1 = new GridData(SWT.LEFT, SWT.TOP, false, false,
				1, 1);
		gd_composite_1.heightHint = 530;
		composite_1.setLayoutData(gd_composite_1);

		table = new Table(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(0, 0, 381, 503);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tblclmnParameter = new TableColumn(table, SWT.NONE);
		tblclmnParameter.setWidth(250);
		tblclmnParameter.setText("Parameter");

		TableColumn tblclmnValue = new TableColumn(table, SWT.NONE);
		tblclmnValue.setWidth(100);
		tblclmnValue.setText("Value");
		GridData gd_label = new GridData(SWT.LEFT, SWT.CENTER, false, true, 1,
				2);

		editor = new TableEditor(table);
		// The editor must have the same size as the cell and must
		// not be any smaller than 50 pixels.
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		editor.minimumWidth = 50;

		table.addSelectionListener(new SelectionAdapter() {
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
				Text newEditor = new Text(table, SWT.NONE);
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

		// Try to populate the table on load
		if (flow.getMatch() != null) {
			displayMatch(Match.getMatchdata(flow));
		}
	}

}
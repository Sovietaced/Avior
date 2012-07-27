package view.tools.flowmanager;

import java.io.IOException;
import java.util.List;

import model.tools.flowmanager.Action;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.custom.TableEditor;

import controller.tools.flowmanager.push.MatchManagerPusher;
import controller.tools.flowmanager.table.MatchToTable;
import controller.util.JSONException;


public class MatchManager {

	protected Shell shell;
	protected Table table_match;
	protected Combo combo;
	protected Composite composite_3;
	String currAction, actionType;
	protected String[][] matchTableFormat;
	final int EDITABLECOLUMN = 1;
	protected static TableEditor editor;
	protected boolean delete;
	protected List<Action> actions;

	public MatchManager() {
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

	// This method will populate the table with a the selected actions
	// parameters
	protected void populateMatchTable() {

		// Clear the table of any data
		table_match.removeAll();
		// currAction = selectedAction;
		try {
			matchTableFormat = MatchToTable.getMatchTableFormat();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (matchTableFormat != null) {
			for (String[] s : matchTableFormat) {
				new TableItem(table_match, SWT.NO_FOCUS).setText(s);
			}
		}
	}

	// protected void populateNewMatchTable() {
	//
	// // Clear the tables of any data
	// table_match.removeAll();
	//
	// matchTableFormat = MatchToTable.getNewMatchTableFormat();
	//
	// if (matchTableFormat != null) {
	// for (String[] s : matchTableFormat) {
	// new TableItem(table_match, SWT.NO_FOCUS).setText(s);
	// }
	// }
	// }

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(400, 600);
		shell.setText("Match Information for Switch : "
				+ StaticFlowManager.getFlow().getSwitch() + " Flow : "
				+ StaticFlowManager.getFlow().getName());
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
				populateMatchTable();
			}
		});

		Button btnClear = new Button(composite, SWT.NONE);
		btnClear.setBounds(122, 0, 109, 29);
		btnClear.setText("Default Values");
		btnClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// populateNewMatchTable();
			}
		});

		Button btnSave = new Button(composite, SWT.NONE);
		btnSave.setBounds(254, 0, 91, 29);
		btnSave.setText("Save");
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StaticFlowManager.setMatch(MatchManagerPusher.addMatch(table_match
						.getItems()));
				// Dispose the editor do it doesn't leave a ghost table item
				if (editor.getEditor() != null) {
					editor.getEditor().dispose();
				}
			}
		});

		Composite composite_1 = new Composite(shell, SWT.NONE);
		GridData gd_composite_1 = new GridData(SWT.LEFT, SWT.TOP, false, false,
				1, 1);
		gd_composite_1.heightHint = 530;
		composite_1.setLayoutData(gd_composite_1);

		table_match = new Table(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
		table_match.setBounds(0, 0, 381, 503);
		table_match.setHeaderVisible(true);
		table_match.setLinesVisible(true);

		editor = new TableEditor(table_match);
		// The editor must have the same size as the cell and must
		// not be any smaller than 50 pixels.
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		editor.minimumWidth = 50;

		table_match.addSelectionListener(new SelectionAdapter() {
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
				Text newEditor = new Text(table_match, SWT.NONE);
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

		TableColumn tblclmnParameter = new TableColumn(table_match, SWT.NONE);
		tblclmnParameter.setWidth(200);
		tblclmnParameter.setText("Parameter");

		TableColumn tblclmnValue = new TableColumn(table_match, SWT.NONE);
		tblclmnValue.setWidth(100);
		tblclmnValue.setText("Value");
		GridData gd_label = new GridData(SWT.LEFT, SWT.CENTER, false, true, 1,
				2);

		populateMatchTable();
	}
}

package view;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;

public class Startup {

	protected static Shell shell;
	protected static Display display;
	protected static Text txtIp;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public Startup() {
		open();
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
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
		shell.dispose();
	}

	public static void connect() {
		int timeOut = 5000;
		try {
			if (InetAddress.getByName(txtIp.getText()).isReachable(timeOut)) {
					// Here we dispose this screen and launch the GUI
					shell.setVisible(false);
					new Gui(txtIp.getText());
			} else {
				MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
				mb.setText("Error!");
				mb.setMessage("Failed to reach the remote IP address. Please make sure you have entered the correct address.");
				mb.open();
			}
		} catch (UnknownHostException e1) {
			MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
			mb.setText("Error!");
			mb.setMessage("Unknown host. Please make sure you have entered the correct address.");
			mb.open();
		} catch (IOException e1) {
			MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
			mb.setText("Error!");
			mb.setMessage("Failed to reach the remote IP address");
			mb.open();
		}
	}

	private void createContents() {
		shell = new Shell();
		shell.setSize(500, 400);
		shell.setText("Avior Launch");
		shell.setLayout(null);

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

		try {
			ImageData ideaImage = new ImageData(getClass().getResourceAsStream(
					"img/floodlight.png"));
			Image floodlight = new Image(display, ideaImage);
			Label lblNewLabel_2 = new Label(shell, SWT.NONE);
			lblNewLabel_2.setBounds(20, 0, 470, 278);
			lblNewLabel_2.setImage(floodlight);
		} catch (Exception j) {
			System.out.println("Error Code I-1");
			j.printStackTrace();
		}

		Label lblEnterTheIp = new Label(shell, SWT.NONE);
		lblEnterTheIp.setBounds(37, 279, 344, 17);
		lblEnterTheIp
				.setText("Enter the IP address of the controller to begin");

		txtIp = new Text(shell, SWT.BORDER);
		txtIp.setBounds(63, 302, 120, 27);
		// Listener for the text box, if enter is pressed we attempt to
		// connect
		txtIp.addTraverseListener(new TraverseListener() {
			@Override
			public void keyTraversed(TraverseEvent e) {
				//TODO insert empty warning
				if (e.detail == SWT.TRAVERSE_RETURN && !txtIp.getText().isEmpty()) {
					connect();
				}
			}
		});

		Label lblIp = new Label(shell, SWT.NONE);
		lblIp.setBounds(40, 308, 17, 27);
		lblIp.setText("IP:");

		Button btnLaunch = new Button(shell, SWT.NONE);
		btnLaunch.setBounds(206, 302, 91, 29);
		btnLaunch.setText("Launch");
		btnLaunch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!txtIp.getText().isEmpty())
				connect();
			}
		});

	}
}
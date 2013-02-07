package view.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class DisplayMessage {

    public static void displayError(Shell shell, String msg){
        MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR
                | SWT.OK);
        mb.setText("Error!");
        mb.setMessage(msg);
        mb.open();
    }

    public static void displayStatus(Shell shell, String msg){
        MessageBox mb = new MessageBox(shell,
                SWT.ICON_WORKING | SWT.OK);
        mb.setText("Status");
        mb.setMessage(msg);
        mb.open();
    }
}

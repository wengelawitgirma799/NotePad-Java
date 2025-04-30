package notepad;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
public class Dialogbox {
	public static int showDialogbox(JFrame frmNotepad,String filename) {
		String[] buttons = { "Save", "Don't Save","Cancel" };
		//save return 0
		//don't save return 1
		//cancel return 2

	    int rc = JOptionPane.showOptionDialog(frmNotepad, "Do you want to save changes to "+filename+" ?", "Notepad",
	        JOptionPane.YES_NO_CANCEL_OPTION, 0, null, buttons, buttons[2]);
	    return rc;
	}
}

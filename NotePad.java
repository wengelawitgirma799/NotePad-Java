package notepad;

import java.awt.EventQueue;
import java.awt.FileDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import javax.swing.JMenu;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.awt.event.InputEvent;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import java.awt.event.KeyAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.event.MenuListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;
import javax.swing.event.MenuEvent;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;


public class NotePad {
	
  /**variable used in this program **/
	private JFrame frmNotepad;  //main frame
	private boolean edited =false;  // to know that file has been edited or not
	private String filename="Untitled";  //Loaded filename 
	private String fullpath ="";     //path of the file on the system
	private boolean saved =false;    //to know that file has been saved or not
	private String clipboardMessage="";  //clipboard content on the system
	private String textContent = "";   //text content in the textarea of the notepad to check file is edited or not
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NotePad window = new NotePad();
					window.frmNotepad.setVisible(true);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,"Something went wrong");
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public NotePad() {
		try {
			initialize();
		}catch(Exception e) {
			JOptionPane.showMessageDialog(this.frmNotepad,"Something went wrong");
		}
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.frmNotepad = new JFrame();
		frmNotepad.setIconImage(Toolkit.getDefaultToolkit().getImage(NotePad.class.getResource("/notepad/noteicon.png")));
		this.frmNotepad.setTitle(this.filename+" - Notepad");
		this.frmNotepad.setBackground(SystemColor.inactiveCaption);
		this.frmNotepad.setBounds(250, 150, 700, 468);
		
		try 
        {
           UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
		
		//main panel
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(255, 255, 255), 4));
		panel.setBackground(SystemColor.menu);
		this.frmNotepad.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout());
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(null);
		scrollPane.setViewportBorder(null);
		panel.add(scrollPane, BorderLayout.CENTER);
		
		//menu bar
		JMenuBar menuBar = new JMenuBar();
		menuBar.setForeground(SystemColor.windowBorder);
		menuBar.setBackground(Color.WHITE);
		menuBar.setMargin(new Insets(0, 0, 2, 0));
		panel.add(menuBar, BorderLayout.NORTH);
		
		//text area
		JTextArea editorPane = new JTextArea();
		editorPane.setTabSize(8);
		editorPane.setBackground(Color.WHITE);
		editorPane.setForeground(Color.BLACK);
		editorPane.setFont(new Font("Consolas", Font.PLAIN, 15));
		editorPane.setWrapStyleWord(false);
		editorPane.setLineWrap(false);
		scrollPane.setViewportView(editorPane);
		
		
		//undo and redo manager for textarea : editorPane
		UndoManager manager = new UndoManager();
		editorPane.getDocument().addUndoableEditListener(new UndoableEditListener() {
			public void undoableEditHappened(UndoableEditEvent e) {
				manager.addEdit(e.getEdit());
			}
		});
		
		
		//file menu
		JMenu fileMenu = new JMenu("File");
		fileMenu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		menuBar.add(fileMenu);
		
		//new option in file menu
		JMenuItem newFile = new JMenuItem("New");
		newFile.setForeground(Color.BLACK);
		newFile.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		fileMenu.add(newFile);
		
		//new window option in file menu
		JMenuItem newWindowFile = new JMenuItem("New Window          ");
		newWindowFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//open the same new window
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							NotePad window2 = new NotePad();
							window2.frmNotepad.setVisible(true);
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null,"Something went wrong");
						}
					}
				});
			}
		});
		newWindowFile.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		newWindowFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		fileMenu.add(newWindowFile);
		
		//open option in file menu
		JMenuItem openFile = new JMenuItem("Open...");
		openFile.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		fileMenu.add(openFile);
		
		//save file option in file menu
		JMenuItem saveFile = new JMenuItem("Save");
		saveFile.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		fileMenu.add(saveFile);
		
		//save as option in file menu
		JMenuItem saveAsFile = new JMenuItem("Save As...");
		saveAsFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileDialog fileDialog = new FileDialog(frmNotepad, "Save As", FileDialog.SAVE);
				fileDialog.setFile("Newfile");
				fileDialog.setDirectory("C:\\");				
				fileDialog.setVisible(true);
				if(fileDialog.getFile() != null) {
					filename = fileDialog.getFile();
					fullpath = fileDialog.getDirectory()+fileDialog.getFile();
					if(!FileReadWrite.writefile(fullpath, editorPane.getText())){
						filename="Untitled";
						fullpath="";
					}
					frmNotepad.setTitle(filename+" - Notepad");
					saved = true;
					textContent = editorPane.getText();
				}
			}
		});
		saveAsFile.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		saveAsFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		fileMenu.add(saveAsFile);
		
		//print option in file menu
		JMenuItem printFile = new JMenuItem("Print...");
		printFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					boolean complete = editorPane.print();
					if(complete) {
						JOptionPane.showMessageDialog(frmNotepad, "Done Printing","Information",JOptionPane.INFORMATION_MESSAGE);
					}else {
						
					}
				}catch(PrinterException exp) {
					JOptionPane.showMessageDialog(frmNotepad, "Something went wrong!","Printer",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		printFile.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		printFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
		fileMenu.add(printFile);
		
		//exit option in file menu
		JMenuItem exitFile = new JMenuItem("Exit");
		exitFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//if file is not saved and content is not  empty
				if(!saved && !(editorPane.getText()).equals("")) {
					//pop up dialog box
					int rc = Dialogbox.showDialogbox(frmNotepad,filename);
					if(rc == 2) {  
						return;
					}else if(rc == 0) {
						if(fullpath.equals("") && filename.equals("Untitled")) {
							FileDialog fileDialog = new FileDialog(frmNotepad, "Save File", FileDialog.SAVE);
							fileDialog.setFile("Newfile.txt");
							fileDialog.setDirectory("C:\\");				
							fileDialog.setVisible(true);
							if(fileDialog.getFile() != null) {
								filename = fileDialog.getFile();
								fullpath = fileDialog.getDirectory()+fileDialog.getFile();
								if(!FileReadWrite.writefile(fullpath, editorPane.getText())){
									filename="Untitled";
									fullpath="";
								}
								frmNotepad.setTitle(filename+" - Notepad");
								saved = true;
								frmNotepad.dispose();
							}else {
								return;
							}
						}else if(!filename.equals("Untitled")){
							FileReadWrite.writefile(fullpath, editorPane.getText());
							frmNotepad.setTitle(filename+" - Notepad");
							saved = true;
							frmNotepad.dispose();
						}
					}else if(rc == 1) {
						frmNotepad.dispose();
					}else {
						return;
					}
				}
				//otherwise
				frmNotepad.dispose();
			}
		});
		exitFile.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		fileMenu.add(exitFile);
		
		//Edit menu
		JMenu editMenu = new JMenu("Edit");
		editMenu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		menuBar.add(editMenu);
		
		//undo option in edit menu
		JMenuItem undoEdit = new JMenuItem("Undo");
		undoEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(manager.canUndo()) {
					try {
						manager.undo();
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(frmNotepad,"Something went wrong!");
					}
					edited =true;
					saved= false;
					frmNotepad.setTitle("*"+filename+" - Notepad");
				}
			}
		});
		undoEdit.setEnabled(false);
		undoEdit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
		undoEdit.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		editMenu.add(undoEdit);
		
		//redo option in edit menu
		JMenuItem redoEdit = new JMenuItem("Redo");
		redoEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(manager.canRedo()) {
					try {
						manager.redo();
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(frmNotepad,"Something went wrong!");
					}
					edited =true;
					saved= false;
					frmNotepad.setTitle("*"+filename+" - Notepad");
				}
			}
		});
		redoEdit.setEnabled(false);
		redoEdit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK));
		redoEdit.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		editMenu.add(redoEdit);
		
		//cut option in edit menu
		JMenuItem cutEdit = new JMenuItem("Cut");
		cutEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editorPane.cut();
				edited =true;
				saved =false;
				frmNotepad.setTitle("*"+filename+" - Notepad");
			}
		});
		cutEdit.setEnabled(false);
		cutEdit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		cutEdit.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		editMenu.add(cutEdit);
		
		//copy option in edit menu
		JMenuItem copyEdit = new JMenuItem("Copy");
		copyEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editorPane.copy();
			}
		});
		copyEdit.setEnabled(false);
		copyEdit.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		copyEdit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		editMenu.add(copyEdit);
		
		//paste option in edit menu
		JMenuItem pasteEdit = new JMenuItem("Paste");
		pasteEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editorPane.replaceSelection("");
				editorPane.insert(clipboardMessage, editorPane.getCaretPosition());
				edited =true;
				saved =false;
				frmNotepad.setTitle("*"+filename+" - Notepad");
			}
		});
		pasteEdit.setEnabled(false);
		pasteEdit.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		pasteEdit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
		editMenu.add(pasteEdit);
		
		//delete option in edit menu
		JMenuItem deleteEdit = new JMenuItem("Delete");
		deleteEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editorPane.replaceSelection("");
				edited =true;
				saved =false;
				frmNotepad.setTitle("*"+filename+" - Notepad");
			}
		});
		deleteEdit.setEnabled(false);
		deleteEdit.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		deleteEdit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		editMenu.add(deleteEdit);
		
		//find option in edit menu
		JMenuItem findEdit = new JMenuItem("Find...");
		findEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new FindDialog(frmNotepad,editorPane);
			}
		});
		findEdit.setEnabled(false);
		findEdit.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		findEdit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
		editMenu.add(findEdit);
		
		//replace option in edit menu
		JMenuItem replaceEdit = new JMenuItem("Replace...");
		replaceEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ReplaceDialog(frmNotepad,editorPane);
			}
		});
		replaceEdit.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		replaceEdit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK));
		editMenu.add(replaceEdit);
		
		//go to option in edit menu
		JMenuItem goToEdit = new JMenuItem("Go To...");
		goToEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 try {
					 //getLineStartOffset -col no
					 //getLineOfOffset -row no
					 int lines =editorPane.getLineCount();
					 int offset = editorPane.getCaretPosition();
			         int line = editorPane.getLineOfOffset(offset);
					 String inputValue = JOptionPane.showInputDialog(frmNotepad,"Go to Line Number :",(line+1));
					 if(inputValue != null) {
						 int inputLine = Integer.parseInt(inputValue);
						 if(inputLine <= lines) {
							 int i=editorPane.getLineStartOffset(inputLine-1);
							 editorPane.setCaretPosition(i);
						 }else {
							 JOptionPane.showMessageDialog(frmNotepad, "The line number is beyond the total number of lines.", "Notepad - Go To Line",JOptionPane.PLAIN_MESSAGE, null);
						 }
					 }
				 }catch(Exception exp) {
					 JOptionPane.showMessageDialog(frmNotepad, "Somthing went wrong!");
				 }
			}
		});
		goToEdit.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		goToEdit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK));
		editMenu.add(goToEdit);
		
		//select all option in edit menu
		JMenuItem selectAllEdit = new JMenuItem("Select All");
		selectAllEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editorPane.selectAll();
			}
		});
		selectAllEdit.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		selectAllEdit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
		editMenu.add(selectAllEdit);
		
		//time and date option in edit menu
		JMenuItem timeDateEdit = new JMenuItem("Time/Date                  ");
		timeDateEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				edited =true;
				saved =false;
				DateFormat dateFormat = new SimpleDateFormat("hh:mm aa dd-MM-yyyy");
		    	String dateString = dateFormat.format(new Date()).toString();
				frmNotepad.setTitle("*"+filename+"Notepad");
				editorPane.replaceSelection("");
				editorPane.insert(dateString, editorPane.getCaretPosition());
			}
		});
		timeDateEdit.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		timeDateEdit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
		editMenu.add(timeDateEdit);
		
		//format menu
		JMenu formatMenu = new JMenu("Format");
		formatMenu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		menuBar.add(formatMenu);
		
		//font option in format menu
		JMenuItem fontFormat = new JMenuItem("Font...                          ");
		fontFormat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new FontChooser(frmNotepad, editorPane,editorPane.getFont());
			}
		});
		
		//word wrap option in format menu
		JMenuItem wordWrapFormat = new JMenuItem("Word Wrap               ");
		wordWrapFormat.setSelected(false);
		wordWrapFormat.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		wordWrapFormat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(wordWrapFormat.isSelected()){
					editorPane.setWrapStyleWord(false);
					editorPane.setLineWrap(false);
					wordWrapFormat.setSelected(false);
					wordWrapFormat.setForeground(Color.BLACK);
				}else {
					editorPane.setWrapStyleWord(true);
					editorPane.setLineWrap(true);
					wordWrapFormat.setSelected(true);
					wordWrapFormat.setForeground(Color.BLUE);
				}
			}
		});
		formatMenu.add(wordWrapFormat);
		fontFormat.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		formatMenu.add(fontFormat);
		
		//View menu 
		JMenu viewMenu = new JMenu("View");
		viewMenu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		menuBar.add(viewMenu);
		
		/** zoom in zoom out and default zoom buttons with zoom menu**/
		
//		JMenu zoomMenuView = new JMenu("Zoom               ");
//		zoomMenuView.setFont(new Font("Segoe UI", Font.PLAIN, 12));
//		viewMenu.add(zoomMenuView);
//		
//		JMenuItem zoomInZoom = new JMenuItem("Zoom In");
//		zoomInZoom.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//			}
//		});
//		zoomInZoom.setFont(new Font("Segoe UI", Font.PLAIN, 12));
//		zoomInZoom.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.CTRL_MASK));
//		zoomMenuView.add(zoomInZoom);
//		
//		JMenuItem zoomOutZoom = new JMenuItem("Zoom Out");
//		zoomOutZoom.setFont(new Font("Segoe UI", Font.PLAIN, 12));
//		zoomOutZoom.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.CTRL_MASK));
//		zoomMenuView.add(zoomOutZoom);
//		
//		JMenuItem restoreZoom = new JMenuItem("Restore Default Zoom         ");
//		restoreZoom.setFont(new Font("Segoe UI", Font.PLAIN, 12));
//		restoreZoom.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0, InputEvent.CTRL_MASK));
//		zoomMenuView.add(restoreZoom);
		
		/****     ------------------------------- **/
		
		//status bar option in view menu
		JMenuItem statusBarView = new JMenuItem("Status Bar");
		statusBarView.setSelected(false);
		statusBarView.setHorizontalAlignment(SwingConstants.LEFT);
		statusBarView.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		viewMenu.add(statusBarView);
		
		/**        creating status bar           **/
		//status bar panel
		JPanel statusBar = new JPanel();
		//action listner for status bar hide/show
		statusBarView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(statusBarView.isSelected()) {
					statusBarView.setSelected(false);
					statusBar.setVisible(false);
					statusBarView.setForeground(Color.BLACK);
				}else {
					statusBarView.setSelected(true);
					statusBar.setVisible(true);
					statusBarView.setForeground(Color.BLUE);
				}
			}
		});		
		panel.add(statusBar, BorderLayout.SOUTH);
		statusBar.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 2));
		statusBar.setVisible(false);
		
		//panel components
		//cursor position label
		JLabel cusorposition = new JLabel("");
		cusorposition.setFont(new Font("Tahoma", Font.PLAIN, 13));
		cusorposition.setBorder(new EmptyBorder(0, 8, 0, 80));
		statusBar.add(cusorposition);
		
		//zoom level label
		JLabel zoomLevel = new JLabel("100% ");
		zoomLevel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		zoomLevel.setBorder(new EmptyBorder(0, 8, 0, 50));
		statusBar.add(zoomLevel);
		
		//text encoding labels
		JLabel text = new JLabel("UTF - 8");
		text.setHorizontalAlignment(SwingConstants.LEFT);
		text.setFont(new Font("Cambria", Font.PLAIN, 12));
		text.setBorder(new EmptyBorder(0, 8, 0, 80));
		statusBar.add(text);
		
		
		
		/***          action listners  main                   ***/
		
		//editor pane action listners
		editorPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				//checking if edited or not
				String newTextContent = editorPane.getText();
				if(!newTextContent.equals(textContent)) {
					edited = true;
					saved = false;
					frmNotepad.setTitle("*"+filename+" - Notepad");
				}
				
				//check for paste button
				try {
					Clipboard c=Toolkit.getDefaultToolkit().getSystemClipboard();
					String clipboardText = (String)c.getData(DataFlavor.stringFlavor);
					if(clipboardText.equals("")) {
					   pasteEdit.setEnabled(false);
					   clipboardMessage = "";
				    }else {
					   pasteEdit.setEnabled(true);
					   clipboardMessage = clipboardText;
				    }
				}catch(Exception exp) {
				}
				
				//check for copy ,cut ,delete buttons
				if(editorPane.getSelectedText()!= null) {
					copyEdit.setEnabled(true);
					cutEdit.setEnabled(true);
					deleteEdit.setEnabled(true);
				}else {
					copyEdit.setEnabled(false);
					cutEdit.setEnabled(false);
					deleteEdit.setEnabled(false);
				}
				//check for undo and redo buttons
				if(manager.canUndo()) {
					undoEdit.setEnabled(true);
				}else {
					undoEdit.setEnabled(false);
				}
				if(manager.canRedo()) {
					redoEdit.setEnabled(true);
				}else {
					redoEdit.setEnabled(false);
				}
				//check for find button
				if(editorPane.getText().equals("")) {
					findEdit.setEnabled(false);
				}else {
					findEdit.setEnabled(true);
				}
			}
			@Override
			public void keyPressed(KeyEvent e) {
				String newTextContent = editorPane.getText();
				if(!newTextContent.equals(textContent)) {
					edited = true;
					saved = false;
					frmNotepad.setTitle("*"+filename+" - Notepad");
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				String newTextContent = editorPane.getText();
				if(!newTextContent.equals(textContent)) {
					edited = true;
					saved = false;
					frmNotepad.setTitle("*"+filename+" - Notepad");
				}
			}
		});
		
	//for updating status bar with line no and col no
		editorPane.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				try {
					 int offset = editorPane.getCaretPosition();
			         int row = editorPane.getLineOfOffset(offset);
			         int col = editorPane.getLineStartOffset(row);
			         row++;
			         col=offset-col +1;
			         cusorposition.setText("Ln "+row+", Col "+col);
				 }catch(Exception exp) {
					 
				 }
			}			 
		});
		
		editorPane.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				try {
					 int offset = editorPane.getCaretPosition();
			         int row = editorPane.getLineOfOffset(offset);
			         int col = editorPane.getLineStartOffset(row);
			         row++;
			         col=offset-col +1;
			         cusorposition.setText("Ln "+row+", Col "+col);
				 }catch(Exception exp) {
					 
				 }
			}
		});
		
		/** when new button is clicked **/
		//newbutton action listner
		newFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//if file is not saved and not empty
				if(!saved && !(editorPane.getText()).equals("")) {
					//pop up dialog box
					int rc = Dialogbox.showDialogbox(frmNotepad,filename);
					if(rc == 2) {  
						return;
					}else if(rc == 0) {
						if(fullpath.equals("") && filename.equals("Untitled")) {
							FileDialog fileDialog = new FileDialog(frmNotepad, "Save File", FileDialog.SAVE);
							fileDialog.setFile("Newfile.txt");
							fileDialog.setDirectory("C:\\");				
							fileDialog.setVisible(true);
							if(fileDialog.getFile() != null) {
								filename = fileDialog.getFile();
								fullpath = fileDialog.getDirectory()+fileDialog.getFile();
								if(!FileReadWrite.writefile(fullpath, editorPane.getText())){
									filename="Untitled";
									fullpath="";
								}
								frmNotepad.setTitle(filename+" - Notepad");
								saved = true;
							}else {
								return;
							}
						}else if(!filename.equals("Untitled")){
							FileReadWrite.writefile(fullpath, editorPane.getText());
							frmNotepad.setTitle(filename+" - Notepad");
							saved = true;
						}
					}else if(rc == 1){
						
					}else {
						return;
					}
				}
				editorPane.setText("");
				saved = false;
				edited=false;
				filename = "Untitled";
				fullpath="";
				frmNotepad.setTitle(filename+" - Notepad");
				editorPane.setFont(new Font("Consolas", Font.PLAIN, 15));
				textContent = "";//editorPane.getText();
			}
		});
		
		//open button action listner
		openFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!saved && edited && !(editorPane.getText()).equals("")) {
					int rc = Dialogbox.showDialogbox(frmNotepad,filename);
					if(rc == 2) {  
						return;
					}else if(rc == 0) {
						if(fullpath.equals("") && filename.equals("Untitled")) {
							FileDialog fileDialog = new FileDialog(frmNotepad, "Save File", FileDialog.SAVE);
							fileDialog.setFile("Newfile.txt");
							fileDialog.setDirectory("C:\\");				
							fileDialog.setVisible(true);
							if(fileDialog.getFile() != null) {
								filename = fileDialog.getFile();
								fullpath = fileDialog.getDirectory()+fileDialog.getFile();
								if(!FileReadWrite.writefile(fullpath, editorPane.getText())){
									filename="Untitled";
									fullpath="";
								}
								frmNotepad.setTitle(filename+" - Notepad");
								saved = true;
							}else {
								return;
							}
							
						}else if(!filename.equals("Untitled")){
							FileReadWrite.writefile(fullpath, editorPane.getText());
							frmNotepad.setTitle(filename+" - Notepad");
							saved = true;
						}
					}else if(rc == 1){
						
					}else {
						return;
					}
				}
				FileDialog fileDialog = new FileDialog(frmNotepad, "Choose a file", FileDialog.LOAD);
				fileDialog.setDirectory("C:\\");				
				fileDialog.setVisible(true);
				if(fileDialog.getFile() != null) {
					filename = fileDialog.getFile();
					fullpath = fileDialog.getDirectory()+fileDialog.getFile();
					String read = FileReadWrite.readfile(fullpath);
					if(!read.equals("")) {
						editorPane.setText(read);
					}else {
						filename="Untitled";
						fullpath="";
					}
					frmNotepad.setTitle(filename+" - Notepad");
					saved = true;
					edited =false;
					editorPane.setCaretPosition(0);
				}
				textContent = editorPane.getText();
			}
		});
		
		//save button action listner
		saveFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!saved && filename.equals("Untitled")) {
					FileDialog fileDialog = new FileDialog(frmNotepad, "Save File", FileDialog.SAVE);
					fileDialog.setFile("Newfile.txt");
					fileDialog.setDirectory("C:\\");				
					fileDialog.setVisible(true);
					if(fileDialog.getFile() != null) {
						filename = fileDialog.getFile();
						fullpath = fileDialog.getDirectory()+fileDialog.getFile();
						if(!FileReadWrite.writefile(fullpath, editorPane.getText())){
							filename="Untitled";
							fullpath="";
						}
						frmNotepad.setTitle(filename+" - Notepad");
						saved = true;
						textContent = editorPane.getText();
					}
				}else if(!saved && !filename.equals("Untitled")){
					FileReadWrite.writefile(fullpath, editorPane.getText());
					frmNotepad.setTitle(filename+" - Notepad");
					saved = true;
					textContent = editorPane.getText();
				}
			}
		});
		
		//when  edit menu  is clicked   : to show and hide buttons
		editMenu.addMenuListener(new MenuListener() {
			public void menuCanceled(MenuEvent e) {
			}
			public void menuDeselected(MenuEvent e) {
			}
			public void menuSelected(MenuEvent e) {
				//check for paste button
				try {
					Clipboard c=Toolkit.getDefaultToolkit().getSystemClipboard();
					String clipboardText = (String)c.getData(DataFlavor.stringFlavor);
					if(clipboardText.equals("")) {
					   pasteEdit.setEnabled(false);
					   clipboardMessage = "";
				    }else {
					   pasteEdit.setEnabled(true);
					   clipboardMessage = clipboardText;
				    }
				}catch(Exception exp) {
//					JOptionPane.showMessageDialog(frmNotepad,"Something went wrong!");
				}
				//check for copy ,cut ,delete buttons
				if(editorPane.getSelectedText()!= null) {
					copyEdit.setEnabled(true);
					cutEdit.setEnabled(true);
					deleteEdit.setEnabled(true);
				}else {
					copyEdit.setEnabled(false);
					cutEdit.setEnabled(false);
					deleteEdit.setEnabled(false);
				}
				//check for undo and redo buttons
				if(manager.canUndo()) {
					undoEdit.setEnabled(true);
				}else {
					undoEdit.setEnabled(false);
				}
				if(manager.canRedo()) {
					redoEdit.setEnabled(true);
				}else {
					redoEdit.setEnabled(false);
				}
				//check for find button
				if(editorPane.getText().equals("")) {
					findEdit.setEnabled(false);
				}else {
					findEdit.setEnabled(true);
				}
				
				
			}
		});
		
		//frame default exit button   override
		frmNotepad.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmNotepad.addWindowListener(new WindowAdapter(){
			@Override
			  public void windowClosing(WindowEvent we){
				  if(!saved && !(editorPane.getText()).equals("")) {
					//pop up dialog box
					int rc = Dialogbox.showDialogbox(frmNotepad,filename);
					if(rc == 2) {  
						return;
					}else if(rc == 0) {
						if(fullpath.equals("") && filename.equals("Untitled")) {
							FileDialog fileDialog = new FileDialog(frmNotepad, "Save File", FileDialog.SAVE);
							fileDialog.setFile("Newfile.txt");
							fileDialog.setDirectory("C:\\");				
							fileDialog.setVisible(true);
							if(fileDialog.getFile() != null) {
								filename = fileDialog.getFile();
								fullpath = fileDialog.getDirectory()+fileDialog.getFile();
								if(!FileReadWrite.writefile(fullpath, editorPane.getText())){
									filename="Untitled";
									fullpath="";
								}
								frmNotepad.setTitle(filename+" - Notepad");
								saved = true;
								frmNotepad.dispose();
							}else {
								return;
							}
						}else if(!filename.equals("Untitled")){
							FileReadWrite.writefile(fullpath, editorPane.getText());
							frmNotepad.setTitle(filename+" - Notepad");
							saved = true;
							frmNotepad.dispose();
						}
					}else if(rc ==1){
						frmNotepad.dispose();
					}else {
						return;
					}
				}else {
					frmNotepad.dispose();
				}	
			 }
		});	
	}
}

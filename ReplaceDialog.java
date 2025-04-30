package notepad;


/** this class is for find option in edit menu **/

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import java.awt.Window.Type;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class ReplaceDialog{

	private JDialog replaceDialog;

	/**
	 * Create the dialog.
	 */
	public ReplaceDialog(JFrame frame,JTextArea editorPane) {
		replaceDialog = new JDialog(frame);
		replaceDialog.setResizable(false);
		replaceDialog.setType(Type.UTILITY);
		replaceDialog.setTitle("Replace");
		replaceDialog.setBounds(100, 100, 421, 247);
		replaceDialog.setLocationRelativeTo(frame);
		replaceDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		replaceDialog.getContentPane().setLayout(null);
		
		try 
        {
           UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
		
		//all j components
		JPanel contentPanel = new JPanel();
		JTextField findField = new JTextField();
		JTextField replaceField = new JTextField();
		JButton findNextButton = new JButton("Find Next");
		JCheckBox wrapAroundButton = new JCheckBox("Wrap around");
		JButton replaceAllButton = new JButton("Replace All");
		JButton replaceButton = new JButton("Replace");
		JButton cancelButton = new JButton("Cancel");
		
		contentPanel.setBounds(0, 0, 405, 208);
		replaceDialog.getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		
		
		
		//find field
		findField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(findField.getText() != null) {
					findNextButton.setEnabled(true);
					replaceButton.setEnabled(true);
					replaceAllButton.setEnabled(true);
				}else {
					findNextButton.setEnabled(false);
					replaceButton.setEnabled(false);
					replaceAllButton.setEnabled(false);
				}
			}
		});
		findField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		findField.setBounds(82, 21, 220, 30);
		contentPanel.add(findField);
		findField.setColumns(10);
		
		JLabel findFieldLabel = new JLabel("Find what:");
		findFieldLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
		findFieldLabel.setLabelFor(findField);
		findFieldLabel.setBounds(10, 20, 62, 30);
		contentPanel.add(findFieldLabel);
		
		
		
		//replace field
		replaceField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		replaceField.setColumns(10);
		replaceField.setBounds(82, 55, 220, 30);
		contentPanel.add(replaceField);
				
		JLabel replaceFieldLabel = new JLabel("Replace with:");
		replaceFieldLabel.setLabelFor(replaceField);
		replaceFieldLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
		replaceFieldLabel.setBounds(10, 55, 72, 30);
		contentPanel.add(replaceFieldLabel);

		
		//find next button
		findNextButton.setEnabled(false);
		findNextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int currentposition = editorPane.getCaretPosition();
				String pattern = findField.getText();
				String editorText= editorPane.getText();

				Pattern p = Pattern.compile(pattern);
				//when wrap around button is selected :: round again and again if pattern is there in the editor text
				if(wrapAroundButton.isSelected()) {
						try {
							Matcher m = p.matcher(editorText);
							if(!m.find()) {
								JOptionPane.showMessageDialog(replaceDialog, "Cannot find '"+pattern+"'","Notepad",JOptionPane.INFORMATION_MESSAGE);							
							}else {
								m=p.matcher(editorText);
								if(m.find(currentposition)) {
									editorPane.setCaretPosition(m.end());
									editorPane.select(m.start(),m.end());
								}else {
									//if not find again search from starting position  (0)
									m=p.matcher(editorText);
									if(m.find(0)) {
										editorPane.setCaretPosition(m.end());
										editorPane.select(m.start(),m.end());
									}
								}
							}	
						}catch(Exception exp) {
							JOptionPane.showMessageDialog(replaceDialog, "Error :find");
						}
			    //when wrap button is not selected
				}else {
					try {
						Matcher m = p.matcher(editorText);
						if(m.find(currentposition)) {
							editorPane.setCaretPosition(m.end());
							editorPane.select(m.start(),m.end());
						}else {
							JOptionPane.showMessageDialog(replaceDialog, "Cannot find '"+pattern+"'","Notepad",JOptionPane.INFORMATION_MESSAGE);							
						}	
					}catch(Exception exp) {
						JOptionPane.showMessageDialog(replaceDialog, "Error :find");
					}
				}
			}
		});
		findNextButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
		findNextButton.setBounds(306, 20, 89, 30);
		replaceDialog.getRootPane().setDefaultButton(findNextButton);
		contentPanel.add(findNextButton);
		
		//cancel button
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				replaceDialog.dispose();
			}
		});
		cancelButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
		cancelButton.setBounds(306, 125, 89, 30);
		contentPanel.add(cancelButton);
		
		//wordwrap button
		wrapAroundButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		wrapAroundButton.setBounds(6, 178, 117, 23);
		contentPanel.add(wrapAroundButton);
		
		//replace button
		replaceButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
		replaceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int currentposition;
				String pattern = findField.getText();
				String editorText=editorPane.getText();
				String replaceText = replaceField.getText();

				Pattern p = Pattern.compile(pattern);
				//when wrap around button is selected :: round again and again if pattern is there in the editor text
				if(wrapAroundButton.isSelected()) {
						try {
							Matcher m = p.matcher(editorText);
							if(!m.find()) {
								JOptionPane.showMessageDialog(replaceDialog, "Cannot find '"+pattern+"'","Notepad",JOptionPane.INFORMATION_MESSAGE);							
							}else {
								//if selected replace it otherwise select next
								if(editorPane.getSelectedText() != null) {
									editorPane.replaceSelection(replaceText);
								}
								editorText = editorPane.getText();
								currentposition = editorPane.getCaretPosition();
								
								m=p.matcher(editorText);
								if(m.find(currentposition)) {
									editorPane.setCaretPosition(m.end());
									editorPane.select(m.start(),m.end());
								}else {
									//if not find again search from starting position  (0)
									m=p.matcher(editorText);
									if(m.find(0)) {
										editorPane.setCaretPosition(m.end());
										editorPane.select(m.start(),m.end());
									}
								}
							}	
						}catch(Exception exp) {
							JOptionPane.showMessageDialog(replaceDialog, "Error :find");
						}
			    //when wrap button is not selected
				}else {
					try {
						//if selected replace it otherwise select next
						if(editorPane.getSelectedText() != null) {
							editorPane.replaceSelection(replaceText);
						}
						editorText = editorPane.getText();
						currentposition = editorPane.getCaretPosition();
						Matcher m = p.matcher(editorText);
						if(m.find(currentposition)) {
							editorPane.setCaretPosition(m.end());
							editorPane.select(m.start(),m.end());
						}else {
							JOptionPane.showMessageDialog(replaceDialog, "Cannot find '"+pattern+"'","Notepad",JOptionPane.INFORMATION_MESSAGE);							
						}	
					}catch(Exception exp) {
						JOptionPane.showMessageDialog(replaceDialog, "Error :find");
					}
				}

			}
		});
		replaceButton.setEnabled(false);
		replaceButton.setBounds(306, 55, 89, 30);
		contentPanel.add(replaceButton);
		
		//replace all button
		replaceAllButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
		replaceAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String pattern = findField.getText();
				String repalceText = replaceField.getText();
				Pattern p = Pattern.compile(pattern);
				Matcher m = p.matcher(editorPane.getText());
				//start from starting and replace all matching with replace Text
				int currentposition = 0;
				while(m.find(currentposition)) {
					editorPane.select(m.start(),m.end());
					editorPane.replaceSelection(repalceText);
					//get position and matcher of new content
					currentposition = editorPane.getCaretPosition();
					m = p.matcher(editorPane.getText());
				}
				//set caret position at start
				editorPane.setCaretPosition(0);
			}
		});
		replaceAllButton.setEnabled(false);
		replaceAllButton.setBounds(306, 90, 89, 30);
		contentPanel.add(replaceAllButton);
		
		
		replaceDialog.setVisible(true);
	}
}

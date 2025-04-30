package notepad;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.border.EmptyBorder;
import java.awt.Font;

import javax.swing.*;
import java.awt.SystemColor;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import javax.swing.event.ListSelectionListener;

import javax.swing.event.ListSelectionEvent;

public class FontChooser {
	//main dialog 
	private JDialog dialog;
	//list of fonts sytles, and sizes
	private  String[] fonts;
	private  String[] sizes = {"8", "9", "10", "11", "12","13","14","15", "16","17", "18","19", "20", "22", "24", "26", "28","30","32","34", "36", "38","40","42","44","46","48", "72"};
	private  String[] styles = {"Plain", "Bold", "Italic", "Bold Italic"};
	
	//variables for setting new font
	private   String font = "Arial";
	private   String size = "10";
	private   int style=Font.PLAIN;   // constant integer form to set new font
	private   String textStyle = "Plain";  //style text form to show on the dialog box as text
	
	/**
	 * Launch the application.
	 */
	//required methods
	private  void getAllfonts() {
		fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	}
	/**
	 * Create the dialog.
	 */
	public  FontChooser(JFrame frame,JTextArea editorPane, Font oldfont) {
		//get all fonts in fonts
		getAllfonts();
		
		//get old font value of editor pane to all font variables
		style = oldfont.getStyle();
		if(oldfont.getStyle() == Font.PLAIN) {
			textStyle = "Plain";
		}else if(oldfont.getStyle() == Font.BOLD) {
			textStyle = "Bold";
		}else if(oldfont.getStyle() == Font.ITALIC) {
			textStyle ="Italic";
		}else {
			textStyle = "Bold Italic";
		}
		size = Integer.toString(oldfont.getSize());
		font = oldfont.getFamily();
		

		//dialog 
		dialog = new JDialog(frame,true);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setTitle("Font");
		dialog.setType(Type.UTILITY);
		dialog.setBounds(200, 100, 439, 486);
		dialog.setLocationRelativeTo(frame);
		dialog.getContentPane().setLayout(new BorderLayout());
		dialog.setResizable(false);
		
		
		//content panel of the dialog
		JPanel contentPanel = new JPanel();
		dialog.getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(null);
		try 
        {
           UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
		
		//font label
		JLabel fontLabel = new JLabel("Font :");
		fontLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		fontLabel.setBounds(24, 41, 48, 14);
		contentPanel.add(fontLabel);
		
		//style label
		JLabel styleLabel = new JLabel("Font Style :");
		styleLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		styleLabel.setBounds(191, 41, 66, 14);
		contentPanel.add(styleLabel);
		
		//size label
		JLabel sizeLabel = new JLabel("Size :");
		sizeLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		sizeLabel.setBounds(330, 42, 48, 14);
		contentPanel.add(sizeLabel);
		
		//Sample text (to show the sample of applied new font ) in the font dialog box
		JTextField sampleText = new JTextField();
		sampleText.setFont(new Font(font,style,Integer.parseInt(size)));
		sampleText.setEditable(false);
		sampleText.setBackground(SystemColor.control);
		sampleText.setHorizontalAlignment(SwingConstants.CENTER);
		sampleText.setText("AaBbYyZz");
		sampleText.setBounds(191, 277, 205, 80);
		contentPanel.add(sampleText);
		sampleText.setColumns(10);
		
		//label for sample text
		JLabel sampleLabel = new JLabel("Sample : ");
		sampleLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		sampleLabel.setLabelFor(sampleLabel);
		sampleLabel.setBounds(191, 245, 66, 14);
		contentPanel.add(sampleLabel);
		
		//unchangeable text field for selected font from the list
		JTextField fontSelected = new JTextField();
		fontSelected.setEditable(false);
		fontSelected.setText(font);
		fontSelected.setBorder(new LineBorder(new Color(0, 0, 0)));
		fontSelected.setFont(new Font("Tahoma", Font.PLAIN, 12));
		fontSelected.setBounds(24, 61, 151, 22);
		contentPanel.add(fontSelected);
		fontSelected.setColumns(10);
		
		//unchangeable text field for selected font style from the list
		JTextField styleSelected = new JTextField();
		styleSelected.setEditable(false);
		styleSelected.setText(textStyle);
		styleSelected.setFont(new Font("Tahoma", Font.PLAIN, 12));
		styleSelected.setColumns(10);
		styleSelected.setBorder(new LineBorder(new Color(0, 0, 0)));
		styleSelected.setBounds(193, 61, 119, 22);
		contentPanel.add(styleSelected);

		//unchangeable text field for selected font size from the list
		JTextField sizeSelected = new JTextField();
		sizeSelected.setText(size);
		sizeSelected.setEditable(false);
		sizeSelected.setFont(new Font("Tahoma", Font.PLAIN, 12));
		sizeSelected.setColumns(10);
		sizeSelected.setBorder(new LineBorder(new Color(0, 0, 0)));
		sizeSelected.setBounds(330, 61, 72, 22);
		contentPanel.add(sizeSelected);
		
		//scroll pane for size list
		JScrollPane sizeScrollPane = new JScrollPane();
		sizeScrollPane.setViewportBorder(new LineBorder(new Color(0, 0, 0), 0));
		sizeScrollPane.setBounds(330, 85, 72, 147);
		contentPanel.add(sizeScrollPane);
		
		//creating size list
		JList sizeList = new JList();
		sizeList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				sizeSelected.setText((String)sizeList.getSelectedValue());
				size = sizeSelected.getText();
				sampleText.setFont(new Font(font,style,Integer.parseInt(size)));
			}
		});
		sizeList.setBorder(null);
		sizeList.setValueIsAdjusting(true);
		sizeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		sizeList.setFont(new Font("Tahoma", Font.PLAIN, 12));
		sizeList.setModel(new AbstractListModel() {
			String[] values = sizes;  //setting sizes list in size list
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		sizeScrollPane.setViewportView(sizeList);
		sizeList.setSelectedValue(size,true);
		
		//scroll pane for style list
		JScrollPane styleScrollPane = new JScrollPane();
		styleScrollPane.setViewportBorder(new LineBorder(new Color(0, 0, 0), 0));
		styleScrollPane.setBounds(193, 85, 119, 147);
		contentPanel.add(styleScrollPane);
		
		//creating style list
		JList styleList = new JList();
		styleList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				styleSelected.setText((String)styleList.getSelectedValue());
				textStyle = styleSelected.getText();
				if(textStyle.equals("Plain")) {
					style = Font.PLAIN;
				}else if(textStyle.equals("Bold")) {
					style=Font.BOLD;
				}else if(textStyle.equals("Italic")) {
					style=Font.ITALIC;
				}else {
					style=Font.BOLD+Font.ITALIC;
				}
				sampleText.setFont(new Font(font,style,Integer.parseInt(size)));
				
			}
		});
		styleList.setModel(new AbstractListModel() {
			String[] values = styles;   //setting styles in this list
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		styleList.setBorder(new LineBorder(new Color(0, 0, 0), 0));
		styleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		styleList.setVisibleRowCount(6);
		styleList.setFont(new Font("Tahoma", Font.PLAIN, 12));
		styleScrollPane.setViewportView(styleList);
		styleList.setSelectedValue(textStyle, true);
		
		//scroll pane for font list
		JScrollPane fontScrollPane = new JScrollPane();
		fontScrollPane.setViewportBorder(new LineBorder(new Color(0, 0, 0), 0));
		fontScrollPane.setBounds(24, 85, 152, 147);
		contentPanel.add(fontScrollPane);
		
		//creating font list
		JList fontList = new JList();
		fontList.setAutoscrolls(true);
		fontList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				fontSelected.setText((String)fontList.getSelectedValue());
				font = fontSelected.getText();
				sampleText.setFont(new Font(font,style,Integer.parseInt(size)));
			}
		});
		fontList.setModel(new AbstractListModel() {
			String[] values = fonts;   //setting fonts in this list
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		fontList.setFont(new Font("Tahoma", Font.PLAIN, 12));
		fontList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		fontList.setVisibleRowCount(6);
		fontScrollPane.setViewportView(fontList);
		fontList.setSelectedValue(font, true);
		
		
		//bottom button pane for ok and cancel
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		dialog.getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		//ok button
		JButton okButton = new JButton("OK");
		dialog.getRootPane().setDefaultButton(okButton);
		okButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
		okButton.setActionCommand("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editorPane.setFont(new Font(font,style,Integer.parseInt(size)));  //set new font to the editor pane
				dialog.dispose();   //exit the dialog
			}
		});
		buttonPane.add(okButton);
		
		//cancel button
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		
		
		
		dialog.setVisible(true); //set visible the dialog box now after doing all these stuffs
		
	}
}


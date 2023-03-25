package editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.text.*;
import java.text.*;
import java.util.*;
import javax.swing.undo.*;

import editor.MyUndoableEditListener.RedoAction;
import editor.MyUndoableEditListener.UndoAction;

import java.net.*;

public class Editor extends JFrame  {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final JTextArea textArea = new JTextArea(0, 0);
	private final JScrollPane scroller = new JScrollPane(textArea);

	private final JPopupMenu pop = new JPopupMenu();

	private final JMenuItem menuItem = new JMenuItem("copy");
	private final JMenuItem menucut = new JMenuItem("cut");
	private final JMenuItem menuclear = new JMenuItem("clear");
	private final JMenuItem menupaste = new JMenuItem("paste");

	private final JToolBar toolBar = new JToolBar();

	private final JButton newFile = new JButton();
	private final JButton openFile = new JButton();
	private final JButton saveFile = new JButton();

	private final JButton cutFile = new JButton();
	private final JButton copyFile = new JButton();
	private final JButton pasteFile = new JButton();

	private final JMenuBar menuBar = new JMenuBar();

	private final JMenu FILE = new JMenu("File");
	private final JMenu EDIT = new JMenu("Edit");
	private final JMenu FORMAT = new JMenu("Format");
	private final JMenu VIEW = new JMenu("View");
	private final JMenu HELP = new JMenu("Help");

	private final JMenuItem NEWFILE = new JMenuItem("New");
	private final JMenuItem OPENFILE = new JMenuItem("Open");
	private final JMenuItem SAVEFILE = new JMenuItem("Save");
	private final JMenuItem SAVEASFILE = new JMenuItem("Save As...");
	private final JMenuItem PRINTFILE = new JMenuItem("Print...");
	private final JMenuItem EXITFILE = new JMenuItem("Exit");

	private final JMenuItem COPYEDIT = new JMenuItem("Copy");
	private final JMenuItem CUTEDIT = new JMenuItem("Cut");
	private final JMenuItem PASTEDIT = new JMenuItem("Paste");
	private final JMenuItem DELETEDIT = new JMenuItem("Delete");
	private final JMenuItem FINDEDIT = new JMenuItem("Find");
	private final JMenuItem FINDNEXTEDIT = new JMenuItem("Find Next");
	private final JMenuItem REPLACEDIT = new JMenuItem("Replace");
	private final JMenuItem GOTOEDIT = new JMenuItem("Go To");
	private final JMenuItem SELECTEDIT = new JMenuItem("Select All");
	private final JMenuItem TIMEDIT = new JMenuItem("Time/Date");

	private final JCheckBoxMenuItem WORDFORMAT = new JCheckBoxMenuItem("Word Wrap");
	private final JMenuItem FONT = new JMenuItem("Font");

	private final JCheckBoxMenuItem STATUSVIEW = new JCheckBoxMenuItem("Status Bar");

	private final JMenuItem ABOUT = new JMenuItem("About");

	String file = null;
	String fileN;

	boolean opened = false;

	JPanel statusPanel = new JPanel();
	JLabel statusLabel;
	JPanel aboutPanel = new JPanel();

	int ind = 0;

	StringBuffer sbufer;
	String findString;

	FontSelector fontSelector = new FontSelector();

	UndoManager undo = new UndoManager();
	UndoAction undoAction = null;
	RedoAction redoAction = null;

	public Editor() {
		// DEFAULT TITLE OF FRAME
		super("untitled");

		// SETS THE SIZE
		this.setSize(800, 600);

		// SETS THE LAYOUT
		this.getContentPane().setLayout(new BorderLayout());

		// SETS WORD WRAP TO TRUE AS DEFAULT
		textArea.setLineWrap(true);

		// SETS FOCUS ON THE TEXTAREA
		textArea.requestFocus(true);

		// ADDS THE SCROLLPANE CONTAINING THE TEXTAREA TO THE CONTAINER
		this.getContentPane().add(scroller, BorderLayout.CENTER);

		// ADDS THE STATUSPANEL
		this.getContentPane().add(statusPanel, BorderLayout.SOUTH);

		// TO ENABLE DRAG MODE
		textArea.setDragEnabled(true);

		// SETS THE TOOLBAR TO BE STATIC I.E DISALLOW THE USER FROM CHANGING THE DOCKING
		// POSITION
		toolBar.setFloatable(false);

		// ADD THE TOOLBAR
		this.getContentPane().add(toolBar, BorderLayout.NORTH);

		// ADD A MOUSELISTENER TO RIGHT CLICK FOR THE POPUPLISTENER
		MouseListener popupListener = new PopupListener(pop);
		textArea.addMouseListener(popupListener);

		// UNDO LISTENER ON AREA
		MyUndoableEditListener editListener = new MyUndoableEditListener(undo);
		textArea.getDocument().addUndoableEditListener(editListener);
		undoAction = editListener.getUndoActionInstance();
		redoAction = editListener.getRedoActionInstance();

		// SETS THE MENUBAR
		FILE.add(NEWFILE);
		FILE.add(OPENFILE);
		FILE.add(SAVEFILE);
		FILE.add(SAVEASFILE);
		FILE.addSeparator();
		FILE.add(PRINTFILE);
		FILE.addSeparator();
		FILE.add(EXITFILE);

		EDIT.add(undoAction);
		EDIT.add(redoAction);
		EDIT.add(CUTEDIT);
		EDIT.add(COPYEDIT);
		EDIT.add(PASTEDIT);
		EDIT.add(DELETEDIT);
		EDIT.addSeparator();
		EDIT.add(FINDEDIT);
		EDIT.add(FINDNEXTEDIT);
		EDIT.add(REPLACEDIT);
		EDIT.add(GOTOEDIT);
		EDIT.addSeparator();
		EDIT.add(SELECTEDIT);
		EDIT.add(TIMEDIT);

		FORMAT.add(WORDFORMAT);
		WORDFORMAT.setSelected(true);
		FORMAT.add(FONT);

		VIEW.add(STATUSVIEW);
		STATUSVIEW.setSelected(true);

		HELP.add(ABOUT);

		FILE.setMnemonic(KeyEvent.VK_F);
		menuBar.add(FILE);
		EDIT.setMnemonic(KeyEvent.VK_E);
		menuBar.add(EDIT);
		FORMAT.setMnemonic(KeyEvent.VK_T);
		menuBar.add(FORMAT);
		VIEW.setMnemonic(KeyEvent.VK_V);
		menuBar.add(VIEW);
		HELP.setMnemonic(KeyEvent.VK_H);
		menuBar.add(HELP);

		// ADD MENUBAR TO THE FRAME
		this.setJMenuBar(menuBar);

		// ADD ITEMS TO THE POPUP
		pop.add(undoAction);
		pop.add(redoAction);
		pop.addSeparator();
		pop.add(menuItem);
		pop.add(menucut);
		pop.add(menupaste);
		pop.add(menuclear);

		// VALIDATION
		newFile.setBorder(null);
		openFile.setBorder(null);
		saveFile.setBorder(null);
		cutFile.setBorder(null);
		copyFile.setBorder(null);
		pasteFile.setBorder(null);

		// ADD TO TOOLBAR
		toolBar.add(newFile);
		newFile.setToolTipText("New file");
		toolBar.addSeparator();
		toolBar.add(openFile);
		openFile.setToolTipText("Open file");
		toolBar.add(saveFile);
		saveFile.setToolTipText("Save file");
		toolBar.addSeparator();
		toolBar.add(cutFile);
		cutFile.setToolTipText("Cut");
		toolBar.add(copyFile);
		copyFile.setToolTipText("Copy");
		toolBar.add(pasteFile);
		pasteFile.setToolTipText("Paste");

		setAllActionListeners();
	}
	
	private void setAllActionListeners() {
		// ACTION FOR NEW FILE ON THE TOOLBAR
		newFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				opened = false;
				if (textArea.getText().equals("")) {
					System.out.print("text is empty");
				} else {
					int confirm = JOptionPane.showConfirmDialog(null, "Would you like to save?", "New File",
							JOptionPane.YES_NO_CANCEL_OPTION);

					if (confirm == JOptionPane.YES_OPTION) {
						saveFile();
						textArea.setText(null);
						statusPanel.removeAll();
						statusPanel.validate();
					} else if (confirm == JOptionPane.CANCEL_OPTION) {
					} else if (confirm == JOptionPane.NO_OPTION) {
						textArea.setText(null);
						statusPanel.removeAll();
						statusPanel.validate();
					}
				}
			}
		});

		//// OPEN BUTTON ON THE TOOLBAR
		openFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFile();
			}
		});

		//// SAVE BUTTON ON THE TOOLBAR
		saveFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveFile();
			}
		});

		// ACTION FOR CUT BUTTON ON THE TOOLBAR
		cutFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.cut();
			}
		});

		// ACTION FOR COPY BUTTON ON THE TOOLBAR
		copyFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.copy();
			}
		});

		// ACTION FOR PASTE BUTTON ON THE TOOLBAR
		pasteFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.paste();
			}
		});

		// ACTIONLISTENER FOR OTHER ITEMS ON THE TOOLBAR
		// COPY BUTTON ON THE TOOLBAR
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.copy();
				menupaste.setEnabled(true);
				pasteFile.setEnabled(true);
				PASTEDIT.setEnabled(true);
			}
		});

		// CUT BUTTON ON THE TOOLBAR
		menucut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.cut();
				menupaste.setEnabled(true);
				pasteFile.setEnabled(true);
				PASTEDIT.setEnabled(true);
			}
		});

		// PASTE BUTTON ON THE TOOLBAR
		menupaste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.paste();
			}
		});

		// CLEAR BUTTON ON THE TOOLBAR
		menuclear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText(null);
			}
		});

		// ACTIONLISTENER FOR ITEMS IN THE MENUBAR

		// OPEN A NEW FILE
		NEWFILE.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		NEWFILE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				opened = false;
				int confirm = JOptionPane.showConfirmDialog(null, "Would you like to save?", "New File",
						JOptionPane.YES_NO_CANCEL_OPTION);

				if (confirm == JOptionPane.YES_OPTION) {
					saveFile();
					textArea.setText(null);
					statusPanel.removeAll();
					statusPanel.validate();
				} else if (confirm == JOptionPane.CANCEL_OPTION) {
				} else {
					textArea.setText(null);
					statusPanel.removeAll();
					statusPanel.validate();
				}
			}
		});

		// SAVE OPTION. HAS A VALIDATION CHECK THAT CHECKS WETHER ITS AN OPENED FILE OR
		// NEW FILE
		SAVEFILE.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		SAVEFILE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveFile();
			}
		});

		// OPTION THAT WILL BRING UP A DIALOG WHICH SAVES THE FILE WITH A NAME AND
		// FORMAT DESIRED
		SAVEASFILE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				opened = false;
				saveFile();
			}
		});
		SELECTEDIT.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		SELECTEDIT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.selectAll();
			}
		});

		// PRINTS WHATEVER IS IN THE TEXT AREA
		PRINTFILE.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		PRINTFILE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (textArea.getText().equals(""))
					JOptionPane.showMessageDialog(null, "Text Area is empty.");
				else
					print(createBuffer());

			}
		});

		// ACTION FOR STATUS BUTTON
		STATUSVIEW.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (STATUSVIEW.isSelected())
					statusPanel.setVisible(true);
				else
					statusPanel.setVisible(false);
			}
		});

		// ACTION FOR OPEN BUTTON
		OPENFILE.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		OPENFILE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFile();
			}
		});

		// ACTION FOR CUT BUTTON
		CUTEDIT.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		CUTEDIT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.cut();
			}
		});

		// ACTION FOR COPY BUTTON
		COPYEDIT.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		COPYEDIT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.copy();
			}
		});

		// ACTION FOR PASTE BUTTON
		PASTEDIT.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
		PASTEDIT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.paste();
			}
		});

		// FONT SELECTOR OPTION
		FONT.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
		FONT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fontSelector.setVisible(true);
				fontSelector.okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						Font selectedFont = fontSelector.returnFont();
						textArea.setFont(selectedFont);
						fontSelector.setVisible(false);
					}
				});

				fontSelector.cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						fontSelector.setVisible(false);
					}
				});
			}
		});

		// PRINTS THE SYSTEM DATE AND TIME IN THE EDITOR
		TIMEDIT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Date currentDate;
				SimpleDateFormat formatter;
				String dd;
				formatter = new SimpleDateFormat("KK:mm aa MMMMMMMMM dd yyyy", Locale.getDefault());
				currentDate = new java.util.Date();
				dd = formatter.format(currentDate);
				textArea.insert(dd, textArea.getCaretPosition());
			}
		});

		// FINDS A WORD IN THE EDITOR
		FINDEDIT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					sbufer = new StringBuffer(textArea.getText());
					findString = JOptionPane.showInputDialog(null, "Find");
					ind = sbufer.indexOf(findString);
					textArea.setCaretPosition(ind);
					textArea.setSelectionStart(ind);
					textArea.setSelectionEnd(ind + findString.length());
				} catch (IllegalArgumentException npe) {
					JOptionPane.showMessageDialog(null, "String not found");
				} catch (NullPointerException nfe) {
				}
			}
		});

		// FINDS A WORD IN THE EDITOR
		FINDNEXTEDIT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					sbufer = new StringBuffer(textArea.getText());
					findString = JOptionPane.showInputDialog(null, "Find");
					ind = sbufer.indexOf(findString, textArea.getCaretPosition());
					textArea.setCaretPosition(ind);
					textArea.setSelectionStart(ind);
					textArea.setSelectionEnd(ind + findString.length());
				} catch (IllegalArgumentException npe) {
					JOptionPane.showMessageDialog(null, "String not found");
				} catch (NullPointerException nfe) {
				}
			}
		});

		// EXITS THE APPLICATION AND CHECKS FOR ANY CHANGES MADE
		EXITFILE.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		EXITFILE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int confirm = JOptionPane.showConfirmDialog(null, "Would you like to save?", "Exit Application",
						JOptionPane.YES_NO_CANCEL_OPTION);

				if (confirm == JOptionPane.YES_OPTION) {
					saveFile();
					dispose();
					System.exit(0);
				} else if (confirm == JOptionPane.CANCEL_OPTION) {
				} else {
					dispose();
					System.exit(0);
				}
			}
		});

		// ACTION FOR REPLACE OPTION
		REPLACEDIT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String replace = JOptionPane.showInputDialog(null, "Replace");
					textArea.replaceSelection(replace);
				} catch (NumberFormatException nfe) {
				}
			}
		});

		// ACTION FOR GO TO OPTION
		GOTOEDIT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Element root = textArea.getDocument().getDefaultRootElement();
					Element paragraph = root
							.getElement(Integer.parseInt(JOptionPane.showInputDialog(null, "Go to line")));
					textArea.setCaretPosition(paragraph.getStartOffset() - 1);
				} catch (NullPointerException npe) {
					JOptionPane.showMessageDialog(null, "Invalid line number");
				} catch (NumberFormatException nfe) {

				}
			}
		});

		// ACTION FOR DELETE OPTION
		DELETEDIT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.replaceSelection(null);
			}
		});

		// SETS THE LINEWRAP OT TRUE OR FALSE
		WORDFORMAT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (WORDFORMAT.isSelected())
					textArea.setLineWrap(true);
				else
					textArea.setLineWrap(false);
			}
		});

		// ABOUT
		ABOUT.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, ActionEvent.CTRL_MASK));
		ABOUT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				URL img = getClass().getResource("logo.jpg");
				String imagesrc = "<img src=\"" + img + "\" width=\"323\" height=\"185\">";
				String message = "Simple text editor\n";
				JOptionPane.showMessageDialog(aboutPanel, "<html><center>" + imagesrc + "<br>" + message);
			}
		});

		// CLOSES THE WINDOW WHEN THE CLOSE BUTTON IS PRESSED
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int confirm = JOptionPane.showConfirmDialog(null, "Would you like to save?", "Exit Application",
						JOptionPane.YES_NO_CANCEL_OPTION);

				if (confirm == JOptionPane.YES_OPTION) {
					saveFile();
					dispose();
					System.exit(0);
				} else if (confirm == JOptionPane.CANCEL_OPTION) {

				} else {
					dispose();
					System.exit(0);
				}
			}
		});
	}
	
	
	// FUNCTION CALLED BY THE SAVE BUTTON
	public void saveFile() {
		String line = textArea.getText();
		if (opened == true) {
			try {
				FileWriter output = new FileWriter(file);
				BufferedWriter bufout = new BufferedWriter(output);
				bufout.write(line, 0, line.length());
				JOptionPane.showMessageDialog(null, "Save Successful");
				bufout.close();
				output.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		} else {
			JFileChooser fc = new JFileChooser();
			int result = fc.showSaveDialog(new JPanel());

			if (result == JFileChooser.APPROVE_OPTION) {
				fileN = String.valueOf(fc.getSelectedFile());

				try {
					FileWriter output = new FileWriter(fileN);
					BufferedWriter bufout = new BufferedWriter(output);
					bufout.write(line, 0, line.length());
					JOptionPane.showMessageDialog(null, "Save Successful");
					bufout.close();
					output.close();
					opened = true;
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
	}

	// PRINT FUNCTION
	public String createBuffer() {
		String buffer;
		buffer = textArea.getText();
		return buffer;
	}

	private void print(String s) {
		StringReader sr = new StringReader(s);
		LineNumberReader lnr = new LineNumberReader(sr);
		Font typeface = new Font("Monospaced", Font.PLAIN, 12);
		PrintJob pjob = getToolkit().getPrintJob(this, "Print report", new Properties());

		if (pjob != null) {
			Graphics pg = pjob.getGraphics();
			if (pg != null) {
				FontMetrics fm = pg.getFontMetrics(typeface);
				int margin = 20;
				int pageHeight = pjob.getPageDimension().height - margin;
				int fontHeight = fm.getHeight();
				int fontDescent = fm.getDescent();
				int curHeight = margin;

				String nextLine;
				pg.setFont(textArea.getFont());

				try {
					do {
						nextLine = lnr.readLine();
						if (nextLine != null) {
							if ((curHeight + fontHeight) > pageHeight) { // New Page
								pg.dispose();
								pg = pjob.getGraphics();
								curHeight = margin;
							}

							curHeight += fontHeight;

							if (pg != null) {
								pg.setFont(typeface);
								pg.drawString(nextLine, margin, curHeight - fontDescent);
							}
						}
					} while (nextLine != null);

				} catch (EOFException eof) {
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			try {
				pg.dispose();
			} catch(Exception e) {}
		}
		if (pjob != null)
			pjob.end();
	}

	// FUNCTION TO OPEN THE FILE
	public void openFile() {
		statusPanel.removeAll();
		statusPanel.validate();
		textArea.setText(null);
		JFileChooser fc = new JFileChooser();
		int result = fc.showOpenDialog(new JPanel());

		if (result == JFileChooser.APPROVE_OPTION) {
			String file = String.valueOf(fc.getSelectedFile());

			File fil = new File(file);
			newFile.setEnabled(false);

			// START THIS THREAD WHILE READING FILE
			Thread loader = new FileLoader(fil, textArea.getDocument());
			loader.start();
			statusPanel.removeAll();
			statusPanel.revalidate();
		}
	}

	/**
	 * Thread to load a file into the text storage model
	 */
	class FileLoader extends Thread {

		JLabel state;
		Document doc;
		File f;
		FileLoader(File f, Document doc) {
			setPriority(4);
			this.f = f;
			this.doc = doc;
		}

		public void run() {
			try {
				// initialize the statusbar
				statusPanel.removeAll();
				JProgressBar progress = new JProgressBar();
				progress.setMinimum(0);
				progress.setMaximum((int) f.length());
				statusPanel.add(new JLabel("opened so far "));
				statusPanel.add(progress);
				statusPanel.revalidate();

				// try to start reading
				@SuppressWarnings("resource")
				Reader in = new FileReader(f);
				char[] buff = new char[4096];
				int nch;
				while ((nch = in.read(buff, 0, buff.length)) != -1) {
					doc.insertString(doc.getLength(), new String(buff, 0, nch), null);
					progress.setValue(progress.getValue() + nch);
				}

				statusPanel.removeAll();
				statusPanel.revalidate();

			} catch (IOException e) {
				System.err.println(e.toString());
			} catch (BadLocationException e) {
				System.err.println(e.getMessage());
			}
			newFile.setEnabled(true);
		}
	}
}

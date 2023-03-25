package editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontSelector extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2110063090847642985L;
	String fontString[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	JComboBox<?> FontSelector = new JComboBox<Object>(fontString);
	JLabel fontLabel = new JLabel("Select Font");

	String fontSizeString[] = { "10", "12", "14", "16", "18", "20", "22", "24", "28" };
	JComboBox<?> fontSize = new JComboBox<Object>(fontSizeString);
	JLabel sizeLabel = new JLabel("Select Size");

	String fontStyleString[] = { "Normal", "Bold", "Italic", "Bold Italic" };
	JComboBox<?> fontStyle = new JComboBox<Object>(fontStyleString);
	JLabel styleLabel = new JLabel("Select Style");

	JButton okButton = new JButton("OK");
	JButton cancelButton = new JButton("Cancel");

	JLabel previewLabel = new JLabel("Preview:");
	JLabel preview = new JLabel("   AaBbCc");

	Font selectedFont;

	public FontSelector() {
		this.setSize(300, 200);
		this.setBackground(Color.blue);
		this.setTitle("Font Selector");

		getContentPane().setLayout(null);

		fontLabel.setBounds(10, 10, 100, 20);
		FontSelector.setBounds(110, 10, 150, 20);
		sizeLabel.setBounds(10, 35, 100, 20);
		fontSize.setBounds(110, 35, 100, 20);
		styleLabel.setBounds(10, 60, 100, 20);
		fontStyle.setBounds(110, 60, 100, 20);

		okButton.setBounds(10, 100, 100, 20);
		cancelButton.setBounds(110, 100, 100, 20);

		previewLabel.setBounds(50, 130, 100, 30);
		preview.setBorder(BorderFactory.createLineBorder(Color.black));
		preview.setBounds(120, 130, 70, 30);

		getContentPane().add(fontLabel);
		getContentPane().add(FontSelector);
		getContentPane().add(sizeLabel);
		getContentPane().add(fontSize);
		getContentPane().add(styleLabel);
		getContentPane().add(fontStyle);
		getContentPane().add(okButton);
		getContentPane().add(cancelButton);
		getContentPane().add(previewLabel);
		getContentPane().add(preview);

		// SETS THE PREVIEW
		FontSelector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				preview.setFont(
						new Font(String.valueOf(FontSelector.getSelectedItem()), fontStyle.getSelectedIndex(), 14));
			}
		});
		fontStyle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				preview.setFont(
						new Font(String.valueOf(FontSelector.getSelectedItem()), fontStyle.getSelectedIndex(), 14));
			}
		});
	}

	public Font returnFont() {
		String fontSS = String.valueOf(FontSelector.getSelectedItem());
		int fontSZ = Integer.parseInt(String.valueOf(fontSize.getSelectedItem()));
		int fontST = fontStyle.getSelectedIndex();

		selectedFont = new Font(fontSS, fontST, fontSZ);

		return selectedFont;
	}

}

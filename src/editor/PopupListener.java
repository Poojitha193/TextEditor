package editor;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

public class PopupListener extends MouseAdapter {

	private JPopupMenu pop;
	public PopupListener(JPopupMenu pop) {
		this.pop = pop;
	}
	public void mousePressed(MouseEvent e) {

		maybeShowPopup(e);
	}

	public void mouseReleased(MouseEvent e) {
		maybeShowPopup(e);
	}

	private void maybeShowPopup(MouseEvent e) {
		if (e.isPopupTrigger()) {
			pop.show(e.getComponent(), e.getX(), e.getY());
		}
	}

}

package editor;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;


public class MyUndoableEditListener implements UndoableEditListener {
	public UndoManager undo;
	public UndoAction undoAction;
	public RedoAction redoAction;

	public MyUndoableEditListener(UndoManager undo) {
		// TODO Auto-generated constructor stub
		this.undo = undo;
		this.undoAction = new UndoAction();
		this.redoAction = new RedoAction();
	}
	
	public UndoAction getUndoActionInstance() {
		if(undoAction == null) return new UndoAction();
		return undoAction;
	}
	
	public RedoAction getRedoActionInstance() {
		if(redoAction == null) return new RedoAction();
		return redoAction;
	}
	
	public void undoableEditHappened(UndoableEditEvent e) {
		// Remember the edit and update the menus
		undo.addEdit(e.getEdit());
		undoAction.update();
		redoAction.update();
	}

	// UNDO AND REDOACTION CLASSES
	// THIS PASRT OF CODE WAS TAKEN FROM THE NOTEPAD DEMO FOUND IN THE JDK1.4.1 DEMO
	// DIRECTORY
	class UndoAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public UndoAction() {
			super("Undo");
			setEnabled(false);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				undo.undo();
			} catch (CannotUndoException ex) {
				System.out.println("Unable to undo: " + ex);
				ex.printStackTrace();
			}
			update();
			redoAction.update();
		}

		protected void update() {
			if (undo.canUndo()) {
				setEnabled(true);
				putValue("Undo", undo.getUndoPresentationName());
			} else {
				setEnabled(false);
				putValue(Action.NAME, "Undo");
			}
		}
	}

	class RedoAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public RedoAction() {
			super("Redo");
			setEnabled(false);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				undo.redo();
			} catch (CannotRedoException ex) {
				System.out.println("Unable to redo: " + ex);
				ex.printStackTrace();
			}
			update();
			undoAction.update();
		}

		protected void update() {
			if (undo.canRedo()) {
				setEnabled(true);
				putValue("Redo", undo.getRedoPresentationName());
			} else {
				setEnabled(false);
				putValue(Action.NAME, "Redo");
			}
		}
	}
}

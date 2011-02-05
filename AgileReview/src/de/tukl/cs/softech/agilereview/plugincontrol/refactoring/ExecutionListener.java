package de.tukl.cs.softech.agilereview.plugincontrol.refactoring;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IExecutionListener;
import org.eclipse.core.commands.NotHandledException;

import de.tukl.cs.softech.agilereview.tools.PluginLogger;
import de.tukl.cs.softech.agilereview.views.ViewControl;
import de.tukl.cs.softech.agilereview.views.commenttable.CommentTableView;

/**
 * ExecutionListener which methods are executed, if a command is triggered.
 */
public class ExecutionListener implements IExecutionListener {

	@Override
	public void notHandled(String commandId, NotHandledException exception) {/* Do nothing */}

	@Override
	public void postExecuteFailure(String commandId, ExecutionException exception) {
		PluginLogger.log(this.getClass().toString(), "postExecuteFailure", commandId);
		if(commandId.equals("org.eclipse.jdt.ui.edit.text.java.move.element")
				|| commandId.equals("org.eclipse.ltk.ui.refactoring.commands.renameResource")
				|| commandId.equals("org.eclipse.ui.edit.rename")) {
			if(ViewControl.isOpen(CommentTableView.class)) {
				CommentTableView.getInstance().resetEditorReferences();
			}
		}
	}

	@Override
	public void postExecuteSuccess(String commandId, Object returnValue) {
		PluginLogger.log(this.getClass().toString(), "postExecuteSuccess", commandId);
		if(commandId.equals("org.eclipse.ui.file.save") || commandId.equals("org.eclipse.ui.file.saveAll")) {
			
		}
		if(commandId.equals("org.eclipse.jdt.ui.edit.text.java.move.element")
				|| commandId.equals("org.eclipse.ltk.ui.refactoring.commands.renameResource")
				|| commandId.equals("org.eclipse.ui.edit.rename")) {
			if(ViewControl.isOpen(CommentTableView.class)) {
				CommentTableView.getInstance().resetEditorReferences();
			}
		}
	}

	@Override
	public void preExecute(String commandId, ExecutionEvent event) {
		PluginLogger.log(this.getClass().toString(), "preExecute", commandId);
		if(commandId.equals("org.eclipse.jdt.ui.edit.text.java.move.element")
				|| commandId.equals("org.eclipse.ltk.ui.refactoring.commands.renameResource")
				|| commandId.equals("org.eclipse.ui.edit.rename")) {
			if(ViewControl.isOpen(CommentTableView.class)) {
				CommentTableView.getInstance().cleanEditorReferences();
			}
		}
	}
}

package de.tukl.cs.softech.agilereview.views.commenttable.handler;

import java.util.regex.Pattern;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.FileEditorInput;

import agileReview.softech.tukl.de.CommentDocument.Comment;
import de.tukl.cs.softech.agilereview.dataaccess.ReviewAccess;
import de.tukl.cs.softech.agilereview.tools.PluginLogger;
import de.tukl.cs.softech.agilereview.tools.PropertiesManager;
import de.tukl.cs.softech.agilereview.views.ViewControl;
import de.tukl.cs.softech.agilereview.views.commenttable.CommentTableView;
import de.tukl.cs.softech.agilereview.wizards.newreview.NewReviewWizard;

/**
 * Handler for adding a new Comment
 */
public class AddNewCommentHandler extends AbstractHandler {
	
	/**
	 * Instance of ReviewAccess
	 */
	private ReviewAccess ra = ReviewAccess.getInstance();

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (!ViewControl.isPerspectiveOpen()) {
			if (ViewControl.getInstance().shouldSwitchPerspective()) {
				ViewControl.getInstance().switchPerspective();
			}
		}
		PluginLogger.log(this.getClass().toString(), "execute", "Command \"Add new Comment\" triggered");			
		String activeReview = PropertiesManager.getPreferences().getString(PropertiesManager.EXTERNAL_KEYS.ACTIVE_REVIEW);
		if (!activeReview.isEmpty()) {
			String pathToFile = "";
			
			IEditorPart part = HandlerUtil.getActiveEditor(event);
			if(part != null) {
				if ( part instanceof IEditorPart ) {
					IEditorInput input = part.getEditorInput();
					if (input != null && input instanceof FileEditorInput) {
						pathToFile = ((FileEditorInput)input).getFile().getFullPath().toOSString().replaceFirst(Pattern.quote(System.getProperty("file.separator")), "");
					}
				}
				String user = PropertiesManager.getPreferences().getString(PropertiesManager.EXTERNAL_KEYS.AUTHOR_NAME);
				Comment newComment = ra.createNewComment(activeReview , user, pathToFile);
				// TODO Hiervon noch was auslagern (Parser, etc)
				if(ViewControl.isOpen(CommentTableView.class)) {
					CommentTableView.getInstance().addComment(newComment);
				}
				// Save the new comment & Refresh the Review Explorer
				ra.save(newComment);/*?|0000026|Thilo|c1|?*/
				ViewControl.refreshViews(ViewControl.REVIEW_EXPLORER);
			} else {
				// no open editor
				MessageDialog.openWarning(null, "Warning: No open file", "Please open a file in an editor before adding comments!");
				PluginLogger.logWarning(this.getClass().toString(), "addNewComment", "No open editor!");
			}
		} else {
			if (this.ra.getAllReviews().isEmpty()) {
				MessageDialog.openInformation(null, "No reviews existent", "Please create a review before adding comments!");
				NewReviewWizard wizard = new NewReviewWizard();
				WizardDialog wd = new WizardDialog(HandlerUtil.getActiveShell(event), wizard);
				wd.setTitle(wizard.getWindowTitle());
				wd.open();
			} else {
				MessageDialog.openInformation(null, "No active review", "Please activate a review before adding comments!");
			}
			PluginLogger.logWarning(this.getClass().toString(), "addNewComment", "No active review!");
		}

		return null;
	}

}

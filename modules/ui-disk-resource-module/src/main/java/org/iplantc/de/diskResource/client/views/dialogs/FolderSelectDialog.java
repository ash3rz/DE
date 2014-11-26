package org.iplantc.de.diskResource.client.views.dialogs;

import org.iplantc.de.client.models.HasPath;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.models.diskResources.TYPE;
import org.iplantc.de.client.models.viewer.InfoType;
import org.iplantc.de.commons.client.views.gxt3.dialogs.IPlantDialog;
import org.iplantc.de.diskResource.client.events.DiskResourceSelectionChangedEvent;
import org.iplantc.de.diskResource.client.events.FolderSelectionEvent;
import org.iplantc.de.diskResource.client.gin.factory.DiskResourcePresenterFactory;
import org.iplantc.de.diskResource.client.views.DiskResourceView;

import com.google.common.base.Preconditions;
import com.google.gwt.user.client.TakesValue;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

import java.util.Collections;
import java.util.List;

/**
 * An <code>IPlantDialog</code> which wraps the standard <code>DiskResourceView</code> for folder
 * selection.
 * <p/>
 * Users of this class are responsible adding hide handlers to get the selected folder.
 *
 * @author jstroot
 */
public class FolderSelectDialog extends IPlantDialog implements TakesValue<Folder> {

    public interface FolderSelectDialogAppearance {
        String headerText();

        String selectorFieldLabel();
    }

    private static final class DiskResourceSelectionChangedHandler implements DiskResourceSelectionChangedEvent.DiskResourceSelectionChangedEventHandler {
        private final DiskResourceView.Presenter presenter;
        private final TakesValue<Folder> dlg;
        private final List<InfoType> infoTypeFilters;

        public DiskResourceSelectionChangedHandler(final DiskResourceView.Presenter presenter,
                                                   final TakesValue<Folder> dlg,
                                                   final List<InfoType> infoTypeFilters) {
            this.presenter = presenter;
            this.dlg = dlg;
            this.infoTypeFilters = infoTypeFilters;
        }

        @Override
        public void onDiskResourceSelectionChanged(DiskResourceSelectionChangedEvent event) {
            Preconditions.checkNotNull(event.getSelection());
            Preconditions.checkArgument(event.getSelection().size() <= 1, "Only single select is supported for FolderSelectDialog");

            List<DiskResource> selection = event.getSelection();
            Folder selectedFolder;
            if (selection.isEmpty()) {
                selectedFolder = null;
            }else if(selection.get(0) instanceof Folder){
                // good to go
                selectedFolder = (Folder) selection.get(0);
            } else if(isSupportedInfoType(selection.get(0))){
                // Make path list item look like a folder
                selectedFolder = presenter.convertToFolder(selection.get(0));
            } else {
                selectedFolder = null;
            }

            dlg.setValue(selectedFolder);
        }

        boolean isSupportedInfoType(DiskResource selectedItem){
            if(selectedItem == null){
                return false;
            }
            InfoType selectedInfoType = InfoType.fromTypeString(selectedItem.getInfoType());
            for(InfoType infoType : infoTypeFilters){
                if(selectedInfoType.equals(infoType)){
                    return true;
                }
            }
            return false;
        }
    }

    private static final class FolderSelectionChangedHandler implements FolderSelectionEvent.FolderSelectionEventHandler {
        private final TakesValue<Folder> dlg;

        private FolderSelectionChangedHandler(final TakesValue<Folder> dlg) {
            this.dlg = dlg;
        }

        @Override
        public void onFolderSelected(FolderSelectionEvent event) {
            Folder diskResource = event.getSelectedFolder();
            dlg.setValue(diskResource);
        }
    }

    private final DiskResourceView.Presenter presenter;
    private Folder selectedFolder;
    private TextField selectedFolderTextField;

    @AssistedInject
    FolderSelectDialog(final DiskResourcePresenterFactory presenterFactory,
                       final FolderSelectDialogAppearance appearance) {
        this(presenterFactory, appearance, null);
    }

    @AssistedInject
    FolderSelectDialog(final DiskResourcePresenterFactory presenterFactory,
                       final FolderSelectDialogAppearance appearance,
                       @Assisted final HasPath folderToSelect) {
        this(presenterFactory, appearance, folderToSelect, Collections.<InfoType>emptyList());
    }

    @AssistedInject
    FolderSelectDialog(final DiskResourcePresenterFactory presenterFactory,
                       final FolderSelectDialogAppearance appearance,
                       @Assisted final HasPath folderToSelect,
                       @Assisted final List<InfoType> infoTypeFilters) {

        // Disable Ok button by default.
        getOkButton().setEnabled(false);

        setResizable(true);
        setSize("640", "480");
        setHeadingText(appearance.headerText());

        selectedFolderTextField = new TextField();
        final FieldLabel fl = new FieldLabel(selectedFolderTextField, appearance.selectorFieldLabel());
        TYPE entityType = infoTypeFilters.isEmpty() ? TYPE.FOLDER : TYPE.ANY;
        // Tell the presenter to add the view with the toolbar and details panel hidden, etc.
        presenter = presenterFactory.filtered(true,
                                              true,
                                              true,
                                              true,
                                              folderToSelect,
                                              infoTypeFilters,
                                              entityType,
                                              fl);

        presenter.addFolderSelectedEventHandler(new FolderSelectionChangedHandler(this));
        presenter.addDiskResourceSelectionChangedEventHandler(new DiskResourceSelectionChangedHandler(presenter,
                                                                                                      this,
                                                                                                      infoTypeFilters));
        presenter.go(this);
    }

    @Override
    public Folder getValue() {
        return selectedFolder;
    }

    @Override
    public void onHide() {
        presenter.cleanUp();
    }

    @Override
    public void setValue(Folder value) {
        this.selectedFolder = value;
        if(value == null){
            selectedFolderTextField.clear();
            getOkButton().setEnabled(false);
            return;
        }

        selectedFolderTextField.setValue(value.getName());
        getOkButton().setEnabled(true);
    }

}

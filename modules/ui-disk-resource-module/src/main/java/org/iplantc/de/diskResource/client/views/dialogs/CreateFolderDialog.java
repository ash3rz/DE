package org.iplantc.de.diskResource.client.views.dialogs;

import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.commons.client.validators.DiskResourceNameValidator;
import org.iplantc.de.commons.client.views.dialogs.IPlantPromptDialog;
import org.iplantc.de.resources.client.messages.I18N;

import com.google.gwt.user.client.ui.HTML;

import com.sencha.gxt.core.client.util.Format;

/**
 * FIXME Create appearance
 * @author jstroot
 */
public class CreateFolderDialog extends IPlantPromptDialog {

    private final DiskResourceUtil diskResourceUtil;
    public CreateFolderDialog(final Folder parentFolder) {
        super(I18N.DISPLAY.folderName(), -1, "", new DiskResourceNameValidator());
        diskResourceUtil = DiskResourceUtil.getInstance();
        setWidth("300px");
        setHeadingText(I18N.DISPLAY.newFolder());
        initDestPathLabel(parentFolder.getPath());
    }

    private void initDestPathLabel(String destPath) {
        HTML htmlDestText = new HTML("<div title='" + destPath + "'style='color: #0098AA;width:100%;padding:5px;text-overflow:ellipsis;'>"
                + Format.ellipse(I18N.DISPLAY.createIn(diskResourceUtil.parseNameFromPath(destPath)), 50) + "</div>");
        insert(htmlDestText, 0);
    }
}
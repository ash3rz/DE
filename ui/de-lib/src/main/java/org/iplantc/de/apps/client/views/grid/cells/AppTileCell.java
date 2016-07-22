package org.iplantc.de.apps.client.views.grid.cells;

import org.iplantc.de.client.models.apps.App;

import com.google.common.collect.Lists;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import java.util.List;

/**
 * @author aramsey
 */
public class AppTileCell extends CompositeCell<App> {

    public interface AppTileCellAppearance {
        void render(Context context, App value, SafeHtmlBuilder sb);

        <X> void render(Context context, App value, SafeHtmlBuilder sb, HasCell<App, X> hasCell);
    }

    private static AppNameCell appNameCell;
    private static AppInfoCell appInfoCell;
    private static AppCommentCell appCommentCell;
    private static AppRatingCell appRatingCell;
    private static AppIntegratorCell appIntegratorCell;
    private static AppCardCell appCardCell;

    private AppTileCellAppearance appearance = GWT.create(AppTileCellAppearance.class);

    public AppTileCell() {
        this(createAppTileCell());
    }

    /**
     * Construct a new {@link CompositeCell}.
     *
     * @param hasCells the cells that makeup the composite
     */
    private AppTileCell(List<HasCell<App, ?>> hasCells) {
        super(hasCells);
    }

    private static List<HasCell<App, ?>> createAppTileCell() {
        List<HasCell<App, ?>> cellList = Lists.newArrayList();

        appNameCell = new AppNameCell();
        appInfoCell = new AppInfoCell();
        appCommentCell = new AppCommentCell();
        appRatingCell = new AppRatingCell();
        appIntegratorCell = new AppIntegratorCell();
        appCardCell = new AppCardCell();

        cellList.add(appNameCell);
        cellList.add(appInfoCell);
        cellList.add(appCommentCell);
        cellList.add(appRatingCell);
        cellList.add(appIntegratorCell);
        cellList.add(appCardCell);

        return cellList;
    }

    @Override
    public void render(Context context, App value, SafeHtmlBuilder sb) {
        super.render(context, value, sb);
        appearance.render(context, value, sb);
    }

    @Override
    protected <X> void render(Context context, App value, SafeHtmlBuilder sb, HasCell<App, X> hasCell) {
        appearance.render(context, value, sb, hasCell);
    }

    public void setHasHandlers(HasHandlers hasHandlers) {
        appNameCell.setHasHandlers(hasHandlers);
        appInfoCell.setHasHandlers(hasHandlers);
        appCommentCell.setHasHandlers(hasHandlers);
        appRatingCell.setHasHandlers(hasHandlers);
    }

    public void setSearchRegexPattern(String pattern) {
        appNameCell.setSearchRegexPattern(pattern);
        appIntegratorCell.setSearchRegexPattern(pattern);
    }

    public void setDebugBaseId(String baseID) {
        appNameCell.setBaseDebugId(baseID);
        appInfoCell.setBaseDebugId(baseID);
    }
}

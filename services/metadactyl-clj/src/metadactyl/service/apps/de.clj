(ns metadactyl.service.apps.de
  (:require [metadactyl.service.apps.de.edit :as edit]
            [metadactyl.service.apps.de.listings :as listings]))

(deftype DeApps [user]
  metadactyl.protocols.Apps

  (listAppCategories [_ params]
    (listings/get-app-groups user params))

  (hasCategory [_ category-id]
    (listings/has-category category-id))

  (listAppsInCategory [_ category-id params]
    (listings/list-apps-in-group user category-id params))

  (searchApps [_ _ params]
    (listings/search-apps user params))

  (canEditApps [_]
    true)

  (addApp [_ app]
    (edit/add-app user app)))
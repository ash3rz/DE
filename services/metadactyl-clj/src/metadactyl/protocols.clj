(ns metadactyl.protocols)

(defprotocol Apps
  "A protocol used to provide an abstraction layer for dealing with app metadata."
  (getClientName [_])
  (getJobTypes [_])
  (listAppCategories [_ params])
  (hasCategory [_ category-id])
  (listAppsInCategory [_ category-id params])
  (searchApps [_ search-term params])
  (canEditApps [_])
  (addApp [_ app])
  (previewCommandLine [_ app])
  (listAppIds [_])
  (deleteApps [_ deletion-request])
  (getAppJobView [_ app-id])
  (deleteApp [_ app-id])
  (relabelApp [_ app])
  (updateApp [_ app])
  (copyApp [_ app-id])
  (getAppDescription [_ app-id])
  (getAppDetails [_ app-id])
  (removeAppFavorite [_ app-id])
  (addAppFavorite [_ app-id])
  (isAppPublishable [_ app-id])
  (makeAppPublic [_ app])
  (deleteAppRating [_ app-id])
  (rateApp [_ app-id rating])
  (getAppTaskListing [_ app-id])
  (getAppToolListing [_ app-id])
  (getAppUi [_ app-id])
  (addPipeline [_ pipeline])
  (formatPipelineTasks [_ pipeline])
  (updatePipeline [_ pipeline])
  (copyPipeline [_ app-id])
  (editPipeline [_ app-id])
  (listJobs [_ params])
  (loadAppTables [_ app-ids])
  (submitJob [_ submission])
  (prepareJobSubmission [_ submission])
  (sendJobSubmission [_ submission job]))

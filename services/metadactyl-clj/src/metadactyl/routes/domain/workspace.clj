(ns metadactyl.routes.domain.workspace
  (:use [ring.swagger.schema :only [describe]]
        [schema.core :only [defschema]])
  (:import [java.util UUID]))

(defschema Workspace
  :id               (describe UUID "The workspace ID.")
  :user_id          (describe UUID "The user's internal ID.")
  :root_category_id (describe UUID "The ID of the user's root app category.")
  :is_public        (describe Boolean "Indicates whether the workspace is public."))
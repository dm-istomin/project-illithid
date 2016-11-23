(ns illithid.subs.welcome
  (:require [re-frame.core :refer [reg-sub]]
            [illithid.db :as db]))

(reg-sub ::characters (fn [db _] (::db/characters db)))

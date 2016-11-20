(ns illithid.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [reg-sub]]
            [illithid.db :as db]))

(reg-sub ::state (fn [db _] (::db/state db)))

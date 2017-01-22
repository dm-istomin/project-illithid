(ns illithid.subs.characters
  (:require [re-frame.core :refer [reg-sub]]
            [illithid.db :as db]))

(reg-sub ::character-ids (fn [db _] (::db/character-ids db)))
(reg-sub
  ::get-character
  (fn [db [_ character-id]] (get-in db [::db/characters character-id])))

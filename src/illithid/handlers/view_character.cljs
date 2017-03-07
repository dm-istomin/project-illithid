(ns illithid.handlers.view-character
  (:require [re-frame.core :refer [reg-event-fx]]
            [illithid.handlers :refer [middleware]]
            [illithid.subs.view-character :refer [current-character-id]]))

(reg-event-fx
  ::prepare-spells
  middleware
  (fn [{:keys [db]} _]
    {:dispatch [:nav/push
                {:key :prepare-spells
                 :params {:character-id (current-character-id db)}}]}))

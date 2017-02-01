(ns illithid.handlers.characters
  (:require [re-frame.core :refer [reg-event-fx reg-event-db]]
            [illithid.db :as db]
            [illithid.character.core :as c]))

(reg-event-fx
  ::load-characters
  (fn [{:keys [db]} [_ & [character-ids]]]
    (if-let [character-ids (seq (or character-ids (::db/character-ids db [])))]
      {:load-storage-n {:keys character-ids
                        :into ::db/characters}}
      {})))

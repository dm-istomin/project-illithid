(ns illithid.handlers.new-character
  (:require [re-frame.core :refer [reg-event-db path]]
            [illithid.db :as db]
            [illithid.character.core :as c]
            [illithid.character.races :refer [races]]
            [illithid.character.classes :refer [classes]]
            [illithid.handlers :as h]))

(def middleware [h/middleware (path ::db/new-character)])

(reg-event-db
  ::set-name
  middleware
  (fn [db [_ new-name]] (assoc db ::c/name new-name)))

(reg-event-db
  ::set-race
  middleware
  (fn [db [_ id-or-race]]
    (assoc db ::c/race (if (keyword? id-or-race)
                         (get races id-or-race)
                         id-or-race))))

(reg-event-db
  ::set-class
  middleware
  (fn [db [_ id-or-class]]
    (assoc db ::c/class (if (keyword? id-or-class)
                         (get classes id-or-class)
                         id-or-class))))


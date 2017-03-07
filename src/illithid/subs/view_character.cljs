(ns illithid.subs.view-character
  (:require [re-frame.core :refer [reg-sub subscribe]]
            [re-frame.db :refer [app-db]]
            [illithid.db :as db]
            [illithid.character.spells :as spells]
            [illithid.character.core :as c]
            [illithid.subs.characters :as sc]))

(reg-sub
  ::current-character-id
  (fn [db _] (some->> db
                      ::db/nav
                      :routes
                      (filter #(= :character-show (:key %)))
                      first
                      :params
                      :character-id)))

(reg-sub
  ::current-character
  (fn [_ _] [app-db (subscribe [::current-character-id])])
  (fn [[db character-id] _] (get-in db [::db/characters character-id])))

(reg-sub
  ::prepared-spells
  :<- [::current-character]
  (fn [character _] (-> character c/prepared-spells vals)))


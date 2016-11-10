(ns illithid.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [reg-sub]]
            [illithid.db :as db]
            [illithid.character.core :as c]))

(reg-sub :get-character (fn [db _] (::db/character db)))

(reg-sub
  :get-abilities
  :<- [:get-character]
  (fn [character _] (::c/abilities character)))

(reg-sub
  :get-ability
  :<- [:get-abilities]
  (fn [abilities [_ ability]] (get abilities ability)))

(reg-sub
  :get-ability-modifier
  :<- [:get-character]
  (fn [character [_ ability]] (c/ability-modifier character ability)))

(ns illithid.subs.new-character
  (:require [re-frame.core :refer [reg-sub]]
            [illithid.db :as db]
            [illithid.character.core :as c]
            [illithid.character.ability :as ability]))

(reg-sub
  ::page
  (fn [db _] (-> db ::db/new-character ::db/new-character-page)))

(reg-sub
  ::previous-page
  (fn [db _] (-> db ::db/new-character ::db/previous-page)))

(reg-sub
  ::name
  (fn [db _] (-> db ::db/new-character ::c/name)))

(reg-sub
  ::race
  (fn [db _] (-> db ::db/new-character ::c/race)))

(reg-sub
  ::class
  (fn [db _] (-> db ::db/new-character ::c/class)))

(reg-sub
  ::ability
  (fn [db [_ ability]] (-> db ::db/new-character ::c/abilities ability)))

(reg-sub
  ::ability-modifier
  (fn [db [_ ability]]
    (-> db ::db/new-character ::c/abilities ability ability/modifier)))

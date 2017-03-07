(ns illithid.subs.prepared-spells
  (:require [re-frame.core :refer [reg-sub]]
            [illithid.db :as db]
            [illithid.subs.view-character :as vch]
            [illithid.character.core :as ch]
            [illithid.character.cclass :as cl]
            [illithid.character.spell :as sp]))

(reg-sub
  ::max-prepared
  :<- [::vch/current-character]
  (fn [character _] (ch/num-prepared-spells character)))

(reg-sub
  ::full?
  :<- [::max-prepared]
  :<- [::num-prepared]
  (fn [[max-prepared num-prepared] _] (>= num-prepared max-prepared)))

(reg-sub
  ::max-spell-level
  :<- [::vch/current-character]
  (fn [character _] (ch/max-spell-level character)))

(reg-sub
  ::available-spells
  :<- [::vch/current-character]
  :<- [::max-spell-level]
  (fn [[character max-spell-level] _]
    (->> character ch/class cl/spells
         (filter #(>= max-spell-level (::sp/level %))))))

(reg-sub ::prepared-spells (fn [db _] (::db/prepared-spells db)))

(reg-sub
  ::num-prepared
  :<- [::prepared-spells]
  (fn [prepared-spells _] (count prepared-spells)))

(reg-sub
  ::prepared?
  :<- [::prepared-spells]
  (fn [prepared-spells [_ spell]] (boolean (get prepared-spells spell))))

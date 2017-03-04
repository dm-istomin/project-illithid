(ns illithid.subs.prepared-spells
  (:require [re-frame.core :refer [reg-sub]]
            [illithid.db :as db]))

(reg-sub ::num-prepared (fn [db _] (-> db ::db/prepared-spells count)))
(reg-sub
  ::prepared?
  (fn [db [_ spell]] (boolean (get (::db/prepared-spells db) spell))))

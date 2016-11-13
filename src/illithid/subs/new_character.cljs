(ns illithid.subs.new-character
  (:require [re-frame.core :refer [reg-sub]]
            [illithid.db :as db]
            [illithid.character.core :as c]))

(reg-sub
  ::name
  (fn [db _] (-> db ::db/new-character ::c/name)))

(reg-sub
  ::race
  (fn [db _] (-> db ::db/new-character ::c/race)))

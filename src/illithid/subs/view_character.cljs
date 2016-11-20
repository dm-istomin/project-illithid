(ns illithid.subs.view-character
  (:require [re-frame.core :refer [reg-sub]]
            [illithid.db :as db]
            [illithid.character.core :as c]))

(reg-sub ::name (fn [db _] (-> db ::db/character ::c/name)))

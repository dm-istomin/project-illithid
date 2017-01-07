(ns illithid.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [reg-sub subscribe]]))

(reg-sub :nav/index (fn [db _] (get-in db [:illithid.db/nav :index])))
(reg-sub :nav/state (fn [db _] (:illithid.db/nav db)))

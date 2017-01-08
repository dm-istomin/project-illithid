(ns illithid.subs.nav
  (:require [re-frame.core :refer [reg-sub]]
            [illithid.db :as db]))

(reg-sub :nav/index (fn [db _] (get-in db [:illithid.db/nav :index])))
(reg-sub :nav/state (fn [db _] (:illithid.db/nav db)))

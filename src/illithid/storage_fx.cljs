(ns illithid.storage-fx
  (:require [cljs.core.async :refer [<!]]
            [re-frame.core :refer [reg-fx reg-event-db dispatch]]
            [glittershark.core-async-storage :refer [get-item set-item]])
  (:require-macros [cljs.core.async.macros :as asyncm :refer [go]]))

(reg-event-db
  ::storage-loaded
  (fn [db [_ {:keys [storage-value db-key]}]]
    (assoc-in db [:illithid.db/storage db-key] storage-value)))

(reg-fx
  :load-storage
  (fn [{storage-key :key, db-key :into}]
    (go (let [[error value] (<! (get-item storage-key))]
          (if error
            (throw error)
            (dispatch [::storage-loaded {:db-key db-key
                                         :storage-value value}]))))))

(reg-fx
  :set-storage
  (fn [{storage-key :key, storage-value :value}]
    (go (let [[error] (<! (set-item storage-key storage-value))]
          (when error (throw error))))))


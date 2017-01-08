(ns illithid.storage-fx
  (:require [cljs.core.async :refer [<!]]
            [re-frame.core :refer [reg-fx reg-event-fx dispatch]]
            [glittershark.core-async-storage :refer [get-item set-item]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(reg-event-fx
  ::storage-loaded
  (fn [{:keys [db]} [_ {:keys [storage-value db-key next-ev]}]]
    (cond->
      {:db (assoc-in db (if (coll? db-key) db-key [db-key]) storage-value)}
      next-ev (assoc :dispatch next-ev))))

(reg-fx
  :load-storage
  (fn [{storage-key :key
        default-value :or
        db-key :into
        next-ev :then-dispatch}]
    (go (let [[error value] (<! (get-item storage-key))]
          (if error
            (throw error)
            (dispatch [::storage-loaded {:db-key db-key
                                         :storage-value (or value default-value)
                                         :next-ev next-ev}]))))))

(reg-fx
  :set-storage
  (fn [{storage-key :key, storage-value :value}]
    (go (let [[error] (<! (set-item storage-key storage-value))]
          (when error (throw error))))))

(reg-fx
  :set-storage-n
  (fn [items]
    (go (doseq [{k :key, v :value} items]
          (when-let [error (first (<! (set-item k v)))]
            (throw error))))))


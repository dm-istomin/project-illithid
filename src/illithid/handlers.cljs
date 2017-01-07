(ns illithid.handlers
  (:require
    [re-frame.core :refer [reg-event-db after debug]]
    [clojure.spec :as s]
    [illithid.db :as db]))

;;;

(defn dec-to-zero
  "Same as dec if not zero"
  [arg]
  (if (< 0 arg)
    (dec arg)
    arg))

;;;

(defn validate-schema!
  "Throw an exception if db doesn't match the schema."
  [db]
  (when-not (s/valid? ::db/app-db db)
    (throw (js/Error. (str "schema check failed: "
                           (s/explain-str ::db/app-db db))))))

(def middleware [debug (after validate-schema!)])

;;;

(reg-event-db
  :initialize-db
  middleware
  (constantly db/initial))

(reg-event-db
  :nav/push
  middleware
  (fn [db [_ value]]
    (-> db
        (update-in [::db/nav :index] inc)
        (update-in [::db/nav :routes] #(conj % value)))))

(reg-event-db
  :nav/pop
  middleware
  (fn [db [_ _]]
    (-> db
        (update-in [::db/nav :index] dec-to-zero)
        (update-in [::db/nav :routes] pop))))

(reg-event-db
  :nav/home
  middleware
  (fn [db [_ _]]
    (-> db
        (assoc-in [::db/nav :index] 0)
        (assoc-in [::db/nav :routes]
                  (vector (get-in db [::db/nav :routes 0]))))))


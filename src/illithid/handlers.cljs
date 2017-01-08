(ns illithid.handlers
  (:require [re-frame.core :refer [reg-event-db reg-event-fx after debug]]
            [clojure.spec :as s]
            [illithid.db :as db]
            [illithid.storage-fx]))
;;;

(defn validate-schema!
  "Throw an exception if db doesn't match the schema."
  [db]
  (when-not (s/valid? ::db/app-db db)
    (throw (js/Error. (str "schema check failed: "
                           (s/explain-str ::db/app-db db))))))

(def middleware [debug (after validate-schema!)])

;;;

(reg-event-fx
  :initialize-db
  middleware
  (fn [_ _]
    {:db db/initial
     :load-storage {:key :character-ids
                    :or #{}
                    :into ::db/character-ids
                    :then-dispatch [::character-ids-loaded]}}))

(reg-event-db
  ::character-ids-loaded
  middleware
  (fn [{::db/keys [character-ids] :as db} _]
    (assoc db ::db/last-character-id
           (apply max
                  (or (seq (map #(->> % name (re-seq #"\d+") first js/parseInt)
                                character-ids))
                      [0])))))

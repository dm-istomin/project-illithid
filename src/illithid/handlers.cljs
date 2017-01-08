(ns illithid.handlers
  (:require [re-frame.core :refer [reg-event-db after debug]]
            [clojure.spec :as s]
            [illithid.db :as db]))
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

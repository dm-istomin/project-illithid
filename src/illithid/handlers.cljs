(ns illithid.handlers
  (:require
    [re-frame.core :refer [reg-event-db reg-event-fx after debug]]
    [clojure.spec :as s :include-macros true]
    [illithid.db :as db :refer [app-db]]
    [illithid.character.core :as c]
    [illithid.character.ability :as a]))

;;;

(defn validate-schema!
  "Throw an exception if db doesn't match the schema."
  [db]
  (when-not (s/valid? ::db/db db)
    (throw (js/Error. (str "schema check failed: "
                           (s/explain-str ::db/db db))))))

(def middleware [debug (after validate-schema!)])

;;;

(reg-event-fx
  :initialize-db
  middleware
  (fn [_ _]
    {:db app-db
     :load-storage {:key :character-ids
                    :or #{}
                    :into ::db/character-ids
                    :then-dispatch [::populate-last-character-id]}}))

(reg-event-fx
  ::populate-last-character-id
  middleware
  (fn [{{::db/keys [character-ids] :as db} :db} _]
    {:db (assoc db ::db/last-character-id
                (apply max (or (seq (map (comp js/parseInt #(subs % 1) name)
                                         character-ids))
                               [0])))
     :dispatch [::load-characters]}))

(reg-event-fx
  ::load-characters
  middleware
  (fn [{{::db/keys [character-ids] :as db} :db}]
    {:db db
     :load-storage-n
     (for [character-id character-ids]
       {:key character-id
        :into [::db/characters character-id]})}))

(reg-event-db
  ::create-character
  (fn [db _]
    (assoc db
           ::db/state ::db/new-character
           ::db/new-character {::db/new-character-page :basic-info})))

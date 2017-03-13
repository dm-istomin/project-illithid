(ns illithid.handlers.prepare-spells
  (:require [re-frame.core :refer [reg-event-db reg-event-fx]]
            [illithid.handlers :refer [middleware]]
            [illithid.db :as db]
            [illithid.character.core :as ch]))

(reg-event-db
  ::initialize
  middleware
  (fn [db _] (assoc db
                    ::db/prepared-spells #{}
                    ::db/state ::db/prepare-spells)))

(reg-event-db
  ::set-prepared
  middleware
  (fn [db [_ spell-id prepared?]]
    (update db ::db/prepared-spells (if prepared? conj disj) spell-id)))

(reg-event-fx
  ::save
  middleware
  (fn [{{::db/keys [prepared-spells] :as db} :db}
       [_ {character-id ::ch/id}]]
    {:db
     (-> db
         (assoc ::db/state ::db/home)
         (dissoc ::db/prepared-spells)
         (assoc-in [::db/characters character-id ::ch/prepared-spells]
                   prepared-spells))

     :merge-storage
     {:key character-id
      :value {::ch/prepared-spells prepared-spells}
      :then-dispatch [:nav/push {:key :character-show
                                 :params {:character-id character-id}}]}}))

(ns illithid.handlers.new-character
  (:require [re-frame.core :refer [reg-event-db path]]
            [illithid.db :as db]
            [illithid.character.core :as c]
            [illithid.character.ability :as a]
            [illithid.character.cclass :as cl]
            [illithid.character.races :refer [races]]
            [illithid.character.classes :refer [classes]]
            [illithid.handlers :as h]))

;; Save the new character to ::db/character
(reg-event-db
  ::save
  h/middleware
  (fn [{::db/keys [new-character] :as db} _]
    (let [saving-throw-proficiencies (-> new-character
                                         ::c/class
                                         ::cl/saving-throw-proficiencies)
          chr
          (-> new-character
              (select-keys
                [::c/name
                 ::c/class
                 ::c/race
                 ::c/abilities
                 ::c/skill-proficiencies])
              (assoc
                ::c/level 1
                ::c/saving-throw-proficiencies saving-throw-proficiencies))]
      (-> db
          (dissoc ::db/new-character)
          (assoc ::db/character chr
                 ::db/state ::db/view-character)))))

(def middleware [h/middleware (path ::db/new-character)])

(reg-event-db
  ::set-page
  middleware
  (fn [db [_ new-page]]
    (cond-> db
      true
      (assoc ::db/new-character-page new-page
             ::db/previous-page (::db/new-character-page db))
      (= new-page :abilities)
      (assoc ::c/abilities
             (into {} (for [ability a/abilities] [ability 10])))
      (= new-page :proficiencies)
      (assoc ::c/skill-proficiencies #{}))))

(reg-event-db
  ::set-name
  middleware
  (fn [db [_ new-name]] (assoc db ::c/name new-name)))

(reg-event-db
  ::set-race
  middleware
  (fn [db [_ id-or-race]]
    (assoc db ::c/race (if (keyword? id-or-race)
                         (get races id-or-race)
                         id-or-race))))

(reg-event-db
  ::set-class
  middleware
  (fn [db [_ id-or-class]]
    (assoc db ::c/class (if (keyword? id-or-class)
                         (get classes id-or-class)
                         id-or-class))))

(reg-event-db
  ::set-ability
  middleware
  (fn [db [_ ability value]]
    (assoc-in db [::c/abilities ability] value)))

(reg-event-db
  ::inc-ability
  middleware
  (fn [db [_ ability]]
    (update-in db [::c/abilities ability]
               #(if (= % a/max-natural-ability) % (inc %)))))

(reg-event-db
  ::dec-ability
  middleware
  (fn [db [_ ability]]
    (update-in db [::c/abilities ability] #(if (= % 1) % (dec %)))))

(reg-event-db
  ::set-skill-proficiency
  middleware
  (fn [db [_ skill proficient?]]
    (update db ::c/skill-proficiencies (if proficient? conj disj) skill)))


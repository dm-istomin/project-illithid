(ns illithid.handlers.new-character-test
  (:require [cljs.test :refer-macros [deftest is testing]]
            [clojure.spec :as s]
            [com.gfredericks.test.chuck.clojure-test :refer-macros [checking]]
            [clojure.test.check.generators :as gen]
            [illithid.test-helpers.spec-check :refer-macros [is-valid]]
            [illithid.handlers.new-character :as handlers]
            [illithid.character.core :as ch]
            [illithid.character.cclass :as cl]
            [illithid.character.race :as r]
            [illithid.db :as db]))

(deftest save-test
  (checking "the resulting updated database" 5
    [character' (s/gen ::ch/character)
     db' (s/gen ::db/app-db)
     :let [character (dissoc character' ::ch/id)
           orig-db (assoc db' ::db/new-character character)
           {:keys [db]} (handlers/save {:db orig-db} [::handlers/save])]]
    (letfn [(to-match [x]
              (select-keys x [::ch/name ::cl/id ::r/id ::ch/abilities
                              ::ch/skill-proficiencies]))
            (matches-character? [candidate]
              (= (to-match character) (to-match candidate)))]

      (is-valid ::db/app-db db)

      (is (some matches-character? (-> db ::db/characters vals))
        "Adds to the ::db/characters map")

      (let [generated-id (->> db ::db/characters
                              (filter (comp matches-character? second))
                              first first)]

        (is (= (-> generated-id name (subs 1) js/parseInt)
               (::db/last-character-id db))
            "Sets the :last-character-id to the generated ID for the character")

        (is (some #{generated-id} (::db/character-ids db))
            "Adds the generated ID to the ::db/character-ids set")))))


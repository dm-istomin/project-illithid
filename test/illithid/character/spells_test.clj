(ns illithid.character.spells-test
  (:require [clojure.test :refer :all]
            [clojure.spec :as s]
            [illithid.character.spells :refer [spells]]
            [illithid.character.spell :as spell]))

(deftest test-spells-are-all-valid-spells
  (doseq [[_ spell] spells]
    (is (s/valid? ::spell/spell spell) (s/explain-str ::spell/spell spell))))

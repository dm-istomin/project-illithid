(ns illithid.character.races
  (:require #?@(:clj [[clojure.spec :as s]
                      [illithid.resources :refer [resource-macro]]])
            [illithid.character.race :as r])
  #?(:cljs (:require-macros [illithid.character.races :refer [defrace]])))

#?(:clj (resource-macro race :folder "races" :spec ::r/race))

(defrace human)
(defrace dwarf)

(def races {:human human
            :dwarf dwarf})

(ns illithid.character.spells
  (:require #?@(:clj [[clojure.spec :as s]
                      [illithid.resources :refer [defresources]]])
            [illithid.character.spell :as sp])
  #?(:cljs (:require-macros [illithid.resources :refer [defresources]])))

(defresources spells, :spec ::sp/spell)

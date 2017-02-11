(ns illithid.character.spells
  (:require #?@(:clj [[clojure.spec :as s]
                      [illithid.resources :refer [defresources]]]))
  #?(:cljs (:require-macros [illithid.resources :refer [defresources]])))

(defresources spells)

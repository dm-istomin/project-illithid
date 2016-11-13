(ns illithid.character.classes
  (:require #?@(:clj [[clojure.spec :as s]
                      [illithid.resources :refer [resource-macro]]])
            [illithid.character.cclass :as c])
  #?(:cljs (:require-macros [illithid.character.classes :refer [defclass]])))

#?(:clj (resource-macro class :folder "classes" :spec ::c/class))

(defclass cleric)

(def classes {:cleric cleric})

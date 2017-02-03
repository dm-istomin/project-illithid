(ns illithid.scenes.character.show
  (:require [re-frame.core :refer [subscribe dispatch]]
            [illithid.subs.characters :as sub]
            [illithid.character.core :as ch]
            [illithid.components.native :refer [view text]]
            [illithid.components.view-character.core :refer [show-character]]))

(defn character-show-scene [params]
  (let [character-id (-> params
                         (aget "character-id")
                         (->> (keyword "illithid.character")))
        character (subscribe [::sub/get-character character-id])]
    (fn [] [show-character @character])))

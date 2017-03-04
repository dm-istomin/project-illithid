(ns illithid.scenes.character.prepare-spells
  (:require [re-frame.core :refer [subscribe]]
            [illithid.subs.characters :as sub]
            [illithid.components.view-character.prepare-spells
             :refer [prepare-spells]]))

(defn prepare-spells-scene [params]
  (let [character-id (-> params
                         (aget "character-id")
                         (->> (keyword "illithid.character")))
        character (subscribe [::sub/get-character character-id])]
    (fn [] [prepare-spells @character])))

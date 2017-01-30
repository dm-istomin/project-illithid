(ns illithid.scenes.spells.detail
  (:require [illithid.components.spells.detail :refer [spell-detail]]
            [illithid.character.spells :refer [spells]]))

(defn spell-detail-scene [params]
  (let [spell (-> params (aget "spell-id") keyword spells)]
    [spell-detail spell]))

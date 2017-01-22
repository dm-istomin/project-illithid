(ns illithid.scenes.spells.list
  (:require [illithid.components.spells.list :refer [spell-list]]
            [illithid.character.spells :refer [spells]]))

(defn spell-list-scene [params]
  [spell-list spells])

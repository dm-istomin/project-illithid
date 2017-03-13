(ns illithid.scenes.spells.list
  (:require [illithid.components.spells.list :refer [spell-list]]
            [illithid.character.spells :as spells]
            [illithid.character.cclass :as cl]
            [illithid.character.classes :refer [classes]]))

(defn spell-list-scene [params]
  (let [params (js->clj params :keywordize-keys true)
        spells (if-let [cls (-> params :id keyword classes)]
                 (cl/spells cls)
                 (vals spells/spells))]
    [spell-list {:spells spells}]))

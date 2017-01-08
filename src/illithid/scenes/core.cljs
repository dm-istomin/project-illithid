(ns illithid.scenes.core
  (:require [re-frame.core :refer [dispatch]]
            [illithid.scenes.home :as home]
            [illithid.scenes.character.index :refer [character-index]]
            [illithid.scenes.character.new :as new-character]
            [illithid.components.native :refer [view text]]
            [illithid.components.spells.detail :refer [spell-detail]]
            [illithid.components.spells.list :refer [spell-list]]
            [illithid.character.spells :refer [spells]]
            [illithid.components.ui :refer [button
                                            card
                                            card-body
                                            subheader]]))

(defn spell-list-scene [params] (spell-list spells))

(defn spell-detail-scene [params]
  (let [spell-data (-> params
                       (aget "spell-data")
                       (js->clj :keywordize-keys true))]
    [spell-detail spell-data]))

(def routes
  {:home home/home

   :characters-index character-index
   :characters-new-basic-info new-character/basic-info
   :characters-new-abilities new-character/abilities
   :characters-new-proficiencies new-character/proficiencies

   :spell-detail spell-detail-scene
   :spell-list spell-list-scene})


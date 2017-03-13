(ns illithid.scenes.character.prepare-spells
  (:require [re-frame.core :refer [subscribe]]
            [illithid.subs.view-character :as sub]
            [illithid.components.view-character.prepare-spells
             :refer [prepare-spells]]))

(defn prepare-spells-scene [params] [prepare-spells])

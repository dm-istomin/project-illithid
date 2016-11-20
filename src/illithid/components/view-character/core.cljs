(ns illithid.components.view-character.core
  (:require [re-frame.core :refer [subscribe]]
            [illithid.subs.view-character :as sub]
            [illithid.components.native :refer [text]]))

(defn view-character []
  (let [character-name (subscribe [::sub/name])]
    (fn []
      [text "Viewing " @character-name])))


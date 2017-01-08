(ns illithid.scenes.character.index
  (:require [re-frame.core :refer [dispatch]]
            [illithid.components.native :refer [view text]]
            [illithid.components.ui :refer [button]]))

(defn character-index []
  [view [text "here are som chars, bro"]
   [button {:text "New Character"
            :on-press #(dispatch
                         [:nav/push :characters-new-basic-info])}]])

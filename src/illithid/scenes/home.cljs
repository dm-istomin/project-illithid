(ns illithid.scenes.home
  (:require [re-frame.core :refer [dispatch]]
            [illithid.components.native :refer [view text]]
            [illithid.components.ui :refer [button]]))

(defn home [params]
  [view
   [text "Welcome to Project Illithid! This is a general navigation screen."]
   [button {:text "Next Scene"
            :on-press #(dispatch [:nav/push :another-scene])}]
   [button {:text "Spell List"
            :on-press #(dispatch [:nav/push :spell-list])}]
   [button {:text "Characters"
            :on-press #(dispatch [:nav/push :characters-index])}]])

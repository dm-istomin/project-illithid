(ns illithid.scenes.home
  (:require [re-frame.core :refer [dispatch]]
            [illithid.components.native :refer [view text]]
            [illithid.components.ui :refer [button]]))

(defn home [params]
  [view
   [text "Welcome to Project Illithid! This is a general navigation screen."]
   [button {:text "Next Scene"
            :on-press #(dispatch [:nav/push {:key :another-scene
                                             :title "Another Scene"}])}]
   [button {:text "Spell List"
            :on-press #(dispatch [:nav/push {:key :spell-list
                                             :title "Spell List"}])}]
   [button {:text "Characters"
            :on-press #(dispatch [:nav/push {:key :characters-index
                                             :title "Characters"}])}]])

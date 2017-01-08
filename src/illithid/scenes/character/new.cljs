(ns illithid.scenes.character.new
  (:require [re-frame.core :refer [dispatch]]
            [illithid.handlers.new-character :as pub]
            [illithid.components.native :refer [view text]]
            [illithid.components.ui :refer [button]]
            [illithid.components.new-character.basic-info :as bi]
            [illithid.components.new-character.abilities :as ab]
            [illithid.components.new-character.proficiencies :as pr]))

(defn- next-button []
  [button {:text "Next", :on-press #(dispatch [::pub/next-page])}])

(defn basic-info []
  (dispatch [::pub/initialize])
  (fn []
    [view
     [bi/basic-info]
     [next-button]]))

(defn abilities []
  [view
   [ab/abilities]
   [next-button]])

(defn proficiencies []
  [view
   [pr/proficiencies]
   [button {:text "Save"
            :on-press #(dispatch [::pub/save])}]])

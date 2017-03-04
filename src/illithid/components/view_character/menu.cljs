(ns illithid.components.view-character.menu
  (:require [re-frame.core :refer [dispatch subscribe]]
            [illithid.subs.view-character :as vch]
            [illithid.react :refer [action-sheet-ios]]))

(def actions
  {"Prepare Spells"
   #(dispatch [:nav/push {:key :prepare-spells
                          :params {:character-id
                                   @(subscribe [::vch/current-character])}}])})

(defn show []
  (.showActionSheetWithOptions
    action-sheet-ios
    #js{:options (apply array (-> actions keys (concat ["Cancel"])))
        :cancelButtonIndex (count actions)}
    #(when-let [action (some-> actions seq (nth % nil) val)]
       (action))))

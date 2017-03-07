(ns illithid.components.view-character.menu
  (:require [re-frame.core :refer [dispatch subscribe]]
            [illithid.handlers.view-character :as pub]
            [illithid.react :refer [action-sheet-ios]]))

(def actions
  {"Prepare Spells"
   #(dispatch [::pub/prepare-spells])})

(defn show []
  (.showActionSheetWithOptions
    action-sheet-ios
    #js{:options (apply array (-> actions keys (concat ["Cancel"])))
        :cancelButtonIndex (count actions)}
    #(when-let [action (some-> actions seq (nth % nil) val)]
       (action))))

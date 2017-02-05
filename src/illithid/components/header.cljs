(ns illithid.components.header
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch]]
            [illithid.platform :as platform :include-macros true]
            [illithid.components.ui :refer [toolbar]]
            [illithid.components.native
             :refer [view text navigation-header navigation-header-title]]))

(defn header [props]
  (let [props (js->clj props :keywordize-keys true)]
    (platform/cond
      :android
      (let [opts {:title (-> props :scene :route :title)
                  :overrides {:background-color "#08708a"
                              :color "#f5f5f5"
                              :left-icon-color "#f5f5f5"}
                  :actions (if-let [action (-> props :scene :route :action)]
                             [action]
                             [])}
            opts (if (= "home" (-> props :scene :route :key)) opts
                   (assoc opts
                          :on-icon-press #(dispatch [:nav/pop])
                          :icon "arrow-back"))]
        [toolbar opts])
      :ios
      [navigation-header
       (assoc
         props
         :render-title-component
         #(r/as-element
            [navigation-header-title (-> props :scene :route :title)])
         :on-navigate-back #(dispatch [:nav/pop]))]
      #_[view
       [text (-> props :scene :route :title)]])))


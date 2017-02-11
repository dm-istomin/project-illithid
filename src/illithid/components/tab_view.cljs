(ns illithid.components.tab-view
  (:require [reagent.core :as r]))

(when (exists? js/require)
  (def react-native-scrollable-tab-view
    (r/adapt-react-class (js/require "react-native-scrollable-tab-view")))

  (def ^:private react (js/require "react")))

(defn tab-view [props & children]
  (let [[props children] (if (map? props)
                           [props children]
                           [{} (cons props children)])
        titles-and-comps (map (juxt (comp :tab-label meta) r/as-element)
                              children)
        comps (for [[title component] titles-and-comps]
                (.cloneElement react component #js{:tabLabel title}))]
    (apply vector react-native-scrollable-tab-view props comps)))


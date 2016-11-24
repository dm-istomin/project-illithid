(ns illithid.android.ui
  (:require [reagent.core :as r]))

(set! js/React (js/require "react-native"))
(set! js/MaterialDesign (js/require "react-native-material-design"))

(def toolbar (r/adapt-react-class (.-Toolbar js/MaterialDesign)))

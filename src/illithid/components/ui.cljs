(ns illithid.components.ui
  (:require [reagent.core :as r]))

(when (exists? js/require)
  (def MaterialDesign
    (js/require "react-native-material-design"))

  ; Material Design Components
  (def toolbar (r/adapt-react-class (.-Toolbar MaterialDesign)))
  (def button (r/adapt-react-class  (.-Button MaterialDesign)))
  (def card (r/adapt-react-class  (.-Card MaterialDesign)))
  (def card-body (r/adapt-react-class (.-Card.Body MaterialDesign)))
  (def drawer (r/adapt-react-class (.-Drawer MaterialDesign)))
  (def subheader (r/adapt-react-class (.-Subheader MaterialDesign)))
  (def icon (r/adapt-react-class (.-Icon MaterialDesign)))
  (def ripple (r/adapt-react-class (.-Ripple MaterialDesign))))

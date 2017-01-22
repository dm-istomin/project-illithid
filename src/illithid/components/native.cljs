(ns illithid.components.native
  (:require [reagent.core :as r]
            [illithid.react :refer [react native?]])
  (:require-macros [illithid.components.native :refer [defclasses]]))

(defclasses
  text Text
  view View
  image Image
  touchable-highlight TouchableHighlight
  touchable-native-feedback TouchableNativeFeedback
  pan-responder PanResponder
  picker Picker
  scroll-view ScrollView
  navigator Navigator
  text-input TextInput
  list-view ListView
  switch Switch)

(def DataSource
  (or (some-> react (aget "ListView") (aget "DataSource"))
      (fn [] (js-obj))))

;; TODO: update `defclass` to support nested components

(def navigation-bar
  (when native?
    (r/adapt-react-class
      (-> react (aget "Navigator") (aget "NavigationBar")))))

(def picker-item
  (when native?
    (r/adapt-react-class
      (-> react (aget "Picker") (aget "Item")))))

(defn add-back-listener [h]
  (when native?
    (.addEventListener (-> react (aget "BackAndroid")) "hardwareBackPress" h)))

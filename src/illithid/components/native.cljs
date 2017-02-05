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
  switch Switch
  navigation-bar [Navigator NavigationBar]
  picker-item [Picker Item])

(def DataSource
  (or (some-> react (aget "ListView") (aget "DataSource"))
      (fn [] (js-obj))))

(def NavigationExperimental (.-NavigationExperimental react))

;;;

(defn add-back-listener [h]
  (when native?
    (.addEventListener (-> react (aget "BackAndroid")) "hardwareBackPress" h)))

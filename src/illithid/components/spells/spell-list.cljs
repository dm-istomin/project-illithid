(ns illithid.components.spells.list
  (:require [re-frame.core :refer [dispatch]]
            [reagent.core :as r]
            [clojure.string :as string]
            [illithid.components.native :refer [view text image list-view]]
            [illithid.character.spell :as sp]
            [illithid.components.ui :refer [ripple]]))

(def logo-img (js/require "./images/magic-missle.png"))

(defn format-classes [classes]
  (string/join ", " (map #(-> % name string/capitalize) classes)))

(def style {:view {:flex-direction "row"
                   :background-color "white"
                   :border-bottom-width 1
                   :border-color "lightgrey"
                   :align-items "center"
                   :padding 10}
            :img {:height 45
                  :width 45
                  :margin-right 10
                  :border-width 3
                  :border-color "darkgrey"
                  :border-radius 25}
            :text {:font-weight "bold"}})

(defn spell-list-item [spell]
  [ripple {:key (:name spell)
           :style (:view style)
           :on-press #(dispatch [:nav/push {:key :spell-detail
                                            :title "Spell Detail"
                                            :params {:spell-data spell}}])}
   [image {:style (:img style) :source logo-img}]
   [view {:style {:flex-direction "column"}}
    [text {:style (:text style)} (-> :name spell .toUpperCase)]
    [text {:style {:color "darkgrey"
                   :font-size 13}}
     (str "Level " (:level spell) " • "
          (format-classes (:classes spell)))]]])

(def data-source
  (React.ListView.DataSource. #js{:rowHasChanged (fn [a b] false)}))

(defn render-row [spell]
  (r/as-element (spell-list-item (js->clj spell :keywordize-keys true))))

(defn spell-list [spells]
  [list-view {:render-row #(render-row %)
     :dataSource (.cloneWithRows data-source (clj->js spells))}])

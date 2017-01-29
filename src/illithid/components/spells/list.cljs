(ns illithid.components.spells.list
  (:require [re-frame.core :refer [dispatch]]
            [reagent.core :as r]
            [clojure.string :as string]
            [illithid.components.native
             :refer [view text image list-view DataSource]]
            [illithid.character.spell :as sp]
            [illithid.components.spells.logo-image :as logo-image]
            [illithid.components.ui :refer [ripple]]))

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
                                            :params {:spell-data spell}}])}
   [image {:style (:img style) :source logo-image/source}]
   [view {:style {:flex-direction "column"}}
    [text {:style (:text style)} (-> :name spell .toUpperCase)]
    [text {:style {:color "darkgrey"
                   :font-size 13}}
     (str "Level " (:level spell) " • "
          (format-classes (:classes spell)))]]])

(def data-source (DataSource. #js{:rowHasChanged (fn [a b] false)}))

(defn render-row [spell]
  (r/as-element (spell-list-item (js->clj spell :keywordize-keys true))))

(defn spell-list [spells]
  [list-view
   {:render-row #(render-row %)
    :dataSource (.cloneWithRows
                  data-source
                  (->> spells vals (sort-by ::sp/name) clj->js))}])

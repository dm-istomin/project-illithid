(ns illithid.components.spells.list
  (:require [re-frame.core :refer [dispatch]]
            [reagent.core :as r]
            [clojure.string :as string]
            [illithid.components.native
             :refer [view text image list-view DataSource touchable-highlight]]
            [illithid.character.spell :as sp]
            [illithid.character.cclass :as c]
            [illithid.character.classes :refer [classes]]
            [illithid.components.spells.logo-image :as logo-image]
            [illithid.components.ui :refer [ripple]]))

;; TODO once we have all the class definition files
;; (defn format-classes [class-list]
;;   (->> class-list
;;       (map classes)
;;       (map ::c/name)
;;       (string/join ", ")))
(defn- format-classes [classes]
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

(defn- spell-level [{::sp/keys [level]}]
  (if (zero? level) "Cantrip" (str "Level " level)))

(defn- spell-list-item [{:keys [spell action]}]
  [touchable-highlight {:key (::sp/name spell)
           :style (:view style)
           :on-press #(dispatch [:nav/push
                                 {:key :spell-detail
                                  :params {:spell-id (::sp/id spell)}}])}
   [view
    [image {:style (:img style) :source logo-image/source}]
    [view {:style {:flex-direction "column"}}
     [text {:style (:text style)} (-> spell ::sp/name .toUpperCase)]
     [text {:style {:color "darkgrey"
                    :font-size 13}}
      (str (spell-level spell) " • "
           (format-classes (::sp/classes spell)))]
     action]]])

(def data-source (DataSource. #js{:rowHasChanged (fn [a b] false)}))

(defn- render-row [{:keys [spell render-action]}]
  (r/as-element
    [spell-list-item {:spell spell
                      :action (when render-action [render-action spell])}]))

(defn spell-list [{:keys [spells render-action]}]
  [list-view
   {:render-row #(render-row {:spell %, :render-action render-action})
    :dataSource (.cloneWithRows
                  data-source
                  (->> spells
                       (sort-by (juxt ::sp/level ::sp/name))
                       (apply array)))}])

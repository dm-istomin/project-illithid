(ns illithid.components.spells.detail
  (:require [re-frame.core :refer [dispatch]]
            [clojure.string :as string]
            [illithid.components.native :refer [view text image scroll-view]]
            [illithid.character.spell :as sp]
            [illithid.components.ui :refer [icon ripple]]))

(def logo-img (js/require "./images/magic-missle.png"))

(def style {:view {:height 604
                   :padding-left 15
                   :padding-right 15
                   :background-color "white"}
            :title {:font-weight "bold"}
            :img {:border-radius 75
                  :border-width 5
                  :border-color "#ccc"
                  :height 115
                  :width 115}
            :header {:font-weight "bold"
                     :width 210
                     :font-size 22}
            :dmg-type-label {:background-color "navy"
                             :font-size 12
                             :color "white"
                             :text-align "center"
                             :padding-bottom -5
                             :padding-left 5
                             :padding-right 5
                             :margin-left 5
                             :font-weight "bold"}
            :table {:flex-wrap "wrap"
                    :flex-direction "row"
                    :justify-content "center"
                    :border-color "#ddd"
                    :border-bottom-width 3
                    :border-top-width 3
                    :padding 10
                    :padding-left 0}
            :body {:padding 40}})

(defn format-spell-school [school] (-> school name string/capitalize))

(defn format-classes [classes]
  (string/join ", " (map #(-> % name string/capitalize) classes)))

(defn format-spell-components [components]
  (string/join ", "
               (map (fn [c] (case (keyword c)
                              :V "Verbal"
                              :S "Somatic"
                              :M "Material"))
                    components)))

(defn spell-detail [props]
  [scroll-view {:style (:view style)}
   [view {:style {:flex-direction "row"
                  :padding 10}}
    [image {:source logo-img :style (:img style)}]
    [view {:style {:margin-left 50}}
      [text {:style (:header style)} (:name props)]
      [view {:style {:flex-direction "row" :margin-top 15}}
        [icon {:name "redo" :size 16 :style {:margin-right 3}}]
        [text {:style {:margin-right 5 :font-weight "bold"}} "RANGE"]
        [text (:range props)]]
      [view {:style {:flex-direction "row"}}
        [icon {:name "timer" :size 16 :style {:margin-right 3}}]
        [text {:style {:margin-right 5 :font-weight "bold"}} "CASTING TIME"]
        [text (or (:casting-time props) "–")]]]]
    [view {:style (:table style)}
     [view {:style {:width 120}}
      [text {:style (:title style)} "LEVEL"]
      [text {:style (:title style)} "CLASSES"]
      [text {:style (:title style)} "SCHOOL"]
      [text {:style (:title style)} "DURATION"]
      [text {:style (:title style)} "COMPONENTS"]
      (if (:material-component props)
         [text {:style (:title style)} "MATERIAL"])]
     [view {:style {:width 180}}
      [text  (props :level)]
      [text  (format-classes (:classes props))]
      [text  (format-spell-school (:school props))]
      [text  (or (:duration props) "–")]
      [text  (format-spell-components (:components props))]
      (if (:material-component props)
        [text {:style {:font-style "italic"}}
          (:material-component props)])]]
    [text {:style (:body style)} (:description props)]])

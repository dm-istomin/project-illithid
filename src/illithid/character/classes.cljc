(ns illithid.character.classes
  (:require #?@(:clj [[clojure.java.io :refer [resource file]]
                      [clojure.edn :as edn]
                      [clojure.spec :as s]
                      [illithid.character.cclass :as c]]))
  (:import [java.io FileReader PushbackReader])
  #?(:cljs (:require-macros [illithid.character.classes :refer [defclass]])))

#?(:clj (defn read-resource [filename]
          (-> filename resource file FileReader. PushbackReader. edn/read)))

(defmacro splice-resource [filename] (read-resource filename))

(defmacro splice-class [class-name]
  (let [filename (str "classes/" class-name ".edn")]
    (s/assert* ::c/class (read-resource filename))))

(defmacro defclass [class-name]
  `(def ~class-name (splice-class ~class-name)))

(defclass cleric)

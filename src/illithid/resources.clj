(ns illithid.resources
  (:require [clojure.java.io :refer [resource file]]
            [clojure.edn :as edn]
            [clojure.spec :as s])
  (:import [java.io FileReader PushbackReader]))

(defn read-resource [filename]
  (-> filename resource file FileReader. PushbackReader. edn/read))

(defmacro splice-resource [filename] (read-resource filename))

(defmacro resource-macro [n & {:keys [folder spec] :or {folder (str n "s")}}]
  (let [assert-form (fn [body] (if spec `(s/assert* ~spec ~body) body))
        filename (gensym "filename")]
    `(defmacro ~(symbol (str "def" n)) [resource-name#]
       (let [~filename (str ~folder "/" resource-name# ".edn")
             resource# ~(assert-form `(read-resource ~filename))]
         (list 'def resource-name# resource#)))))


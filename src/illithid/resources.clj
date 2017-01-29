(ns illithid.resources
  "Utilities for splicing EDN from resource files into Clojurescript"
  (:require [clojure.java.io :refer [reader resource file]]
            [clojure.edn :as edn]
            [clojure.string :refer [split]]
            [clojure.spec :as s]
            [illithid.lib.core :refer [try-require-ns]])
  (:import [java.io File PushbackReader]))

(defn read-resource [filename]
  (-> filename resource file reader PushbackReader. edn/read))

(defn read-resources [dir] ;; -> [{:name filename, :body edn-file-body}, ...]
  (for [^File f (->> dir resource file .listFiles)]
    (with-open [rd (-> f reader PushbackReader.)]
      {:name (.getName f)
       :body (edn/read rd)})))

(defn resource-map [dir & {:keys [spec]}];;->{:file-name-1 edn-file-body-1, ...}
  (into
    {}
    (for [{filename :name, body :body} (read-resources dir)]
      (let [fname (-> filename (split #"\.") first)]
        (when spec
          (try-require-ns spec)
          (try
            (s/assert* spec body)
            (catch Exception e
              (when (re-matches #"^Unable to resolve spec:.*$" (.getMessage e))
                (.println *err* (str "WARNING: " (.getMessage e)))))))
        [(keyword fname) body]))))

(defmacro splice-resource [filename] (read-resource filename))

(defmacro defresources [dir & {:keys [spec]}]
  `(def ~dir ~(-> dir name (resource-map :spec spec))))

(defmacro resource-macro [n & {:keys [folder spec] :or {folder (str n "s")}}]
  (let [assert-form (fn [body] (if spec `(s/assert* ~spec ~body) body))
        filename (gensym "filename")]
    `(defmacro ~(symbol (str "def" n)) [resource-name#]
       (let [~filename (str ~folder "/" resource-name# ".edn")
             resource# ~(assert-form `(read-resource ~filename))]
         (list 'def resource-name# resource#)))))


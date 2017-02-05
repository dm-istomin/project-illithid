(ns illithid.components.native
  (:require [clojure.spec :as s]
            [clojure.core.match :refer [match]]))

(do ;; specs {{{
  (s/def ::react-class-spec (s/or :react-property simple-symbol?
                                  :nested-property (s/coll-of simple-symbol?
                                                              :kind vector?
                                                              :into []
                                                              :min-count 1)))
  (s/def ::defclass-args
    (s/cat :var-name simple-symbol?
           :class-name ::react-class-spec))

  (s/def ::defclasses-args (s/cat :classes (s/* ::defclass-args))))
;; }}}

(defmacro defclass
  "Define `var-name` as an adapted React class based on js/React.<class-name>

   `class-name` can either be a symbol naming an attribute of js/react-native,
   or a vector of symbols naming a nested path of attributes in js/react-native"
  [var-name class-name]
  (let [property
        (match [(s/conform ::react-class-spec class-name)]
          [[:react-property p]]   `(aget illithid.react/react ~(name p))
          [[:nested-property ps]] `(aget illithid.react/react ~@(map name ps)))]
    `(def ~var-name
       (when illithid.react/native?
         (reagent.core/adapt-react-class ~property)))))

(s/fdef defclass :args ::defclass-args)

(defmacro defclasses
  "Define pairs of arguments as adapted react classes as per `defclass`.

   The second half of the pair can either be a symbol naming an attribute of
   js/react-native, or a vector of symbols naming a nested path of attributes in
   js/react-native"
  {:arglists '([var-name attr & var-name attr ...])}
  [& args]
  {:pre  [(even? (count args))]
   :post [(= (+ 2 ;; the +2 are for the `do` and the `nil`
                (/ (count args) 2)) (count %))]}
  (let [{:keys [classes]} (s/conform ::defclasses-args args)]
    `(do
       ~@(for [{:keys [var-name class-name]} classes]
           `(defclass ~var-name ~(second class-name)))
       nil)))

(s/fdef defclasses :args ::defclasses-args)

; vim:fdm=marker:fmr={{{,}}}:


(ns illithid.lib.core
  #?(:clj (:import [java.io FileNotFoundException])))

(defn element-after
  "Get the element after `a-value` in `a-vector`"
  [a-vector a-value]
  (->> a-vector
       (partition 2 1 nil)
       (filter (comp #{a-value} first))
       first second))

(defn dec-to-zero
  "Same as dec if not zero"
  [arg]
  (if (pos? arg) (dec arg) arg))

#?(:clj
    (defn try-require-ns
      "Try to require the namespace named by the namespace of symbol or keyword
       `has-ns`. Returns nil if the namespace in question doesn't exist"
      [has-ns]
      (if-let [nspc (some-> has-ns namespace symbol)]
        (when-not (find-ns nspc)
          (try (require nspc)
               (catch FileNotFoundException _)))
        (throw (RuntimeException.
                 "Argument to `try-require-ns` must have a namespace")))))

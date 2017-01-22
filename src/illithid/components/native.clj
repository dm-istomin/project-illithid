(ns illithid.components.native)

(defmacro defclass
  "Define `var-name` as an adapted React class based on js/React.<attr>"
  [var-name attr]
  `(def ~var-name
     (when illithid.react/native?
       (reagent.core/adapt-react-class
         (aget illithid.react/react ~(name attr))))))

(defmacro defclasses
  "Define pairs of arguments as adapted react classes as per `defclass`"
  {:arglists '([var-name attr & var-name attr ...])}
  [& args]
  {:pre  [(even? (count args))]
   :post [(= (+ 1 ;; the +1 is for the `do`
                (/ (count args) 2)) (count %))]}
  `(do
     ~@(for [[var-name attr] (partition 2 args)]
         `(defclass ~var-name ~attr))))

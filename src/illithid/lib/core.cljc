(ns illithid.lib.core)

(defn element-after
  "Get the element after `a-value` in `a-vector`"
  [a-vector a-value]
  (->> a-vector
       (partition 2 1 nil)
       (filter (comp #{a-value} first))
       first second))

(ns cljsjs.react)

(when-not (or (exists? js/React) (exists? js/require))
  (aset js/window "React" #js{}))

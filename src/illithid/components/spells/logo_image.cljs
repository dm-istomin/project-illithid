(ns illithid.components.spells.logo-image)

(def source (when (exists? js/require)
              (js/require "./images/magic-missle.png")))

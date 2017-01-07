(ns illithid.scenes.character.new
  (:require [re-frame.core :refer [dispatch]]
            [illithid.components.native :refer [view text]]
            [illithid.components.new-character.basic-info :as bi]))

(defn basic-info []
  (dispatch [:illithid.handlers.new-character/initialize])
  (fn [] [bi/basic-info]))

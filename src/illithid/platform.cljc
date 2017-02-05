(ns illithid.platform
  (:refer-clojure :exclude [cond])
  #?(:cljs (:require [illithid.react :refer [react]])))

#?(
   :cljs
   (do
     (def ^:private platform* (.-Platform react))
     (def platform {:os (.-OS platform*)
                    :version (.-Version platform*)})

     (def ios? (= "ios" (:os platform)))
     (def android? (= "android" (:os platform))))

   :clj
   (declare ios? android?))

#?(:clj
    (defmacro cond [& {:keys [ios android]}]
      `(clojure.core/cond
         ios? ~ios
         android? ~android)))

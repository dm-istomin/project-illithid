(ns illithid.specs.reagent
  (:require [clojure.spec :as s]))

(s/def :reagent/component-args
  (s/cat :opts (s/? map?)
         :children (s/*
                     (s/or :element :reagent/hiccup
                           :text string?
                           :element-list (s/coll-of :reagent/hiccup)))))

(s/def :reagent/hiccup
  (s/cat :element (s/or :component :reagent/component-fn
                        :dom-node keyword?)
         :args :reagent/component-args))

(s/def :reagent/component-fn
  (s/fspec :args :reagent/component-args
           :ret (s/or :form-1 :reagent/hiccup
                      :form-2 (s/fspec :args :reagent/component-args
                                       :ret :reagent/hiccup)
                      ;;:form-3 TODO
                      )))

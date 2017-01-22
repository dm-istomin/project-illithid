(ns illithid.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [illithid.handlers.nav-test]))

(doo-tests 'illithid.handlers.nav-test)

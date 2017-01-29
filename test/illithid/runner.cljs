(ns illithid.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [illithid.handlers.nav-test]
              [illithid.handlers.new-character-test]))

(doo-tests 'illithid.handlers.nav-test
           'illithid.handlers.new-character-test)

(ns session-command.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [session-command.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[session-command started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[session-command has shut down successfully]=-"))
   :middleware wrap-dev})

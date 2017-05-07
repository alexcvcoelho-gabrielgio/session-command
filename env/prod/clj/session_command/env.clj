(ns session-command.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[session-command started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[session-command has shut down successfully]=-"))
   :middleware identity})

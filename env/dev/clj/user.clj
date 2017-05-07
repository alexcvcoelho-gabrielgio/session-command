(ns user
  (:require [mount.core :as mount]
            session-command.core))

(defn start []
  (mount/start-without #'session-command.core/repl-server))

(defn stop []
  (mount/stop-except #'session-command.core/repl-server))

(defn restart []
  (stop)
  (start))



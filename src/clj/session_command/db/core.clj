(ns session-command.db.core
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.operators :refer :all]
            [monger.query :refer :all]
            [mount.core :refer [defstate]]
            [session-command.config :refer [env]]))


(defstate db
          :start (mg/connect-via-uri (env :mongo))
          :stop (mg/disconnect db))

(defn get-session [id]
  (mc/find-one-as-map (:db db) "session" {:uuid id}))

(defn get-active []
  (with-collection (:db db) "session"
                   (find {})
                   (sort (array-map :data_ins -1))
                   (limit 110)))
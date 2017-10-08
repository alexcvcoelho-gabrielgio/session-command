(ns session-command.kafka.core
  (:require [kinsky.client :as client]
            [session-command.config :refer [env]]
            [clojure.core.async :as a]
            [jkkramer.verily :as v]))

(def s-validate (v/validations->fn [[:required [:brand :model :hd-id :command :uuid]]]))

(mount.core/defstate conn-session
                     :start (client/producer {:bootstrap.servers (env :kafka)}
                                             (client/keyword-serializer)
                                             (client/edn-serializer))
                     :stop (client/close! conn-session))

(mount.core/defstate conn-tracking
                     :start (client/producer {:bootstrap.servers (env :kafka)}
                                             (client/keyword-serializer)
                                             (client/edn-serializer))
                     :stop (client/close! conn-tracking))

(defn any-nil-empty? [lst]
  (let [values (if (map? lst) (vals lst) lst)]
    (loop [v (first values) rst (rest values)]
      (if (or (= v nil) (= v ""))
        true
        (if (empty? rst)
          false
          (recur (first rst) (rest rst)))))))

(defn push-session [session]
  (if (s-validate session)
    (client/send! conn-session "session" :session session)
    {:error "Value can be null"}))

(defn push-warn [session]
  (if (any-nil-empty? session)
    {:error "Value cannot be null"}
    (client/send! conn-session "session-warn" :session session)))

(defn push-track [track]
  (if (any-nil-empty? track)
    {:error "value cannot be null"}
    (client/send! conn-tracking track)))
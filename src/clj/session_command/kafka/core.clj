(ns session-command.kafka.core
  (:require [kinsky.client :as client]
            [session-command.config :refer [env]]
            [clojure.core.async :as a]))

(defn any-nil-empty? [lst]
  (let [values (if (map? lst) (vals lst) lst)]
    (loop [v (first values) rst (rest values)]
      (if (or (= v nil) (= v ""))
        true
        (if (empty? rst)
          false
          (recur (first rst) (rest rst)))))))

(defn push-session [session]
  (if (any-nil-empty? session)
    {:error "Value can be null"}
    (let [p (client/producer {:bootstrap.servers (env :kafka)}
                             (client/keyword-serializer)
                             (client/edn-serializer))]
      (client/send! p "session" :session session)
      (client/close! p))))

(defn push-warn [session]
  (if (any-nil-empty? session)
    {:error "Value can be null"}
    (let [p (client/producer {:bootstrap.servers (env :kafka)}
                             (client/keyword-serializer)
                             (client/edn-serializer))]
      (client/send! p "session-warn" :session session)
      (client/close! p))))

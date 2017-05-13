(ns session-command.redis.core
  (:require [session-command.config :refer [env]]
            [mount.core :refer [defstate]]
            [clojure.data.json :as json]
            [taoensso.carmine :as car :refer (wcar)]))

(defmacro wcar* [& body] `(car/wcar {:pool {} :spec {:host (env :redis-url) :port 6379}} ~@body))

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
    (do
      (wcar* (car/lpush "session" (json/write-str session)))
      {:data ""})))

(defn push-warn [session]
  (if (any-nil-empty? session)
    {:error "Value can be null"}
    (do
      (wcar* (car/lpush "warn" (json/write-str session)))
      {:data ""})))
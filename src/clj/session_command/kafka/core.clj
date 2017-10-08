(ns session-command.kafka.core
  (:require [kinsky.client :as client]
            [session-command.config :refer [env]]
            [clojure.core.async :as a]
            [jkkramer.verily :as v]))

(def s-validate (v/validations->fn [[:required [:brand :model :hd-id :command :uuid]]]))
(def t-validate (v/validations->fn [[:required [:session-id :gas-lvl :lat :long :vel]]]))
(def w-validate (v/validations->fn [[:required [:session-id :action]]]))

(mount.core/defstate conn
                     :start (client/producer {:bootstrap.servers (env :kafka)}
                                             (client/keyword-serializer)
                                             (client/edn-serializer))
                     :stop (client/close! conn))

(defn push-session [session]
  (if (s-validate session)
    (client/send! conn "session" :session session)
    {:error "Value can be null"}))

(defn push-warn [warn]
  (if (w-validate warn)
    (client/send! conn "warn-warn" :session warn)
    {:error "Value cannot be null"}))

(defn push-track [track]
  (if (t-validate track)
    (client/send! conn "track" :track track)
    {:error "value cannot be null"}))
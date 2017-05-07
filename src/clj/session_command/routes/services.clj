(ns session-command.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [session-command.redis.core :as redis]))

(defapi service-routes
  {:swagger {:ui   "/swagger-ui"
             :spec "/swagger.json"
             :data {:info {:version     "0.0.1"
                           :title       "Session API"
                           :description "Supply function to command session domain"}}}}
  (context "/api" []
    :tags ["session"]

    (POST "/session" []
      :return {:session-id String}
      :body-params [{brand :- String nil}
                    {model :- String nil}
                    {hd-id :- String nil}]
      :summary "Create a new session"
      (let [uuid (str (java.util.UUID/randomUUID))
            rtn (redis/push-session {:command "create_session"
                                     :brand   brand
                                     :model   model
                                     :hd-id   hd-id
                                     :uuid    uuid})]
        (if (nil? (:error rtn))
          (ok {:session-id uuid})
          (bad-request "Invalid params"))))

    (POST "/session/warn" []
      :body-params [{action :- String nil}
                    {session-id :- String nil}]
      :summary "Broadcast a warning signal"
      (let [rtn (redis/push-warn {:command    "warn"
                                  :session-id session-id})]
        (if (nil? (:error rtn))
          (ok)
          (bad-request "Invalid params"))))))

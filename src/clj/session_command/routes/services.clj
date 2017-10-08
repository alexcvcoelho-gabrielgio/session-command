(ns session-command.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [session-command.db.core :as db]
            [session-command.kafka.core :as kafka]))

(defapi service-routes
  {:swagger {:ui   "/swagger"
             :spec "/swagger.json"
             :data {:info {:version     "0.0.1"
                           :title       "Session API"
                           :description "Supply function to command and query session and track domain"}}}}
  (context "/api" []
    :tags ["session"]

    (POST "/session" []
      :return {:session-id String}
      :body-params [{brand :- String nil}
                    {model :- String nil}
                    {hd-id :- String nil}]
      :summary "Creates a new session"
      (let [uuid (str (java.util.UUID/randomUUID))
            rtn (kafka/push-session {:command "create_session"
                                     :brand   brand
                                     :model   model
                                     :hd-id   hd-id
                                     :uuid    uuid})]
        (if (nil? (:error rtn))
          (ok {:session-id uuid})
          (bad-request "Invalid params"))))

    (POST "/track" []
      :body-params [{session-id :- String nil}
                    {lat :- Double 0}
                    {long :- Double 0}
                    {vel :- Double 0}
                    {gas-lvl :- Double 0}]
      :summary "Create a new session"
      (let [rtn (kafka/push-track {:command    "track"
                                   :lat        lat
                                   :long       long
                                   :vel        vel
                                   :gas-lvl    gas-lvl
                                   :session-id session-id})]
        (if (nil? (:error rtn))
          (ok)
          (bad-request "Invalid params"))))

    (POST "/" []
      :query-params [id :- String]
      :summary "Gets session info by id"
      (let [item (db/get-session id)]
        (ok (dissoc (first item) :_id))))

    (POST "/session/warn" []
      :body-params [{action :- String nil}
                    {session-id :- String nil}]
      :summary "Broadcast a warning signal"
      (let [rtn (kafka/push-warn {:command    "warn"
                                  :session-id session-id
                                  :action     action})]
        (if (nil? (:error rtn))
          (ok)
          (bad-request "Invalid params"))))

    (GET "/:session-id" []
      :path-params [{session-id :- String nil}]
      :summary "Get session information"
      (let [rtn (db/get-session session-id)]
        (if (nil? (:error rtn))
          (ok (dissoc (first rtn) :_id :command))
          (bad-request "Invalid params"))))))

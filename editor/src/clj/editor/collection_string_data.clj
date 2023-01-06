;; Copyright 2020-2022 The Defold Foundation
;; Copyright 2014-2020 King
;; Copyright 2009-2014 Ragnar Svensson, Christian Murray
;; Licensed under the Defold License version 1.0 (the "License"); you may not use
;; this file except in compliance with the License.
;;
;; You may obtain a copy of the License, together with FAQs at
;; https://www.defold.com/license
;;
;; Unless required by applicable law or agreed to in writing, software distributed
;; under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
;; CONDITIONS OF ANY KIND, either express or implied. See the License for the
;; specific language governing permissions and limitations under the License.

(ns editor.collection-string-data
  (:require [editor.protobuf :as protobuf])
  (:import [com.dynamo.gameobject.proto GameObject$PrototypeDesc]
           [java.io StringReader]))

(set! *warn-on-reflection* true)

(defn string-decode-embedded-component-desc
  "Takes a GameObject$EmbeddedComponentDesc in map format with string :data and
  returns a GameObject$EmbeddedComponentDesc in map format with the :data field
  converted to a map."
  [ext->embedded-component-resource-type string-encoded-embedded-component-desc]
  (let [component-ext (:type string-encoded-embedded-component-desc)
        component-resource-type (ext->embedded-component-resource-type component-ext)
        component-read-fn (:read-fn component-resource-type)
        string->component-data (fn [^String embedded-component-string]
                                 (with-open [reader (StringReader. embedded-component-string)]
                                   (component-read-fn reader)))]
    (update string-encoded-embedded-component-desc :data string->component-data)))

(defn string-decode-prototype-desc
  "Takes a GameObject$PrototypeDesc in map format with string :data in all its
  GameObject$EmbeddedComponentDescs and returns a GameObject$PrototypeDesc in
  map format with the :data fields in each GameObject$EmbeddedComponentDesc
  converted to a map."
  [ext->embedded-component-resource-type string-encoded-prototype-desc]
  (let [string-decode-embedded-component-desc (partial string-decode-embedded-component-desc ext->embedded-component-resource-type)
        string-decode-embedded-component-descs (partial mapv string-decode-embedded-component-desc)]
    (update string-encoded-prototype-desc :embedded-components string-decode-embedded-component-descs)))

(defn string-decode-embedded-instance-desc
  "Takes a GameObject$EmbeddedInstanceDesc in map format with string :data and
  returns a GameObject$EmbeddedInstanceDesc in map format with the :data field
  converted to a map."
  [ext->embedded-component-resource-type string-encoded-embedded-instance-desc]
  (let [game-object-read-fn (partial protobuf/str->map GameObject$PrototypeDesc)
        string-decode-prototype-desc (partial string-decode-prototype-desc ext->embedded-component-resource-type)
        string-decode-embedded-prototype-desc (comp string-decode-prototype-desc game-object-read-fn)]
    (update string-encoded-embedded-instance-desc :data string-decode-embedded-prototype-desc)))

(defn string-decode-collection-desc
  "Takes a GameObject$CollectionDesc in map format with string :data in all its
  GameObject$EmbeddedInstanceDescs and returns a GameObject$CollectionDesc in
  map format with the :data fields in each GameObject$EmbeddedInstanceDescs
  converted to a map."
  [ext->embedded-component-resource-type string-encoded-collection-desc]
  (let [string-decode-embedded-instance-desc (partial string-decode-embedded-instance-desc ext->embedded-component-resource-type)
        string-decode-embedded-instance-descs (partial mapv string-decode-embedded-instance-desc)]
    (update string-encoded-collection-desc :embedded-instances string-decode-embedded-instance-descs)))
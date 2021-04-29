// Copyright 2020 The Defold Foundation
// Licensed under the Defold License version 1.0 (the "License"); you may not use
// this file except in compliance with the License.
//
// You may obtain a copy of the License, together with FAQs at
// https://www.defold.com/license
//
// Unless required by applicable law or agreed to in writing, software distributed
// under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
// CONDITIONS OF ANY KIND, either express or implied. See the License for the
// specific language governing permissions and limitations under the License.

#ifndef DMSDK_RENDER_H
#define DMSDK_RENDER_H

#include <render/material_ddf.h>

/*# Render API documentation
 * [file:<dmsdk/render/render.h>]
 *
 * Api for render specific data
 *
 * @document
 * @name Render
 * @namespace dmRender
 */

namespace dmRender
{
    /*#
     * Material instance handle
     * @typedef
     * @name HMaterial
     */
    typedef struct Material* HMaterial;

    /*#
     * Get the vertex space (local or world)
     * @name dmRender::GetMaterialVertexSpace
     * @param material [type: dmRender::HMaterial] the material
     * @return vertex_space [type: dmRenderDDF::MaterialDesc::VertexSpace] the vertex space
     */
    dmRenderDDF::MaterialDesc::VertexSpace GetMaterialVertexSpace(HMaterial material);
}

#endif /* DMSDK_RENDER_H */
/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uwu.victoraso.storeapp.room.model

import androidx.room.*
import uwu.victoraso.storeapp.model.Wishlist

/**
 * External data layer representation of an [Wishlist]
 */
@Entity(
    tableName = "wishlistProducts",
)
data class PopulatedWishlist(
    @Embedded
    val entity: CartEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "wishlist_id"
    )
    val productItems: List<CartProductEntity>
)

fun PopulatedWishlist.asExternalModel() = Wishlist(
    id = entity.id,
    name = entity.name,
    itemCount = entity.itemCount,
    wishlistedItems = productItems.map(CartProductEntity::asExternalModel)
)
